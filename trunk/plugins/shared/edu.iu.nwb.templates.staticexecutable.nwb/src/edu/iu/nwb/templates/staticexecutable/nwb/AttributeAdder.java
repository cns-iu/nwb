package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class AttributeAdder {

	private AttributeExtractor[] attributeExtractors;
	private Map schemaUpdate = new HashMap();
	private String[] attributeNames;

	public AttributeAdder(List attributeFiles, String suffix) throws FileNotFoundException {
		
		int numberOfExtractors = attributeFiles.size();
		this.attributeExtractors = new AttributeExtractor[numberOfExtractors];
		this.attributeNames = new String[numberOfExtractors];
		
		for(int ii = 0; ii < numberOfExtractors; ii++) {
			File attributeFile = (File) attributeFiles.get(ii);
			String attributeFileName = attributeFile.getName();
			String attributeName = attributeFileName.substring(0, attributeFileName.length() - suffix.length());
			this.attributeNames[ii] = attributeName;
			this.attributeExtractors[ii] = new AttributeExtractor(attributeFile);
			schemaUpdate.put(attributeName, NWBFileProperty.TYPE_FLOAT);
		}
	}

	public Map addNext(Map attributes) {
		Map updatedAttributes = new HashMap(attributes);
		for(int ii = 0; ii < this.attributeExtractors.length; ii++) {
			updatedAttributes.put(this.attributeNames[ii], this.attributeExtractors[ii].nextValue());
		}
		return updatedAttributes;
	}

	public LinkedHashMap updateSchema(LinkedHashMap schema) {
		LinkedHashMap updatedMap = new LinkedHashMap(schema);
		updatedMap.putAll(schemaUpdate);
		return updatedMap;
	}

}
