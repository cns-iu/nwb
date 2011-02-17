package edu.iu.cns.persistence.session.save;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.Converter;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.utilities.Pair;
import org.osgi.service.log.LogService;

public class SessionDataConverter {
	public static final String FILE_EXTENSION = "file-ext:";
	public static final String FILE = "file:";

	public static final Map<Class<?>, String> OBJECT_FORMAT_TO_FILE_FORMAT =
		mapObjectFormatToFileFormat();

	private static Map<Class<?>, String> mapObjectFormatToFileFormat() {
		Map<Class<?>, String> objectFormatToFileFormat = new HashMap<Class<?>, String>();
		objectFormatToFileFormat.put(java.awt.image.BufferedImage.class, "file:text/jpg");
		objectFormatToFileFormat.put(
			edu.berkeley.guir.prefuse.graph.Graph.class, "file:text/xgmml+xml");
		objectFormatToFileFormat.put(edu.uci.ics.jung.graph.Graph.class, "file:text/xgmml+xml");
		objectFormatToFileFormat.put(prefuse.data.Graph.class, "file:text/graphml+xml");
		objectFormatToFileFormat.put(prefuse.data.Tree.class, "file:text/treeml+xml");
		objectFormatToFileFormat.put(prefuse.data.Table.class, "file:text/csv");

		return Collections.unmodifiableMap(objectFormatToFileFormat);
	}

	private Map<Data, Pair<Data, String>> inputToOutputData =
		new LinkedHashMap<Data, Pair<Data, String>>();
	private Map<Data, Integer> outputDataToIndex = new LinkedHashMap<Data, Integer>();

	public SessionDataConverter(
			LogService logger, DataConversionService dataConverter, Data[] allData) {
		int outputDatumIndex = 0;

		for (Data datum : allData) {
			// REMINDER: Converters should only ever output one Data.
			try {
				if (isAlreadyReadyForOutput(datum)) {
					this.inputToOutputData.put(
						datum, new Pair<Data, String>(datum, datum.getFormat()));
					this.outputDataToIndex.put(datum, outputDatumIndex++);
				} else {
					Pair<Converter, String> converterAndTargetMimeType =
						chooseBestConverter(dataConverter, datum);

					if (converterAndTargetMimeType != null) {
						Converter converter = converterAndTargetMimeType.getFirstObject();
						String targetMimeType = converterAndTargetMimeType.getSecondObject();
						Data convertedDatum = converter.convert(datum);
						this.inputToOutputData.put(
							datum, new Pair<Data, String>(convertedDatum, targetMimeType));
						this.outputDataToIndex.put(convertedDatum, outputDatumIndex++);
					} else {
						String format =
							"Error: Could not save data '%s' in session. " +
							"(It could not be converted.)";
						String logMessage =
							String.format(format, datum.getMetadata().get(DataProperty.LABEL));
						logger.log(LogService.LOG_ERROR, logMessage);
					}
				}
			} catch (ConversionException e) {
				logger.log(LogService.LOG_WARNING, e.getMessage(), e);
			}
		}
	}

	public Map<Data, Pair<Data, String>> getInputToOutputData() {
		return this.inputToOutputData;
	}

	public Map<Data, Integer> getOutputDataToIndex() {
		return this.outputDataToIndex;
	}

	// 1
	private boolean isAlreadyReadyForOutput(Data datum) {
		String format = datum.getFormat();

		if (format.startsWith(FILE_EXTENSION) || format.startsWith(FILE)) {
			return true;
		} else {
			return false;
		}
	}

	// 1
	private Pair<Converter, String> chooseBestConverter(
			DataConversionService dataConverter, Data datum) {
		Class<?> datumClazz = datum.getData().getClass();
		Pair<Class<?>, String> actualSourceTypeWithTargetMimeType = findInMemoryFormat(datumClazz);

		if (actualSourceTypeWithTargetMimeType != null) {
			String targetMimeType = actualSourceTypeWithTargetMimeType.getSecondObject();
			Converter converter = dataConverter.findConverters(datum, targetMimeType)[0];

			return new Pair<Converter, String>(converter, targetMimeType);
		}

		return null;
	}

	// 2
	private Pair<Class<?>, String> findInMemoryFormat(Class<?> objectClazz) {
		for (Class<?> clazz : OBJECT_FORMAT_TO_FILE_FORMAT.keySet()) {
			if (clazz.isAssignableFrom(objectClazz)) {
				return new Pair<Class<?>, String>(clazz, OBJECT_FORMAT_TO_FILE_FORMAT.get(clazz));
			}
		}

		return null;
	}
}