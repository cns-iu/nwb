package edu.iu.epic.visualization.linegraph.model;


/**
 * Objects of this class will provide information required by the iterator. 
 * @author cdtank
 */
public class TimestepBounds {
	
	private Integer lowerbound;
	private Integer upperbound;
	
	public TimestepBounds(Integer lowerbound, 
						  Integer upperbound) {
		
		this.lowerbound = lowerbound == null 
							? 0 
							: lowerbound;
		this.upperbound = upperbound == null 
							? Integer.MAX_VALUE 
							: upperbound;
	}

	public TimestepBounds() {
		this(null, null);
	}

	public Integer getLowerbound() {
		return lowerbound;
	}

	public Integer getUpperbound() {
		return upperbound;
	}

	public void setLowerbound(Integer lowerbound) {
		this.lowerbound = lowerbound;
	}

	public void setUpperbound(Integer upperbound) {
		this.upperbound = upperbound;
	}

}
