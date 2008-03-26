package edu.iu.nwb.database.bib;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;

import javax.sql.DataSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;
import org.python.core.PyFile;
import org.python.core.PyJavaInstance;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class IsiDatabaseReader implements Algorithm {

	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
	private URL script;
	private LogService logger ;

	public IsiDatabaseReader(Data[] data, Dictionary parameters, CIShellContext context, URL script) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.script = script;
		logger = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() {
		
		PythonInterpreter interp = new PythonInterpreter();
		
		File file = (File) data[0].getData();
		DataSource dataSource = (DataSource) data[1].getData();
		
		Connection connection;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.log(LogService.LOG_ERROR, "Problem opening a database connection", e1);
			e1.printStackTrace();
			return null;
		}
		
		logger.log(LogService.LOG_INFO, "Initial setup complete");
		
		try {
			//Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); //do a little setup for the python script
			interp.set("data", new PyFile(file.toURL().openStream())); //send the isi file to the python script
			interp.set("connection", new PyJavaInstance(connection));
			interp.set("logger", new PyJavaInstance(logger));
			logger.log(LogService.LOG_INFO, "Params passed");
			interp.execfile(script.openStream());
			logger.log(LogService.LOG_INFO, "File executed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(LogService.LOG_ERROR, "Problem reading jython script. Notify the application author.", e);
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				// we're already closed, so who cares
				//e1.printStackTrace();
			}
			return null;
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			//we're already closed, so who cares
		}
		
		//PyObject database = interp.get("database");
		
		//Connection output = (Connection) database.__tojava__(Connection.class);
		
		//Data outputData = new BasicData(output, Connection.class.getName());
		
		//Dictionary metadata = outputData.getMetadata();
        //metadata.put(DataProperty.LABEL, "Derby Database with ISI data from " + file.getName());
        //metadata.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE); //really this is wrong
    	
    	//return new Data[] { outputData };
		return null;
	}

}
