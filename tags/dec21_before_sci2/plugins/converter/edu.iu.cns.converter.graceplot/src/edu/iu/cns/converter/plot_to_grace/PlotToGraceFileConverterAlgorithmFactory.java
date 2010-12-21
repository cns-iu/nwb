package edu.iu.cns.converter.plot_to_grace;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class PlotToGraceFileConverterAlgorithmFactory
		implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext ciShellContext) {
        return new PlotToGraceFileConverterAlgorithm(
        	data, parameters, ciShellContext);
    }
}