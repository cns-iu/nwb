package edu.iu.iv.modeling.tarl.publication.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Vector;

import edu.iu.iv.modeling.tarl.publication.AgingHelperInterface;


/**
 * This class provides a default implementation for the <code>AgingHelperInterface</code>.  It uses the same probability distribution as used in the PNAS paper to generate a random number.  It reads this distrbution from a file on the disk.
 *
 * @author Jeegar T Maru
 * @see DefaultPublicationManager
 * @see AgingHelperInterface
 */
public class DefaultAgingHelper implements AgingHelperInterface {
	/**
	 * Stores the probability distribution for simulating the aging effect on citations
	 */
	protected Vector agingProbabilityDistribution;

	/**
	 * Stores the cumulative total of the frequencies of citations
	 */
	protected int totalFrequency;

	/**
	 * Initializes the aging helper
	 */
	public void initialize(File agingFile) {
		String line;
		BufferedReader agingReader;

		agingProbabilityDistribution = new Vector();
		totalFrequency = 0;

		try {
			agingReader =
				new BufferedReader(new FileReader(agingFile));

			while ((line = agingReader.readLine()) != null) {
				totalFrequency += (Float.valueOf(line)).intValue();
				agingProbabilityDistribution.addElement(new Integer(
				        totalFrequency));
			}
		} catch (Exception e) {
			System.err.println(
			    "Exception in reading the input aging function file: " + e);
			System.err.println("Model continuing with default aging values...");

			agingProbabilityDistribution = new Vector();
			totalFrequency = 0;

			totalFrequency += 4607;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 8777;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 12026;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 13992;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 14535;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 13770;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 12023;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 9731;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 7324;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 5135;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 3358;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 2048;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 1166;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 619;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 307;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 142;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 61;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 25;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 9;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 3;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
			totalFrequency += 1;
			agingProbabilityDistribution.addElement(new Integer(totalFrequency));
		}
	}

	/**
	 * Returns a random positive number (or 0) which simulates the aging effect among citations
	 *
	 * @return the random positive number (or 0)
	 */
	public int getRandomYearDifference() {
		int vectorIndex;
		int randomNum;
		Random randomGenerator;

		randomGenerator = new Random();
		randomNum = randomGenerator.nextInt(totalFrequency);

		vectorIndex = 0;

		while (randomNum >= ((Integer)(agingProbabilityDistribution.get(
			        vectorIndex))).intValue())
			vectorIndex++;

		return (vectorIndex + 1);
	}
}
