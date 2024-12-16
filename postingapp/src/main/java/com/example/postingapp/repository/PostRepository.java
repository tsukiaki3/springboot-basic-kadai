package com.example.postingapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
public interface PostRepository extends JpaRepository<Post, Integer> {
	
	 //引数に指定したユーザの投稿を日時が新しい順に取得
	 public List<Post> findByUserOrderByCreatedAtDesc(User user);
	 
	 //追加課題 (更新日時古い順)
	 public List<Post> findByUserOrderByUpdatedAt(User user);
	 
	 //idカラムの値で降順に並べ替え、最初の1件を取得するメソッド(idが最も大きい投稿を取得)
	 public Post findFirstByOrderByIdDesc();
}