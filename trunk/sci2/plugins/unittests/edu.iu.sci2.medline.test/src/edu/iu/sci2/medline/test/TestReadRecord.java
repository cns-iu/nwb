package edu.iu.sci2.medline.test;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.cishell.utilities.UnicodeReader;
import org.junit.Before;
import org.junit.Test;

import prefuse.data.Table;
import edu.iu.sci2.medline.common.MedlineRecordParser;
import edu.iu.sci2.medline.converter.medline_to_medlinetable_converter.MedlineFileReader;
import edu.iu.sci2.medline.converter.medline_to_medlinetable_converter.MedlineFileReader.MedlineFileReaderException;

public class TestReadRecord {
	private File file;
	private BufferedReader reader;

	
	@Before
	public void setup() throws FileNotFoundException {
		this.file = new File ("files/pubmed_result_5.txt");
		this.reader = new BufferedReader(new UnicodeReader(
				new FileInputStream(this.file)));
	}
	
	@Test
	public void testMedlineRecordParser() {
		MedlineRecordParser parser = new MedlineRecordParser(this.reader);
		while (parser.hasNext()){
			System.out.println(parser.getNext());
		}
	}
	
	@Test
	public void testTable() {
		try {
			Table table = MedlineFileReader.read(this.file, null);
			System.out.println(table);
		} catch (MedlineFileReaderException e) {
			e.printStackTrace();
			fail();
		}
	}

}
