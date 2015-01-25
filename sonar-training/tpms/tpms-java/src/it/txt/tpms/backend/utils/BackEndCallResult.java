package it.txt.tpms.backend.utils;


public class BackEndCallResult {
    private int callResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;




    private Object resultData = null;


    public BackEndCallResult(){
    }

    public BackEndCallResult(int callResult){
        this.callResult = callResult;
    }

    public BackEndCallResult(int callResult, Object resultData){
        this.callResult = callResult;
        this.resultData = resultData;
    }


    public int getCallResult() {
        return callResult;
    }

    public boolean isOkResult(){
        return (callResult == BackEndInterfaceConstants.COMMAND_OK_RESULT);
    }

    public Object getResultData() {
        return resultData;
    }

}
