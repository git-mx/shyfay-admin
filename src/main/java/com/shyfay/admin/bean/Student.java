package com.shyfay.admin.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by mx on 2018/8/18 0018.
 */
@Data
public class Student  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer studentId;
    private Integer groupNo;
    private String studentName;
    private String parentName;
    private String contactPhone;
}
