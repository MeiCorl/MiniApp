package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Deal")
public class Deal {
    private long deal_no;
    private int merchant_id;
    private float origin_money;
    private float money;
    private int need_delivery;
    private int deal_status;
    private String creator_name;
    private String creator_openid;
    private String creator_phone;
    private String content;
    private String address;
    private Date create_time;
    private Date pay_time;
}
