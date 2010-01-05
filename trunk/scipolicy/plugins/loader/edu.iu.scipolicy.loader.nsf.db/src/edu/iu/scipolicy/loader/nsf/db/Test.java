package edu.iu.scipolicy.loader.nsf.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFileChooser;

import org.cishell.utilities.StringUtilities;

public class Test {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
		// TODO Auto-generated method stub
		
		String[] originalTokens = {"chintan tank", "Pal, Ranadip", "Waller, S. Travis", "Hu, Xiaohua (Tony)",
									"Sylvia J. Spengler", "Xiaoyang Wang", "chin d tAnk"};
		for (String token : originalTokens) {
			
			String[] nameTokens;
			String lastName, firstName, formattedFullName;
			String middleInitial = "";
			String originalInputName = token;
			
			/*
			 * For Names which follow,
			 * 		"LastName, FirstName"
			 * 		"LastName, FirstInitial. FirstName"
			 * 		"LastName, FirstName (AlternativeName)"
			 * Principal Investigators follow these syntax.
			 * */
			if (token.contains(",")) {
				//TODO: Right now there is no further division of the first name i.e.
				//Everything after the first "," comes in FirstName. Should change this 
				//in the future.
				nameTokens = token.split(",");
				
				lastName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[0]));
				firstName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[1]));;
				
			} else {
				
				/*
				 * For Names which follow,
				 * 		"FirstName LastName"
				 * 		"FirstName MiddleInitial. LastName"
				 * 		"FirstName MiddleInitial LastName"
				 * Program Manager, CO-PI names follow these syntax.
				 * */
				nameTokens = token.split("\\s");
				
				firstName = StringUtilities.simpleClean(
						StringUtilities.toSentenceCase(
								nameTokens[0]));;
								
				if (nameTokens.length == 2) {
					lastName = StringUtilities.simpleClean(
									StringUtilities.toSentenceCase(
											nameTokens[1]));

				} else if (nameTokens.length == 3){
					lastName = StringUtilities.simpleClean(
									StringUtilities.toSentenceCase(
											nameTokens[2]));
					
					String middleInitialCandidate = StringUtilities.simpleClean(
														StringUtilities.toSentenceCase(
																nameTokens[1]));
					
					/*
					 * Since we want to remove trailing "." character from MI.
					 * */
					middleInitial = middleInitialCandidate.substring(0, 1);
					
				} else {
					lastName = "";
				}
			}
			
			formattedFullName = lastName + ", " + firstName + " " + middleInitial;
			
			System.out.println("ln: " + lastName);
			System.out.println("fn: " + firstName);
			System.out.println("mi: " + middleInitial);
			System.out.println("oi: " + originalInputName);
			System.out.println("fo: " + formattedFullName + "\n");
		}
		
		
		
		
		File f = new File("C:\\Documents and Settings\\Administrator\\workspace-slis\\workspace\\edu.iu.scipolicy.loader.nsf.db\\sample_nsf.csv");
		
		String output = computeMD5Checksum(f);
		
		System.out.println("MD5: " + output);
		
		
		

	}

	/**
	 * @param f
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 */
	private static String computeMD5Checksum(File f)
			throws NoSuchAlgorithmException, FileNotFoundException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		System.out.println(f.getName());
		
		
		JFileChooser chooser = new JFileChooser();
	    
	    String fileTypeName = chooser.getTypeDescription(f);
	    System.out.println("File Type= " + fileTypeName);
		
		InputStream is = new FileInputStream(f);				
		byte[] buffer = new byte[8192];
		int read = 0;
		String output = "";
		try {
			while( (read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}		
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			output = bigInt.toString(16);
			
		}
		catch(IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		}
		finally {
			try {
				is.close();
			}
			catch(IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
		return output;
	}

}
