package edu.iu.nwb.tools.mergenodes;

import java.util.HashMap;
import java.util.Iterator;

public class JointNodeMap extends HashMap{
	boolean isDirected;
	public JointNodeMap(boolean isDirected){
		super();
		this.isDirected = isDirected;
	}
	public HashMap getSameIDs(JointNodeMap theMap) throws Exception{
		if (isDirected!= theMap.isDirected()){
			throw new Exception("Error, one EdgeMap is from a directed graph,"+
					" and another is from an undirected graph.");
		}
		HashMap results = new HashMap();
		Iterator keys = this.keySet().iterator();
		while(keys.hasNext()){
			int id = ((Integer)keys.next()).intValue();
			if(!this.containsKey(id, (String) theMap.get(id))){
				results.put(id, this.get(id));
			}
		}
		return results;
	}
	
	private boolean containsKey(int ID, String value) {
		if (!isDirected){
			return this.containsKey(ID);
		}
		else{
			if (this.containsKey(ID)){
				if(((String)this.get(ID)).equalsIgnoreCase(value)){
					return true;
				}
				else 
					return false;
			}
			else 
				return false;
		}
	}
	
	public boolean isDirected(){
		return isDirected;
	}
	
	public void print(){
		Iterator keys =this.keySet().iterator();
		while(keys.hasNext()){
			Integer key = (Integer)keys.next();
			String value = (String)this.get(key);
			System.out.println("id="+key+", type="+value+"\n");
		}
		
	}

}
