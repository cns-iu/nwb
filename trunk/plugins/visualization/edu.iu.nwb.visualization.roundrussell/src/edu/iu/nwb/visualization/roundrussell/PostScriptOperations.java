package edu.iu.nwb.visualization.roundrussell;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.visualization.roundrussell.interpolation.Interpolator;
import edu.iu.nwb.visualization.roundrussell.interpolation.InterpolatorInversionException;
import edu.iu.nwb.visualization.roundrussell.interpolation.InputRangeException;
import edu.iu.nwb.visualization.roundrussell.legend.ColorLegend;
import edu.iu.nwb.visualization.roundrussell.legend.Legend;
import edu.iu.nwb.visualization.roundrussell.utility.Averager;
import edu.iu.nwb.visualization.roundrussell.utility.Range;

/**
 * @author cdtank
 */
public class PostScriptOperations {
	
	private static URL postScriptHeaderFile = null;
	private static String NO_LEGEND_POSTSCRIPT_COMMENT = "% No legend possible due to \n" 
		+ "% inappropriate node color attribute values.\n";
	
	public static void setPostScriptHeaderFile(URL postScriptHeaderFile) {		
		PostScriptOperations.postScriptHeaderFile = postScriptHeaderFile;
	}
	
	public static String getPostScriptUtilityDefinitions() {
		StringTemplate definitionsTemplate =
			RoundRussellAlgorithm.group.getInstanceOf("utilityDefinitions");
		
		return definitionsTemplate.toString();
	}
	
	/*
	 * Get basic PostScript function definitions to be printer in all .ps files.
	 * */
	public static String getPostScriptHeaderContent() throws AlgorithmExecutionException {    	
		InputStream inStream = null;
    	BufferedReader input = null;
    	String line;
    	String psHeaderContentinString = "";
    
    	try {
             URLConnection connection = PostScriptOperations.postScriptHeaderFile.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	    	psHeaderContentinString = psHeaderContentinString.concat(line).concat("\n");
        	}
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} finally {
    		try {
    			if (input != null) {
    				input.close();
    			}
    	        if (inStream != null) { 
    	        	inStream.close();
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
    	
		return psHeaderContentinString;
	}
	
	
	/**
	 * Returns a string containing postscript content for printing the color legend. 
	 * @param colorQuantityInterpolator
	 * @param nodeColorValueRange
	 * @param colorRange
	 * @param nodeColorColumnName
	 * @return
	 * @throws AlgorithmExecutionException
	 * @throws InputRangeException
	 * @throws InterpolatorInversionException
	 */
	public static String getColorLegendContent(Interpolator<Color> colorQuantityInterpolator, 
											   Range<Double> nodeColorValueRange, 
											   Range<Color> colorRange,
											   String nodeColorColumnName) 
		throws AlgorithmExecutionException, 
			InputRangeException, 
			InterpolatorInversionException {
		Color colorMidrange = Averager.mean(colorRange.getMin(), colorRange.getMax());		
		double colorMidrangePreimage = colorQuantityInterpolator.invert(colorMidrange);
		
		// Add circle color legend
		double colorGradientLowerLeftX = Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS;
		double colorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
		double colorGradientWidth =  0.8 * (Legend.DEFAULT_WIDTH_IN_POINTS / 2.0);
		double colorGradientHeight = 10;
		
		String colorTypeLabel = "Node Color";
		String colorScaling = "";
		String colorQuantityAttribute = "based on " 
			+ nodeColorColumnName.toUpperCase() 
			+ " attribute";
		
		try {
			ColorLegend colorGradient = new ColorLegend(nodeColorValueRange,
					colorScaling, 
					colorMidrangePreimage, 
					colorRange, 
					colorTypeLabel, 
					colorQuantityAttribute , 
					colorGradientLowerLeftX, 
					colorGradientLowerLeftY, 
					colorGradientWidth, 
					colorGradientHeight);
			Legend legend = new Legend();
			legend.add(colorGradient);
			return legend.toPostScript();
		} catch (AlgorithmExecutionException e) { 
			return NO_LEGEND_POSTSCRIPT_COMMENT;
		}
	}
}
