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
