package edu.iu.nwb.database.connect;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;


public class ConnectionFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new Connection(data, parameters, context);
    }
}