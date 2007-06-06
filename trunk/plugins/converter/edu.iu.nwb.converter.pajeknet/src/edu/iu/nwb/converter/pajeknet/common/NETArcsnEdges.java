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

public class NETArcsnEdges {
	private static Map<String,String> Attributes = new LinkedHashMap<String,String>();
	private Map<String, Float> Numeric_Parameters;
	private Map<String, String> String_Parameters;
	private int source;
	private int target;
	private String comment = null;
	//private String label = null;


	private boolean valid = false;
	public NETArcsnEdges(){	
		this.Numeric_Parameters = new ConcurrentHashMap<String, Float>();
		this.String_Parameters = new ConcurrentHashMap<String,String>();	
	}

	public NETArcsnEdges(String s) throws Exception{
		String[] properties = NETFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap<String, Float>();
		this.String_Parameters = new ConcurrentHashMap<String,String>();
		this.valid = testArcsnEdges(properties);
	}

	public boolean testArcsnEdges(String...strings) throws Exception{
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

			this.testSourceTargetWeight(stringQueue);

			if(!stringQueue.isEmpty()){


				testParameters(stringQueue);

			}

		}catch(Exception ex){
			throw ex;
		}
		value = finalTest();
		return value;
	}

	public boolean testSourceTargetWeight(Queue<String> qs) throws Exception{
		boolean value = true;
		int i = 0;
		try{
			while(!qs.isEmpty()){
				if((NETFileFunctions.isInList(qs.peek(), ARCEDGEParameter.ARCEDGE_NUMERIC_PARAMETER_LIST)) || (NETFileFunctions.isInList(qs.peek(), ARCEDGEParameter.ARCEDGE_STRING_PARAMETER_LIST))){
					break;
				}
					switch (i){
					case 0:
						this.setSource(qs.poll());
						break;
					case 1:
						this.setTarget(qs.poll());
						break;
					case 2:
						this.setWeight(qs.poll());
						break;
					default:
						throw new Exception("Unknown data found");
					}		
				i++;
			}
			switch(i){
			case 0:
			case 1:
				throw new Exception("Arcs and edges must contain both source and target values");
			case 2:
				this.setWeight("1");
				break;
			}
		}
		catch(NumberFormatException ex){
			throw new Exception("The file contains an invalid sequence in the positional data.");
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


			if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_WIDTH)){
				this.setWidth(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_COLOR)){
				this.setColor(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_PATTERN)){
				this.setPattern(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_SIZE)){
				this.setSize(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_SHAPE)){
				this.setArrowShape(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_POSITION)){
				this.setArrowPosition(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL)){
				this.setLabel(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_POSITION)){
				this.setLabelPosition(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_RADIUS)){
				this.setLabelRadius(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_PHI)){
				this.setLabelPhi(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_COLOR)){
				this.setLabelColor(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_ANGLE)){
				this.setLabelAngle(s2);	
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT_SIZE)){
				this.setFontSize(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT)){
				this.setFont(s2,qs);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_HOOK_ONE)){
				this.setHook(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_HOOK_TWO)){
				this.setHook(s2,2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ANGLE_ONE)){
				this.setAngle(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ANGLE_TWO)){
				this.setAngle(s2,2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_VELOCITY_ONE)){
				this.setVelocity(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_VELOCITY_TWO)){
				this.setVelocity(s2,2);
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

private boolean finalTest() throws Exception{
	if((this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_PHI) == null) && (this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_RADIUS) == null))
		return true;
	else if((this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_PHI) != null) && (this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_RADIUS) != null))
		return true;
	else
		throw new Exception("Only one of two polar coordinates has been set");
}
	
	/*************************
	 * 
	 * Setters
	 * 
	 *************************/

	public void setSource(String s) throws Exception {
		int i = NETFileFunctions.asAnInteger(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Source id must be greater than 0");
		this.source = i;
	}
	
	public void setTarget(String s) throws Exception{
		int i = NETFileFunctions.asAnInteger(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Target id must be greater than 0");
		this.target = i;
	}

	public void setWeight(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_WEIGHT,NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(NETFileProperty.ATTRIBUTE_WEIGHT, f);
	}
	
	
	private void setWidth(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_WIDTH, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_WIDTH, f);
	}
	public void setWidth(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setWidth(f);
		else
			throw new Exception("Line width must be greater than 0.0");
	}
	
	public void setColor(String s) throws Exception {
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_COLOR, NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_COLOR, s);
		}
		else
			throw new Exception(s + " is not a valid color selection.");
	}
	
	public void setPattern(String s) throws Exception {
		if(s.equals("Solid") || s.equals("Dots")){
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_PATTERN, NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_PATTERN, s);
		}
		else
			throw new Exception(s + " is an unrecognized pattern.");
	}
	
	private void setSize(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_SIZE, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_SIZE, f);
	}
	public void setSize(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!(f>0))
			throw new Exception("Arrow size must be greater than 0");
		this.setSize(f);
	}
	
	public void setArrowShape(String s) throws Exception {
		if(s.equals("A") || s.equals("B")){
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, s);
		}
		else
			throw new Exception(s + " is an unrecognized arrow shape");
	}
	
	private void setArrowPosition(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, f);
	}
	public void setArrowPosition(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Arrow position must be greater than 0.");
		this.setArrowPosition(f);
	}
	
	public void setLabel(String s) {
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL, NETFileProperty.TYPE_STRING);
		this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL, s);
	}
	
	private void setLabelPosition(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, f);
	}
	public void setLabelPosition(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Label position must be greater than 0");
		this.setLabelPosition(f);
	}
	
	private void setLabelRadius(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, f);
	}
	public void setLabelRadius(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Label radius must be greater than 0.");
		this.setLabelRadius(f);
	}
	
	private void setLabelPhi(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, f);
	}
	public void setLabelPhi(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!((f >= 0) && (f <= 360)))
			throw new Exception("The label phi must be between 0 and 360 inclusive");
		this.setLabelPhi(f);
	}
	
	public void setLabelColor(String s) throws Exception {
		if(NETFileFunctions.isInList(s, NETFileColor.VERTEX_COLOR_LIST)){
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, s);
		}
		else
			throw new Exception(s + " is not a valid color selection.");
	}

	private void setLabelAngle(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, f);
	}
	public void setLabelAngle(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!((f >= 0) && (f <= 360)))
			throw new Exception("The label angle must be between 0 and 360 inclusive");
		this.setLabelAngle(f);
	}
	
	private void setFontSize(float f) throws Exception {
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, f);
	}
	public void setFontSize(String s) throws Exception {
		float f = NETFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Font size must be greater than 0");
		this.setFontSize(f);
	}
	
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
	public void setFont(String s, Queue<String> qs) throws Exception {
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
			else if(NETFileFunctions.isInList(peekValue, ARCEDGEParameter.ARCEDGE_NUMERIC_PARAMETER_LIST) || NETFileFunctions.isInList(peekValue, ARCEDGEParameter.ARCEDGE_STRING_PARAMETER_LIST))
				break;
			compare = compare + " " + qs.poll();
			compareList = this.setFontPrime(compare, compareList);
		}
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_FONT, NETFileProperty.TYPE_STRING);
		this.String_Parameters.put(NETFileParameter.PARAMETER_FONT, compare);
	}
	
	public void setHook(String s, int i) throws Exception {
		switch(i){
		case 1:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_HOOK_ONE, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_HOOK_ONE, NETFileFunctions.asAFloat(s));
			break;
		case 2:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_HOOK_TWO, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_HOOK_TWO, NETFileFunctions.asAFloat(s));
			default:
				throw new Exception("Unknown hook h"+i);
		}
	}
	
	public void setAngle(String s, int i) throws Exception {
		switch(i){
		case 1:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ANGLE_ONE, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ANGLE_ONE, NETFileFunctions.asAFloat(s));
			break;
		case 2:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ANGLE_TWO, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ANGLE_TWO, NETFileFunctions.asAFloat(s));
			default:
				throw new Exception("Unknown angle a"+i);
		}
	}
	
	public void setVelocity(String s, int i) throws Exception{
		switch(i){
		case 1:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_VELOCITY_ONE, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_VELOCITY_ONE, NETFileFunctions.asAFloat(s));
			break;
		case 2:
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_VELOCITY_TWO, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_VELOCITY_TWO, NETFileFunctions.asAFloat(s));
			default:
				throw new Exception("Unknown velocity k"+i);
		}
	}
	
	/******
	 * Getters
	 ******/
	
	public Object getAttribute(String s){
		String st = NETArcsnEdges.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_SOURCE))
			return this.source;
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_TARGET))
			return this.target;
		else if(st.equalsIgnoreCase("float"))
			return this.Numeric_Parameters.get(s);
		else 
			return this.String_Parameters.get(s);
	}
	
	public boolean isValid(){
		return this.valid;
	}
	
	public static List<NETAttribute> getArcsnEdgesAttributes(){
		ArrayList<NETAttribute> attributeList = new ArrayList<NETAttribute>();
		for(String s : NETArcsnEdges.Attributes.keySet()){
			attributeList.add(new NETAttribute(s,NETArcsnEdges.Attributes.get(s)));
		}
		return attributeList;

	}
	
	/*****
	 * 
	 * Output
	 * 
	 */
	
	public String toString(){
		String output = "";

		for(String s : NETArcsnEdges.Attributes.keySet()){
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
