package edu.iu.nwb.analysis.extractattractors.containers;

import java.math.BigInteger;

public abstract class BigArray {
	BigInteger size = BigInteger.ZERO;
	int buckets;
	public static final BigInteger maxInteger = new BigInteger(new Integer(Integer.MAX_VALUE).toString());
	
	public static BigInteger[] convertBigIntegerToInts(BigInteger size){ 
		return size.divideAndRemainder(maxInteger);
	}
	
	

}
