package org.cishell.gui.prefgui;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.service.prefadmin.PreferenceAD;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.osgi.service.cm.Configuration;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

public class CIShellPreferenceStore implements IPersistentPreferenceStore {

	private LogService log;
	
	private PreferenceOCD prefOCD; //could be useful if we implement some new features
	private Configuration prefConf;
	private Dictionary prefDict;
	private Map prefDefaults;
	//private Map prefTypes;
	
	private boolean needsSaving = false;
	
	public CIShellPreferenceStore(LogService log, PreferenceOCD prefOCD, Configuration prefConf) {
		this.log = log;
		
		this.prefOCD = prefOCD;
		this.prefConf = prefConf;
		this.prefDict = prefConf.getProperties();
		
		generatePrefDefaultsAndTypes(prefOCD);
	}
	
	public boolean contains(String name) {
		return prefDict.get(name) != null;
	}

	public boolean getBoolean(String name) {
		return Boolean.parseBoolean((String) this.prefDict.get(name));
	}

	public boolean getDefaultBoolean(String name) {
		return Boolean.valueOf(((String) this.prefDefaults.get(name))).booleanValue();
	}

	public double getDefaultDouble(String name) {
		return Double.valueOf(((String) this.prefDefaults.get(name))).doubleValue();
	}

	public float getDefaultFloat(String name) {
		return Float.valueOf(((String) this.prefDefaults.get(name))).floatValue();
	}

	public int getDefaultInt(String name) {
		return Integer.valueOf(((String) this.prefDefaults.get(name))).intValue();
	}

	public long getDefaultLong(String name) {
		return Long.valueOf(((String) this.prefDefaults.get(name))).longValue();
	}

	public String getDefaultString(String name) {
		return ((String) this.prefDefaults.get(name));
	}

	public double getDouble(String name) {
		return Double.parseDouble(((String) this.prefDict.get(name)));
	}

	public float getFloat(String name) {
		return Float.parseFloat(((String) this.prefDict.get(name)));
	}

	public int getInt(String name) {
		return Integer.parseInt(((String) this.prefDict.get(name)));
	}

	public long getLong(String name) {
		return Long.parseLong(((String) this.prefDict.get(name)));
	}

	public String getString(String name) {
		
		Object result = this.prefDict.get(name);
		if (result == null) {
		} else {
		}
		return (String) this.prefDict.get(name);
	}

	public boolean isDefault(String name) {
		return prefDefaults.get(name).equals(prefDict.get(name));
	}

	public boolean needsSaving() {
		return this.needsSaving;
	}

	public void putValue(String name, String value) {
		this.prefDict.put(name, value);
	}


	public void setToDefault(String name) {
		this.needsSaving = true;	
		
		String defaultVal = (String) this.prefDefaults.get(name);
		this.prefDict.put(name, defaultVal);
	}
	
	public void setValue(String name, double value) {
		this.needsSaving = true;		
		this.prefDict.put(name, String.valueOf(value));

	}

	public void setValue(String name, float value) {
		this.needsSaving = true;
		
		this.prefDict.put(name, String.valueOf(value));

	}

	public void setValue(String name, int value) {
		this.needsSaving = true;
		
		this.prefDict.put(name, String.valueOf(value));

	}

	public void setValue(String name, long value) {
		this.needsSaving = true;

		this.prefDict.put(name,String.valueOf(value));
	}

	public void setValue(String name, String value) {
		this.needsSaving = true;
		
		this.prefDict.put(name, value);
	}

	public void setValue(String name, boolean value) {
		this.needsSaving = true;

		this.prefDict.put(name, String.valueOf(value));
	}

	public void save() throws IOException {
		this.needsSaving = false;
		
		this.prefConf.update(this.prefDict);
	}
	
	private void generatePrefDefaultsAndTypes(PreferenceOCD prefOCD) {

		
		PreferenceAD[] prefADs = prefOCD.getPreferenceAttributeDefinitions(ObjectClassDefinition.ALL);
		
		Map prefDefaults = new HashMap(prefADs.length);
		Map prefTypes = new HashMap(prefADs.length);
		
		for (int ii = 0; ii < prefADs.length; ii++) {
			PreferenceAD prefAD = prefADs[ii];
			System.out.println("SETTING UP PREFERENCE DEFAULTS");
			
			prefDefaults.put(prefAD.getID(), prefAD.getDefaultValue()[0]);
		}
		
		this.prefDefaults = prefDefaults;
	}
	
	//We don't set defaults like this

	public void setDefault(String name, double value) {
	}

	public void setDefault(String name, float value) {
	}

	public void setDefault(String name, int value) {
	}

	public void setDefault(String name, long value) {
	}

	public void setDefault(String name, String defaultObject) {
	}

	public void setDefault(String name, boolean value) {
	}
	
	//more unsupported methods

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
	}
	
	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
	}
	
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
	}

	

}
