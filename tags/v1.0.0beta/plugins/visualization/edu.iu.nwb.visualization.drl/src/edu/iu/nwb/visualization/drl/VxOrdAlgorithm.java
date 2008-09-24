package edu.iu.nwb.visualization.drl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.templates.staticexecutable.StaticExecutableAlgorithmFactory;
import org.osgi.framework.BundleContext;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class VxOrdAlgorithm implements Algorithm {
    private static StaticExecutableAlgorithmFactory staticAlgorithmFactory;
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public VxOrdAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, BundleContext bContext) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        if (staticAlgorithmFactory == null) {
        	staticAlgorithmFactory = new StaticExecutableAlgorithmFactory("vxord", bContext);
        }
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		String weightAttr = (String)parameters.get("edgeWeight");
    		String xposAttr = (String)parameters.get("xpos");
    		String yposAttr = (String)parameters.get("ypos");
    		
    		File tmpSimFile = File.createTempFile("nwb-", ".int");
			File inputNWBFile = (File)data[0].getData();
			File outputNWBFile = File.createTempFile("nwb-", ".nwb");
			
			//create the SIM File
			NWBFileParser parser = new NWBFileParser(inputNWBFile);
			parser.parse(new NWBToSimFileHandler(weightAttr,new FileOutputStream(tmpSimFile)));
			Data simData = new BasicData(tmpSimFile,"file:text/intsim");
			
			//Run DRL (VxOrd) on SIM File
			Algorithm layoutAlg = staticAlgorithmFactory.createAlgorithm(new Data[]{simData}, parameters, context);
			Data[] coordData;
			try {
				coordData = layoutAlg.execute();
			} catch (AlgorithmExecutionException e) {
				throw new AlgorithmExecutionException("Unable to execute the DrL layout algorithm.", e);
			}
			File coordFile = (File) coordData[0].getData();
			
			//Create a new NWB file w/ injected layout coordinates
			if (coordData != null) {
				NWBCoordMerger merger = new NWBCoordMerger(coordFile,inputNWBFile,xposAttr,yposAttr,outputNWBFile);
				merger.merge();
			} else {
				throw new ParsingException("Layout Failed!");
			}
			
			//If all has gone well, return the new nwb file
			Data outNWBData = new BasicData(outputNWBFile,"file:text/nwb");
			outNWBData.getMetadata().put(DataProperty.LABEL, "Laid Out Network");
			outNWBData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			outNWBData.getMetadata().put(DataProperty.PARENT, data[0]);
			return new Data[]{outNWBData};
			
		} catch (IOException e) {
			throw new AlgorithmExecutionException("There was a problem parsing the input data.", e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("There was a problem parsing the input data.", e);
		}
    }
}