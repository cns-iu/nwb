from re import match
from random import randint
from time import sleep
from thread import start_new_thread

##### Graph utilities #############################################################################
def readUniqueValuesInColumns(graphItems, columns):
    # Emulating a set using dictionary keys.  The values are ignored.
    uniqueValues = {}
    
    for column in columns:
        for graphItem in graphItems:
            try:
                value = getattr(graphItem, column)
                uniqueValues[value] = None
            except AttributeError:
                continue
    
    return uniqueValues.keys()

def setGraphItemColorsToBlack(graphItems=g.nodes):
    for graphItem in graphItems:
        graphItem.color = 'black'


#### Color utilities ###############################################################################
# Because the RGB coordinates span (0, 0, 0) to (255, 255, 255),
# there's no point in setting initialStrictness higher than sqrt(3*255^2) ~= 440.
def randomColorDistinguishableFrom(colors, initialStrictness=440.0, attemptsPerStrictness=50):
    def euclideanDistance(v1, v2):
        def sum(numbers):
            return reduce(lambda x, y: x + y, numbers)
        
        return sqrt(sum([(c1 - c2)**2 for c1, c2 in zip(v1, v2)]))

    strictness = initialStrictness 

    while 1:
        for attempt in range(attemptsPerStrictness):
            candidateRGB = (randint(0, 255), randint(0, 255), randint(0, 255))
            rgbDistances = [euclideanDistance(candidateRGB, color[:3]) for color in colors]
            
            if len(colors) == 0 or min(rgbDistances) >= strictness:
                color = candidateRGB + (255,)
                return color

        # Decrease strictness and try again
        strictness = max(strictness / 2.0, strictness - 20.0)


##### Enumerative coloring ########################################################################
"""
"color scheme"
    A map from attribute values (enumerative "states") to color 4-tuples (r, g, b, transparency).
    Each coordinate ranges from 0 to 255.
"color master"
    Like a color scheme, but:
        - Instead of mapping literal states, its keys are derived from a state according to a
        given masterKeyMaker function.
        - Instead of mapping to a single color, its values are a list of colors in order of
        descending preference.
"""

class ColorMaster:       
    def __init__(self, masterColorScheme=None, masterKeyMaker=lambda x: x):
        if masterColorScheme is None:
            masterColorScheme = {}
                     
        self.masterColorScheme = masterColorScheme            
        self.masterKeyMaker = masterKeyMaker
    
    def colorFor(self, state):
        return self.masterColorScheme[self.masterKeyMaker(state)]

def makeColorSchemeForNodeColumn(attributeName, colorMaster=ColorMaster()):
    states = readUniqueValuesInColumns(g.nodes, [attributeName])
    
    return makeColorSchemeForStates(states, colorMaster)

def makeColorSchemeForStates(states, colorMaster=ColorMaster()):
    colorScheme = {}
    uncoloredStates = []

    # Assign colors according to the color master where possible
    for state in states:
        try:
            candidateColors = colorMaster.colorFor(state)
            usedColors = colorScheme.values()
            availableColors = [cand for cand in candidateColors if cand not in usedColors]
            colorScheme[state] = availableColors[0]
        except (KeyError, IndexError):
            uncoloredStates += [state]

    # States not colored by the master are given (somewhat) random ones
    for state in uncoloredStates:
        usedColors = colorScheme.values()
        colorScheme[state] = randomColorDistinguishableFrom(usedColors)
    
    return colorScheme

def colorNodesByState(attributeName, colorScheme=None):
    return colorGraphItemsByState(g.nodes, attributeName, colorScheme)

def colorGraphItemsByState(graphItems, attributeName, colorScheme=None):
    if colorScheme is None:
        possibleStates = readUniqueValuesInColumns(graphItems, [attributeName])
        colorScheme = makeColorSchemeForStates(possibleStates)
    
    for graphItem in graphItems:
        try:
            state = getattr(graphItem, attributeName)
        except AttributeError:
            continue # TODO Or something else?

        try:
            # Color 4-tuples like (255,255,255,255)
            graphItem.color = '%d,%d,%d,%d' % colorScheme[state]
        except ValueError:
            # Literal colors like 'white'
            graphItem.color = colorScheme[state]
    
    v.repaint()
    
    return colorScheme    

def printLegend(colorScheme):
    # TODO print can mess up the interpreter a little
    print 'Color legend:'
    for x, color in colorScheme.items():
        print x, '\t\t\t', color
    print '\n'


##### Animation of enumerative coloring ############################################################
"""
"frame"
    A time-located graph item attribute name,
    e.g. "value_at_time_1" is a frame of the "value_at_time_" animation.
"animation"
    A collection of frames.
    An animation's name is the common prefix of its frames, e.g. "value_at_time_".
"timestep"
    An integer-valued index to some frame.
    "value_at_time_1" is the timestep=1 frame of the "value_at_time_" animation.
"""
FRAME_PATTERN = r'(.*[^\d])(\d+)'

def isAFrame(attributeName):
    if attributeName is None:
        return 0
    
    return match(FRAME_PATTERN, attributeName)

def extractAnimationName(frame):
    try:
        return match(FRAME_PATTERN, frame).group(1)
    except AttributeError:
        return None

def readNodeAttributeNames():
    return [str(att) for att in g.getNodeSchema().allFields()]

