package com.example.springkadaitodo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springkadaitodo.entity.ToDo;
import com.example.springkadaitodo.service.ToDoService;

//コントローラー
@Controller
public class ToDoController {
	
	//サービス定数
	private final ToDoService toDoService;
	
	//DI(コンストラクタ)
	public ToDoController(ToDoService toDoService) {
		this.toDoService=toDoService;
	}
	
	//URL紐づけ
	@GetMapping("/todo")
	public  String first(Model model) {
		//ToDoを取得
		List<ToDo>todo=toDoService.getAllToDo();
		
		//ビューにtodoという名前で渡す
		model.addAttribute("todo",todo);
		
		return"todoView";
	}
}
