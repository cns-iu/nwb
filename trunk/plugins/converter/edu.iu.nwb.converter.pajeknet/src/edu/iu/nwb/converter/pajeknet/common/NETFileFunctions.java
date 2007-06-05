package edu.iu.nwb.converter.pajeknet.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NETFileFunctions {
	protected static String[] processTokens(String s){
		String[] tokens = s.split("\\s+");
		ArrayList<String> sl = new ArrayList<String>();
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for(String st: tokens){
			if(!append){
				if(!st.startsWith("\"")){
					sl.add(st);
				}
				else if (st.startsWith("\"") && st.endsWith("\"")){
					st.replace("\"", "");
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
		return sl.toArray(tokens);
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

	protected static boolean isAnInteger(String input, String attr) throws NumberFormatException, Exception{
		Integer value = new Integer (input);    
		if (attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_ID) ||
				attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_SOURCE) ||
				attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_TARGET) ){
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
		Float f = new Float(input).floatValue();
		return f;
	}
	protected static boolean isAFloat (String input, String attr) throws NumberFormatException, Exception {
		asAFloat(input);
		return true;
	}

	protected static boolean isInList(String s, String...strings){
		boolean value = false;
		if(strings != null){
			for(String st : strings){
				if(s.equalsIgnoreCase(st))
					return true;
			}
		}
		return value;
	}
	
}
