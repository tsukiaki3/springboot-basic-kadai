package com.example.postingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.entity.User;
import com.example.postingapp.entity.VerificationToken;
import com.example.postingapp.event.SignupEventPublisher;
import com.example.postingapp.form.SignupForm;
import com.example.postingapp.service.UserService;
import com.example.postingapp.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
//認証（ログイン、会員登録機能）コントローラー
public class AuthController {
	
	 private final UserService userService;
	 //イベント発生定数
	 private final SignupEventPublisher signupEventPublisher;
	 
	 //メール認証用サービス定数
	 private final VerificationTokenService verificationTokenService;
	 
	 //DI
     public AuthController(UserService userService,SignupEventPublisher signupEventPublisher,VerificationTokenService verificationTokenService) {
         this.userService = userService;
         this.signupEventPublisher = signupEventPublisher;
         this.verificationTokenService = verificationTokenService;
     }    

	//ログイン
	@GetMapping("/login")
    public String login() {
        return "auth/login";
    }
	
	//会員登録
	 @GetMapping("/signup")
     public String signup(Model model) {
		 
		 //コントローラからビューにデータを渡す場合、Modelクラスを使う
		 //★重要 フォームクラスを利用するために、Modelクラスを使いビューにデータを渡す
         model.addAttribute("signupForm", new SignupForm());
         return "auth/signup";
     } 
	 
	 //フォームの入力内容をPOSTメソッドで送信するので@PostMapping
	 @PostMapping("/signup")
	 //ModelAttributeで送信されたデータを引数に割り当てる、Validatedでバリデーションを行う、BindingResultがバリデーション結果を保持。
     public String signup(@ModelAttribute @Validated SignupForm signupForm,BindingResult bindingResult,RedirectAttributes redirectAttributes,HttpServletRequest httpServletRequest, Model model){
         // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加
         if (userService.isEmailRegistered(signupForm.getEmail())) {
        	 //FieldErrorはエラー情報を保持してクライアント側に通知するクラス
        	 //第一引数にエラー内容を格納するオブジェクト、第二引数に発生させたフィールド名、第三引数にメッセージ
             FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
             
             //addErrorメソッドでメッセージを格納したinstanceをエラーを格納
             bindingResult.addError(fieldError);
         }
 
         // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
         if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
        	 //FieldErrorはエラー情報を保持してクライアント側に通知するクラス
        	//第一引数にエラー内容を格納するオブジェクト、第二引数に発生させたフィールド名、第三引数にメッセージ
             FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
             
           //addErrorメソッドでメッセージを格納したinstanceをエラーを格納
             bindingResult.addError(fieldError);
         }
 
         
         //エラーが存在するかチェック
         if (bindingResult.hasErrors()) {
        	 
        	 //渡す
             model.addAttribute("signupForm", signupForm);
 
             return "auth/signup";
         }
 
         User createdUser = userService.createUser(signupForm);
         
         //getRequestURL()メソッドを使い、リクエストURL（https://ドメイン名/signup）を取得
         String requestUrl = new String(httpServletRequest.getRequestURL());
         
         //Publisherクラスに定義したpublishSignupEvent()メソッドを実行すれば、ユーザーの会員登録が完了したタイミングでイベントを発行
         signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
         
         //リダイレクト先へ渡す
         redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");    
 
         return "redirect:/login";
     }   
	 
	 //メール認証用
	 @GetMapping("/signup/verify")
	 //引数に@RequestParamアノテーションをつけることで、リクエストパラメータの値をその引数に割り当て
     public String verify(@RequestParam(name = "token") String token, Model model) {
		 
		 //トークンを取得（サービスを使い）
         VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
         
         //トークンが存在すれば、会員を有効に
         if (verificationToken != null) {
             User user = verificationToken.getUser();  
             userService.enableUser(user);
             String successMessage = "会員登録が完了しました。";
             model.addAttribute("successMessage", successMessage);            
         } else {
             String errorMessage = "トークンが無効です。";
             model.addAttribute("errorMessage", errorMessage);
         }
         
         return "auth/verify";         
     }    
}