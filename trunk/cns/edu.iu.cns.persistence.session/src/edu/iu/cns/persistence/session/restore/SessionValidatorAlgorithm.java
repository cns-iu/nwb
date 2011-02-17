package edu.iu.cns.persistence.session.restore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.fileloader.FileLoadException;
import org.cishell.app.service.fileloader.FileLoaderService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.utilities.CollectionUtilities;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.ZipUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import edu.iu.cns.persistence.session.common.Utilities;

public class SessionValidatorAlgorithm implements Algorithm, ProgressTrackable {
	private BundleContext bundleContext;
	private CIShellContext ciShellContext;
	private Data inputData;
	private String fileName;
	private LogService logger;
	private DataManagerService dataManager;
	private DataConversionService dataConverter;
	private FileLoaderService fileLoader;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	public SessionValidatorAlgorithm(
			BundleContext bundleContext,
			CIShellContext ciShellContext,
			Data inputData,
			String fileName,
			LogService logger,
			DataManagerService dataManager,
			DataConversionService dataConverter,
			FileLoaderService fileLoader) {
		this.bundleContext = bundleContext;
		this.ciShellContext = ciShellContext;
		this.inputData = inputData;
		this.fileName = fileName;
		this.logger = logger;
		this.dataManager = dataManager;
		this.dataConverter = dataConverter;
		this.fileLoader = fileLoader;
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
		// TODO: Validate header.
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
		Collection<Data> data = new HashSet<Data>();
		Multimap<Integer, Data> validatedDataByIndex = LinkedHashMultimap.create();

		for (Integer datumIndex : metadata.keySet()) {
			try {
				Map<String, String> datumMetadata = metadata.get(datumIndex);
				String fileName = datumMetadata.get(Utilities.FILE_NAME_METADATA_KEY);
				File file = ZipUtilities.readFileFromZipFile(
					entriesByName.get(fileName), sourceSessionFile);
				String fileExtension = FileUtilities.getFileExtension(fileName).substring(1);

				Data[] validatedData = this.fileLoader.loadFileOfType(
					this.bundleContext,
					this.ciShellContext,
					this.logger,
					this.progressMonitor,
					file,
					fileExtension,
					datumMetadata.get(Utilities.TARGET_MIME_TYPE_METADATA_KEY));

				for (Data validatedDatum : validatedData) {
					data.add(validatedDatum);
					validatedDataByIndex.put(datumIndex, validatedDatum);

					Dictionary<String, Object> validatedDatumMetadata =
						validatedDatum.getMetadata();

					for (String key : datumMetadata.keySet()) {
						if (!Utilities.DATUM_INDEX_METADATA_KEY.equals(key) &&
								!Utilities.FILE_NAME_METADATA_KEY.equals(key) &&
								!Utilities.TARGET_MIME_TYPE_METADATA_KEY.equals(key) &&
								!DataProperty.PARENT.equals(key)) {
							validatedDatumMetadata.put(key, datumMetadata.get(key));
						} else if (DataProperty.PARENT.equals(key)) {
							String parentString = datumMetadata.get(key);

							if (!StringUtilities.isNull_Empty_OrWhitespace(parentString)) {
								validatedDatumMetadata.put(key, parentString);
							}
						}
					}
				}
			} catch (FileLoadException e) {
				this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			} catch (IOException e) {
				this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			}
		}

		for (Integer datumIndex : validatedDataByIndex.keySet()) {
			for (Data datum : validatedDataByIndex.get(datumIndex)) {
				Dictionary<String, Object> datumMetadata = datum.getMetadata();
				Object parentIndexObject = datumMetadata.get(DataProperty.PARENT);

				if (parentIndexObject != null) {
					try {
						String parentIndexString = parentIndexObject.toString();

						if (!StringUtilities.isNull_Empty_OrWhitespace(parentIndexString)) {
							int parentIndex = Integer.parseInt(parentIndexString);

							if (parentIndex != datumIndex) {
								Data parent = CollectionUtilities.get(
									validatedDataByIndex.get(parentIndex), 0);
								datumMetadata.put(DataProperty.PARENT, parent);
							} else {
								String format =
									"Error: Data at index %d is its own child/parent. " +
									"This is either a programmer error (in which this should be " +
									"reported as a bug) or a result of a manually-created " +
									"session file.";
								String logMessage = String.format(format, parentIndex);
								this.logger.log(LogService.LOG_ERROR, logMessage);
							}
						}
					} catch (Throwable e) {
						e.printStackTrace();
						this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
					}
				}
			}
		}

		return data.toArray(new Data[0]);
	}

	void addDataToDataManager(Data[] data) {
		for (Data datum : data) {
			this.dataManager.addData(datum);
		}
	}

//	private Map<Integer, Map<String, String>> readMetadata(File metadataFile)
//			throws FileNotFoundException, IOException {
//		Map<Integer, Map<String, String>> metadata =
//			new LinkedHashMap<Integer, Map<String, String>>();
//		Properties properties = new Properties();
//		properties.load(new FileInputStream(metadataFile));
//		Multimap<String, String> datumIndexToKeysFound =
//			determineDatumIndicesToKeysFound(properties);
//		System.err.println("datumIndexToKeysFound: " + datumIndexToKeysFound);
//
//		for (int ii = 0; properties.containsKey(ii); ii++) {
//			Map<String, String> datumMetadata = getOrCreateDatumMetadata(metadata, ii);
//		}
//
//		return metadata;
//	}
//
//	private Multimap<String, String> determineDatumIndicesToKeysFound(Properties properties) {
//		Multimap<String, String> datumIndexToKeysFound = HashMultimap.create();
//
//		for (Object keyObject : properties.keySet()) {
//			String key = keyObject.toString();
//			System.err.println("key: " + key);
//			String[] keyTokens = key.split("\\.");
//			System.err.println("keyTokens: " + keyTokens.length + " " + keyTokens);
//
//			if (keyTokens.length == 2) {
//				try {
//					int datumIndex = Integer.parseInt(keyTokens[0]);
//
//					if (properties.containsKey(keyTokens[0])) {
//						datumIndexToKeysFound.put(keyTokens[0], keyTokens[1]);
//					}
//				} catch (Throwable e) {}
//			}
//		}
//
//		return datumIndexToKeysFound;
//	}
//
//	private Map<String, String> getOrCreateDatumMetadata(
//			Map<Integer, Map<String, String>> metadata, int datumIndex) {
//		if (metadata.containsKey(datumIndex)) {
//			return metadata.get(datumIndex);
//		} else {
//			metadata.put(datumIndex, new HashMap<String, String>());
//
//			return metadata.get(datumIndex);
//		}
//	}
//
//	public static void main(String[] args) {
//		String[] tokens = "1.type".split("\\.");
//
//		for (String token : tokens) {
//			System.err.println(token);
//		}
//	}
}