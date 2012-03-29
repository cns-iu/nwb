package edu.iu.nwb.analysis.burst.batcher;

import java.util.Date;
import org.joda.time.DateTime;

public class BatchByHour extends AbstractBatcher {
	
	public BatchByHour(Date startDate, Date endDate, int batchByUnits) {
		super(startDate, endDate, batchByUnits);
	}

	@Override
	protected DateTime getDateTime(Date date) {
		DateTime datetime = new DateTime(date);
		return new DateTime(
				datetime.getYear(), 
				datetime.getMonthOfYear(), 
				datetime.getDayOfMonth(), 
				datetime.getHourOfDay(), 0, 0, 0);
	}
	
	@Override
	protected String getDateStringByDistance(int distance) {
		return DateTimeUtils.addHours(this.getStartDate(), distance).toDate().toString();
	}

	@Override
	protected int getDistanceByDate(Date date) {
		return DateTimeUtils.getHoursDifferent(this.getStartDate(), new DateTime(date));
	}
	
	@Override
	protected int generateBatcherSize() {
		return DateTimeUtils.getHoursDifferent(this.getStartDate(), this.getEndDate()) + 1;
	}
}
