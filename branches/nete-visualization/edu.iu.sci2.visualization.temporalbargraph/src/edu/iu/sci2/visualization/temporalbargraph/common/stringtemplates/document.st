group document;

header(numberOfPages, date) ::= <<
%!PS-Adobe-3.0
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


pageSetup(pageNumber) ::= <<

%%Page: <pageNumber> <pageNumber>

>>

pageTearDown() ::= <<

showpage

>>