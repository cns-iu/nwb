package edu.iu.nwb.composite.extractcowordfromtable;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

public class ExtractCoWordNetworkAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;
	private AlgorithmFactory extractDirectedNetwork;
	private DataConversionService converter;
	private AlgorithmFactory bibliographicCoupling;

	public ExtractCoWordNetworkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
			AlgorithmFactory extractDirectedNetwork, DataConversionService converter,
			AlgorithmFactory bibliographicCoupling) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
		this.extractDirectedNetwork = extractDirectedNetwork;
		this.converter = converter;
		this.bibliographicCoupling = bibliographicCoupling;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			System.out.println("Starting coword");
			//turn scientometrics data table into prefuse Graph of directed network
			Algorithm extractAlg = extractDirectedNetwork.createAlgorithm(data, parameters, context);
			Data[] directedNetworkData = extractAlg.execute();
			System.out.println("Done extracting directed");
			//converter prefuse Graph of directed network into nwb graph file of directed network
			Data convertedNetworkData = converter.convert(directedNetworkData[0], "file:text/nwb");
			System.out.println("Done converting");
			//turn nwb graph file of directed network into undirected similarity network
			Algorithm bibCouplingAlg = bibliographicCoupling.createAlgorithm(new Data[] { convertedNetworkData },
					new Hashtable(), context);
			Data[] coWordNetworkData = bibCouplingAlg.execute();
			System.out.println("Done with coupling");
			System.out.println("coWord length:" + coWordNetworkData.length);
			//return undirected similarity network
			return coWordNetworkData;
		} catch (ConversionException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}