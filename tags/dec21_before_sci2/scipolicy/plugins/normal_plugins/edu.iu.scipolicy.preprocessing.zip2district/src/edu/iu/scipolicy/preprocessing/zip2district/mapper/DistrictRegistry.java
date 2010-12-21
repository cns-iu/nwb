package edu.iu.scipolicy.preprocessing.zip2district.mapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import edu.iu.scipolicy.preprocessing.zip2district.ZipToDistrictException;
import edu.iu.scipolicy.model.geocode.USDistrict;


public final class DistrictRegistry {
	public static final String DISTRICT_TO_GEOLOCATION_FILE_PATH = "dist2geolocation.txt";
	private static DistrictRegistry instance;
	private Map<String, USDistrict> districtsMap;
	
	private DistrictRegistry(URL url) throws ZipToDistrictException {
		try {
			districtsMap = extractMaps(url);
		} catch (IOException e) {
			throw new ZipToDistrictException("Failed prepare geolocation mapping" + e.getMessage());
		}
	}
	
	public static DistrictRegistry getInstance() throws ZipToDistrictException {
		if (instance == null) {
			URL url = ClassLoader.getSystemResource(DISTRICT_TO_GEOLOCATION_FILE_PATH);
			instance = new DistrictRegistry(url);
		}
		return instance;
	}
	
	public static DistrictRegistry getInstance(BundleContext bundleContext) 
													throws ZipToDistrictException {
		if (instance == null) {
			URL url = bundleContext.getBundle().getResource(DISTRICT_TO_GEOLOCATION_FILE_PATH);
			instance = new DistrictRegistry(url);
		}
		return instance;
	}
	
	public USDistrict getDistrict(String districtLabel) throws ZipToDistrictException {
		if (districtsMap.containsKey(districtLabel)) {
			return districtsMap.get(districtLabel);
		}
		System.out.println(districtLabel);
		throw new ZipToDistrictException("No result found " + districtLabel);
	}

	private static Map<String, USDistrict> extractMaps(URL url) throws IOException, 
																		ZipToDistrictException {
		InputStream inStream = null;
		BufferedReader input = null;
		String line;
		Map<String, USDistrict> map = new HashMap<String, USDistrict>();
		
		try {
			URL geomapUrl = url;
			URLConnection connection =  geomapUrl.openConnection();
			connection.setDoInput(true);
			inStream = connection.getInputStream();
			input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			
			while (null != (line = input.readLine())) {
				USDistrict district;
				try {
					district = USDistrict.valueOf(line);
					map.put(district.getLabel(), district);
				} catch (Exception e) {
					throw new ZipToDistrictException(e.getMessage());
				}
			}
		} finally {
			
			if (inStream != null) {
				inStream.close();
			}
			
			if (input != null) {
				input.close();
			}
		}
		
		return map;
	}
}
