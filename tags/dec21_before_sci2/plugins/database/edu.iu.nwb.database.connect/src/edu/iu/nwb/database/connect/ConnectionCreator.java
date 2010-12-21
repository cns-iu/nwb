package edu.iu.nwb.database.connect;

import java.sql.SQLException;
import java.util.Dictionary;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class ConnectionCreator implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
	private LogService logger;
    
    public ConnectionCreator(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger=(LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	String driver = (String) parameters.get("driver");
    	String url = (String) parameters.get("url");
    	String username = (String) parameters.get("username");
    	String password = (String) parameters.get("password");
    	
    	//DatabaseDataSource prefuseConnection;
    	javax.sql.DataSource dataSource;
    	
    	try {
    		Class.forName(driver);
			//Driver jdbcDriver = (Driver) Class.forName(driver).newInstance(); //we can use this later to check acceptsUrl for better error reporting
    		
    		org.apache.commons.dbcp.ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, username, password);
    		
			GenericObjectPool connectionPool = new GenericObjectPool();
			
			KeyedObjectPoolFactory stmtPool = new GenericKeyedObjectPoolFactory(null);
			
			new PoolableConnectionFactory(connectionFactory, connectionPool, stmtPool, null, false, true);
			
			dataSource = new PoolingDataSource(connectionPool);
			
			try {
				dataSource.getConnection().close();
			} catch (SQLException e) {
				logger.log(LogService.LOG_WARNING, "Problem opening test connection.", e);
			}
			
			//prefuseConnection = prefuse.data.io.sql.ConnectionFactory.getDatabaseConnection(connection);
			
			
			
		} catch (ClassNotFoundException e) {
			throw new AlgorithmExecutionException("Database driver not found: " + driver, e);
		}
		
    	
    	/* DatabaseDataSource connection;
		try {
			connection = prefuse.data.io.sql.ConnectionFactory.getDatabaseConnection(driver, url, username, password);
		} catch (SQLException e) {
			logger.log(LogService.LOG_ERROR, "Problem setting up database connection", e);
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Database driver not found: " + driver, e);
			e.printStackTrace();
			return null;
		} */
    	
    	Data data = new BasicData(dataSource, DataSource.class.getName());
    	Dictionary metadata = data.getMetadata();
        metadata.put(DataProperty.LABEL, "SQL DataSource to " + url);
        //metadata.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE); //really this is wrong
    	
    	return new Data[] { data };
    }
    
}