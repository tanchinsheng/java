package com.st.mcc.rtm.bean;

public class BeanRTMContent
{
	private String runTime;	 //SQL query will convert to Date
	private String testMode;
	private int testTime;
	private int indexTime;
	private int testIndexTime;
	private long accumulatedIndexTime;
	private long accumulatedTestTime;
	private String[] BINS;
	private float[] YIELD;
	
	private BeanRTMBins [] RTM_BINS = new BeanRTMBins [0];
	
	public void setRTMBins(BeanRTMBins[] rtm_bins)
	{
		RTM_BINS = rtm_bins;
	}

	public BeanRTMBins[] getRTMBins()
	{
		return RTM_BINS;
	}
	
	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

    public String getTestMode() {
		return testMode;
	}

	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}

	public int getTestTime() {
		return testTime;
	}

	public void setTestTime(int testTime) {
		this.testTime = testTime;
	}

	public int getIndexTime() {
		return indexTime;
	}

	public void setIndexTime(int indexTime) {
		this.indexTime = indexTime;
	}

	public int getTestIndexTime() {
		return testIndexTime;
	}

	public void setTestIndexTime(int testIndexTime) {
		this.testIndexTime = testIndexTime;
	}

	public void setBins(String [] bins)
	{
		BINS = bins;
	}
	
	public String [] getBins()
	{
		return BINS;
	}
	
	public void setYield(float [] yield)
	{
		YIELD = yield;
	}
	
	public float [] getYield()
	{
		return YIELD;
	}

	public long getAccumulatedIndexTime() {
		return accumulatedIndexTime;
	}

	public void setAccumulatedIndexTime(long accumulatedIndexTime) {
		this.accumulatedIndexTime = accumulatedIndexTime;
	}

	public long getAccumulatedTestTime() {
		return accumulatedTestTime;
	}

	public void setAccumulatedTestTime(long accumulatedTestTime) {
		this.accumulatedTestTime = accumulatedTestTime;
	}

}
