group horizontal_bar_graph;


header(boundingBoxLeft, boundingBoxBottom, boundingBoxRight, boundingBoxTop, sourceDataName, pageWidth, pageHeight) ::= <<
%!PS-Adobe-2.0 EPSF-2.0
%%BoundingBox: <boundingBoxLeft> <boundingBoxBottom> <boundingBoxRight> <boundingBoxTop>
%%Title: Horizontal Bar Graph - <sourceDataName>
%%Creator: SciPolicy
%%Scaled to fit a(n) <pageWidth>"x<pageHeight>" page.
%%EndComments

>>


orientation(rotation) ::= <<

<rotation> rotate

>>


otherTransformations(centerX, centerY, scale) ::= <<

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
	0 setgray
	% 0.4 0.4 0.4 setrgbcolor
	moveto
	dup stringwidth pop neg 15 sub 0 rmoveto
	fontHeightAdjust
	show
} def

/period {
	% Starting stack: lineHeight lineLength lineStartingY lineStartingX
	gsave
		0.0 0.0 0.0 setrgbcolor

		% (lineHeight) setlinewidth
		setlinewidth
		% Post setlienwidth stack: lineLength lineStartingY lineStartingX

		3 1 roll
		% Post roll stack: lineStartingY lineStartingX lineLength

		moveto
		% Post moveto stack: lineLength

		% lineLength 0 rlineto
		0 rlineto

		stroke
	grestore
} def

/infinitePeriod {
	% Starting stack: lineHeight lineLength lineStartingY lineStartingX
	gsave
		0.0 0.0 0.0 setrgbcolor
		[15] 0 setdash

		% (lineHeight) setlinewidth
		setlinewidth
		% Post setlienwidth stack: lineLength lineStartingY lineStartingX

		3 1 roll
		% Post roll stack: lineStartingY lineStartingX lineLength

		moveto
		% Post moveto stack: lineLength

		% lineLength 0 rlineto
		0 rlineto

		stroke
	grestore
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
	fill
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

% \<string\> underlinedShow
% Pops a string
% Shows the string underlined
/underlinedShowDict 1 dict def
/underlinedShow {
	underlinedShowDict begin
		/UNDERLINE_THICKNESS 0.1 def
		/UNDERLINE_VERTICAL_OFFSET -0.8 def

		gsave
			0 UNDERLINE_VERTICAL_OFFSET rmoveto
			dup stringwidth rlineto
			UNDERLINE_THICKNESS setlinewidth stroke
		grestore
		
		show
	end
} bind def

/inch {
	72 mul
} def

>>


showHeaderFooter(
	analysisTime, inputData,
	datasetName,
	label, startDate, endDate, sizeBy, dateFormat, yearLabelFontSize, barLabelFontSize,
	x,
	dateTimeY, inputDataY,
	datasetNameY,
	labelY, startDateY, endDateY, sizeByY, dateFormatY, yearLabelFontSizeY, barLabelFontSizeY,
	footerY) ::= <<

% Actual Header
gsave
	/Garamond findfont 8 scalefont setfont
	.3 setgray

	% Analysis Time
	%<x> inch <dateTimeY> inch moveto
	<x> <dateTimeY> moveto
	(Date and time of analysis: ) show (<analysisTime>) show

	% Input Data
	%<x> inch <inputDataY> inch moveto
	<x> <inputDataY> moveto
	(Input data: ) show (<inputData>) show

	%<x> inch <footerY> inch moveto
	<x> <footerY> moveto
	(Cyberinfrastructure for Network Science Center (2009) Horizontal Bar Graph. Indiana University, ) show
	(http://sci.slis.indiana.edu) underlinedShow
	(.) show
grestore

% Title?
gsave
	%<x> inch <datasetNameY> inch moveto
	<x> <datasetNameY> moveto
	0 setgray
	/Garamond findfont 14 scalefont setfont
	(Horizontal Bar Graph for ) show (<datasetName>) show
grestore

gsave
	0 setgray
	/Garamond findfont 10 scalefont setfont

	% Label Column
	%<x> inch <labelY> inch moveto
	<x> <labelY> moveto
	(Label Column: ) show (<label>) show

	% Start Date Column
	%<x> inch <startDateY> inch moveto
	<x> <startDateY> moveto
	(Start Date Column: ) show (<startDate>) show

	% End Date Column
	%<x> inch <endDateY> inch moveto
	<x> <endDateY> moveto
	(End Date Column: ) show (<endDate>) show

	% End Date Column
	%<x> inch <sizeByY> inch moveto
	<x> <sizeByY> moveto
	(Size By Column: ) show (<sizeBy>) show

	% Date Format Column
	%<x> inch <dateFormatY> inch moveto
	<x> <dateFormatY> moveto
	(Date Format Column: ) show (<dateFormat>) show

	% Year Label Font Size
	%<x> inch <yearLabelFontSizeY> inch moveto
	<x> <yearLabelFontSizeY> moveto
	(Year Label Font Size: ) show (<yearLabelFontSize>) show

	% Bar Label Font Size
	%<x> inch <barLabelFontSizeY> inch moveto
	<x> <barLabelFontSizeY> moveto
	(Bar Label Font Size: ) show (<barLabelFontSize>) show
grestore

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