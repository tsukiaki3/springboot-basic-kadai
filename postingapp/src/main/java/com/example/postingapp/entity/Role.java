package com.example.postingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//エンティティとして機能させる
@Entity

//対応するテーブル名と紐づけ
@Table(name = "roles")

//ゲッターセッターを自動生成
@Data
public class Role {
	//主キー指定
   @Id
   
   //★重要　strategy = GenerationType.IDENTITYで自動採番してくれる
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   
   //対応するカラム名を指定
   @Column(name = "id")
   private Integer id;
       
   //対応するカラム名を指定
   @Column(name = "name")
   private String name;  
}