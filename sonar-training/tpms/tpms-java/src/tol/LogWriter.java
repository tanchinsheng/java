package tol;
import java.io.*;

public class LogWriter
{
  String fName;
  PrintWriter wrtr;


  public LogWriter(String fName, boolean append) throws IOException
  {
    this.fName=fName;
    wrtr=new PrintWriter(new FileWriter(fName,append),true);
  }

  public void close() throws IOException
  {
    wrtr.flush();
    wrtr.close();
  }

  public synchronized void p(Object obj)
  {
    this.p(obj.toString());
  }

  public synchronized void p(Exception e)
  {
    e.printStackTrace(wrtr);
    wrtr.flush();
  }

  public synchronized void p(String line)
  {
    wrtr.println(line);
  }

  public Writer getOut() {return wrtr;}

  public static void main(String[] args) throws Exception
  {
    LogWriter log = new LogWriter("c:/temp/log.txt", false);
    log.p("LINE #1");
    log.p("LINE #2");
    log.p("LINE #3");
  }
}
