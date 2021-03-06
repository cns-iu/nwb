group visualization;

visualizationAreaDefinitions(topVizPosition, pointsPerDay, pointsPerY) ::= <<

%%%%%%%%%%%
% Viz Area 
%%%%%%%%%%%
/pointsPerDay { <pointsPerDay> } def % unused here but useful for legends
/pointsPerY { <pointsPerY> } def % unused here but useful for legends
/topVizPosition { <topVizPosition> } def
/leftTextMargin { visualizationLeft } def

/bar { % x1 y1 deltaX deltaY => -
	dup 0.0 eq 
	{
	pop % x1 y1 deltaX
	3 1 roll % deltaX x1 y1
	moveto % deltaX
	[2] 0 setdash
	0 setlinewidth
	0 rlineto
	stroke
	}	
	{
	newpath
	4 2 roll moveto
	exch dup 0 rlineto 0 3 -1 roll
	rlineto neg 0 rlineto
	fill
	} ifelse
} def
/labelbar { % (label) x1 y1 deltaX deltaY color => -
	gsave
	visualizationLeft visualizationBottom translate
	leftTextMargin 0 translate
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
	0 0 0 setrgbcolor
	exch  % [y] [x]
	dup  % [y] [x] [x]
	3 -1 roll % [x] [x] [y]
	moveto
	leftTextMargin labelbarSpacing sub add exch righttrim
	dup stringwidth pop neg labelbarSpacing sub % (label) x
	exch dup textheight 2 div neg % x (label) y
	3 -1 roll exch % (label) x y
	rmoveto
	show
	grestore
} def

/datelineLineWidth { 0 } def
/datelineDashSize { 5 } def
/dateline { % (label) x => -
	gsave
	visualizationLeft visualizationBottom translate
	leftTextMargin 0 translate
	dup
	0 moveto
	0.3 0.3 0.3 setrgbcolor
	[datelineDashSize] 0 setdash
	datelineLineWidth setlinewidth
	0 topVizPosition rlineto
	stroke
	0 0 0 setrgbcolor
	datelinelabel
	grestore
} def

/datelineLabelFont { /ArialMT } def
/datelineLabelFontSize { 10.0 } def
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