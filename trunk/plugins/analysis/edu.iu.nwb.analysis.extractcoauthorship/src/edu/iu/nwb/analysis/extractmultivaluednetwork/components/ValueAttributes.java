package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

public class ValueAttributes {
	int numOfWorks;
	int timesCited;
	
	public ValueAttributes(){
		numOfWorks = 0;
		timesCited = 0;
	}
	
	public void addWork(){
		numOfWorks++;
	}
	
	public void newCitations(int i){
		timesCited += i;
	}
	
	public boolean equals(Object o){
		if(o.getClass().equals(this.getClass())){
			ValueAttributes a = (ValueAttributes)o;
			if(this.numOfWorks == a.numOfWorks && this.timesCited == a.timesCited){
				return true;
			}
		}
			
		return false;
	}
	
	public int hashCode(){
		String s = numOfWorks + " " + timesCited;
		return s.hashCode();
	}
	
	public String toString(){
		return numOfWorks + " " + timesCited;
	}
	
}
