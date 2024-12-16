package com.example.postingapp.event;

import org.springframework.context.ApplicationEvent;

import com.example.postingapp.entity.User;

import lombok.Getter;

//外部（Listenerクラス）から情報を取得できるようにゲッターを定義
@Getter

//ApplicationEventクラスはイベントを作成するための基本的なクラス。それを今回は継承
public class SignupEvent extends ApplicationEvent {
	//会員登録したユーザー情報変数
    private User user;
    
    //リクエストを受けたURL変数
    private String requestUrl;

    //DI
    public SignupEvent(Object source, User user, String requestUrl) {
    	//publisherクラスのインスタンス渡す
        super(source);

        this.user = user;
        this.requestUrl = requestUrl;
    }
}