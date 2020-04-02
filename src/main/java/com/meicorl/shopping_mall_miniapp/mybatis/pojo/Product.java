package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@Alias("Product")
public class Product implements Serializable {
    private long id;
    private int merchant_id;
    private String product_name;
    private String product_tag;
    private String product_cover;
    private String product_desc;
    private String detail_pictures;
    private int has_stock_limit;
    private int init_stock;
    private int remain_stock;
    private float price;
    private float discount_price;
    private int status;
    private Date create_time;
    private Date update_time;

    public void setPrice(float price) {
        this.price = price;
        this.discount_price = price;
    }

}
