package com.meicorl.shopping_mall_miniapp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 通用接口返回对象
 *
 * @author ：caomei
 * @date ：2019/8/24 23:28
 */
@Builder
@AllArgsConstructor
@Data
public class Response<T> implements Serializable {
    private int ret_code;
    private String ret_msg;
    private Map<String, T> ret_data;
    private String request_id;

    public static Response ok() {
        return Response.builder()
                .ret_code(ResultConstant.SUCCEED_CODE)
                .ret_msg(ResultConstant.SUCCEED)
                .request_id(MDC.get("traceId"))
                .build();
    }

    public static <T> Response ok(String key, T result) {
        return Response.builder()
                .ret_code(ResultConstant.SUCCEED_CODE)
                .ret_msg(ResultConstant.SUCCEED)
                .ret_data(new HashMap<String, Object>(){{ put(key, result); }})
                .request_id(MDC.get("traceId"))
                .build();
    }

    public static Response ok(Map<String, Object> map) {
        return Response.builder()
                .ret_code(ResultConstant.SUCCEED_CODE)
                .ret_msg(ResultConstant.SUCCEED)
                .ret_data(map)
                .request_id(MDC.get("traceId"))
                .build();
    }

    public static Response fail(String msg) {
        return Response.builder()
                .ret_code(ResultConstant.FAILED_CODE)
                .ret_msg(msg)
                .request_id(MDC.get("traceId"))
                .build();
    }
}
