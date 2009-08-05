package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

public class BigBitSetArray extends BigArray{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	ArrayList<BitSet> bigArray;
	
	public BigBitSetArray(BigInteger size){
		this.size = size;
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}
		this.bigArray = new ArrayList<BitSet>(buckets);
		for(int i=0; i < this.buckets-1; i++){
			this.bigArray.add(new BitSet(Integer.MAX_VALUE));
		}
		this.bigArray.add(new BitSet(arraySize[1].intValue()));
	}
	
	public void and(BigBitSetArray set){
		if(set.buckets >= this.buckets){
			for(int i = 0; i < this.bigArray.size(); i++){
				this.bigArray.get(i).and(set.bigArray.get(i));
			}
		}
		else {
			int i;
			for(i = 0; i < set.bigArray.size(); i++){
				this.bigArray.get(i).and(set.bigArray.get(i));
			}
			int stepBack = i;
			for(i = this.bigArray.size()-1; i >= stepBack; i--){
				this.bigArray.remove(i);
			}
		}
		this.buckets = this.bigArray.size();
		this.size = new BigInteger(new Integer(this.buckets-1).toString()).multiply(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
		this.size = size.add(new BigInteger(new Integer(this.bigArray.get(this.buckets-1).length()).toString()));
	}
	
	public void andNot(BigBitSetArray set){
		if(set.buckets >= this.buckets){
			for(int i = 0; i < this.bigArray.size(); i++){
				this.bigArray.get(i).andNot(set.bigArray.get(i));
			}
		}
		else {
			int i;
			for(i = 0; i < set.bigArray.size(); i++){
				this.bigArray.get(i).andNot(set.bigArray.get(i));
			}
			int stepBack = i;
			for(i = this.bigArray.size()-1; i >= stepBack; i--){
				this.bigArray.remove(i);
			}
		}
		this.buckets = this.bigArray.size();
		this.size = new BigInteger(new Integer(this.buckets-1).toString()).multiply(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
		this.size = size.add(new BigInteger(new Integer(this.bigArray.get(this.buckets-1).length()).toString()));
	}
	
	public void or(BigBitSetArray set){
		if(set.buckets >= this.buckets){
			int i = 0;
			for(i = 0; i < this.bigArray.size(); i++){
				this.bigArray.get(i).or(set.bigArray.get(i));
			}
			for(i=i; i < set.bigArray.size(); i++){
				this.bigArray.add(new BitSet());
				this.bigArray.get(i).or(set.bigArray.get(i));
			}
		}
		else{
			for(int i = 0; i < set.bigArray.size();i++){
				this.bigArray.get(i).or(set.bigArray.get(i));
			}
		}
		
		this.buckets = this.bigArray.size();
		this.size = new BigInteger(new Integer(this.buckets-1).toString()).multiply(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
		this.size = size.add(new BigInteger(new Integer(this.bigArray.get(this.buckets-1).length()).toString()));
	}
	
	public void xor(BigBitSetArray set){
		if(set.buckets >= this.buckets){
			int i = 0;
			for(i = 0; i < this.bigArray.size(); i++){
				this.bigArray.get(i).xor(set.bigArray.get(i));
			}
			for(i=i; i < set.bigArray.size(); i++){
				this.bigArray.add(new BitSet());
				this.bigArray.get(i).xor(set.bigArray.get(i));
			}
		}
		else{
			for(int i = 0; i < set.bigArray.size();i++){
				this.bigArray.get(i).xor(set.bigArray.get(i));
			}
		}
		this.buckets = this.bigArray.size();
		this.size = new BigInteger(new Integer(this.buckets-1).toString()).multiply(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
		this.size = size.add(new BigInteger(new Integer(this.bigArray.get(this.buckets-1).length()).toString()));
	}
	
	public boolean intersects(BigBitSetArray set){
		boolean value = false;
		int size = Math.min(this.bigArray.size(), set.bigArray.size());
		for(int i = 0; i < size; i++){
			value |= this.bigArray.get(i).intersects(set.bigArray.get(i));
		}
		
		return value;
	}
	
	public void set(BigInteger pos){
		BigInteger[] intPos = BigArray.convertBigIntegerToInts(pos);
		this.bigArray.get(intPos[0].intValue()).set(intPos[1].intValue());
	}
	
	public boolean get(BigInteger pos){
		BigInteger[] intPos = BigArray.convertBigIntegerToInts(pos);
		return this.bigArray.get(intPos[0].intValue()).get(intPos[1].intValue());
	}
	
	public void clear(){
		this.bigArray.clear();
	}
	

}
