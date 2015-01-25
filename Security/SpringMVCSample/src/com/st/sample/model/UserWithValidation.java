package com.st.sample.model; 

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class UserWithValidation {
		@NotEmpty
		@Pattern(regexp="[a-zA-Z0-9]{1,}", message="Only letters and numbers are allowed")
	   private String userName;
		
	   private String email;	   
	   public void setUserName(String userName){
		      this.userName=userName;
		  }
	   public String getUserName(){
		      return userName;
	   }	   
	   public void setEmail(String email){
		      this.email=email;
		  }
	   public String getEmail(){
		      return email;
	   }
	}
