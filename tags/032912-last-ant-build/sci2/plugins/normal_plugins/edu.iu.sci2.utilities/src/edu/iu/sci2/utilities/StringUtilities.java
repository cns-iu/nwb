package edu.iu.sci2.utilities;

public class StringUtilities {
	public static String implodeStringArray(String[] stringArray, String separator) {
		final int stringArrayLength = stringArray.length;
		StringBuilder workingResultString = new StringBuilder();

		for (int ii = 0; ii < stringArrayLength; ii++) {
			workingResultString.append(stringArray[ii]);
			if (ii != stringArrayLength - 1) {
				workingResultString.append(separator);
			}
		}
		
		return workingResultString.toString();
	}
}
