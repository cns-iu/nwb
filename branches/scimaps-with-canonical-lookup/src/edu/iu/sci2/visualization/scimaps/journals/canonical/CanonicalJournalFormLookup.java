package edu.iu.sci2.visualization.scimaps.journals.canonical;

import java.io.IOException;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import edu.iu.sci2.visualization.scimaps.util.MapResources;

/**
 * A lookup table for the "canonical" form of a journal identifier.
 * 
 * <p>
 * For example the canonical form for both <em>00219258</em> (an <a
 * href="http://en.wikipedia.org/wiki/International_Standard_Serial_Number">ISSN</a>) and
 * <em>j biol chem</em> (a common abbreviation) is <em>journal of biological chemistry</em>.
 */
public class CanonicalJournalFormLookup {
	private final ImmutableMap<String, String> lookup;
	
	private CanonicalJournalFormLookup(Map<String, String> lookup) {
		this.lookup = ImmutableMap.copyOf(lookup);
	}
	
	/**
	 * @return An instance of the canonical journal form lookup
	 * @throws CanonicalJournalFormLookupException
	 *             If the lookup is not available
	 */
	public static CanonicalJournalFormLookup get() throws CanonicalJournalFormLookupException {
		/* XXX We don't expect this backing map to change, so the result of the first load could
		 * be memoized if needed. */
		
		try {
			return new CanonicalJournalFormLookup(MapResources.buildMapFromLinesOfEntries(
					Resources.newReaderSupplier(
							Resources.getResource(CanonicalJournalFormLookup.class, "canonical.tsv"),
							Charsets.UTF_8),
					Splitter.on('\t').trimResults()));
		} catch (IOException e) { // TODO This could be non-fatal, just log a warning, what do you think?
			throw new CanonicalJournalFormLookupException(
					"Failed to load the canonical journal lookup file.", e);
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
	public String lookup(String journal) {
		Preconditions.checkNotNull(journal);

		String journalLowerCase = journal.toLowerCase();
		
		return lookup.containsKey(journalLowerCase) ? lookup.get(journalLowerCase) : journal;
	}


	public static void main(String[] args) {
//		CanonicalJournalFormLookup lookup = null;
//		try {
//			lookup = CanonicalJournalFormLookup.get();
//			System.out.println(lookup.lookup("j biol chem"));
//		} catch (CanonicalJournalFormLookupException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(Iterables.toString(lookup.entrySet().asList().subList(0, 4)));
	}

	
	public static class CanonicalJournalFormLookupException extends Exception {
		private static final long serialVersionUID = -4228304968406767171L;

		private CanonicalJournalFormLookupException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
