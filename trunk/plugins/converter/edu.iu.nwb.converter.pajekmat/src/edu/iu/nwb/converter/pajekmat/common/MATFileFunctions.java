package edu.iu.nwb.converter.pajekmat.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

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
					//st.replaceFirst("\"", "");
					sl.add(st);
				}
				else if (st.startsWith("\"") && st.endsWith("\"")){
					st = st.replaceFirst("\"", "");
					int lastIndex = st.lastIndexOf("\"");
					sl.add(st.substring(0,lastIndex));
				}
				else {
					append = true;
					st.replaceFirst("\"", "");
					bf.append(st);
				}
			}
			else{
				if(st.endsWith("\\\"") ||
						!st.endsWith("\"")	){
					bf.append(" "+st);
				}
				else if (st.endsWith("\"")){    				
					int lastIndex = st.lastIndexOf("\"");
					bf.append(" "+st.substring(0,lastIndex));
					sl.add(bf.toString());
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

		return tokens;


	}
	
	protected static int asAnInteger(String input) throws NumberFormatException{
		return new Integer(input).intValue();
	}

	protected static boolean isAnInteger(String input, String attr){
		try{
		asAnInteger(input);        		
		return true;
		}catch(NumberFormatException nfe){
			return false;
		}
	}    

	protected static boolean isAString(String input, String attr)
			throws AlgorithmExecutionException {
		if(input.getClass().toString().endsWith("String")) {
			return true;
		}
		else {
			throw new AlgorithmExecutionException("Not a String value.");
		}
	}

	protected static float asAFloat(String input) throws NumberFormatException{
		float f = new Float(input).floatValue();
		return f;
	}
	
	protected static boolean isAFloat (String input, String attr){
		try {
			asAFloat(input);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
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
