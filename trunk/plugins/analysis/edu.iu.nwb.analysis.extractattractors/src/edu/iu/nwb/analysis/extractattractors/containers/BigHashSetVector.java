package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class BigHashSetVector extends BigArray implements Iterable<BigInteger>{
	BigInteger size = BigInteger.ZERO;
	int buckets;
	Vector<HashSet<BigInteger>> bigArray;

	public BigHashSetVector(BigInteger size){
		this.size = size;
		BigInteger[] arraySize = convertBigIntegerToInts(size);
		this.buckets = arraySize[0].intValue();
		if(arraySize[1].intValue() > 0){
			this.buckets += 1;
		}
		this.bigArray = new Vector<HashSet<BigInteger>>(buckets);
		for(int i=0; i < this.buckets-1; i++){
			this.bigArray.add(new HashSet<BigInteger>(Integer.MAX_VALUE));
		}
		this.bigArray.add(buckets-1,new HashSet<BigInteger>(arraySize[1].intValue()));
	}



	public BigHashSetVector(){
		
		this.bigArray = new Vector<HashSet<BigInteger>>();
		this.bigArray.add(new HashSet<BigInteger>());
		this.buckets = this.bigArray.size();
		
		this.size = new BigInteger(new Integer(this.bigArray.get(0).size()).toString());
	}

	public Iterator<BigInteger> iterator() {
		// TODO Auto-generated method stub
		return new BigHashSetArrayListIterator(this);
	}

	public boolean add(BigInteger value){
		HashSet<BigInteger> hs;
		boolean returnValue = false;
		int i;
		for(i = 0; i< this.buckets; i++){
			hs = this.bigArray.get(i);
			if(hs.contains(value))
				return returnValue;
			if(hs.size() == Integer.MAX_VALUE)
				continue;
			else
				returnValue = hs.add(value);
			this.bigArray.set(i, hs);
			break;
		}

		hs = this.bigArray.get(i);
		if(hs.size() == Integer.MAX_VALUE){
			hs = new HashSet<BigInteger>();
			this.bigArray.add(hs);
			this.buckets = this.bigArray.size();
		}

		if(returnValue){
			this.size = BigInteger.ZERO;
			for(int j = 0; j < this.buckets; j++){
				this.size = this.size.add(new BigInteger(new Integer(this.bigArray.get(j).size()).toString()));
			}
		
		}
		return returnValue;
	}

	public boolean contains(BigInteger value){
		boolean returnValue = false;
		for(int i = 0; i < this.bigArray.size(); i++){
			returnValue |= this.bigArray.get(i).contains(value);
		}
		return returnValue;
	}

	public void addAll(BigHashSetVector collection){
		Iterator<BigInteger> it = collection.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
		
	}
	
	public BigInteger size(){
		return this.size;
	}
	
	public void clear(){
		for(Iterator<HashSet<BigInteger>> it = this.bigArray.iterator(); it.hasNext();){
			it.next().clear();
		}
		this.bigArray.clear();
		this.size = BigInteger.ZERO;
	}

	private class BigHashSetArrayListIterator implements Iterator<BigInteger>{

		//BigInteger pos = BigInteger.ZERO;
		
		Iterator<HashSet<BigInteger>> hashSetIterator;
		Iterator<BigInteger> bigInttIterator = null;

		private BigHashSetArrayListIterator(BigHashSetVector bigArray){
			
			this.hashSetIterator = bigArray.bigArray.iterator();
			if(this.hashSetIterator.hasNext())
				this.bigInttIterator = this.hashSetIterator.next().iterator();
		}

		public boolean hasNext() {
			if(this.bigInttIterator == null)
				return false;
			else
				return this.bigInttIterator.hasNext() | this.hashSetIterator.hasNext();

		}

		public BigInteger next() {
			if(this.bigInttIterator.hasNext())
				return this.bigInttIterator.next();
			else
				if(this.hashSetIterator.hasNext()){
					this.bigInttIterator = this.hashSetIterator.next().iterator();
					return this.next();
				}
				else
					return null;

		}

		public void remove() {
			this.bigInttIterator.remove();
		}

	}


}
