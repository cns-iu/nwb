package org.cishell.gui.prefgui;

import java.util.Dictionary;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

public class CIShellPreferencePage extends FieldEditorPreferencePage {

	private static final String FONT_PREFIX = "_font";
	private static final String DIRECTORY_PREFIX = "_directory";
	private static final String FILE_PREFIX = "_file";
	private static final String PATH_PREFIX = "_path";
	
	private ObjectClassDefinition prefOCD;
	private Dictionary prefValues;
	
    public CIShellPreferencePage(ObjectClassDefinition prefOCD, Dictionary prefValues,
    		CIShellPreferenceStore prefStore) {
    	super(FieldEditorPreferencePage.FLAT);
    	this.setTitle(prefOCD.getName());
    	
    	this.prefOCD = prefOCD;
    	this.prefValues = prefValues;
    	
    	this.setPreferenceStore(prefStore);
	}
    
    private boolean hasPrefix(AttributeDefinition prefAD, String prefix) {
    	return prefAD.getID().startsWith(prefix);
    }
	
	protected void createFieldEditors() {
		
		AttributeDefinition[] prefADs = 
    		prefOCD.getAttributeDefinitions(ObjectClassDefinition.ALL);
    	
    	for (int ii = 0; ii < prefADs.length; ii++) {
    		System.out.println("Creating field " + ii);
    		
    		AttributeDefinition prefAD = prefADs[ii];
    		
    		int attrType = prefAD.getType();
    		
    		if (attrType == AttributeDefinition.BOOLEAN) {
    			
    			BooleanFieldEditor bField = 
    				new BooleanFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(bField);
    		} else if (attrType == AttributeDefinition.INTEGER) {
    			
    			IntegerFieldEditor iField =
    				new IntegerFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(iField);
    		} else if (attrType == AttributeDefinition.STRING &&
    				prefAD.getOptionLabels() != null && prefAD.getOptionLabels().length > 0) {
    			
    			String[] optionLabels = prefAD.getOptionLabels();
    			String[] optionValues = prefAD.getOptionValues();
    			
    			String [][] labelAndValues = new String[optionLabels.length][2];
    			
    			for (int jj = 0; jj < labelAndValues.length; jj++) {
    				labelAndValues[jj][0] = optionLabels[jj];
    				labelAndValues[jj][1] = optionValues[jj];
    			}
    			
    			RadioGroupFieldEditor rgField 
    			= new RadioGroupFieldEditor(
    					prefAD.getID(),
    					prefAD.getName(),
    					1,
    					labelAndValues,
    					getFieldEditorParent(),
    					true);
    			addField(rgField);
    			
    		} else if (attrType == AttributeDefinition.STRING &&
    				hasPrefix(prefAD, FONT_PREFIX)) {
    			
    			FontFieldEditor foField = 
    				new FontFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(foField);
    		} else if (attrType == AttributeDefinition.STRING &&
    				hasPrefix(prefAD, DIRECTORY_PREFIX)) {
    			
    			DirectoryFieldEditor dField = 
    				new DirectoryFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(dField);
    		} else if (attrType == AttributeDefinition.STRING &&
    				hasPrefix(prefAD, FILE_PREFIX)) {
    			
    			FileFieldEditor fiField = 
    				new FileFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(fiField);
    		} else if (attrType == AttributeDefinition.STRING && 
    				hasPrefix(prefAD, PATH_PREFIX)) {
    			
    			PathEditor pField = 
    				new PathEditor(prefAD.getID(), prefAD.getName(), prefAD.getName(), getFieldEditorParent());
    			addField(pField);
    		} else if (attrType == AttributeDefinition.STRING) {
    			
    			StringFieldEditor sField = 
    				new StringFieldEditor(prefAD.getID(), prefAD.getName(), getFieldEditorParent());
    			addField(sField);
    		}
    	}
	}
}
