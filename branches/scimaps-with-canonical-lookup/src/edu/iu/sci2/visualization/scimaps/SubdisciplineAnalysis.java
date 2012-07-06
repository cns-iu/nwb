package edu.iu.sci2.visualization.scimaps;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

public class SubdisciplineAnalysis {
	private static final Schema TABLE_SCHEMA = AnalysisColumn.immutableSchema();
	
	private final ImmutableSortedMap<Subdiscipline, Float> subdisciplineTotals;
	private final float unmappedTotal; // TODO could be int (if changed carefully), is that more straightforward?
	
	private SubdisciplineAnalysis(SortedMap<Subdiscipline, Float> subdisciplineTotals,
			float unmappedTotal) {
		this.subdisciplineTotals = ImmutableSortedMap.copyOfSorted(subdisciplineTotals);
		this.unmappedTotal = unmappedTotal;
	}

	public static SubdisciplineAnalysis fromMapOfScience(MapOfScience mapOfScience) {
		ImmutableSortedMap.Builder<Subdiscipline, Float> builder = ImmutableSortedMap
				.naturalOrder();
		
		for (Entry<Integer, Float> entry : ImmutableSortedMap.copyOf(
				mapOfScience.getIdWeightMapping()).entrySet()) {
			builder.put(Subdiscipline.forID(entry.getKey()), entry.getValue());
		}
		
		// XXX This is going to break when the involved Set<Journal> is fixed..
		return new SubdisciplineAnalysis(builder.build(), mapOfScience.getUnmappedJournals().size());
	}
	
	public Table copyAsTable() {
		ImmutableSortedSet<Subdiscipline> allSubdisciplines = Subdisciplines.all();
		
		// Plus one for the "Unmapped" row
		Table table = TABLE_SCHEMA.instantiate(allSubdisciplines.size() + 1); 
		
		for (Subdiscipline subdiscipline : allSubdisciplines) {
			// Default to zero
			float total = subdisciplineTotals.containsKey(subdiscipline)
					? subdisciplineTotals.get(subdiscipline)
					: 0.0f;
			
			Tuple row = table.getTuple(table.addRow());
//			AnalysisColumn.ID.checkedSet(row, String.valueOf(subdiscipline.getId()));
//			AnalysisColumn.DESCRIPTION.checkedSet(row, subdiscipline.getDescription());
//			AnalysisColumn.TOTAL.checkedSet(row, total);
			AnalysisColumn.populateRow(row, String.valueOf(subdiscipline.getId()),
					subdiscipline.getDescription(), total);
		}
		
		Tuple unmappedRow = table.getTuple(table.addRow());
		AnalysisColumn.populateRow(unmappedRow, "None", "Unmapped", unmappedTotal); // TODO Shoulder review
		
		return table;
	}
	
	private static final class Subdisciplines {
		private Subdisciplines() {}
		
		static ImmutableSortedSet<Subdiscipline> all() {
			ImmutableSortedSet.Builder<Subdiscipline> builder = ImmutableSortedSet.naturalOrder();
			
			Set<Node> nodeSet = Nodes.getNodes();			
			for (Node node : nodeSet) {
				builder.add(Subdiscipline.forNode(node));
			}
			
			return builder.build();
		}
	}
	
	private static enum AnalysisColumn {
		// XXX Always update #populateRow when changing these elements or their types
		ID("Subdiscipline ID", String.class), // TODO Or Integer.class?
		DESCRIPTION("Description", String.class),
		TOTAL("Journal Occurrences Distributed", float.class); // TODO better column name? TODO float or double?
		
		private final String name;
		private final Class<?> clazz;

		private AnalysisColumn(String name, Class<?> clazz) {
			this.name = name;
			this.clazz = clazz;
		}
		
		static Schema immutableSchema() {
			EnumSet<AnalysisColumn> analysisColumns = EnumSet.allOf(AnalysisColumn.class);
			
			Schema s = new Schema(analysisColumns.size());
			
			for (AnalysisColumn analysisColumn : analysisColumns) {
				s.addColumn(analysisColumn.name, analysisColumn.clazz);
			}
						
			return s.lockSchema();
		}
		
		static void populateRow(Tuple row, String id, String description, float total) {
			ID.checkedSet(row, id);
			DESCRIPTION.checkedSet(row, description);
			TOTAL.checkedSet(row, total);
//			checkedSetString(row, ID.name, id);
//			checkedSetString(row, DESCRIPTION.name, description);
//			checkedSetFloat( row, TOTAL.name, total);
		}
		
		private <T> void checkedSet(Tuple tuple, T value) {
			Preconditions.checkState(
					tuple.canSet(name, clazz),
					"Cannot set TODO value %s in column %s of tuple %s.", value, name, tuple);
			tuple.set(name, value);
		}
		
//		private static void checkedSetString(Tuple tuple, String columnName, String value) {
//			Preconditions.checkState(
//					tuple.canSetString(columnName),
//					"Cannot set String value %s in column %s of tuple %s.", value, columnName, tuple);
//			tuple.setString(columnName, value);
//		}
//
//		private static void checkedSetFloat(Tuple tuple, String columnName, float value) {
//			Preconditions.checkState(
//					tuple.canSetFloat(columnName),
//					"Cannot set float value %s in column %s of tuple %s.", value, columnName, tuple);
//			tuple.setFloat(columnName, value);
//		}
	}
	
	/**
	 * Lightweight version of {@link Node} using its {@link Node#getId() ID} to determine identity
	 * and comparison.
	 */
	// TODO Add identity and comparison to Node, then delete this.
	private static final class Subdiscipline implements Comparable<Subdiscipline> {
		private final Node node; // TODO or id, description?

		private Subdiscipline(Node node) {
			this.node = node;
		}
		
		private static Subdiscipline forNode(Node node) {
			return new Subdiscipline(node);
		}
		
		private static Subdiscipline forID(int id) {
			Node node = Nodes.getNodeByID(id);
			Preconditions.checkArgument(node != null, "No subdiscipline with ID %s.", id);
			
			return Subdiscipline.forNode(node);
		}

		public int getId() {
			return node.getId();
		}

		public String getDescription() {
			return node.getName();
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("node", node)
					.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(node.getId()); // TODO ?
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

			return Objects.equal(this.node.getId(), that.node.getId()); // TODO ?
		}

		@Override
		public int compareTo(Subdiscipline that) {
			return Integer.valueOf(this.node.getId()).compareTo(that.node.getId());
		}
	}
}
