/*
 * Created on Dec 3, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import cern.colt.function.IntIntDoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.io.GraphReader;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.BasicGraphReader;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.DefaultCluster;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.GMLGraphReader;


/**
 * Prefuse Action uses colt matrices to rapidly perform 
 * dendrogram generation.  
 * 
 * Newman Cluster performs Newman clustering as described
 * in the paper "Fast algorithm for detecting community
 * structure in networks." Phys. Rev. E 2004 by M.E.J.
 * Newman
 * 
 * Shortest distance performs greedy average link clustering. 
 *
 * @author Stephen
 */
public class SWClusterAction extends AbstractAction {

    protected double m_total_distances = 0.f;
    protected boolean m_shortest_mode = true;
    
    public SWClusterAction( ) {
        super();
    }
    
    public void setMode( boolean mode ) {
        m_shortest_mode = mode;
    }
    
    public void newmanCluster( ItemRegistry registry ) {
                
        DoubleMatrix2D distance_matrix = DoubleFactory2D.sparse.make( registry.getGraph().getNodeCount(), registry.getGraph().getNodeCount() );
        DoubleMatrix1D a_matrix = DoubleFactory1D.dense.make( registry.getGraph().getNodeCount(), 0. );

        Map<String,Cluster> cluster_map = new HashMap<String,Cluster>();

        // construct the leaf node distance matrix
        
        Iterator edge_iter = registry.getGraph( ).getEdges( );
        m_total_distances = 0.;
        while( edge_iter.hasNext() ) {
            Edge edge = (Edge) edge_iter.next( );
            Cluster clust1 = (Cluster) edge.getFirstNode();
            Cluster clust2 = (Cluster) edge.getSecondNode();
            if( cluster_map.get( clust1.getAttribute("id") ) == null ) {
                cluster_map.put( clust1.getAttribute("id"), clust1 );
            }
            if( cluster_map.get( clust2.getAttribute("id") ) == null ) {
                cluster_map.put( clust2.getAttribute("id"), clust2 );
            }
            int n = Integer.parseInt( clust1.getAttribute("id"));
            int m = Integer.parseInt( clust2.getAttribute("id"));
            // make reciprocal (big values = good in newman, but not in our case)
            double dist = 1/clust1.getCenter().distance(clust2.getCenter()); 
            distance_matrix.set( Math.max( n, m ), Math.min( n, m ), dist );
//            m_total_distances += dist;
//            a_matrix.setQuick( n, a_matrix.getQuick( n ) + dist );
//            a_matrix.setQuick( m, a_matrix.getQuick( m ) + dist );
            m_total_distances += 1;
            a_matrix.setQuick( n, a_matrix.getQuick( n ) + 1 );
            a_matrix.setQuick( m, a_matrix.getQuick( m ) + 1 );
        }
                
//        System.out.println(distance_matrix);
//        System.out.println(count_matrix);
        // agglomerate nodes until we reach a root node (or nodes)
        boolean done = false;
        int trash = 0;
        QFinder qfinder = new QFinder( );
        qfinder.a_matrix = a_matrix;
        QMerger qmerger = new QMerger( );
        while( ! done ) {
            // find the minimum cluster distance
            
            qfinder.reset( );
            
            distance_matrix.forEachNonZero( qfinder );

            //            done = true;
            
            
//            System.out.println(distance_matrix);
//            System.out.println(count_matrix);
            if( qfinder.getVal() == -Double.MAX_VALUE ) {
                break;
            }
            
            // add a parent cluster to the graph

            Cluster clust1 = cluster_map.get( ""+qfinder.getM() );
            Cluster clust2 = cluster_map.get( ""+qfinder.getN() );
            while( clust1.getParent() != null ) {
                clust1 = clust1.getParent( );
            }
            while( clust2.getParent() != null ) {
                clust2 = clust2.getParent( );
            }
            trash++;
            double dist = Math.max( clust1.getHeight(), clust2.getHeight() );
            Cluster new_cluster = new DefaultCluster(
                    (float)(clust1.getCenter().getX()+clust2.getCenter().getX())/2.f,
                    (float)(clust1.getCenter().getY()+clust2.getCenter().getY())/2.f,
                    (float)Math.sqrt( clust1.getRadius()*clust1.getRadius() + clust2.getRadius()*clust2.getRadius() ),
                    clust1, clust2, dist );
            registry.getGraph().addNode(new_cluster);
            
            // merge the clusters distances / counts
        
            int M = Math.max( qfinder.getM( ), qfinder.getN() );
            int N = Math.min( qfinder.getM( ), qfinder.getN() );
            a_matrix.set( N, a_matrix.getQuick(M)+a_matrix.getQuick(N));
            a_matrix.set( M, 0);
//            System.out.println("M = "+M+" N = "+N + " VAL=" + minfinder.getVal() );
            qmerger.setM( M );
            qmerger.setN( N );
            qmerger.setParent( distance_matrix );
            qmerger.setMode( true );
//            System.out.println(distance_matrix.viewPart( 0, M, distance_matrix.rows(), 1));
            distance_matrix.viewPart( 0, M, distance_matrix.rows(), 1).forEachNonZero( qmerger );
            qmerger.setMode( false );
//            System.out.println(distance_matrix.viewPart( M, 0, 1, M ));
            distance_matrix.viewPart( M, 0, 1, M ).forEachNonZero( qmerger ); 
            
//            System.out.println(distance_matrix);
//            System.out.println(count_matrix);
            // free any superfluous memory randomly ~ (1/20) times
            
            if( Math.random() > 0.95 ) {
                distance_matrix.trimToSize( );
            }
            
        }
        
    }
    
    
    public void shortestDistance( ItemRegistry registry ) {
        //System.out.println(""+registry.getGraph().getNodeCount());
        DoubleMatrix2D distance_matrix = DoubleFactory2D.sparse.make( registry.getGraph().getNodeCount(), registry.getGraph().getNodeCount() );
        DoubleMatrix2D count_matrix = DoubleFactory2D.sparse.make( registry.getGraph().getNodeCount(), registry.getGraph().getNodeCount() );

        Map<String,Cluster> cluster_map = new HashMap<String,Cluster>();

        // construct the leaf node distance matrix
        
        Iterator edge_iter = registry.getGraph( ).getEdges( );
        while( edge_iter.hasNext() ) {
            Edge edge = (Edge) edge_iter.next( );
            Cluster clust1 = (Cluster) edge.getFirstNode();
            Cluster clust2 = (Cluster) edge.getSecondNode();
            if( cluster_map.get( clust1.getAttribute("id") ) == null ) {
                cluster_map.put( clust1.getAttribute("id"), clust1 );
            }
            if( cluster_map.get( clust2.getAttribute("id") ) == null ) {
                cluster_map.put( clust2.getAttribute("id"), clust2 );
            }
            int n = Integer.parseInt( clust1.getAttribute("id"));
            int m = Integer.parseInt( clust2.getAttribute("id"));
            double dist = clust1.getCenter().distance(clust2.getCenter());
            distance_matrix.set( Math.max( n, m ), Math.min( n, m ), dist );
            count_matrix.set( Math.max( n, m ), Math.min( n, m ), 1 );
        }
                
//        System.out.println(distance_matrix);
//        System.out.println(count_matrix);
        // agglomerate nodes until we reach a root node (or nodes)
        boolean done = false;
        MinFinder minfinder = new MinFinder( );
        minfinder.count_matrix = count_matrix;
        Merger merger = new Merger( );
        while( ! done ) {

            // find the minimum cluster distance
            
            minfinder.reset( );
            
            distance_matrix.forEachNonZero( minfinder );

            //            done = true;
            
            
//            System.out.println(distance_matrix);
//            System.out.println(count_matrix);
            if( minfinder.getVal() == Double.MAX_VALUE ) {
                break;
            }
            
            // add a parent cluster to the graph

            Cluster clust1 = cluster_map.get( ""+minfinder.getM() );
            Cluster clust2 = cluster_map.get( ""+minfinder.getN() );
            while( clust1.getParent() != null ) {
                clust1 = clust1.getParent( );
            }
            while( clust2.getParent() != null ) {
                clust2 = clust2.getParent( );
            }
//            System.out.println("HERE!");
            Cluster new_cluster = new DefaultCluster(
                    (float)(clust1.getCenter().getX()+clust2.getCenter().getX())/2.f,
                    (float)(clust1.getCenter().getY()+clust2.getCenter().getY())/2.f,
                    (float)Math.sqrt( clust1.getRadius()*clust1.getRadius() + clust2.getRadius()*clust2.getRadius() ),
                    clust1, clust2, minfinder.getVal() );
            registry.getGraph().addNode(new_cluster);
            
            // merge the clusters distances / counts
        
            int M = Math.max( minfinder.getM( ), minfinder.getN() );
            int N = Math.min( minfinder.getM( ), minfinder.getN() );
//            System.out.println("M = "+M+" N = "+N + " VAL=" + minfinder.getVal() );
            merger.setM( M );
            merger.setN( N );
            merger.setParent( distance_matrix );
            merger.setMode( true );
//            System.out.println(distance_matrix.viewPart( 0, M, distance_matrix.rows(), 1));
            distance_matrix.viewPart( 0, M, distance_matrix.rows(), 1).forEachNonZero( merger );
            merger.setMode( false );
//            System.out.println(distance_matrix.viewPart( M, 0, 1, M ));
            distance_matrix.viewPart( M, 0, 1, M ).forEachNonZero( merger ); 
            merger.setParent( count_matrix );
            merger.setMode( true );
            count_matrix.viewPart( 0, M, count_matrix.rows(), 1).forEachNonZero( merger );
            merger.setMode( false );
            count_matrix.viewPart( M, 0, 1, M ).forEachNonZero( merger ); 
            
//            System.out.println(distance_matrix);
//            System.out.println(count_matrix);
            // free any superfluous memory randomly ~ (1/20) times
            
            if( Math.random() > 0.95 ) {
                distance_matrix.trimToSize( );
            }
            
        }
        
    }

