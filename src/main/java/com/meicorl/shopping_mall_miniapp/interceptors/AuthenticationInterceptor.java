package com.meicorl.shopping_mall_miniapp.interceptors;

import com.meicorl.shopping_mall_miniapp.annotations.PassToken;
import com.meicorl.shopping_mall_miniapp.common.Token;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import com.meicorl.shopping_mall_miniapp.utils.TokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String strToken = request.getHeader("x-token"); // 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            return true;
        }

        // 执行认证
        Token token = new Token();
        if(!TokenUtil.checkToken(strToken, token)) {
            response.sendError(403, "token验证失败!");
            return false;
        }
        //todo 将当前登录用户token存放在全局ThreadLocal对象中
        SessionUtil.setCurrentToken(token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        SessionUtil.removeCurrentToken();
    }
}