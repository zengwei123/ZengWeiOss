package com.zw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan("com.zw")
@EnableScheduling
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("-1"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("-1");
        return factory.createMultipartConfig();
    }

}
