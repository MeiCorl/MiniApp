package com.meicorl.shopping_mall_miniapp.services;

import com.meicorl.shopping_mall_miniapp.mybatis.dao.MerchantDao;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Evaluation;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Merchant;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Product;
import com.meicorl.shopping_mall_miniapp.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MerchantService {
    @Autowired
    MerchantDao merchantDao;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * 根据楼栋、楼层拉取商户列表
     * @param building 楼栋
     * @param floor 楼层
     * @return 商户列表
     */
    @Cacheable(value = "MerchantCache", key = "'merchants_at_' + #building + '_' + #floor", unless = "#result.isEmpty()")
    public ArrayList<Merchant> getMerchantsList(String building, int floor) {
        log.info("从数据库读取商户列表, build: {} floor: {}", building, floor);
        return merchantDao.getMerchants(building, floor);
    }

    /**
     * 获取商户评价得分
     * @param merchants 商户列表
     */
    public void getMerchantScores(ArrayList<Merchant> merchants) {
        if(merchants.size() <= 0)
            return;
        ArrayList<Object> merchantIds = new ArrayList<>();
        for(Merchant merchant : merchants)
            merchantIds.add(String.valueOf(merchant.getId()));
        List starList = redisTemplate.opsForHash().multiGet("evaluation_stars", merchantIds);
        log.info("get scores: " + starList);
        int i = 0;
        for(Object star : starList) {
            Merchant merchant = merchants.get(i);
            if(star != null) {
                String[] arr = ((String)star).split("\\*");
                merchant.setStars(Float.parseFloat(arr[0]) / Integer.parseInt(arr[1]));
            }
            else
                merchant.setStars(4);
            i++;
        }
    }

    @Cacheable(value = "EvaluationCache", key = "'evaluations_of_' + #merchantId + '_page:' + #pageNo", unless = "#result.isEmpty()")
    public ArrayList<Evaluation> getMerchantEvaluations(int merchantId, int pageNo) {
        int offset = (pageNo - 1) * 10;
        log.info("从数据库读取评价列表, mertchant: {} pageNo: {}", merchantId, pageNo);
        ArrayList<Evaluation> evaluations = merchantDao.getMerchantEvaluations(merchantId, offset);

        // 更新当前用户对评价列表的操作权限
        String curUserId = SessionUtil.getCurrentUserId();
        if(curUserId != null) {
            for(Evaluation evaluation : evaluations) {
                if(evaluation.getCreator_openid().equals(curUserId))
                    evaluation.setOp_status(1);
            }
        }
        return evaluations;
    }

    @Cacheable(value = "ProductCache", key = "'products_of_' + #merchantId", unless = "#result.isEmpty()")
    public ArrayList<Product> getProductList(int merchantId) {
        log.info("从数据库读取商品列表, mertchant: {}", merchantId);
        return merchantDao.getProductList(merchantId);
    }
}
