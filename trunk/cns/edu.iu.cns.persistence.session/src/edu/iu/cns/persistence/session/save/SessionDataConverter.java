package edu.iu.cns.persistence.session.save;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.Converter;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.utilities.Pair;
import org.osgi.service.log.LogService;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.iu.cns.persistence.session.common.Utilities;
import edu.iu.cns.persistence.session.save.exception.NoCanonicalFormatException;

public class SessionDataConverter {
	public static final Map<Class<?>, String> CLASS_TO_CANONICAL_FORMAT =
		mapClassToCanonicalFormat();

	/* TODO If you ever modify this, check whether any of these (the key classes) are assignable
	 * from the other.
	 * Always put more specific ones higher up.
	 */
	private static Map<Class<?>, String> mapClassToCanonicalFormat() {
		Map<Class<?>, String> classToCanonicalFormat = new LinkedHashMap<Class<?>, String>();
		classToCanonicalFormat.put(java.awt.image.BufferedImage.class, "file:text/jpg");
		/* If you examine the converter graph in GUESS, you'll see that XGMML is the only file
		 * format directly connected to edu.berkeley.guir.prefuse.graph.Graph.
		 * For the only other (conversion) direction, there are at least two steps in between
		 * edu.berkeley.guir.prefuse.graph.Graph and the final file format.
		 */ 
		classToCanonicalFormat.put(
			edu.berkeley.guir.prefuse.graph.Graph.class, "file:text/xgmml+xml");
		classToCanonicalFormat.put(edu.uci.ics.jung.graph.Graph.class, "file:text/xgmml+xml");
		classToCanonicalFormat.put(prefuse.data.Tree.class, "file:text/treeml+xml");
		classToCanonicalFormat.put(prefuse.data.Graph.class, "file:text/graphml+xml");
		classToCanonicalFormat.put(prefuse.data.Table.class, "file:text/csv");

		return Collections.unmodifiableMap(classToCanonicalFormat);
	}

	private Map<Data, Pair<Data, String>> inputToOutputData =
		new LinkedHashMap<Data, Pair<Data, String>>();
	private Map<Data, Integer> outputDataToIndex = new LinkedHashMap<Data, Integer>();
	private BiMap<Data, Data> originalToConvertedData = HashBiMap.create();

	public SessionDataConverter(
			LogService logger, DataConversionService dataConversionService, Data[] allData) {
		int outputDatumIndex = 0;

		for (Data datum : allData) {		
			// REMINDER: Converters should only ever output one Data.
			try {
				if (isAlreadyReadyForOutput(datum)) {
					this.inputToOutputData.put(
						datum, new Pair<Data, String>(datum, datum.getFormat()));
					this.outputDataToIndex.put(datum, outputDatumIndex++);
					this.originalToConvertedData.put(datum, datum);
				} else {
					Pair<Converter, String> converterAndToolFormat =
						findConverterToCanonicalFormat(dataConversionService, datum);

					if (converterAndToolFormat != null) {
						Converter converter = converterAndToolFormat.getFirstObject();
						String toolFormat = converterAndToolFormat.getSecondObject();
						Data convertedDatum = converter.convert(datum);
						
						this.inputToOutputData.put(
							datum, new Pair<Data, String>(convertedDatum, toolFormat));
						this.outputDataToIndex.put(convertedDatum, outputDatumIndex++);
						this.originalToConvertedData.put(datum, convertedDatum);
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

	public BiMap<Data, Data> getOriginalToConvertedData() {
		return this.originalToConvertedData;
	}

	// 1
	private boolean isAlreadyReadyForOutput(Data datum) {
		String format = datum.getFormat();

		return (format.startsWith(Utilities.FILE_EXTENSION) || format.startsWith(Utilities.FILE));
	}

	// 1
	private Pair<Converter, String> findConverterToCanonicalFormat(
			DataConversionService dataConversionService, Data datum) {
		Class<?> datumClazz = datum.getData().getClass();

		try {
			String toolFormat = findCanonicalFormat(datumClazz);
			Converter converter = dataConversionService.findConverters(datum, toolFormat)[0];

			return new Pair<Converter, String>(converter, toolFormat);
		} catch (NoCanonicalFormatException e) {
			return null;
		}
	}

	// 2
	private String findCanonicalFormat(Class<?> objectClazz) throws NoCanonicalFormatException {
		for (Class<?> clazz : CLASS_TO_CANONICAL_FORMAT.keySet()) {
			if (clazz.isAssignableFrom(objectClazz)) {
				return CLASS_TO_CANONICAL_FORMAT.get(clazz);
			}
		}

		throw new NoCanonicalFormatException(String.format(
			"No canonical format could be found for %s", objectClazz.getName()));
	}
}