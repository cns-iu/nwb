import re # lookupCompartmentNodeAttributeNameForTimestep()
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
    for timestep in range(1000): # TODO
        try:
            colorNodesCompartmentallyAtTime(timestep)
            time.sleep(.01)
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

# TODO Unfinished
def detectMaxTimestep():
    compartmentAtTimeNodeAttributeNames = listCompartmentAtTimeNodeAttributeNames()

    timestepToAttributeName = {}
    for att in compartmentAtTimeNodeAttributeNames:
        attPattern = r'%s(\d)+' % COMPARTMENT_AT_TIME_ATTRIBUTE_NAME_PREFIX
        try:
            timestep = int(re.match(attPattern, att).group(1))
            timestepToAttributeName[timestep] = att
        except Exception:
            continue

# TODO Expensive, but it's either this or iteratively zero-padding and hoping for a match.  Which is better?
def lookupCompartmentNodeAttributeNameForTimestep(timestep):
    compartmentAtTimeNodeAttributeNames = listCompartmentAtTimeNodeAttributeNames()

    timestepToAttributeName = {}
    for att in compartmentAtTimeNodeAttributeNames:
        attPattern = r'%s(\d)+' % COMPARTMENT_AT_TIME_ATTRIBUTE_NAME_PREFIX
        try:
            timestep = int(re.match(attPattern, att).group(1))
            timestepToAttributeName[timestep] = att
        except Exception:
            continue

    try:
        return timestepToAttributeName[timestep]
    except KeyError:
        raise ValueError('No compartmental node attribute name matches timestep %d.' % timestep)


def main():
    setNodeColorsToBlack() # TODO remove or no?
    #compartmentAttributeName = detectCompartmentAtTimeAttributeName()
    compartmentAttributeName = lookupCompartmentNodeAttributeNameForTimestep(0)

    #compartmentToColor = colorNodesCompartmentally(compartmentAttributeName)
    compartments = readUniqueValues(g.nodes, compartmentAttributeName)
    compartmentToColor = makeCompartmentColorScheme(compartments)
    colorItemsByState(g.nodes, compartmentAttributeName, compartmentToColor)
    

    #printLegend(compartmentToColor)


main()
