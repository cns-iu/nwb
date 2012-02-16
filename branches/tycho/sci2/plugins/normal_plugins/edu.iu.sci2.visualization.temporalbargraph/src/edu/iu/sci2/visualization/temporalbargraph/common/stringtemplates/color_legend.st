group color_legend;

colorLegendDefinitions() ::= <<

/colorLegendCategory { % (title) rgb
	gsave
	0 0 moveto
	setrgbcolor
	show
	grestore
} def

>>

colorLegendSetup(fontname, fontsize) ::= <<

/colorLegendCategoryFontname{ <fontname> } def
/colorLegendCategoryFontsize { <fontsize> } def
gsave
/colorLegendCategoryFontname colorLegendCategoryFontsize selectfont

>>

colorLegendTearDown() ::= <<

grestore

>>

colorLegendCategory(rgb, title) ::= <<

(<title>) <rgb> colorLegendCategory
0 colorLegendCategoryFontsize translate

>>