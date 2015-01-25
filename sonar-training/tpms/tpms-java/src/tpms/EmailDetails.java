/*
 * Program Name  		:	EmailDetails.java
 * Developed by 		:	STMicroelectronics
 * Date					: 	30-Sep-2007 
 * Description			:	 
 */

package tpms;

public class EmailDetails {
 
    private String emailaddress; 
    
    public EmailDetails()
    {
    }
    
    public EmailDetails ( String emailaddress)
    {
    	this.emailaddress = emailaddress;
    }
    
    public String getEmailaddress() {
    	return emailaddress;
    }
    
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }
    
}

