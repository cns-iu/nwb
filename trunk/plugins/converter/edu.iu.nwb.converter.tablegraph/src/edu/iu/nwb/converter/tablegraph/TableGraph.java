package edu.iu.nwb.converter.tablegraph;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.INIConfiguration;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;

public class TableGraph implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	private DateFormat format;

	public TableGraph(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;

	}

	public Data[] execute() {

		LogService logger = (LogService)context.getService(LogService.class.getName());
		
		INIConfiguration metadata;
		try {
			metadata = new INIConfiguration((String)parameters.get("metadata"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
			logger.log(LogService.LOG_ERROR, "Error in your configuration file.");
			return null;
		}
		Table table = (Table) this.data[0].getData();
		
		
		
		Set sections = metadata.getSections();
		Iterator iter = sections.iterator();
		while(iter.hasNext()) {
			String section = (String) iter.next();
			
			
			
		}
		
		
		Graph graph = null;

		Data graphData = new BasicData(graph, Graph.class.getName());
		
		Dictionary graphMetadata = graphData.getMetaData();
		graphMetadata.put(DataProperty.LABEL, "Multipartite Graph from Table");
		graphMetadata.put(DataProperty.PARENT, this.data[0]);
		graphMetadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		

		return new Data[]{graphData};
	}
}