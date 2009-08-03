package edu.iu.nwb.converter.prefusescopus;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.converter.prefusescopus.util.StringUtil;

public class ScopusReaderAlgorithm implements Algorithm {
    public static final String CSV_MIME_TYPE = "file:text/csv";
	Data[] data;
    CIShellContext context;
    LogService log;
    
    private static final String AUTHOR_COLUMN_NAME = "Authors";
    private static final String ORIG_AUTHOR_COLUMN_NAME_SEPARATOR = ", ";
    private static final String NEW_AUTHOR_COLUMN_NAME_SEPARATOR = "|";
    
    private static final String REFERENCE_COLUMN_NAME = "References";
    private static final String ORIG_REFERENCE_COLUMN_NAME_SEPARATOR = "; ";
    private static final String NEW_REFERENCE_COLUMN_NAME_SEPARATOR = "|";
    
    public ScopusReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Data inputData = convertInputData(data[0]);
    	Table scopusTable = (Table) inputData.getData();
    	scopusTable = normalizeAuthorNames(scopusTable);
    	scopusTable = normalizeReferences(scopusTable);
    	scopusTable = addSelfReferences(scopusTable);
        Data[] outputData = formatAsData(scopusTable);
        return outputData;
    }
    
    
    private Data convertInputData(Data inputData) throws AlgorithmExecutionException {
    	DataConversionService converter = (DataConversionService)
        	context.getService(DataConversionService.class.getName());
    	
		/* This is a bit like a cast. We know the nsf format is also a csv, so
		 * we change the format to csv so the Conversion service knows it is a
		 * csv when it tries to convert it to a prefuse.data.Table
		 */
		 
		//printTable((Table) inputData.getData());
		Data formatChangedData = new BasicData(inputData.getMetadata(),
											  (File) inputData.getData(),
											  CSV_MIME_TYPE);
		try {
			Data convertedData =
				converter.convert(formatChangedData, Table.class.getName());
			return convertedData;
		} catch (ConversionException e){
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }
    
    private Table normalizeAuthorNames(Table scopusTable) {
    	Column authorColumn = scopusTable.getColumn(AUTHOR_COLUMN_NAME);
    	if (authorColumn == null) {
    		printNoAuthorColumnWarning();
    		return scopusTable;
    	}
    	try {
	    	for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
				int rowIndex = tableIt.nextInt();
	    		String authors = authorColumn.getString(rowIndex);
	    		if (authors != null && ! authors.equals("")) {
	    			String normalizedAuthors = normalizeAuthorNames(authors);
	    			authorColumn.setString(normalizedAuthors, rowIndex);
	    		}
	    	}
    	} catch (DataTypeException e) {
    		printColumnNotOfTypeStringWarning(e);
    		return scopusTable;
    	}
    	
    	return scopusTable;
    }
    
    private String normalizeAuthorNames(String authorNames) {
    	//trim leading and trailing whitespace from each author name.
    	String[] eachAuthorName = authorNames.split(ORIG_AUTHOR_COLUMN_NAME_SEPARATOR);
    	String normalizedAuthorNames = StringUtil.join(eachAuthorName, NEW_AUTHOR_COLUMN_NAME_SEPARATOR);
    	return normalizedAuthorNames;
    }
    
    private Table normalizeReferences(Table scopusTable) {
    	Column referenceColumn = scopusTable.getColumn(REFERENCE_COLUMN_NAME);
    	if (referenceColumn == null) {
    		printNoReferenceColumnWarning();
    		return scopusTable;
    	}
    	try {
			for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
				int rowIndex = tableIt.nextInt();
	    		String references = referenceColumn.getString(rowIndex);
	    		if (references != null && ! references.equals("")) {
	    			String normalizedReferences = normalizeReferences(references);
	    			referenceColumn.setString(normalizedReferences, rowIndex);
	    		}
	    	}
    	} catch (DataTypeException e) {
    		printColumnNotOfTypeStringWarning(e);
    		return scopusTable;
    	}
    	
    	return scopusTable;
    }
    
    private String normalizeReferences(String references) {
    	String[] eachReference = references.split(ORIG_REFERENCE_COLUMN_NAME_SEPARATOR);
    	String normalizedReferences = StringUtil.join(eachReference, NEW_REFERENCE_COLUMN_NAME_SEPARATOR);
    	return normalizedReferences;
    }
    
    private static final String SELF_REFERENCE_COLUMN_NAME = "Self Reference";
    
    private Table addSelfReferences(Table scopusTable) throws AlgorithmExecutionException {
    		//create the self-reference column
    		scopusTable.addColumn(SELF_REFERENCE_COLUMN_NAME, String.class);
    		//for each record in the table...
    		for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
    			int rowIndex = tableIt.nextInt();
    			Tuple row = scopusTable.getTuple(rowIndex);
    			//calculate the self-reference based on the contents of other fields
    			String selfReference = createSelfReference(row);
    			//add the self-reference to the current record
    			scopusTable.setString(rowIndex, SELF_REFERENCE_COLUMN_NAME, selfReference);
    		}
    		
    		return scopusTable;
    	}
    
    private static final String AUTHORS_COLUMN_NAME = "Authors";
    private static final String TITLE_COLUMN_NAME = "Title";
    private static final String YEAR_COLUMN_NAME = "Year";
    private static final String SOURCE_TITLE_COLUMN_NAME = "Source title";
    private static final String VOLUME_COLUMN_NAME = "Volume";
    private static final String ISSUE_COLUMN_NAME = "Issue";
    private static final String PAGE_START_COLUMN_NAME = "Page start";
    private static final String PAGE_END_COLUMN_NAME = "Page end";
    
   private String createSelfReference(Tuple isiRow) throws AlgorithmExecutionException {
	   StringBuffer selfReference = new StringBuffer();
	   try {
		   String authors = extractAuthors(isiRow);
		   if (authors == null) {printNoAuthorColumnWarning(); return "";}
		   selfReference.append(authors);
		   selfReference.append(", ");
		   String title = extractTitle(isiRow);
		   if (title == null) {printNoTitleColumnWarning(); return "";}
		   selfReference.append(title);
		   selfReference.append(" ");
		   
		   String year = extractYear(isiRow);
		   if (year != null) {
			   selfReference.append(" (");
			   selfReference.append(year);
			   selfReference.append(")");
		   }
		   String sourceTitle = extractSourceTitle(isiRow);
		   if (sourceTitle != null) {
			   selfReference.append(" ");
			   selfReference.append(sourceTitle);
		   }
		   String volume = extractVolume(isiRow);
		   if (volume != null) {
			   selfReference.append(", ");
			   selfReference.append(volume);
		   }
		   String issue = extractIssue(isiRow);
		   if (issue != null) {
			   selfReference.append(" (");
			   selfReference.append(issue);
			   selfReference.append(")");
		   }
		   String pageStart = extractPageStart(isiRow);
		   if (pageStart != null) {
			   selfReference.append(", pp. ");
			   selfReference.append(pageStart);
		   } 
		   String pageEnd = extractPageEnd(isiRow);
		   if (pageEnd != null) {
			   selfReference.append("-");
			   selfReference.append(pageEnd);
		   }
		} catch (ArrayIndexOutOfBoundsException e) {
			/* Column requested does not exist (for entire table or just this
			 * field?)  Fail silently. This will happen normally.
			 * The remainder of the self reference will be returned.
			 */
		} catch (DataTypeException e) {
			/* Column type cannot be interpreted as a string (?)
			 * This should only happen if the column is of
			 * some bizarre unexpected type.
			 */
			throw new AlgorithmExecutionException(
					"Some elements in the tuple '" + isiRow
					+ "' cannot be converted to a String (apparently)", e);
		}
		
		return selfReference.toString();
   }
    
    
    private String extractPageEnd(Tuple isiRow) {
    	return trimBrackets(isiRow.getString(PAGE_END_COLUMN_NAME));
}

	private String extractPageStart(Tuple isiRow) {
		return trimBrackets(isiRow.getString(PAGE_START_COLUMN_NAME));
}

	private String extractIssue(Tuple isiRow) {
		return trimBrackets(isiRow.getString(ISSUE_COLUMN_NAME));
}

	private String extractVolume(Tuple isiRow) {
		return isiRow.getString(VOLUME_COLUMN_NAME);
}

	private String extractSourceTitle(Tuple isiRow) {
		return isiRow.getString(SOURCE_TITLE_COLUMN_NAME);
}

	private String extractYear(Tuple isiRow) {
	return isiRow.getString(YEAR_COLUMN_NAME);
}

	private String extractTitle(Tuple isiRow) {
		return isiRow.getString(TITLE_COLUMN_NAME);
}

	private String extractAuthors(Tuple isiRow) {
		String authorsWithNewSeparator = isiRow.getString(AUTHORS_COLUMN_NAME);
		String[] eachAuthor = authorsWithNewSeparator.split("\\" + NEW_AUTHOR_COLUMN_NAME_SEPARATOR);
		//we need to use the original separator, because the reference column
		//we are constructing needs to look like the raw references that other
		//papers will use.
		String authorsWithOriginalSeparator = 
			StringUtil.join(eachAuthor, ORIG_AUTHOR_COLUMN_NAME_SEPARATOR);
		return authorsWithOriginalSeparator;
}

	private Data[] formatAsData(Table scopusTable)
			throws AlgorithmExecutionException {
    	try {
			Data[] dm = new Data[] {new BasicData(scopusTable, Table.class.getName())};
			dm[0].getMetadata().put(DataProperty.LABEL, "Normalized Scopus table");
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
			return dm;
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }
	
	private String trimBrackets(String s) {
		if (s == null) return null;
		
		String tempS = s;
		if (tempS.startsWith("[")) {
			tempS = tempS.substring(1);
		}
		if (tempS.endsWith("]")) {
			tempS = tempS.substring(0, tempS.length() - 1);
		}
		
		String result = tempS;
		return result;
	}
    
    private void printNoAuthorColumnWarning() {
    	this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
    			AUTHOR_COLUMN_NAME + "' in scopus file. " +
    					"We will continue on without attempting to normalize this column");
    }
    
    private void printNoReferenceColumnWarning() {
    	this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
    			REFERENCE_COLUMN_NAME + "' in scopus file. " +
    					"We will continue on without attempting to normalize this column");
    }
    
    private void printNoTitleColumnWarning() {
    	this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
    			TITLE_COLUMN_NAME + "' in scopus file. " +
    					"We will continue on without attempting to normalize this column");
    }
    
    private void printColumnNotOfTypeStringWarning(DataTypeException e) {
    	this.log.log(LogService.LOG_WARNING,
    			"The column '" + AUTHOR_COLUMN_NAME
    			+ "' in the scopus file cannot be normalized, because it cannot "
    			+ "be interpreted as text. Skipping normalization of authors", e);
    }
}