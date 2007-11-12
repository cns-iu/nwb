package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.Iterator;
import java.util.TreeSet;

public class CoValued {
	
	Object firstValue;
	Object secondValue;
	boolean directed = false;
	
	public CoValued(Object s1, Object s2, boolean d){
		firstValue = s1;
		secondValue = s2;
		directed = d;
	}
	
	
	public boolean equals(Object o){
		
		
		if(o.getClass().equals(this.getClass())){
			CoValued ca = (CoValued)o;
			if(!directed){
				//If the values are strings, I don't care about case, but, if we want to trust all data,
				//this can be changed.
			if(ca.firstValue.getClass().equals(String.class) && ca.secondValue.getClass().equals(String.class)){
				String s1 = (String)this.firstValue;
				String s2 = (String)ca.firstValue;
				String s3 = (String)this.secondValue;
				String s4 = (String)ca.secondValue;
			if((s1.equalsIgnoreCase(s2) && s3.equalsIgnoreCase(s4))
					|| (s1.equalsIgnoreCase(s4) && s3.equalsIgnoreCase(s2))){
				return true;
			}
			return false;
			}
			else{
				return (this.firstValue.equals(ca.firstValue) && this.secondValue.equals(ca.secondValue) || 
						(this.firstValue.equals(ca.secondValue) && this.secondValue.equals(ca.secondValue)));
			}
			}
			else{
				if(ca.firstValue.getClass().equals(String.class) && ca.secondValue.getClass().equals(String.class)){
					String s1 = (String)this.firstValue;
					String s2 = (String)ca.firstValue;
					String s3 = (String)this.secondValue;
					String s4 = (String)ca.secondValue;
					if((s1.equalsIgnoreCase(s2)) && (s3.equalsIgnoreCase(s4))){
						return true;
					}
				}
				else{
					return this.firstValue.equals(ca.firstValue) && this.secondValue.equals(ca.secondValue);
				}
			}
			return false;
		}
		return false;
	}
	
	public int hashCode(){
		if(!directed){
		TreeSet ts = new TreeSet();
		ts.add(firstValue);
		ts.add(secondValue);
		String s = "";
		for(Iterator it = ts.iterator(); it.hasNext();){
			s += (String)it.next();
		}
	
			return s.hashCode();
		}
		else{
			String s = firstValue.toString()+secondValue.toString();
	
			return s.hashCode();
		}
	}
	
	public String toString(){
		return firstValue + " " + secondValue;
	}

}
