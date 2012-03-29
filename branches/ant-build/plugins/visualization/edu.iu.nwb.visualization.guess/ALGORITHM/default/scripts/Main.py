
# AUTOMATICALLY GENERATED
# Defines wrapper methods that can be used as shortcuts in the interpreter

from com.hp.hpl.guess import Guess

from com.hp.hpl.guess.piccolo import Arrow

from com.hp.hpl.guess.piccolo import Morpher

from com.hp.hpl.guess.util.intervals import DefaultRangeManager

def execfile(s):
	interp.execfile(s)

def makeFromGDF(filename):
	g.makeFromGDF(filename)

def centerAfterLayout(state):
	g.centerAfterLayout(state)

def removeNodeComplete(n):
	g.removeNodeComplete(n)

def initConnection(*d):
	if len(d) == 1:
		r.initConnection(d[0])
	elif len(d) == 0:
		r.initConnection()
	else:
		raise ValueError("Incorrect number of arguments")


def sugiyamaLayout(*d):
	if len(d) == 0:
		g.sugiyamaLayout()
	elif len(d) == 1:
		g.sugiyamaLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def getAppletContext():
	return Guess.getAppletContext()

def generateColors(*d):
	if len(d) == 3:
		return Colors.generateColors(d[0],d[1],d[2])
	elif len(d) == 4:
		return Colors.generateColors(d[0],d[1],d[2],d[3])
	else:
		raise ValueError("Incorrect number of arguments")


def makeFromGML(filename):
	g.makeFromGML(filename)

def searchOverlap(range):
	return DefaultRangeManager.searchOverlap(range)

def exportPS(filename):
	v.exportPS(filename)

def compressOverlapping():
	g.compressOverlapping()

def exportSWF(filename):
	v.exportSWF(filename)

def exportPDF(filename):
	v.exportPDF(filename)

