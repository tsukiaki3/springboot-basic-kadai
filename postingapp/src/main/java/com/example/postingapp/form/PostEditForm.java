package com.example.postingapp.form;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


//更新用フォームクラス
@Data

//フィールドをセットするためのコンストラクタを自動生成してくれる
//(コントローラからビューにインスタンスを渡すときに、投稿の更新前の値をセットするため)
@AllArgsConstructor
public class PostEditForm {
	@Length(max=40,message="最大40文字です")
    @NotBlank(message = "タイトルを入力してください。")
    private String title;

	@Length(max=200,message="最大200文字です")
    @NotBlank(message = "本文を入力してください。")
    private String content;
}
