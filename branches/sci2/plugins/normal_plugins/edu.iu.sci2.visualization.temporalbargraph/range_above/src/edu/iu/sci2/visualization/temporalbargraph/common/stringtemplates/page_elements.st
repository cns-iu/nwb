group page_elements;

/**

Footer - this is a footer element.  
It will ignore a left and put itself in the middle of the page.

*/

footerDefinitions(pageWidth) ::= <<

%%%%%%%%%%%%%
% Footer Area
%%%%%%%%%%%%%

/footerFontSize { 10.0 } def

/footer { % (footer)
	gsave
	0.6 0.6 0.6 setrgbcolor % #999999
	/ArialMT footerFontSize selectfont
	<pageWidth> 2 div footerFontSize footerBottom add center
	show
	grestore
} def 

>>

footer(footer) ::= <<

(<footer>) footer

>>


/**

Legend - this is a legend element.  
It shows a bar with a setable title, start label, and end label.

*/


legendDefinitions(legendWidth) ::= <<

%%%%%%%%%%%%%%%
% Legend Area
%%%%%%%%%%%%%%%
/lineSpacing { 3.0 } def
/legendWidth { <legendWidth> } def

/legendRightPosition { legendWidth } def
/legendYearFontSize { 10.0 } def
/legendTitleFontSize { 10.0 } def
/legendThickness { 10.0 } def

/legend { % (title) (start year) (end year) => -
	gsave
	0 0 0 setrgbcolor
	legendLeft legendBottom translate
	0 0 moveto
	/ArialMT legendTitleFontSize selectfont
	3 -1 roll % (start year) (end year) (title)
	legendRightPosition 2 div % (start year) (end year) (title) [x]
	0  % => (title) [x] [y]
	center
	show
	0 legendTitleFontSize 2 mul translate
	0 0 moveto
	/ArialMT legendYearFontSize selectfont
	exch dup
	stringwidth pop 2 div 2 -1 roll show % [x1] (end) (title)
	exch dup
	stringwidth pop dup 2 div exch % [dx2] [dx2]/2 (end) [x1] (title)
	legendRightPosition exch sub dup 3 -1 roll exch sub exch 0 moveto  % => RM [dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] RM-[dx2] [dx2]/2 (end) [x1] (title) => [dx2]/2 RM-[dx2] RM-[dx2] (end) [x1] (title) => ... => [x2] (end) [x1] (title)
	2 -1 roll show % [x2] [x1] (title)
	exch dup legendYearFontSize moveto % [x1] [x2] (title)
	gsave
	0 0 0 setrgbcolor
	0 legendThickness rlineto
	exch sub 0 rlineto
	0 legendThickness neg rlineto
	fill
	stroke
	grestore
	grestore
} def

>>

legend(title, startYearLabel, endYearLabel) ::= <<

(<title>) (<startYearLabel>) (<endYearLabel>) legend

>>

/**

LegendTitleTop - this is a legend element with the title above the bar.  
It shows a bar with a set-able title, start label, and end label.  It also
includes the range.  You can see there is an option to center the title called
'centerTitle' that is not used.

*/


legendTitleTopDefinitions(legendWidth) ::= <<

%%%%%%%%%%%%%%%
% Legend Area
%%%%%%%%%%%%%%%
/lineSpacing { 3.0 } def
/legendTitleTopWidth { <legendWidth> } def

/legendTitleTopRightPosition { legendTitleTopWidth } def
/legendTitleTopYearFontSize { 10.0 } def
/legendTitleTopTitleFontSize { 10.0 } def
/legendTitleTopThickness { 10.0 } def

/legendTitleTop { % (title) [min] [max] (start year) (end year) => -
	gsave
	0 0 0 setrgbcolor
	legendTitleTopLeft legendTitleTopBottom translate
	0 0 moveto
	legendTitleTopYearLabels
	legendTitleTopBar
	legendTitleTopRange
	legendTitleTopTitle
	grestore
} def

/legendTitleTopYearLabels {
	gsave
	/ArialMT legendTitleTopYearFontSize selectfont
	exch dup
	stringwidth pop 2 div 2 -1 roll show % [x1] (end) (title)
	exch dup
	stringwidth pop dup 2 div exch % [dx2] [dx2]/2 (end) [x1] (title)
	legendTitleTopRightPosition exch sub dup 3 -1 roll exch sub exch 0 moveto  % => RM [dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] [dx2]/2 (end) [x1] (title) => RM-[dx2] RM-[dx2] [dx2]/2 (end) [x1] (title) => [dx2]/2 RM-[dx2] RM-[dx2] (end) [x1] (title) => ... => [x2] (end) [x1] (title)
	2 -1 roll show % [x2] [x1] (title)
	exch dup
	grestore 
	0 legendTitleTopYearFontSize translate % [x1] [x2] (title)
	0 0 moveto
} def

/legendTitleTopBar {
	gsave
	0 translate
	0 0 moveto
	0 0 0 setrgbcolor
	0 legendTitleTopThickness rlineto
	exch sub 0 rlineto
	0 legendTitleTopThickness neg rlineto
	fill
	stroke
	grestore
	0 legendTitleTopThickness translate
	0 0 moveto
} def

/legendTitleTopCenterTitle {
	gsave
	/Arial-BoldMT legendTitleTopTitleFontSize selectfont
	legendTitleTopRightPosition 2 div % (title) [center x]
	0  % => (title) [x] [y]
	center
	show
	grestore
	0 legendTitleTopTitleFontSize lineSpacing add translate
	0 0 moveto
} def 

