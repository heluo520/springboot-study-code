package com.ecjtu.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
@AllArgsConstructor
@Builder
@Data
public class Result {
    private int code;
    private String message;
    private Object data;
    public static Result success(String msg,Object data){
        return new Result(200,msg,data);
    }
    public static Result fail(int code,String msg,Object data){
        return new Result(code,msg,data);
    }
}
