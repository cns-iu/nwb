package edu.iu.sci2.testutilities;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.reference.service.database.DerbyDatabaseService;
import org.cishell.service.database.DatabaseService;
import org.eclipse.osgi.framework.internal.core.BundleContextImpl;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

/**
 * This class exists to mock a valid CIShellContext.
 * The paradigm for "hosting" a service here is to explicitly store it as a private
   member, initializing it in the constructor, starting/stopping it if necessary in
   start and stop, and explicitly checking for its pid in the one version of
   getService that actually has a body.
 * This class should only ever be used for testing, hence why it is in
   edu.iu.sci2.testutilities.
 **/
public class TestContext extends BundleContextImpl implements CIShellContext {
	private Object logService;
	private Object databaseService;
	
	public TestContext() throws BundleException {
		super(new BundleHost(null, null));
		
		// Initialize services here.
		this.logService = new TestLogService();
		this.databaseService = new DerbyDatabaseService();
	}
	
	public void start() {
		// Start services here (if necessary).
		startService(this.logService);
		startService(this.databaseService);
	}
	
	public void stop() {
		// Stop services here (if necessary).
		stopService(this.logService);
		stopService(this.databaseService);
	}

	public void addBundleListener(BundleListener listener) {
	}

	public void addFrameworkListener(FrameworkListener listener) {
	}

	public void addServiceListener(ServiceListener listener) {
	}

	public void addServiceListener(ServiceListener listener, String filter)
		throws InvalidSyntaxException
	{
	}

	public Filter createFilter(String filter) throws InvalidSyntaxException {
		return null;
	}

	public ServiceReference[] getAllServiceReferences(String clazz, String filter)
		throws InvalidSyntaxException
	{
		throw new RuntimeException();
	}

	public Bundle getBundle() {
		throw new RuntimeException();
	}

	public Bundle getBundle(long id) {
		throw new RuntimeException();
	}

	public Bundle[] getBundles() {
		throw new RuntimeException();
	}

	public File getDataFile(String filename) {
		throw new RuntimeException();
	}

	public String getProperty(String key) {
		throw new RuntimeException();
	}

	public Object getService(ServiceReference reference) {
		throw new RuntimeException();
	}

	public ServiceReference getServiceReference(String clazz) {
		throw new RuntimeException();
	}

	public ServiceReference[] getServiceReferences(String clazz, String filter)
		throws InvalidSyntaxException
	{
		throw new RuntimeException();
	}

	public Bundle installBundle(String location) throws BundleException {
		throw new RuntimeException();
	}

	public Bundle installBundle(String location, InputStream input)
		throws BundleException
	{
		throw new RuntimeException();
	}

	public ServiceRegistration registerService(String[] clazzes,
											   Object service,
											   Dictionary properties)
	{
		throw new RuntimeException();
	}

	public ServiceRegistration registerService(String clazz,
											   Object service,
											   Dictionary properties)
	{
		return new TestServiceRegistration();
	}

	public void removeBundleListener(BundleListener listener) {
	}

	public void removeFrameworkListener(FrameworkListener listener) {
	}

	public void removeServiceListener(ServiceListener listener) {
	}

	public boolean ungetService(ServiceReference reference) {
		return false;
	}

	public Object getService(String serviceName) {
		// Explicitly check for the specific pids and return the appropriate
		// service (objects) here.
		if (serviceName.equals(LogService.class.getName()))
			return this.logService;
		else if (serviceName.equals(DatabaseService.class.getName()))
			return this.databaseService;
		
		throw new RuntimeException();
	}

	private void startService(Object service) {
		Method startMethod = getServiceStartMethod(service);
		
		startMethod.setAccessible(true);
		
		try {
			startMethod.invoke(service, this);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private void stopService(Object service) {
		Method stopMethod = getServiceStopMethod(service);
		
		stopMethod.setAccessible(true);
		
		try {
			stopMethod.invoke(service, this);
		}
		catch (IllegalAccessException e) {
		}
		catch (InvocationTargetException e) {
		}
	}
	
	private Method getServiceMethod(Object service, String methodName) {
		Class serviceClass = service.getClass();
		Method[] serviceMethods = serviceClass.getDeclaredMethods();
		
		for (Method method : serviceMethods) {
			if (method.getName().equals(methodName))
				return method;
		}
		
		throw new RuntimeException();
	}
	
	private Method getServiceStartMethod(Object service) {
		return getServiceMethod(service, "start");
	}
	
	private Method getServiceStopMethod(Object service) {
		return getServiceMethod(service, "stop");
	}
}
