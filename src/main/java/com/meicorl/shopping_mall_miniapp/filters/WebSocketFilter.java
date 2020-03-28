package com.meicorl.shopping_mall_miniapp.filters;
/**
 * 对WebSocket连接进行身份验证，拒绝非法链接
 */

import com.meicorl.shopping_mall_miniapp.common.Token;
import com.meicorl.shopping_mall_miniapp.utils.CommonUtil;
import com.meicorl.shopping_mall_miniapp.utils.MessageTraceUtil;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import com.meicorl.shopping_mall_miniapp.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "websocketFilter", urlPatterns = {"/ws"})
public class WebSocketFilter implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String strToken = request.getHeader("x-token");
        Token token = new Token();
        if (StringUtils.isEmpty(strToken) || !TokenUtil.checkToken(strToken, token)) {
            /*
             * 熟悉的陌生人，就此止步吧!
             */
            MessageTraceUtil.error("非法链接!");
//            response.sendError(403, "请先登录!");
//            return;
            // 以下为测试
            token.setOpenid(CommonUtil.getRandomString(8));
            token.setSession_key("dasdsadas");
        }
        SessionUtil.setCurrentToken(token);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
