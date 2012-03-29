package edu.iu.sci2.visualization.scimap.references;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;

import au.com.bytecode.opencsv.CSVReader;

public class MapOfScience {

	private static final int MINIMUM_SIZE = 1;

	private static final int RADIUS_MULTIPLIER = 5;

	protected static Map<String, List<JournalLocation>> journals = loadJournals();
	protected static Map<String, String> pretty = loadPretty();

	protected static Map<String, String> categories = initializeCategories();
	protected static Map<Integer, Node> nodes = loadNodes();

	protected static Map<String, String> lookup = loadLookup();
	//private static List<Edge> edges = loadEdges();
	
	private Map<Integer, Double> totals = new HashMap<Integer, Double>();
	private double scalingFactor;

	public MapOfScience(Map<String, Integer> found, double scalingFactor) {
		this.scalingFactor = scalingFactor;
		
		int total = 0;
		for(String key : found.keySet()) {
			total += found.get(key);
			//System.err.println("Found :" + key + ":" + found.get(key) + ":");
			//String canonical = lookup.get(key);
			List<JournalLocation> matchedLocations = journals.get(key);
			for(JournalLocation journal : matchedLocations) {
				Integer id = journal.getId();
				if(totals.containsKey(id)) {
					totals.put(id, totals.get(id) + found.get(key) * journal.getFraction());
				} else {
					totals.put(id, found.get(key).doubleValue() * journal.getFraction());
				}
			}
		}
	}
	
	

	private static Map<String, String> loadPretty() {
		Map<String, String> pretty = new HashMap<String, String>();

		try {
			CSVReader reader = new CSVReader(new InputStreamReader(MapOfScience.class.getResourceAsStream("pretty.tsv")), '\t');
			String[] line;
			while((line = reader.readNext()) != null) {
				pretty.put(line[0], line[1]);
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load lookup.");
		}
		return pretty;
	}



	private static Map<String, String> initializeCategories() {
		Map<String, String> categories = new HashMap<String, String>();
		
		categories.put("255:255:0", "Social Sciences");
		categories.put("255:133:255", "Electrical Engineering & Computer Science");
		categories.put("0:153:0", "Biology");
		categories.put("0:255:127", "Biotechnology");
		categories.put("255:143:0", "Brain Research");
		categories.put("97:255:255", "Chemical, Mechanical, & Civil Engineering");
		categories.put("0:0:255", "Chemistry");
		categories.put("133:49:12", "Earth Sciences");
		categories.put("240:139:129", "Health Professionals");
		categories.put("255:255:127", "Humanities");
		categories.put("184:0:0", "Infectious Diseases");
		categories.put("163:20:250", "Math & Physics");
		categories.put("255:0:0", "Medical Specialties");
		
		return categories;
	}

	private static Map<String, List<JournalLocation>> loadJournals() {
		Map<String, List<JournalLocation>> journals = new HashMap<String, List<JournalLocation>>();
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(MapOfScience.class.getResourceAsStream("journals.tsv")), '\t');
			String[] line;
			while((line = reader.readNext()) != null) {
				JournalLocation journal = new JournalLocation(line[0], line[1], line[2]);
				String name = journal.getName();
				if(journals.containsKey(name)) {
					journals.get(name).add(journal);
				} else {
					List<JournalLocation> locs = new ArrayList<JournalLocation>();
					locs.add(journal);
					journals.put(name, locs);
				}
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load base map.");
		}

		return journals;
	}

	private static List<Edge> loadEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(MapOfScience.class.getResourceAsStream("edges.csv")));
			String[] line;
			while((line = reader.readNext()) != null) {
				edges.add(new Edge(line[0], line[1], line[2], line[3]));
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load base map.");
		}

		return edges;
	}

	private static Map<Integer, Node> loadNodes() {
		Map<Integer, Node> nodeLookup = new HashMap<Integer, Node>();
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(MapOfScience.class.getResourceAsStream("nodes.csv")));
			String[] line;
			while((line = reader.readNext()) != null) {
				Node node = new Node(line[0], line[1], line[2], line[3], line[4], line[5]);
				nodeLookup.put(node.getId(), node);
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load base map.");
		}

		return nodeLookup;
	}

	private static Map<String, String> loadLookup() {

		Map<String, String> lookup = new HashMap<String, String>();

		try {
			CSVReader reader = new CSVReader(new InputStreamReader(MapOfScience.class.getResourceAsStream("canonical.txt")), '\t');
			String[] line;
			while((line = reader.readNext()) != null) {
				lookup.put(line[0], line[1]);
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load lookup.");
		}
		return lookup;
	}

	public String getPostscript() {
		
		StringTemplate mapTemplate = MapReferences.group.getInstanceOf("mapofscience");

		List<String> edgePostscript = new ArrayList<String>();
		edgePostscript.add("writeedges");

		List<String> emptyNodePostscript = new ArrayList<String>();
		for(Node node : nodes.values()) {
			if(!totals.containsKey(node.getId())) {
				emptyNodePostscript.add(nodePostscript(node));
			}
		}

		List<String> nodePostscript = new ArrayList<String>();
		Integer[] foundIds = foundIdsBySize(totals.keySet());
		for(Integer id : foundIds) {
			Node node = nodes.get(id);
			nodePostscript.add(nodePostscript(node.getX(), node.getY(), node.getRed(), node.getGreen(), node.getBlue(), totals.get(id)));
		}
		
		mapTemplate.setAttribute("edges", edgePostscript);
		mapTemplate.setAttribute("emptyNodes", emptyNodePostscript);
		mapTemplate.setAttribute("nodes", nodePostscript);
		
		return mapTemplate.toString();
	}

	private Integer[] foundIdsBySize(Collection<Integer> ids) {
		Integer[] foundIds = ids.toArray(new Integer[]{});
		Arrays.sort(foundIds, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return totals.get(o1).compareTo(totals.get(o2));
			}
		});
		return foundIds;
	}

	private String nodePostscript(Node node) {
		return nodePostscript(node.getX(), node.getY(), .7, .7, .7, 0.0);
	}

	private String nodePostscript(double x, double y, double red, double green, double blue, Double total) {
		double radius = Math.sqrt(scalingFactor * total) * RADIUS_MULTIPLIER + MINIMUM_SIZE;
		return "" + x + " " + y + " " + radius + " " + red + " " + green + " " + blue + " node";
	}

	private String edgePostscript(Edge edge) {
		Node source = nodes.get(edge.getSource());
		Node target = nodes.get(edge.getTarget());
		return "" + source.getX() + " " + source.getY() + " " + target.getX() + " " + target.getY() + " edge";
	}

}
