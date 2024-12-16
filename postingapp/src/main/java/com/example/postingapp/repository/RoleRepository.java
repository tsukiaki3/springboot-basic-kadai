package com.example.postingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postingapp.entity.Role;

//認可用のリポジトリ			
//JpaRepositoryインターフェースを継承するだけで、基本的なCRUD操作を行うためのメソッドが利用可能
												//<エンティティクラス型、主キーのデータ型>
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	//役割名で役割を検索するメソッド
	 public Role findByName(String name);
}