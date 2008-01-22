package org.cishell.gui.prefgui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.cishell.service.prefadmin.PrefAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;


public class PrefenceGuiAlgorithmFactory implements AlgorithmFactory {
	
	private LogService log;
	private MetaTypeService mts;
	private PrefAdmin prefAdmin;
	
    protected void activate(ComponentContext ctxt) {
    	System.out.println("PREF GUI ACTIVATION BEGIN!");
    	this.log = (LogService) ctxt.locateService("LOG");
        this.mts = (MetaTypeService)ctxt.locateService("MTS");
        this.prefAdmin = (PrefAdmin) ctxt.locateService("PREF_ADMIN");
    	System.out.println("PREF GUI ACTIVATION END!");
    }
    
    protected void deactivate(ComponentContext ctxt) {
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefenceGuiAlgorithm(data, parameters, context, prefAdmin);
    }
    
    public MetaTypeProvider createParameters(Data[] data) {
    	return null;
    }
}