    public class QMerger implements IntIntDoubleFunction {
        
        protected DoubleMatrix2D m_parent;
        protected int m; // is the index from which we are taking the row/col
        protected int n; // is the one to which we are merging it        
        protected boolean m_col_mode = true;
        
        public void setM( int newm ) {
            m = newm;
        }
        
        public void setN( int newn ) {
            n = newn;
        }
        
        public void setMode( boolean col_mode ) {
            m_col_mode = col_mode;
        }
        
        public void setParent( DoubleMatrix2D parent ) {
            m_parent = parent;
        }

        // assumes m > n
        public double apply( int first, int second, double third ) {            
            
//            System.out.println("m = " + m);
//            System.out.println("n = " + n);
//            System.out.println("first = " + first);
            if( m_col_mode ) { // m == 2nd
                if( first != m ) {
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("val = " + m_parent.get(first, n) );
                    m_parent.set( first, n, m_parent.get(first, n) + third );
                }
                else {
                    m_parent.set( n, n, m_parent.get( n, n ) + third );
                }
            }
            else { // m == first
                if( second > n ) {
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("second = " + second);
//                    System.out.println("val = " + m_parent.get(second, n) );
                    m_parent.set( second, n, m_parent.get(second, n) + third );
                }
                else if( second < n ){
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("second = " + second);
//                    System.out.println("val = " + m_parent.get(n, second) );
                    m_parent.set( n, second, m_parent.get(n, second) + third );
                }
            }
            
            return 0.;
        }
        
    }
    
