package tol;

import java.util.Vector;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

/**
 *  Implements a communication channel, via TCP/IP, between a frontend listener
 *  and a backend listener.
 *  Provide a mechanism to send/receive data to/from a single backend listener, listening
 *  on a specific TCP/IP port, according to a specific application protocol<p>
 * */
public class tolSocket
{
 /**
  * backend listener host IP address.
  */
 String                         _ssckIPAddr;
 /**
  * backend listener "base" TCP/IP port
  */
 int                            _ssckBasePort;
 /**
  * backend listener offset
  */
 int                            _offset;
 /**
  * backend listener actual TCP/IP port: results from adding the listener offset
  * to the base TCP/IP port
  */
 int                            _ssckPort;
 /**
  * frontend listener actual TCP/IP port
  */
 int                            _lstSckPort;
 /**
  * frontend socket to send data to backend
  */
 Socket                         lsck;
 /**
  * frontend TCP/IP listener
  */
 ServerSocket lstSck;
 /**
  * frontend socket to receive data from backend
  */
 Socket                         rdrSck;
 /**
  * frontend listener actual IP address (host + port)
  */
 String                         lstSckAddress;
 /**
  * frontend listener actual TCP/IP port
  */
 Integer                        lstSckPort;
 DataInputStream                inStrm;
 DataOutputStream               outStrm;
 static final char              _strTerm = '\n';
 static final char              _dlmChr  = ' ';
 public static final String     _dlmStr  = " ";
 static final String            _EOF     = "EOF";
 /**
  * Backend listener application time out. if the backend listener doesn't reply with the
  * acknowledgment message within this time out it is assumed that the backend elaboration
  * failed
  */
 int                            timeOut; //socket timeout expressed in [ms]
/**
 * Is responsible for recording debug information into a proper log file
 */
 LogWriter log = null;

 /**
  * Sets the log writer
  * @param log - log writer to be used
  */
 public void setLogWriter(LogWriter log) {this.log=log;}
 /**
  * Writes debug information into the log file through {@see #log}
  * @param msg
  */
 public void debug(Object msg) {if (log!=null) log.p(msg);}
 /**
  * Writes debug information into the log file through {@see #log}
  * @param msg
  */
 public void debug(String msg) {if (log!=null) log.p(msg);}

/**
  * @param _lstSckPort frontend listener TCP/IP port (this listener run on local host)
  * @param _ssckIPAddr backend host IP address
  * @param _ssckBasePort backend lisneter base port
  * @param _offset backens listener offset
  * @param _ssckPort backens listener port
  */
 public tolSocket(int _lstSckPort, String _ssckIPAddr, int _ssckBasePort, int _offset, int _ssckPort)
 {
  this._lstSckPort      = _lstSckPort;
  this._ssckIPAddr      = _ssckIPAddr;
  this._ssckBasePort    = _ssckBasePort;
  this._offset          = _offset;
  this._ssckPort        = _ssckPort;
 }

 public void setSsckIPAddr(String s) {this._ssckIPAddr=s;}

 public void setSsckPort(int n) {this._ssckPort=n;}

 /**
  * Initializes both the frontend listener and the socket to send data to backend.
  * @see #initLstSocket()
  * @see #initSocket()
  * @throws IOException
  */
 public void initSockets() throws IOException
 {
  initSocket(); initLstSocket();
 }

 /**
  * Initializes the socket to send data to the backend listener.
  * @throws IOException
  */
 public void initSocket() throws IOException
 {
  //----- FIRST RECORD SEND -----//
  //We muust catch the 'Connection refused' IOException, because
  //the SAS listener could not be listening - we must wait for the
  //SAS process IC (instruction counter) to come back to the listening
  //data step.
  //REMARK: SAS opens the socket at the beginning of the listening data step.
  //        and then free it at the end of that step

  boolean opened=false;
  IOException ioExcept=null;

  debug("PING-TO-"+_ssckIPAddr+":"+_ssckPort+">");
  for (int i=0; ((i<15)&&(!opened)); i++)
  {
    if (i>0)
    {
      try{Thread.currentThread().sleep(500);}
      catch(InterruptedException e) {}
    }
    try
    {
      lsck          = new Socket(_ssckIPAddr,_ssckPort);
      outStrm       = new DataOutputStream(lsck.getOutputStream());
    }
    catch(IOException e)
    {
      ioExcept=e;
      continue;
    }
    opened=true;
  }
  if (!opened) throw ioExcept;
 }

