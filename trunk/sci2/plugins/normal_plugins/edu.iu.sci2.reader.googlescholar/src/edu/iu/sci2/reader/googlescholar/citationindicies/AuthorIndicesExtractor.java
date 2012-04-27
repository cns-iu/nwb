package edu.iu.sci2.reader.googlescholar.citationindicies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;

import edu.iu.sci2.reader.googlescholar.search.GoogleScholarReaderHelper;

// TODO: Auto-generated Javadoc
/**
 * ExtractAuthorIndices.
 * 
 * Web reader algorithm that accepts a table containing a column of author names
 * and returns a table with added columns that give the citation indices tied to
 * that author on their Google Scholar author page
 * 
 * Based on the spec at http://scholar.google.com/robots.txt clawling on google
 * scholar only allowed for URL with 'citations?user=' and
 * '/citations?view_op=new_profile' Also read Citation Export at
 * http://scholar.google.com/scholar/help.html For more information, see
 * http://scholar.google.com/intl/en/scholar/inclusion.html
 * 
 * Author: P632
 * 
 * Code Review: P632
 */
public final class AuthorIndicesExtractor {

	/** The Constant GCR_EXPORT_INDICES_URL. */
	private static final String GCR_EXPORT_INDICES_URL_PREFIX = 
			"http://www.scholar.google.com/citations?user=";

	private static final String GCR_EXPORT_INDICES_URL_POSTFIX = "&hl=en";
	/** The Constant AUTHOR_NAME_COLUMN. */

	/** The Constant INDICES_COLUMN_SIZE. */
	private static final int INDICES_COLUMN_SIZE = 3;

	private AuthorIndicesExtractor() {
		// Utility class; don't instantiate.
	}

	/**
	 * Gets the indices.
	 * 
	 * @param userId
	 *            the userId
	 * @param logger
	 *            the logger
	 * @return the indices
	 * @throws BadLocationException
	 * @throws IOException
	 */
	private static IndicesRecord getIndices(InputStream iStream, String userId)
			throws BadLocationException, IOException {

		List<String[]> indices = new ArrayList<String[]>();

		HTMLEditorKit htmlEditorkit = new HTMLEditorKit();
		HTMLDocument htmlDocument = (HTMLDocument) htmlEditorkit
				.createDefaultDocument();
		htmlDocument.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				iStream));

		htmlEditorkit.read(bReader, htmlDocument, 0);

		// Get an iterator for all HTML tags.
		ElementIterator iterator = new ElementIterator(htmlDocument);
		Element element;

		String[] indiceRow = null;
		while ((element = iterator.next()) != null) {
			// Indices are included inside a table with ID=stats in the HTML
			// output
			if (element.getName().equals("table")) {
				String id = (String) element.getAttributes().getAttribute(
						HTML.Attribute.ID);
				if (id != null && id.equals("stats")) {
					for (int i = 1; i < element.getElementCount(); i++) {
						Element trElement = element.getElement(i);
						int index = 1;
						for (int j = 0; j < trElement.getElementCount(); j++) {
							Element tdElement = trElement.getElement(j);
							String tdClass = (String) tdElement.getAttributes()
									.getAttribute(HTML.Attribute.CLASS);

							if (tdClass != null
									&& tdClass.equals("cit-caption")) {
								String rowName = htmlDocument.getText(
										tdElement.getStartOffset(),
										tdElement.getEndOffset()
												- tdElement.getStartOffset());
								if (!rowName.equals("")) {
									indiceRow = new String[INDICES_COLUMN_SIZE];
									indiceRow[0] = rowName.trim();
								}
							} else if (tdClass != null
									&& tdClass
											.equals("cit-borderleft cit-data")) {
								String value = htmlDocument.getText(
										tdElement.getStartOffset(),
										tdElement.getEndOffset()
												- tdElement.getStartOffset());

								if (indiceRow != null && index <= 2) {
									indiceRow[index++] = value.trim();
								}
							}
						}

						indices.add(indiceRow);
					}
				}
			}
		}

		return createIndicesRecord(userId, indices);

	}

	private static IndicesRecord createIndicesRecord(String userId,
			List<String[]> indices) {
		String citations = null;
		String citationsSince2007 = null;
		String hIndex = null;
		String hIndexSince2007 = null;
		String i10Index = null;
		String i10IndexSince2007 = null;

		for (String[] indiceRow : indices) {

			System.out.println(indiceRow[0]);
			// outputTable.set(index, AUTHOR_NAME_COLUMN_INDEX,
			// resultAuthorName);
			if (indiceRow[0].equals("Citations")) {
				citations = indiceRow[1];
				citationsSince2007 = indiceRow[2];
			} else if (indiceRow[0].equals("h-index")) {
				hIndex = indiceRow[1];
				hIndexSince2007 = indiceRow[2];
			} else if (indiceRow[0].equals("i10-index")) {
				i10Index = indiceRow[1];
				i10IndexSince2007 = indiceRow[2];
			}
		}

		return new IndicesRecord(userId, citations, citationsSince2007, hIndex,
				hIndexSince2007, i10Index, i10IndexSince2007);
	}

	/**
	 * Gets the author indices.
	 * 
	 * @param uniqueUserIds
	 *            the unique authors
	 * @param outputTable
	 *            the output table
	 * @param mergeTable
	 *            the merge table
	 * @param logger
	 *            the logger
	 * @return the author indices table
	 * @throws IndexExtractionException
	 *             If the Author Indicies cannot be parsed.
	 * @throws IOException 
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static IndicesRecord getAuthorIndices(String userId)
			throws IndexExtractionException, IOException, InvalidUrlException,
			NetworkConnectionException {

		/* Download the Google author profile page */
		try {
			InputStream istream = downloadIndicePage(userId.trim());
			return AuthorIndicesExtractor.getIndices(istream, userId);
		} catch (BadLocationException e) {
			throw new IndexExtractionException(
					"The author indicies could not be extracted.", e);
		}
	}

	private static InputStream downloadIndicePage(String userId)
			throws IOException, InvalidUrlException, NetworkConnectionException {
		URL url = new URL(GCR_EXPORT_INDICES_URL_PREFIX + userId
				+ GCR_EXPORT_INDICES_URL_POSTFIX);


		return GoogleScholarReaderHelper.connect(url);
	}

	public static class IndexExtractionException extends Exception {
		private static final long serialVersionUID = 8533261728213137711L;

		public IndexExtractionException(String message, Throwable reason) {
			super(message, reason);
		}
	}
}