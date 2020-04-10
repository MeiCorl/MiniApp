package com.meicorl.shopping_mall_miniapp.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("订单生成POST请求实体")
public class DealRequestModel {
    @ApiModelProperty(value = "订单包含商品列表", required = true)
    private List<DealProduct> product_list;

    @Data
    static class DealProduct {
        int id;
        int amount;
    }
}
