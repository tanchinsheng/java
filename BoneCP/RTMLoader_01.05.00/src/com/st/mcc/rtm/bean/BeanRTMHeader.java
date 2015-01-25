package com.st.mcc.rtm.bean;

import java.util.ArrayList;

import static com.st.mcc.rtm.util.AppConstants.*;
import static com.st.mcc.rtm.util.GeneralUtil.*;


public class BeanRTMHeader 
{
	//1.03.00: Non-nullable
	private String stationNo;
	private String testerId;
	private String prodArea;
	private String facilId;
	
	//1.03.00: TIMESTAMP datatype
	private String startTime = EMPTY_TIME;
	private String estimatedEnd = EMPTY_TIME;
	private String tdLastRunTime = EMPTY_TIME;  //1.01.01: Last run time from touchdown string
	
	private String timeZone = EMPTY_STR;
	private String timeOffset = EMPTY_STR;
	private String testerType = EMPTY_STR;
    private String firstSite = EMPTY_STR;
	private String handlerId = EMPTY_STR;
	private String handlerDriver = EMPTY_STR;
	private String goodBinCsv = EMPTY_STR;
	private String openShortCsv = EMPTY_STR;
	private String arteachBin = EMPTY_STR;
	private String lotId = EMPTY_STR;
	private String modCode = EMPTY_STR;
	private String cmodeCode = EMPTY_STR;
	private String salesType = EMPTY_STR;
	private String program = EMPTY_STR;
	private String tpRevision = EMPTY_STR;
	private String sites = EMPTY_STR;
	private String temperature = EMPTY_STR;
	private String flowType = EMPTY_STR;
	private String testType = EMPTY_STR;
	private String techCode = EMPTY_STR;
	private String difFusion = EMPTY_STR;
	private String packageType = EMPTY_STR;
	private String arteac = EMPTY_STR;
	private String qcSampleSize = EMPTY_STR;
	private String operatorId = EMPTY_STR;
	private String operatorName = EMPTY_STR;
	private String ltor = EMPTY_STR;
	private String orgSalesType = EMPTY_STR;
	
//	private String sitesYields;  //1.03.00: REMOVE: UNUSED
	private int startQty = 0;
	private double avgTestTime = 0;
	private double avgIndexTime = 0;
	private float avgYield = 0;
	private double budgetTestTime = 0;
	private double budgetIndexTime = 0;
	private double budgetYield = 0;
	private int minorStop = 0;
	private float mcba = 0;
	private int testedUnitCount = 0;
	private int untestedUnitCount = 0;
	
	private int goodBinCnt = 0;  //1.01.01: Latest total number of good bin count
	private int tdSameRunTimeCnt = 0;  //1.01.01: No. of duplicated last run time
	private String loadMethod = LOAD_FULL_DEL;  //1.01.01: Determine the TT loading method 
	private int tdIndexStart = 0;  //1.01.01: Start of TD index
	private long accTestTime = 0;  //1.01.01: Accumulated test time
	private long accIndexTime = 0;  //1.01.01: Accumulated index time
	private int tdCnt = 0;  //1.01.01: Total number of touchdowns incl previous loaded TT file if any.
	
	private boolean flagBudgetTestTime = false;  //1.03.00: Rename
//	private boolean flagBudgetIndexTime = false;  //1.03.00: Rename
	private boolean flagBudgetYield = false;  //1.03.00: Renames

	private ArrayList<BeanRTMContent> rtmContent = new ArrayList<BeanRTMContent>();
	
	
	// Touchdown bins string
	public ArrayList<BeanRTMContent> getRtmContent() {
		return rtmContent;
	}

	public void setRtmContent(ArrayList<BeanRTMContent> rtmContent) {
		this.rtmContent = rtmContent;
	}
		
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	//*upper
	public void setTimeZone(String timeZone) {
		this.timeZone = upper(replaceReservedChars(timeZone));
		setTimeOffset();
	}

	public String getFacilId() {
		return facilId;
	}

	public void setFacilId(String facilId) {
		this.facilId = upper(replaceReservedChars(facilId));
	}

	public String getProdArea() {
		return prodArea;
	}

	public void setProdArea(String prodArea) {
		this.prodArea = upper(replaceReservedChars(prodArea));
	}

	public String getTesterType() {
		return testerType;
	}

	public void setTesterType(String testerType) {
		this.testerType = upper(replaceReservedChars(testerType));
	}
	
	public String getTesterId() {
		return testerId;
	}

	public void setTesterId(String testerId) {
		this.testerId = upper(replaceReservedChars(testerId));
	}
	
	public String getStationNo() {
		return stationNo;
	}

	public void setStationNo(String stationNo) {
		this.stationNo = replaceReservedChars(stationNo);
	}

