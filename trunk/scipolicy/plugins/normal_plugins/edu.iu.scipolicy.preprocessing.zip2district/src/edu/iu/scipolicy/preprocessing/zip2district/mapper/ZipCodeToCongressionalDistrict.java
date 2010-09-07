package edu.iu.scipolicy.preprocessing.zip2district.mapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.framework.BundleContext;

import edu.iu.scipolicy.model.geocode.USDistrict;
import edu.iu.scipolicy.model.geocode.USZipCode;
import edu.iu.scipolicy.preprocessing.zip2district.ZipToDistrictException;
import edu.iu.scipolicy.preprocessing.zip2district.model.ZipCodeToDistrictMap;

/**
 * 
 * Convert zip code to congressional district is fundamental
 * by the data set (Table mapping ZIP+4 codes to congressional 
 * districts) comes from the work by Carl Malamud and 
 * Aaron Swartz (http://watchdog.net/about/api#zip2rep). The 
 * data set file (zip4dist-prefix.txt) can be downloaded from 
 * http://www.govtrack.us/developers/data.xpd
 * 
 * Firstly, it build up a cache mapping by using ZipCodeToDistrictMap
 * and provide O(1) look up time. Please refer to ZipCodeToDistrictMap
 * for implementation description
 * 
 * Warning: To reload the mapping, reloadMap()
 * 
 * @author kongch
 *
 */
public class ZipCodeToCongressionalDistrict implements Mapper {
	public static final String DATA_SEPARATOR = " ";
	public static final String ZIP_CODE_TO_DISTRICT_FILE_PATH = "zip4dist-prefix.txt";
	
	/* 
	 * It is load when the first instance is created. It is 
	 * defined as class parameter to prevent reload from the 
	 * consequent new instance since the data set is 25MB
	 */
	private static ZipCodeToDistrictMap zipCodeToDistrictMap;
	private DistrictRegistry districtRegistry;
	private URL url;
	
	public ZipCodeToCongressionalDistrict() {
		url = ClassLoader.getSystemResource(ZIP_CODE_TO_DISTRICT_FILE_PATH);
		zipCodeToDistrictMap = new ZipCodeToDistrictMap();
		try {
			districtRegistry = DistrictRegistry.getInstance();
		} catch (ZipToDistrictException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public ZipCodeToCongressionalDistrict(BundleContext bundleContext) {
		url = bundleContext.getBundle().getResource(ZIP_CODE_TO_DISTRICT_FILE_PATH);
		zipCodeToDistrictMap = new ZipCodeToDistrictMap();
		try {
			districtRegistry = DistrictRegistry.getInstance(bundleContext);
		} catch (ZipToDistrictException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
/** add into JUnit test
	public static void main(String[] args) {
		ZipCodeToCongressionalDistrict converter = new ZipCodeToCongressionalDistrict();
		System.out.println("Finish caching!!!");
		System.out.println(converter.getCongressionalDistrict(USZipCode.parse("47401"))); //IN-09
		System.out.println(converter.getCongressionalDistrict(USZipCode.parse("01046"))); // MA01
		System.out.println(converter.
							getCongressionalDistrict(USZipCode.parse("01453-6503"))); //MA-01
		System.out.println(converter.getCongressionalDistrict(USZipCode.parse("00609"))); //PR-00
		System.out.println(converter.
							getCongressionalDistrict(USZipCode.parse("02116-3991"))); //MA-08
		System.out.println(converter.
							getCongressionalDistrict(USZipCode.parse("02116-3998"))); //MA-09
	}
 * @throws ZipToDistrictException 
**/
	public USDistrict getCongressionalDistrict(USZipCode zipCode) throws ZipToDistrictException {
		String districtName = getZipCodeToDistrictMap(url).getDistrict(zipCode);
		return getUSDistrict(districtName);
	}
	
	public USDistrict getCongressionalDistrict(String zipCode) throws ZipToDistrictException {
		String districtName = getZipCodeToDistrictMap(url).getDistrict(USZipCode.parse(zipCode));
		return getUSDistrict(districtName);
	}
	
	public void reloadMap() throws ZipToDistrictException {
		zipCodeToDistrictMap = new ZipCodeToDistrictMap();
		initializeZipCodeToDistrictMap(url);
	}
	
	/* 
	 * Control setup of ZipCodeToDistrictMap. We don't want to reload
	 * this expensive (25MB file size) algorithm.
	 */
	private ZipCodeToDistrictMap getZipCodeToDistrictMap(URL url) throws ZipToDistrictException {
		if (zipCodeToDistrictMap.isEmpty()) {
			initializeZipCodeToDistrictMap(url);
		}
		return zipCodeToDistrictMap;
	}
	
	private USDistrict getUSDistrict(String districtName) throws ZipToDistrictException {
		return districtRegistry.getDistrict(districtName);
	}
	
	private void initializeZipCodeToDistrictMap(URL zipCodeToDistrictFilePath) 
														throws ZipToDistrictException {
		InputStream inStream = null;
		BufferedReader input = null;
		String line;
		
		try {
			URLConnection connection = zipCodeToDistrictFilePath.openConnection();
			connection.setDoInput(true);
			inStream = connection.getInputStream();
			input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			
			while (null != (line = input.readLine())) {
				String[] tokens = line.split(DATA_SEPARATOR);
				if (tokens.length == 2) {
					zipCodeToDistrictMap.add(tokens[0], tokens[1]);
				}
			}
		} catch (Exception e) {
		    zipCodeToDistrictMap = null;
			String exceptionMessage = String.format(
				"Unable to access database URL %s", zipCodeToDistrictFilePath.toString());
			throw new ZipToDistrictException(exceptionMessage);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				
				if (inStream != null) {
					inStream.close();
				}
		    } catch (IOException e) {
				String exceptionMessage = String.format(
					"Unable to close file %s",
					zipCodeToDistrictFilePath.toString());
				throw new ZipToDistrictException(exceptionMessage, e);
		    }
		}
	}
}
