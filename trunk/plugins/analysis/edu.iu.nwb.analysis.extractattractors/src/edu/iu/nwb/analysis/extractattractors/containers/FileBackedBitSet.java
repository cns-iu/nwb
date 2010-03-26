package edu.iu.nwb.analysis.extractattractors.containers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.channels.FileLock;

public class FileBackedBitSet {
	RandomAccessFile bitSetFile;

	public FileBackedBitSet(BigInteger size){
		try{
			bitSetFile = new RandomAccessFile(File.createTempFile("Attractors-FBBS-", ".txt"), "rw");
			BigInteger [] sizeArray = size.divideAndRemainder(new BigInteger(new Integer(8).toString()));
			long fileSize;
			if(sizeArray[1].compareTo(BigInteger.ZERO)==0)
				fileSize = sizeArray[0].longValue();
			else
				fileSize = sizeArray[0].longValue()+1;
			bitSetFile.setLength(fileSize);
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
			return;
		}catch(IOException ie){
			ie.printStackTrace();
			return;
		}
	}

	public FileLock lockFile(BigInteger location){
		BigInteger[] filePosition = convertBigIntegerToLoc(location);
		long pos = filePosition[1].longValue();

		try{
			long byteNumber = pos/8;
			byte bitNumber = (byte)(pos%8);
			this.bitSetFile.seek(byteNumber);
			FileLock fl = null;
			while((fl = this.bitSetFile.getChannel().tryLock(byteNumber, 1, false)) == null){}
			return fl;
		}catch(IOException ioe){
			ioe.printStackTrace();
			return null;
		}

	}



	public boolean get(BigInteger location){
		BigInteger[] filePosition = convertBigIntegerToLoc(location);
		long pos = filePosition[1].longValue();
		if(location == null)
			return false;

		try{
			long byteNumber = pos/8;
			byte bitNumber = (byte)(pos%8);
			this.bitSetFile.seek(byteNumber);
			byte readByte = this.bitSetFile.readByte();
			int val = (int)Math.pow(2, bitNumber);
			val = val & readByte;
			if(val == 0)
				return false;
			else
				return true;

		}catch(IOException ie){
			ie.printStackTrace();
			return false;
		}

	}

	public boolean set(BigInteger location){
		BigInteger[] filePosition = convertBigIntegerToLoc(location);
		long pos = filePosition[1].longValue();
		try{
			long byteNumber = pos/8;
			byte bitNumber = (byte)(pos%8);



			FileLock fl = null;


			while((fl = this.bitSetFile.getChannel().tryLock(byteNumber, 1, false)) == null){}
			this.bitSetFile.seek(byteNumber);

			int val = (int)Math.pow(2, bitNumber);
			byte writeVal = this.bitSetFile.readByte();



			if(writeVal == (writeVal | val)){
				fl.release();
				return false;
			}else{
				this.bitSetFile.seek(byteNumber);
				writeVal |= val;
				this.bitSetFile.writeByte(writeVal);
				fl.release();
				return true;
			}
		}
		catch(IOException ie){
			ie.printStackTrace();
			return false;
		}
	}

	public static BigInteger[] convertBigIntegerToLoc(BigInteger location){
		BigInteger maxSize = new BigInteger(new Long(Long.MAX_VALUE).toString());

		return location.divideAndRemainder(maxSize);
	}

}
