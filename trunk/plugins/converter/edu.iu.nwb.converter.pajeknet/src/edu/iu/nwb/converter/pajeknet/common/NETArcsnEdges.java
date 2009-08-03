package edu.iu.nwb.converter.pajeknet.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NETArcsnEdges {
	private static Map Attributes = new LinkedHashMap();
	private Map Numeric_Parameters;
	private Map String_Parameters;
	private int source;
	private int target;
	private String comment = null;
	private int unknowns = 0;
	//private String label = null;


	private boolean valid = false;
	public NETArcsnEdges(){
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
	}

	public NETArcsnEdges(String s) throws NETFileFormatException{
		String[] properties = NETFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
		this.valid = testArcsnEdges(properties);
	}

	public boolean testArcsnEdges(String[] strings)
			throws NETFileFormatException {
		boolean value = true;
		Queue stringQueue = new ConcurrentLinkedQueue();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String)stringQueue.peek()).startsWith(NETFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		this.testSourceTargetWeight(stringQueue);

		if(!stringQueue.isEmpty()){
			testParameters(stringQueue);
		}

		return value;
	}

	public boolean testSourceTargetWeight(Queue qs)
			throws NETFileFormatException {
		boolean value = true;

		try{

			if(qs.size() < 2){
				value = false;
				throw new NETFileFormatException(
						"Arcs and edges must contain both source and target values");
			}

			for(int i = 0; i < 3; i++){
				String s = (String) qs.peek();
				value = true;
				switch (i){
				case 0:
					this.setSource(s);
					break;
				case 1:
					this.setTarget(s);
					break;
				case 2:
					try {
						this.setWeight(s);
					} catch(NumberFormatException e) {
						return value;
					}
					break;
				default:
					return true;
				}
				qs.poll();
			}
		} catch(NullPointerException e) {
			this.setWeight("1");
			return value;
		}

		return value;
	}


	public boolean testParameters(Queue qs) {
		boolean value = false;
		while(!qs.isEmpty()){
			String s1 = (String) qs.poll();

			/*if(qs.isEmpty()){
				throw new NETFileFormatException("Expected a value for parameter: " + s1);
			}*/
			String s2 = (String) qs.peek();

			if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_WIDTH)){
				this.setWidth(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_COLOR)){
				if(NETFileFunctions.isAFloat(s2, "float") || NETFileFunctions.isAnInteger(s2, "int")){
					String s = (String)qs.poll();
					s += " " + qs.poll() + " ";
					s += " " + qs.poll();
					this.setColor(s);
				}
				else{
				this.setColor(s2);
				qs.poll();
				}
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_PATTERN)){
				this.setPattern(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_SIZE)){
				this.setSize(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_SHAPE)){
				this.setArrowShape(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_POSITION)){
				this.setArrowPosition(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL)){
				this.setLabel(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_POSITION)){
				this.setLabelPosition(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_RADIUS)){
				this.setLabelRadius(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_PHI)){
				this.setLabelPhi(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_COLOR)){
				if(NETFileFunctions.isAFloat(s2, "float") || NETFileFunctions.isAnInteger(s2, "int")){
					String s = (String)qs.poll();
					s += " " + qs.poll() + " ";
					s += " " + qs.poll();
					this.setLabelColor(s);
				}
				else{
				this.setLabelColor(s2);
				qs.poll();
				}
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_ANGLE)){
				this.setLabelAngle(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT_SIZE)){
				this.setFontSize(s2);
				qs.poll();
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT)){
				this.setFont(s2);
				qs.poll();
			}
			else if(s1.startsWith(ARCEDGEParameter.PARAMETER_HOOK) && NETFileFunctions.isAnInteger(s1.substring(1), "int")){
				this.setHook(s1,s2);
				qs.poll();
			}
			else if(s1.startsWith(ARCEDGEParameter.PARAMETER_ANGLE) && NETFileFunctions.isAnInteger(s1.substring(1), "int")){
				this.setAngle(s1,s2);
				qs.poll();
			}
			else if(s1.startsWith(ARCEDGEParameter.PARAMETER_VELOCITY) && NETFileFunctions.isAnInteger(s1.substring(1),"int")){
				this.setVelocity(s1,s2);
				qs.poll();
			}
			else if(s1.startsWith(NETFileProperty.PREFIX_COMMENTS)){
				qs.clear();
				break;
			}

			else { //add the unknown value as a string parameter.
				this.setUnknownAttribute(s1);
			}
		}
		value = true;
		return value;
	}

	protected static void clearAttributes(){
		NETArcsnEdges.Attributes.clear();
	}

	/*************************
	 *
	 * Setters
	 *
	 *************************/

	public void setUnknownAttribute(String s){
		if(s != null){
		String name = "unknown" + this.unknowns;
		NETArcsnEdges.Attributes.put(name, NETFileProperty.TYPE_STRING);
		this.String_Parameters.put(name, s);
		this.unknowns++;
		}
	}

	public void setSource(String s) throws NETFileFormatException {
		int i = NETFileFunctions.asAnInteger(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new NETFileFormatException(
					"Source id must be greater than 0");
		this.source = i;
	}

	public void setTarget(String s) throws NETFileFormatException {
		int i = NETFileFunctions.asAnInteger(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new NETFileFormatException(
					"Target id must be greater than 0");
		this.target = i;
	}

	public void setWeight(String s) {
		float f = NETFileFunctions.asAFloat(s);
		NETArcsnEdges.Attributes.put(NETFileProperty.ATTRIBUTE_WEIGHT,NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(NETFileProperty.ATTRIBUTE_WEIGHT, new Float(f));
	}


	private void setWidth(float f) {
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_WIDTH, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_WIDTH, new Float(f));
	}
	public void setWidth(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setWidth(f);
	}

	public void setColor(String s) {
		if (s != null) {
			String[] number = s.split(" ");
			if(NETFileFunctions.isAFloat(number[0], "float")
					|| NETFileFunctions.isAnInteger(number[0], "int")){
				NETArcsnEdges.Attributes.put(
						ARCEDGEParameter.PARAMETER_COLOR, "float");
				this.Numeric_Parameters.put(
						ARCEDGEParameter.PARAMETER_COLOR, s);
			}
			else{
				NETArcsnEdges.Attributes.put(
						ARCEDGEParameter.PARAMETER_COLOR,
						NETFileProperty.TYPE_STRING);
				this.String_Parameters.put(
						ARCEDGEParameter.PARAMETER_COLOR, s);
			}
		}
	}

	public void setPattern(String s) {
		if(s != null){
			NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_PATTERN, NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_PATTERN, s);
		}
	}

	private void setSize(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_SIZE, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_SIZE, new Float(f));
	}
	public void setSize(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setSize(f);
	}

	public void setArrowShape(String s) {
		if(s != null){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, NETFileProperty.TYPE_STRING);
		this.String_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, s);
		}
	}

	private void setArrowPosition(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, new Float(f));
	}
	public void setArrowPosition(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setArrowPosition(f);
	}

	public void setLabel(String s) {
		if(s != null){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL, NETFileProperty.TYPE_STRING);
		this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL, s);
		}
	}

	private void setLabelPosition(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, new Float(f));
	}
	public void setLabelPosition(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelPosition(f);
	}

	private void setLabelRadius(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, new Float(f));
	}
	public void setLabelRadius(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelRadius(f);
	}

	private void setLabelPhi(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, new Float(f));
	}
	public void setLabelPhi(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelPhi(f);
	}

	public void setLabelColor(String s) {
		if(s != null) {
			String[] number = s.split(" ");
			if(NETFileFunctions.isAFloat(number[0], "float") || NETFileFunctions.isAnInteger(number[0], "int")){
				NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, "float");
				this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, s);
			}
			else{
				NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, NETFileProperty.TYPE_STRING);
				this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, s);
			}
		}
	}

	private void setLabelAngle(float f){
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, NETFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, new Float(f));
	}
	public void setLabelAngle(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setLabelAngle(f);
	}

	private void setFontSize(float f) {
		NETArcsnEdges.Attributes.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, new Float(f));
	}

	public void setFontSize(String s) {
		float f = NETFileFunctions.asAFloat(s);
		this.setFontSize(f);
	}

	public void setFont(String s) {
		if(s != null){
			NETArcsnEdges.Attributes.put(
					ARCEDGEParameter.PARAMETER_FONT,
					NETFileProperty.TYPE_STRING);
			this.String_Parameters.put(NETFileParameter.PARAMETER_FONT, s);
		}
	}

	public void setHook(String s1, String s2) {
		NETArcsnEdges.Attributes.put(s1, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(
				s1, new Float(NETFileFunctions.asAFloat(s2)));
	}

	public void setAngle(String s1, String s2) {
			NETArcsnEdges.Attributes.put(s1, NETFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(
					s1, new Float(NETFileFunctions.asAFloat(s2)));

	}

	public void setVelocity(String s1, String s2) {
		NETArcsnEdges.Attributes.put(s1, NETFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(
				s1, new Float(NETFileFunctions.asAFloat(s2)));
	}

	/******
	 * Getters
	 ******/

	public Object getAttribute(String s){
		String st = (String) NETArcsnEdges.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_SOURCE))
			return new Integer(this.source);
		else if(s.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_TARGET))
			return new Integer(this.target);
		else if(st.equalsIgnoreCase("float"))
			return this.Numeric_Parameters.get(s);
		else
			return this.String_Parameters.get(s);
	}

	public boolean isValid(){
		return this.valid;
	}

	public static List getArcsnEdgesAttributes(){
		ArrayList attributeList = new ArrayList();
		for(Iterator ii = NETArcsnEdges.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			attributeList.add(new NETAttribute(s, (String) NETArcsnEdges.Attributes.get(s)));
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

		for(Iterator ii = NETArcsnEdges.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
