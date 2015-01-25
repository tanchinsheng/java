package com.st.mcc.rtm.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class BeanRTMBins 
{
	private int SITE_NO;
	private int HW_BIN;
	private double SITE_YIELD;
	private double BIN_COUNT = 0;
	private double GOOD_BIN_COUNT = 0;
//	private double accumulatedYield = 0;   //1.03.00: REMOVE: UNUSED COL

	
	/**
	 * @param sITE_NO the sITE_NO to set
	 */
	public void setSITE_NO(int site_no) {
		SITE_NO = site_no;
	}
	/**
	 * @return the sITE_NO
	 */
	public int getSITE_NO() {
		return SITE_NO;
	}
	/**
	 * @param hW_BIN the hW_BIN to set
	 */
	public void setHW_BIN(int hw_bin) {
		HW_BIN = hw_bin;
	}
	/**
	 * @return the hW_BIN
	 */
	public int getHW_BIN() {
		return HW_BIN;
	}
	/**
	 * @param sITE_YIELD the sITE_YIELD to set
	 */
	public void setSITE_YIELD() {
		if (BIN_COUNT == 0)
		{
			SITE_YIELD = 0;
		}
		else
		{
			NumberFormat formatter = new DecimalFormat("#0.000");
			SITE_YIELD = Double.parseDouble(formatter.format(100 * GOOD_BIN_COUNT / BIN_COUNT));
		}
	}
	/**
	 * @return the sITE_YIELD
	 */
	public double getSITE_YIELD() {
		return SITE_YIELD;
	}
	/**
	 * @param bIN_COUNT the bIN_COUNT to set
	 */
	public void setBIN_COUNT(double bin_count) {
		BIN_COUNT = bin_count;
	}
	/**
	 * @return the bIN_COUNT
	 */
	public double getBIN_COUNT() {
		return BIN_COUNT;
	}
	
	/**
	 * @param gOOD_BIN_COUNT the gOOD_BIN_COUNT to set
	 */
	public void setGOOD_BIN_COUNT(double gOOD_BIN_COUNT) {
		GOOD_BIN_COUNT = gOOD_BIN_COUNT;
	}
	/**
	 * @return the gOOD_BIN_COUNT
	 */
	public double getGOOD_BIN_COUNT() {
		return GOOD_BIN_COUNT;
	}
	
//	public double getAccumulatedYield() {  //1.03.00: REMOVE: UNUSED COL
//		return accumulatedYield;
//	}
//
//	public void setAccumulatedYield(double accumulatedYield) {
//		this.accumulatedYield = accumulatedYield;
//	}
	
}
