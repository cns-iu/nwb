package edu.iu.nwb.util.nwbfile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class NWBFileProperty {
	public static final String PRESERVED_STAR = "*";
    public static final String HEADER_NODE = PRESERVED_STAR+"Nodes";     
    public static final String HEADER_UNDIRECTED_EDGES = PRESERVED_STAR+"UndirectedEdges";
    public static final String HEADER_DIRECTED_EDGES = PRESERVED_STAR+"DirectedEdges";
    
    public static final String PREFIX_COMMENTS ="#";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_SOURCE = "source";
    public static final String ATTRIBUTE_TARGET = "target";
    public static final String ATTRIBUTE_LABEL = "label";
    
    public static final String TYPE_INT = "int";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_FLOAT = "float";  
    public static final String TYPE_REAL = "real"; // XXX: not documented at http://wiki.cns.iu.edu/display/CISHELL/NWB+%28.nwb%29
    
    public static final Collection<String> NUMERIC_ATTRIBUTE_TYPES = Collections.unmodifiableList(
    	Arrays.asList(TYPE_INT, TYPE_FLOAT, TYPE_REAL));
    
    public static final Collection<String> ALL_ATTRIBUTE_TYPES = Collections.unmodifiableList(
		Arrays.asList(TYPE_INT, TYPE_STRING, TYPE_FLOAT, TYPE_REAL));
     
//    public static final String HEADER_NODE_ATTRIBUTES = "*NodeAttributes";
//    public static final String HEADER_EDGE_ATTRIBUTES = "*EdgeAttributes";
    
    public static final String NWB_MIME_TYPE = "file:text/nwb";
    public static final String NWB_FILE_TYPE = "file-ext:nwb";

    public static final LinkedHashMap<String, String> NECESSARY_NODE_ATTRIBUTES =
    	formNecessaryNodeAttributes();

    private static LinkedHashMap<String, String> formNecessaryNodeAttributes() {
		/* It's very important that the implementation is LinkedHashMap,
		 * as this preserves key order according to insertion order.
		 * An unordered map could violate the schema specification that
		 * ATTRIBUTE_ID must come before ATTRIBUTE_LABEL.
		 * By the same reasoning, you must not re-order the insertions below.
		 */
		LinkedHashMap<String, String> necessaryNodeAttributes =
			new LinkedHashMap<String, String>();
		necessaryNodeAttributes.put(NWBFileProperty.ATTRIBUTE_ID, NWBFileProperty.TYPE_INT);
		necessaryNodeAttributes.put(NWBFileProperty.ATTRIBUTE_LABEL, NWBFileProperty.TYPE_STRING);

		return new LinkedHashMap<String, String>(
			Collections.unmodifiableMap(necessaryNodeAttributes));
	}
    
	public static final LinkedHashMap<String, String> NECESSARY_EDGE_ATTRIBUTES =
		formNecessaryEdgeAttributes();

	private static LinkedHashMap<String, String> formNecessaryEdgeAttributes() {
		/* It's very important that the implementation is LinkedHashMap,
		 * as this preserves key order according to insertion order.
		 * An unordered map could violate the schema specification that
		 * ATTRIBUTE_SOURCE must come before ATTRIBUTE_TARGET.
		 * By the same reasoning, you must not re-order the insertions below.
		 */
		LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
		m.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
		m.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);

		return new LinkedHashMap<String, String>(Collections.unmodifiableMap(m));
	}
}
