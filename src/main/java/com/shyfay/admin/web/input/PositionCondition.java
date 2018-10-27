package com.shyfay.admin.web.input;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="职位查询实体", description = "DepartmentCondition")
public class PositionCondition extends BaseCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "职位名称", notes = "职位名称")
    private String positionName;
    @ApiModelProperty(value = "职位等级", notes = "职位等级")
    private String positionLevel;

}