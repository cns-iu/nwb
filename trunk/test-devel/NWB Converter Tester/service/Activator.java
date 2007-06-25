package service;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tester.ConverterTester;

public class Activator implements BundleActivator{
	private ConverterTester ct;
	
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
		try{
			ct = new ConverterTester(b,"/home/kelleyt/workspace/edu.iu.nwb.converter.tester/test_files/test.cfg");
			System.out.println(ct);
		}catch(Exception ex){
			System.err.println(ex);
		}
		
	}
	
	public void stop(BundleContext b){
		System.out.println("Goodbye!");
	}
}
