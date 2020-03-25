package com.meicorl.shopping_mall_miniapp.services;

import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import com.meicorl.shopping_mall_miniapp.mybatis.dao.UserDao;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Evaluation;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    private User getUserByOpenId(String openId) {
        User user = (User) redisTemplate.opsForHash().get("miniapp_users", openId);
        if(user != null)
            return user;
        else
            return userDao.getUserByOpenId(openId);
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    public User getCurrentUser() {
        String openId = SessionUtil.getCurrentUserId();
        return getUserByOpenId(openId);
    }

    /**
     * 为商户评分
     * @param merchantId 商户id
     * @param score 评分（0~5）
     */
    public void setScore(int merchantId, float score) {
        if(score < 0 || score > 5)
            throw new GlobalException("无效分数, 请选择分数：0~5分!");
        redisTemplate.execute(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForHash().increment("evaluation_stars", String.valueOf(merchantId), score);
                redisOperations.opsForHash().increment("evaluation_times", String.valueOf(merchantId), 1);
                return null;
            }
        });
    }

    /**
     * 为商户添加评论
     * @param merchantId 商户id
     * @param comment 评论内容
     */
    public void addEvaluation(int merchantId, String comment) {
        // 获取当前登录用户信息
        User user = getCurrentUser();

        Evaluation evaluation = new Evaluation();
        evaluation.setMerchant_id(merchantId);
        evaluation.setComment(comment);
        evaluation.setCreator_name(user.getNick_name());
        evaluation.setCreator_openid(user.getOpenid());
        evaluation.setCreate_time(new Date());
        if(userDao.addEvaluation(evaluation) < 1)
            throw new GlobalException("评论失败!");
    }

    /**
     * 删除评论
     * @param commentId 评论id
     */
    public void deleteEvaluation(long commentId) {
        // 获取当前登录用户信息
        User user = getCurrentUser();

        // 仅能删除自己的评论
        userDao.deleteEvaluation(commentId, user.getOpenid());
    }

    /**
     * 添加收货地址
     * @param address 地址
     */
    public void addAddress(String address) {
        User user = getCurrentUser();
        if(StringUtils.isEmpty(address))
            throw new GlobalException("收货地址不能为空!");
        if(userDao.addAddress(address, user.getId()) < 1)
            throw new GlobalException("新增地址出错!");
    }
}
