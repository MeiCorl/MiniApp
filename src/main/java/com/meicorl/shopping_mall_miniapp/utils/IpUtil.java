package com.meicorl.shopping_mall_miniapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端真实ip
 */
public class IpUtil {
    private final static Logger logger         = LoggerFactory.getLogger(IpUtil.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
                ipAddress = request.getRemoteAddr();
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            else if (ipAddress.indexOf(",") > 0)
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Fail to get clinet ip, using proxy ip instead!");
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}


