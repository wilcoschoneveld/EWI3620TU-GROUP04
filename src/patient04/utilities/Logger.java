/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.utilities;

/**
 *
 * @author Wilco
 */
public class Logger {
    private static final boolean showLog = true;
    private static final boolean showDebug = true;
    private static final boolean showError = true;
    
    public static void log(String s) {
        if(showLog) System.out.println("PATIENT04 Log: " + s);
    }
    
    public static void debug(String s) {
        if(showDebug) System.out.println("PATIENT04 Debug: " + s);
    }
    
    public static void error(String s) {
        if(showError) System.err.println("PATIENT04 Error: " + s);
    }
    
    public static void fatalerror(String s) {
        throw new RuntimeException(s);
    }
}
