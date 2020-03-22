package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Merchant;
import com.meicorl.shopping_mall_miniapp.services.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping(value = "/merchant")
@Api(tags = "商户相关接口")
public class MerchantController {
    @Autowired
    MerchantService merchantService;

    @ApiOperation("拉取商户列表")
    @GetMapping(value = "/merchant_list")
    public Response getMerchantList(String building, Integer floor) {
        ArrayList<Merchant> merchants = merchantService.getMerchantsList(building, floor);
        return Response.ok(merchants);
    }
}
