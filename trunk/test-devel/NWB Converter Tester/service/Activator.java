package service;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import converter.ConverterLoaderImpl;

public class Activator implements BundleActivator{
	private ConverterLoaderImpl cli;
	public void start(BundleContext b){
		cli = new ConverterLoaderImpl(b);
		System.out.println("Hello!");
		//System.out.println(b.getBundles());
		for(Bundle bn : b.getBundles()){
			
			if(bn.getRegisteredServices() != null){
				System.out.println(bn.getSymbolicName());
				for(ServiceReference ref : bn.getRegisteredServices()){
					System.out.println("\t"+ref);
				}
			}
		}
		
	}
	
	public void stop(BundleContext b){
		System.out.println("Goodbye!");
	}
}
