package edu.iu.sci2.reader.facebook.FacebookFriend.utilities;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
	private FileWriter fw;
	private String eol;

	private String separator;
	
	public CSVWriter(String path) throws IOException
	{
		fw = new FileWriter(path);
		eol= System.getProperty("line.separator");
		separator = ",";
	}
	
	public String getEol() {
		return eol;
	}

	public void setEol(String eol) {
		this.eol = eol;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	public void writeNext(String[] stringList) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		for(String string:stringList)
		{
		    sb.append(string);
			sb.append(this.separator);
		}
		//remove the last separator
		sb.deleteCharAt(sb.length()-1);
		sb.append(eol);
		fw.write(sb.toString());
	}
	
	public void close() throws IOException
	{
	    fw.close();
	}
}
