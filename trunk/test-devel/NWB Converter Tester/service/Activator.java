package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tester.ConverterTester;

public class Activator implements BundleActivator{
	private ConverterTester ct;
	private BundleContext b;
	private CIShellContext c;
	private File configFile;

	public void start(BundleContext b){
		this.b = b;
		this.c = new LocalCIShellContext(b);
		//System.out.println("Hello!");
		//System.out.println(b.getBundles());
		/*for(Bundle bn : b.getBundles()){

			if(bn.getRegisteredServices() != null){
				System.out.println(bn.getSymbolicName());
				for(ServiceReference ref : bn.getRegisteredServices()){
					System.out.println("\t"+ref);
				}
			}
		}*/
		while(configFile == null){
		startUp();
		try{
		processConfigurationFile(configFile);
		configFile = null;
		ct = null;
		}
		catch(FileNotFoundException ex){
			System.out.println("Could not find the specified file");
			configFile = null;
		}
		}

	}

	public void stop(BundleContext b){
		System.out.println("Goodbye!");
		b = null;
		c = null;
		configFile = null;
		ct = null;
	}

	private void processConfigurationFile(File f) throws FileNotFoundException {
		System.out.println("Processing " + f.getName());
		if(!f.isHidden() && (f.getName().charAt(0) != '.')){
			if(f.isDirectory()){
				for(File ff : f.listFiles())
					processConfigurationFile(ff);
			}
			else{
				try{
					ct = new ConverterTester(b, c, f);
					System.out.println(ct);
					ct.testFiles();
					ct.printResults();
				}catch(Exception ex){
					System.out.println("Failed to create ConverterTester\n\n");
					ex.printStackTrace();
				}
			}
		}
	}

	public boolean startUp(){
		Scanner in = new Scanner(System.in);
		while(this.configFile == null){
			System.out.println("Welcome to NWB's Converter Tester\n"+
					"Please enter the name of a configuration file \n"+ 
			"or a directory of configuration files (Q/q to quit): ");
			String s = in.nextLine();
			if(s.trim().equalsIgnoreCase("Q"))
				return false;
			try{
				configFile = new File(s);
			}
			catch (NullPointerException ex){
				System.out.println("Invalid file name");
				configFile = null;
			}


		}

		return true;
	}

}

