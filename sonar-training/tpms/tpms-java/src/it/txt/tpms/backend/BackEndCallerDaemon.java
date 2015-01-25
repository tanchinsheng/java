package it.txt.tpms.backend;



import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 5-lug-2006
 * Time: 9.59.50
 * To change this template use File | Settings | File Templates.
 */
public class BackEndCallerDaemon implements Runnable {

    private String command;
    private boolean waitResult;
    private Process commandProcess = null;



    /**
     *
     * @param command the command that should be executed
     * @param waitResult true if  you want that the run method will finish only when the
     *                   command execution is finished, false otherwise
     */
    public BackEndCallerDaemon(String command, boolean waitResult) {
        this.command = command;
        this.waitResult = waitResult;
    }

    /**
     * @param command the command that should be executed
     * the run method will not wait for the command execution termination (i.e. waitResult = false)
     */
    public BackEndCallerDaemon(String command) {
        this.command = command;
        this.waitResult = false;
    }


    /**
     * Starts the comamnd execution
     */
    public void run() {

        try {
            commandProcess = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (waitResult && commandProcess != null) commandProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Kills the command process and subprocess.
     * The subprocess represented by the command process object is forcibly terminated.
     */
    public void destroyCommandProcess(){
        if (commandProcess != null)
            commandProcess.destroy();
    }


    /**
     * Returns the exit value for the command subprocess
     * @return the exit value of the subprocess represented by this Process object (produced by command execution).
     *         by convention, the value 0 indicates normal termination, BUT pay attention (!!!) tis value depends on the command.
     * @throws IllegalThreadStateException f the subprocess represented by this Process object has not yet terminated
     *          or is not yet started
     */
    public int getCommandExitValue() throws IllegalThreadStateException{
        if (commandProcess != null){
            return commandProcess.exitValue();
        } else {
            throw new IllegalThreadStateException("Process not still running...");
        }
    }

    /**
     * Gets the output stream of the subprocess produced by the command execution. Output to the stream is piped into the
     * standard input stream of the process represented by this Process object.
     * Implementation note: It is a good idea for the output stream to be buffered.
     * @return the output stream connected to the normal input of the subprocess (produced by the command execution), null if the command is not still executed
     */
    public OutputStream getCommandOutputStream() {
        if (commandProcess != null)
            return commandProcess.getOutputStream();
        else
            return null;
    }

    /**
     * Gets the input stream of the subprocess produced by the command execution. The stream obtains data piped from the
     * standard output stream of the process represented by this Process object.
     * Implementation note: It is a good idea for the input stream to be buffered.
     * @return the input stream connected to the normal output of the subprocess (produced by the command execution), null if the command is not still executed
     */
    public InputStream getCommandInputStream() {
        if (commandProcess != null)
            return commandProcess.getInputStream();
        else
            return null;
    }

    /**
     * Gets the error stream of the subprocess produced by the command execution. The stream obtains data piped from the
     * error output stream of the process represented by this Process object.
     * Implementation note: It is a good idea for the input stream to be buffered.
     * @return the input stream connected to the error stream of the subprocess (produced by the command execution), null if the command is not still executed
     */
    public InputStream getCommandErrorStream() {
        if (commandProcess != null)
            return commandProcess.getErrorStream();
        else
            return null;
    }
}
