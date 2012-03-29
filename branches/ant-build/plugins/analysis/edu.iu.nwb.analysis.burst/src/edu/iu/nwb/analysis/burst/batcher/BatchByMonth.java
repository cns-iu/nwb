package edu.iu.nwb.analysis.burst.batcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;

public class BatchByMonth extends AbstractBatcher {
	public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM, yyyy");
	
	public BatchByMonth(Date startDate, Date endDate, int batchByUnits) {
		super(startDate, endDate, batchByUnits);
	}
	
	@Override
	protected DateTime getDateTime(Date date) {
		DateTime datetime = new DateTime(date);
		return new DateTime(
				datetime.getYear(), 
				datetime.getMonthOfYear(), 1, 0, 0, 0, 0);
	}
	
	@Override
	protected String getDateStringByDistance(int distance) {
		return MONTH_FORMAT.format(
				DateTimeUtils.addMonths(this.getStartDate(), distance).toDate());
	}

	@Override
	protected int getDistanceByDate(Date date) {
		return DateTimeUtils.getMonthsDifferent(this.getStartDate(), new DateTime(date));
	}

	@Override
	protected int generateBatcherSize() {
		return DateTimeUtils.getMonthsDifferent(this.getStartDate(), this.getEndDate()) + 1;
	}
}
