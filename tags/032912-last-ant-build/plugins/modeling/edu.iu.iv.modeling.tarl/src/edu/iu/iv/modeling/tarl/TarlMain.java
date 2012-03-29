package edu.iu.iv.modeling.tarl;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.input.InputReader;
import edu.iu.iv.modeling.tarl.input.MainParameters;
import edu.iu.iv.modeling.tarl.input.impl.DefaultInputReader;
import edu.iu.iv.modeling.tarl.input.impl.DefaultMainParameters;
import edu.iu.iv.modeling.tarl.main.TarlHelper;
import edu.iu.iv.modeling.tarl.main.impl.DefaultTarlHelper;
import edu.iu.iv.modeling.tarl.output.GraphGenerator;
import edu.iu.iv.modeling.tarl.output.impl.JUNGGraphGenerator;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.StringLabeller.UniqueLabelException;
import edu.uci.ics.jung.io.PajekNetWriter;

/**
 * 
 * @author ben
 *
 */
public class TarlMain {
  /**
   * Stores an instance of the <code>InputReader</code>
   */
  private InputReader inputReader;

  /**
   * Stores an instance of <code>TarlHelperInterface</code>
   */
  private TarlHelper tarlHelper;

  private MainParameters mainParameters;

  public TarlMain(File inputScriptFile, File agingFunctionFile) {
	  try {
          System.out.print("Initializing...");
		  initialize(inputScriptFile, agingFunctionFile);
          System.out.println("done.");
	  }
	  catch (TarlException te) {
          System.err.println(te.getMessage());
          System.err.println("Cannot continue.");
		  te.printStackTrace();
	  }
  }
  
  /**
   * 
   * @return
   */
  public boolean execute() {
      System.out.print("Running Model...");
      try {
		tarlHelper.runModel();
      } 
      catch (TarlException te) {
        System.err.println(te.getMessage());
        System.err.println("Cannot continue.");
		te.printStackTrace();
      }
      System.out.println("done.");
      System.out.println("Generating Graphs:");

      AuthorManager am = tarlHelper.getTarlExecuter().getAuthorManager();
      PublicationManager pm = tarlHelper.getTarlExecuter()
              .getPublicationManager();
      GraphGenerator gg = new JUNGGraphGenerator(pm, am);
      
      System.out.print("Co-author graph...");
      Graph g = (Graph)gg.generateCoAuthorGraph();
      writePajekNet(g, "co-author-graph.net");
      System.out.println("done.");
      System.out.print("Co-citation graph...");
      g = (Graph)gg.generateCitationGraph();
      writePajekNet(g, "citation-graph.net");
      System.out.println("done.");
      System.out.print("Author-Publication graph...");
      g = (Graph)gg.generateAuthorPublicationGraph();
      writePajekNet(g, "Author-Publication-graph.net");
      System.out.println("done.");
      
      System.out.println("done.");      
      tarlHelper.cleanUpSystem();
      
      return true;
  }
  
  /**
   * 
   */
  private final String KEY_VERTEX_ID = new String("ID");
  
  /**
   * 
   * @param g
   * @param graphName
   */
  private void writePajekNet(Graph g, String graphName) {
	  Set            vertexSet = g.getVertices();
	  
	  StringLabeller stringLabeller = StringLabeller.getLabeller(g);
	  for (Iterator it = vertexSet.iterator(); it.hasNext(); ) {
	    Vertex vertex     = (Vertex) it.next();
	    String vertexName = (String)vertex.getUserDatum(KEY_VERTEX_ID);
	    try {
			stringLabeller.setLabel(vertex, vertexName);
		} catch (UniqueLabelException ule) {
			System.out.println("Error: during applying labels to vertices");
			ule.printStackTrace();
		}
	  }
	  
	  PajekNetWriter pnw = new PajekNetWriter();
	  try {
		pnw.save(g, graphName, stringLabeller, null);
	  } catch (IOException ioe) {
		System.out.println("Error during writing pajek file");
		ioe.printStackTrace();
	  }
  }
  
  /**
   * 
   * @param scriptFile
   * @param agingFunctionFile
   * @throws TarlException
   */
  private void initialize(File scriptFile, File agingFunctionFile) 
                                              throws TarlException {
      inputReader = new DefaultInputReader();
      inputReader.initialize(scriptFile);
      mainParameters = new DefaultMainParameters();
      mainParameters = inputReader.getModelParameters();
      this.tarlHelper = new DefaultTarlHelper();
      this.tarlHelper.initializeModel(mainParameters.getHelperParameters(),
              agingFunctionFile);
  }
  
  /**
   * 
   * @param args
   * @return
   */
  private static boolean parmsOk(String[] args) {
	if (args.length != 2) {
      System.out.println("Error: usage is java TartMain " + 
        "<inputScriptFile> <agingFunctionFile>");
      return false;
	}
	
    try {
      File inputScriptFile   = new File(args[0]);
      File agingFunctionFile = new File(args[1]);
	        
      if (inputScriptFile.exists() && agingFunctionFile.exists()) {
        return true;
      }
      else {
        System.out.println("Error: usage is java TartMain " + 
         		           "<inputScriptFile> <agingFunctionFile>");
        return false;
      }
    }
    catch (SecurityException se) {
      System.err.println(se.getMessage());
      System.err.println("Cannot continue.");
      se.printStackTrace();
      
      return false;
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    if (!parmsOk(args)) {
      System.exit(1);
    }

    //Extract the file names and create a TARL algorithm
    File inputScriptFile   = new File(args[0]);
    File agingFunctionFile = new File(args[1]);
    
    TarlMain tarlAlg = new TarlMain(inputScriptFile, agingFunctionFile);
    tarlAlg.execute();
  }
}
