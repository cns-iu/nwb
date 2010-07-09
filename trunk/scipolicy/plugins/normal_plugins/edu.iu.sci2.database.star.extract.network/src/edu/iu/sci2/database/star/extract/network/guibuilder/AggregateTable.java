package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.cishell.utilities.swt.model.datasynchronizer.TextDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class AggregateTable {
	public static final int ROW_HEIGHT = 36;
	public static final String DELETE_BUTTON_TEXT = "Delete?";
	public static final String AGGREGATE_FUNCTION_COLUMN_HEADER = "Aggregate Function:";
	public static final String CORE_ENTITY_COLUMN_HEADER = "Core Entity Column:";
	public static final String NAME_COLUMN_HEADER = "Name for the Results";

	private GUIModel model;
	private Table table;
	private TableColumn column;
	private List<AggregateTableRow> rows = new ArrayList<AggregateTableRow>();

	public AggregateTable(GUIModel model, Composite parent, Object layoutData) {
		this.model = model;
		this.table = new Table(parent, SWT.VIRTUAL);
		this.column = new TableColumn(this.table, SWT.NONE);

		this.table.setLayoutData(layoutData);
		this.table.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		this.table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = ROW_HEIGHT;
			}
		});

		this.column.pack();
		addRow();
		addRow();
	}

	public Collection<AggregateTableRow> getRows() {
		return this.rows;
	}

	public AggregateTableRow addRow() {
		AggregateTableRow row = new AggregateTableRow(this.rows.size());
		this.rows.add(row);

		return row;
	}

	public void removeRow(int rowIndex) {
		this.table.remove(rowIndex);
		this.rows.get(rowIndex).container.dispose();
		this.rows.remove(rowIndex);

		for (int ii = 0; ii < this.rows.size(); ii++) {
			this.rows.get(ii).rowIndex = ii;
		}
	}

	public class AggregateTableRow {
		private int rowIndex;
		private Composite container;
		private Button deleteButton;
		private GUIModelField<String, Combo, DropDownDataSynchronizer> aggregateFunctionName;
		private GUIModelField<String, Combo, DropDownDataSynchronizer> coreEntityColumnName;
		private GUIModelField<String, Text, TextDataSynchronizer> resultColumnName;

		public AggregateTableRow(int rowIndex) {
			this.rowIndex = rowIndex;
			createContainer();
			createDeleteButton();
			createAggregateFunctionName();
//			createCoreEntityColumnName();
//			createResultColumnName();

			setupContainerEditor();
		}

		public String getAggregateFunctionName() {
			return this.aggregateFunctionName.getValue();
		}

		public String getCoreEntityColumnName() {
			return this.coreEntityColumnName.getValue();
		}

		public String getResultColumnName() {
			return this.resultColumnName.getValue();
		}

		private void createContainer() {
			this.container = new Composite(AggregateTable.this.table, SWT.BORDER);
			this.container.setLayout(createContainerLayout());
		}

		private GridLayout createContainerLayout() {
			GridLayout layout = new GridLayout(4, false);

			return layout;
		}

		private void createDeleteButton() {
			this.deleteButton = new Button(this.container, SWT.PUSH);
			this.deleteButton.setLayoutData(createDeleteButtonLayoutData());
			this.deleteButton.setText(DELETE_BUTTON_TEXT);
			this.deleteButton.pack();
			this.deleteButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent event) {
					selected(event);
				}

				public void widgetSelected(SelectionEvent event) {
					selected(event);
				}

				public void selected(SelectionEvent event) {
					AggregateTable.this.removeRow(AggregateTableRow.this.rowIndex);
				}
			});
		}

		private GridData createDeleteButtonLayoutData() {
			GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);

			return layoutData;
		}

		private void createAggregateFunctionName() {
//			this.aggregateFunctionName = AggregateTable.this.model.addSingleSelectionDropDown(
				
		}

		private void setupContainerEditor() {
			this.container.pack();

			TableItem tableItem = new TableItem(AggregateTable.this.table, SWT.NONE);
			TableEditor editor = new TableEditor(AggregateTable.this.table);
			editor.minimumWidth = this.container.getSize().x;
			editor.minimumHeight = this.container.getSize().y;
			editor.horizontalAlignment = SWT.LEFT;
			editor.verticalAlignment = SWT.CENTER;
			editor.setEditor(this.container, tableItem, 0);
		}
	}
}