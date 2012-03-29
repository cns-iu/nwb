package edu.iu.cns.r.exportdata;

import java.io.File;

import edu.iu.cns.r.utility.RStreamLog;

public class RFileExportLog extends RStreamLog {
	private File writtenFile;

	public RFileExportLog(RStreamLog output, File writtenFile) {
		this(output.getStandardOutput(), output.getErrorOutput(), writtenFile);
	}

	public RFileExportLog(String standardOutput, String errorOutput, File writtenFile) {
		super(standardOutput, errorOutput);

		this.writtenFile = writtenFile;
	}

	public File getWrittenFile() {
		return this.writtenFile;
	}
}