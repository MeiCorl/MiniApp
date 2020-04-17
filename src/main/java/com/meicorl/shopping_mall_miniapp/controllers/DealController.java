package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.annotations.RequireToken;
import com.meicorl.shopping_mall_miniapp.common.DealRequestModel;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.services.DealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/deal")
@Api(tags = "订单相关接口")
public class DealController {
    @Autowired
    DealService dealService;

    @ApiOperation("下单接口")
    @PostMapping(value = "/generate_deals")
    @RequireToken
    public Response generateDeals(DealRequestModel dealRequestModel) {
        List<DealRequestModel.DealProduct> productList = dealRequestModel.getProduct_list();
        return Response.ok();
    }
}
