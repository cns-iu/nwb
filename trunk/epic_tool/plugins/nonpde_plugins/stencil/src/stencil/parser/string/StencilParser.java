// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g 2009-10-23 23:24:53

  package stencil.parser.string;

  //TODO: Remove/delete glyph operation
  //TODO: Replacement of identifiers with numbers in tuples

  import static stencil.parser.ParserConstants.*;
  import java.util.ArrayList;
  import java.util.List;
    


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

@SuppressWarnings("all")
public class StencilParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANNOTATION", "BOOLEAN_OP", "BASIC", "CONSUMES", "CALL_GROUP", "CALL_CHAIN", "FUNCTION", "LIST", "CANVAS_DEF", "NUMBER", "OPERATOR_INSTANCE", "OPERATOR_PROXY", "OPERATOR_REFERENCE", "OPERATOR_TEMPLATE", "OPERATOR_RULE", "OPERATOR_BASE", "POST", "PRE", "PREDICATE", "PROGRAM", "PACK", "PYTHON_FACET", "RULE", "SIGIL", "SPECIALIZER", "TUPLE_PROTOTYPE", "TUPLE_REF", "MAP_ENTRY", "ALL", "BASE", "CANVAS", "COLOR", "DEFAULT", "EXTERNAL", "FILTER", "FROM", "GLYPH", "GUIDE", "IMPORT", "LOCAL", "LAYER", "OPERATOR", "TEMPLATE", "ORDER", "PYTHON", "RETURN", "STREAM", "VIEW", "AS", "FACET", "GROUP", "CLOSE_GROUP", "ARG", "CLOSE_ARG", "SEPARATOR", "RANGE", "NAMESPACE", "NAMESPLIT", "DEFINE", "DYNAMIC", "YIELDS", "FEED", "GUIDE_FEED", "GUIDE_YIELD", "GATE", "SPLIT", "JOIN", "TAG", "ID", "CODE_BLOCK", "TAGGED_ID", "STRING", "DIGITS", "NESTED_BLOCK", "ESCAPE_SEQUENCE", "WS", "COMMENT", "'>'", "'Init'", "'='", "'n'", "'_'", "'>='", "'<'", "'<='", "'!='", "'=~'", "'!~'", "'-'", "'+'"
    };
    public static final int FUNCTION=10;
    public static final int LAYER=44;
    public static final int EXTERNAL=37;
    public static final int SEPARATOR=58;
    public static final int TUPLE_PROTOTYPE=29;
    public static final int EOF=-1;
    public static final int TUPLE_REF=30;
    public static final int SIGIL=27;
    public static final int OPERATOR_TEMPLATE=17;
    public static final int CANVAS=34;
    public static final int T__93=93;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int IMPORT=42;
    public static final int CALL_CHAIN=9;
    public static final int T__90=90;
    public static final int ARG=56;
    public static final int CLOSE_ARG=57;
    public static final int RETURN=49;
    public static final int BASE=33;
    public static final int GUIDE_YIELD=67;
    public static final int COMMENT=80;
    public static final int STREAM=50;
    public static final int OPERATOR_PROXY=15;
    public static final int GATE=68;
    public static final int GLYPH=40;
    public static final int MAP_ENTRY=31;
    public static final int T__81=81;
    public static final int RULE=26;
    public static final int T__82=82;
    public static final int VIEW=51;
    public static final int T__83=83;
    public static final int CLOSE_GROUP=55;
    public static final int NUMBER=13;
    public static final int OPERATOR_INSTANCE=14;
    public static final int NAMESPLIT=61;
    public static final int LOCAL=43;
    public static final int T__85=85;
    public static final int LIST=11;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int NAMESPACE=60;
    public static final int CONSUMES=7;
    public static final int BOOLEAN_OP=5;
    public static final int YIELDS=64;
    public static final int GROUP=54;
    public static final int WS=79;
    public static final int OPERATOR_RULE=18;
    public static final int FILTER=38;
    public static final int TAGGED_ID=74;
    public static final int FROM=39;
    public static final int PACK=24;
    public static final int DYNAMIC=63;
    public static final int FACET=53;
    public static final int CANVAS_DEF=12;
    public static final int ORDER=47;
    public static final int ANNOTATION=4;
    public static final int SPLIT=69;
    public static final int DIGITS=76;
    public static final int PYTHON_FACET=25;
    public static final int PRE=21;
    public static final int OPERATOR_REFERENCE=16;
    public static final int ID=72;
    public static final int DEFINE=62;
    public static final int PREDICATE=22;
    public static final int COLOR=35;
    public static final int AS=52;
    public static final int ESCAPE_SEQUENCE=78;
    public static final int GUIDE=41;
    public static final int PYTHON=48;
    public static final int ALL=32;
    public static final int GUIDE_FEED=66;
    public static final int NESTED_BLOCK=77;
    public static final int OPERATOR=45;
    public static final int DEFAULT=36;
    public static final int RANGE=59;
    public static final int FEED=65;
    public static final int JOIN=70;
    public static final int TAG=71;
    public static final int OPERATOR_BASE=19;
    public static final int TEMPLATE=46;
    public static final int SPECIALIZER=28;
    public static final int CODE_BLOCK=73;
    public static final int POST=20;
    public static final int PROGRAM=23;
    public static final int BASIC=6;
    public static final int CALL_GROUP=8;
    public static final int STRING=75;

    // delegates
    // delegators


        public StencilParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public StencilParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return StencilParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g"; }


      List<String> errors = new ArrayList<String>();

      /**Buried IDs are strings that cannot be input as identifiers according to the
       * Stencil grammar, but are used internally as IDs.
       */
      public static String buryID(String input) {return "#" + input;}
      
      public void emitErrorMessage(String msg) {errors.add(msg);}
      public List getErrors() {return errors;}


      public static enum RuleOpts {
        All,  //Anything is allowed 
        Simple, //Only argument lists (no split or range)
        Empty
      }; //Must be empty


    public static class program_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:1: program : ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )* -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) ) ;
    public final StencilParser.program_return program() throws RecognitionException {
        StencilParser.program_return retval = new StencilParser.program_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.imports_return imports1 = null;

        StencilParser.externals_return externals2 = null;

        StencilParser.order_return order3 = null;

        StencilParser.canvasLayer_return canvasLayer4 = null;

        StencilParser.streamDef_return streamDef5 = null;

        StencilParser.layerDef_return layerDef6 = null;

        StencilParser.operatorDef_return operatorDef7 = null;

        StencilParser.pythonDef_return pythonDef8 = null;

        StencilParser.operatorTemplate_return operatorTemplate9 = null;


        RewriteRuleSubtreeStream stream_operatorTemplate=new RewriteRuleSubtreeStream(adaptor,"rule operatorTemplate");
        RewriteRuleSubtreeStream stream_layerDef=new RewriteRuleSubtreeStream(adaptor,"rule layerDef");
        RewriteRuleSubtreeStream stream_operatorDef=new RewriteRuleSubtreeStream(adaptor,"rule operatorDef");
        RewriteRuleSubtreeStream stream_order=new RewriteRuleSubtreeStream(adaptor,"rule order");
        RewriteRuleSubtreeStream stream_canvasLayer=new RewriteRuleSubtreeStream(adaptor,"rule canvasLayer");
        RewriteRuleSubtreeStream stream_pythonDef=new RewriteRuleSubtreeStream(adaptor,"rule pythonDef");
        RewriteRuleSubtreeStream stream_externals=new RewriteRuleSubtreeStream(adaptor,"rule externals");
        RewriteRuleSubtreeStream stream_streamDef=new RewriteRuleSubtreeStream(adaptor,"rule streamDef");
        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:9: ( ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )* -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:11: ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )*
            {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:11: ( imports )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==IMPORT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:11: imports
            	    {
            	    pushFollow(FOLLOW_imports_in_program640);
            	    imports1=imports();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_imports.add(imports1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            pushFollow(FOLLOW_externals_in_program643);
            externals2=externals();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_externals.add(externals2.getTree());
            pushFollow(FOLLOW_order_in_program645);
            order3=order();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_order.add(order3.getTree());
            pushFollow(FOLLOW_canvasLayer_in_program647);
            canvasLayer4=canvasLayer();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_canvasLayer.add(canvasLayer4.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:48: ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )*
            loop2:
            do {
                int alt2=6;
                switch ( input.LA(1) ) {
                case STREAM:
                    {
                    alt2=1;
                    }
                    break;
                case LAYER:
                    {
                    alt2=2;
                    }
                    break;
                case OPERATOR:
                    {
                    alt2=3;
                    }
                    break;
                case PYTHON:
                    {
                    alt2=4;
                    }
                    break;
                case TEMPLATE:
                    {
                    alt2=5;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:49: streamDef
            	    {
            	    pushFollow(FOLLOW_streamDef_in_program650);
            	    streamDef5=streamDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_streamDef.add(streamDef5.getTree());

            	    }
            	    break;
            	case 2 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:61: layerDef
            	    {
            	    pushFollow(FOLLOW_layerDef_in_program654);
            	    layerDef6=layerDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_layerDef.add(layerDef6.getTree());

            	    }
            	    break;
            	case 3 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:72: operatorDef
            	    {
            	    pushFollow(FOLLOW_operatorDef_in_program658);
            	    operatorDef7=operatorDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_operatorDef.add(operatorDef7.getTree());

            	    }
            	    break;
            	case 4 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:86: pythonDef
            	    {
            	    pushFollow(FOLLOW_pythonDef_in_program662);
            	    pythonDef8=pythonDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_pythonDef.add(pythonDef8.getTree());

            	    }
            	    break;
            	case 5 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:156:98: operatorTemplate
            	    {
            	    pushFollow(FOLLOW_operatorTemplate_in_program666);
            	    operatorTemplate9=operatorTemplate();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_operatorTemplate.add(operatorTemplate9.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);



            // AST REWRITE
            // elements: imports, pythonDef, operatorTemplate, externals, canvasLayer, operatorDef, order, layerDef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 157:5: -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:157:8: ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PROGRAM, "PROGRAM"), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:158:11: ^( LIST[\"Imports\"] ( imports )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Imports"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:158:29: ( imports )*
                while ( stream_imports.hasNext() ) {
                    adaptor.addChild(root_2, stream_imports.nextTree());

                }
                stream_imports.reset();

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_order.nextTree());
                adaptor.addChild(root_1, stream_externals.nextTree());
                adaptor.addChild(root_1, stream_canvasLayer.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:162:11: ^( LIST[\"Layers\"] ( layerDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Layers"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:162:28: ( layerDef )*
                while ( stream_layerDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_layerDef.nextTree());

                }
                stream_layerDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:163:11: ^( LIST[\"Operators\"] ( operatorDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Operators"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:163:31: ( operatorDef )*
                while ( stream_operatorDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_operatorDef.nextTree());

                }
                stream_operatorDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:164:11: ^( LIST[\"Pythons\"] ( pythonDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Pythons"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:164:29: ( pythonDef )*
                while ( stream_pythonDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_pythonDef.nextTree());

                }
                stream_pythonDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:165:11: ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "OperatorTemplates"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:165:39: ( operatorTemplate )*
                while ( stream_operatorTemplate.hasNext() ) {
                    adaptor.addChild(root_2, stream_operatorTemplate.nextTree());

                }
                stream_operatorTemplate.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class imports_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "imports"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:170:1: imports : IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )? -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] ) -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args) -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] ) -> ^( IMPORT[$name.text] $as $args) ;
    public final StencilParser.imports_return imports() throws RecognitionException {
        StencilParser.imports_return retval = new StencilParser.imports_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token as=null;
        Token IMPORT10=null;
        Token ARG11=null;
        Token CLOSE_ARG12=null;
        Token AS13=null;
        StencilParser.argList_return args = null;


        Object name_tree=null;
        Object as_tree=null;
        Object IMPORT10_tree=null;
        Object ARG11_tree=null;
        Object CLOSE_ARG12_tree=null;
        Object AS13_tree=null;
        RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
        RewriteRuleTokenStream stream_IMPORT=new RewriteRuleTokenStream(adaptor,"token IMPORT");
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleSubtreeStream stream_argList=new RewriteRuleSubtreeStream(adaptor,"rule argList");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:3: ( IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )? -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] ) -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args) -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] ) -> ^( IMPORT[$name.text] $as $args) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:5: IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )?
            {
            IMPORT10=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_imports825); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IMPORT.add(IMPORT10);

            name=(Token)match(input,ID,FOLLOW_ID_in_imports829); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:20: ( ARG args= argList CLOSE_ARG )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==ARG) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:21: ARG args= argList CLOSE_ARG
                    {
                    ARG11=(Token)match(input,ARG,FOLLOW_ARG_in_imports832); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG11);

                    pushFollow(FOLLOW_argList_in_imports836);
                    args=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_argList.add(args.getTree());
                    CLOSE_ARG12=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_imports838); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG12);


                    }
                    break;

            }

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:50: ( AS as= ID )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==AS) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:171:51: AS as= ID
                    {
                    AS13=(Token)match(input,AS,FOLLOW_AS_in_imports843); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AS.add(AS13);

                    as=(Token)match(input,ID,FOLLOW_ID_in_imports847); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(as);


                    }
                    break;

            }



            // AST REWRITE
            // elements: ID, IMPORT, IMPORT, as, args, ID, as, args, IMPORT, IMPORT
            // token labels: as
            // rule labels: retval, args
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_as=new RewriteRuleTokenStream(adaptor,"token as",as);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"rule args",args!=null?args.tree:null);

            root_0 = (Object)adaptor.nil();
            // 172:7: -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] )
            if (as==null && args==null) {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:172:36: ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, (Object)adaptor.create(ID, ""));
                adaptor.addChild(root_1, (Object)adaptor.create(LIST, "Arguments"));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 173:7: -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args)
            if (as==null && args!=null) {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:173:36: ^( IMPORT[$name.text] ID[\"\"] $args)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, (Object)adaptor.create(ID, ""));
                adaptor.addChild(root_1, stream_args.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 174:7: -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] )
            if (as!=null && args==null) {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:174:36: ^( IMPORT[$name.text] $as LIST[\"Arguments\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_as.nextNode());
                adaptor.addChild(root_1, (Object)adaptor.create(LIST, "Arguments"));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 175:7: -> ^( IMPORT[$name.text] $as $args)
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:175:10: ^( IMPORT[$name.text] $as $args)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_as.nextNode());
                adaptor.addChild(root_1, stream_args.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "imports"

    public static class order_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "order"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:177:1: order : ( ORDER orderRef ( '>' orderRef )* -> ^( ORDER ( orderRef )+ ) | -> ^( ORDER ) );
    public final StencilParser.order_return order() throws RecognitionException {
        StencilParser.order_return retval = new StencilParser.order_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ORDER14=null;
        Token char_literal16=null;
        StencilParser.orderRef_return orderRef15 = null;

        StencilParser.orderRef_return orderRef17 = null;


        Object ORDER14_tree=null;
        Object char_literal16_tree=null;
        RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
        RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
        RewriteRuleSubtreeStream stream_orderRef=new RewriteRuleSubtreeStream(adaptor,"rule orderRef");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:178:3: ( ORDER orderRef ( '>' orderRef )* -> ^( ORDER ( orderRef )+ ) | -> ^( ORDER ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==ORDER) ) {
                alt6=1;
            }
            else if ( (LA6_0==EOF||LA6_0==CANVAS||(LA6_0>=LAYER && LA6_0<=TEMPLATE)||LA6_0==PYTHON||LA6_0==STREAM) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:178:5: ORDER orderRef ( '>' orderRef )*
                    {
                    ORDER14=(Token)match(input,ORDER,FOLLOW_ORDER_in_order943); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ORDER.add(ORDER14);

                    pushFollow(FOLLOW_orderRef_in_order945);
                    orderRef15=orderRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderRef.add(orderRef15.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:178:20: ( '>' orderRef )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==81) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:178:21: '>' orderRef
                    	    {
                    	    char_literal16=(Token)match(input,81,FOLLOW_81_in_order948); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_81.add(char_literal16);

                    	    pushFollow(FOLLOW_orderRef_in_order950);
                    	    orderRef17=orderRef();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_orderRef.add(orderRef17.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);



                    // AST REWRITE
                    // elements: orderRef, ORDER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 179:5: -> ^( ORDER ( orderRef )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:179:8: ^( ORDER ( orderRef )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_ORDER.nextNode(), root_1);

                        if ( !(stream_orderRef.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_orderRef.hasNext() ) {
                            adaptor.addChild(root_1, stream_orderRef.nextTree());

                        }
                        stream_orderRef.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:180:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 180:5: -> ^( ORDER )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:180:8: ^( ORDER )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ORDER, "ORDER"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "order"

    public static class orderRef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orderRef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:1: orderRef : ( ID -> ^( LIST[\"Streams\"] ID ) | GROUP ID ( SPLIT ID )+ CLOSE_GROUP -> ^( LIST[\"Streams\"] ( ID )+ ) );
    public final StencilParser.orderRef_return orderRef() throws RecognitionException {
        StencilParser.orderRef_return retval = new StencilParser.orderRef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID18=null;
        Token GROUP19=null;
        Token ID20=null;
        Token SPLIT21=null;
        Token ID22=null;
        Token CLOSE_GROUP23=null;

        Object ID18_tree=null;
        Object GROUP19_tree=null;
        Object ID20_tree=null;
        Object SPLIT21_tree=null;
        Object ID22_tree=null;
        Object CLOSE_GROUP23_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_SPLIT=new RewriteRuleTokenStream(adaptor,"token SPLIT");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:183:3: ( ID -> ^( LIST[\"Streams\"] ID ) | GROUP ID ( SPLIT ID )+ CLOSE_GROUP -> ^( LIST[\"Streams\"] ( ID )+ ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                alt8=1;
            }
            else if ( (LA8_0==GROUP) ) {
                alt8=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:183:5: ID
                    {
                    ID18=(Token)match(input,ID,FOLLOW_ID_in_orderRef985); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID18);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 183:8: -> ^( LIST[\"Streams\"] ID )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:183:11: ^( LIST[\"Streams\"] ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Streams"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:184:5: GROUP ID ( SPLIT ID )+ CLOSE_GROUP
                    {
                    GROUP19=(Token)match(input,GROUP,FOLLOW_GROUP_in_orderRef1000); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP19);

                    ID20=(Token)match(input,ID,FOLLOW_ID_in_orderRef1002); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID20);

                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:184:14: ( SPLIT ID )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==SPLIT) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:184:15: SPLIT ID
                    	    {
                    	    SPLIT21=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_orderRef1005); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT21);

                    	    ID22=(Token)match(input,ID,FOLLOW_ID_in_orderRef1007); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_ID.add(ID22);


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);

                    CLOSE_GROUP23=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_orderRef1011); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP23);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 184:38: -> ^( LIST[\"Streams\"] ( ID )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:184:42: ^( LIST[\"Streams\"] ( ID )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Streams"), root_1);

                        if ( !(stream_ID.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_ID.hasNext() ) {
                            adaptor.addChild(root_1, stream_ID.nextNode());

                        }
                        stream_ID.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "orderRef"

    public static class externals_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "externals"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:1: externals : ( externalStream )* -> ^( LIST[\"Externals\"] ( externalStream )* ) ;
    public final StencilParser.externals_return externals() throws RecognitionException {
        StencilParser.externals_return retval = new StencilParser.externals_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.externalStream_return externalStream24 = null;


        RewriteRuleSubtreeStream stream_externalStream=new RewriteRuleSubtreeStream(adaptor,"rule externalStream");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:10: ( ( externalStream )* -> ^( LIST[\"Externals\"] ( externalStream )* ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:12: ( externalStream )*
            {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:12: ( externalStream )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==EXTERNAL) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:12: externalStream
            	    {
            	    pushFollow(FOLLOW_externalStream_in_externals1029);
            	    externalStream24=externalStream();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_externalStream.add(externalStream24.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);



            // AST REWRITE
            // elements: externalStream
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 186:28: -> ^( LIST[\"Externals\"] ( externalStream )* )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:31: ^( LIST[\"Externals\"] ( externalStream )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Externals"), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:51: ( externalStream )*
                while ( stream_externalStream.hasNext() ) {
                    adaptor.addChild(root_1, stream_externalStream.nextTree());

                }
                stream_externalStream.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "externals"

    public static class externalStream_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "externalStream"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:187:1: externalStream : EXTERNAL STREAM name= ID tuple[false] -> ^( EXTERNAL[$name.text] tuple ) ;
    public final StencilParser.externalStream_return externalStream() throws RecognitionException {
        StencilParser.externalStream_return retval = new StencilParser.externalStream_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token EXTERNAL25=null;
        Token STREAM26=null;
        StencilParser.tuple_return tuple27 = null;


        Object name_tree=null;
        Object EXTERNAL25_tree=null;
        Object STREAM26_tree=null;
        RewriteRuleTokenStream stream_EXTERNAL=new RewriteRuleTokenStream(adaptor,"token EXTERNAL");
        RewriteRuleTokenStream stream_STREAM=new RewriteRuleTokenStream(adaptor,"token STREAM");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:187:15: ( EXTERNAL STREAM name= ID tuple[false] -> ^( EXTERNAL[$name.text] tuple ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:187:17: EXTERNAL STREAM name= ID tuple[false]
            {
            EXTERNAL25=(Token)match(input,EXTERNAL,FOLLOW_EXTERNAL_in_externalStream1046); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EXTERNAL.add(EXTERNAL25);

            STREAM26=(Token)match(input,STREAM,FOLLOW_STREAM_in_externalStream1048); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM26);

            name=(Token)match(input,ID,FOLLOW_ID_in_externalStream1052); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_tuple_in_externalStream1054);
            tuple27=tuple(false);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_tuple.add(tuple27.getTree());


            // AST REWRITE
            // elements: tuple, EXTERNAL
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 187:54: -> ^( EXTERNAL[$name.text] tuple )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:187:57: ^( EXTERNAL[$name.text] tuple )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXTERNAL, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_tuple.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "externalStream"

    public static class canvasLayer_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "canvasLayer"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:192:1: canvasLayer : ( CANVAS name= ID canvasProperties ( guideDef )+ -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) ) | -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) ) );
    public final StencilParser.canvasLayer_return canvasLayer() throws RecognitionException {
        StencilParser.canvasLayer_return retval = new StencilParser.canvasLayer_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token CANVAS28=null;
        StencilParser.canvasProperties_return canvasProperties29 = null;

        StencilParser.guideDef_return guideDef30 = null;


        Object name_tree=null;
        Object CANVAS28_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CANVAS=new RewriteRuleTokenStream(adaptor,"token CANVAS");
        RewriteRuleSubtreeStream stream_canvasProperties=new RewriteRuleSubtreeStream(adaptor,"rule canvasProperties");
        RewriteRuleSubtreeStream stream_guideDef=new RewriteRuleSubtreeStream(adaptor,"rule guideDef");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:193:3: ( CANVAS name= ID canvasProperties ( guideDef )+ -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) ) | -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==CANVAS) ) {
                alt11=1;
            }
            else if ( (LA11_0==EOF||(LA11_0>=LAYER && LA11_0<=TEMPLATE)||LA11_0==PYTHON||LA11_0==STREAM) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:193:5: CANVAS name= ID canvasProperties ( guideDef )+
                    {
                    CANVAS28=(Token)match(input,CANVAS,FOLLOW_CANVAS_in_canvasLayer1077); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CANVAS.add(CANVAS28);

                    name=(Token)match(input,ID,FOLLOW_ID_in_canvasLayer1081); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_canvasProperties_in_canvasLayer1083);
                    canvasProperties29=canvasProperties();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_canvasProperties.add(canvasProperties29.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:193:37: ( guideDef )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==GUIDE) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:193:37: guideDef
                    	    {
                    	    pushFollow(FOLLOW_guideDef_in_canvasLayer1085);
                    	    guideDef30=guideDef();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_guideDef.add(guideDef30.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);



                    // AST REWRITE
                    // elements: canvasProperties, guideDef
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 194:5: -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:194:8: ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CANVAS_DEF, (name!=null?name.getText():null)), root_1);

                        adaptor.addChild(root_1, stream_canvasProperties.nextTree());
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:194:50: ^( LIST[\"Guides\"] ( guideDef )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Guides"), root_2);

                        if ( !(stream_guideDef.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_guideDef.hasNext() ) {
                            adaptor.addChild(root_2, stream_guideDef.nextTree());

                        }
                        stream_guideDef.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 195:5: -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:8: ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CANVAS_DEF, "default"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:32: ^( SPECIALIZER DEFAULT )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(DEFAULT, "DEFAULT"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:55: ^( LIST[\"Guides\"] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Guides"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "canvasLayer"

    public static class guideDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "guideDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:1: guideDef : GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )* -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) ) ;
    public final StencilParser.guideDef_return guideDef() throws RecognitionException {
        StencilParser.guideDef_return retval = new StencilParser.guideDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token type=null;
        Token layer=null;
        Token attribute=null;
        Token GUIDE31=null;
        Token FROM32=null;
        StencilParser.specializer_return spec = null;

        StencilParser.rule_return rule33 = null;


        Object type_tree=null;
        Object layer_tree=null;
        Object attribute_tree=null;
        Object GUIDE31_tree=null;
        Object FROM32_tree=null;
        RewriteRuleTokenStream stream_GUIDE=new RewriteRuleTokenStream(adaptor,"token GUIDE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_FROM=new RewriteRuleTokenStream(adaptor,"token FROM");
        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:9: ( GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )* -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:11: GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )*
            {
            GUIDE31=(Token)match(input,GUIDE,FOLLOW_GUIDE_in_guideDef1137); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GUIDE.add(GUIDE31);

            type=(Token)match(input,ID,FOLLOW_ID_in_guideDef1141); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            pushFollow(FOLLOW_specializer_in_guideDef1145);
            spec=specializer(RuleOpts.Simple);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_specializer.add(spec.getTree());
            FROM32=(Token)match(input,FROM,FOLLOW_FROM_in_guideDef1148); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FROM.add(FROM32);

            layer=(Token)match(input,ID,FOLLOW_ID_in_guideDef1152); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(layer);

            attribute=(Token)match(input,ID,FOLLOW_ID_in_guideDef1156); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(attribute);

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:86: ( rule[\"glyph\"] )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==CANVAS||LA12_0==GLYPH||LA12_0==LOCAL||LA12_0==RETURN||LA12_0==VIEW||LA12_0==GROUP||LA12_0==ID) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:86: rule[\"glyph\"]
            	    {
            	    pushFollow(FOLLOW_rule_in_guideDef1158);
            	    rule33=rule("glyph");

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_rule.add(rule33.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);



            // AST REWRITE
            // elements: layer, type, rule, spec, GUIDE
            // token labels: layer, type
            // rule labels: spec, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_layer=new RewriteRuleTokenStream(adaptor,"token layer",layer);
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_spec=new RewriteRuleSubtreeStream(adaptor,"rule spec",spec!=null?spec.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 198:4: -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:7: ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(GUIDE, (attribute!=null?attribute.getText():null)), root_1);

                adaptor.addChild(root_1, stream_layer.nextNode());
                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_spec.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:51: ^( LIST[\"Rules\"] ( rule )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Rules"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:67: ( rule )*
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_2, stream_rule.nextTree());

                }
                stream_rule.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "guideDef"

    public static class canvasProperties_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "canvasProperties"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:200:1: canvasProperties : specializer[RuleOpts.Simple] ;
    public final StencilParser.canvasProperties_return canvasProperties() throws RecognitionException {
        StencilParser.canvasProperties_return retval = new StencilParser.canvasProperties_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.specializer_return specializer34 = null;



        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:200:17: ( specializer[RuleOpts.Simple] )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:200:19: specializer[RuleOpts.Simple]
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_specializer_in_canvasProperties1195);
            specializer34=specializer(RuleOpts.Simple);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, specializer34.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "canvasProperties"

    public static class streamDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "streamDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:204:1: streamDef : STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+ -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )* ;
    public final StencilParser.streamDef_return streamDef() throws RecognitionException {
        StencilParser.streamDef_return retval = new StencilParser.streamDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token STREAM35=null;
        StencilParser.tuple_return tuple36 = null;

        StencilParser.consumesBlock_return consumesBlock37 = null;


        Object name_tree=null;
        Object STREAM35_tree=null;
        RewriteRuleTokenStream stream_STREAM=new RewriteRuleTokenStream(adaptor,"token STREAM");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        RewriteRuleSubtreeStream stream_consumesBlock=new RewriteRuleSubtreeStream(adaptor,"rule consumesBlock");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:3: ( STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+ -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )* )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:5: STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+
            {
            STREAM35=(Token)match(input,STREAM,FOLLOW_STREAM_in_streamDef1209); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM35);

            name=(Token)match(input,ID,FOLLOW_ID_in_streamDef1213); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_tuple_in_streamDef1215);
            tuple36=tuple(true);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_tuple.add(tuple36.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:33: ( consumesBlock[\"return\"] )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==FROM) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:34: consumesBlock[\"return\"]
            	    {
            	    pushFollow(FOLLOW_consumesBlock_in_streamDef1220);
            	    consumesBlock37=consumesBlock("return");

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_consumesBlock.add(consumesBlock37.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);



            // AST REWRITE
            // elements: consumesBlock, tuple, STREAM
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 206:5: -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )*
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:8: ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )*
                while ( stream_tuple.hasNext()||stream_STREAM.hasNext() ) {
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:8: ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STREAM, (name!=null?name.getText():null)), root_1);

                    adaptor.addChild(root_1, stream_tuple.nextTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:35: ^( LIST[\"Consumes\"] ( consumesBlock )+ )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Consumes"), root_2);

                    if ( !(stream_consumesBlock.hasNext()) ) {
                        throw new RewriteEarlyExitException();
                    }
                    while ( stream_consumesBlock.hasNext() ) {
                        adaptor.addChild(root_2, stream_consumesBlock.nextTree());

                    }
                    stream_consumesBlock.reset();

                    adaptor.addChild(root_1, root_2);
                    }

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_tuple.reset();
                stream_STREAM.reset();

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "streamDef"

    public static class layerDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "layerDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:1: layerDef : LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+ -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) ;
    public final StencilParser.layerDef_return layerDef() throws RecognitionException {
        StencilParser.layerDef_return retval = new StencilParser.layerDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token LAYER38=null;
        StencilParser.implantationDef_return implantationDef39 = null;

        StencilParser.consumesBlock_return consumesBlock40 = null;


        Object name_tree=null;
        Object LAYER38_tree=null;
        RewriteRuleTokenStream stream_LAYER=new RewriteRuleTokenStream(adaptor,"token LAYER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_consumesBlock=new RewriteRuleSubtreeStream(adaptor,"rule consumesBlock");
        RewriteRuleSubtreeStream stream_implantationDef=new RewriteRuleSubtreeStream(adaptor,"rule implantationDef");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:3: ( LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+ -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:5: LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+
            {
            LAYER38=(Token)match(input,LAYER,FOLLOW_LAYER_in_layerDef1255); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LAYER.add(LAYER38);

            name=(Token)match(input,ID,FOLLOW_ID_in_layerDef1259); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_implantationDef_in_layerDef1261);
            implantationDef39=implantationDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_implantationDef.add(implantationDef39.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:35: ( consumesBlock[\"glyph\"] )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==FROM) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:35: consumesBlock[\"glyph\"]
            	    {
            	    pushFollow(FOLLOW_consumesBlock_in_layerDef1263);
            	    consumesBlock40=consumesBlock("glyph");

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_consumesBlock.add(consumesBlock40.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);



            // AST REWRITE
            // elements: consumesBlock, LAYER, implantationDef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 210:5: -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:210:8: ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LAYER, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_implantationDef.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:210:44: ^( LIST[\"Consumes\"] ( consumesBlock )+ )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Consumes"), root_2);

                if ( !(stream_consumesBlock.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_consumesBlock.hasNext() ) {
                    adaptor.addChild(root_2, stream_consumesBlock.nextTree());

                }
                stream_consumesBlock.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "layerDef"

    public static class implantationDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "implantationDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:212:1: implantationDef : ( ARG type= ID CLOSE_ARG -> GLYPH[$type.text] | -> GLYPH[DEFAULT_GLYPH_TYPE] );
    public final StencilParser.implantationDef_return implantationDef() throws RecognitionException {
        StencilParser.implantationDef_return retval = new StencilParser.implantationDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token type=null;
        Token ARG41=null;
        Token CLOSE_ARG42=null;

        Object type_tree=null;
        Object ARG41_tree=null;
        Object CLOSE_ARG42_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:213:3: ( ARG type= ID CLOSE_ARG -> GLYPH[$type.text] | -> GLYPH[DEFAULT_GLYPH_TYPE] )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==ARG) ) {
                alt15=1;
            }
            else if ( (LA15_0==FROM) ) {
                alt15=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:213:5: ARG type= ID CLOSE_ARG
                    {
                    ARG41=(Token)match(input,ARG,FOLLOW_ARG_in_implantationDef1298); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG41);

                    type=(Token)match(input,ID,FOLLOW_ID_in_implantationDef1302); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(type);

                    CLOSE_ARG42=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_implantationDef1304); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG42);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 213:27: -> GLYPH[$type.text]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(GLYPH, (type!=null?type.getText():null)));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:214:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 214:5: -> GLYPH[DEFAULT_GLYPH_TYPE]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(GLYPH, DEFAULT_GLYPH_TYPE));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "implantationDef"

    public static class consumesBlock_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "consumesBlock"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:216:1: consumesBlock[String def] : FROM stream= ID ( filterRule )* ( rule[def] )+ -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) ) ;
    public final StencilParser.consumesBlock_return consumesBlock(String def) throws RecognitionException {
        StencilParser.consumesBlock_return retval = new StencilParser.consumesBlock_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token stream=null;
        Token FROM43=null;
        StencilParser.filterRule_return filterRule44 = null;

        StencilParser.rule_return rule45 = null;


        Object stream_tree=null;
        Object FROM43_tree=null;
        RewriteRuleTokenStream stream_FROM=new RewriteRuleTokenStream(adaptor,"token FROM");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        RewriteRuleSubtreeStream stream_filterRule=new RewriteRuleSubtreeStream(adaptor,"rule filterRule");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:3: ( FROM stream= ID ( filterRule )* ( rule[def] )+ -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:5: FROM stream= ID ( filterRule )* ( rule[def] )+
            {
            FROM43=(Token)match(input,FROM,FOLLOW_FROM_in_consumesBlock1333); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FROM.add(FROM43);

            stream=(Token)match(input,ID,FOLLOW_ID_in_consumesBlock1337); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(stream);

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:20: ( filterRule )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==FILTER) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:20: filterRule
            	    {
            	    pushFollow(FOLLOW_filterRule_in_consumesBlock1339);
            	    filterRule44=filterRule();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_filterRule.add(filterRule44.getTree());

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:32: ( rule[def] )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==CANVAS||LA17_0==GLYPH||LA17_0==LOCAL||LA17_0==RETURN||LA17_0==VIEW||LA17_0==GROUP||LA17_0==ID) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:32: rule[def]
            	    {
            	    pushFollow(FOLLOW_rule_in_consumesBlock1342);
            	    rule45=rule(def);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_rule.add(rule45.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt17 >= 1 ) break loop17;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
            } while (true);



            // AST REWRITE
            // elements: rule, filterRule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 218:5: -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:218:8: ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CONSUMES, (stream!=null?stream.getText():null)), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:218:33: ^( LIST[\"Filters\"] ( filterRule )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Filters"), root_2);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:218:51: ( filterRule )*
                while ( stream_filterRule.hasNext() ) {
                    adaptor.addChild(root_2, stream_filterRule.nextTree());

                }
                stream_filterRule.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:218:64: ^( LIST[\"Rules\"] ( rule )+ )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Rules"), root_2);

                if ( !(stream_rule.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_2, stream_rule.nextTree());

                }
                stream_rule.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "consumesBlock"

    public static class filterRule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "filterRule"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:220:1: filterRule : FILTER rulePredicate DEFINE callGroup -> ^( FILTER rulePredicate callGroup ) ;
    public final StencilParser.filterRule_return filterRule() throws RecognitionException {
        StencilParser.filterRule_return retval = new StencilParser.filterRule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FILTER46=null;
        Token DEFINE48=null;
        StencilParser.rulePredicate_return rulePredicate47 = null;

        StencilParser.callGroup_return callGroup49 = null;


        Object FILTER46_tree=null;
        Object DEFINE48_tree=null;
        RewriteRuleTokenStream stream_DEFINE=new RewriteRuleTokenStream(adaptor,"token DEFINE");
        RewriteRuleTokenStream stream_FILTER=new RewriteRuleTokenStream(adaptor,"token FILTER");
        RewriteRuleSubtreeStream stream_callGroup=new RewriteRuleSubtreeStream(adaptor,"rule callGroup");
        RewriteRuleSubtreeStream stream_rulePredicate=new RewriteRuleSubtreeStream(adaptor,"rule rulePredicate");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:221:3: ( FILTER rulePredicate DEFINE callGroup -> ^( FILTER rulePredicate callGroup ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:221:5: FILTER rulePredicate DEFINE callGroup
            {
            FILTER46=(Token)match(input,FILTER,FOLLOW_FILTER_in_filterRule1382); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FILTER.add(FILTER46);

            pushFollow(FOLLOW_rulePredicate_in_filterRule1384);
            rulePredicate47=rulePredicate();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_rulePredicate.add(rulePredicate47.getTree());
            DEFINE48=(Token)match(input,DEFINE,FOLLOW_DEFINE_in_filterRule1386); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DEFINE.add(DEFINE48);

            pushFollow(FOLLOW_callGroup_in_filterRule1388);
            callGroup49=callGroup();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callGroup.add(callGroup49.getTree());


            // AST REWRITE
            // elements: FILTER, callGroup, rulePredicate
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 222:5: -> ^( FILTER rulePredicate callGroup )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:222:8: ^( FILTER rulePredicate callGroup )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_FILTER.nextNode(), root_1);

                adaptor.addChild(root_1, stream_rulePredicate.nextTree());
                adaptor.addChild(root_1, stream_callGroup.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "filterRule"

    public static class rulePredicate_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rulePredicate"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:224:1: rulePredicate : ( GROUP ALL CLOSE_GROUP -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) );
    public final StencilParser.rulePredicate_return rulePredicate() throws RecognitionException {
        StencilParser.rulePredicate_return retval = new StencilParser.rulePredicate_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP50=null;
        Token ALL51=null;
        Token CLOSE_GROUP52=null;
        Token GROUP53=null;
        Token SEPARATOR57=null;
        Token CLOSE_GROUP61=null;
        StencilParser.value_return value54 = null;

        StencilParser.booleanOp_return booleanOp55 = null;

        StencilParser.value_return value56 = null;

        StencilParser.value_return value58 = null;

        StencilParser.booleanOp_return booleanOp59 = null;

        StencilParser.value_return value60 = null;


        Object GROUP50_tree=null;
        Object ALL51_tree=null;
        Object CLOSE_GROUP52_tree=null;
        Object GROUP53_tree=null;
        Object SEPARATOR57_tree=null;
        Object CLOSE_GROUP61_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_booleanOp=new RewriteRuleSubtreeStream(adaptor,"rule booleanOp");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:225:3: ( GROUP ALL CLOSE_GROUP -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==GROUP) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==ALL) ) {
                    int LA19_2 = input.LA(3);

                    if ( (LA19_2==CLOSE_GROUP) ) {
                        alt19=1;
                    }
                    else if ( (LA19_2==81||LA19_2==83||(LA19_2>=86 && LA19_2<=91)) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA19_1==DEFAULT||LA19_1==ARG||LA19_1==NAMESPLIT||LA19_1==ID||(LA19_1>=TAGGED_ID && LA19_1<=DIGITS)||LA19_1==85||(LA19_1>=92 && LA19_1<=93)) ) {
                    alt19=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:225:5: GROUP ALL CLOSE_GROUP
                    {
                    GROUP50=(Token)match(input,GROUP,FOLLOW_GROUP_in_rulePredicate1412); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP50);

                    ALL51=(Token)match(input,ALL,FOLLOW_ALL_in_rulePredicate1414); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALL.add(ALL51);

                    CLOSE_GROUP52=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_rulePredicate1416); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP52);



                    // AST REWRITE
                    // elements: ALL
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 226:5: -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:226:8: ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:226:29: ^( PREDICATE ALL )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(PREDICATE, "PREDICATE"), root_2);

                        adaptor.addChild(root_2, stream_ALL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:227:5: GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP
                    {
                    GROUP53=(Token)match(input,GROUP,FOLLOW_GROUP_in_rulePredicate1439); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP53);

                    pushFollow(FOLLOW_value_in_rulePredicate1441);
                    value54=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value54.getTree());
                    pushFollow(FOLLOW_booleanOp_in_rulePredicate1443);
                    booleanOp55=booleanOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp55.getTree());
                    pushFollow(FOLLOW_value_in_rulePredicate1445);
                    value56=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value56.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:227:33: ( SEPARATOR value booleanOp value )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==SEPARATOR) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:227:34: SEPARATOR value booleanOp value
                    	    {
                    	    SEPARATOR57=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_rulePredicate1448); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR57);

                    	    pushFollow(FOLLOW_value_in_rulePredicate1450);
                    	    value58=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value58.getTree());
                    	    pushFollow(FOLLOW_booleanOp_in_rulePredicate1452);
                    	    booleanOp59=booleanOp();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp59.getTree());
                    	    pushFollow(FOLLOW_value_in_rulePredicate1454);
                    	    value60=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value60.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);

                    CLOSE_GROUP61=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_rulePredicate1458); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP61);



                    // AST REWRITE
                    // elements: value, booleanOp, value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 228:5: -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:8: ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        if ( !(stream_value.hasNext()||stream_booleanOp.hasNext()||stream_value.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_value.hasNext()||stream_booleanOp.hasNext()||stream_value.hasNext() ) {
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:29: ^( PREDICATE value booleanOp value )
                            {
                            Object root_2 = (Object)adaptor.nil();
                            root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(PREDICATE, "PREDICATE"), root_2);

                            adaptor.addChild(root_2, stream_value.nextTree());
                            adaptor.addChild(root_2, stream_booleanOp.nextTree());
                            adaptor.addChild(root_2, stream_value.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_value.reset();
                        stream_booleanOp.reset();
                        stream_value.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rulePredicate"

    public static class operatorTemplate_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "operatorTemplate"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:234:1: operatorTemplate : TEMPLATE OPERATOR name= ID -> ^( OPERATOR_TEMPLATE[$name.text] ) ;
    public final StencilParser.operatorTemplate_return operatorTemplate() throws RecognitionException {
        StencilParser.operatorTemplate_return retval = new StencilParser.operatorTemplate_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token TEMPLATE62=null;
        Token OPERATOR63=null;

        Object name_tree=null;
        Object TEMPLATE62_tree=null;
        Object OPERATOR63_tree=null;
        RewriteRuleTokenStream stream_TEMPLATE=new RewriteRuleTokenStream(adaptor,"token TEMPLATE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:234:18: ( TEMPLATE OPERATOR name= ID -> ^( OPERATOR_TEMPLATE[$name.text] ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:234:20: TEMPLATE OPERATOR name= ID
            {
            TEMPLATE62=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_operatorTemplate1492); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TEMPLATE.add(TEMPLATE62);

            OPERATOR63=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorTemplate1494); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR63);

            name=(Token)match(input,ID,FOLLOW_ID_in_operatorTemplate1498); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 234:46: -> ^( OPERATOR_TEMPLATE[$name.text] )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:234:49: ^( OPERATOR_TEMPLATE[$name.text] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR_TEMPLATE, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "operatorTemplate"

    public static class operatorDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "operatorDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:236:1: operatorDef : ( OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+ -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) ) | OPERATOR name= ID BASE base= ID specializer[RuleOpts.All] -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer ) );
    public final StencilParser.operatorDef_return operatorDef() throws RecognitionException {
        StencilParser.operatorDef_return retval = new StencilParser.operatorDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token base=null;
        Token OPERATOR64=null;
        Token YIELDS66=null;
        Token OPERATOR69=null;
        Token BASE70=null;
        StencilParser.tuple_return tuple65 = null;

        StencilParser.tuple_return tuple67 = null;

        StencilParser.operatorRule_return operatorRule68 = null;

        StencilParser.specializer_return specializer71 = null;


        Object name_tree=null;
        Object base_tree=null;
        Object OPERATOR64_tree=null;
        Object YIELDS66_tree=null;
        Object OPERATOR69_tree=null;
        Object BASE70_tree=null;
        RewriteRuleTokenStream stream_YIELDS=new RewriteRuleTokenStream(adaptor,"token YIELDS");
        RewriteRuleTokenStream stream_BASE=new RewriteRuleTokenStream(adaptor,"token BASE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        RewriteRuleSubtreeStream stream_operatorRule=new RewriteRuleSubtreeStream(adaptor,"rule operatorRule");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:3: ( OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+ -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) ) | OPERATOR name= ID BASE base= ID specializer[RuleOpts.All] -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer ) )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==OPERATOR) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==ID) ) {
                    int LA21_2 = input.LA(3);

                    if ( (LA21_2==BASE) ) {
                        alt21=2;
                    }
                    else if ( (LA21_2==GROUP||LA21_2==ID) ) {
                        alt21=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:5: OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+
                    {
                    OPERATOR64=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorDef1517); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR64);

                    name=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1522); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_tuple_in_operatorDef1524);
                    tuple65=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple65.getTree());
                    YIELDS66=(Token)match(input,YIELDS,FOLLOW_YIELDS_in_operatorDef1527); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_YIELDS.add(YIELDS66);

                    pushFollow(FOLLOW_tuple_in_operatorDef1529);
                    tuple67=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple67.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:56: ( operatorRule )+
                    int cnt20=0;
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==ALL||LA20_0==GROUP) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:56: operatorRule
                    	    {
                    	    pushFollow(FOLLOW_operatorRule_in_operatorDef1532);
                    	    operatorRule68=operatorRule();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_operatorRule.add(operatorRule68.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt20 >= 1 ) break loop20;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(20, input);
                                throw eee;
                        }
                        cnt20++;
                    } while (true);



                    // AST REWRITE
                    // elements: tuple, YIELDS, OPERATOR, operatorRule, tuple
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 238:5: -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:9: ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, (name!=null?name.getText():null)), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:32: ^( YIELDS tuple tuple )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(stream_YIELDS.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_tuple.nextTree());
                        adaptor.addChild(root_2, stream_tuple.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:54: ^( LIST[\"Rules\"] ( operatorRule )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Rules"), root_2);

                        if ( !(stream_operatorRule.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_operatorRule.hasNext() ) {
                            adaptor.addChild(root_2, stream_operatorRule.nextTree());

                        }
                        stream_operatorRule.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:239:5: OPERATOR name= ID BASE base= ID specializer[RuleOpts.All]
                    {
                    OPERATOR69=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorDef1567); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR69);

                    name=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1571); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    BASE70=(Token)match(input,BASE,FOLLOW_BASE_in_operatorDef1573); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BASE.add(BASE70);

                    base=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1577); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(base);

                    pushFollow(FOLLOW_specializer_in_operatorDef1579);
                    specializer71=specializer(RuleOpts.All);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_specializer.add(specializer71.getTree());


                    // AST REWRITE
                    // elements: specializer
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 240:5: -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:240:8: ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR_REFERENCE, (name!=null?name.getText():null)), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(OPERATOR_BASE, (base!=null?base.getText():null)));
                        adaptor.addChild(root_1, stream_specializer.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "operatorDef"

    public static class operatorRule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "operatorRule"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:254:1: operatorRule : predicate GATE ( rule[\"return\"] )+ -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) ) ;
    public final StencilParser.operatorRule_return operatorRule() throws RecognitionException {
        StencilParser.operatorRule_return retval = new StencilParser.operatorRule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GATE73=null;
        StencilParser.predicate_return predicate72 = null;

        StencilParser.rule_return rule74 = null;


        Object GATE73_tree=null;
        RewriteRuleTokenStream stream_GATE=new RewriteRuleTokenStream(adaptor,"token GATE");
        RewriteRuleSubtreeStream stream_predicate=new RewriteRuleSubtreeStream(adaptor,"rule predicate");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:255:3: ( predicate GATE ( rule[\"return\"] )+ -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:255:5: predicate GATE ( rule[\"return\"] )+
            {
            pushFollow(FOLLOW_predicate_in_operatorRule1623);
            predicate72=predicate();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_predicate.add(predicate72.getTree());
            GATE73=(Token)match(input,GATE,FOLLOW_GATE_in_operatorRule1625); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GATE.add(GATE73);

            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:255:20: ( rule[\"return\"] )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==GROUP) ) {
                    int LA22_2 = input.LA(2);

                    if ( (LA22_2==CLOSE_GROUP) ) {
                        alt22=1;
                    }
                    else if ( (LA22_2==ID) ) {
                        int LA22_4 = input.LA(3);

                        if ( (LA22_4==CLOSE_GROUP||LA22_4==SEPARATOR) ) {
                            alt22=1;
                        }


                    }


                }
                else if ( (LA22_0==CANVAS||LA22_0==GLYPH||LA22_0==LOCAL||LA22_0==RETURN||LA22_0==VIEW||LA22_0==ID) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:255:20: rule[\"return\"]
            	    {
            	    pushFollow(FOLLOW_rule_in_operatorRule1627);
            	    rule74=rule("return");

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_rule.add(rule74.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);



            // AST REWRITE
            // elements: rule, predicate
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 256:5: -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:256:8: ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR_RULE, "OPERATOR_RULE"), root_1);

                adaptor.addChild(root_1, stream_predicate.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:256:34: ^( LIST[\"Rules\"] ( rule )+ )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Rules"), root_2);

                if ( !(stream_rule.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_rule.hasNext() ) {
                    adaptor.addChild(root_2, stream_rule.nextTree());

                }
                stream_rule.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "operatorRule"

    public static class predicate_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "predicate"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:258:1: predicate : ( ( GROUP )? ALL ( CLOSE_GROUP )? -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) );
    public final StencilParser.predicate_return predicate() throws RecognitionException {
        StencilParser.predicate_return retval = new StencilParser.predicate_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP75=null;
        Token ALL76=null;
        Token CLOSE_GROUP77=null;
        Token GROUP78=null;
        Token SEPARATOR82=null;
        Token CLOSE_GROUP86=null;
        StencilParser.value_return value79 = null;

        StencilParser.booleanOp_return booleanOp80 = null;

        StencilParser.value_return value81 = null;

        StencilParser.value_return value83 = null;

        StencilParser.booleanOp_return booleanOp84 = null;

        StencilParser.value_return value85 = null;


        Object GROUP75_tree=null;
        Object ALL76_tree=null;
        Object CLOSE_GROUP77_tree=null;
        Object GROUP78_tree=null;
        Object SEPARATOR82_tree=null;
        Object CLOSE_GROUP86_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_booleanOp=new RewriteRuleSubtreeStream(adaptor,"rule booleanOp");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:3: ( ( GROUP )? ALL ( CLOSE_GROUP )? -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==GROUP) ) {
                int LA26_1 = input.LA(2);

                if ( (LA26_1==DEFAULT||LA26_1==ARG||LA26_1==NAMESPLIT||LA26_1==ID||(LA26_1>=TAGGED_ID && LA26_1<=DIGITS)||LA26_1==85||(LA26_1>=92 && LA26_1<=93)) ) {
                    alt26=2;
                }
                else if ( (LA26_1==ALL) ) {
                    int LA26_4 = input.LA(3);

                    if ( (LA26_4==CLOSE_GROUP||LA26_4==GATE) ) {
                        alt26=1;
                    }
                    else if ( (LA26_4==81||LA26_4==83||(LA26_4>=86 && LA26_4<=91)) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 4, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA26_0==ALL) ) {
                alt26=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:5: ( GROUP )? ALL ( CLOSE_GROUP )?
                    {
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:5: ( GROUP )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==GROUP) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:5: GROUP
                            {
                            GROUP75=(Token)match(input,GROUP,FOLLOW_GROUP_in_predicate1659); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GROUP.add(GROUP75);


                            }
                            break;

                    }

                    ALL76=(Token)match(input,ALL,FOLLOW_ALL_in_predicate1662); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALL.add(ALL76);

                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:16: ( CLOSE_GROUP )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==CLOSE_GROUP) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:259:16: CLOSE_GROUP
                            {
                            CLOSE_GROUP77=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_predicate1664); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP77);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: ALL
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 260:5: -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:260:8: ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:260:29: ^( PREDICATE ALL )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(PREDICATE, "PREDICATE"), root_2);

                        adaptor.addChild(root_2, stream_ALL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:261:5: GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP
                    {
                    GROUP78=(Token)match(input,GROUP,FOLLOW_GROUP_in_predicate1688); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP78);

                    pushFollow(FOLLOW_value_in_predicate1690);
                    value79=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value79.getTree());
                    pushFollow(FOLLOW_booleanOp_in_predicate1692);
                    booleanOp80=booleanOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp80.getTree());
                    pushFollow(FOLLOW_value_in_predicate1694);
                    value81=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value81.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:261:33: ( SEPARATOR value booleanOp value )*
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);

                        if ( (LA25_0==SEPARATOR) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:261:34: SEPARATOR value booleanOp value
                    	    {
                    	    SEPARATOR82=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_predicate1697); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR82);

                    	    pushFollow(FOLLOW_value_in_predicate1699);
                    	    value83=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value83.getTree());
                    	    pushFollow(FOLLOW_booleanOp_in_predicate1701);
                    	    booleanOp84=booleanOp();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp84.getTree());
                    	    pushFollow(FOLLOW_value_in_predicate1703);
                    	    value85=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value85.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop25;
                        }
                    } while (true);

                    CLOSE_GROUP86=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_predicate1707); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP86);



                    // AST REWRITE
                    // elements: booleanOp, value, value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 262:5: -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:262:8: ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        if ( !(stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext() ) {
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:262:29: ^( PREDICATE value booleanOp value )
                            {
                            Object root_2 = (Object)adaptor.nil();
                            root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(PREDICATE, "PREDICATE"), root_2);

                            adaptor.addChild(root_2, stream_value.nextTree());
                            adaptor.addChild(root_2, stream_booleanOp.nextTree());
                            adaptor.addChild(root_2, stream_value.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_booleanOp.reset();
                        stream_value.reset();
                        stream_value.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "predicate"

    public static class rule_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rule"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:269:1: rule[String def] : target[def] ( DEFINE | DYNAMIC ) callGroup -> ^( RULE target callGroup ( DEFINE )? ( DYNAMIC )? ) ;
    public final StencilParser.rule_return rule(String def) throws RecognitionException {
        StencilParser.rule_return retval = new StencilParser.rule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DEFINE88=null;
        Token DYNAMIC89=null;
        StencilParser.target_return target87 = null;

        StencilParser.callGroup_return callGroup90 = null;


        Object DEFINE88_tree=null;
        Object DYNAMIC89_tree=null;
        RewriteRuleTokenStream stream_DEFINE=new RewriteRuleTokenStream(adaptor,"token DEFINE");
        RewriteRuleTokenStream stream_DYNAMIC=new RewriteRuleTokenStream(adaptor,"token DYNAMIC");
        RewriteRuleSubtreeStream stream_callGroup=new RewriteRuleSubtreeStream(adaptor,"rule callGroup");
        RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"rule target");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:3: ( target[def] ( DEFINE | DYNAMIC ) callGroup -> ^( RULE target callGroup ( DEFINE )? ( DYNAMIC )? ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:5: target[def] ( DEFINE | DYNAMIC ) callGroup
            {
            pushFollow(FOLLOW_target_in_rule1745);
            target87=target(def);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_target.add(target87.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:17: ( DEFINE | DYNAMIC )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==DEFINE) ) {
                alt27=1;
            }
            else if ( (LA27_0==DYNAMIC) ) {
                alt27=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:18: DEFINE
                    {
                    DEFINE88=(Token)match(input,DEFINE,FOLLOW_DEFINE_in_rule1749); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DEFINE.add(DEFINE88);


                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:27: DYNAMIC
                    {
                    DYNAMIC89=(Token)match(input,DYNAMIC,FOLLOW_DYNAMIC_in_rule1753); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DYNAMIC.add(DYNAMIC89);


                    }
                    break;

            }

            pushFollow(FOLLOW_callGroup_in_rule1756);
            callGroup90=callGroup();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callGroup.add(callGroup90.getTree());


            // AST REWRITE
            // elements: target, DEFINE, callGroup, DYNAMIC
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 271:5: -> ^( RULE target callGroup ( DEFINE )? ( DYNAMIC )? )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:271:8: ^( RULE target callGroup ( DEFINE )? ( DYNAMIC )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_target.nextTree());
                adaptor.addChild(root_1, stream_callGroup.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:271:32: ( DEFINE )?
                if ( stream_DEFINE.hasNext() ) {
                    adaptor.addChild(root_1, stream_DEFINE.nextNode());

                }
                stream_DEFINE.reset();
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:271:40: ( DYNAMIC )?
                if ( stream_DYNAMIC.hasNext() ) {
                    adaptor.addChild(root_1, stream_DYNAMIC.nextNode());

                }
                stream_DYNAMIC.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rule"

    public static class callGroup_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callGroup"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:273:1: callGroup : ( ( callChain SPLIT )=> callChain ( SPLIT callChain )+ JOIN callChain -> ^( CALL_GROUP ( callChain )+ ) | callChain -> ^( CALL_GROUP callChain ) );
    public final StencilParser.callGroup_return callGroup() throws RecognitionException {
        StencilParser.callGroup_return retval = new StencilParser.callGroup_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SPLIT92=null;
        Token JOIN94=null;
        StencilParser.callChain_return callChain91 = null;

        StencilParser.callChain_return callChain93 = null;

        StencilParser.callChain_return callChain95 = null;

        StencilParser.callChain_return callChain96 = null;


        Object SPLIT92_tree=null;
        Object JOIN94_tree=null;
        RewriteRuleTokenStream stream_JOIN=new RewriteRuleTokenStream(adaptor,"token JOIN");
        RewriteRuleTokenStream stream_SPLIT=new RewriteRuleTokenStream(adaptor,"token SPLIT");
        RewriteRuleSubtreeStream stream_callChain=new RewriteRuleSubtreeStream(adaptor,"rule callChain");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:3: ( ( callChain SPLIT )=> callChain ( SPLIT callChain )+ JOIN callChain -> ^( CALL_GROUP ( callChain )+ ) | callChain -> ^( CALL_GROUP callChain ) )
            int alt29=2;
            alt29 = dfa29.predict(input);
            switch (alt29) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:5: ( callChain SPLIT )=> callChain ( SPLIT callChain )+ JOIN callChain
                    {
                    pushFollow(FOLLOW_callChain_in_callGroup1793);
                    callChain91=callChain();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callChain.add(callChain91.getTree());
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:35: ( SPLIT callChain )+
                    int cnt28=0;
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( (LA28_0==SPLIT) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:36: SPLIT callChain
                    	    {
                    	    SPLIT92=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_callGroup1796); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT92);

                    	    pushFollow(FOLLOW_callChain_in_callGroup1798);
                    	    callChain93=callChain();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_callChain.add(callChain93.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt28 >= 1 ) break loop28;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(28, input);
                                throw eee;
                        }
                        cnt28++;
                    } while (true);

                    JOIN94=(Token)match(input,JOIN,FOLLOW_JOIN_in_callGroup1802); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_JOIN.add(JOIN94);

                    pushFollow(FOLLOW_callChain_in_callGroup1804);
                    callChain95=callChain();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callChain.add(callChain95.getTree());


                    // AST REWRITE
                    // elements: callChain
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 275:5: -> ^( CALL_GROUP ( callChain )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:275:8: ^( CALL_GROUP ( callChain )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL_GROUP, "CALL_GROUP"), root_1);

                        if ( !(stream_callChain.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_callChain.hasNext() ) {
                            adaptor.addChild(root_1, stream_callChain.nextTree());

                        }
                        stream_callChain.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:276:5: callChain
                    {
                    pushFollow(FOLLOW_callChain_in_callGroup1823);
                    callChain96=callChain();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callChain.add(callChain96.getTree());


                    // AST REWRITE
                    // elements: callChain
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 277:5: -> ^( CALL_GROUP callChain )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:277:8: ^( CALL_GROUP callChain )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL_GROUP, "CALL_GROUP"), root_1);

                        adaptor.addChild(root_1, stream_callChain.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "callGroup"

    public static class callChain_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callChain"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:279:1: callChain : callTarget -> ^( CALL_CHAIN callTarget ) ;
    public final StencilParser.callChain_return callChain() throws RecognitionException {
        StencilParser.callChain_return retval = new StencilParser.callChain_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.callTarget_return callTarget97 = null;


        RewriteRuleSubtreeStream stream_callTarget=new RewriteRuleSubtreeStream(adaptor,"rule callTarget");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:279:10: ( callTarget -> ^( CALL_CHAIN callTarget ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:279:12: callTarget
            {
            pushFollow(FOLLOW_callTarget_in_callChain1842);
            callTarget97=callTarget();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callTarget.add(callTarget97.getTree());


            // AST REWRITE
            // elements: callTarget
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 279:23: -> ^( CALL_CHAIN callTarget )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:279:26: ^( CALL_CHAIN callTarget )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL_CHAIN, "CALL_CHAIN"), root_1);

                adaptor.addChild(root_1, stream_callTarget.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "callChain"

    public static class callTarget_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callTarget"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:1: callTarget : ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget );
    public final StencilParser.callTarget_return callTarget() throws RecognitionException {
        StencilParser.callTarget_return retval = new StencilParser.callTarget_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.value_return value98 = null;

        StencilParser.emptySet_return emptySet99 = null;

        StencilParser.valueList_return valueList100 = null;

        StencilParser.functionCallTarget_return functionCallTarget101 = null;


        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_valueList=new RewriteRuleSubtreeStream(adaptor,"rule valueList");
        RewriteRuleSubtreeStream stream_emptySet=new RewriteRuleSubtreeStream(adaptor,"rule emptySet");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:3: ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget )
            int alt30=4;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:5: value
                    {
                    pushFollow(FOLLOW_value_in_callTarget1860);
                    value98=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value98.getTree());


                    // AST REWRITE
                    // elements: value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 282:11: -> ^( PACK value )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:14: ^( PACK value )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PACK, "PACK"), root_1);

                        adaptor.addChild(root_1, stream_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:283:5: emptySet
                    {
                    pushFollow(FOLLOW_emptySet_in_callTarget1874);
                    emptySet99=emptySet();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_emptySet.add(emptySet99.getTree());


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 283:14: -> ^( PACK )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:283:17: ^( PACK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PACK, "PACK"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:5: valueList
                    {
                    pushFollow(FOLLOW_valueList_in_callTarget1886);
                    valueList100=valueList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueList.add(valueList100.getTree());


                    // AST REWRITE
                    // elements: valueList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 284:15: -> ^( PACK valueList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:18: ^( PACK valueList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PACK, "PACK"), root_1);

                        adaptor.addChild(root_1, stream_valueList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:285:5: functionCallTarget
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functionCallTarget_in_callTarget1900);
                    functionCallTarget101=functionCallTarget();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionCallTarget101.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "callTarget"

    public static class functionCallTarget_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionCallTarget"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:287:1: functionCallTarget : ( ( functionCall passOp )=>f1= functionCall passOp f2= callTarget -> ^( $f1 passOp $f2) | f1= functionCall -> ^( $f1 YIELDS ^( PACK DEFAULT ) ) );
    public final StencilParser.functionCallTarget_return functionCallTarget() throws RecognitionException {
        StencilParser.functionCallTarget_return retval = new StencilParser.functionCallTarget_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.functionCall_return f1 = null;

        StencilParser.callTarget_return f2 = null;

        StencilParser.passOp_return passOp102 = null;


        RewriteRuleSubtreeStream stream_functionCall=new RewriteRuleSubtreeStream(adaptor,"rule functionCall");
        RewriteRuleSubtreeStream stream_passOp=new RewriteRuleSubtreeStream(adaptor,"rule passOp");
        RewriteRuleSubtreeStream stream_callTarget=new RewriteRuleSubtreeStream(adaptor,"rule callTarget");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:3: ( ( functionCall passOp )=>f1= functionCall passOp f2= callTarget -> ^( $f1 passOp $f2) | f1= functionCall -> ^( $f1 YIELDS ^( PACK DEFAULT ) ) )
            int alt31=2;
            alt31 = dfa31.predict(input);
            switch (alt31) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:5: ( functionCall passOp )=>f1= functionCall passOp f2= callTarget
                    {
                    pushFollow(FOLLOW_functionCall_in_functionCallTarget1921);
                    f1=functionCall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_functionCall.add(f1.getTree());
                    pushFollow(FOLLOW_passOp_in_functionCallTarget1923);
                    passOp102=passOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_passOp.add(passOp102.getTree());
                    pushFollow(FOLLOW_callTarget_in_functionCallTarget1927);
                    f2=callTarget();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callTarget.add(f2.getTree());


                    // AST REWRITE
                    // elements: f2, passOp, f1
                    // token labels: 
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 289:6: -> ^( $f1 passOp $f2)
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:289:9: ^( $f1 passOp $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_f1.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_passOp.nextTree());
                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:290:5: f1= functionCall
                    {
                    pushFollow(FOLLOW_functionCall_in_functionCallTarget1953);
                    f1=functionCall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_functionCall.add(f1.getTree());


                    // AST REWRITE
                    // elements: f1
                    // token labels: 
                    // rule labels: retval, f1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 290:21: -> ^( $f1 YIELDS ^( PACK DEFAULT ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:290:24: ^( $f1 YIELDS ^( PACK DEFAULT ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_f1.nextNode(), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(YIELDS, "YIELDS"));
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:290:37: ^( PACK DEFAULT )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(PACK, "PACK"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(DEFAULT, "DEFAULT"));

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functionCallTarget"

    public static class functionCall_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionCall"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:293:1: functionCall : name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) ) ;
    public final StencilParser.functionCall_return functionCall() throws RecognitionException {
        StencilParser.functionCall_return retval = new StencilParser.functionCall_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.callName_return name = null;

        StencilParser.specializer_return specializer103 = null;

        StencilParser.valueList_return valueList104 = null;


        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        RewriteRuleSubtreeStream stream_callName=new RewriteRuleSubtreeStream(adaptor,"rule callName");
        RewriteRuleSubtreeStream stream_valueList=new RewriteRuleSubtreeStream(adaptor,"rule valueList");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:294:3: (name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:294:5: name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList
            {
            pushFollow(FOLLOW_callName_in_functionCall1984);
            name=callName(MAIN_BLOCK_TAG);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callName.add(name.getTree());
            pushFollow(FOLLOW_specializer_in_functionCall1987);
            specializer103=specializer(RuleOpts.All);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_specializer.add(specializer103.getTree());
            pushFollow(FOLLOW_valueList_in_functionCall1990);
            valueList104=valueList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_valueList.add(valueList104.getTree());


            // AST REWRITE
            // elements: valueList, specializer
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 295:5: -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:295:8: ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUNCTION, ((Tree)name.tree).getText()), root_1);

                adaptor.addChild(root_1, stream_specializer.nextTree());
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:295:60: ^( LIST[\"args\"] valueList )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "args"), root_2);

                adaptor.addChild(root_2, stream_valueList.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "functionCall"

    public static class callName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callName"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:298:1: callName[String defaultCall] : (pre= ID NAMESPACE post= ID -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text] -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall] | name= ID -> {name.getText().indexOf(\".\") > 0}? ID[$name.text] -> ID[$name.text + \".\" + defaultCall] );
    public final StencilParser.callName_return callName(String defaultCall) throws RecognitionException {
        StencilParser.callName_return retval = new StencilParser.callName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token pre=null;
        Token post=null;
        Token name=null;
        Token NAMESPACE105=null;

        Object pre_tree=null;
        Object post_tree=null;
        Object name_tree=null;
        Object NAMESPACE105_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_NAMESPACE=new RewriteRuleTokenStream(adaptor,"token NAMESPACE");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:3: (pre= ID NAMESPACE post= ID -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text] -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall] | name= ID -> {name.getText().indexOf(\".\") > 0}? ID[$name.text] -> ID[$name.text + \".\" + defaultCall] )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==ID) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==NAMESPACE) ) {
                    alt32=1;
                }
                else if ( (LA32_1==GROUP||LA32_1==ARG) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:5: pre= ID NAMESPACE post= ID
                    {
                    pre=(Token)match(input,ID,FOLLOW_ID_in_callName2024); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(pre);

                    NAMESPACE105=(Token)match(input,NAMESPACE,FOLLOW_NAMESPACE_in_callName2026); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMESPACE.add(NAMESPACE105);

                    post=(Token)match(input,ID,FOLLOW_ID_in_callName2030); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(post);



                    // AST REWRITE
                    // elements: ID, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 300:5: -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text]
                    if (post.getText().indexOf(".") > 0) {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (pre!=null?pre.getText():null) + NAMESPACE + (post!=null?post.getText():null)));

                    }
                    else // 301:5: -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (pre!=null?pre.getText():null) + NAMESPACE + (post!=null?post.getText():null) + "." + defaultCall));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:302:5: name= ID
                    {
                    name=(Token)match(input,ID,FOLLOW_ID_in_callName2078); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);



                    // AST REWRITE
                    // elements: ID, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 303:5: -> {name.getText().indexOf(\".\") > 0}? ID[$name.text]
                    if (name.getText().indexOf(".") > 0) {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (name!=null?name.getText():null)));

                    }
                    else // 304:5: -> ID[$name.text + \".\" + defaultCall]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (name!=null?name.getText():null) + "." + defaultCall));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "callName"

    public static class target_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "target"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:306:1: target[String def] : ( GLYPH tuple[false] | RETURN tuple[false] | CANVAS tuple[false] | LOCAL tuple[false] | VIEW tuple[false] | tuple[true] -> {def.equals(\"glyph\")}? ^( GLYPH tuple ) -> {def.equals(\"return\")}? ^( RETURN tuple ) -> ^( DEFAULT tuple ) );
    public final StencilParser.target_return target(String def) throws RecognitionException {
        StencilParser.target_return retval = new StencilParser.target_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GLYPH106=null;
        Token RETURN108=null;
        Token CANVAS110=null;
        Token LOCAL112=null;
        Token VIEW114=null;
        StencilParser.tuple_return tuple107 = null;

        StencilParser.tuple_return tuple109 = null;

        StencilParser.tuple_return tuple111 = null;

        StencilParser.tuple_return tuple113 = null;

        StencilParser.tuple_return tuple115 = null;

        StencilParser.tuple_return tuple116 = null;


        Object GLYPH106_tree=null;
        Object RETURN108_tree=null;
        Object CANVAS110_tree=null;
        Object LOCAL112_tree=null;
        Object VIEW114_tree=null;
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:307:3: ( GLYPH tuple[false] | RETURN tuple[false] | CANVAS tuple[false] | LOCAL tuple[false] | VIEW tuple[false] | tuple[true] -> {def.equals(\"glyph\")}? ^( GLYPH tuple ) -> {def.equals(\"return\")}? ^( RETURN tuple ) -> ^( DEFAULT tuple ) )
            int alt33=6;
            switch ( input.LA(1) ) {
            case GLYPH:
                {
                alt33=1;
                }
                break;
            case RETURN:
                {
                alt33=2;
                }
                break;
            case CANVAS:
                {
                alt33=3;
                }
                break;
            case LOCAL:
                {
                alt33=4;
                }
                break;
            case VIEW:
                {
                alt33=5;
                }
                break;
            case GROUP:
            case ID:
                {
                alt33=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }

            switch (alt33) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:307:5: GLYPH tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    GLYPH106=(Token)match(input,GLYPH,FOLLOW_GLYPH_in_target2129); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GLYPH106_tree = (Object)adaptor.create(GLYPH106);
                    root_0 = (Object)adaptor.becomeRoot(GLYPH106_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2132);
                    tuple107=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple107.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:308:5: RETURN tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    RETURN108=(Token)match(input,RETURN,FOLLOW_RETURN_in_target2139); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    RETURN108_tree = (Object)adaptor.create(RETURN108);
                    root_0 = (Object)adaptor.becomeRoot(RETURN108_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2142);
                    tuple109=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple109.getTree());

                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:309:5: CANVAS tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    CANVAS110=(Token)match(input,CANVAS,FOLLOW_CANVAS_in_target2149); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CANVAS110_tree = (Object)adaptor.create(CANVAS110);
                    root_0 = (Object)adaptor.becomeRoot(CANVAS110_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2152);
                    tuple111=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple111.getTree());

                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:310:5: LOCAL tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    LOCAL112=(Token)match(input,LOCAL,FOLLOW_LOCAL_in_target2159); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LOCAL112_tree = (Object)adaptor.create(LOCAL112);
                    root_0 = (Object)adaptor.becomeRoot(LOCAL112_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2162);
                    tuple113=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple113.getTree());

                    }
                    break;
                case 5 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:311:5: VIEW tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    VIEW114=(Token)match(input,VIEW,FOLLOW_VIEW_in_target2169); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    VIEW114_tree = (Object)adaptor.create(VIEW114);
                    root_0 = (Object)adaptor.becomeRoot(VIEW114_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2172);
                    tuple115=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple115.getTree());

                    }
                    break;
                case 6 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:312:5: tuple[true]
                    {
                    pushFollow(FOLLOW_tuple_in_target2179);
                    tuple116=tuple(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple116.getTree());


                    // AST REWRITE
                    // elements: tuple, tuple, tuple
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 313:5: -> {def.equals(\"glyph\")}? ^( GLYPH tuple )
                    if (def.equals("glyph")) {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:313:31: ^( GLYPH tuple )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(GLYPH, "GLYPH"), root_1);

                        adaptor.addChild(root_1, stream_tuple.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 314:5: -> {def.equals(\"return\")}? ^( RETURN tuple )
                    if (def.equals("return")) {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:314:32: ^( RETURN tuple )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RETURN, "RETURN"), root_1);

                        adaptor.addChild(root_1, stream_tuple.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 315:5: -> ^( DEFAULT tuple )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:315:8: ^( DEFAULT tuple )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DEFAULT, "DEFAULT"), root_1);

                        adaptor.addChild(root_1, stream_tuple.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "target"

    public static class pythonDef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pythonDef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:319:1: pythonDef : ( ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID ( pythonBlock )+ ) | PYTHON name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ ) );
    public final StencilParser.pythonDef_return pythonDef() throws RecognitionException {
        StencilParser.pythonDef_return retval = new StencilParser.pythonDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token env=null;
        Token name=null;
        Token PYTHON117=null;
        Token ARG118=null;
        Token CLOSE_ARG119=null;
        Token PYTHON121=null;
        StencilParser.pythonBlock_return pythonBlock120 = null;

        StencilParser.pythonBlock_return pythonBlock122 = null;


        Object env_tree=null;
        Object name_tree=null;
        Object PYTHON117_tree=null;
        Object ARG118_tree=null;
        Object CLOSE_ARG119_tree=null;
        Object PYTHON121_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_PYTHON=new RewriteRuleTokenStream(adaptor,"token PYTHON");
        RewriteRuleSubtreeStream stream_pythonBlock=new RewriteRuleSubtreeStream(adaptor,"rule pythonBlock");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:3: ( ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID ( pythonBlock )+ ) | PYTHON name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==PYTHON) ) {
                int LA36_1 = input.LA(2);

                if ( (LA36_1==ARG) && (synpred3_Stencil())) {
                    alt36=1;
                }
                else if ( (LA36_1==ID) ) {
                    alt36=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 36, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:5: ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+
                    {
                    PYTHON117=(Token)match(input,PYTHON,FOLLOW_PYTHON_in_pythonDef2240); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PYTHON.add(PYTHON117);

                    ARG118=(Token)match(input,ARG,FOLLOW_ARG_in_pythonDef2242); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG118);

                    env=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2246); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(env);

                    CLOSE_ARG119=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_pythonDef2248); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG119);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2252); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:57: ( pythonBlock )+
                    int cnt34=0;
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( (LA34_0==FACET||LA34_0==TAGGED_ID) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:57: pythonBlock
                    	    {
                    	    pushFollow(FOLLOW_pythonBlock_in_pythonDef2254);
                    	    pythonBlock120=pythonBlock();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_pythonBlock.add(pythonBlock120.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt34 >= 1 ) break loop34;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(34, input);
                                throw eee;
                        }
                        cnt34++;
                    } while (true);



                    // AST REWRITE
                    // elements: PYTHON, ID, pythonBlock
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 321:5: -> ^( PYTHON[$name.text] ID ( pythonBlock )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:321:8: ^( PYTHON[$name.text] ID ( pythonBlock )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON, (name!=null?name.getText():null)), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        if ( !(stream_pythonBlock.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_pythonBlock.hasNext() ) {
                            adaptor.addChild(root_1, stream_pythonBlock.nextTree());

                        }
                        stream_pythonBlock.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:322:5: PYTHON name= ID ( pythonBlock )+
                    {
                    PYTHON121=(Token)match(input,PYTHON,FOLLOW_PYTHON_in_pythonDef2277); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PYTHON.add(PYTHON121);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2281); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:322:20: ( pythonBlock )+
                    int cnt35=0;
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( (LA35_0==FACET||LA35_0==TAGGED_ID) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:322:20: pythonBlock
                    	    {
                    	    pushFollow(FOLLOW_pythonBlock_in_pythonDef2283);
                    	    pythonBlock122=pythonBlock();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_pythonBlock.add(pythonBlock122.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt35 >= 1 ) break loop35;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(35, input);
                                throw eee;
                        }
                        cnt35++;
                    } while (true);



                    // AST REWRITE
                    // elements: ID, pythonBlock, PYTHON
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 323:5: -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:323:8: ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON, (name!=null?name.getText():null)), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(ID, buryID((name!=null?name.getText():null))));
                        if ( !(stream_pythonBlock.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_pythonBlock.hasNext() ) {
                            adaptor.addChild(root_1, stream_pythonBlock.nextTree());

                        }
                        stream_pythonBlock.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pythonDef"

    public static class pythonBlock_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pythonBlock"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:325:1: pythonBlock : ( FACET 'Init' CODE_BLOCK -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK ) | annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK ) );
    public final StencilParser.pythonBlock_return pythonBlock() throws RecognitionException {
        StencilParser.pythonBlock_return retval = new StencilParser.pythonBlock_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token FACET123=null;
        Token string_literal124=null;
        Token CODE_BLOCK125=null;
        Token FACET127=null;
        Token YIELDS129=null;
        Token CODE_BLOCK131=null;
        StencilParser.annotations_return annotations126 = null;

        StencilParser.tuple_return tuple128 = null;

        StencilParser.tuple_return tuple130 = null;


        Object name_tree=null;
        Object FACET123_tree=null;
        Object string_literal124_tree=null;
        Object CODE_BLOCK125_tree=null;
        Object FACET127_tree=null;
        Object YIELDS129_tree=null;
        Object CODE_BLOCK131_tree=null;
        RewriteRuleTokenStream stream_YIELDS=new RewriteRuleTokenStream(adaptor,"token YIELDS");
        RewriteRuleTokenStream stream_CODE_BLOCK=new RewriteRuleTokenStream(adaptor,"token CODE_BLOCK");
        RewriteRuleTokenStream stream_FACET=new RewriteRuleTokenStream(adaptor,"token FACET");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        RewriteRuleSubtreeStream stream_annotations=new RewriteRuleSubtreeStream(adaptor,"rule annotations");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:326:2: ( FACET 'Init' CODE_BLOCK -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK ) | annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==FACET) ) {
                int LA37_1 = input.LA(2);

                if ( (LA37_1==82) ) {
                    alt37=1;
                }
                else if ( (LA37_1==ID) ) {
                    alt37=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 37, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA37_0==TAGGED_ID) ) {
                alt37=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:326:4: FACET 'Init' CODE_BLOCK
                    {
                    FACET123=(Token)match(input,FACET,FOLLOW_FACET_in_pythonBlock2312); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FACET.add(FACET123);

                    string_literal124=(Token)match(input,82,FOLLOW_82_in_pythonBlock2314); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal124);

                    CODE_BLOCK125=(Token)match(input,CODE_BLOCK,FOLLOW_CODE_BLOCK_in_pythonBlock2316); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CODE_BLOCK.add(CODE_BLOCK125);



                    // AST REWRITE
                    // elements: CODE_BLOCK
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 327:3: -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:327:6: ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON_FACET, "Init"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:327:29: ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(YIELDS, "YIELDS"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"));
                        adaptor.addChild(root_2, (Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:327:71: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_2);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:327:93: ^( ANNOTATION[\"Type\"] STRING[\"NA\"] )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(ANNOTATION, "Type"), root_3);

                        adaptor.addChild(root_3, (Object)adaptor.create(STRING, "NA"));

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_CODE_BLOCK.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:4: annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK
                    {
                    pushFollow(FOLLOW_annotations_in_pythonBlock2353);
                    annotations126=annotations();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_annotations.add(annotations126.getTree());
                    FACET127=(Token)match(input,FACET,FOLLOW_FACET_in_pythonBlock2355); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FACET.add(FACET127);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonBlock2359); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_tuple_in_pythonBlock2361);
                    tuple128=tuple(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple128.getTree());
                    YIELDS129=(Token)match(input,YIELDS,FOLLOW_YIELDS_in_pythonBlock2364); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_YIELDS.add(YIELDS129);

                    pushFollow(FOLLOW_tuple_in_pythonBlock2366);
                    tuple130=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple130.getTree());
                    CODE_BLOCK131=(Token)match(input,CODE_BLOCK,FOLLOW_CODE_BLOCK_in_pythonBlock2369); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CODE_BLOCK.add(CODE_BLOCK131);



                    // AST REWRITE
                    // elements: tuple, CODE_BLOCK, annotations, YIELDS, tuple
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 329:3: -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:329:6: ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON_FACET, name), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:329:27: ^( YIELDS tuple tuple )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(stream_YIELDS.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_tuple.nextTree());
                        adaptor.addChild(root_2, stream_tuple.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_annotations.nextTree());
                        adaptor.addChild(root_1, stream_CODE_BLOCK.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pythonBlock"

    public static class annotations_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "annotations"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:331:1: annotations : (a= annotation -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) ) | -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) ) );
    public final StencilParser.annotations_return annotations() throws RecognitionException {
        StencilParser.annotations_return retval = new StencilParser.annotations_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.annotation_return a = null;


        RewriteRuleSubtreeStream stream_annotation=new RewriteRuleSubtreeStream(adaptor,"rule annotation");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:332:3: (a= annotation -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) ) | -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==TAGGED_ID) ) {
                alt38=1;
            }
            else if ( (LA38_0==FACET) ) {
                alt38=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:332:5: a= annotation
                    {
                    pushFollow(FOLLOW_annotation_in_annotations2402);
                    a=annotation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_annotation.add(a.getTree());


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 332:18: -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:332:21: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:332:43: ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(ANNOTATION, "TYPE"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(STRING, (a!=null?input.toString(a.start,a.stop):null).toUpperCase().substring(1)));

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:333:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 333:5: -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:333:8: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:333:30: ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(ANNOTATION, "TYPE"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(STRING, "CATEGORIZE"));

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "annotations"

    public static class annotation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "annotation"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:1: annotation : t= TAGGED_ID -> ANNOTATION[\"JUNK\"] ;
    public final StencilParser.annotation_return annotation() throws RecognitionException {
        StencilParser.annotation_return retval = new StencilParser.annotation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;

        Object t_tree=null;
        RewriteRuleTokenStream stream_TAGGED_ID=new RewriteRuleTokenStream(adaptor,"token TAGGED_ID");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:11: (t= TAGGED_ID -> ANNOTATION[\"JUNK\"] )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:13: t= TAGGED_ID
            {
            t=(Token)match(input,TAGGED_ID,FOLLOW_TAGGED_ID_in_annotation2445); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TAGGED_ID.add(t);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 335:25: -> ANNOTATION[\"JUNK\"]
            {
                adaptor.addChild(root_0, (Object)adaptor.create(ANNOTATION, "JUNK"));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "annotation"

    public static class specializer_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "specializer"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:339:1: specializer[RuleOpts opts] : ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) );
    public final StencilParser.specializer_return specializer(RuleOpts opts) throws RecognitionException {
        StencilParser.specializer_return retval = new StencilParser.specializer_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ARG132=null;
        Token CLOSE_ARG135=null;
        Token ARG136=null;
        Token CLOSE_ARG139=null;
        Token ARG140=null;
        Token SPLIT142=null;
        Token CLOSE_ARG145=null;
        Token ARG146=null;
        Token SPLIT148=null;
        Token CLOSE_ARG151=null;
        Token ARG152=null;
        Token CLOSE_ARG154=null;
        StencilParser.range_return range133 = null;

        StencilParser.sepArgList_return sepArgList134 = null;

        StencilParser.split_return split137 = null;

        StencilParser.sepArgList_return sepArgList138 = null;

        StencilParser.range_return range141 = null;

        StencilParser.split_return split143 = null;

        StencilParser.sepArgList_return sepArgList144 = null;

        StencilParser.split_return split147 = null;

        StencilParser.range_return range149 = null;

        StencilParser.sepArgList_return sepArgList150 = null;

        StencilParser.argList_return argList153 = null;


        Object ARG132_tree=null;
        Object CLOSE_ARG135_tree=null;
        Object ARG136_tree=null;
        Object CLOSE_ARG139_tree=null;
        Object ARG140_tree=null;
        Object SPLIT142_tree=null;
        Object CLOSE_ARG145_tree=null;
        Object ARG146_tree=null;
        Object SPLIT148_tree=null;
        Object CLOSE_ARG151_tree=null;
        Object ARG152_tree=null;
        Object CLOSE_ARG154_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_SPLIT=new RewriteRuleTokenStream(adaptor,"token SPLIT");
        RewriteRuleSubtreeStream stream_argList=new RewriteRuleSubtreeStream(adaptor,"rule argList");
        RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");
        RewriteRuleSubtreeStream stream_sepArgList=new RewriteRuleSubtreeStream(adaptor,"rule sepArgList");
        RewriteRuleSubtreeStream stream_split=new RewriteRuleSubtreeStream(adaptor,"rule split");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:3: ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) )
            int alt39=6;
            alt39 = dfa39.predict(input);
            switch (alt39) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:5: ARG range sepArgList CLOSE_ARG {...}?
                    {
                    ARG132=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2464); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG132);

                    pushFollow(FOLLOW_range_in_specializer2466);
                    range133=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range133.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2468);
                    sepArgList134=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList134.getTree());
                    CLOSE_ARG135=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2470); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG135);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: range, sepArgList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 341:29: -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:341:32: ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        adaptor.addChild(root_1, stream_range.nextTree());
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:341:52: ^( SPLIT BASIC PRE ID[(String) null] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(BASIC, "BASIC"));
                        adaptor.addChild(root_2, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_2, (Object)adaptor.create(ID, (String) null));

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_sepArgList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:342:5: ARG split[false] sepArgList CLOSE_ARG {...}?
                    {
                    ARG136=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2503); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG136);

                    pushFollow(FOLLOW_split_in_specializer2505);
                    split137=split(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split137.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2508);
                    sepArgList138=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList138.getTree());
                    CLOSE_ARG139=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2510); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG139);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: split, sepArgList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 343:29: -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:343:32: ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:343:46: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(RANGE, "RANGE"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));
                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_split.nextTree());
                        adaptor.addChild(root_1, stream_sepArgList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:344:5: ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}?
                    {
                    ARG140=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG140);

                    pushFollow(FOLLOW_range_in_specializer2544);
                    range141=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range141.getTree());
                    SPLIT142=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_specializer2546); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT142);

                    pushFollow(FOLLOW_split_in_specializer2548);
                    split143=split(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split143.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2551);
                    sepArgList144=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList144.getTree());
                    CLOSE_ARG145=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2553); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG145);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: split, sepArgList, range
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 345:29: -> ^( SPECIALIZER range split sepArgList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:345:33: ^( SPECIALIZER range split sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        adaptor.addChild(root_1, stream_range.nextTree());
                        adaptor.addChild(root_1, stream_split.nextTree());
                        adaptor.addChild(root_1, stream_sepArgList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:346:5: ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}?
                    {
                    ARG146=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2578); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG146);

                    pushFollow(FOLLOW_split_in_specializer2580);
                    split147=split(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split147.getTree());
                    SPLIT148=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_specializer2583); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT148);

                    pushFollow(FOLLOW_range_in_specializer2585);
                    range149=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range149.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2587);
                    sepArgList150=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList150.getTree());
                    CLOSE_ARG151=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2589); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG151);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: split, range, sepArgList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 347:29: -> ^( SPECIALIZER range split sepArgList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:347:32: ^( SPECIALIZER range split sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        adaptor.addChild(root_1, stream_range.nextTree());
                        adaptor.addChild(root_1, stream_split.nextTree());
                        adaptor.addChild(root_1, stream_sepArgList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:348:5: ARG argList CLOSE_ARG {...}?
                    {
                    ARG152=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2613); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG152);

                    pushFollow(FOLLOW_argList_in_specializer2615);
                    argList153=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_argList.add(argList153.getTree());
                    CLOSE_ARG154=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2617); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG154);

                    if ( !((opts != RuleOpts.Empty)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts != RuleOpts.Empty");
                    }


                    // AST REWRITE
                    // elements: argList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 349:31: -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:349:34: ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:349:48: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(RANGE, "RANGE"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));
                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:349:93: ^( SPLIT BASIC PRE ID[(String) null] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(BASIC, "BASIC"));
                        adaptor.addChild(root_2, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_2, (Object)adaptor.create(ID, (String) null));

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_argList.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:350:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 350:5: -> ^( SPECIALIZER DEFAULT )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:350:8: ^( SPECIALIZER DEFAULT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(DEFAULT, "DEFAULT"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "specializer"

    public static class sepArgList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sepArgList"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:352:1: sepArgList : ( SEPARATOR argList | -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) );
    public final StencilParser.sepArgList_return sepArgList() throws RecognitionException {
        StencilParser.sepArgList_return retval = new StencilParser.sepArgList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR155=null;
        StencilParser.argList_return argList156 = null;


        Object SEPARATOR155_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:353:3: ( SEPARATOR argList | -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==SEPARATOR) ) {
                alt40=1;
            }
            else if ( (LA40_0==CLOSE_ARG) ) {
                alt40=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:353:5: SEPARATOR argList
                    {
                    root_0 = (Object)adaptor.nil();

                    SEPARATOR155=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_sepArgList2674); if (state.failed) return retval;
                    pushFollow(FOLLOW_argList_in_sepArgList2677);
                    argList156=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, argList156.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:354:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 354:5: -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:354:8: ^( LIST[\"Values Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Values Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:354:36: ^( LIST[\"Map Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Map Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sepArgList"

    public static class argList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "argList"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:356:1: argList : ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList );
    public final StencilParser.argList_return argList() throws RecognitionException {
        StencilParser.argList_return retval = new StencilParser.argList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR160=null;
        StencilParser.values_return values157 = null;

        StencilParser.mapList_return mapList158 = null;

        StencilParser.values_return values159 = null;

        StencilParser.mapList_return mapList161 = null;


        Object SEPARATOR160_tree=null;
        RewriteRuleSubtreeStream stream_mapList=new RewriteRuleSubtreeStream(adaptor,"rule mapList");
        RewriteRuleSubtreeStream stream_values=new RewriteRuleSubtreeStream(adaptor,"rule values");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:3: ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList )
            int alt41=4;
            alt41 = dfa41.predict(input);
            switch (alt41) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:5: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 357:5: -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:8: ^( LIST[\"Values Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Values Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:36: ^( LIST[\"Map Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Map Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:358:5: values
                    {
                    pushFollow(FOLLOW_values_in_argList2722);
                    values157=values();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_values.add(values157.getTree());


                    // AST REWRITE
                    // elements: values
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 358:12: -> values ^( LIST[\"Map Arguments\"] )
                    {
                        adaptor.addChild(root_0, stream_values.nextTree());
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:358:22: ^( LIST[\"Map Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Map Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:359:5: mapList
                    {
                    pushFollow(FOLLOW_mapList_in_argList2737);
                    mapList158=mapList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mapList.add(mapList158.getTree());


                    // AST REWRITE
                    // elements: mapList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 359:13: -> ^( LIST[\"Value Arguments\"] ) mapList
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:359:16: ^( LIST[\"Value Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Value Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }
                        adaptor.addChild(root_0, stream_mapList.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:360:5: values SEPARATOR mapList
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_values_in_argList2752);
                    values159=values();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, values159.getTree());
                    SEPARATOR160=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_argList2754); if (state.failed) return retval;
                    pushFollow(FOLLOW_mapList_in_argList2757);
                    mapList161=mapList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mapList161.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "argList"

    public static class values_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "values"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:362:1: values : atom ( SEPARATOR atom )* -> ^( LIST[\"Value Arguments\"] ( atom )* ) ;
    public final StencilParser.values_return values() throws RecognitionException {
        StencilParser.values_return retval = new StencilParser.values_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR163=null;
        StencilParser.atom_return atom162 = null;

        StencilParser.atom_return atom164 = null;


        Object SEPARATOR163_tree=null;
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:3: ( atom ( SEPARATOR atom )* -> ^( LIST[\"Value Arguments\"] ( atom )* ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:5: atom ( SEPARATOR atom )*
            {
            pushFollow(FOLLOW_atom_in_values2767);
            atom162=atom();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_atom.add(atom162.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:10: ( SEPARATOR atom )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==SEPARATOR) ) {
                    int LA42_2 = input.LA(2);

                    if ( (LA42_2==ALL||LA42_2==DEFAULT||LA42_2==NAMESPLIT||(LA42_2>=TAGGED_ID && LA42_2<=DIGITS)||(LA42_2>=92 && LA42_2<=93)) ) {
                        alt42=1;
                    }


                }


                switch (alt42) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:11: SEPARATOR atom
            	    {
            	    SEPARATOR163=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_values2770); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR163);

            	    pushFollow(FOLLOW_atom_in_values2772);
            	    atom164=atom();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_atom.add(atom164.getTree());

            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);



            // AST REWRITE
            // elements: atom
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 363:28: -> ^( LIST[\"Value Arguments\"] ( atom )* )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:31: ^( LIST[\"Value Arguments\"] ( atom )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Value Arguments"), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:363:57: ( atom )*
                while ( stream_atom.hasNext() ) {
                    adaptor.addChild(root_1, stream_atom.nextTree());

                }
                stream_atom.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "values"

    public static class mapList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mapList"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:365:1: mapList : mapEntry ( SEPARATOR mapEntry )* -> ^( LIST[\"Map Arguments\"] ( mapEntry )* ) ;
    public final StencilParser.mapList_return mapList() throws RecognitionException {
        StencilParser.mapList_return retval = new StencilParser.mapList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR166=null;
        StencilParser.mapEntry_return mapEntry165 = null;

        StencilParser.mapEntry_return mapEntry167 = null;


        Object SEPARATOR166_tree=null;
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleSubtreeStream stream_mapEntry=new RewriteRuleSubtreeStream(adaptor,"rule mapEntry");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:3: ( mapEntry ( SEPARATOR mapEntry )* -> ^( LIST[\"Map Arguments\"] ( mapEntry )* ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:5: mapEntry ( SEPARATOR mapEntry )*
            {
            pushFollow(FOLLOW_mapEntry_in_mapList2794);
            mapEntry165=mapEntry();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_mapEntry.add(mapEntry165.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:14: ( SEPARATOR mapEntry )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==SEPARATOR) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:15: SEPARATOR mapEntry
            	    {
            	    SEPARATOR166=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_mapList2797); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR166);

            	    pushFollow(FOLLOW_mapEntry_in_mapList2799);
            	    mapEntry167=mapEntry();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mapEntry.add(mapEntry167.getTree());

            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);



            // AST REWRITE
            // elements: mapEntry
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 366:36: -> ^( LIST[\"Map Arguments\"] ( mapEntry )* )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:39: ^( LIST[\"Map Arguments\"] ( mapEntry )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Map Arguments"), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:63: ( mapEntry )*
                while ( stream_mapEntry.hasNext() ) {
                    adaptor.addChild(root_1, stream_mapEntry.nextTree());

                }
                stream_mapEntry.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mapList"

    public static class mapEntry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mapEntry"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:368:1: mapEntry : k= ID '=' v= atom -> ^( MAP_ENTRY[$k.text] $v) ;
    public final StencilParser.mapEntry_return mapEntry() throws RecognitionException {
        StencilParser.mapEntry_return retval = new StencilParser.mapEntry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token k=null;
        Token char_literal168=null;
        StencilParser.atom_return v = null;


        Object k_tree=null;
        Object char_literal168_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:369:3: (k= ID '=' v= atom -> ^( MAP_ENTRY[$k.text] $v) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:369:5: k= ID '=' v= atom
            {
            k=(Token)match(input,ID,FOLLOW_ID_in_mapEntry2826); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(k);

            char_literal168=(Token)match(input,83,FOLLOW_83_in_mapEntry2828); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_83.add(char_literal168);

            pushFollow(FOLLOW_atom_in_mapEntry2832);
            v=atom();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_atom.add(v.getTree());


            // AST REWRITE
            // elements: v
            // token labels: 
            // rule labels: v, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"rule v",v!=null?v.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 369:21: -> ^( MAP_ENTRY[$k.text] $v)
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:369:24: ^( MAP_ENTRY[$k.text] $v)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MAP_ENTRY, (k!=null?k.getText():null)), root_1);

                adaptor.addChild(root_1, stream_v.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mapEntry"

    public static class tuple_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tuple"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:1: tuple[boolean allowEmpty] : ( emptySet {...}? -> ^( TUPLE_PROTOTYPE ) | ID -> ^( TUPLE_PROTOTYPE ID ) | GROUP ID ( SEPARATOR ID )* CLOSE_GROUP -> ^( TUPLE_PROTOTYPE ( ID )+ ) );
    public final StencilParser.tuple_return tuple(boolean allowEmpty) throws RecognitionException {
        StencilParser.tuple_return retval = new StencilParser.tuple_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID170=null;
        Token GROUP171=null;
        Token ID172=null;
        Token SEPARATOR173=null;
        Token ID174=null;
        Token CLOSE_GROUP175=null;
        StencilParser.emptySet_return emptySet169 = null;


        Object ID170_tree=null;
        Object GROUP171_tree=null;
        Object ID172_tree=null;
        Object SEPARATOR173_tree=null;
        Object ID174_tree=null;
        Object CLOSE_GROUP175_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_emptySet=new RewriteRuleSubtreeStream(adaptor,"rule emptySet");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:372:3: ( emptySet {...}? -> ^( TUPLE_PROTOTYPE ) | ID -> ^( TUPLE_PROTOTYPE ID ) | GROUP ID ( SEPARATOR ID )* CLOSE_GROUP -> ^( TUPLE_PROTOTYPE ( ID )+ ) )
            int alt45=3;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==GROUP) ) {
                int LA45_1 = input.LA(2);

                if ( (LA45_1==CLOSE_GROUP) ) {
                    alt45=1;
                }
                else if ( (LA45_1==ID) ) {
                    alt45=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 45, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA45_0==ID) ) {
                alt45=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }
            switch (alt45) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:372:5: emptySet {...}?
                    {
                    pushFollow(FOLLOW_emptySet_in_tuple2853);
                    emptySet169=emptySet();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_emptySet.add(emptySet169.getTree());
                    if ( !((allowEmpty)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "tuple", "allowEmpty");
                    }


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 373:5: -> ^( TUPLE_PROTOTYPE )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:373:8: ^( TUPLE_PROTOTYPE )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:5: ID
                    {
                    ID170=(Token)match(input,ID,FOLLOW_ID_in_tuple2871); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID170);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 375:5: -> ^( TUPLE_PROTOTYPE ID )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:375:8: ^( TUPLE_PROTOTYPE ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:376:5: GROUP ID ( SEPARATOR ID )* CLOSE_GROUP
                    {
                    GROUP171=(Token)match(input,GROUP,FOLLOW_GROUP_in_tuple2889); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP171);

                    ID172=(Token)match(input,ID,FOLLOW_ID_in_tuple2891); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID172);

                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:376:14: ( SEPARATOR ID )*
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( (LA44_0==SEPARATOR) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:376:15: SEPARATOR ID
                    	    {
                    	    SEPARATOR173=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_tuple2894); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR173);

                    	    ID174=(Token)match(input,ID,FOLLOW_ID_in_tuple2896); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_ID.add(ID174);


                    	    }
                    	    break;

                    	default :
                    	    break loop44;
                        }
                    } while (true);

                    CLOSE_GROUP175=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_tuple2900); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP175);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 377:5: -> ^( TUPLE_PROTOTYPE ( ID )+ )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:377:8: ^( TUPLE_PROTOTYPE ( ID )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"), root_1);

                        if ( !(stream_ID.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_ID.hasNext() ) {
                            adaptor.addChild(root_1, stream_ID.nextNode());

                        }
                        stream_ID.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "tuple"

    public static class emptySet_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "emptySet"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:380:1: emptySet : GROUP CLOSE_GROUP ;
    public final StencilParser.emptySet_return emptySet() throws RecognitionException {
        StencilParser.emptySet_return retval = new StencilParser.emptySet_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP176=null;
        Token CLOSE_GROUP177=null;

        Object GROUP176_tree=null;
        Object CLOSE_GROUP177_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:380:9: ( GROUP CLOSE_GROUP )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:380:11: GROUP CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP176=(Token)match(input,GROUP,FOLLOW_GROUP_in_emptySet2921); if (state.failed) return retval;
            CLOSE_GROUP177=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_emptySet2924); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "emptySet"

    public static class valueList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "valueList"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:1: valueList : GROUP value ( SEPARATOR value )* CLOSE_GROUP ;
    public final StencilParser.valueList_return valueList() throws RecognitionException {
        StencilParser.valueList_return retval = new StencilParser.valueList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP178=null;
        Token SEPARATOR180=null;
        Token CLOSE_GROUP182=null;
        StencilParser.value_return value179 = null;

        StencilParser.value_return value181 = null;


        Object GROUP178_tree=null;
        Object SEPARATOR180_tree=null;
        Object CLOSE_GROUP182_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:10: ( GROUP value ( SEPARATOR value )* CLOSE_GROUP )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:13: GROUP value ( SEPARATOR value )* CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP178=(Token)match(input,GROUP,FOLLOW_GROUP_in_valueList2933); if (state.failed) return retval;
            pushFollow(FOLLOW_value_in_valueList2936);
            value179=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, value179.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:26: ( SEPARATOR value )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==SEPARATOR) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:27: SEPARATOR value
            	    {
            	    SEPARATOR180=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_valueList2939); if (state.failed) return retval;
            	    pushFollow(FOLLOW_value_in_valueList2942);
            	    value181=value();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, value181.getTree());

            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);

            CLOSE_GROUP182=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_valueList2946); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "valueList"

    public static class range_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "range"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:384:1: range : ( number RANGE number -> ^( RANGE number number ) | number RANGE 'n' -> ^( RANGE number NUMBER[RANGE_END] ) | 'n' RANGE 'n' -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) );
    public final StencilParser.range_return range() throws RecognitionException {
        StencilParser.range_return retval = new StencilParser.range_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token RANGE184=null;
        Token RANGE187=null;
        Token char_literal188=null;
        Token char_literal189=null;
        Token RANGE190=null;
        Token char_literal191=null;
        StencilParser.number_return number183 = null;

        StencilParser.number_return number185 = null;

        StencilParser.number_return number186 = null;


        Object RANGE184_tree=null;
        Object RANGE187_tree=null;
        Object char_literal188_tree=null;
        Object char_literal189_tree=null;
        Object RANGE190_tree=null;
        Object char_literal191_tree=null;
        RewriteRuleTokenStream stream_RANGE=new RewriteRuleTokenStream(adaptor,"token RANGE");
        RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
        RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:385:3: ( number RANGE number -> ^( RANGE number number ) | number RANGE 'n' -> ^( RANGE number NUMBER[RANGE_END] ) | 'n' RANGE 'n' -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) )
            int alt47=3;
            alt47 = dfa47.predict(input);
            switch (alt47) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:385:5: number RANGE number
                    {
                    pushFollow(FOLLOW_number_in_range2959);
                    number183=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number183.getTree());
                    RANGE184=(Token)match(input,RANGE,FOLLOW_RANGE_in_range2961); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE184);

                    pushFollow(FOLLOW_number_in_range2963);
                    number185=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number185.getTree());


                    // AST REWRITE
                    // elements: number, RANGE, number
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 386:5: -> ^( RANGE number number )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:386:8: ^( RANGE number number )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_RANGE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_number.nextTree());
                        adaptor.addChild(root_1, stream_number.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:387:5: number RANGE 'n'
                    {
                    pushFollow(FOLLOW_number_in_range2983);
                    number186=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number186.getTree());
                    RANGE187=(Token)match(input,RANGE,FOLLOW_RANGE_in_range2985); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE187);

                    char_literal188=(Token)match(input,84,FOLLOW_84_in_range2987); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(char_literal188);



                    // AST REWRITE
                    // elements: RANGE, number
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 388:5: -> ^( RANGE number NUMBER[RANGE_END] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:388:8: ^( RANGE number NUMBER[RANGE_END] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_RANGE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_number.nextTree());
                        adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, RANGE_END));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:389:5: 'n' RANGE 'n'
                    {
                    char_literal189=(Token)match(input,84,FOLLOW_84_in_range3008); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(char_literal189);

                    RANGE190=(Token)match(input,RANGE,FOLLOW_RANGE_in_range3010); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE190);

                    char_literal191=(Token)match(input,84,FOLLOW_84_in_range3012); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(char_literal191);



                    // AST REWRITE
                    // elements: RANGE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 390:5: -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:8: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_RANGE.nextNode(), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, RANGE_END));
                        adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, RANGE_END));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "range"

    public static class split_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "split"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:393:1: split[boolean pre] : ( ID -> {pre}? ^( SPLIT BASIC PRE ID ) -> ^( SPLIT BASIC POST ID ) | ORDER ID -> {pre}? ^( SPLIT ORDER PRE ID ) -> ^( SPLIT ORDER POST ID ) );
    public final StencilParser.split_return split(boolean pre) throws RecognitionException {
        StencilParser.split_return retval = new StencilParser.split_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID192=null;
        Token ORDER193=null;
        Token ID194=null;

        Object ID192_tree=null;
        Object ORDER193_tree=null;
        Object ID194_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:394:3: ( ID -> {pre}? ^( SPLIT BASIC PRE ID ) -> ^( SPLIT BASIC POST ID ) | ORDER ID -> {pre}? ^( SPLIT ORDER PRE ID ) -> ^( SPLIT ORDER POST ID ) )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==ID) ) {
                alt48=1;
            }
            else if ( (LA48_0==ORDER) ) {
                alt48=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }
            switch (alt48) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:394:5: ID
                    {
                    ID192=(Token)match(input,ID,FOLLOW_ID_in_split3040); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID192);



                    // AST REWRITE
                    // elements: ID, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 394:9: -> {pre}? ^( SPLIT BASIC PRE ID )
                    if (pre) {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:394:19: ^( SPLIT BASIC PRE ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(BASIC, "BASIC"));
                        adaptor.addChild(root_1, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 395:7: -> ^( SPLIT BASIC POST ID )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:395:16: ^( SPLIT BASIC POST ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(BASIC, "BASIC"));
                        adaptor.addChild(root_1, (Object)adaptor.create(POST, "POST"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:396:7: ORDER ID
                    {
                    ORDER193=(Token)match(input,ORDER,FOLLOW_ORDER_in_split3087); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ORDER.add(ORDER193);

                    ID194=(Token)match(input,ID,FOLLOW_ID_in_split3089); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID194);



                    // AST REWRITE
                    // elements: ORDER, ORDER, ID, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 397:7: -> {pre}? ^( SPLIT ORDER PRE ID )
                    if (pre) {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:397:17: ^( SPLIT ORDER PRE ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, stream_ORDER.nextNode());
                        adaptor.addChild(root_1, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 398:5: -> ^( SPLIT ORDER POST ID )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:398:14: ^( SPLIT ORDER POST ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, stream_ORDER.nextNode());
                        adaptor.addChild(root_1, (Object)adaptor.create(POST, "POST"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "split"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:400:1: value : ( tupleRef | atom );
    public final StencilParser.value_return value() throws RecognitionException {
        StencilParser.value_return retval = new StencilParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.tupleRef_return tupleRef195 = null;

        StencilParser.atom_return atom196 = null;



        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:400:7: ( tupleRef | atom )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==ARG||LA49_0==ID||LA49_0==85) ) {
                alt49=1;
            }
            else if ( (LA49_0==ALL||LA49_0==DEFAULT||LA49_0==NAMESPLIT||(LA49_0>=TAGGED_ID && LA49_0<=DIGITS)||(LA49_0>=92 && LA49_0<=93)) ) {
                alt49=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:400:9: tupleRef
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_tupleRef_in_value3140);
                    tupleRef195=tupleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tupleRef195.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:400:21: atom
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_value3145);
                    atom196=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom196.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:1: atom : ( sigil | number | STRING | DEFAULT | ALL );
    public final StencilParser.atom_return atom() throws RecognitionException {
        StencilParser.atom_return retval = new StencilParser.atom_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING199=null;
        Token DEFAULT200=null;
        Token ALL201=null;
        StencilParser.sigil_return sigil197 = null;

        StencilParser.number_return number198 = null;


        Object STRING199_tree=null;
        Object DEFAULT200_tree=null;
        Object ALL201_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:7: ( sigil | number | STRING | DEFAULT | ALL )
            int alt50=5;
            switch ( input.LA(1) ) {
            case TAGGED_ID:
                {
                alt50=1;
                }
                break;
            case NAMESPLIT:
            case DIGITS:
            case 92:
            case 93:
                {
                alt50=2;
                }
                break;
            case STRING:
                {
                alt50=3;
                }
                break;
            case DEFAULT:
                {
                alt50=4;
                }
                break;
            case ALL:
                {
                alt50=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:9: sigil
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sigil_in_atom3153);
                    sigil197=sigil();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sigil197.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:17: number
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_number_in_atom3157);
                    number198=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, number198.getTree());

                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:26: STRING
                    {
                    root_0 = (Object)adaptor.nil();

                    STRING199=(Token)match(input,STRING,FOLLOW_STRING_in_atom3161); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING199_tree = (Object)adaptor.create(STRING199);
                    adaptor.addChild(root_0, STRING199_tree);
                    }

                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:35: DEFAULT
                    {
                    root_0 = (Object)adaptor.nil();

                    DEFAULT200=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_atom3165); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DEFAULT200_tree = (Object)adaptor.create(DEFAULT200);
                    adaptor.addChild(root_0, DEFAULT200_tree);
                    }

                    }
                    break;
                case 5 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:401:45: ALL
                    {
                    root_0 = (Object)adaptor.nil();

                    ALL201=(Token)match(input,ALL,FOLLOW_ALL_in_atom3169); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ALL201_tree = (Object)adaptor.create(ALL201);
                    adaptor.addChild(root_0, ALL201_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "atom"

    public static class tupleRef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tupleRef"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:403:1: tupleRef : ( ID -> ^( TUPLE_REF ID ) | '_' -> ^( TUPLE_REF NUMBER[\"0\"] ) | ARG number CLOSE_ARG -> ^( TUPLE_REF number ) );
    public final StencilParser.tupleRef_return tupleRef() throws RecognitionException {
        StencilParser.tupleRef_return retval = new StencilParser.tupleRef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID202=null;
        Token char_literal203=null;
        Token ARG204=null;
        Token CLOSE_ARG206=null;
        StencilParser.number_return number205 = null;


        Object ID202_tree=null;
        Object char_literal203_tree=null;
        Object ARG204_tree=null;
        Object CLOSE_ARG206_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_85=new RewriteRuleTokenStream(adaptor,"token 85");
        RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:3: ( ID -> ^( TUPLE_REF ID ) | '_' -> ^( TUPLE_REF NUMBER[\"0\"] ) | ARG number CLOSE_ARG -> ^( TUPLE_REF number ) )
            int alt51=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt51=1;
                }
                break;
            case 85:
                {
                alt51=2;
                }
                break;
            case ARG:
                {
                alt51=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }

            switch (alt51) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:5: ID
                    {
                    ID202=(Token)match(input,ID,FOLLOW_ID_in_tupleRef3181); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID202);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 404:8: -> ^( TUPLE_REF ID )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:11: ^( TUPLE_REF ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:405:5: '_'
                    {
                    char_literal203=(Token)match(input,85,FOLLOW_85_in_tupleRef3195); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_85.add(char_literal203);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 405:9: -> ^( TUPLE_REF NUMBER[\"0\"] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:405:12: ^( TUPLE_REF NUMBER[\"0\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, "0"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:407:5: ARG number CLOSE_ARG
                    {
                    ARG204=(Token)match(input,ARG,FOLLOW_ARG_in_tupleRef3211); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG204);

                    pushFollow(FOLLOW_number_in_tupleRef3213);
                    number205=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number205.getTree());
                    CLOSE_ARG206=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_tupleRef3215); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG206);



                    // AST REWRITE
                    // elements: number
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 407:26: -> ^( TUPLE_REF number )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:407:29: ^( TUPLE_REF number )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, stream_number.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "tupleRef"

    public static class qualifiedID_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "qualifiedID"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:409:1: qualifiedID : ID ARG number CLOSE_ARG ;
    public final StencilParser.qualifiedID_return qualifiedID() throws RecognitionException {
        StencilParser.qualifiedID_return retval = new StencilParser.qualifiedID_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID207=null;
        Token ARG208=null;
        Token CLOSE_ARG210=null;
        StencilParser.number_return number209 = null;


        Object ID207_tree=null;
        Object ARG208_tree=null;
        Object CLOSE_ARG210_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:409:13: ( ID ARG number CLOSE_ARG )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:409:15: ID ARG number CLOSE_ARG
            {
            root_0 = (Object)adaptor.nil();

            ID207=(Token)match(input,ID,FOLLOW_ID_in_qualifiedID3231); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID207_tree = (Object)adaptor.create(ID207);
            root_0 = (Object)adaptor.becomeRoot(ID207_tree, root_0);
            }
            ARG208=(Token)match(input,ARG,FOLLOW_ARG_in_qualifiedID3234); if (state.failed) return retval;
            pushFollow(FOLLOW_number_in_qualifiedID3237);
            number209=number();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, number209.getTree());
            CLOSE_ARG210=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_qualifiedID3239); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "qualifiedID"

    public static class sigil_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sigil"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:1: sigil : t= TAGGED_ID sValueList -> ^( SIGIL[$t.text] sValueList ) ;
    public final StencilParser.sigil_return sigil() throws RecognitionException {
        StencilParser.sigil_return retval = new StencilParser.sigil_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;
        StencilParser.sValueList_return sValueList211 = null;


        Object t_tree=null;
        RewriteRuleTokenStream stream_TAGGED_ID=new RewriteRuleTokenStream(adaptor,"token TAGGED_ID");
        RewriteRuleSubtreeStream stream_sValueList=new RewriteRuleSubtreeStream(adaptor,"rule sValueList");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:6: (t= TAGGED_ID sValueList -> ^( SIGIL[$t.text] sValueList ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:8: t= TAGGED_ID sValueList
            {
            t=(Token)match(input,TAGGED_ID,FOLLOW_TAGGED_ID_in_sigil3249); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TAGGED_ID.add(t);

            pushFollow(FOLLOW_sValueList_in_sigil3251);
            sValueList211=sValueList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_sValueList.add(sValueList211.getTree());


            // AST REWRITE
            // elements: sValueList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 411:31: -> ^( SIGIL[$t.text] sValueList )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:34: ^( SIGIL[$t.text] sValueList )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SIGIL, (t!=null?t.getText():null)), root_1);

                adaptor.addChild(root_1, stream_sValueList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sigil"

    public static class sValueList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sValueList"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:412:9: private sValueList : GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP ;
    public final StencilParser.sValueList_return sValueList() throws RecognitionException {
        StencilParser.sValueList_return retval = new StencilParser.sValueList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP212=null;
        Token SEPARATOR214=null;
        Token CLOSE_GROUP216=null;
        StencilParser.sValue_return sValue213 = null;

        StencilParser.sValue_return sValue215 = null;


        Object GROUP212_tree=null;
        Object SEPARATOR214_tree=null;
        Object CLOSE_GROUP216_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:412:19: ( GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:412:22: GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP212=(Token)match(input,GROUP,FOLLOW_GROUP_in_sValueList3269); if (state.failed) return retval;
            pushFollow(FOLLOW_sValue_in_sValueList3272);
            sValue213=sValue();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sValue213.getTree());
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:412:36: ( SEPARATOR sValue )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==SEPARATOR) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:412:37: SEPARATOR sValue
            	    {
            	    SEPARATOR214=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_sValueList3275); if (state.failed) return retval;
            	    pushFollow(FOLLOW_sValue_in_sValueList3278);
            	    sValue215=sValue();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, sValue215.getTree());

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            CLOSE_GROUP216=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_sValueList3282); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sValueList"

    public static class sValue_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sValue"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:9: private sValue : ( tupleRef | number | STRING );
    public final StencilParser.sValue_return sValue() throws RecognitionException {
        StencilParser.sValue_return retval = new StencilParser.sValue_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING219=null;
        StencilParser.tupleRef_return tupleRef217 = null;

        StencilParser.number_return number218 = null;


        Object STRING219_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:16: ( tupleRef | number | STRING )
            int alt53=3;
            switch ( input.LA(1) ) {
            case ARG:
            case ID:
            case 85:
                {
                alt53=1;
                }
                break;
            case NAMESPLIT:
            case DIGITS:
            case 92:
            case 93:
                {
                alt53=2;
                }
                break;
            case STRING:
                {
                alt53=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }

            switch (alt53) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:18: tupleRef
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_tupleRef_in_sValue3292);
                    tupleRef217=tupleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tupleRef217.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:29: number
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_number_in_sValue3296);
                    number218=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, number218.getTree());

                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:38: STRING
                    {
                    root_0 = (Object)adaptor.nil();

                    STRING219=(Token)match(input,STRING,FOLLOW_STRING_in_sValue3300); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING219_tree = (Object)adaptor.create(STRING219);
                    adaptor.addChild(root_0, STRING219_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sValue"

    public static class booleanOp_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "booleanOp"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:416:1: booleanOp : (t= '>' -> BOOLEAN_OP[t] | t= '>=' -> BOOLEAN_OP[t] | t= '<' -> BOOLEAN_OP[t] | t= '<=' -> BOOLEAN_OP[t] | t= '=' -> BOOLEAN_OP[t] | t= '!=' -> BOOLEAN_OP[t] | t= '=~' -> BOOLEAN_OP[t] | t= '!~' -> BOOLEAN_OP[t] );
    public final StencilParser.booleanOp_return booleanOp() throws RecognitionException {
        StencilParser.booleanOp_return retval = new StencilParser.booleanOp_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;

        Object t_tree=null;
        RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
        RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
        RewriteRuleTokenStream stream_86=new RewriteRuleTokenStream(adaptor,"token 86");
        RewriteRuleTokenStream stream_87=new RewriteRuleTokenStream(adaptor,"token 87");
        RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:417:3: (t= '>' -> BOOLEAN_OP[t] | t= '>=' -> BOOLEAN_OP[t] | t= '<' -> BOOLEAN_OP[t] | t= '<=' -> BOOLEAN_OP[t] | t= '=' -> BOOLEAN_OP[t] | t= '!=' -> BOOLEAN_OP[t] | t= '=~' -> BOOLEAN_OP[t] | t= '!~' -> BOOLEAN_OP[t] )
            int alt54=8;
            switch ( input.LA(1) ) {
            case 81:
                {
                alt54=1;
                }
                break;
            case 86:
                {
                alt54=2;
                }
                break;
            case 87:
                {
                alt54=3;
                }
                break;
            case 88:
                {
                alt54=4;
                }
                break;
            case 83:
                {
                alt54=5;
                }
                break;
            case 89:
                {
                alt54=6;
                }
                break;
            case 90:
                {
                alt54=7;
                }
                break;
            case 91:
                {
                alt54=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:417:5: t= '>'
                    {
                    t=(Token)match(input,81,FOLLOW_81_in_booleanOp3314); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_81.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 417:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:418:5: t= '>='
                    {
                    t=(Token)match(input,86,FOLLOW_86_in_booleanOp3329); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_86.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 418:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:419:5: t= '<'
                    {
                    t=(Token)match(input,87,FOLLOW_87_in_booleanOp3343); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_87.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 419:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:420:5: t= '<='
                    {
                    t=(Token)match(input,88,FOLLOW_88_in_booleanOp3358); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_88.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 420:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:421:5: t= '='
                    {
                    t=(Token)match(input,83,FOLLOW_83_in_booleanOp3372); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 421:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:422:5: t= '!='
                    {
                    t=(Token)match(input,89,FOLLOW_89_in_booleanOp3387); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 422:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:423:5: t= '=~'
                    {
                    t=(Token)match(input,90,FOLLOW_90_in_booleanOp3401); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_90.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 423:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 8 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:424:5: t= '!~'
                    {
                    t=(Token)match(input,91,FOLLOW_91_in_booleanOp3415); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_91.add(t);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 424:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "booleanOp"

    public static class passOp_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "passOp"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:427:1: passOp : ( YIELDS | GUIDE_YIELD );
    public final StencilParser.passOp_return passOp() throws RecognitionException {
        StencilParser.passOp_return retval = new StencilParser.passOp_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set220=null;

        Object set220_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:428:3: ( YIELDS | GUIDE_YIELD )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:
            {
            root_0 = (Object)adaptor.nil();

            set220=(Token)input.LT(1);
            if ( input.LA(1)==YIELDS||input.LA(1)==GUIDE_YIELD ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set220));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "passOp"

    public static class number_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "number"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:434:1: number : ( doubleNum | intNum );
    public final StencilParser.number_return number() throws RecognitionException {
        StencilParser.number_return retval = new StencilParser.number_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.doubleNum_return doubleNum221 = null;

        StencilParser.intNum_return intNum222 = null;



        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:434:9: ( doubleNum | intNum )
            int alt55=2;
            switch ( input.LA(1) ) {
            case NAMESPLIT:
                {
                alt55=1;
                }
                break;
            case DIGITS:
                {
                int LA55_2 = input.LA(2);

                if ( (LA55_2==NAMESPLIT) ) {
                    alt55=1;
                }
                else if ( (LA55_2==EOF||LA55_2==ALL||LA55_2==CANVAS||(LA55_2>=FILTER && LA55_2<=GUIDE)||(LA55_2>=LOCAL && LA55_2<=TEMPLATE)||(LA55_2>=PYTHON && LA55_2<=VIEW)||(LA55_2>=GROUP && LA55_2<=CLOSE_GROUP)||(LA55_2>=CLOSE_ARG && LA55_2<=RANGE)||(LA55_2>=SPLIT && LA55_2<=JOIN)||LA55_2==ID||LA55_2==81||LA55_2==83||(LA55_2>=86 && LA55_2<=91)) ) {
                    alt55=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 55, 2, input);

                    throw nvae;
                }
                }
                break;
            case 92:
                {
                int LA55_3 = input.LA(2);

                if ( (LA55_3==DIGITS) ) {
                    int LA55_6 = input.LA(3);

                    if ( (LA55_6==NAMESPLIT) ) {
                        alt55=1;
                    }
                    else if ( (LA55_6==EOF||LA55_6==ALL||LA55_6==CANVAS||(LA55_6>=FILTER && LA55_6<=GUIDE)||(LA55_6>=LOCAL && LA55_6<=TEMPLATE)||(LA55_6>=PYTHON && LA55_6<=VIEW)||(LA55_6>=GROUP && LA55_6<=CLOSE_GROUP)||(LA55_6>=CLOSE_ARG && LA55_6<=RANGE)||(LA55_6>=SPLIT && LA55_6<=JOIN)||LA55_6==ID||LA55_6==81||LA55_6==83||(LA55_6>=86 && LA55_6<=91)) ) {
                        alt55=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 55, 6, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 55, 3, input);

                    throw nvae;
                }
                }
                break;
            case 93:
                {
                int LA55_4 = input.LA(2);

                if ( (LA55_4==DIGITS) ) {
                    int LA55_6 = input.LA(3);

                    if ( (LA55_6==NAMESPLIT) ) {
                        alt55=1;
                    }
                    else if ( (LA55_6==EOF||LA55_6==ALL||LA55_6==CANVAS||(LA55_6>=FILTER && LA55_6<=GUIDE)||(LA55_6>=LOCAL && LA55_6<=TEMPLATE)||(LA55_6>=PYTHON && LA55_6<=VIEW)||(LA55_6>=GROUP && LA55_6<=CLOSE_GROUP)||(LA55_6>=CLOSE_ARG && LA55_6<=RANGE)||(LA55_6>=SPLIT && LA55_6<=JOIN)||LA55_6==ID||LA55_6==81||LA55_6==83||(LA55_6>=86 && LA55_6<=91)) ) {
                        alt55=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 55, 6, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 55, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;
            }

            switch (alt55) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:434:12: doubleNum
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doubleNum_in_number3457);
                    doubleNum221=doubleNum();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doubleNum221.getTree());

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:434:24: intNum
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_intNum_in_number3461);
                    intNum222=intNum();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, intNum222.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "number"

    public static class intNum_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "intNum"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:436:1: intNum : ( (n= '-' | p= '+' ) d= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] ) | d= DIGITS -> ^( NUMBER[$d.text] ) );
    public final StencilParser.intNum_return intNum() throws RecognitionException {
        StencilParser.intNum_return retval = new StencilParser.intNum_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token n=null;
        Token p=null;
        Token d=null;

        Object n_tree=null;
        Object p_tree=null;
        Object d_tree=null;
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleTokenStream stream_DIGITS=new RewriteRuleTokenStream(adaptor,"token DIGITS");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:3: ( (n= '-' | p= '+' ) d= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] ) | d= DIGITS -> ^( NUMBER[$d.text] ) )
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( ((LA57_0>=92 && LA57_0<=93)) ) {
                alt57=1;
            }
            else if ( (LA57_0==DIGITS) ) {
                alt57=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }
            switch (alt57) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:5: (n= '-' | p= '+' ) d= DIGITS
                    {
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:5: (n= '-' | p= '+' )
                    int alt56=2;
                    int LA56_0 = input.LA(1);

                    if ( (LA56_0==92) ) {
                        alt56=1;
                    }
                    else if ( (LA56_0==93) ) {
                        alt56=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 56, 0, input);

                        throw nvae;
                    }
                    switch (alt56) {
                        case 1 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:6: n= '-'
                            {
                            n=(Token)match(input,92,FOLLOW_92_in_intNum3474); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_92.add(n);


                            }
                            break;
                        case 2 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:14: p= '+'
                            {
                            p=(Token)match(input,93,FOLLOW_93_in_intNum3480); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(p);


                            }
                            break;

                    }

                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_intNum3485); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 437:30: -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:33: ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, p!=null?"+":"-" + (d!=null?d.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:438:5: d= DIGITS
                    {
                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_intNum3500); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 438:14: -> ^( NUMBER[$d.text] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:438:17: ^( NUMBER[$d.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, (d!=null?d.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "intNum"

    public static class doubleNum_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "doubleNum"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:440:1: doubleNum : ( '.' d2= DIGITS -> ^( NUMBER[\"0.\" + $d2.text] ) | d= DIGITS '.' d2= DIGITS -> ^( NUMBER[$d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] ) );
    public final StencilParser.doubleNum_return doubleNum() throws RecognitionException {
        StencilParser.doubleNum_return retval = new StencilParser.doubleNum_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token d2=null;
        Token d=null;
        Token n=null;
        Token p=null;
        Token char_literal223=null;
        Token char_literal224=null;
        Token char_literal225=null;

        Object d2_tree=null;
        Object d_tree=null;
        Object n_tree=null;
        Object p_tree=null;
        Object char_literal223_tree=null;
        Object char_literal224_tree=null;
        Object char_literal225_tree=null;
        RewriteRuleTokenStream stream_NAMESPLIT=new RewriteRuleTokenStream(adaptor,"token NAMESPLIT");
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleTokenStream stream_DIGITS=new RewriteRuleTokenStream(adaptor,"token DIGITS");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:441:3: ( '.' d2= DIGITS -> ^( NUMBER[\"0.\" + $d2.text] ) | d= DIGITS '.' d2= DIGITS -> ^( NUMBER[$d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] ) )
            int alt59=3;
            switch ( input.LA(1) ) {
            case NAMESPLIT:
                {
                alt59=1;
                }
                break;
            case DIGITS:
                {
                alt59=2;
                }
                break;
            case 92:
            case 93:
                {
                alt59=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:441:5: '.' d2= DIGITS
                    {
                    char_literal223=(Token)match(input,NAMESPLIT,FOLLOW_NAMESPLIT_in_doubleNum3517); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMESPLIT.add(char_literal223);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3521); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d2);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 441:19: -> ^( NUMBER[\"0.\" + $d2.text] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:441:22: ^( NUMBER[\"0.\" + $d2.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, "0." + (d2!=null?d2.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:442:5: d= DIGITS '.' d2= DIGITS
                    {
                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3536); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);

                    char_literal224=(Token)match(input,NAMESPLIT,FOLLOW_NAMESPLIT_in_doubleNum3538); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMESPLIT.add(char_literal224);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d2);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 442:28: -> ^( NUMBER[$d.text + \".\" + $d2.text] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:442:31: ^( NUMBER[$d.text + \".\" + $d2.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, (d!=null?d.getText():null) + "." + (d2!=null?d2.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:5: (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS
                    {
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:5: (n= '-' | p= '+' )
                    int alt58=2;
                    int LA58_0 = input.LA(1);

                    if ( (LA58_0==92) ) {
                        alt58=1;
                    }
                    else if ( (LA58_0==93) ) {
                        alt58=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 58, 0, input);

                        throw nvae;
                    }
                    switch (alt58) {
                        case 1 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:6: n= '-'
                            {
                            n=(Token)match(input,92,FOLLOW_92_in_doubleNum3558); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_92.add(n);


                            }
                            break;
                        case 2 :
                            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:14: p= '+'
                            {
                            p=(Token)match(input,93,FOLLOW_93_in_doubleNum3564); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(p);


                            }
                            break;

                    }

                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3569); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);

                    char_literal225=(Token)match(input,NAMESPLIT,FOLLOW_NAMESPLIT_in_doubleNum3571); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMESPLIT.add(char_literal225);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3575); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d2);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 443:44: -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] )
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:47: ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, p!=null?"+":"-" + (d!=null?d.getText():null) + "." + (d2!=null?d2.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "doubleNum"

    // $ANTLR start synpred1_Stencil
    public final void synpred1_Stencil_fragment() throws RecognitionException {   
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:5: ( callChain SPLIT )
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:274:6: callChain SPLIT
        {
        pushFollow(FOLLOW_callChain_in_synpred1_Stencil1787);
        callChain();

        state._fsp--;
        if (state.failed) return ;
        match(input,SPLIT,FOLLOW_SPLIT_in_synpred1_Stencil1789); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_Stencil

    // $ANTLR start synpred2_Stencil
    public final void synpred2_Stencil_fragment() throws RecognitionException {   
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:5: ( functionCall passOp )
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:6: functionCall passOp
        {
        pushFollow(FOLLOW_functionCall_in_synpred2_Stencil1913);
        functionCall();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_passOp_in_synpred2_Stencil1915);
        passOp();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_Stencil

    // $ANTLR start synpred3_Stencil
    public final void synpred3_Stencil_fragment() throws RecognitionException {   
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:5: ( PYTHON ARG )
        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:6: PYTHON ARG
        {
        match(input,PYTHON,FOLLOW_PYTHON_in_synpred3_Stencil2233); if (state.failed) return ;
        match(input,ARG,FOLLOW_ARG_in_synpred3_Stencil2235); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_Stencil

    // Delegated rules

    public final boolean synpred1_Stencil() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_Stencil_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Stencil() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Stencil_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_Stencil() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_Stencil_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA29 dfa29 = new DFA29(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA31 dfa31 = new DFA31(this);
    protected DFA39 dfa39 = new DFA39(this);
    protected DFA41 dfa41 = new DFA41(this);
    protected DFA47 dfa47 = new DFA47(this);
    static final String DFA29_eotS =
        "\17\uffff";
    static final String DFA29_eofS =
        "\17\uffff";
    static final String DFA29_minS =
        "\1\40\14\0\2\uffff";
    static final String DFA29_maxS =
        "\1\135\14\0\2\uffff";
    static final String DFA29_acceptS =
        "\15\uffff\1\1\1\2";
    static final String DFA29_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\2\uffff}>";
    static final String[] DFA29_transitionS = {
            "\1\13\3\uffff\1\12\21\uffff\1\14\1\uffff\1\3\4\uffff\1\5\12"+
            "\uffff\1\1\1\uffff\1\4\1\11\1\6\10\uffff\1\2\6\uffff\1\7\1\10",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA29_eot = DFA.unpackEncodedString(DFA29_eotS);
    static final short[] DFA29_eof = DFA.unpackEncodedString(DFA29_eofS);
    static final char[] DFA29_min = DFA.unpackEncodedStringToUnsignedChars(DFA29_minS);
    static final char[] DFA29_max = DFA.unpackEncodedStringToUnsignedChars(DFA29_maxS);
    static final short[] DFA29_accept = DFA.unpackEncodedString(DFA29_acceptS);
    static final short[] DFA29_special = DFA.unpackEncodedString(DFA29_specialS);
    static final short[][] DFA29_transition;

    static {
        int numStates = DFA29_transitionS.length;
        DFA29_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA29_transition[i] = DFA.unpackEncodedString(DFA29_transitionS[i]);
        }
    }

    class DFA29 extends DFA {

        public DFA29(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 29;
            this.eot = DFA29_eot;
            this.eof = DFA29_eof;
            this.min = DFA29_min;
            this.max = DFA29_max;
            this.accept = DFA29_accept;
            this.special = DFA29_special;
            this.transition = DFA29_transition;
        }
        public String getDescription() {
            return "273:1: callGroup : ( ( callChain SPLIT )=> callChain ( SPLIT callChain )+ JOIN callChain -> ^( CALL_GROUP ( callChain )+ ) | callChain -> ^( CALL_GROUP callChain ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA29_1 = input.LA(1);

                         
                        int index29_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA29_2 = input.LA(1);

                         
                        int index29_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA29_3 = input.LA(1);

                         
                        int index29_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA29_4 = input.LA(1);

                         
                        int index29_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA29_5 = input.LA(1);

                         
                        int index29_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA29_6 = input.LA(1);

                         
                        int index29_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA29_7 = input.LA(1);

                         
                        int index29_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA29_8 = input.LA(1);

                         
                        int index29_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA29_9 = input.LA(1);

                         
                        int index29_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA29_10 = input.LA(1);

                         
                        int index29_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA29_11 = input.LA(1);

                         
                        int index29_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA29_12 = input.LA(1);

                         
                        int index29_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Stencil()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index29_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 29, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA30_eotS =
        "\135\uffff";
    static final String DFA30_eofS =
        "\1\uffff\1\2\22\uffff\1\4\10\uffff\1\4\77\uffff";
    static final String DFA30_minS =
        "\2\40\1\uffff\1\40\1\uffff\1\40\2\uffff\2\67\1\75\1\66\1\114\1\67"+
        "\2\114\3\67\2\40\1\114\1\71\2\114\1\70\1\67\1\114\1\67\1\40\1\67"+
        "\1\71\1\114\1\67\1\71\2\67\1\75\1\114\1\67\2\114\2\67\1\114\1\71"+
        "\1\114\1\70\1\67\1\114\1\71\2\114\1\67\1\114\2\67\1\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\2\114\1\71\2\114"+
        "\1\67\1\114\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114"+
        "\2\71\1\114\1\67\1\71";
    static final String DFA30_maxS =
        "\1\135\1\110\1\uffff\1\135\1\uffff\1\135\2\uffff\2\133\1\135\1\66"+
        "\1\114\1\133\2\114\3\133\1\135\1\110\1\114\1\75\2\114\1\135\1\133"+
        "\1\114\1\133\1\110\1\72\1\71\1\114\1\133\1\75\2\72\1\135\1\114\1"+
        "\75\2\114\1\72\1\133\1\114\1\71\1\114\1\135\1\133\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\133\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71"+
        "\1\114\1\72\1\75\1\72\2\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\71\1\114\1\72\1\75\1\72\1\114\2\71\1\114\1\72\1\71";
    static final String DFA30_acceptS =
        "\2\uffff\1\1\1\uffff\1\4\1\uffff\1\2\1\3\125\uffff";
    static final String DFA30_specialS =
        "\135\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\2\3\uffff\1\2\21\uffff\1\3\1\uffff\1\2\4\uffff\1\2\12\uffff"+
            "\1\1\1\uffff\3\2\10\uffff\1\2\6\uffff\2\2",
            "\1\2\1\uffff\1\2\3\uffff\4\2\1\uffff\4\2\1\uffff\4\2\2\uffff"+
            "\1\5\1\uffff\1\4\3\uffff\1\4\10\uffff\2\2\1\uffff\1\2",
            "",
            "\1\7\3\uffff\1\7\22\uffff\1\6\1\7\4\uffff\1\7\12\uffff\1\7"+
            "\1\uffff\3\7\10\uffff\1\7\6\uffff\2\7",
            "",
            "\1\22\3\uffff\1\21\22\uffff\1\2\1\12\4\uffff\1\14\12\uffff"+
            "\1\10\1\uffff\1\13\1\20\1\15\10\uffff\1\11\6\uffff\1\16\1\17",
            "",
            "",
            "\1\24\2\uffff\1\23\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\25\16\uffff\1\26\17\uffff\1\27\1\30",
            "\1\31",
            "\1\32",
            "\1\4\2\uffff\1\4\2\uffff\1\33\23\uffff\1\2\1\uffff\1\2\2\uffff"+
            "\6\2",
            "\1\34",
            "\1\34",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\35\2\uffff\1\4\11\uffff\1\2\14\uffff\1\2\1\uffff\1\2\2\uffff"+
            "\6\2",
            "\1\4\3\uffff\1\4\23\uffff\1\4\4\uffff\1\4\12\uffff\1\36\1\uffff"+
            "\3\4\10\uffff\1\4\6\uffff\2\4",
            "\1\4\1\uffff\1\4\3\uffff\4\4\1\uffff\4\4\1\uffff\4\4\2\uffff"+
            "\1\4\7\uffff\2\2\1\4\2\uffff\1\4\1\uffff\2\4\1\uffff\1\4",
            "\1\37",
            "\1\41\3\uffff\1\40",
            "\1\42",
            "\1\42",
            "\1\45\4\uffff\1\46\12\uffff\1\43\2\uffff\1\52\1\47\10\uffff"+
            "\1\44\6\uffff\1\50\1\51",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\53",
            "\1\4\2\uffff\1\4\2\uffff\1\54\23\uffff\1\2\1\uffff\1\2\2\uffff"+
            "\6\2",
            "\1\4\1\uffff\1\4\3\uffff\4\4\1\uffff\4\4\1\uffff\4\4\2\uffff"+
            "\1\4\11\uffff\1\4\2\uffff\1\4\1\2\2\4\1\uffff\1\4",
            "\1\24\2\uffff\1\23",
            "\1\41",
            "\1\55",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\41\3\uffff\1\56",
            "\1\60\2\uffff\1\57",
            "\1\60\2\uffff\1\57",
            "\1\61\16\uffff\1\62\17\uffff\1\63\1\64",
            "\1\65",
            "\1\60\2\uffff\1\57\2\uffff\1\66",
            "\1\67",
            "\1\67",
            "\1\60\2\uffff\1\57",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\70",
            "\1\41",
            "\1\71",
            "\1\74\4\uffff\1\75\12\uffff\1\72\2\uffff\1\101\1\76\10\uffff"+
            "\1\73\6\uffff\1\77\1\100",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\102",
            "\1\104\3\uffff\1\103",
            "\1\105",
            "\1\105",
            "\1\60\2\uffff\1\57",
            "\1\106",
            "\1\60\2\uffff\1\57\2\uffff\1\107",
            "\1\4\2\uffff\1\4\26\uffff\1\2\1\uffff\1\2\2\uffff\6\2",
            "\1\41",
            "\1\60\2\uffff\1\57",
            "\1\60\2\uffff\1\57",
            "\1\110\16\uffff\1\111\17\uffff\1\112\1\113",
            "\1\114",
            "\1\60\2\uffff\1\57\2\uffff\1\115",
            "\1\116",
            "\1\116",
            "\1\60\2\uffff\1\57",
            "\1\104",
            "\1\117",
            "\1\60\2\uffff\1\57",
            "\1\104\3\uffff\1\120",
            "\1\60\2\uffff\1\57",
            "\1\121",
            "\1\122",
            "\1\124\3\uffff\1\123",
            "\1\125",
            "\1\125",
            "\1\60\2\uffff\1\57",
            "\1\126",
            "\1\60\2\uffff\1\57\2\uffff\1\127",
            "\1\104",
            "\1\130",
            "\1\60\2\uffff\1\57",
            "\1\124",
            "\1\131",
            "\1\60\2\uffff\1\57",
            "\1\124\3\uffff\1\132",
            "\1\60\2\uffff\1\57",
            "\1\133",
            "\1\104",
            "\1\124",
            "\1\134",
            "\1\60\2\uffff\1\57",
            "\1\124"
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "281:1: callTarget : ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget );";
        }
    }
    static final String DFA31_eotS =
        "\u08f5\uffff";
    static final String DFA31_eofS =
        "\52\uffff\1\156\u08ca\uffff";
    static final String DFA31_minS =
        "\1\110\1\66\1\110\2\40\1\66\1\114\1\71\2\114\1\73\2\66\4\71\1\110"+
        "\2\67\1\75\1\66\1\114\1\67\2\114\3\67\1\71\1\114\1\75\1\40\1\71"+
        "\1\124\1\70\2\40\1\66\1\75\1\71\2\40\1\114\1\71\2\114\1\70\1\67"+
        "\1\114\1\67\2\71\1\114\1\71\2\114\1\66\1\114\1\71\2\114\3\71\1\123"+
        "\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\66\1\114\1\71\2\114"+
        "\3\71\1\66\1\114\1\71\2\114\3\71\1\123\1\114\1\73\2\114\1\73\2\67"+
        "\1\75\1\66\1\114\1\67\2\114\3\67\2\uffff\1\71\1\114\1\67\1\71\2"+
        "\67\1\75\1\114\1\67\2\114\2\67\1\114\1\57\1\40\1\66\1\71\1\114\1"+
        "\71\1\70\1\71\1\114\1\71\1\40\1\71\1\70\1\71\1\114\1\71\2\114\1"+
        "\67\1\114\1\67\1\70\1\71\1\114\1\110\1\71\1\70\1\71\1\114\1\40\1"+
        "\71\1\40\1\73\1\114\1\75\1\73\1\124\1\114\1\71\2\114\1\70\1\67\1"+
        "\114\1\67\1\71\1\114\1\70\1\67\1\114\1\71\2\114\1\67\1\114\2\67"+
        "\1\71\1\110\1\66\1\114\1\71\2\114\3\71\1\123\1\71\1\114\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\66\1\114\1\71\2\114\3\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\66\1\114\1\71\2\114\3\71\1\123\1\114\1\66\1\114"+
        "\1\71\2\114\3\71\1\73\1\71\1\114\1\71\3\114\2\71\1\114\1\67\1\71"+
        "\2\67\1\75\1\114\1\67\2\114\2\67\1\114\1\71\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\40\1\66\1\71\1\70"+
        "\1\71\1\114\1\40\1\71\1\40\1\71\1\70\1\71\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\71\1\70\1\71\1\114\1\110\1\71\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114"+
        "\1\67\1\40\1\71\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\70"+
        "\1\71\1\114\1\71\1\40\1\71\1\70\1\71\1\114\1\110\1\71\1\40\1\66"+
        "\1\71\1\114\1\71\1\73\1\71\1\114\1\70\1\67\1\114\1\71\2\114\1\67"+
        "\1\114\2\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\66"+
        "\1\114\1\71\2\114\3\71\1\123\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\123\1\66\1\114\1\71\2\114\3\71\1\114\1\66\1\114\1\71\2\114\3"+
        "\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1"+
        "\114\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114\3\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\66\1\114\1\71\2\114\3\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\1\66\1\114\1\71\2\114"+
        "\3\71\1\123\1\71\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\114\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114\1\71"+
        "\1\70\1\71\1\114\1\40\1\71\1\40\1\70\1\71\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\40\1\70\1\71\1\114\2\71\1\70\1\71\1\114\1\110\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\40\2\71\1\114\1\67\1\114\1\71\2\114"+
        "\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\1\114\1\71"+
        "\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114"+
        "\1\67\1\114\1\67\1\71\1\70\1\71\1\114\1\110\1\71\1\70\1\71\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\40\1\71\1\70\1\71\1\114\1\40\1\71"+
        "\1\40\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\71"+
        "\1\114\3\67\1\75\1\114\1\67\2\114\1\67\1\71\1\66\1\114\1\71\2\114"+
        "\3\71\1\123\1\114\1\66\1\114\1\71\2\114\3\71\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114"+
        "\3\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\2\67\1\75\1\114"+
        "\1\67\2\114\1\67\1\71\1\123\1\114\1\71\1\114\1\67\1\71\1\67\1\114"+
        "\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67"+
        "\1\114\1\66\1\114\1\71\2\114\5\71\1\114\1\67\1\71\1\67\1\114\1\71"+
        "\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\71\1\114\1\67\1\71"+
        "\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67"+
        "\1\71\1\67\1\114\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114"+
        "\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114"+
        "\1\66\1\114\1\71\2\114\3\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\123\1\66\1\114\1\71\2\114\3\71\1\114\1\66\1\114\1\71\2\114\4"+
        "\71\1\114\1\67\1\71\1\67\1\114\2\71\1\70\1\71\1\114\1\71\2\114\1"+
        "\67\1\114\1\67\1\70\1\71\1\114\1\71\1\40\1\71\1\70\1\71\1\114\1"+
        "\110\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70"+
        "\1\71\1\114\1\110\1\71\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67"+
        "\1\71\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\40\2\71\1\114"+
        "\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71"+
        "\1\114\2\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67"+
        "\2\71\1\114\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67"+
        "\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\40\1\71\1\114\1\71"+
        "\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\1\70"+
        "\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\40\1\70\1\71\1\114\2\71"+
        "\1\70\1\71\1\114\1\110\2\71\1\114\3\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\67\1\71\1\67\1\114\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\66\1\114\1\71\2\114\3\71\2\67\1\75\1\114\1\67\2\114"+
        "\1\67\1\71\1\123\1\114\1\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114"+
        "\5\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114"+
        "\1\67\1\71\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\2\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\66"+
        "\1\114\1\71\2\114\4\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114\3\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\114\2\67\1\75\1\114\1\67\2\114"+
        "\1\67\1\71\1\123\1\114\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71"+
        "\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\70"+
        "\1\71\1\114\1\110\1\71\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67"+
        "\1\40\2\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67"+
        "\1\40\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\2\71"+
        "\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\2\71"+
        "\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\110"+
        "\1\71\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\70\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\1\40\2\71\1\114\1\67\1\71\1\67"+
        "\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71"+
        "\1\67\1\114\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\66"+
        "\1\114\1\71\2\114\4\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114\4\71\1\114\1\67\1\71"+
        "\1\67\1\114\2\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114"+
        "\1\67\2\114\1\67\1\71\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114"+
        "\3\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114"+
        "\1\67\1\71\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67"+
        "\1\75\1\114\1\67\2\114\1\67\1\71\1\123\1\114\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\2\67\1\75\1\114\1\67"+
        "\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114"+
        "\4\71\1\114\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67"+
        "\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\40\1\71\1\114\1\71"+
        "\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\1\114"+
        "\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\2\71"+
        "\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114"+
        "\1\67\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\71"+
        "\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114"+
        "\1\67\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\71"+
        "\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\40\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\114\1\71\2\114"+
        "\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\3\71\1\114\1\67"+
        "\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114\4\71\1\114\1\67\1\71"+
        "\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\71"+
        "\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\2\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67"+
        "\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114\3\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\71\1\114"+
        "\1\67\1\71\1\67\1\114\2\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\114\1\67\1\71\1\67\1\114\1\66\1\114\1\71\2\114\4\71\1\114\1\67"+
        "\1\71\1\67\1\114\2\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\1\71\1\114\1\67\1\114\1\71\2\114"+
        "\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\2\71\1\114\1\67"+
        "\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\2\71\1\114\1\67\1\70"+
        "\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\71\1\114\1\67\1\70\1\71\1\114\2\71\1\114\1\67\1\71"+
        "\1\114\1\67\1\70\1\71\1\114\1\71\2\114\1\67\1\114\1\67\3\71\1\114"+
        "\1\67\1\71\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71"+
        "\1\67\1\114\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67"+
        "\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114\3\71\1\114\1\67"+
        "\1\71\1\67\1\114\3\71\1\114\1\67\1\71\1\67\1\114\1\71\2\67\1\75"+
        "\1\114\1\67\2\114\1\67\1\71\1\114\2\71\2\67\1\75\1\114\1\67\2\114"+
        "\1\67\1\71\1\114\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\70\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\71\2\114\1\67\1\114"+
        "\1\67\1\71\1\114\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\70\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\71\2\114\1\67\1\114"+
        "\1\67\1\71\1\114\1\67\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71"+
        "\1\114\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114\2\71"+
        "\1\114\1\67\1\71\1\67\1\114\4\71\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\67\1\71\1\67\1\114\1\71\1\114\1\67\1\71\1\67\1\114"+
        "\1\71\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\114\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114"+
        "\1\67\1\71\1\114\1\67\1\71\1\114\1\67\1\71\1\67\1\114\4\71\1\114"+
        "\1\67\1\71\1\67\1\114\3\71\1\114\1\67\1\71\1\114\1\67\2\71";
    static final String DFA31_maxS =
        "\1\110\1\74\1\110\2\135\1\70\1\114\1\75\2\114\1\73\2\66\3\72\1\123"+
        "\1\110\2\72\1\135\1\66\1\114\1\75\2\114\3\72\1\73\1\114\2\135\1"+
        "\75\1\124\3\135\1\66\1\135\1\105\1\135\1\110\1\114\1\75\2\114\1"+
        "\135\1\72\1\114\1\75\1\73\1\105\1\114\1\105\2\114\1\66\1\114\1\75"+
        "\2\114\3\72\1\123\1\114\1\105\2\72\1\135\1\114\1\75\2\114\1\72\1"+
        "\66\1\114\1\75\2\114\3\72\1\66\1\114\1\75\2\114\3\72\1\123\1\114"+
        "\1\75\2\114\1\73\2\72\1\135\1\66\1\114\1\75\2\114\3\72\2\uffff\1"+
        "\71\1\114\1\72\1\75\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\110"+
        "\1\135\1\66\1\105\1\114\1\105\1\135\1\72\1\114\1\75\1\135\1\73\1"+
        "\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\114\1\110"+
        "\1\75\1\135\1\72\1\114\1\135\1\75\1\135\1\73\1\114\1\135\1\75\1"+
        "\124\1\114\1\75\2\114\1\135\1\72\1\114\1\75\1\71\1\114\1\135\1\72"+
        "\1\114\1\75\2\114\1\72\1\114\1\75\2\72\1\110\1\66\1\114\1\75\2\114"+
        "\3\72\1\123\1\105\1\114\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1"+
        "\66\1\114\1\75\2\114\5\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114"+
        "\1\72\1\75\1\72\1\114\2\72\1\135\1\114\1\75\2\114\2\72\1\123\1\114"+
        "\2\72\1\135\1\114\1\75\2\114\2\72\1\66\1\114\1\75\2\114\3\72\1\123"+
        "\1\114\1\66\1\114\1\75\2\114\3\72\1\73\1\72\1\114\1\75\3\114\1\72"+
        "\1\71\1\114\1\72\1\75\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\135\1\66\1\72\1\135\1\72\1\114\1\135\1\75\1\135\1\105\1\135\1"+
        "\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\135\1\72\1\114\1\110"+
        "\1\75\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72"+
        "\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\135\1\72\1\114\1"+
        "\75\2\114\1\72\1\114\1\75\1\135\1\72\1\114\1\75\1\135\1\72\1\135"+
        "\1\72\1\114\1\110\1\75\1\135\1\66\1\72\1\114\1\75\1\73\1\71\1\114"+
        "\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\71\1\114\1\72\1\66\1\114\1\75\2\114\3\72\1\123"+
        "\2\72\1\135\1\114\1\75\2\114\2\72\1\123\1\66\1\114\1\75\2\114\3"+
        "\72\1\114\1\66\1\114\1\75\2\114\5\72\1\135\1\114\1\75\2\114\1\72"+
        "\1\71\1\114\1\72\1\75\1\72\1\114\2\72\1\135\1\114\1\75\2\114\2\72"+
        "\1\123\1\114\1\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114"+
        "\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66\1\114\1\75"+
        "\2\114\5\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\66\1\114\1\75\2"+
        "\114\5\72\1\135\1\114\1\75\2\114\2\72\1\123\1\114\1\66\1\114\1\75"+
        "\2\114\3\72\1\123\1\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\1"+
        "\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71\1\114\1\72\1\75\1\72\1"+
        "\114\1\71\1\135\1\72\1\114\1\135\1\75\2\135\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\2\135\1\72\1\114\1\75\1\72\1\135\1\72\1\114\1\110"+
        "\1\75\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72"+
        "\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\71\1\114\1\72\1\114"+
        "\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75"+
        "\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114"+
        "\1\75\2\114\1\72\1\114\1\75\1\72\1\135\1\72\1\114\1\110\1\75\1\135"+
        "\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\135\1\72\1\114"+
        "\1\135\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\71\1\114\3\72\1\135\1\114\1\75\2\114\2\72\1\66\1\114\1\75"+
        "\2\114\3\72\1\123\1\114\1\66\1\114\1\75\2\114\5\72\1\135\1\114\1"+
        "\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66\1\114\1\75\2"+
        "\114\5\72\1\135\1\114\1\75\2\114\2\72\1\114\2\72\1\135\1\114\1\75"+
        "\2\114\2\72\1\123\1\114\1\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72"+
        "\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66"+
        "\1\114\1\75\2\114\3\72\2\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72"+
        "\1\135\1\114\1\75\2\114\2\72\1\114\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\2\72\1\135\1\114\1\75\2\114\2\72\1\123\1\114\2\72\1\135\1"+
        "\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66\1\114"+
        "\1\75\2\114\5\72\1\135\1\114\1\75\2\114\2\72\1\123\1\66\1\114\1"+
        "\75\2\114\3\72\1\114\1\66\1\114\1\75\2\114\3\72\1\71\1\114\1\72"+
        "\1\75\1\72\1\114\2\71\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75"+
        "\1\135\1\72\1\114\1\75\1\135\1\72\1\135\1\72\1\114\1\110\1\75\1"+
        "\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114"+
        "\1\110\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\135"+
        "\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\71\1\114\1\72"+
        "\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114"+
        "\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75"+
        "\1\72\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\114"+
        "\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75"+
        "\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\2\135\1\72\1\114\1"+
        "\75\1\72\1\135\1\72\1\114\1\110\1\75\1\71\1\114\3\72\1\135\1\114"+
        "\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\2\72\1\135\1\114"+
        "\1\75\2\114\2\72\1\114\1\66\1\114\1\75\2\114\5\72\1\135\1\114\1"+
        "\75\2\114\2\72\1\123\1\114\1\71\1\114\1\72\1\75\1\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\2\72\1\123\1\114\2\72\1\135\1\114\1"+
        "\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\2\72\1\135\1\114"+
        "\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66\1\114\1\75"+
        "\2\114\3\72\2\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114"+
        "\1\75\2\114\2\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71"+
        "\1\114\1\72\1\75\1\72\1\114\2\71\1\114\1\72\1\75\1\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\66\1\114\1\75\2\114\3\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\2\72\1\114\2\72\1\135\1\114\1\75\2"+
        "\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66\1\114\1\75\2\114"+
        "\5\72\1\135\1\114\1\75\2\114\2\72\1\114\2\72\1\135\1\114\1\75\2"+
        "\114\2\72\1\123\1\114\1\71\1\114\1\75\2\114\1\72\1\114\1\75\1\71"+
        "\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\135"+
        "\1\72\1\114\1\110\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1"+
        "\75\1\135\1\72\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72"+
        "\1\114\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72"+
        "\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72\1\114"+
        "\1\75\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\71"+
        "\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135"+
        "\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72"+
        "\1\114\1\75\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72"+
        "\1\135\1\72\1\114\1\110\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1"+
        "\114\1\75\1\72\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\135"+
        "\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75"+
        "\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\2\72\1\135\1\114\1\75"+
        "\2\114\2\72\1\123\1\114\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1"+
        "\114\1\72\1\75\1\72\1\114\1\66\1\114\1\75\2\114\3\72\1\71\2\72\1"+
        "\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\66"+
        "\1\114\1\75\2\114\3\72\1\71\1\114\1\72\1\75\1\72\1\114\2\71\1\114"+
        "\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72\1\114"+
        "\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\71\1\114\1\72\1\75\1\72\1\114\3\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\71\2\72\1\135"+
        "\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71\1\114"+
        "\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72\1\123"+
        "\1\114\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\66\1\114\1\75\2\114\3\72\1\71\1\114\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114\1\72"+
        "\1\114\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\135\1\72\1\114\1\75\1\114\1\75\2\114\1\72\1\114\1\75\1\71"+
        "\1\114\1\72\1\135\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114\1\72"+
        "\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114\1\72\1\71\1\114\1\72"+
        "\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114\1\72\1\135\1\72\1\114"+
        "\1\75\2\114\1\72\1\114\1\75\1\135\1\72\1\114\1\75\2\114\1\72\1\114"+
        "\1\75\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\135\1\72\1\114\1\75\2\71\1\114\1\72\1\75\1\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\66\1\114\1\75\2\114\3\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72\1\114\2\71\2\72\1\135"+
        "\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114\1\71\1\114"+
        "\1\72\1\75\1\72\1\114\3\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71"+
        "\1\114\1\72\1\75\1\72\1\114\1\71\1\114\1\72\1\75\1\72\1\114\2\71"+
        "\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\66\1\114\1\75\2\114\3\72\1\71\1\114\1\72\1\75\1\72\1\114\2\71"+
        "\1\114\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72"+
        "\1\114\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\135\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114\1\75"+
        "\2\114\1\72\1\114\1\75\1\72\1\71\1\114\1\72\1\135\1\72\1\114\1\75"+
        "\2\114\1\72\1\114\1\75\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71"+
        "\1\114\1\72\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71"+
        "\1\114\1\72\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71"+
        "\1\114\1\72\1\135\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114\1\72"+
        "\1\135\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\72\2\71\1\114\1\72"+
        "\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75\2\114\2\72\1\114\1\71"+
        "\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\71\1\114\1\72\1\75\1\72\1\114\3\71\1\114\1\72\1\75\1\72"+
        "\1\114\3\71\1\114\1\72\1\75\1\72\1\114\1\71\2\72\1\135\1\114\1\75"+
        "\2\114\2\72\1\114\2\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114"+
        "\1\72\1\75\1\72\1\114\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114"+
        "\1\72\1\71\1\114\1\72\1\71\1\114\1\72\1\135\1\72\1\114\1\75\2\114"+
        "\1\72\1\114\1\75\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75"+
        "\1\72\1\114\1\71\1\114\1\72\1\75\1\72\1\114\2\71\1\114\1\72\1\75"+
        "\1\72\1\114\4\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72"+
        "\1\75\1\72\1\114\1\71\1\114\1\72\1\75\1\72\1\114\1\71\1\114\1\75"+
        "\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114\1\72\1\71\1\114"+
        "\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\71\1\114"+
        "\1\72\1\71\1\114\1\72\1\75\1\72\1\114\4\71\1\114\1\72\1\75\1\72"+
        "\1\114\3\71\1\114\1\72\1\71\1\114\1\72\2\71";
    static final String DFA31_acceptS =
        "\155\uffff\1\1\1\2\u0886\uffff";
    static final String DFA31_specialS =
        "\52\uffff\1\0\u08ca\uffff}>";
    static final String[] DFA31_transitionS = {
            "\1\1",
            "\1\4\1\uffff\1\3\3\uffff\1\2",
            "\1\5",
            "\1\17\3\uffff\1\16\12\uffff\1\21\11\uffff\1\13\3\uffff\1\6"+
            "\12\uffff\1\20\1\uffff\1\14\1\15\1\7\7\uffff\1\12\7\uffff\1"+
            "\10\1\11",
            "\1\34\3\uffff\1\33\23\uffff\1\24\4\uffff\1\26\12\uffff\1\22"+
            "\1\uffff\1\25\1\32\1\27\10\uffff\1\23\6\uffff\1\30\1\31",
            "\1\4\1\uffff\1\3",
            "\1\35",
            "\1\13\1\40\1\37\1\uffff\1\36",
            "\1\41",
            "\1\41",
            "\1\42",
            "\1\4",
            "\1\43",
            "\1\13\1\40",
            "\1\13\1\40",
            "\1\13\1\40",
            "\1\46\1\45\12\uffff\1\47\15\uffff\1\44",
            "\1\50",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "\1\53\16\uffff\1\54\17\uffff\1\55\1\56",
            "\1\57",
            "\1\60",
            "\1\52\2\uffff\1\51\2\uffff\1\61",
            "\1\62",
            "\1\62",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "\1\13\1\40\1\37",
            "\1\63",
            "\1\65\16\uffff\1\66\7\uffff\1\64\7\uffff\1\67\1\70",
            "\1\100\3\uffff\1\77\30\uffff\1\72\12\uffff\1\101\1\uffff\1"+
            "\71\1\76\1\73\17\uffff\1\74\1\75",
            "\1\13\1\40\1\37\1\uffff\1\102",
            "\1\103",
            "\1\106\4\uffff\1\107\12\uffff\1\104\2\uffff\1\113\1\110\10"+
            "\uffff\1\105\6\uffff\1\111\1\112",
            "\1\123\3\uffff\1\122\30\uffff\1\115\14\uffff\1\114\1\121\1"+
            "\116\17\uffff\1\117\1\120",
            "\1\133\3\uffff\1\132\24\uffff\1\46\3\uffff\1\125\12\uffff\1"+
            "\134\1\uffff\1\124\1\131\1\126\17\uffff\1\127\1\130",
            "\1\4",
            "\1\135\16\uffff\1\136\7\uffff\1\141\7\uffff\1\137\1\140",
            "\1\46\1\45\12\uffff\1\47",
            "\1\154\3\uffff\1\153\23\uffff\1\144\4\uffff\1\146\12\uffff"+
            "\1\142\1\uffff\1\145\1\152\1\147\10\uffff\1\143\6\uffff\1\150"+
            "\1\151",
            "\1\156\1\uffff\1\156\3\uffff\4\156\1\uffff\4\156\1\uffff\4"+
            "\156\2\uffff\1\156\11\uffff\1\155\2\uffff\1\155\1\uffff\2\156"+
            "\1\uffff\1\156",
            "\1\157",
            "\1\161\3\uffff\1\160",
            "\1\162",
            "\1\162",
            "\1\165\4\uffff\1\166\12\uffff\1\163\2\uffff\1\172\1\167\10"+
            "\uffff\1\164\6\uffff\1\170\1\171",
            "\1\52\2\uffff\1\51",
            "\1\173",
            "\1\52\2\uffff\1\51\2\uffff\1\174",
            "\1\13\1\40\1\37",
            "\1\177\1\176\12\uffff\1\175",
            "\1\u0080",
            "\1\177\1\176\2\uffff\1\u0081\7\uffff\1\175",
            "\1\u0082",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\13\1\40\2\uffff\1\u0085",
            "\1\u0086",
            "\1\u0086",
            "\1\13\1\40",
            "\1\13\1\40",
            "\1\13\1\40",
            "\1\u0087",
            "\1\u0088",
            "\1\177\1\176\12\uffff\1\175",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u008b\16\uffff\1\u008c\17\uffff\1\u008d\1\u008e",
            "\1\u008f",
            "\1\u008a\2\uffff\1\u0089\2\uffff\1\u0090",
            "\1\u0091",
            "\1\u0091",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u0092",
            "\1\u0093",
            "\1\13\1\u0095\2\uffff\1\u0094",
            "\1\u0096",
            "\1\u0096",
            "\1\13\1\u0095",
            "\1\13\1\u0095",
            "\1\13\1\u0095",
            "\1\u0097",
            "\1\u0098",
            "\1\46\1\u009a\2\uffff\1\u0099",
            "\1\u009b",
            "\1\u009b",
            "\1\46\1\u009a",
            "\1\46\1\u009a",
            "\1\46\1\u009a",
            "\1\u009c",
            "\1\u009d",
            "\1\u009f\1\uffff\1\u009e",
            "\1\u00a0",
            "\1\u00a0",
            "\1\u00a1",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "\1\u00a2\16\uffff\1\u00a3\17\uffff\1\u00a4\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\52\2\uffff\1\51\2\uffff\1\u00a8",
            "\1\u00a9",
            "\1\u00a9",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "\1\52\2\uffff\1\51",
            "",
            "",
            "\1\161",
            "\1\u00aa",
            "\1\52\2\uffff\1\51",
            "\1\161\3\uffff\1\u00ab",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u00ae\16\uffff\1\u00af\17\uffff\1\u00b0\1\u00b1",
            "\1\u00b2",
            "\1\u00ad\2\uffff\1\u00ac\2\uffff\1\u00b3",
            "\1\u00b4",
            "\1\u00b4",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\52\2\uffff\1\51",
            "\1\u00b5",
            "\1\u00b7\30\uffff\1\u00b6",
            "\1\u00bf\3\uffff\1\u00be\24\uffff\1\177\3\uffff\1\u00b9\12"+
            "\uffff\1\u00c0\1\uffff\1\u00b8\1\u00bd\1\u00ba\17\uffff\1\u00bb"+
            "\1\u00bc",
            "\1\4",
            "\1\177\1\176\12\uffff\1\175",
            "\1\u00c1",
            "\1\177\1\176\2\uffff\1\u00c2\7\uffff\1\175",
            "\1\u00c5\4\uffff\1\u00c6\12\uffff\1\u00c3\2\uffff\1\u00ca\1"+
            "\u00c7\10\uffff\1\u00c4\6\uffff\1\u00c8\1\u00c9",
            "\1\13\1\40",
            "\1\u00cb",
            "\1\13\1\40\2\uffff\1\u00cc",
            "\1\u00d4\3\uffff\1\u00d3\30\uffff\1\u00ce\14\uffff\1\u00cd"+
            "\1\u00d2\1\u00cf\17\uffff\1\u00d0\1\u00d1",
            "\1\13\1\40\1\37",
            "\1\u00d7\4\uffff\1\u00d8\12\uffff\1\u00d5\2\uffff\1\u00dc\1"+
            "\u00d9\10\uffff\1\u00d6\6\uffff\1\u00da\1\u00db",
            "\1\13\1\40",
            "\1\u00dd",
            "\1\u00df\3\uffff\1\u00de",
            "\1\u00e0",
            "\1\u00e0",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u00e1",
            "\1\u008a\2\uffff\1\u0089\2\uffff\1\u00e2",
            "\1\u00e5\4\uffff\1\u00e6\12\uffff\1\u00e3\2\uffff\1\u00ea\1"+
            "\u00e7\10\uffff\1\u00e4\6\uffff\1\u00e8\1\u00e9",
            "\1\13\1\u0095",
            "\1\u00eb",
            "\1\u00ec",
            "\1\13\1\u0095\2\uffff\1\u00ed",
            "\1\u00f0\4\uffff\1\u00f1\12\uffff\1\u00ee\2\uffff\1\u00f5\1"+
            "\u00f2\10\uffff\1\u00ef\6\uffff\1\u00f3\1\u00f4",
            "\1\46\1\u009a",
            "\1\u00f6",
            "\1\u00fe\3\uffff\1\u00fd\30\uffff\1\u00f8\12\uffff\1\u00ff"+
            "\1\uffff\1\u00f7\1\u00fc\1\u00f9\17\uffff\1\u00fa\1\u00fb",
            "\1\46\1\u009a\2\uffff\1\u0100",
            "\1\u0108\3\uffff\1\u0107\30\uffff\1\u0102\14\uffff\1\u0101"+
            "\1\u0106\1\u0103\17\uffff\1\u0104\1\u0105",
            "\1\u009f",
            "\1\u0109",
            "\1\u010b\16\uffff\1\u010c\7\uffff\1\u010a\7\uffff\1\u010d\1"+
            "\u010e",
            "\1\u009f\1\uffff\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\u0113\3\uffff\1\u0112",
            "\1\u0114",
            "\1\u0114",
            "\1\u0117\4\uffff\1\u0118\12\uffff\1\u0115\2\uffff\1\u011c\1"+
            "\u0119\10\uffff\1\u0116\6\uffff\1\u011a\1\u011b",
            "\1\52\2\uffff\1\51",
            "\1\u011d",
            "\1\52\2\uffff\1\51\2\uffff\1\u011e",
            "\1\161",
            "\1\u011f",
            "\1\u0122\4\uffff\1\u0123\12\uffff\1\u0120\2\uffff\1\u0127\1"+
            "\u0124\10\uffff\1\u0121\6\uffff\1\u0125\1\u0126",
            "\1\52\2\uffff\1\51",
            "\1\u0128",
            "\1\u012a\3\uffff\1\u0129",
            "\1\u012b",
            "\1\u012b",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u012c",
            "\1\u00ad\2\uffff\1\u00ac\2\uffff\1\u012d",
            "\1\52\2\uffff\1\51",
            "\1\u012f\1\u012e",
            "\1\u0130",
            "\1\u0131",
            "\1\u0132",
            "\1\177\1\u0134\2\uffff\1\u0133",
            "\1\u0135",
            "\1\u0135",
            "\1\177\1\u0134",
            "\1\177\1\u0134",
            "\1\177\1\u0134",
            "\1\u0136",
            "\1\177\1\176\12\uffff\1\175",
            "\1\u0137",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u013a\16\uffff\1\u013b\17\uffff\1\u013c\1\u013d",
            "\1\u013e",
            "\1\u0139\2\uffff\1\u0138\2\uffff\1\u013f",
            "\1\u0140",
            "\1\u0140",
            "\1\u0139\2\uffff\1\u0138",
            "\1\13\1\40",
            "\1\u0141",
            "\1\u0142",
            "\1\u0143",
            "\1\13\1\u0145\2\uffff\1\u0144",
            "\1\u0146",
            "\1\u0146",
            "\1\13\1\u0145",
            "\1\13\1\u0145",
            "\1\13\1\u0145",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u0147\16\uffff\1\u0148\17\uffff\1\u0149\1\u014a",
            "\1\u014b",
            "\1\u008a\2\uffff\1\u0089\2\uffff\1\u014c",
            "\1\u014d",
            "\1\u014d",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u00df",
            "\1\u014e",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u00df\3\uffff\1\u014f",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u0150",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0153\16\uffff\1\u0154\17\uffff\1\u0155\1\u0156",
            "\1\u0157",
            "\1\u0152\2\uffff\1\u0151\2\uffff\1\u0158",
            "\1\u0159",
            "\1\u0159",
            "\1\u0152\2\uffff\1\u0151",
            "\1\13\1\u0095",
            "\1\u015a",
            "\1\u015b",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u015e\16\uffff\1\u015f\17\uffff\1\u0160\1\u0161",
            "\1\u0162",
            "\1\u015d\2\uffff\1\u015c\2\uffff\1\u0163",
            "\1\u0164",
            "\1\u0164",
            "\1\u015d\2\uffff\1\u015c",
            "\1\46\1\u009a",
            "\1\u0165",
            "\1\u0166",
            "\1\46\1\u009a\2\uffff\1\u0167",
            "\1\u0168",
            "\1\u0168",
            "\1\46\1\u009a",
            "\1\46\1\u009a",
            "\1\46\1\u009a",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "\1\u016c",
            "\1\46\1\u016e\2\uffff\1\u016d",
            "\1\u016f",
            "\1\u016f",
            "\1\46\1\u016e",
            "\1\46\1\u016e",
            "\1\46\1\u016e",
            "\1\u009f",
            "\1\u0171\1\u0170",
            "\1\u0172",
            "\1\u0171\1\u0170\2\uffff\1\u0173",
            "\1\u0174",
            "\1\u0174",
            "\1\u0175",
            "\1\u0171\1\u0170",
            "\1\u0113",
            "\1\u0176",
            "\1\52\2\uffff\1\51",
            "\1\u0113\3\uffff\1\u0177",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u017a\16\uffff\1\u017b\17\uffff\1\u017c\1\u017d",
            "\1\u017e",
            "\1\u0179\2\uffff\1\u0178\2\uffff\1\u017f",
            "\1\u0180",
            "\1\u0180",
            "\1\u0179\2\uffff\1\u0178",
            "\1\52\2\uffff\1\51",
            "\1\u0181",
            "\1\161",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u0182\16\uffff\1\u0183\17\uffff\1\u0184\1\u0185",
            "\1\u0186",
            "\1\u00ad\2\uffff\1\u00ac\2\uffff\1\u0187",
            "\1\u0188",
            "\1\u0188",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u012a",
            "\1\u0189",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u012a\3\uffff\1\u018a",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u018b",
            "\1\u0193\3\uffff\1\u0192\24\uffff\1\u012f\3\uffff\1\u018d\12"+
            "\uffff\1\u0194\1\uffff\1\u018c\1\u0191\1\u018e\17\uffff\1\u018f"+
            "\1\u0190",
            "\1\4",
            "\1\u012f\1\u012e",
            "\1\u0197\4\uffff\1\u0198\12\uffff\1\u0195\2\uffff\1\u019c\1"+
            "\u0199\10\uffff\1\u0196\6\uffff\1\u019a\1\u019b",
            "\1\177\1\u0134",
            "\1\u019d",
            "\1\u01a6\3\uffff\1\u01a5\30\uffff\1\u01a0\12\uffff\1\u019e"+
            "\1\uffff\1\u019f\1\u01a4\1\u01a1\17\uffff\1\u01a2\1\u01a3",
            "\1\177\1\u0134\2\uffff\1\u01a7",
            "\1\u01af\3\uffff\1\u01ae\30\uffff\1\u01a9\14\uffff\1\u01a8"+
            "\1\u01ad\1\u01aa\17\uffff\1\u01ab\1\u01ac",
            "\1\177\1\176\12\uffff\1\175",
            "\1\u01b2\4\uffff\1\u01b3\12\uffff\1\u01b0\2\uffff\1\u01b7\1"+
            "\u01b4\10\uffff\1\u01b1\6\uffff\1\u01b5\1\u01b6",
            "\1\13\1\40",
            "\1\u01b8",
            "\1\u01ba\3\uffff\1\u01b9",
            "\1\u01bb",
            "\1\u01bb",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u01bc",
            "\1\u0139\2\uffff\1\u0138\2\uffff\1\u01bd",
            "\1\13\1\40",
            "\1\u01c0\4\uffff\1\u01c1\12\uffff\1\u01be\2\uffff\1\u01c5\1"+
            "\u01c2\10\uffff\1\u01bf\6\uffff\1\u01c3\1\u01c4",
            "\1\13\1\u0145",
            "\1\u01c6",
            "\1\u01c7",
            "\1\13\1\u0145\2\uffff\1\u01c8",
            "\1\u01c9",
            "\1\u01cb\3\uffff\1\u01ca",
            "\1\u01cc",
            "\1\u01cc",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u01cd",
            "\1\u008a\2\uffff\1\u0089\2\uffff\1\u01ce",
            "\1\u00df",
            "\1\u01cf",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u01d2\4\uffff\1\u01d3\12\uffff\1\u01d0\2\uffff\1\u01d7\1"+
            "\u01d4\10\uffff\1\u01d1\6\uffff\1\u01d5\1\u01d6",
            "\1\13\1\u0095",
            "\1\u01d8",
            "\1\u01da\3\uffff\1\u01d9",
            "\1\u01db",
            "\1\u01db",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u01dc",
            "\1\u0152\2\uffff\1\u0151\2\uffff\1\u01dd",
            "\1\u01e5\3\uffff\1\u01e4\30\uffff\1\u01df\14\uffff\1\u01de"+
            "\1\u01e3\1\u01e0\17\uffff\1\u01e1\1\u01e2",
            "\1\13\1\u0095",
            "\1\u01e8\4\uffff\1\u01e9\12\uffff\1\u01e6\2\uffff\1\u01ed\1"+
            "\u01ea\10\uffff\1\u01e7\6\uffff\1\u01eb\1\u01ec",
            "\1\46\1\u009a",
            "\1\u01ee",
            "\1\u01f0\3\uffff\1\u01ef",
            "\1\u01f1",
            "\1\u01f1",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u01f2",
            "\1\u015d\2\uffff\1\u015c\2\uffff\1\u01f3",
            "\1\u01f6\4\uffff\1\u01f7\12\uffff\1\u01f4\2\uffff\1\u01fb\1"+
            "\u01f8\10\uffff\1\u01f5\6\uffff\1\u01f9\1\u01fa",
            "\1\46\1\u009a",
            "\1\u01fc",
            "\1\46\1\u009a\2\uffff\1\u01fd",
            "\1\u0205\3\uffff\1\u0204\30\uffff\1\u01ff\14\uffff\1\u01fe"+
            "\1\u0203\1\u0200\17\uffff\1\u0201\1\u0202",
            "\1\46\1\u009a",
            "\1\u0208\4\uffff\1\u0209\12\uffff\1\u0206\2\uffff\1\u020d\1"+
            "\u020a\10\uffff\1\u0207\6\uffff\1\u020b\1\u020c",
            "\1\46\1\u016e",
            "\1\u020e",
            "\1\u020f",
            "\1\46\1\u016e\2\uffff\1\u0210",
            "\1\u0218\3\uffff\1\u0217\24\uffff\1\u0171\3\uffff\1\u0212\12"+
            "\uffff\1\u0219\1\uffff\1\u0211\1\u0216\1\u0213\17\uffff\1\u0214"+
            "\1\u0215",
            "\1\4",
            "\1\u0171\1\u0170",
            "\1\u021a",
            "\1\u0171\1\u0170\2\uffff\1\u021b",
            "\1\u009f",
            "\1\u0113",
            "\1\u021c",
            "\1\u021f\4\uffff\1\u0220\12\uffff\1\u021d\2\uffff\1\u0224\1"+
            "\u0221\10\uffff\1\u021e\6\uffff\1\u0222\1\u0223",
            "\1\52\2\uffff\1\51",
            "\1\u0225",
            "\1\u0227\3\uffff\1\u0226",
            "\1\u0228",
            "\1\u0228",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0229",
            "\1\u0179\2\uffff\1\u0178\2\uffff\1\u022a",
            "\1\52\2\uffff\1\51",
            "\1\u022b",
            "\1\u022d\3\uffff\1\u022c",
            "\1\u022e",
            "\1\u022e",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u022f",
            "\1\u00ad\2\uffff\1\u00ac\2\uffff\1\u0230",
            "\1\u012a",
            "\1\u0231",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u0232",
            "\1\u0233",
            "\1\u012f\1\u0235\2\uffff\1\u0234",
            "\1\u0236",
            "\1\u0236",
            "\1\u012f\1\u0235",
            "\1\u012f\1\u0235",
            "\1\u012f\1\u0235",
            "\1\u0237",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u023a\16\uffff\1\u023b\17\uffff\1\u023c\1\u023d",
            "\1\u023e",
            "\1\u0239\2\uffff\1\u0238\2\uffff\1\u023f",
            "\1\u0240",
            "\1\u0240",
            "\1\u0239\2\uffff\1\u0238",
            "\1\177\1\u0134",
            "\1\u0241",
            "\1\u0242",
            "\1\u0243",
            "\1\177\1\u0134\2\uffff\1\u0244",
            "\1\u0245",
            "\1\u0245",
            "\1\177\1\u0134",
            "\1\177\1\u0134",
            "\1\177\1\u0134",
            "\1\u0246",
            "\1\u0247",
            "\1\u0248",
            "\1\177\1\u024a\2\uffff\1\u0249",
            "\1\u024b",
            "\1\u024b",
            "\1\177\1\u024a",
            "\1\177\1\u024a",
            "\1\177\1\u024a",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u024c\16\uffff\1\u024d\17\uffff\1\u024e\1\u024f",
            "\1\u0250",
            "\1\u0139\2\uffff\1\u0138\2\uffff\1\u0251",
            "\1\u0252",
            "\1\u0252",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u01ba",
            "\1\u0253",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u01ba\3\uffff\1\u0254",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u0255",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0258\16\uffff\1\u0259\17\uffff\1\u025a\1\u025b",
            "\1\u025c",
            "\1\u0257\2\uffff\1\u0256\2\uffff\1\u025d",
            "\1\u025e",
            "\1\u025e",
            "\1\u0257\2\uffff\1\u0256",
            "\1\13\1\u0145",
            "\1\u025f",
            "\1\u0260",
            "\1\u01cb",
            "\1\u0261",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u01cb\3\uffff\1\u0262",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u0263",
            "\1\u00df",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0264\16\uffff\1\u0265\17\uffff\1\u0266\1\u0267",
            "\1\u0268",
            "\1\u0152\2\uffff\1\u0151\2\uffff\1\u0269",
            "\1\u026a",
            "\1\u026a",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u01da",
            "\1\u026b",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u01da\3\uffff\1\u026c",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u026d",
            "\1\u026e",
            "\1\u026f",
            "\1\13\1\u0095\2\uffff\1\u0270",
            "\1\u0271",
            "\1\u0271",
            "\1\13\1\u0095",
            "\1\13\1\u0095",
            "\1\13\1\u0095",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u0272\16\uffff\1\u0273\17\uffff\1\u0274\1\u0275",
            "\1\u0276",
            "\1\u015d\2\uffff\1\u015c\2\uffff\1\u0277",
            "\1\u0278",
            "\1\u0278",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u01f0",
            "\1\u0279",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u01f0\3\uffff\1\u027a",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u027b",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u027e\16\uffff\1\u027f\17\uffff\1\u0280\1\u0281",
            "\1\u0282",
            "\1\u027d\2\uffff\1\u027c\2\uffff\1\u0283",
            "\1\u0284",
            "\1\u0284",
            "\1\u027d\2\uffff\1\u027c",
            "\1\46\1\u009a",
            "\1\u0285",
            "\1\u0286",
            "\1\u0287",
            "\1\46\1\u0289\2\uffff\1\u0288",
            "\1\u028a",
            "\1\u028a",
            "\1\46\1\u0289",
            "\1\46\1\u0289",
            "\1\46\1\u0289",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u028d\16\uffff\1\u028e\17\uffff\1\u028f\1\u0290",
            "\1\u0291",
            "\1\u028c\2\uffff\1\u028b\2\uffff\1\u0292",
            "\1\u0293",
            "\1\u0293",
            "\1\u028c\2\uffff\1\u028b",
            "\1\46\1\u016e",
            "\1\u0294",
            "\1\u0295",
            "\1\u0296",
            "\1\u0297",
            "\1\u0171\1\u0299\2\uffff\1\u0298",
            "\1\u029a",
            "\1\u029a",
            "\1\u0171\1\u0299",
            "\1\u0171\1\u0299",
            "\1\u0171\1\u0299",
            "\1\u029b",
            "\1\u0171\1\u0170",
            "\1\u029c",
            "\1\u0113",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u029d\16\uffff\1\u029e\17\uffff\1\u029f\1\u02a0",
            "\1\u02a1",
            "\1\u0179\2\uffff\1\u0178\2\uffff\1\u02a2",
            "\1\u02a3",
            "\1\u02a3",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0227",
            "\1\u02a4",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0227\3\uffff\1\u02a5",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u02a6",
            "\1\u022d",
            "\1\u02a7",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u022d\3\uffff\1\u02a8",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u02a9",
            "\1\u012a",
            "\1\u02ac\4\uffff\1\u02ad\12\uffff\1\u02aa\2\uffff\1\u02b1\1"+
            "\u02ae\10\uffff\1\u02ab\6\uffff\1\u02af\1\u02b0",
            "\1\u012f\1\u0235",
            "\1\u02b2",
            "\1\u02ba\3\uffff\1\u02b9\30\uffff\1\u02b4\12\uffff\1\u02bb"+
            "\1\uffff\1\u02b3\1\u02b8\1\u02b5\17\uffff\1\u02b6\1\u02b7",
            "\1\u012f\1\u0235\2\uffff\1\u02bc",
            "\1\u02c4\3\uffff\1\u02c3\30\uffff\1\u02be\14\uffff\1\u02bd"+
            "\1\u02c2\1\u02bf\17\uffff\1\u02c0\1\u02c1",
            "\1\u02c7\4\uffff\1\u02c8\12\uffff\1\u02c5\2\uffff\1\u02cc\1"+
            "\u02c9\10\uffff\1\u02c6\6\uffff\1\u02ca\1\u02cb",
            "\1\177\1\u0134",
            "\1\u02cd",
            "\1\u02cf\3\uffff\1\u02ce",
            "\1\u02d0",
            "\1\u02d0",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u02d1",
            "\1\u0239\2\uffff\1\u0238\2\uffff\1\u02d2",
            "\1\u02da\3\uffff\1\u02d9\30\uffff\1\u02d4\14\uffff\1\u02d3"+
            "\1\u02d8\1\u02d5\17\uffff\1\u02d6\1\u02d7",
            "\1\u02dd\4\uffff\1\u02de\12\uffff\1\u02db\2\uffff\1\u02e2\1"+
            "\u02df\10\uffff\1\u02dc\6\uffff\1\u02e0\1\u02e1",
            "\1\177\1\u0134",
            "\1\u02e3",
            "\1\177\1\u0134\2\uffff\1\u02e4",
            "\1\177\1\u0134",
            "\1\u02e7\4\uffff\1\u02e8\12\uffff\1\u02e5\2\uffff\1\u02ec\1"+
            "\u02e9\10\uffff\1\u02e6\6\uffff\1\u02ea\1\u02eb",
            "\1\177\1\u024a",
            "\1\u02ed",
            "\1\u02ee",
            "\1\177\1\u024a\2\uffff\1\u02ef",
            "\1\u02f0",
            "\1\u02f2\3\uffff\1\u02f1",
            "\1\u02f3",
            "\1\u02f3",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u02f4",
            "\1\u0139\2\uffff\1\u0138\2\uffff\1\u02f5",
            "\1\u01ba",
            "\1\u02f6",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u02f9\4\uffff\1\u02fa\12\uffff\1\u02f7\2\uffff\1\u02fe\1"+
            "\u02fb\10\uffff\1\u02f8\6\uffff\1\u02fc\1\u02fd",
            "\1\13\1\u0145",
            "\1\u02ff",
            "\1\u0301\3\uffff\1\u0300",
            "\1\u0302",
            "\1\u0302",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0303",
            "\1\u0257\2\uffff\1\u0256\2\uffff\1\u0304",
            "\1\u030c\3\uffff\1\u030b\30\uffff\1\u0306\14\uffff\1\u0305"+
            "\1\u030a\1\u0307\17\uffff\1\u0308\1\u0309",
            "\1\13\1\u0145",
            "\1\u01cb",
            "\1\u030d",
            "\1\u008a\2\uffff\1\u0089",
            "\1\u030e",
            "\1\u0310\3\uffff\1\u030f",
            "\1\u0311",
            "\1\u0311",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0312",
            "\1\u0152\2\uffff\1\u0151\2\uffff\1\u0313",
            "\1\u01da",
            "\1\u0314",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0317\4\uffff\1\u0318\12\uffff\1\u0315\2\uffff\1\u031c\1"+
            "\u0319\10\uffff\1\u0316\6\uffff\1\u031a\1\u031b",
            "\1\13\1\u0095",
            "\1\u031d",
            "\1\13\1\u0095\2\uffff\1\u031e",
            "\1\u031f",
            "\1\u0321\3\uffff\1\u0320",
            "\1\u0322",
            "\1\u0322",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u0323",
            "\1\u015d\2\uffff\1\u015c\2\uffff\1\u0324",
            "\1\u01f0",
            "\1\u0325",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u0328\4\uffff\1\u0329\12\uffff\1\u0326\2\uffff\1\u032d\1"+
            "\u032a\10\uffff\1\u0327\6\uffff\1\u032b\1\u032c",
            "\1\46\1\u009a",
            "\1\u032e",
            "\1\u0330\3\uffff\1\u032f",
            "\1\u0331",
            "\1\u0331",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0332",
            "\1\u027d\2\uffff\1\u027c\2\uffff\1\u0333",
            "\1\46\1\u009a",
            "\1\u0336\4\uffff\1\u0337\12\uffff\1\u0334\2\uffff\1\u033b\1"+
            "\u0338\10\uffff\1\u0335\6\uffff\1\u0339\1\u033a",
            "\1\46\1\u0289",
            "\1\u033c",
            "\1\u033d",
            "\1\46\1\u0289\2\uffff\1\u033e",
            "\1\u0341\4\uffff\1\u0342\12\uffff\1\u033f\2\uffff\1\u0346\1"+
            "\u0343\10\uffff\1\u0340\6\uffff\1\u0344\1\u0345",
            "\1\46\1\u016e",
            "\1\u0347",
            "\1\u0349\3\uffff\1\u0348",
            "\1\u034a",
            "\1\u034a",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u034b",
            "\1\u028c\2\uffff\1\u028b\2\uffff\1\u034c",
            "\1\u0354\3\uffff\1\u0353\30\uffff\1\u034e\14\uffff\1\u034d"+
            "\1\u0352\1\u034f\17\uffff\1\u0350\1\u0351",
            "\1\46\1\u016e",
            "\1\u0357\4\uffff\1\u0358\12\uffff\1\u0355\2\uffff\1\u035c\1"+
            "\u0359\10\uffff\1\u0356\6\uffff\1\u035a\1\u035b",
            "\1\u0171\1\u0299",
            "\1\u035d",
            "\1\u0366\3\uffff\1\u0365\30\uffff\1\u0360\12\uffff\1\u035e"+
            "\1\uffff\1\u035f\1\u0364\1\u0361\17\uffff\1\u0362\1\u0363",
            "\1\u0171\1\u0299\2\uffff\1\u0367",
            "\1\u036f\3\uffff\1\u036e\30\uffff\1\u0369\14\uffff\1\u0368"+
            "\1\u036d\1\u036a\17\uffff\1\u036b\1\u036c",
            "\1\u0171\1\u0170",
            "\1\u0370",
            "\1\u0372\3\uffff\1\u0371",
            "\1\u0373",
            "\1\u0373",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0374",
            "\1\u0179\2\uffff\1\u0178\2\uffff\1\u0375",
            "\1\u0227",
            "\1\u0376",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u022d",
            "\1\u0377",
            "\1\u00ad\2\uffff\1\u00ac",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u037a\16\uffff\1\u037b\17\uffff\1\u037c\1\u037d",
            "\1\u037e",
            "\1\u0379\2\uffff\1\u0378\2\uffff\1\u037f",
            "\1\u0380",
            "\1\u0380",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u012f\1\u0235",
            "\1\u0381",
            "\1\u0382",
            "\1\u012f\1\u0235\2\uffff\1\u0383",
            "\1\u0384",
            "\1\u0384",
            "\1\u012f\1\u0235",
            "\1\u012f\1\u0235",
            "\1\u012f\1\u0235",
            "\1\u0385",
            "\1\u0386",
            "\1\u0387",
            "\1\u0388",
            "\1\u012f\1\u038a\2\uffff\1\u0389",
            "\1\u038b",
            "\1\u038b",
            "\1\u012f\1\u038a",
            "\1\u012f\1\u038a",
            "\1\u012f\1\u038a",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u038c\16\uffff\1\u038d\17\uffff\1\u038e\1\u038f",
            "\1\u0390",
            "\1\u0239\2\uffff\1\u0238\2\uffff\1\u0391",
            "\1\u0392",
            "\1\u0392",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u02cf",
            "\1\u0393",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u02cf\3\uffff\1\u0394",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u0395",
            "\1\u0396",
            "\1\u0397",
            "\1\177\1\u0399\2\uffff\1\u0398",
            "\1\u039a",
            "\1\u039a",
            "\1\177\1\u0399",
            "\1\177\1\u0399",
            "\1\177\1\u0399",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u039d\16\uffff\1\u039e\17\uffff\1\u039f\1\u03a0",
            "\1\u03a1",
            "\1\u039c\2\uffff\1\u039b\2\uffff\1\u03a2",
            "\1\u03a3",
            "\1\u03a3",
            "\1\u039c\2\uffff\1\u039b",
            "\1\177\1\u0134",
            "\1\u03a4",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u03a7\16\uffff\1\u03a8\17\uffff\1\u03a9\1\u03aa",
            "\1\u03ab",
            "\1\u03a6\2\uffff\1\u03a5\2\uffff\1\u03ac",
            "\1\u03ad",
            "\1\u03ad",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\177\1\u024a",
            "\1\u03ae",
            "\1\u03af",
            "\1\u02f2",
            "\1\u03b0",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u02f2\3\uffff\1\u03b1",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u03b2",
            "\1\u01ba",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u03b3\16\uffff\1\u03b4\17\uffff\1\u03b5\1\u03b6",
            "\1\u03b7",
            "\1\u0257\2\uffff\1\u0256\2\uffff\1\u03b8",
            "\1\u03b9",
            "\1\u03b9",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0301",
            "\1\u03ba",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0301\3\uffff\1\u03bb",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u03bc",
            "\1\u03bd",
            "\1\u03be",
            "\1\13\1\u0145\2\uffff\1\u03bf",
            "\1\u03c0",
            "\1\u03c0",
            "\1\13\1\u0145",
            "\1\13\1\u0145",
            "\1\13\1\u0145",
            "\1\u01cb",
            "\1\u0310",
            "\1\u03c1",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0310\3\uffff\1\u03c2",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u03c3",
            "\1\u01da",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u03c6\16\uffff\1\u03c7\17\uffff\1\u03c8\1\u03c9",
            "\1\u03ca",
            "\1\u03c5\2\uffff\1\u03c4\2\uffff\1\u03cb",
            "\1\u03cc",
            "\1\u03cc",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\13\1\u0095",
            "\1\u03cd",
            "\1\u0321",
            "\1\u03ce",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u0321\3\uffff\1\u03cf",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u03d0",
            "\1\u01f0",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u03d1\16\uffff\1\u03d2\17\uffff\1\u03d3\1\u03d4",
            "\1\u03d5",
            "\1\u027d\2\uffff\1\u027c\2\uffff\1\u03d6",
            "\1\u03d7",
            "\1\u03d7",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0330",
            "\1\u03d8",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0330\3\uffff\1\u03d9",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u03da",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u03dd\16\uffff\1\u03de\17\uffff\1\u03df\1\u03e0",
            "\1\u03e1",
            "\1\u03dc\2\uffff\1\u03db\2\uffff\1\u03e2",
            "\1\u03e3",
            "\1\u03e3",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\46\1\u0289",
            "\1\u03e4",
            "\1\u03e5",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u03e6\16\uffff\1\u03e7\17\uffff\1\u03e8\1\u03e9",
            "\1\u03ea",
            "\1\u028c\2\uffff\1\u028b\2\uffff\1\u03eb",
            "\1\u03ec",
            "\1\u03ec",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u0349",
            "\1\u03ed",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u0349\3\uffff\1\u03ee",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u03ef",
            "\1\u03f0",
            "\1\u03f1",
            "\1\46\1\u016e\2\uffff\1\u03f2",
            "\1\u03f3",
            "\1\u03f3",
            "\1\46\1\u016e",
            "\1\46\1\u016e",
            "\1\46\1\u016e",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u03f6\16\uffff\1\u03f7\17\uffff\1\u03f8\1\u03f9",
            "\1\u03fa",
            "\1\u03f5\2\uffff\1\u03f4\2\uffff\1\u03fb",
            "\1\u03fc",
            "\1\u03fc",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u0171\1\u0299",
            "\1\u03fd",
            "\1\u03fe",
            "\1\u03ff",
            "\1\u0171\1\u0299\2\uffff\1\u0400",
            "\1\u0401",
            "\1\u0401",
            "\1\u0171\1\u0299",
            "\1\u0171\1\u0299",
            "\1\u0171\1\u0299",
            "\1\u0402",
            "\1\u0403",
            "\1\u0404",
            "\1\u0171\1\u0406\2\uffff\1\u0405",
            "\1\u0407",
            "\1\u0407",
            "\1\u0171\1\u0406",
            "\1\u0171\1\u0406",
            "\1\u0171\1\u0406",
            "\1\u0372",
            "\1\u0408",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0372\3\uffff\1\u0409",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u040a",
            "\1\u0227",
            "\1\u022d",
            "\1\u040d\4\uffff\1\u040e\12\uffff\1\u040b\2\uffff\1\u0412\1"+
            "\u040f\10\uffff\1\u040c\6\uffff\1\u0410\1\u0411",
            "\1\u012f\1\u0235",
            "\1\u0413",
            "\1\u0415\3\uffff\1\u0414",
            "\1\u0416",
            "\1\u0416",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0417",
            "\1\u0379\2\uffff\1\u0378\2\uffff\1\u0418",
            "\1\u041b\4\uffff\1\u041c\12\uffff\1\u0419\2\uffff\1\u0420\1"+
            "\u041d\10\uffff\1\u041a\6\uffff\1\u041e\1\u041f",
            "\1\u012f\1\u0235",
            "\1\u0421",
            "\1\u012f\1\u0235\2\uffff\1\u0422",
            "\1\u042a\3\uffff\1\u0429\30\uffff\1\u0424\14\uffff\1\u0423"+
            "\1\u0428\1\u0425\17\uffff\1\u0426\1\u0427",
            "\1\u012f\1\u0235",
            "\1\u042d\4\uffff\1\u042e\12\uffff\1\u042b\2\uffff\1\u0432\1"+
            "\u042f\10\uffff\1\u042c\6\uffff\1\u0430\1\u0431",
            "\1\u012f\1\u038a",
            "\1\u0433",
            "\1\u0434",
            "\1\u012f\1\u038a\2\uffff\1\u0435",
            "\1\u0436",
            "\1\u0438\3\uffff\1\u0437",
            "\1\u0439",
            "\1\u0439",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u043a",
            "\1\u0239\2\uffff\1\u0238\2\uffff\1\u043b",
            "\1\u02cf",
            "\1\u043c",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u043f\4\uffff\1\u0440\12\uffff\1\u043d\2\uffff\1\u0444\1"+
            "\u0441\10\uffff\1\u043e\6\uffff\1\u0442\1\u0443",
            "\1\177\1\u0399",
            "\1\u0445",
            "\1\u0446",
            "\1\177\1\u0399\2\uffff\1\u0447",
            "\1\u044a\4\uffff\1\u044b\12\uffff\1\u0448\2\uffff\1\u044f\1"+
            "\u044c\10\uffff\1\u0449\6\uffff\1\u044d\1\u044e",
            "\1\177\1\u0134",
            "\1\u0450",
            "\1\u0452\3\uffff\1\u0451",
            "\1\u0453",
            "\1\u0453",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u0454",
            "\1\u039c\2\uffff\1\u039b\2\uffff\1\u0455",
            "\1\177\1\u0134",
            "\1\u0458\4\uffff\1\u0459\12\uffff\1\u0456\2\uffff\1\u045d\1"+
            "\u045a\10\uffff\1\u0457\6\uffff\1\u045b\1\u045c",
            "\1\177\1\u024a",
            "\1\u045e",
            "\1\u0460\3\uffff\1\u045f",
            "\1\u0461",
            "\1\u0461",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u0462",
            "\1\u03a6\2\uffff\1\u03a5\2\uffff\1\u0463",
            "\1\u046b\3\uffff\1\u046a\30\uffff\1\u0465\14\uffff\1\u0464"+
            "\1\u0469\1\u0466\17\uffff\1\u0467\1\u0468",
            "\1\177\1\u024a",
            "\1\u02f2",
            "\1\u046c",
            "\1\u0139\2\uffff\1\u0138",
            "\1\u046d",
            "\1\u046f\3\uffff\1\u046e",
            "\1\u0470",
            "\1\u0470",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0471",
            "\1\u0257\2\uffff\1\u0256\2\uffff\1\u0472",
            "\1\u0301",
            "\1\u0473",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0476\4\uffff\1\u0477\12\uffff\1\u0474\2\uffff\1\u047b\1"+
            "\u0478\10\uffff\1\u0475\6\uffff\1\u0479\1\u047a",
            "\1\13\1\u0145",
            "\1\u047c",
            "\1\13\1\u0145\2\uffff\1\u047d",
            "\1\u0310",
            "\1\u047e",
            "\1\u0152\2\uffff\1\u0151",
            "\1\u0481\4\uffff\1\u0482\12\uffff\1\u047f\2\uffff\1\u0486\1"+
            "\u0483\10\uffff\1\u0480\6\uffff\1\u0484\1\u0485",
            "\1\13\1\u0095",
            "\1\u0487",
            "\1\u0489\3\uffff\1\u0488",
            "\1\u048a",
            "\1\u048a",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u048b",
            "\1\u03c5\2\uffff\1\u03c4\2\uffff\1\u048c",
            "\1\13\1\u0095",
            "\1\u0321",
            "\1\u048d",
            "\1\u015d\2\uffff\1\u015c",
            "\1\u048e",
            "\1\u0490\3\uffff\1\u048f",
            "\1\u0491",
            "\1\u0491",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0492",
            "\1\u027d\2\uffff\1\u027c\2\uffff\1\u0493",
            "\1\u0330",
            "\1\u0494",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0497\4\uffff\1\u0498\12\uffff\1\u0495\2\uffff\1\u049c\1"+
            "\u0499\10\uffff\1\u0496\6\uffff\1\u049a\1\u049b",
            "\1\46\1\u0289",
            "\1\u049d",
            "\1\u049f\3\uffff\1\u049e",
            "\1\u04a0",
            "\1\u04a0",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u04a1",
            "\1\u03dc\2\uffff\1\u03db\2\uffff\1\u04a2",
            "\1\u04aa\3\uffff\1\u04a9\30\uffff\1\u04a4\14\uffff\1\u04a3"+
            "\1\u04a8\1\u04a5\17\uffff\1\u04a6\1\u04a7",
            "\1\46\1\u0289",
            "\1\u04ab",
            "\1\u04ad\3\uffff\1\u04ac",
            "\1\u04ae",
            "\1\u04ae",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u04af",
            "\1\u028c\2\uffff\1\u028b\2\uffff\1\u04b0",
            "\1\u0349",
            "\1\u04b1",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u04b4\4\uffff\1\u04b5\12\uffff\1\u04b2\2\uffff\1\u04b9\1"+
            "\u04b6\10\uffff\1\u04b3\6\uffff\1\u04b7\1\u04b8",
            "\1\46\1\u016e",
            "\1\u04ba",
            "\1\46\1\u016e\2\uffff\1\u04bb",
            "\1\u04be\4\uffff\1\u04bf\12\uffff\1\u04bc\2\uffff\1\u04c3\1"+
            "\u04c0\10\uffff\1\u04bd\6\uffff\1\u04c1\1\u04c2",
            "\1\u0171\1\u0299",
            "\1\u04c4",
            "\1\u04c6\3\uffff\1\u04c5",
            "\1\u04c7",
            "\1\u04c7",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u04c8",
            "\1\u03f5\2\uffff\1\u03f4\2\uffff\1\u04c9",
            "\1\u04d1\3\uffff\1\u04d0\30\uffff\1\u04cb\14\uffff\1\u04ca"+
            "\1\u04cf\1\u04cc\17\uffff\1\u04cd\1\u04ce",
            "\1\u04d4\4\uffff\1\u04d5\12\uffff\1\u04d2\2\uffff\1\u04d9\1"+
            "\u04d6\10\uffff\1\u04d3\6\uffff\1\u04d7\1\u04d8",
            "\1\u0171\1\u0299",
            "\1\u04da",
            "\1\u0171\1\u0299\2\uffff\1\u04db",
            "\1\u0171\1\u0299",
            "\1\u04de\4\uffff\1\u04df\12\uffff\1\u04dc\2\uffff\1\u04e3\1"+
            "\u04e0\10\uffff\1\u04dd\6\uffff\1\u04e1\1\u04e2",
            "\1\u0171\1\u0406",
            "\1\u04e4",
            "\1\u04e5",
            "\1\u0171\1\u0406\2\uffff\1\u04e6",
            "\1\u0372",
            "\1\u04e7",
            "\1\u0179\2\uffff\1\u0178",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u04e8\16\uffff\1\u04e9\17\uffff\1\u04ea\1\u04eb",
            "\1\u04ec",
            "\1\u0379\2\uffff\1\u0378\2\uffff\1\u04ed",
            "\1\u04ee",
            "\1\u04ee",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0415",
            "\1\u04ef",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0415\3\uffff\1\u04f0",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u04f1",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u04f4\16\uffff\1\u04f5\17\uffff\1\u04f6\1\u04f7",
            "\1\u04f8",
            "\1\u04f3\2\uffff\1\u04f2\2\uffff\1\u04f9",
            "\1\u04fa",
            "\1\u04fa",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u012f\1\u0235",
            "\1\u04fb",
            "\1\u04fc",
            "\1\u04fd",
            "\1\u012f\1\u04ff\2\uffff\1\u04fe",
            "\1\u0500",
            "\1\u0500",
            "\1\u012f\1\u04ff",
            "\1\u012f\1\u04ff",
            "\1\u012f\1\u04ff",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0503\16\uffff\1\u0504\17\uffff\1\u0505\1\u0506",
            "\1\u0507",
            "\1\u0502\2\uffff\1\u0501\2\uffff\1\u0508",
            "\1\u0509",
            "\1\u0509",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u012f\1\u038a",
            "\1\u050a",
            "\1\u050b",
            "\1\u0438",
            "\1\u050c",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u0438\3\uffff\1\u050d",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u050e",
            "\1\u02cf",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u0511\16\uffff\1\u0512\17\uffff\1\u0513\1\u0514",
            "\1\u0515",
            "\1\u0510\2\uffff\1\u050f\2\uffff\1\u0516",
            "\1\u0517",
            "\1\u0517",
            "\1\u0510\2\uffff\1\u050f",
            "\1\177\1\u0399",
            "\1\u0518",
            "\1\u0519",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u051a\16\uffff\1\u051b\17\uffff\1\u051c\1\u051d",
            "\1\u051e",
            "\1\u039c\2\uffff\1\u039b\2\uffff\1\u051f",
            "\1\u0520",
            "\1\u0520",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u0452",
            "\1\u0521",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u0452\3\uffff\1\u0522",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u0523",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u0524\16\uffff\1\u0525\17\uffff\1\u0526\1\u0527",
            "\1\u0528",
            "\1\u03a6\2\uffff\1\u03a5\2\uffff\1\u0529",
            "\1\u052a",
            "\1\u052a",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u0460",
            "\1\u052b",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u0460\3\uffff\1\u052c",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u052d",
            "\1\u052e",
            "\1\u052f",
            "\1\177\1\u024a\2\uffff\1\u0530",
            "\1\u0531",
            "\1\u0531",
            "\1\177\1\u024a",
            "\1\177\1\u024a",
            "\1\177\1\u024a",
            "\1\u02f2",
            "\1\u046f",
            "\1\u0532",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u046f\3\uffff\1\u0533",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u0534",
            "\1\u0301",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u0537\16\uffff\1\u0538\17\uffff\1\u0539\1\u053a",
            "\1\u053b",
            "\1\u0536\2\uffff\1\u0535\2\uffff\1\u053c",
            "\1\u053d",
            "\1\u053d",
            "\1\u0536\2\uffff\1\u0535",
            "\1\13\1\u0145",
            "\1\u053e",
            "\1\u0310",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u053f\16\uffff\1\u0540\17\uffff\1\u0541\1\u0542",
            "\1\u0543",
            "\1\u03c5\2\uffff\1\u03c4\2\uffff\1\u0544",
            "\1\u0545",
            "\1\u0545",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0489",
            "\1\u0546",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0489\3\uffff\1\u0547",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0548",
            "\1\u0321",
            "\1\u0490",
            "\1\u0549",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0490\3\uffff\1\u054a",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u054b",
            "\1\u0330",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u054c\16\uffff\1\u054d\17\uffff\1\u054e\1\u054f",
            "\1\u0550",
            "\1\u03dc\2\uffff\1\u03db\2\uffff\1\u0551",
            "\1\u0552",
            "\1\u0552",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u049f",
            "\1\u0553",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u049f\3\uffff\1\u0554",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u0555",
            "\1\u0556",
            "\1\u0557",
            "\1\46\1\u0289\2\uffff\1\u0558",
            "\1\u0559",
            "\1\u0559",
            "\1\46\1\u0289",
            "\1\46\1\u0289",
            "\1\46\1\u0289",
            "\1\u04ad",
            "\1\u055a",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u04ad\3\uffff\1\u055b",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u055c",
            "\1\u0349",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u055f\16\uffff\1\u0560\17\uffff\1\u0561\1\u0562",
            "\1\u0563",
            "\1\u055e\2\uffff\1\u055d\2\uffff\1\u0564",
            "\1\u0565",
            "\1\u0565",
            "\1\u055e\2\uffff\1\u055d",
            "\1\46\1\u016e",
            "\1\u0566",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u0567\16\uffff\1\u0568\17\uffff\1\u0569\1\u056a",
            "\1\u056b",
            "\1\u03f5\2\uffff\1\u03f4\2\uffff\1\u056c",
            "\1\u056d",
            "\1\u056d",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u04c6",
            "\1\u056e",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u04c6\3\uffff\1\u056f",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u0570",
            "\1\u0571",
            "\1\u0572",
            "\1\u0171\1\u0574\2\uffff\1\u0573",
            "\1\u0575",
            "\1\u0575",
            "\1\u0171\1\u0574",
            "\1\u0171\1\u0574",
            "\1\u0171\1\u0574",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0578\16\uffff\1\u0579\17\uffff\1\u057a\1\u057b",
            "\1\u057c",
            "\1\u0577\2\uffff\1\u0576\2\uffff\1\u057d",
            "\1\u057e",
            "\1\u057e",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0171\1\u0299",
            "\1\u057f",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0582\16\uffff\1\u0583\17\uffff\1\u0584\1\u0585",
            "\1\u0586",
            "\1\u0581\2\uffff\1\u0580\2\uffff\1\u0587",
            "\1\u0588",
            "\1\u0588",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0171\1\u0406",
            "\1\u0589",
            "\1\u058a",
            "\1\u0372",
            "\1\u058b",
            "\1\u058d\3\uffff\1\u058c",
            "\1\u058e",
            "\1\u058e",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u058f",
            "\1\u0379\2\uffff\1\u0378\2\uffff\1\u0590",
            "\1\u0415",
            "\1\u0591",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u0594\4\uffff\1\u0595\12\uffff\1\u0592\2\uffff\1\u0599\1"+
            "\u0596\10\uffff\1\u0593\6\uffff\1\u0597\1\u0598",
            "\1\u012f\1\u0235",
            "\1\u059a",
            "\1\u059c\3\uffff\1\u059b",
            "\1\u059d",
            "\1\u059d",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u059e",
            "\1\u04f3\2\uffff\1\u04f2\2\uffff\1\u059f",
            "\1\u012f\1\u0235",
            "\1\u05a2\4\uffff\1\u05a3\12\uffff\1\u05a0\2\uffff\1\u05a7\1"+
            "\u05a4\10\uffff\1\u05a1\6\uffff\1\u05a5\1\u05a6",
            "\1\u012f\1\u04ff",
            "\1\u05a8",
            "\1\u05a9",
            "\1\u012f\1\u04ff\2\uffff\1\u05aa",
            "\1\u05ad\4\uffff\1\u05ae\12\uffff\1\u05ab\2\uffff\1\u05b2\1"+
            "\u05af\10\uffff\1\u05ac\6\uffff\1\u05b0\1\u05b1",
            "\1\u012f\1\u038a",
            "\1\u05b3",
            "\1\u05b5\3\uffff\1\u05b4",
            "\1\u05b6",
            "\1\u05b6",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u05b7",
            "\1\u0502\2\uffff\1\u0501\2\uffff\1\u05b8",
            "\1\u05c0\3\uffff\1\u05bf\30\uffff\1\u05ba\14\uffff\1\u05b9"+
            "\1\u05be\1\u05bb\17\uffff\1\u05bc\1\u05bd",
            "\1\u012f\1\u038a",
            "\1\u0438",
            "\1\u05c1",
            "\1\u0239\2\uffff\1\u0238",
            "\1\u05c4\4\uffff\1\u05c5\12\uffff\1\u05c2\2\uffff\1\u05c9\1"+
            "\u05c6\10\uffff\1\u05c3\6\uffff\1\u05c7\1\u05c8",
            "\1\177\1\u0399",
            "\1\u05ca",
            "\1\u05cc\3\uffff\1\u05cb",
            "\1\u05cd",
            "\1\u05cd",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u05ce",
            "\1\u0510\2\uffff\1\u050f\2\uffff\1\u05cf",
            "\1\u05d7\3\uffff\1\u05d6\30\uffff\1\u05d1\14\uffff\1\u05d0"+
            "\1\u05d5\1\u05d2\17\uffff\1\u05d3\1\u05d4",
            "\1\177\1\u0399",
            "\1\u05d8",
            "\1\u05da\3\uffff\1\u05d9",
            "\1\u05db",
            "\1\u05db",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u05dc",
            "\1\u039c\2\uffff\1\u039b\2\uffff\1\u05dd",
            "\1\u0452",
            "\1\u05de",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u05df",
            "\1\u05e1\3\uffff\1\u05e0",
            "\1\u05e2",
            "\1\u05e2",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u05e3",
            "\1\u03a6\2\uffff\1\u03a5\2\uffff\1\u05e4",
            "\1\u0460",
            "\1\u05e5",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u05e8\4\uffff\1\u05e9\12\uffff\1\u05e6\2\uffff\1\u05ed\1"+
            "\u05ea\10\uffff\1\u05e7\6\uffff\1\u05eb\1\u05ec",
            "\1\177\1\u024a",
            "\1\u05ee",
            "\1\177\1\u024a\2\uffff\1\u05ef",
            "\1\u046f",
            "\1\u05f0",
            "\1\u0257\2\uffff\1\u0256",
            "\1\u05f3\4\uffff\1\u05f4\12\uffff\1\u05f1\2\uffff\1\u05f8\1"+
            "\u05f5\10\uffff\1\u05f2\6\uffff\1\u05f6\1\u05f7",
            "\1\13\1\u0145",
            "\1\u05f9",
            "\1\u05fb\3\uffff\1\u05fa",
            "\1\u05fc",
            "\1\u05fc",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u05fd",
            "\1\u0536\2\uffff\1\u0535\2\uffff\1\u05fe",
            "\1\13\1\u0145",
            "\1\u05ff",
            "\1\u0601\3\uffff\1\u0600",
            "\1\u0602",
            "\1\u0602",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0603",
            "\1\u03c5\2\uffff\1\u03c4\2\uffff\1\u0604",
            "\1\u0489",
            "\1\u0605",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0490",
            "\1\u0606",
            "\1\u027d\2\uffff\1\u027c",
            "\1\u0607",
            "\1\u0609\3\uffff\1\u0608",
            "\1\u060a",
            "\1\u060a",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u060b",
            "\1\u03dc\2\uffff\1\u03db\2\uffff\1\u060c",
            "\1\u049f",
            "\1\u060d",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u0610\4\uffff\1\u0611\12\uffff\1\u060e\2\uffff\1\u0615\1"+
            "\u0612\10\uffff\1\u060f\6\uffff\1\u0613\1\u0614",
            "\1\46\1\u0289",
            "\1\u0616",
            "\1\46\1\u0289\2\uffff\1\u0617",
            "\1\u04ad",
            "\1\u0618",
            "\1\u028c\2\uffff\1\u028b",
            "\1\u061b\4\uffff\1\u061c\12\uffff\1\u0619\2\uffff\1\u0620\1"+
            "\u061d\10\uffff\1\u061a\6\uffff\1\u061e\1\u061f",
            "\1\46\1\u016e",
            "\1\u0621",
            "\1\u0623\3\uffff\1\u0622",
            "\1\u0624",
            "\1\u0624",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0625",
            "\1\u055e\2\uffff\1\u055d\2\uffff\1\u0626",
            "\1\46\1\u016e",
            "\1\u0627",
            "\1\u0629\3\uffff\1\u0628",
            "\1\u062a",
            "\1\u062a",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u062b",
            "\1\u03f5\2\uffff\1\u03f4\2\uffff\1\u062c",
            "\1\u04c6",
            "\1\u062d",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u0630\4\uffff\1\u0631\12\uffff\1\u062e\2\uffff\1\u0635\1"+
            "\u0632\10\uffff\1\u062f\6\uffff\1\u0633\1\u0634",
            "\1\u0171\1\u0574",
            "\1\u0636",
            "\1\u0637",
            "\1\u0171\1\u0574\2\uffff\1\u0638",
            "\1\u063b\4\uffff\1\u063c\12\uffff\1\u0639\2\uffff\1\u0640\1"+
            "\u063d\10\uffff\1\u063a\6\uffff\1\u063e\1\u063f",
            "\1\u0171\1\u0299",
            "\1\u0641",
            "\1\u0643\3\uffff\1\u0642",
            "\1\u0644",
            "\1\u0644",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0645",
            "\1\u0577\2\uffff\1\u0576\2\uffff\1\u0646",
            "\1\u0171\1\u0299",
            "\1\u0649\4\uffff\1\u064a\12\uffff\1\u0647\2\uffff\1\u064e\1"+
            "\u064b\10\uffff\1\u0648\6\uffff\1\u064c\1\u064d",
            "\1\u0171\1\u0406",
            "\1\u064f",
            "\1\u0651\3\uffff\1\u0650",
            "\1\u0652",
            "\1\u0652",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0653",
            "\1\u0581\2\uffff\1\u0580\2\uffff\1\u0654",
            "\1\u065c\3\uffff\1\u065b\30\uffff\1\u0656\14\uffff\1\u0655"+
            "\1\u065a\1\u0657\17\uffff\1\u0658\1\u0659",
            "\1\u0171\1\u0406",
            "\1\u058d",
            "\1\u065d",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u058d\3\uffff\1\u065e",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u065f",
            "\1\u0415",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u0660\16\uffff\1\u0661\17\uffff\1\u0662\1\u0663",
            "\1\u0664",
            "\1\u04f3\2\uffff\1\u04f2\2\uffff\1\u0665",
            "\1\u0666",
            "\1\u0666",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u059c",
            "\1\u0667",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u059c\3\uffff\1\u0668",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u0669",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u066c\16\uffff\1\u066d\17\uffff\1\u066e\1\u066f",
            "\1\u0670",
            "\1\u066b\2\uffff\1\u066a\2\uffff\1\u0671",
            "\1\u0672",
            "\1\u0672",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u012f\1\u04ff",
            "\1\u0673",
            "\1\u0674",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0675\16\uffff\1\u0676\17\uffff\1\u0677\1\u0678",
            "\1\u0679",
            "\1\u0502\2\uffff\1\u0501\2\uffff\1\u067a",
            "\1\u067b",
            "\1\u067b",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u05b5",
            "\1\u067c",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u05b5\3\uffff\1\u067d",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u067e",
            "\1\u067f",
            "\1\u0680",
            "\1\u012f\1\u038a\2\uffff\1\u0681",
            "\1\u0682",
            "\1\u0682",
            "\1\u012f\1\u038a",
            "\1\u012f\1\u038a",
            "\1\u012f\1\u038a",
            "\1\u0438",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u0683\16\uffff\1\u0684\17\uffff\1\u0685\1\u0686",
            "\1\u0687",
            "\1\u0510\2\uffff\1\u050f\2\uffff\1\u0688",
            "\1\u0689",
            "\1\u0689",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u05cc",
            "\1\u068a",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u05cc\3\uffff\1\u068b",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u068c",
            "\1\u068d",
            "\1\u068e",
            "\1\177\1\u0399\2\uffff\1\u068f",
            "\1\u0690",
            "\1\u0690",
            "\1\177\1\u0399",
            "\1\177\1\u0399",
            "\1\177\1\u0399",
            "\1\u05da",
            "\1\u0691",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u05da\3\uffff\1\u0692",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u0693",
            "\1\u0452",
            "\1\u05e1",
            "\1\u0694",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u05e1\3\uffff\1\u0695",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u0696",
            "\1\u0460",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0699\16\uffff\1\u069a\17\uffff\1\u069b\1\u069c",
            "\1\u069d",
            "\1\u0698\2\uffff\1\u0697\2\uffff\1\u069e",
            "\1\u069f",
            "\1\u069f",
            "\1\u0698\2\uffff\1\u0697",
            "\1\177\1\u024a",
            "\1\u06a0",
            "\1\u046f",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u06a1\16\uffff\1\u06a2\17\uffff\1\u06a3\1\u06a4",
            "\1\u06a5",
            "\1\u0536\2\uffff\1\u0535\2\uffff\1\u06a6",
            "\1\u06a7",
            "\1\u06a7",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u05fb",
            "\1\u06a8",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u05fb\3\uffff\1\u06a9",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u06aa",
            "\1\u0601",
            "\1\u06ab",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0601\3\uffff\1\u06ac",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u06ad",
            "\1\u0489",
            "\1\u0490",
            "\1\u0609",
            "\1\u06ae",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u0609\3\uffff\1\u06af",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u06b0",
            "\1\u049f",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u06b3\16\uffff\1\u06b4\17\uffff\1\u06b5\1\u06b6",
            "\1\u06b7",
            "\1\u06b2\2\uffff\1\u06b1\2\uffff\1\u06b8",
            "\1\u06b9",
            "\1\u06b9",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\46\1\u0289",
            "\1\u06ba",
            "\1\u04ad",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u06bb\16\uffff\1\u06bc\17\uffff\1\u06bd\1\u06be",
            "\1\u06bf",
            "\1\u055e\2\uffff\1\u055d\2\uffff\1\u06c0",
            "\1\u06c1",
            "\1\u06c1",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0623",
            "\1\u06c2",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0623\3\uffff\1\u06c3",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u06c4",
            "\1\u0629",
            "\1\u06c5",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u0629\3\uffff\1\u06c6",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u06c7",
            "\1\u04c6",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u06ca\16\uffff\1\u06cb\17\uffff\1\u06cc\1\u06cd",
            "\1\u06ce",
            "\1\u06c9\2\uffff\1\u06c8\2\uffff\1\u06cf",
            "\1\u06d0",
            "\1\u06d0",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0171\1\u0574",
            "\1\u06d1",
            "\1\u06d2",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u06d3\16\uffff\1\u06d4\17\uffff\1\u06d5\1\u06d6",
            "\1\u06d7",
            "\1\u0577\2\uffff\1\u0576\2\uffff\1\u06d8",
            "\1\u06d9",
            "\1\u06d9",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0643",
            "\1\u06da",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0643\3\uffff\1\u06db",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u06dc",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u06dd\16\uffff\1\u06de\17\uffff\1\u06df\1\u06e0",
            "\1\u06e1",
            "\1\u0581\2\uffff\1\u0580\2\uffff\1\u06e2",
            "\1\u06e3",
            "\1\u06e3",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0651",
            "\1\u06e4",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0651\3\uffff\1\u06e5",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u06e6",
            "\1\u06e7",
            "\1\u06e8",
            "\1\u0171\1\u0406\2\uffff\1\u06e9",
            "\1\u06ea",
            "\1\u06ea",
            "\1\u0171\1\u0406",
            "\1\u0171\1\u0406",
            "\1\u0171\1\u0406",
            "\1\u058d",
            "\1\u06eb",
            "\1\u0379\2\uffff\1\u0378",
            "\1\u06ec",
            "\1\u06ee\3\uffff\1\u06ed",
            "\1\u06ef",
            "\1\u06ef",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u06f0",
            "\1\u04f3\2\uffff\1\u04f2\2\uffff\1\u06f1",
            "\1\u059c",
            "\1\u06f2",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u06f5\4\uffff\1\u06f6\12\uffff\1\u06f3\2\uffff\1\u06fa\1"+
            "\u06f7\10\uffff\1\u06f4\6\uffff\1\u06f8\1\u06f9",
            "\1\u012f\1\u04ff",
            "\1\u06fb",
            "\1\u06fd\3\uffff\1\u06fc",
            "\1\u06fe",
            "\1\u06fe",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u06ff",
            "\1\u066b\2\uffff\1\u066a\2\uffff\1\u0700",
            "\1\u0708\3\uffff\1\u0707\30\uffff\1\u0702\14\uffff\1\u0701"+
            "\1\u0706\1\u0703\17\uffff\1\u0704\1\u0705",
            "\1\u012f\1\u04ff",
            "\1\u0709",
            "\1\u070b\3\uffff\1\u070a",
            "\1\u070c",
            "\1\u070c",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u070d",
            "\1\u0502\2\uffff\1\u0501\2\uffff\1\u070e",
            "\1\u05b5",
            "\1\u070f",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0712\4\uffff\1\u0713\12\uffff\1\u0710\2\uffff\1\u0717\1"+
            "\u0714\10\uffff\1\u0711\6\uffff\1\u0715\1\u0716",
            "\1\u012f\1\u038a",
            "\1\u0718",
            "\1\u012f\1\u038a\2\uffff\1\u0719",
            "\1\u071a",
            "\1\u071c\3\uffff\1\u071b",
            "\1\u071d",
            "\1\u071d",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u071e",
            "\1\u0510\2\uffff\1\u050f\2\uffff\1\u071f",
            "\1\u05cc",
            "\1\u0720",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u0723\4\uffff\1\u0724\12\uffff\1\u0721\2\uffff\1\u0728\1"+
            "\u0725\10\uffff\1\u0722\6\uffff\1\u0726\1\u0727",
            "\1\177\1\u0399",
            "\1\u0729",
            "\1\177\1\u0399\2\uffff\1\u072a",
            "\1\u05da",
            "\1\u072b",
            "\1\u039c\2\uffff\1\u039b",
            "\1\u05e1",
            "\1\u072c",
            "\1\u03a6\2\uffff\1\u03a5",
            "\1\u072f\4\uffff\1\u0730\12\uffff\1\u072d\2\uffff\1\u0734\1"+
            "\u0731\10\uffff\1\u072e\6\uffff\1\u0732\1\u0733",
            "\1\177\1\u024a",
            "\1\u0735",
            "\1\u0737\3\uffff\1\u0736",
            "\1\u0738",
            "\1\u0738",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0739",
            "\1\u0698\2\uffff\1\u0697\2\uffff\1\u073a",
            "\1\177\1\u024a",
            "\1\u073b",
            "\1\u073d\3\uffff\1\u073c",
            "\1\u073e",
            "\1\u073e",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u073f",
            "\1\u0536\2\uffff\1\u0535\2\uffff\1\u0740",
            "\1\u05fb",
            "\1\u0741",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u0601",
            "\1\u0742",
            "\1\u03c5\2\uffff\1\u03c4",
            "\1\u0609",
            "\1\u0743",
            "\1\u03dc\2\uffff\1\u03db",
            "\1\u0746\4\uffff\1\u0747\12\uffff\1\u0744\2\uffff\1\u074b\1"+
            "\u0748\10\uffff\1\u0745\6\uffff\1\u0749\1\u074a",
            "\1\46\1\u0289",
            "\1\u074c",
            "\1\u074e\3\uffff\1\u074d",
            "\1\u074f",
            "\1\u074f",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u0750",
            "\1\u06b2\2\uffff\1\u06b1\2\uffff\1\u0751",
            "\1\46\1\u0289",
            "\1\u0752",
            "\1\u0754\3\uffff\1\u0753",
            "\1\u0755",
            "\1\u0755",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0756",
            "\1\u055e\2\uffff\1\u055d\2\uffff\1\u0757",
            "\1\u0623",
            "\1\u0758",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0629",
            "\1\u0759",
            "\1\u03f5\2\uffff\1\u03f4",
            "\1\u075c\4\uffff\1\u075d\12\uffff\1\u075a\2\uffff\1\u0761\1"+
            "\u075e\10\uffff\1\u075b\6\uffff\1\u075f\1\u0760",
            "\1\u0171\1\u0574",
            "\1\u0762",
            "\1\u0764\3\uffff\1\u0763",
            "\1\u0765",
            "\1\u0765",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0766",
            "\1\u06c9\2\uffff\1\u06c8\2\uffff\1\u0767",
            "\1\u076f\3\uffff\1\u076e\30\uffff\1\u0769\14\uffff\1\u0768"+
            "\1\u076d\1\u076a\17\uffff\1\u076b\1\u076c",
            "\1\u0171\1\u0574",
            "\1\u0770",
            "\1\u0772\3\uffff\1\u0771",
            "\1\u0773",
            "\1\u0773",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0774",
            "\1\u0577\2\uffff\1\u0576\2\uffff\1\u0775",
            "\1\u0643",
            "\1\u0776",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0777",
            "\1\u0779\3\uffff\1\u0778",
            "\1\u077a",
            "\1\u077a",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u077b",
            "\1\u0581\2\uffff\1\u0580\2\uffff\1\u077c",
            "\1\u0651",
            "\1\u077d",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0780\4\uffff\1\u0781\12\uffff\1\u077e\2\uffff\1\u0785\1"+
            "\u0782\10\uffff\1\u077f\6\uffff\1\u0783\1\u0784",
            "\1\u0171\1\u0406",
            "\1\u0786",
            "\1\u0171\1\u0406\2\uffff\1\u0787",
            "\1\u058d",
            "\1\u06ee",
            "\1\u0788",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u06ee\3\uffff\1\u0789",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u078a",
            "\1\u059c",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u078b\16\uffff\1\u078c\17\uffff\1\u078d\1\u078e",
            "\1\u078f",
            "\1\u066b\2\uffff\1\u066a\2\uffff\1\u0790",
            "\1\u0791",
            "\1\u0791",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u06fd",
            "\1\u0792",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u06fd\3\uffff\1\u0793",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u0794",
            "\1\u0795",
            "\1\u0796",
            "\1\u012f\1\u04ff\2\uffff\1\u0797",
            "\1\u0798",
            "\1\u0798",
            "\1\u012f\1\u04ff",
            "\1\u012f\1\u04ff",
            "\1\u012f\1\u04ff",
            "\1\u070b",
            "\1\u0799",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u070b\3\uffff\1\u079a",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u079b",
            "\1\u05b5",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u079e\16\uffff\1\u079f\17\uffff\1\u07a0\1\u07a1",
            "\1\u07a2",
            "\1\u079d\2\uffff\1\u079c\2\uffff\1\u07a3",
            "\1\u07a4",
            "\1\u07a4",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u012f\1\u038a",
            "\1\u07a5",
            "\1\u071c",
            "\1\u07a6",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u071c\3\uffff\1\u07a7",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u07a8",
            "\1\u05cc",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u07ab\16\uffff\1\u07ac\17\uffff\1\u07ad\1\u07ae",
            "\1\u07af",
            "\1\u07aa\2\uffff\1\u07a9\2\uffff\1\u07b0",
            "\1\u07b1",
            "\1\u07b1",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\177\1\u0399",
            "\1\u07b2",
            "\1\u05da",
            "\1\u05e1",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u07b3\16\uffff\1\u07b4\17\uffff\1\u07b5\1\u07b6",
            "\1\u07b7",
            "\1\u0698\2\uffff\1\u0697\2\uffff\1\u07b8",
            "\1\u07b9",
            "\1\u07b9",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0737",
            "\1\u07ba",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0737\3\uffff\1\u07bb",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u07bc",
            "\1\u073d",
            "\1\u07bd",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u073d\3\uffff\1\u07be",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u07bf",
            "\1\u05fb",
            "\1\u0601",
            "\1\u0609",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u07c0\16\uffff\1\u07c1\17\uffff\1\u07c2\1\u07c3",
            "\1\u07c4",
            "\1\u06b2\2\uffff\1\u06b1\2\uffff\1\u07c5",
            "\1\u07c6",
            "\1\u07c6",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u074e",
            "\1\u07c7",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u074e\3\uffff\1\u07c8",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u07c9",
            "\1\u0754",
            "\1\u07ca",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u0754\3\uffff\1\u07cb",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u07cc",
            "\1\u0623",
            "\1\u0629",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u07cd\16\uffff\1\u07ce\17\uffff\1\u07cf\1\u07d0",
            "\1\u07d1",
            "\1\u06c9\2\uffff\1\u06c8\2\uffff\1\u07d2",
            "\1\u07d3",
            "\1\u07d3",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0764",
            "\1\u07d4",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0764\3\uffff\1\u07d5",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u07d6",
            "\1\u07d7",
            "\1\u07d8",
            "\1\u0171\1\u0574\2\uffff\1\u07d9",
            "\1\u07da",
            "\1\u07da",
            "\1\u0171\1\u0574",
            "\1\u0171\1\u0574",
            "\1\u0171\1\u0574",
            "\1\u0772",
            "\1\u07db",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0772\3\uffff\1\u07dc",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u07dd",
            "\1\u0643",
            "\1\u0779",
            "\1\u07de",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0779\3\uffff\1\u07df",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u07e0",
            "\1\u0651",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u07e3\16\uffff\1\u07e4\17\uffff\1\u07e5\1\u07e6",
            "\1\u07e7",
            "\1\u07e2\2\uffff\1\u07e1\2\uffff\1\u07e8",
            "\1\u07e9",
            "\1\u07e9",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u0171\1\u0406",
            "\1\u07ea",
            "\1\u06ee",
            "\1\u07eb",
            "\1\u04f3\2\uffff\1\u04f2",
            "\1\u07ec",
            "\1\u07ee\3\uffff\1\u07ed",
            "\1\u07ef",
            "\1\u07ef",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u07f0",
            "\1\u066b\2\uffff\1\u066a\2\uffff\1\u07f1",
            "\1\u06fd",
            "\1\u07f2",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u07f5\4\uffff\1\u07f6\12\uffff\1\u07f3\2\uffff\1\u07fa\1"+
            "\u07f7\10\uffff\1\u07f4\6\uffff\1\u07f8\1\u07f9",
            "\1\u012f\1\u04ff",
            "\1\u07fb",
            "\1\u012f\1\u04ff\2\uffff\1\u07fc",
            "\1\u070b",
            "\1\u07fd",
            "\1\u0502\2\uffff\1\u0501",
            "\1\u0800\4\uffff\1\u0801\12\uffff\1\u07fe\2\uffff\1\u0805\1"+
            "\u0802\10\uffff\1\u07ff\6\uffff\1\u0803\1\u0804",
            "\1\u012f\1\u038a",
            "\1\u0806",
            "\1\u0808\3\uffff\1\u0807",
            "\1\u0809",
            "\1\u0809",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u080a",
            "\1\u079d\2\uffff\1\u079c\2\uffff\1\u080b",
            "\1\u012f\1\u038a",
            "\1\u071c",
            "\1\u080c",
            "\1\u0510\2\uffff\1\u050f",
            "\1\u080f\4\uffff\1\u0810\12\uffff\1\u080d\2\uffff\1\u0814\1"+
            "\u0811\10\uffff\1\u080e\6\uffff\1\u0812\1\u0813",
            "\1\177\1\u0399",
            "\1\u0815",
            "\1\u0817\3\uffff\1\u0816",
            "\1\u0818",
            "\1\u0818",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u0819",
            "\1\u07aa\2\uffff\1\u07a9\2\uffff\1\u081a",
            "\1\177\1\u0399",
            "\1\u081b",
            "\1\u081d\3\uffff\1\u081c",
            "\1\u081e",
            "\1\u081e",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u081f",
            "\1\u0698\2\uffff\1\u0697\2\uffff\1\u0820",
            "\1\u0737",
            "\1\u0821",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u073d",
            "\1\u0822",
            "\1\u0536\2\uffff\1\u0535",
            "\1\u0823",
            "\1\u0825\3\uffff\1\u0824",
            "\1\u0826",
            "\1\u0826",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u0827",
            "\1\u06b2\2\uffff\1\u06b1\2\uffff\1\u0828",
            "\1\u074e",
            "\1\u0829",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u0754",
            "\1\u082a",
            "\1\u055e\2\uffff\1\u055d",
            "\1\u082b",
            "\1\u082d\3\uffff\1\u082c",
            "\1\u082e",
            "\1\u082e",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u082f",
            "\1\u06c9\2\uffff\1\u06c8\2\uffff\1\u0830",
            "\1\u0764",
            "\1\u0831",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0834\4\uffff\1\u0835\12\uffff\1\u0832\2\uffff\1\u0839\1"+
            "\u0836\10\uffff\1\u0833\6\uffff\1\u0837\1\u0838",
            "\1\u0171\1\u0574",
            "\1\u083a",
            "\1\u0171\1\u0574\2\uffff\1\u083b",
            "\1\u0772",
            "\1\u083c",
            "\1\u0577\2\uffff\1\u0576",
            "\1\u0779",
            "\1\u083d",
            "\1\u0581\2\uffff\1\u0580",
            "\1\u0840\4\uffff\1\u0841\12\uffff\1\u083e\2\uffff\1\u0845\1"+
            "\u0842\10\uffff\1\u083f\6\uffff\1\u0843\1\u0844",
            "\1\u0171\1\u0406",
            "\1\u0846",
            "\1\u0848\3\uffff\1\u0847",
            "\1\u0849",
            "\1\u0849",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u084a",
            "\1\u07e2\2\uffff\1\u07e1\2\uffff\1\u084b",
            "\1\u0171\1\u0406",
            "\1\u06ee",
            "\1\u07ee",
            "\1\u084c",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u07ee\3\uffff\1\u084d",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u084e",
            "\1\u06fd",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0851\16\uffff\1\u0852\17\uffff\1\u0853\1\u0854",
            "\1\u0855",
            "\1\u0850\2\uffff\1\u084f\2\uffff\1\u0856",
            "\1\u0857",
            "\1\u0857",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u012f\1\u04ff",
            "\1\u0858",
            "\1\u070b",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u0859\16\uffff\1\u085a\17\uffff\1\u085b\1\u085c",
            "\1\u085d",
            "\1\u079d\2\uffff\1\u079c\2\uffff\1\u085e",
            "\1\u085f",
            "\1\u085f",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u0808",
            "\1\u0860",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u0808\3\uffff\1\u0861",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u0862",
            "\1\u071c",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u0863\16\uffff\1\u0864\17\uffff\1\u0865\1\u0866",
            "\1\u0867",
            "\1\u07aa\2\uffff\1\u07a9\2\uffff\1\u0868",
            "\1\u0869",
            "\1\u0869",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u0817",
            "\1\u086a",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u0817\3\uffff\1\u086b",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u086c",
            "\1\u081d",
            "\1\u086d",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u081d\3\uffff\1\u086e",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u086f",
            "\1\u0737",
            "\1\u073d",
            "\1\u0825",
            "\1\u0870",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u0825\3\uffff\1\u0871",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u0872",
            "\1\u074e",
            "\1\u0754",
            "\1\u082d",
            "\1\u0873",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u082d\3\uffff\1\u0874",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u0875",
            "\1\u0764",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u0878\16\uffff\1\u0879\17\uffff\1\u087a\1\u087b",
            "\1\u087c",
            "\1\u0877\2\uffff\1\u0876\2\uffff\1\u087d",
            "\1\u087e",
            "\1\u087e",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u0171\1\u0574",
            "\1\u087f",
            "\1\u0772",
            "\1\u0779",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u0880\16\uffff\1\u0881\17\uffff\1\u0882\1\u0883",
            "\1\u0884",
            "\1\u07e2\2\uffff\1\u07e1\2\uffff\1\u0885",
            "\1\u0886",
            "\1\u0886",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u0848",
            "\1\u0887",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u0848\3\uffff\1\u0888",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u0889",
            "\1\u07ee",
            "\1\u088a",
            "\1\u066b\2\uffff\1\u066a",
            "\1\u088d\4\uffff\1\u088e\12\uffff\1\u088b\2\uffff\1\u0892\1"+
            "\u088f\10\uffff\1\u088c\6\uffff\1\u0890\1\u0891",
            "\1\u012f\1\u04ff",
            "\1\u0893",
            "\1\u0895\3\uffff\1\u0894",
            "\1\u0896",
            "\1\u0896",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0897",
            "\1\u0850\2\uffff\1\u084f\2\uffff\1\u0898",
            "\1\u012f\1\u04ff",
            "\1\u0899",
            "\1\u089b\3\uffff\1\u089a",
            "\1\u089c",
            "\1\u089c",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u089d",
            "\1\u079d\2\uffff\1\u079c\2\uffff\1\u089e",
            "\1\u0808",
            "\1\u089f",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u08a0",
            "\1\u08a2\3\uffff\1\u08a1",
            "\1\u08a3",
            "\1\u08a3",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u08a4",
            "\1\u07aa\2\uffff\1\u07a9\2\uffff\1\u08a5",
            "\1\u0817",
            "\1\u08a6",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u081d",
            "\1\u08a7",
            "\1\u0698\2\uffff\1\u0697",
            "\1\u0825",
            "\1\u08a8",
            "\1\u06b2\2\uffff\1\u06b1",
            "\1\u082d",
            "\1\u08a9",
            "\1\u06c9\2\uffff\1\u06c8",
            "\1\u08ac\4\uffff\1\u08ad\12\uffff\1\u08aa\2\uffff\1\u08b1\1"+
            "\u08ae\10\uffff\1\u08ab\6\uffff\1\u08af\1\u08b0",
            "\1\u0171\1\u0574",
            "\1\u08b2",
            "\1\u08b4\3\uffff\1\u08b3",
            "\1\u08b5",
            "\1\u08b5",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08b6",
            "\1\u0877\2\uffff\1\u0876\2\uffff\1\u08b7",
            "\1\u0171\1\u0574",
            "\1\u08b8",
            "\1\u08ba\3\uffff\1\u08b9",
            "\1\u08bb",
            "\1\u08bb",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u08bc",
            "\1\u07e2\2\uffff\1\u07e1\2\uffff\1\u08bd",
            "\1\u0848",
            "\1\u08be",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u07ee",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08bf\16\uffff\1\u08c0\17\uffff\1\u08c1\1\u08c2",
            "\1\u08c3",
            "\1\u0850\2\uffff\1\u084f\2\uffff\1\u08c4",
            "\1\u08c5",
            "\1\u08c5",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0895",
            "\1\u08c6",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u0895\3\uffff\1\u08c7",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08c8",
            "\1\u089b",
            "\1\u08c9",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u089b\3\uffff\1\u08ca",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u08cb",
            "\1\u0808",
            "\1\u08a2",
            "\1\u08cc",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u08a2\3\uffff\1\u08cd",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u08ce",
            "\1\u0817",
            "\1\u081d",
            "\1\u0825",
            "\1\u082d",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08cf\16\uffff\1\u08d0\17\uffff\1\u08d1\1\u08d2",
            "\1\u08d3",
            "\1\u0877\2\uffff\1\u0876\2\uffff\1\u08d4",
            "\1\u08d5",
            "\1\u08d5",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08b4",
            "\1\u08d6",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08b4\3\uffff\1\u08d7",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08d8",
            "\1\u08ba",
            "\1\u08d9",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u08ba\3\uffff\1\u08da",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u08db",
            "\1\u0848",
            "\1\u08dc",
            "\1\u08de\3\uffff\1\u08dd",
            "\1\u08df",
            "\1\u08df",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08e0",
            "\1\u0850\2\uffff\1\u084f\2\uffff\1\u08e1",
            "\1\u0895",
            "\1\u08e2",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u089b",
            "\1\u08e3",
            "\1\u079d\2\uffff\1\u079c",
            "\1\u08a2",
            "\1\u08e4",
            "\1\u07aa\2\uffff\1\u07a9",
            "\1\u08e5",
            "\1\u08e7\3\uffff\1\u08e6",
            "\1\u08e8",
            "\1\u08e8",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08e9",
            "\1\u0877\2\uffff\1\u0876\2\uffff\1\u08ea",
            "\1\u08b4",
            "\1\u08eb",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08ba",
            "\1\u08ec",
            "\1\u07e2\2\uffff\1\u07e1",
            "\1\u08de",
            "\1\u08ed",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08de\3\uffff\1\u08ee",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08ef",
            "\1\u0895",
            "\1\u089b",
            "\1\u08a2",
            "\1\u08e7",
            "\1\u08f0",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08e7\3\uffff\1\u08f1",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08f2",
            "\1\u08b4",
            "\1\u08ba",
            "\1\u08de",
            "\1\u08f3",
            "\1\u0850\2\uffff\1\u084f",
            "\1\u08e7",
            "\1\u08f4",
            "\1\u0877\2\uffff\1\u0876",
            "\1\u08de",
            "\1\u08e7"
    };

    static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
    static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
    static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
    static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
    static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
    static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
    static final short[][] DFA31_transition;

    static {
        int numStates = DFA31_transitionS.length;
        DFA31_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
        }
    }

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = DFA31_eot;
            this.eof = DFA31_eof;
            this.min = DFA31_min;
            this.max = DFA31_max;
            this.accept = DFA31_accept;
            this.special = DFA31_special;
            this.transition = DFA31_transition;
        }
        public String getDescription() {
            return "287:1: functionCallTarget : ( ( functionCall passOp )=>f1= functionCall passOp f2= callTarget -> ^( $f1 passOp $f2) | f1= functionCall -> ^( $f1 YIELDS ^( PACK DEFAULT ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA31_42 = input.LA(1);

                         
                        int index31_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA31_42==YIELDS||LA31_42==GUIDE_YIELD) && (synpred2_Stencil())) {s = 109;}

                        else if ( (LA31_42==EOF||LA31_42==ALL||LA31_42==CANVAS||(LA31_42>=FILTER && LA31_42<=GUIDE)||(LA31_42>=LOCAL && LA31_42<=TEMPLATE)||(LA31_42>=PYTHON && LA31_42<=VIEW)||LA31_42==GROUP||(LA31_42>=SPLIT && LA31_42<=JOIN)||LA31_42==ID) ) {s = 110;}

                         
                        input.seek(index31_42);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 31, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA39_eotS =
        "\44\uffff";
    static final String DFA39_eofS =
        "\1\2\43\uffff";
    static final String DFA39_minS =
        "\1\47\1\40\1\uffff\1\71\1\110\1\uffff\1\114\1\71\2\114\1\73\2\uffff"+
        "\2\71\1\114\1\75\1\71\1\124\2\71\1\114\1\71\3\114\1\71\2\uffff\1"+
        "\71\1\114\3\71\1\114\1\71";
    static final String DFA39_maxS =
        "\1\70\1\135\1\uffff\1\123\1\110\1\uffff\1\114\1\75\2\114\1\73\2"+
        "\uffff\1\105\1\73\1\114\1\135\1\75\1\124\1\73\1\105\1\114\1\105"+
        "\3\114\1\105\2\uffff\1\105\1\114\1\105\1\73\1\105\1\114\1\105";
    static final String DFA39_acceptS =
        "\2\uffff\1\6\2\uffff\1\5\5\uffff\1\4\1\2\16\uffff\1\3\1\1\7\uffff";
    static final String DFA39_specialS =
        "\44\uffff}>";
    static final String[] DFA39_transitionS = {
            "\1\2\1\uffff\1\2\2\uffff\3\2\1\uffff\1\2\1\uffff\1\2\3\uffff"+
            "\1\2\1\uffff\1\1",
            "\1\5\3\uffff\1\5\12\uffff\1\4\11\uffff\1\5\3\uffff\1\6\12\uffff"+
            "\1\3\1\uffff\2\5\1\7\7\uffff\1\12\7\uffff\1\10\1\11",
            "",
            "\2\14\12\uffff\1\13\15\uffff\1\5",
            "\1\15",
            "",
            "\1\16",
            "\2\5\1\20\1\uffff\1\17",
            "\1\21",
            "\1\21",
            "\1\22",
            "",
            "",
            "\2\14\12\uffff\1\13",
            "\2\5\1\20",
            "\1\23",
            "\1\25\16\uffff\1\26\7\uffff\1\24\7\uffff\1\27\1\30",
            "\2\5\1\20\1\uffff\1\31",
            "\1\32",
            "\2\5\1\20",
            "\2\34\12\uffff\1\33",
            "\1\35",
            "\2\34\2\uffff\1\36\7\uffff\1\33",
            "\1\37",
            "\1\37",
            "\1\40",
            "\2\34\12\uffff\1\33",
            "",
            "",
            "\2\34\12\uffff\1\33",
            "\1\41",
            "\2\34\2\uffff\1\42\7\uffff\1\33",
            "\2\5\1\20",
            "\2\34\12\uffff\1\33",
            "\1\43",
            "\2\34\12\uffff\1\33"
    };

    static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
    static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
    static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
    static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
    static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
    static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
    static final short[][] DFA39_transition;

    static {
        int numStates = DFA39_transitionS.length;
        DFA39_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
        }
    }

    class DFA39 extends DFA {

        public DFA39(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 39;
            this.eot = DFA39_eot;
            this.eof = DFA39_eof;
            this.min = DFA39_min;
            this.max = DFA39_max;
            this.accept = DFA39_accept;
            this.special = DFA39_special;
            this.transition = DFA39_transition;
        }
        public String getDescription() {
            return "339:1: specializer[RuleOpts opts] : ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) );";
        }
    }
    static final String DFA41_eotS =
        "\u008c\uffff";
    static final String DFA41_eofS =
        "\u008c\uffff";
    static final String DFA41_minS =
        "\1\40\1\uffff\1\66\1\114\1\71\2\114\3\71\1\uffff\1\70\1\71\1\114"+
        "\1\40\1\uffff\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\uffff"+
        "\1\66\1\114\1\71\2\114\3\71\1\114\1\70\1\71\1\114\1\71\2\114\1\67"+
        "\1\114\1\67\1\70\1\71\1\114\2\71\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\1\114\1\67\1\71\1\67\1\114\2\67\1\75\1\114\1\67\2\114\1\67"+
        "\1\71\2\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114\1\67\1\70\1\71"+
        "\1\114\1\71\2\114\1\67\1\114\1\67\2\71\1\114\1\67\1\71\1\67\1\114"+
        "\1\71\2\67\1\75\1\114\1\67\2\114\1\67\1\71\1\114\1\67\1\71\1\67"+
        "\1\114\1\71\1\114\1\67\1\114\1\71\2\114\1\67\1\114\1\67\1\71\1\114"+
        "\1\67\2\71\1\114\1\67\1\71\1\67\1\114\2\71\1\114\1\67\1\71";
    static final String DFA41_maxS =
        "\1\135\1\uffff\1\66\1\114\1\75\2\114\3\72\1\uffff\1\135\1\72\1\114"+
        "\1\135\1\uffff\1\75\2\72\1\135\1\114\1\75\2\114\2\72\1\uffff\1\66"+
        "\1\114\1\75\2\114\3\72\1\114\1\135\1\72\1\114\1\75\2\114\1\72\1"+
        "\114\1\75\1\135\1\72\1\114\1\75\3\72\1\135\1\114\1\75\2\114\1\72"+
        "\1\71\1\114\1\72\1\75\1\72\1\114\2\72\1\135\1\114\1\75\2\114\2\72"+
        "\2\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114\1\72\1\135\1\72\1\114"+
        "\1\75\2\114\1\72\1\114\1\75\1\72\1\71\1\114\1\72\1\75\1\72\1\114"+
        "\1\71\2\72\1\135\1\114\1\75\2\114\1\72\1\71\1\114\1\72\1\75\1\72"+
        "\1\114\1\71\1\114\1\72\1\114\1\75\2\114\1\72\1\114\1\75\1\71\1\114"+
        "\1\72\2\71\1\114\1\72\1\75\1\72\1\114\2\71\1\114\1\72\1\71";
    static final String DFA41_acceptS =
        "\1\uffff\1\1\10\uffff\1\3\4\uffff\1\2\12\uffff\1\4\161\uffff";
    static final String DFA41_specialS =
        "\u008c\uffff}>";
    static final String[] DFA41_transitionS = {
            "\1\11\3\uffff\1\10\24\uffff\1\1\3\uffff\1\3\12\uffff\1\12\1"+
            "\uffff\1\2\1\7\1\4\17\uffff\1\5\1\6",
            "",
            "\1\13",
            "\1\14",
            "\1\17\1\16\2\uffff\1\15",
            "\1\20",
            "\1\20",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\17\1\16",
            "",
            "\1\23\4\uffff\1\24\12\uffff\1\21\2\uffff\1\30\1\25\10\uffff"+
            "\1\22\6\uffff\1\26\1\27",
            "\1\17\1\16",
            "\1\31",
            "\1\42\3\uffff\1\41\30\uffff\1\34\12\uffff\1\32\1\uffff\1\33"+
            "\1\40\1\35\17\uffff\1\36\1\37",
            "",
            "\1\17\1\16\2\uffff\1\43",
            "\1\45\2\uffff\1\44",
            "\1\45\2\uffff\1\44",
            "\1\46\16\uffff\1\47\17\uffff\1\50\1\51",
            "\1\52",
            "\1\45\2\uffff\1\44\2\uffff\1\53",
            "\1\54",
            "\1\54",
            "\1\45\2\uffff\1\44",
            "\1\17\1\16",
            "",
            "\1\55",
            "\1\56",
            "\1\17\1\16\2\uffff\1\57",
            "\1\60",
            "\1\60",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\61",
            "\1\64\4\uffff\1\65\12\uffff\1\62\2\uffff\1\71\1\66\10\uffff"+
            "\1\63\6\uffff\1\67\1\70",
            "\1\17\1\16",
            "\1\72",
            "\1\74\3\uffff\1\73",
            "\1\75",
            "\1\75",
            "\1\45\2\uffff\1\44",
            "\1\76",
            "\1\45\2\uffff\1\44\2\uffff\1\77",
            "\1\102\4\uffff\1\103\12\uffff\1\100\2\uffff\1\107\1\104\10"+
            "\uffff\1\101\6\uffff\1\105\1\106",
            "\1\17\1\16",
            "\1\110",
            "\1\17\1\16\2\uffff\1\111",
            "\1\17\1\16",
            "\1\45\2\uffff\1\44",
            "\1\45\2\uffff\1\44",
            "\1\112\16\uffff\1\113\17\uffff\1\114\1\115",
            "\1\116",
            "\1\45\2\uffff\1\44\2\uffff\1\117",
            "\1\120",
            "\1\120",
            "\1\45\2\uffff\1\44",
            "\1\74",
            "\1\121",
            "\1\45\2\uffff\1\44",
            "\1\74\3\uffff\1\122",
            "\1\45\2\uffff\1\44",
            "\1\123",
            "\1\125\2\uffff\1\124",
            "\1\125\2\uffff\1\124",
            "\1\126\16\uffff\1\127\17\uffff\1\130\1\131",
            "\1\132",
            "\1\125\2\uffff\1\124\2\uffff\1\133",
            "\1\134",
            "\1\134",
            "\1\125\2\uffff\1\124",
            "\1\17\1\16",
            "\1\135",
            "\1\136",
            "\1\140\3\uffff\1\137",
            "\1\141",
            "\1\141",
            "\1\45\2\uffff\1\44",
            "\1\142",
            "\1\45\2\uffff\1\44\2\uffff\1\143",
            "\1\74",
            "\1\144",
            "\1\45\2\uffff\1\44",
            "\1\147\4\uffff\1\150\12\uffff\1\145\2\uffff\1\154\1\151\10"+
            "\uffff\1\146\6\uffff\1\152\1\153",
            "\1\17\1\16",
            "\1\155",
            "\1\157\3\uffff\1\156",
            "\1\160",
            "\1\160",
            "\1\125\2\uffff\1\124",
            "\1\161",
            "\1\125\2\uffff\1\124\2\uffff\1\162",
            "\1\17\1\16",
            "\1\140",
            "\1\163",
            "\1\45\2\uffff\1\44",
            "\1\140\3\uffff\1\164",
            "\1\45\2\uffff\1\44",
            "\1\165",
            "\1\74",
            "\1\125\2\uffff\1\124",
            "\1\125\2\uffff\1\124",
            "\1\166\16\uffff\1\167\17\uffff\1\170\1\171",
            "\1\172",
            "\1\125\2\uffff\1\124\2\uffff\1\173",
            "\1\174",
            "\1\174",
            "\1\125\2\uffff\1\124",
            "\1\157",
            "\1\175",
            "\1\125\2\uffff\1\124",
            "\1\157\3\uffff\1\176",
            "\1\125\2\uffff\1\124",
            "\1\177",
            "\1\140",
            "\1\u0080",
            "\1\45\2\uffff\1\44",
            "\1\u0081",
            "\1\u0083\3\uffff\1\u0082",
            "\1\u0084",
            "\1\u0084",
            "\1\125\2\uffff\1\124",
            "\1\u0085",
            "\1\125\2\uffff\1\124\2\uffff\1\u0086",
            "\1\157",
            "\1\u0087",
            "\1\125\2\uffff\1\124",
            "\1\140",
            "\1\u0083",
            "\1\u0088",
            "\1\125\2\uffff\1\124",
            "\1\u0083\3\uffff\1\u0089",
            "\1\125\2\uffff\1\124",
            "\1\u008a",
            "\1\157",
            "\1\u0083",
            "\1\u008b",
            "\1\125\2\uffff\1\124",
            "\1\u0083"
    };

    static final short[] DFA41_eot = DFA.unpackEncodedString(DFA41_eotS);
    static final short[] DFA41_eof = DFA.unpackEncodedString(DFA41_eofS);
    static final char[] DFA41_min = DFA.unpackEncodedStringToUnsignedChars(DFA41_minS);
    static final char[] DFA41_max = DFA.unpackEncodedStringToUnsignedChars(DFA41_maxS);
    static final short[] DFA41_accept = DFA.unpackEncodedString(DFA41_acceptS);
    static final short[] DFA41_special = DFA.unpackEncodedString(DFA41_specialS);
    static final short[][] DFA41_transition;

    static {
        int numStates = DFA41_transitionS.length;
        DFA41_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA41_transition[i] = DFA.unpackEncodedString(DFA41_transitionS[i]);
        }
    }

    class DFA41 extends DFA {

        public DFA41(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 41;
            this.eot = DFA41_eot;
            this.eof = DFA41_eof;
            this.min = DFA41_min;
            this.max = DFA41_max;
            this.accept = DFA41_accept;
            this.special = DFA41_special;
            this.transition = DFA41_transition;
        }
        public String getDescription() {
            return "356:1: argList : ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList );";
        }
    }
    static final String DFA47_eotS =
        "\17\uffff";
    static final String DFA47_eofS =
        "\17\uffff";
    static final String DFA47_minS =
        "\1\75\1\114\1\73\2\114\1\uffff\1\73\1\114\1\75\2\73\2\uffff\1\114"+
        "\1\73";
    static final String DFA47_maxS =
        "\1\135\1\114\1\75\2\114\1\uffff\1\73\1\114\1\135\1\75\1\73\2\uffff"+
        "\1\114\1\73";
    static final String DFA47_acceptS =
        "\5\uffff\1\3\5\uffff\1\2\1\1\2\uffff";
    static final String DFA47_specialS =
        "\17\uffff}>";
    static final String[] DFA47_transitionS = {
            "\1\1\16\uffff\1\2\7\uffff\1\5\7\uffff\1\3\1\4",
            "\1\6",
            "\1\10\1\uffff\1\7",
            "\1\11",
            "\1\11",
            "",
            "\1\10",
            "\1\12",
            "\1\14\16\uffff\1\14\7\uffff\1\13\7\uffff\2\14",
            "\1\10\1\uffff\1\15",
            "\1\10",
            "",
            "",
            "\1\16",
            "\1\10"
    };

    static final short[] DFA47_eot = DFA.unpackEncodedString(DFA47_eotS);
    static final short[] DFA47_eof = DFA.unpackEncodedString(DFA47_eofS);
    static final char[] DFA47_min = DFA.unpackEncodedStringToUnsignedChars(DFA47_minS);
    static final char[] DFA47_max = DFA.unpackEncodedStringToUnsignedChars(DFA47_maxS);
    static final short[] DFA47_accept = DFA.unpackEncodedString(DFA47_acceptS);
    static final short[] DFA47_special = DFA.unpackEncodedString(DFA47_specialS);
    static final short[][] DFA47_transition;

    static {
        int numStates = DFA47_transitionS.length;
        DFA47_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA47_transition[i] = DFA.unpackEncodedString(DFA47_transitionS[i]);
        }
    }

    class DFA47 extends DFA {

        public DFA47(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 47;
            this.eot = DFA47_eot;
            this.eof = DFA47_eof;
            this.min = DFA47_min;
            this.max = DFA47_max;
            this.accept = DFA47_accept;
            this.special = DFA47_special;
            this.transition = DFA47_transition;
        }
        public String getDescription() {
            return "384:1: range : ( number RANGE number -> ^( RANGE number number ) | number RANGE 'n' -> ^( RANGE number NUMBER[RANGE_END] ) | 'n' RANGE 'n' -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) );";
        }
    }
 

    public static final BitSet FOLLOW_imports_in_program640 = new BitSet(new long[]{0x0005F42400000000L});
    public static final BitSet FOLLOW_externals_in_program643 = new BitSet(new long[]{0x0005F00400000000L});
    public static final BitSet FOLLOW_order_in_program645 = new BitSet(new long[]{0x0005700400000000L});
    public static final BitSet FOLLOW_canvasLayer_in_program647 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_streamDef_in_program650 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_layerDef_in_program654 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_operatorDef_in_program658 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_pythonDef_in_program662 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_operatorTemplate_in_program666 = new BitSet(new long[]{0x0005700000000002L});
    public static final BitSet FOLLOW_IMPORT_in_imports825 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_imports829 = new BitSet(new long[]{0x0110000000000002L});
    public static final BitSet FOLLOW_ARG_in_imports832 = new BitSet(new long[]{0x2200001100000000L,0x0000000030001D00L});
    public static final BitSet FOLLOW_argList_in_imports836 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_imports838 = new BitSet(new long[]{0x0010000000000002L});
    public static final BitSet FOLLOW_AS_in_imports843 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_imports847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_order943 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_orderRef_in_order945 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_order948 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_orderRef_in_order950 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_ID_in_orderRef985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_orderRef1000 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_orderRef1002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_orderRef1005 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_orderRef1007 = new BitSet(new long[]{0x0080000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_orderRef1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_externalStream_in_externals1029 = new BitSet(new long[]{0x0000002000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_externalStream1046 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_STREAM_in_externalStream1048 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_externalStream1052 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_externalStream1054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CANVAS_in_canvasLayer1077 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_canvasLayer1081 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_canvasProperties_in_canvasLayer1083 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_guideDef_in_canvasLayer1085 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_GUIDE_in_guideDef1137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1141 = new BitSet(new long[]{0x0100008000000000L});
    public static final BitSet FOLLOW_specializer_in_guideDef1145 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_FROM_in_guideDef1148 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1152 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1156 = new BitSet(new long[]{0x004A090400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_guideDef1158 = new BitSet(new long[]{0x004A090400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_specializer_in_canvasProperties1195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STREAM_in_streamDef1209 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_streamDef1213 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_streamDef1215 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_consumesBlock_in_streamDef1220 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_LAYER_in_layerDef1255 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_layerDef1259 = new BitSet(new long[]{0x0100008000000000L});
    public static final BitSet FOLLOW_implantationDef_in_layerDef1261 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_consumesBlock_in_layerDef1263 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_ARG_in_implantationDef1298 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_implantationDef1302 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_implantationDef1304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FROM_in_consumesBlock1333 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_consumesBlock1337 = new BitSet(new long[]{0x004A094400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_filterRule_in_consumesBlock1339 = new BitSet(new long[]{0x004A094400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_consumesBlock1342 = new BitSet(new long[]{0x004A090400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_FILTER_in_filterRule1382 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_rulePredicate_in_filterRule1384 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_DEFINE_in_filterRule1386 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_callGroup_in_filterRule1388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_rulePredicate1412 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ALL_in_rulePredicate1414 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_rulePredicate1416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_rulePredicate1439 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1441 = new BitSet(new long[]{0x0000000000000000L,0x000000000FCA0000L});
    public static final BitSet FOLLOW_booleanOp_in_rulePredicate1443 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1445 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_rulePredicate1448 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1450 = new BitSet(new long[]{0x0000000000000000L,0x000000000FCA0000L});
    public static final BitSet FOLLOW_booleanOp_in_rulePredicate1452 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1454 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_rulePredicate1458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_operatorTemplate1492 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorTemplate1494 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorTemplate1498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorDef1517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1522 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_operatorDef1524 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_YIELDS_in_operatorDef1527 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_operatorDef1529 = new BitSet(new long[]{0x0040000100000000L});
    public static final BitSet FOLLOW_operatorRule_in_operatorDef1532 = new BitSet(new long[]{0x0040000100000002L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorDef1567 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1571 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_BASE_in_operatorDef1573 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1577 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_specializer_in_operatorDef1579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_operatorRule1623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_GATE_in_operatorRule1625 = new BitSet(new long[]{0x004A090400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_operatorRule1627 = new BitSet(new long[]{0x004A090400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_GROUP_in_predicate1659 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ALL_in_predicate1662 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_predicate1664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_predicate1688 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_predicate1690 = new BitSet(new long[]{0x0000000000000000L,0x000000000FCA0000L});
    public static final BitSet FOLLOW_booleanOp_in_predicate1692 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_predicate1694 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_predicate1697 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_predicate1699 = new BitSet(new long[]{0x0000000000000000L,0x000000000FCA0000L});
    public static final BitSet FOLLOW_booleanOp_in_predicate1701 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_predicate1703 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_predicate1707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_target_in_rule1745 = new BitSet(new long[]{0xC000000000000000L});
    public static final BitSet FOLLOW_DEFINE_in_rule1749 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_DYNAMIC_in_rule1753 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_callGroup_in_rule1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callChain_in_callGroup1793 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_callGroup1796 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_callChain_in_callGroup1798 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000060L});
    public static final BitSet FOLLOW_JOIN_in_callGroup1802 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_callChain_in_callGroup1804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callChain_in_callGroup1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callTarget_in_callChain1842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_callTarget1860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptySet_in_callTarget1874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueList_in_callTarget1886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCallTarget_in_callTarget1900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_functionCallTarget1921 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000009L});
    public static final BitSet FOLLOW_passOp_in_functionCallTarget1923 = new BitSet(new long[]{0x2140001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_callTarget_in_functionCallTarget1927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_functionCallTarget1953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callName_in_functionCall1984 = new BitSet(new long[]{0x0140000000000000L});
    public static final BitSet FOLLOW_specializer_in_functionCall1987 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_valueList_in_functionCall1990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_callName2024 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_NAMESPACE_in_callName2026 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_callName2030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_callName2078 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GLYPH_in_target2129 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_target2139 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CANVAS_in_target2149 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_in_target2159 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VIEW_in_target2169 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_target2179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PYTHON_in_pythonDef2240 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_ARG_in_pythonDef2242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2246 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_pythonDef2248 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2252 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_pythonBlock_in_pythonDef2254 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_PYTHON_in_pythonDef2277 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2281 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_pythonBlock_in_pythonDef2283 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_FACET_in_pythonBlock2312 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_pythonBlock2314 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_CODE_BLOCK_in_pythonBlock2316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_pythonBlock2353 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_FACET_in_pythonBlock2355 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonBlock2359 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_pythonBlock2361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_YIELDS_in_pythonBlock2364 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_pythonBlock2366 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_CODE_BLOCK_in_pythonBlock2369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations2402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAGGED_ID_in_annotation2445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2464 = new BitSet(new long[]{0x2000000000000000L,0x0000000030101000L});
    public static final BitSet FOLLOW_range_in_specializer2466 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2468 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2503 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2505 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2508 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2542 = new BitSet(new long[]{0x2000000000000000L,0x0000000030101000L});
    public static final BitSet FOLLOW_range_in_specializer2544 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_specializer2546 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2548 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2551 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2578 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2580 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_specializer2583 = new BitSet(new long[]{0x2000000000000000L,0x0000000030101000L});
    public static final BitSet FOLLOW_range_in_specializer2585 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2587 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2613 = new BitSet(new long[]{0x2200001100000000L,0x0000000030001D00L});
    public static final BitSet FOLLOW_argList_in_specializer2615 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_sepArgList2674 = new BitSet(new long[]{0x2000001100000000L,0x0000000030001D00L});
    public static final BitSet FOLLOW_argList_in_sepArgList2677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_values_in_argList2722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mapList_in_argList2737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_values_in_argList2752 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_argList2754 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_mapList_in_argList2757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_values2767 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_values2770 = new BitSet(new long[]{0x2000001100000000L,0x0000000030001C00L});
    public static final BitSet FOLLOW_atom_in_values2772 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_mapEntry_in_mapList2794 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_mapList2797 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_mapEntry_in_mapList2799 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_ID_in_mapEntry2826 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_mapEntry2828 = new BitSet(new long[]{0x2000001100000000L,0x0000000030001C00L});
    public static final BitSet FOLLOW_atom_in_mapEntry2832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptySet_in_tuple2853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_tuple2871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_tuple2889 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_tuple2891 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_tuple2894 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_tuple2896 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_tuple2900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_emptySet2921 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_emptySet2924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_valueList2933 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_valueList2936 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_valueList2939 = new BitSet(new long[]{0x2100001100000000L,0x0000000030201D00L});
    public static final BitSet FOLLOW_value_in_valueList2942 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_valueList2946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_range2959 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range2961 = new BitSet(new long[]{0x2000000000000000L,0x0000000030001000L});
    public static final BitSet FOLLOW_number_in_range2963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_range2983 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range2985 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_range2987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_range3008 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range3010 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_range3012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_split3040 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_split3087 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_split3089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleRef_in_value3140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_value3145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sigil_in_atom3153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_atom3157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_atom3161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_atom3165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_atom3169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_tupleRef3181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_tupleRef3195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_tupleRef3211 = new BitSet(new long[]{0x2000000000000000L,0x0000000030001000L});
    public static final BitSet FOLLOW_number_in_tupleRef3213 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_tupleRef3215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_qualifiedID3231 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_ARG_in_qualifiedID3234 = new BitSet(new long[]{0x2000000000000000L,0x0000000030001000L});
    public static final BitSet FOLLOW_number_in_qualifiedID3237 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_qualifiedID3239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAGGED_ID_in_sigil3249 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_sValueList_in_sigil3251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_sValueList3269 = new BitSet(new long[]{0x2100000000000000L,0x0000000030201900L});
    public static final BitSet FOLLOW_sValue_in_sValueList3272 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_sValueList3275 = new BitSet(new long[]{0x2100000000000000L,0x0000000030201900L});
    public static final BitSet FOLLOW_sValue_in_sValueList3278 = new BitSet(new long[]{0x0480000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_sValueList3282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleRef_in_sValue3292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_sValue3296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_sValue3300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_booleanOp3314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_booleanOp3329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_booleanOp3343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_booleanOp3358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_booleanOp3372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_booleanOp3387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_booleanOp3401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_booleanOp3415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_passOp0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doubleNum_in_number3457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_intNum_in_number3461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_intNum3474 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_93_in_intNum3480 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_intNum3485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIGITS_in_intNum3500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAMESPLIT_in_doubleNum3517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3536 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_NAMESPLIT_in_doubleNum3538 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_doubleNum3558 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_93_in_doubleNum3564 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3569 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_NAMESPLIT_in_doubleNum3571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callChain_in_synpred1_Stencil1787 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_synpred1_Stencil1789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_synpred2_Stencil1913 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000009L});
    public static final BitSet FOLLOW_passOp_in_synpred2_Stencil1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PYTHON_in_synpred3_Stencil2233 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_ARG_in_synpred3_Stencil2235 = new BitSet(new long[]{0x0000000000000002L});

}