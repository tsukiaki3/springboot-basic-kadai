package com.example.postingapp.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.postingapp.entity.User;

//DIコンテナに登録
@Component
public class SignupEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    //DI
    public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishSignupEvent(User user, String requestUrl) {
    	//イベントを発行するには、ApplicationEventPublisherインターフェースが提供するpublishEvent()メソッド
    	//今回はユーザーの会員登録が完了したタイミングでイベントを発行
        applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
    }
}