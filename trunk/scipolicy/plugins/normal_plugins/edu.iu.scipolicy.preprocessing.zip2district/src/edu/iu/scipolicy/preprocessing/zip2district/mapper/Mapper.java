package edu.iu.scipolicy.preprocessing.zip2district.mapper;

import edu.iu.scipolicy.preprocessing.zip2district.ZipToDistrictException;
import edu.iu.scipolicy.preprocessing.zip2district.model.USZipCode;

public interface Mapper {
	public String getCongressionalDistrict(USZipCode zipCode) throws ZipToDistrictException;
	public String getCongressionalDistrict(String zipCode) throws ZipToDistrictException;
}
