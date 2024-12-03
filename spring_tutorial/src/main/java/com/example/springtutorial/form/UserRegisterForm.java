package com.example.springtutorial.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//getter・setterを自動生成
@Data
public class UserRegisterForm {
	
	//バリデーション
	@NotBlank(message = "ユーザー名を入力してください。")
    // ユーザー名
    private String userName;

	@NotBlank(message = "パスワードを入力してください。")
    @Size(min = 8, message = "パスワードは少なくとも8文字は必要です。")
    // パスワード
    private String password;

	//1か2のどちらかしかないため
	@NotNull(message = "ロールを正しく取得できませんでした。")
    // ロールID
    private Integer roleId;
}