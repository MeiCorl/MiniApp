package com.meicorl.shopping_mall_miniapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class WebsocketTestController {
    @RequestMapping(value = "/client")
    public String getSocketClient() {
        return "websocket_client";
    }
}
