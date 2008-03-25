package edu.iu.nwb.converter.pajekmatpajeknet;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

public class PajekmatToPajeknetFactory implements AlgorithmFactory{

	protected void activate(ComponentContext ctxt) { }
    protected void deactivate(ComponentContext ctxt) { }
	
	
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		// TODO Auto-generated method stub
		return new PajekmatToPajeknet(data,parameters,context);
	}

	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}

}
