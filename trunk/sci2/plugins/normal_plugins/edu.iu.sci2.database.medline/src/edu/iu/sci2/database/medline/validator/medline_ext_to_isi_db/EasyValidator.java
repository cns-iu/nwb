package edu.iu.sci2.database.medline.validator.medline_ext_to_isi_db;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import com.google.common.base.Preconditions;

import edu.iu.sci2.medline.common.MedlineTable;

/**
 * <p>
 * This is a 'validator' that reuses the existing validators and converters to
 * transform a {@code file-ext:txt} MEDLINE®/PubMed® file to a {@code db:isi}
 * file.
 * </p>
 * 
 * <p>
 * Specifically it uses {@code edu.iu.sci2.medline.validator.Validator} for
 * validation,
 * {@code edu.iu.sci2.medline.converter.medline_to_table_converter.Converter} to
 * get a {@linkplain MedlineTable}, and
 * {@code edu.iu.sci2.database.medline.converter.medline_table_to_isi_db.Converter}
 * to get the {@code db:isi} {@linkplain Database}.
 * </p>
 */
public class EasyValidator implements Algorithm, ProgressTrackable {

	public static class Factory implements AlgorithmFactory, ProgressTrackable {
		private BundleContext bundleContext;
		private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
		private static final String validatorPID = "edu.iu.sci2.medline.validator.Validator";
		private static final String medlineToMedlineTableConverterPID = "edu.iu.sci2.medline.converter.medline_to_table_converter.Converter";
		private static final String medlineTableToDBConverterPID = "edu.iu.sci2.database.medline.converter.medline_table_to_isi_db.Converter";

		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {
			Preconditions.checkNotNull(this.bundleContext,
					"This bundle was not activated correctly.");
			Preconditions.checkNotNull(data, "The data must not be null.");
			Preconditions
					.checkArgument(parameters.isEmpty(),
							"The CIShell 1.0 Spec guarantees that parameters will be null for a validator.");

			this.progressMonitor.start(ProgressMonitor.WORK_TRACKABLE, 3);
			this.progressMonitor.describeWork("Finding the validator.");
			AlgorithmFactory validator = getValidator(this.bundleContext);
			this.progressMonitor.worked(1);

			this.progressMonitor.describeWork("Finding the table converter.");
			AlgorithmFactory medlineToTableConverter = getMedlineToMedlineTableConverter(this.bundleContext);
			this.progressMonitor.worked(1);

			this.progressMonitor
					.describeWork("Finding the database converter.");
			AlgorithmFactory medlineTableToDBConveter = getMedlineTableToDBConverter(this.bundleContext);
			this.progressMonitor.worked(1);

			if (validator == null) {
				throw new AlgorithmCreationFailedException(
						"This algorithm relies on '" + validatorPID
								+ "', but that algorithm could not be found.");
			}

			if (medlineToTableConverter == null) {
				throw new AlgorithmCreationFailedException(
						"This algorithm relies on '"
								+ medlineToMedlineTableConverterPID
								+ "', but that algorithm could not be found.");
			}

			if (medlineTableToDBConveter == null) {
				throw new AlgorithmCreationFailedException(
						"This algorithm relies on '"
								+ medlineTableToDBConverterPID
								+ "', but that algorithm could not be found.");
			}

			this.progressMonitor.done();
			return new EasyValidator(data, ciShellContext, validator,
					medlineToTableConverter, medlineTableToDBConveter);
		}

		protected void activate(ComponentContext componentContext) {
			this.bundleContext = componentContext.getBundleContext();
		}

		private static AlgorithmFactory getMedlineTableToDBConverter(
				BundleContext bundleContext) {
			return AlgorithmUtilities.getAlgorithmFactoryByPID(
					medlineTableToDBConverterPID, bundleContext);
		}

		private static AlgorithmFactory getMedlineToMedlineTableConverter(
				BundleContext bundleContext) {
			return AlgorithmUtilities.getAlgorithmFactoryByPID(
					medlineToMedlineTableConverterPID, bundleContext);
		}

		private static AlgorithmFactory getValidator(BundleContext bundleContext) {
			return AlgorithmUtilities.getAlgorithmFactoryByPID(validatorPID,
					bundleContext);
		}

		@Override
		public void setProgressMonitor(ProgressMonitor monitor) {
			this.progressMonitor = monitor;

		}