 /**
  * Initializes the frontend listener allocating its TCP/IP port.
  * @throws IOException
  */
 public void initLstSocket() throws IOException
 {
   initLstSocket(0);
   //REMARK: timeout = 0 means 'infinite' timeout (it's the default server socket setting)
 }

 /**
  * Initializes the frontend listener allocating its TCP/IP port.
  * @param tOut time out expressed in ms for the backend response
  * @throws IOException
  */
 public void initLstSocket(int tOut) throws IOException
 //tOut is expressed in seconds
 {
  lstSck        = new ServerSocket(_lstSckPort);
  timeOut       = tOut*1000;
  lstSckAddress = InetAddress.getLocalHost().getHostAddress();
  lstSckAddress = lstSckAddress.concat(":");
  lstSckPort    = new Integer(_lstSckPort);
  lstSckAddress = lstSckAddress.concat(lstSckPort.toString());
  debug("IP:PORT-ALLOCATED>" + lstSckAddress); //debug
 }

 /**
  * Closes the socket to send to backend
  * @throws IOException
  */
 public void closeSocket() throws IOException
 {outStrm.close(); lsck.close();}

/**
 * Deallocates the frontend listener.
 * @throws IOException
 */
 public void closeLstSocket() throws IOException
 {lstSck.close();}


/**
 * Deallocates both the frontend listener and the socket to send to backend.
 * @throws IOException
 */
 public void closeSockets() throws IOException
 {
  outStrm.close(); lsck.close(); lstSck.close();
 }

/**
 * Sends the line <i>s</i> to the backend listener, along with the necessary control signals.<p>
 * Assumes that the socket to send to backend has been already allocated/initialized.
 * @param s
 * @throws IOException
 */
 public void send(String s) throws IOException
 {
  String st = "";
  st = st.concat(lstSckAddress);
  st = st.concat(" ");
  st = st.concat(s);
  debug("SEND-TO-"+_ssckIPAddr+":"+_ssckPort+">" + st);
  for (int i=0; i<st.length(); i++) outStrm.writeByte((int)st.charAt(i));
  outStrm.writeByte((int)_strTerm);
  outStrm.flush();
 }


/**
 * Sends a batch of lines <i>s</i> to the backend listener, along with the necessary control signals.<p>
 * Assumes that the socket to send to backend has been already allocated/initialized.
 * @param v batch of liens to send: each vector item is a single line (each line is a String)
 * @throws IOException
 */
 public void send(Vector v) throws IOException
 {
  for (int i=0; i<v.size(); i++)
  {
    String st = new String();
    st = st.concat(lstSckAddress);
    st = st.concat(" ");
    st = st.concat((String)v.elementAt(i));
    debug("SEND-TO-"+_ssckIPAddr+":"+_ssckPort+">" + st);

    for (int j=0; j<st.length(); j++) outStrm.writeByte((int)st.charAt(j));
    outStrm.writeByte((int)_strTerm);
  }

  for (int i=0; i<_EOF.length(); i++)
      outStrm.writeByte((int)_EOF.charAt(i));
  outStrm.writeByte((int)_strTerm);

  outStrm.flush();
 }

/**
 * Sends the line <i>s</i> to the backend listener, along with the necessary control signals.<p>
 * A proper socket il allocated and then close after the send operation.
 * @param s
 * @throws IOException
 */
 public synchronized void cSend(String s) throws IOException
 {
  initSocket();
  send(s);
  closeSocket();
 }

/**
 * Sends a batch of lines <i>s</i> to the backend listener, along with the necessary control signals.<p>
 * A proper socket il allocated and then close after the send operation.
 * @param v batch of liens to send: each vector item is a single line (each line is a String)
 * @throws IOException
 */
 public synchronized void cSend(Vector v, boolean debug) throws IOException
 {
  if (!debug)
  {
    initSocket();
    send(v);
    closeSocket();
  }
  else
  {
    for (int i=0; i<v.size(); i++)
    {
      debug("PROCESSING>"+v.elementAt(i));
    }
  }
 }

/**
 * Receives the response, assuming that it consists of a single line, from the backend listener along
 * with the necessary control signals.<p>
 * Assumes that the socket to send to backend has been already allocated/initialized.
 * @param debug if true make I/O operation over the socket is replaced by a 5 seconds
 * wait for debug purposes
 * @return backend response
 * @throws IOException
 */
 public synchronized String receive(boolean debug) throws IOException
 // questo metodo funziona perfettamente
 // istanzia la socket rdrSck e la chiude
 {
   if (!debug)
   {
     StringBuffer strBf = new StringBuffer();
     char c;
     lstSck.setSoTimeout(timeOut); //timeOut must be set each time the accept method is call
     rdrSck    = lstSck.accept();
     inStrm    = new DataInputStream(rdrSck.getInputStream ());

     while ((c=(char)inStrm.readByte())!=_strTerm) { strBf.append((char)c); }
     inStrm.close();
     rdrSck.close();
     return strBf.toString();
   }
   else
   {
     try{Thread.currentThread().sleep(5000);}
     catch(InterruptedException e) {throw new IOException(e.getMessage());}
     debug("SCK-ACK>");
     return new String("0");
   }
 }

/**
 * Receives the response, potentially consisting of more lines, from the backend listener along
 * with the necessary control signals.<p>
 * Assumes that the socket to send to backend has been already allocated/initialized.
 * @param debug if true make I/O operation over the socket is replaced by a 5 seconds
 * wait for debug purposes
 * @return backend response consisting of more lines: each vector item is a single line
 * (a String)
 * @throws IOException
 */
    public synchronized String receive(Vector v, boolean debug) throws IOException
    // questo metodo funziona perfettamente
    // istanzia la socket rdrSck e la chiude
    {
      if (!debug)
      {
        String s = "";
        lstSck.setSoTimeout(timeOut); //timeOut must be set each time the accept method is call
        rdrSck    = lstSck.accept();
        inStrm    = new DataInputStream(rdrSck.getInputStream ());
        System.out.println();
        while(!s.equals(tolSocket._EOF))
        {
            StringBuffer strBf = new StringBuffer();
            char c;
            while ((c=(char)inStrm.readByte())!=_strTerm)
            {
                strBf.append((char)c);
            }
            s=strBf.toString();
            System.out.println("S>"+s);
            if (!s.equals(tolSocket._EOF)) v.addElement(s);
        }
        inStrm.close();
        rdrSck.close();
        return (String)v.elementAt(0);
      }
      else
      {
        try{Thread.currentThread().sleep(5000);}
        catch(InterruptedException e) {throw new IOException(e.getMessage());}
        debug("SCK-ACK>");
        return "";
      }
    }

/**
 * It in turn sends the line <i>s</i> to the backend listener and receives the backend response.<p>
 * @see #cSend(String)
 * @see #receive(boolean)
 * @param s line to send
 * @return  backend response (consisting in a single line)
 * @throws IOException
 */
 public synchronized String cSendRecv(String s) throws IOException
 {
  cSend(s);
  return (receive(false));
 }

/**
 * It in turn sends a batch of lines <i>s</i> to the backend listener and receives the backend response.<p>
 * @see #cSend(Vector, boolean)
 * @see #receive(boolean)
 * @param v lines to send
 * @return  backend response (consisting in a single line)
 * @throws IOException
 */
 public synchronized String cSendRecv(Vector v) throws IOException
 {
  cSend(v, false);
  return (receive(false));
 }

