package edu.iu.nwb.analysis.burst.batcher;

import java.util.Date;
import org.joda.time.DateTime;

public class BatchByYear extends AbstractBatcher {
	
	public BatchByYear(Date startDate, Date endDate, int batchByUnits) {
		super(startDate, endDate, batchByUnits);
	}

	@Override
	protected DateTime getDateTime(Date date) {
		DateTime datetime = new DateTime(date);
		return new DateTime(datetime.getYear(), 1, 1, 0, 0, 0, 0);
	}
	
	@Override
	protected String getDateStringByDistance(int distance) {
		return String.valueOf(DateTimeUtils.addYears(this.getStartDate(), distance).getYear());
	}

	@Override
	protected int getDistanceByDate(Date date) {
		return DateTimeUtils.getYearsDifferent(this.getStartDate(), new DateTime(date));
	}
	
	@Override
	protected int generateBatcherSize() {
		return DateTimeUtils.getYearsDifferent(this.getStartDate(), this.getEndDate()) + 1;
	}
}
