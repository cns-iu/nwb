package service;

import org.cishell.service.conversion.Converter;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import converter.ConverterLoaderImpl;

public class Activator implements BundleActivator{
	private ConverterLoaderImpl cli;
	private Converter comparisonConverter;
	private Converter testConverter;
	private ConfigurationFileParser cfp;
	public void start(BundleContext b){
		
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
		this.cli = new ConverterLoaderImpl(b);
		this.cfp = new ConfigurationFileParser();
		try{
		this.comparisonConverter = this.cli.getConverter(this.cfp.getComparisonConverters());
		this.testConverter = this.cli.getConverter(this.cfp.getTestConverters());
		System.out.println(this.comparisonConverter);
		System.out.println(this.testConverter);
		}
		catch(Exception ex){
			System.err.println(ex);
		}
	}
	
	public void stop(BundleContext b){
		System.out.println("Goodbye!");
	}
}
