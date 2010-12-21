package edu.iu.scipolicy.preprocessing.zip2district.mapper;

import edu.iu.scipolicy.model.geocode.USDistrict;
import edu.iu.scipolicy.model.geocode.USZipCode;
import edu.iu.scipolicy.preprocessing.zip2district.ZipToDistrictException;

public interface Mapper {
	public USDistrict getCongressionalDistrict(USZipCode zipCode) throws ZipToDistrictException;
	public USDistrict getCongressionalDistrict(String zipCode) throws ZipToDistrictException;
}
