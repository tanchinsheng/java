package tol;

import java.util.Vector;

public class SimpleDispatcher
{
 protected int offset;
 protected int cont;
 protected int pendingCont;
 protected int pendArr[];
 protected int N;
 LogWriter log = null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public SimpleDispatcher(int N)
 {
  this.N = N;
  offset = 0;
  cont = 0;
  pendingCont = 0; //nella nuova ottica pending cont e' booleano (= 0 || 1)
  pendArr = new int[N];
  for (int i=0;i<N;i++) pendArr[i]=0;
 }


 public synchronized boolean isFree(int proc) {return (pendArr[proc]==0);}

 public int getPending() {return pendingCont;}

 public void markFirstFailedProc(int n)
 {
   this.offset=((n+1) < this.N ? n+1 : 0);
   debug("DISP-OFFSET>"+new Integer(this.offset).toString());
 }

 public synchronized boolean pushThisProcess(int proc)
 {
   if (pendArr[proc]==0)
   {
      pendArr[proc]=1;
      pendingCont++;
      debug("PUSH"+proc+">");
      return true;
   }
   else return false;
 }

 public synchronized Vector push(int k)
 //if there are k free processes returns a vector containing theese processes IDs, whereas
 //it returns null
 {
  Vector v = new Vector();
  //determines the free processes and pops their IDs into the v vector
  for (int i=0; i<N; i++)
  {
    int j=(i+this.offset)%this.N;
    if (pendArr[j]==0) v.addElement(new Integer(j));
    if (v.size()==k) break;
  }
  //if there are enough free processes returns the v vector and sets theese processes as buisy,
  //whereas returns null
  if (v.size()==k)
  {
    for (int i=0; i<v.size(); i++)
    {
      int proc = ((Integer)v.elementAt(i)).intValue();
      pendArr[proc]=1;
      debug("PUSH"+proc+">");
    }
    pendingCont++;
  }
  else
  {
    v=null;
  }
  return v;
 }


 public synchronized void pop(Vector procsVect)
 //It sets each process whose ID is contained into the procsVet vector as free
 {
  if (procsVect==null) return;
  for (int i=0; i<procsVect.size(); i++)
  {
   int proc = ((Integer)procsVect.elementAt(i)).intValue();
   pendArr[proc]=0;
   pendingCont--;
   debug("POP"+proc+">");
  }
 }
}











