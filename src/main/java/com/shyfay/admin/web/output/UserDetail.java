package com.shyfay.admin.web.output;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionName;
    private String idNumber;
    private String userName;
    private Integer userSex;
    private Long birthdayTime;
    private String houseAddress;
    private String phoneNumber;
    private String imageUrl;
    private String loginName;
    private String loginPassword;
}
