package com.example.postingapp.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.Post;
import com.example.postingapp.service.PostService;

//テスト時に設定や依存関係などの環境を起動する
@SpringBootTest

//MockMvcインスタンスを注入できるようにする
@AutoConfigureMockMvc

//テスト用の設定ファイルを指定
//(application-○○○.properties」の「○○○」の部分を指定)
@ActiveProfiles("test")
public class PostControllerTest {
	
	//MockMvcを注入。MockMvcとはツールで、テストを効率的に行うためのもの
	//(フィールドに対してDIを行う手法を、フィールドインジェクション。コードを簡潔に書けるメリットがある)
    @Autowired
    private MockMvc mockMvc;
    
    //(フィールドに対してDIを行う手法を、フィールドインジェクション。コードを簡潔に書けるメリットがある)
    @Autowired
    private PostService postService; 

    //★投稿一覧ページテスト
    //テスト時に実行させる
    @Test
    //@WithUserDetailsアノテーションでログイン済みユーザーとして振る舞う
    @WithUserDetails("taro.samurai@example.com")
    public void ログイン済みの場合は投稿一覧ページが正しく表示される() throws Exception {
    	//perform()メソッドでHTTPリクエストを送信(今回はGETメソッドで "/posts"にHTTPリクエストを送信)
        mockMvc.perform(get("/posts"))
        
        //andExpect()メソッドで検証
        	   //HTTPステータスコードが200OKであるか？
               .andExpect(status().isOk())
               
               //表示されたビューが"posts/index"である。
               .andExpect(view().name("posts/index"));
    }

