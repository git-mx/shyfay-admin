package com.shyfay.admin.common.constant;

public enum StatusConstant {

    //删除状态
    IS_DELETE_NO("否", 0), IS_DELETE_YES("是", 1)
    //性别
    , USER_SEX_MAN("男", 1), USER_SEX_WOMAN("女", 2)
    ;


    private int index;
    private String name;

    // 构造方法
    private StatusConstant(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取状态名称
    public static String getName(int index) {
        for (StatusConstant statusConstant : StatusConstant.values()) {
            if (statusConstant.getIndex() == index) {
                return statusConstant.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
