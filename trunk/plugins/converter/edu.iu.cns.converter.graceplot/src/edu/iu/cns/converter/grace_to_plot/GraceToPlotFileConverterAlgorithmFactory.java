package edu.iu.cns.converter.grace_to_plot;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class GraceToPlotFileConverterAlgorithmFactory
		implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext ciShellContext) {
        return new GraceToPlotFileConverterAlgorithm(
        	data, parameters, ciShellContext);
    }
}