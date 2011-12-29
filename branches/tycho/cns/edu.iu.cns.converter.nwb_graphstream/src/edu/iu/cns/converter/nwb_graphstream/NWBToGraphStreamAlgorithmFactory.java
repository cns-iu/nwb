package edu.iu.cns.converter.nwb_graphstream;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class NWBToGraphStreamAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inputData = data[0];
    	File nwbFile = (File) inputData.getData();

        return new NWBToGraphStreamAlgorithm(inputData, nwbFile);
    }
}