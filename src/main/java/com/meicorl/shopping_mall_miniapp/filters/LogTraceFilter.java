package com.meicorl.shopping_mall_miniapp.filters;
/**
 *  C端请求接入层, 用于为每个请求分配一个独立的请求id，便于跟踪请求
 */

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebFilter(filterName = "traceFilter", urlPatterns = {"/token/*", "/merchant/*", "/user/*", "/vip/*"})
public class LogTraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.put("traceId", UUID.randomUUID().toString().replaceAll("-", ""));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
