package edu.iu.sci2.visualization.scimaps.analysis;

import java.util.Set;
import java.util.SortedMap;

import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

import edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.Subdiscipline;
import edu.iu.sci2.visualization.scimaps.analysis.table.Column;
import edu.iu.sci2.visualization.scimaps.analysis.table.Row;

/**
 * A summary of the subdisciplines in a map of science according to how often each was mapped into.
 */
public class SubdisciplineAnalysis extends AbstractTabularAnalysis<Subdiscipline> {
	static final Column<Integer> ID = Column.create(Integer.class, "Subdiscipline ID");
	static final Column<String> DESCRIPTION = Column.create(String.class, "Description");
	static final Column<Float> TOTAL = Column.create(Float.class, "Journal Occurrences Distributed");
	
	private final ImmutableSortedMap<Subdiscipline, Float> subdisciplineTotals;
	private final int unmappedTotal;
	
	/**
	 * @param subdisciplineTotals
	 *            The distribution of journal occurrences into each subdiscipline
	 * @param unmappedTotal
	 *            The number of journal occurrences not matched to any subdiscipline
	 */
	public SubdisciplineAnalysis(SortedMap<Subdiscipline, Float> subdisciplineTotals,
			int unmappedTotal) {
		super(ImmutableSet.of(ID, DESCRIPTION, TOTAL));
		Preconditions.checkNotNull(subdisciplineTotals);
		
		this.subdisciplineTotals = ImmutableSortedMap.copyOfSorted(subdisciplineTotals);
		this.unmappedTotal = unmappedTotal;
	}
	
	@Override
	protected Iterable<Subdiscipline> getElements() {
		ImmutableSortedSet.Builder<Subdiscipline> builder = ImmutableSortedSet.naturalOrder();
		
		@SuppressWarnings("unchecked")
		Set<Node> nodeSet = Nodes.getNodes();
		
		for (Node node : nodeSet) {
			builder.add(Subdiscipline.forNode(node));
		}
		
		return builder.build();
	}

	@Override
	protected Row createRowFor(Subdiscipline subdiscipline) {
		return new Row()
					.put(ID, subdiscipline.getId())
					.put(DESCRIPTION, subdiscipline.getName())
					.put(TOTAL, getTotalOrZero(subdiscipline));
	}

	private float getTotalOrZero(Subdiscipline subdiscipline) {
		return subdisciplineTotals.containsKey(subdiscipline)
				? subdisciplineTotals.get(subdiscipline)
				: 0.0f;
	}
	
	@Override
	protected Iterable<Row> createAdditionalRows() {
		// Special "Unmapped" row
		Row unmappedRow = new Row()
								.put(ID, null)
								.put(DESCRIPTION, "Unmapped")
								.put(TOTAL, (float) unmappedTotal);

		return ImmutableList.of(unmappedRow);
	}

	/**
	 * Thin wrapper for {@link Node} that uses its {@link Node#getId() ID} to decide identity and
	 * comparison.
	 */
	// TODO Add identity and comparison to Node, then delete this.
	public static class Subdiscipline implements Comparable<Subdiscipline> {
		private final Node node;

		private Subdiscipline(Node node) {
			this.node = Preconditions.checkNotNull(node);
		}
		
		private static Subdiscipline forNode(Node node) {
			Preconditions.checkNotNull(node);
			
			return new Subdiscipline(node);
		}
		
		public static Subdiscipline forID(int id) {
			Node node = Nodes.getNodeByID(id);
			Preconditions.checkArgument(node != null, "No subdiscipline with ID %s.", id);
			
			return Subdiscipline.forNode(node);
		}

		public int getId() {
			return node.getId();
		}

		public String getName() {
			return node.getName();
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("node", node).toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(node.getId());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null) {
				return false;
			}
			if (!(o instanceof Subdiscipline)) {
				return false;
			}
			Subdiscipline that = (Subdiscipline) o;

			return Objects.equal(this.node.getId(), that.node.getId());
		}

		@Override
		public int compareTo(Subdiscipline that) {
			return Integer.valueOf(this.node.getId()).compareTo(that.node.getId());
		}
	}
}
