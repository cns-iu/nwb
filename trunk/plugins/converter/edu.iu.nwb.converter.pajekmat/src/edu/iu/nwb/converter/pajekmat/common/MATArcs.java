package edu.iu.nwb.converter.pajekmat.common;

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
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();	
	}

	public MATArcs(String s) throws Exception{
		String[] properties = MATFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
		this.valid = testArcsnEdges(properties);
	}

	public boolean testArcsnEdges(String[] strings) throws Exception{
		boolean value = false;
		Queue stringQueue = new ConcurrentLinkedQueue();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String)stringQueue.peek()).startsWith(MATFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		try{

			this.testSourceTargetWeight(stringQueue);

			if(!stringQueue.isEmpty()){

			}

		}catch(Exception ex){
			throw ex;
		}
		return value;
	}

	public boolean testSourceTargetWeight(Queue qs) throws Exception{
		boolean value = false;
		int i = 0;
		try{
			while(!qs.isEmpty()){
			
					switch (i){
					case 0:
						this.setSource((String) qs.poll());
						break;
					case 1:
						this.setTarget((String) qs.poll());
						break;
					case 2:
						this.setWeight((String) qs.poll());
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
		value = true;
		return value;
	}
	

	protected static void clearAttributes(){
		MATArcs.Attributes.clear();
	}
	
	/*************************
	 * 
	 * Setters
	 * 
	 *************************/

	public void setSource(String s) throws Exception {
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_SOURCE, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Source id must be greater than 0");
		this.source = i;
	}
	
	public void setTarget(String s) throws Exception{
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_TARGET, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Target id must be greater than 0");
		this.target = i;
	}

	public void setWeight(String s) throws Exception{
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
