package edu.iu.nwb.converter.pajekmat.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MATFileFunctions {
	public static String[] processTokens(String s){
		String str = s.trim();
		//System.out.println("::"+str+"::");
		String[] tokens = str.split("\\s+");
		ArrayList sl = new ArrayList();
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for(int ii = 0; ii < tokens.length; ii++){
			String st = tokens[ii];
			if(!append){
				if(!st.startsWith("\"")){
					st = st.replace("\"", "");
					sl.add(st);
				}
				else if (st.startsWith("\"") && st.endsWith("\"")){
					st = st.replace("\"", "");
					sl.add(st);
				}
				else {
					append = true;
					bf.append(st);
				}
			}
			else{
				if(st.endsWith("\\\"") ||
						!st.endsWith("\"")	){
					bf.append(" "+st);
				}
				else if (st.endsWith("\"")){    				
					bf.append(" "+st);
					sl.add(bf.toString().replace("\"", ""));
					bf = new StringBuffer();
					append=false;
				}

			}
		}
		tokens = new String[sl.size()];
		return (String[]) sl.toArray(tokens);
	}


	protected static String[] processTokens(StringTokenizer st){
		int total = st.countTokens();
		int tokenIndex =0;
		String [] tokens = new String [total];
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for (int index =0; index<total; index++){
			String element = st.nextToken();
			//System.out.print("\""+element+"\"\t");
			if (!append){
				if (!element.startsWith("\"")){
					tokens[tokenIndex]= element;
					tokenIndex++;

				}
				else if (element.startsWith("\"") &&
						element.endsWith("\"")){
					tokens[tokenIndex]= element;
					tokenIndex++;

				}
				else{
					append = true;
					bf.append(element);
				}    			
			}
			else {
				if(element.endsWith("\\\"") ||
						!element.endsWith("\"")	){
					bf.append(" "+element);
				}
				else if (element.endsWith("\"")){    				
					bf.append(" "+element);
					tokens[tokenIndex]= bf.toString();
					tokenIndex++;
					bf = new StringBuffer();
					append=false;
				}
			}
		}

		//	System.out.println();

		return tokens;


	}
	
	protected static int asAnInteger(String input) throws NumberFormatException{
		int i = new Integer(input).intValue();
		return i;
	}

	protected static boolean isAnInteger(String input, String attr) throws NumberFormatException, Exception{
		Integer value = new Integer (input);    
		if (attr.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_ID) ||
				attr.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_SOURCE) ||
				attr.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_TARGET) ){
			if (value.intValue()<1)
				throw new Exception("The node id must be greater than 0.");
		}    		
		return true;    	
	}    

	protected static boolean isAString(String input, String attr) throws Exception {
		/*if (!input.startsWith("\"") || !input.endsWith("\"")) {
			
		}   */ 	
		if(input.getClass().toString().endsWith("String"))
		return true;
		throw new Exception("Not a String value.");
	}

	protected static float asAFloat(String input) throws NumberFormatException{
		float f = new Float(input).floatValue();
		return f;
	}
	protected static boolean isAFloat (String input, String attr) throws NumberFormatException, Exception {
		asAFloat(input);
		return true;
	}

	public static boolean isInList(String s, String[] strings){
		boolean value = false;
		if(strings != null){
			for(int ii = 0; ii < strings.length; ii++){
				String st = strings[ii];
				if(s.equalsIgnoreCase(st))
					return true;
			}
		}
		return value;
	}
	
}
