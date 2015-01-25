/**
 * 
 */
package com.st.mcc.rtm.util.exception;

/**
 * @author hdle
 *
 */
public class FunctionalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// instance variables
	private String m_className = "";
	private String m_message = "";
	
	// Constructor
	public FunctionalException(String ps_className, String ps_message)
	{
		m_className = ps_className;
		m_message = ps_message;
	}
	
	// Instance functions
	public String toString()
	{
		StringBuffer lo_returnString = new StringBuffer(m_className + " : " + m_message);
		return lo_returnString.toString();  
	}
    /**
     * @return Returns the m_message.
     */
    public String getMessage()
    {
        return this.m_message;
    }

}
