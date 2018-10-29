package com.shyfay.admin.web.iobean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="PositionIoentity", description="职位修改/列表/详情实体")
public class PositionIoentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "职位ID", name = "positionId")
    @NotNull
    private Long positionId;
    @ApiModelProperty(value = "职位名称", name = "positionName")
    @NotEmpty
    private String positionName;
    @ApiModelProperty(value="职级", name="positionLevel")
    @NotEmpty
    private String positionLevel;
    @ApiModelProperty(value = "备注", name = "positionRemark")
    @NotEmpty
    private String positionRemark;

}