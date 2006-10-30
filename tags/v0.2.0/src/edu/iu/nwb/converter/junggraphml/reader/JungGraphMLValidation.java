package edu.iu.nwb.converter.junggraphml.reader;

import java.util.Dictionary;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.framework.data.BasicData;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.uci.ics.jung.io.GraphMLFile; 

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungGraphMLValidation implements AlgorithmFactory {

    protected void activate(ComponentContext ctxt) {
    }
    protected void deactivate(ComponentContext ctxt) {}

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JungGraphMLValidationAlg(data, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
    	  return null;
    }

    public class JungGraphMLValidationAlg implements Algorithm {
	    private Data[] data;
	    
	    public JungGraphMLValidationAlg(Data[] data, CIShellContext context) {
	        this.data = data;
	    }

		public Data[] execute() {
			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
			try {
				(new GraphMLFile()).load(new FileReader(inData));
				Data[] dm = new Data[] {new BasicData(inData, "file:text/graphml+xml")};
				dm[0].getMetaData().put(DataProperty.LABEL, "Jung GraphML file: " + fileHandler);
                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
                
				return dm;
			}catch (FileNotFoundException exception){
				return null;
			}catch (Exception e){
				return null;	
			}
			
			
		}
    }
}