package edu.iu.cns.r.utility;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.ToCaseFunction;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class RHomeFinder {
	public static final Collection<String> R_HOME_ENVIRONMENT_VARIABLES_TO_CHECK =
		Sets.newHashSet("R", "R_HOME");
	public static final Collection<String> LOWER_CASE_R_HOME_ENVIRONMENT_VARIABLES_TO_CHECK =
		Collections2.transform(
			R_HOME_ENVIRONMENT_VARIABLES_TO_CHECK, ToCaseFunction.TO_LOWER_CASE_FUNCTION);

	public static String findRHome() {
		Map<String, String> environmentVariables = System.getenv();

		for (String key : environmentVariables.keySet()) {
			String lowerCasedKey = key.toLowerCase();

			if (LOWER_CASE_R_HOME_ENVIRONMENT_VARIABLES_TO_CHECK.contains(lowerCasedKey)) {
				String rHome =
					StringUtilities.stripSurroundingQuotes(environmentVariables.get(key));
				String rHomeWithBin = String.format("%s%sbin", rHome, File.separator);

				if (isValidRHome(rHome)) {
					return rHome;
				} else if (isValidRHome(rHomeWithBin)) {
					return rHomeWithBin;
				}
			}
		}

		return null;
	}

	public static boolean isValidRHome(String path) {
		File rDirectory = new File(path);

		if (!rDirectory.exists() || !rDirectory.isDirectory()) {
			return false;
		}

		String[] rDirectoryFileNames = rDirectory.list();

		for (String rDirectoryFileName : rDirectoryFileNames) {
			/* So we've got this list of file names in this directory, but directory names are
			 * included.
			 * At least in Windows, case persists but doesn't matter.
			 * So, to accommodate both of this concerns, we loop through all of the files in this
			 * directory, making sure they're actually files, and checking them against our
			 * designated R executable file name (ignoring case).
			 */

			File rDirectoryFile = new File(rDirectoryFileName);

			if (rDirectoryFile.isDirectory()) {
				continue;
			}

			String rBaseFileName = FileUtilities.extractFileName(rDirectoryFile.getName());

			if (RProperties.R_EXECUTABLE_BASE_NAME.equalsIgnoreCase(rBaseFileName)) {
				return true;
			}
		}

		return false;
	}
}