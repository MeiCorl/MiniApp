package com.meicorl.shopping_mall_miniapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan(basePackages = "com.meicorl.shopping_mall_miniapp.mybatis.dao")
@ServletComponentScan   // 允许扫描Servlet组件(过滤器、监听器等)
@SpringBootApplication
public class ShoppingMallMiniappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallMiniappApplication.class, args);
    }

}
