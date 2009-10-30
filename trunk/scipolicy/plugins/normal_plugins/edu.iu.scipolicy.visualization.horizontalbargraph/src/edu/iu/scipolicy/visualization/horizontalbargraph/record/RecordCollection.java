package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;

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
	
	//
	public double calculateMinimumAmountPerUnitOfTime(
			UnitOfTime unitOfTime, int minimumUnitOfTime) {
		double minimumAmountPerUnitOfTime = 0.0; // Double.MAX_VALUE
		
		for (Record record : this.records) {
			double amount = record.getAmount();
			
			//TODO: negatives should have been taken care of way earlier
			if (amount <= 0.0) {
				continue;
			}
			
			/*int timeBetween = Math.max(
				unitOfTime.timeBetween(
					record.getStartDate(), record.getEndDate()),
				minimumUnitOfTime);
			double recordMinimumAmountPerUnitOfTime = amount / timeBetween;*/
			
			double recordMinimumAmountPerUnitOfTime =
				record.calculateAmountPerUnitOfTime(
					unitOfTime, minimumUnitOfTime);
			
			if ((minimumAmountPerUnitOfTime == 0.0) || //TODO: no need to check for 0.0 if we use Double.MAX_VALUE
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
			public boolean hasStartDate() {
				return false;
			}
			
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumStartDate();
			}
			
			public boolean hasEndDate() {
				return true;
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
				return RecordCollection.this.getMaximumEndDate();
			}
		};
		
		addRecord(record);
	}
	
	public void addRecordWithNoDates(String label, double amount) {
		Record record = new AbstractRecord(label, amount) {
			public boolean hasStartDate() {
				return false;
			}
			
			public DateTime getStartDate() {
				return RecordCollection.this.getMinimumStartDate();
			}
			
			public boolean hasEndDate() {
				return false;
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
		
		//TODO: setMinimumStartDate() should be removed and getMinimumStartDate not used internally; access fields of ourself directly unless there's a good reason not to.
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
	
	private class NormalRecord extends AbstractRecord {
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