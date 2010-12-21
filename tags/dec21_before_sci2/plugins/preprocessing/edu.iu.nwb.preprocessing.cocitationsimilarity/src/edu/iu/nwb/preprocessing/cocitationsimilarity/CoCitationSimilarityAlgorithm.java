package edu.iu.nwb.preprocessing.cocitationsimilarity;

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

public class CoCitationSimilarityAlgorithm implements Algorithm {
    private static StaticExecutableAlgorithmFactory staticAlgorithmFactory;
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public CoCitationSimilarityAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, BundleContext bContext) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        if (staticAlgorithmFactory == null) {
        	staticAlgorithmFactory = new StaticExecutableAlgorithmFactory("cocitation", bContext);
        }
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		//File tmpEdgeFile = File.createTempFile("nwb-", ".edge");
			File inputNWBFile = (File)data[0].getData();
			File outputNWBFile = File.createTempFile("CoCitationSimilarity-", ".nwb");
			
			//create the network File
			
			//EdgeCounter counter = new EdgeCounter();
			//new NWBFileParser(inputNWBFile).parse(counter);
			//int edges = 
			
			
			NWBFileParser parser = new NWBFileParser(inputNWBFile);
			CocitationComputation computation = new CocitationComputation();
			parser.parse(computation);
			
			parser = new NWBFileParser(inputNWBFile);
			parser.parse(new Merger(computation, outputNWBFile));
			//Data simData = new BasicData(tmpEdgeFile,"file:text/edge");
			
			//Run cocitation on network File
			//Algorithm coCitationAlg = staticAlgorithmFactory.createAlgorithm(new Data[]{simData}, parameters, context);
			//Data[] edgeData = coCitationAlg.execute();
			//File edgeFile = (File) edgeData[0].getData();
			
			//Create a new NWB file w/ new similarity edges
			//if (edgeData != null) {
			//	NWBEdgeMerger merger = new NWBEdgeMerger(edgeFile,inputNWBFile,outputNWBFile);
			//	merger.merge();
			//} else {
			//	throw new ParsingException("Co-Citation Failed!");
			//}
			
			//If all has gone well, return the new nwb file
			Data outNWBData = new BasicData(outputNWBFile,"file:text/nwb");
			outNWBData.getMetadata().put(DataProperty.LABEL, "Bibliographic Coupling Similarity Network");
			outNWBData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			outNWBData.getMetadata().put(DataProperty.PARENT, data[0]);
			return new Data[]{outNWBData};
			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(),e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(e.getMessage(),e);
		}
    }
}