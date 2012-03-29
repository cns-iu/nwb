package edu.iu.nwb.analysis.java.directedknn.components;

import java.util.HashMap;
import java.util.Map;

public class DirectedKNNArrayAndMapContainer {
	private Map inDegreeToKInIn;
	private Map inDegreeToKInOut;
	private Map outDegreeToKOutIn;
	private Map outDegreeToKOutOut;
	
	int undirectedDegree = 0;
	
	private Map inDegreeTotals;
	private Map	outDegreeTotals;
	
	
	public DirectedKNNArrayAndMapContainer(){
		this.inDegreeToKInIn = new HashMap();
		this.inDegreeToKInOut = new HashMap();
		this.outDegreeToKOutIn = new HashMap();
		this.outDegreeToKOutOut = new HashMap();
		this.inDegreeTotals = new HashMap();
		this.outDegreeTotals = new HashMap();
	}
	
	public void updateDegree(){
		this.undirectedDegree++;
	}
	
	public int getUndirectedDegree(){
		return this.undirectedDegree;
	}
	
	public void addKNNValues(int inDegree, int outDegree, double kInIn, double kInOut, double kOutIn, double kOutOut){
		updateInDegreeValues(inDegree,kInIn,kInOut);
		updateOutDegreeValues(outDegree,kOutIn,kOutOut);
	}
	
	public void updateInDegreeValues(int inDegree, double kInIn, double kInOut){
		
		Integer mapKey = new Integer(inDegree);
		Double inIn = (Double)this.inDegreeToKInIn.get(mapKey);
		if(inIn == null){
			this.inDegreeToKInIn.put(mapKey, new Double(kInIn));
			this.inDegreeToKInOut.put(mapKey, new Double(kInOut));
			
		}else{
			this.inDegreeToKInIn.put(mapKey, new Double(kInIn+(inIn.doubleValue())));
			Double inOut = (Double)this.inDegreeToKInOut.get(mapKey);
			this.inDegreeToKInOut.put(mapKey, new Double(kInOut+inOut.doubleValue()));
		}
		
	}
	
	public Map getKInInMap(){
		return this.inDegreeToKInIn;
	}
	
	public Map getKInOutMap(){
		return this.inDegreeToKInOut;
	}
	
	public Map getKOutInMap(){
		return this.outDegreeToKOutIn;
	}
	
	public Map getKOutOutMap(){
		return this.outDegreeToKOutOut;
	}
	
	public Map getInDegreeTotals(){
		return this.inDegreeTotals;
	}
	
	public Map getOutDegreeTotals(){
		return this.outDegreeTotals;
	}
	
	public static void updateDegreeTotals(int degree, Map degreeTotals){
		Integer inDegreeCount = (Integer)degreeTotals.get(new Integer(degree));
		if(inDegreeCount == null){
			degreeTotals.put(new Integer(degree), new Integer(1));
		}else{
			degreeTotals.put(new Integer(degree), new Integer(inDegreeCount.intValue()+1));
		}
	}
	
	public void updateInDegreeTotals(int inDegree){
		updateDegreeTotals(inDegree,this.inDegreeTotals);
	}
	
	public void updateOutDegreeTotals(int outDegree){
		updateDegreeTotals(outDegree,this.outDegreeTotals);
	}
	
	public void updateOutDegreeValues(int outDegree, double kOutIn, double kOutOut){
		Integer mapKey = new Integer(outDegree);
		Double outIn = (Double)this.outDegreeToKOutIn.get(mapKey);
		if(outIn == null){
			this.outDegreeToKOutIn.put(mapKey, new Double(kOutIn));
			this.outDegreeToKOutOut.put(mapKey, new Double(kOutOut));
		}else{
			this.outDegreeToKOutIn.put(mapKey, new Double(kOutIn+outIn.doubleValue()));
			Double outOut = (Double)this.outDegreeToKOutOut.get(mapKey);
			this.outDegreeToKOutOut.put(mapKey, new Double(kOutOut+outOut.doubleValue()));
		}
		
	}
	
	public double getKInIn(int inDegree){
		double kInIn = ((Double)this.inDegreeToKInIn.get(new Integer(inDegree))).doubleValue();
		double degreeTotal = ((Integer)this.inDegreeTotals.get(new Integer(inDegree))).doubleValue();
		return kInIn/degreeTotal;
	}
	
	public double getKInOut(int inDegree){
		double kInOut = ((Double)this.inDegreeToKInOut.get(new Integer(inDegree))).doubleValue();
		double degreeTotal = ((Integer)this.inDegreeTotals.get(new Integer(inDegree))).doubleValue();
		return kInOut/degreeTotal;
	}
	
	public double getKOutOut(int outDegree){
		double kOutOut = ((Double)this.outDegreeToKOutOut.get(new Integer(outDegree))).doubleValue();
		double degreeTotal = ((Integer)this.outDegreeTotals.get(new Integer(outDegree))).doubleValue();
		return kOutOut/(double)degreeTotal;
	}
	
	public double getKOutIn(int outDegree){
		double kOutIn = ((Double)this.outDegreeToKOutIn.get(new Integer(outDegree))).doubleValue();
		double degreeTotal = ((Integer)this.outDegreeTotals.get(new Integer(outDegree))).doubleValue();
		return kOutIn/(double)degreeTotal;
	}
	
	
	public double getInDegreeProbability(int inDegree, int numberOfNodes){
		Integer inDegreeTotal = ((Integer)this.inDegreeTotals.get(new Integer(inDegree)));
		if(inDegreeTotal == null)
			return 0;
		return inDegreeTotal.doubleValue()/(double)numberOfNodes;
	}
	
	public double getOutDegreeProbability(int outDegree, int numberOfNodes){
		Integer outDegreeTotal = ((Integer)this.outDegreeTotals.get(new Integer(outDegree)));
		if(outDegreeTotal == null)
			return 0;
		return outDegreeTotal.doubleValue()/(double)numberOfNodes;
	}
	
	
	

}
