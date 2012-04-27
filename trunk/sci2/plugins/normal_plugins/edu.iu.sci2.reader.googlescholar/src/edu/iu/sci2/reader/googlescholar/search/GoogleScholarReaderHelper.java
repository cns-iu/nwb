package edu.iu.sci2.reader.googlescholar.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cishell.utilities.network.DownloadHandler;
import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

/**
 * <p>
 * Google only allows API query for 1,000 times. Sadly, there is no API
 * available for Scholar. We need to be strategic with the usage.
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
 */
public final class GoogleScholarReaderHelper {

	/** The Constant GCR_SEARCH_AUTHOR_URL. */
	private static final String GCR_SEARCH_AUTHOR_URL = 
			"http://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=";

	/** The Constant USER_URL_START_INDEX. */
	private static final int USER_URL_START_INDEX = 16;

	/** The Constant USER_URL_END_INDEX. */
	private static final int USER_URL_END_INDEX = 28;

	/** Email. */
	private static final String EMAIL_INFO = "Verified email at";

	/** Cited by. */
	private static final String CITED_INFO = "Cited by";

	/** The class for the author element in the DOM. **/
	private static final String AUTHOR_ELEMENT_CLASS = "cit-dark-large-link";

	private GoogleScholarReaderHelper() {
		// Utility Class, don't instantiate.
	}

	/**
	 * 
	 * @param authorName
	 *            the author name
	 * @throws IOException
	 *             If the URL cannot be read or connected to, or the downloaded
	 *             HTML cannot be parsed.
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static Collection<AuthorRecord> searchAuthor(String authorName)
			throws IOException, InvalidUrlException, NetworkConnectionException {
		String encodedAuthorName = authorName.trim().replace(" ", "+");
		URL url = new URL(GCR_SEARCH_AUTHOR_URL + encodedAuthorName);
		InputStream iStream = connect(url);

		// Jsoup:Java HTML Parser
		Document htmldoc = Jsoup.parse(iStream, "UTF-8", url.toString());

		// get results returned for given author search

		Elements authorElements = htmldoc
				.getElementsByClass(AUTHOR_ELEMENT_CLASS);
		Collection<AuthorRecord> records = new ArrayList<AuthorRecord>();

		for (int i = 0; i < authorElements.size(); i++) {
			Element authorElement = authorElements.get(i);

			String name = getAuthorName(authorElement);
			String userId = getUserId(authorElement);

			String authorInfo = authorElement.parent().text();
			authorInfo = authorInfo.replace(name + " ", "");

			int emailIndex = authorInfo.indexOf(EMAIL_INFO);
			int citedByIndex = authorInfo.indexOf(CITED_INFO);

			String university = getUniversity(authorInfo, emailIndex);
			String email = getEmail(authorInfo, emailIndex, citedByIndex);
			String citedBy = getCitedBy(authorInfo, citedByIndex);

			AuthorRecord record = new AuthorRecord(name, userId, university,
					email, citedBy, authorName);

			records.add(record);
		}

		return records;
	}

	/**
	 * Connect to the url.
	 * 
	 * @param url
	 *            the url
	 * @param logger
	 *            the logger
	 * @return the input stream
	 * @throws IOException
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static InputStream connect(URL url) 
			throws IOException, InvalidUrlException, NetworkConnectionException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setAllowUserInteraction(false);
		connection.setDoOutput(true);
		connection.addRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		DownloadHandler.startConnect(connection);
		
		return connection.getInputStream();
	}

	/**
	 * Gets the unique authors.
	 * 
	 * @param delimiter
	 *            The delimiter for values in the {@code authorColumnName} or
	 *            {@code null} if no delimiter should be used.
	 * @return the unique authors
	 */
	public static Set<String> getUniqueAuthors(Table table,
			String authorColumnName, String delimiter, LogService logger) {

		Set<String> uniqueAuthors = new HashSet<String>();
		Iterator<?> rows = table.tuples();

		while (rows.hasNext()) {
			Tuple row = (Tuple) rows.next();

			if (row.canGetString(authorColumnName)) {
				String authorString = row.getString(authorColumnName);

				if (delimiter == null) {
					uniqueAuthors.add(authorString);
				} else {
					String escapedDelimiter = "\\" + delimiter;
					String[] authorNames = authorString.split(escapedDelimiter);
					for (String authorName : authorNames) {
						uniqueAuthors.add(authorName);
					}
				}
			}
		}

		return uniqueAuthors;
	}

	/**
	 * 
	 * @param authorElement
	 * @return Author name returned by Google Scholar
	 */
	private static String getAuthorName(Element authorElement) {
		return authorElement.text().trim();
	}

	/**
	 * 
	 * @param authorElement
	 * @return Google citation user id
	 */
	private static String getUserId(Element authorElement) {

		String userIdUrL = authorElement.attr("href");
		String userId = userIdUrL.substring(
				userIdUrL.indexOf("/citations?user=") + USER_URL_START_INDEX,
				userIdUrL.indexOf("/citations?user=") + USER_URL_END_INDEX);

		return userId;

	}

	/**
	 * Gets the email.
	 * 
	 * @param authorInfo
	 *            the author info
	 * @param emailIndex
	 *            the email index
	 * @param citedByIndex
	 *            the cited by index
	 * @return the email
	 */
	private static String getEmail(String authorInfo, int emailIndex,
			int citedByIndex) {

		String email = null;

		if (emailIndex != -1 && citedByIndex != -1) {
			email = authorInfo.substring(emailIndex + EMAIL_INFO.length(),
					citedByIndex).trim();
		} else if (emailIndex != -1 && citedByIndex == -1) {
			email = authorInfo.substring(emailIndex + EMAIL_INFO.length(),
					authorInfo.length()).trim();
		}

		return email;

	}

	/**
	 * Gets the cited by.
	 * 
	 * @param authorInfo
	 *            the author info
	 * @param citedByIndex
	 *            the cited by index
	 * @return the cited by
	 */
	private static String getCitedBy(String authorInfo, int citedByIndex) {
		String citedBy = null;
		if (citedByIndex != -1) {
			citedBy = authorInfo.substring(citedByIndex + CITED_INFO.length(),
					authorInfo.length()).trim();
		}
		return citedBy;
	}

	/**
	 * Gets the university.
	 * 
	 * @param authorInfo
	 *            the author info
	 * @param emailIndex
	 *            the email index
	 * @return the university
	 */
	private static String getUniversity(String authorInfo, int emailIndex) {
		if (emailIndex == -1) {
			return authorInfo;
		} else {
			return authorInfo.substring(0, emailIndex).trim();
		}
	}
}
