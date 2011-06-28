import javax.swing as swing
import re


FORMAT_NODE_LABELS_TEXT = "Format Node Labels"
FORMAT_EDGE_LABELS_TEXT = "Format Edge Labels"
ORIGINAL_LABEL_FIELD_NAME = "originallabel"

PRE_GROUP_INDEX = 0
OPENING_BRACE_GROUP_INDEX = 1
ATTRIBUTE_NAME_GROUP_INDEX = 2
CLOSING_BRACE_GROUP_INDEX = 3
POST_GROUP_INDEX = 4

preExpression = r"(?P<pre>.*?)"
openingBraceExpression = r"(?P<openingBrace>{\s*)"
attributeNameExpression = r"(?P<attributeName>[_a-zA-Z0-9]*)"
closingBraceExpression = r"(?P<closingBrace>\s*})"
postExpression = r"(?P<post>.*?)$"
expression = r"%s%s%s%s%s" % (preExpression,
                              openingBraceExpression,
                              attributeNameExpression,
                              closingBraceExpression,
                              postExpression)

pattern = re.compile(expression)

class GraphObjectLabelFormatter:
    def __init__(self, graphObjects, graphObjectTypeString, propertiesList, addFieldFunction):
        self.graphObjects = graphObjects
        self.graphObjectTypeString = graphObjectTypeString
        self.propertiesList = list(propertiesList)
        self.previousLabelFormattingString = "{%s}" % ORIGINAL_LABEL_FIELD_NAME
        self.addFieldFunction = addFieldFunction
        
#        self.addOriginalLabelFieldToObjects()
    
#    def addOriginalLabelFieldToObjects(self):
#        if ORIGINAL_LABEL_FIELD_NAME not in self.propertiesList:
#            self.addFieldFunction(ORIGINAL_LABEL_FIELD_NAME, Types.VARCHAR, "")
#            setattr(self.graphObjects, ORIGINAL_LABEL_FIELD_NAME, self.graphObjects.label)
#            self.propertiesList.append(ORIGINAL_LABEL_FIELD_NAME)
    
    def hookIntoEditMenu(self, shouldAddSeparator, menuItemText):
        editMenu = getEditMenu()
        menuItem = self._findHookedMenuItem(editMenu, menuItemText)
    
        if menuItem is None:
            if shouldAddSeparator:
                editMenu.addSeparator()
            
            menuItem = self._createMenuItem(menuItemText)
            editMenu.add(menuItem)
    
    def hookIntoGraphModifier(self, buttonText):
        graphModifier = getGraphModifier()
        graphModifierButton = self._findHookedButton(
            graphModifier.formatLabelsButtonPanel, buttonText)
        
        if graphModifierButton is None:
            graphModifierButton = self._createGraphModifierButton(buttonText)
            graphModifier.formatLabelsButtonPanel.add(graphModifierButton)
            graphModifier.update(graphModifier.getGraphics())
    
    def formatLabelsBasedOnUserPrompt(self, event):
        labelFormattingString = self._getLabelFormattingStringFromUser()
        
        if labelFormattingString is None:
            return
        
        self.formatLabels(labelFormattingString)
        self.previousLabelFormattingString = labelFormattingString
        v.repaint()
    
    def formatLabels(self, labelFormattingString):
        if not labelFormattingString or not self.graphObjects or len(self.graphObjects) == 0:
            return
        
        matches = matchAll(labelFormattingString)
        
        for graphObject in self.graphObjects:
            graphObject.label = self.formLabel(graphObject, labelFormattingString, matches)
    
    def formLabel(self, graphObject, labelFormattingString, matches):
        labelBeingFormed = ""
        
        for match in matches:
            pre = match[PRE_GROUP_INDEX]
            openingBrace = match[OPENING_BRACE_GROUP_INDEX]
            attributeName = match[ATTRIBUTE_NAME_GROUP_INDEX]
            closingBrace = match[CLOSING_BRACE_GROUP_INDEX]
            post = match[POST_GROUP_INDEX]
            
            if attributeName in self.propertiesList or hasattr(graphObject, attributeName):
                labelBeingFormed += "%s%s%s" % (pre, getattr(graphObject, attributeName), post)
            else:
                labelBeingFormed += "%s%s%s%s%s" % \
                    (pre, openingBrace, attributeName, closingBrace, post)
        
        formedLabel = labelBeingFormed
        
        return formedLabel
    
    def _findHookedMenuItem(self, menu, menuItemText):
        numMenuItems = menu.getItemCount()
    
        for itemIndex in range(numMenuItems):
            item = menu.getItem(itemIndex)
            
            if item is not None and item.getText() == menuItemText:
                return item
        
        return None

    def _findHookedButton(self, buttonPanel, buttonText):
        for component in buttonPanel.getComponents():
            if component is not None and component.getText() == buttonText:
                return component
        
        return None
    
    def _createMenuItem(self, menuItemText):
        formatLabelsMenuItem = \
            JMenuItem(menuItemText,
                      actionPerformed=self.formatLabelsBasedOnUserPrompt)
        
        return formatLabelsMenuItem
    
    def _createGraphModifierButton(self, buttonText):
        formatLabelsButton = \
            JButton(buttonText,
                    actionPerformed=self.formatLabelsBasedOnUserPrompt)
        
        return formatLabelsButton
    
    def _getLabelFormattingStringFromUser(self):
        labelFormattingString = swing.JOptionPane.showInputDialog(
            None,
            "Example: {first_name} {last_name} lives at {address}.",
            "Enter a formatting string for %s labels" % \
                self.graphObjectTypeString,
            1,
            None,
            None,
            self.previousLabelFormattingString)
        
        return labelFormattingString

def getEditMenu():
    menuBar = ui.getGMenuBar()
    menus = menuBar.getComponents()
    
    # Find the Edit menu.
    for menu in menus:
        if menu.text == "Edit":
            return menu
    
    return None

def getGraphModifier():
    graphModifier = GraphModifier.self
    
    return graphModifier

def matchAll(labelFormattingString):
    matchedTokenSets = []
    stringToBeMatched = labelFormattingString
    match = pattern.match(stringToBeMatched)
    
    if match is None:
        return [[labelFormattingString, "", "", "", ""]]
    
    while match is not None:
        matchedTokens = [
            match.group("pre"),
            match.group("openingBrace"),
            match.group("attributeName"),
            match.group("closingBrace"),
            ""
        ]
        
        matchedTokenSets.append(matchedTokens)
        
        stringToBeMatched = match.group("post")
        match = pattern.match(stringToBeMatched)
    
    matchedTokens[POST_GROUP_INDEX] = stringToBeMatched
    
    return matchedTokenSets

nodeLabelFormatter = GraphObjectLabelFormatter(
    graphObjects=g.nodes,
    graphObjectTypeString="node",
    propertiesList=nodePropertiesList,
    addFieldFunction=addNodeField)
nodeLabelFormatter.hookIntoEditMenu(true, FORMAT_NODE_LABELS_TEXT)
nodeLabelFormatter.hookIntoGraphModifier(FORMAT_NODE_LABELS_TEXT)

edgeLabelFormatter = GraphObjectLabelFormatter(
    graphObjects=g.edges,
    graphObjectTypeString="edge",
    propertiesList=edgePropertiesList,
    addFieldFunction=addEdgeField)
edgeLabelFormatter.hookIntoEditMenu(false, FORMAT_EDGE_LABELS_TEXT)
edgeLabelFormatter.hookIntoGraphModifier(FORMAT_EDGE_LABELS_TEXT)
