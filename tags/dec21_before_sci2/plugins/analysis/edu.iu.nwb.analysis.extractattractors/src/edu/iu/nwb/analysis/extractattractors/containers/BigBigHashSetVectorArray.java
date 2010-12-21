package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;

public class BigBigHashSetVectorArray extends BigArray{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	BigHashSetVector[][] bigArray;
	
	public BigBigHashSetVectorArray(BigInteger size){
		this.size = size;
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}
		this.bigArray = new BigHashSetVector[buckets][];
		for(int i = 0; i < this.buckets-1; i++){
			this.bigArray[i] = new BigHashSetVector[Integer.MAX_VALUE];
		}
		this.bigArray[this.buckets-1] = new BigHashSetVector[arraySize[1].intValue()];
	}
	
	public void put(BigInteger location, BigHashSetVector value){
		BigInteger[] intLocation = convertBigIntegerToInts(location);
		this.bigArray[intLocation[0].intValue()][intLocation[1].intValue()] = value;
	}
	
	public BigHashSetVector get(BigInteger location){
		BigInteger[] intLocation = convertBigIntegerToInts(location);
		return this.bigArray[intLocation[0].intValue()][intLocation[1].intValue()];
	}
	
	public void clear(){
		BigInteger start = BigInteger.ZERO;
		BigInteger end = this.size;
		BigInteger[] intLocation;
		while(start.compareTo(end) < 0){
			try{
			intLocation = convertBigIntegerToInts(start);
			this.bigArray[intLocation[0].intValue()][intLocation[1].intValue()].clear();
			start = start.add(BigInteger.ONE);
			}catch(NullPointerException npe){
				
			}
		}
		
	}
	

}
