package edu.iu.sci2.preprocessing.zip2district.model;

import java.util.HashMap;
import java.util.Map;

import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.zip2district.ZipToDistrictException;

/**
 * 
 * PostBoxToDistrictMap is the mapping from post box number 
 * (last four digits of 9 digits Zip code) to congressional 
 * district. It is useless without ZipCodeToDistrictMap. 
 * 
 * There is two type of mapping here:
 * 1) postBoxToDistricts is the primary map. Any match should
 * be consider as result
 * 2) secondaryPostBoxToDistricts is the secondary map for the 
 * zip code data that contains "N" which mean "the rest of". It 
 * only should be considered if the post box didn't found in 
 * postBoxToDistricts
 * 
 * For more information about postBoxToDistricts and 
 * secondaryPostBoxToDistricts, please refer to ZipCodeToDistrictMap
 * 
 * @author kongch
 *
 */
public class PostBoxToDistrictMap {
	public static final String SECONDARY_IDENTIFIER = "N";
	private Map<String, String> postBoxToDistricts;
	private Map<String, String> secondaryPostBoxToDistricts;
	
	public PostBoxToDistrictMap() {
		postBoxToDistricts = new HashMap<String, String>();
		secondaryPostBoxToDistricts = new HashMap<String, String>();
	}
	
	/*
	 * Prepare mapping for later look up
	 */
	public void add(String postBox, String district) {
		if (postBox.contains(SECONDARY_IDENTIFIER)) {
			addSecondaryPostBoxToDistricts(postBox, district);
		} else {
			addPostBoxToDistricts(postBox, district);
		}
	}

	/**
	 * Look up district from the given post box number.
	 * @param zipCode - Instance of class ZipCode
	 * @return Return the corresponding district if 
	 * success. Else return an empty String
	 * @throws ZipToDistrictException 
	 */
	public String getDistrict(USZipCode zipCode) throws ZipToDistrictException {
		String district;
		try {
			district = getFromPostBoxToDistricts(zipCode);
		} catch (ZipToDistrictException e) {
			district = getFromSecondaryPostBoxToDistricts(zipCode);
		}

		return district;
	}
	
	private void addPostBoxToDistricts(String postBox, String district) {
		this.postBoxToDistricts.put(postBox, district);
	}

	private void addSecondaryPostBoxToDistricts(String postBox, String district) {
		postBox = postBox.replace(SECONDARY_IDENTIFIER, "");
		this.secondaryPostBoxToDistricts.put(postBox, district);
	}
	
	private String getFromPostBoxToDistricts(USZipCode zipCode) throws ZipToDistrictException {
		String postBox = zipCode.getPostBox();
		int remain = postBox.length();
		
		while (remain > 0) {
			String postBoxKey = postBox.substring(0, remain);
			String district = this.postBoxToDistricts.get(postBoxKey);
			remain--;
			if (district != null) {
				return district;
			}
		}
		
		throw new ZipToDistrictException("No result found");
	}
	
	private String getFromSecondaryPostBoxToDistricts(USZipCode zipCode) 
												throws ZipToDistrictException {
		String postBox = zipCode.getPostBox();
		int remain = postBox.length();

		/* secondary doesn't contain  full post box */
		if (remain == USZipCode.POST_BOX_LENGTH) {
			remain--;
		}
		
		while (remain > 0) {
			String postBoxKey = postBox.substring(0, remain);
			String district = this.secondaryPostBoxToDistricts.get(postBoxKey);
			remain--;
			if (district != null) {
				return district;
			}
		}
		
		/* Doesn't found any match */
		throw new ZipToDistrictException("No result found");
	}

}
