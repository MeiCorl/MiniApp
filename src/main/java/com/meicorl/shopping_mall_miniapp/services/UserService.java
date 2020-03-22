package com.meicorl.shopping_mall_miniapp.services;

import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import com.meicorl.shopping_mall_miniapp.mybatis.dao.UserDao;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserDao userDao;

    @Transactional
    public void saveUser(User user) {
        if(userDao.saveUser(user) < 1)
            throw new GlobalException("保存用户信息出错！");
        redisTemplate.opsForHash().put("miniapp_users", user.getOpenid(), user);
    }

    public User getUserByOpenId(String openId) {
        String userInfo = (String) redisTemplate.opsForHash().get("miniapp_users", openId);
        if(userInfo != null)
            return JSONObject.parseObject(userInfo, User.class);
        else
            return userDao.getUserByOpenId(openId);
    }
}
