package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.annotations.CheckToken;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.common.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/merchant")
@Api(tags = "商户相关接口")
public class MerchantController {

    @ApiOperation("拉取商户列表")
    @RequestMapping(value = "/merchant_list")
    @CheckToken
    public Response getMerchantList(Token token, String building, int floor) {
        System.out.println("building: " + building + ", floor: " + floor);
        System.out.println("Token: " + token);
        return Response.ok();
    }
}
