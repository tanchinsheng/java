package it.txt.tpms.backend;

import it.txt.afs.clearcase.ClearcaseInterface;
import it.txt.afs.clearcase.utils.ClearcaseInterfaceCostants;

import java.io.IOException;
import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 5-lug-2006
 * Time: 10.18.26
 */
public class BackEndInterface extends ClearcaseInterface {

    public static int invokeCommand ( String unixUser, String inFilePath, String outFilePath, String sessionId, Object dbUpdateObject, Method dbUpdateMethod, Object[] dbUpdateMetodParametersValues ) throws IOException {
        int result = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
        try {
            result = invokeCommand( unixUser, inFilePath, outFilePath, sessionId );
        } finally {
            //in any case try to call the deamon that invokes the DB update...
            File outFile = new File( outFilePath);
            UpdateDBProcess updateDBProcess = new UpdateDBProcess(dbUpdateObject, dbUpdateMethod, dbUpdateMetodParametersValues, outFile);
            updateDBProcess.setDaemon( true );
            updateDBProcess.start();
        }
        return result;
    }

}
