package edu.iu.nwb.converter.pajekmat.common;

/*
 * The .net file format for Pajek network files contains certain preserved characters
 * and attributes. This file defines those for use in applications that access
 * Pajek .net files. For now, we will focus on supporting only the most simple
 * .net files, but, in the future we will have full support.
 * 
 * Written by: Tim Kelley
 * Date: May 16, 2007
 */

public class MATFileProperty {
	public static final String PRESERVED_STAR = "*";
    public static final String HEADER_VERTICES = PRESERVED_STAR+"vertices";     
    public static final String HEADER_EDGES = PRESERVED_STAR+"edges";
    public static final String HEADER_ARCS = PRESERVED_STAR+"arcs";
    public static final String HEADER_MATRIX = PRESERVED_STAR+"matrix";
    
    public static final String PREFIX_COMMENTS ="%";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_LABEL = "label";
    public static final String ATTRIBUTE_SOURCE = "source";
    public static final String ATTRIBUTE_TARGET = "target";
    public static final String ATTRIBUTE_WEIGHT = "weight";
    
    
    public static final String TYPE_INT = "int";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_FLOAT = "float";        
     
//    public static final String HEADER_NODE_ATTRIBUTES = "*NodeAttributes";
//    public static final String HEADER_EDGE_ATTRIBUTES = "*EdgeAttributes";
   
    public static final String MAT_MIME_TYPE = "file:application/pajekmat";
    public static final String MAT_FILE_TYPE = "file-ext:mat";
    
}
