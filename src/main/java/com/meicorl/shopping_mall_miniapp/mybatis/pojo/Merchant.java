package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Merchant")
public class Merchant {
    private int id;
    private String merchant_name;
    private int merchant_type;
    private String logo;
    private String description;
    private String building;
    private int floor;
    private String owner_name;
    private String phone;
    private String password;
    private int status;
    private Date create_time;
    private Date update_time;
}
