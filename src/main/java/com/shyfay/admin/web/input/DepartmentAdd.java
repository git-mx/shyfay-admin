package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="DepartmentAdd", description="部门添加实体")
public class DepartmentAdd implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="创建人ID", name="createId")
    @NotNull
    private Long createId;

    @ApiModelProperty(value="部门名称", name="departmentName")
    @NotEmpty
    private String departmentName;

    @ApiModelProperty(value="成立时间", name="establishTime")
    @NotNull
    private Long establishTime;

    @ApiModelProperty(value="备注", name="departmentRemark")
    @NotEmpty
    private String departmentRemark;
}
