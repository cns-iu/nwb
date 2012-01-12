package edu.iu.sci2.visualization.temporalbargraph;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.InvalidRecordException;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;
import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.string;

public class DocumentPostScriptCreator {
	
	private String labelColumn;
	private String startDateColumn;
	private String endDateColumn;
	private String sizeByColumn;
	private String startDateFormat;
	private String endDateFormat;
	private String query;
	private double userPageWidth;
    private double userPageHeight;
    private boolean shouldScaleOutput;
    
    private static final double POINTS_PER_INCH = 72;
    private static final double VIZ_AREA_TOP_MARGIN = 1;
    private static final double VIZ_AREA_BOTTOM_MARGIN = 2;
    private static final double VIZ_AREA_HEIGHT_WIDTH_RATIO = .5;
    
    private double vizHeight;
    private double vizWidth;
    private double pageWidth;
    private double pageHeight;
    private double topVizMargin;
    private double leftVizMargin;
    private double bottomVizMargin;
	
	/**
	 * This will handles the postscript needed to create a document.
	 * @param labelColumn
	 * @param startDateColumn
	 * @param endDateColumn
	 * @param sizeByColumn
	 * @param startDateFormat
	 * @param endDateFormat
	 * @param query
	 * @param pageWidth
	 * @param pageHeight
	 * @param shouldScaleOutput
	 */
	public DocumentPostScriptCreator(
			String labelColumn,
			String startDateColumn,
			String endDateColumn,
			String sizeByColumn,
			String startDateFormat,
			String endDateFormat,
			String query,
			double pageWidth,
			double pageHeight,
			boolean shouldScaleOutput)
	{
		this.labelColumn = labelColumn;
		this.startDateColumn = startDateColumn;
		this.endDateColumn = endDateColumn;
		this.sizeByColumn = sizeByColumn;
		this.startDateFormat = startDateFormat;
		this.endDateFormat = endDateFormat;
		this.query = query;
		this.userPageWidth = pageWidth;
		this.userPageHeight = pageHeight;
		this.shouldScaleOutput = shouldScaleOutput;
		
		this.pageHeight = this.userPageHeight * POINTS_PER_INCH;
		this.pageWidth = this.userPageWidth * POINTS_PER_INCH;
		
		this.topVizMargin = VIZ_AREA_TOP_MARGIN * POINTS_PER_INCH;
		this.bottomVizMargin = VIZ_AREA_BOTTOM_MARGIN * POINTS_PER_INCH;
		this.vizHeight = this.pageHeight - this.topVizMargin - this.bottomVizMargin;
		this.vizWidth = this.vizHeight * VIZ_AREA_HEIGHT_WIDTH_RATIO;
		this.leftVizMargin = (this.pageWidth - vizWidth) / 2;
	}

	/**
	 * Create all the postscript that represents a document.
	 * @param table
	 * @param logger
	 * @param csvWriter
	 * @return
	 * @throws PostScriptCreationException
	 */
	public String createPostScript(
			Table table, LogService logger, CSVWriter csvWriter)
			throws PostScriptCreationException {
		
		/**
		 * A postscript document looks like the following:
		 * Header
		 * Procedure Definitions
		 * Document Setup
		 * Pages
		 * Document Trailer
		 */
		List<Record> sortedRecords = getSortedRecordsFromTable(table, logger);

		VizAreaPSCreator vizAreaPsCreator =
				new VizAreaPSCreator(csvWriter, sortedRecords, vizHeight, vizWidth, shouldScaleOutput);
		
		List<String> vizAreas = vizAreaPsCreator.getPostScriptPages();
		int numberOfPages = vizAreas.size();
				
		StringBuilder documentPostScript = new StringBuilder();
		documentPostScript.append(getHeader(numberOfPages));
		documentPostScript.append(getProcedureDefinitions());
		documentPostScript.append(getDocumentSetup());
		documentPostScript.append(getPages(vizAreas, query));
		documentPostScript.append(getDocumentTrailer());
		return documentPostScript.toString();
	}

