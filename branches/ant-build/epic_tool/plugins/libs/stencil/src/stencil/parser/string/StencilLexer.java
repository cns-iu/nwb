// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g 2009-11-18 16:32:35

	package stencil.parser.string;
	import static stencil.tuple.Tuples.stripQuotes;	

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class StencilLexer extends Lexer {
    public static final int DIRECT_YIELD=9;
    public static final int PRE=21;
    public static final int CLOSE_GROUP=54;
    public static final int AS=51;
    public static final int NUMBER=13;
    public static final int TEMPLATE=45;
    public static final int FACET=52;
    public static final int NAMESPACE=59;
    public static final int VIEW=50;
    public static final int IMPORT=41;
    public static final int TUPLE_REF=30;
    public static final int PREDICATE=22;
    public static final int POST=20;
    public static final int ORDER=46;
    public static final int BASIC=6;
    public static final int OPERATOR_BASE=19;
    public static final int LOCAL=42;
    public static final int FUNCTION=10;
    public static final int ANIMATED_DYNAMIC=63;
    public static final int YIELDS=64;
    public static final int TAG=71;
    public static final int CANVAS=34;
    public static final int RANGE=58;
    public static final int SIGIL=27;
    public static final int SPLIT=69;
    public static final int FILTER=37;
    public static final int T__89=89;
    public static final int WS=79;
    public static final int STRING=75;
    public static final int ANIMATED=62;
    public static final int T__92=92;
    public static final int T__88=88;
    public static final int T__90=90;
    public static final int OPERATOR_REFERENCE=16;
    public static final int CANVAS_DEF=12;
    public static final int T__91=91;
    public static final int OPERATOR_TEMPLATE=17;
    public static final int CONSUMES=7;
    public static final int DYNAMIC=61;
    public static final int T__85=85;
    public static final int PYTHON=47;
    public static final int ANNOTATION=4;
    public static final int FEED=65;
    public static final int BOOLEAN_OP=5;
    public static final int ALL=32;
    public static final int T__93=93;
    public static final int T__86=86;
    public static final int MAP_ENTRY=31;
    public static final int T__94=94;
    public static final int EXTERNAL=36;
    public static final int LIST=11;
    public static final int GLYPH=39;
    public static final int T__95=95;
    public static final int OPERATOR_PROXY=15;
    public static final int GATE=68;
    public static final int RULE=26;
    public static final int PACK=24;
    public static final int TUPLE_PROTOTYPE=29;
    public static final int PROGRAM=23;
    public static final int T__87=87;
    public static final int GUIDE_FEED=66;
    public static final int OPERATOR_INSTANCE=14;
    public static final int GUIDE=40;
    public static final int LAYER=43;
    public static final int GROUP=53;
    public static final int NESTED_BLOCK=77;
    public static final int OPERATOR=44;
    public static final int JOIN=70;
    public static final int PYTHON_FACET=25;
    public static final int BASE=33;
    public static final int FROM=38;
    public static final int ID=72;
    public static final int STREAM=49;
    public static final int DIGITS=76;
    public static final int OPERATOR_RULE=18;
    public static final int TAGGED_ID=74;
    public static final int CODE_BLOCK=73;
    public static final int CALL_CHAIN=8;
    public static final int COMMENT=80;
    public static final int SPECIALIZER=28;
    public static final int CLOSE_ARG=56;
    public static final int GUIDE_YIELD=67;
    public static final int T__84=84;
    public static final int SEPARATOR=57;
    public static final int DEFINE=60;
    public static final int RETURN=48;
    public static final int ESCAPE_SEQUENCE=78;
    public static final int ARG=55;
    public static final int EOF=-1;
    public static final int DEFAULT=35;
    public static final int T__82=82;
    public static final int T__81=81;
    public static final int T__83=83;

    // delegates
    // delegators

    public StencilLexer() {;} 
    public StencilLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public StencilLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g"; }

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:12:5: ( 'all' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:12:7: 'all'
            {
            match("all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "BASE"
    public final void mBASE() throws RecognitionException {
        try {
            int _type = BASE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:13:6: ( 'base' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:13:8: 'base'
            {
            match("base"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BASE"

    // $ANTLR start "CANVAS"
    public final void mCANVAS() throws RecognitionException {
        try {
            int _type = CANVAS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:14:8: ( 'canvas' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:14:10: 'canvas'
            {
            match("canvas"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CANVAS"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:15:9: ( 'default' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:15:11: 'default'
            {
            match("default"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "EXTERNAL"
    public final void mEXTERNAL() throws RecognitionException {
        try {
            int _type = EXTERNAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:16:10: ( 'external' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:16:12: 'external'
            {
            match("external"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXTERNAL"

    // $ANTLR start "FILTER"
    public final void mFILTER() throws RecognitionException {
        try {
            int _type = FILTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:17:8: ( 'filter' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:17:10: 'filter'
            {
            match("filter"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FILTER"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:18:6: ( 'from' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:18:8: 'from'
            {
            match("from"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FROM"

    // $ANTLR start "GLYPH"
    public final void mGLYPH() throws RecognitionException {
        try {
            int _type = GLYPH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:19:7: ( 'glyph' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:19:9: 'glyph'
            {
            match("glyph"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GLYPH"

    // $ANTLR start "GUIDE"
    public final void mGUIDE() throws RecognitionException {
        try {
            int _type = GUIDE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:20:7: ( 'guide' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:20:9: 'guide'
            {
            match("guide"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GUIDE"

    // $ANTLR start "IMPORT"
    public final void mIMPORT() throws RecognitionException {
        try {
            int _type = IMPORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:21:8: ( 'import' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:21:10: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IMPORT"

    // $ANTLR start "LOCAL"
    public final void mLOCAL() throws RecognitionException {
        try {
            int _type = LOCAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:22:7: ( 'local' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:22:9: 'local'
            {
            match("local"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOCAL"

    // $ANTLR start "LAYER"
    public final void mLAYER() throws RecognitionException {
        try {
            int _type = LAYER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:23:7: ( 'layer' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:23:9: 'layer'
            {
            match("layer"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LAYER"

    // $ANTLR start "OPERATOR"
    public final void mOPERATOR() throws RecognitionException {
        try {
            int _type = OPERATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:24:10: ( 'operator' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:24:12: 'operator'
            {
            match("operator"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPERATOR"

    // $ANTLR start "TEMPLATE"
    public final void mTEMPLATE() throws RecognitionException {
        try {
            int _type = TEMPLATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:25:10: ( 'template' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:25:12: 'template'
            {
            match("template"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TEMPLATE"

    // $ANTLR start "ORDER"
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:26:7: ( 'order' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:26:9: 'order'
            {
            match("order"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ORDER"

    // $ANTLR start "PYTHON"
    public final void mPYTHON() throws RecognitionException {
        try {
            int _type = PYTHON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:27:8: ( 'python' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:27:10: 'python'
            {
            match("python"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PYTHON"

    // $ANTLR start "RETURN"
    public final void mRETURN() throws RecognitionException {
        try {
            int _type = RETURN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:28:8: ( 'return' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:28:10: 'return'
            {
            match("return"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RETURN"

    // $ANTLR start "STREAM"
    public final void mSTREAM() throws RecognitionException {
        try {
            int _type = STREAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:29:8: ( 'stream' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:29:10: 'stream'
            {
            match("stream"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STREAM"

    // $ANTLR start "VIEW"
    public final void mVIEW() throws RecognitionException {
        try {
            int _type = VIEW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:30:6: ( 'view' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:30:8: 'view'
            {
            match("view"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VIEW"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:31:4: ( 'as' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:31:6: 'as'
            {
            match("as"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "FACET"
    public final void mFACET() throws RecognitionException {
        try {
            int _type = FACET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:32:7: ( 'facet' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:32:9: 'facet'
            {
            match("facet"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FACET"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:33:7: ( '(' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:33:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GROUP"

    // $ANTLR start "CLOSE_GROUP"
    public final void mCLOSE_GROUP() throws RecognitionException {
        try {
            int _type = CLOSE_GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:34:13: ( ')' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:34:15: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLOSE_GROUP"

    // $ANTLR start "ARG"
    public final void mARG() throws RecognitionException {
        try {
            int _type = ARG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:35:5: ( '[' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:35:7: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ARG"

    // $ANTLR start "CLOSE_ARG"
    public final void mCLOSE_ARG() throws RecognitionException {
        try {
            int _type = CLOSE_ARG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:36:11: ( ']' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:36:13: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLOSE_ARG"

    // $ANTLR start "SEPARATOR"
    public final void mSEPARATOR() throws RecognitionException {
        try {
            int _type = SEPARATOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:37:11: ( ',' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:37:13: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEPARATOR"

    // $ANTLR start "RANGE"
    public final void mRANGE() throws RecognitionException {
        try {
            int _type = RANGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:38:7: ( '..' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:38:9: '..'
            {
            match(".."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RANGE"

    // $ANTLR start "NAMESPACE"
    public final void mNAMESPACE() throws RecognitionException {
        try {
            int _type = NAMESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:39:11: ( '::' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:39:13: '::'
            {
            match("::"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NAMESPACE"

    // $ANTLR start "DEFINE"
    public final void mDEFINE() throws RecognitionException {
        try {
            int _type = DEFINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:40:8: ( ':' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:40:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFINE"

    // $ANTLR start "DYNAMIC"
    public final void mDYNAMIC() throws RecognitionException {
        try {
            int _type = DYNAMIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:41:9: ( ':*' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:41:11: ':*'
            {
            match(":*"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DYNAMIC"

    // $ANTLR start "ANIMATED"
    public final void mANIMATED() throws RecognitionException {
        try {
            int _type = ANIMATED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:42:10: ( '<:' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:42:12: '<:'
            {
            match("<:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANIMATED"

    // $ANTLR start "ANIMATED_DYNAMIC"
    public final void mANIMATED_DYNAMIC() throws RecognitionException {
        try {
            int _type = ANIMATED_DYNAMIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:43:18: ( '<*' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:43:20: '<*'
            {
            match("<*"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANIMATED_DYNAMIC"

    // $ANTLR start "YIELDS"
    public final void mYIELDS() throws RecognitionException {
        try {
            int _type = YIELDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:44:8: ( '->' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:44:10: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "YIELDS"

    // $ANTLR start "FEED"
    public final void mFEED() throws RecognitionException {
        try {
            int _type = FEED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:45:6: ( '>>' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:45:8: '>>'
            {
            match(">>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FEED"

    // $ANTLR start "GUIDE_FEED"
    public final void mGUIDE_FEED() throws RecognitionException {
        try {
            int _type = GUIDE_FEED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:46:12: ( '#>>' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:46:14: '#>>'
            {
            match("#>>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GUIDE_FEED"

    // $ANTLR start "GUIDE_YIELD"
    public final void mGUIDE_YIELD() throws RecognitionException {
        try {
            int _type = GUIDE_YIELD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:47:13: ( '#->' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:47:15: '#->'
            {
            match("#->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GUIDE_YIELD"

    // $ANTLR start "GATE"
    public final void mGATE() throws RecognitionException {
        try {
            int _type = GATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:48:6: ( '=>' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:48:8: '=>'
            {
            match("=>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GATE"

    // $ANTLR start "SPLIT"
    public final void mSPLIT() throws RecognitionException {
        try {
            int _type = SPLIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:49:7: ( '|' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:49:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPLIT"

    // $ANTLR start "JOIN"
    public final void mJOIN() throws RecognitionException {
        try {
            int _type = JOIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:50:6: ( '-->' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:50:8: '-->'
            {
            match("-->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JOIN"

    // $ANTLR start "TAG"
    public final void mTAG() throws RecognitionException {
        try {
            int _type = TAG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:51:5: ( '@' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:51:7: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAG"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:52:7: ( '>' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:52:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:53:7: ( 'Init' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:53:9: 'Init'
            {
            match("Init"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:54:7: ( '=' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:54:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:55:7: ( '_' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:55:9: '_'
            {
            match('_'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:56:7: ( '>=' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:56:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:57:7: ( '<' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:57:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:58:7: ( '<=' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:58:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:59:7: ( '!=' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:59:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:60:7: ( '=~' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:60:9: '=~'
            {
            match("=~"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:61:7: ( '!~' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:61:9: '!~'
            {
            match("!~"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:62:7: ( '-[' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:62:9: '-['
            {
            match("-["); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:63:7: ( ']>' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:63:9: ']>'
            {
            match("]>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:64:7: ( '-' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:64:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:65:7: ( '+' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:65:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:66:7: ( '.' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:66:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "TAGGED_ID"
    public final void mTAGGED_ID() throws RecognitionException {
        try {
            int _type = TAGGED_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:469:10: ( TAG ID )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:469:12: TAG ID
            {
            mTAG(); 
            mID(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAGGED_ID"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( ( '.' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' ) )* )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( ( '.' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' ) )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:37: ( ( '.' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:38: ( '.' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )
            	    {
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:38: ( '.' )?
            	    int alt1=2;
            	    int LA1_0 = input.LA(1);

            	    if ( (LA1_0=='.') ) {
            	        alt1=1;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:471:38: '.'
            	            {
            	            match('.'); 

            	            }
            	            break;

            	    }

            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "DIGITS"
    public final void mDIGITS() throws RecognitionException {
        try {
            int _type = DIGITS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:473:9: ( ( '0' .. '9' )+ )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:473:11: ( '0' .. '9' )+
            {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:473:11: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:473:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIGITS"

    // $ANTLR start "CODE_BLOCK"
    public final void mCODE_BLOCK() throws RecognitionException {
        try {
            int _type = CODE_BLOCK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:477:5: ( NESTED_BLOCK )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:477:7: NESTED_BLOCK
            {
            mNESTED_BLOCK(); 
            setText(getText().substring(1, getText().length()-1));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CODE_BLOCK"

    // $ANTLR start "NESTED_BLOCK"
    public final void mNESTED_BLOCK() throws RecognitionException {
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:481:5: ( '{' ( options {greedy=false; k=2; } : NESTED_BLOCK | . )* '}' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:481:7: '{' ( options {greedy=false; k=2; } : NESTED_BLOCK | . )* '}'
            {
            match('{'); 
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:481:11: ( options {greedy=false; k=2; } : NESTED_BLOCK | . )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='}') ) {
                    alt4=3;
                }
                else if ( (LA4_0=='{') ) {
                    alt4=1;
                }
                else if ( ((LA4_0>='\u0000' && LA4_0<='z')||LA4_0=='|'||(LA4_0>='~' && LA4_0<='\uFFFF')) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:481:41: NESTED_BLOCK
            	    {
            	    mNESTED_BLOCK(); 

            	    }
            	    break;
            	case 2 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:481:56: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('}'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NESTED_BLOCK"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:485:7: ( '\"' ( ESCAPE_SEQUENCE | ~ ( '\\\\' | '\"' ) )* '\"' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:485:10: '\"' ( ESCAPE_SEQUENCE | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:485:14: ( ESCAPE_SEQUENCE | ~ ( '\\\\' | '\"' ) )*
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\\') ) {
                    alt5=1;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFF')) ) {
                    alt5=2;
                }


                switch (alt5) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:485:16: ESCAPE_SEQUENCE
            	    {
            	    mESCAPE_SEQUENCE(); 

            	    }
            	    break;
            	case 2 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:485:34: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('\"'); 
            setText(stripQuotes(getText()));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "ESCAPE_SEQUENCE"
    public final void mESCAPE_SEQUENCE() throws RecognitionException {
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:490:5: ( '\\\\b' | '\\\\t' | '\\\\n' | '\\\\f' | '\\\\r' | '\\\\\\\"' | '\\\\\\'' | '\\\\\\\\' )
            int alt6=8;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:490:7: '\\\\b'
                    {
                    match("\\b"); 

                    setText(getText().substring(0, getText().length()-2) + "\b");

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:491:7: '\\\\t'
                    {
                    match("\\t"); 

                    setText(getText().substring(0, getText().length()-2) + "\t");

                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:492:7: '\\\\n'
                    {
                    match("\\n"); 

                    setText(getText().substring(0, getText().length()-2) + "\n");

                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:493:7: '\\\\f'
                    {
                    match("\\f"); 

                    setText(getText().substring(0, getText().length()-2) + "\f");

                    }
                    break;
                case 5 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:494:7: '\\\\r'
                    {
                    match("\\r"); 

                    setText(getText().substring(0, getText().length()-2) + "\r");

                    }
                    break;
                case 6 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:495:7: '\\\\\\\"'
                    {
                    match("\\\""); 

                    setText(getText().substring(0, getText().length()-2) + "\"");

                    }
                    break;
                case 7 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:496:7: '\\\\\\''
                    {
                    match("\\'"); 

                    setText(getText().substring(0, getText().length()-2) + "\'");

                    }
                    break;
                case 8 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:497:7: '\\\\\\\\'
                    {
                    match("\\\\"); 

                    setText(getText().substring(0, getText().length()-2) + "\\");

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPE_SEQUENCE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:500:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )+ )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:500:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )+
            {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:500:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\t' && LA7_0<='\n')||(LA7_0>='\f' && LA7_0<='\r')||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:501:9: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:501:13: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:501:18: ( options {greedy=false; } : . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:501:44: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 

            skip(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:8: ( ALL | BASE | CANVAS | DEFAULT | EXTERNAL | FILTER | FROM | GLYPH | GUIDE | IMPORT | LOCAL | LAYER | OPERATOR | TEMPLATE | ORDER | PYTHON | RETURN | STREAM | VIEW | AS | FACET | GROUP | CLOSE_GROUP | ARG | CLOSE_ARG | SEPARATOR | RANGE | NAMESPACE | DEFINE | DYNAMIC | ANIMATED | ANIMATED_DYNAMIC | YIELDS | FEED | GUIDE_FEED | GUIDE_YIELD | GATE | SPLIT | JOIN | TAG | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | TAGGED_ID | ID | DIGITS | CODE_BLOCK | STRING | WS | COMMENT )
        int alt9=62;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:10: ALL
                {
                mALL(); 

                }
                break;
            case 2 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:14: BASE
                {
                mBASE(); 

                }
                break;
            case 3 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:19: CANVAS
                {
                mCANVAS(); 

                }
                break;
            case 4 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:26: DEFAULT
                {
                mDEFAULT(); 

                }
                break;
            case 5 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:34: EXTERNAL
                {
                mEXTERNAL(); 

                }
                break;
            case 6 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:43: FILTER
                {
                mFILTER(); 

                }
                break;
            case 7 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:50: FROM
                {
                mFROM(); 

                }
                break;
            case 8 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:55: GLYPH
                {
                mGLYPH(); 

                }
                break;
            case 9 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:61: GUIDE
                {
                mGUIDE(); 

                }
                break;
            case 10 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:67: IMPORT
                {
                mIMPORT(); 

                }
                break;
            case 11 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:74: LOCAL
                {
                mLOCAL(); 

                }
                break;
            case 12 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:80: LAYER
                {
                mLAYER(); 

                }
                break;
            case 13 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:86: OPERATOR
                {
                mOPERATOR(); 

                }
                break;
            case 14 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:95: TEMPLATE
                {
                mTEMPLATE(); 

                }
                break;
            case 15 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:104: ORDER
                {
                mORDER(); 

                }
                break;
            case 16 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:110: PYTHON
                {
                mPYTHON(); 

                }
                break;
            case 17 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:117: RETURN
                {
                mRETURN(); 

                }
                break;
            case 18 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:124: STREAM
                {
                mSTREAM(); 

                }
                break;
            case 19 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:131: VIEW
                {
                mVIEW(); 

                }
                break;
            case 20 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:136: AS
                {
                mAS(); 

                }
                break;
            case 21 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:139: FACET
                {
                mFACET(); 

                }
                break;
            case 22 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:145: GROUP
                {
                mGROUP(); 

                }
                break;
            case 23 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:151: CLOSE_GROUP
                {
                mCLOSE_GROUP(); 

                }
                break;
            case 24 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:163: ARG
                {
                mARG(); 

                }
                break;
            case 25 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:167: CLOSE_ARG
                {
                mCLOSE_ARG(); 

                }
                break;
            case 26 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:177: SEPARATOR
                {
                mSEPARATOR(); 

                }
                break;
            case 27 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:187: RANGE
                {
                mRANGE(); 

                }
                break;
            case 28 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:193: NAMESPACE
                {
                mNAMESPACE(); 

                }
                break;
            case 29 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:203: DEFINE
                {
                mDEFINE(); 

                }
                break;
            case 30 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:210: DYNAMIC
                {
                mDYNAMIC(); 

                }
                break;
            case 31 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:218: ANIMATED
                {
                mANIMATED(); 

                }
                break;
            case 32 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:227: ANIMATED_DYNAMIC
                {
                mANIMATED_DYNAMIC(); 

                }
                break;
            case 33 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:244: YIELDS
                {
                mYIELDS(); 

                }
                break;
            case 34 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:251: FEED
                {
                mFEED(); 

                }
                break;
            case 35 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:256: GUIDE_FEED
                {
                mGUIDE_FEED(); 

                }
                break;
            case 36 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:267: GUIDE_YIELD
                {
                mGUIDE_YIELD(); 

                }
                break;
            case 37 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:279: GATE
                {
                mGATE(); 

                }
                break;
            case 38 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:284: SPLIT
                {
                mSPLIT(); 

                }
                break;
            case 39 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:290: JOIN
                {
                mJOIN(); 

                }
                break;
            case 40 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:295: TAG
                {
                mTAG(); 

                }
                break;
            case 41 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:299: T__81
                {
                mT__81(); 

                }
                break;
            case 42 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:305: T__82
                {
                mT__82(); 

                }
                break;
            case 43 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:311: T__83
                {
                mT__83(); 

                }
                break;
            case 44 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:317: T__84
                {
                mT__84(); 

                }
                break;
            case 45 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:323: T__85
                {
                mT__85(); 

                }
                break;
            case 46 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:329: T__86
                {
                mT__86(); 

                }
                break;
            case 47 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:335: T__87
                {
                mT__87(); 

                }
                break;
            case 48 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:341: T__88
                {
                mT__88(); 

                }
                break;
            case 49 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:347: T__89
                {
                mT__89(); 

                }
                break;
            case 50 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:353: T__90
                {
                mT__90(); 

                }
                break;
            case 51 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:359: T__91
                {
                mT__91(); 

                }
                break;
            case 52 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:365: T__92
                {
                mT__92(); 

                }
                break;
            case 53 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:371: T__93
                {
                mT__93(); 

                }
                break;
            case 54 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:377: T__94
                {
                mT__94(); 

                }
                break;
            case 55 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:383: T__95
                {
                mT__95(); 

                }
                break;
            case 56 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:389: TAGGED_ID
                {
                mTAGGED_ID(); 

                }
                break;
            case 57 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:399: ID
                {
                mID(); 

                }
                break;
            case 58 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:402: DIGITS
                {
                mDIGITS(); 

                }
                break;
            case 59 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:409: CODE_BLOCK
                {
                mCODE_BLOCK(); 

                }
                break;
            case 60 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:420: STRING
                {
                mSTRING(); 

                }
                break;
            case 61 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:427: WS
                {
                mWS(); 

                }
                break;
            case 62 :
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:1:430: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA6_eotS =
        "\12\uffff";
    static final String DFA6_eofS =
        "\12\uffff";
    static final String DFA6_minS =
        "\1\134\1\42\10\uffff";
    static final String DFA6_maxS =
        "\1\134\1\164\10\uffff";
    static final String DFA6_acceptS =
        "\2\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10";
    static final String DFA6_specialS =
        "\12\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1",
            "\1\7\4\uffff\1\10\64\uffff\1\11\5\uffff\1\2\3\uffff\1\5\7\uffff"+
            "\1\4\3\uffff\1\6\1\uffff\1\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "488:1: fragment ESCAPE_SEQUENCE : ( '\\\\b' | '\\\\t' | '\\\\n' | '\\\\f' | '\\\\r' | '\\\\\\\"' | '\\\\\\'' | '\\\\\\\\' );";
        }
    }
    static final String DFA9_eotS =
        "\1\uffff\17\42\3\uffff\1\76\1\uffff\1\100\1\103\1\107\1\113\1\116"+
        "\1\uffff\1\123\1\uffff\1\124\1\42\1\127\10\uffff\1\42\1\133\23\42"+
        "\31\uffff\1\42\3\uffff\1\160\1\uffff\24\42\1\uffff\1\u0085\4\42"+
        "\1\u008a\14\42\1\u0097\1\u0098\1\uffff\4\42\1\uffff\1\u009d\1\u009e"+
        "\1\u009f\1\42\1\u00a1\1\u00a2\1\42\1\u00a4\4\42\2\uffff\1\u00a9"+
        "\2\42\1\u00ac\3\uffff\1\u00ad\2\uffff\1\42\1\uffff\1\42\1\u00b0"+
        "\1\u00b1\1\u00b2\1\uffff\1\u00b3\1\42\2\uffff\2\42\4\uffff\1\u00b7"+
        "\1\u00b8\1\u00b9\3\uffff";
    static final String DFA9_eofS =
        "\u00ba\uffff";
    static final String DFA9_minS =
        "\1\11\1\154\2\141\1\145\1\170\1\141\1\154\1\155\1\141\1\160\1\145"+
        "\1\171\1\145\1\164\1\151\3\uffff\1\76\1\uffff\1\56\2\52\1\55\1\75"+
        "\1\55\1\76\1\uffff\1\101\1\156\1\56\1\75\7\uffff\1\154\1\56\1\163"+
        "\1\156\1\146\1\164\1\154\1\157\1\143\1\171\1\151\1\160\1\143\1\171"+
        "\1\145\1\144\1\155\2\164\1\162\1\145\31\uffff\1\151\3\uffff\1\56"+
        "\1\uffff\1\145\1\166\1\141\1\145\1\164\1\155\1\145\1\160\1\144\1"+
        "\157\1\141\1\145\1\162\1\145\1\160\1\150\1\165\1\145\1\167\1\164"+
        "\1\uffff\1\56\1\141\1\165\1\162\1\145\1\56\1\164\1\150\1\145\1\162"+
        "\1\154\1\162\1\141\1\162\1\154\1\157\1\162\1\141\2\56\1\uffff\1"+
        "\163\1\154\1\156\1\162\1\uffff\3\56\1\164\2\56\1\164\1\56\1\141"+
        "\2\156\1\155\2\uffff\1\56\1\164\1\141\1\56\3\uffff\1\56\2\uffff"+
        "\1\157\1\uffff\1\164\3\56\1\uffff\1\56\1\154\2\uffff\1\162\1\145"+
        "\4\uffff\3\56\3\uffff";
    static final String DFA9_maxS =
        "\1\174\1\163\2\141\1\145\1\170\1\162\1\165\1\155\1\157\1\162\1\145"+
        "\1\171\1\145\1\164\1\151\3\uffff\1\76\1\uffff\1\56\1\72\1\75\1\133"+
        "\2\76\1\176\1\uffff\1\172\1\156\1\172\1\176\7\uffff\1\154\1\172"+
        "\1\163\1\156\1\146\1\164\1\154\1\157\1\143\1\171\1\151\1\160\1\143"+
        "\1\171\1\145\1\144\1\155\2\164\1\162\1\145\31\uffff\1\151\3\uffff"+
        "\1\172\1\uffff\1\145\1\166\1\141\1\145\1\164\1\155\1\145\1\160\1"+
        "\144\1\157\1\141\1\145\1\162\1\145\1\160\1\150\1\165\1\145\1\167"+
        "\1\164\1\uffff\1\172\1\141\1\165\1\162\1\145\1\172\1\164\1\150\1"+
        "\145\1\162\1\154\1\162\1\141\1\162\1\154\1\157\1\162\1\141\2\172"+
        "\1\uffff\1\163\1\154\1\156\1\162\1\uffff\3\172\1\164\2\172\1\164"+
        "\1\172\1\141\2\156\1\155\2\uffff\1\172\1\164\1\141\1\172\3\uffff"+
        "\1\172\2\uffff\1\157\1\uffff\1\164\3\172\1\uffff\1\172\1\154\2\uffff"+
        "\1\162\1\145\4\uffff\3\172\3\uffff";
    static final String DFA9_acceptS =
        "\20\uffff\1\26\1\27\1\30\1\uffff\1\32\7\uffff\1\46\4\uffff\1\66"+
        "\1\71\1\72\1\73\1\74\1\75\1\76\25\uffff\1\64\1\31\1\33\1\67\1\34"+
        "\1\36\1\35\1\37\1\40\1\57\1\56\1\41\1\47\1\63\1\65\1\42\1\55\1\51"+
        "\1\43\1\44\1\45\1\61\1\53\1\50\1\70\1\uffff\1\54\1\60\1\62\1\uffff"+
        "\1\24\24\uffff\1\1\24\uffff\1\2\4\uffff\1\7\14\uffff\1\23\1\52\4"+
        "\uffff\1\25\1\10\1\11\1\uffff\1\13\1\14\1\uffff\1\17\4\uffff\1\3"+
        "\2\uffff\1\6\1\12\2\uffff\1\20\1\21\1\22\1\4\3\uffff\1\5\1\15\1"+
        "\16";
    static final String DFA9_specialS =
        "\u00ba\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\46\1\uffff\2\46\22\uffff\1\46\1\40\1\45\1\32\4\uffff\1\20"+
            "\1\21\1\uffff\1\41\1\24\1\30\1\25\1\47\12\43\1\26\1\uffff\1"+
            "\27\1\33\1\31\1\uffff\1\35\10\42\1\36\21\42\1\22\1\uffff\1\23"+
            "\1\uffff\1\37\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\42\1\10"+
            "\2\42\1\11\2\42\1\12\1\14\1\42\1\15\1\16\1\13\1\42\1\17\4\42"+
            "\1\44\1\34",
            "\1\50\6\uffff\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\60\7\uffff\1\56\10\uffff\1\57",
            "\1\61\10\uffff\1\62",
            "\1\63",
            "\1\65\15\uffff\1\64",
            "\1\66\1\uffff\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "",
            "",
            "",
            "\1\75",
            "",
            "\1\77",
            "\1\102\17\uffff\1\101",
            "\1\105\17\uffff\1\104\2\uffff\1\106",
            "\1\111\20\uffff\1\110\34\uffff\1\112",
            "\1\115\1\114",
            "\1\120\20\uffff\1\117",
            "\1\121\77\uffff\1\122",
            "",
            "\32\125\4\uffff\1\125\1\uffff\32\125",
            "\1\126",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\130\100\uffff\1\131",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\132",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\156",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\157",
            "",
            "",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00a0",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00a3",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00aa",
            "\1\u00ab",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            "\1\u00ae",
            "",
            "\1\u00af",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\u00b4",
            "",
            "",
            "\1\u00b5",
            "\1\u00b6",
            "",
            "",
            "",
            "",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "\1\42\1\uffff\12\42\7\uffff\32\42\4\uffff\1\42\1\uffff\32\42",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ALL | BASE | CANVAS | DEFAULT | EXTERNAL | FILTER | FROM | GLYPH | GUIDE | IMPORT | LOCAL | LAYER | OPERATOR | TEMPLATE | ORDER | PYTHON | RETURN | STREAM | VIEW | AS | FACET | GROUP | CLOSE_GROUP | ARG | CLOSE_ARG | SEPARATOR | RANGE | NAMESPACE | DEFINE | DYNAMIC | ANIMATED | ANIMATED_DYNAMIC | YIELDS | FEED | GUIDE_FEED | GUIDE_YIELD | GATE | SPLIT | JOIN | TAG | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | TAGGED_ID | ID | DIGITS | CODE_BLOCK | STRING | WS | COMMENT );";
        }
    }
 

}