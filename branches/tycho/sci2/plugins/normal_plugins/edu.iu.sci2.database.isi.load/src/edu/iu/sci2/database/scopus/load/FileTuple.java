package edu.iu.sci2.database.scopus.load;

import edu.iu.sci2.database.scholarly.FileField;
import prefuse.data.Tuple;

public class FileTuple<T extends FileField> extends ForwardingTuple {
	public FileTuple(Tuple delegate) {
		super(delegate);
	}
	
	public Object getField(T key) {
		return this.get(key.getName());
	}
	
	public String getStringField(T key) {
		return this.getString(key.getName());
	}
	
}
