/*
 * Created on Sep 25, 2004
 */
package edu.iu.iv.toolkits.networkanalysistoolkit.util;

import java.text.DecimalFormat;

import org.cishell.framework.CIShellContext;
import org.osgi.service.log.LogService;

/**
 * @author Shashikant
 */
public class Util {
	CIShellContext context;
    private static final Util INSTANCE = new Util();

    // singleton pattern
    private Util() {
        doubleFormat = new DecimalFormat("##.####");
    }

    public static Util getInstance() {
        return INSTANCE;
    }
    
    public void setContext(CIShellContext context) {
    	this.context = context;
    }

    private DecimalFormat doubleFormat;

    public String formatDoubleToString(double d) {
        return doubleFormat.format(d);
    }

    public boolean checkInteger(int num, int min, int max, boolean inclusive) {
        return (inclusive ? (num >= min && num <= max)
                : (num > min && num < max));
    }

    public boolean checkDouble(double num, int min, int max, boolean inclusive) {
        return (inclusive ? (num >= min && num <= max)
                : (num > min && num < max));
    }
    
    public static void println(String string) {
    	/**
    	 * need to fix this
    	 */
        //IVC.getInstance().getConsole().print(string + "\n");
    	LogService log = (LogService) Util.getInstance().context.getService(LogService.class.getName());
        
    	log.log(LogService.LOG_INFO, string);
    }
}