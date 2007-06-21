package tester.graphcomparison;

public class ComparisonResult {

	private boolean succeeded;
	private String explanation;
	public ComparisonResult(boolean succeeded) {
		this(succeeded, "");
	}
	
	public ComparisonResult(boolean succeeded, String explanation) {
		this.succeeded = succeeded;
		this.explanation = explanation;
	}
	
	public boolean comparisonSucceeded() {
		return succeeded;
	}
	
	public String explanation() {
		return explanation;
	}
	
	public String toString() {
		if (comparisonSucceeded()) {
			return "Success!";
		} else {
			return "Failure: " + explanation;
		}
	}
}