	/**
	 * Create the postscript for the pages area.
	 * @param visualizations
	 * @param query
	 * @return
	 */
	private String getPages(List<String> visualizations, String query) {
		StringBuilder pagesPostScript = new StringBuilder();
		pagesPostScript.append(getDocumentDefinitions());
		
		for (String visualization : visualizations) {
			int pageNumber = visualizations.indexOf(visualization) + 1;
			pagesPostScript.append(getPage(visualization, pageNumber));
		}
		
		return pagesPostScript.toString();
	}
	
	/**
	 * Create the postscript for an individual page.
	 * @param visualization
	 * @param pageNumber
	 * @return
	 */
	private String getPage(String visualization, int pageNumber) {
		StringBuilder pagePostScript = new StringBuilder();
		pagePostScript.append(
				PostScriptFormationUtilities.line("%Page: " + pageNumber + " " + pageNumber));
		pagePostScript.append(getPageTitle());
		pagePostScript.append(getPageInfo(query));
		pagePostScript.append(getPageFooter());
		pagePostScript.append(getHowto());
		String scaleTitle = "Area size equals total budget";
		pagePostScript.append(getScale(scaleTitle));

		pagePostScript.append(getVisualizationArea(visualization));
		
		pagePostScript.append(PostScriptFormationUtilities.line("showpage"));
		return pagePostScript.toString();
	}

	/**
	 * Create the postscript for a vistualization area.
	 * @param visualization
	 * @return
	 */
	private String getVisualizationArea(String visualization) {
		StringBuilder visualizationArea = new StringBuilder();
		visualizationArea.append(
				PostScriptFormationUtilities.line("gsave") +
				PostScriptFormationUtilities.line(leftVizMargin	+ " % x") + 
				PostScriptFormationUtilities.line(bottomVizMargin + " % y") + 
				PostScriptFormationUtilities.line("translate"));
		visualizationArea.append(visualization);
		
		visualizationArea.append(PostScriptFormationUtilities.line("grestore"));
		return visualizationArea.toString();
	}

	/**
	 * Get the postscript needed for a scale.
	 */
	private static String getScale(String title){
		StringBuilder scale = new StringBuilder();
		scale.append(PostScriptFormationUtilities.line("(" + title + ") (Start Year) (End Year) scalebar"));
		return scale.toString();
	}
	
	/**
	 * Get the postscript needed for a page footer.
	 * @return
	 */
	private static String getPageFooter() {
		StringBuilder footer = new StringBuilder();
		footer.append(PostScriptFormationUtilities.line("(NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)) footer"));
		return footer.toString();
	}

	/**
	 * Get the postscript needed for a page title.
	 * @return
	 */
	private static String getPageTitle() {
		StringBuilder pageTitle = new StringBuilder();
		pageTitle.append(PostScriptFormationUtilities.line("(Temporal Bar Graph) title"));
		return pageTitle.toString();
	}

	/**
	 * Get the postscript needed for a pageInfo
	 * @param query
	 * @return
	 */
	private static String getPageInfo(String rawQuery) {
		//TODO clean the query string
		StringBuilder pageInfo = new StringBuilder();
		String query = rawQuery;
		if(query != null && query.length() > 0){
			query = "Matching query '" + query + "'";
		} else {
			query = "";
		}
		
		pageInfo.append(PostScriptFormationUtilities.line(
				string(query) + " (NIH funding data rendered by using 'Temporal Bar Graph' on " 
				+ new DateTime().toLocalDate().toString("yyyy.MM.dd")
				+ ") queryInfo"));
		return pageInfo.toString();
	}

	/**
	 * Get the postscript needed for the howto area
	 * @return
	 */
	private static String getHowto(){
		StringBuilder howto = new StringBuilder();
		howto.append(PostScriptFormationUtilities.line("howto"));
		return howto.toString();
	}
	
