package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}") //$: properties 파일 읽어오기
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**") //웹에 입력하는 url이 /images로 시작시에 uploadPath에 설정한 폴더를 기준으로 파일을 읽어옴
                .addResourceLocations(uploadPath);  //저장된 파일을 읽어올 root 경로 설정
    }

}
