package edu.iu.scipolicy.visualization.scimap.journals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.utilities.NumberUtilities;

import au.com.bytecode.opencsv.CSVReader;

public class MapOfScience {
	/* Brightnesses correspond to PostScript's setgray command.
	 * 0 is black, 1 is white.
	 */
	public static final double CIRCLE_BRIGHTNESS = 0.3;
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.3;
	public static final double EXTREMA_LABEL_FONT_SIZE = 6;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_FONT_SIZE = 10;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.25;
	public static final double KEY_LABEL_BRIGHTNESS = 0.3;
	public static final double KEY_LABEL_FONT_SIZE = 6;
	public static final String FONT_NAME = "Garamond";	
	
	
	public static final String JOURNALS_PATH = "journals.tsv";

	public static final String PRETTY_PATH = "pretty.tsv";

	public static final char JOURNALS_SEPARATOR = '\t';

	public static final char PRETTY_SEPARATOR = '\t';

	public static final char CANONICAL_SEPARATOR = '\t';

	private static final int MINIMUM_SIZE = 1;

	private static final int RADIUS_MULTIPLIER = 5;

	protected static Map<String, List<JournalLocation>> journals = loadJournals();
	protected static Map<String, String> pretty = loadPretty();

	protected static Map<String, String> categories = initializeCategories();
	protected static Map<Integer, Node> nodes = loadNodes();

	protected static Map<String, String> lookup = loadLookup();
	//private static List<Edge> edges = loadEdges();

	protected Map<Integer, Double> totals = new HashMap<Integer, Double>();
	private int idsHit = 0;
	
	public MapOfScience(Map<String, Integer> found) {		
		int total = 0;
		for(String key : found.keySet()) {
			total += found.get(key);
			//System.err.println("Found :" + key + ":" + found.get(key) + ":");
			//String canonical = lookup.get(key);
			List<JournalLocation> matchedLocations = journals.get(key);
			for(JournalLocation journal : matchedLocations) {
				Integer id = journal.getId();
				
				if(totals.containsKey(id)) {
					totals.put(
							id,
							totals.get(id) + found.get(key) * journal.getFraction());
				} else {
					totals.put(
							id,
							found.get(key).doubleValue() * journal.getFraction());
					idsHit++;
				}
			}
		}
	}
	
	public int getIDsHit() {
		return idsHit;
	}
	
