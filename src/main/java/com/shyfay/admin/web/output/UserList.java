package com.shyfay.admin.web.output;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserList implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String departmentName;
    private String positionName;
    private String idNumber;
    private String userName;
    private Integer userSex;
    private Long birthdayTime;
    private String houseAddress;
    private String phoneNumber;
}
