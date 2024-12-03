package com.example.springtutorial.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springtutorial.entity.User;

//UserDatailsインタフェース(元々用意されているフレームワーク)を実装するためimplementsを使う
public class UserDetailsImpl implements UserDetails {
    // Userエンティティ定数(認証対象のユーザーデータが格納される)
    private final User user;
    
    //GrantedAuthoriyインタフェース定数
    private final Collection<GrantedAuthority> authorities;

    //コンストラクタ内で初期化
    public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    
    //メソッドにはそれぞれOverrideアノテーションをつける。可読性の向上やエラーで知らせる、継承先の変更しても気づける
    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ログイン時に利用するユーザー名を返す
    @Override
    public String getUsername() {
    	return user.getUserName();
    }

    // ロールのコレクションを返す
    @Override
    //独自にカスタマイズしたクラスを許容した書き方Collection<? extends GrantedAuthority>
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // アカウントが期限切れでなければtrueを返す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされていなければtrueを返す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ユーザーのパスワードが期限切れでなければtrueを返す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効であればtrueを返す
    @Override
    public boolean isEnabled() {
        return true;
    }
}
