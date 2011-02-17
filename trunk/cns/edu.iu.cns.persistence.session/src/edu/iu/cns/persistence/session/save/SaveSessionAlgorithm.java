package edu.iu.cns.persistence.session.save;

import java.io.File;
import java.io.IOException;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class SaveSessionAlgorithm implements Algorithm {
	public static final String ANY_FILE_EXTENSION = "file-ext:*";

//	private BundleContext bundleContext;
	private File targetSessionFile;
	private LogService logger;
	private DataManagerService dataManager;
	private DataConversionService dataConverter;

	public SaveSessionAlgorithm(
			BundleContext bundleContext,
			File targetSessionFile,
			LogService logger,
			DataManagerService dataManager,
			DataConversionService dataConverter) {
//		this.bundleContext = bundleContext;
		this.targetSessionFile = targetSessionFile;
		this.logger = logger;
		this.dataManager = dataManager;
		this.dataConverter = dataConverter;

		try {
			if (!this.targetSessionFile.exists()) {
				this.targetSessionFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
//			throw new AlgorithmCreationFailedException(e.getMessage(), e);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// 0
	public Data[] execute() throws AlgorithmExecutionException {
		SessionDataGetter sessionDataGetter = new SessionDataGetter(this.logger, this.dataManager);
		Data[] allData = sessionDataGetter.getData();

		if (allData == null) {
			return null;
		}

		SessionDataConverter sessionDataConverter =
			new SessionDataConverter(this.logger, this.dataConverter, allData);
		SessionWriter sessionWriter = new SessionWriter(
			sessionDataConverter.getInputToOutputData(),
			sessionDataConverter.getOutputDataToIndex(),
			this.targetSessionFile);

		return null;
	}

	// 1
//	private Pair<Converter, String> chooseBestConverter(Data datum) {
//		Converter[] outputConverters = getOutputConverters(datum);
//		Multimap<Converter, Converter> outputToInputConverters =
//			getOutputToInputConverters(outputConverters, datum);
//
//		if (outputToInputConverters.size() > 0) {
//			return chooseBestOutputConverterConsideringInputConverters(
//				outputConverters, outputToInputConverters);
//		} else {
//			return new Pair<Converter, String>(
//				chooseBestOutputConverter(outputConverters), datum.getFormat());
//		}
//	}

	// 2
//	private Converter[] getOutputConverters(Data datum) {
//		return this.dataConverter.findConverters(datum, ANY_FILE_EXTENSION);
//	}
//
//	// 2
//	private Multimap<Converter, Converter> getOutputToInputConverters(
//			Converter[] outputConverters, Data datum) {
//		Multimap<Converter, Converter> outputToInputConverters = LinkedHashMultimap.create();
//
//		for (Converter outputConverter : outputConverters) {
//			String outputMimeType =
//				outputConverter.getProperties().get(AlgorithmProperty.OUT_DATA).toString();
//			Collection<String> validatedInputMimeTypes =
//				getValidatedInputMimeTypesForReadingOutputMimeTypeBackIn(outputMimeType);
//			Collection<Converter> inputConverters =
//				getInputConverters(validatedInputMimeTypes, datum);
//			outputToInputConverters.putAll(outputConverter, inputConverters);
//		}
//
//		return outputToInputConverters;
//	}
//
//	// 2
//	private Pair<Converter, String> chooseBestOutputConverterConsideringInputConverters(
//			Converter[] outputConverters, Multimap<Converter, Converter> outputToInputConverters) {
//		System.err.println("\t\tchooseBestOutputConverterConsideringInputConverters");
//		if (outputConverters.length == 0) {
//			return null;
//		}
//
//		Collection<Converter> losslessOutputConverters = new HashSet<Converter>();
//		Collection<Converter> lossyOutputConverters = new HashSet<Converter>();
//		Multimap<Converter, Converter> losslessOutputToLosslessInputConverters =
//			HashMultimap.create();
//		Multimap<Converter, Converter> losslessOutputToLossyInputConverters =
//			HashMultimap.create();
//		Multimap<Converter, Converter> lossyOutputToLosslessInputConverters =
//			HashMultimap.create();
//		Multimap<Converter, Converter> lossyOutputToLossyInputConverters = HashMultimap.create();
//
//		Converter losslessOutput_CandidateConverter = null;
//		String losslessOutput_CandidateTargetMimeType = null;
//		Converter losslessInput_CandidateConverter = null;
//		String losslessInput_CandidateTargetMimeType = null;
//
//		/* We prefer a lossless-output converter whose output can also be converted
//		 * back losslessly.
//		 * If we don't find that, we'll take the first lossless converter we find that can be
//		 * converted back.
//		 */
//
//		for (Converter outputConverter : outputConverters) {
//			if (AlgorithmProperty.LOSSLESS.equals(outputConverter.calculateLossiness())) {
//				losslessOutputConverters.add(outputConverter);
//
//				for (Converter inputConverter : outputToInputConverters.get(outputConverter)) {
//					if (AlgorithmProperty.LOSSLESS.equals(inputConverter.calculateLossiness())) {
//						losslessOutputToLosslessInputConverters.put(
//							outputConverter, inputConverter);
////						return new Pair<Converter, String>(
////							outputConverter,
////							inputConverter.getProperties().get(
////								AlgorithmProperty.IN_DATA).toString());
//					} else if (losslessOutput_CandidateConverter == null) {
//						losslessOutputToLossyInputConverters.put(outputConverter, inputConverter);
//						losslessOutput_CandidateConverter = outputConverter;
//						losslessOutput_CandidateTargetMimeType =
//							inputConverter.getProperties().get(
//								AlgorithmProperty.IN_DATA).toString();
//					}
//				}
//			} else {
//				lossyOutputConverters.add(outputConverter);
//
//				for (Converter inputConverter : outputToInputConverters.get(outputConverter)) {
//					if (AlgorithmProperty.LOSSLESS.equals(inputConverter.calculateLossiness()) &&
//							(losslessInput_CandidateConverter != null)) {
//						lossyOutputToLosslessInputConverters.put(outputConverter, inputConverter);
//						losslessInput_CandidateConverter = outputConverter;
//						losslessInput_CandidateTargetMimeType = inputConverter.getProperties().get(
//							AlgorithmProperty.IN_DATA).toString();
//					} else {
//						lossyOutputToLossyInputConverters.put(outputConverter, inputConverter);
//					}
//				}
//			}
//		}
//
//		if (losslessOutputToLosslessInputConverters.size() > 0) {
//			
//		}
//
//		/* If our lossless-output candidate is not null, we found a lossless-output converter that
//		 * doesn't have a corresponding lossless input converter, but rather does have a lossy
//		 * input converter.
//		 */
//
//		if (losslessOutput_CandidateConverter != null) {
//			return new Pair<Converter, String>(
//				losslessOutput_CandidateConverter, losslessOutput_CandidateTargetMimeType);
//		}
//
//		/* Since our lossless-output candidate IS null, we didn't find a lossless-output converter
//		 * whose output can be converted back.
//		 * So, we'll take the first output converter whose output can be converted back losslessly.
//		 */
//
//		if (losslessInput_CandidateConverter != null) {
//			return new Pair<Converter, String>(
//				losslessInput_CandidateConverter, losslessInput_CandidateTargetMimeType);
//		}
//
//		/* Our lossless-input candidate is null, which means we'll take the first converter whose
//		 * output can be converted back.
//		 */
//
//		for (Converter outputConverter : outputConverters) {
//			Collection<Converter> inputConverters = outputToInputConverters.get(outputConverter);
//
//			if (inputConverters.size() > 0) {
//				Converter inputConverter = CollectionUtilities.get(inputConverters, 0);
//
//				return new Pair<Converter, String>(
//					outputConverter,
//					inputConverter.getProperties().get(AlgorithmProperty.IN_DATA).toString());
//			}
//		}
//
//		return new Pair<Converter, String>(chooseBestOutputConverter(outputConverters), null);
//	}

//	private Converter chooseBestOutputConverter(Converter[] outputConverters) {
//		if (outputConverters.length == 0) {
//			return null;
//		}
//
//		/* If we reached this point, that means there's no converter whose output can be converted
//		 * back. So, we'll take the first lossless output converter if we have one.
//		 */
//
//		for (Converter outputConverter : outputConverters) {
//			if (AlgorithmProperty.LOSSLESS.equals(outputConverter.calculateLossiness())) {
//				return outputConverter;
//			}
//		}
//
//		// There are no lossless output converters, so we'll just take the first one.
//
//		return outputConverters[0];
//	}

	// 3
//	Collection<String> getValidatedInputMimeTypesForReadingOutputMimeTypeBackIn(
//			String outputMimeType) {
//		try {
//			String fileExtension = FileUtilities.extractExtension(outputMimeType);
//			String format =
//				"(& (type=validator) (| (in_data=file-ext:%1$s) (also_validates=%1$s)))";
//			String validatorsQuery = String.format(format, fileExtension);
//			ServiceReference[] restoreValidators = this.bundleContext.getAllServiceReferences(
//				AlgorithmFactory.class.getName(), validatorsQuery);
//			
//			if (restoreValidators == null) {
//				return new HashSet<String>();
//			} else {
//				Collection<String> ValidatedInputMimeTypesForReadingOutputMimeTypeBackIn =
//					new HashSet<String>();
//
//				for (ServiceReference validator : restoreValidators) {
//					String validatorOutputMimeType =
//						validator.getProperty(AlgorithmProperty.OUT_DATA).toString();
//					ValidatedInputMimeTypesForReadingOutputMimeTypeBackIn.add(
//						validatorOutputMimeType);
//				}
//
//				System.err.println(
//					"\t\t\trestoreValidatorOutputMimeTypes: " +
//					ValidatedInputMimeTypesForReadingOutputMimeTypeBackIn);
//
//				return ValidatedInputMimeTypesForReadingOutputMimeTypeBackIn;
//			}
//		} catch (InvalidSyntaxException e) {
//			e.printStackTrace();
//
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

	// 3
//	private Collection<Converter> getInputConverters(
//			Collection<String> validatedInputMimeTypes, Data datum) {
//		Collection<Converter> inputConverters = new LinkedHashSet<Converter>();
//
//		for (String validatedInputMimeType : validatedInputMimeTypes) {
//			inputConverters.addAll(Arrays.asList(this.dataConverter.findConverters(
//				validatedInputMimeType, datum.getFormat())));
//		}
//
//		return inputConverters;
//	}

//	private Converter determineBestConverter(Data datum) {
//		System.err.println("Datum: " + datum + " (" + datum.getMetadata().get(DataProperty.LABEL) + ")");
//		Converter[] saveConverters = this.dataConverter.findConverters(datum, ANY_FILE_EXTENSION);
//
//		if (saveConverters.length == 0) {
//			/* TODO: Write a better log message detailing the specific data item and how there are
//			 * no converters for it.
//			 */
//			this.logger.log(
//				LogService.LOG_WARNING, "Error: Calculated an empty converter chain.");
//
//			return null;
//		} /* else if (converters.length == 1) {
//			Converter converter = converters[0];
//
//			if (!canConvertBackFrom(datum, converter)) {
//				String logMessage = "Datum can be saved out but not loaded back in.";
//				this.logger.log(LogService.LOG_WARNING, logMessage);
//			}
//
//			return converter;
//		} */ else {
//			Multimap<Converter, Converter> saveToRestoreConverters =
//				getSaveToRestoreConverters(saveConverters);
//			System.err.println("saveToRestoreConverters: " + saveToRestoreConverters);
//
////			if (saveToRestoreConverters.size() == 0) {
////				/* TODO: Write a real log message regarding why this particular data item is
////				 * problematic, including its name, etc.
////				 */
////				String logMessage = "Datum can be saved out but not loaded back in.";
////				this.logger.log(LogService.LOG_WARNING, logMessage);
////			}
//
//			return null;
//		}
//	}
//
//	private Multimap<Converter, Converter> getSaveToRestoreConverters(Converter[] saveConverters) {
//		Multimap<Converter, Converter> saveToRestoreConverters = LinkedHashMultimap.create();
//
//		for (Converter saveConverter : saveConverters) {
//			Dictionary properties = saveConverter.getProperties();
//			String startMimeType = (String) properties.get(AlgorithmProperty.OUT_DATA);
//			String endMimeType = (String) properties.get(AlgorithmProperty.IN_DATA);
//			Collection<String> restoreValidatorOutputMimeTypes =
//				getRestoreValidatorOutputMimeTypes(endMimeType, startMimeType);
//
//			Converter[] restoreConverters =
//				this.dataConverter.findConverters(startMimeType, endMimeType);
//			System.err.println(String.format(
//				"\tConverters for (%s, %s): %s",
//				startMimeType,
//				endMimeType,
//				Arrays.asList(restoreConverters)));
//
//			for (Converter restoreConverter : restoreConverters) {
//				saveToRestoreConverters.put(saveConverter, restoreConverter);
//				System.err.println("\tCurrent saveToRestoreConverters: " + saveToRestoreConverters);
//			}
//		}
//
//		return saveToRestoreConverters;
//	}
//
//	private Collection<String> getRestoreValidatorOutputMimeTypes(
//			String startMimeType, String endMimeType) {
//		try {
//			String fileExtension = FileUtilities.extractExtension(startMimeType);
//			String format =
//				"(& (type=validator) (| (in_data=file-ext:%1$s) (also_validates=%1$s)))";
//			String validatorsQuery = String.format(format, fileExtension);
//			ServiceReference[] restoreValidators = this.bundleContext.getAllServiceReferences(
//				AlgorithmFactory.class.getName(), validatorsQuery);
//			
//			if (restoreValidators == null) {
//				// TODO: Print some warning?
////				throw new FileLoadException(String.format(
////					"The file %s cannot be loaded as type %s.", file.getName(), mimeType));
//				return new HashSet<String>();
//			} else {
//				Collection<String> restoreValidatorOutputMimeTypes = new HashSet<String>();
//
//				for (ServiceReference validator : restoreValidators) {
//					String validatorOutputMimeType =
//						validator.getProperty(AlgorithmProperty.OUT_DATA).toString();
//					// TODO: Get converters from validatorOutputMimeType to endMimeType
//					restoreValidatorOutputMimeTypes.add(validatorOutputMimeType);
//				}
//
//				System.err.println("\t\t\trestoreValidatorOutputMimeTypes: " + restoreValidatorOutputMimeTypes);
//
//				return restoreValidatorOutputMimeTypes;
//			}
//		} catch (InvalidSyntaxException e) {
//			e.printStackTrace();
//
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
}