package edu.iu.nwb.converter.text;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class TextHandlerAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary<String, Object> parameters,
    								 CIShellContext ciShellContext) {
    	File textFile = (File) data[0].getData();
    	
        return new TextHandlerAlgorithm(textFile);
    }
}