package edu.iu.sci2.preprocessing.zip2district.model;

import java.util.HashMap;
import java.util.Map;

import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.zip2district.ZipToDistrictException;

/**
 * 
 * This map structure is design to parse data from zip4dist-prefix.txt 
 * file (downloaded from http://www.govtrack.us/developers/data.xpd) 
 * that map 9 digits zip code to congressional district
 * 
 * The mapping is complicated. Basically the data file provide 
 * mapping of following
 * 
 * <3 to 9 digits Zip Code> <district>
 * 
 * Here is a brief of the example mapping of zip4dist-prefix.txt and its 
 * detail
 * 1) 098 WA-04 : Any zip code start with 098 is belong to WA-04 district
 * 2) 49803 WA-05 : Any zip code start with 49803 is belong to WA-05 district
 * 3) 78901-12 MA-34: Any zip code start with 78901-12 is belong to MA-24 district
 * 4) 41111-9063 MA-01 : Direct match of 41111-9063 is belong to MA-01 district
 * 5) 41111-90N MA-09: Any zip code start with 41111-90 but not 41111-9063 is 
 * 					   belong to MA-09 district
 * 
 * Given the above data rules, I intend to modularized this problem to 2 parts
 * Part 1: Problem 1) and problem 2) are solved by ZipCodeToDistrictMap
 * Part 2: The remaining problems are solved by PostBoxToDistrictMap
 * 
 * This implementation look up efficiency is in constant time. 
 *  
 * @author kongch
 *
 */
public class ZipCodeToDistrictMap {
	public static final String UZIP_SEPARATOR = "-";
	/* Direct map of uzip to congressional district */
	private Map<String, String> uzipToDistrictMap;
	
	/* Map of uzip to the PostBoxToDistrictMap that holds the district list */
	private Map<String, PostBoxToDistrictMap> uzipToPostBoxToDistrictMap;
	
	public ZipCodeToDistrictMap() {
		uzipToDistrictMap = new HashMap<String, String>();
		uzipToPostBoxToDistrictMap = new HashMap<String, PostBoxToDistrictMap>();
	}
	
	/** 
	 * Prepare mapping set of 9 digits ZIP code to congressional district 
	 * for look up. uzip is the first 5 digits ZIP code and postBox is 
	 * the last 4 digits ZIP code.
	 */
	public void add(String zipCode, String district) {
		String uzip = null;
		String postBox = null;
		String[] zipString = zipCode.split(UZIP_SEPARATOR);
		
		if ((zipString.length > 0)) {
			uzip = zipString[0].trim();
		}
		
		if (zipString.length > 1) {
			postBox = zipString[1].trim();
		}
		
		if (postBox != null) {
			addUzipToPostBoxToDistrictMap(uzip, postBox, district.trim());
		} else if (uzip != null) {
			addUzipToDistrictMap(uzip, district.trim());
		}
	}
	
	/**
	 * Get congressional district that the 9 digits zip code belong to.
	 * @param zipCode - 9 digits Zip codes to be look up
	 * @return Return congressional district in String if match found. 
	 * Else empty String is returned. 
	 * @throws ZipToDistrictException 
	 */
	public String getDistrict(USZipCode zipCode) throws ZipToDistrictException {
		String district;
		try {
			district = getFromUzipToDistrictMap(zipCode);
		} catch (ZipToDistrictException e) {
			district = getFromUzipToPostBoxToDistrictMap(zipCode);
		}
		
		return district;
	}
	
	public boolean isEmpty() {
		return (uzipToDistrictMap.isEmpty() && uzipToPostBoxToDistrictMap.isEmpty());
	}
	
	private String getFromUzipToDistrictMap(USZipCode zipCode) throws ZipToDistrictException {
		String uzip = zipCode.getUzip();
		int remain = uzip.length();
		
		while (remain > 0) {
			String uzipKey = uzip.substring(0, remain);
			String district = this.uzipToDistrictMap.get(uzipKey);
			remain--;
			if (district != null) {
				return district;
			}
		}
		
		throw new ZipToDistrictException("No result found");
	}
	
	private String getFromUzipToPostBoxToDistrictMap(USZipCode zipCode) 
															throws ZipToDistrictException {
		PostBoxToDistrictMap postBoxToDistrictMap = this.uzipToPostBoxToDistrictMap.
																get(zipCode.getUzip());
		
		/* no matched 5 digits zip found */
		if (postBoxToDistrictMap == null) {
			throw new ZipToDistrictException("No result found");
		}
		
		return postBoxToDistrictMap.getDistrict(zipCode);

	}
	
	private void addUzipToDistrictMap(String uzip, String district) {
		this.uzipToDistrictMap.put(uzip, district);
	}
	
	private void addUzipToPostBoxToDistrictMap(String uzip, String postBox, String district) {
		PostBoxToDistrictMap postBoxToDistrictMap = this.uzipToPostBoxToDistrictMap.get(uzip);
		
		if (postBoxToDistrictMap == null) {
			postBoxToDistrictMap = new PostBoxToDistrictMap();
			this.uzipToPostBoxToDistrictMap.put(uzip, postBoxToDistrictMap);
		}
		postBoxToDistrictMap.add(postBox , district);
	}
}
