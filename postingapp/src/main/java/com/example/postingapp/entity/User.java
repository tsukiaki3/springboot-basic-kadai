package com.example.postingapp.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "email")
    private String email;
        
    @Column(name = "password")
    private String password;    
    
    //★重要 多対一のリレーション。1人のユーザーは1つの役割だが、1つの役割は複数のユーザーを持てる
    @ManyToOne
    
    //ただのカラムではなくJoinColumnを使う
    @JoinColumn(name = "role_id")
    private Role role;   
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    
    //insertable属性には値を挿入できるかどうか,updatable属性には値を更新できるかどうか(カラムに挿入または更新する値の管理をデータベース側に任せられる)
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    
  //insertable属性には値を挿入できるかどうか,updatable属性には値を更新できるかどうか(カラムに挿入または更新する値の管理をデータベース側に任せられる)
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt; 
}

