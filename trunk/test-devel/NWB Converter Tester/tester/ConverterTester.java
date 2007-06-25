package tester;

import java.io.File;

import org.cishell.service.conversion.Converter;
import org.osgi.framework.BundleContext;

import service.ConfigurationFileParser;
import converter.ConverterLoaderImpl;
import tester.graphcomparison.*;

public class ConverterTester {
	private ConverterLoaderImpl cli;
	private ConfigurationFileParser cfp;
	private Converter testConverters;
	private Converter comparisonConverters;
	private DefaultGraphComparer dgc;
	
	
	public ConverterTester(BundleContext b){
		cli = new ConverterLoaderImpl(b);
		cfp = new ConfigurationFileParser();
		
	}
	
	public ConverterTester(BundleContext b, File configFile) throws Exception{
		cli = new ConverterLoaderImpl(b);
		cfp = new ConfigurationFileParser(configFile);
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
	}
	
	public ConverterTester(BundleContext b, String configFileName) throws Exception {
		cli = new ConverterLoaderImpl(b);
		cfp = new ConfigurationFileParser(new File(configFileName));
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
	}
	
	public String toString(){
		String output = "";
		output += cfp.toString();
		output += testConverters.toString();
		output += comparisonConverters.toString();
		return output;
	}
	
	
}
