/**
 * 
 */
package com.st.mcc.rtm.util.exception;

/**
 * @author hdle
 *
 */
public class TechnicalException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// instance variables
	private String ms_className = "";

	private String ms_message = "";

	private Throwable mo_exception = null;

	// Constructor
	public TechnicalException(String ps_className, String ps_message, Throwable po_throwable)
	{
		ms_className = ps_className;
		ms_message = ps_message;
		mo_exception = po_throwable;
	}

	// Accessors
	public String getMessage()
	{
		return ms_message;
	}

	/**
	 * @return Returns the exception.
	 */
	public Throwable getException()
	{
		return this.mo_exception;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return this.ms_className;
	}

	/**
	 * @param ps_exception
	 *            The exception to set.
	 */
	public void setException(Exception ps_exception)
	{
		this.mo_exception = ps_exception;
	}

	/**
	 * @param ps_className
	 *            The className to set.
	 */
	public void setClassName(String ps_className)
	{
		this.ms_className = ps_className;
	}

	// Instance functions
	public String toString()
	{
		StringBuffer lo_returnString = new StringBuffer(ms_className + " : "
				+ ms_message);
		lo_returnString.append(System.getProperty("line.separator"));
		if (mo_exception != null)
		{
			lo_returnString.append(mo_exception.getMessage());
			StackTraceElement[] lo_stackElements = mo_exception.getStackTrace();
			for (int i = 0; i < lo_stackElements.length; i++)
			{
				lo_returnString.append(System.getProperty("line.separator"));
				lo_returnString.append(lo_stackElements[i].toString());
			}
		}
		return lo_returnString.toString();
	}

}
