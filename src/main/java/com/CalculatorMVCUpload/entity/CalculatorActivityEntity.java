package com.CalculatorMVCUpload.entity;

import com.CalculatorMVCUpload.entity.users.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users_receipt_summary")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CalculatorActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_time")
    private Instant activityTime;

    @Column(name = "user_id")
    public int userId;

    @Column(name = "login")
    public String login;

    @Column(name = "company_name")
    public String companyName;

    @Column(name = "certain_place_address")
    public String certainPlaceAddress;

    @Column(name = "type")
    private String type;

    @Column(name = "materials")
    private String materials;

    @Column(name = "material_price")
    private double materialPrice;

    @Column(name = "add_price")
    private double addPrice;

    @Column(name = "all_price")
    private double allPrice;

    @Column(name = "main_coeff")
    private double mainCoeff;

    @Column(name = "material_coeff")
    private double materialCoeff;

    @Column(name = "slabs")
    private double slabs;

    @Column(name = "product_square")
    private double productSquare;

    /*public List<String> getClassFieldsName(){
        Field[] fields = CalculatorActivityEntity.class.getFields();
        List<Field> fieldList = Arrays.asList(fields);

        fieldList.stream().map(item -> item.getName()).collect(Collectors.toList());
        return fieldList;
    }*/

}