    public class QFinder implements IntIntDoubleFunction {
        public DoubleMatrix1D a_matrix = null;
        protected int min_m;
        protected int min_n;
        protected double max_q;
        
        public QFinder( ) {
            reset();
        }
        
        public int getM( ) {
            return min_m;
        }
        public int getN( ) {
            return min_n;
        }
        public double getVal( ) {
            return max_q;
        }
        
        public void reset() {
            min_m = 0;
            min_n = 0;
            max_q = -Double.MAX_VALUE;
        }
        
        public double apply( int first, int second, double third ) {
            
            if( first == second ) {
                return third;
            }
            
//            System.out.println("checking = " + min_val + " versus " + (third/count_matrix.getQuick(first,second)));
            double d_q = 2*(third/m_total_distances-(a_matrix.getQuick(first)*a_matrix.getQuick(second))/(m_total_distances*m_total_distances)); 
            if( d_q > max_q ) {
                min_m = first;
                min_n = second;
                max_q = d_q;
            }
            
            return third;        
        }
        
    }

    public class Merger implements IntIntDoubleFunction {
        
        protected DoubleMatrix2D m_parent;
        protected int m; // is the index from which we are taking the row/col
        protected int n; // is the one to which we are merging it        
        protected boolean m_col_mode = true;
        
