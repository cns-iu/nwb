/*
 * Created on Aug 17, 2004
 */
package edu.iu.iv.modeling.tarl.output.impl;

import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.output.GraphGenerator;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleDirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.SimpleUndirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.UserData;

/**
 * @author Team IVC
 */
public class JUNGGraphGenerator implements GraphGenerator {

    AuthorManager authorManager;

    PublicationManager pubManager;

    public Class getGraphClass() {
        return Graph.class;
    }

    /**
     * Constructs a graph generator using the specified publication and author
     * managers.
     * 
     * @param pubManager
     *            The publication manager to use for publications.
     * @param authorManager
     *            The author manager to use for authors.
     */
    public JUNGGraphGenerator(PublicationManager pubManager,
            AuthorManager authorManager) {
        this.pubManager = pubManager;
        this.authorManager = authorManager;
    }

    /**
     * Generates a author-publication graph of type JUNG Graph
     * (edu.uci.ics.jung.graph.Graph) This graph has two types of vertices, one
     * type representing the authors and another type representing the papers.
     * The return value should be type cast to to this Graph object in order to
     * use it as a graph.
     * 
     * Adds keys to user data of the resulting authPubGraph to enable
     * identification of the type of vertex as being an author or a publication.
     * Procedure to process this graph: <br>
     * Object VERTEX_TYPE_KEY = authPubGraph.getUserDatum("Vertex Type Key") ;
     * Object AUTHOR_TYPE = new String("Author") ; Object PUBLICATION_TYPE = new
     * String("Publication") ;<br/><b>Algorithm </b>: <br/>
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      	FOR EACH pub IN pubManager.getPublications()
     *      	    graph.addVertex(pub) ;
     *      		FOR EACH citedPub in pub.getCitedPublications() ;
     *      			graph.addEdge(pub, citedPub, type=&quot;citation&quot;) ;
     *      		ENDFOR
     *      		FOREACH author in pub.getAuthors()
     *      			graph.addEdge(author, &lt;AllOtherAuthors&gt;, type=&quot;coauthor&quot;) ;
     *      			graph.addEdge(author, pub, type=&quot;author&quot;) ;
     *      		ENDIF 
     *      	ENDFOR 
     *      
     *     
     *    
     *   
     *  
     * </pre>
     * 
     * @see edu.iu.iv.modeling.tarl.output.GraphGenerator#generateAuthorPublicationGraph()
     */
    public Object generateAuthorPublicationGraph() {
        Graph authPubGraph = new SparseGraph();
        final String VERTEX_TYPE_KEY = new String("VertexType");
//        final String VERTEX_TYPE_AUTHOR = new String("Author");
        final String VERTEX_TYPE_PUBLICATION = new String("Publication");
        final String EDGE_TYPE_KEY = new String("EdgeType");
        final String EDGE_TYPE_CITATION = new String("Citation");
        final String EDGE_TYPE_COAUTHOR = new String("Coauthor");
        final String EDGE_TYPE_AUTHOR = new String("Author");
        authPubGraph.setUserDatum("VertexType", VERTEX_TYPE_KEY,
                UserData.REMOVE);
        Vertex sourcePub;
        for (Iterator pubIter = pubManager.getPublications().getIterator(); pubIter
                .hasNext();) {
            Publication pub = (Publication) pubIter.next();
            sourcePub = addVertexToGraph(authPubGraph, "paper" + pub.getId(),
                    false);
            if (!sourcePub.containsUserDatumKey(VERTEX_TYPE_KEY))
                sourcePub.addUserDatum(VERTEX_TYPE_KEY,
                        VERTEX_TYPE_PUBLICATION, UserData.REMOVE);
            PublicationGroup citedPubGroup = pub.getCitations();
            if (citedPubGroup != null) {
                for (Iterator citedPubIter = citedPubGroup.getIterator(); citedPubIter
                        .hasNext();) {
                    Publication citedPub = (Publication) citedPubIter.next();
                    Vertex destPub = addVertexToGraph(authPubGraph, "paper"
                            + citedPub.getId(), false);
                    if (!destPub.containsUserDatumKey(VERTEX_TYPE_KEY))
                        destPub.addUserDatum(VERTEX_TYPE_KEY,
                                VERTEX_TYPE_PUBLICATION, UserData.REMOVE);
                    if (sourcePub.findEdge(destPub) == null) {
                        Edge e = new UndirectedSparseEdge(sourcePub, destPub);
                        e.setUserDatum(EDGE_TYPE_KEY, EDGE_TYPE_CITATION,
                                UserData.REMOVE);
                        authPubGraph.addEdge(e);
                    }
                }
            }
            Author[] authorArray = (Author[]) pub.getAuthors().getAuthors()
                    .toArray(new Author[0]);
            int c = 0;
            for (Iterator authIter = pub.getAuthors().getIterator(); authIter
                    .hasNext(); ++c)
                authorArray[c] = (Author) authIter.next();
            for (int i = 0; i < authorArray.length; ++i) {
                Vertex v1 = addVertexToGraph(authPubGraph, "auth"
                        + authorArray[i].getId(), false);
                if (sourcePub.findEdge(v1) == null) {
                    Edge e = new UndirectedSparseEdge(sourcePub, v1);
                    e.setUserDatum(EDGE_TYPE_KEY, EDGE_TYPE_AUTHOR,
                            UserData.REMOVE);
                    authPubGraph.addEdge(e);
                }
                for (int j = i; j < authorArray.length; ++j) {
                    Vertex v2 = addVertexToGraph(authPubGraph, "auth"
                            + authorArray[i].getId(), false);
                    if (v1.findEdge(v2) == null) {
                        Edge e = new UndirectedSparseEdge(v1, v2);
                        e.setUserDatum(EDGE_TYPE_KEY, EDGE_TYPE_COAUTHOR,
                                UserData.REMOVE);
                        authPubGraph.addEdge(e);
                    }
                }
            }
        }
        return authPubGraph;
    }