	public String getFirstSite() {
		return firstSite;
	}

	public void setFirstSite(String firstSite) {
		this.firstSite = firstSite;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = upper(replaceReservedChars(handlerId));
	}

	public String getHandlerDriver() {
		return handlerDriver;
	}

	//*upper
	public void setHandlerDriver(String handlerDriver) {
		this.handlerDriver = upper(replaceReservedChars(handlerDriver));
	}

	public String getGoodBinCsv() {
		return goodBinCsv;
	}

	public void setGoodBinCsv(String goodBinCsv) {
		this.goodBinCsv = goodBinCsv;
	}

	public String getOpenShortCsv() {
		return openShortCsv;
	}

	public void setOpenShortCsv(String openShortCsv) {
		this.openShortCsv = replaceReservedChars(openShortCsv);
	}

	public String getArteachBin() {
		return arteachBin;
	}

	public void setArteachBin(String arteachBin) {
		this.arteachBin = replaceReservedChars(arteachBin);
	}

	public String getQcSampleSize() {
		return qcSampleSize;
	}

	public void setQcSampleSize(String qcSampleSize) {
		this.qcSampleSize = replaceReservedChars(qcSampleSize);
	}

	public String getLotId() {
		return lotId;
	}

	//*upper
	public void setLotId(String lotId) {
		this.lotId = upper(replaceReservedChars(lotId));
	}

	public String getModCode() {
		return modCode;
	}

	//*upper
	public void setModCode(String modCode) {
		this.modCode = upper(replaceReservedChars(modCode));
	}

	public String getCmodeCode() {
		return cmodeCode;
	}

	public void setCmodeCode(String cmodeCode) {
		this.cmodeCode = upper(replaceReservedChars(cmodeCode));
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		
		String[] searchChars = new String[]{"-", "%", "/"};
		String replaceChar = "_";
		String sty = salesType;
		orgSalesType = upper(replaceReservedChars(salesType));
		
		for(int i=0; i < searchChars.length; i++)
		{
			sty = sty.replace(searchChars[i], replaceChar);
		}
//		String sty = salesType.replace("-", "_");
//		sty = sty.replace("%", "_");
//		sty = sty.replace("/", "_");  //1.03.00
		this.salesType = upper(replaceReservedChars(sty));
	}

	public String getOrgSalesType() {
		return orgSalesType;
	}

	public String getProgram() {
		return program;
	}

	//*upper
	public void setProgram(String program) {
		this.program = upper(replaceReservedChars(program));
	}

	public String getTpRevision() {
		return tpRevision;
	}

	//*upper
	public void setTpRevision(String tpRevision) {
		this.tpRevision = upper(replaceReservedChars(tpRevision));
	}

	public String getSites() {
		return sites;
	}

	public void setSites(String sites) {
		this.sites = sites;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = replaceReservedChars(temperature);
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = upper(replaceReservedChars(flowType));
	}

	public String getTestType() {
		return testType;
	}

	//*upper
	public void setTestType(String testType) {
		this.testType = upper(replaceReservedChars(testType));
	}

	public String getTechCode() {
		return techCode;
	}

	//*upper
	public void setTechCode(String techCode) {
		this.techCode = upper(replaceReservedChars(techCode));
	}

	public String getDifFusion() {
		return difFusion;
	}

	//*upper
	public void setDifFusion(String difFusion) {
		this.difFusion = upper(replaceReservedChars(difFusion));
	}

	public String getPackageType() {
		return packageType;
	}

	//*upper
	public void setPackageType(String packageType) {
		this.packageType = upper(replaceReservedChars(packageType));
	}

	public String getArteac() {
		return arteac;
	}

	public void setArteac(String arteac) {
		this.arteac = upper(replaceReservedChars(arteac));
	}

	public int getStartQty() {
		return startQty;
	}

	public void setStartQty(int startQty) {
		this.startQty = startQty;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = replaceReservedChars(operatorId);
	}

	public String getOperatorName() {
		return operatorName;
	}

	//*upper
	//1.01.01C: To handle special chars "'"
	public void setOperatorName(String operatorName) {
		this.operatorName = upper(replaceReservedChars(operatorName));
	}

	public double getAvgTestTime() {
		return avgTestTime;
	}

	public void setAvgTestTime(double avgTestTime) {
		this.avgTestTime = avgTestTime;
	}

	public double getAvgIndexTime() {
		return avgIndexTime;
	}

	public void setAvgIndexTime(double avgIndexTime) {
		this.avgIndexTime = avgIndexTime;
	}

	public float getAvgYield() {
		return avgYield;
	}

	public void setAvgYield(float avgYield) {
		this.avgYield = avgYield;
	}

