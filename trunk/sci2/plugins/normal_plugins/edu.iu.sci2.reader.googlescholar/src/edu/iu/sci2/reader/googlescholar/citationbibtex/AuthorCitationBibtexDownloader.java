package edu.iu.sci2.reader.googlescholar.citationbibtex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cishell.utilities.network.DownloadHandler;
import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;


/**
 * Accepts one author name as a parameter and returns citations in a Bibtex file.
 * 
 * Based on the spec at http://scholar.google.com/robots.txt clawling on google 
 * scholar only allowed for URL with 'citations?user=' and '/citations?view_op=new_profile'
 * Also read Citation Export at http://scholar.google.com/scholar/help.html
 * For more information, see http://scholar.google.com/intl/en/scholar/inclusion.html
 * 
 * Author: P632
 * 
 * Code Review: P632
 */
public final class AuthorCitationBibtexDownloader {

	/** The Constant BUFFER_SIZE. */
	private static final int BUFFER_SIZE = 1024;

	/** The Constant GCR_EXPORT_CITATION_URL. */
	private static final String GCR_EXPORT_CITATION_URL_PREFIX = 
			"http://www.scholar.google.com/citations?user=";
	private static final String GCR_EXPORT_CITATION_URL_POSTFIX = "&view_op=export_citations&hl=en";

	/** The Constant OUTPUT_FILE_EXTENSTION_BIBTEX. */
	private static final String OUTPUT_FILE_EXTENSTION_BIBTEX = ".bib";

	private AuthorCitationBibtexDownloader() {
		// Utility class. Do not instantiate.
	}


	private static File downloadFile(String fileURL, String fileName) 
			throws IOException, InvalidUrlException, NetworkConnectionException {

		InputStream inStream = null;
		FileOutputStream fileOutStream = null;
		
		try {
			URL url = new URL(fileURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			DownloadHandler.startConnect(connection);
			
			if (connection.getContentType().contains("bibtex")) {
				
				int byteRead;
				byte[] buffer = new byte[BUFFER_SIZE];
				File authorBibtexfile = new File(fileName + OUTPUT_FILE_EXTENSTION_BIBTEX);
				fileOutStream = new FileOutputStream(authorBibtexfile);
				
				inStream = connection.getInputStream();
				while ((byteRead = inStream.read(buffer)) != -1) {
					fileOutStream.write(buffer, 0, byteRead);
				}
				
				return authorBibtexfile;
			}
			
			return null;
			
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (fileOutStream != null) {
				fileOutStream.close();
			}
		}
	}

	/**
	 * Download the Bibtex file from Google Scholar.
	 * @return {@code null} if no bibtex file was found otherwise the Bibtex file
	 * @throws IOException 
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static File downloadAuthorBibtexFile(String userId) 
			throws IOException, InvalidUrlException, NetworkConnectionException {

		String gcrUrl = GCR_EXPORT_CITATION_URL_PREFIX + userId + GCR_EXPORT_CITATION_URL_POSTFIX;
		
		return downloadFile(gcrUrl, userId);
	}
}
