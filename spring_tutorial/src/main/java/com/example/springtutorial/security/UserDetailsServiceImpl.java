package com.example.springtutorial.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springtutorial.entity.User;
import com.example.springtutorial.repository.UserRepository;

//サービスとして認識させる。、UserDetailsServiceインターフェースを実装
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	//リポジトリ定数
    private final UserRepository userRepository;

    //コンストラクタ内で依存性の注入
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //DBからユーザー情報を取得し、UserDetailsオブジェクトを生成するメソッド定義
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 送信されたユーザー名と一致するユーザー情報をテーブルから取得(get(0)にすることで先頭要素をとる)
            User user = userRepository.findByUserName(username).get(0);

            // ゲッターロールIDに応じたロール名を取得
            String userRoleName = switch(user.getRoleId()) {
                case 1  -> "ROLE_GENERAL";
                case 2  -> "ROLE_ADMIN";
                default -> "ROLE_GENERAL";
            };

            //ロールのGrantedAutohorityインタフェースのリストとする
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // ユーザーのロール名をリストに追加
            //ロールをリストに追加するときには、SimpleGrantedAuthorityクラスを使う
            authorities.add(new SimpleGrantedAuthority(userRoleName));

            // ユーザー情報とロールリストをもとにUserDetailsImplを生成し、返す
            return new UserDetailsImpl(user, authorities);
            
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }
}
