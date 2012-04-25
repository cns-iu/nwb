package edu.iu.sci2.database.isi.extract.network.weighted_document_document_network;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.DatabaseUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

import com.google.common.base.Joiner;

public class WeightedDocumentDocumentNetworkAlgorithm implements Algorithm {
	
	private Database database;
	private LogService logger;
	private Data parent;
	private float threshold;
	private ComparisonAlgorithm comparisonAlgorithm;
    
	public WeightedDocumentDocumentNetworkAlgorithm(Data[] data,
			float threshold, LogService logger, ComparisonAlgorithm comparisonAlgorithm) {

		this.database = (Database) data[0].getData();
		this.threshold = threshold;
		this.logger = logger;
		this.parent = data[0];
		this.comparisonAlgorithm = comparisonAlgorithm;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {

		Collection<Document> documents = getDocuments();

		Graph graph = getGraph(documents, this.threshold,
				this.comparisonAlgorithm);
		
		return new Data[] { DataFactory.withClassNameAsFormat(graph,
				DataProperty.NETWORK_TYPE, this.parent,
				"Document-Document Graph with Weighted Edges using the '"
						+ this.comparisonAlgorithm
						+ "' Comparison Alogrithm where the Threshold is "
						+ this.threshold) };
	}

	private static Graph getGraph(Collection<Document> documents,
			float threshold, ComparisonAlgorithm comparisonAlgorithm) {
		final String labelColumnName = "label";
		final String weightColumnName = "weight";
		
		/*
		 * Create Graph
		 */
		Table nodeData = new Table();
		nodeData.addColumn(labelColumnName, String.class);

		Table edgeData = new Table(0, 1);
		edgeData.addColumn(weightColumnName, float.class);
		edgeData.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class);
		edgeData.addColumn(Graph.DEFAULT_TARGET_KEY, int.class);

		Graph graph = new Graph(nodeData, edgeData, false);


		/*
		 * Create Nodes
		 */
		Map<Document, Node> documentToNode = new HashMap<Document, Node>();

		for (Document doc : documents) {
			Node docNode = graph.addNode();
			docNode.setString(labelColumnName, doc.getName());
			documentToNode.put(doc, docNode);
		}

    	/*
    	 * Create Edges
    	 */
		List<Document> allDocuments = new ArrayList<Document>(
				documents);
		for (int ii = 0; ii < allDocuments.size(); ii++) {
			Document doc = allDocuments.get(ii);
			Node selfNode = documentToNode.get(doc);
			for (int jj = ii + 1; jj < allDocuments.size(); jj++) {
				Document otherDocument = allDocuments.get(jj);
				float similarity = comparisonAlgorithm.calculateSimilarity(
						doc.getKeywordIDs(), otherDocument.getKeywordIDs());

				if (Math.abs(similarity) < threshold) {
					continue;
				}

				Node otherNode = documentToNode.get(otherDocument);
				Edge edge = graph.addEdge(selfNode, otherNode);
				edge.setFloat(weightColumnName, similarity);
			}
		}
		return graph;
	}
	


