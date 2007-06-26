package tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.Converter;
import org.osgi.framework.BundleContext;

import prefuse.data.Graph;
import service.ConfigurationFileParser;
import tester.graphcomparison.ComparisonResult;
import tester.graphcomparison.DefaultGraphComparer;
import converter.ConverterLoaderImpl;

public class ConverterTester {
	private CIShellContext cContext;
	private ConfigurationFileParser cfp;
	private ConverterLoaderImpl cli;
	private Converter comparisonConverters;
	private DefaultGraphComparer dgc;
	//private Map<String, Exception> fileErrors;
	private static File tempDir;
	private Converter testConverters;
	private Map<String, ComparisonResult> results;
	
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
		results = new HashMap<String, ComparisonResult>();
		setupDirectory();
	}

	public ConverterTester(BundleContext b, CIShellContext c, String configFileName) throws Exception {
		cContext = c;
		cli = new ConverterLoaderImpl(b,cContext);
		cfp = new ConfigurationFileParser(new File(configFileName));
		testConverters = cli.getConverter(cfp.getTestConverters());
		comparisonConverters = cli.getConverter(cfp.getComparisonConverters());
		results = new HashMap<String, ComparisonResult>();
		setupDirectory();
	}
	
	
	private static void setupDirectory(){
		tempDir = new File(System.getProperty("user.home") + File.separator + "converterTemp");
		tempDir.mkdir();
	}
	
	
	private void compareFiles(File sourceFile, File convertedFile){
		System.out.println("Comparing: " + sourceFile.getName() + " and " + convertedFile.getName());
		try{
		dgc = new DefaultGraphComparer();
		results.put(sourceFile.getName() + " " + convertedFile.getName(), dgc.compare((Graph)convertFile(sourceFile,this.comparisonConverters).getData(), (Graph)convertFile(convertedFile,this.comparisonConverters).getData(), cfp.getNodeIDChange()));
		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void compareFiles(){
		for(File f : tempDir.listFiles()){
			compareFiles(f,f);
		}
	}
	
	private Data convertFile(File f, Converter cnv){
		
		try{
			//String s = f.getName();
			//String extension = this.testConverters.getProperties().get(AlgorithmProperty.OUT_DATA).toString();
			System.out.println("Converting " + f.getCanonicalPath());
			Data inData = new BasicData(f.getCanonicalPath(),"");
			Data dm = cnv.convert(inData);


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
	

	public void testFile(File f){
		
		System.out.println("Testing " + f.getName());
		if(!f.isHidden()){
			if(f.isDirectory()){
				for(File ff : f.listFiles())
					testFile(ff);
			}
			else{
				Data dm = convertFile(f,this.testConverters);
				if(dm != null){
					writeAsFile(dm, f.getName());
					compareFiles(f,(File)dm.getData());
				}
			}
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
	
	private void writeAsFile(Data inDM, String fileName){
		String s = fileName.substring(0,fileName.lastIndexOf("."));
		if(inDM != null){
			try{
				copy((File)inDM.getData(), new File(tempDir.getCanonicalPath()+File.separator+"converted"+s+ this.cfp.getExtension()));
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
	}
	
	private boolean copy(File in, File out) {
    	try {
    		FileInputStream  fis = new FileInputStream(in);
    		FileOutputStream fos = new FileOutputStream(out);
    		
    		FileChannel readableChannel = fis.getChannel();
    		FileChannel writableChannel = fos.getChannel();
    		
    		writableChannel.truncate(0);
    		writableChannel.transferFrom(readableChannel, 0, readableChannel.size());
    		fis.close();
    		fos.close();
    		return true;
    	}
    	catch (IOException ioe) {
    		System.out.println("Copy Error: IOException during copy\n" + ioe.getMessage());
            return false;
    	}
    }
	
	public void printResults(){
		for(String s : this.results.keySet()){
			System.out.println(s);
			System.out.println("\t"+this.results.get(s));
		}
	}


}
