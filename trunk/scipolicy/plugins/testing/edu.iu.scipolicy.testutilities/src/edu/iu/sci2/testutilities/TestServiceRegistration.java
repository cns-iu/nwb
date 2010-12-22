package edu.iu.sci2.testutilities;

import java.util.Dictionary;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * This class is necessary because TestContext.registerService is called by the
   services themselves, and they expect the result (which is a ServiceRegistration)
   to be non-null.
 **/
public class TestServiceRegistration implements ServiceRegistration {
	public ServiceReference getReference() {
		return null;
	}

	public void setProperties(Dictionary properties) {
	}

	public void unregister() {
	}
}
