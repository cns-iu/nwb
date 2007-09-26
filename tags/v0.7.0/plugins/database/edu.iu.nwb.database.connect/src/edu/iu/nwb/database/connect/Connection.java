package edu.iu.nwb.database.connect;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.DatabaseDataSource;

public class Connection implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
	private LogService logger;
    
    public Connection(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger=(LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	
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
				// TODO Auto-generated catch block
				logger.log(LogService.LOG_ERROR, "Problem opening test connection.", e);
				e.printStackTrace();
			}
			
			//prefuseConnection = prefuse.data.io.sql.ConnectionFactory.getDatabaseConnection(connection);
			
			
			
		} catch (ClassNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Database driver not found: " + driver, e);
			e.printStackTrace();
			return null;
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
    	Dictionary metadata = data.getMetaData();
        metadata.put(DataProperty.LABEL, "SQL DataSource to " + url);
        //metadata.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE); //really this is wrong
    	
    	return new Data[] { data };
    }
    
}