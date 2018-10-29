package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="StudentUpdate", description="学生信息修改/列表/详情实体")
public class StudentUpdate implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "学生信息ID", name = "studentId")
    @NotNull
    private Long studentId;

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