	private static Map<String, String> loadPretty() {
		Map<String, String> pretty = new HashMap<String, String>();

		try {
			CSVReader prettyReader =
				new CSVReader(
						createResourceReader(PRETTY_PATH),
						PRETTY_SEPARATOR);
			String[] line;
			while((line = prettyReader.readNext()) != null) {
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
		Map<String, List<JournalLocation>> journals =
			new HashMap<String, List<JournalLocation>>();
		
		try {
			CSVReader journalsReader =
				new CSVReader(
						createResourceReader(JOURNALS_PATH),
						JOURNALS_SEPARATOR);
			String[] line;
			while((line = journalsReader.readNext()) != null) {
				JournalLocation journal =
					new JournalLocation(line[0], line[1], line[2]);
				String name = journal.getName();
				if(journals.containsKey(name)) {
					journals.get(name).add(journal);
				} else {
					List<JournalLocation> locs =
						new ArrayList<JournalLocation>();
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

	private static Map<Integer, Node> loadNodes() {
		Map<Integer, Node> nodeLookup = new HashMap<Integer, Node>();
		try {
			CSVReader reader = new CSVReader(createResourceReader("nodes.csv"));
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
	
	private static Reader createResourceReader(String path) {
		return new InputStreamReader(MapOfScience.class.getResourceAsStream(path));
	}

	private static Map<String, String> loadLookup() {

		Map<String, String> lookup = new HashMap<String, String>();

		try {
			CSVReader canonicalReader =
				new CSVReader(
						createResourceReader("canonical.txt"),
						CANONICAL_SEPARATOR);
			String[] line;
			while((line = canonicalReader.readNext()) != null) {
				lookup.put(line[0], line[1]);
			}
		} catch (IOException e) {
			//be horrible until this is refactored to properly handle the error.
			throw new IllegalArgumentException("Unable to load lookup.");
		}
		return lookup;
	}

	public String getPostScript() {
		StringTemplate mapTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("mapofscience");

		List<String> edgePostScript = new ArrayList<String>();
		edgePostScript.add("writeedges");

		List<String> emptyNodePostScript = new ArrayList<String>();
		for(Node node : nodes.values()) {
			if(!totals.containsKey(node.getId())) {
				emptyNodePostScript.add(nodePostScript(node));
			}
		}

		List<String> nodePostScript = new ArrayList<String>();
		Integer[] foundIds = foundIdsBySize(totals.keySet());
		for(Integer id : foundIds) {
			Node node = nodes.get(id);
			nodePostScript.add(
					nodePostScript(
							node.getX(),
							node.getY(),
							node.getRed(),
							node.getGreen(),
							node.getBlue(),
							totals.get(id)));
		}

		mapTemplate.setAttribute("edges", edgePostScript);
		mapTemplate.setAttribute("emptyNodes", emptyNodePostScript);
		mapTemplate.setAttribute("nodes", nodePostScript);

		StringTemplate legendDefinitionsTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("legendDefinitions");
		StringTemplate circleAreaLegendDefinitionsTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("circleAreaLegendDefinitions");
		
		
		mapTemplate.setAttribute("x", 50);
		mapTemplate.setAttribute("y", 60);

		double minArea = Collections.min(totals.values());
		double maxArea = Collections.max(totals.values());
		int midArea = (int) Math.round((minArea + maxArea) / 2.0);
		mapTemplate.setAttribute("minRadius", calculateRadius(minArea));
		mapTemplate.setAttribute("midRadius", calculateRadius(midArea));
		mapTemplate.setAttribute("maxRadius", calculateRadius(maxArea));

		mapTemplate.setAttribute("circleBrightness", CIRCLE_BRIGHTNESS);

				
		mapTemplate.setAttribute("minLabel", round(minArea));
		mapTemplate.setAttribute("midLabel", round(midArea));
		mapTemplate.setAttribute("maxLabel", round(maxArea));

		mapTemplate.setAttribute(
				"extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		mapTemplate.setAttribute(
				"extremaLabelFontSize", EXTREMA_LABEL_FONT_SIZE);

		mapTemplate.setAttribute("typeLabel", "TYPE LABEL");
		mapTemplate.setAttribute(
				"typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		mapTemplate.setAttribute(
				"typeLabelFontSize", TYPE_LABEL_FONT_SIZE);

		mapTemplate.setAttribute(
				"scalingLabel", "(" + "SCALING LABEL" + ")");
		mapTemplate.setAttribute(
				"scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);

		mapTemplate.setAttribute("keyLabel", "Circle Area: Journal Count");
		mapTemplate.setAttribute(
				"keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		mapTemplate.setAttribute(
				"keyLabelFontSize", KEY_LABEL_FONT_SIZE);

		mapTemplate.setAttribute("fontName", FONT_NAME);
		
		
		/* TODO Hack starts here! */
//		String preamble = "0 300 translate /inch {72 mul} def /fontSize 10 def /Garamond findfont fontSize scalefont setfont\n\n";
//		StringTemplate ucsdTemplate =
//			ScienceMapAlgorithm.group.getInstanceOf("ucsd");
		
		return /*preamble + ucsdTemplate.toString() + */legendDefinitionsTemplate.toString()
				+ circleAreaLegendDefinitionsTemplate.toString()
				+ mapTemplate.toString();
//				+ circleAreaLegendTemplate.toString();
	}
	
	private String round(double number) {
		if (number == (int) number) {
			return String.valueOf((int) number);
		} else {
			return String.valueOf(NumberUtilities.roundToNDecimalPlaces(number, 2));
		}
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

	private String nodePostScript(Node node) {
		return nodePostScript(node.getX(), node.getY(), .7, .7, .7, 0.0);
	}

	private String nodePostScript(
			double x, double y, double red, double green, double blue, Double total) {
		double radius = Math.sqrt(total) * RADIUS_MULTIPLIER + MINIMUM_SIZE;
		return "" + x + " " + y + " " + radius + " 1 " + red + " " + green + " " + blue + " node";
	}
	
	private double calculateRadius(double total) {
		return Math.sqrt(total) * RADIUS_MULTIPLIER + MINIMUM_SIZE;//Math.sqrt(area / Math.PI);
	}
}
