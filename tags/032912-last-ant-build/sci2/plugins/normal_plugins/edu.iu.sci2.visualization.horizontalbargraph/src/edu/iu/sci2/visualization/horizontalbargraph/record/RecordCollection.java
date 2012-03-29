package edu.iu.sci2.visualization.horizontalbargraph.record;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

import edu.iu.sci2.visualization.horizontalbargraph.ScalingFunction;
import edu.iu.sci2.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.sci2.visualization.horizontalbargraph.utility.PreprocessedRecordInformation;

public class RecordCollection {
	private Collection<Record> records = new HashSet<Record>();
	private DateTime minimumDate = null;
	private DateTime maximumDate = null;
	private PreprocessedRecordInformation recordInformation;
	private ScalingFunction scalingFunction;
	
	public RecordCollection(
			PreprocessedRecordInformation recordInformation, ScalingFunction scalingFunction) {
		this.recordInformation = recordInformation;
		this.scalingFunction = scalingFunction;
	}
	
	public Collection<Record> getRecords() {
		return this.records;
	}
	
	public DateTime getMinimumDate() {
		return this.minimumDate;
	}
	
	public DateTime getMaximumDate() {
		return this.maximumDate;
	}

	public double calculateMinimumAmountPerUnitOfTime(UnitOfTime unitOfTime) {
		double minimumAmountPerUnitOfTime = Double.MAX_VALUE;

		for (Record record : this.records) {
			double amount = record.getAmount();

			if (amount == 0.0) {
				continue;
			}
			
			double recordMinimumAmountPerUnitOfTime =
				record.getAmountPerUnitOfTime();

			if (recordMinimumAmountPerUnitOfTime < minimumAmountPerUnitOfTime) {
				minimumAmountPerUnitOfTime = recordMinimumAmountPerUnitOfTime;
			}
		}
		
		return minimumAmountPerUnitOfTime;
	}
	
	public void addNormalRecord(
			String label, String colorizedBy, DateTime startDate, DateTime endDate, double originalAmount) {
		DateTime fixedEndDate = fixEndDate(startDate, endDate);
		double fixedAmount = fixAmount(originalAmount);
		boolean hasInvalidAmount = hasInvalidAmount(originalAmount);
		Record record =
			new NormalRecord(label, colorizedBy, startDate, fixedEndDate, fixedAmount, hasInvalidAmount);
		
		addRecord(record);
	}
	
	public void addRecordWithNoStartDate(
			String label, String colorizedBy, final DateTime endDate, double originalAmount) {
		double fixedAmount = fixAmount(originalAmount);
		boolean hasInvalidAmount = hasInvalidAmount(originalAmount);
		Record record = new AbstractRecord(label, colorizedBy, fixedAmount, hasInvalidAmount) {
			public boolean hasStartDate() {
				return false;
			}
			
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumDate();
			}
			
			public boolean hasEndDate() {
				return true;
			}
			
			public DateTime getEndDate() {
				DateTime fixedEndDate = fixEndDate(
					RecordCollection.this.getMinimumDate(), endDate);

				return fixedEndDate;
			}
		};
		
