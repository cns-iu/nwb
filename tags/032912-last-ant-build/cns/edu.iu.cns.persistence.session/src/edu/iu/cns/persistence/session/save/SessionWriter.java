package edu.iu.cns.persistence.session.save;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.Pair;
import org.cishell.utilities.ZipIOException;
import org.cishell.utilities.ZipUtilities;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.BiMap;

import edu.iu.cns.persistence.session.common.Utilities;

public class SessionWriter {
	public SessionWriter(
			Map<Data, Pair<Data, String>> inputToOutputData,
			Map<Data, Integer> outputDataToIndex,
			BiMap<Data, Data> originalToConvertedData,
			File targetSessionFile) {
		Map<String, Integer> metadataColumnNameToHeaderIndex =
			compileMetadataSchema(inputToOutputData.values());

		try {
			File metadataFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				Utilities.METADATA_FILE_NAME, Utilities.METADATA_FILE_EXTENSION);
			CSVWriter metadataWriter = new CSVWriter(new FileWriter(metadataFile));
			writeMetadataHeader(metadataColumnNameToHeaderIndex, metadataWriter);

			Map<File, String> outputFileToEntryName = new LinkedHashMap<File, String>();
			int datumIndex = 0;

			for (Data inputDatum : inputToOutputData.keySet()) {
				Pair<Data, String> outputDatumAndToolMimeType =
					inputToOutputData.get(inputDatum);
				Data outputDatum = outputDatumAndToolMimeType.getFirstObject();
				String outputDatumToolMimeType = outputDatumAndToolMimeType.getSecondObject();
				String zippedFileName = formZippedFileName(outputDatum);
				
				writeMetadataEntry(
					metadataWriter,
					datumIndex,
					zippedFileName,
					inputDatum,
					outputDatum,
					outputDatumToolMimeType,
					metadataColumnNameToHeaderIndex,
					outputDataToIndex,
					originalToConvertedData);

				outputFileToEntryName.put((File) outputDatum.getData(), zippedFileName);
				datumIndex++;
			}

			metadataWriter.close();

			writeSessionFile(metadataFile, outputFileToEntryName, /*outputFiles,*/ targetSessionFile);
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
			Utilities.ZIPPED_FILE_NAME_KEY, metadataColumnNameToHeaderIndex.size());
		metadataColumnNameToHeaderIndex.put(
			Utilities.FILE_NAME_METADATA_KEY, metadataColumnNameToHeaderIndex.size());
		metadataColumnNameToHeaderIndex.put(
			Utilities.TOOL_MIME_TYPE_METADATA_KEY, metadataColumnNameToHeaderIndex.size());

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
	private String formZippedFileName(Data datum) {
		String zippedFileName = String.format(
			"Data_%d_%s", datum.hashCode(), ((File) datum.getData()).getName());

		return zippedFileName;
	}

	// 1
	private void writeMetadataEntry(
			CSVWriter metadataWriter,
			int datumIndex,
			String zippedFileName,
			Data inputDatum,
			Data outputDatum,
			String toolMimeType,
			Map<String, Integer> metadataColumnNameToHeaderIndex,
			Map<Data, Integer> outputDataToIndex,
			BiMap<Data, Data> originalToConvertedData) {
		String fileName = ((File) outputDatum.getData()).getName();
		
		String[] entry = makeEntry(
			datumIndex,
			zippedFileName,
			fileName,
			toolMimeType,
			inputDatum.getMetadata(),
			metadataColumnNameToHeaderIndex,
			outputDataToIndex,
			originalToConvertedData);
		
		metadataWriter.writeNext(entry);
	}

	// 1
	private void writeSessionFile(
			File metadataFile,
			Map<File, String> outputFileToEntryName,
//			Collection<File> outputFiles,
			File targetSessionFile) throws ZipIOException {
		Map<File, String> fileToZippedName = new LinkedHashMap<File, String>(outputFileToEntryName);
		fileToZippedName.put(metadataFile, Utilities.FULL_METADATA_FILE_NAME);

		/*for (File outputFile : outputFiles) {
			fileToZippedName.put(outputFile, outputFile.getName());
		}*/

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
			String zippedFileName,
			String fileName,
			String toolMimeType,
			Dictionary<String, Object> metadata,
			Map<String, Integer> metadataColumnNameToHeaderIndex,
			Map<Data, Integer> outputDataToIndex,
			BiMap<Data, Data> originalToConvertedData) {
		String[] entry = new String[metadataColumnNameToHeaderIndex.size()];
		entry[metadataColumnNameToHeaderIndex.get(Utilities.DATUM_INDEX_METADATA_KEY)] =
			Integer.toString(datumIndex);
		entry[metadataColumnNameToHeaderIndex.get(Utilities.ZIPPED_FILE_NAME_KEY)] =
			zippedFileName;
		entry[metadataColumnNameToHeaderIndex.get(Utilities.FILE_NAME_METADATA_KEY)] = fileName;
		entry[metadataColumnNameToHeaderIndex.get(Utilities.TOOL_MIME_TYPE_METADATA_KEY)] =
			toolMimeType;

		for (String key : metadataColumnNameToHeaderIndex.keySet()) {
			if (!Utilities.DEFAULT_METADATA_KEYS.contains(key)) {
				int index = metadataColumnNameToHeaderIndex.get(key);
				Object metadataProperty = metadata.get(key);

				if (metadataProperty != null) {
					if (DataProperty.PARENT.equals(key)) {
						Data rawParent = (Data) metadataProperty;
						Data actualParent = originalToConvertedData.get(rawParent);
						int actualParentIndex = outputDataToIndex.get(actualParent);
						entry[index] = Integer.toString(actualParentIndex);
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