package edu.iu.nwb.converter.pajeknet.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NETVertex {
	private static Map<String,String> Attributes = new LinkedHashMap<String,String>();
	private int id = -1;
	
	private float xpos;
	private float ypos;
	private float zpos;

	private float size;
	private float x_fact;
	private float y_fact;
	private float phi;
	private float r;
	private float q;
	private float bw;
	private float la;
	private float lr;
	private float lphi;
	private float fos;

	private String label = null;
	private String ic;
	private String bc;
	private String font;
	private String shape;

	private boolean valid = false;

	public NETVertex(){

	}

	public NETVertex(String s) throws Exception {
		String[] properties = NETFileFunctions.processTokens(s);

		this.valid = testVertices(properties);
		
	}

	public boolean testVertices(String...strings) throws Exception{
		/*for(NETAttribute na : this.vertexAttrList){
			System.out.println(na.getAttrName() + " " + na.getDataType());
		}*/
		String[] sa;
		boolean value = false;
		boolean checkPos = true;
		Queue<String> stringQueue = new ConcurrentLinkedQueue<String>();
		for(String s : strings){
			//System.out.println(s);
			stringQueue.add(s);
		}
		if(stringQueue.peek().startsWith(NETFileProperty.PREFIX_COMMENTS))
			return true;
		ArrayList<String> testConditions = new ArrayList<String>();
		testConditions.add(stringQueue.poll());
		testConditions.add(stringQueue.poll());

		try{
			sa = new String[testConditions.size()];
			this.testVertexID(testConditions.toArray(sa));
		}
		catch(Exception ex){
		
			throw ex;
		}
		testConditions.clear();
		if(!stringQueue.isEmpty()){
			try {
				String s = stringQueue.peek();
				try{
					NETFileFunctions.isAFloat(s, null);
				} catch(NumberFormatException ex){
					checkPos = false;
				}
				if(checkPos){
					if(stringQueue.size() > 2){
						for(int i = 0; i < 3; i++){
							testConditions.add(stringQueue.poll());
						}
					}
					else {
						throw new Exception("If a vertex contains positional data it must contain positional data for three dimensions.");
					}
					sa = new String[testConditions.size()];

					this.testVertexPosition((String[])testConditions.toArray(sa));
					
					if(!this.testVertexShape(stringQueue.peek())){

						//testVertexDefaults(stringQueue);
						testParameters(stringQueue);
					}
					else {
						stringQueue.poll();
						//testVertexDefaults(stringQueue);
						testParameters(stringQueue);
					}
				}
				else{
					String str = stringQueue.peek();
					if(!this.testVertexShape(str)){

						testVertexDefaults(stringQueue);
					}
					else {

						stringQueue.poll();
						testVertexDefaults(stringQueue);
					}
				}

			}
			catch(Exception ex){
				throw ex;
			}
		}
		value = true;
		return value;
	}

	public boolean testVertexDefaults(Queue<String> qs) throws Exception{

		boolean value = false;
		while(!qs.isEmpty()){
			String s1 = qs.poll();

			if(qs.isEmpty()){
				throw new Exception("Expected a value for parameter: " + s1);
			}
			String s2 = qs.poll();

			System.out.println(s1 + " " + s2+"\n------------\n");
			boolean b1 = false ,b2 = false;
			if(ValidateNETFile.isInList(s1,NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST)){

				try{
					b1 = NETFileFunctions.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
					b2 = NETFileFunctions.isAnInteger(s2, NETFileProperty.TYPE_INT);
				}

				catch(NumberFormatException ex){
				}
				if((!(b1 || b2))){
					throw new Exception("Unexpected value: " + s2 + " for parameter " + s1);
				}


			}
			else if(ValidateNETFile.isInList(s1,NETFileParameter.VERTEX_COLOR_PARAMETER_LIST)){


				try{
					b1 = NETFileFunctions.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
					b2 = NETFileFunctions.isAnInteger(s2, NETFileProperty.TYPE_INT);

				}
				catch(NumberFormatException ex){

				}
				if((b1 || b2)){
					value = false;
					throw new Exception("Expected a String but received a number ("+ s2 + ") for parameter: " + s1);

				}

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

	public boolean testVertexID(String[] s) throws Exception{
		try
		{
			NETFileFunctions.isAnInteger(s[0], NETFileProperty.ATTRIBUTE_ID);
			NETFileFunctions.isAString (s[1], NETFileProperty.ATTRIBUTE_LABEL);
			this.setID(new Integer(s[0]).intValue());
			this.setLabel(s[1]);
			NETAttribute netAttr = new NETAttribute
			(NETFileProperty.ATTRIBUTE_ID, NETFileProperty.TYPE_INT);	

			Attributes.put(netAttr.getAttrName(), netAttr.getDataType());
			netAttr = new NETAttribute(NETFileProperty.ATTRIBUTE_LABEL, NETFileProperty.TYPE_STRING);
			Attributes.put(netAttr.getAttrName(), netAttr.getDataType());
			return true;
		}
		catch(Exception ex){
			throw ex;
		}
	}

	public boolean testVertexPosition(String...st) throws Exception{
		int length = st.length;
		boolean value = false;
		float f = 0;
		int i = 0;
		try{
			for(i = 0; i < length; i++){
				f = Float.parseFloat(st[i]);
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

			}

		}
		catch(NumberFormatException ex){
			if((i > 0) && (i < 3)){
				value = false;
				throw new Exception("The file contains an invalid sequence in the positional data.");
			}
			else
				value = false;
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

			//System.out.println(s1 + " " + s2+"\n------------\n");
			
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
				
			}
			boolean b1 = false ,b2 = false;
			if(ValidateNETFile.isInList(s1,NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST)){

				try{
					b1 = NETFileFunctions.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
					b2 = NETFileFunctions.isAnInteger(s2, NETFileProperty.TYPE_INT);
				}

				catch(NumberFormatException ex){
				}
				if((!(b1 || b2))){
					throw new Exception("Unexpected value: " + s2 + " for parameter " + s1);
				}


			}
			else if(ValidateNETFile.isInList(s1,NETFileParameter.VERTEX_COLOR_PARAMETER_LIST)){


				try{
					b1 = NETFileFunctions.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
					b2 = NETFileFunctions.isAnInteger(s2, NETFileProperty.TYPE_INT);

				}
				catch(NumberFormatException ex){

				}
				if((b1 || b2)){
					value = false;
					throw new Exception("Expected a String but received a number ("+ s2 + ") for parameter: " + s1);

				}

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
	
	private void setID(int i) throws Exception{
		if(i < 0){
			throw new NumberFormatException("Vertex ID must be greater than 0");
		}
		this.id = i;
	}
	
	private void setLabel(String s){
		this.label = s;
	}
	
	private void setXpos(float f){
		NETVertex.Attributes.put("xpos", "float");
		this.xpos = f;
	}
	
	private void setYpos(float f){
		NETVertex.Attributes.put("ypos", "float");
		this.ypos = f;
	}
	
	private void setZpos(float f){
		NETVertex.Attributes.put("zpos", "float");
		this.zpos = f;
	}
	
	private void setPos(String s, float f){
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
	
	private void setShape(String s){
		NETVertex.Attributes.put("shape", "string");
		this.shape = s;
	}
	
	
	
	private void setPhi(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_PHI, "float");
		this.phi = f;
	}
	private void setPhi(String s) throws Exception{
			float f = NETFileFunctions.asAFloat(s);
			if((f >= 0) && (f <= 360))
				this.setPhi(f);
			else
				throw new Exception("Value must be between 0 and 360, inclusive");
	}
	
	
	private void setSize(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_SIZE, "string");
		this.size = f;
	}
	
	private void setSize(String s) throws Exception{
			float f = NETFileFunctions.asAFloat(s);
			if(f > 0)
				this.setSize(f);
			else
				throw new Exception("Size must be greater than 0.0");
	}
	
	
	private void setFont(String s){
		NETVertex.Attributes.put("font", "string");
		this.font = s;
	}
	
	
	private void setBorderColor(String s){
		NETVertex.Attributes.put("bc", "string");
		this.bc = s;
	}
	
	private void setBorderWidth(float f){
		NETVertex.Attributes.put("bw", "float");
		this.bw = f;
	}
	
	private void setInternalColor(String s){
		NETVertex.Attributes.put("ic", "string");
		this.ic = s;
	}
	private void setFontSize(float f){
		NETVertex.Attributes.put("fos", "float");
		this.fos = f;
	}

	private void setLabelAngle(float f){
		NETVertex.Attributes.put("la", "float");
		this.la = f;
	}
	
	private void setLabelPhi(float f){
		NETVertex.Attributes.put("lphi", "float");
		this.lphi = f;
	}
	
	private void setLabelRadius(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_LR, "float");
		this.lr = f;
	}
	

	
	
	private void setXScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_X_FACT, "float");
		this.x_fact = f;
	}
	
	private void setXScaleFactor(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
		this.setXScaleFactor(f);
		else
			throw new Exception("Scale factor must be greater than 0.0");		
	}
	
	
	private void setYScaleFactor(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_Y_FACT, "float");
		this.y_fact = f;
	}
	
	private void setYScaleFactor(String s) throws Exception{	
			float f = NETFileFunctions.asAFloat(s);
			if(f > 0)
			this.setYScaleFactor(f);
			else
				throw new Exception("Scale factor must be greater than 0.0");		
	}
	
	
	private void setCornerRadius(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_R, "float");
		this.r = f;
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
	
	private void setDiamondRatio(float f){
		NETVertex.Attributes.put(NETFileParameter.PARAMETER_Q, "float");
		this.q = f;
	}
	private void setDiamondRatio(String s) throws Exception{
		float f = NETFileFunctions.asAFloat(s);
		if(f > 0)
			this.setCornerRadius(f);
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
		return this.xpos;
	}
	
	public float getYpos(){
		return this.ypos;
	}
	
	public float getZpos(){
		return this.zpos;
	}
	
	public String getShape() throws Exception{
		return this.shape;
	}
	
	public boolean isValid(){
		return this.valid;
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
}
