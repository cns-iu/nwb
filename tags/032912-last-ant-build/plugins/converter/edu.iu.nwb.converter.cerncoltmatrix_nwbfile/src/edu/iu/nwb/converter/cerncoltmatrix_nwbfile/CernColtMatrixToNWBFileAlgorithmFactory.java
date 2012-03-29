package edu.iu.nwb.converter.cerncoltmatrix_nwbfile;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class CernColtMatrixToNWBFileAlgorithmFactory
		implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
        return new CernColtMatrixToNWBFileAlgorithm(data, parameters, context);
    }
}