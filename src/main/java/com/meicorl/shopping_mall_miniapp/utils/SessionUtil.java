package com.meicorl.shopping_mall_miniapp.utils;

import com.meicorl.shopping_mall_miniapp.common.Token;

public class SessionUtil {
    // 定义一个线程域，存放登录用户
    private static ThreadLocal<Token> threadLocal = new ThreadLocal<>();

    public static Token getCurrentToken() {
        return threadLocal.get();
    }

    public static void setCurrentToken(Token token) {
        threadLocal.set(token);
    }

    public static void removeCurrentToken() {
        threadLocal.remove();
    }

    public static String getCurrentUserId() {
        Token token = threadLocal.get();
        if (token != null)
            return token.getOpenid();
        else
            return null;
    }
}
