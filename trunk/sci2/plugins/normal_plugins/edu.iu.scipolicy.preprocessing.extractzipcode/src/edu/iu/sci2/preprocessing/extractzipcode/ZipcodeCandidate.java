package edu.iu.sci2.preprocessing.extractzipcode;


/**
 * ZipcodeCandidate Value Object.
 * @author cdtank
 *
 */
public class ZipcodeCandidate {

	private String zipCodeCandidate;
	private int startPosition;
	private int endPosition;
	private int length;
	private String originalAddress;
	
	public ZipcodeCandidate(String originalAddress, String zipCodeCandidate,
			int startPosition, int endPosition, int length) {
		this.zipCodeCandidate = zipCodeCandidate;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.length = length;
		this.originalAddress = originalAddress;
	}

	public String getZipCodeCandidate() {
		return zipCodeCandidate;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public int getLength() {
		return length;
	}

	public String getOriginalAddress() {
		return originalAddress;
	}

	public void setZipCodeCandidate(String zipCodeCandidate) {
		this.zipCodeCandidate = zipCodeCandidate;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setOriginalAddress(String originalAddress) {
		this.originalAddress = originalAddress;
	}
}