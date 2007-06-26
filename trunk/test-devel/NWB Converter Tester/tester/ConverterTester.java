package tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.Converter;
import org.osgi.framework.BundleContext;

import prefuse.data.Graph;
import service.ConfigurationFileParser;
import tester.graphcomparison.DefaultGraphComparer;
import converter.ConverterLoaderImpl;

public class ConverterTester {
	private CIShellContext cContext;
	private ConfigurationFileParser cfp;
	private ConverterLoaderImpl cli;
	private Converter comparisonConverters;
	private DefaultGraphComparer dgc;
	private Map<String, Exception> fileErrors;
	private static File tempDir;
	private Converter testConverters;

	public ConverterTester(BundleContext b, CIShellContext c){
		this.cContext = c;
		cli = new ConverterLoaderImpl(b, this.cContext);
		cfp = new ConfigurationFileParser();

	}

	public ConverterTester(BundleContext b, CIShellContext c, File configFile) throws Exception{
		cContext = c;
		cli = new ConverterLoaderImpl(b, cContext);
		cfp = new ConfigurationFileParser(configFile);
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
		//setupDirectory();
	}

	public ConverterTester(BundleContext b, CIShellContext c, String configFileName) throws Exception {
		cContext = c;
		cli = new ConverterLoaderImpl(b,cContext);
		cfp = new ConfigurationFileParser(new File(configFileName));
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
		//setupDirectory();
	}
	
	
	private void compareFiles(File sourceFile, File convertedFile){
		try{
		Data sourceData = new BasicData(sourceFile.getCanonicalPath(),"");
		Data convertedData = new BasicData(convertedFile.getCanonicalPath(),"");
		dgc = new DefaultGraphComparer();
		dgc.compare((Graph)comparisonConverters.convert(sourceData), (Graph)comparisonConverters.convert(convertedData), cfp.getNodeIDChange());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private Data convertFile(File f){
		
		try{
			//String s = f.getName();
			//String extension = this.testConverters.getProperties().get(AlgorithmProperty.OUT_DATA).toString();
			System.out.println("Converting " + f.getCanonicalPath());
			Data inData = new BasicData(f.getCanonicalPath(),"");
			Data dm = this.testConverters.convert(inData);


			if(dm != null){
				System.out.println("Successfully Converted ");
				return dm;
			}
			return null;
		}
		catch(Exception ex){
			System.out.println("Could not Convert");
			//this.fileErrors.put(s, ex);
			//ex.printStackTrace();
			return null;
		}
	}
	

	public String[] getErrors(){
		String[] output = new String[this.fileErrors.size()];
		int i = 0;
		for(String s : this.fileErrors.keySet()){
			String ss = s + ":\n";
			for(StackTraceElement ste : this.fileErrors.get(s).getStackTrace()){
				ss += "\t" + ste + "\n";
			}
			output[i] = ss;
			i++;
		}
		return output;
	}


	public String printErrors(){
		String output = "";
		for(String s : this.getErrors()){
			output += s + "\n";
		}
		return output;
	}

	public void testFile(File f){
		
		System.out.println("Testing " + f.getName());
		if(!f.isHidden()){
			if(f.isDirectory()){
				for(File ff : f.listFiles())
					testFile(ff);
			}
			else
				convertFile(f);
		}

	}

	public void testFiles(){
		System.out.println(this.cfp.getFiles().length);
		
		for(File f : this.cfp.getFiles()){
			
			this.testFile(f);
		}
	}

	public String toString(){
		String output = "";
		output += cfp.toString();
		output += testConverters.toString();
		output += comparisonConverters.toString();
		return output;
	}
	
	private void writeAsFile(Data inDM) throws FileNotFoundException, IOException, Exception{
		if(inDM != null){
			try{
				
			}catch(Exception ex){
				ex.printStackTrace();
				throw ex;
			}
			
		}
	}


}
