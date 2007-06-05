package edu.iu.nwb.converter.pajeknet.common;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NETVertex {
	private static Map<String,String> Attributes = new LinkedHashMap<String,String>();
	private Map<String, Float> Numeric_Parameters;
	private Map<String, String> String_Parameters;
	private int id;
	private String comment = null;
	private String label = null;


	private boolean valid = false;

	public NETVertex(){
		this.Numeric_Parameters = new ConcurrentHashMap<String, Float>();
		this.String_Parameters = new ConcurrentHashMap<String,String>();
	}

	public NETVertex(String s) throws Exception {
		String[] properties = NETFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap<String, Float>();
		this.String_Parameters = new ConcurrentHashMap<String,String>();
		this.valid = testVertices(properties);

	}

	public boolean testVertices(String...strings) throws Exception{
		boolean value = false;
		Queue<String> stringQueue = new ConcurrentLinkedQueue<String>();
		for(String s : strings){
			stringQueue.add(s);
		}
		if(stringQueue.peek().startsWith(NETFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(String s : strings)
				comment += s;
			return true;
		}
		
		try{
			
		this.testVertexID(stringQueue);
		
		if(!stringQueue.isEmpty()){
			
				this.testVertexPosition(stringQueue);	
				if(!this.testVertexShape(stringQueue.peek())){
					testParameters(stringQueue);
				}
				else {
					stringQueue.poll();
					testParameters(stringQueue);
				}
			}
			
		}catch(Exception ex){
			throw ex;
		}
		value = finalCheck();
		return value;
	}

	public boolean testVertexID(Queue<String> qs) throws Exception{
		try
		{
			int i = 0;
			while(!qs.isEmpty()){
			
				String s = qs.peek();
				switch(i){
				case 0:
					NETFileFunctions.isAnInteger(s, NETFileProperty.ATTRIBUTE_ID);
					this.setID(s);
					break;
				case 1:
					NETFileFunctions.isAString(s, NETFileProperty.ATTRIBUTE_LABEL);
					this.setLabel(s);
					break;
				default:
					return true;
				}
				qs.poll();
				i++;
			}
			return true;
		}
		catch(Exception ex){
			throw ex;
		}
	}

	public boolean testVertexPosition(Queue<String> qs) throws Exception{

		boolean value = true;
		float f = 0;
		int i = 0;
		try{
			while(!qs.isEmpty()){
				if((NETFileFunctions.isInList(qs.peek(), NETFileShape.ATTRIBUTE_SHAPE_LIST) || (NETFileFunctions.isInList(qs.peek(), NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST)) || (NETFileFunctions.isInList(qs.peek(), NETFileParameter.VERTEX_STRING_PARAMETER_LIST)))){
					break;
				}
				f = Float.parseFloat(qs.poll());
				if(NETVertex.betweenZeroandOne(f)){  //we should actually make this better to handle round off errors.
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
					}
				}
				else {
					value = false;
					throw new Exception("Positional data must be between 0.0 and 1.0");
				}
				i++;
			}

		}
		catch(NumberFormatException ex){
			throw new Exception("The file contains an invalid sequence in the positional data.");
		}

		return value;
	}



	public boolean testVertexShape(String st) throws Exception{
		boolean value = false;
		try {
			for(String s : NETFileShape.ATTRIBUTE_SHAPE_LIST){
				if(st.equalsIgnoreCase(s)){
					this.setShape(s);
					return true;
				}
			}
		}
		catch(Exception ex){
			throw ex;
		}
		return value;
	}


	public boolean testParameters(Queue<String> qs) throws Exception{
		boolean value = false;

		while(!qs.isEmpty()){
			String s1 = qs.poll();

			if(qs.isEmpty()){
				throw new Exception("Expected a value for parameter: " + s1);
			}
			String s2 = qs.poll();


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
			else if(s1.equalsIgnoreCase(NETFileParameter.PARAMETER_IC)){
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
				this.setFont(s2,qs);	
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

			else {
				throw new Exception("Unknown parameter: " + s1);
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
	public void setID(String s) throws Exception{
		int i = new Float(NETFileFunctions.asAFloat(s)).intValue();
		if(i < 0){
			throw new NumberFormatException("Vertex ID must be greater than 0");
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
		this.Numeric_Parameters.put("xpos", f);
	}

	private void setYpos(float f){
		NETVertex.Attributes.put("ypos", "float");
		this.Numeric_Parameters.put("ypos", f);
	}

	private void setZpos(float f){
		NETVertex.Attributes.put("zpos", "float");
		this.Numeric_Parameters.put("zpos", f);
	}

	public void setPos(String s, float f){
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
		}
	}

	/*
	 * setShape
	 * setShape attempts to set the shape of a vertex. If the shape is not in the recognized shape list
	 * it throws an exception saying so.
	 * written by: Tim Kelley
	 */

	public void setShape(String s) throws Exception{
		if(!NETFileFunctions.isInList(s, NETFileShape.ATTRIBUTE_SHAPE_LIST))
			throw new Exception(s + " is an unknown shape");
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
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_PHI, f);
	}
	public void setPhi(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if((f >= 0) && (f <= 360))
			this.setPhi(f);
		else
			throw new Exception("Value must be between 0 and 360, inclusive");
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
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_SIZE, f);
	}
	public void setSize(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setSize(f);
		else
			throw new Exception("Size must be greater than 0.0");
	}



	/*
	 * setFont
	 * setFont takes a string and a queue of strings and compares them with the system font list. If the
	 * listed font is not found on the system, it throws an exception, otherwise, it sets the font attribute
	 * in the attribute list and sets the font value for the vertex.
	 * written by: Tim Kelley
	 */
	private TreeSet<String> setFontPrime(String s, TreeSet<String> ss){
		String compare = s;
		TreeSet<String> ts = new TreeSet<String>();
		compare = compare.toLowerCase();
		for(String st : ss){
			if(st.startsWith(s)){
				ts.add(st);
			}
		}
		return ts;
	}
	public void setFont(String s, Queue<String> qs) throws Exception{
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		TreeSet<String> compareList = new TreeSet<String>();
		String compare = s;
		for(String st : fonts){
			if(st.startsWith(compare)){
				compareList.add(st);		
			}
		}	

		while(!qs.isEmpty()){

			String peekValue = qs.peek();
			if(compareList.isEmpty())
				throw new Exception(compare + " is not a recognized font on this system");
			else if(NETFileFunctions.isInList(peekValue, NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST) || NETFileFunctions.isInList(peekValue, NETFileParameter.VERTEX_STRING_PARAMETER_LIST))
				break;
			compare = compare + " " + qs.poll();
			compareList = this.setFontPrime(compare, compareList);
		}
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_FONT, "string");
		this.String_Parameters.put(NETFileParameter.PARAMETER_FONT, compare);
	}

	/*
	 * setBorderColor
	 * setBorderColor takes a string, compares it with a list of recognized colors. if the color is recognized
	 * the function sets the border color attribute in the attribute list and the border color value for the vertex.
	 * Otherwise, it throws an Exception.
	 * written by: Tim Kelley
	 */

	public void setBorderColor(String s) throws Exception{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_BC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_BC, s);
		}
		else
			throw new Exception(s + " is not a valid color selection");
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
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_BW, f);
	}
	public void setBorderWidth(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f <= 0)
			throw new Exception("The border width must be greater than 0.0");
		this.setBorderWidth(f);
	}

	/*
	 * setInternalColor
	 * sets the fill color for the vertex based on a string. throws an exception
	 * if the string is not a recognized color.
	 * written by: Tim Kelley
	 */	


	private void setInternalColor(String s) throws Exception{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_IC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_IC,s);
		}
		else
			throw new Exception(s + " is not a valid color selection");

	}

	/*
	 * setFontSize
	 * takes a string, casts as float, throws exception if less than or equal to
	 * 0 or not a float value
	 * written by: Tim Kelley
	 */

	private void setFontSize(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_FOS, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_FOS,f);
	}
	public void setFontSize(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0){
			this.setFontSize(f);
		}
		else
			throw new Exception("The Font must be greater than 0.0");
	}

	/*
	 * setLabelAngle
	 * sets the angle that the label is displayed at, throws exception if
	 * string is not a float and if the float is not between 0 and 360 inclusive.
	 * written by: Tim Kelley
	 */

	private void setLabelAngle(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LA, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LA, f);
	}
	private void setLabelAngle(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if((f >= 0) && (f <= 360))
			this.setLabelAngle(f);
		else
			throw new Exception("The label angle must be between 0 and 360, inclusive");
	}
	/*
	 * setLabelPhi
	 * sets the second of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelPhi(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LPHI, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LPHI, f);
	}
	private void setLabelPhi(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if((f >= 0) && (f <= 360))
			this.setLabelPhi(f);
		else
			throw new Exception("The label position in degrees must be between 0 and 360, inclusive");
	}


	/*
	 * setLabelRadius
	 * sets the first of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelRadius(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LR, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_LR, f);
	}
	public void setLabelRadius(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f <= 0)
			throw new Exception("The Label radius must be greater than 0.0");
		this.setLabelRadius(f);
	}

	/*
	 * setLabelColor
	 * sets the color of the label, throws exception if string is not a recognized
	 * color.
	 * written by: Tim Kelley
	 */
	private void setLabelColor(String s) throws Exception{
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETVertex.Attributes.put(NETFileParameter.PARAMETER_LC, "string");
			this.String_Parameters.put(NETFileParameter.PARAMETER_LC, s);
		}
		else
			throw new Exception(s + " is an invalid color selection");

	}

	/*
	 * set?ScaleFactor
	 * scales the horizontal or vertical dimensions of a shape.
	 * written by: Tim Kelley
	 */
	private void setXScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_X_FACT, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_X_FACT, f);
	}
	public void setXScaleFactor(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f <= 0)
			throw new Exception("Scale factor must be greater than 0.0");
		this.setXScaleFactor(f);

	}

	private void setYScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_Y_FACT, "float");
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_Y_FACT, f);
	}
	public void setYScaleFactor(String s) throws Exception{	
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setYScaleFactor(f);
		else
			throw new Exception("Scale factor must be greater than 0.0");		
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
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_R, f);
	}
	private void setCornerRadius(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setCornerRadius(f);
		else
			throw new Exception("The corner radius must be greater than 0.0");
		try {
			if(!(this.getShape().equalsIgnoreCase(NETFileShape.SHAPE_BOX) || this.getShape().equalsIgnoreCase(NETFileShape.SHAPE_DIAMOND))){
				throw new Exception("This parameter is not used by this vertex's shape");
			}
		}
		catch(NullPointerException ex){
			throw new Exception("This parameter is not used by this vertex's shape");
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
		this.Numeric_Parameters.put(NETFileParameter.PARAMETER_Q, f);
	}
	public void setDiamondRatio(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setDiamondRatio(f);
		else
			throw new Exception("The Diamond scaling ratio must be greater than 0.0");
		try {
			if(!this.getShape().equalsIgnoreCase(NETFileShape.SHAPE_DIAMOND)){
				throw new Exception("This parameter is not used by this vertex's shape");
			}
		}
		catch(NullPointerException ex){
			throw new Exception("This parameter is not used by this vertex's shape");
		}

	}



	/****************************
	 * Getters
	 ****************************/

	public static List<NETAttribute> getVertexAttributes(){
		ArrayList<NETAttribute> attributeList = new ArrayList<NETAttribute>();
		for(String s : NETVertex.Attributes.keySet()){
			attributeList.add(new NETAttribute(s,NETVertex.Attributes.get(s)));
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
		return this.Numeric_Parameters.get("xpos");
	}

	public float getYpos(){
		return this.Numeric_Parameters.get("ypos");
	}

	public float getZpos(){
		return this.Numeric_Parameters.get("zpos");
	}

	public String getShape() throws Exception{
		return this.String_Parameters.get("shape");
	}

	public boolean isValid(){
		return this.valid;
	}

	public Object getAttribute(String s){
		String st = NETVertex.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_ID))
			return this.getID();
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_LABEL))
			return this.getLabel();
		else if(st.equalsIgnoreCase("float"))
			return this.Numeric_Parameters.get(s);
		else 
			return this.String_Parameters.get(s);
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
	
	private boolean finalCheck() throws Exception{
	
		if((this.getAttribute("lphi") == null) && (this.getAttribute("lr") == null))
			return true;
		else if((this.getAttribute("lphi") != null) && (this.getAttribute("lr") != null))
			return true;
		else
			throw new Exception("Only one of two polar coordinates has been set");
		
	}

	/************
	 *  Output
	 *************/

	public String toString(){
		String output = "";

		for(String s : NETVertex.Attributes.keySet()){
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
