package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value="用户列表查询实体", description = "UserCondition")
public class UserCondition  extends BaseCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="部门ID")
    private Long departmentId;

    @ApiModelProperty(value="职位ID")
    private Long positionId;

    @ApiModelProperty(value="用户姓名")
    private String userName;

    @ApiModelProperty(value="性别")
    private Integer userSex;

}