	/**
	 * Get the postscript for the procedure definitions area.
	 * @return
	 */
	private static String getProcedureDefinitions() {
		StringBuilder procedureDefinitions = new StringBuilder();
		procedureDefinitions.append(PostScriptFormationUtilities.line("%%BeginProlog"));
		procedureDefinitions.append(PostScriptFormationUtilities.line("%%EndProlog"));
		return procedureDefinitions.toString();
	}

	/**
	 * Get the postscript for the document trailer area.
	 * @return
	 */
	private static String getDocumentTrailer() {
		StringBuilder documentTrailer = new StringBuilder();
		documentTrailer.append(PostScriptFormationUtilities.line("%%Trailer"));
		return documentTrailer.toString();
	}

	/**
	 * Get the postscript for the document setup area.
	 * @return
	 */
	private String getDocumentSetup() {
		StringBuilder documentSetup = new StringBuilder();
		documentSetup.append(PostScriptFormationUtilities.line("%%BeginSetup"));
		documentSetup.append(
				PostScriptFormationUtilities.line("%%BeginFeature: *PageSize Letter") +
				PostScriptFormationUtilities.line("<< /PageSize [" + this.pageWidth + " " + this.pageHeight + "] >> setpagedevice") +
				PostScriptFormationUtilities.line("%%EndFeature"));
		documentSetup.append(PostScriptFormationUtilities.line("%%EndSetup"));
		return documentSetup.toString();
	}

	/**
	 * Get the header for the document
	 * @param pages The number of pages of the document
	 * @return
	 */
	private static String getHeader(int pages) {
		StringBuilder header = new StringBuilder();
		header.append(PostScriptFormationUtilities.line("%!PS-Adobe-3.0 EPSF-3.0") 
				+ PostScriptFormationUtilities.line("%%Pages: " + pages)
				+ PostScriptFormationUtilities.line("%%Title: (Temporal Line Graph)")
				+ PostScriptFormationUtilities.line("%%Creator: (Sci2)")
				+ PostScriptFormationUtilities.line("%%CreationDate: (01/05/2001)")
				+ PostScriptFormationUtilities.line("%%BeginFeature: *PageSize Letter") 
				+ PostScriptFormationUtilities.line("letter")
				+ PostScriptFormationUtilities.line("%%EndFeature") 
				+ PostScriptFormationUtilities.line("%%EndComments")
		);
		return header.toString();
	}

	
	/**
	 * This returns all the "headers" that are needed to define a document.
	 * @return
	 */
	private String getDocumentDefinitions() {
		StringBuilder headers = new StringBuilder();
		// The left viz margin
		headers.append(PostScriptFormationUtilities.definition("leftvizmargin", Double.toString(leftVizMargin)));
		
		// The bottom viz margin
		headers.append(PostScriptFormationUtilities.definition("bottomvizmargin", Double.toString(bottomVizMargin)));		
		
		// The page width
		headers.append(PostScriptFormationUtilities.definition("pageWidth", Double.toString(pageWidth)));

		// The page height
		headers.append(PostScriptFormationUtilities.definition("pageHeight", Double.toString(pageHeight)));

		// Center text
		headers.append(
				PostScriptFormationUtilities.line("/center { % (text) x y => -") + 
				PostScriptFormationUtilities.line("	moveto dup stringwidth pop -2 div 0 rmoveto") + 
				PostScriptFormationUtilities.line("} def"));
		
		headers.append(getTitleDefinitions());
		
		headers.append(getQueryInfoDefinitions());
		
		headers.append(getFooterDefinitions());
		
		headers.append(getVisualizationDefinitions());
	
		headers.append(getScaleBoxDefinitions());
		
		headers.append(getHowtoDefinitions());
				
		return headers.toString();
	}

