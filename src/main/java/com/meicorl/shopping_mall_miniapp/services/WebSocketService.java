package com.meicorl.shopping_mall_miniapp.services;
/**
 * WebSocket Server： 这里采用传统的java websocket，未使用SpringBoot提供的websocket
 * @author caomei
 * @data 2020/03/26
 */

import com.meicorl.shopping_mall_miniapp.utils.MessageTraceUtil;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@ServerEndpoint(value = "/ws")
public class WebSocketService {
    // 记录当前在线用户数量
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // 线程安全set， 存放当前每个连如客户端的WebSocketService对象
    private static CopyOnWriteArraySet<WebSocketService> clients = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
        int numOfOnlineUser = onlineCount.getAndIncrement();
        MessageTraceUtil.info("一个新用户上线了, 当前在线用户数量: " + (numOfOnlineUser + 1));
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        int numOfOnlineUser = onlineCount.getAndDecrement();
        MessageTraceUtil.info("一个新用户下线了, 当前在线用户数量: " + (numOfOnlineUser - 1));
    }

    @OnMessage
    public void onMessage(String msg, Session session) {

    }
}
