package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@ApiModel(value="StudentAdd", description="学生信息实体")
public class StudentAdd implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="学生姓名", name="studentName")
    @NotEmpty
    private String studentName;

    @ApiModelProperty(value="家长姓名", name="parentName")
    @NotEmpty
    private String parentName;

    @ApiModelProperty(value="联系电话", name="contactPhone")
    @NotEmpty
    private String contactPhone;
}
