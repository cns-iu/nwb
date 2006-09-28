package edu.iu.nwb.visualization.jungnetworklayout;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class JUNGLayoutGUI extends JFrame {
    private static final long serialVersionUID = -1066069022881159726L;

    private Layout layout ;

    public JUNGLayoutGUI(String title, Layout layout) {
        super(title);
        this.layout = layout ;
        initialize();
    }

    private PluggableRenderer pr;

    private VisualizationViewer vv;

    public void initialize() {
        pr = new PluggableRenderer();
        pr.setVertexShapeFunction(new VertexShapeFunction() {
            public Shape getShape(Vertex arg0) {
                //changed from new Ellipse2D.Float(0, 0, 5, 5);
                //not sure what's goin on with jung, but by doing this
                //it put the center of the circle at the origin so 
                //when lines are drawn they draw to the center of the circle
                return new Ellipse2D.Float(-5, -5, 10, 10);
            }
        });
        vv = new VisualizationViewer(layout, pr);
        vv.setBackground(Color.WHITE);
        this.getContentPane().add(vv);
        this.vv.removeMouseListener(this.vv.getMouseListeners()[0]);
        this.vv.removeMouseMotionListener(this.vv.getMouseMotionListeners()[0]);
        ZoomPanMouseBehaviour zpmb = new ZoomPanMouseBehaviour() ;
        zpmb.setListeningSource(this.vv) ;
    }
}

class ZoomPanMouseBehaviour {

    private VisualizationViewer vv;

    public ZoomPanMouseBehaviour() {
        // do nothing ;
    }
    public void setListeningSource(VisualizationViewer vv) {
        this.vv = vv;
        configure();
        enableListeners() ;
    }
    private boolean enable = false ;
    public void disableListeners() {
        enable = false ;
    }
    public void enableListeners() {
        enable = true ;
    }
    public void destroyListeners() {
        this.vv.removeMouseListener(vvMouseAdapter) ;
        this.vv.removeMouseMotionListener(vvMouseMotionAdapter) ;
    }
    private MouseAdapter vvMouseAdapter ;
    private MouseMotionAdapter vvMouseMotionAdapter ;
    private void configure() {
        vvMouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                mousePress(event);
            }

            public void mouseReleased(MouseEvent event) {
                mouseRelease(event);
            }
        } ;
        vvMouseMotionAdapter = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event))
                    leftMouseButtonDragged(event);
                else if (SwingUtilities.isRightMouseButton(event))
                    rightMouseButtonDragged(event);
            }
        } ;
        this.vv.addMouseListener(vvMouseAdapter);
        this.vv.addMouseMotionListener(vvMouseMotionAdapter);
    }

    protected void mouseRelease(MouseEvent event) {
        ok = true;
    }

    protected void mousePress(MouseEvent event) {
        if (!ok)
            return;
        this.mouseX = event.getX();
        this.mouseY = event.getY();
        ok = false;
    }

    private boolean ok = true;

    private int mouseX, mouseY;

    protected void leftMouseButtonDragged(MouseEvent event) {
        if (!enable) return ;
        double dx = (double) (event.getX() - this.mouseX) / this.vv.getWidth();
        double dy = (double) (event.getY() - this.mouseY) / this.vv.getHeight();
        double ox = -dx / this.vv.getScaleX();
        double oy = -dy / this.vv.getScaleY();
        this.vv.setOffset(ox, oy);
        this.vv.repaint();
    }

    protected void rightMouseButtonDragged(MouseEvent event) {
        if (!enable) return ;
        double dx = (double) (event.getX() - this.mouseX) / this.vv.getWidth();
        double dy = (double) (event.getY() - this.mouseY) / this.vv.getHeight();
        double avgd = (dx + dy) / 2;
        this.vv
                .setScale(this.vv.getScaleX() + avgd, this.vv.getScaleY()
                        + avgd);
        this.vv.repaint();
    }
}