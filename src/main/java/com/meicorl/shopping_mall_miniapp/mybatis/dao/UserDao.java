package com.meicorl.shopping_mall_miniapp.mybatis.dao;


import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Deal;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Evaluation;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface UserDao {
    int saveUser(User user);
    User getUserByOpenId(@Param("openid") String openId);
    int addEvaluation(Evaluation evaluation);
    int deleteEvaluation(@Param("id") long commentId,
                         @Param("openid") String openId);
    int addAddress(@Param("address") String address,
                   @Param("id") long userId);
    ArrayList<Deal> getMyDeals(@Param("openid") String openId);
}
