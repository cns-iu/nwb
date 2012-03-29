package edu.iu.epic.modelbuilder.gui.parametertable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;

@SuppressWarnings("serial")
public class ParameterTableParameterDeleter 
	extends AbstractCellEditor
	implements TableCellRenderer, TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private JTable table;
	private DefaultTableModel defaultTableModel;
	private JButton renderButton;
	private JButton editButton;
	private String text;
	private int rowNumberActedUpon;
	private Model inMemoryModel;
	private NotificationArea[] notificationAreas; 

	public ParameterTableParameterDeleter(URL deleteButtonNormalStateImage, 
						   URL deleteButtonMousePressedImage, 
						   DefaultTableModel defaultTableModel,
						   JTable table,
						   NotificationArea[] notificationAreas, Model inMemoryModel) { 
		super();
		this.table = table;
		this.defaultTableModel = defaultTableModel;
		this.notificationAreas = notificationAreas;
		this.inMemoryModel = inMemoryModel;

		/*
         * First image for when the Button is not pressed. Second image for when the Button is 
         * pressed. 
         * */
		
		renderButton = new JButton(new ImageIcon(deleteButtonNormalStateImage));
		
		editButton = new JButton(new ImageIcon(deleteButtonMousePressedImage));
		
		editButton.setFocusPainted(false);
		editButton.addActionListener(this);

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(ParameterTable.DELETE_BUTTON_COLUMN_INDEX).setCellRenderer(this);
		columnModel.getColumn(ParameterTable.DELETE_BUTTON_COLUMN_INDEX).setCellEditor(this);
		
	}

	public Component getTableCellRendererComponent(JTable table, 
												   Object value, 
												   boolean isSelected, 
												   boolean hasFocus, 
												   int row, 
												   int column) {
		if (hasFocus) {
			renderButton.setForeground(table.getForeground());
			renderButton.setBackground(UIManager.getColor("Button.background"));
		} else if (isSelected) {
			renderButton.setForeground(table.getSelectionForeground());
			renderButton.setBackground(table.getSelectionBackground());
		} else {
			renderButton.setForeground(table.getForeground());
			renderButton.setBackground(UIManager.getColor("Button.background"));
		}

		renderButton.setText((value == null) ? "" : value.toString());
		return renderButton;
	}

	public Component getTableCellEditorComponent(JTable table, 
												 Object value, 
												 boolean isSelected, 
												 int row, 
												 int column) {
		text = (value == null) ? "" : value.toString();
		editButton.setText(text);
		return editButton;
	}

	public Object getCellEditorValue() {
		return text;
	}

	public void actionPerformed(ActionEvent e) {
		fireEditingStopped();
		rowNumberActedUpon = table.getSelectedRow();
		String parameterNameToBeDeleted = (String) table.getModel()
												.getValueAt(rowNumberActedUpon, 
															ParameterTable
																.PARAMETER_NAME_COLUMN_INDEX);
		defaultTableModel.removeRow(rowNumberActedUpon);
		inMemoryModel.removeParameterDefinition(parameterNameToBeDeleted);
		
		try {
			notificationAreas[0].addAllNotifications(
					inMemoryModel.listUnboundReferencedParameters());
		} catch (InvalidParameterExpressionException exception) {
			notificationAreas[1].addNotification("Errors in testing of undefined parameters.");
		}
		
	}
	
	/**
	 * @return the rowNumberActedUpon
	 */
	public int getRowNumberActedUpon() {
		return rowNumberActedUpon;
	}
	
}