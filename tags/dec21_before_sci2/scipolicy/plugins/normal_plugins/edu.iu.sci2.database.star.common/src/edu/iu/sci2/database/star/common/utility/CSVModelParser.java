package edu.iu.sci2.database.star.common.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.utilities.ProgressMonitorUtilities;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.sci2.database.star.common.entity.CoreEntity;
import edu.iu.sci2.database.star.common.entity.CoreEntityManager;
import edu.iu.sci2.database.star.common.entity.LeafEntityManager;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class CSVModelParser {
	public static DatabaseModel parse(
			String coreEntityDisplayName,
			String coreEntityTableName,
			String[] coreColumns,
			String[] nonCoreColumns,
			CSVReader reader,
			Map<String, ColumnDescriptor> columnDescriptors,
			ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, IOException {
		CoreEntityManager coreEntityManager = createCoreEntityTable(
			coreEntityDisplayName, coreEntityTableName, coreColumns, columnDescriptors);
		Map<String, LeafEntityManager> leafEntityManagers =
			createEntityTables(coreEntityManager, nonCoreColumns, columnDescriptors);

		String[] row;
		int last = 0;
		int total = 0;
		double unitsWorked = 0;

		while ((row = reader.readNext()) != null) {
			ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

			// Core entity.

			CoreEntity coreEntity = coreEntityManager.addEntity(row);

			// Leaf entities.

			for (String leafEntityName : nonCoreColumns) {
				ColumnDescriptor column = columnDescriptors.get(leafEntityName);
				leafEntityManagers.get(column.getNameForDatabase()).addEntity(row, coreEntity);
			}

			unitsWorked++;
			progressMonitor.worked(unitsWorked);

			last++;
			total++;

			if (last == 100) {
				System.err.println("Completed " + total + " so far.");
				last = 0;
			}
		}

		List<RowItemContainer<? extends RowItem<?>>> rowItemLists =
			new ArrayList<RowItemContainer<? extends RowItem<?>>>();
		rowItemLists.add(coreEntityManager.getEntities());

		for (LeafEntityManager entityManager : leafEntityManagers.values()) {
			for (RowItemContainer<? extends RowItem<?>> rowItems :
					entityManager.getRowItemContainers()) {
				rowItemLists.add(rowItems);
			}
		}

		return new DatabaseModel(rowItemLists);
	}

	private static CoreEntityManager createCoreEntityTable(
			String coreEntityDisplayName,
			String coreEntityTableName,
			String[] coreColumns,
			Map<String, ColumnDescriptor> columnDescriptors) {
		return new CoreEntityManager(
			coreEntityDisplayName,
			coreEntityTableName,
			fetchCoreEntityColumns(coreColumns, columnDescriptors));
	}

	private static Map<String, LeafEntityManager> createEntityTables(
			CoreEntityManager coreEntityManager,
			String[] nonCoreColumns,
			Map<String, ColumnDescriptor> columnDescriptors) {
		Map<String, LeafEntityManager> entityTables = new HashMap<String, LeafEntityManager>();

		for (String entityName : nonCoreColumns) {
			ColumnDescriptor column = columnDescriptors.get(entityName);
			entityTables.put(
				column.getNameForDatabase(),
				new LeafEntityManager(coreEntityManager.getEntities(), column));
		}

		return entityTables;
	}

	private static List<ColumnDescriptor> fetchCoreEntityColumns(
			String[] coreColumns, Map<String, ColumnDescriptor> columnDescriptors) {
		List<ColumnDescriptor> columns = new ArrayList<ColumnDescriptor>();

		for (String coreColumn : coreColumns) {
			columns.add(columnDescriptors.get(coreColumn));
		}

		return columns;
	}
}