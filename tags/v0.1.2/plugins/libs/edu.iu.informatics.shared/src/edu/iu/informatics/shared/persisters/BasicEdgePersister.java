package edu.iu.informatics.shared.persisters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.iu.informatics.shared.FileBackedEdgeModel;
import edu.iu.iv.common.property.PropertyMap;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.core.persistence.Persister;
import edu.iu.iv.core.persistence.PersisterProperty;
import edu.iu.iv.core.persistence.ResourceDescriptor;
import edu.iu.nwb.core.model.Edge;
import edu.iu.nwb.core.model.NWBModel;

public class BasicEdgePersister implements Persister {
	
	private PropertyMap propertyMap ;
	
	private int         numNodes;
	
	public BasicEdgePersister () {
		this(-1);
	}
	
	public BasicEdgePersister (int numNodes) {
		propertyMap = new PropertyMap() ;
		propertyMap.put(PersisterProperty.PERSISTER_NAME, "Informatics Edge NWB Filetype Persister") ;
		propertyMap.put(PersisterProperty.RESTORABLE_MODEL_DESCRIPTION, "Very Basic Edge File format") ;
		propertyMap.put(PersisterProperty.SUPPORTED_FILE_EXTENSION, "") ;
		propertyMap.put(PersisterProperty.FORMAT_NAME, "Basic Edge") ;
		propertyMap.put(PersisterProperty.FORMAT_DESCRIPTION, "List of source and target nodes per edge") ;
		propertyMap.put(PersisterProperty.RESTORABLE_MODEL_TYPE, DataModelType.NETWORK);
		
//		System.out.println("Using BasicEdgePersister with parameters passed to constructor");
		this.numNodes = numNodes;
	}
	
	public void persist(Object model, ResourceDescriptor resource)
			throws IOException, PersistenceException {
		FileResourceDescriptor frd = (FileResourceDescriptor)resource ;

		save((NWBModel)model, frd.getFilePath()) ;
	}

	public Object restore(ResourceDescriptor resource) throws IOException,
			OutOfMemoryError, PersistenceException {
		return new FileBackedEdgeModel((FileResourceDescriptor)resource, numNodes);
	}

	public boolean canPersist(Object model) {
		// check if its an instance of a NWB model
		return model instanceof NWBModel;
		
		
	}

	public boolean canRestore(ResourceDescriptor resource) {
		/*
		if (resource instanceof FileResourceDescriptor) {
			FileResourceDescriptor frd = (FileResourceDescriptor)resource ;
			if (frd.getFileExtension().equals("") && !frd.isCompressionEnabled())
				return true ;
		}
		*/
		return false;
	}

	public PropertyMap getProperties() {
		return propertyMap;
	}

	/**
	 * Save the NWB model to network file format
	 * 
	 * @param model
	 * @param fileOut
	 */
	private void save(NWBModel model, String fileOut) {
		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));

			Iterator   iterator   = model.getDirectedEdges();
			
			while (iterator.hasNext()) {
				PropertyMap propMap = (PropertyMap)iterator.next();
				String origin  = (String)propMap.getPropertyValue(Edge.ORIGIN);
				String dest    = (String)propMap.getPropertyValue(Edge.DEST);
				//System.out.println(origin + " " + dest);
				out.println(origin + " " + dest);
			}
			
			iterator   = model.getUndirectedEdges();
			
			while (iterator.hasNext()) {
				PropertyMap propMap = (PropertyMap)iterator.next();
				String origin  = (String)propMap.getPropertyValue(Edge.ORIGIN);
				String dest    = (String)propMap.getPropertyValue(Edge.DEST);
				//System.out.println(origin + " " + dest);
				out.println(origin + " " + dest);
			}

			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
