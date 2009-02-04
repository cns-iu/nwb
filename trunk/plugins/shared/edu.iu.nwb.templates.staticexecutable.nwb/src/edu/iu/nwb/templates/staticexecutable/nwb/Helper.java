package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.templates.staticexecutable.StaticExecutableAlgorithmFactory;
import org.osgi.service.component.ComponentContext;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class Helper implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	private StaticExecutableAlgorithmFactory staticAlgorithmFactory;

	public Helper(Data[] data, Dictionary parameters, CIShellContext context, ComponentContext componentContext) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.staticAlgorithmFactory = new StaticExecutableAlgorithmFactory((String) componentContext.getProperties().get("Algorithm-Directory"), componentContext.getBundleContext());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		File nwbFile = (File) this.data[0].getData();
		GetMetadataAndCounts handler = new GetMetadataAndCounts();
		String weightAttribute = (String) this.parameters.get("weightAttribute");
		
		boolean ignoreWeightAttribute = HelperFactory.DEFAULT_WEIGHT.equals(weightAttribute);
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			throw new AlgorithmExecutionException("Problem reading the input file.", e1);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Invalid input file.", e);
		}

		int nodeCount = handler.getNodeCount();
		int undirectedEdgeCount = handler.getUndirectedEdgeCount();
		int directedEdgeCount = handler.getDirectedEdgeCount();

		if(undirectedEdgeCount != 0 && directedEdgeCount != 0) {
			throw new AlgorithmExecutionException("This network has both directed and undirected edges. " +
					"That combination is not allowed by this algorithm. " +
			"Consider transforming the network into one that is all directed or all undirected, perhaps with symmetrize.");
		}
		
		if(undirectedEdgeCount == 0 && directedEdgeCount == 0) {
			throw new AlgorithmExecutionException("This network has no edges. " +
					"This algorithm only works on networks with edges.");
		}
		
		try {
			File simpleFormat = File.createTempFile("nwb-", ".simple");
			FileOutputStream simpleOutputStream = new FileOutputStream(simpleFormat);
			NWBSimplifier simplifier = new NWBSimplifier(simpleOutputStream, nodeCount, undirectedEdgeCount + directedEdgeCount, weightAttribute, ignoreWeightAttribute); //one of the edge counts is guaranteed to be zero
			new NWBFileParser(nwbFile).parse(simplifier);
			if(simplifier.hadIssue()) {
				throw new AlgorithmExecutionException(simplifier.getReason());
			}
			
			
			simpleOutputStream.flush();
			simpleOutputStream.close();

			Data simpleData = new BasicData(simpleFormat, null);
			Algorithm realAlgorithm = staticAlgorithmFactory.createAlgorithm(new Data[]{simpleData}, parameters, context);

			List transformedOutput = new ArrayList();
			List forNodes = new ArrayList();
			List forEdges = new ArrayList();
			Data[] output = realAlgorithm.execute(); //let any exceptions bubble up, they're already real exceptions
			//System.err.println("SEA Length is: " + output.length);
			Data firstAttributeData = null;
			for(int ii = 0; ii < output.length; ii++) {
				Data outputData = output[ii];
				String fileName = ((File) outputData.getData()).getName();
				if(fileName.endsWith(".nodes")) {
					forNodes.add(outputData.getData()); //always a file object
					if(firstAttributeData == null) {
						firstAttributeData = outputData;
					}
				} else if(fileName.endsWith(".edges")) {
					forEdges.add(outputData.getData()); //ditto
					if(firstAttributeData == null) {
						firstAttributeData = outputData;
					}
				} else {
					transformedOutput.add(outputData);
				}
			}

			if(firstAttributeData != null) {
				File realFormat = File.createTempFile("nwb-", ".nwb");
				FileOutputStream realOutputStream = new FileOutputStream(realFormat);
				try {
					NWBFileParserHandler integrator = new NWBIntegrator(realOutputStream, forNodes, forEdges);
					new NWBFileParser(nwbFile).parse(integrator);
				} catch(IllegalArgumentException e) {
					//quite awful, but the handler methods can't have throws added
					throw new AlgorithmExecutionException(e.getMessage(), e.getCause());
				}
				realOutputStream.close();
				//make data of realFormat, stick at beginning of transformedOutput, turn into array, and give it back

				Data nwbOutput = new BasicData(firstAttributeData.getMetadata(), realFormat, "file:text/nwb");

				transformedOutput.add(0, nwbOutput);
			}
			return (Data[]) transformedOutput.toArray(new Data[]{});

		} catch(IOException e) {
			throw new AlgorithmExecutionException("Problems processing the input.");
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Invalid input file.", e);
		}

	}
}