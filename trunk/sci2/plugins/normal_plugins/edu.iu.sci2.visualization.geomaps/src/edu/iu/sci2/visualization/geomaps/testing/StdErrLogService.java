package edu.iu.sci2.visualization.geomaps.testing;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public final class StdErrLogService implements LogService {
	/**
	 * @see LogService#LOG_ERROR
	 * @see LogService#LOG_WARNING
	 * @see LogService#LOG_INFO
	 * @see LogService#LOG_DEBUG
	 */
	@Override
	public void log(int level, String message) {
		System.err.println("[Level " + level + "]: " + message);
	}

	@Override
	public void log(ServiceReference sr, int level, String message) {
		log(level, message);
	}

	@Override
	public void log(int level, String message, Throwable cause) {
		log(level, message + " from cause " + cause.getMessage());
	}

	@Override
	public void log(
			ServiceReference sr,
			int level,
			String message,
			Throwable cause) {
		log(level, message, cause);
	}
}