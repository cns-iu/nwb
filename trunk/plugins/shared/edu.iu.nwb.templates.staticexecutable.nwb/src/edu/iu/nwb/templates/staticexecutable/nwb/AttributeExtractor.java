package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AttributeExtractor {
	
	private BufferedReader reader;

	public AttributeExtractor(File nodeAttributeFile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(nodeAttributeFile));
	}


	public Double nextValue() {
		try {
			return Double.valueOf(this.reader.readLine().trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("This algorithm behaved incorrectly. Please report it to the developers.", e);
		} catch (IOException e) {
			throw new IllegalArgumentException("This algorithm is unable to read a file it needs to continue.", e);
		}
	}

}
