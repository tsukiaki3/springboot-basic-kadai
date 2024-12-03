package com.example.springtutorial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//設定用のクラスとして認識させる
@Configuration

//セキュリティ機能を有効にする
@EnableWebSecurity

//メソッドレベルでのセキュリティ機能を有効にする
@EnableMethodSecurity
public class WebSecurityConfig {


	//@Beanをつけることで、戻り値をDIコンテナに登録。SecurityFilterChainインターフェースを用いて、メソッド定義
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	//引数で渡されたHttpSecurityインタフェースを用いて設定する
        http
        	//アクセス許可に関する設定を行うラムダ式
            .authorizeHttpRequests((requests) -> requests
            		
            	// requestMather().permitAll()で全ユーザーにアクセスを許可するURL
                .requestMatchers("/login", "/resources/**").permitAll() 
                
                // aniRequest()で上記以外のURLはログインが必要
                .anyRequest().authenticated()  
            )
            //ログインに関する設定ラムダ式
            .formLogin((form) -> form
            		
            	// ログインページのURL。★ここでログイン画面を表示させている。コントローラーに記述した(/login)と紐づいたメソッド
                .loginPage("/login")     
                
                // ログインフォームの送信先URL
                .loginProcessingUrl("/login") 
                
                // ログイン成功時のリダイレクト先URL。成功時には?loggedInクエリパラメータをつけ、ビューで成否を判断可能にしている
                .defaultSuccessUrl("/adminuser?loggedIn") 
                
                // ログイン失敗時のリダイレクト先URL。失敗時には?errorのクエリパラメータをつけ、ビューで成否を判断可能にしている
                .failureUrl("/login?error") 
                
                //誰でもログイン関連のURLにアクセス可能
                .permitAll()
            )
            //ログアウトに関する設定ラムダ式
            .logout((logout) -> logout
            	// ログアウト時のリダイレクト先URL
                .logoutSuccessUrl("/?loggedOut") 
                
                //誰でもログアウト関連のURLにアクセス可能
                .permitAll()
            );

        //セキュリティ設定の確定.戻り値を返す。
        return http.build();
    }

    //パスワード処理メソッド
    @Bean
    PasswordEncoder passwordEncoder() {
    	
    	//ハッシュ化に用いるアルゴリズムクラスのインスタンスを返す
        return new BCryptPasswordEncoder();
    }
}
