package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

public class RecordCollection {
	private Collection<Record> records = new HashSet<Record>();
	private DateTime minimumStartDate = null;
	private DateTime maximumEndDate = null;
	
	public RecordCollection() {
	}
	
	public Collection<Record> getRecords() {
		return this.records;
	}
	
	public DateTime getMinimumStartDate() {
		return this.minimumStartDate;
	}
	
	public DateTime getMaximumEndDate() {
		return this.maximumEndDate;
	}
	
	public double calculateMinimumAmountPerUnitOfTime(
			UnitOfTime unitOfTime, int minimumUnitOfTime) {
		double minimumAmountPerUnitOfTime = 0.0;
		
		for (Record record : this.records) {
			double amount = record.getAmount();
			
			if (amount <= 0.0) {
				continue;
			}
			
			int timeBetween = Math.max(
				unitOfTime.timeBetween(
					record.getStartDate(), record.getEndDate()),
				minimumUnitOfTime);
			double recordMinimumAmountPerUnitOfTime = amount / timeBetween;
			
			if ((minimumAmountPerUnitOfTime == 0.0) ||
					(recordMinimumAmountPerUnitOfTime <
						minimumAmountPerUnitOfTime)) {
				minimumAmountPerUnitOfTime = recordMinimumAmountPerUnitOfTime;
			}
		}
		
		return minimumAmountPerUnitOfTime;
	}
	
	public double calculateTotalAmountPerUnitOfTime(
			UnitOfTime unitOfTime, int minimumUnitOfTime) {
		double totalAmount = 0.0;
		
		for (Record record : this.records) {
			totalAmount += record.getAmount();
		}
		
		int timeBetween = Math.max(
			unitOfTime.timeBetween(getMinimumStartDate(), getMaximumEndDate()),
			minimumUnitOfTime);
		
		return totalAmount / timeBetween;
	}
	
	public void addNormalRecord(
			String label,
			DateTime startDate,
			DateTime endDate,
			double amount) {
		Record record = new NormalRecord(label, startDate, endDate, amount);
		
		addRecord(record);
	}
	
	public void addRecordWithNoStartDate(
			String label, final DateTime endDate, double amount) {
		Record record = new AbstractRecord(label, amount) {
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumStartDate();
			}
			
			public DateTime getEndDate() {
				return endDate;
			}
		};
		
		addRecord(record);
	}
	
	public void addRecordWithNoEndDate(
			String label, final DateTime startDate, double amount) {
		Record record = new AbstractRecord(label, amount) {
			public DateTime getStartDate() {
				return startDate;
			}
			
			public DateTime getEndDate() {
				return RecordCollection.this.getMaximumEndDate();
			}
		};
		
		addRecord(record);
	}
	
	public void addRecordWithNoDates(String label, double amount) {
		Record record = new AbstractRecord(label, amount) {
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumStartDate();
			}
			
			public DateTime getEndDate() {
				return RecordCollection.this.getMaximumEndDate();
			}
		};
		
		addRecord(record);
	}
	
	public SortedSet<Record> getSortedRecords() {
		return new TreeSet<Record>(getRecords());
	}
	
	private void addRecord(Record newRecord) {
		DateTime newRecordStartDate = newRecord.getStartDate();
		DateTime minimumStartDate = getMinimumStartDate();
		DateTime maximumEndDate = getMaximumEndDate();
		
		if ((minimumStartDate == null) ||
				(newRecordStartDate.compareTo(minimumStartDate) < 0)) {
			setMinimumStartDate(newRecordStartDate);
		}
		
		DateTime newRecordEndDate = newRecord.getEndDate();
		
		if ((maximumEndDate == null) ||
				(newRecordEndDate.compareTo(getMaximumEndDate()) > 0)) {
			setMaximumEndDate(newRecordEndDate);
		}
		
		this.records.add(newRecord);
	}
	
	private void setMinimumStartDate(DateTime minimumStartDate) {
		this.minimumStartDate = minimumStartDate;
	}
	
	private void setMaximumEndDate(DateTime maximumEndDate) {
		this.maximumEndDate = maximumEndDate;
	}
	
	private static abstract class AbstractRecord implements Record {
		private String label;
		private double amount;
		
		public AbstractRecord(String label, double amount) {
			this.label = label;
			this.amount = amount;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public double getAmount() {
			return this.amount;
		}
		
		public int compareTo(Record otherRecord) {
			return getStartDate().compareTo(otherRecord.getStartDate());
		}
	};
	
	private static class NormalRecord extends AbstractRecord {
		private DateTime startDate;
		private DateTime endDate;
		
		public NormalRecord(
				String label,
				DateTime startDate,
				DateTime endDate,
				double amount) {
			super(label, amount);
			this.startDate = startDate;
			this.endDate = endDate;
		}

		public DateTime getStartDate() {
			return this.startDate;
		}
	
		public DateTime getEndDate() {
			return this.endDate;
		}
	}
}