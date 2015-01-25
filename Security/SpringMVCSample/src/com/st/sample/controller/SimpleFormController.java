package com.st.sample.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.st.sample.model.User;

@Controller
public class SimpleFormController {
	   
	@RequestMapping(value = "/simpleForm.html", method = RequestMethod.GET)
	public void simpleForm(Model model) {
		   model.addAttribute(new User());
	  }
	@RequestMapping(value = "/simpleForm.html", method = RequestMethod.POST)
	public void simple(@ModelAttribute User user, Model model, HttpSession session) {
		   model.addAttribute("user", user);		
		   
		   session.setAttribute("session-user-name", user.getUserName());
	}
}
