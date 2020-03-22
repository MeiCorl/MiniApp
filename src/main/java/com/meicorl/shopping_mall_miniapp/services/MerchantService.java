package com.meicorl.shopping_mall_miniapp.services;

import com.meicorl.shopping_mall_miniapp.mybatis.dao.MerchantDao;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class MerchantService {
    @Autowired
    MerchantDao merchantDao;

    /**
     * 根据楼栋、楼层拉取商户列表
     * @param building 楼栋
     * @param floor 楼层
     * @return 商户列表
     */
    @Cacheable(value = "MyCache", key = "'merchants_at_' + #building + '_' + #floor", unless = "#result.isEmpty()")
    public ArrayList<Merchant> getMerchantsList(String building, int floor) {
        log.info("从数据库读取!");
        return merchantDao.getMerchants(building, floor);
    }
}