        public void setM( int newm ) {
            m = newm;
        }
        
        public void setN( int newn ) {
            n = newn;
        }
        
        public void setMode( boolean col_mode ) {
            m_col_mode = col_mode;
        }
        
        public void setParent( DoubleMatrix2D parent ) {
            m_parent = parent;
        }
        
        // assumes m > n
        public double apply( int first, int second, double third ) {            
            
//            System.out.println("m = " + m);
//            System.out.println("n = " + n);
//            System.out.println("first = " + first);
            if( m_col_mode ) { // m == 2nd
                if( first != m ) {
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("val = " + m_parent.get(first, n) );
                    m_parent.set( first, n, m_parent.get(first, n) + third );
                }
            }
            else { // m == first
                if( second > n ) {
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("second = " + second);
//                    System.out.println("val = " + m_parent.get(second, n) );
                    m_parent.set( second, n, m_parent.get(second, n) + third );
                }
                else if( second < n ){
//                    System.out.println("m = " + m);
//                    System.out.println("n = " + n);
//                    System.out.println("first = " + first);
//                    System.out.println("second = " + second);
//                    System.out.println("val = " + m_parent.get(n, second) );
                    m_parent.set( n, second, m_parent.get(n, second) + third );
                }
            }
            
            return 0.;
        }
        
    }
    
    public class MinFinder implements IntIntDoubleFunction {
        public DoubleMatrix2D count_matrix = null;
        protected int min_m;
        protected int min_n;
        protected double min_val;
        
        public MinFinder( ) {
            reset();
        }
        
        public int getM( ) {
            return min_m;
        }
        public int getN( ) {
            return min_n;
        }
        public double getVal( ) {
            return min_val;
        }
        
        public void reset() {
            min_m = 0;
            min_n = 0;
            min_val = Double.MAX_VALUE;
        }
        
        public double apply( int first, int second, double third ) {
//            System.out.println("checking = " + min_val + " versus " + (third/count_matrix.getQuick(first,second)));
            if( third / count_matrix.getQuick(first,second) < min_val ) {
                min_m = first;
                min_n = second;
                min_val = third / count_matrix.getQuick(first,second);
            }
            
            return third;        
        }
        
    }

    public class MaxFinder implements IntIntDoubleFunction {
        public DoubleMatrix2D count_matrix = null;
        protected int max_m;
        protected int max_n;
        protected double max_val;
        
        public MaxFinder( ) {
            reset();
        }
        
        public int getM( ) {
            return max_m;
        }
        public int getN( ) {
            return max_n;
        }
        public double getVal( ) {
            return max_val;
        }
        
        public void reset() {
            max_m = 0;
            max_n = 0;
            max_val = Double.MIN_VALUE;
        }
        
