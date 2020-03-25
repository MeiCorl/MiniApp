package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Evaluation;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Merchant;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Product;
import com.meicorl.shopping_mall_miniapp.services.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/merchant")
@Api(tags = "商户相关接口")
public class MerchantController {
    @Autowired
    MerchantService merchantService;

    @ApiOperation("拉取商户列表")
    @GetMapping(value = "/merchant_list")
    public Response getMerchantList(String building, Integer floor) {
        // 查询商户列表
        ArrayList<Merchant> merchants = merchantService.getMerchantsList(building, floor);

        // 查询商户评价得分
        merchantService.getMerchantScores(merchants);
        return Response.ok("merchant_list", merchants);
    }

    @ApiOperation("拉取商户评价列表")
    @GetMapping(value = "/evaluation_list")
    public Response getEvaluationList(int merchantId, int pageNo) {
        if(pageNo <= 0)
            return Response.fail("无效参数!");
        ArrayList<Evaluation> evaluations = merchantService.getMerchantEvaluations(merchantId, pageNo);
        return Response.ok("evaluation_list", evaluations);
    }

    @ApiOperation("拉取商品列表")
    @GetMapping(value = "/product_list")
    public Response getProductList(int merchantId) {
        ArrayList<Product> products = merchantService.getProductList(merchantId);
        return Response.ok("product_list", products);
    }
}
