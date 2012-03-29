package edu.iu.nwb.analysis.burst.batcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;

/*
 * All long type batches will be cast to int since it is
 * not possible to handle long size of batches. 
 */
public class BatchByDay extends AbstractBatcher {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
	
	public BatchByDay(Date startDate, Date endDate, int batchByUnits) {
		super(startDate, endDate, batchByUnits);
	}
	
	@Override
	protected DateTime getDateTime(Date date) {
		DateTime datetime = new DateTime(date);
		return new DateTime(
				datetime.getYear(), 
				datetime.getMonthOfYear(), 
				datetime.getDayOfMonth(), 0, 0, 0, 0);
	}
	

	@Override
	protected String getDateStringByDistance(int distance) {
		return DATE_FORMAT.format(
				DateTimeUtils.addDays(this.getStartDate(), distance).toDate());
	}

	@Override
	protected int getDistanceByDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return DateTimeUtils.getDaysDifferent(this.getStartDate(), new DateTime(date));
	}
	
	@Override
	protected int generateBatcherSize() {
		return DateTimeUtils.getDaysDifferent(this.getStartDate(), this.getEndDate()) + 1;
	}
}
