package com.meicorl.shopping_mall_miniapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MessageTraceUtil {
    private static final Logger logger = LoggerFactory.getLogger(MessageTraceUtil.class);

    public static void info(String msg) {
        logger.info(msg);
    }

    private static String getStackTrace(Throwable t) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
