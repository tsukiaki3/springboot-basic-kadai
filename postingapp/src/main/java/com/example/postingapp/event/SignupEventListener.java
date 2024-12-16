package com.example.postingapp.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.postingapp.entity.User;
import com.example.postingapp.service.VerificationTokenService;

//DIコンテナクラスに登録させる
@Component
public class SignupEventListener {
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;

    //DI
    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = mailSender;
    }

    //★重要イベント発生時に実行したいメソッドに対して@EventListenerアノテーションをつける
    @EventListener
    
    //★重要「どのイベントの発生時か」を指定するため、通知を受け付けるEventクラスをメソッドの引数に設定
    private void onSignupEvent(SignupEvent signupEvent) {
    	// SignupEventクラスから通知を受けたときに実行される処理
        User user = signupEvent.getUser();
        //トークンをUUID(重複しないIDで生成)
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);

       //今回はメール関係の処理が難しそうなので省く
//        String senderAddress = "springboot.postingapp@example.com";
//        String recipientAddress = user.getEmail();
//        String subject = "メール認証";
//        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
//        String message = "以下のリンクをクリックして会員登録を完了してください。";
//
//        //SimpleMailMessageクラスを使ってメール内容を作成
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        
//        //送信元メールアドレスセット
//        mailMessage.setFrom(senderAddress);
//        
//        //送信先メールアドレスセット
//        mailMessage.setTo(recipientAddress);
//        
//        //件名セット
//        mailMessage.setSubject(subject);
//        
//        //メッセージ・本文セット
//        mailMessage.setText(message + "\n" + confirmationUrl);
//        
//        //javaMailSenderインターフェースを使ってメールを送信
//        javaMailSender.send(mailMessage);
    }
}