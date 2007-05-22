package edu.iu.nwb.converter.edgelist;

//Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class EdgeListValidatorFactory implements AlgorithmFactory {
	   
	   protected void activate(ComponentContext ctxt) {}
	   protected void deactivate(ComponentContext ctxt) { }
	
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	    */
	   public MetaTypeProvider createParameters(Data[] dm) {
	        return null;
	   }
	    
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	    */
	   public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	           CIShellContext context) {
		   return new EdgeListValidator(dm, parameters, context);
	   }
	    
	   public class EdgeListValidator implements Algorithm {
	        
	    	Data[] data;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public EdgeListValidator(Data[] dm, Dictionary parameters, CIShellContext context) {
	        	this.data = dm;
	            this.parameters = parameters;
	            this.ciContext = context;
	        }

	        public Data[] execute() {

				String fileHandler = (String) data[0].getData();
				File inData = new File(fileHandler);

				Data[] dm = new Data[] {new BasicData(inData, "file:text/edgelist")};
				dm[0].getMetaData().put(DataProperty.LABEL, "edgelist file: " + fileHandler);
				dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
            	return dm;

	        }
	      

	    }
}






  

    
    
    

