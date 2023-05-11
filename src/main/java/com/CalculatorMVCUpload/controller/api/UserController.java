package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.ManagerAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.ShopAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.exception.IncorrectPayloadException;
import com.CalculatorMVCUpload.payload.request.SingleIdRequest;
import com.CalculatorMVCUpload.payload.request.users.RegistrationRequest;
import com.CalculatorMVCUpload.payload.request.users.UserEditRequest;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.payload.response.UserListResponse;
import com.CalculatorMVCUpload.service.users.KeyManagerService;
import com.CalculatorMVCUpload.service.users.ShopUsersService;
import com.CalculatorMVCUpload.service.users.UserManagementService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private KeyManagerService keyManagerService;

    @Autowired
    private ShopUsersService shopUsersService;

    /*@DeleteMapping("/deleteUser")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteUser(@RequestHeader(name = "Authorization") String bearer,
                           @RequestBody SingleIdRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        int userIdToDelete = request.getSomeId();
        UserEntity userToDelete = userService.findById(userIdToDelete);

        if (userToDelete != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToDelete.getRoleEntity().getName())) {
            userService.deleteUser(userIdToDelete);
        } else throw new IncorrectPayloadException("Bad user change request");
    }*/

    @CrossOrigin
    @GetMapping("/getUser/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_SHOP", "ROLE_KEYMANAGER"})
    public UserInfoResponse getUser(@PathVariable int id,
                                    @RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        UserEntity userEntity = userService.findById(id);
        UserInfoResponse userInfoResponse = null;
        if (jwtProvider.getRoleFromAccessToken(token).contains("SHOP")) {
            if (shopUsersService.getShopViaUserId(id).getShop().getId() != jwtProvider.getIdFromAccessToken(token)) {
                return null;
            }
        }
        if (jwtProvider.getRoleFromAccessToken(token).contains("KEYMANAGER")) {
            if (keyManagerService.getManagerViaUserId(id).getKeyManager().getId() != jwtProvider.getIdFromAccessToken(token)) {
                return null;
            }
        }
        if (userEntity != null) {
            userInfoResponse = userManagementService.transferUserEntityToUserInfoResponse(userEntity);
        }
        return userInfoResponse;
    }

    @CrossOrigin
    @GetMapping("/getAllUsers")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<UserListResponse> getAllUsers() {
        List<UserEntity> allUsers = userManagementService.getAllUsers();
        List<UserListResponse> userListResponses = userManagementService.transferUserEntitiesToUserListResponse(allUsers);
        return userListResponses;
    }

    @CrossOrigin
    @PutMapping("/editUser")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void editUser(@RequestHeader(name = "Authorization") String bearer,
                         @RequestBody UserEditRequest request) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        int userIdToEdit = request.getUserId();
        UserEntity userToEdit = userService.findById(userIdToEdit);

        if (userToEdit != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToEdit.getRoleEntity().getName())) {
            userManagementService.editUserFields(userToEdit, request);

            userService.updateUser(userToEdit);

            if (request.getKeyManager() != null) {
                connectUserWithManager(request.getKeyManager(), userIdToEdit);
            }
            if (request.getShopId() != null) {
                connectUserWithShop(request.getShopId(), userIdToEdit);
            }
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @CrossOrigin
    @PutMapping("/register")
    @RolesAllowed({"ROLE_SHOP"})
    public String registerNewUser(@RequestHeader(name = "Authorization") String bearer,
                                  @RequestBody @Valid RegistrationRequest registrationRequest) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        if (userService.findByLogin(registrationRequest.getLogin()) != null
                || userService.findByEmail(registrationRequest.getEmail()) != null
                || userService.findByLogin(registrationRequest.getEmail()) != null
                || userService.findByEmail(registrationRequest.getLogin()) != null) {
            log.warning("Trying to register user with existing email or login");
            throw new ExistingLoginEmailRegisterException("This login or email is already registered");
        }
        UserEntity mainUser = userService.findById(jwtProvider.getIdFromAccessToken(token));
        UserEntity userEntity = userService.formNewUser(registrationRequest);
        userEntity.setCompanyName(mainUser.getCompanyName());
        shopUsersService.connectShopAndUser(mainUser, userEntity);
        userService.saveUser(userEntity, registrationRequest.getDesiredRole());
        return "OK";
    }

    public void connectUserWithManager(int userIdMain, int userIdNonMain) {
        UserEntity manager = userService.findById(userIdMain);
        UserEntity user = userService.findById(userIdNonMain);
        if (manager.getRoleEntity().getName().contains("KEYMANAGER")) {
            keyManagerService.connectUserAndManager(manager, user);
            return;
        }
        log.warning("Failed to connect user " + userIdMain + " as manager and user " + userIdNonMain + " as employee");
    }

    public void connectUserWithShop(int userIdMain, int userIdNonMain) {
        UserEntity shop = userService.findById(userIdMain);
        UserEntity user = userService.findById(userIdNonMain);
        if (shop.getRoleEntity().getName().contains("SHOP")) {
            shopUsersService.connectShopAndUser(shop, user);
            user.setCompanyName(shop.getCompanyName());
            userService.updateUser(user);
            return;
        }
        log.warning("Failed to connect user " + userIdMain + " as shop and user " + userIdNonMain + " as employee");
    }

    @CrossOrigin
    @GetMapping("/getAllUsersWithoutKeyManagers")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<UserListResponse> getAllUsersWithoutKeyManagers() {
        List<UserListResponse> resultList = new ArrayList<>();
        List<UserEntity> allUsers = userManagementService.getAllUsers();
        List<ManagerAndUsersEntity> allManagersAndUsers = keyManagerService.getAllManagersAndUsers();
        for (UserEntity userEntity : allUsers) {
            if (!userEntity.getRoleEntity().getName().contains("ADMIN")
                    && !userEntity.getRoleEntity().getName().contains("MODERATOR")
                    && !userEntity.getRoleEntity().getName().contains("KEYMANAGER")) {
                boolean flagKeyManagerAsserts = false;
                for (ManagerAndUsersEntity entity : allManagersAndUsers) {
                    if (entity.getUser().getId() == userEntity.getId()) {
                        flagKeyManagerAsserts = true;
                        break;
                    }
                }
                if (!flagKeyManagerAsserts) {
                    resultList.add(userManagementService.transferSingleUserEntityToUserResponse(userEntity));
                }
            }
        }
        return resultList;
    }

    @CrossOrigin
    @GetMapping("/getMyUsers")
    @RolesAllowed({"ROLE_KEYMANAGER"})
    public List<UserListResponse> getAllUsersThatHaveThisManager(@RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int idFromAccessToken = jwtProvider.getIdFromAccessToken(token);
        List<UserListResponse> resultList = new ArrayList<>();
        List<ManagerAndUsersEntity> managerViaUserId = keyManagerService.getManagerLinesViaManagerId(idFromAccessToken);
        for (ManagerAndUsersEntity entity : managerViaUserId) {
            resultList.add(userManagementService.transferSingleUserEntityToUserResponse(entity.getUser()));
        }
        return resultList;
    }

    @CrossOrigin
    @GetMapping("/getMyShopEmployees")
    @RolesAllowed({"ROLE_SHOP"})
    public List<UserListResponse> getAllUsersFromMyShop(@RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int idFromAccessToken = jwtProvider.getIdFromAccessToken(token);
        List<UserListResponse> resultList = new ArrayList<>();
        List<ShopAndUsersEntity> shopViaUserId = shopUsersService.getShopLinesViaShopId(idFromAccessToken);
        if (shopViaUserId.size() != 0) {
            for (ShopAndUsersEntity entity : shopViaUserId) {
                resultList.add(userManagementService.transferSingleUserEntityToUserResponse(entity.getUser()));
            }
        }
        return resultList;
    }

    @CrossOrigin
    @PutMapping("/editShopEmployee")
    @RolesAllowed({"ROLE_SHOP"})
    public String editShopEmployee(@RequestHeader(name = "Authorization") String bearer,
                                   @RequestBody UserEditRequest request) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int idFromAccessToken = jwtProvider.getIdFromAccessToken(token);
        int userIdToEdit = request.getUserId();
        UserEntity userToEdit = userService.findById(userIdToEdit);
        if (shopUsersService.getShopViaUserId(userIdToEdit).getShop().getId() == idFromAccessToken
                && idFromAccessToken != 0) {
            userManagementService.editUserFields(userToEdit, request);
            userService.updateUser(userToEdit);
            return "OK";
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @CrossOrigin
    @PostMapping("/blockShop")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void blockShop(@RequestBody SingleIdRequest request) {
        int idShopToBlock = request.getSomeId();
        List<ShopAndUsersEntity> shopLinesViaShopId = shopUsersService.getShopLinesViaShopId(idShopToBlock);
        List<UserEntity> shopEmployees = shopLinesViaShopId.stream()
                .map(ShopAndUsersEntity::getUser)
                .collect(Collectors.toList());
        shopEmployees.add(shopUsersService.getShopViaUserId(idShopToBlock).getShop());
        userService.batchUserBlocking(shopEmployees);
    }

}