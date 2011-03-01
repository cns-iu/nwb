package edu.iu.cns.persistence.session.restore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.ZipUtilities;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.iu.cns.persistence.session.common.Utilities;

public class SessionValidatorAlgorithm implements Algorithm, ProgressTrackable {
	private String fileName;
	private LogService logger;
	private DataManagerService dataManager;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	public SessionValidatorAlgorithm(
			String fileName,
			LogService logger,
			DataManagerService dataManager) {
		this.fileName = fileName;
		this.logger = logger;
		this.dataManager = dataManager;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			ZipFile sessionFile = new ZipFile(this.fileName);
			Map<String, ZipEntry> entriesByName =
				ZipUtilities.mapFileNamesToEntries(sessionFile, false);

			if (!entriesByName.containsKey(Utilities.FULL_METADATA_FILE_NAME)) {
				String format =
					"Session file %s could not be loaded because it is missing " +
					"the required metadata file (%s).";
				String exceptionMessage =
					String.format(format, this.fileName, Utilities.FULL_METADATA_FILE_NAME);
				throw new AlgorithmExecutionException(exceptionMessage);
			}

			ZipEntry metadataEntry = entriesByName.get(Utilities.FULL_METADATA_FILE_NAME);
			File metadataFile = ZipUtilities.readFileFromZipFile(metadataEntry, sessionFile);
			Map<Integer, Map<String, String>> metadata = readMetadata(metadataFile);
			Data[] builtData = buildData(entriesByName, metadata, sessionFile);

			return builtData;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	private Map<Integer, Map<String, String>> readMetadata(File metadataFile)
			throws FileNotFoundException, IOException {
		Map<Integer, Map<String, String>> metadata =
			new LinkedHashMap<Integer, Map<String, String>>();
		CSVReader reader = new CSVReader(new FileReader(metadataFile));
		BiMap<String, Integer> header =
			HashBiMap.create(MapUtilities.mapKeyToIndices(Arrays.asList(reader.readNext())));
		// TODO: Validate header. (Maybe eventually.)
		int datumIndexKeyIndex = header.get(Utilities.DATUM_INDEX_METADATA_KEY);
		String[] line;

		while ((line = reader.readNext()) != null) {
			int datumIndex = Integer.parseInt(line[datumIndexKeyIndex]);
			Map<String, String> datumMetadata = new HashMap<String, String>();

			for (int ii = 0; ii < line.length; ii++) {
				if (ii != datumIndexKeyIndex) {
					String key = header.inverse().get(ii);
					String value = line[header.get(key)];
					datumMetadata.put(key, value);
				}
			}

			metadata.put(datumIndex, datumMetadata);
		}

		return metadata;
	}

	private Data[] buildData(
			Map<String, ZipEntry> entriesByName,
			Map<Integer, Map<String, String>> metadata,
			ZipFile sourceSessionFile) {
		Map<Integer, Data> datumIndexToDatum = new LinkedHashMap<Integer, Data>();

		for (Integer datumIndex : metadata.keySet()) {
			try {
				Map<String, String> datumMetadata = metadata.get(datumIndex);
				String zippedFileName = datumMetadata.get(Utilities.ZIPPED_FILE_NAME_KEY);
//				String fileName = datumMetadata.get(Utilities.FILE_NAME_METADATA_KEY);
				File file = ZipUtilities.readFileFromZipFile(
					entriesByName.get(zippedFileName), sourceSessionFile);
				String toolMimeType = datumMetadata.get(Utilities.TOOL_MIME_TYPE_METADATA_KEY);

				/* TODO: There's a chance that this branch structure should be reinstated, but I
				 * don't think that's very high.
				 * Data are always saved out as files, and we happen to have bidirectional
				 * converters for all of our supported formats (at least, as far as session saving
				 * goes), so it's basically always safe to assume that isFileMimeType(toolMimeType)
				 * is true.
				 */
//				if (isFileMimeType(toolMimeType)) {
				Data readDatum = new BasicData(file, toolMimeType);
				fillMetadata(readDatum, datumMetadata);
				datumIndexToDatum.put(datumIndex, readDatum);
//				} else {
//					System.err.println("askldjfsdjfkljldfjskldfjklasdjfklasjdfkljsfklj");
//					String readDatumFileExtension = FileUtilities.getFileExtension(fileName);
//					String fileMimeType = String.format("file-ext:%s", readDatumFileExtension);
//					Data readDatum = new BasicData(file, fileMimeType);
//					Data convertedDatum = this.dataConverter.convert(readDatum, toolMimeType);
//					fillMetadata(convertedDatum, datumMetadata);
//				}
			} catch (IOException e) {
				this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			}
			/* TODO: See above TODO.
			} catch (ConversionException e) {
				e.printStackTrace();
				this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			} */
		}

		for (Integer datumIndex : datumIndexToDatum.keySet()) {
			Data datum = datumIndexToDatum.get(datumIndex);
			Map<String, String> readDatumMetadata = metadata.get(datumIndex);
			Integer parentDatumIndex = getParentIndex(readDatumMetadata);

			if (parentDatumIndex != null) {
				Dictionary<String, Object> datumMetadata = datum.getMetadata();
				Data parentDatum = datumIndexToDatum.get(parentDatumIndex);
				datumMetadata.put(DataProperty.PARENT, parentDatum);
			}
		}

		for (Data datum : datumIndexToDatum.values()) {
			this.dataManager.addData(datum);
		}

		/* Assuming you have a firm understanding of how CIShell works, you know that validators,
		 * converters, file handlers, etc. are all just special forms of algorithms.
		 * Converters are handled specially when it comes to the conversion service, and validators
		 * are handled specially when it comes to file loading.
		 * Because validators are actually the "entry point" of files into the tool (read: Data
		 * Manager), they are expected to ALWAYS return an actual Data[] in their execute() method.
		 * Most of the time, it's okay if an algorithm returns null or a Data[], but since THIS
		 * very algorithm happens to be a validator, we have to return Data[0] here.
		 */
		return new Data[0];
	}

//	private boolean isFileMimeType(String mimeType) {
//		return (
//			mimeType.startsWith(Utilities.FILE_EXTENSION) || mimeType.startsWith(Utilities.FILE));
//	}

	private void fillMetadata(Data datum, Map<String, String> sourceMetadata) {
		Dictionary<String, Object> readDatumMetadata = datum.getMetadata();

		for (String key : sourceMetadata.keySet()) {
			if (!Utilities.METADATA_KEYS_TO_IGNORE_WHEN_READING.contains(key)) {
				readDatumMetadata.put(key, sourceMetadata.get(key));
			}
		}
	}

	private Integer getParentIndex(Map<String, String> readDatumMetadata) {
		if (readDatumMetadata.containsKey(DataProperty.PARENT)) {
			try {
				return Integer.parseInt(readDatumMetadata.get(DataProperty.PARENT));
			} catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	void addDataToDataManager(Data[] data) {
		for (Data datum : data) {
			this.dataManager.addData(datum);
		}
	}
}