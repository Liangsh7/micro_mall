package com.mall.promo.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.mall.promo")
@MapperScan(basePackages = "com.mall.promo.dal")
public class PromoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromoProviderApplication.class, args);
    }

}