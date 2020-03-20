package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("User")
public class User {
    private long id;
    private String openid;
    private String phone;
    private String nick_name;
    private String sex;
    private String head_img;
    private String address_list;
    private int vip_level;
    private int vip_score;
    private float account_balance;
    private Date create_time;

    public User(String openid, String phone, String nick_name, String sex, String head_img, Date create_time) {
        this.openid = openid;
        this.phone = phone;
        this.nick_name = nick_name;
        this.sex = sex;
        this.head_img = head_img;
        this.create_time = create_time;
    }
}

