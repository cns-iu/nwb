package edu.iu.sci2.visualization.scimaps.journals;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

import edu.iu.sci2.visualization.scimaps.journals.canonical.CanonicalJournalForms;

/**
 * Keeps track of a set of journals, and how many times each has occurred.
 */
public final class JournalDataset {
	private final ImmutableMultiset<JournalDataset.Journal> journals;
	
	private JournalDataset(Multiset<JournalDataset.Journal> journals) {
		this.journals = ImmutableMultiset.copyOf(journals);
	}
	
	/**
	 * Constructs a JournalDataset for a multiset of {@link JournalDataset.Journal journals}.
	 * 
	 * @param journals
	 *            A multiset of journals
	 * @throws NullPointerException
	 *             if {@code journals} is null
	 */
	public static JournalDataset forJournals(Multiset<JournalDataset.Journal> journals) {
		Preconditions.checkNotNull(journals);
		
		return new JournalDataset(journals);
	}
	
	/**
	 * Constructs a dataset using all journal names from a String-valued column
	 * {@code journalColumnName} of {@code table}. A counts of missing values (if any) can be sent
	 * to a provided {@link LogService}.
	 * 
	 * @param table
	 *            A table having some String-valued column of journal identifiers
	 * @param journalColumnName
	 *            The name of the column containing journal identifiers
	 * @param logger
	 *            A {@link LogService} for reporting results, or null to suppress warnings
	 * 
	 * @throws NullPointerException
	 *             if {@code table} is null or {@code journalColumnName} is null
	 * @throws IllegalArgumentException
	 *             if the table cannot serve String values from column {@code journalColumnName}
	 */
	public static JournalDataset fromTable(Table table, String journalColumnName,
			LogService logger) {
		Preconditions.checkNotNull(table);
		Preconditions.checkNotNull(journalColumnName);
		Preconditions.checkArgument(
				table.canGetString(journalColumnName),
				"Cannot get String values from column %s of table %s.", journalColumnName, table);
		
		TableReader tableReader = new TableReader(table, journalColumnName);
		
		if (logger != null) {
			tableReader.logWarningsTo(logger);
		}
		
		return JournalDataset.forJournals(tableReader.buildJournals());
	}
	
	/**
	 * @see JournalDataset#fromTable(TupleSet, String, LogService)
	 */
	private static class TableReader {
		private final TupleSet table;
		private final String journalColumnName;
		
		private final ImmutableMultiset.Builder<JournalDataset.Journal> builder;
		
		private int nullCount;
		
		/**
		 * @see JournalDataset#fromTable(TupleSet, String, LogService)
		 */
		TableReader(TupleSet tupleSet, String journalColumnName) {
			this.table = tupleSet;
			this.journalColumnName = journalColumnName;
			
			this.builder = ImmutableMultiset.builder();
			this.nullCount = 0;
			
			readTable();
		}
		
		void logWarningsTo(LogService logger) {
			if (nullCount > 0) {
				logger.log(LogService.LOG_WARNING, String.format(
						"Skipped %d rows with missing journal identifiers.", nullCount));
			}
		}
		
		ImmutableMultiset<JournalDataset.Journal> buildJournals() {
			return builder.build();
		}

		private void readTable() {
			for (@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
					Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
				readRow(rows.next());
			}
		}

		private void readRow(Tuple row) {
			assert (row.canGetString(journalColumnName)) : "TableReader can only be constructed " +
					"via JournalDataset.fromTable, which fails if it cannot get Strings from " +
					"this column of the table as a whole.";

			String journalName = row.getString(journalColumnName);
				
			if (journalName == null) {
				nullCount++;
				return;
			}
				
			builder.add(JournalDataset.Journal.forName(journalName.trim()));
		}
	}

	/**
	 * Whether this journal dataset is empty.
	 */
	public boolean isEmpty() {
		return journals.isEmpty();
	}
	
	/**
	 * The total number of occurrences across all journals in this dataset.
	 */
	public int size() {
		return journals.size();
	}

	/**
	 * The number of occurrences of a journal in this dataset, possibly zero but never negative.
	 */
	public int count(JournalDataset.Journal journal) {
		return journals.count(journal);
	}
	
	/**
	 * The sum of the number of occurrences in the dataset of all the journals in {@code journals}.
	 * 
	 * @see #count(Journal)
	 * @throws NullPointerException
	 *             if {@code journals} is null
	 */
	public int totalCount(Set<JournalDataset.Journal> journals) {
		Preconditions.checkNotNull(journals);
		
		int totalCount = 0;
		
		for (JournalDataset.Journal journal : journals) {
			totalCount += count(journal);
		}
		
		return totalCount;
	}
	
	public ImmutableSet<JournalDataset.Journal> distinctJournals() {
		return ImmutableSet.copyOf(journals.elementSet());
	}
	

	/* TODO This is a bridge method for interacting with existing code that expects the dataset
	 * delivered as a Map<String, Integer>. Ultimately the receiving code should be rewritten to
	 * accept a JournalDataset and this method should be deleted. */
	/**
	 * A {@link Map} from each distinct journal identifier occurring in this dataset to the number
	 * of times it occurs.
	 */
	public ImmutableMap<String, Integer> copyAsIdentifierCountMap() {
		ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
		
		for (Entry<JournalDataset.Journal> entry : journals.entrySet()) {
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
	 * A journal whose identity is decided by its identifier.
	 */
	// TODO Replace oim.vivo.scimapcore.journal.Journal with something more like this
	public static final class Journal {
		private final String identifier;

		private Journal(String identifier) {
			this.identifier = identifier;
		}

		/**
		 * An instance representing the journal identified by this name. The journal name will be
		 * trimmed and lowercased before use. One or more other forms of normalization appropriate
		 * for equating journal identifiers may also be applied. The number or nature of these
		 * other normalizations is not specified.
		 * 
		 * @param name
		 *            A string identifying some journal
		 * @throws NullPointerException
		 *             if {@code identifier} is null
		 */
		public static JournalDataset.Journal forName(String name) {
			Preconditions.checkNotNull(name);
			
			String normalized = name.trim().toLowerCase();
			
			normalized =
					JournalsMapAlgorithm.RESOLVE_JOURNALS_TO_CANONICAL_NAME
					? CanonicalJournalForms.lookup(normalized)
					: normalized;
			
			return new JournalDataset.Journal(normalized);
		}
		
		public static JournalDataset.Journal forVivoCoreJournal(
				oim.vivo.scimapcore.journal.Journal vivoCoreJournal) {
			return JournalDataset.Journal.forName(vivoCoreJournal.getJournalName());
		}

		// TODO Make this an instance method on oim.vivo.scimapcore.journal.Journal?
		public static ImmutableSet<JournalDataset.Journal> forVivoCoreJournals(
				Iterable<? extends oim.vivo.scimapcore.journal.Journal> vivoCoreJournals) {
			ImmutableSet.Builder<JournalDataset.Journal> datasetJournals = ImmutableSet.builder();
			
			for (oim.vivo.scimapcore.journal.Journal vivoCoreJournal : vivoCoreJournals) {
				datasetJournals.add(JournalDataset.Journal.forVivoCoreJournal(vivoCoreJournal));
			}
			
			return datasetJournals.build();
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
			if (!(o instanceof JournalDataset.Journal)) {
				return false;
			}
			JournalDataset.Journal that = (JournalDataset.Journal) o;

			return Objects.equal(this.identifier, that.identifier);
		}
	}
}
