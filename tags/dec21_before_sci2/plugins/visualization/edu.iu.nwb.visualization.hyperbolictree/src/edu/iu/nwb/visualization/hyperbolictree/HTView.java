
package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface HTView {

    //public boolean getLongNameMode();

    //public boolean getFastMode();

    public void startMouseListening();

    public void stopMouseListening();

    public void repaint();

    public int getHeight();

    public int getWidth();

    public Insets getInsets();

    public HTNode getNodeUnderTheMouse(MouseEvent event);

    public void translateToOrigin(HTNode node);

    public void setImage(Image image);

    public void addMouseListener(MouseListener l);

}
