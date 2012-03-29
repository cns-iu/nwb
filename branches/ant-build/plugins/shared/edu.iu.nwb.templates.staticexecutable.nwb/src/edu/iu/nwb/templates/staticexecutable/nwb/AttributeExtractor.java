package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AttributeExtractor {
	
	private BufferedReader reader;
	private NumberFormat format = new DecimalFormat();

	public AttributeExtractor(File nodeAttributeFile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(nodeAttributeFile));
	}


	public Number nextValue() {
		String numberString;
		try {
			numberString = this.reader.readLine().trim();
		} catch (IOException e) {
			throw new IllegalArgumentException("This algorithm is unable to read a file it needs to continue.", e);

		}
		try {
			return Double.valueOf(numberString);
		} catch (NumberFormatException e) {
				try {
					return format.parse(numberString);
				} catch (ParseException e1) {
					throw new IllegalArgumentException("This algorithm behaved incorrectly. Please report it to the developers.", e);
				}
		}
	}

}
