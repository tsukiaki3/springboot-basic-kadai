package com.example.springtutorial.event;

import org.springframework.context.ApplicationEvent;

//ApplicationEventクラスを継承
public class OneSecEvent extends ApplicationEvent {
    // コンストラクタ（イベント処理を初期化）
    public OneSecEvent(Object source) {
    	
    	//継承元であるApplicationEventクラスのコンストラクタを呼ぶ
        super(source);
    }
}
