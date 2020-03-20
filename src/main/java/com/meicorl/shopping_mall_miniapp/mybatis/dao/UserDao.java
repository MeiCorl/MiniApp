package com.meicorl.shopping_mall_miniapp.mybatis.dao;


import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    int saveUser(User user);
    User getUserByOpenId(@Param("openid") String openId);
}
