package com.example.springtutorial.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.Getter;

// @Componentアノテーションをつけましょう。DIコンテナに登録しておき、イベント発生を待つ
@Component

//getterを自動生成
@Getter
public class OneSecEventListener {
    // 秒数カウント変数
    private int count;

    //イベント発生時の処理
    //イベントを受け付けるメソッドには、 @EventListenerアノテーションをつける
    @EventListener
    private void onOneSecEvent(OneSecEvent event) {
        this.count++; // カウントを1秒加算
    }
}
