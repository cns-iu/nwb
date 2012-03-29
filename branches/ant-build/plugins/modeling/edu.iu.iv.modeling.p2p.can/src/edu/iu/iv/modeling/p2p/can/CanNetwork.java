package edu.iu.iv.modeling.p2p.can;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import edu.uci.ics.jung.io.GraphMLFile;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.GraphDraw;

/**
 * This class implements the CAN network modelling algorithm
 * @author Hardik Sheth (hsheth@indiana.edu)  
 */
public class CanNetwork {
	private int networkSize;
	private static Graph g = null;
	static GraphDraw gd;
	private float avgDegree;
	private int minDegree, maxDegree;
	private LogService logger;
	private String failReason;
	
	/**
	 * Constructor for CanNetwork. Initializes an UndirectedSparseGraph and sets the network size
	 * @param networkSize
	 */
	public CanNetwork(int networkSize, LogService logger) {
		this.networkSize = networkSize;
		g = new UndirectedSparseGraph();
		minDegree = maxDegree = 0;
		avgDegree = 0;
		this.logger = logger;
		failReason = null;
	}
	
	/**
	 * This method returns the Graph object built on CAN network
	 * @return graph 
	 */
	public Graph getGraph() {
		return g;
	}
	
	/**
	* This method builds the network based on CAN topology
	* @return boolean
	*/
	public boolean buildCanNetwork() {
		if (networkSize == 0) {
		    failReason = "Invalid network size!";
			logger.log(LogService.LOG_ERROR, failReason);
			return false;
		}
		Random ranNum = new Random();
		int CurrentId = 1;
		int home;
		double xvalue, yvalue, zvalue;
		UndirectedSparseVertex  tempNode, newNode, oldNode;
		
		// Add the first node in the graph
		UndirectedSparseVertex initialNode = new UndirectedSparseVertex();
		initialNode.setUserDatum("id", new Integer(0), UserData.SHARED);
		initialNode.setUserDatum("xmin", new Double(0.0), UserData.SHARED);
		initialNode.setUserDatum("ymin", new Double(0.0), UserData.SHARED);
		initialNode.setUserDatum("zmin", new Double(0.0), UserData.SHARED);
		initialNode.setUserDatum("xmax", new Double(1.0), UserData.SHARED);
		initialNode.setUserDatum("ymax", new Double(1.0), UserData.SHARED);
		initialNode.setUserDatum("zmax", new Double(1.0), UserData.SHARED);
		initialNode.setUserDatum("xmid", new Double(0.5), UserData.SHARED);
		initialNode.setUserDatum("ymid", new Double(0.5), UserData.SHARED);
		initialNode.setUserDatum("zmid", new Double(0.5), UserData.SHARED);
		g.addVertex(initialNode);

		// Associate a node to an id to retrieve node based on id in future
		g.setUserDatum("0", initialNode, UserData.SHARED);
		
		// Iterate for additional nodes, splitting the region every time
		while (CurrentId < networkSize) {
			xvalue = ranNum.nextDouble();
			yvalue = ranNum.nextDouble();
			zvalue = ranNum.nextDouble();
			home = -1;			
			Set nodes = g.getVertices();			
			for (int i=0; i < nodes.size(); i++) {
				 if (home > -1)
					 break;					 
				 tempNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());	
				 if (xvalue > ((Double) tempNode.getUserDatum("xmin")).doubleValue() && xvalue < ((Double) tempNode.getUserDatum("xmax")).doubleValue())
					 if (yvalue > ((Double) tempNode.getUserDatum("ymin")).doubleValue() && yvalue < ((Double) tempNode.getUserDatum("ymax")).doubleValue())
						if (zvalue > ((Double) tempNode.getUserDatum("zmin")).doubleValue() && zvalue < ((Double) tempNode.getUserDatum("zmax")).doubleValue())
							 home = i;
			 } 

			// If home not found, Error...
			if(home == -1)
			 {
			    failReason = "Node can't find its region!! node: " + CurrentId;
			    logger.log(LogService.LOG_ERROR, failReason);
			 	
				 return false;
			 }

			// Add the new node with id = CurrentId
			newNode = new UndirectedSparseVertex();
			newNode.setUserDatum("id", new Integer(CurrentId), UserData.SHARED);
			newNode.setUserDatum("xmin", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("ymin", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("zmin", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("xmax", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("ymax", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("zmax", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("xmid", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("ymid", new Double(0.0), UserData.SHARED);
			newNode.setUserDatum("zmid", new Double(0.0), UserData.SHARED);			
			
			g.addVertex(newNode);
			g.setUserDatum(new Integer(CurrentId).toString(), newNode, UserData.SHARED);
			// System.out.println("Added Node "+CurrentId);			 
						
			oldNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(home).toString());					 
			splitZone(oldNode, newNode, xvalue, yvalue, zvalue);
			fixNeighbors(oldNode, newNode);
						  
			 CurrentId++;
		}
		
		calculateStats();	
		return true;

	} //end of function buildNetwork
	
	
/**
 * This method splits the region of oldNode into two along the longer dimension and give one half to the newNode
 * @param oldNode
 * @param newNode
 * @param xvalue
 * @param yvalue
 * @param zvalue
 */
	private void splitZone(UndirectedSparseVertex oldNode, UndirectedSparseVertex newNode, double x, double y, double z)
		 {
			double xdiff, ydiff, zdiff;

			xdiff = ((Double) oldNode.getUserDatum("xmax")).doubleValue() - ((Double) oldNode.getUserDatum("xmin")).doubleValue();
			ydiff = ((Double) oldNode.getUserDatum("ymax")).doubleValue() - ((Double) oldNode.getUserDatum("ymin")).doubleValue();
			zdiff = ((Double) oldNode.getUserDatum("zmax")).doubleValue() - ((Double) oldNode.getUserDatum("zmin")).doubleValue();
						 
			if(xdiff == Math.max(zdiff, Math.max(xdiff, ydiff))) // Split on x-axis
			  {
				  if(((Double) oldNode.getUserDatum("xmid")).doubleValue() < x)
				   {
					   newNode.setUserDatum("xmin", (Double) oldNode.getUserDatum("xmid"), UserData.SHARED);
					   newNode.setUserDatum("xmax", (Double) oldNode.getUserDatum("xmax"), UserData.SHARED);
					   oldNode.setUserDatum("xmax", (Double) newNode.getUserDatum("xmin"), UserData.SHARED);
 
					   newNode.setUserDatum("ymax", (Double) oldNode.getUserDatum("ymax"), UserData.SHARED );
					   newNode.setUserDatum("ymin", (Double) oldNode.getUserDatum("ymin"), UserData.SHARED );
					   newNode.setUserDatum("zmax", (Double) oldNode.getUserDatum("zmax"), UserData.SHARED );
					   newNode.setUserDatum("zmin", (Double) oldNode.getUserDatum("zmin"), UserData.SHARED );

					   newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
										    ((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 
					   newNode.setUserDatum("ymid",  new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
											((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );
 
					   newNode.setUserDatum("zmid",  new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															   + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
																((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
																
					   oldNode.setUserDatum("xmid",  new Double ( ((Double) oldNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) oldNode.getUserDatum("xmax")).doubleValue() - 
											((Double) oldNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
				   }
				  else // (oldNode.getUserDatum("xmid")).doubleValue() >= x)
				   {
					   newNode.setUserDatum("xmin",  (Double) oldNode.getUserDatum("xmin"), UserData.SHARED );
					   newNode.setUserDatum("xmax",  (Double) oldNode.getUserDatum("xmid"), UserData.SHARED );
					   oldNode.setUserDatum("xmin",  (Double) newNode.getUserDatum("xmax"), UserData.SHARED );
 
					   newNode.setUserDatum("ymax",  (Double) oldNode.getUserDatum("ymax"), UserData.SHARED);
					   newNode.setUserDatum("ymin",  (Double) oldNode.getUserDatum("ymin"), UserData.SHARED );
					   newNode.setUserDatum("zmax",  (Double) oldNode.getUserDatum("zmax"), UserData.SHARED);
					   newNode.setUserDatum("zmin",  (Double) oldNode.getUserDatum("zmin"), UserData.SHARED );
										   
					   newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
											((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 
					   newNode.setUserDatum("ymid", new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
											((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );
											
					   newNode.setUserDatum("zmid", new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															   + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
																((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
                                                                                               
					   oldNode.setUserDatum("xmid",  new Double ( ((Double) oldNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) oldNode.getUserDatum("xmax")).doubleValue() - 
											((Double) oldNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
				   }
			  }			  
			  else if (ydiff == Math.max(zdiff, Math.max(xdiff, ydiff))) // Split on y-axis
			  {
				  if(((Double) oldNode.getUserDatum("ymid")).doubleValue() < y)
				   {
					   newNode.setUserDatum("ymin", (Double) oldNode.getUserDatum("ymid"), UserData.SHARED );
					   newNode.setUserDatum("ymax",  (Double) oldNode.getUserDatum("ymax"), UserData.SHARED );
					   oldNode.setUserDatum("ymax",  (Double) newNode.getUserDatum("ymin"), UserData.SHARED );
 
					   newNode.setUserDatum("xmax",  (Double) oldNode.getUserDatum("xmax"), UserData.SHARED );
					   newNode.setUserDatum("xmin",  (Double) oldNode.getUserDatum("xmin"), UserData.SHARED );
						newNode.setUserDatum("zmax",  (Double) oldNode.getUserDatum("zmax"), UserData.SHARED );
						newNode.setUserDatum("zmin",  (Double) oldNode.getUserDatum("zmin"), UserData.SHARED );
	
					   newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
											((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 
					   newNode.setUserDatum("ymid",  new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
											((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );

					   newNode.setUserDatum("zmid",  new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															   + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
																((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 	
					   oldNode.setUserDatum("ymid",  new Double ( ((Double) oldNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) oldNode.getUserDatum("ymax")).doubleValue() - 
											((Double) oldNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );
				   }
				  else // (oldNode.getUserDatum("ymid")).doubleValue() >= y)
				   {
					   newNode.setUserDatum("ymin",  (Double) oldNode.getUserDatum("ymin"), UserData.SHARED);
					   newNode.setUserDatum("ymax",  (Double) oldNode.getUserDatum("ymid"), UserData.SHARED);
					   oldNode.setUserDatum("ymin",  (Double) newNode.getUserDatum("ymax"), UserData.SHARED);
 
					   newNode.setUserDatum("xmax",  (Double) oldNode.getUserDatum("xmax"), UserData.SHARED );
					   newNode.setUserDatum("xmin",  (Double) oldNode.getUserDatum("xmin"), UserData.SHARED );
					   newNode.setUserDatum("zmax",  (Double) oldNode.getUserDatum("zmax"), UserData.SHARED );
					   newNode.setUserDatum("zmin",  (Double) oldNode.getUserDatum("zmin"), UserData.SHARED );

					   newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
											((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
                                                                                                  
					   newNode.setUserDatum("ymid",  new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
											((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );

					   newNode.setUserDatum("zmid",  new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															   + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
																((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
																
					   oldNode.setUserDatum("ymid",  new Double ( ((Double) oldNode.getUserDatum("ymin")).doubleValue()
										   + ( (((Double) oldNode.getUserDatum("ymax")).doubleValue() - 
											((Double) oldNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );
				   }
			  }		
			else // zdiff is max
			{
				if(((Double) oldNode.getUserDatum("zmid")).doubleValue() < z)
				 {
					 newNode.setUserDatum("zmin", (Double) oldNode.getUserDatum("zmid"), UserData.SHARED );
					 newNode.setUserDatum("zmax",  (Double) oldNode.getUserDatum("zmax"), UserData.SHARED );
					 oldNode.setUserDatum("zmax",  (Double) newNode.getUserDatum("zmin"), UserData.SHARED );
 
					 newNode.setUserDatum("xmax",  (Double) oldNode.getUserDatum("xmax"), UserData.SHARED );
					 newNode.setUserDatum("xmin",  (Double) oldNode.getUserDatum("xmin"), UserData.SHARED );
					  newNode.setUserDatum("ymax",  (Double) oldNode.getUserDatum("ymax"), UserData.SHARED );
					  newNode.setUserDatum("ymin",  (Double) oldNode.getUserDatum("ymin"), UserData.SHARED );
	
					 newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										 + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
										  ((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 
					 newNode.setUserDatum("ymid",  new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										 + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
										  ((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );

					 newNode.setUserDatum("zmid",  new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															 + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
															  ((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
 	
					 oldNode.setUserDatum("zmid",  new Double ( ((Double) oldNode.getUserDatum("zmin")).doubleValue()
										 + ( (((Double) oldNode.getUserDatum("zmax")).doubleValue() - 
										  ((Double) oldNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
				 }
				else // (oldNode.getUserDatum("zmid")).doubleValue() >= z)
				 {
					 newNode.setUserDatum("zmin",  (Double) oldNode.getUserDatum("zmin"), UserData.SHARED);
					 newNode.setUserDatum("zmax",  (Double) oldNode.getUserDatum("zmid"), UserData.SHARED);
					 oldNode.setUserDatum("zmin",  (Double) newNode.getUserDatum("zmax"), UserData.SHARED);
 
					 newNode.setUserDatum("xmax",  (Double) oldNode.getUserDatum("xmax"), UserData.SHARED );
					 newNode.setUserDatum("xmin",  (Double) oldNode.getUserDatum("xmin"), UserData.SHARED );
					 newNode.setUserDatum("ymax",  (Double) oldNode.getUserDatum("ymax"), UserData.SHARED );
					 newNode.setUserDatum("ymin",  (Double) oldNode.getUserDatum("ymin"), UserData.SHARED );

					 newNode.setUserDatum("xmid",  new Double ( ((Double) newNode.getUserDatum("xmin")).doubleValue()
										 + ( (((Double) newNode.getUserDatum("xmax")).doubleValue() - 
										  ((Double) newNode.getUserDatum("xmin")).doubleValue()) / 2.0) ), UserData.SHARED );
                                                                                                  
					 newNode.setUserDatum("ymid",  new Double ( ((Double) newNode.getUserDatum("ymin")).doubleValue()
										 + ( (((Double) newNode.getUserDatum("ymax")).doubleValue() - 
										  ((Double) newNode.getUserDatum("ymin")).doubleValue()) / 2.0) ), UserData.SHARED );

					 newNode.setUserDatum("zmid",  new Double ( ((Double) newNode.getUserDatum("zmin")).doubleValue()
															 + ( (((Double) newNode.getUserDatum("zmax")).doubleValue() - 
															  ((Double) newNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
																
					 oldNode.setUserDatum("zmid",  new Double ( ((Double) oldNode.getUserDatum("zmin")).doubleValue()
										 + ( (((Double) oldNode.getUserDatum("zmax")).doubleValue() - 
										  ((Double) oldNode.getUserDatum("zmin")).doubleValue()) / 2.0) ), UserData.SHARED );
				 }
  			}

		 } //end of function splitZone
		 
		 
/**
 * This method fixes the neighbors after the region is split. Updates all the neighbors of oldNode, oldNode itself and the newNode
 * @param oldNode
 * @param newNode
 */
	private void fixNeighbors(UndirectedSparseVertex oldNode, UndirectedSparseVertex newNode)
	 {
		UndirectedSparseVertex temp;
		Set node_neighbors;
		Iterator iter;
        
		if(((Double)oldNode.getUserDatum("xmin")).doubleValue() < ((Double)newNode.getUserDatum("xmin")).doubleValue())
		 {
			 // check if newID.getUserDatum(xmax) == 1
			 // if yes, check for old neighbors of oldID
			 // with x.min = 0, and connect, remove these
			 // neighbors from oldID, and then do normal update
			 if(((Double)newNode.getUserDatum("xmax")).doubleValue() == 1.0)
			  {
				  node_neighbors = oldNode.getNeighbors();
				  iter = node_neighbors.iterator();
				  while (iter.hasNext())
				   {
					   temp = (UndirectedSparseVertex) iter.next();
					   		if( ((Double)temp.getUserDatum("xmin")).doubleValue() == 0.0
							&& overlapD(((Double)newNode.getUserDatum("ymin")).doubleValue(), ((Double)newNode.getUserDatum("ymax")).doubleValue(),
									((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue()) 
							&& overlapD(((Double)newNode.getUserDatum("zmin")).doubleValue(), ((Double)newNode.getUserDatum("zmax")).doubleValue(),
									((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue()) ) 
							 {
								 g.addEdge( new UndirectedSparseEdge(newNode, temp) );
								 if(((Double)oldNode.getUserDatum("xmin")).doubleValue() != ((Double)temp.getUserDatum("xmax")).doubleValue()
									&& ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue()) 
								  {								  	  
									  	g.removeEdge( temp.findEdge(oldNode) );									  
								  }
							 }						
				   }
			  }
                
			 updateThem(oldNode, newNode, 1);
		 }
		else if(((Double)oldNode.getUserDatum("xmin")).doubleValue() > ((Double)newNode.getUserDatum("xmin")).doubleValue())
		 {
			 // check if newID.getUserDatum(xmin) == 0
			 // if yes, check for old neighbors of oldID
			 // with x.max = 1, and connect, remove these
			 // neighbors from oldID, and then do normal update
			 if(((Double)newNode.getUserDatum("xmin")).doubleValue() == 0.0)
			  {
				node_neighbors = oldNode.getNeighbors();
				iter = node_neighbors.iterator();
				while (iter.hasNext())
				 {
						temp = (UndirectedSparseVertex) iter.next();
							if( ((Double)temp.getUserDatum("xmax")).doubleValue() == 1.0
							&& overlapD(((Double)newNode.getUserDatum("ymin")).doubleValue(), ((Double)newNode.getUserDatum("ymax")).doubleValue(),
									((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue()) 
							&& overlapD(((Double)newNode.getUserDatum("zmin")).doubleValue(), ((Double)newNode.getUserDatum("zmax")).doubleValue(),
									((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue()) )
							 {
								 g.addEdge( new UndirectedSparseEdge(newNode, temp) );
								 if(((Double)oldNode.getUserDatum("xmax")).doubleValue() != ((Double)temp.getUserDatum("xmin")).doubleValue()
									&& ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue())
								  {									
									 	g.removeEdge( temp.findEdge(oldNode) );
								  }
							 }
				   }
			  }
                
			 updateThem(oldNode, newNode, 1);
		 }
		else if(((Double)oldNode.getUserDatum("ymin")).doubleValue() < ((Double)newNode.getUserDatum("ymin")).doubleValue())
		 {
			 // check if newID.getUserDatum("ymax")).doubleValue() == 1
			 // if yes, check for old neighbors of oldID
			 // with y.min = 0, and connect, remove these
			 // neighbors from oldID, and then do normal update
			 if(((Double)newNode.getUserDatum("ymax")).doubleValue() == 1.0)
			  {
				node_neighbors = oldNode.getNeighbors();
				iter = node_neighbors.iterator();
				while (iter.hasNext())
				{
					  temp = (UndirectedSparseVertex) iter.next();
						   if( ((Double)temp.getUserDatum("ymin")).doubleValue() == 0.0
							  && overlapD(((Double)newNode.getUserDatum("xmin")).doubleValue(), ((Double)newNode.getUserDatum("xmax")).doubleValue(),
									((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue()) 
							  && overlapD(((Double)newNode.getUserDatum("zmin")).doubleValue(), ((Double)newNode.getUserDatum("zmax")).doubleValue(),
									((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue()) )
							{
								g.addEdge( new UndirectedSparseEdge(newNode, temp) );
								if(((Double)oldNode.getUserDatum("ymin")).doubleValue() != ((Double)temp.getUserDatum("ymax")).doubleValue()
								   && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue())
								 {									
										g.removeEdge( temp.findEdge(oldNode) );
								 }
							}					   
				  }
			 }
        
			 updateThem(oldNode, newNode, 2);
		 }
		else if(((Double)oldNode.getUserDatum("ymin")).doubleValue() > ((Double)newNode.getUserDatum("ymin")).doubleValue())
		 {
			 // check if newID.getUserDatum("ymin")).doubleValue() == 0
			 // if yes, check for old neighbors of oldID
			 // with y.max = 1, and connect, remove these
			 // neighbors from oldID, and then do normal update
			 if(((Double)newNode.getUserDatum("ymin")).doubleValue() == 0.0)
			  {
				node_neighbors = oldNode.getNeighbors();
				iter = node_neighbors.iterator();
				while (iter.hasNext())
				{
					   temp = (UndirectedSparseVertex) iter.next();
							if( ((Double)temp.getUserDatum("ymax")).doubleValue() == 1.0
							&& overlapD(((Double)newNode.getUserDatum("xmin")).doubleValue(), ((Double)newNode.getUserDatum("xmax")).doubleValue(),
								((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue()) 
							&& overlapD(((Double)newNode.getUserDatum("zmin")).doubleValue(), ((Double)newNode.getUserDatum("zmax")).doubleValue(),
								((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue()) )								
							 {
								g.addEdge( new UndirectedSparseEdge(newNode, temp) );
								 if(((Double)oldNode.getUserDatum("ymax")).doubleValue() != ((Double)temp.getUserDatum("ymin")).doubleValue()
									&& ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue())
								  {									
										g.removeEdge( temp.findEdge(oldNode) );
								  }
							 }						
				   }
			  }
 
			 updateThem(oldNode, newNode, 2);
		 }		
		else if(((Double)oldNode.getUserDatum("zmin")).doubleValue() < ((Double)newNode.getUserDatum("zmin")).doubleValue())
		{
					  // check if newID.zmax == 1
					  // if yes, check for old neighbors of oldID
					  // with z.min = 0, and connect, remove these
					  // neighbors from oldID, and then do normal update
			if(((Double)newNode.getUserDatum("zmax")).doubleValue() == 1.0)
			 {
			   node_neighbors = oldNode.getNeighbors();
			   iter = node_neighbors.iterator();
			   while (iter.hasNext())
			   {
					 temp = (UndirectedSparseVertex) iter.next();
						  if( ((Double)temp.getUserDatum("zmin")).doubleValue() == 0.0
							 && overlapD(((Double)newNode.getUserDatum("xmin")).doubleValue(), ((Double)newNode.getUserDatum("xmax")).doubleValue(),
								   ((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue()) 
							 && overlapD(((Double)newNode.getUserDatum("ymin")).doubleValue(), ((Double)newNode.getUserDatum("ymax")).doubleValue(),
								   ((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue()) )
						   {
							   g.addEdge( new UndirectedSparseEdge(newNode, temp) );
							   if(((Double)oldNode.getUserDatum("zmin")).doubleValue() != ((Double)temp.getUserDatum("zmax")).doubleValue()
								  && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue())
								{									
									   g.removeEdge( temp.findEdge(oldNode) );
								}
						   }					   
				 }
			}
        
			updateThem(oldNode, newNode, 3);
		}
		else // if(((Double)oldNode.getUserDatum("zmin")).doubleValue() > ((Double)newNode.getUserDatum("zmin")).doubleValue())
		 {
			 // check if newID.getUserDatum("zmin")).doubleValue() == 0
			 // if yes, check for old neighbors of oldID
			 // with z.max = 1, and connect, remove these
			 // neighbors from oldID, and then do normal update
			 if(((Double)newNode.getUserDatum("zmin")).doubleValue() == 0.0)
			  {
				node_neighbors = oldNode.getNeighbors();
				iter = node_neighbors.iterator();
				while (iter.hasNext())
				{
					   temp = (UndirectedSparseVertex) iter.next();
							if( ((Double)temp.getUserDatum("zmax")).doubleValue() == 1.0
							&& overlapD(((Double)newNode.getUserDatum("xmin")).doubleValue(), ((Double)newNode.getUserDatum("xmax")).doubleValue(),
								((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue()) 
							&& overlapD(((Double)newNode.getUserDatum("ymin")).doubleValue(), ((Double)newNode.getUserDatum("ymax")).doubleValue(),
								((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue()) )								
							 {
								g.addEdge( new UndirectedSparseEdge(newNode, temp) );
//								System.out.println("Adding "+newNode.getUserDatum("id")+" "+temp.getUserDatum("id"));
								 if(((Double)oldNode.getUserDatum("zmax")).doubleValue() != ((Double)temp.getUserDatum("zmin")).doubleValue()
									&& ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)oldNode.getUserDatum("id")).intValue() && ((Integer)temp.getUserDatum("id")).intValue() != ((Integer)newNode.getUserDatum("id")).intValue())
								  {									
										g.removeEdge( temp.findEdge(oldNode) );
								  }
							 }						
				   }
			  }
 
			 updateThem(oldNode, newNode, 3);
		 }		
		 
		g.addEdge( new UndirectedSparseEdge(newNode, oldNode) );		
	 } //end of function fixNeighbors
	 
	 

	/**
	 * This method is a helper for fixNeighbors method. Updates the edges given the dimension alongwhich to split 
	 * @param oldNode
	 * @param newNode
	 * @param split dimension
	 */
	private void updateThem(UndirectedSparseVertex oldNode, UndirectedSparseVertex newNode, int split)
	 {
	 	 UndirectedSparseVertex temp;
	 	 
			for(int i=0; i<networkSize; i++) {			
				temp = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());
				if (oldNode.isNeighborOf(temp)) 
				{				
			  	   if(split == 1)  // x-split
					{
						if(overlap(((Double)newNode.getUserDatum("xmin")).doubleValue(),((Double)newNode.getUserDatum("xmax")).doubleValue(),
								((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue())) // overlap with newID
						 {
							g.addEdge( new UndirectedSparseEdge(newNode, temp) );	
						 }
 
						if(!(overlap(((Double)oldNode.getUserDatum("xmin")).doubleValue(), ((Double)oldNode.getUserDatum("xmax")).doubleValue(),
								((Double)temp.getUserDatum("xmin")).doubleValue(), ((Double)temp.getUserDatum("xmax")).doubleValue()))) // no overlap with oldID
						 {							
								g.removeEdge(temp.findEdge(oldNode));
						 }						 
					}
				   else if(split == 2) // y-split
					{
						if(overlap(((Double)newNode.getUserDatum("ymin")).doubleValue(),((Double)newNode.getUserDatum("ymax")).doubleValue(),
								   ((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue())) // overlap with newID
						 {
							g.addEdge( new UndirectedSparseEdge(newNode, temp) );							 
						 }
 
						if(!(overlap(((Double)oldNode.getUserDatum("ymin")).doubleValue(), ((Double)oldNode.getUserDatum("ymax")).doubleValue(),
									((Double)temp.getUserDatum("ymin")).doubleValue(), ((Double)temp.getUserDatum("ymax")).doubleValue()))) // no overlap with oldID
						 {
								g.removeEdge( temp.findEdge(oldNode) );							 
						 }
					}
				   else // z-split
					{
						if(overlap(((Double)newNode.getUserDatum("zmin")).doubleValue(),((Double)newNode.getUserDatum("zmax")).doubleValue(),
								   ((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue())) // overlap with newID
						 {
							g.addEdge( new UndirectedSparseEdge(newNode, temp) );							 
						 }
 
						if(!(overlap(((Double)oldNode.getUserDatum("zmin")).doubleValue(), ((Double)oldNode.getUserDatum("zmax")).doubleValue(),
									((Double)temp.getUserDatum("zmin")).doubleValue(), ((Double)temp.getUserDatum("zmax")).doubleValue()))) // no overlap with oldID
						 {
								g.removeEdge( temp.findEdge(oldNode) );							 
						 }
					}
				}
			 }		  
	 } // end of function updateThem
	 
	 
	/**
	 * This method is a helper for updateThem method. Checks if the regions are overlapping or are equal.
	 * @param newmin
	 * @param newmax
	 * @param neighmin
	 * @param neighmax
	 * @return boolean
	 */
	private boolean overlap(double newmin, double newmax, double neighmin, double neighmax)
	 {
		 if(newmin<= neighmin && neighmin <= newmax)
			 return true;
          
		 if(newmin<= neighmax && neighmax <= newmax)
			 return true;
                  
		 if(newmin >= neighmin && newmax <= neighmax)
			 return true;
 
		 return false;
	 }


	/**
	 * This method is a helper for updateThem method. Checks if the regions are strictly overlapping.
	 * @param newmin
	 * @param newmax
	 * @param neighmin
	 * @param neighmax
	 * @return boolean
	 */
	private boolean overlapD(double newmin, double newmax, double neighmin, double neighmax)
	 {
		 if(newmin< neighmin && neighmin < newmax)
			 return true;
          
		 if(newmin< neighmax && neighmax < newmax)
			 return true;
                  
		 if(newmin > neighmin && newmax < neighmax)
			 return true;

//		if(newmin > neighmin && newmin < neighmax)
//					 return true;
// 
//		if(newmin == neighmin && newmax == neighmax)
//					 return true;
//
//		if(newmax > neighmin && newmax < neighmax)
//					 return true;

		 return false;
	 }
	 
	 /**
	  * Calculate Graph Statistics (min degree, max degree, avg degree).
	  */
	 private void calculateStats() {		
		UndirectedSparseVertex temp;
		minDegree = ((UndirectedSparseVertex) g.getUserDatum(new Integer(1).toString())).degree();
		maxDegree = 0;
		for(int i=0; i<g.numVertices(); i++) {
				temp = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());				
				avgDegree += temp.degree();
				if (temp.degree() < minDegree) {
					minDegree = temp.degree();
				}
				if (temp.degree() > maxDegree) {
					maxDegree = temp.degree();
				}
		}	 	
		avgDegree /= g.numVertices();	 	
	 }
	 
	 /**
	 * Print the network graph 
	 */
	 public void printNetwork() {
		UndirectedSparseVertex temp, tmpnode;
		Iterator neigh_it;
		float avg_deg = 0;
		System.out.println("Number of Nodes = " + g.numVertices()); 
	 	for(int i=0; i<g.numVertices(); i++) {
				temp = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());
//	 			print node information
	 			System.out.println("Node "+temp.getUserDatum("id"));
				System.out.println("\txmin="+temp.getUserDatum("xmin")+", xmax="+temp.getUserDatum("xmax")+", ymin="+temp.getUserDatum("ymin")+", ymax="+temp.getUserDatum("ymax")+
							", zmin="+temp.getUserDatum("zmin")+", zmax="+temp.getUserDatum("zmax"));
				System.out.println("\tDegree="+temp.degree());
				avg_deg += temp.degree();
				System.out.print("\tNeighbors: ");
				Set node_neighbors = temp.getNeighbors();
				neigh_it = node_neighbors.iterator();
				while( neigh_it.hasNext() ) {
					tmpnode = (UndirectedSparseVertex) neigh_it.next();
					System.out.print(tmpnode.getUserDatum("id")+"\t");
				}
				System.out.println();
	 	}	 	
	 	avg_deg /= g.numVertices();
		System.out.println("\nAverage Degree = "+avg_deg);
	 } //end of function printNetwork

	
	/**
	* Create a Pajek file
	*/
	public void writePajekFile(String filename) {	
	    PajekNetWriter pnf = new PajekNetWriter();
		try {
            pnf.save(g, filename);
        } catch (IOException e) {
            String msg = "I/O Error while writing file: " + filename ;
            logger.log(LogService.LOG_WARNING, msg, e);
        }		
	}
	
	/**
	 * Write the graph to a file
	 */
	public void writeGraph(String filename) {
		GraphMLFile gmlf = new GraphMLFile();
		gmlf.save(g, filename);
	}

	/**
	 * Get average degree of the network
	 * @return average degree
	 */
	public float getAvgDegree() {
		return avgDegree;
	}

	/**
	 * Get max degree of the network
	 * @return max degree
	 */
	public int getMaxDegree() {
		return maxDegree;
	}

	/**
	 * Get min degree of the network
	 * @return min degree
	 */
	public int getMinDegree() {
		return minDegree;
	}

	/**
	 * Get the reason because of which modelig failed
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}
	
} //end of class CanNetwork
