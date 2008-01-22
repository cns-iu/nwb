package org.cishell.testalgorithm4;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class TestAlgorithm4 implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public TestAlgorithm4(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
        return null;
    }
}