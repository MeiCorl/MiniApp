package com.meicorl.shopping_mall_miniapp.services;
/**
 * WebSocket Server： 接收小程序客户端消息
 * 这里采用传统的java websocket，未使用SpringBoot提供的websocket
 * @author caomei
 * @data 2020/03/26
 */

import com.meicorl.shopping_mall_miniapp.annotations.RequireToken;
import com.meicorl.shopping_mall_miniapp.utils.MessageTraceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class MessageService {
    @Value("${merchants_listener_topic}")
    String merchant_topic;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    // 记录当前在线用户数量
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // 线程安全set， 存放当前每个连如客户端的WebSocketService对象
    private static CopyOnWriteArraySet<MessageService> clients = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话
    private Session session;

    @OnOpen
    @RequireToken
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
        int numOfOnlineUser = onlineCount.getAndIncrement();
        MessageTraceUtil.info("一个新用户上线, 当前在线用户数量: " + (numOfOnlineUser + 1));
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        int numOfOnlineUser = onlineCount.getAndDecrement();
        MessageTraceUtil.info("一个新用户下线, 当前在线用户数量: " + (numOfOnlineUser - 1));
    }

    @OnMessage
    public void onMessage(String msg, Session session) {

    }

    /**
     * 处理Redis订阅消息
     * @param message
     */
    public void onMessage(String message) {
        MessageTraceUtil.info("收到一个新消息： " + message);
        stringRedisTemplate.convertAndSend(merchant_topic, message);
    }
}
