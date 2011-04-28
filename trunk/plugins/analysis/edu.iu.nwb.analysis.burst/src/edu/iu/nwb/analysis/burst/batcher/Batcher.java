package edu.iu.nwb.analysis.burst.batcher;

import java.util.Date;

public interface Batcher {
	String getDateStringByIndex(int index);
	int getIndexByDate(Date date);
	int getSize();
}
