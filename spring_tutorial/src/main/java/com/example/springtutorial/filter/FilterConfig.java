package com.example.springtutorial.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//設定用のクラスのため、@Configurationアノテーション
@Configuration
public class FilterConfig {
	
	//★フィルター登録処理
    @Bean
    
    //フィルター登録用のメソッドを定義するときは、FilterRegistrationBeanクラスを使う
    FilterRegistrationBean<HeaderFooterFilter> registerCorrectFilter() {

        // 重要　フィルター登録用のオブジェクトを生成
        FilterRegistrationBean<HeaderFooterFilter> regBean = new FilterRegistrationBean<>();

        // 重要　フィルターを設定(setFilter()メソッドにフィルターのインスタンスを渡すことで、フィルターを設定)
        regBean.setFilter(new HeaderFooterFilter());

        //  全リクエスト・レスポンスがフィルターの対象(addUrlPatterns()メソッドを使えば、フィルターを適用するパターンを設定可)
        //*にすることで全リクエスト・レスポンスが対象になる。仮に/loginにすればloginの所だけフィルター作動（コンソール画面の文字が少し変わる）
        regBean.addUrlPatterns("/*");

        // オブジェクトを返すことでDIコンテナに登録
        return regBean;
    }
}
