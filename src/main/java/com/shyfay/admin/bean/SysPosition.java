package com.shyfay.admin.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long positionId;
    private String positionName;
    private String positionLevel;
    private Long createTime;
    private Long updateTime;
    private String positionRemark;
    private Integer deleteStatus;
    private Long createId;
    private Long upadteId;
}
