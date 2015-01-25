package tol;

public class ParamException extends Exception
{
  public static int _MISSING=0;
  public static int _WRONG_FORMAT=1;

  String parName="";
  int    type=0;

  public ParamException(String parName)
  {
   super(new String("CAN'T READ THE '"+parName+"' PARAMETER"));
   this.parName=parName;
  }

  public ParamException(String parName, int type)
  {
   super(new String("CAN'T READ THE '"+parName+"' PARAMETER"));
   this.parName=parName;
   this.type=type;
  }

  public ParamException(String msg, String parName) {super(msg); this.parName=parName;}

  public ParamException(String msg, String parName, int type)
  {
   super(msg); this.parName=parName; this.type=type;
  }

  public String getParName() {return parName;}

  public String getUserMsg()
  {
   if (type==_MISSING)      return new String("THE '"+parName+"' PARAMETER IS MISSING");
   if (type==_WRONG_FORMAT) return new String("WRONG FORMAT FOR THE "+parName+"' PARAMETER");
   return                          new String("CAN'T READ THE '"+parName+"' PARAMETER");
  }
}