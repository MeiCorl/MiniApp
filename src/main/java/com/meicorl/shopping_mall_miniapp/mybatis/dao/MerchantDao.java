package com.meicorl.shopping_mall_miniapp.mybatis.dao;

import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Evaluation;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface MerchantDao {
    ArrayList<Merchant> getMerchants(@Param("building") String buildig,
                                    @Param("floor") int floor);

    ArrayList<Evaluation> getMerchantEvaluations(@Param("merchant_id") int merchantId,
                                                 @Param("offset") int offset,
                                                 @Param("limit") int limit);
}
