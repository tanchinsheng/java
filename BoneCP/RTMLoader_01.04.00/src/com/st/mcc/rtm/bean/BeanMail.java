package com.st.mcc.rtm.bean;

public class BeanMail {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Member data
    private String ms_userName = "";
    private String ms_userMailAddress = "";
    private String ms_emailSubject = "";
    private String ms_emailText = "";

    // Accessors
    // getters
    public String getUserMailAddress()
    {
        return ms_userMailAddress;
    }

    public String getEmailText()
    {
        return ms_emailText;
    }

    public String getUserName()
    {
        return ms_userName;
    }

    public String getEmailSubject()
    {
        return ms_emailSubject;
    }

    // Setters
    public void setUserMailAddress(String ps_userMailAddress)
    {
        ms_userMailAddress = ps_userMailAddress;
    }

    public void setEmailText(String ps_emailText)
    {
        ms_emailText = ps_emailText;
    }

    public void setUsername(String ps_userName)
    {
        ms_userName = ps_userName;
    }

    public void setEmailSubject(String ps_emailSubject)
    {
        ms_emailSubject = ps_emailSubject;
    }

    public String toString()
    {
        StringBuffer toReturn = new StringBuffer("BeanMail(");
        toReturn.append(ms_emailSubject + ",");
        toReturn.append(ms_emailText + ",");
        toReturn.append(ms_userMailAddress + ",");
        toReturn.append(ms_userName + ")");
        return toReturn.toString();
    }
}
