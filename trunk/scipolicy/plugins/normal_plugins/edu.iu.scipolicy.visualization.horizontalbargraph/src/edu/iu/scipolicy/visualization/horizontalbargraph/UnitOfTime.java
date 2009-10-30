package edu.iu.scipolicy.visualization.horizontalbargraph;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

public enum UnitOfTime {
	DAYS {
		public int timeBetween(DateTime startDate, DateTime endDate) {
			return Days.daysBetween(startDate, endDate).getDays();
		}
	},
	MONTHS {
		public int timeBetween(DateTime startDate, DateTime endDate) {
			return Months.monthsBetween(startDate, endDate).getMonths();
		}
	},
	YEARS {
		public int timeBetween(DateTime startDate, DateTime endDate) {
			return Years.yearsBetween(startDate, endDate).getYears();
		}
	};
	
	public abstract int timeBetween(DateTime startDate, DateTime endDate);
}