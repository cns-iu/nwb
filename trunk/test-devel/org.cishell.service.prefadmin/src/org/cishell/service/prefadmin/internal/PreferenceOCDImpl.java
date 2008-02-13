package org.cishell.service.prefadmin.internal;

import java.io.IOException;
import java.io.InputStream;

import org.cishell.service.prefadmin.PreferenceAD;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PreferenceOCDImpl implements ObjectClassDefinition, PreferenceOCD {
	
	private ObjectClassDefinition realOCD;
	private PreferenceAD[] wrappedADs;
	
	private LogService log;
	
	public PreferenceOCDImpl(LogService log, ObjectClassDefinition realOCD) {
		this.log = log;
		
		this.realOCD = realOCD;
		
		//TODO: don't always return all attributeDefinitions, regardless of filter
		this.wrappedADs = wrapAttributeDefinitions(realOCD.getAttributeDefinitions(ObjectClassDefinition.ALL));
	}
	
	private PreferenceAD[] wrapAttributeDefinitions(AttributeDefinition[] realAttributeDefinitions) {
		PreferenceAD[] wrappedADs = new PreferenceAD[realAttributeDefinitions.length];
		
		for (int i = 0; i < realAttributeDefinitions.length; i++) {
			AttributeDefinition realAD = realAttributeDefinitions[i];
			PreferenceAD wrappedAD = new PreferenceADImpl(this.log, realAD);
			
			wrappedADs[i] = wrappedAD;
		}
		
		return wrappedADs;
	}


	//use in standard way
	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getAttributeDefinitions(int)
	 */
	public AttributeDefinition[] getAttributeDefinitions(int filter) {
		return this.realOCD.getAttributeDefinitions(filter);
	}
	
	//use to get at the special preference attribute goodness.
	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getPreferenceAttributeDefinitions(int)
	 */
	public PreferenceAD[] getPreferenceAttributeDefinitions(int filter) {
		return this.wrappedADs;
	}

	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getDescription()
	 */
	public String getDescription() {
		return this.realOCD.getDescription();
	}

	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getID()
	 */
	public String getID() {
		return this.realOCD.getID();
	}

	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getIcon(int)
	 */
	public InputStream getIcon(int size) throws IOException {
		return this.realOCD.getIcon(size);
	}

	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceObjectClassDefinition#getName()
	 */
	public String getName() {
		return this.realOCD.getName();
	}

}
