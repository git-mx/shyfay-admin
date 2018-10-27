package com.shyfay.admin.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long departmentId;
    private String departmentName;
    private Long establishTime;
    private Long createTime;
    private Long updateTime;
    private String departmentRemark;
    private Integer deleteStatus;
    private Long createId;
    private Long upadteId;
}
