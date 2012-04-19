package edu.iu.sci2.medline.converter.medlinetable_to_table_converter;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;

import prefuse.data.Table;

import com.google.common.base.Preconditions;

import edu.iu.sci2.medline.common.MedlineTable;

/**
 * A converter that will convert from a {@link MedlineTable} to a
 * {@linkplain Table}.
 * 
 */
public class Converter implements Algorithm {
	public static class Factory implements AlgorithmFactory {

		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {
			Preconditions
					.checkArgument(parameters.isEmpty(),
							"The CIShell 1.0 Spec guarantees that parameters will be null for a converter.");
			return new Converter(data[0]);
		}

	}

	private Data datum;

	public Converter(Data datum) {
		Preconditions.checkNotNull(datum,
				"The datum from the data[] must not be null");
		this.datum = datum;

	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			MedlineTable medlineTable = (MedlineTable) this.datum.getData();
			Table table = medlineTable.getTable();
			return new Data[] { DataFactory.withClassNameAsFormat(table,
					DataProperty.TABLE_TYPE, this.datum,
					"A table from " + medlineTable) };
		} catch (ClassCastException e) {
			throw new AlgorithmExecutionException(
					"The pathname could not be retrieved from the data.", e);
		}
	}
}
