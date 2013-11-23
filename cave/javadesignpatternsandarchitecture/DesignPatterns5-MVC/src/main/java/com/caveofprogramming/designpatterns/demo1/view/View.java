package com.caveofprogramming.designpatterns.demo1.view;

import javax.swing.JFrame;

import com.caveofprogramming.designpatterns.demo1.model.Model;

public class View extends JFrame {
	
	private Model model;

	public View(Model model) {
		super("MVC Demo");
		
		this.model = model;
	}
	
	

}
