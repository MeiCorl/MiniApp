//package com.meicorl.shopping_mall_miniapp.components;
//
//import com.meicorl.shopping_mall_miniapp.common.Token;
//import com.meicorl.shopping_mall_miniapp.utils.TokenUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import java.security.Principal;
//import java.util.Map;
//
///**
// * @author caomei
// * @date 2020/03/26
// * 重写WebSocket建立连接事握手逻辑，加入token验证，防止非法连接
// */
//@Slf4j
//@Component
//public class WebsocketHandshakeHandler extends DefaultHandshakeHandler {
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest httpRequest = servletServerHttpRequest.getServletRequest();
//            /*
//             * 熟悉的陌生人，就此止步吧!
//             */
//            Token token = new Token();
//            String strToken = httpRequest.getHeader("x-token");
//            if (StringUtils.isEmpty(strToken) || !TokenUtil.checkToken(strToken, token)) {
//                logger.warn("非法Socket连接!");
//                return null;
//            }
//            return new MyPrincipal() {
//                @Override
//                public String getName() {
//                    return null;
//                }
//
//                @Override
//                public Token getToken() {
//                    return token;
//                }
//            };
//        }
//        return null;
//    }
//}