	/**
	 * Get the definitions needed for the howto area.
	 * @return
	 */
	private String getHowtoDefinitions() {
		StringBuilder howto = new StringBuilder();
		// Define the howto
		double howToLeftMargin = (this.pageWidth / 2) + (.5 * POINTS_PER_INCH);
		double howToTopMargin = (this.bottomVizMargin - (.5 * POINTS_PER_INCH));

		double howtoTitleFontSize = 14;
		double howtoTextFontSize = 10;
		howto.append(PostScriptFormationUtilities.line("/howto {")
				+ PostScriptFormationUtilities.line("	gsave")
				+ PostScriptFormationUtilities.line("	0.0 0.0 0.0 setrgbcolor")
				+ PostScriptFormationUtilities.line("	/howtoTitleFont { " + howtoTitleFontSize + " } def")
				+ PostScriptFormationUtilities.line("	/UniverseExtended howtoTitleFont selectfont")
				+ PostScriptFormationUtilities.line("	" + howToLeftMargin + " % x")
				+ PostScriptFormationUtilities.line("	" + howToTopMargin + " howtoTitleFont sub % y")
				+ PostScriptFormationUtilities.line("	moveto")
				+ PostScriptFormationUtilities.line("	(How To Read This Map) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	0.75 0.75 0.75 setrgbcolor")
				+ PostScriptFormationUtilities.line("	/howtoTextFont { " + howtoTextFontSize + " } def")
				+ PostScriptFormationUtilities.line("	/Arial howtoTextFont selectfont")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(At vero eos et accusamus et iusto odio dignissimos) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(ducimus qui blanditiis praesentium voluptatum deleniti) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(atque corrupti quos dolores et quas molestias excepturi) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(sint occaecati cupiditate non provident, similique sunt in) dup")
				+ PostScriptFormationUtilities.line(	"	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(culpa qui officia deserunt mollitia animi, id est laborum et) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	howtoTextFont neg rmoveto")
				+ PostScriptFormationUtilities.line("	(dolorum fuga. Et harum quidem rerum facilis est et) dup")
				+ PostScriptFormationUtilities.line("	stringwidth pop neg 2 -1 roll")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	grestore")
				+ PostScriptFormationUtilities.line("} def"));
		return howto.toString();
	}