    //テスト時に実行させる
    @Test
    public void 未ログインの場合は投稿一覧ページからログインページにリダイレクトする() throws Exception {
    	
    	//perform()メソッドでHTTPリクエストを送信(今回はGETメソッドで "/posts"にHTTPリクエストを送信)
        mockMvc.perform(get("/posts"))
        
      //andExpect()メソッドで検証
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    //★投稿詳細ページテスト
    @Test
    @WithUserDetails("taro.samurai@example.com")
    public void ログイン済みの場合は投稿詳細ページが正しく表示される() throws Exception {
        mockMvc.perform(get("/posts/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("posts/show"));
    }

    @Test
    public void 未ログインの場合は投稿詳細ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/posts/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }    
    
    
    //★投稿作成ページテスト
    @Test
    @WithUserDetails("taro.samurai@example.com")
    public void ログイン済みの場合は投稿作成ページが正しく表示される() throws Exception {
        mockMvc.perform(get("/posts/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("posts/register"));
    }

    @Test
    public void 未ログインの場合は投稿作成ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/posts/register"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    } 
    
    //★投稿作成機能テスト
    @Test
    @WithUserDetails("taro.samurai@example.com")
    
    //Transactionalアノテーションを付与すると、テストメソッドの実行後にデータベースの変更をロールバック、つまりテストメソッド実行前の状態に戻す
    @Transactional
    public void ログイン済みの場合は投稿作成後に投稿一覧ページにリダイレクトする() throws Exception {
    	//with()メソッドをつなげ、引数にcsrf()メソッドを指定することで、HTTPリクエストにCSRFトークンを含める
    	//★重要Webサーバーの状態を変更するHTTPリクエストにはCSRFトークンを含める必要がある
    	//param()メソッドをつなげることで、HTTPリクエストにリクエストパラメータを含める.つまり、フォームを送信したときと同じ振る舞い
        mockMvc.perform(post("/posts/create").with(csrf()).param("title", "テストタイトル").param("content", "テスト内容"))
        
        		//is3xxRedirection()は、HTTPステータスコードが3xx（リダイレクト）であるかどうかをチェックする関数
               .andExpect(status().is3xxRedirection())
               
               //絶対パスと相対パスのどちらで指定するかは、「アプリの内部でどのようにリダイレクトを実装しているか」に依存
               //return "redirect:/posts";のように相対パスなら相対パスで行う
               .andExpect(redirectedUrl("/posts"));

        //サービスに定義したidカラムの値で降順に並べ替え、最初の1件を取得するメソッド。idが最も大きい投稿を取得する
        Post post = postService.findFirstPostByOrderByIdDesc();
        
        //assertThat()メソッドを使うことで、以下のように実際の結果が期待する結果と一致するかどうかを検証
        //IsEqualToは一致すれば成功
        assertThat(post.getTitle()).isEqualTo("テストタイトル");
        assertThat(post.getContent()).isEqualTo("テスト内容");
    }

    @Test
  //Transactionalアノテーションを付与すると、テストメソッドの実行後にデータベースの変更をロールバック、つまりテストメソッド実行前の状態に戻す
    @Transactional
    public void 未ログインの場合は投稿を作成せずにログインページにリダイレクトする() throws Exception {
    	//with()メソッドをつなげ、引数にcsrf()メソッドを指定することで、HTTPリクエストにCSRFトークンを含める
    	//★重要Webサーバーの状態を変更するHTTPリクエストにはCSRFトークンを含める必要がある
    	//param()メソッドをつなげることで、HTTPリクエストにリクエストパラメータを含める.つまり、フォームを送信したときと同じ振る舞い
        mockMvc.perform(post("/posts/create").with(csrf()).param("title", "テストタイトル").param("content", "テスト内容"))
        
        		//is3xxRedirection()は、HTTPステータスコードが3xx（リダイレクト）であるかどうかをチェックする関数
               .andExpect(status().is3xxRedirection())
               
               //絶対パスと相対パスのどちらで指定するかは、「アプリの内部でどのようにリダイレクトを実装しているか」に依存
               .andExpect(redirectedUrl("http://localhost/login"));

        Post post = postService.findFirstPostByOrderByIdDesc();
        
        //assertThat()メソッドを使うことで、以下のように実際の結果が期待する結果と一致するかどうかを検証
        //isNotEqualToが一致しなければ成功
        assertThat(post.getTitle()).isNotEqualTo("テストタイトル");
        assertThat(post.getContent()).isNotEqualTo("テスト内容");
    } 
    
    //★投稿編集ページテスト
    @Test
    @WithUserDetails("taro.samurai@example.com")
    public void ログイン済みの場合は自身の投稿編集ページが正しく表示される() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("posts/edit"));
    }

    //idが2のユーザーでログインする。つまり他人の投稿編集ページへ試みる
    @Test
    @WithUserDetails("jiro.samurai@example.com")
    public void ログイン済みの場合は他人の投稿編集ページから投稿一覧ページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/posts"));
    }

    @Test
    public void 未ログインの場合は投稿編集ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    
    //★投稿更新機能テスト
    @Test
    @WithUserDetails("taro.samurai@example.com")
    @Transactional
    public void ログイン済みの場合は自身の投稿更新後に投稿詳細ページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/update").with(csrf()).param("title", "テストタイトル").param("content", "テスト内容"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/posts/1"));

        //findPostById()メソッドを使い、idが1の投稿データ（Optional<Post>型のオブジェクト）を取得
        Optional<Post> optionalPost = postService.findPostById(1);
        
        //isPresent()メソッドを使うことで、Optional<T>型のオブジェクトが存在するかどうかを検証
        assertThat(optionalPost).isPresent();
        
        //Optional<Post>型のままではエンティティの各フィールドに直接アクセスできないため、Post型に変換し渡してあげる
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isEqualTo("テストタイトル");
        assertThat(post.getContent()).isEqualTo("テスト内容");
    }

    //idが2のユーザーでログインする。つまり他人の投稿編集ページへ試みる
    @Test
    @WithUserDetails("jiro.samurai@example.com")
    @Transactional
    public void ログイン済みの場合は他人の投稿を更新せずに投稿一覧ページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/update").with(csrf()).param("title", "テストタイトル").param("content", "テスト内容"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/posts"));
        //findPostById()メソッドを使い、idが1の投稿データ（Optional<Post>型のオブジェクト）を取得
        Optional<Post> optionalPost = postService.findPostById(1);
        
        //isPresent()メソッドを使うことで、Optional<T>型のオブジェクトが存在するかどうかを検証
        assertThat(optionalPost).isPresent();
        
        //Optional<Post>型のままではエンティティの各フィールドに直接アクセスできないため、Post型に変換し渡してあげる
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isNotEqualTo("テストタイトル");
        assertThat(post.getContent()).isNotEqualTo("テスト内容");
    }

    //未ログイン
    @Test
    @Transactional
    public void 未ログインの場合は投稿を更新せずにログインページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/update").with(csrf()).param("title", "テストタイトル").param("content", "テスト内容"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));

        //findPostById()メソッドを使い、idが1の投稿データ（Optional<Post>型のオブジェクト）を取得
        Optional<Post> optionalPost = postService.findPostById(1);
        
        //isPresent()メソッドを使うことで、Optional<T>型のオブジェクトが存在するかどうかを検証
        assertThat(optionalPost).isPresent();
        
        //Optional<Post>型のままではエンティティの各フィールドに直接アクセスできないため、Post型に変換し渡してあげる
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isNotEqualTo("テストタイトル");
        assertThat(post.getContent()).isNotEqualTo("テスト内容");
    } 
    
    
    //★削除機能
    @Test
    @WithUserDetails("taro.samurai@example.com")
    @Transactional
    public void ログイン済みの場合は自身の投稿削除後に投稿一覧ページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/posts"));

        Optional<Post> optionalPost = postService.findPostById(1);
        
        //データが削除されていることをisEmpty()メソッドで検証
        assertThat(optionalPost).isEmpty();
    }

    //idが2のユーザーでログインする。つまり他人の投稿編集ページへ試みる。逆に投稿が削除されていないことを確認
    @Test
    @WithUserDetails("jiro.samurai@example.com")
    @Transactional
    public void ログイン済みの場合は他人の投稿を削除せずに投稿一覧ページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/posts"));

        Optional<Post> optionalPost = postService.findPostById(1);
        
        //isPresent()メソッドを使うことで、Optional<T>型のオブジェクトが存在するかどうかを検証
        assertThat(optionalPost).isEmpty();
    }

    //逆に投稿が削除されていないことを確認
    @Test
    @Transactional
    public void 未ログインの場合は投稿を削除せずにログインページにリダイレクトする() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));

        Optional<Post> optionalPost = postService.findPostById(1);
        
        //isPresent()メソッドを使うことで、Optional<T>型のオブジェクトが存在するかどうかを検証
        assertThat(optionalPost).isPresent();
    }
}