package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.Iterator;
import java.util.TreeSet;

public class CoValued {
	
	Object firstValue;
	Object secondValue;
	
	public CoValued(Object s1, Object s2){
		firstValue = s1;
		secondValue = s2;
	}
	
	
	public boolean equals(Object o){
		if(o.getClass().equals(this.getClass())){
			CoValued ca = (CoValued)o;
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
			else
				return this.firstValue.equals(ca.firstValue) && this.secondValue.equals(ca.secondValue);
		}
		return false;
	}
	
	public int hashCode(){
		TreeSet ts = new TreeSet();
		ts.add(firstValue);
		ts.add(secondValue);
		String s = "";
		for(Iterator it = ts.iterator(); it.hasNext();){
			s += (String)it.next();
		}
		
		return s.hashCode();
	}
	
	public String toString(){
		return firstValue + " " + secondValue;
	}

}
