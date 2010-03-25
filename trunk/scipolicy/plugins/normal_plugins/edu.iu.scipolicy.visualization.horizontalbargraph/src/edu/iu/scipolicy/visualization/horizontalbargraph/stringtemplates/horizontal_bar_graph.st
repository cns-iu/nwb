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

<rotation> rotate
<centerX> <centerY> translate
<scale> <scale> scale

>>


functions() ::= <<

/fontHeightAdjust {
	0 -3 rmoveto
} def

/tickLabel {
	gsave
	moveto
	dup stringwidth pop 2 div neg 0 rmoveto
	fontHeightAdjust
	show
	grestore
} def

/recordLabel {
	0.4 0.4 0.4 setrgbcolor
	moveto
	dup stringwidth pop neg 15 sub 0 rmoveto
	fontHeightAdjust
	show
} def

/period {
	0.0 0.0 0.0 setrgbcolor
	newpath
		4 2 roll moveto
		exch dup 0 rlineto 0 3 -1 roll
		rlineto neg 0 rlineto
	closepath
	fill
} def

/infinitePeriod {
	1.0 0.0 0.0 setrgbcolor
	newpath
		4 2 roll moveto
		exch dup 0 rlineto 0 3 -1 roll
		rlineto neg 0 rlineto
	closepath
	stroke
} def

/arrow {
	newpath
		5 index 5 index moveto
		3 index 3 index lineto
		1 index 1 index lineto
	closepath
	fill
} def

/infiniteArrow {
	newpath
		5 index 5 index moveto
		3 index 3 index lineto
		1 index 1 index lineto
	closepath
	stroke
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


undoYearLabelProperties(fontFamily, inverseFontSize) ::= <<

/<fontFamily> findfont <inverseFontSize> scalefont setfont

>>


yearLabelWithVerticalTick(year, x, tickSize, height) ::= <<

0 setgray
(<year>) <x> <tickSize> tickLabel
<x> <tickSize> <height> vertical

>>


barProperties(fontFamily, fontSize) ::= <<

/<fontFamily> findfont <fontSize> scalefont setfont

>>


bar(label, textX, textY, barX, barY, barWidth, barHeight) ::= <<

(<label>) <textX> <textY> recordLabel
<barX> <barY> <barWidth> <barHeight> period

>>


infiniteBar(label, textX, textY, barX, barY, barWidth, barHeight) ::= <<

(<label>) <textX> <textY> recordLabel
<barX> <barY> <barWidth> <barHeight> infinitePeriod

>>


arrow(startX, startY, middleX, middleY, endX, endY) ::= <<

<startX> <startY> <middleX> <middleY> <endX> <endY> arrow

>>


infiniteArrow(startX, startY, middleX, middleY, endX, endY) ::= <<

<startX> <startY> <middleX> <middleY> <endX> <endY> infiniteArrow

>>


setRGBColor(red, green, blue) ::= <<

<red> <green> <blue> setrgbcolor

>>