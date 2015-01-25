package com.st.sample.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.st.sample.model.User;

@Controller
public class FormSessionBeanController implements BeanFactoryAware {
	
	//private User user;
	
	private BeanFactory beanFactory;
	   
	@RequestMapping(value = "/formSessionBean.html", method = RequestMethod.GET)
	public void simpleForm(Model model) {			
		   model.addAttribute((User) beanFactory.getBean("userBean"));
	  }
	@RequestMapping(value = "/formSessionBean.html", method = RequestMethod.POST)
	public void simple(@ModelAttribute User user, Model model) {		
		   
		  User usr = (User) beanFactory.getBean("userBean");
		  usr.setUserName(user.getUserName());
		  usr.setEmail(user.getEmail());
		   model.addAttribute("user", usr);		
	}
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
