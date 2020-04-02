package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Activity implements Serializable {
    private int id;
    private String act_name;
    private String act_cover;
    private Date begin_time;
    private Date end_time;
    private Date create_time;
    private Date update_time;
    private int status; // 0: 活动尚未开始  1: 活动进行中
}
