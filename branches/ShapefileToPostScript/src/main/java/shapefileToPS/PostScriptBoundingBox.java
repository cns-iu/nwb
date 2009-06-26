package shapefileToPS;

public class PostScriptBoundingBox {
	public static final double DISPLAY_LOWER_LEFT_X_IN_INCHES = 0.0;
	public static final double DISPLAY_LOWER_LEFT_Y_IN_INCHES = 0.0;
	public static final double PAGE_WIDTH_IN_INCHES = 8.5;
	public static final double PAGE_HEIGHT_IN_INCHES = 11.0;
	public static final double POINTS_PER_INCH = 72.0;
	
	private double minX, minY, maxX, maxY;
	private double displayCenterXInPoints, displayCenterYInPoints;
	private double scale;
	private double dataCenterX, dataCenterY;

	public PostScriptBoundingBox(double dataMinX, double dataMinY, double dataMaxX, double dataMaxY) {

		double displayWidthInPoints = POINTS_PER_INCH * PAGE_WIDTH_IN_INCHES;
		double displayHeightInPoints = POINTS_PER_INCH * PAGE_HEIGHT_IN_INCHES;
		
		double displayLowerLeftXInPoints = POINTS_PER_INCH
				* DISPLAY_LOWER_LEFT_X_IN_INCHES;
		double displayLowerLeftYInPoints = POINTS_PER_INCH
		* DISPLAY_LOWER_LEFT_Y_IN_INCHES;
		
		this.displayCenterXInPoints = displayLowerLeftXInPoints
				+ displayWidthInPoints / 2;	
		this.displayCenterYInPoints = displayLowerLeftYInPoints
				+ displayHeightInPoints / 2;
		
		this.dataCenterX = (dataMaxX + dataMinX) / 2;
		this.dataCenterY = (dataMaxY + dataMinY) / 2;
		
		this.scale = calculateScale(dataMinX, dataMinY, dataMaxX, dataMaxY, displayWidthInPoints, displayHeightInPoints);

		this.minX = Math.round(f(dataMinX, displayCenterXInPoints, scale,
				dataCenterX));
		this.minY = Math.round(f(dataMinY, displayCenterYInPoints, scale,
				dataCenterY));
		this.maxX = Math.round(f(dataMaxX, displayCenterXInPoints, scale,
				dataCenterX));
		this.maxY = Math.round(f(dataMaxY, displayCenterYInPoints, scale,
				dataCenterY));
	}

	//TODO: rename
	private double f(double z, double displayCenterInPoints, double scale,
			double dataCenter) {
		return (scale * z + displayCenterInPoints) - scale * dataCenter;
	}

	public String toString() {
		return minX + " " + minY + " " + maxX + " " + maxY;
	}
	
	private double calculateScale(double dataMinX, double dataMinY,
			double dataMaxX, double dataMaxY, double displayWidthInPoints, double displayHeightInPoints) {
		double dataWidth = dataMaxX - dataMinX;
		double dataHeight = dataMaxY - dataMinY;
		double dataAspectRatio = dataWidth / dataHeight;
		double displayAspectRatio = displayWidthInPoints
				/ displayHeightInPoints;
		double scale = 1;
		if (displayAspectRatio < dataAspectRatio) {
			// System.out.println("the limiting dimension is width");
			scale = displayWidthInPoints / dataWidth;

		} else {
			// System.out.println("the limiting dimension is height");
			scale = displayHeightInPoints / dataHeight;
		}

		return scale;
	}

	public String createDrawingCode() {
		String s = "";

		s += "gsave" + "\n";
		s += "  " + ".2 setlinewidth" + "\n";
		s += "  " + "newpath" + "\n";
		s += "  " + "  " + minX + " " + minY + " moveto" + "\n";
		s += "  " + "  " + minX + " " + maxY + " lineto" + "\n";
		s += "  " + "  " + maxX + " " + maxY + " lineto" + "\n";
		s += "  " + "  " + maxX + " " + minY + " lineto" + "\n";
		s += "  " + "closepath\n";
		s += "  " + "stroke\n";
		s += "grestore\n";

		return s;
	}

	public String getDisplayTranslateString() {
		return displayCenterXInPoints + " " + displayCenterYInPoints + " translate" + "\n";
	}

	public String getScaleString() {
		return scale + " " + scale + " scale" + "\n";
	}

	public String getDataTranslateString() {
		return (-dataCenterX) + " " + (-dataCenterY) + " translate" + "\n";
	}
}
