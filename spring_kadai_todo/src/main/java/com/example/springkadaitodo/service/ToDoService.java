package com.example.springkadaitodo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springkadaitodo.entity.ToDo;
import com.example.springkadaitodo.repository.ToDoRepository;

@Service
public class ToDoService {
	//リポジトリ定数
	private final ToDoRepository toDoRepository;
	
	//DI
	public ToDoService(ToDoRepository toDoRepository) {
		this.toDoRepository=toDoRepository;
	}
	
	// ToDoの一括取得メソッド
    public List<ToDo> getAllToDo() {
        return toDoRepository.findAll();
    }
}