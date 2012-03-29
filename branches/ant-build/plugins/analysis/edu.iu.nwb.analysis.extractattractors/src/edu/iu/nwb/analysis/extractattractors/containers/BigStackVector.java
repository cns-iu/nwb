package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;
import java.util.Stack;
import java.util.Vector;

public class BigStackVector extends BigArray{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	int bucketPos = 0;
	Vector<Stack<BigInteger>> bigArray;
	
	
	public BigStackVector(BigInteger size){
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}
		this.bigArray = new Vector<Stack<BigInteger>>(buckets);
		for(int i=0; i < this.buckets-1; i++){
			this.bigArray.add(new Stack<BigInteger>());
			this.bigArray.get(i).ensureCapacity(Integer.MAX_VALUE);
		}
		this.bigArray.add(buckets-1,new Stack<BigInteger>());
		this.bigArray.get(this.buckets-1).ensureCapacity(arraySize[1].intValue());
	}
	
	public BigStackVector(){
		
		this.bigArray = new Vector<Stack<BigInteger>>();
		this.bigArray.add(new Stack<BigInteger>());
		this.buckets = this.bigArray.size();
		
	}
	
	public void push(BigInteger value){
		if(this.bigArray.get(this.bucketPos).size() != Integer.MAX_VALUE){
			this.bigArray.get(this.bucketPos).push(value);	
		}
		else{
			this.bucketPos += 1;
			if(this.bucketPos == this.bigArray.size())
				this.bigArray.add(new Stack<BigInteger>());
			this.bigArray.get(this.bucketPos).push(value);
			this.bigArray.get(this.bucketPos).trimToSize();
		}
		this.size = this.size.add(BigInteger.ONE);
	}
	
	public BigInteger peek(){
		return this.bigArray.get(this.bucketPos).peek();
	}
	
	public BigInteger pop(){
		if(this.bucketPos==0 && this.bigArray.get(0).size()==0)
			return null;
		if(this.bigArray.get(this.bucketPos).size()==0){
			this.bucketPos -=1;
			return this.pop();
		}
		this.size = this.size.subtract(BigInteger.ONE);
		return this.bigArray.get(this.bucketPos).pop();
	}
	
	public boolean isEmpty(){
		return (this.bucketPos == 0 && this.bigArray.get(0).size()==0);
	}
	
	public BigInteger size(){
		return this.size;
	}

}
