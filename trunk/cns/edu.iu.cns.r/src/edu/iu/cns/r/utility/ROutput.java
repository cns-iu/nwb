package edu.iu.cns.r.utility;

import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

public class ROutput {
	private String standardOutput = "";
	private String errorOutput = "";

	public ROutput(String standardOutput, String errorOutput) {
		this.standardOutput = standardOutput;
		this.errorOutput = errorOutput;
	}

	public String getStandardOutput() {
		return this.standardOutput;
	}

	public String getErrorOutput() {
		return this.errorOutput;
	}

	public void log(
			LogService logger, boolean shouldLogStandardOutput, boolean shouldLogErrorOutput) {
		if (shouldLogStandardOutput &&
				!StringUtilities.isNull_Empty_OrWhitespace(this.standardOutput)) {
			logger.log(LogService.LOG_INFO, this.standardOutput);
		}

		if (shouldLogErrorOutput &&
				!StringUtilities.isNull_Empty_OrWhitespace(this.errorOutput)) {
			logger.log(LogService.LOG_ERROR, this.errorOutput);
		}
	}
}