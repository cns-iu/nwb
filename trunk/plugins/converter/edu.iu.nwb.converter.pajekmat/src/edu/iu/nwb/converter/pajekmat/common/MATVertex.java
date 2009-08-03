package edu.iu.nwb.converter.pajekmat.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MATVertex {
	private static Map Attributes = new LinkedHashMap();
	private Map Numeric_Parameters;
	private Map String_Parameters;
	private int id;
	private String comment = null;
	private String label = null;
	private int unknowns = 0;


	private boolean valid = false;

	public MATVertex(){
		this.Numeric_Parameters = new HashMap();
		this.String_Parameters = new HashMap();
	}

	public MATVertex(String s) throws MATFileFormatException {
		String[] properties = MATFileFunctions.processTokens(s);
		this.Numeric_Parameters = new HashMap();
		this.String_Parameters = new HashMap();
		this.valid = testVertices(properties);

	}

	public boolean testVertices(String[] strings) throws MATFileFormatException {
		boolean value = false;
		LinkedList stringQueue = new LinkedList();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String) stringQueue.getFirst()).startsWith(MATFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		this.testVertexID(stringQueue);

		if(!stringQueue.isEmpty()){

			this.testVertexPosition(stringQueue);	
			if(! setVertexShape((String) stringQueue.getFirst())){
				testParameters(stringQueue);
			}
			else {
				stringQueue.removeFirst();
				testParameters(stringQueue);
			}
		}
		value = finalCheck();
		return value;
	}

	public boolean testVertexID(LinkedList qs) throws MATFileFormatException {
		if(qs.size() < 2){
			throw new MATFileFormatException("Vertices must have both ID and Label");
		}
		String s = (String) qs.removeFirst();

		this.setID(s);
		s = (String)qs.removeFirst();
		this.setLabel(s);

		return true;
	}

	public boolean testVertexPosition(LinkedList qs) throws MATFileFormatException {

		boolean value = true;
		float f = 0;
		int i = 0;
		try {
			for(;;){
				if(qs.isEmpty()) {
					return false;  //no positional data
				}

				f = new Float((String)qs.getFirst()).floatValue();

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
				i++;
				qs.removeFirst();
			}
		} catch(NumberFormatException e) {
			return value;
		}


	}



	public boolean setVertexShape(String st) {
		try {
			this.setShape(st);
			return true;
		} catch(MATFileFormatException ex){
			return false;
		}
	}


	public boolean testParameters(LinkedList qs) throws MATFileFormatException {
		boolean value = false;

		while(!qs.isEmpty()){
			String s1 = (String) qs.removeFirst();

			if(MATFileFunctions.isInList(s1,MATFileShape.ATTRIBUTE_SHAPE_LIST)){
				this.setShape(s1);
				continue;
			}

			String s2 = (String) qs.getFirst();


			if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_X_FACT)){
				this.setXScaleFactor(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_Y_FACT)){
				this.setYScaleFactor(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_SIZE)){
				this.setSize(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_PHI)){
				this.setPhi(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_R)){
				this.setCornerRadius(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_Q)){
				this.setDiamondRatio(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_IC) || s1.equalsIgnoreCase(MATFileParameter.PARAMETER_COLOR)){
				if(MATFileFunctions.isAFloat(s2, "float") || MATFileFunctions.isAnInteger(s2, "int")){
					String s = (String)qs.removeFirst();
					s += " " + qs.removeFirst() + " ";
					s += " " + qs.removeFirst();
					this.setInternalColor(s);
				}
				else{
					this.setInternalColor(s2);
					qs.removeFirst();
				}
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_BC)){
				if(MATFileFunctions.isAFloat(s2, "float") || MATFileFunctions.isAnInteger(s2, "int")){
					String s = (String)qs.removeFirst();
					s += " " + qs.removeFirst() + " ";
					s += " " + qs.removeFirst();
					this.setBorderColor(s);
				}
				else{
					this.setBorderColor(s2);
					qs.removeFirst();
				}
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_BW)){
				this.setBorderWidth(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_LC)){
				if(MATFileFunctions.isAFloat(s2, "float") || MATFileFunctions.isAnInteger(s2, "int")){
					String s = (String)qs.removeFirst();
					s += " " + qs.removeFirst() + " ";
					s += " " + qs.removeFirst();
					this.setLabelColor(s);
				}
				else{
					this.setLabelColor(s2);
					qs.removeFirst();
				}
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_LA)){
				this.setLabelAngle(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_FONT)){
				this.setFont(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_LPHI)){
				this.setLabelPhi(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_FOS)){
				this.setFontSize(s2);
				qs.removeFirst();
			}
			else if(s1.equalsIgnoreCase(MATFileParameter.PARAMETER_LR)){
				this.setLabelRadius(s2);
				qs.removeFirst();
			}


			else if(s1.startsWith(MATFileProperty.PREFIX_COMMENTS)){
				qs.clear();
				break;
			}
			else if(s1.startsWith(MATFileParameter.PARAMETER_SHAPE)){
				this.setVertexShape(s2);
				qs.removeFirst();
			}

			else {
				setUnknownAttribute(s1);
			}
		}
		value = true;
		return value;

	}

	public static void clearAttributes(){
		MATVertex.Attributes.clear();
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
		Attributes.put(MATFileProperty.ATTRIBUTE_ID, MATFileProperty.TYPE_INT);
		this.id = i;
	}
	public void setID(String s) throws MATFileFormatException{
		int i = MATFileFunctions.asAnInteger(s);
		if(i < 1){
			throw new MATFileFormatException("Vertex ID must be greater than or equal to 1");
		}
		this.setID(i);
	}

	/*
	 * setLabel
	 * setLabel sets the label of the vertex. 
	 * written by: Tim Kelley
	 */


	public void setLabel(String s) throws MATFileFormatException{
		if(s == null || s.equals(""))
			throw new MATFileFormatException("Vertices must have both ID and Label");
		MATVertex.Attributes.put(MATFileProperty.ATTRIBUTE_LABEL, MATFileProperty.TYPE_STRING);

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
		MATVertex.Attributes.put("xpos", "float");
		this.Numeric_Parameters.put("xpos", new Float(f));
	}

	private void setYpos(float f){
		MATVertex.Attributes.put("ypos", "float");
		this.Numeric_Parameters.put("ypos", new Float(f));
	}

	private void setZpos(float f){
		MATVertex.Attributes.put("zpos", "float");
		this.Numeric_Parameters.put("zpos", new Float(f));
	}

	public void setPos(String s, float f) throws MATFileFormatException{
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
			throw new MATFileFormatException("Unknown positional data");
		}
	}

	/*
	 * setShape
	 * setShape attempts to set the shape of a vertex. If the shape is not in the recognized shape list
	 * it throws an exception saying so.
	 * written by: Tim Kelley
	 */

	public void setShape(String s) throws MATFileFormatException{
		if(s != null){
			MATVertex.Attributes.put("shape", "string");
			this.String_Parameters.put("shape", s);
		}
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
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_PHI, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_PHI, new Float(f));
	}
	public void setPhi(String s) {
		float f = MATFileFunctions.asAFloat(s);
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
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_SIZE, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_SIZE, new Float(f));
	}
	public void setSize(String s){
		float f = MATFileFunctions.asAFloat(s);
		this.setSize(f);

	}



	/*
	 * setFont
	 * setFont takes a string and a queue of strings and compares them with the system font list. If the
	 * listed font is not found on the system, it throws an exception, otherwise, it sets the font attribute
	 * in the attribute list and sets the font value for the vertex.
	 * written by: Tim Kelley
	 */

	public void setFont(String s) throws MATFileFormatException{
		if(s!=null)
			MATVertex.Attributes.put(MATFileParameter.PARAMETER_FONT, "string");
		this.String_Parameters.put(MATFileParameter.PARAMETER_FONT, s);
	}

	/*
	 * setBorderColor
	 * setBorderColor takes a string, compares it with a list of recognized colors. if the color is recognized
	 * the function sets the border color attribute in the attribute list and the border color value for the vertex.
	 * Otherwise, it throws an Exception.
	 * written by: Tim Kelley
	 */

	public void setBorderColor(String s) throws MATFileFormatException{
		if(s!= null){
			String[] number = s.split("\\s+");
			if(MATFileFunctions.isAFloat(number[0], "float") || MATFileFunctions.isAnInteger(number[0], "int")){
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_BC, "float");
				this.Numeric_Parameters.put(MATFileParameter.PARAMETER_BC, s);
			}
			else{
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_BC, "string");
				this.String_Parameters.put(MATFileParameter.PARAMETER_BC, s);
			}

		}
	}


	/*
	 * setBorderWidth
	 * this function takes a string, casts it as a float and adds border width to the
	 * attribute list and sets the border width value for the vertex. if the borderwidth is less than 
	 * 0 or the string is not a float, it throws an exception.
	 * written by: Tim Kelley
	 */
	private void setBorderWidth(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_BW, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_BW, new Float(f));
	}
	public void setBorderWidth(String s) {
		float f = MATFileFunctions.asAFloat(s);
		this.setBorderWidth(f);
	}

	/*
	 * setInternalColor
	 * sets the fill color for the vertex based on a string. throws an exception
	 * if the string is not a recognized color.
	 * written by: Tim Kelley
	 */	


	private void setInternalColor(String s) throws MATFileFormatException{
		if(s != null){
			String[] number = s.split("\\s+");
			if(MATFileFunctions.isAFloat(number[0], "float") || MATFileFunctions.isAnInteger(number[0], "int")){
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_IC, "float");
				this.Numeric_Parameters.put(MATFileParameter.PARAMETER_IC, s);
			}
			else{
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_IC, "string");
				this.String_Parameters.put(MATFileParameter.PARAMETER_IC, s);
			}
		}
	}

	/*
	 * setFontSize
	 * takes a string, casts as float, throws exception if less than or equal to
	 * 0 or not a float value
	 * written by: Tim Kelley
	 */

	private void setFontSize(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_FOS, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_FOS,new Float(f));
	}
	public void setFontSize(String s) {
		float f = MATFileFunctions.asAFloat(s);
		this.setFontSize(f);

	}

	/*
	 * setLabelAngle
	 * sets the angle that the label is displayed at, throws exception if
	 * string is not a float and if the float is not between 0 and 360 inclusive.
	 * written by: Tim Kelley
	 */

	private void setLabelAngle(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_LA, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_LA, new Float(f));
	}
	private void setLabelAngle(String s){
		float f = MATFileFunctions.asAFloat(s);
		this.setLabelAngle(f);

	}
	/*
	 * setLabelPhi
	 * sets the second of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelPhi(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_LPHI, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_LPHI, new Float(f));
	}
	private void setLabelPhi(String s){
		float f = MATFileFunctions.asAFloat(s);
		this.setLabelPhi(f);

	}


	/*
	 * setLabelRadius
	 * sets the first of two polar coordinates for positioning the label.
	 * written by: Tim Kelley
	 */
	private void setLabelRadius(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_LR, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_LR, new Float(f));
	}
	public void setLabelRadius(String s){
		float f = MATFileFunctions.asAFloat(s);
		this.setLabelRadius(f);
	}

	/*
	 * setLabelColor
	 * sets the color of the label, throws exception if string is not a recognized
	 * color.
	 * written by: Tim Kelley
	 */
	private void setLabelColor(String s) throws MATFileFormatException {
		if(s!= null){
			String[] number = s.split(" ");
			if(MATFileFunctions.isAFloat(number[0], "float") || MATFileFunctions.isAnInteger(number[0], "int")){
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_LC, "float");
				this.Numeric_Parameters.put(MATFileParameter.PARAMETER_LC, s);
			}
			else{
				MATVertex.Attributes.put(MATFileParameter.PARAMETER_LC, "string");
				this.String_Parameters.put(MATFileParameter.PARAMETER_LC, s);
			}
		}

	}

	/*
	 * set?ScaleFactor
	 * scales the horizontal or vertical dimensions of a shape.
	 * written by: Tim Kelley
	 */
	private void setXScaleFactor(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_X_FACT, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_X_FACT, new Float(f));
	}
	public void setXScaleFactor(String s) {
		float f = MATFileFunctions.asAFloat(s);
		this.setXScaleFactor(f);

	}

	private void setYScaleFactor(float f){
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_Y_FACT, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_Y_FACT, new Float(f));
	}
	public void setYScaleFactor(String s) {	
		float f = MATFileFunctions.asAFloat(s);
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
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_R, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_R, new Float(f));
	}
	private void setCornerRadius(String s) throws MATFileFormatException {

		try {
			float f = MATFileFunctions.asAFloat(s);

			this.setCornerRadius(f);
		}
		catch(NumberFormatException ex){
			throw new MATFileFormatException("This parameter is not used by this vertex's shape");
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
		MATVertex.Attributes.put(MATFileParameter.PARAMETER_Q, "float");
		this.Numeric_Parameters.put(MATFileParameter.PARAMETER_Q, new Float(f));
	}
	public void setDiamondRatio(String s) throws MATFileFormatException{

		try {
			float f = MATFileFunctions.asAFloat(s);
			this.setDiamondRatio(f);
		}
		catch(NumberFormatException ex){
			throw new MATFileFormatException("This parameter is not used by this vertex's shape");
		}

	}

	private void setUnknownAttribute(String s){
		if(s != null){
			String name = "unknown" + this.unknowns;
			MATVertex.Attributes.put(name, MATFileProperty.TYPE_STRING);
			this.String_Parameters.put(name, s);
			this.unknowns++;
		}
	}



	/****************************
	 * Getters
	 ****************************/

	public static List getVertexAttributes(){
		ArrayList attributeList = new ArrayList();
		for(Iterator ii = MATVertex.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			attributeList.add(new MATAttribute(s, (String) MATVertex.Attributes.get(s)));
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

	public String getShape(){
		return (String) this.String_Parameters.get("shape");
	}

	public boolean isValid(){
		return this.valid;
	}

	public Object getAttribute(String s){
		//System.out.println(s);
		String st = (String) MATVertex.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_ID))
			return new Integer(this.getID());
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_LABEL))
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

	private boolean finalCheck() throws MATFileFormatException{

		if((this.getAttribute(MATFileParameter.PARAMETER_LPHI) == null) && (this.getAttribute(MATFileParameter.PARAMETER_LR) == null))
			return true;
		else if((this.getAttribute(MATFileParameter.PARAMETER_LPHI) != null) && (this.getAttribute(MATFileParameter.PARAMETER_LR) != null))
			return true;
		else
			throw new MATFileFormatException("Only one of two polar coordinates has been set");

	}

	/************
	 *  Output
	 *************/

	public String toString(){
		String output = "";

		for(Iterator ii = MATVertex.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
