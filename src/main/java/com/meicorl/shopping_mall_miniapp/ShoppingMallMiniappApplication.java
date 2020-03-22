package com.meicorl.shopping_mall_miniapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan(basePackages = "com.meicorl.shopping_mall_miniapp.mybatis.dao")
@ServletComponentScan   // 允许扫描Servlet组件(过滤器、监听器等)
@SpringBootApplication
@EnableCaching
public class ShoppingMallMiniappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallMiniappApplication.class, args);
    }

}
