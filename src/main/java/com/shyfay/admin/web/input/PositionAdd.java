package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ApiModel(value="PositionAdd", description="职位添加实体")
public class PositionAdd implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="创建人ID", name="createId")
    @NotNull
    private Long createId;

    @ApiModelProperty(value="职位名称", name="positionName")
    @NotEmpty
    private String positionName;
    @ApiModelProperty(value="职级", name="positionLevel")
    @NotEmpty
    private String positionLevel;
    @ApiModelProperty(value="备注", name="positionRemark")
    @NotEmpty
    private String positionRemark;
}
