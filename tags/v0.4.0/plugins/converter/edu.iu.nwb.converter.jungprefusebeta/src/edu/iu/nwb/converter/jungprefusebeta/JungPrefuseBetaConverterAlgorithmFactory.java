package edu.iu.nwb.converter.jungprefusebeta;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;


public class JungPrefuseBetaConverterAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JungPrefuseBetaConverterAlgorithm(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
}