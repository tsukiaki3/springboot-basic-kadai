package com.example.springtutorial.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

//@ComponentがついていることでDIコンテナに登録され、必要なタイミングでイベント発行（必須）
@Component

//定数を引数として受け取るコンストラクタを自動生成
@RequiredArgsConstructor

//1秒おきなど決まった処理を有効化するため
@EnableScheduling
public class OneSecEventPublisher {
	
	//ApplicationEventPublisherインターフェース定数
    private final ApplicationEventPublisher applicationEventPublisher;

    // あくまでスケジュールであり処理ではない。メソッドの上で1秒経過ごとにイベントを発生(@Scheduled(fixedRate = 実行間隔)と指定。実行間隔はミリ秒で指定)
    @Scheduled(fixedRate = 1000)
    public void publishOneSecEvent() {
    	//毎回新しいイベントのインスタンスを生成して渡す
        OneSecEvent event = new OneSecEvent(this);
        
        //publishEventでイベントを発行
        applicationEventPublisher.publishEvent(event);
    }
}
