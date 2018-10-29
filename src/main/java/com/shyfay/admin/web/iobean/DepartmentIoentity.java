package com.shyfay.admin.web.iobean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel(value="DepartmentIoentity", description="部门修改/列表/详情实体")
public class DepartmentIoentity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="部门ID", name="departmentId")
    @NotNull
    private Long departmentId;


    @ApiModelProperty(value="部门名称", name="departmentName")
    @NotEmpty
    private String departmentName;

    @ApiModelProperty(value="成立时间", name="establishTime")
    @NotNull
    private Long establishTime;

    @ApiModelProperty(value="备注", name="departmentRemark")
    private String departmentRemark;

}
