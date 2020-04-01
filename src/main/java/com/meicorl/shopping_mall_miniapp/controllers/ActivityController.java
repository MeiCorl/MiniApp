package com.meicorl.shopping_mall_miniapp.controllers;

import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.Response;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Activity;
import com.meicorl.shopping_mall_miniapp.services.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/activity")
@Api("活动专区相关接口")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @ApiOperation("拉取当前未结束的限时特价活动列表")
    @GetMapping(value = "/activity_list")
    public Response getActivities() {
        List<Activity> activities = activityService.getActivities();
        return Response.ok("activity_list", activities);
    }

    @ApiOperation("拉取某个限时特价活动下的商品列表")
    @GetMapping(value = "/activity_products")
    public Response getActivityProducts(int activityId) {
        List<JSONObject> productList = activityService.getActivityProducts(activityId);
        return Response.ok("product_list", productList);
    }

}
