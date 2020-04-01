package com.meicorl.shopping_mall_miniapp.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ActivityService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 拉取特价活动列表
     * @return
     */

    @Cacheable(value = "ActivityCache", key = "'on_sale_activities'", unless = "#result.isEmpty()")
    public List<Activity> getActivities() {
        Set<String> activityKeys = stringRedisTemplate.keys("activity_*");
        log.info("当前有效特价活动: " + activityKeys);
        List<String> activityInfos = stringRedisTemplate.opsForValue().multiGet(activityKeys);
        List<Activity> activities = new ArrayList<>();
        if(activityInfos != null) {
            for(String activityInfo : activityInfos)
                activities.add(JSON.parseObject(activityInfo, Activity.class));
        }
        return activities;
    }

    /**
     * 拉取特价活动下商品列表
     * @param activityId 活动id
     * @return
     */
    @Cacheable(value = "ActivityCache", key = "'products_of activity_' + #activityId", unless = "#result.isEmpty()")
    public List<JSONObject> getActivityProducts(int activityId) {
        // 获取活动商品折扣表
        Map<String, Float> discountTable = stringRedisTemplate.<String, Float>opsForHash().entries(String.format("discount_of_activity_%d", activityId));
        Set<String> productIds = discountTable.keySet();

        // 查询商品基本信息
        List<String> productInfos = stringRedisTemplate.<String, String>opsForHash().multiGet("products", productIds);
        List<JSONObject> productList = new ArrayList<>(productInfos.size());
        for(String productInfo : productInfos) {
            JSONObject product = JSONObject.parseObject(productInfo);
            String productId= product.getString("id");
            product.put("discount_price", discountTable.get(productId) * product.getFloatValue("price"));  // 计算商品折后价格
            productList.add(product);
        }
        return productList;
    }
}
