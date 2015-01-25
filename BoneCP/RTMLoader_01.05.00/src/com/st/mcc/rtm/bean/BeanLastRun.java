package com.st.mcc.rtm.bean;


public class BeanLastRun 
{
	private String timeZone;
	private String startTime;
	private String tdLastRunTime;
	private int tdSameLastRunTimeCnt = 0;
	private int tdIndex = 0;
	private long accIndexTime = 0;
	private long accTestTime = 0;
	private int testedUnitCnt = 0;  
	private int goodBinCnt = 0;  
	private int minorStop = 0;
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getTdLastRunTime() {
		return tdLastRunTime;
	}
	
	public void setTdLastRunTime(String tdLastRunTime) {
		this.tdLastRunTime = tdLastRunTime;
	}
	
	public int getTdSameLastRunTimeCnt() {
		return tdSameLastRunTimeCnt;
	}
	
	public void setTdSameLastRunTimeCnt(int tdSameLastRunTimeCnt) {
		this.tdSameLastRunTimeCnt = tdSameLastRunTimeCnt;
	}
	
	public int getTdIndex() {
		return tdIndex;
	}
	
	public void setTdIndex(int tdIndex) {
		this.tdIndex = tdIndex;
	}
	
	public long getAccIndexTime() {
		return accIndexTime;
	}
	
	public void setAccIndexTime(long accIndexTime) {
		this.accIndexTime = accIndexTime;
	}
	
	public long getAccTestTime() {
		return accTestTime;
	}
	
	public void setAccTestTime(long accTestTime) {
		this.accTestTime = accTestTime;
	}

	public int getTestedUnitCnt() {
		return testedUnitCnt;
	}

	public void setTestedUnitCnt(int testedUnitCnt) {
		this.testedUnitCnt = testedUnitCnt;
	}

	public int getGoodBinCnt() {
		return goodBinCnt;
	}

	public void setGoodBinCnt(int goodBinCnt) {
		this.goodBinCnt = goodBinCnt;
	}

	public int getMinorStop() {
		return minorStop;
	}

	public void setMinorStop(int minorStop) {
		this.minorStop = minorStop;
	}


	
}
