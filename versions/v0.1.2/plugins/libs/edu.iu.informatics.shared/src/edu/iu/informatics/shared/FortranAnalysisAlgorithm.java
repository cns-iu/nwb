package edu.iu.informatics.shared;

import java.util.ArrayList;
import java.util.List;

import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.nwb.core.model.NWBModel;

/**
 * Network Analysis programs that are implemented in Fortran share
 * this data
 * 
 * @author Team NWB
 *
 */
public abstract class FortranAnalysisAlgorithm extends FortranAlgorithm {
	
	/**
	 * Create a Fortran Analysis Algorithm
	 * @param dm The datamodel to run analysis on
	 */
	public FortranAnalysisAlgorithm(DataModel dm) {
		super(dm);
	}

	/**
	 * Set the default parameters common for all fortran analysis algorithms
	 */
	protected void setDefaultParameters() {
	    NWBModel   nwbModel = (NWBModel)dm.getData();
	    long       numNodes = nwbModel.getNumNodes();
	    long       numEdges = nwbModel.getNumDirectedEdges() + nwbModel.getNumUndirectedEdges();

        parameterMap.putTextOption("nodes",
        		                  "Number of Nodes:",
        		                  "Enter the number of nodes in the network",
        		                  ""+numNodes,
        		                  null);
        parameterMap.putTextOption("edges",
        		                  "Number of Edges:","Enter the number of edges in the network",
        		                  ""+numEdges,
        		                  null);
        
	}

	/**
	 * Get the default parameters for this algorithm
	 * @return List of default parameters
	 */
	protected List getDefaultParameters() {
		ArrayList list = new ArrayList();
		
	    NWBModel   nwbModel = (NWBModel)dm.getData();
	    long       numNodes = nwbModel.getNumNodes();
	    long       numEdges = nwbModel.getNumDirectedEdges() + nwbModel.getNumUndirectedEdges();
		
	    list.add(new Long(numNodes));
	    list.add(new Long(numEdges));
	    
	    return list;
	}
}