package com.shyfay.admin.web.input;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="UserAdd", description="用户添加实体")
public class UserAdd implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="部门主键",name = "departmentId")
    @NotNull
    private Long departmentId;
    @ApiModelProperty(value="职位主键",name = "positionId")
    @NotNull
    private Long positionId;
    @ApiModelProperty(value="身份证号",name = "idNumber")
    @NotEmpty
    private String idNumber;
    @ApiModelProperty(value="姓名",name = "userName")
    @NotEmpty
    private String userName;
    @ApiModelProperty(value="性别1--男2--女",name = "userSex")
    @NotNull
    private Integer userSex;
    @ApiModelProperty(value="出生日期",name = "birthdayTime")
    @NotNull
    private Long birthdayTime;
    @ApiModelProperty(value="地址",name = "houseAddress")
    @NotEmpty
    private String houseAddress;
    @ApiModelProperty(value="联系电话",name = "phoneNumber")
    @NotEmpty
    private String phoneNumber;
    @ApiModelProperty(value="头像图片地址",name = "phoneNumber")
    private String imageUrl;
    @ApiModelProperty(value="用户名",name = "loginName")
    @NotEmpty
    private String loginName;
    @ApiModelProperty(value="密码",name = "loginPassword")
    @NotEmpty
    private String loginPassword;
    @ApiModelProperty(value="创建人ID",name = "createId")
    @NotNull
    private Long createId;

}
