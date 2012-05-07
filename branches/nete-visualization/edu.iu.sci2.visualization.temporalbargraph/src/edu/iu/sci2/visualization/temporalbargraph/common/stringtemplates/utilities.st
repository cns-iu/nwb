group utilities;

/**

This will allow you to set element definitions if you know the element name,
even if you are not the one putting that element in the page.

*/

pagePositionDefinitions(element, left, bottom) ::= <<

%%%%%%%%%%%%%%%%%%%%%%%
% <element> Positioning
%%%%%%%%%%%%%%%%%%%%%%%

/<element>Left { <left> } def
/<element>Bottom { <bottom> } def

>>


/**

These are helpful utility functions that do not relate to a specific page 
element, but can be used by any to get useful values to manipulate.

*/



pageUtilityDefinitions() ::= <<

%%%%%%%%%%%
% Utilities
%%%%%%%%%%%
% Taken from http://stackoverflow.com/questions/518837/how-can-you-get-the-height-metric-of-a-string-in-postscript
/textheight { % (text) => [text_height]
    gsave                                  % save graphic context
    {                            
        100 100 moveto                     % move to some point 
        true charpath pathbbox      	   % gets text path bounding box (LLx LLy URx URy)
        exch pop 3 -1 roll pop             % keeps LLy and URy
        exch sub                           % URy - LLy
    }
    stopped                                % did the last block fail?
    {
        pop pop                            % get rid of "stopped" junk
        currentfont /FontMatrix get 3 get  % gets alternative text height
    }
    if
    grestore                               % restore graphic context
} bind def

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

/centerTextLine { % x y string => - moves to where the text would be centered
    textheight 0.5 mul neg
    0 exch rmoveto
} bind def

>>