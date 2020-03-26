package com.meicorl.shopping_mall_miniapp.interceptors;

import com.meicorl.shopping_mall_miniapp.annotations.RequireToken;
import com.meicorl.shopping_mall_miniapp.common.Token;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import com.meicorl.shopping_mall_miniapp.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 鉴权拦截器,对于某些接口，需要用户登录才能放开访问权限
 * RequireToken注解表示明确需要用户登录,没有token不放行，不带RequireToken则对token无要求，此时有token仍会解析
 * @author caomei
 * @date 2020/03/26
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        logger.info("开始token校验...");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod)object;
        Method method = handlerMethod.getMethod();
        // 执行认证
        boolean needToken = method.isAnnotationPresent(RequireToken.class);
        String strToken = request.getHeader("x-token");
        Token token = new Token();
        if (StringUtils.isEmpty(strToken) || !TokenUtil.checkToken(strToken, token)) {
            if(needToken) {
                response.sendError(403, "用户未登录!");
                return false;
            }
            else {
                logger.info("跳过token验证!");
                return true;
            }
        }
        // 将当前登录用户token存放在全局ThreadLocal对象中
        SessionUtil.setCurrentToken(token);
        logger.info("token验证成功!");
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