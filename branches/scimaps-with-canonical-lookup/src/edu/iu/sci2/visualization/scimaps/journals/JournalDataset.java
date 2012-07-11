package edu.iu.sci2.visualization.scimaps.journals;

import java.util.Iterator;
import java.util.Map;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

import edu.iu.sci2.visualization.scimaps.journals.canonical.CanonicalJournalForms;

/**
 * Keeps track of a set of journals, and how many times each has occurred.
 */
// TODO Replace oim.vivo.scimapcore.journal.Journal with something more like JournalDataset.Journal
public final class JournalDataset {
	private final ImmutableMultiset<Journal> journals;
	
	private JournalDataset(Multiset<Journal> journals) {
		this.journals = ImmutableMultiset.copyOf(journals);
	}

	/**
	 * Constructs a JournalDataset from a {@link Table} by reading the values in
	 * {@code journalColumnName}. Counts of unreadable, missing, and loaded values (both total and
	 * distinct) are sent to the provided {@link LogService}.
	 * 
	 * @param table
	 *            A table having some String-valued column of journal identifiers
	 * @param journalColumnName
	 *            The name of the column containing journal identifiers
	 * @param logger
	 *            A {@link LogService} for reporting results
	 * @throws NullPointerException
	 *             if any parameter is null
	 */
	public static JournalDataset fromTable(Table table, String journalColumnName, LogService logger) {
		Preconditions.checkNotNull(table);
		Preconditions.checkNotNull(journalColumnName);
		Preconditions.checkNotNull(logger);
		
		ImmutableMultiset.Builder<Journal> builder = ImmutableMultiset.builder();

		int unreadableCount = 0;
		int nullCount = 0;
		
		for (@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
				Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
			Tuple row = rows.next();

			if (!row.canGetString(journalColumnName)) {
				unreadableCount++;
				continue;				
			}
			
			String journalName = row.getString(journalColumnName);
				
			if (journalName == null) {
				nullCount++;
				continue;
			}
				
			builder.add(Journal.forIdentifier(journalName.trim()));
		}

		if (unreadableCount > 0) { 
			logger.log(LogService.LOG_WARNING, String.format(
					"Skipped %d rows with unreadable journal identifiers.", nullCount));
		}
		
		if (nullCount > 0) {
			logger.log(LogService.LOG_WARNING,
					String.format("Skipped %d rows with missing journal identifiers.", nullCount));
		}
		
		ImmutableMultiset<Journal> journals = builder.build();
		
		logger.log(LogService.LOG_INFO, String.format(
				"Loaded %d occurrences of %d distinct journals.", journals.size(), journals
						.elementSet().size()));
		
		return new JournalDataset(journals);
	}

	/**
	 * Whether this journal dataset is empty.
	 */
	public boolean isEmpty() {
		return journals.isEmpty();
	}
	
	/**
	 * The number of occurrences of a journal in this dataset, possibly zero but never negative.
	 */
	public int count(Journal journal) {
		return journals.count(journal);
	}

	/* TODO This is a bridge method for interacting with existing code that expects the dataset
	 * delivered as a Map<String, Integer>. Ultimately the receiving code should be rewritten to
	 * accept a JournalDataset and this method should be deleted. */
	/**
	 * A {@link Map} from each distinct journal identifier occurring in this dataset to the number
	 * of times it occurs.
	 */
	ImmutableMap<String, Integer> copyAsIdentifierCountMap() {
		ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
		
		for (Entry<Journal> entry : journals.entrySet()) {
			builder.put(entry.getElement().getIdentifier(), entry.getCount());
		}
		
		return builder.build();
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("journals", journals).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(journals);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof JournalDataset)) {
			return false;
		}
		JournalDataset that = (JournalDataset) o;
		
		return Objects.equal(this.journals, that.journals);
	}

	/* XXX In the future this may:
	 * - Reject unknown identifiers?
	 * - Provide the "pretty" identifier when available
	 */
	/**
	 * A journal.
	 */
	public static final class Journal {
		private final String identifier;

		private Journal(String identifier) {
			this.identifier = identifier;
		}
		
		/**
		 * A Journal instance representing this String {@code identifier}.
		 * 
		 * @param identifier
		 *            A string identifying some journal
		 * @throws NullPointerException
		 *             if {@code identifier} is null
		 */
		public static Journal forIdentifier(String identifier) {
			Preconditions.checkNotNull(identifier);
			
			String preparedIdentifier =
					JournalsMapAlgorithm.RESOLVE_JOURNALS_TO_CANONICAL_NAME
					? CanonicalJournalForms.lookup(identifier)
					: identifier;
			
			return new Journal(preparedIdentifier);
		}
		
		/**
		 * The identifier of this journal.
		 */
		public String getIdentifier() {
			return identifier;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("identifier", identifier)
					.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(identifier);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null) {
				return false;
			}
			if (!(o instanceof Journal)) {
				return false;
			}
			Journal that = (Journal) o;

			return Objects.equal(this.identifier, that.identifier);
		}
	}
}
