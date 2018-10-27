package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
@ApiModel(value="用户登录实体", description = "用户登录实体")
public class UserLogin implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(value="用户名", required=true, notes="用户名")
    @NotEmpty
    private String loginName;

    @ApiModelProperty(value="密码", required=true, notes="密码")
    @NotEmpty
    private String loginPassword;

    @ApiModelProperty(value="验证码", required=true, notes="验证码")
    @NotEmpty
    private String loginCode;

}
