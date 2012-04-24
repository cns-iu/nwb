package edu.iu.sci2.medline.common;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.osgi.service.log.LogService;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * This class is for parsing a MEDLINE®/PubMed® file into {@link MedlineRecord}
 * (s). *
 */
public class MedlineRecordParser {
	private Scanner recordScanner;
	private LogService logger;
	/**
	 * <p>Create a parser that can return the records one at a time.</p>
	 * 
	 * <p>If this functionality is not needed @see {@link MedlineRecordParser#getAllRecords(BufferedReader)}.</p>
	 */
	public MedlineRecordParser(BufferedReader reader, LogService logger) {
		Preconditions.checkNotNull(reader);
		Preconditions.checkNotNull(logger);
		this.logger = logger;
		this.recordScanner = new Scanner(reader).useDelimiter(Pattern.compile(
				"^\\s*$", Pattern.MULTILINE));
	}

	/**
	 * @return {@code True} if there are more records to be parsed, {@code False} otherwise
	 */
	public boolean hasNext() {
		return this.recordScanner.hasNext();
	}

	/**
	 * @return The next {@link MedlineRecord}
	 * @throws {@link NoSuchElementException} - if no more {@link MedlineRecord}s are available 
	 */
	public MedlineRecord getNext() {
		String nextRecordString = this.recordScanner.next();
		return new MedlineRecord(nextRecordString, this.logger);
	}

	/**
	 * <p>Return all the {@link MedlineRecord}s from a reader at once.</p>
	 */
	public static ImmutableList<MedlineRecord> getAllRecords(
			BufferedReader reader, LogService logger) {
		List<MedlineRecord> records = new ArrayList<MedlineRecord>();
		MedlineRecordParser recordParser = new MedlineRecordParser(reader, logger);
		while (recordParser.hasNext()) {
			records.add(recordParser.getNext());
		}
		return ImmutableList.copyOf(records);
	}
}
