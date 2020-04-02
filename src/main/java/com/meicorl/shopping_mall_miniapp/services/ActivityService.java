package com.meicorl.shopping_mall_miniapp.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Activity;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<Activity> activities = new ArrayList<>();
        stringRedisTemplate.execute(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations ro) throws DataAccessException {
                Set<String> activityKeys = ro.keys("activity_*");
                log.info("当前有效特价活动: " + activityKeys);
                List<String> activityInfos = ro.opsForValue().multiGet(activityKeys);
                if(activityInfos != null) {
                    Date now = new Date();
                    for(String activityInfo : activityInfos) {
                        Activity activity = JSON.parseObject(activityInfo, Activity.class);
                        if(now.before(activity.getBegin_time()))
                            activity.setStatus(0);
                        else
                            activity.setStatus(1);
                        activities.add(activity);
                    }
                }
                return null;
            }
        });
        return activities;
    }

    /**
     * 拉取特价活动下商品列表(这里不对接口响应做进一步缓存，防止活动过期还能拉取到活动商品)
     * @param activityId 活动id
     * @return
     */
    public List<Product> getActivityProducts(int activityId) {
        List<Product> productList = new ArrayList<>();

        stringRedisTemplate.execute(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations ro) throws DataAccessException {
                // 查询活动基本信息
                String activityInfo = (String)ro.opsForValue().get(String.format("activity_%d", activityId));
                if(activityInfo == null)
                    throw new GlobalException("活动不存在或已结束!");
                Activity activity = JSONObject.parseObject(activityInfo, Activity.class);
                Date now = new Date();
                if(now.before(activity.getBegin_time()))
                    throw new GlobalException("活动尚未开始,请耐心等待!");

                // 获取活动商品折扣表
                Map<String, String> discountTable = ro.opsForHash().entries(String.format("discount_of_activity_%d", activityId));
                discountTable.remove("");
                Set<String> productIds = discountTable.keySet();

                // 查询商品基本信息
                List<String> productInfos = ro.opsForHash().multiGet("products", productIds);

                for(String productInfo : productInfos) {
                    if(productInfo != null) {
                        Product product = JSONObject.parseObject(productInfo, Product.class);
                        long productId = product.getId();
                        float price = product.getPrice();
                        product.setDiscount_price((Float.parseFloat(discountTable.get(String.valueOf(productId))) / 10) * price);
                        productList.add(product);
                    }
                }
                return null;
            }
        });
        return productList;
    }
}
