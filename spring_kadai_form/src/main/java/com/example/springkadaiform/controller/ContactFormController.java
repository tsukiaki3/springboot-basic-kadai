package com.example.springkadaiform.controller;

import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springkadaiform.form.ContactForm;


@Controller
public class ContactFormController {
	@GetMapping("/form")
	public String toiawase(Model model) {
		if(!model.containsAttribute("contactForm")) {
			
			//ContactFormを渡してあげる
			model.addAttribute("contactForm",new ContactForm());
		}
		return "contactFormView";
	}
	
	@PostMapping("/confirm")
	public String check(RedirectAttributes redirectAttributes,
			@Validated ContactForm form,BindingResult result) {
		
		//バリデーションエラーがあるか確認
		if(result.hasErrors()) {
			//フォームクラスをビューに渡す
			redirectAttributes.addFlashAttribute("contactForm",form);
				
			//バリデーション結果をビューに渡す
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX
						+Conventions.getVariableName(form),result);
				
			//リダイレクト(formに紐づいたメソッド実行)
			return "redirect:/form";
				
		}

		// フォームクラスをビューに受け渡す
		redirectAttributes.addFlashAttribute("contactForm", form);
		return "confirmView";
	}
}