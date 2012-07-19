package edu.iu.sci2.visualization.scimaps.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

public class TextualMapsTest {
	private static final Joiner LINE_JOINER = Joiner.on("\n");
	private static final Joiner ENTRY_JOINER = Joiner.on("\t");
	private static final Splitter ENTRY_SPLITTER = Splitter.on("\t");
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testFailsIfInputSupplierIsNull() throws IOException {
		thrown.expect(NullPointerException.class);
		TextualMaps.buildMapFromLinesOfEntries(null, ENTRY_SPLITTER);
	}
	
	@Test
	public void testFailsIfKeyValueSplitterIsNull() throws IOException {
		thrown.expect(NullPointerException.class);
		TextualMaps.buildMapFromLinesOfEntries(supplierOf(""), null);
	}
	
	@Test(expected=IOException.class)
	public void testFailsIfInputSupplierThrowsIOException() throws IOException {
		@SuppressWarnings("unchecked") // Type never used, always throws
		InputSupplier<Reader> mockSupplier = EasyMock.createMock(InputSupplier.class);
		EasyMock.expect(mockSupplier.getInput()).andThrow(new IOException());
		EasyMock.replay(mockSupplier);
		
		TextualMaps.buildMapFromLinesOfEntries(mockSupplier, ENTRY_SPLITTER);
	}
	
	@Test
	public void testEmptyInputBuildsEmptyMap() throws IOException {
		assertEquals(
				ImmutableMap.of(),
				TextualMaps.buildMapFromLinesOfEntries(supplierOf(""), ENTRY_SPLITTER));
	}
	
	@Test
	public void testSplitToOneItemFails() throws IOException {
		thrown.expect(IllegalArgumentException.class);
		TextualMaps.buildMapFromLinesOfEntries(
				supplierOf("a"),
				ENTRY_SPLITTER);
	}
	
	@Test
	public void testSplitToThreeItemsFails() throws IOException {
		thrown.expect(IllegalArgumentException.class);
		TextualMaps.buildMapFromLinesOfEntries(
				supplierOf(ENTRY_JOINER.join(
						"k", "v", "?!")),
				ENTRY_SPLITTER);
	}
	
	@Test
	public void testFailsOnDuplicateKeys() throws IOException {
		thrown.expect(IllegalArgumentException.class);
		TextualMaps.buildMapFromLinesOfEntries(
				supplierOf(ENTRY_JOINER.join(
						"k", "v1",
						"k", "v2")),
				ENTRY_SPLITTER);
	}
	
	@Test
	public void testOneEntry() throws IOException {
		assertEquals(
				ImmutableMap.of("k", "v"),
				TextualMaps.buildMapFromLinesOfEntries(
						supplierOf(ENTRY_JOINER.join(
								"k", "v")),
						ENTRY_SPLITTER));
	}
	
	@Test
	public void testThreeEntries() throws IOException {
		assertEquals(
				ImmutableMap.of(
						"k1", "v1",
						"k2", "v2",
						"k3", "v3"),
				TextualMaps.buildMapFromLinesOfEntries(
						supplierOf(LINE_JOINER.join(
								ENTRY_JOINER.join("k1", "v1"),
								ENTRY_JOINER.join("k2", "v2"),
								ENTRY_JOINER.join("k3", "v3"))),
						ENTRY_SPLITTER));
	}
	
	private static InputSupplier<StringReader> supplierOf(String s) {
		return CharStreams.newReaderSupplier(s);
	}
}
