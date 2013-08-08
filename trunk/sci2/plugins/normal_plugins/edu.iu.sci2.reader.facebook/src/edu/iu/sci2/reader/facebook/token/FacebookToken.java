package edu.iu.sci2.reader.facebook.token;

import java.util.Dictionary;

import javax.swing.JOptionPane;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.facebook.facade.Facade;

import prefuse.data.Table;

public class FacebookToken implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    private LogService logger;
    private Facade facade;
    
    public FacebookToken(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
        this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
        facade = new Facade(this.logger);
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	this.logger.log(LogService.LOG_INFO, "-----");
    	this.logger.log(LogService.LOG_INFO, "Facebook Access Token Plugin");
		this.logger
				.log(LogService.LOG_WARNING,
						"The use of the Facebook API is governed by following policies:");
		this.logger
				.log(LogService.LOG_WARNING,
						"This is a Facebook application that helps "
								+ "user export data out of Facebook for reuse in Visualization or any possible method of "
								+ "digital story telling. Data is exported in csv format. ");
		this.logger
				.log(LogService.LOG_WARNING,
						"According to Facebook's Statement of Rights and Responsibility. "
								+ "You own all of the content and information you post on Facebook, and you can control how it is shared through your privacy and application settings.");
		this.logger
				.log(LogService.LOG_INFO, "Please refer the following link:");
		this.logger.log(LogService.LOG_WARNING,
				"https://developers.facebook.com/policy");
		this.logger.log(LogService.LOG_INFO, "-----");
    	
		int confirmMsg = JOptionPane
				.showConfirmDialog(
						null,
						"Please login into your web browser and copy the access token returned to allow Sci2 to access your Facebook infomation \nAccess Token will expire in two hours",
						"Are you ready to login in Web browser?",
						JOptionPane.YES_NO_OPTION);
        Table table = null;
		if (confirmMsg == JOptionPane.YES_OPTION) {
			System.out.println("Inside Confirm");
			table = writeAcessToken();
		}
		
        return generateOutData(table); 
    }
    
    public Table writeAcessToken() {
    	this.logger.log(LogService.LOG_INFO, "Opening Facebook login page");
    	String token = facade.getAccessToken();
    	Table table = new Table();
        table.addColumn("Token", String.class);
        Integer rowNum = table.addRow();
        table.set(rowNum, "Token", token);
    	this.logger.log(LogService.LOG_WARNING, "This token is valid for two hours. Please obtain a new token after this duration");
    	return table;
    }
    
    
    
    private Data[] generateOutData(Table table) {
        /*
        * After getting the output in table format make it available to the user.
        */
       Data output = new BasicData(table, Table.class.getName());
       Dictionary<String, Object> metadata = output.getMetadata();
       metadata.put(DataProperty.LABEL, "FacebookAccessToken");
       metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);        
       return new Data[] { output };
   }    
    
}