def detectAnimationNames():
    def removeDuplicates(ls):
        d = {}
        for item in ls:
            d[item] = None
        return d.keys()
    
    # TODO Also scan edge schema?
    nodeFrames = [att for att in readNodeAttributeNames() if isAFrame(att)]
    animationNames = removeDuplicates([extractAnimationName(frame) for frame in nodeFrames])
    animationNames.sort()
    
    return animationNames

def detectAnimationName():
    animationNames = detectAnimationNames()
    
    try:
        return animationNames[0]
    except IndexError:
        return None

def listFramesInAnimation(animationName=detectAnimationName()):
    def attributeIsInAnimation(attributeName, animationName):        
        if animationName is None:
            return 0
        
        return attributeName.startswith(animationName)
    
    animationFrames = \
        [att for att in readNodeAttributeNames() if attributeIsInAnimation(att, animationName)]
    animationFrames.sort()

    return animationFrames

def listTimestepsInAnimation(animationName=detectAnimationName()):
    timesteps = makeTimestepToFrame(animationName).keys()
    timesteps.sort()
    return timesteps

def detectFirstTimestep(animationName=detectAnimationName()):
    try:
        return min(listTimestepsInAnimation(animationName))
    except ValueError:
        return None

def makeTimestepToFrame(animationName):
    timestepToFrame = {}
    for frame in listFramesInAnimation(animationName):
        framePattern = r'%s(\d+)' % animationName
        try:
            timestep = int(match(framePattern, frame).group(1))
            timestepToFrame[timestep] = frame
        except Exception:
            continue

    return timestepToFrame

def getAnimationFrameAtTime(animationName, timestep):
    timestepToFrame = makeTimestepToFrame(animationName)

    try:
        return timestepToFrame[timestep]
    except KeyError:
        raise ValueError('No frame for timestep %d in animation %s.' % (timestep, animationName))

def readStatesInAnimation(graphItems, animationName=detectAnimationName()):
    return readUniqueValuesInColumns(graphItems, listFramesInAnimation(animationName))

def makeColorSchemeForAnimation(
        graphItems=g.nodes, animationName=detectAnimationName(), colorMaster=ColorMaster()):
    allPossibleStates = readStatesInAnimation(graphItems, animationName)
    
    return makeColorSchemeForStates(allPossibleStates, colorMaster)

# Globally memoized color schemes for consistency across multiple animations and timesteps
ANIMATION_NAME_TO_COLOR_SCHEME = {}
def showTimestep(
        timestep=detectFirstTimestep(), animationName=detectAnimationName(), colorScheme=None):
    global ANIMATION_NAME_TO_COLOR_SCHEME

    if animationName is None or timestep is None:
        return {}

    # Determine color scheme.
    if colorScheme is not None:
        # If a color scheme is given, memoize and use it.
        ANIMATION_NAME_TO_COLOR_SCHEME[animationName] = colorScheme
    else:
        if animationName in ANIMATION_NAME_TO_COLOR_SCHEME.keys():
            # Use memoized scheme.
            colorScheme = ANIMATION_NAME_TO_COLOR_SCHEME[animationName]
        else:
            # Make a new scheme and memoize it.
            colorScheme = makeColorSchemeForAnimation(animationName=animationName)
            ANIMATION_NAME_TO_COLOR_SCHEME[animationName] = colorScheme
    
    #printLegend(colorScheme)
    
    # Apply coloring.
    return colorNodesByState(getAnimationFrameAtTime(animationName, timestep), colorScheme)

def animate(animationName=detectAnimationName(), secondsPer=0.4, colorScheme=None):
    timesteps = listTimestepsInAnimation(animationName)
    timesteps.sort()
    
    if len(timesteps) > 0:    
        def showEachTimestep(animationName, timesteps, secondsPer, colorScheme):
            for timestep in timesteps:
                showTimestep(timestep, animationName, colorScheme)
                sleep(secondsPer)
                
            #print 'Animation "%s" complete.\n\n' % animationName 
        
        start_new_thread(showEachTimestep, (animationName, timesteps, secondsPer, colorScheme))

def showFirstTimestep(animationName=detectAnimationName(), colorScheme=None):
    return showTimestep(detectFirstTimestep(animationName), animationName, colorScheme)


##### Compartmental coloring #######################################################################
def compartmentColorMasterKeyMaker(compartment):
    if compartment is None:
        return None
    else:
        return compartment[0].upper()

COMPARTMENT_AT_TIME_ANIMATION_NAME = 'compartment_at_time_'
COMPARTMENT_COLOR_MASTER = \
    ColorMaster(
        {'S': [(10, 152, 233, 255)],
         'E': [(229, 135, 32, 255)],
         'L': [(0, 183, 133, 255)],
         'I': [(253, 80, 38, 255), (229, 135, 32, 255), (237, 200, 22, 255)],
         'R': [(131, 187, 64, 255)],
         None: [(125, 125, 125, 255)]},
        compartmentColorMasterKeyMaker)

def makeCompartmentColorScheme():
    return makeColorSchemeForAnimation(
                animationName=COMPARTMENT_AT_TIME_ANIMATION_NAME,
                colorMaster=COMPARTMENT_COLOR_MASTER)

def showFirstCompartmentTimestep():
    return showFirstTimestep(COMPARTMENT_AT_TIME_ANIMATION_NAME, makeCompartmentColorScheme())


def main():
    try:
        showFirstCompartmentTimestep()
    except Exception:
        # TODO Do nothing?
        return


main()