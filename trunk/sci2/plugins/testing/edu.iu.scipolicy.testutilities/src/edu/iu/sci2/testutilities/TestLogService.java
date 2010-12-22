package edu.iu.sci2.testutilities;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

// This class is meant to do nothing (for testing).
public class TestLogService implements LogService {
	public void start(BundleContext context) {
	}
	
	public void stop(BundleContext context) {
	}

	public void log(int level, String message) {
		System.out.println(message);
	}

	public void log(int level, String message, Throwable exception) {
		System.out.println(message);
		exception.printStackTrace();
	}

	public void log(ServiceReference sr, int level, String message) {
		System.out.println(message);
	}

	public void log
		(ServiceReference sr, int level, String message, Throwable exception)
	{
		System.out.println(message);
		exception.printStackTrace();
	}
}