/legendTitleTopTitle {
	gsave
	/Arial-BoldMT legendTitleTopTitleFontSize selectfont
	0 0 moveto
	show
	grestore
	0 legendTitleTopTitleFontSize lineSpacing add translate
	0 0 moveto
} def 

/legendTitleTopRange { % [max] [min] => translation to top of text + spacing
	exch % [max] [min]
	/rangeBodyFontSize { 10 } def
	/ArialMT rangeBodyFontSize selectfont
	
	0 rangeBodyFontSize lineSpacing add translate
	0 0 moveto
	20 string cvs % [max] => (max)
	(Maximum = ) exch concatstrings	
	show
	
	0 rangeBodyFontSize lineSpacing add translate
	0 0 moveto
	20 string cvs
	(Minimum = ) exch concatstrings
	show
	
	0 rangeBodyFontSize lineSpacing add translate
	0 0 moveto
} def

	
>>

legendTitleTop(title, startYearLabel, endYearLabel, min, max) ::= <<

(<title>) (<max>) (<min>) (<startYearLabel>) (<endYearLabel>) legendTitleTop

>>

/**
	
	Title - puts a page title with a query string, info about the generation,
	and a date left aligned at the top.
	
*/

leftAlignedTitleWithQueryAndInfoDefinitions(titleFontType, titleFontSize, titleFontColor, otherFontType, otherFontSize, otherFontColor) ::= <<

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Title - leftAlignedTitleWithQueryAndInfo
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

/leftAlignedTitleWithQueryAndInfo { % title query date
/lineSpacing { 3.0 } def
/titleFontSize { <titleFontSize> } def
/titleFontType { /<titleFontType> } def
/titleFontColor { <titleFontColor> } def
/titleFont { 
	titleFontType titleFontSize selectfont 
	titleFontColor setrgbcolor
} def
/otherFontSize { <otherFontSize> } def
/otherFontType { /<otherFontType> } def
/otherFontColor { <otherFontColor> } def
/otherFont {
	otherFontType otherFontSize selectfont 
	otherFontColor setrgbcolor
} def

gsave
leftAlignedTitleWithQueryAndInfoLeft leftAlignedTitleWithQueryAndInfoBottom translate

0 0 moveto
otherFont
show
0 otherFontSize lineSpacing add translate

0 0 moveto 
otherFont
(NIH funding data rendered using 'Temporal Bar Graph') show
0 otherFontSize lineSpacing add translate

dup % (query) (query)
length 0 gt % query exists or doesn't
{ 
	0 0 moveto
	otherFont
	show
	0 otherFontSize lineSpacing add translate
} 
{ pop } 
ifelse

0 0 moveto
titleFont
show
0 titleFontSize lineSpacing add translate

grestore

} def

>>

leftAlignedTitleWithQueryAndInfo(title, query, date) ::= <<

(<title>) (<query>) (<date>) leftAlignedTitleWithQueryAndInfo

>>

/**

Title - puts a title at the top of the page

*/

pageTitleDefinitions(titleFontSize) ::= <<

%%%%%%%%%%%%%%%
% Title
%%%%%%%%%%%%%%%
/titleFontSize { <titleFontSize> } def

/title { % (title) => -  
	gsave
	0.0 0.0 0.0 setrgbcolor  
	/UniverseExtended titleFontSize selectfont  
	titleLeft % x  
	titleBottom titleFontSize add % y  
	center % put it in the center of the x and y above 
	show 
	grestore 
} def

>>

pageTitle(title) ::= <<

(<title>) title

>>

/** 
Query Info - this is a blurb about the query and information about how the rendering was done
*/

queryinfoDefinitions(queryInfoFontSize) ::= <<

%%%%%%%%%%%
% QueryInfo 
%%%%%%%%%%%
/queryInfoFontSize { <queryInfoFontSize> } def
/queryInfo { % (query) (info)
	gsave
	0.75 0.75 0.75 setrgbcolor
	/ArialMT queryInfoFontSize selectfont
	queryinfoLeft % x
	queryinfoBottom queryInfoFontSize queryInfoFontSize add add % y
	center % center on the left value
	show
	queryinfoLeft % x
	queryinfoBottom queryInfoFontSize add % y
	center % put the info at the center of queryinfoLeft
	show
	grestore
} def

>>

queryinfo(query, date) ::= <<

(<query>) (NIH funding data rendered by using 'Temporal Bar Graph' on <date>) queryInfo

>>


/**
Howto - this is a box explaining how to read the map.
*/

howtoDefinitions(howtoTitleFontSize, howtoTextFontSize) ::= <<

%%%%%%%%%%%
%  Howto  %
%%%%%%%%%%%
/howto {
	/howtoTitleFontSize { <howtoTitleFontSize> } def
	/howtoTextFontSize { <howtoTextFontSize> } def
	gsave
	/howtoLineSpacing { 1.0 } def
	0.6 0.6 0.6 setrgbcolor % #999999
	/ArialMT howtoTextFontSize selectfont
	howtoLeft howtoBottom translate
	0 0 moveto
		
	gsave
	(colored according to category (if one exists).)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	gsave
	(area based on the relative weight within the dataset, and is)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	gsave
	(timeline, which spans the total time of the dataset, is given an)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	gsave
	(belong to a distinct category.  Each of the records is shown on the)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	gsave
	(have specific beginnings and endings, are weighted, and may)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	gsave
	(Temporal Bar Graph is a visualization of a dataset whose records)
	show
	grestore
	0 howtoTextFontSize howtoLineSpacing add rmoveto
	
	
	0.0 0.0 0.0 setrgbcolor
	/Arial-BoldMT howtoTitleFontSize selectfont
	(How To Read This Map)
	show
	grestore
} def

>>

howto() ::= <<

howto

>>