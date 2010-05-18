package edu.iu.epic.site.search;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.json.JSONException;
import org.json.JSONObject;

public class Searcher {
	public JSONObject searchItemIndex(String querystring, String fieldToSearch,
			String indexPath) throws ParseException, CorruptIndexException,
			IOException, JSONException {

		Query query = new QueryParser(fieldToSearch, new StandardAnalyzer())
				.parse(querystring);

		int hitsPerPage = 10;
		IndexSearcher searcher = new IndexSearcher(indexPath);
		TopDocCollector collector = new TopDocCollector(hitsPerPage);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		System.out.println("Found " + hits.length + " hits.");
		JSONObject json = new JSONObject();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String itemId = d.get("id");
			float itemScore = hits[i].score;
			JSONObject item = new JSONObject();
			item.put("item_id", itemId);
			item.put("item_score", itemScore);
			json.append("result", item);
			System.out.println((i + 1) + ". " + itemId + " - "
					+ d.get("all") + "(" + itemScore + ")");
		}

		searcher.close();
		return json;
	}

	public static void main(String[] args) throws CorruptIndexException, ParseException, IOException, JSONException {
		Searcher searcher = new Searcher();
		String searchString = args.length > 0 ? args[0] : "";
		System.out.println(searcher.searchItemIndex(searchString, "all", "index"));
	}
}