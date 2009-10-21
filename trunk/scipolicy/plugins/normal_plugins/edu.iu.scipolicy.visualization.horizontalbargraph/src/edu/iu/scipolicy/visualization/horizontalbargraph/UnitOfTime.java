package edu.iu.scipolicy.visualization.horizontalbargraph;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

public enum UnitOfTime {
	DAYS {
		int timeBetween(DateTime startDate, DateTime endDate) {
			return Days.daysBetween(startDate, endDate).getDays();
		}
	},
	MONTHS {
		int timeBetween(DateTime startDate, DateTime endDate) {
			return Months.monthsBetween(startDate, endDate).getMonths();
		}
	},
	YEARS {
		int timeBetween(DateTime startDate, DateTime endDate) {
			return Years.yearsBetween(startDate, endDate).getYears();
		}
	};
	
	abstract int timeBetween(DateTime startDate, DateTime endDate);
}