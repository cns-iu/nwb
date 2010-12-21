package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;

public class BigBigIntegerArray extends BigArray{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	BigInteger[][] bigArray;
	
	public BigBigIntegerArray(BigInteger size){
		
		this.size = size;
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}
		this.bigArray = new BigInteger[buckets][];
		for(int i=0; i < this.buckets-1; i++){
			this.bigArray[i] = new BigInteger[Integer.MAX_VALUE];
		}
		this.bigArray[this.buckets-1] = new BigInteger[arraySize[1].intValue()];
		
	}
	
	
	
	
	public void put(BigInteger location, BigInteger value){
		BigInteger[] intLocation = convertBigIntegerToInts(location);
		this.bigArray[intLocation[0].intValue()][intLocation[1].intValue()] = value;
		
	}
	
	public BigInteger get(BigInteger location) throws NullPointerException{
		BigInteger[] intLocation = convertBigIntegerToInts(location);
		return this.bigArray[intLocation[0].intValue()][intLocation[1].intValue()];
	}
	
	public static void fill(BigBigIntegerArray targetArray, BigInteger value){
		BigInteger start = BigInteger.ZERO;
		BigInteger end = targetArray.getSize();
		while(start.compareTo(end) < 0){
			targetArray.put(start, value);
			start = start.add(BigInteger.ONE);
		}
	}
	
	public BigHashSetVector keySet(){
		BigHashSetVector keySet = new BigHashSetVector();
		
		BigInteger start = BigInteger.ZERO;
		BigInteger end = this.getSize();
		
		while(start.compareTo(end) < 0){
			BigInteger value = this.get(start);
			if(value != null)
				keySet.add(this.get(start));
			start = start.add(BigInteger.ONE);
		}
		
		return keySet;
	}
	
	public BigInteger getSize(){
		return this.size;
	}

}
