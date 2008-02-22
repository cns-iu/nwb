package edu.iu.nwb.converter.prefuserefer;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class ReferReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public ReferReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	System.out.println("Read");
        return null;
    }
}