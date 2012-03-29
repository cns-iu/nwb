package edu.iu.nwb.converter.postscript.gs.ghostscript;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.apache.commons.lang.SystemUtils;
import org.cishell.utilities.process.OutputGobblingProcessRunner;
import org.cishell.utilities.process.ProcessReport;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import edu.iu.nwb.converter.postscript.gs.util.WindowsExecutableDetector;

/**
 * "High-level" or raster output devices available in Ghostscript as listed 
 * <a href="http://pages.cs.wisc.edu/~ghost/doc/AFPL/devices.htm">here</a>.
 */
public enum GhostscriptDevice {
	/* The Ghostscript flag -sDEVICE=<device name> is case-sensitive.
	 * These names must ultimately be used in lower case.
	 */
	// "High-level" file formats
	epswrite("EPS output (like PostScript Distillery)"),
	pdfwrite("PDF output (like Adobe Acrobat Distiller)"),
	pswrite("PostScript output (like PostScript Distillery)"),
	pxlmono("Black-and-white PCL XL"),
	pxlcolor("Color PCL XL"),
	// Other raster file formats and devices
	bit("Plain bits, monochrome"),
	bitrgb("Plain bits, RGB"),
	bitcmyk("Plain bits, CMYK"),
	bmpmono("Monochrome MS Windows .BMP file format"),
	bmp16("4-bit (EGA/VGA) .BMP file format"),
	bmp256("8-bit (256-color) .BMP file format"),
	bmp16m("24-bit .BMP file format"),
	cgmmono("Monochrome (black-and-white) CGM -- LOW LEVEL OUTPUT ONLY"),
	cgm8("8-bit (256-color) CGM -- DITTO"),
	cgm24("24-bit color CGM -- DITTO"),
	jpeg("JPEG format, RGB output"),
	jpeggray("JPEG format, gray output"),
	miff24("ImageMagick MIFF format, 24-bit direct color, RLE compressed"),
	pcxmono("PCX file format, monochrome (1-bit black and white)"),
	pcxgray("PCX file format, 8-bit gray scale"),
	pcx16("PCX file format, 4-bit planar (EGA/VGA) color"),
	pcx256("PCX file format, 8-bit chunky color"),
	pcx24b("PCX file format, 24-bit color (3 8-bit planes)"),
	pcxcmyk("PCX file format, 4-bit chunky CMYK color"),
	pbm("Portable Bitmap (plain format)"),
	pbmraw("Portable Bitmap (raw format)"),
	pgm("Portable Graymap (plain format)"),
	pgmraw("Portable Graymap (raw format)"),
	pgnm("Portable Graymap (plain format), optimizing to PBM if possible"),
	pgnmraw("Portable Graymap (raw format), optimizing to PBM if possible"),
	pnm("Portable Pixmap (plain format) (RGB), optimizing to PGM or PBM if possible"),
	pnmraw("Portable Pixmap (raw format) (RGB), optimizing to PGM or PBM if possible"),
	ppm("Portable Pixmap (plain format) (RGB)"),
	ppmraw("Portable Pixmap (raw format) (RGB)"),
	pkm("Portable inKmap (plain format) (4-bit CMYK => RGB)"),
	pkmraw("Portable inKmap (raw format) (4-bit CMYK => RGB)"),
	plan9bm("Plan 9 bitmap format"),
	pngmono("Monochrome Portable Network Graphics (PNG)"),
	pnggray("8-bit gray Portable Network Graphics (PNG)"),
	png16("4-bit color Portable Network Graphics (PNG)"),
	png256("8-bit color Portable Network Graphics (PNG)"),
	png16m("24-bit color Portable Network Graphics (PNG)",
			ImmutableList.of("-dTextAlphaBits=4", "-dGraphicsAlphaBits=4")),
	psmono("PostScript (Level 1) monochrome image"),
	psgray("PostScript (Level 1) 8-bit gray image"),
	psrgb("PostScript (Level 2) 24-bit color image"),
	tiff12nc("TIFF 12-bit RGB, no compression"),
	tiff24nc("TIFF 24-bit RGB, no compression (NeXT standard format)"),
	tifflzw("TIFF LZW (tag = 5) (monochrome)"),
	tiffpack("TIFF PackBits (tag = 32773) (monochrome)");
	
