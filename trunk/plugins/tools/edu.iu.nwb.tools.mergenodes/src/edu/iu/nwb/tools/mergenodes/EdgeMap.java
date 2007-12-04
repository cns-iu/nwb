package edu.iu.nwb.tools.mergenodes;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;

public class EdgeMap {
	//key = source node id
	//value = target node id
	private Map edgeMap;
	private boolean isDirected;
	public static final String TAG_SOURCE = "source";
	public static final String TAG_TARGET = "target";
	
	public EdgeMap(boolean isDirected){
		edgeMap = new HashMap();
		this.isDirected=isDirected;
	}
	
	
	public void put(int source, int target){
		edgeMap.put(source, target);
	}
	
	public HashSet getSourceList(int targetID)throws Exception{
		HashSet sourceSet  = new HashSet();
		Iterator keys = edgeMap.keySet().iterator();
		while(keys.hasNext()){
			int source = ((Integer)keys.next()).intValue();
			int target = ((Integer)edgeMap.get(source)).intValue();
			if(target== targetID){
				if (!sourceSet.add(source)){
					throw new Exception ("find duplicat edges, souce = "+source+
							", target = "+target+".\n");
				}
			}
		}
		return sourceSet;
		
	}
	
	public HashSet getTargetList(int sourceID)throws Exception{
		HashSet targetSet  = new HashSet();
		Iterator keys = edgeMap.keySet().iterator();
		while(keys.hasNext()){
			int source = ((Integer)keys.next()).intValue();
			int target = ((Integer)edgeMap.get(source)).intValue();
			if(source== sourceID){
				if (!targetSet.add(target)){
					throw new Exception ("find duplicat edges, souce = "+source+
							", target = "+target+".\n");
				}
			}
		}
		return targetSet;

	}
	
	public JointNodeMap getFullList(int id) throws Exception {
		JointNodeMap completetMap  = new JointNodeMap(isDirected);
		Iterator keys = edgeMap.keySet().iterator();
		while(keys.hasNext()){
			int source = ((Integer)keys.next()).intValue();
			int target = ((Integer)edgeMap.get(source)).intValue();
			if(isDirected){
				//key+value can not be duplicated
				if (source == id){
					if (!completetMap.containsKey(target)){
						completetMap.put(target, "target");						
					}
					else if (completetMap.containsKey(target) &&
						  !((String)completetMap.get(target)).equals("target")){
						completetMap.put(target, "target");
					}
					else {
						throw new Exception ("find duplicat edges, souce = "+source+
								", target = "+target+".\n");
					}						  
				}
				else if (target == id){
					if (!completetMap.containsKey(source)){
						completetMap.put(source, "source");						
					}
					else if (completetMap.containsKey(source) &&
						  !((String)completetMap.get(source)).equals("source")){
						completetMap.put(source, "source");
					}
					else {
						throw new Exception ("find duplicat edges, souce = "+source+
								", target = "+target+".\n");
					}
				}
			}
			else {
				//for undirected graph, key can not be duplicated
				if (source == id){
					if (!completetMap.containsKey(target)){
						completetMap.put(target, "target");						
					}
					else {
						throw new Exception ("find duplicat edges, souce = "+source+
								", target = "+target+".\n");
					}						  
				}
				else if (target == id){
					if (!completetMap.containsKey(source)){
						completetMap.put(source, "source");						
					}
					else {
						throw new Exception ("find duplicat edges, souce = "+source+
								", target = "+target+".\n");
					}
				}
			}
			
		}
		return completetMap;
		
	}
	public boolean isDirected(){
		return isDirected;
	}
	
	
	
	

}
