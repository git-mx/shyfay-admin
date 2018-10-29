package com.shyfay.admin.web.input;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="StudentCondition", description = "学生查询实体")
public class StudentCondition extends BaseCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "学生姓名", notes = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "订单号", notes = "订单号")
    private String orderNumber;

}