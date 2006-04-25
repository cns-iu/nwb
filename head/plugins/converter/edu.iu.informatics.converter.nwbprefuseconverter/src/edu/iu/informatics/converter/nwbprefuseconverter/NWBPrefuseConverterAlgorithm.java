/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.converter.nwbprefuseconverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.iu.iv.common.property.PropertyMap;
import edu.iu.iv.core.IVC;
import edu.iu.iv.core.UnsupportedModelException;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.BasicDataModel;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelProperty;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.persisters.standard.PrefusexGMMLGraphPersister;
import edu.iu.nwb.core.model.Edge;
import edu.iu.nwb.core.model.NWBModel;
import edu.iu.nwb.core.model.Node;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author Team NWB
 */
public class NWBPrefuseConverterAlgorithm extends AbstractAlgorithm {    
    private static final String ALGORITHM_NAME = "NWB -> Prefuse Converter";
    private DataModel dm;
    
    /**
     * Creates a new NWBPrefuseConverterAlgorithm.
     * 
     * @param dm The datamodel to convert
     */
	public NWBPrefuseConverterAlgorithm(DataModel dm) {
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    
	    this.dm = dm;
	}
	
	/**
	 * Executes this NWBPrefuseConverterAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute() {
		NWBModel nwbModel = (NWBModel) dm.getData();

		//Create the temporary file directory
		String temp = IVC.getInstance().getTemporaryFilesFolder();
		File tempFile;
		try {
			tempFile = File.createTempFile("graph-ml-", ".xml", new File(temp));
			writeGraphMl(nwbModel, tempFile);

			try {
				//Create the datamodel using the Prefuse Persister
				PrefusexGMMLGraphPersister persister = new PrefusexGMMLGraphPersister();
		        
				Object model = persister.restore(new BasicFileResourceDescriptor(tempFile));
				String label = tempFile.getName();
				DataModelType type = DataModelType.NETWORK;

				DataModel dataModel = new BasicDataModel(model);
				PropertyMap propMap = dataModel.getProperties();
				propMap.put(DataModelProperty.LABEL, label);
				propMap.put(DataModelProperty.TYPE, type);
				dataModel.getProperties().setPropertyValue(DataModelProperty.PARENT, this.dm);
				
				try {
					//Add the datamodel to IVC
					IVC.getInstance().addModel(dataModel);
				} catch (UnsupportedModelException e) {
					e.printStackTrace();
				}

			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return true;
	}

	/**
	 * Write out graphML format given an NWB model
	 * @param nwbModel The model to write
	 * @param tempFile The temporary file to write to
	 */
	private void writeGraphMl(NWBModel nwbModel, File tempFile) {
		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
			
			//Write the header
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">");
			out.println("<graph edgedefault=\"undirected\">");
			
			//Write out the nodes
			Iterator nodeIter = nwbModel.getNodes();			
			while (nodeIter.hasNext()) {
				Node bnc = (Node)nodeIter.next();
				Object numAttr = bnc.getPropertyValue(Node.ID);
				out.println("<node id=\"" + numAttr.toString() + "\" label=\""+bnc.toString()+"\"> </node>");			
//				out.println("<node id=\"" + numAttr.toString() + "\"></node>");
			}
			
			//Write out the undirected edges
			Iterator edgeIter = nwbModel.getUndirectedEdges();
			while (edgeIter.hasNext()) {
				Edge bec = (Edge)edgeIter.next();
				
				
				out.println("<edge source=\"" + bec.getPropertyValue(Edge.ORIGIN)  + 
						    "\" target=\"" + bec.getPropertyValue(Edge.DEST) + "\">" +
						    "</edge>");
			}
			
			out.println("</graph>");
			out.println("</graphml>");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}