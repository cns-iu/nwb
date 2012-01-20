package edu.iu.sci2.visualization.geomaps.testing;

import org.cishell.framework.CIShellContext;

/* A CIShell whose getService always returns a LogService.
 * The LogService simply writes to System.err.
 */
public class LogOnlyCIShellContext implements CIShellContext {
	@Override
	public Object getService(String service) {
		return new StdErrLogService();
	}
}
