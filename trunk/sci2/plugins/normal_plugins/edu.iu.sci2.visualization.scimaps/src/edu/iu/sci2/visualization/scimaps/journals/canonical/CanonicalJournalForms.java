package edu.iu.sci2.visualization.scimaps.journals.canonical;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import edu.iu.sci2.visualization.scimaps.util.TextualMaps;


/**
 * A lookup table for the "canonical" form of a journal identifier.
 * 
 * <p>
 * For example the canonical form for both <em>00219258</em> (an <a
 * href="http://en.wikipedia.org/wiki/International_Standard_Serial_Number">ISSN</a>) and
 * <em>j biol chem</em> (a common abbreviation) is <em>journal of biological chemistry</em>.
 */
public final class CanonicalJournalForms {
	private CanonicalJournalForms() {}
	
	private static final ImmutableMap<String, String> CANONICAL = buildMap();	
	private static ImmutableMap<String, String> buildMap() {
		try {
			return TextualMaps.buildMapFromLinesOfEntries(
					Resources.newReaderSupplier(
							Resources.getResource(CanonicalJournalForms.class, "canonical.tsv"),
							Charsets.UTF_8), // TODO test this
					Splitter.on('\t').trimResults());
		} catch (IOException e) {
			throw new RuntimeException("Failed to load canonical journal form lookup table.", e);
		}
	}
	
	/**
	 * Looks up a canonical form for this journal identifier.
	 * 
	 * @param journal
	 *            A journal identifier, such as a name (possibly abbreviated) or an ISSN
	 * @return The canonical form, if one is defined, and the input unmodified otherwise
	 * @throws NullPointerException
	 *             if {@code journal} is null
	 */
	public static String lookup(String journal) {
		Preconditions.checkNotNull(journal);

		String normalized = journal.trim().toLowerCase();
		
		return CANONICAL.containsKey(normalized) ? CANONICAL.get(normalized) : journal;
	}
}
