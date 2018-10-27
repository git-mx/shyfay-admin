package com.shyfay.admin.common.base;

/**
 * 应用异常编码
 *
 * @author
 * @since
 */
public class ExceptionCode {

    public static final CodeMessage SUCCESS = new CodeMessage(0, "成功");
    public static final CodeMessage FAIL = new CodeMessage(1, "失败");
    public static final CodeMessage PARAM_ILLEGAL = new CodeMessage(2, "参数非法");
    public static final CodeMessage SERVER_ERROR = new CodeMessage(3, "内部服务器错误");
    public static final CodeMessage LOGIN_ERROR = new CodeMessage(4, "用户名或密码错误");
    public static final CodeMessage CODE_ERROR = new CodeMessage(5, "验证码错误");
    public static final CodeMessage CODE_EXPIRED = new CodeMessage(6, "验证码已过期");
    public static final CodeMessage OLD_PASSWORD_ERROE = new CodeMessage(7, "旧密码错误");
    public static final CodeMessage SESSION_EXPIRE = new CodeMessage(8, "用户登录已过期，请从新登录");
    public static final CodeMessage LOGIN_NAME_EXISTS = new CodeMessage(9, "用户名已存在，请重新输入");

}