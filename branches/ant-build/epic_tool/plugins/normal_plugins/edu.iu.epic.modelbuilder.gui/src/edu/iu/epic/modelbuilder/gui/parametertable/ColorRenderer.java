package edu.iu.epic.modelbuilder.gui.parametertable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ColorRenderer extends JLabel
                           implements TableCellRenderer {
    private Border unselectedBorder = null;
    private Border selectedBorder = null;
    private boolean isBordered = true;

    public ColorRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true); //MUST do this for background to show up.
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object colorObject,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        Color color = (Color) colorObject;
        setBackground(color);
        
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder =
                    	BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                }
                
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder =
                    	BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                }
                
                setBorder(unselectedBorder);
            }
        }
        
        return this;
    }
    
}
