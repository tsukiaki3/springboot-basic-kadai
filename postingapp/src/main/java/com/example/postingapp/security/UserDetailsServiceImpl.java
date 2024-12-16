package com.example.postingapp.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.postingapp.entity.User;
import com.example.postingapp.repository.UserRepository;

//サービスクラスとして認識させる
@Service

//SpringSecurityが提供するUserDetailsServiceインターフェースを実装
public class UserDetailsServiceImpl implements UserDetailsService {
	
	//userRepository定数
    private final UserRepository userRepository;

    //DI
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //UserDetailsServiceインタフェースに用意してあるloadUserByUsername()を上書き
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
        	
        	//フォームから送信されたメールアドレスに一致するユーザーを取得（ユーザーリポジトリの中に書いたfindByEmail）
            User user = userRepository.findByEmail(email);
            
            //ユーザーの役割を取得
            String userRoleName = user.getRole().getName();
            
            
            //空の配列を生成（GrantedAuthorityは、ユーザーが持つ権限（ロールやパーミッション）を表すインターフェース）
            //質問　ここなぜ配列なの？
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            
            //追加している（SimpleGrantedAuthorityは、ユーザーの持つ単一の権限を表すクラス）
            authorities.add(new SimpleGrantedAuthority(userRoleName));
            
            //UserDetailsImplクラスのインスタンス生成
            return new UserDetailsImpl(user, authorities);
            
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }
}