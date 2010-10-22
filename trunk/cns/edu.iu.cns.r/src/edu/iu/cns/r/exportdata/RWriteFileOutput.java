package edu.iu.cns.r.exportdata;

import java.io.File;

import edu.iu.cns.r.utility.ROutput;

public class RWriteFileOutput extends ROutput {
	private File writtenFile;

	public RWriteFileOutput(ROutput output, File writtenFile) {
		this(output.getStandardOutput(), output.getErrorOutput(), writtenFile);
	}

	public RWriteFileOutput(String standardOutput, String errorOutput, File writtenFile) {
		super(standardOutput, errorOutput);

		this.writtenFile = writtenFile;
	}

	public File getWrittenFile() {
		return this.writtenFile;
	}
}