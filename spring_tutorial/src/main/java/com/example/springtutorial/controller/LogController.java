package com.example.springtutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

//ロガー（出力を提供）するオブジェクトの生成（logという名前のロガーができる）
@Slf4j
@RestController
public class LogController {
    @GetMapping("/log")
    public String logMessage() {
    	
    	//ログの出力。log.メソッド名("ログメッセージ") という書き方が基本
        log.error("これはERRORレベルのログです。");
        log.warn("これはWARNレベルのログです。");
        log.info("これはINFOレベルのログです。");
        
        //↓２つはコンソールに出ない
        log.debug("これはDEBUGレベルのログです。");
        log.trace("これはTRACEレベルのログです。");

        //画面に表示(ビューを通さない)
        return "ログを出力しました。";
    }
}
