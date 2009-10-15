package edu.iu.cns.converter.plot_to_csv;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class PlotToCSVConverterAlgorithmFactory implements AlgorithmFactory {	
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext ciShellContext) {
        return new PlotToCSVConverterAlgorithm(
        	data, parameters, ciShellContext);
    }
}