group web_temporal_bar_graph;

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
	\<\< /PageSize [<pageWidth> <pageHeight>] \>\> setpagedevice
%%EndFeature
%%EndSetup
	
>>

documentTrailer() ::= <<

%%Trailer

>>

pageUtilities() ::= <<

%%%%%%%%%%%
% Utilities
%%%%%%%%%%%

/center { % (text) x y => -
	moveto dup stringwidth pop -2 div 0 rmoveto
} def

/concatstrings { % (a) (b) => (ab)
	exch dup length
	2 index length add string
	dup dup 4 2 roll copy length
	4 -1 roll putinterval
} bind def

/righttrim { % [x]points (abcde) -> (a...)
	dup % => [x] (text) (text)
	stringwidth pop % => [x] (text) [text_width]
	3 -1 roll % => (text) [text_width] [x]
	dup % => (text) [text_width] [x] [x]
	3 1 roll % => (text) [x] [text_width] [x]
	gt % => (text) [x] 
	{ 
	exch dup % => [x] (text) (text)
	length 1 sub % [x] (text) [text_length - 1]
	0 exch getinterval % (t)
	righttrim
	dup length 3 sub % (text) [text_length - 3]
	0 exch getinterval % (t)
	(...) concatstrings % [x] (t...)
	} 
	{
	pop % => (t...)
	}
	ifelse
} def

>>

visualizationAreaDefinitions(topVizPosition, leftVizMargin) ::= <<

%%%%%%%%%%%
% Viz Area 
%%%%%%%%%%%
/topVizPosition { <topVizPosition> } def
/leftVizMargin { <leftVizMargin> } def

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
	leftVizMargin exch righttrim
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

visualizationAreaSetup(leftMargin, bottomMargin) ::= <<

gsave
/Arial 16 selectfont
<leftMargin> <bottomMargin> translate

>>

visualizationAreaTearDown() ::= <<

grestore

>>

visualizationLabelBarFont(fontname, fontsize) ::= <<

/<fontname> <fontsize> selectfont

>>

visualizationLabelBar(label, x1, y1, deltaX, deltaY) ::= <<

(<label>) <x1> <y1> <deltaX> <deltaY> labelbar

>>

visualizationDateLine(label, x1) ::= <<

(<label>) <x1> dateline

>>

scalebarAreaDefinitions(scaleBarLeftPosition, scaleBarBottomPosition, scaleBarWidth) ::= <<

%%%%%%%%%%%%%%%
% ScaleBar Area
%%%%%%%%%%%%%%%

/scaleBarLeftPosition { <scaleBarLeftPosition> } def
/scaleBarBottomPosition { <scaleBarBottomPosition> } def
/scaleBarWidth { <scaleBarWidth> } def

/scaleBarRightPosition { scaleBarWidth } def
/scaleBarYearFontSize { 10.0 } def
/scaleBarTitleFontSize { 10.0 } def
/scaleBarThickness { 10.0 } def

/scalebar { % (title) (start year) (end year) => -
	gsave
	.75 .75 .75 setrgbcolor
	scaleBarLeftPosition scaleBarBottomPosition translate
	0 0 moveto
	/Arial scaleBarTitleFontSize selectfont
	3 -1 roll % (start year) (end year) (title)
	scaleBarRightPosition 2 div % (start year) (end year) (title) [x]
	0  % => (title) [x] [y]
	center
	show
	0 scaleBarTitleFontSize 2 mul translate
	0 0 moveto
	/Arial scaleBarYearFontSize selectfont
	exch dup
	stringwidth pop 2 div 2 -1 roll show % [x1] (end) (title)
	exch dup
	stringwidth pop dup 2 div exch % [dx2] [dx2]/2 (end) [x1] (title)
	scaleBarRightPosition exch sub dup 3 -1 roll exch sub exch 0 moveto  % => RM [dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] RM-[dx2] [dx2]/2 (end) [x1] (title) => [dx2]/2 RM-[dx2] RM-[dx2] (end) [x1] (title) => ... => [x2] (end) [x1] (title)
	2 -1 roll show % [x2] [x1] (title)
	exch dup scaleBarYearFontSize moveto % [x1] [x2] (title)
	gsave
	0 0 0 setrgbcolor
	0 scaleBarThickness rlineto
	exch sub 0 rlineto
	0 scaleBarThickness neg rlineto
	fill
	stroke
	grestore
	grestore
} def

>>

scalebarScaleBar(title, startYearLabel, endYearLabel) ::= <<

(<title>) (<startYearLabel>) (<endYearLabel>) scalebar

>>

footerDefinitions(footerBottomPosition, pageWidth) ::= <<

%%%%%%%%%%%%%
% Footer Area
%%%%%%%%%%%%%

/footerBottomPosition { <footerBottomPosition> } def
/footerFontSize { 12.0 } def

/footer { % (footer)
	gsave
	0.75 0.75 0.75 setrgbcolor
	/Arial footerFontSize selectfont
	<pageWidth> 2 div footerFontSize footerBottomPosition add center
	show
	grestore
} def 

>>

footer(footer) ::= <<

(<footer>) footer

>>

pageSetup(pageNumber) ::= <<

%Page: <pageNumber> <pageNumber>

>>

pageTearDown() ::= <<

showpage

>>