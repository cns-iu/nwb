package edu.iu.nwb.common.persisters;

import java.io.IOException;

import edu.iu.iv.common.property.PropertyMap;
import edu.iu.iv.core.IVC;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.core.persistence.Persister;
import edu.iu.iv.core.persistence.PersisterProperty;
import edu.iu.iv.core.persistence.ResourceDescriptor;
import edu.iu.nwb.core.io.NWBFile;
import edu.iu.nwb.core.model.NWBModel;

public class NWBModelPersister implements Persister {
	
	private PropertyMap propertyMap ;
	
	public NWBModelPersister () {
		propertyMap = new PropertyMap() ;
		propertyMap.put(PersisterProperty.PERSISTER_NAME, "NWB Filetype Persister") ;
		propertyMap.put(PersisterProperty.RESTORABLE_MODEL_DESCRIPTION, "NWB File format") ;
		propertyMap.put(PersisterProperty.SUPPORTED_FILE_EXTENSION, ".nwb") ;
		propertyMap.put(PersisterProperty.FORMAT_NAME, "NWB") ;
		propertyMap.put(PersisterProperty.FORMAT_DESCRIPTION, "File format for NWB datatypes") ;
		propertyMap.put(PersisterProperty.RESTORABLE_MODEL_TYPE, DataModelType.NETWORK);
	}

	public void persist(Object model, ResourceDescriptor resource)
			throws IOException, PersistenceException {

		FileResourceDescriptor frd = (FileResourceDescriptor)resource ;

        IVC.getInstance().getConsole().print("" + frd.getFilePath() + "," + model.getClass());
		(new NWBFile()).save((NWBModel)model, frd.getFilePath()) ;
	}

	public Object restore(ResourceDescriptor resource) throws IOException,
			OutOfMemoryError, PersistenceException {
		FileResourceDescriptor frd = (FileResourceDescriptor)resource ;
		return (new NWBFile()).load(frd.getFilePath()) ;		
	}

	public boolean canPersist(Object model) {
	    // check if its an instance of a NWB model
		return model instanceof NWBModel ;
	}

	public boolean canRestore(ResourceDescriptor resource) {
		if (resource instanceof FileResourceDescriptor) {
			FileResourceDescriptor frd = (FileResourceDescriptor)resource ;
			if (frd.getFileExtension().equals(".nwb") && !frd.isCompressionEnabled())
				return true ;
		}
		return false;
	}

	public PropertyMap getProperties() {
		return propertyMap;
	}

}
