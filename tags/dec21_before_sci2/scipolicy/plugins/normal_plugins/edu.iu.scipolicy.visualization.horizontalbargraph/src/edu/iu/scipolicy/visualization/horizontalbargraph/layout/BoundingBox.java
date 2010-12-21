package edu.iu.scipolicy.visualization.horizontalbargraph.layout;

public class BoundingBox {
	private long left;
	private long bottom;
	private long right;
	private long top;
	
	public BoundingBox(long left, long bottom, long right, long top) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}
	
	public long getLeft() {
		return this.left;
	}
	
	public long getBottom() {
		return this.bottom;
	}
	
	public long getRight() {
		return this.right;
	}
	
	public long getTop() {
		return this.top;
	}

	public long getWidth() {
		return Math.abs(getRight() - getLeft());
	}

	public long getHeight() {
		return Math.abs(getTop() - getBottom());
	}
}