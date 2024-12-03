package com.example.springtutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springtutorial.event.OneSecEventListener;

@Controller
public class EventController {
	
	//イベント処理定数
    private final OneSecEventListener eventListener;

    //DI
    public EventController(OneSecEventListener eventListener) {
        // イベントリスナークラスの依存性を注入
        this.eventListener = eventListener;
    }

    @GetMapping("/event")
    public String event(Model model) {
        // イベントリスナークラスの秒数カウントをビューに渡す
        int count = eventListener.getCount();
        model.addAttribute("count", count);

        return "eventView";
    }
}
