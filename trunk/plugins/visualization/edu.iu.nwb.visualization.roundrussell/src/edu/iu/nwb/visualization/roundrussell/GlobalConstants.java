package edu.iu.nwb.visualization.roundrussell;

public class GlobalConstants {
	
	
	public static String PS_HEADER_CONTENT = "%!PS-Adobe-3.0 EPSF-3.0\n" +
		"%%Title: Hierarchical\n" +
		"%%BoundingBox:-1200 -1200 1200 1200\n" +
		"%%EndComments\n" +
		"300 400 translate\n" +
		".25 .25 scale\n" +
		"/Courier findfont\n" +
		"%/Times-Roman findfont\n" +
		"/fontsize 15 def\n" +
		"fontsize scalefont\n" +
		"setfont\n" +
		"\n" +
		"0 setlinewidth\n" +
		".7 setgray\n" +
		"\n" +
		"\n" +
		"/node { % (label) x y node\n" +
		"    gsave\n" +
		"    /oldfontsize fontsize def\n" +
		"    3 2 roll\n" +
		"    1 add dup fontsize mul /fontsize exch def\n" +
		"    currentfont exch scalefont setfont % undone by grestore, and I should really not even have the fontsize variable\n" +
		"    exch\n" +
		"    atan\n" +
		"    dup\n" +
		"    rotate\n" +
		"    radius 10 add 0 translate\n" +
		"    0 0 moveto\n" +
		"    dup 90 gt exch 270 lt and {180 rotate dup 0 exch stringwidth pop sub 0 rmoveto} if\n" +
		"    0 fontsize 3 div neg rmoveto\n" +
		"    show\n" +
		"    /fontsize oldfontsize def\n" +
		"    grestore\n" +
		"} def\n" +
		"\n" +
		"/flatten {\n" +
		"    {} forall\n" +
		"} def\n" +
		"\n" +
		"/array-node {\n" +
		"    flatten node\n" +
		"} def\n" +
		"\n" +
		"/divider { % angle divider\n" +
		"    gsave\n" +
		"    rotate\n" +
		"    3 setlinewidth\n" +
		"    radius 150 add 0 moveto\n" +
		"    radius 200 add 0 lineto\n" +
		"    stroke\n" +
		"    grestore\n" +
		"} def\n" +
		"\n" +
		"\n" +
		"/colors [\n" +
		"    [228 26 28]\n" +
		"    [55 126 184]\n" +
		"    [77 175 74]\n" +
		"    [152 78 163]\n" +
		"    [255 127 0]\n" +
		"    [255 255 51]\n" +
		"    [166 86 40]\n" +
		"    [241 140 210]\n" +
		"    [0 28 168]\n" +
		"    [174 165 0]\n" +
		"    [68 217 177]\n" +
		"    [255 222 98]\n" +
		"    [164 0 70]\n" +
		"    [139 155 147]\n" +
		"] def\n" +
		"\n" +
		"\n" +
		"/level {\n" +
		"    gsave\n" +
		"    5 setlinewidth\n" +
		"    1 add 20 mul radius add 200 add /levelradius exch def\n" +
		"    /dividers exch def\n" +
		"    dividers length 1 sub dividers exch get /previous exch def % trick to make colors go all the way around\n" +
		"    0 1 dividers length 1 sub {\n" +
		"        dup colors length mod colors exch get {255 div} forall setrgbcolor\n" +
		"        levelradius previous cos mul levelradius previous sin mul moveto\n" +
		"        dividers exch get dup 0 0 levelradius previous 4 index arc\n" +
		"        dup dup levelradius 20 sub exch cos mul exch levelradius 20 sub exch sin mul lineto\n" +
		"        stroke\n" +
		"        /previous exch def\n" +
		"    } for\n" +
		"\n" +
		"    grestore\n" +
		"} def\n" +
		"\n" +
		"/edge {\n" +
		"    gsave\n" +
		"    5 mul 1 add setlinewidth\n" +
		"    dup length 8 lt {.95} {.7} ifelse setgray\n" +
		"    b\n" +
		"    grestore\n" +
		"} def\n" +
		"\n" +
		"/twodup {\n" +
		"    1 index 1 index\n" +
		"} def\n" +
		"\n" +
		"\n" +
		"/beziercatmull {\n" +
		"    /y3 exch def\n" +
		"    /x3 exch def\n" +
		"    /y2 exch def\n" +
		"    /x2 exch def\n" +
		"    /y1 exch def\n" +
		"    /x1 exch def\n" +
		"    /y0 exch def\n" +
		"    /x0 exch def\n" +
		"\n" +
		"    0 1 6 div sub x0 mul\n" +
		"    x1 add\n" +
		"    1 6 div x2 mul add\n" +
		"\n" +
		"    0 1 6 div sub y0 mul\n" +
		"    y1 add\n" +
		"    1 6 div y2 mul add\n" +
		"\n" +
		"    1 6 div x1 mul\n" +
		"    x2 add\n" +
		"    0 1 6 div sub x3 mul add\n" +
		"\n" +
		"    1 6 div y1 mul\n" +
		"    y2 add\n" +
		"    0 1 6 div sub y3 mul add\n" +
		"\n" +
		"    x2\n" +
		"\n" +
		"    y2\n" +
		"\n" +
		"    curveto\n" +
		"\n" +
		"} def\n" +
		"\n" +
		"/catmull {\n" +
		"    /points exch def\n" +
		"    newpath\n" +
		"    points 0 get points 1 get\n" +
		"    1 index 1 index moveto\n" +
		"    0 1 5 {\n" +
		"        points exch get\n" +
		"    } for\n" +
		"    beziercatmull\n" +
		"\n" +
		"    0 2 points length 8 sub {\n" +
		"        8 {\n" +
		"            dup 1 add exch\n" +
		"            points exch get\n" +
		"            exch\n" +
		"        } repeat\n" +
		"        pop\n" +
		"        beziercatmull\n" +
		"    } for\n" +
		"\n" +
		"\n" +
		"    points length 6 sub 1 points length 1 sub {\n" +
		"        points exch get\n" +
		"    } for\n" +
		"    points points length 2 sub get points points length 1 sub get\n" +
		"    beziercatmull\n" +
		"\n" +
		"    stroke\n" +
		"} def\n" +
		"\n" +
		"/bezierb {\n" +
		"    /y3 exch def\n" +
		"    /x3 exch def\n" +
		"    /y2 exch def\n" +
		"    /x2 exch def\n" +
		"    /y1 exch def\n" +
		"    /x1 exch def\n" +
		"    /y0 exch def\n" +
		"    /x0 exch def\n" +
		"\n" +
		"    1 6 div x0 mul\n" +
		"    2 3 div x1 mul add\n" +
		"    1 6 div x2 mul add\n" +
		"\n" +
		"    1 6 div y0 mul\n" +
		"    2 3 div y1 mul add\n" +
		"    1 6 div y2 mul add\n" +
		"\n" +
		"    moveto\n" +
		"\n" +
		"    2 3 div x1 mul\n" +
		"    1 3 div x2 mul add\n" +
		"\n" +
		"    2 3 div y1 mul\n" +
		"    1 3 div y2 mul add\n" +
		"\n" +
		"    1 3 div x1 mul\n" +
		"    2 3 div x2 mul add\n" +
		"\n" +
		"    1 3 div y1 mul\n" +
		"    2 3 div y2 mul add\n" +
		"\n" +
		"    1 6 div x1 mul\n" +
		"    2 3 div x2 mul add\n" +
		"    1 6 div x3 mul add\n" +
		"\n" +
		"    1 6 div y1 mul\n" +
		"    2 3 div y2 mul add\n" +
		"    1 6 div y3 mul add\n" +
		"\n" +
		"    curveto\n" +
		"\n" +
		"} def\n" +
		"\n" +
		"/b {\n" +
		"    gsave\n" +
		"    /points exch def\n" +
		"\n" +
		"    points length 4 eq { % if only four members, duplicate the first\n" +
		"        /points [\n" +
		"            points 0 get\n" +
		"            points 1 get\n" +
		"            points {} forall\n" +
		"        ] def\n" +
		"    } if\n" +
		"\n" +
		"    newpath\n" +
		"    3 {\n" +
		"        points 0 get\n" +
		"        points 1 get\n" +
		"    } repeat\n" +
		"    points 2 get\n" +
		"    points 3 get\n" +
		"    bezierb\n" +
		"\n" +
		"    points 0 get\n" +
		"    points 1 get\n" +
		"    0 1 5 {\n" +
		"        points exch get\n" +
		"    } for\n" +
		"    bezierb\n" +
		"\n" +
		"    0 2 points length 8 sub {\n" +
		"        8 {\n" +
		"            dup 1 add exch\n" +
		"            points exch get\n" +
		"            exch\n" +
		"        } repeat\n" +
		"        pop\n" +
		"        bezierb\n" +
		"    } for\n" +
		"\n" +
		"    points length 6 sub 1 points length 1 sub {\n" +
		"        points exch get\n" +
		"    } for\n" +
		"    points points length 2 sub get points points length 1 sub get\n" +
		"    bezierb\n" +
		"\n" +
		"    points length 4 sub 1 points length 1 sub {\n" +
		"        points exch get\n" +
		"    } for\n" +
		"    points points length 2 sub get points points length 1 sub get\n" +
		"    points points length 2 sub get points points length 1 sub get\n" +
		"    bezierb\n" +
		"\n" +
		"    <<\n" +
		"        /ShadingType 2\n" +
		"        /ColorSpace /DeviceRGB\n" +
		"        /Coords [-1000 -1000 1000 1000]\n" +
		"        /BBox [-1000 -1000 1000 1000]\n" +
		"        /Function\n" +
		"            <<\n" +
		"                /FunctionType 2\n" +
		"                /Domain [0 1]\n" +
		"                /c0 [1 0 0]\n" +
		"                /c1 [0 1 0]\n" +
		"                /N 1\n" +
		"            >>\n" +
		"    >>\n" +
		"    gsave\n" +
		"    currentlinewidth 1 add setlinewidth\n" +
		"    1 setgray\n" +
		"    stroke\n" +
		"    grestore\n" +
		"    stroke\n" +
		"    grestore\n" +
		"} def\n" +
		"\n" +
		"\n" +
		"\n" +
		"\n";

}
