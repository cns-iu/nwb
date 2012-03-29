package edu.iu.nwb.analysis.extractattractors.components;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigHashSetVector;

public class SmallSimpleStateSpaceGraph extends SimpleStateSpaceGraph{
	private int[] outNeighbors;
	private HashSet<Integer>[] inNeighbors;
	private HashMap<Integer,Integer> smallMappings = null;
	int nextLoc = 0;

	public SmallSimpleStateSpaceGraph(int nodeStates,int labelSize){
		super(nodeStates,labelSize);
	}

	@Override
	public void addEdge(BigInteger source, BigInteger target) {
		if(this.smallMappings == null){
			this.outNeighbors[source.intValue()] = target.intValue();
			HashSet<Integer> inNeighbor = this.inNeighbors[target.intValue()];
			if(inNeighbor == null){
				inNeighbor = new HashSet<Integer>();
			}
			inNeighbor.add(new Integer(source.intValue()));
			this.inNeighbors[target.intValue()] = inNeighbor;
		}else{
			Integer src = new Integer(source.intValue());
			Integer tgt = new Integer(target.intValue());
			if(!this.smallMappings.containsKey(src)){
				this.smallMappings.put(src, new Integer(nextLoc));
				
				this.nextLoc++;
			}
			if(!this.smallMappings.containsKey(tgt)){
				this.smallMappings.put(tgt, new Integer(this.nextLoc));
				
				this.nextLoc++;
			}

			this.outNeighbors[this.smallMappings.get(src).intValue()] = tgt.intValue();
			HashSet<Integer> inNeighbor = this.inNeighbors[this.smallMappings.get(tgt).intValue()];
			if(inNeighbor == null){
				inNeighbor = new HashSet<Integer>();
			}
			inNeighbor.add(src);
			this.inNeighbors[this.smallMappings.get(tgt).intValue()] = inNeighbor;

		}
	}

	@Override
	public void createEdgeLists(BigInteger size) {
		// TODO Auto-generated method stub
		super.createEdgeLists(size);
		if((size.longValue()*2) < super.maxSize.intValue()){
			smallMappings = new HashMap<Integer,Integer>(size.intValue());
			outNeighbors = new int[size.intValue()];
			inNeighbors = (HashSet<Integer>[])new HashSet[size.intValue()];
		}else{	
			outNeighbors = new int[super.maxSize.intValue()];
			inNeighbors = (HashSet<Integer>[])new HashSet[super.maxSize.intValue()];
		}
		java.util.Arrays.fill(outNeighbors, -1);
	}

	@Override
	public BigHashSetVector getBigInNeighbors(BigInteger target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getOutNeighbor(BigInteger source) {
		// TODO Auto-generated method stub
		if(this.smallMappings == null){
			if(this.outNeighbors[source.intValue()] == -1)
				return null;
			else
				return new BigInteger(new Integer(this.outNeighbors[source.intValue()]).toString());
		}
		else{
			try{
				return new BigInteger(new Integer(this.outNeighbors[this.smallMappings.get(new Integer(source.intValue()))]).toString());
			}catch(NullPointerException npe){
				return null;
			}
		}

	}

	@Override
	public BigBigIntegerArray getOutNeighbors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashSet<Integer> getSmallInNeighbors(int target) {
		if(this.smallMappings == null)
			return this.inNeighbors[target];
		else
			return this.inNeighbors[this.smallMappings.get(new Integer(target)).intValue()];
	}

	@Override
	public int[] getSmallOutNeighbors() {
		// TODO Auto-generated method stub
		return this.outNeighbors;
	}

}
