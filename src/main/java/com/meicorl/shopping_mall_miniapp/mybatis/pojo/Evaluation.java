package com.meicorl.shopping_mall_miniapp.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Evaluation")
public class Evaluation implements Serializable {
    private long id;
    private int merchant_id;
    private String comment;
    private String creator_name;
    private String creator_openid;
    private Date create_time;
    private int op_status;   // 用户标记yoghurt是否对该条评论有操作权限, 0: 没有   1: 有
}
