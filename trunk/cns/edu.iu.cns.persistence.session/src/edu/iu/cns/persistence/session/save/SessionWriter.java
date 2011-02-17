package edu.iu.cns.persistence.session.save;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.Pair;
import org.cishell.utilities.ZipIOException;
import org.cishell.utilities.ZipUtilities;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.cns.persistence.session.common.Utilities;

public class SessionWriter {
	public SessionWriter(
			Map<Data, Pair<Data, String>> inputToOutputData,
			Map<Data, Integer> outputDataToIndex,
			File targetSessionFile) {
		Map<String, Integer> metadataColumnNameToHeaderIndex =
			compileMetadataSchema(inputToOutputData.values());

		try {
			File metadataFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				Utilities.METADATA_FILE_NAME, Utilities.METADATA_FILE_EXTENSION);
			CSVWriter metadataWriter = new CSVWriter(new FileWriter(metadataFile));
			writeMetadataHeader(metadataColumnNameToHeaderIndex, metadataWriter);

			List<File> outputFiles = new ArrayList<File>();
			int datumIndex = 0;

			for (Data inputDatum : inputToOutputData.keySet()) {
				Pair<Data, String> outputDatumWithTargetMimeType =
					inputToOutputData.get(inputDatum);
				Data outputDatum = outputDatumWithTargetMimeType.getFirstObject();
				String outputDatumTargetMimeType = outputDatumWithTargetMimeType.getSecondObject();
				writeMetadataEntry(
					metadataWriter,
					datumIndex,
					inputDatum,
					outputDatum,
					outputDatumTargetMimeType,
					metadataColumnNameToHeaderIndex,
					outputDataToIndex);
				outputFiles.add((File) outputDatum.getData());
				datumIndex++;
			}

			metadataWriter.close();

			writeSessionFile(metadataFile, outputFiles, targetSessionFile);
		} catch (Exception e) {
//			throw new AlgorithmExecutionException(e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// 1
	private Map<String, Integer> compileMetadataSchema(Collection<Pair<Data, String>> outputData) {
		Map<String, Integer> metadataColumnNameToHeaderIndex =
			new LinkedHashMap<String, Integer>();
		metadataColumnNameToHeaderIndex.put(
			Utilities.DATUM_INDEX_METADATA_KEY, metadataColumnNameToHeaderIndex.size());
		metadataColumnNameToHeaderIndex.put(
			Utilities.FILE_NAME_METADATA_KEY, metadataColumnNameToHeaderIndex.size());
		metadataColumnNameToHeaderIndex.put(
			Utilities.TARGET_MIME_TYPE_METADATA_KEY, metadataColumnNameToHeaderIndex.size());

		for (Pair<Data, String> outputDatum : outputData) {
			Dictionary<String, Object> metadata = outputDatum.getFirstObject().getMetadata();

			for (Enumeration<String> keys = metadata.keys(); keys.hasMoreElements();) {
				String key = keys.nextElement();

				if (!DataProperty.SERVICE_REFERENCE.equals(key) &&
						!metadataColumnNameToHeaderIndex.containsKey(key)) {
					metadataColumnNameToHeaderIndex.put(
						key, metadataColumnNameToHeaderIndex.size());
				}
			}
		}

		return metadataColumnNameToHeaderIndex;
	}

	// 1
	private void writeMetadataHeader(
			Map<String, Integer> metadataColumnNameToHeaderIndex, CSVWriter metadataWriter) {
		String[] header = makeHeader(metadataColumnNameToHeaderIndex);
		metadataWriter.writeNext(header);
	}

	// 1
	private void writeMetadataEntry(
			CSVWriter metadataWriter,
			int datumIndex,
			Data inputDatum,
			Data outputDatum,
			String targetMimeType,
			Map<String, Integer> metadataColumnNameToHeaderIndex,
			Map<Data, Integer> outputDataToIndex) {
		String fileName = ((File) outputDatum.getData()).getName();
		String[] entry = makeEntry(
			datumIndex,
			fileName,
			targetMimeType,
			inputDatum.getMetadata(),
			metadataColumnNameToHeaderIndex,
			outputDataToIndex);
		metadataWriter.writeNext(entry);
	}

	// 1
	private void writeSessionFile(
			File metadataFile,
			Collection<File> outputFiles,
			File targetSessionFile) throws ZipIOException {
		Map<File, String> fileToZippedName = new LinkedHashMap<File, String>();
		String metadataZippedFileName = Utilities.FULL_METADATA_FILE_NAME;
		fileToZippedName.put(metadataFile, metadataZippedFileName);

		for (File outputFile : outputFiles) {
			fileToZippedName.put(outputFile, outputFile.getName());
		}

		ZipUtilities.zipFilesWithNames(fileToZippedName, targetSessionFile);
//		ZipUtilities.zipFiles(outputFiles, this.targetSessionFile);
	}

	// 2
	private String[] makeHeader(Map<String, Integer> metadataColumnNameToHeaderIndex) {
		String[] header = new String[metadataColumnNameToHeaderIndex.size()];

		for (String key : metadataColumnNameToHeaderIndex.keySet()) {
			if (!DataProperty.SERVICE_REFERENCE.equals(key)) {
				int headerIndex = metadataColumnNameToHeaderIndex.get(key);
				header[headerIndex] = key;
			}
		}

		return header;
	}

	// 2
	private String[] makeEntry(
			int datumIndex,
			String fileName,
			String targetMimeType,
			Dictionary<String, Object> metadata,
			Map<String, Integer> metadataColumnNameToHeaderIndex,
			Map<Data, Integer> outputDataToIndex) {
		String[] entry = new String[metadataColumnNameToHeaderIndex.size()];
		entry[metadataColumnNameToHeaderIndex.get(Utilities.DATUM_INDEX_METADATA_KEY)] =
			Integer.toString(datumIndex);
		entry[metadataColumnNameToHeaderIndex.get(Utilities.FILE_NAME_METADATA_KEY)] = fileName;
		entry[metadataColumnNameToHeaderIndex.get(Utilities.TARGET_MIME_TYPE_METADATA_KEY)] =
			targetMimeType;

		for (String key : metadataColumnNameToHeaderIndex.keySet()) {
			if (!Utilities.DATUM_INDEX_METADATA_KEY.equals(key) &&
					!Utilities.FILE_NAME_METADATA_KEY.equals(key) &&
					!Utilities.TARGET_MIME_TYPE_METADATA_KEY.equals(key) &&
					!DataProperty.SERVICE_REFERENCE.equals(key)) {
				int index = metadataColumnNameToHeaderIndex.get(key);
				Object metadataProperty = metadata.get(key);

				if (metadataProperty != null) {
					if (DataProperty.PARENT.equals(key)) {
						System.err.println("DATA NAME: " + metadata.get(DataProperty.LABEL));
						Data parent = (Data) metadataProperty;
						int parentIndex = outputDataToIndex.get(parent);
						entry[index] = Integer.toString(parentIndex);
					} else {
						entry[index] = metadataProperty.toString();
					}
				} else {
					entry[index] = "";
				}
			}
		}

		return entry;
	}
}