package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Product")
public class Product {
    private long id;
    private int merchant_id;
    private String product_name;
    private String product_cover;
    private String product_desc;
    private String detail_pictures;
    private int has_stock_limit;
    private int init_stock;
    private int remain_stock;
    private float price;
    private int status;
    private Date create_time;
    private Date update_time;
}
