package com.yuno.api.config;

import com.yuno.api.ApiApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiApplication apiApplication;

    WebConfig(ApiApplication apiApplication) {
        this.apiApplication = apiApplication;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**") //chỉ áp dụng cho các API bắt đầu bằng /api/
            .allowedOrigins("http://localhost:3000") //cho phép cổng 3000 gọi
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //cho phép các cách gọi này
            .allowedHeaders("*") //cho phép mọi loại tiêu đề (headers)
            .allowCredentials(true);
    }
}
