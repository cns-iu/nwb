from com.hp.hpl.guess import Field
from math import sqrt


def interpolate(value, minvalue, maxvalue, minfinal, maxfinal):
	return minfinal + 1.0 * (maxfinal - minfinal) * (value - minvalue) / (maxvalue - minvalue)



def interpolateColor(value, minvalue, maxvalue, first, second):
	result = []
	for (a, b) in zip(first, second):
		result.append(interpolate(value, minvalue, maxvalue, a, b))
	return tuple(result)

def noop(value):
	return value

def colorizer(color):
	return "%s,255" % ','.join(map(str,map(int,color)))

def groupColorize(items, column, first, second):
	min = Double.MAX_VALUE
	max = Double.MIN_VALUE
	for item in items:
		value = item.__getattr__(column.name)
		if value < min:
			min = value
		if value > max:
			max = value
	for item in items:
		value = item.__getattr__(column.name)
		item.__setattr__('color', colorizer(interpolateColor(value, min, max, first, second)))

def groupResizeLinear(items, column, minfinal, maxfinal):
	attribute = None
	finalize = noop
	if column.getType() == Field.NODE:
		minfinal = minfinal ** 2
		maxfinal = maxfinal ** 2
		finalize = sqrt
		attribute = 'size'
	elif column.getType() == Field.EDGE:
		attribute = 'width'
	min = Double.MAX_VALUE
	max = Double.MIN_VALUE
	for item in items:
		value = item.__getattr__(column.name)
		if value < min:
			min = value
		if value > max:
			max = value
	for item in items:
		value = item.__getattr__(column.name)
		item.__setattr__(attribute, finalize(interpolate(value, min, max, minfinal, maxfinal)))


def fixedColorize(column, first, second):
	first = formatcolor(first)
	second = formatcolor(second)
	if column.getType() == Field.NODE:
		groupColorize(g.nodes, column, first, second)
	elif column.getType() == Field.EDGE:
		groupColorize(g.edges, column, first, second)

def fixedResizeLinear(column, minfinal, maxfinal):
	if column.getType() == Field.NODE:
		groupResizeLinear(g.nodes, column, minfinal, maxfinal)
	elif column.getType() == Field.EDGE:
		groupResizeLinear(g.edges, column, minfinal, maxfinal)
	

def scaler(factor):
	for node in g.nodes:
		node.x, node.y = node.x * factor, node.y * factor
	center()

def formatcolor(arg):
	# if arg is a string, convert it to an r,g,b tuple
	if isinstance(arg, type("")):
		# if arg is a list of color values...
		if arg.find(",") != -1:
			# split it, and make each element an int
			arg = map(int, arg.split(","))
		else: #arg is the name of a color...
			#get the Color object corresponding to the name
			arg = Colors.getColor(arg, Color.red)
			# and make it into a r,g,b tuple
			arg = [arg.red, arg.green, arg.blue]
		
	# trim down to 3 elements (removing transparency if it is defined))
	if len(arg) > 3:
		arg = arg[:3]

	return arg

		
resizeLinear = fixedResizeLinear
colorize = fixedColorize
