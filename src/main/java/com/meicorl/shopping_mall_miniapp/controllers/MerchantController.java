package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.common.Token;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/merchant")
@Api(tags = "商户相关接口")
public class MerchantController {

    @ApiOperation("拉取商户列表")
    @GetMapping(value = "/merchant_list")
    public Response getMerchantList(String building, Integer floor) {
        System.out.println("building: " + building + ", floor: " + floor);
        Token token = SessionUtil.getCurrentToken();
        System.out.println("Token: " + token);
        return Response.ok();
    }
}
