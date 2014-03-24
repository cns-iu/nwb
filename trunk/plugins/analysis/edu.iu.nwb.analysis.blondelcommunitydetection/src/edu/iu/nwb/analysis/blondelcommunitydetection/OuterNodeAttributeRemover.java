package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import org.cishell.utilities.FileUtilities;

import com.google.common.base.Joiner;

public final class OuterNodeAttributeRemover {
	private static final String TAB = "\t";
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String SECTION_HEADER = "*";
	private static final String ISOLATE_CLUSTER = "\"isolate";
	private static final Joiner joiner = Joiner.on(TAB);

	private OuterNodeAttributeRemover() {
	}

	public static File removeOutermostNodeAttributeIfDuplicate(File inputFile)
			throws OuterNodeAttributeRemoverException {
		
		File result = inputFile;
		File tmp = null;
		while (tmp != result) {
			tmp = result;
			result = removeDuplicateAttributes(result);
		}
		return result;
	}
	
	private static File removeDuplicateAttributes(File inputFile) throws OuterNodeAttributeRemoverException{
		BufferedReader reader = null;
		Writer writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			File outputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
							"blondel-nwb-", "nwb");
			writer = new BufferedWriter(new FileWriter(outputFile));
	
			if (parseNodeEntries(reader, writer)) {
				parseEdgeEntries(reader, writer);
				return outputFile;
			} else {
				return inputFile;
			}
		} catch (FileNotFoundException e) {
			throw new OuterNodeAttributeRemoverException(
					"Failed to initial " + inputFile, e);
		} catch (IOException e) {
			throw new OuterNodeAttributeRemoverException(
					"Failed pass nodes and edges attributes", e);
		} finally {
			close(reader);
			close(writer);
		}
	}

	private static boolean parseNodeEntries(BufferedReader bufferedReader,
			Writer writer) throws IOException {
		// Node section header
		writeLine(writer, bufferedReader.readLine());
		// Attribute titles
		String line = bufferedReader.readLine();
		String[] lineArray = line.split(TAB);
		writeLine(writer, joiner.join(Arrays.copyOfRange(lineArray, 0, lineArray.length-1)));
		while ((line = bufferedReader.readLine()) != null) {
			lineArray = line.split(TAB);
			int size = lineArray.length;
			if (size == 0) {
				continue;
			}

			// Edge section header
			if (isSectionHeader(lineArray[0])) {
				writeLine(writer, line);
				break;
			}

			if (outestAttributeIsNotDuplicate(lineArray)) {
				return false;
			}

			String newString = joiner.join(Arrays.copyOfRange(lineArray, 0, lineArray.length-1));
			writeLine(writer, newString);
		}
		return true;
	}
	
	private static void writeLine(Writer writer, String line) throws IOException {
		writer.write(line);
		writer.write(NEW_LINE);
	}

	private static void parseEdgeEntries(BufferedReader bufferedReader,
			Writer writer) throws IOException {
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			writeLine(writer, line);
		}
	}

	private static boolean isSectionHeader(String string) {
		return string.startsWith(SECTION_HEADER);
	}

	private static boolean outestAttributeIsNotDuplicate(String[] strings) {
		int size = strings.length;
		if (size < 2 || strings[size - 1].startsWith(ISOLATE_CLUSTER)) {
			return false;
		}
		return !strings[size - 1].equals(strings[size - 2]);
	}

	/*
	 * Quietly closes a @Closeable object (like file I/O). Throws an exception
	 * if close does not work, since that means the algorithm probably will not
	 * work.
	 */
	private static void close(Closeable c)
			throws OuterNodeAttributeRemoverException {
		if (c == null)
			return;
		try {
			c.close();
		} catch (IOException e) {
			throw new OuterNodeAttributeRemoverException(
					"Error when attempting to close an NWB file stream using close() method.",
					e);
		}
	}
}
