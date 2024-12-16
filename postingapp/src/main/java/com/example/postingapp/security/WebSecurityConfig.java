package com.example.postingapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//設定用クラスとして認識させる
@Configuration

//セキュリティ機能を有効にする
@EnableWebSecurity

//メソッドレベルでのセキュリティ機能を有効(例そのユーザーのみがそのメソッドにアクセスできる)
@EnableMethodSecurity
public class WebSecurityConfig {
	
	//★重要@Beanアノテーションで、起動時にメソッドの戻り値（インスタンス）がDIコンテナに登録
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        
        	//authorizeHttpRequests()メソッドで誰に、どのページへのアクセスを許可するかを設定
            .authorizeHttpRequests((requests) -> requests
            		
            	//全ユーザーにアクセスを許可するURL
                .requestMatchers("/css/**","/signup/**").permitAll() 
                
                // 上記以外のURLはログインが必要
                .anyRequest().authenticated()  
            )
            
            //formLogin()メソッドやlogout()メソッドでログイン・ログアウトに関するURLを設定
            .formLogin((form) -> form
            		
            	// ログインページのURL
                .loginPage("/login")
                
                //ログインフォームの送信先URL
                .loginProcessingUrl("/login")
                
                // ログイン成功時のリダイレクト先URL
                .defaultSuccessUrl("/posts?loggedIn")
                
                // ログイン失敗時のリダイレクト先URL
                .failureUrl("/login?error") 
                
                //最後のpermitAll()メソッドは、ログイン・ログアウト関連のURLが誰でもアクセス可能を示す
                //これがなければバリデーションが表示されない
                .permitAll()
            )
            .logout((logout) -> logout
            		
            	// ログアウト後のリダイレクト先URL
                .logoutSuccessUrl("/login?loggedOut")  
                .permitAll()
            );

        return http.build();
    }

    
    //BCryptPasswordEncoderクラスのインスタンスを返すことで、
    //パスワードのハッシュアルゴリズムを「BCrypt」に設定
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
