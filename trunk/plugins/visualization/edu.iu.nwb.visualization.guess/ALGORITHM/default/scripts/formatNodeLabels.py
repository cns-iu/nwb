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









#################### TODO Needs to be in its own file

#import re # lookupCompartmentNodeAttributeNameForTimestep()
import random # safeColorByRandom()
import operator # euclideanDistance()
import time # colorNodesCompartmentallyOverTime()

COMPARTMENT_AT_TIME_ATTRIBUTE_NAME_PREFIX = 'compartment_at_time_'
NONE_COLOR = (125, 125, 125, 255)
# Keys must agree with format returned by makeMasterKeyFor(compartment)
# Values should be a list of colors in order of descending preference
COMPARTMENT_TO_COLOR_MASTER = \
    {'S': [(10, 152, 233, 255)],
     'E': [(229, 135, 32, 255)],
     'L': [(0, 183, 133, 255)],
     'I': [(253, 80, 38, 255), (229, 135, 32, 255), (237, 200, 22, 255)],
     'R': [(131, 187, 64, 255)]}
True = 1
False = 0

##### Utilities ##################################
def dict(tuples):
    d = {}
    for k, v in tuples:
        d[k] = v

    return d

def removeDuplicates(ls):
    return dict([(item, None) for item in ls]).keys()

def meanInt(a, b):
    return int((a + b) / 2.0)

def readUniqueValues(items, attributeName):
    # TODO Could be more efficient..
    return removeDuplicates([getattr(item, attributeName) for item in items])

def setNodeColorsToBlack(): # TODO Just for testing
    for n in g.nodes:
        n.color = 'black'

# The first available item in candidates
#   If none, the last candidate
#     If none, None
def firstAvailableOrLastCandidate(candidates, unavailable):
    available = [cand for cand in candidates if cand not in unavailable]
	
    try:
        return available[0]
    except IndexError:
        try:
            return candidates[-1]
        except IndexError:
            return None

#### Color utilities #############################
def safeColorCoordinate(coordinates):
    coordinates = [0] + [255] + coordinates
    coordinates.sort()
    coordinates = removeDuplicates(coordinates)

    spreadToCoord = dict([(int(coordinates[i+1] - coordinates[i]), meanInt(coordinates[i+1], coordinates[i])) for i in range(len(coordinates) - 1)])
    
    # Max might not be unique but we don't really care
    maxSpread = max(spreadToCoord.keys())

    return spreadToCoord[maxSpread]

def euclideanDistance(v1, v2):
    return sqrt(reduce(operator.add, [(c2 - c1)**2 for c1, c2 in zip(v1, v2)]))

