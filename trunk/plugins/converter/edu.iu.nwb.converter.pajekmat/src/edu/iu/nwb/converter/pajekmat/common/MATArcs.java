package edu.iu.nwb.converter.pajekmat.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;


public class MATArcs {
	private static Map Attributes = new LinkedHashMap();
	private Map Numeric_Parameters;
	private Map String_Parameters;
	private int source;
	private int target;
	private String comment = null;
	//private String label = null;


	private boolean valid = false;
	public MATArcs(){	
		this.Numeric_Parameters = new HashMap();
		this.String_Parameters = new HashMap();	
	}

	public MATArcs(String s) throws AlgorithmExecutionException {
		String[] properties = MATFileFunctions.processTokens(s);
		this.Numeric_Parameters = new HashMap();
		this.String_Parameters = new HashMap();
		this.valid = testArcsnEdges(properties);
	}

	public boolean testArcsnEdges(String[] strings) throws AlgorithmExecutionException {
		boolean value = false;
		LinkedList stringQueue = new LinkedList();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String)stringQueue.getFirst()).startsWith(MATFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		this.testSourceTargetWeight(stringQueue);

		if(!stringQueue.isEmpty()) {
			// Do nothing?
		}
			
		return value;
	}

	public boolean testSourceTargetWeight(LinkedList qs) throws AlgorithmExecutionException {
		boolean value = false;
		int i = 0;
		try{
			while(!qs.isEmpty()){
			
					switch (i){
					case 0:
						this.setSource((String) qs.removeFirst());
						break;
					case 1:
						this.setTarget((String) qs.removeFirst());
						break;
					case 2:
						this.setWeight((String) qs.removeFirst());
						break;
					default:
						throw new AlgorithmExecutionException("Unknown data found");
					}		
				i++;
			}
			switch(i){
			case 0:
			case 1:
				throw new AlgorithmExecutionException("Arcs and edges must contain both source and target values");
			case 2:
				this.setWeight("1");
				break;
			}
		} catch (NumberFormatException e) {
			throw new AlgorithmExecutionException("The file contains an invalid sequence in the positional data.", e);
		}
		value = true;
		return value;
	}
	

	protected static void clearAttributes(){
		MATArcs.Attributes.clear();
	}
	
	/*************************
	 * 
	 * Setters
	 * @throws AlgorithmExecutionException 
	 * 
	 *************************/

	public void setSource(String s) throws AlgorithmExecutionException {
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_SOURCE, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new AlgorithmExecutionException("Source id must be greater than 0");
		this.source = i;
	}
	
	public void setTarget(String s) throws AlgorithmExecutionException {
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_TARGET, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new AlgorithmExecutionException("Target id must be greater than 0");
		this.target = i;
	}

	public void setWeight(String s) {
		float f = MATFileFunctions.asAFloat(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_WEIGHT,MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(MATFileProperty.ATTRIBUTE_WEIGHT, new Float(f));
	}
	
	
	
	/******
	 * Getters
	 ******/
	
	public Object getAttribute(String s){
		String st = (String) MATArcs.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_SOURCE))
			return new Integer(this.source);
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_TARGET))
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
		for(Iterator ii = MATArcs.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			attributeList.add(new MATAttribute(s, (String) MATArcs.Attributes.get(s)));
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

		for(Iterator ii = MATArcs.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}
