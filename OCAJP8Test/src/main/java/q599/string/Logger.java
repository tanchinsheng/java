/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q599.string;

/**
 *
 * Which of the following options will empty the contents of the StringBuilder
 * referred to by variable sb in method dumpLog()?
 */
public class Logger {

    /**
     * @param args the command line arguments
     */
    private StringBuilder sb = new StringBuilder();

    public void logMsg(String location, String message) {
        sb.append(location);
        sb.append("-");
        sb.append(message);
    }

    public void dumpLog() {
        System.out.println(sb.toString());
        //Empty the contents of sb here     
        sb.delete(0, sb.length());
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
