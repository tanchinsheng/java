package com.st.sample.model; 

import java.io.Serializable;

public class User implements Serializable {
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
