package edu.iu.nwb.converter.nwbgraphml;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

/* TODO This entire plugin should be scrapped and rewritten
 * using edu.iu.nwb.util.nwbfile utilities.
 */
public class NWBToGraphMLbyStaxFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inData = data[0];
    	File inNWBFile = (File) inData.getData();
    	LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());

        return new NWBToGraphMLbyStax(inNWBFile, logger);
    }
}