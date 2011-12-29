# TODO: Should these be in a common file?  Both this file and Colorize.py depend on them.
RESIZE_LINEAR_BUTTON_TEXT = "Resize Linear"
COLORIZE_BUTTON_TEXT = "Colorize"
GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION = "Nodes"
GRAPH_OBJECT_TYPE_SELECTION_BOX_EDGE_OPTION = "Edges"
DO_RESIZE_LINEAR_BUTTON_TEXT = "Do Resize Linear"
DO_COLORIZE_BUTTON_TEXT = "Do Colorize"
RESIZE_LINEAR_TEXT_FIELD_SIZE = 7
COLOR_SELECTOR_BUTTON_SIZE = 20
FROM_LABEL_TEXT = "From: "
TO_LABEL_TEXT = "To: "
DEFAULT_VALUE_LABEL_TEXT_FORMAT = "When the %s have no '%s': "

RESIZE_LINEAR_DEFAULT_VALUE_HELP_TEXT = \
    "The size to make nodes/edges when they don't have the value you're sizing on."
COLORIZE_DEFAULT_VALUE_HELP_TEXT = \
    "The color to make nodes/edges when they don't have the value you're coloring on."

class ResizeLinearPanel(JPanel):
    def __init__(self, nodeColumnNames, edgeColumnNames, graphModifier):
        JPanel.__init__(self, GridLayout(2, 1))
        
        PREFERRED_WIDTH = 900
        PREFERRED_HEIGHT = 50
        PANEL_WIDTH = PREFERRED_WIDTH
        PANEL_HEIGHT = PREFERRED_HEIGHT / 2
        
        self.setPreferredSize(Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT))
        self.setVisible(false)
        
        self.nodeColumnNames = nodeColumnNames
        self.edgeColumnNames = edgeColumnNames
        
        self.nodeComboBoxModel = DefaultComboBoxModel(self.nodeColumnNames)
        self.edgeComboBoxModel = DefaultComboBoxModel(self.edgeColumnNames)
        
        self.topPanel = JPanel()
        self.topPanel.setPreferredSize(Dimension(PANEL_WIDTH, PANEL_HEIGHT))
        self.add(self.topPanel)
        
        self.bottomPanel = JPanel()
        self.bottomPanel.setPreferredSize(Dimension(PANEL_WIDTH, PANEL_HEIGHT))
        self.add(self.bottomPanel)
        
        # We're creating the bottom panel components first because the top panel components need
        # to change text in them (which happens upon creation).
        
        self.defaultValueFieldLabel = self._createDefaultValueFieldLabel()
        self.bottomPanel.add(self.defaultValueFieldLabel)
        self.defaultValueField = self._createDefaultValueField()
        self.bottomPanel.add(self.defaultValueField)
        self.toggleDefaultFieldVisibility(false)
        
        self.graphObjectTypeSelectionBox = self._createGraphObjectTypeSelectionBox()
        self.topPanel.add(self.graphObjectTypeSelectionBox)
        
        self.columnSelectionBox = self._createColumnSelectionBox()
        self.topPanel.add(self.columnSelectionBox)
        
        self.minTextFieldLabel = self._createMinTextFieldLabel()
        self.topPanel.add(self.minTextFieldLabel)
        self.minTextField = self._createMinTextField()
        self.topPanel.add(self.minTextField)
        
        self.maxTextFieldLabel = self._createMaxTextFieldLabel()
        self.topPanel.add(self.maxTextFieldLabel)
        self.maxTextField = self._createMaxTextField()
        self.topPanel.add(self.maxTextField)
        
        self.doResizeLinearButton = self._createDoResizeLinearButton()
        self.topPanel.add(self.doResizeLinearButton)
    
    def toggleDefaultFieldVisibility(self, visibility):
        self.defaultValueFieldLabel.setVisible(visibility)
        self.defaultValueField.setVisible(visibility)
    
    def _createGraphObjectTypeSelectionBox(self):
        class GraphObjectTypeSelectedListener(awt.event.ActionListener):
            def __init__(self, resizeLinearPanel):
                self.resizeLinearPanel = resizeLinearPanel
            
            def actionPerformed(self, event):
                self.resizeLinearPanel._chooseGraphObjectType()
        
        graphObjectTypeSelectionBoxOptions = [
            GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION,
            GRAPH_OBJECT_TYPE_SELECTION_BOX_EDGE_OPTION
        ]
        
        graphObjectTypeSelectionBox = JComboBox(
            graphObjectTypeSelectionBoxOptions,
            actionListener=GraphObjectTypeSelectedListener(self))
        
        return graphObjectTypeSelectionBox
    
    def _createColumnSelectionBox(self):
        class ColumnSelectedListener(awt.event.ActionListener):
            def __init__(self, resizeLinearPanel, columnSelectionBox):
                self.resizeLinearPanel = resizeLinearPanel
                self.columnSelectionBox = columnSelectionBox
            
            def actionPerformed(self, event):
                self.resizeLinearPanel._chooseField()
        
        columnSelectionBox = JComboBox(self.nodeComboBoxModel)
        columnSelectionBox.addActionListener(ColumnSelectedListener(self, columnSelectionBox))
        columnSelectionBox.setEditable(false)
        
        return columnSelectionBox
    
    def _createMinTextFieldLabel(self):
        minTextFieldLabel = JLabel(FROM_LABEL_TEXT)
        
        return minTextFieldLabel
    
    def _createMinTextField(self):
        minTextField = JTextField(RESIZE_LINEAR_TEXT_FIELD_SIZE)
        
        return minTextField
    
    def _createMaxTextFieldLabel(self):
        maxTextFieldLabel = JLabel(TO_LABEL_TEXT)
        
        return maxTextFieldLabel
    
    def _createMaxTextField(self):
        maxTextField = JTextField(RESIZE_LINEAR_TEXT_FIELD_SIZE)
        
        return maxTextField
    
    def _createDefaultValueFieldLabel(self):
        defaultValueFieldLabel = JLabel(DEFAULT_VALUE_LABEL_TEXT_FORMAT)
        
        return defaultValueFieldLabel
    
    def _createDefaultValueField(self):
        defaultValueField = JTextField(RESIZE_LINEAR_TEXT_FIELD_SIZE)
        
        return defaultValueField
    
    def _createDoResizeLinearButton(self):
        doResizeLinearButton = JButton(DO_RESIZE_LINEAR_BUTTON_TEXT)
        doResizeLinearButton.actionPerformed = self._doResizeLinear
        
        return doResizeLinearButton
    
    def _chooseGraphObjectType(self):
        objectType = self.graphObjectTypeSelectionBox.getSelectedItem()
        
        if objectType == GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION:
            self.columnSelectionBox.setModel(self.nodeComboBoxModel)
        else:
            self.columnSelectionBox.setModel(self.edgeComboBoxModel)
        
        self._chooseField()
    
    def _chooseField(self):
        self.toggleDefaultFieldVisibility(false)
        self._updateDefaultValueFieldLabel()
        objectType = self.graphObjectTypeSelectionBox.getSelectedItem()
        selectedItem = self.columnSelectionBox.getSelectedItem()
        
        # TODO: This should probably be a separate method, but oh well.
        if objectType == GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION:
            for node in g.nodes:
                if node.__getattr__(selectedItem) is None:
                    self.toggleDefaultFieldVisibility(true)
                    
                    return
        else:
            for edge in g.edges:
                if edge.__getattr__(selectedItem) is None:
                    self.toggleDefaultFieldVisibility(true)
                    
                    return
    
    def _updateDefaultValueFieldLabel(self):
        objectType = self.graphObjectTypeSelectionBox.getSelectedItem().lower()
        selectedItem = self.columnSelectionBox.getSelectedItem()
        self.defaultValueFieldLabel.setText(
            DEFAULT_VALUE_LABEL_TEXT_FORMAT % (objectType, selectedItem))
    
    def _doResizeLinear(self, event):
        selectedFieldName = self.columnSelectionBox.getSelectedItem()
        
        minText = self.minTextField.getText()
        min = float(minText)
        
        maxText = self.maxTextField.getText()
        max = float(maxText)
        
        if self.defaultValueField.isVisible():
            defaultValue = float(self.defaultValueField.getText())
        else:
            defaultValue = None
        
        resizeLinear(eval(selectedFieldName), min, max, defaultValue)
        
        self.setVisible(false)
