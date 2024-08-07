package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.users.RegistrationRequest;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.repository.UserEntityRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity saveUser(UserEntity userEntity, RegistrationRequest.DesiredRole desiredRole) {
        log.info("Saving new user to database " + userEntity.getLogin());
        RoleEntity userRole = roleEntityRepository.findByName("ROLE_" + desiredRole);
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userEntityRepository.save(userEntity);
    }

    @Transactional
    public UserEntity updateUserPassword(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userEntityRepository.save(userEntity);
    }

    @Transactional
    public UserEntity updateUser(UserEntity userEntity) {
        return userEntityRepository.save(userEntity);
    }

    @Transactional
    public List<UserEntity> getNewRegisteredUsersByTime(Instant startTime, Instant endTime) {
        return userEntityRepository.selectByRegistrationTime(startTime, endTime);
    }

    public void deleteUser(int id) {
        UserEntity userEntity = findById(id);
        userEntityRepository.delete(userEntity);
    }

    @Transactional
    public void batchUserBlocking(List<UserEntity> listOfBlockingUsers) {
        for (UserEntity userEntity : listOfBlockingUsers) {
            userEntity.setEnabled(false);
        }
        userEntityRepository.saveAllAndFlush(listOfBlockingUsers);
    }

    @Transactional
    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    @Transactional
    public UserEntity findByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }

    public UserEntity findById(int id) {
        Optional<UserEntity> optionalUserEntity = userEntityRepository.findById(id);
        UserEntity userEntity = null;
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
        }
        return userEntity;
    }

    @Transactional
    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    @Transactional
    public UserEntity findByIdAndPassword(int id, String password) {
        UserEntity userEntity = findById(id);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    @Transactional
    public UserEntity findByEmailAndPassword(String email, String password) {
        UserEntity userEntity = findByEmail(email);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public UserEntity formNewUser(RegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(request.getPassword());
        userEntity.setLogin(request.getLogin());
        userEntity.setEmail(request.getEmail());
        userEntity.setFullName(request.getFullName());
        userEntity.setCompanyName(request.getCompanyName());
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setAddress(request.getAddress());
        userEntity.setCertainPlaceAddress(request.getCertainPlaceAddress());
        userEntity.setAppAccess(request.getAppAccess());
        userEntity.setRegistrationTime(Instant.now());
        userEntity.setEnabled(true);
        return userEntity;
    }
}
