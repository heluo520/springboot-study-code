package com.ecjtu.constant;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
public enum UserEnum {
    USER_NOT_FOUND(200,"用户不存在"),
    PASSWORD_ERROR(200,"密码错误");
    private final int code;
    private final String msg;
    UserEnum(int code,String msg){
        this.code = code;
        this.msg =msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
