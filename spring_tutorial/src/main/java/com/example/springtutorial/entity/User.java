package com.example.springtutorial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//クラスをエンティティとして定義させる
@Entity

//クラスにテーブルを紐づけ
@Table(name="users")

//ゲッターセッターなどを自動生成
@Data
public class User {

	//主キーと認識させる
	 @Id
	 
	 //主キーに入れる値を自動生成する。
	 //生成方式をGenerationType.IDENTITYとすると、データベースのAUTO_INCREMENT(自動採番)と同等の処理
     @GeneratedValue(strategy = GenerationType.IDENTITY)
	 
	 //ColumnでDBのカラムを紐づけ
     @Column(name = "user_id")
	 
	 //int型ではなくIntegerを使うことを推奨
     private Integer userId;

     @Column(name = "user_name")
     private String userName;

     @Column(name = "password")
     private String password;

     @Column(name = "role_id")
     private Integer roleId;
}