package edu.iu.nwb.converter.pajeknet.common;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NETVertex {
	private static Map Attributes = new LinkedHashMap();
	private Map Numeric_Parameters;
	private Map String_Parameters;
	private int id;
	private String comment = null;
	private String label = null;


	private boolean valid = false;

	public NETVertex(){
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
	}

	public NETVertex(String s) throws Exception {
		String[] properties = NETFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
		this.valid = testVertices(properties);

	}

	public boolean testVertices(String[] strings) throws Exception{
		boolean value = true;
		Queue stringQueue = new ConcurrentLinkedQueue();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String) stringQueue.peek()).startsWith(NETFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		try{

			testVertexID(stringQueue);

			if(!stringQueue.isEmpty()){

				testVertexPosition(stringQueue);	
				testParameters(stringQueue);
				
			}

		}catch(Exception ex){
			throw ex;
		}
		
		return value;
	}

	public boolean testVertexID(Queue qs) throws NETFileFormatException{
		try
		{
			if(qs.size() < 2){
				throw new NETFileFormatException("Vertices must have both ID and Label");
			}

			String s = (String) qs.poll();

			this.setID(s);
			s = (String) qs.poll();
			this.setLabel(s);


			return true;
		}
		catch(Exception ex){
			throw new NETFileFormatException(ex);
		}
	}

	public boolean testVertexPosition(Queue qs) throws NETFileFormatException{

		boolean value = false;
		float f = 0;
		int i = 0;
		try{
			for(;;){
				if(qs.isEmpty())
					return false; //no positional data

				String s = (String) qs.peek();
				
				f = new Float(s).floatValue();

				value = true;
				switch (i){
				case 0:
					this.setPos("Xpos", f);
					break;
				case 1:
					this.setPos("Ypos", f);
					break;
				case 2:
					this.setPos("Zpos", f);
					break;
				default:
					return true;
				}
				i++;
				qs.poll();
			}
		}
		catch(NumberFormatException ex){
			return value;
		}


	}



	public boolean setVertexShape(String st) throws NETFileFormatException{
		try {
			
					setShape(st);
			
			
			return false;
		}
		catch(Exception ex){
			return false;
		}
	}


	public boolean testParameters(Queue qs) throws NETFileFormatException{
		boolean value = false;

		while(!qs.isEmpty()){
			String s1 = (String) qs.poll();
			
			if(NETFileFunctions.isInList(s1, NETFileShape.ATTRIBUTE_SHAPE_LIST)){
				this.setShape(s1);
				continue;
			}

			if(qs.isEmpty()){
				throw new NETFileFormatException("Expected a value for parameter: " + s1);
			}
			String s2 = (String) qs.poll();


			if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_X_FACT)){
				this.setXScaleFactor(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_Y_FACT)){
				this.setYScaleFactor(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_SIZE)){
				this.setSize(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_PHI)){
				this.setPhi(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_R)){
				this.setCornerRadius(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_Q)){
				this.setDiamondRatio(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_IC) || s1.equalsIgnoreCase(NETFileParameter.PARAMETER_COLOR)){
				this.setInternalColor(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_BC)){
				this.setBorderColor(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_BW)){
				this.setBorderWidth(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_LC)){
				this.setLabelColor(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_LA)){
				this.setLabelAngle(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_FONT)){
				this.setFont(s2);	
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_LPHI)){
				this.setLabelPhi(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_FOS)){
				this.setFontSize(s2);
			}
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_LR)){
				this.setLabelRadius(s2);
			}


			else if(s1.startsWith(NETFileProperty.PREFIX_COMMENTS)){
				qs.clear();
				break;
			}
			else if(s1.startsWith(NETFileParameter.PARAMETER_SHAPE)){
				this.setVertexShape(s2);
			}

			else {
				throw new NETFileFormatException("Unknown parameter: " + s1);  //to be changed to allow for nonsense parameters.
			}
		}
		value = true;
		return value;

	}

	/**********************
	 * Setters
	 **********************/

	/*
	 * setID
	 * setID sets the ID of each vertex. The public version takes in a string and
	 * tries to cast it as an integer. If it succeeds, then checks to see if it is
	 * greater than 0. If that succeeds, then the id is set.
	 * written by: Tim Kelley
	 */

	private void setID(int i){
		Attributes.put(NETFileProperty.ATTRIBUTE_ID, NETFileProperty.TYPE_INT);
		this.id = i;
	}
	public void setID(String s) throws NETFileFormatException{
		int i = NETFileFunctions.asAnInteger(s);
		if(i < 1){
			throw new NETFileFormatException("Vertex ID must be greater than or equal to 1.");
		}
		this.setID(i);
	}

	/*
	 * setLabel
	 * setLabel sets the label of the vertex. 
	 * written by: Tim Kelley
	 */


	public void setLabel(String s){
		NETVertex.Attributes.put(NETFileProperty.ATTRIBUTE_LABEL, NETFileProperty.TYPE_STRING);
		this.label = s;

	}

	/*
	 * setPos
	 * the setPos function takes in a string telling which positional data to set
	 * and calls the required set?Pos(float) function. Those functions add the
	 * positional data attributes to the attribute list and add the values to
	 * the numeric value attributes.
	 * written by: Tim Kelley
	 */

	private void setXpos(float f){
		NETVertex.Attributes.put("xpos", "float");
		this.Numeric_Parameters.put("xpos", new Float(f));
	}

	private void setYpos(float f){
		NETVertex.Attributes.put("ypos", "float");
		this.Numeric_Parameters.put("ypos", new Float(f));
	}

	private void setZpos(float f){
		NETVertex.Attributes.put("zpos", "float");
		this.Numeric_Parameters.put("zpos", new Float(f));
	}

	public void setPos(String s, float f) throws NETFileFormatException{
		switch(s.charAt(0)){
		case 'x':
		case 'X':
			this.setXpos(f);
			break;
		case 'y':
		case 'Y':
			this.setYpos(f);
			break;
		case 'z':
		case 'Z':
			this.setZpos(f);
			break;
		default :
			throw new NETFileFormatException("Unknown positional data");
		}
	}

	/*
	 * setShape
	 * setShape attempts to set the shape of a vertex. If the shape is not in the recognized shape list
	 * it throws an exception saying so.
	 * written by: Tim Kelley
	 */

	public void setShape(String s) throws NETFileFormatException{
		NETVertex.Attributes.put("shape", "string");
		this.String_Parameters.put("shape", s);
	}

	/*
	 * setPhi
	 * phi is a positional angle of the object. theSetPhi(String) takes in a string value,
	 * casts it to a float and establishes the phi attribute in the attribute list and sets the 
	 * attribute value for the vertex. It throws an exception if the string is not a float, or,
	 * if the float value is not between 0 and 360 degrees inclusive.
	 * written by: Tim Kelley
	 */



	private void setPhi(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_PHI, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_PHI, new Float(f));
	}
	public void setPhi(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setPhi(f);
	}

	/*
	 * setSize
	 * setSize takes a String, casts it as a float. Size must be greater than 0, and so setSize throws an
	 * Exception when either the String is not a float value, or if the float value is less than or equal to
	 * zero.
	 * written by: Tim Kelley
	 */


	private void setSize(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_SIZE, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_SIZE, new Float(f));
	}
	public void setSize(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setSize(f);
	}


	public void setFont(String s) throws NETFileFormatException{
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_FONT, "string");
		this.String_Parameters.put(NETFileParameter.PARAMETER_FONT, s);
	}

	/*
	 * setBorderColor
	 * setBorderColor takes a string, compares it with a list of recognized colors. if the color is recognized
	 * the function sets the border color attribute in the attribute list and the border color value for the vertex.
	 * Otherwise, it throws an Exception.
	 * written by: Tim Kelley
	 */

	public void setBorderColor(String s) throws NETFileFormatException{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_BC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_BC, s);
		}
		else
			throw new NETFileFormatException(s + " is not a valid color selection");
	}

	/*
	 * setBorderWidth
	 * this function takes a string, casts it as a float and adds border width to the
	 * attribute list and sets the border width value for the vertex. if the borderwidth is less than 
	 * 0 or the string is not a float, it throws an exception.
	 * written by: Tim Kelley
	 */
	private void setBorderWidth(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_BW, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_BW, new Float(f));
	}
	public void setBorderWidth(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setBorderWidth(f);
	}

	/*
	 * setInternalColor
	 * sets the fill color for the vertex based on a string. throws an exception
	 * if the string is not a recognized color.
	 * written by: Tim Kelley
	 */	


	private void setInternalColor(String s) throws NETFileFormatException{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_IC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_IC,s);
		}
		else
			throw new NETFileFormatException(s + " is not a valid color selection");

	}

	/*
	 * setFontSize
	 * takes a string, casts as float, throws exception if less than or equal to
	 * 0 or not a float value
	 * written by: Tim Kelley
	 */

	private void setFontSize(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_FOS, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_FOS,new Float(f));
	}
	public void setFontSize(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setFontSize(f);
	}

	/*
	 * setLabelAngle
	 * sets the angle that the label is displayed at, throws exception if
	 * string is not a float and if the float is not between 0 and 360 inclusive.
	 * written by: Tim Kelley
	 */

	private void setLabelAngle(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LA, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LA, new Float(f));
	}
	private void setLabelAngle(String s)  {
		float f = NETFileFunctions.asAFloat(s);

		this.setLabelAngle(f);

	}
	/*
	 * setLabelPhi
	 * sets the second of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelPhi(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LPHI, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LPHI, new Float(f));
	}
	private void setLabelPhi(String s)  {
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelPhi(f);

	}


	/*
	 * setLabelRadius
	 * sets the first of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelRadius(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LR, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LR, new Float(f));
	}
	public void setLabelRadius(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelRadius(f);
	}

	/*
	 * setLabelColor
	 * sets the color of the label, throws exception if string is not a recognized
	 * color.
	 * written by: Tim Kelley
	 */
	private void setLabelColor(String s) throws NETFileFormatException{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_LC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_LC, s);
		}
		else
			throw new NETFileFormatException(s + " is an invalid color selection");

	}

	/*
	 * set?ScaleFactor
	 * scales the horizontal or vertical dimensions of a shape.
	 * written by: Tim Kelley
	 */
	private void setXScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_X_FACT, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_X_FACT, new Float(f));
	}
	public void setXScaleFactor(String s){
		float f = NETFileFunctions.asAFloat(s);
		this.setXScaleFactor(f);

	}

	private void setYScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_Y_FACT, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_Y_FACT, new Float(f));
	}
	public void setYScaleFactor(String s){	
		float f = NETFileFunctions.asAFloat(s);
		this.setYScaleFactor(f);

	}

	/*
	 * setCornerRadius
	 * the corner radius controls the rounding of corners on box and diamond shapes.
	 * this function takes a string, casts it as a float, then adds the corner radius
	 * attribute to the attribute list, and sets the value of the corner radius for the vertex.
	 * if there is no given shape, or the given shape is not a box or diamond, the function
	 * throws an exception. Also, if the radius is less than or equal to 0, the function throws
	 * an exception.
	 * written by: Tim Kelley
	 */
	private void setCornerRadius(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_R, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_R, new Float(f));
	}
	private void setCornerRadius(String s) throws NETFileFormatException{
		try{
		float f = NETFileFunctions.asAFloat(s);
		setCornerRadius(f);
		}
		catch(NumberFormatException ex){
			throw new NETFileFormatException(s + " is not a valid value for the parameter " + NETFileParameter.PARAMETER_R);
		}

	}

	/*
	 * setDiamondRatio
	 * the Diamond Ratio is the ratio between the vertical and horizontal aspects of
	 * the diamond shape.
	 * setDiamondRatio takes a string, casts it as a float and adds diamond ratio
	 * to the attribute list and sets the value for the vertex. if the given shape
	 * is not set or the given shape is not a diamond, it throws an exception.
	 * written by: Tim Kelley
	 */
	private void setDiamondRatio(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_Q, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_Q, new Float(f));
	}
	public void setDiamondRatio(String s) throws NETFileFormatException{
		

		try {
			float f = NETFileFunctions.asAFloat(s);
			
			this.setDiamondRatio(f);
		}
		catch(NumberFormatException ex){
			throw new NETFileFormatException(s + " is not a valid value for parameter " + NETFileParameter.PARAMETER_Q);
		}

	}

	protected static void clearAttributes(){
		NETVertex.Attributes.clear();
	}



	/****************************
	 * Getters
	 ****************************/

	public static List getVertexAttributes(){
		ArrayList attributeList = new ArrayList();
		for(Iterator ii = NETVertex.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			attributeList.add(new NETAttribute(s, (String) NETVertex.Attributes.get(s)));
		}
		return attributeList;

	}

	public String getLabel(){
		return this.label;
	}


	public int getID(){
		return this.id;
	}

	public float getXpos(){
		return ((Float) this.Numeric_Parameters.get("xpos")).floatValue();
	}

	public float getYpos(){
		return ((Float) this.Numeric_Parameters.get("ypos")).floatValue();
	}

	public float getZpos(){
		return ((Float) this.Numeric_Parameters.get("zpos")).floatValue();
	}

	public String getShape() throws NullPointerException{
		return (String) this.String_Parameters.get("shape");
	}

	public boolean isValid(){
		return this.valid;
	}

	public Object getAttribute(String s){
		//System.out.println(s);
		String st = (String) NETVertex.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_ID))
			return new Integer(this.getID());
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_LABEL))
			return this.getLabel();
		else if(st.equalsIgnoreCase("float"))
			return this.Numeric_Parameters.get(s);
		else {
			return this.String_Parameters.get(s);
		}
	}

	/****************************
	 * Static Functions
	 ****************************/

	public static boolean betweenZeroandOne(float f){
		float val = f;
		if((val > .000001) || (val < 1.000001))
			return true;
		val = 1-f;
		if((val > .0000001) || (val < 1.000001))
			return true;
		return false;
	}


	/************
	 *  Output
	 *************/

	public String toString(){
		String output = "";

		for(Iterator ii = NETVertex.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
