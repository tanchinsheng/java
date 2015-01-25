package com.st.sample.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.st.sample.model.UserWithValidation;

@Controller
public class FormWithValidationController {
	   
	@RequestMapping(value = "/formWithValidation.html", method = RequestMethod.GET)
	public void simpleForm(Map model) {
		model.put("userWithValidation", new UserWithValidation());
	  }

	
	@RequestMapping(value = "/formWithValidation.html", method = RequestMethod.POST)
	public String processValidatinForm(@Valid UserWithValidation user,
			BindingResult result, Map model) {
		if (result.hasErrors()) {
			model.put("error", "There are errors");
			return "formWithValidation";
		}
		model.put("userWithValidation", user);
		return "formWithValidation";
	}
}
