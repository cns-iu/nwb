group visualization;

visualizationAreaDefinitions(topVizPosition) ::= <<

%%%%%%%%%%%
% Viz Area 
%%%%%%%%%%%
/topVizPosition { <topVizPosition> } def
/leftVizMargin { visualizationLeft } def

/bar { % x1 y1 deltaX deltaY => -
	newpath
	4 2 roll moveto
	exch dup 0 rlineto 0 3 -1 roll
	rlineto neg 0 rlineto
	fill
} def
/labelbar { % (label) x1 y1 deltaX deltaY color => -
	gsave
	visualizationLeft visualizationBottom translate
	0 0 moveto
	setrgbcolor
	5 -1 roll % stack: x1 y1 deltaX deltaY label
	4 index 4 index 3 index 2 div add % stack: x1 y1 deltaX deltaY label x1 (y1 + deltaY/2)
	labelbarLabel % draw the label at x1 (y1 + deltaY/2)
	bar
	grestore
} def
/labelbarSpacing { visualizationLabelBarFontSize } def
/labelbarLabel { % (label) [x] [y] => -
	gsave
	moveto
	leftVizMargin labelbarSpacing sub exch righttrim
	dup stringwidth pop neg labelbarSpacing sub % (label) x
	exch dup textheight 2 div neg % x (label) y
	3 -1 roll exch % (label) x y
	rmoveto
	show
	grestore
} def

/datelineLineWidth { 1 } def
/datelineDashSize { 15 } def
/dateline { % (label) x => -
	gsave
	visualizationLeft visualizationBottom translate
	dup
	0 moveto
	0.75 0.75 0.75 setrgbcolor
	[datelineDashSize] 0 setdash
	datelineLineWidth setlinewidth
	0 topVizPosition rlineto
	stroke
	0 0 0 setrgbcolor
	datelinelabel
	grestore
} def

/datelineLabelFont { /Arial } def
/datelineLabelFontSize { 12.0 } def
/datelinelabel { % (label) x => -
	datelineLabelFont datelineLabelFontSize selectfont
	datelineLabelFontSize neg center
	show
} def

>>

visualizationAreaSetup() ::= <<

gsave


>>

visualizationAreaTearDown() ::= <<

grestore

>>

visualizationLabelBarFont(fontname, fontsize) ::= <<

/visualizationLabelBarFontSize { <fontsize> } def
/<fontname> visualizationLabelBarFontSize selectfont

>>

visualizationLabelBar(label, x1, y1, deltaX, deltaY, color) ::= <<

(<label>) <x1> <y1> <deltaX> <deltaY> <color> labelbar

>>

visualizationDateLine(label, x1) ::= <<

(<label>) <x1> dateline

>>