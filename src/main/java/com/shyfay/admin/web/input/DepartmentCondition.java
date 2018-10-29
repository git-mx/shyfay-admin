package com.shyfay.admin.web.input;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="DepartmentCondition", description = "部门查询实体")
public class DepartmentCondition extends BaseCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="部门名称", notes = "departmentName")
    private String departmentName;
}
