package com.yourname.student.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;    // 响应码：200成功，4xx客户端错误，5xx服务端错误
    private String message;  // 响应信息
    private T data;          // 响应数据

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败响应（自定义错误码和信息）
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
