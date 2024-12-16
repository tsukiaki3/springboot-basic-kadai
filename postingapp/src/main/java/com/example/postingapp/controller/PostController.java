package com.example.postingapp.controller;

import java.util.List;
//Optional型とは、値が存在するかどうかを明示的に扱うため
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.PostEditForm;
import com.example.postingapp.form.PostRegisterForm;
import com.example.postingapp.security.UserDetailsImpl;
import com.example.postingapp.service.PostService;

@Controller

//@RequestMappingアノテーションをつけることで、各メソッドに共通のパス（今回は「/posts」）を繰り返し記述する必要がなくなる
@RequestMapping("/posts")
public class PostController {
	//サービス定数
    private final PostService postService;

    //DI
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    //UserDetailsImplクラスはユーザー情報を保持する役割
    //インタフェースを実装したクラスに@AuthenticationPrincipalアノテーションをつけることで、現在ログイン中のユーザー情報を取得
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
    	
    	//ユーザーを取得
        User user = userDetailsImpl.getUser();
        
        //サービスに記述した特定のユーザーに紐づく投稿の一覧を作成日時が新しい順で取得
        List<Post> posts = postService.findByUserOrderByUpdatedAt(user);

        //データを渡す
        model.addAttribute("posts", posts);

        return "posts/index";
    }
    
    //★投稿詳細ページ
    @GetMapping("/{id}")
    //引数に@PathVariableアノテーションをつけることで、URLの一部を割り当て。これにより変数のように扱える。showメソッド内でidの値を使い処理できる。
    public String show(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
    	// 指定したidを持つ投稿を取得(サービスに定義した処理)
        Optional<Post> optionalPost  = postService.findPostById(id);

        //投稿が存在しない場合の処理
        if (optionalPost.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "投稿が存在しません。");

            //リダイレクト
            return "redirect:/posts";
        }

        //Optional<Post>型のままではエンティティの各フィールドに直接アクセスできないため,post型に変換
        Post post = optionalPost.get();
        
        //データを渡してあげる
        model.addAttribute("post", post);

        return "posts/show";
    }  
    
    
    //★投稿作成ページ
    @GetMapping("/register")
    public String register(Model model) {
    	
    	//フォームクラスのインスタンスを渡してあげる
        model.addAttribute("postRegisterForm", new PostRegisterForm());

        return "posts/register";
    }
    
    
    //★投稿作成機能
    @PostMapping("/create")
    
    //@AuthenticationPrincipalアノテーションで現在ログイン中のユーザー情報を取得.
    //@ModelAttributeで引数を割り当て
    public String create(@ModelAttribute @Validated PostRegisterForm postRegisterForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes,
                         Model model)
    {
    	//エラーがあるかチェック
        if (bindingResult.hasErrors()) {
        	//エラーが存在すれば投稿作成ページを再度表示
            model.addAttribute("postRegisterForm", postRegisterForm);
            return "posts/register";
        }

        //ユーザー取得
        User user = userDetailsImpl.getUser();

        //★重要　サービスに定義したcreatePostを使い、投稿をpostsテーブルに追加
        postService.createPost(postRegisterForm, user);
        
        //リダイレクト
        redirectAttributes.addFlashAttribute("successMessage", "投稿が完了しました。");
        return "redirect:/posts";
    } 
    
    //★投稿編集ページ
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id,
                       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                       RedirectAttributes redirectAttributes,
                       Model model)
    {
    	
    	//Optional型とは、値が存在するかどうかを明示的に扱うものfindPostByIdで指定したidを持つ投稿を取得する
        Optional<Post> optionalPost = postService.findPostById(id);
        
        //ユーザーを取得
        User user = userDetailsImpl.getUser();

        //URLのidに一致する投稿が存在しない場合だけでなく、その投稿を作成したユーザーがログイン中のユーザーと一致しない場合にも投稿一覧ページにリダイレクト
        //すべてのクラスがObjectクラスを継承しているため、エンティティに対してもこのequals()メソッドが使用可能
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");
            return "redirect:/posts";
        }

        //投稿取得
        Post post = optionalPost.get();
        
        //投稿のidを送ってあげる
        model.addAttribute("post", post);
        
        //更新前の値であるフォームの初期値をビューに渡す(取得してきたpostを使い) 
        model.addAttribute("postEditForm", new PostEditForm(post.getTitle(), post.getContent()));

        return "posts/edit";
    } 
    
    //★投稿更新機能
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated PostEditForm postEditForm,
                         BindingResult bindingResult,
                         @PathVariable(name = "id") Integer id,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes,
                         Model model)
    {
    	//Optional型とは、値が存在するかどうかを明示的に扱うものfindPostByIdで指定したidを持つ投稿を取得する
        Optional<Post> optionalPost = postService.findPostById(id);
        
        //ユーザーを取得
        User user = userDetailsImpl.getUser();

        //URLのidに一致する投稿が存在しない場合だけでなく、その投稿を作成したユーザーがログイン中のユーザーと一致しない場合にも投稿一覧ページにリダイレクト
        //すべてのクラスがObjectクラスを継承しているため、エンティティに対してもこのequals()メソッドが使用可能
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");

            return "redirect:/posts";
        }

        //投稿取得
        Post post = optionalPost.get();

        //エラーがあるかチェック
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("postEditForm", postEditForm);

            return "posts/edit";
        }

        //成功すればサービスに定義したupdaePostをフォームクラスと投稿を使い,呼ぶ
        postService.updatePost(postEditForm, post);
        redirectAttributes.addFlashAttribute("successMessage", "投稿を編集しました。");

        return "redirect:/posts/" + id;
    }
    
    //★削除機能
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes,
                         Model model)
    {
    	//Optional型とは、値が存在するかどうかを明示的に扱うものfindPostByIdで指定したidを持つ投稿を取得する
        Optional<Post> optionalPost = postService.findPostById(id);
        
        //ユーザーを取得
        User user = userDetailsImpl.getUser();

        //他人の投稿を削除できないようにするために
        //URLのidに一致する投稿が存在しない場合だけでなく、その投稿を作成したユーザーがログイン中のユーザーと一致しない場合にも投稿一覧ページにリダイレクト
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");

            return "redirect:/posts";
        }

        //投稿取得
        Post post = optionalPost.get();
        
        //サービスに定義した削除機能を行う
        postService.deletePost(post);
        redirectAttributes.addFlashAttribute("successMessage", "投稿を削除しました。");

        return "redirect:/posts";
    }
}