def groupBy(*d):
	if len(d) == 2:
		return g.groupBy(d[0],d[1])
	elif len(d) == 1:
		return g.groupBy(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def jSpringLayout(*d):
	if len(d) == 0:
		g.jSpringLayout()
	elif len(d) == 1:
		g.jSpringLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def setDisplayBackground(c):
	v.setDisplayBackground(c)

def setNodeDisappearBy(nodeDisBy):
	Morpher.setNodeDisappearBy(nodeDisBy)

def gemLayout(*d):
	if len(d) == 0:
		g.gemLayout()
	elif len(d) == 1:
		g.gemLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def averageColor(c1,c2):
	return Colors.averageColor(c1,c2)

def makeErdosRenyiRandom(nodes,p):
	g.makeErdosRenyiRandom(nodes,p)

def ls(*d):
	if len(d) == 2:
		db.loadState(d[0],d[1])
	elif len(d) == 1:
		db.loadState(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def exportGDF(filename):
	g.exportGDF(filename)

def getNodeAppearAfter():
	return Morpher.getNodeAppearAfter()

def weightedKKLayout():
	g.weightedKKLayout()

def removeEdgeComplete(e):
	g.removeEdgeComplete(e)

def kkLayout(*d):
	if len(d) == 0:
		g.kkLayout()
	elif len(d) == 2:
		g.kkLayout(d[0],d[1])
	else:
		raise ValueError("Incorrect number of arguments")


def addNodeField(fieldName,sqlType,defaultValue):
	return g.addNodeField(fieldName,sqlType,defaultValue)

def exportCGM(filename):
	v.exportCGM(filename)

def createConvexHull(seq,c):
	return vf.createConvexHull(seq,c)

def isRConnected():
	return r.isConnected()

def mdsLayout():
	g.mdsLayout()

def makeLattice1DRandom(nodes,tor):
	g.makeLattice1DRandom(nodes,tor)

def removeComplete(seq):
	g.removeComplete(seq)

def setEdgeAppearAfter(edgeAppearAfter):
	Morpher.setEdgeAppearAfter(edgeAppearAfter)

def makeFromDL(filename):
	g.makeFromDL(filename)

def remove(seq):
	return g.remove(seq)

def removeEdge(e):
	return g.removeEdge(e)

def getNodeSchema():
	return g.getNodeSchema()

def getEdgeDisappearBy():
	return Morpher.getEdgeDisappearBy()

def addRange(o,range):
	DefaultRangeManager.addRange(o,range)

def stopMovie():
	v.stopMovie()

def addEdge(node1,node2):
	return g.addEdge(node1,node2)

def getMTF():
	return Guess.getMTF()

def getSynchronous():
	return Guess.getSynchronous()

def setBackgroundImage(*d):
	if len(d) == 1:
		v.setBackgroundImage(d[0])
	elif len(d) == 3:
		v.setBackgroundImage(d[0],d[1],d[2])
	else:
		raise ValueError("Incorrect number of arguments")


def jkkLayout1():
	g.jkkLayout1()

def setNodeAppearAfter(nodeAppearAfter):
	Morpher.setNodeAppearAfter(nodeAppearAfter)

def removeDisconnected():
	return g.removeDisconnected()

def dagLayout():
	g.dagLayout()

def expandOverlapping():
	g.expandOverlapping()

def dragwindow():
	DragWindow.create()

def getEdgeAppearAfter():
	return Morpher.getEdgeAppearAfter()

def stoplog():
	interp.stoplog()

def setMTF(state):
	Guess.setMTF(state)

def add(seq):
	g.add(seq)

def drawwindow():
	DrawWindow.create()

def removeNode(n):
	return g.removeNode(n)

def hideDisconnected():
	g.hideDisconnected()

def getDirected():
	return vf.getDirected()

def springLayout(*d):
	if len(d) == 0:
		g.springLayout()
	elif len(d) == 1:
		g.springLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def setDirected(dir):
	vf.setDirected(dir)

def jfrLayout():
	g.jfrLayout()

def setPageRankBias(bias):
	g.setPageRankBias(bias)

def setFrozen(state):
	v.setFrozen(state)

def makeEppsteinRandom(nodes,edges,r):
	g.makeEppsteinRandom(nodes,edges,r)

def circleLayout(*d):
	if len(d) == 0:
		g.circleLayout()
	elif len(d) == 2:
		g.circleLayout(d[0],d[1])
	elif len(d) == 4:
		g.circleLayout(d[0],d[1],d[2],d[3])
	else:
		raise ValueError("Incorrect number of arguments")


def weakComponentClusters():
	return g.weakComponentClusters()

def rshutdown():
	r.shutdown()

def translateLayout(newX,newY):
	g.translateLayout(newX,newY)

def removeBackgroundImage():
	v.removeBackgroundImage()

def getSubgraph(name):
	return Subgraph.getRootSubgraph(name)

def groupAndSortBy(*d):
	if len(d) == 1:
		return g.groupAndSortBy(d[0])
	elif len(d) == 2:
		return g.groupAndSortBy(d[0],d[1])
	else:
		raise ValueError("Incorrect number of arguments")


def searchContained(range):
	return DefaultRangeManager.searchContained(range)

def physicsLayout(*d):
	if len(d) == 0:
		g.physicsLayout()
	elif len(d) == 1:
		g.physicsLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def getGraphMap():
	return r.getGraphMap()

def sortBy(*d):
	if len(d) == 2:
		return g.sortBy(d[0],d[1])
	elif len(d) == 1:
		return g.sortBy(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def reval(s):
	return r.evalString(s)

def removeConvexHulls(seq):
	vf.removeConvexHulls(seq)

def randomLayout(*d):
	if len(d) == 0:
		g.randomLayout()
	elif len(d) == 2:
		g.randomLayout(d[0],d[1])
	else:
		raise ValueError("Incorrect number of arguments")


def randomColor(*d):
	if len(d) == 0:
		return Colors.randomColor()
	elif len(d) == 1:
		return Colors.randomColor(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def moveLayout(newX,newY):
	g.moveLayout(newX,newY)

def sql(expression):
	db.q(expression)

def morph(state,duration):
	g.morph(state,duration)

def makeSimpleDirectedRandom(nodes,edges):
	g.makeSimpleDirectedRandom(nodes,edges)

def exportJPG(filename):
	v.exportJPG(filename)

def radialLayout(center):
	g.radialLayout(center)

def density():
	return g.density()

def overrideArrowLength(length):
	Arrow.overrideArrowLength(length)

def rescaleLayout(*d):
	if len(d) == 2:
		g.rescaleLayout(d[0],d[1])
	elif len(d) == 1:
		g.rescaleLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def setSynchronous(state):
	Guess.setSynchronous(state)

def weightedSpringLayout(*d):
	if len(d) == 2:
		g.weightedSpringLayout(d[0],d[1])
	elif len(d) == 3:
		g.weightedSpringLayout(d[0],d[1],d[2])
	else:
		raise ValueError("Incorrect number of arguments")


def resizeLinear(f,start,end):
	g.resizeLinear(f,start,end)

def exportGIF(filename):
	v.exportGIF(filename)

def binPackLayout(*d):
	if len(d) == 0:
		g.binPackLayout()
	elif len(d) == 1:
		g.binPackLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def getJythonConsole():
	return Guess.getJythonConsole()

def colorize(*d):
	if len(d) == 1:
		g.colorize(d[0])
	elif len(d) == 3:
		g.colorize(d[0],d[1],d[2])
	elif len(d) == 4:
		g.colorize(d[0],d[1],d[2],d[3])
	else:
		raise ValueError("Incorrect number of arguments")


def frLayout(*d):
	if len(d) == 0:
		g.frLayout()
	elif len(d) == 2:
		g.frLayout(d[0],d[1])
	else:
		raise ValueError("Incorrect number of arguments")


def setEdgeDisappearBy(edgeDisBy):
	Morpher.setEdgeDisappearBy(edgeDisBy)

def infowindow(*d):
	if len(d) == 0:
		InfoWindow.create()
	elif len(d) == 0:
		SpreadSheet.create()
	else:
		raise ValueError("Incorrect number of arguments")


def getDisplayBackground():
	return v.getDisplayBackground()

def edgeBetweennessClusters(numEdgesToRemove):
	return g.edgeBetweennessClusters(numEdgesToRemove)

def exportPNG(filename):
	v.exportPNG(filename)

def exportSVG(filename):
	v.exportSVG(filename)

def ss(state):
	db.saveState(state)

def overrideArrowWidth(width):
	Arrow.overrideArrowWidth(width)

def addEdgeField(fieldName,sqlType,defaultValue):
	return g.addEdgeField(fieldName,sqlType,defaultValue)

def getPageRankBias():
	return g.getPageRankBias()

def resizeRandom(f,start,end):
	g.resizeRandom(f,start,end)

def sugiyamaLayout2():
	g.sugiyamaLayout2()

def removeConvexHull(c):
	vf.removeConvexHull(c)

def exportJAVA(filename):
	v.exportJAVA(filename)

def getConvexHulls():
	return vf.getConvexHulls()

def getEdgeSchema():
	return g.getEdgeSchema()

def startMovie(*d):
	if len(d) == 2:
		v.startMovie(d[0],d[1])
	elif len(d) == 3:
		v.startMovie(d[0],d[1],d[2])
	else:
		raise ValueError("Incorrect number of arguments")


def exportEPS(filename):
	v.exportEPS(filename)

def makeKleinbergRandom(nodes,clust):
	g.makeKleinbergRandom(nodes,clust)

def makeBarabasiAlbertRandom(vert,edges,evolve):
	g.makeBarabasiAlbertRandom(vert,edges,evolve)

def isomLayout(*d):
	if len(d) == 0:
		g.isomLayout()
	elif len(d) == 1:
		g.isomLayout(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def exportEMF(filename):
	v.exportEMF(filename)

def makeFromPajek(filename):
	g.makeFromPajek(filename)

def center(*d):
	if len(d) == 0:
		v.center()
	elif len(d) == 1:
		v.center(d[0])
	else:
		raise ValueError("Incorrect number of arguments")


def setRankerWeightField(f):
	g.setRankerWeightField(f)

def log(filename):
	interp.log(filename)

def searchContains(range):
	return DefaultRangeManager.searchContains(range)

def makeLattice2DRandom(nodes,tor):
	g.makeLattice2DRandom(nodes,tor)

def readjustEdges():
	g.readjustEdges()

def jkkLayout2():
	g.jkkLayout2()

def complement(seq):
	return g.complement(seq)

def removeSelfLoops():
	return g.removeSelfLoops()

def biComponentClusters():
	return g.biComponentClusters()

def getNodeDisappearBy():
	return Morpher.getNodeDisappearBy()

def searchExact(range):
	return DefaultRangeManager.searchExact(range)

def makeSimpleRandom(nodes,edges):
	g.makeSimpleRandom(nodes,edges)

def getStates():
	return db.getStates()

__FUNCTION_DICTIONARY = {"execfile" : "void execfile(String s);\n", 
						"makeFromGDF" : "void makeFromGDF(String filename) throws java.io.IOException \n", 
						"centerAfterLayout" : "void centerAfterLayout(boolean state) \n should we center after doing a layout\n disable this to control it yourself\n", 
						"removeNodeComplete" : "void removeNodeComplete(Node n) \n removes a node completely (node, and all edges connected to it, \n will likely be unrecoverable).  Removes from all states.\n \tparam n the node to remove\n", 
						"initConnection" : "void initConnection(String server) \n\nvoid initConnection() \n", 
						"sugiyamaLayout" : "void sugiyamaLayout() \n Sugiyama\n\nvoid sugiyamaLayout(boolean bends) \n Sugiyama\n", 
						"getAppletContext" : "static AppletContext getAppletContext() \n get the applet context\n", 
						"generateColors" : "static ArrayList generateColors(String startC, String endC, int inBetween) \n\nstatic ArrayList generateColors(String startC, String middleC, String endC, int inBetween) \n percent;\n percent;\n", 
						"makeFromGML" : "void makeFromGML(String filename) \n", 
						"searchOverlap" : "static Collection searchOverlap(String range) \n", 
						"exportPS" : "void exportPS(String filename);\n", 
						"compressOverlapping" : "void compressOverlapping() \n", 
						"exportSWF" : "void exportSWF(String filename);\n", 
						"exportPDF" : "void exportPDF(String filename);\n", 
						"groupBy" : "Collection groupBy(PySequence seq, Field field) \n\nCollection groupBy(Field field) \n", 
						"jSpringLayout" : "void jSpringLayout() \n the JUNG Spring Layout (seems a little broken).  Asks\n every 30 seconds if you want to keep running\n\nvoid jSpringLayout(int max) \n run the JUNG spring layout for max iterations\n \tparam max the number of iterations\n", 
						"setDisplayBackground" : "void setDisplayBackground(Color c);\n", 
						"setNodeDisappearBy" : "static void setNodeDisappearBy(float nodeDisBy) \n", 
						"gemLayout" : "void gemLayout() \n GEM Layout\n\nvoid gemLayout(long seed) \n GEM Layout\n \tparam seed the seed for the random number generator, useful\n for getting the same layout every time.\n", 
						"averageColor" : "static String averageColor(String c1, String c2) \n", 
						"makeErdosRenyiRandom" : "void makeErdosRenyiRandom(int nodes, double p) \n", 
						"ls" : "void loadState(Graph g, int state);\n\nvoid loadState(String state);\n", 
						"exportGDF" : "void exportGDF(String filename) \n", 
						"getNodeAppearAfter" : "static float getNodeAppearAfter() \n", 
						"weightedKKLayout" : "void weightedKKLayout() \n the JUNG Kamada-Kawai (regular)\n", 
						"removeEdgeComplete" : "void removeEdgeComplete(Edge e) \n removes an edge completely.  Removes from all states.\n \tparam e the node to remove\n", 
						"kkLayout" : "void kkLayout() \n Kamada-Kawai, puts nodes in 1000 x 1000 square\n\nvoid kkLayout(int width, int height) \n Kamada-Kawai into a width x height grid\n \tparam width\n \tparam height\n", 
						"addNodeField" : "Field addNodeField(String fieldName, int sqlType, Object defaultValue) \n", 
						"exportCGM" : "void exportCGM(String filename);\n", 
						"createConvexHull" : "ConvexHull createConvexHull(PySequence seq, String c) \n", 
						"isRConnected" : "boolean isConnected() \n", 
						"mdsLayout" : "void mdsLayout() \n Multidimensional scaling layout based on edge weight\n", 
						"makeLattice1DRandom" : "void makeLattice1DRandom(int nodes, boolean tor) \n", 
						"removeComplete" : "void removeComplete(PySequence seq) \n remove nodes and edges completely from the system (all states)\n \tparam seq the nodes + edges\n", 
						"setEdgeAppearAfter" : "static void setEdgeAppearAfter(float edgeAppearAfter) \n", 
						"makeFromDL" : "void makeFromDL(String filename) \n", 
						"remove" : "Set remove(PySequence seq) \n remove nodes and edges from the graph\n \tparam seq the nodes + edges\n", 
						"removeEdge" : "Set removeEdge(Edge e) \n remove an edge from the graph\n \tparam e the edge to remove\n", 
						"getNodeSchema" : "NodeSchema getNodeSchema() \n", 
						"getEdgeDisappearBy" : "static float getEdgeDisappearBy() \n", 
						"addRange" : "static void addRange(Object o, String range) \n", 
						"stopMovie" : "void stopMovie() \n basically dumps the movie to disk\n", 
						"addEdge" : "UndirectedEdge addEdge(Node node1, Node node2) \n user's API shortcut for adding a new undirected edge.\n", 
						"getMTF" : "static boolean getMTF() \n Are objects being moved to the front when they change\n", 
						"getSynchronous" : "static boolean getSynchronous() \n thread management\n", 
						"setBackgroundImage" : "void setBackgroundImage(String filename);\n\nvoid setBackgroundImage(String filename, double x, double y);\n", 
						"jkkLayout1" : "void jkkLayout1() \n the JUNG Kamada-Kawai (regular)\n", 
						"setNodeAppearAfter" : "static void setNodeAppearAfter(float nodeAppearAfter) \n", 
						"removeDisconnected" : "Set removeDisconnected() \n", 
						"dagLayout" : "void dagLayout() \n DAG\n", 
						"expandOverlapping" : "void expandOverlapping() \n", 
						"dragwindow" : "static void create() \n", 
						"getEdgeAppearAfter" : "static float getEdgeAppearAfter() \n", 
						"stoplog" : "void stoplog();\n", 
						"setMTF" : "static void setMTF(boolean state) \n should objects in the visualization be moved to the front\n when they change\n", 
						"add" : "void add(PySequence seq) \n add nodes and edges from the graph\n \tparam seq the nodes + edges\n", 
						"drawwindow" : "static void create() \n", 
						"removeNode" : "Set removeNode(Node n) \n remove a node from the graph\n \tparam n the node to remove\n", 
						"hideDisconnected" : "void hideDisconnected() \n", 
						"getDirected" : "boolean getDirected() \n", 
						"springLayout" : "void springLayout() \n spring layout, runs for 30 seconds and asks if you want\n to continue\n\nvoid springLayout(int max) \n spring layout for max iterations\n \tparam max number of iterations\n", 
						"setDirected" : "void setDirected(boolean dir) \n", 
						"jfrLayout" : "void jfrLayout() \n the JUNG Fructerman-Rheingold Layout\n", 
						"setPageRankBias" : "void setPageRankBias(double bias) \n", 
						"setFrozen" : "void setFrozen(boolean state);\n", 
						"makeEppsteinRandom" : "void makeEppsteinRandom(int nodes, int edges, int r) \n", 
						"circleLayout" : "void circleLayout() \n places all nodes in a circle\n\nvoid circleLayout(Field f, Node c) \n places all nodes in a circle with node c in the middle\n and its neighbors around that center with a radius\n proportional to field f (all nodes that are not neighbors\n of c will be left as is)\n \tparam f the field to use to calc the radius\n \tparam c the node to put in the center\n\nvoid circleLayout(Field f, Node c, double xorigin, double yorigin) \n places all nodes in a circle with node c in the middle\n and its neighbors around that center with a radius\n proportional to field f (all nodes that are not neighbors\n of c will be left as is)\n \tparam f the field to use to calc the radius\n \tparam c the node to put in the center\n \tparam xorigin the x location for the center node\n \tparam yorigin the y location for the center node\n", 
						"weakComponentClusters" : "Set weakComponentClusters() \n do weak component clustering\n \treturn a set of sets\n", 
						"rshutdown" : "void shutdown() throws Exception \n", 
						"translateLayout" : "void translateLayout(int newX, int newY) \n moves the nodes to a new origin\n \tparam newX\n \tparam newY\n", 
						"removeBackgroundImage" : "void removeBackgroundImage();\n", 
						"getSubgraph" : "static Subgraph getRootSubgraph(String name) \n", 
						"groupAndSortBy" : "Collection groupAndSortBy(Field field) \n\nCollection groupAndSortBy(PySequence seq, Field field) \n", 
						"searchContained" : "static Collection searchContained(String range) \n", 
						"physicsLayout" : "void physicsLayout() \n physics layout, asks every 30 seconds if you want to keep running\n\nvoid physicsLayout(int max) \n run physics layout for max steps\n \tparam max the number of steps\n", 
						"getGraphMap" : "GraphMap getGraphMap() \n", 
						"sortBy" : "Collection sortBy(PySequence seq, Field field) \n\nCollection sortBy(Field field) \n", 
						"reval" : "Object evalString(String s) \n", 
						"removeConvexHulls" : "void removeConvexHulls(PySequence seq) \n", 
						"randomLayout" : "void randomLayout() \n randomly places nodes in a 1000 x 1000 grid\n\nvoid randomLayout(int width, int height) \n randomly places nodes in a width x height grid\n \tparam width width\n \tparam height height\n", 
						"randomColor" : "static String randomColor() \n\nstatic String randomColor(int alpha) \n", 
						"moveLayout" : "void moveLayout(int newX, int newY) \n moves the nodes to a new origin\n \tparam newX\n \tparam newY\n", 
						"sql" : "synchronized void q(String expression) throws SQLException \n", 
						"morph" : "void morph(String state, long duration) \n", 
						"makeSimpleDirectedRandom" : "void makeSimpleDirectedRandom(int nodes, int edges) \n", 
						"exportJPG" : "void exportJPG(String filename);\n", 
						"radialLayout" : "void radialLayout(Node center) \n places all nodes in a growing radius around center\n \tparam center the node to put in the center\n", 
						"density" : "double density() \n", "overrideArrowLength" : "static void overrideArrowLength(int length) \n", 
						"rescaleLayout" : "void rescaleLayout(int width, int height) \n rescales nodes to fit into width x height\n \tparam width the rescale width\n \tparam height the rescale height\n\nvoid rescaleLayout(double percent) \n rescales the layout to %percent\n \tparam percent the percent to shrink/grow\n", 
						"setSynchronous" : "static void setSynchronous(boolean state) \n should layouts run in their own threads?\n", 
						"weightedSpringLayout" : "void weightedSpringLayout(int min, int max) \n spring layout, runs for 30 seconds and asks if you want\n to continue\n\nvoid weightedSpringLayout(int minL, int maxL, int max) \n spring layout for max iterations\n \tparam max number of iterations\n", 
						"resizeLinear" : "void resizeLinear(Field f, double start, double end) \n", 
						"exportGIF" : "void exportGIF(String filename);\n", 
						"binPackLayout" : "void binPackLayout() \n packs all nodes\n\nvoid binPackLayout(boolean rescale) \n packs all nodes\n", 
						"getJythonConsole" : "static TextPaneJythonConsole getJythonConsole() \n gets a reference to the existing GUI console object, or\n null if it doesn't exist\n", 
						"colorize" : "void colorize(Field f) \n\nvoid colorize(Field f, Color start, Color end) \n\nvoid colorize(Field f, Color start, Color middle, Color end) \n", 
						"frLayout" : "void frLayout() \n Fruchterman-Rheingold, puts nodes in 1000 x 1000 square\n\nvoid frLayout(int width, int height) \n Fruchterman-Rheingold into a width x height grid\n \tparam width\n \tparam height\n", "setEdgeDisappearBy" : "static void setEdgeDisappearBy(float edgeDisBy) \n", 
						"infowindow" : "static void create() \n\nstatic void create() \n", 
						"getDisplayBackground" : "Color getDisplayBackground();\n", 
						"edgeBetweennessClusters" : "Set edgeBetweennessClusters(int numEdgesToRemove) \n do Edge betweenness clustering\n \tparam numEdgesToRemove edges to remove\n \treturn a set of sets\n", 
						"exportPNG" : "void exportPNG(String filename);\n", 
						"exportSVG" : "void exportSVG(String filename);\n", 
						"ss" : "void saveState(int state);\n", 
						"overrideArrowWidth" : "static void overrideArrowWidth(int width) \n", 
						"addEdgeField" : "Field addEdgeField(String fieldName, int sqlType, Object defaultValue) \n", 
						"getPageRankBias" : "double getPageRankBias() \n", 
						"resizeRandom" : "void resizeRandom(Field f, double start, double end) \n", 
						"sugiyamaLayout2" : "void sugiyamaLayout2() \n Sugiyama\n", 
						"removeConvexHull" : "void removeConvexHull(ConvexHull c) \n", 
						"exportJAVA" : "void exportJAVA(String filename);\n", 
						"getConvexHulls" : "Collection getConvexHulls() \n", 
						"getEdgeSchema" : "EdgeSchema getEdgeSchema() \n", 
						"startMovie" : "void startMovie(int fps, String filename) \n\nvoid startMovie(int fps, String filename, boolean auto) \n", 
						"exportEPS" : "void exportEPS(String filename);\n", 
						"makeKleinbergRandom" : "void makeKleinbergRandom(int nodes, double clust) \n", 
						"makeBarabasiAlbertRandom" : "void makeBarabasiAlbertRandom(int vert, int edges, int evolve) \n", 
						"isomLayout" : "void isomLayout() \n ISOM Layout, asks every 30 seconds if you want to continue\n\nvoid isomLayout(int max) \n ISOM Layout for max steps\n \tparam max the number of iterations\n", 
						"exportEMF" : "void exportEMF(String filename);\n", 
						"makeFromPajek" : "void makeFromPajek(String filename) \n", 
						"center" : "void center();\n\nvoid center(Object o);\n", 
						"setRankerWeightField" : "void setRankerWeightField(Field f) \n", 
						"log" : "void log(String filename);\n", 
						"searchContains" : "static Collection searchContains(String range) \n", 
						"makeLattice2DRandom" : "void makeLattice2DRandom(int nodes, boolean tor) \n", 
						"readjustEdges" : "void readjustEdges() \n moves edges so they don't overlap\n", 
						"jkkLayout2" : "void jkkLayout2() \n the JUNG Kamada-Kawai (int)\n", 
						"complement" : "Set complement(PySequence seq) \n returns the complement (e.g. all nodes/edges in the\n graph that are not in the set).  If the set contains only nodes\n only nodes will be returned.  Likewise for edges.  If the set contains\n both, both nodes and edges will be returned.\n \tparam seq the nodes and/or edges\n", 
						"removeSelfLoops" : "Set removeSelfLoops() \n", 
						"biComponentClusters" : "Set biComponentClusters() \n do Bi-Component Clustering\n \treturn a set of sets\n", 
						"getNodeDisappearBy" : "static float getNodeDisappearBy() \n", 
						"searchExact" : "static Collection searchExact(String range) \n", 
						"makeSimpleRandom" : "void makeSimpleRandom(int nodes, int edges) \n", 
						"getStates" : "Set getStates();\n"}


def addNode(_name,**_extra):
	_temp = g.addNode(_name)
	if not isinstance(_name,Class.forName("com.hp.hpl.guess.Node")):
		_temp.label = _name
	for _toset in _extra.keys():
		_temp.__setattr__(_toset,_extra[_toset])
	return _temp

def copyNode(_template,_name,**_extra):
	if not isinstance(_template,Class.forName("com.hp.hpl.guess.Node")):
		raise TypeError,'First argument nust be a template Node'
	if not isinstance(_name,Class.forName("org.python.core.PyString")):
		raise TypeError,'Second argument must be a name'
	_temp = g.addNode(_name)
	for _field in Node.fieldNames():
		if (_field != "name"):
			_temp.__setattr__(_field,_template.__getattr__(_field))
	for _toset in _extra.keys():
		_temp.__setattr__(_toset,_extra[_toset])
	return(_temp)

def addEdge(*d,**_extra):
	return addUndirectedEdge(*d,**_extra)

def addDirectedEdge(*d,**_extra):
	_temp = None
	if d.__len__() == 1:
		_temp = g.addEdge(d[0])
	elif d.__len__() == 2:
		return g.addDirectedEdge(d[0],d[1])
	for _toset in _extra.keys():
		_temp.__setattr__(_toset,_extra[_toset])
	if not _extra.has_key("label"):
		_temp.label = str(_temp.weight)
	return(_temp)

def addUndirectedEdge(*d,**_extra):
	_temp = None
	if d.__len__() == 1:
		_temp = g.addEdge(d[0])
	elif d.__len__() == 2:
		return g.addUndirectedEdge(d[0],d[1])
	for _toset in _extra.keys():
		_temp.__setattr__(_toset,_extra[_toset])
	if not _extra.has_key("label"):
		_temp.label = str(_temp.weight)
	return(_temp)

# Some demo functions that manipulate the graph in various ways

from java.lang import Math

# iteratively removes all nodes that have
# a total degree of 1
# assumes self loops have been removed
def removeLeaves():
	g.nodes[0].totaldegree
	_m = (totaldegree <= 1)
	_toRet = []
	while _m.__len__() > 0:
		_toRet += remove(_m)
		_m = (totaldegree <= 1)
	return _toRet



# iteratively removes nodes that have an outdegree
# of introduces a new edge
# assumes self loops have been removed
def shortcutNodes():
	g.nodes[0].totaldegree
	_m = (totaldegree == 2)
	_toRet = []
	_added = []
	while len(_m) > 0:	
		for _z in _m:
			_pot = _z.getNeighbors()
			if len(_pot) == 2:
				if len(_pot[0]-_pot[1]) == 0:
					_added += [addEdge(_pot[0],_pot[1])]
				_toRet += removeNode(_z)
			_m = (totaldegree == 2)
	_temp = intersectionHelper(_toRet,_added)
	return [_temp[0],_temp[1]]

# iteratively call removeLeaves and shortcutNodes to 
# condense the graph
def condenseGraph():
	_toRet = []
	_added = []
	_toRet += removeDisconnected()
	_toRet += removeSelfLoops()
	g.nodes[0].totaldegree	
	_t1 = (totaldegree <= 1)
	_t2 = (totaldegree == 2)
	while (_t1.__len__() > 0) | (_t2.__len__() > 0):
		_toRet += removeLeaves()
		_scr = shortcutNodes()
		_toRet += _scr[0]
		_added += _scr[1]
		_t1 = (totaldegree <= 1)
		_t2 = (totaldegree == 2)
	_temp = intersectionHelper(_toRet,_added)
	return [_temp[0],_temp[1]]

def intersectionHelper(_l1,_l2):
	_templist = _l1 & _l2
	_ln1 = []
	_ln2 = []
	for _elem in _l1:
		if _elem not in _templist:
			_ln1 += [_elem]
	for _elem in _l2:
		if _elem not in _templist:
			_ln2 += [_elem]
	return [_ln1,_ln2]

# does a "skitter" plot which lays out nodes around a circle based on the
# ordering given an input field (lexical for text, numerical for numbers).
# The radius is defined by the totaldegree of the node
def skitter(_field):
	_maxangle = 2 * Math.PI
	_ordering = sortBy(_field)
	_increment = _maxangle / len(_ordering)
	_curangle = 0
	g.nodes[0].outdegree
	_maxdeg = outdegree.max + 1.0
	for _n in _ordering:
		_radius = 1 - Math.log((_n.outdegree + 1.0) / _maxdeg)
		_radius = _radius * 500.0
		_x = 500.0 + _radius * Math.cos(_curangle)
		_y = 500.0 + _radius * Math.sin(_curangle)
		_n.setX(_x)
		_n.setY(_y) 
		_curangle += _increment

# colors a given edge the "average" of it's two endpoints
def averageEdgeColor(_edge):
	_n1 = _edge.getNode1()
	_n2 = _edge.getNode2()
	_edge.color = averageColor(_n1.color,_n2.color)

from com.hp.hpl.guess.jfreechart import GChartFrame
from com.hp.hpl.guess.jfreechart import GHistoChartFrame
from com.hp.hpl.guess.jfreechart import GPieChartFrame

# plots the distribution of values for a given field in a chart 
def plotDistrib(_field,_chart = None, _sortBy = None, legend = 1, visible = 1):
	if _sortBy == None:
		_sortBy = _field
	_l = sortBy(_sortBy)
	_names = []
	_values = []
	for _z in _l:
		_names += [_z]
		_values += [_z.__getattr__(_field.getName())]
	_xlabel = ""
	if (_field.getType() == 1):
		_xlabel = "Rank Ordered Nodes"
	else:
		_xlabel = "Rank Ordered Edges"
	if _chart == None:
		_chart = GChartFrame(_field.getName(),_names,_values,_xlabel,visible,legend)
	else:
		_chart.addToChart(_field.getName(),_names,_values,_xlabel)
	return _chart

def sizecmpd(_a,_b):
	return len(_b) - len(_a)

def sizecmpi(_a,_b):
	return len(_b) - len(_a)

def plotSizes(_field,legend=0,visible=1):
	_groups = groupBy(_field)
	_groups.sort(sizecmpd)
	_values = []
	_names = []
	_colors = []
	for _g in _groups:
		_values += [len(_g)]
		_name = _g[0].__getattr__(_field.getName())
		if _g == None:
			_name = "X"
		_names += [_name]
		_colors += [_g[0].color]
	_chart = GHistoChartFrame(_field.getName(),_groups,_values,_names,_colors,visible,legend)
	return _chart

def plotSizesPie(_field,legend=0,visible=1):
	_groups = groupBy(_field)
	_groups.sort(sizecmpd)
	_values = []
	_names = []
	_colors = []
	for _g in _groups:
		_values += [len(_g)]
		_name = _g[0].__getattr__(_field.getName())
		if _g == None:
			_name = "X"
		_names += [_name]
		_colors += [_g[0].color]
	_chart = GPieChartFrame(_field.getName(),_groups,_values,_names,_colors,visible,legend)
	return _chart

def plotHistogram(_field,bins=10,legend=0,visible=1):
	_sorted = sortBy(_field)
	_min = float(_field.min)
	_max = float(_field.max)
	_binsize = (_max - _min)/float(bins)
	_binend = _min + _binsize 
	_curgroup = [] 
	_values = []
	_names = []
	_colors = []
	_groups = []
	for _e in _sorted:
		_val = _e.__getattr__(_field.getName())
		if _val > _binend:
			_groups += [_curgroup]
			_values += [len(_curgroup)]
			_names += ["<="+str(_binend)];
			_colors += [blue]
			_binend = _binend + _binsize
			_curgroup = []
		_curgroup += [_e]
	_groups += [_curgroup]
	_values += [len(_curgroup)]
	_names += ["<="+str(_max)];
	_colors += [blue]
	_chart = GHistoChartFrame(_field.getName(),_groups,_values,_names,_colors,visible,legend)
	return _chart


# calculates the distance between two nodes
def distance(_node1,_node2):
	return Math.sqrt(((_node1.x - _node2.x) * (_node1.x - _node2.x)) + ((_node1.y - _node2.y) * (_node1.y - _node2.y)))

def ninverse(_set):
	_toret = []
	for _t in g.nodes:
		if _t not in _set:
			_toret += [_t]
	return _toret 

def einverse(_set):
	_toret = []
	for _t in g.edges:
		if _t not in _set:
			_toret += [_t]
	return _toret 

import math

def floatRange(a, b, inc):
	try: x = [float(a)]
	except: return False
	for i in range(1, int(math.ceil((b - a ) / inc))):
		x. append(a + i * inc)
	return x

