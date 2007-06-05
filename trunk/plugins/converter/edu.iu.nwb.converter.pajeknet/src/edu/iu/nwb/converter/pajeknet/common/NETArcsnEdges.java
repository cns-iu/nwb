package edu.iu.nwb.converter.pajeknet.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NETArcsnEdges {
	private static Map<String,String> Attributes = new LinkedHashMap<String,String>();
	private Map<String, Float> Numeric_Parameters;
	private Map<String, String> String_Parameters;
	private int source;
	private int target;
	private String comment = null;
	private String label = null;


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

			this.testSourceTarget(stringQueue);

			if(!stringQueue.isEmpty()){


				testParameters(stringQueue);

			}

		}catch(Exception ex){
			throw ex;
		}
		value = true;
		return value;
	}

	public boolean testSourceTarget(Queue<String> qs) throws Exception{
		boolean value = false;

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
				this.setFont(s2);
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

	/*************************
	 * 
	 * Setters
	 * 
	 *************************/
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
			throw new Exception(s + " is not a valid color selection");
	}
	
	public void setPattern(String s) throws Exception {
		
	}
	
	public void setSize(String s) throws Exception {
		
	}
	
	public void setArrowShape(String s) throws Exception {
		
	}
	
	public void setArrowPosition(String s) throws Exception {
		
	}
	
	public void setLabel(String s) throws Exception {
		
	}
	
	public void setLabelPosition(String s) throws Exception {
		
	}
	
	public void setLabelRadius(String s) throws Exception {
		
	}
	
	public void setLabelPhi(String s) throws Exception {
		
	}
	
	public void setLabelColor(String s) throws Exception {
		
	}
	
	public void setLabelAngle(String s) throws Exception {
		
	}
	
	public void setFontSize(String s) throws Exception {
		
	}
	
	public void setFont(String s) throws Exception {
		
	}
	
	public void setHook(String s, int i) throws Exception {
		
	}
	
	public void setAngle(String s, int i) throws Exception {
	
	}
	
	public void setVelocity(String s, int i) throws Exception{
		
	}
	
	

}
