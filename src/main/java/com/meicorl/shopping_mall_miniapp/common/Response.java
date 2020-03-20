package com.meicorl.shopping_mall_miniapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * description: 通用接口返回对象
 *
 * @author ：xushipeng
 * @date ：2019/8/24 23:28
 */
@Builder
@AllArgsConstructor
@Data
public class Response<T> implements Serializable {
    private int retCode;
    private String retMsg;
    private T content;
    private String requestId;

    public static Response ok() {
        return Response.builder()
                .retCode(ResultConstant.SUCCEED_CODE)
                .retMsg(ResultConstant.SUCCEED)
                .requestId(MDC.get("traceId"))
                .build();
    }
    public static <T> Response ok(T result) {
        return Response.builder()
                .retCode(ResultConstant.SUCCEED_CODE)
                .retMsg(ResultConstant.SUCCEED)
                .content(result)
                .requestId(MDC.get("traceId"))
                .build();
    }

    public static Response fail() {
        return Response.builder()
                .retCode(ResultConstant.FAILED_CODE)
                .retMsg(ResultConstant.FAILED)
                .requestId(MDC.get("traceId"))
                .build();
    }

    public static Response fail(int code) {
        return Response.builder()
                .retCode(code)
                .retMsg(ResultConstant.FAILED)
                .requestId(MDC.get("traceId"))
                .build();
    }

    public static Response fail(String msg) {
        return Response.builder()
                .retCode(ResultConstant.FAILED_CODE)
                .retMsg(msg)
                .requestId(MDC.get("traceId"))
                .build();
    }

    public static Response fail(int code, String msg) {
        return Response.builder()
                .retCode(code)
                .retMsg(msg)
                .requestId(MDC.get("traceId"))
                .build();
    }

    public static <T> Response fail(String msg, T result) {
        return Response.builder()
                .retCode(ResultConstant.FAILED_CODE)
                .retMsg(msg)
                .requestId(MDC.get("traceId"))
                .content(result).build();
    }

    public static <T> Response fail(int code, String msg, T result) {
        return Response.builder().
                retCode(code)
                .retMsg(msg)
                .requestId(MDC.get("traceId"))
                .content(result).build();
    }
}
