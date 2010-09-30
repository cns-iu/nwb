package edu.iu.nwb.composite.extractauthorpapernetwork.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public enum AuthorPaperFormat {
	BIBTEX("bibtex", "author", "title"),
	ISI("isi", "Authors", "Cite Me As"),
	SCOPUS("scopus", "Authors", "Title"),
	END_NOTE("endnote", "Author", "Title");

	public static final Map<String, String> AUTHOR_NAME_COLUMNS_BY_FORMATS =
		Collections.unmodifiableMap(mapFormatsToNameColumns());
	public static final Map<String, String> PAPER_NAME_COLUMNS_BY_FORMATS =
		Collections.unmodifiableMap(mapFormatsToPaperColumns());
	public static final Collection<String> FORMATS_SUPPORTED =
		Collections.unmodifiableCollection(compileFormatsSupported());

	private static Map<String, String> mapFormatsToNameColumns() {
		Map<String, String> authorNameColumnsByFormats = new HashMap<String, String>();

		for (AuthorPaperFormat format : AuthorPaperFormat.values()) {
			authorNameColumnsByFormats.put(format.getFormat(), format.getAuthorNameColumn());
		}

		return authorNameColumnsByFormats;
	}

	private static Map<String, String> mapFormatsToPaperColumns() {
		Map<String, String> paperNameColumnsByFormats = new HashMap<String, String>();

		for (AuthorPaperFormat format : AuthorPaperFormat.values()) {
			paperNameColumnsByFormats.put(format.getFormat(), format.getPaperNameColumn());
		}

		return paperNameColumnsByFormats;
	}

	private static Collection<String> compileFormatsSupported() {
		Collection<String> formatsSupported = new HashSet<String>();

		for (AuthorPaperFormat format : AuthorPaperFormat.values()) {
			formatsSupported.add(format.getFormat());
		}

		return formatsSupported;
	}

	private String format;
	private String authorNameColumn;
	private String paperNameColumn;

	private AuthorPaperFormat(String format, String authorNameColumn, String paperNameColumn) {
		this.format = format;
		this.authorNameColumn = authorNameColumn;
		this.paperNameColumn = paperNameColumn;
	}

	public String getFormat() {
		return this.format;
	}

	public String getAuthorNameColumn() {
		return this.authorNameColumn;
	}

	public String getPaperNameColumn() {
		return this.paperNameColumn;
	}
}