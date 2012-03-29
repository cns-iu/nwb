import java

class newhighlight(java.lang.Object):

	# so we can "unhighlight" nodes
	_toFix = {}
	
	def __init__(self):
		# add the listeners
		graphevents.mouseEnterNode = self.mouseEnter
		graphevents.mouseLeaveNode = self.mouseLeave
		graphevents.clickNode = self.mouseClick
		
		# remove default behaviors
		vf.defaultNodeHighlights(false)
		vf.defaultNodeZooming(false)

	def mouseEnter(self,_node):
		# when we enter the node we should
		# track all current colors, make the node
		# yellow, the edges orange, and the neighbors red
		self._toFix[_node] = _node.color
		StatusBar.setStatus(str(_node))
		_node.color = yellow
		for _e in _node.getOutEdges():
			self._toFix[_e] = _e.color
			_e.color = orange
		for _n in _node.getNeighbors():
			if (_n != _node):
				self._toFix[_n] = _n.color
				_n.color = red

	def mouseLeave(self,_node):
		# put back all the original colors
		for _elem in self._toFix.keys():
			_elem.color = self._toFix[_elem]
		self._toFix.clear();

	def mouseClick(self,_node):
		# zoom to the node AND its neighbors
		_toCenter = [_node]
		_toCenter += _node.getNeighbors()
		center(_toCenter)

newhighlight()