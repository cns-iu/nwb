package edu.iu.sci2.reader.googlescholar.search;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

/**
 * <p>
 * Web reader algorithm that accepts a table containing a column of author names
 * and returns a table with added columns that give the citation indices tied to
 * that author on their Google Scholar author page.
 * </p>
 * 
 * <p>
 * Based on the <a href="http://scholar.google.com/robots.txt">spec</a> for
 * crawling on google scholar only allowed for URL with 'citations?user=' and
 * '/citations?view_op=new_profile'
 * </p>
 * 
 * <p>
 * Also read <a href="http://scholar.google.com/scholar/help.html">Citation
 * Export</a>. For more information, see <a
 * href="http://scholar.google.com/intl/en/scholar/inclusion.html"
 * >http://scholar.google.com/intl/en/scholar/inclusion.html</a>.
 * </p>
 * 
 * @author P632
 */
public class AuthorSearch {
	private int countSingleAuthorFound = 0;
	private int countMultipleAuthorFound = 0;
	private int countAuthorNotFound = 0;
	private Table authorInformationTable = null;

	/**
	 * 
	 * @param uniqueAuthors A list of <b>unique</b> author names.
	 * @param logger
	 */
	public AuthorSearch(Set<String> uniqueAuthors, LogService logger) {
		this.authorInformationTable = getAuthorInformation(uniqueAuthors,
				logger);
	}

	/**
	 * Download the author information and put it into a table.
	 */
	private Table getAuthorInformation(Set<String> uniqueAuthors,
			LogService logger) {

		AuthorRecordMergeTable mergeTable = new AuthorRecordMergeTable();

		for (String authorName : uniqueAuthors) {

			Collection<AuthorRecord> resultAuthorList = searchMatchedAuthors(authorName);

			if (resultAuthorList.isEmpty()) {
				logger.log(LogService.LOG_INFO, "Author '" + authorName + "' was not found.");
				countAuthorNotFound++;
			} else {
				if (resultAuthorList.size() == 1) {
					countSingleAuthorFound++;
				} else {
					countMultipleAuthorFound++;
				}

				/* Add records into merge table */
				for (AuthorRecord record : resultAuthorList) {
					mergeTable.addAuthorRecord(authorName, record);
				}
			}
		}
		return mergeTable.getTable();
	}

	/**
	 * @return A list of {@link AuthorRecord}s found from the {@code authorName}
	 *         or an empty list if an error occurs.
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	private static Collection<AuthorRecord> searchMatchedAuthors(
			String authorName) {
		try {
			return GoogleScholarReaderHelper.searchAuthor(authorName);
		} catch (IOException e) {
			return Collections.emptyList();
		} catch (InvalidUrlException e) {
			return Collections.emptyList();
		} catch (NetworkConnectionException e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Return the author information in a {@link Table}.
	 */
	public Table getAuthorInformationTable() {
		return this.authorInformationTable;
	}

	/**
	 * Gets the count single author found.
	 * 
	 * @return the count single author found
	 */
	public int getCountSingleAuthorFound() {
		return countSingleAuthorFound;
	}

	/**
	 * Gets the count multiple author found.
	 * 
	 * @return the count multiple author found
	 */
	public int getCountMultipleAuthorFound() {
		return countMultipleAuthorFound;
	}

	/**
	 * Gets the count author not found.
	 * 
	 * @return the count author not found
	 */
	public int getCountAuthorNotFound() {
		return countAuthorNotFound;
	}
}