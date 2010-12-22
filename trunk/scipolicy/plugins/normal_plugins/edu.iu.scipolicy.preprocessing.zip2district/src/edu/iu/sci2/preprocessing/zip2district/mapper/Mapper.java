package edu.iu.sci2.preprocessing.zip2district.mapper;

import edu.iu.sci2.model.geocode.USDistrict;
import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.zip2district.ZipToDistrictException;

public interface Mapper {
	public USDistrict getCongressionalDistrict(USZipCode zipCode) throws ZipToDistrictException;
	public USDistrict getCongressionalDistrict(String zipCode) throws ZipToDistrictException;
}
