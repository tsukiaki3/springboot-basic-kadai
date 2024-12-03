package com.example.springtutorial.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//設定用のクラスとして認識させる
@Configuration

//インターセプターの登録には、WebMvcConfigurerインターフェースを使う
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
    	//インターセプタークラスのインスタンスを生成し、addInterceptor()メソッドに渡し、インターセプトの登録
        registry.addInterceptor(new LogInterceptor());
    }
}
