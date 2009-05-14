package edu.iu.epic.site.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;

public class Indexer {

	public void buildItemIndex(String databaseFile, String indexLocation)
			throws CorruptIndexException, LockObtainFailedException,
			IOException, ClassNotFoundException, SQLException {

		// You MUST use the same analyzer for reading from and writing to the
		// index.
		IndexWriter indexWriter = new IndexWriter(indexLocation,
				new StandardAnalyzer(), true,
				IndexWriter.MaxFieldLength.UNLIMITED);

		Class.forName("org.sqlite.JDBC");
		Connection connection = DriverManager.getConnection("jdbc:sqlite:"
				+ databaseFile);
		
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement
				.executeQuery("select id, name, description from core_item;");

		while (resultSet.next()) {
			String id = resultSet.getString("id");
			String name = resultSet.getString("name");
			String description = resultSet.getString("description");
			addItemDocument(indexWriter, id, name, description);
		}

		connection.close();
		indexWriter.close();
	}

	private void addItemDocument(IndexWriter indexWriter, String id,
			String name, String description) throws CorruptIndexException,
			IOException {
		Document doc = new Document();
		doc.add(new Field("id", id, Field.Store.YES, Field.Index.NO));
		doc.add(new Field("name", name, Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("description", description, Field.Store.YES,
				Field.Index.ANALYZED));
		doc.add(new Field("all", name + " " + description, Field.Store.YES,
				Field.Index.ANALYZED));
		indexWriter.addDocument(doc);

	}

	public static void main(String[] args) throws IOException, ParseException,
			ClassNotFoundException, SQLException {
		Indexer indexer = new Indexer();
		indexer.buildItemIndex("sqlite.db", "index");
	}
}
