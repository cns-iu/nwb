package edu.iu.cns.database.extract.generic;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import edu.iu.cns.database.extract.generic.service.ExtractionService;

public class Activator implements BundleActivator {

	//hold on to our service registration so we can unregister when this plugin stops.
	private ServiceRegistration extractionServiceRegistration;
	
	public void start(BundleContext context) throws Exception {
		//Allow the database service to be found by other services/plugins
		this.extractionServiceRegistration = context.registerService(
				ExtractionService.class.getName(), new ExtractionService(), new Hashtable());
	}

	public void stop(BundleContext context) throws Exception {
		//disallow the database service to be found by other services/plugins
		this.extractionServiceRegistration.unregister();
	}

}