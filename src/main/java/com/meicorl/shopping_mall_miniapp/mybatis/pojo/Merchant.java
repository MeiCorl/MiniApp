package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("Merchant")
public class Merchant implements Serializable {
    private int id;
    private String merchant_name;
//    private int merchant_type;
    private String logo;
    private String description;
    private String building;
    private int floor;
//    private String owner_name;
//    private String phone;
//    private String password;
//    private int status;
//    private Date create_time;
//    private Date update_time;
}
