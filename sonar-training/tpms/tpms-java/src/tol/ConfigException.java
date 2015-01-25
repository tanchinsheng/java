package tol;


public class ConfigException extends Exception
{
 String scope="";

 public ConfigException(String scope)
 {
   super(new String("WRONG '"+scope+"' CONFIGURATION"));
   this.scope=scope;
 }

 public ConfigException(String msg, String scope) {super(msg); this.scope=scope;}

 public String getScope() {return scope;}

 public String getUsrMsg() {return new String("WRONG '"+scope+"' CONFIGURATION");}
}
