package com.example.springkadaitodo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springkadaitodo.entity.ToDo;

//JpaRepositoryを継承するだけでCRUD機能を利用可能<エンティティ名、主キーのデータ型>
public interface ToDoRepository extends JpaRepository<ToDo, Integer>{

	List<ToDo>findByTitle(String todoTitle);
}
