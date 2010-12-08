package edu.iu.cns.inspectdata;

import java.io.File;
import java.util.Dictionary;
import java.util.Enumeration;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class DataInspectorAlgorithm implements Algorithm {
    private Data data;
    private LogService logger;
    
    public DataInspectorAlgorithm(Data data, LogService logger) {
        this.data = data;
        this.logger = logger;
    }

    public Data[] execute() {
    	this.logger.log(
    		LogService.LOG_INFO,
    		String.format("Data item '%s' (%s):", this.data.toString(), this.data.getFormat()));
    	Object dataContent = this.data.getData();

    	if (dataContent instanceof File) {
    		File dataFile = (File) dataContent;
    		this.logger.log(
    			LogService.LOG_INFO,
    			String.format("\tIs a file (absolute path: '%s')", dataFile.getAbsolutePath()));
    	}

    	Dictionary<String, Object> metadata = this.data.getMetadata();
    	Enumeration<String> keys = metadata.keys();

    	while (keys.hasMoreElements()) {
    		String key = keys.nextElement();
    		this.logger.log(
    			LogService.LOG_INFO,
    			String.format("\t(Metadata) '%s' = '%s'", key, metadata.get(key).toString()));
    	}

        return null;
    }
}