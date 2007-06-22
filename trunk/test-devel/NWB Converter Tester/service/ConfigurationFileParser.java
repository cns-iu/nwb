package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

import tester.ConfigurationFileConstants;

public class ConfigurationFileParser {
	private Queue<File> comparisonFiles;
	private Queue<String> comparisonConverters;
	private Queue<String> testConverters;
	private Boolean nodeIDChange = true;
	
	private Boolean processFileList  = false;
	private Boolean processComparisonConvertersList = false;
	private Boolean processTestConvertersList = false;
	
	
	public ConfigurationFileParser(){
		comparisonConverters = new LinkedList<String>();
		testConverters = new LinkedList<String>();
		nodeIDChange = true;
	}
	
	public ConfigurationFileParser(File f) throws Exception{
		comparisonConverters = new LinkedList<String>();
		testConverters = new LinkedList<String>();
		nodeIDChange = true;
		this.parseFile(f);
	}
	
	public void parseFile(File f) throws Exception{
		String line = null;
		BufferedReader reader = 
			new BufferedReader(new FileReader(f));
		
		while((line = reader.readLine().toLowerCase()) != null){
			if(line.startsWith(ConfigurationFileConstants.TEST_GRAPHS)){
				line.replace(ConfigurationFileConstants.TEST_GRAPHS, "");
				this.processFileList = true;
				this.processComparisonConvertersList = false;
				this.processTestConvertersList = false;	
			}
			if(line.startsWith(ConfigurationFileConstants.COMPARISON_CONVERTERS)){
				line.replace(ConfigurationFileConstants.COMPARISON_CONVERTERS, "");
				this.processFileList = false;
				this.processComparisonConvertersList = true;
				this.processTestConvertersList = false;
			}
			if(line.startsWith(ConfigurationFileConstants.TEST_CONVERTERS)){
				line.replace(ConfigurationFileConstants.TEST_CONVERTERS, "");
				this.processFileList = false;
				this.processComparisonConvertersList = false;
				this.processTestConvertersList = true;
			}
			if(line.startsWith(ConfigurationFileConstants.NODE_ID_CHANGE)){
				line.replace(ConfigurationFileConstants.NODE_ID_CHANGE, "");
				this.nodeIDChange = new Boolean(line).booleanValue();
			}
			if(this.processFileList){
				this.processFiles(this.processLine(line));
			}
			if(this.processComparisonConvertersList){
				this.processComparisonConverters(this.processLine(line));
			}
			if(this.processTestConvertersList){
				this.processTestConverters(this.processLine(line));
			}
			
		}
	}
	
	private String[] processLine(String s){
		String[] line = s.split(",");
		for(String ss : line){
			ss.trim();
		}
		return line;
	}
	
	private void processFiles(String...strings) throws Exception{
		for(String s : strings){
			File f = new File(s);
			this.comparisonFiles.add(f);
		}
	}
	
	private void processTestConverters(String...strings){
		for(String s : strings){
			this.testConverters.add(s);
		}
	}
	
	private void processComparisonConverters(String...strings){
		for(String s : strings){
			this.comparisonConverters.add(s);
		}
	}
	
	public String[] getFiles(){
		return this.comparisonFiles.toArray(new String[0]);
	}
	
	public String[] getComparisonConverters(){
		return this.comparisonConverters.toArray(new String[0]);
	}
	
	public String[] getTestConverters(){
		return this.testConverters.toArray(new String[0]);
	}
	
	public Boolean getNodeIDChange(){
		return this.nodeIDChange;
	}
	
}

	
