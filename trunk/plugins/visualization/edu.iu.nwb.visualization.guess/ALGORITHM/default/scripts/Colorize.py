class ColorizePanel(JPanel):
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
        
        self.defaultColorLabel = self._createDefaultColorLabel()
        self.bottomPanel.add(self.defaultColorLabel)
        self.defaultColorSelectorButton = self._createDefaltColorSelectorButton()
        self.bottomPanel.add(self.defaultColorSelectorButton)
        self.toggleDefaultSelectorVisibility(false)
        
        self.graphObjectTypeSelectionBox = self._createGraphObjectTypeSelectionBox()
        self.topPanel.add(self.graphObjectTypeSelectionBox)
        
        self.columnSelectionBox = self._createColumnSelectionBox()
        self.topPanel.add(self.columnSelectionBox)
        
        self.firstColorLabel = self._createFirstColorLabel()
        self.topPanel.add(self.firstColorLabel)
        self.firstColorSelectorButton = self._createFirstColorSelectorButton()
        self.topPanel.add(self.firstColorSelectorButton)
        
        self.secondColorLabel = self._createSecondColorLabel()
        self.topPanel.add(self.secondColorLabel)
        self.secondColorSelectorButton = self._createSecondColorSelectorButton()
        self.topPanel.add(self.secondColorSelectorButton)
        
        self.doColorizeButton = self._createDoColorizeButton()
        self.topPanel.add(self.doColorizeButton)
    
    def toggleDefaultSelectorVisibility(self, visibility):
        self.defaultColorLabel.setVisible(visibility)
        self.defaultColorSelectorButton.setVisible(visibility)
    
    def _createGraphObjectTypeSelectionBox(self):
        class GraphObjectTypeSelectedListener(awt.event.ActionListener):
            def __init__(self, colorizePanel):
                self.colorizePanel = colorizePanel
            
            def actionPerformed(self, event):
                self.colorizePanel._chooseGraphObjectType()
        
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
            def __init__(self, colorizePanel, columnSelectionBox):
                self.colorizePanel = colorizePanel
                self.columnSelectionBox = columnSelectionBox
            
            def actionPerformed(self, event):
                self.colorizePanel.chooseField()
        
        columnSelectionBox = JComboBox(self.nodeComboBoxModel)
        columnSelectionBox.addActionListener(ColumnSelectedListener(self, columnSelectionBox))
        columnSelectionBox.setEditable(false)
        
        return columnSelectionBox
    
    def _createFirstColorLabel(self):
        firstColorLabel = JLabel(FROM_LABEL_TEXT)
        
        return firstColorLabel
    
    def _createFirstColorSelectorButton(self):
        firstColorSelectorButton = JButton("#")
        firstColorSelectorButton.setPreferredSize(
            Dimension(COLOR_SELECTOR_BUTTON_SIZE, COLOR_SELECTOR_BUTTON_SIZE))
        firstColorSelectorButton.actionPerformed = self._chooseFirstColor
        firstColorSelectorButton.setBackground(Color.lightGray)
        firstColorSelectorButton.setForeground(Color.lightGray)
        
        return firstColorSelectorButton
    
    def _createSecondColorLabel(self):
        secondColorLabel = JLabel(TO_LABEL_TEXT)
        
        return secondColorLabel
    
    def _createSecondColorSelectorButton(self):
        secondColorSelectorButton = JButton("#")
        secondColorSelectorButton.setPreferredSize(
            Dimension(COLOR_SELECTOR_BUTTON_SIZE, COLOR_SELECTOR_BUTTON_SIZE))
        secondColorSelectorButton.actionPerformed = self._chooseSecondColor
        secondColorSelectorButton.setBackground(Color.lightGray)
        secondColorSelectorButton.setForeground(Color.lightGray)
        
        return secondColorSelectorButton
    
    def _createDefaultColorLabel(self):
        defaultColorLabel = JLabel(DEFAULT_VALUE_LABEL_TEXT_FORMAT)
        
        return defaultColorLabel
    
    def _createDefaltColorSelectorButton(self):
        defaultColorSelectorButton = JButton("#")
        defaultColorSelectorButton.setPreferredSize(
            Dimension(COLOR_SELECTOR_BUTTON_SIZE, COLOR_SELECTOR_BUTTON_SIZE))
        defaultColorSelectorButton.actionPerformed = self._chooseDefaultColor
        defaultColorSelectorButton.setBackground(Color.lightGray)
        defaultColorSelectorButton.setForeground(Color.lightGray)
        
        return defaultColorSelectorButton
    
    def _createDoColorizeButton(self):
        doColorizeButton = JButton(DO_COLORIZE_BUTTON_TEXT)
        doColorizeButton.actionPerformed = self.doColorize
        
        return doColorizeButton
    
    def _chooseGraphObjectType(self):
        selectedItem = self.graphObjectTypeSelectionBox.getSelectedItem()
        
        if selectedItem == GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION:
            self.columnSelectionBox.setModel(self.nodeComboBoxModel)
        else:
            self.columnSelectionBox.setModel(self.edgeComboBoxModel)
        
        self.chooseField()
    
    def chooseField(self):
        self.toggleDefaultSelectorVisibility(false)
        self.updateDefaultColorFieldLabel()
        objectType = self.graphObjectTypeSelectionBox.getSelectedItem()
        selectedItem = self.columnSelectionBox.getSelectedItem()
        
        # TODO: This should probably be a separate method, but oh well.
        if objectType == GRAPH_OBJECT_TYPE_SELECTION_BOX_NODE_OPTION:
            for node in g.nodes:
                if node.__getattr__(selectedItem) is None:
                    self.toggleDefaultSelectorVisibility(true)
                    
                    return
        else:
            for edge in g.edges:
                if edge.__getattr__(selectedItem) is None:
                    self.toggleDefaultSelectorVisibility(true)
                    
                    return
    
    def _chooseFirstColor(self, event):
        title = "Choose the First Color"
        color = JColorChooser.showDialog(
            None, title, self.firstColorSelectorButton.getBackground())
        
        if color is not None:
            self.firstColorSelectorButton.setBackground(color)
            self.firstColorSelectorButton.setForeground(color)
    
    def _chooseSecondColor(self, event):
        title = "Choose the Second Color"
        color = JColorChooser.showDialog(
            None, title, self.secondColorSelectorButton.getBackground())
        
        if color is not None:
            self.secondColorSelectorButton.setBackground(color)
            self.secondColorSelectorButton.setForeground(color)
    
    def _chooseDefaultColor(self, event):
        title = "Choose the Default Color"
        color = JColorChooser.showDialog(
            None, title, self.defaultColorSelectorButton.getBackground())
        
        if color is not None:
            self.defaultColorSelectorButton.setBackground(color)
            self.defaultColorSelectorButton.setForeground(color)
    
    def updateDefaultColorFieldLabel(self):
        objectType = self.graphObjectTypeSelectionBox.getSelectedItem().lower()
        selectedItem = self.columnSelectionBox.getSelectedItem()
        self.defaultColorLabel.setText(
            DEFAULT_VALUE_LABEL_TEXT_FORMAT % (objectType, selectedItem))
    
    def doColorize(self, event):
        selectedFieldName = self.columnSelectionBox.getSelectedItem()
        
        firstColor = self.firstColorSelectorButton.getBackground()
        firstColorTuple = [
            firstColor.getRed(), firstColor.getGreen(), firstColor.getBlue()
        ]
        
        secondColor = self.secondColorSelectorButton.getBackground()
        secondColorTuple = [
            secondColor.getRed(), secondColor.getGreen(), secondColor.getBlue()
        ]
        
        if self.defaultColorSelectorButton.isVisible():
            defaultColor = self.defaultColorSelectorButton.getBackground()
            defaultColor = [
                defaultColor.getRed(), defaultColor.getGreen(), defaultColor.getBlue()
            ]
        else:
            defaultColor = None
        
        colorize(eval(selectedFieldName), firstColorTuple, secondColorTuple, defaultColor)
        
        self.setVisible(false)
