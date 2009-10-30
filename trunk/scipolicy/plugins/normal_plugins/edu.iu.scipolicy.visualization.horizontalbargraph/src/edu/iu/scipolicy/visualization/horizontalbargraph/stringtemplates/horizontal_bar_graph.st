group horizontal_bar_graph;


header(boundingBoxLeft, boundingBoxBottom, boundingBoxRight, boundingBoxTop, sourceDataName, pageWidth, pageHeight) ::= <<

%!PS-Adobe-2.0 EPSF-2.0
%%BoundingBox: <boundingBoxLeft> <boundingBoxBottom> <boundingBoxRight> <boundingBoxTop>
%%Title: Horizontal Bar Graph - <sourceDataName>
%%Creator: SciPolicy
%%Scaled to fit a(n) <pageWidth>"x<pageHeight>" page.
%%EndComments

>>


transformations(centerX, centerY, scale, rotation) ::= <<

<scale> <scale> scale
<centerX> <centerY> translate
<rotation> rotate

>>


functions() ::= <<

/fontHeightAdjust {
	0 -3 rmoveto
} def

/ticklabel {
	gsave
	moveto
	dup stringwidth pop 2 div neg 0 rmoveto
	fontHeightAdjust
	show
	grestore
} def

/recordLabel {
	moveto
	dup stringwidth pop neg 15 sub 0 rmoveto
	fontHeightAdjust
	show
} def

/period {
	newpath
	4 2 roll moveto
	exch dup 0 rlineto 0 3 -1 roll
	rlineto neg 0 rlineto
	fill
} def

/record {
	5 -1 roll
	4 index 4 index 3 index 2 div add
	0.4 0.4 0.4 setrgbcolor
	recordLabel
	0.0 0.0 0.0 setrgbcolor
	period
} def

/vertical {
	gsave
	[15] 0 setdash
	1 setlinewidth
	0.75 0.75 0.75 setrgbcolor
	2 index
	newpath
	exch
	moveto
	lineto
	stroke
	grestore
} def

>>


yearLabelProperties(red, green, blue, lineWidth, fontFamily, fontSize) ::= <<

<red> <green> <blue> setrgbcolor
<lineWidth> setlinewidth
/<fontFamily> findfont <fontSize> scalefont setfont

>>


yearLabelWithVerticalTick(year, x, tickSize, height) ::= <<

0 setgray
(<year>) <x> <tickSize> ticklabel
<x> <tickSize> <height> vertical

>>


record(label, x, y, width, height, leftArrow, rightArrow) ::= <<

(<label>) <x> <y> <width> <height> record

>>


setRGBColor(red, green, blue) ::= <<

<red> <green> <blue> setrgbcolor

>>


show() ::= <<

show

>>