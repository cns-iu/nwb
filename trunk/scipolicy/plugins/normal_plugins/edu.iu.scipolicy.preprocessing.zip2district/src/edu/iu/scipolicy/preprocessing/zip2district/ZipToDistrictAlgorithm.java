package edu.iu.scipolicy.preprocessing.zip2district;


import java.text.NumberFormat;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.zip2district.mapper.Mapper;

/**
 * 
 * This algorithm converts the given 9 digits U.S. zip codes into congressional 
 * district. The current algorithm create a mapping of U.S. Zip code to 
 * congressional district by using zip4dist-prefix.txt from. A lookup is made 
 * from the Zip code provided by user. The zip4dist-prefix.txt can be downloaded 
 * from <a href="http://www.govtrack.us/developers/data.xpd">Govtrack</a> 
 * @author kongch
 *
 */

public class ZipToDistrictAlgorithm implements Algorithm {
	public static final String[] DISTRICT_COLUMN_NAMES = {"Congressional District", "District"};
	private Data[] data;
	private LogService logger;
	private Table originalTable;
	private String zipCodeColumnName;
	private Mapper mapper;
    
    public ZipToDistrictAlgorithm(
    		Data[] data,
    		LogService logger,
    		Table originalTable,
    		String zipCodeColumnName,
    		Mapper mapper) {
        this.data = data;
		this.logger = logger;
		this.originalTable = originalTable;
		this.zipCodeColumnName = zipCodeColumnName;
		this.mapper = mapper;
	}

	public Data[] execute() {
		
		/* Start compute Zip codes to districts */
		Table outputTable = mapZipCodesToDistricts();
		
		/* After getting the output in table format make it available to the user */
		Data output = new BasicData(outputTable, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL,
					 String.format("With Congressional District from '%s'", 
							 						this.zipCodeColumnName));
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[] { output };
    }
	
	/*
	 * Input data from the ZIP_CODE_COLUMN_NAME is obtained from the original table & Lookups 
	 * through Zip code and district map. After processing all rows, the new output table is
	 * returned having original data and one new columns for congressional district. 
	 */
	public Table mapZipCodesToDistricts() {
		/*
		 * Create new output table with a new district column by using 
		 * the schema from the original table.
		 */
		String districtColumnName = TableUtilities.formNonConflictingNewColumnName(
											originalTable.getSchema(), DISTRICT_COLUMN_NAMES);
		logger.log(LogService.LOG_INFO,
				String.format("District values added to %s, respectively.", districtColumnName));
		
		Table outputTable = originalTable.getSchema().instantiate();
		outputTable.addColumn(districtColumnName, String.class);
		
		int failedCount = 0;
		int zipCodeColumnNumber = originalTable.getColumnNumber(zipCodeColumnName);
		int districtColumnNumber = outputTable.getColumnNumber(districtColumnName);
		Iterator<?> zipCodeColumnIterator = originalTable.iterator();
		while (zipCodeColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(zipCodeColumnIterator.next().toString());
			
			/* Convert Zip code to district */
			String district = "";
			String currentZipCode = "";
			
			Object zipCodeObject = originalTable.get(currentRowNumber, zipCodeColumnNumber);
			if (zipCodeObject != null) {
				currentZipCode = zipCodeObject.toString();
				try {
					district = mapper.getCongressionalDistrict(currentZipCode);
				} catch (ZipToDistrictException e) {
					/* Look up failed. Warn user about it */
					failedCount++;
					printWarningMessage(currentZipCode);
				}
			} else {
				/* Look up failed. Warn user about it */
				failedCount++;
				printWarningMessage(currentZipCode);
			}

			/*
			 * Add the new row to the new table
			 * by copying the original row & then adding 2 new columns to it.
			 */
			outputTable.addRow();
			TableUtilities.copyTableRow(currentRowNumber, currentRowNumber, 
										outputTable, originalTable);
			outputTable.set(currentRowNumber, districtColumnNumber, district);
		}
		/* Show statistic information */
		int totalRow = originalTable.getRowCount();
		NumberFormat numberFormat = NumberFormat.getInstance();
		logger.log(LogService.LOG_INFO, String.format(
				"Successfully converted %s out of %s ZIP codes to congressional districts.", 
				numberFormat.format(totalRow - failedCount),
				numberFormat.format(totalRow)));
		return outputTable;
	}
	
	private void printWarningMessage(String zipCodeString) {
		logger.log(LogService.LOG_WARNING,
					String.format(
					"The row with \"%s\" ZIP code has not been given a"
					+ "congressional district. ZIP+4 code is needed",
					zipCodeString));
	}
}
