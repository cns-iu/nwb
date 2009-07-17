package edu.iu.scipolicy.analysis.blondelcommunitydetection.algorithmstages;

// This is the third stage of the Blondel Community Detection algorithm.
// The input to this stage is the output of the second stage, which is a tree
// file.
// The input tree file consists of pairs of integer, where each first
// integer is a node index and each second integer is the index of the
// community that it belongs to.  Each node index of 0 indicates a new
// community level.
// For a given community level, each node is actually a community of the
// previous level.  The exception to this rule is the first level, for which
// each node is an actual node on the original network.
// The input tree file is parsed and mapped to the nodes on the original input
// network.  The result of this mapping is a new network with nodes that are
// annotated to have attributes that specify which communities they belong to
// at various community levels.  This new network is output as a new NWB
// file.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.scipolicy.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.scipolicy.analysis.blondelcommunitydetection.TreeFileParsingException;
import edu.iu.scipolicy.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes.NWBAndTreeFileMergingException;
import edu.iu.scipolicy.analysis.blondelcommunitydetection.nwbfileparserhandlers.tree_to_nwb.Merger;

public class NWBAndTreeFilesMerger {
	public static File mergeCommunitiesFileWithNWBFile(File communitiesFile,
													   File nwbFile,
													   NetworkInfo networkInfo)
    		throws NWBAndTreeFileMergingException {
    	try {
    		File outputNWBFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    					"blondel-nwb-", "nwb");
    		Merger merger = new Merger(
    			communitiesFile, outputNWBFile, networkInfo);
    		
    		NWBFileParser fileParser = new NWBFileParser(nwbFile);
    		fileParser.parse(merger);
    		
    		return outputNWBFile;
    	}
    	catch (FileNotFoundException fileNotFoundException) {
    		throw new NWBAndTreeFileMergingException(fileNotFoundException);
    	}
    	catch (IOException ioException) {
    		throw new NWBAndTreeFileMergingException(ioException);
    	}
    	catch (ParsingException parsingException) {
    		throw new NWBAndTreeFileMergingException(parsingException);
    	}
    	catch (TreeFileParsingException treeFileParsingException) {
    		throw new NWBAndTreeFileMergingException(treeFileParsingException);
    	}
    }
}