	public static final ImmutableList<String> SCRIPT_DRIVEN_GHOSTSCRIPT_ARGUMENTS =
			ImmutableList.of("-P-", "-q", "-dSAFER", "-dNOPAUSE", "-dBATCH");
	public static final String UNIX_INTERPRETER_NAME =
			"gs";
	public static final ImmutableCollection<String> CANDIDATE_WINDOWS_INTERPRETER_NAMES =
			ImmutableSet.of("gswin64c.exe", "gswin32c.exe");
	
	private final String description;
	private final ImmutableCollection<String> deviceSpecificFlags;

	GhostscriptDevice(String description) {
		this(description, ImmutableList.<String>of());
	}
	GhostscriptDevice(String description, Collection<String> deviceSpecificFlags) {
		this.description = description;
		this.deviceSpecificFlags = ImmutableList.copyOf(deviceSpecificFlags);
	}

	public String getDescription() {
		return description;
	}
	
	public ImmutableCollection<String> getDeviceSpecificFlags() {
		return deviceSpecificFlags;
	}
	
	/* TODO
	 * Overloads for (1) overriding flags and (2) additional flags.
	 */
	
	/**
	 * Constructs a list of commands (suitable for constructing a {@link ProcessBuilder}) that
	 * will direct a Ghostscript interpreter to convert {@code inputFile} to {@code outputFile}
	 * using this GhostscriptDevice.  Any {@code additionalFlags} are appended directly after the
	 * {@link #deviceSpecificFlags}.
	 */
	public ImmutableList<String> ghostscriptConversionInstructionsFor(
			File inputFile, File outputFile, String... additionalFlags)
					throws GhostscriptException {
		return ImmutableList.copyOf(Iterables.concat(ImmutableList.of(
				platformSpecificExecutableParts(),
				SCRIPT_DRIVEN_GHOSTSCRIPT_ARGUMENTS,
				ImmutableList.of(String.format("-sDEVICE=%s", toString())),
				deviceSpecificFlags,
				Arrays.asList(additionalFlags),
				ImmutableList.of(String.format("-sOutputFile=%s", outputFile.getAbsolutePath())),
				ImmutableList.of("-f",
								 inputFile.getAbsolutePath()))));
	}
	
	/**
	 * Converts {@code inputFile} to a File in this format and saves it at the location named by
	 * {@code outputFile}.
	 * 
	 * @param	additionalFlags	See {@link #ghostscriptConversionInstructionsFor(File, File, String...)}.
	 */
	public ProcessReport convert(File inputFile, File outputFile, String... additionalFlags)
			throws GhostscriptException {
		OutputGobblingProcessRunner processRunner =
				new OutputGobblingProcessRunner(
						new ProcessBuilder(
								ghostscriptConversionInstructionsFor(
										inputFile, outputFile, additionalFlags)),
						Charset.defaultCharset().toString());
		
		try {
			ProcessReport report = processRunner.run();
			System.err.println(report);
			
			if ((!report.isExitNormal()) || outputFile.length() == 0L) {
				throw new GhostscriptException(report.toString());
			}
			
			return report;
		} catch (IOException e) {
			throw new GhostscriptException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new GhostscriptException(e.getMessage(), e);
		}
	}
	
	private static ImmutableList<String> platformSpecificExecutableParts()
			throws GhostscriptException {
		if (!SystemUtils.IS_OS_WINDOWS) {
			return ImmutableList.of(UNIX_INTERPRETER_NAME);
		}
		
		/* On Windows we need to:
		 * (1) Determine whether the GS installation is 64-bit or 32-bit so that we call the
		 * interpreter by the correct name.
		 * (2) Call via "cmd /C" to ensure proper PATH resolution and proper capturing of the
		 * interpreter's exit value. */
		try {
			String executableName =
					WindowsExecutableDetector.findFirstPresentOnSystem(
							CANDIDATE_WINDOWS_INTERPRETER_NAMES, "-version");
			
			return ImmutableList.of("cmd", "/C", executableName);
		} catch (NoSuchElementException e) {
			throw new GhostscriptException(
					"No Ghostscript interpreter detected.  " +
					"Candidate names were: " + CANDIDATE_WINDOWS_INTERPRETER_NAMES);
		}
	}
}
