package edu.iu.sci2.visualization.scimaps.testing;

import org.cishell.framework.CIShellContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/* A CIShell whose getService always returns a LogService.
 * The LogService simply writes to System.err.
 */
public class LogOnlyCIShellContext implements CIShellContext {
	public Object getService(String service) {
		return new LogService() {
			public void log(int level, String message) {
				System.err.println("[Level " + level + "]: " + message);
			}

			public void log(ServiceReference sr, int level, String message) {
				log(level, message);
			}
			
			public void log(int level, String message, Throwable cause) {
				log(level, message + " from cause " + cause.getMessage());
			}

			public void log(
					ServiceReference sr,
					int level,
					String message,
					Throwable cause) {
				log(level, message, cause);
			}
		};
	}
}