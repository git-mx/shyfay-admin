package com.shyfay.admin.web.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value="列表查询实体", description = "BaseCondition")
public class BaseCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="当前页", required = true)
    @NotNull
    @Min(value=1, message="当前页不能小于1")
    private Integer pageIndex;

    @ApiModelProperty(value="每页大小", required = true)
    @NotNull
    @Min(value=10, message="每页大小不能小于10")
    private Integer pageSize;
}