		@Override
		public ProgressMonitor getProgressMonitor() {
			return this.progressMonitor;
		}

	}

	private Data[] data;
	private CIShellContext ciShellContext;
	private AlgorithmFactory validator;
	private AlgorithmFactory medlineToTableConverter;
	private AlgorithmFactory medlineTableToDBConveter;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	public EasyValidator(Data[] data, CIShellContext ciShellContext,
			AlgorithmFactory validator,
			AlgorithmFactory medlineToTableConverter,
			AlgorithmFactory medlineTableToDBConveter) {
		this.data = data;
		this.ciShellContext = ciShellContext;
		this.validator = validator;
		this.medlineToTableConverter = medlineToTableConverter;
		this.medlineTableToDBConveter = medlineTableToDBConveter;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		this.progressMonitor.start(ProgressMonitor.WORK_TRACKABLE, 3);
		try {
			this.progressMonitor.describeWork("Converting to a File.");
			Data[] medlineFile = this.validator.createAlgorithm(this.data,
					new Hashtable<String, Object>(), this.ciShellContext)
					.execute();
			checkMedlineData(medlineFile);
			this.progressMonitor.worked(1);

			this.progressMonitor.describeWork("Converting to a Table.");
			Data[] medlineTable = this.medlineToTableConverter.createAlgorithm(
					medlineFile, new Hashtable<String, Object>(),
					this.ciShellContext).execute();
			checkMedlineTableData(medlineTable);
			this.progressMonitor.worked(1);

			this.progressMonitor.describeWork("Converting to a Database.");
			Data[] medlineDB = this.medlineTableToDBConveter.createAlgorithm(
					medlineTable, new Hashtable<String, Object>(),
					this.ciShellContext).execute();
			checkMedlineDBData(medlineDB);
			this.progressMonitor.worked(1);

			return medlineDB;
		} catch (AlgorithmExecutionException e) {
			// SOMEDAY let cishell handle the print of the stack trace.
			e.printStackTrace();
			throw new AlgorithmExecutionException(
					"Failed to 'validate' from a medline file to isi:db", e);
		} catch (AlgorithmCreationFailedException e) {
			// SOMEDAY let cishell handle the print of the stack trace.
			e.printStackTrace();
			throw new AlgorithmExecutionException(
					"Failed to 'validate' from a medline file to isi:db", e);
		} finally {
			this.progressMonitor.done();
		}
	}

	private static void checkMedlineDBData(Data[] medlineDB)
			throws AlgorithmExecutionException {
		if (medlineDB == null) {
			throw new AlgorithmExecutionException("The medline DB was null.");
		}

		if (medlineDB[0] == null) {
			throw new AlgorithmExecutionException(
					"The medline DB data was null.");
		}

		if (medlineDB[0].getData() == null) {
			throw new AlgorithmExecutionException(
					"The medline DB data's data was null.");
		}

		if (!(medlineDB[0].getData() instanceof Database)) {
			throw new AlgorithmExecutionException(
					"The medline DB data's data was not of the expected format.");
		}

	}

	private static void checkMedlineTableData(Data[] medlineTable)
			throws AlgorithmExecutionException {
		if (medlineTable == null) {
			throw new AlgorithmExecutionException("The medline table was null.");
		}

		if (medlineTable[0] == null) {
			throw new AlgorithmExecutionException(
					"The medline table data was null.");
		}

		if (medlineTable[0].getData() == null) {
			throw new AlgorithmExecutionException(
					"The medline table data's data was null.");
		}

		if (!(medlineTable[0].getData() instanceof MedlineTable)) {
			throw new AlgorithmExecutionException(
					"The medline table data's data was not of the expected format.");
		}

	}

	public static void checkMedlineData(Data[] medlineFile)
			throws AlgorithmExecutionException {
		if (medlineFile == null) {
			throw new AlgorithmExecutionException("The medline file was null.");
		}

		if (medlineFile[0] == null) {
			throw new AlgorithmExecutionException(
					"The medline file data was null.");
		}

		if (medlineFile[0].getData() == null) {
			throw new AlgorithmExecutionException(
					"The medline file data's data was null.");
		}

		if (!(medlineFile[0].getData() instanceof File)) {
			throw new AlgorithmExecutionException(
					"The medline file data's data was not of the expected format.");
		}
	}

	@Override
	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	@Override
	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}
}
