package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

public class Color {
	public final static int MAXIMUM_RANGE = 0xFF;
	public final static int SHIFT_TO_GREEN_BITS = 8;
	public final static int SHIFT_TO_RED_BITS = 16;
	
	private static String SEPARATOR = " ";
	private float red;
	private float green;
	private float blue;
	
	private Color(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color(int rgbColor){
		this(extractRed(rgbColor),  extractGreen(rgbColor), extractBlue(rgbColor));
	}
	
	private static float extractRed(int rgbColor){
		return (float)(((rgbColor>>SHIFT_TO_RED_BITS) & MAXIMUM_RANGE)) / MAXIMUM_RANGE;
	}
	
	private static float extractGreen(int rgbColor){
		return (float)(((rgbColor>>SHIFT_TO_GREEN_BITS) & MAXIMUM_RANGE)) / MAXIMUM_RANGE;
	}
	
	private static float extractBlue(int rgbColor){
		return (float)((rgbColor & MAXIMUM_RANGE)) / MAXIMUM_RANGE;
	}
	
	public float getRed(){
		return this.red;
	}
	
	public float getGreen(){
		return this.green;
	}
	
	public float getBlue(){
		return this.blue;
	}
	
	@Override
	public String toString(){
		return red + SEPARATOR + green + SEPARATOR + blue;
	}
}
