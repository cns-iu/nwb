package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BigIntegerHashMap extends BigArray implements Iterable<BigInteger>{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	Vector<ConcurrentHashMap<BigInteger,BigInteger>> bigArray;
	
	public BigIntegerHashMap(BigInteger size){
		this.size = size;
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}

		this.bigArray = new Vector<ConcurrentHashMap<BigInteger,BigInteger>>(buckets);
		for(int i=0; i < this.buckets-1; i++){
			this.bigArray.add(new ConcurrentHashMap<BigInteger,BigInteger>(Integer.MAX_VALUE));
		}
		this.bigArray.add(buckets-1,new ConcurrentHashMap<BigInteger,BigInteger>(arraySize[1].intValue()));
	}
	
	public BigIntegerHashMap(){
		this.bigArray = new Vector<ConcurrentHashMap<BigInteger,BigInteger>>();
		this.bigArray.add(new ConcurrentHashMap<BigInteger,BigInteger>());
		this.buckets = this.bigArray.size();
		
		this.size = new BigInteger(new Integer(this.bigArray.get(0).size()).toString());
	}
	
	public BigInteger put(BigInteger source, BigInteger target){
		ConcurrentHashMap<BigInteger,BigInteger> chm;
		BigInteger returnValue = null;
		int i;
		for(i = 0; i < this.buckets; i++){
			chm = this.bigArray.get(i);
			if(chm.containsKey(source)){
				return chm.put(source, target);
			}
			if(chm.size() == Integer.MAX_VALUE)
				continue;
			else
				returnValue = chm.put(source, target);
			this.bigArray.set(i, chm);
			break;
		}
		
		chm = this.bigArray.get(i);
		if(chm.size() == Integer.MAX_VALUE){
			chm = new ConcurrentHashMap<BigInteger,BigInteger>();
			this.bigArray.add(chm);
			this.buckets = this.bigArray.size();
		}
		
		if(returnValue != null){
			this.size = BigInteger.ZERO;
			for(int j = 0; j < this.buckets; j++){
				this.size = this.size.add(new BigInteger(new Integer(this.bigArray.get(j).size()).toString()));
			}
		
		}
		
		
		return returnValue;
	}
	
	public boolean containsKey(BigInteger source){
		boolean returnValue = false;
		for(Iterator<ConcurrentHashMap<BigInteger,BigInteger>> it = this.bigArray.iterator(); it.hasNext();){
			returnValue |= it.next().containsKey(source);
		}
		
		return returnValue;
	}
	
	public BigInteger get(BigInteger source){
		BigInteger returnValue = null;
		
		for(Iterator<ConcurrentHashMap<BigInteger,BigInteger>> it = this.bigArray.iterator(); it.hasNext();){
			returnValue = it.next().get(source);
			if(returnValue != null){
				return returnValue;
			}
		}
		
		return returnValue;
	}

	public Iterator<BigInteger> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
