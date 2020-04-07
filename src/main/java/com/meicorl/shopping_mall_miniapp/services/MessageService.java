package com.meicorl.shopping_mall_miniapp.services;
/**
 * Message Server: 接收小程序客户端消息, 及商户后端消息
 * 这里采用传统的java websocket，未使用SpringBoot提供的websocket
 * @author caomei
 * @data 2020/03/26
 */
import com.alibaba.fastjson.JSON;
import com.meicorl.shopping_mall_miniapp.common.Message;
import com.meicorl.shopping_mall_miniapp.components.SpringUtil;
import com.meicorl.shopping_mall_miniapp.utils.MessageTraceUtil;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.websocket.*;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@ServerEndpoint(value = "/ws")
public class MessageService {
    @Value("${merchants_listener_topic}")
    String merchant_topic;

    @Value("${merchants_message_queue}")
    String merchants_message_queue;

    @Value("${miniapp_message_queue}")
    String miniapp_message_queue;

    /**
     * 敲黑板: 有坑!!! @ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     * 参考解释: websocket是多对象的，每个用户的聊天客户端对应 java 后台的一个 websocket 对象，前后台一对一（多对多）实时连接，
     * 所以 websocket 不可能像 servlet 一样做成单例的，让所有聊天用户连接到一个 websocket对象，这样无法保存所有用户的实时连接信息。
     * 可能 spring 开发者考虑到这个问题，没有让 spring 创建管理 websocket ，而是由 java 原来的机制管理websocket ，
     * 所以用户聊天时创建的 websocket 连接对象不是 spring 创建的，spring 也不会为不是他创建的对象进行依赖注入
     */
    private static StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

    /** 记录当前在线用户数量 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /** 记录当前每个连如客户端的sessionId与WebSocketService对象的对应关系 */
    private static ConcurrentHashMap<String, MessageService> clients = new ConcurrentHashMap<>(20);

    /** 记录当前登录用户userId（openId）与sessionId的对应关系（用于处理redis消息时根据用户id找到用户对应的sessionId） */
    private static ConcurrentHashMap<String, String> userSessionMap = new ConcurrentHashMap<>(20);

    /** 记录当前登录用户sessionId与userId（openId）的对应关系（用户连接断开时，根据连接sessionId找到对应的用户) */
    private static ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>(20);

    /** 与某个客户端的连接会话 */
    private Session session;

    /**
     * 处理小程序用户webSocket连接请求
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        // 获取当前连接用户userId和sessionId
        String userId= SessionUtil.getCurrentUserId();
        String sessionId = session.getId();
        assert userId != null;

        this.session = session;
        clients.put(sessionId, this);
        userSessionMap.put(userId, sessionId);
        sessionUserMap.put(sessionId, userId);
        int numOfOnlineUser = onlineCount.getAndIncrement();
        MessageTraceUtil.info(String.format("用户%s已上线, sessionId=%s, 当前在线用户数量: %d", userId, sessionId, numOfOnlineUser + 1));

        // 检查是否有发往该用户的离线消息
        String offlineMessageKey = String.format("messages_for_%s", userId);
        List<String> messages = stringRedisTemplate.opsForList().range(offlineMessageKey, 0, -1);
        if(messages != null && messages.size() > 0) {
            RemoteEndpoint.Basic remote = session.getBasicRemote();
            for(String message : messages) {
                try {
                    remote.sendText(message);
                } catch (IOException e) {
                    MessageTraceUtil.error(String.format("离线消息发送失败: e.getMessage(), message: %s", message));
                }
            }
            // 删除离线消息队列中已经发送的消息
            stringRedisTemplate.opsForList().trim(offlineMessageKey, messages.size(), -1);
            MessageTraceUtil.info("离线消息推送成功!");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String sessionId =session.getId();
        String userId= sessionUserMap.get(sessionId);

        clients.remove(sessionId);
        sessionUserMap.remove(sessionId);
        userSessionMap.remove(userId);

        int numOfOnlineUser = onlineCount.getAndDecrement();
        MessageTraceUtil.info(String.format("用户%s已下线, sessionId=%s, 当前在线用户数量: %d", userId, sessionId, numOfOnlineUser - 1));
        session.close();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    	MessageTraceUtil.info("Throwable msg: " + throwable.getMessage());
    	
        String sessionId =session.getId();
        String userId= sessionUserMap.get(sessionId);

        clients.remove(sessionId);
        sessionUserMap.remove(sessionId);
        userSessionMap.remove(userId);

        try {
            session.close();
        } catch (IOException e) {
            MessageTraceUtil.error("onError excepiton: " + e);
        }
    }

    /**
     * 处理小程序发来的消息，发往商户管理后端redis订阅主题
     * @param msg 消息内容
     * @param session 当前会话session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        Message message = JSON.parseObject(msg, Message.class);
        // 检查下消息中发送方和当前连接用户是否一致
        String userId = sessionUserMap.get(session.getId());
        if(!userId.equals(message.getFrom_id())) {
            MessageTraceUtil.error(String.format("消息发送方与当前连接用户不一致, user_id: %s  from_id: %s", userId, message.getFrom_id()));
            return;
        }
        stringRedisTemplate.execute(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                // 消息写入商户后端消费队列
                redisOperations.opsForList().rightPush(merchants_message_queue, msg);
                // 通知商户后端有新消息到达
                redisOperations.convertAndSend(merchant_topic, "message");
                return null;
            }
        });
        MessageTraceUtil.info(String.format("收到一条来自%s的消息, 已发往商户: %s, 消息内容如下: %s", message.getFrom_id(), message.getTo_id(), message.getBody()));
    }

    /**
     * 处理Redis订阅消息, 发现小程序用户
     * @param redisMsg
     */
    public void receiveMessage(String redisMsg) {
        if(!redisMsg.equals("message"))
            return;
        // 从消费队列获取消息
        String msg = stringRedisTemplate.opsForList().leftPop(miniapp_message_queue);
        if(StringUtils.isEmpty(msg))
            return;
        Message message = JSON.parseObject(msg, Message.class);
        MessageTraceUtil.info(String.format("收到一条来自商户%s的消息, 即将发往用户: %s, 消息内容如下: %s", message.getFrom_id(), message.getTo_id(), message.getBody()));

        // 检查小程序用户是否在线
        String userId = message.getTo_id();
        if(userSessionMap.containsKey(userId)) {
            MessageService client = clients.get(userSessionMap.get(userId));
            client.sendMessage(msg);
        }
        else {
            // 将消息发往用户离线消息队列
            String offlineMessageKey = String.format("messages_for_%s", userId);
            stringRedisTemplate.opsForList().rightPush(offlineMessageKey, msg);
        }
    }

    /**
     * 发送消息到小程序客户端
     * @param msg
     */
    private void sendMessage(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            MessageTraceUtil.error(String.format("消息发送失败: e.getMessage(), message: %s", msg));
        }
    }
}
