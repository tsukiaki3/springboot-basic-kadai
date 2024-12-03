package com.example.springtutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

     @GetMapping("/login")
     public String login() {
         // ログイン画面表示
         return "auth/login";
     }
}
