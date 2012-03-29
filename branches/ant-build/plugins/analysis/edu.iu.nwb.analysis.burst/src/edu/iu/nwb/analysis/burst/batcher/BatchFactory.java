package edu.iu.nwb.analysis.burst.batcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum BatchFactory {
	/* User-friendly names must be unique. */
	YEARS("Years") {
		public Batcher getBatcher(Date startDate, Date endDate, int batchLength) {
			return new BatchByYear(startDate, endDate, batchLength);
		}
	},
	MONTHS("Months") {
		public Batcher getBatcher(Date startDate, Date endDate, int batchLength) {
			return new BatchByMonth(startDate, endDate, batchLength);
		}
	},
	DAYS("Days") {
		public Batcher getBatcher(Date startDate, Date endDate, int batchLength) {
			return new BatchByDay(startDate, endDate, batchLength);
		}
	},
	HOURS("Hours") {
		public Batcher getBatcher(Date startDate, Date endDate, int batchLength) {
			return new BatchByHour(startDate, endDate, batchLength);
		}
	},
	MINUTES("Minutes") {
		public Batcher getBatcher(Date startDate, Date endDate, int batchLength) {
			return new BatchByMinute(startDate, endDate, batchLength);
		}
	};

	public static final Map<String, BatchFactory> NAME_TO_BATCH_FACTORY =
		new HashMap<String, BatchFactory>();
	static {
		for (BatchFactory batchFactory : BatchFactory.values()) {
			NAME_TO_BATCH_FACTORY.put(batchFactory.getUserFriendlyName(), batchFactory);
		}
	}
	
	private String userFriendlyName;
	
	BatchFactory(String userFriendlyName) {
		this.userFriendlyName = userFriendlyName;
	}
	
	public static BatchFactory getBatchFactory(String userFriendlyName) {
		return NAME_TO_BATCH_FACTORY.get(userFriendlyName);
	}
	
	public static String[] userFriendlyNames() {
		BatchFactory[] values = BatchFactory.values();
		String[] stringValues = new String[values.length];
		
		int i = 0;
		for (BatchFactory value : values) {
			stringValues[i++] = value.getUserFriendlyName();
		}
		
		return stringValues;
	}
	
	public abstract Batcher getBatcher(Date startDate, Date endDate, int batchLength);
	
	public String getUserFriendlyName() {
		return userFriendlyName;
	}
}
