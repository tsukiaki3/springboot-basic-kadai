package com.example.springtutorial.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//ビューを通さない
@RestController
public class ProfileController {
	
	//メッセージプリンター定数（まだ空）
    private final MessagePrinter messagePrinter;

    //DI
    public ProfileController(MessagePrinter messagePrinter) {
        // プロファイルに応じたMessagePrinterを格納
        this.messagePrinter = messagePrinter;
    }

    // URLアクセス時にメッセージを出力
    @GetMapping("/profile")
    public String profile() {
    	
    	//↓で定義したDevPrinterとProdprinterクラスはMessagePrinterを実装しており、
        return messagePrinter.printMessage();
    }
}

//プロファイルを切り替える場合インタフェースを定義する（具体的な処理不要）
interface MessagePrinter {
    String printMessage(); // メッセージを出力
}

// 開発環境向けの設定クラス
@Component

//@Profileアノテーションでプロファイル名を指定
@Profile("dev")
//インタフェースを実装したクラス
class DevPrinter implements MessagePrinter {
    public String printMessage() {
        return "これは開発環境で実行されました。";
    }
}

// 本番環境向けの設定クラス
@Component

//@Profileアノテーションでプロファイル名を指定
@Profile("prod")
//インタフェースを実装したクラス
class ProdPrinter implements MessagePrinter  {
    public String printMessage() {
        return "これは本番環境で実行されました。";
    }
}
