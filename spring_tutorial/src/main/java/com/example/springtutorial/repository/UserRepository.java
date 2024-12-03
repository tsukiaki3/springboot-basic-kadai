package com.example.springtutorial.repository;

 import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springtutorial.entity.User;

 //JpaRepositoryを継承するだけでCRUD機能を利用可能<エンティティ名、主キーのデータ型>
 public interface UserRepository extends JpaRepository<User, Integer> { 
     // 具体的には、「findBy + エンティティのフィールド名」 という名前のメソッドを定義。
 	//フィールド名をもとにカラムから検索
     List<User> findByUserName(String userName);
 }