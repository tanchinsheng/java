package com.caveofprogramming.designpatterns.demo1.controller;

import com.caveofprogramming.designpatterns.demo1.model.Model;
import com.caveofprogramming.designpatterns.demo1.view.LoginFormEvent;
import com.caveofprogramming.designpatterns.demo1.view.LoginListener;
import com.caveofprogramming.designpatterns.demo1.view.View;

public class Controller implements LoginListener {
	private View view;
	private Model model;
	
	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
	}

	@Override
	public void loginPerformed(LoginFormEvent event) {
		System.out.println("Login event received: " + event.getName() + "; " + event.getPassword());
		
	}
	
	
}
