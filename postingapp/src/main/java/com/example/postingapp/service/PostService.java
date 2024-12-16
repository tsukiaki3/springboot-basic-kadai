package com.example.postingapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.PostEditForm;
import com.example.postingapp.form.PostRegisterForm;
import com.example.postingapp.repository.PostRepository;

@Service
public class PostService {
	//リポジトリ定数
    private final PostRepository postRepository;

    //DI
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //追加課題
    // 特定のユーザーの投稿の一覧を作成日時が新しい順で取得する
    public List<Post> findByUserOrderByUpdatedAt(User user) {
        return postRepository.findByUserOrderByUpdatedAt(user);
    }
    
    // 指定したidを持つ投稿を取得する
    public Optional<Post> findPostById(Integer id) {
        return postRepository.findById(id);
    }  
    
    // idカラムの値で降順に並べ替え、最初の1件を取得するメソッド。idが最も大きい投稿を取得する
    public Post findFirstPostByOrderByIdDesc() {
        return postRepository.findFirstByOrderByIdDesc();
    } 
    
    //★投稿作成機能
    @Transactional
    public void createPost(PostRegisterForm postRegisterForm, User user) {
    	//投稿用のインスタンス生成
        Post post = new Post();

        //タイトル・内容・ユーザーをセット
        post.setTitle(postRegisterForm.getTitle());
        post.setContent(postRegisterForm.getContent());
        post.setUser(user);

        //保存
        postRepository.save(post);
    }  
    
    //★投稿更新機能
    @Transactional
    //フォームから送信された内容でpostsテーブルのデータを更新するメソッド
    //(第2引数で更新対象のエンティティを受け取り)
    public void updatePost(PostEditForm postEditForm, Post post) {
        post.setTitle(postEditForm.getTitle());
        post.setContent(postEditForm.getContent());

        //保存
        postRepository.save(post);
    }  
    
    //★削除機能
    @Transactional
    public void deletePost(Post post) {
    	
    	//deleteやsaveはJpaRepositoryインターフェースを継承した時点で使える
    	//削除
        postRepository.delete(post);
    }
}