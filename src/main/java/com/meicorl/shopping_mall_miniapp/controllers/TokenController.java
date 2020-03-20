package com.meicorl.shopping_mall_miniapp.controllers;

import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.common.TokenRequestModel;
import com.meicorl.shopping_mall_miniapp.services.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/token")
@Api(tags = "登录相关接口")
public class TokenController {
    @Autowired
    WechatService wechatService;

    @ApiOperation(value = "获取用户登录token")
    @PostMapping(value = "/getToken")
    public Response getToken(@RequestBody TokenRequestModel request) {
        JSONObject token = wechatService.getWxUserInfo(request.getCode(), request.getNickName(),  request.getHeadImg(),
                request.getSex(), request.getPhoneNumber());
        return Response.ok(token);
    }
}
