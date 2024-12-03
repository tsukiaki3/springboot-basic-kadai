package com.example.springtutorial.controller;

import java.util.List;

import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springtutorial.entity.User;
import com.example.springtutorial.form.UserRegisterForm;
import com.example.springtutorial.service.UserService;

@Controller
public class AdminUserController {
    private final UserService userService;

    //依存性注入（DI）。コンストラクタインジェクション
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    //URLの紐づけ(一番最初)
    @GetMapping("/adminuser")
    public String adminUser(Model model) {
        // サービスクラスに定義したgetAllUsers()メソッドを呼び、リストを取得
        List<User> users = userService.getAllUsers();

        // ビューに取得したユーザーリストを返す、usersという名前で
        model.addAttribute("users", users);

        
        // containsAttribute()メソッドで存在チェック(すでにインスタンスが存在する場合上書きされる。たとえば直前の入浴内容を表示したい場合消えてしまう)
        if (!model.containsAttribute("userRegisterForm")) {
            // ビューにフォームクラスのインスタンスを渡す
            model.addAttribute("userRegisterForm", new UserRegisterForm());
        }
        
        //ビュー名を返す
        return "adminUserView";
    }
    
    //データ登録要求が送られてくるため、受け取るためにPostmappingアノテーション
    @PostMapping("/register")
    //フォームクラスを参照するために引数に指定（UserRegisterForm form）
    //@Validatedアノテーションでフォームクラスでのバリデーションを有効にし、Bindingresultにバリデーションのエラー情報が格納
    public String registerUser(RedirectAttributes redirectAttributes,
    		@Validated UserRegisterForm form, BindingResult result) {
    		//@RequestParam(HTMLのname)
    		//adminUserViewのuser_nameを受け取り
//    		@RequestParam("user_name") String userName,
//    		
//    		//adminUserViewのnameを受け取り
//            @RequestParam("password") String password,
//            
//          //adminUserViewのrole_idを受け取り
//            @RequestParam("role_id") int roleId) {
    	
    	// バリデーションエラーがあったら終了
        if (result.hasErrors()) {
            // フォームクラスをビューに受け渡す
            redirectAttributes.addFlashAttribute("userRegisterForm", form);
            // バリデーション結果をビューに受け渡す
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX
                    + Conventions.getVariableName(form), result);

            // adminuserにリダイレクトしてリストを再表示
            return "redirect:/adminuser";
        }

        try {
        	
        	//サービス経由でデータの登録
        	// リクエストパラメータからのデータを用いてユーザー登録
            userService.createUser(form.getUserName(), form.getPassword(), form.getRoleId());
            // リクエストパラメータからのデータを用いてユーザー登録
//            userService.createUser(userName, password, roleId);

            // 登録成功時はリダイレクト先にメッセージをビューに受け渡す
            redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました。");

        } catch (IllegalArgumentException e) {
            // 登録失敗時はエラーメッセージをビューに受け渡す
            redirectAttributes.addFlashAttribute("failureMessage", e.getMessage());

            // フォームクラスをビューに受け渡す
            redirectAttributes.addFlashAttribute("userRegisterForm", form);
            
            // 再表示用の入力データをビューに受け渡す（パスワードは除く）
//            redirectAttributes.addFlashAttribute("userName", userName);
//            redirectAttributes.addFlashAttribute("roleId", roleId);
        }

        // adminuserにリダイレクト(別のURLへ遷移)してリストを再表示
        return "redirect:/adminuser";
    }
}
