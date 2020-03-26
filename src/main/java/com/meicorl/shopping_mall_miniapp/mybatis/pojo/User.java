package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@Alias("User")
public class User implements Serializable {
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

    public User(long id, String openid, String phone, String nick_name, String sex, String head_img, String address_list, int vip_level, int vip_score, float account_balance, Date create_time) {
        this.id = id;
        this.openid = openid;
        this.phone = phone;
        this.nick_name = nick_name;
        this.sex = sex;
        this.head_img = head_img;
        this.address_list = address_list;
        this.vip_level = vip_level;
        this.vip_score = vip_score;
        this.account_balance = account_balance;
        this.create_time = create_time;
    }

    public User(String openid, String phone, String nick_name, String sex, String head_img, Date create_time) {
        this.openid = openid;
        this.phone = phone;
        this.nick_name = nick_name;
        this.sex = sex;
        this.head_img = head_img;
        this.create_time = create_time;
    }


}