	/**
	 * Get the definitions needed for the scalebox area.
	 * @return
	 */
	private String getScaleBoxDefinitions() {
		StringBuilder scaleBox = new StringBuilder();
		// Define scale
		double scaleWidth = 2 * POINTS_PER_INCH;
		double scaleHeight = 1.5 * POINTS_PER_INCH;
		double scaleLeftMargin = ((this.pageWidth / 2) - scaleWidth) / 2;
		double scaleBottomMargin = this.bottomVizMargin - scaleHeight;
		double scaleRightMargin = scaleWidth + scaleLeftMargin;
		double defaultFontSize = 10;
		
		scaleBox.append(PostScriptFormationUtilities.definition("scaleLeftMargin", Double.toString(scaleLeftMargin)));
		scaleBox.append(PostScriptFormationUtilities.definition("scaleRightMargin", Double.toString(scaleRightMargin)));
		scaleBox.append(PostScriptFormationUtilities.definition("scaleBottomMargin", Double.toString(scaleBottomMargin)));
		scaleBox.append(
			PostScriptFormationUtilities.line("/scalebar { % (title) (start year) (end year) => -")+
			PostScriptFormationUtilities.line("	gsave")+
			PostScriptFormationUtilities.line("	scaleLeftMargin scaleBottomMargin translate")+
			PostScriptFormationUtilities.line("	0 0 moveto")+
			PostScriptFormationUtilities.line("	.75 .75 .75 setrgbcolor")+
			PostScriptFormationUtilities.line("	/scaleBarYearFontSize { " + defaultFontSize + " } def")+
			PostScriptFormationUtilities.line("	/Arial scaleBarYearFontSize selectfont")+
			PostScriptFormationUtilities.line("	exch dup")+
			PostScriptFormationUtilities.line("	stringwidth pop 2 div 2 -1 roll show % [x1] (end) (title)")+
			PostScriptFormationUtilities.line("	exch dup")+
			PostScriptFormationUtilities.line("	stringwidth pop dup 2 div exch % [dx2] [dx2]/2 (end) [x1] (title)")+
			PostScriptFormationUtilities.line("	scaleRightMargin exch sub dup 3 -1 roll exch sub exch 0 moveto  % => RM [dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] RM-[dx2] [dx2]/2 (end) [x1] (title) => [dx2]/2 RM-[dx2] RM-[dx2] (end) [x1] (title) => ... => [x2] (end) [x1] (title)")+
			PostScriptFormationUtilities.line("	2 -1 roll show % [x2] [x1] (title)")+
			PostScriptFormationUtilities.line("	exch dup scaleBarYearFontSize moveto % [x1] [x2] (title)")+
			PostScriptFormationUtilities.line("	/scaleBarThickness { " + defaultFontSize + " } def")+
			PostScriptFormationUtilities.line("	0 scaleBarThickness rlineto")+
			PostScriptFormationUtilities.line("	exch sub 0 rlineto")+
			PostScriptFormationUtilities.line("	0 scaleBarThickness neg rlineto")+
			PostScriptFormationUtilities.line("	fill")+
			PostScriptFormationUtilities.line("	stroke")+
			PostScriptFormationUtilities.line("	/scalebartitlefontsize { " + defaultFontSize + " } def")+
			PostScriptFormationUtilities.line("	/Arial scalebartitlefontsize selectfont")+
			PostScriptFormationUtilities.line("	scaleRightMargin 2 div % (title) [x]")+
			PostScriptFormationUtilities.line("	0 scaleBarYearFontSize add scaleBarThickness add scalebartitlefontsize 2 div add % => (title) [x] [y]")+
			PostScriptFormationUtilities.line("	center")+
			PostScriptFormationUtilities.line("	show")+
			PostScriptFormationUtilities.line("	grestore")+
			PostScriptFormationUtilities.line("} def"));
		return scaleBox.toString();
	}

	/**
	 * Get the definitions needed for the visualization area.
	 * @return
	 */
	private static String getVisualizationDefinitions() {
		StringBuilder visualization = new StringBuilder();

		visualization.append(VizAreaPSCreator.getPostScriptVisualizationDefinitions());
		
		return visualization.toString();
	}

	/**
	 * Get the definitions needed for the footer area.
	 * @return
	 */
	private static String getFooterDefinitions() {
		StringBuilder footer = new StringBuilder();
		double footerFontSize = 12;
		footer.append(PostScriptFormationUtilities.definition("footerFontSize", Double.toString(footerFontSize)));
		footer.append(PostScriptFormationUtilities.line("/footer { % (footer)"));
		footer.append(PostScriptFormationUtilities.line("	gsave"));
		footer.append(PostScriptFormationUtilities.line("	0.75 0.75 0.75 setrgbcolor"));
		footer.append(PostScriptFormationUtilities.line("	/Arial footerFontSize selectfont"));
		footer.append(PostScriptFormationUtilities.line("	pageWidth 2 div footerFontSize center"));
		footer.append(PostScriptFormationUtilities.line("	show"));
		footer.append(PostScriptFormationUtilities.line("	grestore"));
		footer.append(PostScriptFormationUtilities.line("} def "));
		return footer.toString();
	}

