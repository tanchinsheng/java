package it.txt.tpms.tp;

public class TPTestDetails {

    private int testNoID;
    private String testNo;
    private String newFlag;
    private double oldLSL;
    private double oldUSL;
    private String unit;
    private double newLSL;
    private double newUSL;
    private String testsComments;
    
    public TPTestDetails()
    {
    }
    
    public TPTestDetails ( int testNoID, String testNo, String newFlag, double oldLSL, double oldUSL, String unit, double newLSL, double newUSL, String testsComments){
    	this.testNoID = testNoID;
        this.testNo = testNo;
        this.newFlag = newFlag;
        this.oldLSL = oldLSL;
        this.oldUSL = oldUSL;
        this.unit = unit;
        this.newLSL = newLSL;
        this.newUSL = newUSL;
        this.testsComments = testsComments;
    }

    public int getTestNoID() {
    	return testNoID;
    }  
    
    public String getTestNo() {
    	return testNo;
    }

    public String getNewFlag() {
    	return newFlag;
    }
   
    public double getOldLSL() {
    	return oldLSL;
    }
    
    public double getOldUSL() {
    	return oldUSL;
    }
    
    public String getUnit() {
    	return unit;
    }  
    
    public double getNewLSL() {
    	return newLSL;
    }  
    
    public double getNewUSL() {
    	return newUSL;
    }  
    
    public String getTestsComments() {
    	return testsComments;
    }      
    
    public void setTestNoID(int testNoID) {
        this.testNoID = testNoID;
    }  
    
    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }
    
    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }
    
    public void setOldLSL(double oldLSL) {
        this.oldLSL = oldLSL;
    }
    
    public void setOldUSL(double oldUSL) {
        this.oldUSL = oldUSL;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }  
    
    public void setNewLSL(double newLSL) {
        this.newLSL = newLSL;
    }  
    
    public void setNewUSL(double newUSL) {
        this.newUSL = newUSL;
    }  
    
    public void setTestsComments(String testsComments) {
        this.testsComments = testsComments;
    }  
    
}
