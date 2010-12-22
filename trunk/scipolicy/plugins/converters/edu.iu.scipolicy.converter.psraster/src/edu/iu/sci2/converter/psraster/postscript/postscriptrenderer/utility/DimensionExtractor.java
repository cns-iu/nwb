package edu.iu.sci2.converter.psraster.postscript.postscriptrenderer.utility;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.log.LogService;

public class DimensionExtractor {
	
	private static Dimension DEFAULT_IMAGE_DIMENSIONS = new Dimension(800, 600);
	private static int CHECK_X_LINES_FOR_BOUNDING_BOX_BEFORE_GIVING_UP = 40;
	private static Pattern boundingBoxLinePattern = createBoundingBoxLineRegexPattern();
	
	/*
	 * Retrieve the image dimensions from the PostScript file's bounding box line,
	 * or in case of any problems, return the default image dimensions.
	 */
	public static Dimension determineImageDimensions(File postScriptFile, LogService logger) {
    	try {
		
			/*
			 * look through the PostScript file until...
			 * we find the bounding box line, or
			 * we've checked enough lines to give up, or
			 * the file ends.
			 */
    		
    		BufferedReader postScriptReader = 
    			new BufferedReader(new FileReader(postScriptFile));
    			
    		String postScriptLine = null;
			int numLinesChecked = 0;
			while (true) {
				postScriptLine = postScriptReader.readLine();
				
				if (postScriptLine == null) {
					throwExceptionForNoBoundingBoxLineInFile();
				}
				
				//if the current PostScript file line matches the bounding box pattern...
				//TODO: (There may possibly be a more efficient way to do this)
				Matcher lineMatcher = boundingBoxLinePattern.matcher(postScriptLine);
				if (lineMatcher.matches()) {
					
					//WE'VE FOUND THE BOUNDING BOX LINE!
					String boundingBoxLine = postScriptLine;
					
					Dimension boundingBoxDimensions = 
						extractDimensions(lineMatcher, boundingBoxLine);
					
					//SUCCESS!
					return boundingBoxDimensions;
				}
				
				numLinesChecked++;
				if (numLinesChecked > CHECK_X_LINES_FOR_BOUNDING_BOX_BEFORE_GIVING_UP) {
					throwExceptionForBoundingBoxLineNotInHeader();
				}
			}
				
		//Error handling: Print a warning message, and return the default dimension
			
    	} catch (DimensionDeterminingException e) {
    		logger.log(LogService.LOG_WARNING, e.getMessage(), e);
    		logThatWeArereUsingTheDefaultDimensions(logger);
    		return DEFAULT_IMAGE_DIMENSIONS;
    	} catch (FileNotFoundException e) {
    		String message = "Could not find the PostScript file " + 
    			"'" + postScriptFile.getAbsolutePath() + "'" + 
    			" in order to determine the PostScript image's dimensions.";
    		logger.log(LogService.LOG_WARNING, message, e);
    		logThatWeArereUsingTheDefaultDimensions(logger);
    		return DEFAULT_IMAGE_DIMENSIONS;
		} catch (IOException e) {
			String message = "Encountered a problem while reading the PostScript file " + 
			"'" + postScriptFile.getAbsolutePath() + "'" + 
			" in order to determine the PostScript image's dimensions.";
			logger.log(LogService.LOG_WARNING, message, e);
			logThatWeArereUsingTheDefaultDimensions(logger);
			return DEFAULT_IMAGE_DIMENSIONS;
		} catch (NumberFormatException e) {
			String message = 
				"Could not parse the image dimension information for the PostScript file " +
				"'" + postScriptFile.getAbsolutePath() + "'."; 
			logger.log(LogService.LOG_WARNING, message, e);
			logThatWeArereUsingTheDefaultDimensions(logger);
			return DEFAULT_IMAGE_DIMENSIONS;
		} catch (Exception e) {
			String message = 
				"Encountered an unexpected exception while looking for image dimension " +
				"information inside the PostScript file " +
				"'" + postScriptFile.getAbsolutePath() + "'."; 
			logger.log(LogService.LOG_WARNING, message, e);
			logThatWeArereUsingTheDefaultDimensions(logger);
			return DEFAULT_IMAGE_DIMENSIONS;
		}
	}
	
