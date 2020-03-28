package com.meicorl.shopping_mall_miniapp.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private String message_type;   // 消息类型， message: 普通消息   deal: 订单消息
    private String from;
    private String to;
    private MessageBody body;
    private String timestamp;

    static class MessageBody {
        String type;      // 消息数据类型, text: 普通文本数据  jpg: jpg图片数据  png: png图片数据
        String content;   // 消息内容， 如果type为图片类型， 则传图片Base64字符串

        @Override
        public String toString() {
            return "{type: " +
                    type +
                    ", content: " +
                    content +
                    "}";
        }
    }
}
