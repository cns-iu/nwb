from java.awt.geom import GeneralPath
from java.awt import Polygon
import jarray

xpoints = jarray.array((10,5,0,5),'i')
ypoints = jarray.array((5,10,5,0),'i')
diamond = Polygon(xpoints,ypoints,4);
shapeDB.addShape(104,diamond)

xpoints = jarray.array((55, 67, 109, 73, 83, 55, 27, 37, 1, 43),'i')
ypoints = jarray.array((0, 36, 36, 54, 96, 72, 96, 54, 36, 36),'i')
star = Polygon(xpoints,ypoints,10)
shapeDB.addShape(105,star)
 
triangle = GeneralPath()
triangle.moveTo(5,0)
triangle.lineTo(10,5)
triangle.lineTo(0,5)
triangle.lineTo(5,0)
shapeDB.addShape(106,triangle)

