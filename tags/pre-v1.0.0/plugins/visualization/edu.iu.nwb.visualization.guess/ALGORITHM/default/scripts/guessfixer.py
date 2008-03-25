from com.hp.hpl.guess import Field
from math import sqrt


def interpolate(value, minvalue, maxvalue, minfinal, maxfinal):
	return minfinal + 1.0 * (maxfinal - minfinal) * (value - minvalue) / (maxvalue - minvalue)

def noop(value):
	return value

def fixedResizeLinear(column, minfinal, maxfinal):
	items = None
	attribute = None
	finalize = noop
	if column.getType() == Field.NODE:
		minfinal = minfinal ** 2
		maxfinal = maxfinal ** 2
		finalize = sqrt
		items = g.nodes
		attribute = 'size'
	elif column.getType() == Field.EDGE:
		items = g.edges
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
		
resizeLinear = fixedResizeLinear