	private static Dimension extractDimensions(Matcher boundingBoxMatcher, String boundingBoxLine) 
		throws DimensionDeterminingException {

		if (boundingBoxMatcher.groupCount() != NUM_NUMBERS_IN_PATTERN) {
    		throwExceptionForInvalidBoundingBoxLine(boundingBoxLine);
    	}
		
		//(dimensions of bounding box should be in the captured groups of the regex)
		
		float bottomLeftX = Float.parseFloat(boundingBoxMatcher.group(1));
		float bottomLeftY = Float.parseFloat(boundingBoxMatcher.group(2));
		float topRightX = Float.parseFloat(boundingBoxMatcher.group(3));
		float topRightY = Float.parseFloat(boundingBoxMatcher.group(4));
		
		float imageWidth = topRightX - bottomLeftX;
		float imageHeight = topRightY - bottomLeftY;
		
		int roundedImageWidth = roundUp(imageWidth);
		int roundedImageHeight = roundUp(imageHeight);
		
		if (roundedImageWidth < 0 || roundedImageHeight < 0) {
			throwExceptionForNegativeBoundingBoxDimensions(
					roundedImageWidth, roundedImageHeight);
		}
		
		Dimension boundingBoxDimensions = 
			new Dimension(roundedImageWidth, roundedImageHeight);
		
		return boundingBoxDimensions;
	}
	
	private static void throwExceptionForInvalidBoundingBoxLine(String boundingBoxLine) 
		throws DimensionDeterminingException {
		String message = 
			"Could not determine intended image dimensions from PostScript file.";
		throw new DimensionDeterminingException(message);
	}
	
	private static void throwExceptionForBoundingBoxLineNotInHeader() 
		throws DimensionDeterminingException {
		String message = 
			"No dimension information in header of PostScript file" +
			"(Could not find a BoundingBox line in the first " + 
			CHECK_X_LINES_FOR_BOUNDING_BOX_BEFORE_GIVING_UP + 
			" lines.)";
		throw new DimensionDeterminingException(message);
		
	}
	
	private static void throwExceptionForNegativeBoundingBoxDimensions(float imageWidth, float imageHeight)
		throws DimensionDeterminingException {
		String message = 
			"Postscript file contains invalid negative image dimensions " +
			"'(" + imageWidth + ", " + imageHeight + ")'.";
		throw new DimensionDeterminingException(message);	
	}


	private static void throwExceptionForNoBoundingBoxLineInFile() 
		throws DimensionDeterminingException {
		String message = 
			"Postscript file does not contain image dimensions information.";
		throw new DimensionDeterminingException(message);	
	}
	
	private static void logThatWeArereUsingTheDefaultDimensions(LogService logger) {
		String message = 
			"Using default dimensions " + 
			"'(" + DEFAULT_IMAGE_DIMENSIONS.width + "," 
			+ DEFAULT_IMAGE_DIMENSIONS.height + ")'.";
		logger.log(LogService.LOG_INFO, message);
	}

	private static int NUM_NUMBERS_IN_PATTERN = 4;
	
	private static Pattern createBoundingBoxLineRegexPattern() {
		//create regex to match the bounding box line in the PostScript file.
		
		//Bounding Box line should look like %%BoundingBox:-300 +300.0 +3210 11874.993
		
		//(regex is "^.*BoundingBox:\\s*([-+]?[0-9]*\\.?[0-9]+)\\s+([-+]?[0-9]*\\.?[0-9]+)\\s+([-+]?[0-9]*\\.?[0-9]+)\\s+([-+]?[0-9]*\\.?[0-9]+).*$"
		
		final String lineStart = "^";
		final String anything = ".*";
		final String oneOrMoreWhitespace = "\\s+";
		final String zeroOrMoreWhitespace = "\\s*";
		final String lineEnd = "$";
		
		final String boundingBoxToken = lineStart + anything + "BoundingBox:";
		
		final String optionalPlusOrMinus = "[-+]?";
		final String zeroOrMoreDigits = "[0-9]*";
		final String oneOrMoreDigits = "[0-9]+";
		String optionalDecimalPoint = "\\.?";
		
		//[-+]?[0-9]*\\.?[0-9]+
		final String aNumber = optionalPlusOrMinus + zeroOrMoreDigits + optionalDecimalPoint + oneOrMoreDigits;
		
		final String boundingBoxLineRegex = boundingBoxToken + 
			zeroOrMoreWhitespace + captureGroup(aNumber) + //bottomLeftX
			oneOrMoreWhitespace + captureGroup(aNumber) + //bottomLeftY
			oneOrMoreWhitespace + captureGroup(aNumber) + //topRightX
			oneOrMoreWhitespace + captureGroup(aNumber) + //topRightY
			anything +
			lineEnd;
		
		Pattern localBoundingBoxLinePattern = Pattern.compile(boundingBoxLineRegex, Pattern.CASE_INSENSITIVE);
	
		return localBoundingBoxLinePattern;
	}
	
	//values in a capture group can be retrieved from the regular expression matcher later
	private static String captureGroup(String regex) {
		return "(" + regex + ")";
	}
	
	private static int roundUp(float number) {
		return Math.round(number + .5f);
	}
	
	public static void main(String[] args) {
		Pattern p = createBoundingBoxLineRegexPattern();
		Matcher m = p.matcher("%%BoundingBox: -300 +300.0 +3210 11874.993");
		if (m.matches()) {
			System.out.println("Matches");
		} else {
			System.out.println("No match");
		}
	}
}
