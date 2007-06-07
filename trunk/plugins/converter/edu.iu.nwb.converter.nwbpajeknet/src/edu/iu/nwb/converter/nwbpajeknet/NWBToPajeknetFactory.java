package edu.iu.nwb.converter.nwbpajeknet;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

public class NWBToPajeknetFactory implements AlgorithmFactory {

	protected void activate(ComponentContext ctxt) { System.out.println("I'm ACTIVATED!");}
    protected void deactivate(ComponentContext ctxt) { }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new NWBToPajeknet(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }

}
