group category_breakdown;

categoryColumnDefinitions() ::= <<

/categoryColumn { % [columnNumber] [#ofCategory] [r] [g] [b] (categoryName) ... [r] [g] [b] (categoryName)
	/categoryWidth { 200 } def
	/categoryMargin { 50 } def
	/columnOffset { categoryWidth categoryMargin add } def
	/categoryFontSize { 10 } def
	/categoryFont { /ArialMT categoryFontSize selectfont } def
	gsave
	categoryFont
	categoryBreakdownLeft categoryBreakdownBottom translate
	columnOffset mul 0 moveto
		{
			0 categoryFontSize 2 mul neg rmoveto
			setrgbcolor
			gsave 
				categoryFontSize 0 rlineto
				0 categoryFontSize rlineto
				categoryFontSize neg 0 rlineto
				fill
			grestore
			
			gsave
				5 0 rmoveto
				0 0 0 setrgbcolor
				categoryFontSize 0 rmoveto
				categoryWidth exch righttrim show 
			grestore
		}
		repeat
	grestore
	
} def

>>