	/**
	 * Get the definitions needed for the queryinfo area.
	 * @return
	 */
	private static String getQueryInfoDefinitions() {
		StringBuilder queryInfo = new StringBuilder();
		// The def for the queryInfo
		double queryInfoFontSize = 12;
		queryInfo.append(PostScriptFormationUtilities.definition("queryInfoFontSize", Double.toString(queryInfoFontSize)));
		queryInfo.append(PostScriptFormationUtilities.line("/queryInfo { % (query) (info)")
				+ PostScriptFormationUtilities.line("	gsave")
				+ PostScriptFormationUtilities.line("	0.75 0.75 0.75 setrgbcolor")
				+ PostScriptFormationUtilities.line("	/Arial queryInfoFontSize selectfont")
				+ PostScriptFormationUtilities.line("	pageWidth 2 div % x")
				+ PostScriptFormationUtilities.line("	pageHeight titleFontSize sub queryInfoFontSize sub % y")
				+ PostScriptFormationUtilities.line("	center % subtract the size of the title and this font and put it in the center")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	pageWidth 2 div % x")
				+ PostScriptFormationUtilities.line("	pageHeight titleFontSize sub queryInfoFontSize sub queryInfoFontSize sub % y")
				+ PostScriptFormationUtilities.line("	center % subtract the size of the title and this font and put it in the center")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("	grestore")
				+ PostScriptFormationUtilities.line("} def"));
		return queryInfo.toString();
	}
	
	/**
	 * Get the definitions needed for the title area.
	 * @return
	 */
	private static String getTitleDefinitions() {
		StringBuilder title = new StringBuilder();
		double titleFontSize = 16;
		title.append(PostScriptFormationUtilities.definition("titleFontSize", Double.toString(titleFontSize)));
		title.append(
				PostScriptFormationUtilities.line("/title { % (title) => -") + 
				PostScriptFormationUtilities.line("	0.0 0.0 0.0 setrgbcolor") + 
				PostScriptFormationUtilities.line("	/UniverseExtended titleFontSize selectfont") + 
				PostScriptFormationUtilities.line("	pageWidth 2 div % x") + 
				PostScriptFormationUtilities.line("	pageHeight titleFontSize sub % y") + 
				PostScriptFormationUtilities.line("	center % put it in the center of the x and the top minus the font size") + 
				PostScriptFormationUtilities.line("	show") + 
				PostScriptFormationUtilities.line("} def"));
		
		return title.toString();
	}

	/**
	 * Get the sorted records from a table.
	 * @param table
	 * @param logger
	 * @return A sorted list of Record objects from the table.
	 * @throws PostScriptCreationException
	 */
	private List<Record> getSortedRecordsFromTable(Table table, LogService logger) throws PostScriptCreationException {
		Record[] records;
		try {
			records = readRecordsFromTable(table, logger);
		} catch (ParseException parseException) {
			throw new PostScriptCreationException(parseException);
		}
		
		if (records.length == 0) {
			String exceptionMessage = "No records to create PostScript from!";
			throw new PostScriptCreationException(exceptionMessage);
		}
		
		// TODO Ordering.natural().sortedCopy(records);
		Record[] sortedRecords = sortRecords(records);
		
		return Arrays.asList(sortedRecords);
	}

	/**
	 * Get the records from a table.
	 * @param table
	 * @param logger
	 * @return A Record[] of all the records found in the table
	 * @throws ParseException
	 */
	// TODO pull up
	private Record[] readRecordsFromTable(Table table, LogService logger)
			throws ParseException {
		List<Record> workingRecordSet = new ArrayList<Record>();
		
		for(Iterator<?> rows = table.tuples(); rows.hasNext();) {
			Tuple row = (Tuple) rows.next();
			
			try {
				Record newRecord = new Record(
					row,
					this.labelColumn,
					this.startDateColumn,
					this.endDateColumn,
					this.sizeByColumn,
					this.startDateFormat,
					this.endDateFormat);
				
				workingRecordSet.add(newRecord);
			} catch (InvalidRecordException e) {
				logger.log(LogService.LOG_WARNING, e.getMessage(), e);
			}
		}

		return workingRecordSet.toArray(new Record[0]);
	}
	
	/**
	 * Sort the records in an array
	 * @param originalRecords
	 * @return A sorted Record[]
	 */
	private static Record[] sortRecords(Record[] originalRecords) {

		Record[] sortedRecords = Arrays.copyOf(originalRecords,
				originalRecords.length);

		Arrays.sort(sortedRecords, Record.START_DATE_ORDERING);

		return sortedRecords;
	}
	
	
}