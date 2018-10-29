package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="UserPassword", description = "用户登录实体")
public class UserPassword implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(required = true, value="用户ID", notes = "用户ID")
    @NotNull
    private Long userId;

    @ApiModelProperty(required = true, value="旧密码", notes = "旧密码")
    @NotEmpty
    private String oldPassword;

    @ApiModelProperty(required = true, value="新密码", notes = "新密码")
    @NotEmpty
    private String newPassword;
}
