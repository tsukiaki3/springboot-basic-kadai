package com.example.springtutorial.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

//ロガー自動生成
@Slf4j

//★重要@ControllerAdviceをつけると、グローバル（どこからでも参照できる）なクラスとして扱われ,どのコントローラ、モデルで発生しても捕まえる
//補足：コントローラではない
@ControllerAdvice
public class ErrorHandling {

	//★重要()内はキャッチしたい例外クラス名
    @ExceptionHandler(Exception.class)
    
    //@ResponseStatusアノテーションもつけ、HTTPのステータスコードを設定。これにより、ブラウザ側でもエラーの発生を把握
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex) {

        // ログにエラー内容を出力
        log.error("エラーが発生しました。メソッド名：{}, 例外クラス名：{}",
        		
        		//ex.getStackTrace()[0].getMethodName()　例外が発生したメソッド名を受け取る
                ex.getStackTrace()[0].getMethodName(),
                
                //ex.getClass().getName(）発生した例外クラス名を受け取る
                ex.getClass().getName());

        // エラーページを表示
        return "errorView";
    }
}
