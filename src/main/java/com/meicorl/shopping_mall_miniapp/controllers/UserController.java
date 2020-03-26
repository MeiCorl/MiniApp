package com.meicorl.shopping_mall_miniapp.controllers;

import com.meicorl.shopping_mall_miniapp.annotations.RequireToken;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Deal;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import com.meicorl.shopping_mall_miniapp.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("拉取用户信息")
    @GetMapping(value = "/me")
    @RequireToken
    public Response showMe() {
        User user = userService.getCurrentUser();
        return Response.ok("user_info", user);
    }

    @ApiOperation("为商户打分")
    @PutMapping(value = "/set_score")
    @RequireToken
    public Response setScore(int merchantId, float score) {
        userService.setScore(merchantId, score);
        return Response.ok();
    }

    @ApiOperation("新增评论")
    @PutMapping(value = "/add_evaluation")
    @RequireToken
    public Response addEvaluation(int merchantId, String conmment) {
        userService.addEvaluation(merchantId, conmment);
        return Response.ok();
    }

    @ApiOperation("删除评论")
    @PutMapping(value = "/delete_evaluation")
    @RequireToken
    public Response deleteEvaluation(long conmmentId) {
        userService.deleteEvaluation(conmmentId);
        return Response.ok();
    }

    @ApiOperation("添加收货地址")
    @PutMapping(value = "/add_address")
    public Response addAddress(String address) {
        userService.addAddress(address);
        return Response.ok();
    }

    @ApiOperation("查询我的订单")
    @GetMapping(value = "/my_deals")
    public Response getMyDeals() {
        ArrayList<Deal> deals = userService.getMyDeals();
        return Response.ok("my_deals", deals);
    }
}
