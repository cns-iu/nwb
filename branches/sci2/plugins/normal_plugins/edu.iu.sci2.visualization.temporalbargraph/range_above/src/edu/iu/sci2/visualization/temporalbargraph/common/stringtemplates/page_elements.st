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
	/Arial-ItalicMT footerFontSize selectfont
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


legendTitleTopDefinitions(areaColumn, minArea, maxArea, labelColumn, colorText1, colorText2) ::= <<

%%%%%%%%%%%%%%%
% Legend Area
%%%%%%%%%%%%%%%
/legendTitleTopTop { legendTitleTopBottom } def % HACK until I redo all the postscript to work from the top down.

/legendTitleTopTitle { (Legend) } def
/legendTitleTopArea { (Area size: <areaColumn>) } def
/legendTitleTopMin { (Minimum = <minArea>) } def
/legendTitleTopMax { (Maximum = <maxArea>) } def
/legendTitleTopLabel { (Text label: <labelColumn>) } def
/legendTitleTopColor1 { (<colorText1>) } def
/legendTitleTopColor2 { (<colorText2>) } def

/legendTitleTopNormalFontSize { 10 } def
/legendTitleTopTitleFontSize { 14 } def
/legendTitleTopNormalFont { /ArialMT legendTitleTopNormalFontSize selectfont } def
/legendTitleTopTitleFont { /Arial-BoldMT legendTitleTopTitleFontSize selectfont } def


/legendTitleTop { % (title) [min] [max] (category) => -
	gsave
	0 0 0 setrgbcolor
	legendTitleTopLeft legendTitleTopTop translate
	
	0 0 moveto
	legendTitleTopTitleFont 
	legendTitleTopTitle show
	legendTitleTopTitleFontSize 1 add neg 0 exch translate
	
	0 0 moveto
	legendTitleTopNormalFont
	legendTitleTopArea show
	legendTitleTopNormalFontSize 2 add neg 0 exch translate
	
	0 0 moveto
	legendTitleTopMin show
	legendTitleTopNormalFontSize 2 add neg 0 exch translate
	
	0 0 moveto
	legendTitleTopMax show
	legendTitleTopNormalFontSize neg 0 exch translate
	legendTitleTopNormalFontSize neg 0 exch translate
	
	0 0 moveto
	legendTitleTopLabel show
	legendTitleTopNormalFontSize 1 add neg 0 exch translate
	
	0 0 moveto 
	legendTitleTopColor1 show
	legendTitleTopNormalFontSize 1 add neg 0 exch translate
	
	0 0 moveto
	legendTitleTopColor2 show
	legendTitleTopNormalFontSize 1 add neg 0 exch translate
	grestore
} def
	
>>

legendTitleTop() ::= <<

legendTitleTop

>>

/**
	
	Title - puts a page title with a user supplied string, and a date left aligned at the top.
	
*/

pageHeadingDefinitions(title, query, date) ::= <<

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Title - pageHeading
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

/pageHeading {

/pageHeadingTop { pageHeadingBottom } def % HACK until I've rewritten this
/lineSpacing { 3.0 } def
/titleFontSize { 14 } def
/titleFont { /Arial-BoldMT titleFontSize selectfont } def
/otherFontSize { 10 } def
/otherFont { /ArialMT otherFontSize selectfont } def

/title { (<title>) } def
/query { (<query>) } def
/date { (<date>) } def

gsave

pageHeadingLeft pageHeadingTop translate
0 0 moveto
	
0 titleFontSize neg moveto
gsave titleFont title show grestore
0 titleFontSize lineSpacing add neg rmoveto

query length 0 gt % query exists or doesn't
{ 
	gsave otherFont query show grestore
	0 otherFontSize lineSpacing add neg rmoveto
} 
if

gsave otherFont date show grestore
0 otherFontSize lineSpacing add neg rmoveto

grestore

} def

>>

pageHeading() ::= <<

pageHeading

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
	gsave (This ) show /Arial-ItalicMT howtoTextFontSize selectfont (temporal bar graph) show howtoTextFont ( visualization represents each record as a horizontal bar) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (with a specific start and end date and a text label on its left side. The area of each) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (bar encodes a numerical attribute value, e.g., total amount of funding. Bars may be) show grestore
	0 howtoTextFontSize howtoLineSpacing add neg rmoveto
	gsave (colored to present categorical attribute values of records.) show grestore
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
