package edu.iu.cns.inspectdata;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.column.Column;

public class DataInspectorAlgorithm implements Algorithm {
    private Data data;
    private LogService logger;
    
    public DataInspectorAlgorithm(Data data, LogService logger) {
        this.data = data;
        this.logger = logger;
    }

    public Data[] execute() {
    	Object dataContent = this.data.getData();
    	this.logger.log(
    		LogService.LOG_INFO,
    		String.format(
    			"Data item '%s' (%s)(%s):",
    			this.data.toString(),
    			this.data.getFormat(),
    			dataContent.getClass().getName()));

    	if (dataContent instanceof File) {
    		File dataFile = (File) dataContent;
    		this.logger.log(
    			LogService.LOG_INFO,
    			String.format("\tIs a file (absolute path: '%s')", dataFile.getAbsolutePath()));
    	} else if (dataContent instanceof Table) {
    		Table dataTable = (Table) dataContent;
    		printTable(dataTable, "Table");
    	} else if (dataContent instanceof Graph) {
    		Graph dataGraph = (Graph) dataContent;
    		Table nodeTable = dataGraph.getNodeTable();
    		Table edgeTable = dataGraph.getEdgeTable();
    		printTable(nodeTable, "Node");
    		printTable(edgeTable, "Edge");
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

    private void printTable(Table table, String tableType) {
		List<String> schema = new ArrayList<String>();

		for (int ii = 0; ii < table.getSchema().getColumnCount(); ii++) {
			String name = table.getSchema().getColumnName(ii);
			String type = table.getColumn(ii).getClass().getName();
			String schemaEntry = String.format("%s: %s", name, type);
			schema.add(schemaEntry);
		}

		String logMessage =
			String.format("%s Schema: %s", tableType, StringUtilities.implodeItems(schema, ", "));
		this.logger.log(LogService.LOG_INFO, logMessage);
    }
}