package edu.iu.scipolicy.converter.psraster.postscript.postscriptrenderer.utility;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
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
					
					if (lineMatcher.groupCount() != NUM_NUMBERS_IN_PATTERN) {
		        		throwExceptionForInvalidBoundingBoxLine(boundingBoxLine);
		        	}
					
					int bottomLeftX = Integer.parseInt(lineMatcher.group(1));
					int bottomLeftY = Integer.parseInt(lineMatcher.group(2));
					int topRightX = Integer.parseInt(lineMatcher.group(3));
					int topRightY = Integer.parseInt(lineMatcher.group(4));
					
					int imageWidth = topRightX - bottomLeftX;
					int imageHeight = topRightY - bottomLeftY;
					
					if (imageWidth < 0 || imageHeight < 0) {
						throwExceptionForNegativeBoundingBoxDimensions(imageWidth, imageHeight);
					}
					
					Dimension boundingBoxDimensions = new Dimension(imageWidth, imageHeight);
					
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
	
	private static void throwExceptionForNegativeBoundingBoxDimensions(int imageWidth, int imageHeight)
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
		
		//Bounding Box line should look like %%BoundingBox: 10 23 40 219
		
		//(regex is "^.*BoundingBox\\S*\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+).*$"
		
		String lineStart = "^";
		String anything = ".*";
		String anyNonWhitespace = "\\S*";
		String oneOrMoreDigit = "\\d+";
		String oneOrMoreWhitespace = "\\s+";
		String lineEnd = "$";
		
		String boundingBoxToken = lineStart + anything + "BoundingBox" + anyNonWhitespace;
		String aNumber = oneOrMoreDigit;
		
		String boundingBoxLineRegex = boundingBoxToken + 
			oneOrMoreWhitespace + captureGroup(aNumber) + //bottomLeftX
			oneOrMoreWhitespace + captureGroup(aNumber) + //bottomLeftY
			oneOrMoreWhitespace + captureGroup(aNumber) + //topRightX
			oneOrMoreWhitespace + captureGroup(aNumber) + //topRightY
			anything +
			lineEnd;
		
		Pattern localBoundingBoxLinePattern = Pattern.compile(boundingBoxLineRegex);
	
		return localBoundingBoxLinePattern;
	}
	
	private static String captureGroup(String regex) {
		return "(" + regex + ")";
	}
}
