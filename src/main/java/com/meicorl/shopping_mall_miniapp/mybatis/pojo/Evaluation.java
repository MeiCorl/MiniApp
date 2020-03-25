package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@Alias("Evaluation")
public class Evaluation implements Serializable {
    private long id;
    private int merchant_id;
    private String comment;
    private String creator_name;
    private String creator_openid;
    private Date create_time;
    private int op_status;   // 用户标记yoghurt是否对该条评论有操作权限, 0: 没有   1: 有

    public Evaluation(int merchantId, String comment, String creatorName, String creatorOpenId, Date create_time) {
        this.merchant_id = merchantId;
        this.comment = comment;
        this.creator_name = creatorName;
        this.creator_openid = creatorOpenId;
        this.create_time = create_time;
    }
}
