package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.annotations.RequireToken;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.services.VipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vip")
@Api(tags = "会员相关接口")
public class VipController {
    @Autowired
    VipService vipService;

    @ApiOperation("积分兑换")
    @GetMapping(value = "/bonus_exchange")
    @RequireToken
    public Response bonusExchange() {
        return Response.ok();
    }

    @ApiOperation("会员充值")
    @GetMapping(value = "/recharge")
    @RequireToken
    public Response recharge() {
        return Response.ok();
    }
}
