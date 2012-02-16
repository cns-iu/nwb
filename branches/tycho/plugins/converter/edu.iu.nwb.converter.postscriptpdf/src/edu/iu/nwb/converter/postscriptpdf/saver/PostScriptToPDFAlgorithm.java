package edu.iu.nwb.converter.postscriptpdf.saver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.utilities.process.OutputGobblingProcessRunner;
import org.cishell.utilities.process.ProcessResult;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import edu.iu.nwb.converter.postscriptpdf.handler.PDFHandlerAlgorithm;

/**
 * Calls an external program to distill the input PostScript file
 * to a Portable Document Format file.
 */
public class PostScriptToPDFAlgorithm implements Algorithm {
	public static final String OUTPUT_DATA_LABEL = "as PDF";
	public static final String TEMPORARY_PDF_FILE_PREFIX = "Distilled-PostScript_";
	public static final String TEMPORARY_PDF_FILE_SUFFIX = ".pdf";
	public static final String TOP_LEVEL_ERROR_MESSAGE =
			"The conversion from PostScript to PDF did not complete normally.  " +
			"This converter requires a Ghostscript installation.  " +
			"Potential problems: " +
			"(1) Ghostscript may not be installed.  " +
			"See [url]http://pages.cs.wisc.edu/~ghost/[/url].  " +
			"(2) Ghostscript may be installed but not included on this " +
			"system's \"path\" environment variable.  " +
			"Ensure that the \"bin\" and \"lib\" subdirectories of your " +
			"Ghostscript installation are on the \"path\".  " +
			"For help setting \"path\" see [url]http://java.com/en/download/help/path.xml[/url].";

	private Data postScriptData;
	
	
	public static class Factory implements AlgorithmFactory {
		@Override
		public Algorithm createAlgorithm(
				Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
			return new PostScriptToPDFAlgorithm(data[0]);
		}
	}
	public PostScriptToPDFAlgorithm(Data postScriptData) {
		this.postScriptData = postScriptData;
	}
	

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		File postScriptFile = (File) postScriptData.getData();

		try {
			File pdfFile = distill(postScriptFile);
			
			return new Data[] { wrapAsData(pdfFile, postScriptData) };
		} catch (DistillationException e) {
			throw new AlgorithmExecutionException(
					TOP_LEVEL_ERROR_MESSAGE,
					new ConversionException(e));
		}
	}

	/**
	 * Uses an external program to convert the given PostScript file to a PDF file.
	 */
	public static File distill(File postScriptFile)	throws DistillationException {
		try {
			File pdfFile =
					createTemporaryFile(TEMPORARY_PDF_FILE_PREFIX, TEMPORARY_PDF_FILE_SUFFIX);
			
			ImmutableList<String> distillInstructions =
					createDistillInstructions(postScriptFile, pdfFile);

			ProcessResult result = runDistiller(distillInstructions);

			// Check the result
			if ((!result.isExitNormal()) || (pdfFile.length() == 0L)) {
				throw new DistillationException(createDetailedReport(distillInstructions, result));
			}
			
			return pdfFile;
		} catch (IOException e) {
			throw new DistillationException("Input/output problem running distiller.", e);
		} catch (InterruptedException e) {
			throw new DistillationException("Distiller interrupted.", e);
		}
	}


	private static File createTemporaryFile(String prefix, String suffix) throws IOException {
		File pdfFile = File.createTempFile(prefix, suffix);
		pdfFile.deleteOnExit();
		
		return pdfFile;
	}


	private static ImmutableList<String> createDistillInstructions(
			File postScriptFile, File pdfFile) {
		if (SystemUtils.IS_OS_WINDOWS) {
			/* There exist analogous batch scripts to ps2pdf in the Windows GhostScript
			 * distribution, but calling them (at least with "cmd /C" prepended so that %Path%
			 * is considered) appears to discard the core exit value from GS.
			 */
			return ImmutableList.of(
					"cmd", "/C", "gswin32c.exe", "-P-", "-dSAFER", "-q", "-P-", "-dNOPAUSE",
					"-dBATCH", "-sDEVICE=pdfwrite", "-sstdout=%stderr",
					String.format("-sOutputFile=%s", pdfFile.getAbsolutePath()),
					"-P-", "-dSAFER", "-c", ".setpdfwrite",
					"-f", postScriptFile.getAbsolutePath());
		} else {			
			return ImmutableList.of(
					"ps2pdf",
					postScriptFile.getAbsolutePath(),
					pdfFile.getAbsolutePath());
		}
	}


	private static ProcessResult runDistiller(List<String> instructions)
				throws IOException, InterruptedException {
		sendDebugMessage(
			String.format(
					"Running distiller program.  The instructions are %s.",
					instructions));
		
		ProcessResult result = runProcess(instructions);
		
		sendDebugMessage("Finished running distiller program.");				
		sendDebugMessage(result.report());
		
		return result;
	}


	private static ProcessResult runProcess(List<String> instructions)
			throws IOException,	InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(instructions);
		
		OutputGobblingProcessRunner runner =
				new OutputGobblingProcessRunner(
						processBuilder, Charset.defaultCharset().toString());
		ProcessResult result = runner.run();
		
		return result;
	}


	private static String createDetailedReport(
			ImmutableList<String> processInstructions, ProcessResult result) {
		return Joiner.on("  ").join(
				ImmutableList.of(
						"Problem running the distiller.",
						String.format(
								"The program and arguments are %s.",
								processInstructions.toString()),
						result.report()));
	}

	private static void sendDebugMessage(String message) {
		System.err.println(message);
	}
	
	private static Data wrapAsData(File pdfFile, Data parent) {
		Data pdfFileData = new BasicData(pdfFile, PDFHandlerAlgorithm.PDF_MIME_TYPE);
	
		Dictionary<String, Object> metadata = pdfFileData.getMetadata();
		metadata.put(DataProperty.LABEL, OUTPUT_DATA_LABEL);
		metadata.put(DataProperty.PARENT, parent);
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
	
		return pdfFileData;
	}


	@Override
    public String toString() {
		return Objects.toStringHelper(this)
				.add("postScriptData", postScriptData)
				.toString();
    }
}