package com.meicorl.shopping_mall_miniapp.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private String message_type;
    private String from;
    private String to;
    private String data_type;
    private String data_content;
    private String timestamp;
}