	public double getBudgetTestTime() {
		return budgetTestTime;
	}

	public void setBudgetTestTime(double budgetTestTime) {
		this.budgetTestTime = budgetTestTime;
	}

	public double getBudgetIndexTime() {
		return budgetIndexTime;
	}

	public void setBudgetIndexTime(double budgetIndexTime) {
		this.budgetIndexTime = budgetIndexTime;
	}

	public double getBudgetYield() {
		return budgetYield;
	}

	public void setBudgetYield(double budgetYield) {
		this.budgetYield = budgetYield;
	}

	public int getMinorStop() {
		return minorStop;
	}

	public void setMinorStop(int minorStop) {
		this.minorStop = minorStop;
	}

	public float getMcba() {
		return mcba;
	}

	public void setMcba(float mcba) {
		this.mcba = mcba;
	}

	public String getEstimatedEnd() {
		if (estimatedEnd != null)
		{
			return estimatedEnd;
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public void setEstimatedEnd(String estimatedEnd) {
		this.estimatedEnd = estimatedEnd;
	}

	//1.03.00: REMOVE
//	public String getSitesYields() {
//		return sitesYields;
//	}
//
//	public void setSitesYields(String sitesYields) {
//		this.sitesYields = sitesYields;
//	}

	public void setTimeOffset()
	{		
		if (timeZone.substring(0, 3).equals("GMT"))
		{
			if (timeZone.substring(3, 4).equals("-"))
			{
				timeOffset = "+";
			}
			else
			{
				timeOffset = "-";
			}
			timeOffset = timeOffset + timeZone.substring(4) +  "/24";
		}
		
		if (timeZone.substring(0, 3).equals("CET"))
		{
			timeOffset = "-1/24";
		}
	}
	
	public String getTimeOffset()
	{
		return timeOffset;
	}

	public String getLtor() {
		return ltor;
	}

	public void setLtor(String ltor) {
		this.ltor = ltor;
	}

	public int getTestedUnitCount() {
		return testedUnitCount;
	}

	public void setTestedUnitCount(int testedUnitCount) {
		this.testedUnitCount = testedUnitCount;
	}
	
	public int getUntestedUnitCount() {
		return untestedUnitCount;
	}

	public void setUntestedUnitCount(int untestedUnitCount) {
		this.untestedUnitCount = untestedUnitCount;
	}

	public boolean isFlagBudgetTestTime() {
		return flagBudgetTestTime;
	}

	public void setFlagBudgetTestTime(boolean flagBudgetTestTime) {
		this.flagBudgetTestTime = flagBudgetTestTime;
	}

	//1.03.00: REMOVE: UNUSED
//	public boolean isFlagBudgetIndexTime() {
//		return flagBudgetIndexTime;
//	}
//
//	public void setFlagBudgetIndexTime(boolean flagBudgetIndexTime) {
//		this.flagBudgetIndexTime = flagBudgetIndexTime;
//	}

	public boolean isFlagBudgetYield() {
		return flagBudgetYield;
	}

	public void setFlagBudgetYield(boolean flagBudgetYield) {
		this.flagBudgetYield = flagBudgetYield;
	}

	//1.01.01
	public void setTdLastRunTime(String tdLastRunTime) {
		this.tdLastRunTime = tdLastRunTime;
	}

	public String getTdLastRunTime() {
		return tdLastRunTime;
	}

	public void setTdSameRunTimeCnt(int tdSameRunTimeCnt) {
		this.tdSameRunTimeCnt = tdSameRunTimeCnt;
	}

	public int getTdSameRunTimeCnt() {
		return tdSameRunTimeCnt;
	}

	public String getLoadMethod() {
		return loadMethod;
	}

	public void setLoadMethod(String loadMethod) {
		this.loadMethod = loadMethod;
	}

	public int getGoodBinCnt() {
		return goodBinCnt;
	}

	public void setGoodBinCnt(int goodBinCnt) {
		this.goodBinCnt = goodBinCnt;
	}

	public int getTdIndexStart() {
		return tdIndexStart;
	}

	public void setTdIndexStart(int tdIndexStart) {
		this.tdIndexStart = tdIndexStart;
	}

	public long getAccTestTime() {
		return accTestTime;
	}

	public void setAccTestTime(long accTestTime) {
		this.accTestTime = accTestTime;
	}

	public long getAccIndexTime() {
		return accIndexTime;
	}

	public void setAccIndexTime(long accIndexTime) {
		this.accIndexTime = accIndexTime;
	}

	public int getTdCnt() {
		return tdCnt;
	}

	public void setTdCnt(int tdCnt) {
		this.tdCnt = tdCnt;
	}

}
