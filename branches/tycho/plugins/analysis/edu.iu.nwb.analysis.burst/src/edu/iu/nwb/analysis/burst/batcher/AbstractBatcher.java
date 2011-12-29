package edu.iu.nwb.analysis.burst.batcher;

import java.util.Date;
import org.joda.time.DateTime;

public abstract class AbstractBatcher implements Batcher {
	private DateTime startDate;
	private DateTime endDate;
	private int size;
	private int batchByUnits;
	
	public AbstractBatcher(Date startDate, Date endDate, int batchByUnits) {
		this.startDate = this.getDateTime(startDate);
		this.endDate = this.getDateTime(endDate);
		this.setBatchByUnits(batchByUnits);
		this.setSize(batchByUnits);
	}
	
	public String getDateStringByIndex(int index) {
		int distance = index * this.batchByUnits;
		return this.getDateStringByDistance(distance);
	}

	public int getIndexByDate(Date date) {
		return this.getDistanceByDate(date) / this.batchByUnits;
	}
	
	public DateTime getStartDate() {
		return startDate;
	}
	
	public DateTime getEndDate() {
		return endDate;
	}

	public int getSize() {
		return this.size;
	}
	
	/* Set to the given value if it is bigger than 0. Else set to default value = 1. */
	private void setBatchByUnits(int batchByUnits) {
		this.batchByUnits = Math.max(1, batchByUnits);
	}
	
	private void setSize(int batchByUnits) {
		int size = this.generateBatcherSize();
		
		if (batchByUnits > 0) {
			this.size = size / batchByUnits;
			
			/* If we need one extra bin to cover a final fractional batch unit, add it here. */
			if (size % batchByUnits > 0) {
				this.size++;
			}
		} else {
			this.size = size;
		}
	}
	
	protected abstract DateTime getDateTime(Date date);
	protected abstract String getDateStringByDistance(int distance);
	protected abstract int getDistanceByDate(Date date);
	protected abstract int generateBatcherSize();
}
