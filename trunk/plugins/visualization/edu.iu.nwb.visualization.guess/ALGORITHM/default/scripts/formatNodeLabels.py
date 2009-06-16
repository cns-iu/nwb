import javax.swing as swing
import re


FORMAT_NODE_LABELS_TEXT = "Format Node Labels"
ORIGINAL_LABEL_FIELD_NAME = "originallabel"

previousLabelFormattingString = "{%s}" % ORIGINAL_LABEL_FIELD_NAME

def setupOriginalLabelField():
    if ORIGINAL_LABEL_FIELD_NAME not in nodePropertiesList:
        addNodeField(ORIGINAL_LABEL_FIELD_NAME, Types.VARCHAR, "")
        setattr(g.nodes, ORIGINAL_LABEL_FIELD_NAME, g.nodes.label)
        nodePropertiesList.append(ORIGINAL_LABEL_FIELD_NAME)

def setupFormatNodeLabelsMenuItem():
    editMenu = getEditMenu()
    formatNodeLabelsMenuItem = getFormatNodeLabelsMenuItem(editMenu)
    
    if formatNodeLabelsMenuItem is None:
        editMenu.addSeparator()
        formatNodeLabelsMenuItem = createFormatNodeLabelsMenuItem()
        editMenu.add(formatNodeLabelsMenuItem)

def getEditMenu():
    menuBar = ui.getGMenuBar()
    menus = menuBar.getComponents()
    
    # Find the Edit menu.
    for menu in menus:
        if menu.text == "Edit":
            return menu
    
    return None

def getFormatNodeLabelsMenuItem(editMenu):
    numMenuItems = editMenu.getItemCount()
    
    for itemIndex in range(numMenuItems):
        item = editMenu.getItem(itemIndex)
        
        if item is not None and item.getText() == FORMAT_NODE_LABELS_TEXT:
            return item
    
    return None

def createFormatNodeLabelsMenuItem():
    formatNodeLabelsMenuItem = JMenuItem(FORMAT_NODE_LABELS_TEXT)
    formatNodeLabelsMenuItem.actionPerformed = \
        lambda event: formatNodeLabels()
    
    return formatNodeLabelsMenuItem

def formatNodeLabels():
    labelFormattingString = getLabelFormattingStringFromUser()
    
    if labelFormattingString is None:
        return
    
    processLabelFormattingString(labelFormattingString, g.nodes)
    
    global previousLabelFormattingString
    previousLabelFormattingString = labelFormattingString

def getLabelFormattingStringFromUser():
    labelFormattingString = swing.JOptionPane.showInputDialog(
        None,
        "Example: {first_name} {last_name} lives at {address}.",
        "Enter a formatting string for node labels",
        1,
        None,
        None,
        previousLabelFormattingString)
    
    return labelFormattingString

# Parse the label formatting string and apply it to all of the nodes (when
# triggered).

PRE_GROUP_INDEX = 0
OPENING_BRACE_GROUP_INDEX = 1
NODE_ATTRIBUTE_NAME_GROUP_INDEX = 2
CLOSING_BRACE_GROUP_INDEX = 3
POST_GROUP_INDEX = 4

preExpression = r"(?P<pre>.*?)"
openingBraceExpression = r"(?P<openingBrace>{\s*)"
nodeAttributeNameExpression = r"(?P<nodeAttributeName>[_a-zA-Z0-9]*)"
closingBraceExpression = r"(?P<closingBrace>\s*})"
postExpression = r"(?P<post>.*?)$"
expression = r"%s%s%s%s%s" % (preExpression,
                              openingBraceExpression,
                              nodeAttributeNameExpression,
                              closingBraceExpression,
                              postExpression)

pattern = re.compile(expression)

def processLabelFormattingString(labelFormattingString, nodes):
    if not labelFormattingString or not nodes or len(nodes) == 0:
        return
    
    matches = matchAll(labelFormattingString)

# enable this if you want to save old versions of your label string    
#    newBackupLabelFieldName = determineNewBackupLabelFieldName(nodes)
#    addNodeField(newBackupLabelFieldName, Types.VARCHAR, "")
#    
#    if len(matches) == 0:
#        setattr(nodes, newBackupLabelFieldName, nodes.label)
#        nodes.label = labelFormattingString
#        
#        return
    
    for node in nodes:
#        setattr(node, newBackupLabelFieldName, node.label)
        node.label = formNodeLabel(node, labelFormattingString, matches)

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
            match.group("nodeAttributeName"),
            match.group("closingBrace"),
            ""
        ]
        
        matchedTokenSets.append(matchedTokens)
        
        stringToBeMatched = match.group("post")
        match = pattern.match(stringToBeMatched)
    
    matchedTokens[POST_GROUP_INDEX] = stringToBeMatched
    
    return matchedTokenSets

# enable this if you want to save old versions of your label string    
#BASE_BACKUP_LABEL_ATTRIBUTE_NAME = "old_label_"
#
#def determineNewBackupLabelFieldName(nodes):
#    backupLabelNumber = 0
#    
#    while (true):
#        backupLabelAttributeName = "%s%s" % (BASE_BACKUP_LABEL_ATTRIBUTE_NAME,
#                                             backupLabelNumber)
#        
#        if backupLabelAttributeName not in nodePropertiesList:
#            nodePropertiesList.append(backupLabelAttributeName)
#            
#            return backupLabelAttributeName
#        else:
#            backupLabelNumber += 1

def formNodeLabel(node, labelFormattingString, matches):
    labelBeingFormed = ""
    
    for match in matches:
        pre = match[PRE_GROUP_INDEX]
        openingBrace = match[OPENING_BRACE_GROUP_INDEX]
        nodeAttributeName = match[NODE_ATTRIBUTE_NAME_GROUP_INDEX]
        closingBrace = match[CLOSING_BRACE_GROUP_INDEX]
        post = match[POST_GROUP_INDEX]
        
        if nodeAttributeName in nodePropertiesList:
            labelBeingFormed += "%s%s%s" % \
                (pre, getattr(node, nodeAttributeName), post)
        else:
            labelBeingFormed += "%s%s%s%s%s" % \
                (pre, openingBrace, nodeAttributeName, closingBrace, post)
    
    formedLabel = labelBeingFormed
    
    return formedLabel

setupOriginalLabelField()
setupFormatNodeLabelsMenuItem()


#enable this and the line at the bottom to add format node 
#labels button to graph modifier
#def setupFormatNodeLabelsGraphModifierButton():
#    graphModifier = GraphModifier.self
#    buttonPanel = graphModifier.buttonPanel
#    
#    editNodeLabelsButton = JButton(FORMAT_NODE_LABELS_TEXT)
#    editNodeLabelsButton.actionPerformed = \
#        lambda event: formatNodeLabels()
#    
#    graphModifier.editNodeLabelsButton = editNodeLabelsButton
#    buttonPanel.add(graphModifier.editNodeLabelsButton)

#setupFormatNodeLabelsGraphModifierButton()
