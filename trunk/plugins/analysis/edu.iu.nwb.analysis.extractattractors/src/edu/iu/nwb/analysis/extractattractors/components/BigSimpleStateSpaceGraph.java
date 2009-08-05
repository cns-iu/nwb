package edu.iu.nwb.analysis.extractattractors.components;

import java.math.BigInteger;
import java.util.HashSet;

import edu.iu.nwb.analysis.extractattractors.containers.BigBigHashSetVectorArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigHashSetVector;
import edu.iu.nwb.analysis.extractattractors.containers.BigIntegerHashMap;

public class BigSimpleStateSpaceGraph extends SimpleStateSpaceGraph{
	private BigBigIntegerArray outNeighbors;
	private BigBigHashSetVectorArray inNeighbors;
	private BigIntegerHashMap bigMappings = null;
	private BigInteger nextLoc = BigInteger.ZERO;
	
	public BigSimpleStateSpaceGraph(int nodeStates, int labelSize){
		super(nodeStates,labelSize);
	}

	@Override
	public void addEdge(BigInteger source, BigInteger target) {
		// TODO Auto-generated method stub
		if(this.bigMappings == null){
			this.outNeighbors.put(source, target);
			BigHashSetVector inList = this.inNeighbors.get(target);
			if(inList == null){
				inList = new BigHashSetVector();
			}
			inList.add(source);
			this.inNeighbors.put(target, inList);
		}else{
			if(!this.bigMappings.containsKey(source)){
				this.bigMappings.put(source, this.nextLoc);
				this.nextLoc = this.nextLoc.add(BigInteger.ONE);
			}
			if(!this.bigMappings.containsKey(target)){
				this.bigMappings.put(target, this.nextLoc);
				this.nextLoc = this.nextLoc.add(BigInteger.ONE);
			}
			
			this.outNeighbors.put(this.bigMappings.get(source), target);
			BigHashSetVector inList = this.inNeighbors.get(this.bigMappings.get(target));
			if(inList == null){
				inList = new BigHashSetVector();
			}
			inList.add(source);
			this.inNeighbors.put(this.bigMappings.get(target), inList);
		}
		
	}

	@Override
	public void createEdgeLists(BigInteger size) {
		// TODO Auto-generated method stub
		super.createEdgeLists(size);
		
		if(size.multiply(new BigInteger(new Integer(2).toString())).compareTo(super.maxSize) < 0){
			this.bigMappings = new BigIntegerHashMap(size);
			this.outNeighbors = new BigBigIntegerArray(size);
			this.inNeighbors = new BigBigHashSetVectorArray(size);
		}else{
			this.outNeighbors = new BigBigIntegerArray(super.maxSize);
			this.inNeighbors = new BigBigHashSetVectorArray(super.maxSize);
		}
		BigBigIntegerArray.fill(outNeighbors, null);
	}

	@Override
	public BigHashSetVector getBigInNeighbors(BigInteger target) {
		// TODO Auto-generated method stub
		if(this.bigMappings == null){
			return this.inNeighbors.get(target);
		}else{
			return this.inNeighbors.get(this.bigMappings.get(target));
		}
	}

	@Override
	public BigInteger getOutNeighbor(BigInteger source) {
		// TODO Auto-generated method stub
		if(this.bigMappings == null){
			return this.outNeighbors.get(source);
		}
		else{
			try{
				return this.outNeighbors.get(this.bigMappings.get(source));
			}catch(NullPointerException npe){
				return null;
			}
		}
	}

	@Override
	public BigBigIntegerArray getOutNeighbors() {
		// TODO Auto-generated method stub
		return this.outNeighbors;
	}

	@Override
	public HashSet<Integer> getSmallInNeighbors(int target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getSmallOutNeighbors() {
		// TODO Auto-generated method stub
		return null;
	}

}