    /**
     * Generates a co-author graph of type JUNG Graph
     * (edu.uci.ics.jung.graph.Graph) using the author manager. The return value
     * should be type cast to this Graph object in order to use it as a graph.
     * <br/><b>Algorithm </b>: <br/>
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      	FOR EACH author IN authorManager.getAuthors() 
     *      		FOR EACH pub IN pubManager.getPublications()
     *      			IF author IS IN pub.getAuthors() 
     *      				graph.addEdge(author, &lt;AllOtherAuthors&gt;) ;
     *      			ENDIF 
     *      		ENDFOR 
     *      ENDFOR
     *      
     *     
     *    
     *   
     *  
     * </pre>
     * 
     * @see edu.iu.iv.modeling.tarl.output.GraphGenerator#generateCoAuthorGraph()
     */
    public Object generateCoAuthorGraph() {
        Graph g = new UndirectedSparseGraph();
        Iterator authIter = authorManager.getAuthors().getIterator();
        while (authIter.hasNext()) {
            Author author = (Author) authIter.next();
            Vertex v = addVertexToGraph(g, "auth" + author.getId(), false);
            Iterator pubIter = pubManager.getPublications().getIterator();
            while (pubIter.hasNext()) {
                Publication pub = (Publication) pubIter.next();
                AuthorGroup authorGroup = pub.getAuthors();
                if (authorGroup.containsAuthor(author)) {
                    for (Iterator iter = authorGroup.getIterator(); iter
                            .hasNext();) {
                        Author coAuthor = (Author) iter.next();
                        Vertex v1 = addVertexToGraph(g, "auth"
                                + coAuthor.getId(), false);
                        if (!v.isNeighborOf(v1))
                            g.addEdge(new UndirectedSparseEdge(v, v1));
                    }
                }
            }
        }
        return g;
    }

    /**
     * Generates a co-citation graph of type JUNG Graph
     * (edu.uci.ics.jung.graph.Graph) using the publication manager. The return
     * value should be type cast to to this Graph object in order to use it as a
     * graph.
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      		FOR pub IN pubManager.getPublications()
     *      			FOR citedPub IN pub.getCitedPublications()
     *      				graph.addEdge(pub, citedPub) ;
     *      			ENDFOR
     *      		ENDFOR
     *      
     *     
     *    
     *   
     *  
     * </pre>
     * 
     * @see edu.iu.iv.modeling.tarl.output.GraphGenerator#generateCitationGraph()
     */
    public Object generateCitationGraph() {
        Graph g = new DirectedSparseGraph();

        Iterator pubGroupIterator = pubManager.getPublications().getIterator();

        boolean DIRECTED = true;
        while (pubGroupIterator.hasNext()) {
            Publication pub = (Publication) pubGroupIterator.next();
            Vertex source = addVertexToGraph(g, "paper" + pub.getId(), DIRECTED);
            PublicationGroup pubGroup = pub.getCitations();
            Iterator citedPubIterator;
            if (pubGroup != null) {
                citedPubIterator = pubGroup.getIterator();
                while (citedPubIterator.hasNext()) {
                    Vertex dest = addVertexToGraph(g, "paper"
                            + ((Publication) citedPubIterator.next()).getId(),
                            DIRECTED);
                    if (source.findEdge(dest) == null)
                        g.addEdge(new DirectedSparseEdge(source, dest));
                }
            }
        }

        return g;
    }

    private final String KEY_VERTEX_ID = new String("ID");

    private Vertex addVertexToGraph(Graph g, String vertexID, boolean directed) {
        Vertex v;
        if (!g.getVertices().contains(g.getUserDatum(vertexID))) {
            if (directed)
                v = new SimpleDirectedSparseVertex();
            else
                v = new SimpleUndirectedSparseVertex();
            v.setUserDatum(KEY_VERTEX_ID, vertexID, UserData.REMOVE);
            g.addUserDatum(vertexID, v, UserData.REMOVE);
            g.addVertex(v);
        } else {
            v = (Vertex) g.getUserDatum(vertexID);
        }
        return v;
    }

    /**
     * 
     * @see edu.iu.iv.modeling.tarl.output.GraphGenerator#setAuthorManager(AuthorManager)
     */
    public void setAuthorManager(AuthorManager authorManager) {
        this.authorManager = authorManager;
    }

    /**
     * @see edu.iu.iv.modeling.tarl.output.GraphGenerator#setPublicationManager(PublicationManager)
     */
    public void setPublicationManager(PublicationManager pubManager) {
        this.pubManager = pubManager;
    }
}