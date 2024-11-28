package com.example.springkadaiform.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//ゲッター・セッター自動生成
@Data
public class ContactForm {

	//名前
   @NotBlank(message = "お名前を入力してください。")
   private String name;

   //メールアドレス
   @Email(message = "メールアドレスの入力形式が正しくありません。")
   @NotBlank(message = "メールアドレスを入力してください。")
   private String mail;

   //お問い合わせ
   @NotBlank(message = "お問い合わせ内容を入力してください")
   private String toi;
}
