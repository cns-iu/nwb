package edu.iu.sci2.visualization.scimaps.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;

/**
 * Utilities for interpreting textual lines of entries as a serialization of a {@link Map}.
 */
public final class MapResources {
	private MapResources() {}
	
	/**
	 * Builds the map by represented by textual lines of entries from an {@link InputSupplier}. The
	 * key and value of each entry line is interpreted using the given {@link Splitter}.
	 * 
	 * @param inputSupplier
	 *            A {@link Readable} and {@link Closeable} supplier of input to be interpreted as a
	 *            {@link Map}, one key-value entry per line
	 * @param keyValueSplitter
	 *            A {@link Splitter} whose first and second iterates of each line parsed will be
	 *            interpreted as the entry's key and the value, respectively
	 * @throws NullPointerException
	 *             If any parameter is null
	 * @throws IOException
	 *             If the input cannot be read
	 * @throws IllegalArgumentException
	 *             If the entries define an invalid {@link Map}, having for example duplicate keys
	 */
	public static <R extends Readable & Closeable> ImmutableMap<String, String> buildMapFromLinesOfEntries(
			InputSupplier<R> inputSupplier, Splitter keyValueSplitter) throws IOException {
		Preconditions.checkNotNull(inputSupplier);
		Preconditions.checkNotNull(keyValueSplitter);
		
		return CharStreams.readLines(inputSupplier, new MapEntryLineProcessor(keyValueSplitter));
	}

	
	private static final class MapEntryLineProcessor implements
			LineProcessor<ImmutableMap<String, String>> {
		private final Splitter keyValueSplitter;
		private final ImmutableMap.Builder<String, String> builder;
		
		private MapEntryLineProcessor(Splitter keyValueSplitter) {
			this.keyValueSplitter = Preconditions.checkNotNull(keyValueSplitter);
			
			this.builder = ImmutableMap.builder();
		}

		@Override
		public boolean processLine(String line) throws IOException {
			Iterable<String> tokens = keyValueSplitter.split(line);
			
			Preconditions.checkArgument(
					Iterables.size(tokens) == 2,
					"Line \"%s\" contains %s tokens (%s) but exactly two were expected.",
					line,
					Iterables.size(tokens),
					Iterables.toString(tokens));			
			String key = Iterables.get(tokens, 0);
			String value = Iterables.get(tokens, 1);
			
			builder.put(key, value);
			
			return true; // Never halt processing early
		}

		private ImmutableMap<String, String> buildMap() {
			return builder.build();
		}

		@Override
		public ImmutableMap<String, String> getResult() {
			ImmutableMap<String, String> map = buildMap();
			
			return map;
		}
	}
}