def safeColorByRandom(colors):
    tolerance = 400.0 # No point in setting this higher than sqrt(3*255^2) ~= 440
    safeColorFound = False

    ATTEMPTS_PER_TOLERANCE = 100
    while not safeColorFound:
        for attempt in range(ATTEMPTS_PER_TOLERANCE):
            randomColor = (random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
            distances = [euclideanDistance(randomColor, color) for color in colors]
            
            if min(distances) >= tolerance:
                return randomColor + (255,)

        tolerance = max(tolerance / 2.0, tolerance - 20.0)

def safeColor(colors):
    return safeColorByRandom(colors)


##### Enumerative coloring ######################
def makeStateColorSchemeFromMaster(states, masterKeyFor, masterColorScheme, noneColor):
    colorFor = {None: noneColor}
    uncoloredStates = []

    # Assign colors according to the master scheme where possible
    for state in states:
        if state == None:
            continue

        try:
            candidateColors = masterColorScheme[masterKeyFor(state)]
            unavailableColors = colorFor.values()
            availableColors = [cand for cand in candidateColors if cand not in unavailableColors]
            colorFor[state] = availableColors[0]
            #colorFor[state] = firstAvailableOrLastCandidate(candidateColors, unavailableColors)
        except (KeyError, IndexError):
            uncoloredStates += [state]     

    # States not yet assigned a color are given one
    for state in uncoloredStates:
        nextSafeColor = safeColor(colorFor.values())
        colorFor[state] = nextSafeColor
    
    return colorFor
        
def colorItemsByState(items, attributeName, stateToColor):
    for item in items:
        state = getattr(item, attributeName)

        try:
            # Color tuples
            item.color = '%d,%d,%d,%d' % stateToColor[state]
        except ValueError:
            # TODO Necessary?
            # Literal colors
            item.color = stateToColor[state]

def colorItemsByEnumeration(items, attributeName, stateColorSchemeMaker):
    states = readUniqueValues(items, attributeName)
    # TODO It would probably be best to put states in some order here.. according to some comparator parameter?
    stateToColor = stateColorSchemeMaker(states)

    colorItemsByState(items, attributeName, stateToColor)

    return stateToColor

def printLegend(stateToColor):
    # TODO print seems to not work as desired?
    print 'Color legend:'
    for state, color in stateToColor.items():
        print state, '\t\t\t', color
    print


##### Compartmental coloring ####################
def makeMasterKeyFor(compartment):
    return compartment[0].upper()

def makeCompartmentColorScheme(compartments):
    return makeStateColorSchemeFromMaster(compartments, makeMasterKeyFor, COMPARTMENT_TO_COLOR_MASTER, NONE_COLOR)

def colorCompartmentally(items, compartmentAttributeName):
    compartmentToColor = colorItemsByEnumeration(items, compartmentAttributeName, makeCompartmentColorScheme)

    return compartmentToColor

def colorNodesCompartmentally(compartmentAttributeName):
    compartmentToColor = colorCompartmentally(g.nodes, compartmentAttributeName)

    return compartmentToColor

COMPARTMENT_TO_COLOR = None
def colorNodesCompartmentallyAtTime(timestep):
    global COMPARTMENT_TO_COLOR
    
    compartmentAttributeName = lookupCompartmentNodeAttributeNameForTimestep(timestep)

    if COMPARTMENT_TO_COLOR != None:
        # Once a color scheme is defined, re-use it
        colorItemsByState(g.nodes, compartmentAttributeName, COMPARTMENT_TO_COLOR)
    else:
        COMPARTMENT_TO_COLOR = colorCompartmentally(g.nodes, compartmentAttributeName)

    return COMPARTMENT_TO_COLOR

# TODO Unfinished
def colorNodesCompartmentallyOverTime():
    for timestep in range(detectMinTimestep(), detectMaxTimestep() + 1):
        try:
            colorNodesCompartmentallyAtTime(timestep)
            time.sleep(.001)
        except ValueError:
            pass # TODO


##### Time #######################################
def listCompartmentAtTimeNodeAttributeNames():
    nodeAttributeNames = [str(att) for att in g.getNodeSchema().allFields()]
    compartmentAtTimeNodeAttributeNames = [att for att in nodeAttributeNames if att.startswith(COMPARTMENT_AT_TIME_ATTRIBUTE_NAME_PREFIX)]

    return compartmentAtTimeNodeAttributeNames

def detectCompartmentAtTimeAttributeName():
    compartmentAtTimeNodeAttributeNames = listCompartmentAtTimeNodeAttributeNames()

    if len(compartmentAtTimeNodeAttributeNames) == 0:
        raise ValueError('No compartmental time node attribute detected.')
    elif len(compartmentAtTimeNodeAttributeNames) == 1:
        return compartmentAtTimeNodeAttributeNames[0]
    else:
        # TODO Pick one arbitrarily?
        return compartmentAtTimeNodeAttributeNames[0]

def readTimestepToAttributeName():
    compartmentAtTimeNodeAttributeNames = listCompartmentAtTimeNodeAttributeNames()

    timestepToAttributeName = {}
    for att in compartmentAtTimeNodeAttributeNames:
        attPattern = r'%s(\d+)' % COMPARTMENT_AT_TIME_ATTRIBUTE_NAME_PREFIX
        try:
            timestep = int(re.match(attPattern, att).group(1))
            timestepToAttributeName[timestep] = att
        except Exception:
            continue

    return timestepToAttributeName

def detectMinTimestep():
    timestepToAttributeName = readTimestepToAttributeName()
    return min(timestepToAttributeName.keys())

def detectMaxTimestep():
    timestepToAttributeName = readTimestepToAttributeName()
    return max(timestepToAttributeName.keys())

# TODO Expensive, but it's either this or iteratively zero-padding and hoping for a match.  Which is better?
def lookupCompartmentNodeAttributeNameForTimestep(timestep):
    timestepToAttributeName = readTimestepToAttributeName()

    try:
        return timestepToAttributeName[timestep]
    except KeyError:
        raise ValueError('No compartmental node attribute name matches timestep %d.' % timestep)


def main():    
    try:
        #setNodeColorsToBlack() # TODO remove or no?
        compartmentAttributeName = detectCompartmentAtTimeAttributeName()
        #compartmentAttributeName = lookupCompartmentNodeAttributeNameForTimestep(0)

        #compartmentToColor = colorNodesCompartmentally(compartmentAttributeName)
        compartments = readUniqueValues(g.nodes, compartmentAttributeName)
        compartmentToColor = makeCompartmentColorScheme(compartments)
        colorItemsByState(g.nodes, compartmentAttributeName, compartmentToColor)

        #printLegend(compartmentToColor)
    except Exception:
        return


main()

