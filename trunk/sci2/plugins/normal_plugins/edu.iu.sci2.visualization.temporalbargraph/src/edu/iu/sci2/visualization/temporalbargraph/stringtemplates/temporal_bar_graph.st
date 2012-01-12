group temporal_bar_graph;

header(numberOfPages, date) ::= <<
	%!PS-Adobe-3.0 EPSF-3.0
	%%Pages: <numberOfPages>
	%%Title: (Temporal Bar Graph)
	%%Creator: (Sci2)
	%%CreationDate: (<date>)
	%%BeginFeature: *PageSize Letter
		letter
	%%EndFeature
	%%EndComments
>>

procedureDefinitions() ::= <<
	%%BeginProlog
	%%EndProlog
>>

documentSetup(pageWidth, pageHeight) ::= <<
	%%BeginSetup
	%%BeginFeature: *PageSize Letter
		\<\< /PageSize [pageWidth pageHeight] \>\> setpagedevice
	%%EndFeature
	%%EndSetup
>>

documentTrailer() ::= <<
%%Trailer
>>

visualizationAreaHeader(pageWidth, pageHeight, labelBarSpacing, datelineLineWidth, datelineDashSize, datelineLabelFontSize) ::= <<
%%%%%%%%%%%
% Viz Area 
%%%%%%%%%%%
/leftVizPosition { 0.25 pageWidth mul } def
/bottomVizPosition { 0.33 pageHeight mul } def
/topVizMargin { .20 pageHeight mul } def
/topVizPosition { pageHeight topVizMargin sub } def
/vizWidth { pageWidth leftVizPosition sub } def
/vizHeight { pageHeight bottomVizPosition sub topVizMargin} def

/bar { % x1 y1 deltaX deltaY => -
	newpath
	4 2 roll moveto
	exch dup 0 rlineto 0 3 -1 roll
	rlineto neg 0 rlineto
	fill
} def
/labelbar { % (label) x1 y1 deltaX deltaY => -
	5 -1 roll % stack: x1 y1 deltaX deltaY label
	4 index 4 index 3 index 2 div add % stack: x1 y1 deltaX deltaY label x1 (y1 + deltaY/2)
	labelbarLabel % draw the label at x1 (y1 + deltaY/2)
	bar
} def
/labelbarSpacing { 14.0 } def
/labelbarLabel { % (label) => -
	moveto
	leftVizPosition labelbarSpacing sub exch righttrim
	dup stringwidth pop neg labelbarSpacing sub 0 rmoveto
	show
} def

/datelineLineWidth { 1 } def
/datelineDashSize { 15 } def
/dateline { % (label) x => -
	gsave
	dup
	0 moveto
	.75 .75 .75 setrgbcolor
	[datelineDashSize] 0 setdash
	datelineLineWidth setlinewidth
	0 topVizPosition rlineto
	stroke
	datelinelabel
	grestore
} def

/datelineLabelFontSize { 6.0 } def
/datelinelabel { % (label) x => -
	/Arial datelineLabelFontSize selectfont
	datelineLabelFontSize neg center
	show
} def

>>

pages() ::= <<

>>