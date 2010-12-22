package edu.iu.sci2.database.star.gui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.utility.StarDatabaseDataValidator;

public class StarDatabaseGUIAlgorithmFactory implements AlgorithmFactory {
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService)componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data parentData = data[0];
    	StarDatabaseDataValidator.validateData(data[0], this.logger);
    	DatabaseService databaseService =
    			(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());

        return new StarDatabaseGUIAlgorithm(parentData, this.logger, databaseService);
    }
}