        public double apply( int first, int second, double third ) {
//            System.out.println("checking = " + min_val + " versus " + (third/count_matrix.getQuick(first,second)));
            if( third / count_matrix.getQuick(first,second) > max_val ) {
                max_m = first;
                max_n = second;
                max_val = third / count_matrix.getQuick(first,second);
            }
            
            return third;        
        }
        
    }
    
    @Override
    public void run( ItemRegistry registry, double frac ) {

        if( m_shortest_mode ) 
            shortestDistance(registry);
        else
            newmanCluster(registry);

    }

    /**
     * @param args
     */
    public static void main( String[] args ) {


        if( args.length == 0 ) {
            // construct cluster graph with known clustering topology
            
            Node node1 = new DefaultCluster(20.f,20.f);
            node1.setAttribute("id","0");
            Node node2 = new DefaultCluster(18.f,20.f);
            node2.setAttribute("id","1");
            Node node3 = new DefaultCluster(20.f,24.f);
            node3.setAttribute("id","2");
            Node node4 = new DefaultCluster(20.f,15.f);
            node4.setAttribute("id","3");
            Node node5 = new DefaultCluster(40.f,15.f);
            node5.setAttribute("id","4");
            Edge edge1 = new DefaultEdge(node1, node2);
            Edge edge2 = new DefaultEdge(node1, node3);
            Edge edge3 = new DefaultEdge(node1, node4);
            Edge edge4 = new DefaultEdge(node1, node5);
            Edge edge5 = new DefaultEdge(node2, node3);
            Edge edge6 = new DefaultEdge(node3, node5);
            
            Graph graph = new DefaultGraph();
            graph.addNode( node1 );
            graph.addNode( node2 );
            graph.addNode( node3 );
            graph.addNode( node4 );
            graph.addNode( node5 );
            graph.addEdge( edge1 );
            graph.addEdge( edge2 );
            graph.addEdge( edge3 );
            graph.addEdge( edge4 );
            graph.addEdge( edge5 );
            graph.addEdge( edge6 );
            
            ItemRegistry registry = new ItemRegistry(graph);
            
            // run clusterizer on the graph
            SWClusterAction cluster = new SWClusterAction();
            cluster.shortestDistance( registry );        
    
            // print results
            Cluster dendrogram_root = null;
            Iterator iter_nodes = registry.getGraph().getNodes();
            while( iter_nodes.hasNext() ) {
                dendrogram_root = (Cluster) iter_nodes.next( );
                if( dendrogram_root.isRoot() )
                    break;
            }
            DefaultCluster.printChildren( dendrogram_root );
        }
        else {
            try {
                GraphReader gr = null;
                if( args[0].endsWith(".gml" ) ){
                    gr = new GMLGraphReader( );
                }
                else {
                    gr = new BasicGraphReader( );
                }
                Graph graph = gr.loadGraph(args[0]);

                ItemRegistry registry = new ItemRegistry( graph );
                
                LinLogAction lla = new LinLogAction( );
                
                System.out.print("Testing Layout...");
                long begin_time = System.currentTimeMillis();
                // test the linlog layout
                lla.doLayout( registry );
                System.out.println("done in " + ((System.currentTimeMillis()-begin_time)) + " milliseconds" );
                
//                Iterator iter = registry.getGraph().getNodes();
//                while( iter.hasNext() ) {
//                    Cluster cluster = (Cluster) iter.next();
//                    System.out.println("Cluster " + cluster.getAttribute("id") + " at "
//                            + cluster.getCenter() );
//                }

                // run clusterizer on the graph
                SWClusterAction cluster = new SWClusterAction();
                
                System.out.print("Testing shortestDistance...");
                begin_time = System.currentTimeMillis();
                cluster.shortestDistance( registry );                        
                System.out.println("done in " + ((System.currentTimeMillis()-begin_time)) + " milliseconds" );
                
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

}
