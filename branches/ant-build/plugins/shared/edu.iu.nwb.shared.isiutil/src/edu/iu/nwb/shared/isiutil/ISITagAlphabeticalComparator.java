package edu.iu.nwb.shared.isiutil;

import java.util.Comparator;

public class ISITagAlphabeticalComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		ISITag t1 = (ISITag) arg0;
		ISITag t2 = (ISITag) arg1;
		
		return t1.getColumnName().compareToIgnoreCase(t2.getColumnName());
	}

}
