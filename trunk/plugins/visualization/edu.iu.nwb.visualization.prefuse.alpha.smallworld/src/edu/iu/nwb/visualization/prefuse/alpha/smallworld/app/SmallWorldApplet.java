/*
 * Created on Dec 23, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.GraphReader;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.action.ClusterBounds;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.action.DendroLayout;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.action.KillYourParents;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.action.LinLogAction;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.action.SWClusterAction;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.control.AnchorAlwaysUpdateControl;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.layout.DOALayout;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.render.LensDisplay;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.render.TubeRenderer;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.render.VoroHackRenderer;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.BasicGraphReader;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.DefaultVoroNode;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.GMLGraphReader;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.ProgressUpdate;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.SmallWorldComparator;


/**
 * A prefuse applet implementation of the paper "Interactive
 * Visualization of Small World Graphs" by Frank van Ham and 
 * Jarke van Wijk in InfoVis 2004.
 *
 * @author Stephen
 */
public class SmallWorldApplet extends JApplet implements ComponentListener,
        ChangeListener, ActionListener, ItemListener, ProgressUpdate {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String GRAPH_FRIENDSTER = "/friendster.xml";
    public static final String GRAPH_TERROR     = "/terror.xml";
    public static final int PASSES = 10;    

    private KillYourParents m_patricide = null;
    private LinLogAction m_linlog = null;
    private SWClusterAction m_clust = null;
    private JCheckBox m_quality = null;
    private TubeRenderer m_tube = null;
    private LoadThread m_lt = null;
    private JProgressBar progress = null;
    private JSlider slider          = null;
    private JSlider slider_inner          = null;
    private JSlider slider_outer          = null;
    private ItemRegistry m_registry;
    private Display m_display;
    private DOALayout m_doa_layout;
    private ActionList m_implicit;
    private ActionList m_refit;
    private ActionList m_layout;
    
    public void update() {
        SwingUtilities.invokeLater( new Runnable( ) {
            public void run( ) {
                progress.setValue( progress.getValue()+1 );
                progress.setString( ""+progress.getValue()+"%");    
                getContentPane().repaint();
            }
        });        
    }

    public void startSetup( ) {
        // start action lists
        m_patricide.run( m_registry, 1.00 );
        m_linlog.run( m_registry, 1.00 );
        m_refit.runNow();        
    }
    public void finishSetup( ) {
        m_display.addControlListener(new AnchorAlwaysUpdateControl( m_doa_layout, m_layout ));        
    }

    public void init() {
        try {
            
            JFileChooser jfc = new JFileChooser( );
            String filename = null;
            
            int retval = jfc.showOpenDialog( null );
            
            // grab file through file chooser
            
            if( retval == JFileChooser.APPROVE_OPTION ) {
                if( jfc.getSelectedFile().exists( ) ) {
                    
                    filename = jfc.getSelectedFile().getPath( );
                }
            }

            GraphReader gr = null;
            // create test graph and registry
            if( filename.endsWith( ".gml" ) ){
                gr = new GMLGraphReader( );
            }
            else {
                gr = new BasicGraphReader( );
            }
            Graph g = gr.loadGraph( new FileInputStream( new File( filename ) ) );
            System.out.println(filename + " node count = " + g.getNodeCount() );
            m_registry = new ItemRegistry(g);
            m_registry.addItemClass( ItemRegistry.DEFAULT_NODE_CLASS, DefaultVoroNode.class );
            m_registry.setItemComparator( new SmallWorldComparator( ) );
            
            // intialize renderer
            
            VoroHackRenderer vnetR = new VoroHackRenderer( PASSES );
//            VoroNetRenderer vnetR = new VoroNetRenderer();
//            vnetR.m_registry = m_registry;
            m_tube = new TubeRenderer();
            m_tube.setTubes( false );
            DefaultRendererFactory rfac = new DefaultRendererFactory();
            rfac.addRenderer( ItemRegistry.DEFAULT_NODE_CLASS, vnetR );
            rfac.setEdgeRenderer( m_tube );
            
            m_registry.setRendererFactory( rfac );
            
            // initialize action lists
            
            m_patricide = new KillYourParents( );
            m_linlog = new LinLogAction( this );
            ClusterBounds bounds = new ClusterBounds( );
            m_clust = new SWClusterAction( );
            m_clust.setMode(true);
//            preproc = new ActionList( m_registry );
//            preproc.add( linlog );
            m_refit = new ActionList( m_registry );
            m_refit.add( bounds );
            m_refit.add( m_clust );
            //m_refit.runAfter( preproc );
//            m_physics = new ActionList( m_registry );
//            m_physics.add( bounds );
//            m_physics.add( linlog );
            m_implicit = new ActionList( m_registry );
            m_implicit.add( bounds );
            m_implicit.add( m_clust );
            m_implicit.add( new DendroLayout( ) );
            
            m_doa_layout = new DOALayout( );
            m_doa_layout.setLayoutAnchor( new Point2D.Double( 0, 0 ) );
            m_doa_layout.setTube( m_tube );
            m_layout = new ActionList(m_registry);            
            m_layout.add(m_doa_layout);
//            m_layout.add(new VoroNetLayout());
            m_layout.add( new RepaintAction( ) );
            m_layout.runAfter( m_refit );
            
            // initialize display
            
            m_display = new LensDisplay(m_registry, PASSES);
            m_display.setSize(400,400);
            m_display.setBackground(Color.WHITE);
            m_display.setHighQuality(false);
            //m_display.setHighQuality(true);
            
            // create and display application window

            m_quality = new JCheckBox( "High Quality", false );            
            m_quality.addItemListener( this );
            JRadioButton average_link_button = new JRadioButton("Average Link");
            average_link_button.setActionCommand( "average link" );
            average_link_button.setSelected( true );
            average_link_button.addActionListener( this );
            JRadioButton newman_button = new JRadioButton("Newman");
            newman_button.setActionCommand( "newman" );
            newman_button.setSelected( false );
            newman_button.addActionListener( this );
            ButtonGroup bg_1 = new ButtonGroup();
            bg_1.add( average_link_button );
            bg_1.add( newman_button );
            JPanel button_panel_1 = new JPanel();
            button_panel_1.setLayout( new GridLayout(1,3) );
            button_panel_1.add( average_link_button );
            button_panel_1.add( newman_button );
            JRadioButton no_edges_button = new JRadioButton("no edges");
            no_edges_button.setActionCommand( "no edges" );
            no_edges_button.setSelected( true );
            no_edges_button.addActionListener( this );
            JRadioButton line_edges_button = new JRadioButton("line edges");
            line_edges_button.setActionCommand( "line edges" );
            line_edges_button.setSelected( false );
            line_edges_button.addActionListener( this );
            JRadioButton tube_edges_button = new JRadioButton("tube edges");
            tube_edges_button.setActionCommand( "tube edges" );
            tube_edges_button.setSelected( false );
            tube_edges_button.addActionListener( this );
            ButtonGroup bg = new ButtonGroup();
            bg.add( no_edges_button );
            bg.add( line_edges_button );
            bg.add( tube_edges_button );
            JPanel button_panel = new JPanel();
            button_panel.setLayout( new GridLayout(1,3) );
            button_panel.add( no_edges_button );
            button_panel.add( line_edges_button );
            button_panel.add( tube_edges_button );
            progress = new JProgressBar( JProgressBar.HORIZONTAL );            
            slider = new JSlider( JSlider.VERTICAL, 0, 100, (int)(100.*m_doa_layout.getConstantDOA()) );
            slider.addChangeListener( this );
            slider_inner = new JSlider( JSlider.HORIZONTAL, 10, 100, (int)DOALayout.ZERO_RADIUS );
            slider_inner.addChangeListener( this );
            slider_outer = new JSlider( JSlider.HORIZONTAL, 10, 200, (int)DOALayout.DOA_RADIUS );
            slider_outer.addChangeListener( this );
            JPanel slider_panel = new JPanel();
            slider_panel.setLayout( new GridLayout(1, 2) );
            slider_panel.add( slider_inner );
            slider_panel.add( slider_outer );
            JPanel meta_panel = new JPanel();
            meta_panel.setLayout( new GridLayout( 3, 1 ) );
            meta_panel.add( button_panel );
            meta_panel.add( slider_panel );
            meta_panel.add( button_panel_1 );
            JPanel lower_panel = new JPanel();
            lower_panel.setLayout( new GridLayout( 2, 1 ) );
            lower_panel.add( m_quality );
            lower_panel.add( progress );            
//            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
            getContentPane( ).setLayout( new BorderLayout(5,5) );
            getContentPane( ).add(m_display, BorderLayout.CENTER );
            getContentPane( ).add(slider, BorderLayout.EAST );
            getContentPane( ).add(lower_panel, BorderLayout.SOUTH );
            getContentPane( ).add(meta_panel, BorderLayout.NORTH );
//            pack( );
            setVisible( true );
            m_display.addComponentListener( this );
            
            m_lt = new LoadThread( this );
            m_lt.start();
            
        } catch ( Exception e ) {
            e.printStackTrace();
        }   
    } //
        
    public void componentHidden(ComponentEvent e) {
        
    }
    public void componentMoved(ComponentEvent e) {
        
    }
    public void componentResized(ComponentEvent e) {
        m_layout.runAfter( m_refit );
        m_refit.runNow( );
    }
    public void componentShown(ComponentEvent e) {
        
    }
    public void stateChanged( ChangeEvent e ) {
        if( e.getSource( ) == slider ) {
            
            if( m_registry != null ) {
                        m_doa_layout.setConstantDOA( slider.getValue() * 0.01 );
                m_layout.runNow();
            }
        }
        if( e.getSource( ) == slider_inner ) {
            
            if( m_registry != null ) {
                DOALayout.ZERO_RADIUS = slider_inner.getValue();
                m_layout.runNow();
            }
        }
        if( e.getSource( ) == slider_outer ) {
            
            if( m_registry != null ) {
                DOALayout.DOA_RADIUS = slider_outer.getValue();
                m_layout.runNow();
            }
        }
    }
    
    public class LoadThread extends Thread {

        SmallWorldApplet m_small_world = null;
        public boolean already_started = false;
        
        public LoadThread( SmallWorldApplet small_world ) {
            
            super( );
            
            m_small_world = small_world;
            
        } //
        
        public void run( ) {
            
            m_small_world.startSetup();
            if( !already_started ) {
                m_small_world.finishSetup();
                already_started = true;
            }
            
        } //
        
    } //
    
    public void actionPerformed(ActionEvent e) {
        if( e.getActionCommand().equalsIgnoreCase("no edges") ) {
            m_doa_layout.setEdges( false );
            m_layout.runNow();
        }
        if( e.getActionCommand().equalsIgnoreCase("line edges") ) {
            m_doa_layout.setEdges( true );
            m_tube.setTubes( false );
            m_layout.runNow();
        }
        if( e.getActionCommand().equalsIgnoreCase("tube edges") ) {
            m_doa_layout.setEdges( true );
            m_tube.setTubes( true );
            m_layout.runNow();
        }
        if( e.getActionCommand().equalsIgnoreCase("average link") ) {
            m_linlog.setDirty(true);
            m_clust.setMode(true);
            m_layout.runAfter( m_refit );
            m_lt = new LoadThread( this );
            m_lt.already_started = true;
            progress.setValue( 0 );
            m_lt.start();
        }
        if( e.getActionCommand().equalsIgnoreCase("newman") ) {
            m_clust.setMode(false);
            m_layout.runAfter( m_implicit );
            m_implicit.runNow(  );
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if( e.getSource() == m_quality ) {
            if( e.getStateChange() == ItemEvent.DESELECTED ) {
                m_display.setHighQuality( false );
                m_layout.runNow();
            }
            else {
                m_display.setHighQuality( true );
                m_layout.runNow();
            }
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        // TODO Auto-generated method stub

    }

}
