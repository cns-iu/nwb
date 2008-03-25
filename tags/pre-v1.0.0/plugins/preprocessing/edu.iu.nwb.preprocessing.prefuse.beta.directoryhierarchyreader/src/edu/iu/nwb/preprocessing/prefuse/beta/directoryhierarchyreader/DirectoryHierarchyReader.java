/*
 * Created on Jan 26, 2005
 */
package edu.iu.nwb.preprocessing.prefuse.beta.directoryhierarchyreader;

import java.io.File;
import java.io.FileFilter;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;


/**
 * A Directory Hierarchy Reader that reads a directory (with options) and 
 * creates a Prefuse Graph of it.
 * 
 * @author Shashikant
 */
public class DirectoryHierarchyReader {
	
	
    public static final int INDEFINITE = -1;
    private final static String labelKey = "label";

    private final static FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isFile();
        }};
        
    private final static FileFilter dirFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory();
        }};

    public static Graph readDirectory(File directory, int levels, boolean readFiles) {
        Tree tree = new Tree();
        
        String LABEL = "label";
        Schema LABEL_SCHEMA = new Schema();
        LABEL_SCHEMA.addColumn(LABEL, String.class, "");
        
        tree.addColumns(LABEL_SCHEMA);
        Node rootNode = tree.addRoot();
        readDirectory(rootNode, directory, 0, levels, readFiles);
        return tree;
    }

    private static void readDirectory(Node node, File curDir, int curLevel, int levels, boolean readFiles) {
        
        node.setString(labelKey, curDir.getName());
        
        // if this is the last level, then we need to stop
        if (curLevel == levels) {
            return;
        // otherwise build the sub-tree
        } else {
            if (readFiles) {
                File[] fileList = curDir.listFiles(fileFilter);
                
                //if they don't have permission then it'll be null, 
                //so just make it so there is no files in the dir
                if (fileList == null) fileList = new File[] {};
                
                for (int i = 0; i < fileList.length; ++i) {
                    Node tnFile = ((Tree) node.getGraph()).addChild(node);
                    tnFile.setString(labelKey, fileList[i].getName());
                }
            }
            File[] dirList = curDir.listFiles(dirFilter);
            
            //if they don't have permission then it'll be null, 
            //so just make it so there is no dirs in the dir
            if (dirList == null) dirList = new File[] {};
            
            for (int i = 0; i < dirList.length; ++i) {
            	Node tnDir = ((Tree) node.getGraph()).addChild(node);
                readDirectory(tnDir, dirList[i], curLevel + 1, levels, readFiles);
            }
        }
    }
}