 public synchronized String debug(Vector v, int proc) throws Exception
 {
  for (int i=0; i<v.size(); i++)
  {
    Thread.currentThread().sleep(5000);
    debug("PROCESSING"+proc+">"+v.elementAt(i));
  }
  return new String("0");
 }

/** Returns backend listener base port and offset indication in such a form:
 * $BASE_PORT-$OFFSET
 */
 public String getPortAndOffsetStr()
 {
   return new Integer(_ssckBasePort).toString()+"-"+new Integer(_offset).toString();
 }

/**
 * Extracts and returns the port number from a complete listener IP address specficiation
 * in such a form: $HOST:$PORT
 */
 public static String getPortFotmIPAddressString(String s)
 {
     int i=s.lastIndexOf(':');
     return s.substring(i+1);
 }

/**
 * Extracts and returns the host from a complete listener IP address specfication
 * in such a form: $HOST:$PORT
 */
 public static String getHostFotmIPAddressString(String s)
 {
     int i=s.lastIndexOf(':');
     return s.substring(0,i);
 }

 public static void main(String args[]) throws Exception
 {
     System.out.println(getHostFotmIPAddressString("pippo:txt"));
     System.out.println(getPortFotmIPAddressString("pippo:txt"));
     Runtime.getRuntime().exec("D:/Program Files/Microsoft Office/Office/excel.exe");
 }
};