		addRecord(record);
	}
	
	public void addRecordWithNoEndDate(
			String label, String colorizedBy, final DateTime startDate, double originalAmount) {
		double fixedAmount = fixAmount(originalAmount);
		boolean hasInvalidAmount = hasInvalidAmount(originalAmount);
		Record record = new AbstractRecord(label, colorizedBy, fixedAmount, hasInvalidAmount) {
			public boolean hasStartDate() {
				return true;
			}
			
			public DateTime getStartDate() {
				return startDate;
			}
			
			public boolean hasEndDate() {
				return false;
			}
			
			public DateTime getEndDate() {
				DateTime fixedEndDate = fixEndDate(
					startDate, RecordCollection.this.getMaximumDate());

				return fixedEndDate;
			}
		};
		
		addRecord(record);
	}
	
	public void addRecordWithNoDates(String label, String colorizedBy, double originalAmount) {
		double fixedAmount = fixAmount(originalAmount);
		boolean hasInvalidAmount = hasInvalidAmount(originalAmount);
		Record record = new AbstractRecord(label, colorizedBy, fixedAmount, hasInvalidAmount) {
			public boolean hasStartDate() {
				return false;
			}
			
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumDate();
			}
			
			public boolean hasEndDate() {
				return false;
			}
			
			public DateTime getEndDate() {
				DateTime fixedEndDate = fixEndDate(
					RecordCollection.this.getMinimumDate(),
					RecordCollection.this.getMaximumDate());

				return fixedEndDate;
			}
		};
		
		addRecord(record);
	}
	
	public SortedSet<Record> getSortedRecords() {
		return new TreeSet<Record>(getRecords());
	}
	
	private void addRecord(Record newRecord) {
		DateTime newRecordStartDate = newRecord.getStartDate();
		
		if (newRecordStartDate != null) {
			DateTime minimumDate = getMinimumDate();

			if ((minimumDate == null) || (newRecordStartDate.compareTo(minimumDate) < 0)) {
				setMinimumDate(newRecordStartDate);
			}

			DateTime maximumDate = getMaximumDate();

			if ((maximumDate == null) || (newRecordStartDate.compareTo(maximumDate) > 0)) {
				setMaximumDate(newRecordStartDate);
			}
		}
		
		DateTime newRecordEndDate = newRecord.getEndDate();
		
		if (newRecordEndDate != null) {
			DateTime maximumDate = getMaximumDate();
			
			if ((maximumDate == null) || (newRecordEndDate.compareTo(maximumDate) > 0)) {
				setMaximumDate(newRecordEndDate);
			}

			DateTime minimumDate = getMinimumDate();

			if ((minimumDate == null) || (newRecordEndDate.compareTo(minimumDate) < 0)) {
				setMinimumDate(newRecordEndDate);
			}
		}
		
		this.records.add(newRecord);
	}
	
	private void setMinimumDate(DateTime minimumDate) {
		DateTime january1stThisYear = minimumDate.withMonthOfYear(1).withDayOfMonth(1);
		this.minimumDate = january1stThisYear;
	}
	
	private void setMaximumDate(DateTime maximumDate) {
		DateTime january1stNextYear =
			maximumDate.plusYears(1).withMonthOfYear(1).withDayOfMonth(1);
		this.maximumDate = january1stNextYear;
	}

	private DateTime fixEndDate(DateTime startDate, DateTime endDate) {
		if ((startDate != null) && startDate.equals(endDate)) {
			return endDate.plusYears(1);
		}

		return endDate;
	}

	private double fixAmount(double amount) {
		if (!Double.isInfinite(amount) && !Double.isNaN(amount)) {
//			return amount;
			return this.scalingFunction.scale(amount);
		} else {
//			return this.recordInformation.getMaximumAmountFound();
			return this.scalingFunction.scale(this.recordInformation.getMaximumAmountFound());
		}
	}

	private boolean hasInvalidAmount(double amount) {
		return Double.isInfinite(amount) || Double.isNaN(amount);
	}
	
	private class NormalRecord extends AbstractRecord {
		private DateTime startDate;
		private DateTime endDate;
		
		public NormalRecord(
				String label,
				String colorizedBy,
				DateTime startDate,
				DateTime endDate,
				double amount,
				boolean hasInvalidAmount) {
			super(label, colorizedBy, amount, hasInvalidAmount);
			this.startDate = startDate;
			this.endDate = endDate;
		}
		
		public boolean hasStartDate() {
			return true;
		}

		public DateTime getStartDate() {
			return this.startDate;
		}
		
		public boolean hasEndDate() {
			return true;
		}
	
		public DateTime getEndDate() {
			return this.endDate;
		}
	}
}