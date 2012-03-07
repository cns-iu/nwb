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

LegendTitleTop - this is a legend element with the title above the bar.  
It shows a bar with a set-able title, start label, and end label.  It also
includes the range.  You can see there is an option to center the title called
'centerTitle' that is not used.

*/


legendTitleTopDefinitions(areaColumn, minArea, maxArea, colorText1, colorText2) ::= <<

%%%%%%%%%%%%%%%
% Legend Area
%%%%%%%%%%%%%%%
/legendTitleTopTop { legendTitleTopBottom } def % HACK until I redo all the postscript to work from the top down.

/legendTitleTopTitle { (Legend) } def
/legendTitleTopArea { (Area size: <areaColumn>) } def
/legendTitleTopMin { (Minimum = <minArea>) } def
/legendTitleTopMax { (Maximum = <maxArea>) } def
/legendTitleTopColor1 { (<colorText1>) } def
/legendTitleTopColor2 { (<colorText2>) } def

/lineSpacing { 3.0 } def
/legendTitlteTopNormalFont { /ArialMT 10 selectfont } def
/legendTitlteTopTitleFont { /Arial-BoldMT 14 selectfont } def


/legendTitleTop { % (title) [min] [max] (category) => -
	gsave
	0 0 0 setrgbcolor
	legendTitleTopLeft legendTitleTopTop translate
	0 0 moveto
	legendTitlteTopTitleFont 
	legendTitleTopTitle dup show
	textheight 1 add neg 0 exch translate
	0 0 moveto
	legendTitlteTopNormalFont
	legendTitleTopArea dup show
	textheight 2 add neg 0 exch translate
	0 0 moveto
	legendTitleTopMin dup show
	textheight 2 add neg 0 exch translate
	0 0 moveto
	legendTitleTopMax dup dup show
	textheight neg 0 exch translate
	textheight neg 0 exch translate
	0 0 moveto
	
	legendTitleTopColor1 dup show
	textheight 1 add neg 0 exch translate
	0 0 moveto
	legendTitleTopColor2 dup show
	textheight 1 add neg 0 exch translate
	0 0 moveto	
	grestore
} def
	
>>

legendTitleTop() ::= <<

legendTitleTop

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
(NIH funding data rendered using "Temporal Bar Graph") show
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
	0 0 0 setrgbcolor
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
	/howtoTop { howtoBottom } def % HACK!  This is a shortcut until I update the code
	/howtoTitleFontSize { <howtoTitleFontSize> } def
	/howtoTitleFont { /Arial-BoldMT howtoTitleFontSize selectfont } def
	/howtoTextFontSize { <howtoTextFontSize> } def
	/howtoTextFont { /ArialMT howtoTextFontSize selectfont } def
	/howtoLineSpacing { 1.0 } def
	
	gsave
	0 0 0 setrgbcolor
	/ArialMT howtoTextFontSize selectfont
	howtoLeft howtoBottom translate
	0 howtoTitleFontSize neg moveto
	gsave howtoTitleFont (How To Read This Map) show grestore
	0 howtoTitleFontSize howtoLineSpacing add neg rmoveto
	
	howtoTextFont
	gsave (Temporal Bar Graph is a visualization of a dataset whose records have specific) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (beginnings and endings, are weighted, and may belong to a distinct category. Each) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (of the records is shown on the timeline, which spans the total time of the dataset, is) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (given an area based on the relative weight within the dataset, and is colored) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (according to the category (if one exists).) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	grestore
	
} def

>>

howto() ::= <<

howto

>>

/**
Area Legend

This will draw a few boxes to describe area further.  NOTE!  The bottom specified is used as the top!!

gapYearValue is the number of years, as a decimal rounded to one place that prepresents the distance between the year ticks. 
*/
areaLegendDefinitions(barWidth, bigBarHeight, bigBarValue, medBarHeight, medBarValue, smallBarHeight, smallBarValue, yearValue) ::= <<

%%%%%%%%%%%%%%%%
%  AreaLegend  %
%%%%%%%%%%%%%%%%
/areaLegend {
	/areaLegendTop { areaLegendBottom } def % HACK.  Sadly I chose the wrong dimentions to specify.  I should have done top and left.  Until I fix that, this is here.
	/fontSize { 10 } def
	/barWidth { <barWidth> } def
	/space { 6 } def
	
	/draw0Legend { true } def
	/bigBarHeight { <bigBarHeight> } def
	/medBarHeight { <medBarHeight> } def
	/smallBarHeight { <smallBarHeight> } def
	
	/bigBarValue { (<bigBarValue>) } def
	/medBarValue { (<medBarValue>) } def
	/smallBarValue { (<smallBarValue>) } def
	
	/yearValue { (<yearValue> Year(s)) } def
	
	gsave
	/ArialMT fontSize selectfont
	0 0 moveto
	areaLegendLeft areaLegendTop translate
	% Draw the big bar and label
	0 0 moveto
		barWidth 0 rlineto
		0 bigBarHeight neg rlineto
		barWidth neg 0 rlineto
	fill
	barWidth 5 add 0 moveto
	0 bigBarHeight 2 div neg 1 add rmoveto 
	bigBarValue centerTextLine bigBarValue show
	0 bigBarHeight space add neg translate
	
	% Draw the med bar and label
	0 0 moveto
		barWidth 0 rlineto
		0 medBarHeight neg rlineto
		barWidth neg 0 rlineto
	fill
	barWidth 5 add 0 moveto
	0 medBarHeight 2 div neg 1 add rmoveto 
	medBarValue centerTextLine medBarValue show
	0 medBarHeight space add neg translate
	
	% Draw the small bar and label
	0 0 moveto
		barWidth 0 rlineto
		0 smallBarHeight neg rlineto
		barWidth neg 0 rlineto
	fill
	barWidth 5 add 0 moveto
	0 smallBarHeight 2 div neg 1 add rmoveto 
	smallBarValue centerTextLine smallBarValue show
	0 smallBarHeight space 4 add add neg translate
	
	% Draw the 0 bar and lable if needed
	0 0 moveto
	  draw0Legend 
	  {
		gsave
	    [2] 0 setdash
		0 setlinewidth
		barWidth 0 rlineto
		stroke
		barWidth 5 add 0 moveto
		(0) centerTextLine (0) show
		grestore
		0 space neg translate
	  } 
	if
	
	% Draw the year line
	%%
	% This creates a notch, but I don't know how to fix that.
	%%
	0 0 moveto
	0 5 neg rlineto stroke
	0 0 moveto
	barWidth 0 rlineto stroke
	barWidth 0 moveto
	0 5 neg rlineto stroke
	% Draw the year label
	0 fontSize neg translate
	0 5 neg moveto
	yearValue show
	0 fontSize space add neg translate
	grestore

} def

>>

areaLegend() ::= <<

areaLegend

>>
