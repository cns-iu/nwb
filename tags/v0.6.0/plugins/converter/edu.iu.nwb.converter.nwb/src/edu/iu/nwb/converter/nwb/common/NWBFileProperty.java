package edu.iu.nwb.converter.nwb.common;

public class NWBFileProperty {
	public static final String PRESERVED_STAR = "*";
    public static final String HEADER_NODE = PRESERVED_STAR+"Nodes";     
    public static final String HEADER_UNDIRECTED_EDGES = PRESERVED_STAR+"UndirectedEdges";
    public static final String HEADER_DIRECTED_EDGES = PRESERVED_STAR+"DirectedEdges";
    
    public static final String PREFIX_COMMENTS ="#";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_SOURCE = "source";
    public static final String ATTRIBUTE_TARGET = "target";
    
    public static final String TYPE_INT = "int";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_FLOAT = "float";        
     
//    public static final String HEADER_NODE_ATTRIBUTES = "*NodeAttributes";
//    public static final String HEADER_EDGE_ATTRIBUTES = "*EdgeAttributes";
    
    public static final String NWB_MIME_TYPE = "file:text/nwb";
    public static final String NWB_FILE_TYPE = "file-ext:nwb";
    
}
