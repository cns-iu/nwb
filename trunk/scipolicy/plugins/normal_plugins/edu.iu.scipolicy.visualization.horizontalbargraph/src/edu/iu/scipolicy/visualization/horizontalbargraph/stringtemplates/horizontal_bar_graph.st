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
	0.8 0.8 0.8 setrgbcolor
	newpath
		4 2 roll moveto
		exch dup 0 rlineto 0 3 -1 roll
		rlineto neg 0 rlineto
	closepath
	fill
} def

/infinitePeriod {
	0.0 0.0 0.0 setrgbcolor
	newpath
		4 2 roll moveto
		exch dup 0 rlineto 0 3 -1 roll
		rlineto neg 0 rlineto
	closepath
	fill
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
	<x> inch <dateTimeY> inch moveto
	(Date and time of analysis: ) show (<analysisTime>) show

	% Input Data
	<x> inch <inputDataY> inch moveto
	(Input data: ) show (<inputData>) show

	<x> inch <footerY> inch moveto
	(Cyberinfrastructure for Network Science Center (2009) Horizontal Bar Graph. Indiana University, ) show
	(http://sci.slis.indiana.edu) underlinedShow
	(.) show
grestore

% Title?
gsave
	<x> inch <datasetNameY> inch moveto
	0 setgray
	/Garamond findfont 14 scalefont setfont
	(Horizontal Bar Graph for ) show (<datasetName>) show
grestore

gsave
	0 setgray
	/Garamond findfont 10 scalefont setfont

	% Label Column
	<x> inch <labelY> inch moveto
	(Label Column: ) show (<label>) show

	% Start Date Column
	<x> inch <startDateY> inch moveto
	(Start Date Column: ) show (<startDate>) show

	% End Date Column
	<x> inch <endDateY> inch moveto
	(End Date Column: ) show (<endDate>) show

	% End Date Column
	<x> inch <sizeByY> inch moveto
	(Size By Column: ) show (<sizeBy>) show

	% Date Format Column
	<x> inch <dateFormatY> inch moveto
	(Date Format Column: ) show (<dateFormat>) show

	% Year Label Font Size
	<x> inch <yearLabelFontSizeY> inch moveto
	(Year Label Font Size: ) show (<yearLabelFontSize>) show

	% Bar Label Font Size
	<x> inch <barLabelFontSizeY> inch moveto
	(Bar Label Font Size: ) show (<barLabelFontSize>) show

	%<x> inch 9.62 inch moveto
	%(These 83 records are associated with 10 of 13 disciplines of science and 57 of 554 research specialties in the UCSD Map of Science.) show
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