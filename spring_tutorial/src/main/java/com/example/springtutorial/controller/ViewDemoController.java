package com.example.springtutorial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewDemoController {
	
	//GetMappingは表示させるURLの末尾につける文字。究極なんでもいい
	@GetMapping("/view")
	    public String viewDemo() {
	        return "tutorialView";
	    }
}
