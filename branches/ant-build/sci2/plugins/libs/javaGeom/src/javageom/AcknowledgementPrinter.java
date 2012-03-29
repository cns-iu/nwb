package javageom;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;


public class AcknowledgementPrinter implements BundleActivator {

	@Override
	public void start(BundleContext bc) throws Exception {
		ServiceReference<LogService> ref = (ServiceReference<LogService>) bc.getServiceReference(LogService.class.getName());
		if (ref != null) {
			LogService log = bc.getService(ref);
			log.log(LogService.LOG_INFO, "Using the JavaGeom library, under the terms of the GNU LGPL");
			log.log(LogService.LOG_INFO, "JavaGeom: http://geom-java.sourceforge.net/index.html");
		}
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
