package service;

import java.io.File;

import org.cishell.service.conversion.Converter;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import converter.ConverterLoaderImpl;

public class Activator implements BundleActivator{
	private ConverterLoaderImpl cli;
	private ConfigurationFileParser cfp;
	private Converter testConverters;
	private Converter comparisonConverters;
	
	public void start(BundleContext b){
		cli = new ConverterLoaderImpl(b);
		cfp = new ConfigurationFileParser();
		System.out.println("Hello!");
		//System.out.println(b.getBundles());
		/*for(Bundle bn : b.getBundles()){
			
			if(bn.getRegisteredServices() != null){
				System.out.println(bn.getSymbolicName());
				for(ServiceReference ref : bn.getRegisteredServices()){
					System.out.println("\t"+ref);
				}
			}
		}*/
		try{
		cfp.parseFile(new File("/home/kelleyt/workspace/edu.iu.nwb.converter.tester/test_files/test.cfg"));
		System.out.println(cfp);
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
		System.out.println(testConverters + "\n" + comparisonConverters);
		}catch(Exception ex){
			System.err.println(ex);
		}
		
	}
	
	public void stop(BundleContext b){
		System.out.println("Goodbye!");
	}
}