	private Collection<Document> getDocuments()
			throws AlgorithmExecutionException {
		Connection connection = DatabaseUtilities.connect(this.database,
				"Connection to database failed.\n");
		String newLine = System.getProperty("line.separator");
		try {

			Statement statement = DatabaseUtilities.createStatement(connection,
					"Creating a statement failed.\n");

			/**
			 * If speed becomes an issue, restructure the code so that {@link Document}
			 * doesn't need the {@link Keyword}s, only the keyword PKs from the table.
			 * 
			 * If the above change is made, you can remove the left join and really 
			 * speed up the query.
			 */
			String query = 
					"SELECT DOCUMENTS.PK as DOCUMENT_ID," + newLine
					+ "\t" + "DOCUMENTS.PUBLICATION_YEAR as DOCUMENT_PUB_YEAR," + newLine
					+ "\t" + "DOCUMENTS.VOLUME as DOCUMENT_VOLUME," + newLine
					+ "\t" + "DOCUMENTS.DIGITAL_OBJECT_IDENTIFIER as DOCUMENT_DOI," + newLine
					+ "\t" + "DOCUMENTS.BEGINNING_PAGE as DOCUMENT_FIRST_PAGE," + newLine
					+ "\t" + "SOURCES.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION " 
						+ "as DOCUMENT_ABVR_JOURNAL_NAME," + newLine
					+ "\t" + "PEOPLE.RAW_NAME as DOCUMENT_FIRST_AUTHOR_NAME," + newLine
					+ "\t" + "DOCUMENT_KEYWORDS.KEYWORD_ID as KEYWORD_ID," + newLine
					+ "\t" + "KEYWORDS.NAME as KEYWORD_NAME" + newLine
				+ "FROM DOCUMENTS" + newLine
				+ "LEFT JOIN SOURCES" + newLine
				+ "ON SOURCES.PK=DOCUMENTS.SOURCE_ID" + newLine
				+ "LEFT JOIN PEOPLE" + newLine
				+ "ON PEOPLE.PK=DOCUMENTS.FIRST_AUTHOR_ID" + newLine
				+ "RIGHT JOIN DOCUMENT_KEYWORDS" + newLine
				+ "ON DOCUMENT_KEYWORDS.DOCUMENT_ID=DOCUMENTS.PK" + newLine
				+ "LEFT JOIN KEYWORDS" + newLine
				+ "ON KEYWORDS.PK=DOCUMENT_KEYWORDS.KEYWORD_ID";
			statement.execute(query);

			ResultSet resultSet = statement.getResultSet();
			Map<String, Document> documentIDToDocument = new HashMap<String, Document>();
			
			while (resultSet.next()) {
				// Indices from the query.
				String documentID = resultSet.getString("DOCUMENT_ID");
				String keywordID = resultSet.getString("KEYWORD_ID");
				String keywordName = resultSet.getString("KEYWORD_NAME");

				if (!documentIDToDocument.containsKey(documentID)) {
					String publicationYear = resultSet
							.getString("DOCUMENT_PUB_YEAR");
					String volume = resultSet.getString("DOCUMENT_VOLUME");
					String doi = resultSet.getString("DOCUMENT_DOI");
					String firstPage = resultSet
							.getString("DOCUMENT_FIRST_PAGE");
					String abvrJournalName = resultSet
							.getString("DOCUMENT_ABVR_JOURNAL_NAME");
					String authorName = resultSet
							.getString("DOCUMENT_FIRST_AUTHOR_NAME");
					
					/*
					 * This holds all the strings to be joined to create the
					 * label for the document. It follows the pattern the flat
					 * file ISI uses to create the 'CITE ME AS' column.
					 */
					List<String> documentNameParameters = new ArrayList<String>();
					
					documentNameParameters.add(authorName.replaceAll("\"", ""));
					if (!"".equals(publicationYear) && publicationYear != null) {
						documentNameParameters.add(publicationYear);
					}
					if (!"".equals(abvrJournalName) && abvrJournalName != null) {
						documentNameParameters.add(abvrJournalName);
					}
					if (!"".equals(volume) && volume != null) {
						documentNameParameters.add("V" + volume);
					}
					if (!"".equals(firstPage) && firstPage != null) {
						documentNameParameters.add("P" + firstPage);
					}
					if (!"".equals(doi) && doi != null) {
						documentNameParameters.add("DOI:" + doi);
					}

					String documentName = Joiner.on(",").join(
							documentNameParameters);
					documentIDToDocument.put(documentID, new Document(
							documentID, documentName));
				}

				Keyword keyword = new Keyword(keywordID, keywordName);

				Document document = documentIDToDocument.get(documentID);
				document.addKeyword(keyword);
			}
			try {
				statement.close();
			} catch (SQLException e) {
				this.logger.log(LogService.LOG_WARNING,
						"A statement could not be closed." + e.getMessage());
			}
			return documentIDToDocument.values();
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"A connection could not be made to the database: "
							+ newLine
							+ e.getMessage());
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"A SQL Exception occured with trying to get the "
							+ "documents and keywords from the database."
							+ newLine
							+ e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				this.logger.log(
						LogService.LOG_WARNING,
						"A connection to the database could not be closed."
								+ e.getMessage());
			}
		}
	}
    
	private class Document {
		private String id;
		private String name;
		private List<Keyword> keywords;
		private Set<String> keywordIDs;

		public Document(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		// Unused currently but potentially useful.
		@SuppressWarnings("unused")
		public String getId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}

		// Unused currently but potentially useful.
		@SuppressWarnings("unused")
		public List<Keyword> getKeywords() {
			return this.keywords;
		}
		
		private void addKeywordID(String keywordID) {
			if (this.keywordIDs == null) {
				this.keywordIDs = new HashSet<String>();
			}
			this.keywordIDs.add(keywordID);
		}
		
		public void addKeyword(Keyword keyword) {
			if (this.keywords == null) {
				this.keywords = new ArrayList<Keyword>();
			}
			this.keywords.add(keyword);
			
			addKeywordID(keyword.getId());
		}
		public Set<String> getKeywordIDs() {
			return this.keywordIDs;
		}
	}
	
	private class Keyword {
		private String id;
		private String name;

		public Keyword(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return this.id;
		}

		// Unused currently but potentially useful.
		@SuppressWarnings("unused")
		public String getName() {
			return this.name;
		}
	}
}