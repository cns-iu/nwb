/*
 * Created on Jan 26, 2005
 */
package edu.iu.iv.preprocessing.directoryhierarchyreader;

import java.io.File;
import java.io.FileFilter;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

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
        Tree t = new DefaultTree();
        TreeNode rootNode = readDirectory(directory, 0, levels, readFiles);
        t.setRoot(rootNode);
        return t;
    }

    private static TreeNode readDirectory(File curDir, int curLevel, int levels, boolean readFiles) {
        // make a tree node of the current directory
        TreeNode curDirTreeNode = new DefaultTreeNode();
        curDirTreeNode.setAttribute(labelKey, curDir.getName());
        
        // if this is the last level, then we need to stop
        // and return the tree node for the current directory.
        if (curLevel == levels)
            return curDirTreeNode;
        // otherwise build the sub-tree and return that.
        else {
            if (readFiles) {
                File[] fileList = curDir.listFiles(fileFilter);
                
                //if they don't have permission then it'll be null, 
                //so just make it so there is no files in the dir
                if (fileList == null) fileList = new File[] {};
                
                for (int i = 0; i < fileList.length; ++i) {
                    TreeNode tnFile = new DefaultTreeNode();
                    tnFile.setAttribute(labelKey, fileList[i].getName());
                    curDirTreeNode.addChild(new DefaultEdge(curDirTreeNode,
                            tnFile));
                }
            }
            File[] dirList = curDir.listFiles(dirFilter);
            
            //if they don't have permission then it'll be null, 
            //so just make it so there is no dirs in the dir
            if (dirList == null) dirList = new File[] {};
            
            for (int i = 0; i < dirList.length; ++i) {
                TreeNode tnDir = readDirectory(dirList[i], curLevel + 1, levels, readFiles);
                curDirTreeNode.addChild(new DefaultEdge(curDirTreeNode, tnDir));
            }
            return curDirTreeNode;
        }
    }
}