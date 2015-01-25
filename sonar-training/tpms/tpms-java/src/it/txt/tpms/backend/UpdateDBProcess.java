package it.txt.tpms.backend;

import it.txt.afs.AfsCommonStaticClass;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-nov-2006
 * Time: 15.41.49
 * this class was thinked to put a patch to the fact that if the user does not wait until the action result is been displayed the db is not updated.
 */
public class UpdateDBProcess extends Thread {
    private Object updateDbObject = null;
    private Method updateDbMethod = null;
    private Object[] updateDbMethodParametersValues = null;
    private File outFile = null;


    public UpdateDBProcess ( Object updateDbObject, Method updateDbMethod, Object[] updateDbMethodParametersValues, File outFile ) {
        this.updateDbObject = updateDbObject;
        this.updateDbMethod = updateDbMethod;
        this.updateDbMethodParametersValues = updateDbMethodParametersValues;
        this.outFile = outFile;
    }


    public void run () {
        if ( updateDbObject != null && updateDbMethod != null && outFile != null ) {
            //verify that the updateDbMethod is owned by updateDbObject
            if ( updateDbMethod.getDeclaringClass().getName().equals( updateDbObject.getClass().getName() ) ) {
                //wait until the BE action is terminated
                while ( !outFile.exists() ) {
                    try {
                        Thread.sleep( 100 );
                    } catch ( InterruptedException e ) {
                        AfsCommonStaticClass.errorLog( "UpdateDbProcess :: run : InterruptedException : " + e.getMessage(), e );
                    }
                }
                //than update db...
                try {
                    updateDbMethod.invoke( updateDbObject, updateDbMethodParametersValues );
                } catch ( IllegalAccessException e ) {
                    AfsCommonStaticClass.errorLog( "UpdateDbProcess :: run : IllegalAccessException : " + e.getMessage(), e );
                } catch ( InvocationTargetException e ) {
                    AfsCommonStaticClass.errorLog( "UpdateDbProcess :: run : InvocationTargetException : " + e.getMessage(), e );
                }
            } else {
                AfsCommonStaticClass.errorLog( "UpdateDbProcess :: run : the given method it's not owned by the given object" );
            }
        }
    }


}
