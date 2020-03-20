package com.meicorl.shopping_mall_miniapp.aspects;


import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import com.meicorl.shopping_mall_miniapp.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * description: 全局异常处理
 *
 * @author ：caomei
 * @date ：2019/8/24 23:28
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAspect {

    /**
     * description: 捕获全局异常
     * created by caomei 2019/8/24 23:28
     *
     * @return Response
     * @Param: ex
     */
    @ExceptionHandler(Exception.class)
    public Response globalException(Throwable ex) {
        if (ex instanceof GlobalException) {
            log.info("访问出错: " + ex.getMessage());
            return Response.fail(ex.getMessage());
        }
        log.error("访问出错: ", ex);
        return Response.fail("服务器内部错误, 请联系开发人员查看!");
    }
}
