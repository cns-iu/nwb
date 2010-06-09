// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g 2009-11-18 16:32:33

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANNOTATION", "BOOLEAN_OP", "BASIC", "CONSUMES", "CALL_CHAIN", "DIRECT_YIELD", "FUNCTION", "LIST", "CANVAS_DEF", "NUMBER", "OPERATOR_INSTANCE", "OPERATOR_PROXY", "OPERATOR_REFERENCE", "OPERATOR_TEMPLATE", "OPERATOR_RULE", "OPERATOR_BASE", "POST", "PRE", "PREDICATE", "PROGRAM", "PACK", "PYTHON_FACET", "RULE", "SIGIL", "SPECIALIZER", "TUPLE_PROTOTYPE", "TUPLE_REF", "MAP_ENTRY", "ALL", "BASE", "CANVAS", "DEFAULT", "EXTERNAL", "FILTER", "FROM", "GLYPH", "GUIDE", "IMPORT", "LOCAL", "LAYER", "OPERATOR", "TEMPLATE", "ORDER", "PYTHON", "RETURN", "STREAM", "VIEW", "AS", "FACET", "GROUP", "CLOSE_GROUP", "ARG", "CLOSE_ARG", "SEPARATOR", "RANGE", "NAMESPACE", "DEFINE", "DYNAMIC", "ANIMATED", "ANIMATED_DYNAMIC", "YIELDS", "FEED", "GUIDE_FEED", "GUIDE_YIELD", "GATE", "SPLIT", "JOIN", "TAG", "ID", "CODE_BLOCK", "TAGGED_ID", "STRING", "DIGITS", "NESTED_BLOCK", "ESCAPE_SEQUENCE", "WS", "COMMENT", "'>'", "'Init'", "'='", "'_'", "'>='", "'<'", "'<='", "'!='", "'=~'", "'!~'", "'-['", "']>'", "'-'", "'+'", "'.'"
    };
    public static final int PRE=21;
    public static final int DIRECT_YIELD=9;
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
    public static final int FUNCTION=10;
    public static final int LOCAL=42;
    public static final int ANIMATED_DYNAMIC=63;
    public static final int YIELDS=64;
    public static final int TAG=71;
    public static final int CANVAS=34;
    public static final int RANGE=58;
    public static final int SIGIL=27;
    public static final int T__89=89;
    public static final int FILTER=37;
    public static final int SPLIT=69;
    public static final int WS=79;
    public static final int STRING=75;
    public static final int T__92=92;
    public static final int ANIMATED=62;
    public static final int T__88=88;
    public static final int OPERATOR_REFERENCE=16;
    public static final int T__90=90;
    public static final int CANVAS_DEF=12;
    public static final int OPERATOR_TEMPLATE=17;
    public static final int T__91=91;
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
    public static final int T__82=82;
    public static final int DEFAULT=35;
    public static final int T__81=81;
    public static final int T__83=83;

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
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g"; }


      List<String> errors = new ArrayList<String>();
      boolean poolErrors = false;
      
      /**Buried IDs are strings that cannot be input as identifiers according to the
       * Stencil grammar, but are used internally as IDs.
       */
      public static String buryID(String input) {return "#" + input;}
      
      /**Should error messages be collected up and reported all at once?
       * If set to true, a list of errors will be returned after trying to parse.
       * If set to false, the first error ends the parsing.
       */
      public void poolErrors(boolean pool) {this.poolErrors = pool;}
      public void emitErrorMessage(String msg) {
      	if (poolErrors) {errors.add(msg);}
      	else {super.emitErrorMessage(msg);}
      }
      
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:1: program : ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )* -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) ) ;
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


        RewriteRuleSubtreeStream stream_imports=new RewriteRuleSubtreeStream(adaptor,"rule imports");
        RewriteRuleSubtreeStream stream_externals=new RewriteRuleSubtreeStream(adaptor,"rule externals");
        RewriteRuleSubtreeStream stream_operatorDef=new RewriteRuleSubtreeStream(adaptor,"rule operatorDef");
        RewriteRuleSubtreeStream stream_streamDef=new RewriteRuleSubtreeStream(adaptor,"rule streamDef");
        RewriteRuleSubtreeStream stream_order=new RewriteRuleSubtreeStream(adaptor,"rule order");
        RewriteRuleSubtreeStream stream_canvasLayer=new RewriteRuleSubtreeStream(adaptor,"rule canvasLayer");
        RewriteRuleSubtreeStream stream_layerDef=new RewriteRuleSubtreeStream(adaptor,"rule layerDef");
        RewriteRuleSubtreeStream stream_pythonDef=new RewriteRuleSubtreeStream(adaptor,"rule pythonDef");
        RewriteRuleSubtreeStream stream_operatorTemplate=new RewriteRuleSubtreeStream(adaptor,"rule operatorTemplate");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:9: ( ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )* -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:11: ( imports )* externals order canvasLayer ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )*
            {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:11: ( imports )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==IMPORT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:11: imports
            	    {
            	    pushFollow(FOLLOW_imports_in_program637);
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

            pushFollow(FOLLOW_externals_in_program640);
            externals2=externals();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_externals.add(externals2.getTree());
            pushFollow(FOLLOW_order_in_program642);
            order3=order();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_order.add(order3.getTree());
            pushFollow(FOLLOW_canvasLayer_in_program644);
            canvasLayer4=canvasLayer();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_canvasLayer.add(canvasLayer4.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:48: ( streamDef | layerDef | operatorDef | pythonDef | operatorTemplate )*
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
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:49: streamDef
            	    {
            	    pushFollow(FOLLOW_streamDef_in_program647);
            	    streamDef5=streamDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_streamDef.add(streamDef5.getTree());

            	    }
            	    break;
            	case 2 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:61: layerDef
            	    {
            	    pushFollow(FOLLOW_layerDef_in_program651);
            	    layerDef6=layerDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_layerDef.add(layerDef6.getTree());

            	    }
            	    break;
            	case 3 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:72: operatorDef
            	    {
            	    pushFollow(FOLLOW_operatorDef_in_program655);
            	    operatorDef7=operatorDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_operatorDef.add(operatorDef7.getTree());

            	    }
            	    break;
            	case 4 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:86: pythonDef
            	    {
            	    pushFollow(FOLLOW_pythonDef_in_program659);
            	    pythonDef8=pythonDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_pythonDef.add(pythonDef8.getTree());

            	    }
            	    break;
            	case 5 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:167:98: operatorTemplate
            	    {
            	    pushFollow(FOLLOW_operatorTemplate_in_program663);
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
            // elements: pythonDef, imports, operatorTemplate, operatorDef, externals, layerDef, canvasLayer, order
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 168:5: -> ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:168:8: ^( PROGRAM ^( LIST[\"Imports\"] ( imports )* ) order externals canvasLayer ^( LIST[\"Layers\"] ( layerDef )* ) ^( LIST[\"Operators\"] ( operatorDef )* ) ^( LIST[\"Pythons\"] ( pythonDef )* ) ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PROGRAM, "PROGRAM"), root_1);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:169:11: ^( LIST[\"Imports\"] ( imports )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Imports"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:169:29: ( imports )*
                while ( stream_imports.hasNext() ) {
                    adaptor.addChild(root_2, stream_imports.nextTree());

                }
                stream_imports.reset();

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_order.nextTree());
                adaptor.addChild(root_1, stream_externals.nextTree());
                adaptor.addChild(root_1, stream_canvasLayer.nextTree());
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:173:11: ^( LIST[\"Layers\"] ( layerDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Layers"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:173:28: ( layerDef )*
                while ( stream_layerDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_layerDef.nextTree());

                }
                stream_layerDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:174:11: ^( LIST[\"Operators\"] ( operatorDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Operators"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:174:31: ( operatorDef )*
                while ( stream_operatorDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_operatorDef.nextTree());

                }
                stream_operatorDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:175:11: ^( LIST[\"Pythons\"] ( pythonDef )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Pythons"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:175:29: ( pythonDef )*
                while ( stream_pythonDef.hasNext() ) {
                    adaptor.addChild(root_2, stream_pythonDef.nextTree());

                }
                stream_pythonDef.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:176:11: ^( LIST[\"OperatorTemplates\"] ( operatorTemplate )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "OperatorTemplates"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:176:39: ( operatorTemplate )*
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:181:1: imports : IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )? -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] ) -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args) -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] ) -> ^( IMPORT[$name.text] $as $args) ;
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
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_argList=new RewriteRuleSubtreeStream(adaptor,"rule argList");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:3: ( IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )? -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] ) -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args) -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] ) -> ^( IMPORT[$name.text] $as $args) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:5: IMPORT name= ID ( ARG args= argList CLOSE_ARG )? ( AS as= ID )?
            {
            IMPORT10=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_imports822); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IMPORT.add(IMPORT10);

            name=(Token)match(input,ID,FOLLOW_ID_in_imports826); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:20: ( ARG args= argList CLOSE_ARG )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==ARG) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:21: ARG args= argList CLOSE_ARG
                    {
                    ARG11=(Token)match(input,ARG,FOLLOW_ARG_in_imports829); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG11);

                    pushFollow(FOLLOW_argList_in_imports833);
                    args=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_argList.add(args.getTree());
                    CLOSE_ARG12=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_imports835); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG12);


                    }
                    break;

            }

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:50: ( AS as= ID )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==AS) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:182:51: AS as= ID
                    {
                    AS13=(Token)match(input,AS,FOLLOW_AS_in_imports840); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AS.add(AS13);

                    as=(Token)match(input,ID,FOLLOW_ID_in_imports844); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(as);


                    }
                    break;

            }



            // AST REWRITE
            // elements: ID, IMPORT, IMPORT, args, ID, as, as, IMPORT, IMPORT, args
            // token labels: as
            // rule labels: args, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_as=new RewriteRuleTokenStream(adaptor,"token as",as);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"rule args",args!=null?args.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 183:7: -> {as==null && args==null}? ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] )
            if (as==null && args==null) {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:183:36: ^( IMPORT[$name.text] ID[\"\"] LIST[\"Arguments\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, (Object)adaptor.create(ID, ""));
                adaptor.addChild(root_1, (Object)adaptor.create(LIST, "Arguments"));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 184:7: -> {as==null && args!=null}? ^( IMPORT[$name.text] ID[\"\"] $args)
            if (as==null && args!=null) {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:184:36: ^( IMPORT[$name.text] ID[\"\"] $args)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, (Object)adaptor.create(ID, ""));
                adaptor.addChild(root_1, stream_args.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 185:7: -> {as!=null && args==null}? ^( IMPORT[$name.text] $as LIST[\"Arguments\"] )
            if (as!=null && args==null) {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:185:36: ^( IMPORT[$name.text] $as LIST[\"Arguments\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IMPORT, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_as.nextNode());
                adaptor.addChild(root_1, (Object)adaptor.create(LIST, "Arguments"));

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 186:7: -> ^( IMPORT[$name.text] $as $args)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:186:10: ^( IMPORT[$name.text] $as $args)
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:188:1: order : ( ORDER orderRef ( '>' orderRef )* -> ^( ORDER ( orderRef )+ ) | -> ^( ORDER ) );
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
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:189:3: ( ORDER orderRef ( '>' orderRef )* -> ^( ORDER ( orderRef )+ ) | -> ^( ORDER ) )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:189:5: ORDER orderRef ( '>' orderRef )*
                    {
                    ORDER14=(Token)match(input,ORDER,FOLLOW_ORDER_in_order940); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ORDER.add(ORDER14);

                    pushFollow(FOLLOW_orderRef_in_order942);
                    orderRef15=orderRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_orderRef.add(orderRef15.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:189:20: ( '>' orderRef )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==81) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:189:21: '>' orderRef
                    	    {
                    	    char_literal16=(Token)match(input,81,FOLLOW_81_in_order945); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_81.add(char_literal16);

                    	    pushFollow(FOLLOW_orderRef_in_order947);
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
                    // elements: ORDER, orderRef
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 190:5: -> ^( ORDER ( orderRef )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:190:8: ^( ORDER ( orderRef )+ )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:191:5: 
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
                    // 191:5: -> ^( ORDER )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:191:8: ^( ORDER )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:193:1: orderRef : ( ID -> ^( LIST[\"Streams\"] ID ) | GROUP ID ( SPLIT ID )+ CLOSE_GROUP -> ^( LIST[\"Streams\"] ( ID )+ ) );
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
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_SPLIT=new RewriteRuleTokenStream(adaptor,"token SPLIT");
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:194:3: ( ID -> ^( LIST[\"Streams\"] ID ) | GROUP ID ( SPLIT ID )+ CLOSE_GROUP -> ^( LIST[\"Streams\"] ( ID )+ ) )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:194:5: ID
                    {
                    ID18=(Token)match(input,ID,FOLLOW_ID_in_orderRef982); if (state.failed) return retval; 
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
                    // 194:8: -> ^( LIST[\"Streams\"] ID )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:194:11: ^( LIST[\"Streams\"] ID )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:5: GROUP ID ( SPLIT ID )+ CLOSE_GROUP
                    {
                    GROUP19=(Token)match(input,GROUP,FOLLOW_GROUP_in_orderRef997); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP19);

                    ID20=(Token)match(input,ID,FOLLOW_ID_in_orderRef999); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID20);

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:14: ( SPLIT ID )+
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
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:15: SPLIT ID
                    	    {
                    	    SPLIT21=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_orderRef1002); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT21);

                    	    ID22=(Token)match(input,ID,FOLLOW_ID_in_orderRef1004); if (state.failed) return retval; 
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

                    CLOSE_GROUP23=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_orderRef1008); if (state.failed) return retval; 
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
                    // 195:38: -> ^( LIST[\"Streams\"] ( ID )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:195:42: ^( LIST[\"Streams\"] ( ID )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:1: externals : ( externalStream )* -> ^( LIST[\"Externals\"] ( externalStream )* ) ;
    public final StencilParser.externals_return externals() throws RecognitionException {
        StencilParser.externals_return retval = new StencilParser.externals_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.externalStream_return externalStream24 = null;


        RewriteRuleSubtreeStream stream_externalStream=new RewriteRuleSubtreeStream(adaptor,"rule externalStream");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:10: ( ( externalStream )* -> ^( LIST[\"Externals\"] ( externalStream )* ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:12: ( externalStream )*
            {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:12: ( externalStream )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==EXTERNAL) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:12: externalStream
            	    {
            	    pushFollow(FOLLOW_externalStream_in_externals1026);
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
            // 197:28: -> ^( LIST[\"Externals\"] ( externalStream )* )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:31: ^( LIST[\"Externals\"] ( externalStream )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Externals"), root_1);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:197:51: ( externalStream )*
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:1: externalStream : EXTERNAL STREAM name= ID tuple[false] -> ^( EXTERNAL[$name.text] tuple ) ;
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
        RewriteRuleTokenStream stream_STREAM=new RewriteRuleTokenStream(adaptor,"token STREAM");
        RewriteRuleTokenStream stream_EXTERNAL=new RewriteRuleTokenStream(adaptor,"token EXTERNAL");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:15: ( EXTERNAL STREAM name= ID tuple[false] -> ^( EXTERNAL[$name.text] tuple ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:17: EXTERNAL STREAM name= ID tuple[false]
            {
            EXTERNAL25=(Token)match(input,EXTERNAL,FOLLOW_EXTERNAL_in_externalStream1043); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EXTERNAL.add(EXTERNAL25);

            STREAM26=(Token)match(input,STREAM,FOLLOW_STREAM_in_externalStream1045); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM26);

            name=(Token)match(input,ID,FOLLOW_ID_in_externalStream1049); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_tuple_in_externalStream1051);
            tuple27=tuple(false);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_tuple.add(tuple27.getTree());


            // AST REWRITE
            // elements: EXTERNAL, tuple
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 198:54: -> ^( EXTERNAL[$name.text] tuple )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:198:57: ^( EXTERNAL[$name.text] tuple )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:203:1: canvasLayer : ( CANVAS name= ID canvasProperties ( guideDef )+ -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) ) | -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) ) );
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
        RewriteRuleTokenStream stream_CANVAS=new RewriteRuleTokenStream(adaptor,"token CANVAS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_canvasProperties=new RewriteRuleSubtreeStream(adaptor,"rule canvasProperties");
        RewriteRuleSubtreeStream stream_guideDef=new RewriteRuleSubtreeStream(adaptor,"rule guideDef");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:204:3: ( CANVAS name= ID canvasProperties ( guideDef )+ -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) ) | -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) ) )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:204:5: CANVAS name= ID canvasProperties ( guideDef )+
                    {
                    CANVAS28=(Token)match(input,CANVAS,FOLLOW_CANVAS_in_canvasLayer1074); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CANVAS.add(CANVAS28);

                    name=(Token)match(input,ID,FOLLOW_ID_in_canvasLayer1078); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_canvasProperties_in_canvasLayer1080);
                    canvasProperties29=canvasProperties();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_canvasProperties.add(canvasProperties29.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:204:37: ( guideDef )+
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
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:204:37: guideDef
                    	    {
                    	    pushFollow(FOLLOW_guideDef_in_canvasLayer1082);
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
                    // elements: guideDef, canvasProperties
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 205:5: -> ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:8: ^( CANVAS_DEF[$name.text] canvasProperties ^( LIST[\"Guides\"] ( guideDef )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CANVAS_DEF, (name!=null?name.getText():null)), root_1);

                        adaptor.addChild(root_1, stream_canvasProperties.nextTree());
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:205:50: ^( LIST[\"Guides\"] ( guideDef )+ )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:5: 
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
                    // 206:5: -> ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:8: ^( CANVAS_DEF[\"default\"] ^( SPECIALIZER DEFAULT ) ^( LIST[\"Guides\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CANVAS_DEF, "default"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:32: ^( SPECIALIZER DEFAULT )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(DEFAULT, "DEFAULT"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:206:55: ^( LIST[\"Guides\"] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:1: guideDef : GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )* -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) ) ;
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
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:9: ( GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )* -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:11: GUIDE type= ID spec= specializer[RuleOpts.Simple] FROM layer= ID attribute= ID ( rule[\"glyph\"] )*
            {
            GUIDE31=(Token)match(input,GUIDE,FOLLOW_GUIDE_in_guideDef1134); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GUIDE.add(GUIDE31);

            type=(Token)match(input,ID,FOLLOW_ID_in_guideDef1138); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(type);

            pushFollow(FOLLOW_specializer_in_guideDef1142);
            spec=specializer(RuleOpts.Simple);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_specializer.add(spec.getTree());
            FROM32=(Token)match(input,FROM,FOLLOW_FROM_in_guideDef1145); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FROM.add(FROM32);

            layer=(Token)match(input,ID,FOLLOW_ID_in_guideDef1149); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(layer);

            attribute=(Token)match(input,ID,FOLLOW_ID_in_guideDef1153); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(attribute);

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:86: ( rule[\"glyph\"] )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==CANVAS||LA12_0==GLYPH||LA12_0==LOCAL||LA12_0==RETURN||LA12_0==VIEW||LA12_0==GROUP||LA12_0==ID) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:208:86: rule[\"glyph\"]
            	    {
            	    pushFollow(FOLLOW_rule_in_guideDef1155);
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
            // elements: spec, GUIDE, rule, layer, type
            // token labels: type, layer
            // rule labels: spec, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_type=new RewriteRuleTokenStream(adaptor,"token type",type);
            RewriteRuleTokenStream stream_layer=new RewriteRuleTokenStream(adaptor,"token layer",layer);
            RewriteRuleSubtreeStream stream_spec=new RewriteRuleSubtreeStream(adaptor,"rule spec",spec!=null?spec.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 209:4: -> ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:7: ^( GUIDE[$attribute.text] $layer $type $spec ^( LIST[\"Rules\"] ( rule )* ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(GUIDE, (attribute!=null?attribute.getText():null)), root_1);

                adaptor.addChild(root_1, stream_layer.nextNode());
                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_spec.nextTree());
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:51: ^( LIST[\"Rules\"] ( rule )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Rules"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:209:67: ( rule )*
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:211:1: canvasProperties : specializer[RuleOpts.Simple] ;
    public final StencilParser.canvasProperties_return canvasProperties() throws RecognitionException {
        StencilParser.canvasProperties_return retval = new StencilParser.canvasProperties_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.specializer_return specializer34 = null;



        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:211:17: ( specializer[RuleOpts.Simple] )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:211:19: specializer[RuleOpts.Simple]
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_specializer_in_canvasProperties1192);
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:215:1: streamDef : STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+ -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )* ;
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
        RewriteRuleSubtreeStream stream_consumesBlock=new RewriteRuleSubtreeStream(adaptor,"rule consumesBlock");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:216:3: ( STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+ -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )* )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:216:5: STREAM name= ID tuple[true] ( consumesBlock[\"return\"] )+
            {
            STREAM35=(Token)match(input,STREAM,FOLLOW_STREAM_in_streamDef1206); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM35);

            name=(Token)match(input,ID,FOLLOW_ID_in_streamDef1210); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_tuple_in_streamDef1212);
            tuple36=tuple(true);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_tuple.add(tuple36.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:216:33: ( consumesBlock[\"return\"] )+
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
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:216:34: consumesBlock[\"return\"]
            	    {
            	    pushFollow(FOLLOW_consumesBlock_in_streamDef1217);
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
            // 217:5: -> ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )*
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:8: ( ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )*
                while ( stream_tuple.hasNext()||stream_STREAM.hasNext() ) {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:8: ^( STREAM[$name.text] tuple ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STREAM, (name!=null?name.getText():null)), root_1);

                    adaptor.addChild(root_1, stream_tuple.nextTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:217:35: ^( LIST[\"Consumes\"] ( consumesBlock )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:219:1: layerDef : LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+ -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) ;
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
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:220:3: ( LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+ -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:220:5: LAYER name= ID implantationDef ( consumesBlock[\"glyph\"] )+
            {
            LAYER38=(Token)match(input,LAYER,FOLLOW_LAYER_in_layerDef1252); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LAYER.add(LAYER38);

            name=(Token)match(input,ID,FOLLOW_ID_in_layerDef1256); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            pushFollow(FOLLOW_implantationDef_in_layerDef1258);
            implantationDef39=implantationDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_implantationDef.add(implantationDef39.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:220:35: ( consumesBlock[\"glyph\"] )+
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
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:220:35: consumesBlock[\"glyph\"]
            	    {
            	    pushFollow(FOLLOW_consumesBlock_in_layerDef1260);
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
            // elements: LAYER, consumesBlock, implantationDef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 221:5: -> ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:221:8: ^( LAYER[$name.text] implantationDef ^( LIST[\"Consumes\"] ( consumesBlock )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LAYER, (name!=null?name.getText():null)), root_1);

                adaptor.addChild(root_1, stream_implantationDef.nextTree());
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:221:44: ^( LIST[\"Consumes\"] ( consumesBlock )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:223:1: implantationDef : ( ARG type= ID CLOSE_ARG -> GLYPH[$type.text] | -> GLYPH[DEFAULT_GLYPH_TYPE] );
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
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:224:3: ( ARG type= ID CLOSE_ARG -> GLYPH[$type.text] | -> GLYPH[DEFAULT_GLYPH_TYPE] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:224:5: ARG type= ID CLOSE_ARG
                    {
                    ARG41=(Token)match(input,ARG,FOLLOW_ARG_in_implantationDef1295); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG41);

                    type=(Token)match(input,ID,FOLLOW_ID_in_implantationDef1299); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(type);

                    CLOSE_ARG42=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_implantationDef1301); if (state.failed) return retval; 
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
                    // 224:27: -> GLYPH[$type.text]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(GLYPH, (type!=null?type.getText():null)));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:225:5: 
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
                    // 225:5: -> GLYPH[DEFAULT_GLYPH_TYPE]
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:227:1: consumesBlock[String def] : FROM stream= ID ( filterRule )* ( rule[def] )+ -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) ) ;
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
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:3: ( FROM stream= ID ( filterRule )* ( rule[def] )+ -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:5: FROM stream= ID ( filterRule )* ( rule[def] )+
            {
            FROM43=(Token)match(input,FROM,FOLLOW_FROM_in_consumesBlock1330); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FROM.add(FROM43);

            stream=(Token)match(input,ID,FOLLOW_ID_in_consumesBlock1334); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(stream);

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:20: ( filterRule )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==FILTER) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:20: filterRule
            	    {
            	    pushFollow(FOLLOW_filterRule_in_consumesBlock1336);
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

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:32: ( rule[def] )+
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
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:228:32: rule[def]
            	    {
            	    pushFollow(FOLLOW_rule_in_consumesBlock1339);
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
            // 229:5: -> ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:229:8: ^( CONSUMES[$stream.text] ^( LIST[\"Filters\"] ( filterRule )* ) ^( LIST[\"Rules\"] ( rule )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CONSUMES, (stream!=null?stream.getText():null)), root_1);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:229:33: ^( LIST[\"Filters\"] ( filterRule )* )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Filters"), root_2);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:229:51: ( filterRule )*
                while ( stream_filterRule.hasNext() ) {
                    adaptor.addChild(root_2, stream_filterRule.nextTree());

                }
                stream_filterRule.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:229:64: ^( LIST[\"Rules\"] ( rule )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:231:1: filterRule : FILTER rulePredicate DEFINE callChain -> ^( FILTER rulePredicate callChain ) ;
    public final StencilParser.filterRule_return filterRule() throws RecognitionException {
        StencilParser.filterRule_return retval = new StencilParser.filterRule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FILTER46=null;
        Token DEFINE48=null;
        StencilParser.rulePredicate_return rulePredicate47 = null;

        StencilParser.callChain_return callChain49 = null;


        Object FILTER46_tree=null;
        Object DEFINE48_tree=null;
        RewriteRuleTokenStream stream_DEFINE=new RewriteRuleTokenStream(adaptor,"token DEFINE");
        RewriteRuleTokenStream stream_FILTER=new RewriteRuleTokenStream(adaptor,"token FILTER");
        RewriteRuleSubtreeStream stream_rulePredicate=new RewriteRuleSubtreeStream(adaptor,"rule rulePredicate");
        RewriteRuleSubtreeStream stream_callChain=new RewriteRuleSubtreeStream(adaptor,"rule callChain");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:232:3: ( FILTER rulePredicate DEFINE callChain -> ^( FILTER rulePredicate callChain ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:232:5: FILTER rulePredicate DEFINE callChain
            {
            FILTER46=(Token)match(input,FILTER,FOLLOW_FILTER_in_filterRule1379); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_FILTER.add(FILTER46);

            pushFollow(FOLLOW_rulePredicate_in_filterRule1381);
            rulePredicate47=rulePredicate();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_rulePredicate.add(rulePredicate47.getTree());
            DEFINE48=(Token)match(input,DEFINE,FOLLOW_DEFINE_in_filterRule1383); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DEFINE.add(DEFINE48);

            pushFollow(FOLLOW_callChain_in_filterRule1385);
            callChain49=callChain();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callChain.add(callChain49.getTree());


            // AST REWRITE
            // elements: FILTER, callChain, rulePredicate
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 233:5: -> ^( FILTER rulePredicate callChain )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:233:8: ^( FILTER rulePredicate callChain )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_FILTER.nextNode(), root_1);

                adaptor.addChild(root_1, stream_rulePredicate.nextTree());
                adaptor.addChild(root_1, stream_callChain.nextTree());

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:235:1: rulePredicate : ( GROUP ALL CLOSE_GROUP -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) );
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
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_booleanOp=new RewriteRuleSubtreeStream(adaptor,"rule booleanOp");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:236:3: ( GROUP ALL CLOSE_GROUP -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==GROUP) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==ALL) ) {
                    int LA19_2 = input.LA(3);

                    if ( (LA19_2==CLOSE_GROUP) ) {
                        alt19=1;
                    }
                    else if ( (LA19_2==81||LA19_2==83||(LA19_2>=85 && LA19_2<=90)) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 2, input);

                        throw nvae;
                    }
                }
                else if ( ((LA19_1>=CANVAS && LA19_1<=DEFAULT)||LA19_1==LOCAL||LA19_1==VIEW||LA19_1==ARG||LA19_1==ID||(LA19_1>=TAGGED_ID && LA19_1<=DIGITS)||LA19_1==84||(LA19_1>=93 && LA19_1<=95)) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:236:5: GROUP ALL CLOSE_GROUP
                    {
                    GROUP50=(Token)match(input,GROUP,FOLLOW_GROUP_in_rulePredicate1409); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP50);

                    ALL51=(Token)match(input,ALL,FOLLOW_ALL_in_rulePredicate1411); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALL.add(ALL51);

                    CLOSE_GROUP52=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_rulePredicate1413); if (state.failed) return retval; 
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
                    // 237:5: -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:8: ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:237:29: ^( PREDICATE ALL )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:5: GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP
                    {
                    GROUP53=(Token)match(input,GROUP,FOLLOW_GROUP_in_rulePredicate1436); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP53);

                    pushFollow(FOLLOW_value_in_rulePredicate1438);
                    value54=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value54.getTree());
                    pushFollow(FOLLOW_booleanOp_in_rulePredicate1440);
                    booleanOp55=booleanOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp55.getTree());
                    pushFollow(FOLLOW_value_in_rulePredicate1442);
                    value56=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value56.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:33: ( SEPARATOR value booleanOp value )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==SEPARATOR) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:238:34: SEPARATOR value booleanOp value
                    	    {
                    	    SEPARATOR57=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_rulePredicate1445); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR57);

                    	    pushFollow(FOLLOW_value_in_rulePredicate1447);
                    	    value58=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value58.getTree());
                    	    pushFollow(FOLLOW_booleanOp_in_rulePredicate1449);
                    	    booleanOp59=booleanOp();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp59.getTree());
                    	    pushFollow(FOLLOW_value_in_rulePredicate1451);
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

                    CLOSE_GROUP61=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_rulePredicate1455); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP61);



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
                    // 239:5: -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:239:8: ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        if ( !(stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext() ) {
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:239:29: ^( PREDICATE value booleanOp value )
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
    // $ANTLR end "rulePredicate"

    public static class operatorTemplate_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "operatorTemplate"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:245:1: operatorTemplate : TEMPLATE OPERATOR name= ID -> ^( OPERATOR_TEMPLATE[$name.text] ) ;
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
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_TEMPLATE=new RewriteRuleTokenStream(adaptor,"token TEMPLATE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:245:18: ( TEMPLATE OPERATOR name= ID -> ^( OPERATOR_TEMPLATE[$name.text] ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:245:20: TEMPLATE OPERATOR name= ID
            {
            TEMPLATE62=(Token)match(input,TEMPLATE,FOLLOW_TEMPLATE_in_operatorTemplate1489); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TEMPLATE.add(TEMPLATE62);

            OPERATOR63=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorTemplate1491); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR63);

            name=(Token)match(input,ID,FOLLOW_ID_in_operatorTemplate1495); if (state.failed) return retval; 
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
            // 245:46: -> ^( OPERATOR_TEMPLATE[$name.text] )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:245:49: ^( OPERATOR_TEMPLATE[$name.text] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:247:1: operatorDef : ( OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+ -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) ) | OPERATOR name= ID BASE base= ID specializer[RuleOpts.All] -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer ) );
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
        RewriteRuleTokenStream stream_OPERATOR=new RewriteRuleTokenStream(adaptor,"token OPERATOR");
        RewriteRuleTokenStream stream_BASE=new RewriteRuleTokenStream(adaptor,"token BASE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_operatorRule=new RewriteRuleSubtreeStream(adaptor,"rule operatorRule");
        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:248:3: ( OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+ -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) ) | OPERATOR name= ID BASE base= ID specializer[RuleOpts.All] -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer ) )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:248:5: OPERATOR name= ID tuple[false] YIELDS tuple[false] ( operatorRule )+
                    {
                    OPERATOR64=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorDef1514); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR64);

                    name=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1519); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_tuple_in_operatorDef1521);
                    tuple65=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple65.getTree());
                    YIELDS66=(Token)match(input,YIELDS,FOLLOW_YIELDS_in_operatorDef1524); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_YIELDS.add(YIELDS66);

                    pushFollow(FOLLOW_tuple_in_operatorDef1526);
                    tuple67=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple67.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:248:56: ( operatorRule )+
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
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:248:56: operatorRule
                    	    {
                    	    pushFollow(FOLLOW_operatorRule_in_operatorDef1529);
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
                    // elements: YIELDS, OPERATOR, operatorRule, tuple, tuple
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 249:5: -> ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:249:9: ^( OPERATOR[$name.text] ^( YIELDS tuple tuple ) ^( LIST[\"Rules\"] ( operatorRule )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, (name!=null?name.getText():null)), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:249:32: ^( YIELDS tuple tuple )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(stream_YIELDS.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_tuple.nextTree());
                        adaptor.addChild(root_2, stream_tuple.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:249:54: ^( LIST[\"Rules\"] ( operatorRule )+ )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:250:5: OPERATOR name= ID BASE base= ID specializer[RuleOpts.All]
                    {
                    OPERATOR69=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_operatorDef1564); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OPERATOR.add(OPERATOR69);

                    name=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1568); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    BASE70=(Token)match(input,BASE,FOLLOW_BASE_in_operatorDef1570); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BASE.add(BASE70);

                    base=(Token)match(input,ID,FOLLOW_ID_in_operatorDef1574); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(base);

                    pushFollow(FOLLOW_specializer_in_operatorDef1576);
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
                    // 251:5: -> ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:251:8: ^( OPERATOR_REFERENCE[$name.text] OPERATOR_BASE[$base.text] specializer )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:265:1: operatorRule : predicate GATE ( rule[\"return\"] )+ -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) ) ;
    public final StencilParser.operatorRule_return operatorRule() throws RecognitionException {
        StencilParser.operatorRule_return retval = new StencilParser.operatorRule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GATE73=null;
        StencilParser.predicate_return predicate72 = null;

        StencilParser.rule_return rule74 = null;


        Object GATE73_tree=null;
        RewriteRuleTokenStream stream_GATE=new RewriteRuleTokenStream(adaptor,"token GATE");
        RewriteRuleSubtreeStream stream_rule=new RewriteRuleSubtreeStream(adaptor,"rule rule");
        RewriteRuleSubtreeStream stream_predicate=new RewriteRuleSubtreeStream(adaptor,"rule predicate");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:266:3: ( predicate GATE ( rule[\"return\"] )+ -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:266:5: predicate GATE ( rule[\"return\"] )+
            {
            pushFollow(FOLLOW_predicate_in_operatorRule1620);
            predicate72=predicate();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_predicate.add(predicate72.getTree());
            GATE73=(Token)match(input,GATE,FOLLOW_GATE_in_operatorRule1622); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_GATE.add(GATE73);

            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:266:20: ( rule[\"return\"] )+
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
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:266:20: rule[\"return\"]
            	    {
            	    pushFollow(FOLLOW_rule_in_operatorRule1624);
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
            // elements: predicate, rule
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 267:5: -> ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:267:8: ^( OPERATOR_RULE predicate ^( LIST[\"Rules\"] ( rule )+ ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR_RULE, "OPERATOR_RULE"), root_1);

                adaptor.addChild(root_1, stream_predicate.nextTree());
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:267:34: ^( LIST[\"Rules\"] ( rule )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:269:1: predicate : ( ( GROUP )? ALL ( CLOSE_GROUP )? -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) );
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
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_booleanOp=new RewriteRuleSubtreeStream(adaptor,"rule booleanOp");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:3: ( ( GROUP )? ALL ( CLOSE_GROUP )? -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) ) | GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==GROUP) ) {
                int LA26_1 = input.LA(2);

                if ( ((LA26_1>=CANVAS && LA26_1<=DEFAULT)||LA26_1==LOCAL||LA26_1==VIEW||LA26_1==ARG||LA26_1==ID||(LA26_1>=TAGGED_ID && LA26_1<=DIGITS)||LA26_1==84||(LA26_1>=93 && LA26_1<=95)) ) {
                    alt26=2;
                }
                else if ( (LA26_1==ALL) ) {
                    int LA26_4 = input.LA(3);

                    if ( (LA26_4==CLOSE_GROUP||LA26_4==GATE) ) {
                        alt26=1;
                    }
                    else if ( (LA26_4==81||LA26_4==83||(LA26_4>=85 && LA26_4<=90)) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:5: ( GROUP )? ALL ( CLOSE_GROUP )?
                    {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:5: ( GROUP )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==GROUP) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:5: GROUP
                            {
                            GROUP75=(Token)match(input,GROUP,FOLLOW_GROUP_in_predicate1656); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_GROUP.add(GROUP75);


                            }
                            break;

                    }

                    ALL76=(Token)match(input,ALL,FOLLOW_ALL_in_predicate1659); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ALL.add(ALL76);

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:16: ( CLOSE_GROUP )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==CLOSE_GROUP) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:270:16: CLOSE_GROUP
                            {
                            CLOSE_GROUP77=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_predicate1661); if (state.failed) return retval; 
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
                    // 271:5: -> ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:271:8: ^( LIST[\"Predicates\"] ^( PREDICATE ALL ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:271:29: ^( PREDICATE ALL )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:272:5: GROUP value booleanOp value ( SEPARATOR value booleanOp value )* CLOSE_GROUP
                    {
                    GROUP78=(Token)match(input,GROUP,FOLLOW_GROUP_in_predicate1685); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP78);

                    pushFollow(FOLLOW_value_in_predicate1687);
                    value79=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value79.getTree());
                    pushFollow(FOLLOW_booleanOp_in_predicate1689);
                    booleanOp80=booleanOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp80.getTree());
                    pushFollow(FOLLOW_value_in_predicate1691);
                    value81=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value81.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:272:33: ( SEPARATOR value booleanOp value )*
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);

                        if ( (LA25_0==SEPARATOR) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:272:34: SEPARATOR value booleanOp value
                    	    {
                    	    SEPARATOR82=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_predicate1694); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR82);

                    	    pushFollow(FOLLOW_value_in_predicate1696);
                    	    value83=value();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value.add(value83.getTree());
                    	    pushFollow(FOLLOW_booleanOp_in_predicate1698);
                    	    booleanOp84=booleanOp();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_booleanOp.add(booleanOp84.getTree());
                    	    pushFollow(FOLLOW_value_in_predicate1700);
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

                    CLOSE_GROUP86=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_predicate1704); if (state.failed) return retval; 
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
                    // 273:5: -> ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:273:8: ^( LIST[\"Predicates\"] ( ^( PREDICATE value booleanOp value ) )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Predicates"), root_1);

                        if ( !(stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_booleanOp.hasNext()||stream_value.hasNext()||stream_value.hasNext() ) {
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:273:29: ^( PREDICATE value booleanOp value )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:280:1: rule[String def] : target[def] ( DEFINE | DYNAMIC ) callChain -> ^( RULE target callChain ( DEFINE )? ( DYNAMIC )? ) ;
    public final StencilParser.rule_return rule(String def) throws RecognitionException {
        StencilParser.rule_return retval = new StencilParser.rule_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DEFINE88=null;
        Token DYNAMIC89=null;
        StencilParser.target_return target87 = null;

        StencilParser.callChain_return callChain90 = null;


        Object DEFINE88_tree=null;
        Object DYNAMIC89_tree=null;
        RewriteRuleTokenStream stream_DEFINE=new RewriteRuleTokenStream(adaptor,"token DEFINE");
        RewriteRuleTokenStream stream_DYNAMIC=new RewriteRuleTokenStream(adaptor,"token DYNAMIC");
        RewriteRuleSubtreeStream stream_callChain=new RewriteRuleSubtreeStream(adaptor,"rule callChain");
        RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"rule target");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:3: ( target[def] ( DEFINE | DYNAMIC ) callChain -> ^( RULE target callChain ( DEFINE )? ( DYNAMIC )? ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:5: target[def] ( DEFINE | DYNAMIC ) callChain
            {
            pushFollow(FOLLOW_target_in_rule1742);
            target87=target(def);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_target.add(target87.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:17: ( DEFINE | DYNAMIC )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:18: DEFINE
                    {
                    DEFINE88=(Token)match(input,DEFINE,FOLLOW_DEFINE_in_rule1746); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DEFINE.add(DEFINE88);


                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:281:27: DYNAMIC
                    {
                    DYNAMIC89=(Token)match(input,DYNAMIC,FOLLOW_DYNAMIC_in_rule1750); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DYNAMIC.add(DYNAMIC89);


                    }
                    break;

            }

            pushFollow(FOLLOW_callChain_in_rule1753);
            callChain90=callChain();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callChain.add(callChain90.getTree());


            // AST REWRITE
            // elements: callChain, DYNAMIC, target, DEFINE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 282:5: -> ^( RULE target callChain ( DEFINE )? ( DYNAMIC )? )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:8: ^( RULE target callChain ( DEFINE )? ( DYNAMIC )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RULE, "RULE"), root_1);

                adaptor.addChild(root_1, stream_target.nextTree());
                adaptor.addChild(root_1, stream_callChain.nextTree());
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:32: ( DEFINE )?
                if ( stream_DEFINE.hasNext() ) {
                    adaptor.addChild(root_1, stream_DEFINE.nextNode());

                }
                stream_DEFINE.reset();
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:282:40: ( DYNAMIC )?
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

    public static class callChain_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callChain"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:1: callChain : callChainMember -> ^( CALL_CHAIN callChainMember ) ;
    public final StencilParser.callChain_return callChain() throws RecognitionException {
        StencilParser.callChain_return retval = new StencilParser.callChain_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.callChainMember_return callChainMember91 = null;


        RewriteRuleSubtreeStream stream_callChainMember=new RewriteRuleSubtreeStream(adaptor,"rule callChainMember");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:10: ( callChainMember -> ^( CALL_CHAIN callChainMember ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:12: callChainMember
            {
            pushFollow(FOLLOW_callChainMember_in_callChain1780);
            callChainMember91=callChainMember();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_callChainMember.add(callChainMember91.getTree());


            // AST REWRITE
            // elements: callChainMember
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 284:28: -> ^( CALL_CHAIN callChainMember )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:284:31: ^( CALL_CHAIN callChainMember )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL_CHAIN, "CALL_CHAIN"), root_1);

                adaptor.addChild(root_1, stream_callChainMember.nextTree());

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

    public static class callChainMember_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callChainMember"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:285:1: callChainMember : ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget );
    public final StencilParser.callChainMember_return callChainMember() throws RecognitionException {
        StencilParser.callChainMember_return retval = new StencilParser.callChainMember_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.value_return value92 = null;

        StencilParser.emptySet_return emptySet93 = null;

        StencilParser.valueList_return valueList94 = null;

        StencilParser.functionCallTarget_return functionCallTarget95 = null;


        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_emptySet=new RewriteRuleSubtreeStream(adaptor,"rule emptySet");
        RewriteRuleSubtreeStream stream_valueList=new RewriteRuleSubtreeStream(adaptor,"rule valueList");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:286:3: ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget )
            int alt28=4;
            alt28 = dfa28.predict(input);
            switch (alt28) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:286:5: value
                    {
                    pushFollow(FOLLOW_value_in_callChainMember1797);
                    value92=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value92.getTree());


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
                    // 286:11: -> ^( PACK value )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:286:14: ^( PACK value )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:287:5: emptySet
                    {
                    pushFollow(FOLLOW_emptySet_in_callChainMember1811);
                    emptySet93=emptySet();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_emptySet.add(emptySet93.getTree());


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
                    // 287:14: -> ^( PACK )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:287:17: ^( PACK )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:5: valueList
                    {
                    pushFollow(FOLLOW_valueList_in_callChainMember1823);
                    valueList94=valueList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueList.add(valueList94.getTree());


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
                    // 288:15: -> ^( PACK valueList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:288:18: ^( PACK valueList )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:289:5: functionCallTarget
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_functionCallTarget_in_callChainMember1837);
                    functionCallTarget95=functionCallTarget();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, functionCallTarget95.getTree());

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
    // $ANTLR end "callChainMember"

    public static class functionCallTarget_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionCallTarget"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:292:1: functionCallTarget : ( ( functionCall passOp )=>f1= functionCall passOp f2= callChainMember -> ^( $f1 passOp $f2) | f1= functionCall -> ^( $f1 DIRECT_YIELD ^( PACK DEFAULT ) ) );
    public final StencilParser.functionCallTarget_return functionCallTarget() throws RecognitionException {
        StencilParser.functionCallTarget_return retval = new StencilParser.functionCallTarget_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.functionCall_return f1 = null;

        StencilParser.callChainMember_return f2 = null;

        StencilParser.passOp_return passOp96 = null;


        RewriteRuleSubtreeStream stream_callChainMember=new RewriteRuleSubtreeStream(adaptor,"rule callChainMember");
        RewriteRuleSubtreeStream stream_functionCall=new RewriteRuleSubtreeStream(adaptor,"rule functionCall");
        RewriteRuleSubtreeStream stream_passOp=new RewriteRuleSubtreeStream(adaptor,"rule passOp");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:293:3: ( ( functionCall passOp )=>f1= functionCall passOp f2= callChainMember -> ^( $f1 passOp $f2) | f1= functionCall -> ^( $f1 DIRECT_YIELD ^( PACK DEFAULT ) ) )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==ID) ) {
                int LA29_1 = input.LA(2);

                if ( (synpred1_Stencil()) ) {
                    alt29=1;
                }
                else if ( (true) ) {
                    alt29=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }
            switch (alt29) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:293:5: ( functionCall passOp )=>f1= functionCall passOp f2= callChainMember
                    {
                    pushFollow(FOLLOW_functionCall_in_functionCallTarget1861);
                    f1=functionCall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_functionCall.add(f1.getTree());
                    pushFollow(FOLLOW_passOp_in_functionCallTarget1863);
                    passOp96=passOp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_passOp.add(passOp96.getTree());
                    pushFollow(FOLLOW_callChainMember_in_functionCallTarget1867);
                    f2=callChainMember();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callChainMember.add(f2.getTree());


                    // AST REWRITE
                    // elements: f2, f1, passOp
                    // token labels: 
                    // rule labels: f2, f1, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 294:6: -> ^( $f1 passOp $f2)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:294:9: ^( $f1 passOp $f2)
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:295:5: f1= functionCall
                    {
                    pushFollow(FOLLOW_functionCall_in_functionCallTarget1893);
                    f1=functionCall();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_functionCall.add(f1.getTree());


                    // AST REWRITE
                    // elements: f1
                    // token labels: 
                    // rule labels: f1, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 295:21: -> ^( $f1 DIRECT_YIELD ^( PACK DEFAULT ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:295:24: ^( $f1 DIRECT_YIELD ^( PACK DEFAULT ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_f1.nextNode(), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(DIRECT_YIELD, "DIRECT_YIELD"));
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:295:43: ^( PACK DEFAULT )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:298:1: functionCall : ( ( callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList )=>name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) ) | name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] emptySet -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] ) ) );
    public final StencilParser.functionCall_return functionCall() throws RecognitionException {
        StencilParser.functionCall_return retval = new StencilParser.functionCall_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.callName_return name = null;

        StencilParser.specializer_return specializer97 = null;

        StencilParser.valueList_return valueList98 = null;

        StencilParser.specializer_return specializer99 = null;

        StencilParser.emptySet_return emptySet100 = null;


        RewriteRuleSubtreeStream stream_emptySet=new RewriteRuleSubtreeStream(adaptor,"rule emptySet");
        RewriteRuleSubtreeStream stream_callName=new RewriteRuleSubtreeStream(adaptor,"rule callName");
        RewriteRuleSubtreeStream stream_specializer=new RewriteRuleSubtreeStream(adaptor,"rule specializer");
        RewriteRuleSubtreeStream stream_valueList=new RewriteRuleSubtreeStream(adaptor,"rule valueList");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:3: ( ( callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList )=>name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) ) | name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] emptySet -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] ) ) )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==ID) ) {
                int LA30_1 = input.LA(2);

                if ( (synpred2_Stencil()) ) {
                    alt30=1;
                }
                else if ( (true) ) {
                    alt30=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 30, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:4: ( callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList )=>name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList
                    {
                    pushFollow(FOLLOW_callName_in_functionCall1938);
                    name=callName(MAIN_BLOCK_TAG);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callName.add(name.getTree());
                    pushFollow(FOLLOW_specializer_in_functionCall1941);
                    specializer97=specializer(RuleOpts.All);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_specializer.add(specializer97.getTree());
                    pushFollow(FOLLOW_valueList_in_functionCall1944);
                    valueList98=valueList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueList.add(valueList98.getTree());


                    // AST REWRITE
                    // elements: specializer, valueList
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 301:5: -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:301:8: ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] valueList ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUNCTION, ((Tree)name.tree).getText()), root_1);

                        adaptor.addChild(root_1, stream_specializer.nextTree());
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:301:60: ^( LIST[\"args\"] valueList )
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
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:302:5: name= callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] emptySet
                    {
                    pushFollow(FOLLOW_callName_in_functionCall1972);
                    name=callName(MAIN_BLOCK_TAG);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_callName.add(name.getTree());
                    pushFollow(FOLLOW_specializer_in_functionCall1975);
                    specializer99=specializer(RuleOpts.All);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_specializer.add(specializer99.getTree());
                    pushFollow(FOLLOW_emptySet_in_functionCall1978);
                    emptySet100=emptySet();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_emptySet.add(emptySet100.getTree());


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
                    // 303:5: -> ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:303:8: ^( FUNCTION[((Tree)name.tree).getText()] specializer ^( LIST[\"args\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUNCTION, ((Tree)name.tree).getText()), root_1);

                        adaptor.addChild(root_1, stream_specializer.nextTree());
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:303:60: ^( LIST[\"args\"] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "args"), root_2);

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
    // $ANTLR end "functionCall"

    public static class callName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "callName"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:306:1: callName[String defaultCall] : (pre= ID NAMESPACE post= ID -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text] -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall] | name= ID -> {name.getText().indexOf(\".\") > 0}? ID[$name.text] -> ID[$name.text + \".\" + defaultCall] );
    public final StencilParser.callName_return callName(String defaultCall) throws RecognitionException {
        StencilParser.callName_return retval = new StencilParser.callName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token pre=null;
        Token post=null;
        Token name=null;
        Token NAMESPACE101=null;

        Object pre_tree=null;
        Object post_tree=null;
        Object name_tree=null;
        Object NAMESPACE101_tree=null;
        RewriteRuleTokenStream stream_NAMESPACE=new RewriteRuleTokenStream(adaptor,"token NAMESPACE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:307:3: (pre= ID NAMESPACE post= ID -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text] -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall] | name= ID -> {name.getText().indexOf(\".\") > 0}? ID[$name.text] -> ID[$name.text + \".\" + defaultCall] )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==ID) ) {
                int LA31_1 = input.LA(2);

                if ( (LA31_1==NAMESPACE) ) {
                    alt31=1;
                }
                else if ( (LA31_1==GROUP||LA31_1==ARG) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:307:5: pre= ID NAMESPACE post= ID
                    {
                    pre=(Token)match(input,ID,FOLLOW_ID_in_callName2012); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(pre);

                    NAMESPACE101=(Token)match(input,NAMESPACE,FOLLOW_NAMESPACE_in_callName2014); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAMESPACE.add(NAMESPACE101);

                    post=(Token)match(input,ID,FOLLOW_ID_in_callName2018); if (state.failed) return retval; 
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
                    // 308:5: -> {post.getText().indexOf(\".\") > 0}? ID[$pre.text + NAMESPACE + $post.text]
                    if (post.getText().indexOf(".") > 0) {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (pre!=null?pre.getText():null) + NAMESPACE + (post!=null?post.getText():null)));

                    }
                    else // 309:5: -> ID[$pre.text + NAMESPACE + $post.text + \".\" + defaultCall]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (pre!=null?pre.getText():null) + NAMESPACE + (post!=null?post.getText():null) + "." + defaultCall));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:310:5: name= ID
                    {
                    name=(Token)match(input,ID,FOLLOW_ID_in_callName2066); if (state.failed) return retval; 
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
                    // 311:5: -> {name.getText().indexOf(\".\") > 0}? ID[$name.text]
                    if (name.getText().indexOf(".") > 0) {
                        adaptor.addChild(root_0, (Object)adaptor.create(ID, (name!=null?name.getText():null)));

                    }
                    else // 312:5: -> ID[$name.text + \".\" + defaultCall]
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:314:1: target[String def] : ( GLYPH tuple[false] | RETURN tuple[false] | CANVAS tuple[false] | LOCAL tuple[false] | VIEW tuple[false] | tuple[true] -> {def.equals(\"glyph\")}? ^( GLYPH tuple ) -> {def.equals(\"return\")}? ^( RETURN tuple ) -> ^( DEFAULT tuple ) );
    public final StencilParser.target_return target(String def) throws RecognitionException {
        StencilParser.target_return retval = new StencilParser.target_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GLYPH102=null;
        Token RETURN104=null;
        Token CANVAS106=null;
        Token LOCAL108=null;
        Token VIEW110=null;
        StencilParser.tuple_return tuple103 = null;

        StencilParser.tuple_return tuple105 = null;

        StencilParser.tuple_return tuple107 = null;

        StencilParser.tuple_return tuple109 = null;

        StencilParser.tuple_return tuple111 = null;

        StencilParser.tuple_return tuple112 = null;


        Object GLYPH102_tree=null;
        Object RETURN104_tree=null;
        Object CANVAS106_tree=null;
        Object LOCAL108_tree=null;
        Object VIEW110_tree=null;
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:315:3: ( GLYPH tuple[false] | RETURN tuple[false] | CANVAS tuple[false] | LOCAL tuple[false] | VIEW tuple[false] | tuple[true] -> {def.equals(\"glyph\")}? ^( GLYPH tuple ) -> {def.equals(\"return\")}? ^( RETURN tuple ) -> ^( DEFAULT tuple ) )
            int alt32=6;
            switch ( input.LA(1) ) {
            case GLYPH:
                {
                alt32=1;
                }
                break;
            case RETURN:
                {
                alt32=2;
                }
                break;
            case CANVAS:
                {
                alt32=3;
                }
                break;
            case LOCAL:
                {
                alt32=4;
                }
                break;
            case VIEW:
                {
                alt32=5;
                }
                break;
            case GROUP:
            case ID:
                {
                alt32=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:315:5: GLYPH tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    GLYPH102=(Token)match(input,GLYPH,FOLLOW_GLYPH_in_target2117); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GLYPH102_tree = (Object)adaptor.create(GLYPH102);
                    root_0 = (Object)adaptor.becomeRoot(GLYPH102_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2120);
                    tuple103=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple103.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:316:5: RETURN tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    RETURN104=(Token)match(input,RETURN,FOLLOW_RETURN_in_target2127); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    RETURN104_tree = (Object)adaptor.create(RETURN104);
                    root_0 = (Object)adaptor.becomeRoot(RETURN104_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2130);
                    tuple105=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple105.getTree());

                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:317:5: CANVAS tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    CANVAS106=(Token)match(input,CANVAS,FOLLOW_CANVAS_in_target2137); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CANVAS106_tree = (Object)adaptor.create(CANVAS106);
                    root_0 = (Object)adaptor.becomeRoot(CANVAS106_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2140);
                    tuple107=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple107.getTree());

                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:318:5: LOCAL tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    LOCAL108=(Token)match(input,LOCAL,FOLLOW_LOCAL_in_target2147); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LOCAL108_tree = (Object)adaptor.create(LOCAL108);
                    root_0 = (Object)adaptor.becomeRoot(LOCAL108_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2150);
                    tuple109=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple109.getTree());

                    }
                    break;
                case 5 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:319:5: VIEW tuple[false]
                    {
                    root_0 = (Object)adaptor.nil();

                    VIEW110=(Token)match(input,VIEW,FOLLOW_VIEW_in_target2157); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    VIEW110_tree = (Object)adaptor.create(VIEW110);
                    root_0 = (Object)adaptor.becomeRoot(VIEW110_tree, root_0);
                    }
                    pushFollow(FOLLOW_tuple_in_target2160);
                    tuple111=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tuple111.getTree());

                    }
                    break;
                case 6 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:320:5: tuple[true]
                    {
                    pushFollow(FOLLOW_tuple_in_target2167);
                    tuple112=tuple(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple112.getTree());


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
                    // 321:5: -> {def.equals(\"glyph\")}? ^( GLYPH tuple )
                    if (def.equals("glyph")) {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:321:31: ^( GLYPH tuple )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(GLYPH, "GLYPH"), root_1);

                        adaptor.addChild(root_1, stream_tuple.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 322:5: -> {def.equals(\"return\")}? ^( RETURN tuple )
                    if (def.equals("return")) {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:322:32: ^( RETURN tuple )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RETURN, "RETURN"), root_1);

                        adaptor.addChild(root_1, stream_tuple.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 323:5: -> ^( DEFAULT tuple )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:323:8: ^( DEFAULT tuple )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:327:1: pythonDef : ( ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID ( pythonBlock )+ ) | PYTHON name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ ) );
    public final StencilParser.pythonDef_return pythonDef() throws RecognitionException {
        StencilParser.pythonDef_return retval = new StencilParser.pythonDef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token env=null;
        Token name=null;
        Token PYTHON113=null;
        Token ARG114=null;
        Token CLOSE_ARG115=null;
        Token PYTHON117=null;
        StencilParser.pythonBlock_return pythonBlock116 = null;

        StencilParser.pythonBlock_return pythonBlock118 = null;


        Object env_tree=null;
        Object name_tree=null;
        Object PYTHON113_tree=null;
        Object ARG114_tree=null;
        Object CLOSE_ARG115_tree=null;
        Object PYTHON117_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_PYTHON=new RewriteRuleTokenStream(adaptor,"token PYTHON");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_pythonBlock=new RewriteRuleSubtreeStream(adaptor,"rule pythonBlock");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:3: ( ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID ( pythonBlock )+ ) | PYTHON name= ID ( pythonBlock )+ -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ ) )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==PYTHON) ) {
                int LA35_1 = input.LA(2);

                if ( (LA35_1==ARG) && (synpred3_Stencil())) {
                    alt35=1;
                }
                else if ( (LA35_1==ID) ) {
                    alt35=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 35, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:5: ( PYTHON ARG )=> PYTHON ARG env= ID CLOSE_ARG name= ID ( pythonBlock )+
                    {
                    PYTHON113=(Token)match(input,PYTHON,FOLLOW_PYTHON_in_pythonDef2228); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PYTHON.add(PYTHON113);

                    ARG114=(Token)match(input,ARG,FOLLOW_ARG_in_pythonDef2230); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG114);

                    env=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2234); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(env);

                    CLOSE_ARG115=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_pythonDef2236); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG115);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2240); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:57: ( pythonBlock )+
                    int cnt33=0;
                    loop33:
                    do {
                        int alt33=2;
                        int LA33_0 = input.LA(1);

                        if ( (LA33_0==FACET||LA33_0==TAGGED_ID) ) {
                            alt33=1;
                        }


                        switch (alt33) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:57: pythonBlock
                    	    {
                    	    pushFollow(FOLLOW_pythonBlock_in_pythonDef2242);
                    	    pythonBlock116=pythonBlock();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_pythonBlock.add(pythonBlock116.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt33 >= 1 ) break loop33;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(33, input);
                                throw eee;
                        }
                        cnt33++;
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
                    // 329:5: -> ^( PYTHON[$name.text] ID ( pythonBlock )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:329:8: ^( PYTHON[$name.text] ID ( pythonBlock )+ )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:330:5: PYTHON name= ID ( pythonBlock )+
                    {
                    PYTHON117=(Token)match(input,PYTHON,FOLLOW_PYTHON_in_pythonDef2265); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PYTHON.add(PYTHON117);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonDef2269); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:330:20: ( pythonBlock )+
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
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:330:20: pythonBlock
                    	    {
                    	    pushFollow(FOLLOW_pythonBlock_in_pythonDef2271);
                    	    pythonBlock118=pythonBlock();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_pythonBlock.add(pythonBlock118.getTree());

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
                    // elements: pythonBlock, PYTHON, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 331:5: -> ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:331:8: ^( PYTHON[$name.text] ID[buryID($name.text)] ( pythonBlock )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:333:1: pythonBlock : ( FACET 'Init' CODE_BLOCK -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK ) | annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK ) );
    public final StencilParser.pythonBlock_return pythonBlock() throws RecognitionException {
        StencilParser.pythonBlock_return retval = new StencilParser.pythonBlock_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token FACET119=null;
        Token string_literal120=null;
        Token CODE_BLOCK121=null;
        Token FACET123=null;
        Token YIELDS125=null;
        Token CODE_BLOCK127=null;
        StencilParser.annotations_return annotations122 = null;

        StencilParser.tuple_return tuple124 = null;

        StencilParser.tuple_return tuple126 = null;


        Object name_tree=null;
        Object FACET119_tree=null;
        Object string_literal120_tree=null;
        Object CODE_BLOCK121_tree=null;
        Object FACET123_tree=null;
        Object YIELDS125_tree=null;
        Object CODE_BLOCK127_tree=null;
        RewriteRuleTokenStream stream_YIELDS=new RewriteRuleTokenStream(adaptor,"token YIELDS");
        RewriteRuleTokenStream stream_CODE_BLOCK=new RewriteRuleTokenStream(adaptor,"token CODE_BLOCK");
        RewriteRuleTokenStream stream_FACET=new RewriteRuleTokenStream(adaptor,"token FACET");
        RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        RewriteRuleSubtreeStream stream_annotations=new RewriteRuleSubtreeStream(adaptor,"rule annotations");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:334:2: ( FACET 'Init' CODE_BLOCK -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK ) | annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==FACET) ) {
                int LA36_1 = input.LA(2);

                if ( (LA36_1==82) ) {
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
            else if ( (LA36_0==TAGGED_ID) ) {
                alt36=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:334:4: FACET 'Init' CODE_BLOCK
                    {
                    FACET119=(Token)match(input,FACET,FOLLOW_FACET_in_pythonBlock2300); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FACET.add(FACET119);

                    string_literal120=(Token)match(input,82,FOLLOW_82_in_pythonBlock2302); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal120);

                    CODE_BLOCK121=(Token)match(input,CODE_BLOCK,FOLLOW_CODE_BLOCK_in_pythonBlock2304); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CODE_BLOCK.add(CODE_BLOCK121);



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
                    // 335:3: -> ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:6: ^( PYTHON_FACET[\"Init\"] ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE ) ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) ) CODE_BLOCK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON_FACET, "Init"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:29: ^( YIELDS TUPLE_PROTOTYPE TUPLE_PROTOTYPE )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(YIELDS, "YIELDS"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"));
                        adaptor.addChild(root_2, (Object)adaptor.create(TUPLE_PROTOTYPE, "TUPLE_PROTOTYPE"));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:71: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"Type\"] STRING[\"NA\"] ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_2);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:335:93: ^( ANNOTATION[\"Type\"] STRING[\"NA\"] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:336:4: annotations FACET name= ID tuple[true] YIELDS tuple[false] CODE_BLOCK
                    {
                    pushFollow(FOLLOW_annotations_in_pythonBlock2341);
                    annotations122=annotations();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_annotations.add(annotations122.getTree());
                    FACET123=(Token)match(input,FACET,FOLLOW_FACET_in_pythonBlock2343); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FACET.add(FACET123);

                    name=(Token)match(input,ID,FOLLOW_ID_in_pythonBlock2347); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);

                    pushFollow(FOLLOW_tuple_in_pythonBlock2349);
                    tuple124=tuple(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple124.getTree());
                    YIELDS125=(Token)match(input,YIELDS,FOLLOW_YIELDS_in_pythonBlock2352); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_YIELDS.add(YIELDS125);

                    pushFollow(FOLLOW_tuple_in_pythonBlock2354);
                    tuple126=tuple(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_tuple.add(tuple126.getTree());
                    CODE_BLOCK127=(Token)match(input,CODE_BLOCK,FOLLOW_CODE_BLOCK_in_pythonBlock2357); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CODE_BLOCK.add(CODE_BLOCK127);



                    // AST REWRITE
                    // elements: CODE_BLOCK, tuple, tuple, annotations, YIELDS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 337:3: -> ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:337:6: ^( PYTHON_FACET[name] ^( YIELDS tuple tuple ) annotations CODE_BLOCK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PYTHON_FACET, name), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:337:27: ^( YIELDS tuple tuple )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:339:1: annotations : (a= annotation -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) ) | -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) ) );
    public final StencilParser.annotations_return annotations() throws RecognitionException {
        StencilParser.annotations_return retval = new StencilParser.annotations_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.annotation_return a = null;


        RewriteRuleSubtreeStream stream_annotation=new RewriteRuleSubtreeStream(adaptor,"rule annotation");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:3: (a= annotation -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) ) | -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==TAGGED_ID) ) {
                alt37=1;
            }
            else if ( (LA37_0==FACET) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:5: a= annotation
                    {
                    pushFollow(FOLLOW_annotation_in_annotations2390);
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
                    // 340:18: -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:21: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:340:43: ^( ANNOTATION[\"TYPE\"] STRING[$a.text.toUpperCase().substring(1)] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:341:5: 
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
                    // 341:5: -> ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:341:8: ^( LIST[\"Annotations\"] ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Annotations"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:341:30: ^( ANNOTATION[\"TYPE\"] STRING[\"CATEGORIZE\"] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:343:1: annotation : t= TAGGED_ID -> ANNOTATION[\"JUNK\"] ;
    public final StencilParser.annotation_return annotation() throws RecognitionException {
        StencilParser.annotation_return retval = new StencilParser.annotation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;

        Object t_tree=null;
        RewriteRuleTokenStream stream_TAGGED_ID=new RewriteRuleTokenStream(adaptor,"token TAGGED_ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:343:11: (t= TAGGED_ID -> ANNOTATION[\"JUNK\"] )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:343:13: t= TAGGED_ID
            {
            t=(Token)match(input,TAGGED_ID,FOLLOW_TAGGED_ID_in_annotation2433); if (state.failed) return retval; 
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
            // 343:25: -> ANNOTATION[\"JUNK\"]
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:347:1: specializer[RuleOpts opts] : ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) );
    public final StencilParser.specializer_return specializer(RuleOpts opts) throws RecognitionException {
        StencilParser.specializer_return retval = new StencilParser.specializer_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ARG128=null;
        Token CLOSE_ARG131=null;
        Token ARG132=null;
        Token CLOSE_ARG135=null;
        Token ARG136=null;
        Token SPLIT138=null;
        Token CLOSE_ARG141=null;
        Token ARG142=null;
        Token SPLIT144=null;
        Token CLOSE_ARG147=null;
        Token ARG148=null;
        Token CLOSE_ARG150=null;
        StencilParser.range_return range129 = null;

        StencilParser.sepArgList_return sepArgList130 = null;

        StencilParser.split_return split133 = null;

        StencilParser.sepArgList_return sepArgList134 = null;

        StencilParser.range_return range137 = null;

        StencilParser.split_return split139 = null;

        StencilParser.sepArgList_return sepArgList140 = null;

        StencilParser.split_return split143 = null;

        StencilParser.range_return range145 = null;

        StencilParser.sepArgList_return sepArgList146 = null;

        StencilParser.argList_return argList149 = null;


        Object ARG128_tree=null;
        Object CLOSE_ARG131_tree=null;
        Object ARG132_tree=null;
        Object CLOSE_ARG135_tree=null;
        Object ARG136_tree=null;
        Object SPLIT138_tree=null;
        Object CLOSE_ARG141_tree=null;
        Object ARG142_tree=null;
        Object SPLIT144_tree=null;
        Object CLOSE_ARG147_tree=null;
        Object ARG148_tree=null;
        Object CLOSE_ARG150_tree=null;
        RewriteRuleTokenStream stream_SPLIT=new RewriteRuleTokenStream(adaptor,"token SPLIT");
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleSubtreeStream stream_sepArgList=new RewriteRuleSubtreeStream(adaptor,"rule sepArgList");
        RewriteRuleSubtreeStream stream_argList=new RewriteRuleSubtreeStream(adaptor,"rule argList");
        RewriteRuleSubtreeStream stream_split=new RewriteRuleSubtreeStream(adaptor,"rule split");
        RewriteRuleSubtreeStream stream_range=new RewriteRuleSubtreeStream(adaptor,"rule range");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:348:3: ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) )
            int alt38=6;
            alt38 = dfa38.predict(input);
            switch (alt38) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:348:5: ARG range sepArgList CLOSE_ARG {...}?
                    {
                    ARG128=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2452); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG128);

                    pushFollow(FOLLOW_range_in_specializer2454);
                    range129=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range129.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2456);
                    sepArgList130=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList130.getTree());
                    CLOSE_ARG131=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2458); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG131);

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
                    // 349:29: -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:349:32: ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        adaptor.addChild(root_1, stream_range.nextTree());
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:349:52: ^( SPLIT BASIC PRE ID[(String) null] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:350:5: ARG split[false] sepArgList CLOSE_ARG {...}?
                    {
                    ARG132=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2491); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG132);

                    pushFollow(FOLLOW_split_in_specializer2493);
                    split133=split(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split133.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2496);
                    sepArgList134=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList134.getTree());
                    CLOSE_ARG135=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2498); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG135);

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
                    // 351:29: -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:351:32: ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:351:46: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:352:5: ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}?
                    {
                    ARG136=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2530); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG136);

                    pushFollow(FOLLOW_range_in_specializer2532);
                    range137=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range137.getTree());
                    SPLIT138=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_specializer2534); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT138);

                    pushFollow(FOLLOW_split_in_specializer2536);
                    split139=split(false);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split139.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2539);
                    sepArgList140=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList140.getTree());
                    CLOSE_ARG141=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2541); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG141);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: sepArgList, split, range
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 353:29: -> ^( SPECIALIZER range split sepArgList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:353:33: ^( SPECIALIZER range split sepArgList )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:354:5: ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}?
                    {
                    ARG142=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2566); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG142);

                    pushFollow(FOLLOW_split_in_specializer2568);
                    split143=split(true);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_split.add(split143.getTree());
                    SPLIT144=(Token)match(input,SPLIT,FOLLOW_SPLIT_in_specializer2571); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SPLIT.add(SPLIT144);

                    pushFollow(FOLLOW_range_in_specializer2573);
                    range145=range();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range.add(range145.getTree());
                    pushFollow(FOLLOW_sepArgList_in_specializer2575);
                    sepArgList146=sepArgList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sepArgList.add(sepArgList146.getTree());
                    CLOSE_ARG147=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2577); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG147);

                    if ( !((opts == RuleOpts.All)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "specializer", "opts == RuleOpts.All");
                    }


                    // AST REWRITE
                    // elements: range, sepArgList, split
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 355:29: -> ^( SPECIALIZER range split sepArgList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:355:32: ^( SPECIALIZER range split sepArgList )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:356:5: ARG argList CLOSE_ARG {...}?
                    {
                    ARG148=(Token)match(input,ARG,FOLLOW_ARG_in_specializer2601); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG148);

                    pushFollow(FOLLOW_argList_in_specializer2603);
                    argList149=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_argList.add(argList149.getTree());
                    CLOSE_ARG150=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_specializer2605); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG150);

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
                    // 357:31: -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:34: ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPECIALIZER, "SPECIALIZER"), root_1);

                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:48: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(RANGE, "RANGE"), root_2);

                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));
                        adaptor.addChild(root_2, (Object)adaptor.create(NUMBER, RANGE_END));

                        adaptor.addChild(root_1, root_2);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:357:93: ^( SPLIT BASIC PRE ID[(String) null] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:358:5: 
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
                    // 358:5: -> ^( SPECIALIZER DEFAULT )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:358:8: ^( SPECIALIZER DEFAULT )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:360:1: sepArgList : ( SEPARATOR argList | -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) );
    public final StencilParser.sepArgList_return sepArgList() throws RecognitionException {
        StencilParser.sepArgList_return retval = new StencilParser.sepArgList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR151=null;
        StencilParser.argList_return argList152 = null;


        Object SEPARATOR151_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:361:3: ( SEPARATOR argList | -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==SEPARATOR) ) {
                alt39=1;
            }
            else if ( (LA39_0==CLOSE_ARG) ) {
                alt39=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:361:5: SEPARATOR argList
                    {
                    root_0 = (Object)adaptor.nil();

                    SEPARATOR151=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_sepArgList2662); if (state.failed) return retval;
                    pushFollow(FOLLOW_argList_in_sepArgList2665);
                    argList152=argList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, argList152.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:362:5: 
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
                    // 362:5: -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:362:8: ^( LIST[\"Values Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Values Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:362:36: ^( LIST[\"Map Arguments\"] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:364:1: argList : ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList );
    public final StencilParser.argList_return argList() throws RecognitionException {
        StencilParser.argList_return retval = new StencilParser.argList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR156=null;
        StencilParser.values_return values153 = null;

        StencilParser.mapList_return mapList154 = null;

        StencilParser.values_return values155 = null;

        StencilParser.mapList_return mapList157 = null;


        Object SEPARATOR156_tree=null;
        RewriteRuleSubtreeStream stream_mapList=new RewriteRuleSubtreeStream(adaptor,"rule mapList");
        RewriteRuleSubtreeStream stream_values=new RewriteRuleSubtreeStream(adaptor,"rule values");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:365:3: ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList )
            int alt40=4;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:365:5: 
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
                    // 365:5: -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:365:8: ^( LIST[\"Values Arguments\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Values Arguments"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:365:36: ^( LIST[\"Map Arguments\"] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:5: values
                    {
                    pushFollow(FOLLOW_values_in_argList2710);
                    values153=values();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_values.add(values153.getTree());


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
                    // 366:12: -> values ^( LIST[\"Map Arguments\"] )
                    {
                        adaptor.addChild(root_0, stream_values.nextTree());
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:366:22: ^( LIST[\"Map Arguments\"] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:367:5: mapList
                    {
                    pushFollow(FOLLOW_mapList_in_argList2725);
                    mapList154=mapList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mapList.add(mapList154.getTree());


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
                    // 367:13: -> ^( LIST[\"Value Arguments\"] ) mapList
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:367:16: ^( LIST[\"Value Arguments\"] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:368:5: values SEPARATOR mapList
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_values_in_argList2740);
                    values155=values();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, values155.getTree());
                    SEPARATOR156=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_argList2742); if (state.failed) return retval;
                    pushFollow(FOLLOW_mapList_in_argList2745);
                    mapList157=mapList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mapList157.getTree());

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:370:1: values : atom ( SEPARATOR atom )* -> ^( LIST[\"Value Arguments\"] ( atom )* ) ;
    public final StencilParser.values_return values() throws RecognitionException {
        StencilParser.values_return retval = new StencilParser.values_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR159=null;
        StencilParser.atom_return atom158 = null;

        StencilParser.atom_return atom160 = null;


        Object SEPARATOR159_tree=null;
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:3: ( atom ( SEPARATOR atom )* -> ^( LIST[\"Value Arguments\"] ( atom )* ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:5: atom ( SEPARATOR atom )*
            {
            pushFollow(FOLLOW_atom_in_values2755);
            atom158=atom();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_atom.add(atom158.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:10: ( SEPARATOR atom )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==SEPARATOR) ) {
                    int LA41_2 = input.LA(2);

                    if ( (LA41_2==ALL||LA41_2==DEFAULT||(LA41_2>=TAGGED_ID && LA41_2<=DIGITS)||(LA41_2>=93 && LA41_2<=95)) ) {
                        alt41=1;
                    }


                }


                switch (alt41) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:11: SEPARATOR atom
            	    {
            	    SEPARATOR159=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_values2758); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR159);

            	    pushFollow(FOLLOW_atom_in_values2760);
            	    atom160=atom();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_atom.add(atom160.getTree());

            	    }
            	    break;

            	default :
            	    break loop41;
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
            // 371:28: -> ^( LIST[\"Value Arguments\"] ( atom )* )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:31: ^( LIST[\"Value Arguments\"] ( atom )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Value Arguments"), root_1);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:371:57: ( atom )*
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:373:1: mapList : mapEntry ( SEPARATOR mapEntry )* -> ^( LIST[\"Map Arguments\"] ( mapEntry )* ) ;
    public final StencilParser.mapList_return mapList() throws RecognitionException {
        StencilParser.mapList_return retval = new StencilParser.mapList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEPARATOR162=null;
        StencilParser.mapEntry_return mapEntry161 = null;

        StencilParser.mapEntry_return mapEntry163 = null;


        Object SEPARATOR162_tree=null;
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleSubtreeStream stream_mapEntry=new RewriteRuleSubtreeStream(adaptor,"rule mapEntry");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:3: ( mapEntry ( SEPARATOR mapEntry )* -> ^( LIST[\"Map Arguments\"] ( mapEntry )* ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:5: mapEntry ( SEPARATOR mapEntry )*
            {
            pushFollow(FOLLOW_mapEntry_in_mapList2782);
            mapEntry161=mapEntry();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_mapEntry.add(mapEntry161.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:14: ( SEPARATOR mapEntry )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==SEPARATOR) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:15: SEPARATOR mapEntry
            	    {
            	    SEPARATOR162=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_mapList2785); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR162);

            	    pushFollow(FOLLOW_mapEntry_in_mapList2787);
            	    mapEntry163=mapEntry();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mapEntry.add(mapEntry163.getTree());

            	    }
            	    break;

            	default :
            	    break loop42;
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
            // 374:36: -> ^( LIST[\"Map Arguments\"] ( mapEntry )* )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:39: ^( LIST[\"Map Arguments\"] ( mapEntry )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LIST, "Map Arguments"), root_1);

                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:374:63: ( mapEntry )*
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:376:1: mapEntry : k= ID '=' v= atom -> ^( MAP_ENTRY[$k.text] $v) ;
    public final StencilParser.mapEntry_return mapEntry() throws RecognitionException {
        StencilParser.mapEntry_return retval = new StencilParser.mapEntry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token k=null;
        Token char_literal164=null;
        StencilParser.atom_return v = null;


        Object k_tree=null;
        Object char_literal164_tree=null;
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:377:3: (k= ID '=' v= atom -> ^( MAP_ENTRY[$k.text] $v) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:377:5: k= ID '=' v= atom
            {
            k=(Token)match(input,ID,FOLLOW_ID_in_mapEntry2814); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(k);

            char_literal164=(Token)match(input,83,FOLLOW_83_in_mapEntry2816); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_83.add(char_literal164);

            pushFollow(FOLLOW_atom_in_mapEntry2820);
            v=atom();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_atom.add(v.getTree());


            // AST REWRITE
            // elements: v
            // token labels: 
            // rule labels: retval, v
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"rule v",v!=null?v.tree:null);

            root_0 = (Object)adaptor.nil();
            // 377:21: -> ^( MAP_ENTRY[$k.text] $v)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:377:24: ^( MAP_ENTRY[$k.text] $v)
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:379:1: tuple[boolean allowEmpty] : ( emptySet {...}? -> ^( TUPLE_PROTOTYPE ) | ID -> ^( TUPLE_PROTOTYPE ID ) | GROUP ID ( SEPARATOR ID )* CLOSE_GROUP -> ^( TUPLE_PROTOTYPE ( ID )+ ) );
    public final StencilParser.tuple_return tuple(boolean allowEmpty) throws RecognitionException {
        StencilParser.tuple_return retval = new StencilParser.tuple_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID166=null;
        Token GROUP167=null;
        Token ID168=null;
        Token SEPARATOR169=null;
        Token ID170=null;
        Token CLOSE_GROUP171=null;
        StencilParser.emptySet_return emptySet165 = null;


        Object ID166_tree=null;
        Object GROUP167_tree=null;
        Object ID168_tree=null;
        Object SEPARATOR169_tree=null;
        Object ID170_tree=null;
        Object CLOSE_GROUP171_tree=null;
        RewriteRuleTokenStream stream_SEPARATOR=new RewriteRuleTokenStream(adaptor,"token SEPARATOR");
        RewriteRuleTokenStream stream_CLOSE_GROUP=new RewriteRuleTokenStream(adaptor,"token CLOSE_GROUP");
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_emptySet=new RewriteRuleSubtreeStream(adaptor,"rule emptySet");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:380:3: ( emptySet {...}? -> ^( TUPLE_PROTOTYPE ) | ID -> ^( TUPLE_PROTOTYPE ID ) | GROUP ID ( SEPARATOR ID )* CLOSE_GROUP -> ^( TUPLE_PROTOTYPE ( ID )+ ) )
            int alt44=3;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==GROUP) ) {
                int LA44_1 = input.LA(2);

                if ( (LA44_1==CLOSE_GROUP) ) {
                    alt44=1;
                }
                else if ( (LA44_1==ID) ) {
                    alt44=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 44, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA44_0==ID) ) {
                alt44=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:380:5: emptySet {...}?
                    {
                    pushFollow(FOLLOW_emptySet_in_tuple2841);
                    emptySet165=emptySet();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_emptySet.add(emptySet165.getTree());
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
                    // 381:5: -> ^( TUPLE_PROTOTYPE )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:381:8: ^( TUPLE_PROTOTYPE )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:382:5: ID
                    {
                    ID166=(Token)match(input,ID,FOLLOW_ID_in_tuple2859); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID166);



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
                    // 383:5: -> ^( TUPLE_PROTOTYPE ID )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:383:8: ^( TUPLE_PROTOTYPE ID )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:384:5: GROUP ID ( SEPARATOR ID )* CLOSE_GROUP
                    {
                    GROUP167=(Token)match(input,GROUP,FOLLOW_GROUP_in_tuple2877); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_GROUP.add(GROUP167);

                    ID168=(Token)match(input,ID,FOLLOW_ID_in_tuple2879); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID168);

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:384:14: ( SEPARATOR ID )*
                    loop43:
                    do {
                        int alt43=2;
                        int LA43_0 = input.LA(1);

                        if ( (LA43_0==SEPARATOR) ) {
                            alt43=1;
                        }


                        switch (alt43) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:384:15: SEPARATOR ID
                    	    {
                    	    SEPARATOR169=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_tuple2882); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SEPARATOR.add(SEPARATOR169);

                    	    ID170=(Token)match(input,ID,FOLLOW_ID_in_tuple2884); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_ID.add(ID170);


                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);

                    CLOSE_GROUP171=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_tuple2888); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_GROUP.add(CLOSE_GROUP171);



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
                    // 385:5: -> ^( TUPLE_PROTOTYPE ( ID )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:385:8: ^( TUPLE_PROTOTYPE ( ID )+ )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:388:1: emptySet : GROUP CLOSE_GROUP ;
    public final StencilParser.emptySet_return emptySet() throws RecognitionException {
        StencilParser.emptySet_return retval = new StencilParser.emptySet_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP172=null;
        Token CLOSE_GROUP173=null;

        Object GROUP172_tree=null;
        Object CLOSE_GROUP173_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:388:9: ( GROUP CLOSE_GROUP )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:388:11: GROUP CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP172=(Token)match(input,GROUP,FOLLOW_GROUP_in_emptySet2909); if (state.failed) return retval;
            CLOSE_GROUP173=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_emptySet2912); if (state.failed) return retval;

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:1: valueList : GROUP value ( SEPARATOR value )* CLOSE_GROUP ;
    public final StencilParser.valueList_return valueList() throws RecognitionException {
        StencilParser.valueList_return retval = new StencilParser.valueList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP174=null;
        Token SEPARATOR176=null;
        Token CLOSE_GROUP178=null;
        StencilParser.value_return value175 = null;

        StencilParser.value_return value177 = null;


        Object GROUP174_tree=null;
        Object SEPARATOR176_tree=null;
        Object CLOSE_GROUP178_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:10: ( GROUP value ( SEPARATOR value )* CLOSE_GROUP )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:13: GROUP value ( SEPARATOR value )* CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP174=(Token)match(input,GROUP,FOLLOW_GROUP_in_valueList2921); if (state.failed) return retval;
            pushFollow(FOLLOW_value_in_valueList2924);
            value175=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, value175.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:26: ( SEPARATOR value )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==SEPARATOR) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:390:27: SEPARATOR value
            	    {
            	    SEPARATOR176=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_valueList2927); if (state.failed) return retval;
            	    pushFollow(FOLLOW_value_in_valueList2930);
            	    value177=value();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, value177.getTree());

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);

            CLOSE_GROUP178=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_valueList2934); if (state.failed) return retval;

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:392:1: range : ( number RANGE number -> ^( RANGE number number ) | number RANGE i= ID {...}? -> ^( RANGE number NUMBER[RANGE_END] ) | s= ID RANGE e= ID {...}? -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) );
    public final StencilParser.range_return range() throws RecognitionException {
        StencilParser.range_return retval = new StencilParser.range_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token i=null;
        Token s=null;
        Token e=null;
        Token RANGE180=null;
        Token RANGE183=null;
        Token RANGE184=null;
        StencilParser.number_return number179 = null;

        StencilParser.number_return number181 = null;

        StencilParser.number_return number182 = null;


        Object i_tree=null;
        Object s_tree=null;
        Object e_tree=null;
        Object RANGE180_tree=null;
        Object RANGE183_tree=null;
        Object RANGE184_tree=null;
        RewriteRuleTokenStream stream_RANGE=new RewriteRuleTokenStream(adaptor,"token RANGE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:393:3: ( number RANGE number -> ^( RANGE number number ) | number RANGE i= ID {...}? -> ^( RANGE number NUMBER[RANGE_END] ) | s= ID RANGE e= ID {...}? -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) )
            int alt46=3;
            alt46 = dfa46.predict(input);
            switch (alt46) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:393:5: number RANGE number
                    {
                    pushFollow(FOLLOW_number_in_range2949);
                    number179=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number179.getTree());
                    RANGE180=(Token)match(input,RANGE,FOLLOW_RANGE_in_range2951); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE180);

                    pushFollow(FOLLOW_number_in_range2953);
                    number181=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number181.getTree());


                    // AST REWRITE
                    // elements: number, number, RANGE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 394:5: -> ^( RANGE number number )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:394:8: ^( RANGE number number )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:395:5: number RANGE i= ID {...}?
                    {
                    pushFollow(FOLLOW_number_in_range2973);
                    number182=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number182.getTree());
                    RANGE183=(Token)match(input,RANGE,FOLLOW_RANGE_in_range2975); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE183);

                    i=(Token)match(input,ID,FOLLOW_ID_in_range2979); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(i);

                    if ( !(((i!=null?i.getText():null).equals(FINAL_VALUE))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "range", "$i.text.equals(FINAL_VALUE)");
                    }


                    // AST REWRITE
                    // elements: number, RANGE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 397:5: -> ^( RANGE number NUMBER[RANGE_END] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:397:8: ^( RANGE number NUMBER[RANGE_END] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:398:5: s= ID RANGE e= ID {...}?
                    {
                    s=(Token)match(input,ID,FOLLOW_ID_in_range3008); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(s);

                    RANGE184=(Token)match(input,RANGE,FOLLOW_RANGE_in_range3010); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANGE.add(RANGE184);

                    e=(Token)match(input,ID,FOLLOW_ID_in_range3014); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(e);

                    if ( !(((s!=null?s.getText():null).equals(FINAL_VALUE) && (e!=null?e.getText():null).equals(FINAL_VALUE))) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "range", "$s.text.equals(FINAL_VALUE) && $e.text.equals(FINAL_VALUE)");
                    }


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
                    // 400:5: -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:400:8: ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:403:1: split[boolean pre] : ( ID -> {pre}? ^( SPLIT BASIC PRE ID ) -> ^( SPLIT BASIC POST ID ) | ORDER ID -> {pre}? ^( SPLIT ORDER PRE ID ) -> ^( SPLIT ORDER POST ID ) );
    public final StencilParser.split_return split(boolean pre) throws RecognitionException {
        StencilParser.split_return retval = new StencilParser.split_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID185=null;
        Token ORDER186=null;
        Token ID187=null;

        Object ID185_tree=null;
        Object ORDER186_tree=null;
        Object ID187_tree=null;
        RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:3: ( ID -> {pre}? ^( SPLIT BASIC PRE ID ) -> ^( SPLIT BASIC POST ID ) | ORDER ID -> {pre}? ^( SPLIT ORDER PRE ID ) -> ^( SPLIT ORDER POST ID ) )
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==ID) ) {
                alt47=1;
            }
            else if ( (LA47_0==ORDER) ) {
                alt47=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }
            switch (alt47) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:5: ID
                    {
                    ID185=(Token)match(input,ID,FOLLOW_ID_in_split3048); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID185);



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
                    // 404:9: -> {pre}? ^( SPLIT BASIC PRE ID )
                    if (pre) {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:404:19: ^( SPLIT BASIC PRE ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(BASIC, "BASIC"));
                        adaptor.addChild(root_1, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 405:9: -> ^( SPLIT BASIC POST ID )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:405:19: ^( SPLIT BASIC POST ID )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:406:7: ORDER ID
                    {
                    ORDER186=(Token)match(input,ORDER,FOLLOW_ORDER_in_split3098); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ORDER.add(ORDER186);

                    ID187=(Token)match(input,ID,FOLLOW_ID_in_split3100); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID187);



                    // AST REWRITE
                    // elements: ID, ID, ORDER, ORDER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 407:9: -> {pre}? ^( SPLIT ORDER PRE ID )
                    if (pre) {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:407:19: ^( SPLIT ORDER PRE ID )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SPLIT, "SPLIT"), root_1);

                        adaptor.addChild(root_1, stream_ORDER.nextNode());
                        adaptor.addChild(root_1, (Object)adaptor.create(PRE, "PRE"));
                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    else // 408:9: -> ^( SPLIT ORDER POST ID )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:408:19: ^( SPLIT ORDER POST ID )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:410:1: value : ( tupleRef | atom );
    public final StencilParser.value_return value() throws RecognitionException {
        StencilParser.value_return retval = new StencilParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.tupleRef_return tupleRef188 = null;

        StencilParser.atom_return atom189 = null;



        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:410:7: ( tupleRef | atom )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==CANVAS||LA48_0==LOCAL||LA48_0==VIEW||LA48_0==ARG||LA48_0==ID||LA48_0==84) ) {
                alt48=1;
            }
            else if ( (LA48_0==ALL||LA48_0==DEFAULT||(LA48_0>=TAGGED_ID && LA48_0<=DIGITS)||(LA48_0>=93 && LA48_0<=95)) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:410:9: tupleRef
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_tupleRef_in_value3158);
                    tupleRef188=tupleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tupleRef188.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:410:21: atom
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_value3163);
                    atom189=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom189.getTree());

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:1: atom : ( sigil | number | STRING | DEFAULT | ALL );
    public final StencilParser.atom_return atom() throws RecognitionException {
        StencilParser.atom_return retval = new StencilParser.atom_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING192=null;
        Token DEFAULT193=null;
        Token ALL194=null;
        StencilParser.sigil_return sigil190 = null;

        StencilParser.number_return number191 = null;


        Object STRING192_tree=null;
        Object DEFAULT193_tree=null;
        Object ALL194_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:7: ( sigil | number | STRING | DEFAULT | ALL )
            int alt49=5;
            switch ( input.LA(1) ) {
            case TAGGED_ID:
                {
                alt49=1;
                }
                break;
            case DIGITS:
            case 93:
            case 94:
            case 95:
                {
                alt49=2;
                }
                break;
            case STRING:
                {
                alt49=3;
                }
                break;
            case DEFAULT:
                {
                alt49=4;
                }
                break;
            case ALL:
                {
                alt49=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }

            switch (alt49) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:9: sigil
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sigil_in_atom3171);
                    sigil190=sigil();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sigil190.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:17: number
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_number_in_atom3175);
                    number191=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, number191.getTree());

                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:26: STRING
                    {
                    root_0 = (Object)adaptor.nil();

                    STRING192=(Token)match(input,STRING,FOLLOW_STRING_in_atom3179); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING192_tree = (Object)adaptor.create(STRING192);
                    adaptor.addChild(root_0, STRING192_tree);
                    }

                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:35: DEFAULT
                    {
                    root_0 = (Object)adaptor.nil();

                    DEFAULT193=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_atom3183); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DEFAULT193_tree = (Object)adaptor.create(DEFAULT193);
                    adaptor.addChild(root_0, DEFAULT193_tree);
                    }

                    }
                    break;
                case 5 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:411:45: ALL
                    {
                    root_0 = (Object)adaptor.nil();

                    ALL194=(Token)match(input,ALL,FOLLOW_ALL_in_atom3187); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ALL194_tree = (Object)adaptor.create(ALL194);
                    adaptor.addChild(root_0, ALL194_tree);
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:413:1: tupleRef : ( simpleRef | simpleRef ( qualifiedRef )+ -> ^( simpleRef ( qualifiedRef )+ ) );
    public final StencilParser.tupleRef_return tupleRef() throws RecognitionException {
        StencilParser.tupleRef_return retval = new StencilParser.tupleRef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.simpleRef_return simpleRef195 = null;

        StencilParser.simpleRef_return simpleRef196 = null;

        StencilParser.qualifiedRef_return qualifiedRef197 = null;


        RewriteRuleSubtreeStream stream_simpleRef=new RewriteRuleSubtreeStream(adaptor,"rule simpleRef");
        RewriteRuleSubtreeStream stream_qualifiedRef=new RewriteRuleSubtreeStream(adaptor,"rule qualifiedRef");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:414:3: ( simpleRef | simpleRef ( qualifiedRef )+ -> ^( simpleRef ( qualifiedRef )+ ) )
            int alt51=2;
            alt51 = dfa51.predict(input);
            switch (alt51) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:414:5: simpleRef
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_simpleRef_in_tupleRef3199);
                    simpleRef195=simpleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleRef195.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:415:5: simpleRef ( qualifiedRef )+
                    {
                    pushFollow(FOLLOW_simpleRef_in_tupleRef3205);
                    simpleRef196=simpleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_simpleRef.add(simpleRef196.getTree());
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:415:15: ( qualifiedRef )+
                    int cnt50=0;
                    loop50:
                    do {
                        int alt50=2;
                        int LA50_0 = input.LA(1);

                        if ( (LA50_0==ARG) ) {
                            alt50=1;
                        }


                        switch (alt50) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:415:15: qualifiedRef
                    	    {
                    	    pushFollow(FOLLOW_qualifiedRef_in_tupleRef3207);
                    	    qualifiedRef197=qualifiedRef();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_qualifiedRef.add(qualifiedRef197.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt50 >= 1 ) break loop50;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(50, input);
                                throw eee;
                        }
                        cnt50++;
                    } while (true);



                    // AST REWRITE
                    // elements: simpleRef, qualifiedRef
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 415:29: -> ^( simpleRef ( qualifiedRef )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:415:32: ^( simpleRef ( qualifiedRef )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_simpleRef.nextNode(), root_1);

                        if ( !(stream_qualifiedRef.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_qualifiedRef.hasNext() ) {
                            adaptor.addChild(root_1, stream_qualifiedRef.nextTree());

                        }
                        stream_qualifiedRef.reset();

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

    public static class simpleRef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "simpleRef"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:417:9: private simpleRef : ( ID -> ^( TUPLE_REF ID ) | '_' -> ^( TUPLE_REF NUMBER[\"0\"] ) | ARG number CLOSE_ARG -> ^( TUPLE_REF number ) | c= CANVAS -> ^( TUPLE_REF ID[$c.text] ) | l= LOCAL -> ^( TUPLE_REF ID[$l.text] ) | v= VIEW -> ^( TUPLE_REF ID[$v.text] ) );
    public final StencilParser.simpleRef_return simpleRef() throws RecognitionException {
        StencilParser.simpleRef_return retval = new StencilParser.simpleRef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token c=null;
        Token l=null;
        Token v=null;
        Token ID198=null;
        Token char_literal199=null;
        Token ARG200=null;
        Token CLOSE_ARG202=null;
        StencilParser.number_return number201 = null;


        Object c_tree=null;
        Object l_tree=null;
        Object v_tree=null;
        Object ID198_tree=null;
        Object char_literal199_tree=null;
        Object ARG200_tree=null;
        Object CLOSE_ARG202_tree=null;
        RewriteRuleTokenStream stream_VIEW=new RewriteRuleTokenStream(adaptor,"token VIEW");
        RewriteRuleTokenStream stream_LOCAL=new RewriteRuleTokenStream(adaptor,"token LOCAL");
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
        RewriteRuleTokenStream stream_CANVAS=new RewriteRuleTokenStream(adaptor,"token CANVAS");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:418:3: ( ID -> ^( TUPLE_REF ID ) | '_' -> ^( TUPLE_REF NUMBER[\"0\"] ) | ARG number CLOSE_ARG -> ^( TUPLE_REF number ) | c= CANVAS -> ^( TUPLE_REF ID[$c.text] ) | l= LOCAL -> ^( TUPLE_REF ID[$l.text] ) | v= VIEW -> ^( TUPLE_REF ID[$v.text] ) )
            int alt52=6;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt52=1;
                }
                break;
            case 84:
                {
                alt52=2;
                }
                break;
            case ARG:
                {
                alt52=3;
                }
                break;
            case CANVAS:
                {
                alt52=4;
                }
                break;
            case LOCAL:
                {
                alt52=5;
                }
                break;
            case VIEW:
                {
                alt52=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 52, 0, input);

                throw nvae;
            }

            switch (alt52) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:418:5: ID
                    {
                    ID198=(Token)match(input,ID,FOLLOW_ID_in_simpleRef3229); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID198);



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
                    // 418:9: -> ^( TUPLE_REF ID )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:418:12: ^( TUPLE_REF ID )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:419:5: '_'
                    {
                    char_literal199=(Token)match(input,84,FOLLOW_84_in_simpleRef3244); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(char_literal199);



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
                    // 419:9: -> ^( TUPLE_REF NUMBER[\"0\"] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:419:12: ^( TUPLE_REF NUMBER[\"0\"] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:420:5: ARG number CLOSE_ARG
                    {
                    ARG200=(Token)match(input,ARG,FOLLOW_ARG_in_simpleRef3259); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG200);

                    pushFollow(FOLLOW_number_in_simpleRef3261);
                    number201=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(number201.getTree());
                    CLOSE_ARG202=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_simpleRef3263); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG202);



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
                    // 420:26: -> ^( TUPLE_REF number )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:420:29: ^( TUPLE_REF number )
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
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:421:5: c= CANVAS
                    {
                    c=(Token)match(input,CANVAS,FOLLOW_CANVAS_in_simpleRef3279); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CANVAS.add(c);



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
                    // 421:14: -> ^( TUPLE_REF ID[$c.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:421:17: ^( TUPLE_REF ID[$c.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(ID, (c!=null?c.getText():null)));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:422:5: l= LOCAL
                    {
                    l=(Token)match(input,LOCAL,FOLLOW_LOCAL_in_simpleRef3296); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LOCAL.add(l);



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
                    // 422:13: -> ^( TUPLE_REF ID[$l.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:422:16: ^( TUPLE_REF ID[$l.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(ID, (l!=null?l.getText():null)));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:423:5: v= VIEW
                    {
                    v=(Token)match(input,VIEW,FOLLOW_VIEW_in_simpleRef3313); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VIEW.add(v);



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
                    // 423:12: -> ^( TUPLE_REF ID[$v.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:423:15: ^( TUPLE_REF ID[$v.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, (Object)adaptor.create(ID, (v!=null?v.getText():null)));

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
    // $ANTLR end "simpleRef"

    public static class qualifiedRef_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "qualifiedRef"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:425:9: private qualifiedRef : ( ARG i= ID CLOSE_ARG -> ^( TUPLE_REF $i) | ARG n= number CLOSE_ARG -> ^( TUPLE_REF $n) | ARG '_' CLOSE_ARG -> ^( TUPLE_REF NUMBER[\"0\"] ) );
    public final StencilParser.qualifiedRef_return qualifiedRef() throws RecognitionException {
        StencilParser.qualifiedRef_return retval = new StencilParser.qualifiedRef_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token i=null;
        Token ARG203=null;
        Token CLOSE_ARG204=null;
        Token ARG205=null;
        Token CLOSE_ARG206=null;
        Token ARG207=null;
        Token char_literal208=null;
        Token CLOSE_ARG209=null;
        StencilParser.number_return n = null;


        Object i_tree=null;
        Object ARG203_tree=null;
        Object CLOSE_ARG204_tree=null;
        Object ARG205_tree=null;
        Object CLOSE_ARG206_tree=null;
        Object ARG207_tree=null;
        Object char_literal208_tree=null;
        Object CLOSE_ARG209_tree=null;
        RewriteRuleTokenStream stream_ARG=new RewriteRuleTokenStream(adaptor,"token ARG");
        RewriteRuleTokenStream stream_CLOSE_ARG=new RewriteRuleTokenStream(adaptor,"token CLOSE_ARG");
        RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_number=new RewriteRuleSubtreeStream(adaptor,"rule number");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:426:3: ( ARG i= ID CLOSE_ARG -> ^( TUPLE_REF $i) | ARG n= number CLOSE_ARG -> ^( TUPLE_REF $n) | ARG '_' CLOSE_ARG -> ^( TUPLE_REF NUMBER[\"0\"] ) )
            int alt53=3;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==ARG) ) {
                switch ( input.LA(2) ) {
                case ID:
                    {
                    alt53=1;
                    }
                    break;
                case 84:
                    {
                    alt53=3;
                    }
                    break;
                case DIGITS:
                case 93:
                case 94:
                case 95:
                    {
                    alt53=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }
            switch (alt53) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:426:5: ARG i= ID CLOSE_ARG
                    {
                    ARG203=(Token)match(input,ARG,FOLLOW_ARG_in_qualifiedRef3335); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG203);

                    i=(Token)match(input,ID,FOLLOW_ID_in_qualifiedRef3339); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(i);

                    CLOSE_ARG204=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_qualifiedRef3341); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG204);



                    // AST REWRITE
                    // elements: i
                    // token labels: i
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 426:24: -> ^( TUPLE_REF $i)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:426:27: ^( TUPLE_REF $i)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, stream_i.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:427:5: ARG n= number CLOSE_ARG
                    {
                    ARG205=(Token)match(input,ARG,FOLLOW_ARG_in_qualifiedRef3356); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG205);

                    pushFollow(FOLLOW_number_in_qualifiedRef3360);
                    n=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_number.add(n.getTree());
                    CLOSE_ARG206=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_qualifiedRef3362); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG206);



                    // AST REWRITE
                    // elements: n
                    // token labels: 
                    // rule labels: retval, n
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_n=new RewriteRuleSubtreeStream(adaptor,"rule n",n!=null?n.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 427:28: -> ^( TUPLE_REF $n)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:427:31: ^( TUPLE_REF $n)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TUPLE_REF, "TUPLE_REF"), root_1);

                        adaptor.addChild(root_1, stream_n.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:428:5: ARG '_' CLOSE_ARG
                    {
                    ARG207=(Token)match(input,ARG,FOLLOW_ARG_in_qualifiedRef3377); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARG.add(ARG207);

                    char_literal208=(Token)match(input,84,FOLLOW_84_in_qualifiedRef3379); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(char_literal208);

                    CLOSE_ARG209=(Token)match(input,CLOSE_ARG,FOLLOW_CLOSE_ARG_in_qualifiedRef3381); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CLOSE_ARG.add(CLOSE_ARG209);



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
                    // 428:23: -> ^( TUPLE_REF NUMBER[\"0\"] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:428:26: ^( TUPLE_REF NUMBER[\"0\"] )
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
    // $ANTLR end "qualifiedRef"

    public static class sigil_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sigil"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:430:1: sigil : t= TAGGED_ID sValueList -> ^( SIGIL[$t.text] sValueList ) ;
    public final StencilParser.sigil_return sigil() throws RecognitionException {
        StencilParser.sigil_return retval = new StencilParser.sigil_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;
        StencilParser.sValueList_return sValueList210 = null;


        Object t_tree=null;
        RewriteRuleTokenStream stream_TAGGED_ID=new RewriteRuleTokenStream(adaptor,"token TAGGED_ID");
        RewriteRuleSubtreeStream stream_sValueList=new RewriteRuleSubtreeStream(adaptor,"rule sValueList");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:430:6: (t= TAGGED_ID sValueList -> ^( SIGIL[$t.text] sValueList ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:430:8: t= TAGGED_ID sValueList
            {
            t=(Token)match(input,TAGGED_ID,FOLLOW_TAGGED_ID_in_sigil3399); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TAGGED_ID.add(t);

            pushFollow(FOLLOW_sValueList_in_sigil3401);
            sValueList210=sValueList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_sValueList.add(sValueList210.getTree());


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
            // 430:31: -> ^( SIGIL[$t.text] sValueList )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:430:34: ^( SIGIL[$t.text] sValueList )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:431:9: private sValueList : GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP ;
    public final StencilParser.sValueList_return sValueList() throws RecognitionException {
        StencilParser.sValueList_return retval = new StencilParser.sValueList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GROUP211=null;
        Token SEPARATOR213=null;
        Token CLOSE_GROUP215=null;
        StencilParser.sValue_return sValue212 = null;

        StencilParser.sValue_return sValue214 = null;


        Object GROUP211_tree=null;
        Object SEPARATOR213_tree=null;
        Object CLOSE_GROUP215_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:431:19: ( GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:431:22: GROUP sValue ( SEPARATOR sValue )* CLOSE_GROUP
            {
            root_0 = (Object)adaptor.nil();

            GROUP211=(Token)match(input,GROUP,FOLLOW_GROUP_in_sValueList3419); if (state.failed) return retval;
            pushFollow(FOLLOW_sValue_in_sValueList3422);
            sValue212=sValue();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sValue212.getTree());
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:431:36: ( SEPARATOR sValue )*
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==SEPARATOR) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:431:37: SEPARATOR sValue
            	    {
            	    SEPARATOR213=(Token)match(input,SEPARATOR,FOLLOW_SEPARATOR_in_sValueList3425); if (state.failed) return retval;
            	    pushFollow(FOLLOW_sValue_in_sValueList3428);
            	    sValue214=sValue();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, sValue214.getTree());

            	    }
            	    break;

            	default :
            	    break loop54;
                }
            } while (true);

            CLOSE_GROUP215=(Token)match(input,CLOSE_GROUP,FOLLOW_CLOSE_GROUP_in_sValueList3432); if (state.failed) return retval;

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:432:9: private sValue : ( tupleRef | number | STRING );
    public final StencilParser.sValue_return sValue() throws RecognitionException {
        StencilParser.sValue_return retval = new StencilParser.sValue_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING218=null;
        StencilParser.tupleRef_return tupleRef216 = null;

        StencilParser.number_return number217 = null;


        Object STRING218_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:432:16: ( tupleRef | number | STRING )
            int alt55=3;
            switch ( input.LA(1) ) {
            case CANVAS:
            case LOCAL:
            case VIEW:
            case ARG:
            case ID:
            case 84:
                {
                alt55=1;
                }
                break;
            case DIGITS:
            case 93:
            case 94:
            case 95:
                {
                alt55=2;
                }
                break;
            case STRING:
                {
                alt55=3;
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:432:18: tupleRef
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_tupleRef_in_sValue3442);
                    tupleRef216=tupleRef();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, tupleRef216.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:432:29: number
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_number_in_sValue3446);
                    number217=number();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, number217.getTree());

                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:432:38: STRING
                    {
                    root_0 = (Object)adaptor.nil();

                    STRING218=(Token)match(input,STRING,FOLLOW_STRING_in_sValue3450); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING218_tree = (Object)adaptor.create(STRING218);
                    adaptor.addChild(root_0, STRING218_tree);
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:435:1: booleanOp : (t= '>' -> BOOLEAN_OP[t] | t= '>=' -> BOOLEAN_OP[t] | t= '<' -> BOOLEAN_OP[t] | t= '<=' -> BOOLEAN_OP[t] | t= '=' -> BOOLEAN_OP[t] | t= '!=' -> BOOLEAN_OP[t] | t= '=~' -> BOOLEAN_OP[t] | t= '!~' -> BOOLEAN_OP[t] );
    public final StencilParser.booleanOp_return booleanOp() throws RecognitionException {
        StencilParser.booleanOp_return retval = new StencilParser.booleanOp_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token t=null;

        Object t_tree=null;
        RewriteRuleTokenStream stream_85=new RewriteRuleTokenStream(adaptor,"token 85");
        RewriteRuleTokenStream stream_87=new RewriteRuleTokenStream(adaptor,"token 87");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_86=new RewriteRuleTokenStream(adaptor,"token 86");
        RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
        RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
        RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:436:3: (t= '>' -> BOOLEAN_OP[t] | t= '>=' -> BOOLEAN_OP[t] | t= '<' -> BOOLEAN_OP[t] | t= '<=' -> BOOLEAN_OP[t] | t= '=' -> BOOLEAN_OP[t] | t= '!=' -> BOOLEAN_OP[t] | t= '=~' -> BOOLEAN_OP[t] | t= '!~' -> BOOLEAN_OP[t] )
            int alt56=8;
            switch ( input.LA(1) ) {
            case 81:
                {
                alt56=1;
                }
                break;
            case 85:
                {
                alt56=2;
                }
                break;
            case 86:
                {
                alt56=3;
                }
                break;
            case 87:
                {
                alt56=4;
                }
                break;
            case 83:
                {
                alt56=5;
                }
                break;
            case 88:
                {
                alt56=6;
                }
                break;
            case 89:
                {
                alt56=7;
                }
                break;
            case 90:
                {
                alt56=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:436:5: t= '>'
                    {
                    t=(Token)match(input,81,FOLLOW_81_in_booleanOp3464); if (state.failed) return retval; 
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
                    // 436:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:437:5: t= '>='
                    {
                    t=(Token)match(input,85,FOLLOW_85_in_booleanOp3479); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_85.add(t);



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
                    // 437:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:438:5: t= '<'
                    {
                    t=(Token)match(input,86,FOLLOW_86_in_booleanOp3493); if (state.failed) return retval; 
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
                    // 438:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:439:5: t= '<='
                    {
                    t=(Token)match(input,87,FOLLOW_87_in_booleanOp3508); if (state.failed) return retval; 
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
                    // 439:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:440:5: t= '='
                    {
                    t=(Token)match(input,83,FOLLOW_83_in_booleanOp3522); if (state.failed) return retval; 
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
                    // 440:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:441:5: t= '!='
                    {
                    t=(Token)match(input,88,FOLLOW_88_in_booleanOp3537); if (state.failed) return retval; 
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
                    // 441:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:442:5: t= '=~'
                    {
                    t=(Token)match(input,89,FOLLOW_89_in_booleanOp3551); if (state.failed) return retval; 
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
                    // 442:13: -> BOOLEAN_OP[t]
                    {
                        adaptor.addChild(root_0, (Object)adaptor.create(BOOLEAN_OP, t));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 8 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:443:5: t= '!~'
                    {
                    t=(Token)match(input,90,FOLLOW_90_in_booleanOp3565); if (state.failed) return retval; 
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
                    // 443:13: -> BOOLEAN_OP[t]
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:445:1: passOp : ( directYield | GUIDE_YIELD );
    public final StencilParser.passOp_return passOp() throws RecognitionException {
        StencilParser.passOp_return retval = new StencilParser.passOp_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token GUIDE_YIELD220=null;
        StencilParser.directYield_return directYield219 = null;


        Object GUIDE_YIELD220_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:446:3: ( directYield | GUIDE_YIELD )
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==YIELDS||LA57_0==91) ) {
                alt57=1;
            }
            else if ( (LA57_0==GUIDE_YIELD) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:446:5: directYield
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_directYield_in_passOp3582);
                    directYield219=directYield();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, directYield219.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:447:5: GUIDE_YIELD
                    {
                    root_0 = (Object)adaptor.nil();

                    GUIDE_YIELD220=(Token)match(input,GUIDE_YIELD,FOLLOW_GUIDE_YIELD_in_passOp3588); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GUIDE_YIELD220_tree = (Object)adaptor.create(GUIDE_YIELD220);
                    adaptor.addChild(root_0, GUIDE_YIELD220_tree);
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
    // $ANTLR end "passOp"

    public static class directYield_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "directYield"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:450:1: directYield : ( '-[' id= ID ']>' -> ^( DIRECT_YIELD[$id.text] ) | YIELDS -> ^( DIRECT_YIELD[(String) null] ) );
    public final StencilParser.directYield_return directYield() throws RecognitionException {
        StencilParser.directYield_return retval = new StencilParser.directYield_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token id=null;
        Token string_literal221=null;
        Token string_literal222=null;
        Token YIELDS223=null;

        Object id_tree=null;
        Object string_literal221_tree=null;
        Object string_literal222_tree=null;
        Object YIELDS223_tree=null;
        RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
        RewriteRuleTokenStream stream_YIELDS=new RewriteRuleTokenStream(adaptor,"token YIELDS");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:451:3: ( '-[' id= ID ']>' -> ^( DIRECT_YIELD[$id.text] ) | YIELDS -> ^( DIRECT_YIELD[(String) null] ) )
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==91) ) {
                alt58=1;
            }
            else if ( (LA58_0==YIELDS) ) {
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:451:5: '-[' id= ID ']>'
                    {
                    string_literal221=(Token)match(input,91,FOLLOW_91_in_directYield3604); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_91.add(string_literal221);

                    id=(Token)match(input,ID,FOLLOW_ID_in_directYield3608); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id);

                    string_literal222=(Token)match(input,92,FOLLOW_92_in_directYield3610); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_92.add(string_literal222);



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
                    // 451:21: -> ^( DIRECT_YIELD[$id.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:451:24: ^( DIRECT_YIELD[$id.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DIRECT_YIELD, (id!=null?id.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:452:5: YIELDS
                    {
                    YIELDS223=(Token)match(input,YIELDS,FOLLOW_YIELDS_in_directYield3623); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_YIELDS.add(YIELDS223);



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
                    // 452:12: -> ^( DIRECT_YIELD[(String) null] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:452:15: ^( DIRECT_YIELD[(String) null] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DIRECT_YIELD, (String) null), root_1);

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
    // $ANTLR end "directYield"

    public static class number_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "number"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:456:1: number : ( doubleNum | intNum );
    public final StencilParser.number_return number() throws RecognitionException {
        StencilParser.number_return retval = new StencilParser.number_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        StencilParser.doubleNum_return doubleNum224 = null;

        StencilParser.intNum_return intNum225 = null;



        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:456:9: ( doubleNum | intNum )
            int alt59=2;
            switch ( input.LA(1) ) {
            case 95:
                {
                alt59=1;
                }
                break;
            case DIGITS:
                {
                int LA59_2 = input.LA(2);

                if ( (LA59_2==95) ) {
                    alt59=1;
                }
                else if ( (LA59_2==EOF||LA59_2==ALL||LA59_2==CANVAS||(LA59_2>=FILTER && LA59_2<=GUIDE)||(LA59_2>=LOCAL && LA59_2<=TEMPLATE)||(LA59_2>=PYTHON && LA59_2<=VIEW)||(LA59_2>=GROUP && LA59_2<=CLOSE_GROUP)||(LA59_2>=CLOSE_ARG && LA59_2<=RANGE)||LA59_2==SPLIT||LA59_2==ID||LA59_2==81||LA59_2==83||(LA59_2>=85 && LA59_2<=90)) ) {
                    alt59=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 2, input);

                    throw nvae;
                }
                }
                break;
            case 93:
                {
                int LA59_3 = input.LA(2);

                if ( (LA59_3==DIGITS) ) {
                    int LA59_6 = input.LA(3);

                    if ( (LA59_6==95) ) {
                        alt59=1;
                    }
                    else if ( (LA59_6==EOF||LA59_6==ALL||LA59_6==CANVAS||(LA59_6>=FILTER && LA59_6<=GUIDE)||(LA59_6>=LOCAL && LA59_6<=TEMPLATE)||(LA59_6>=PYTHON && LA59_6<=VIEW)||(LA59_6>=GROUP && LA59_6<=CLOSE_GROUP)||(LA59_6>=CLOSE_ARG && LA59_6<=RANGE)||LA59_6==SPLIT||LA59_6==ID||LA59_6==81||LA59_6==83||(LA59_6>=85 && LA59_6<=90)) ) {
                        alt59=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 59, 6, input);

                        throw nvae;
                    }
                }
                else if ( (LA59_3==95) ) {
                    alt59=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 3, input);

                    throw nvae;
                }
                }
                break;
            case 94:
                {
                int LA59_4 = input.LA(2);

                if ( (LA59_4==DIGITS) ) {
                    int LA59_6 = input.LA(3);

                    if ( (LA59_6==95) ) {
                        alt59=1;
                    }
                    else if ( (LA59_6==EOF||LA59_6==ALL||LA59_6==CANVAS||(LA59_6>=FILTER && LA59_6<=GUIDE)||(LA59_6>=LOCAL && LA59_6<=TEMPLATE)||(LA59_6>=PYTHON && LA59_6<=VIEW)||(LA59_6>=GROUP && LA59_6<=CLOSE_GROUP)||(LA59_6>=CLOSE_ARG && LA59_6<=RANGE)||LA59_6==SPLIT||LA59_6==ID||LA59_6==81||LA59_6==83||(LA59_6>=85 && LA59_6<=90)) ) {
                        alt59=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 59, 6, input);

                        throw nvae;
                    }
                }
                else if ( (LA59_4==95) ) {
                    alt59=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 4, input);

                    throw nvae;
                }
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:456:12: doubleNum
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doubleNum_in_number3642);
                    doubleNum224=doubleNum();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doubleNum224.getTree());

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:456:24: intNum
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_intNum_in_number3646);
                    intNum225=intNum();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, intNum225.getTree());

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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:458:9: private intNum : ( (n= '-' | p= '+' ) d= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] ) | d= DIGITS -> ^( NUMBER[$d.text] ) );
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
        RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
        RewriteRuleTokenStream stream_DIGITS=new RewriteRuleTokenStream(adaptor,"token DIGITS");
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:3: ( (n= '-' | p= '+' ) d= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] ) | d= DIGITS -> ^( NUMBER[$d.text] ) )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( ((LA61_0>=93 && LA61_0<=94)) ) {
                alt61=1;
            }
            else if ( (LA61_0==DIGITS) ) {
                alt61=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;
            }
            switch (alt61) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:5: (n= '-' | p= '+' ) d= DIGITS
                    {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:5: (n= '-' | p= '+' )
                    int alt60=2;
                    int LA60_0 = input.LA(1);

                    if ( (LA60_0==93) ) {
                        alt60=1;
                    }
                    else if ( (LA60_0==94) ) {
                        alt60=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 60, 0, input);

                        throw nvae;
                    }
                    switch (alt60) {
                        case 1 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:6: n= '-'
                            {
                            n=(Token)match(input,93,FOLLOW_93_in_intNum3661); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(n);


                            }
                            break;
                        case 2 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:14: p= '+'
                            {
                            p=(Token)match(input,94,FOLLOW_94_in_intNum3667); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_94.add(p);


                            }
                            break;

                    }

                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_intNum3672); if (state.failed) return retval; 
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
                    // 459:30: -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:459:33: ^( NUMBER[p!=null?\"+\":\"-\" + $d.text] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:460:5: d= DIGITS
                    {
                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_intNum3687); if (state.failed) return retval; 
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
                    // 460:14: -> ^( NUMBER[$d.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:460:17: ^( NUMBER[$d.text] )
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
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:462:9: private doubleNum : ( '.' d2= DIGITS -> ^( NUMBER[\"0.\" + $d2.text] ) | d= DIGITS '.' d2= DIGITS -> ^( NUMBER[$d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + \".\" + $d2.text] ) );
    public final StencilParser.doubleNum_return doubleNum() throws RecognitionException {
        StencilParser.doubleNum_return retval = new StencilParser.doubleNum_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token d2=null;
        Token d=null;
        Token n=null;
        Token p=null;
        Token char_literal226=null;
        Token char_literal227=null;
        Token char_literal228=null;
        Token char_literal229=null;

        Object d2_tree=null;
        Object d_tree=null;
        Object n_tree=null;
        Object p_tree=null;
        Object char_literal226_tree=null;
        Object char_literal227_tree=null;
        Object char_literal228_tree=null;
        Object char_literal229_tree=null;
        RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
        RewriteRuleTokenStream stream_DIGITS=new RewriteRuleTokenStream(adaptor,"token DIGITS");
        RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:463:3: ( '.' d2= DIGITS -> ^( NUMBER[\"0.\" + $d2.text] ) | d= DIGITS '.' d2= DIGITS -> ^( NUMBER[$d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] ) | (n= '-' | p= '+' ) '.' d2= DIGITS -> ^( NUMBER[p!=null?\"+\":\"-\" + \".\" + $d2.text] ) )
            int alt64=4;
            switch ( input.LA(1) ) {
            case 95:
                {
                alt64=1;
                }
                break;
            case DIGITS:
                {
                alt64=2;
                }
                break;
            case 93:
                {
                int LA64_3 = input.LA(2);

                if ( (LA64_3==95) ) {
                    alt64=4;
                }
                else if ( (LA64_3==DIGITS) ) {
                    alt64=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 64, 3, input);

                    throw nvae;
                }
                }
                break;
            case 94:
                {
                int LA64_4 = input.LA(2);

                if ( (LA64_4==DIGITS) ) {
                    alt64=3;
                }
                else if ( (LA64_4==95) ) {
                    alt64=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 64, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:463:5: '.' d2= DIGITS
                    {
                    char_literal226=(Token)match(input,95,FOLLOW_95_in_doubleNum3706); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_95.add(char_literal226);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3710); if (state.failed) return retval; 
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
                    // 463:19: -> ^( NUMBER[\"0.\" + $d2.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:463:22: ^( NUMBER[\"0.\" + $d2.text] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:464:5: d= DIGITS '.' d2= DIGITS
                    {
                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3725); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);

                    char_literal227=(Token)match(input,95,FOLLOW_95_in_doubleNum3727); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_95.add(char_literal227);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3731); if (state.failed) return retval; 
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
                    // 464:28: -> ^( NUMBER[$d.text + \".\" + $d2.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:464:31: ^( NUMBER[$d.text + \".\" + $d2.text] )
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
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:465:5: (n= '-' | p= '+' ) d= DIGITS '.' d2= DIGITS
                    {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:465:5: (n= '-' | p= '+' )
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==93) ) {
                        alt62=1;
                    }
                    else if ( (LA62_0==94) ) {
                        alt62=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 62, 0, input);

                        throw nvae;
                    }
                    switch (alt62) {
                        case 1 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:465:6: n= '-'
                            {
                            n=(Token)match(input,93,FOLLOW_93_in_doubleNum3747); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(n);


                            }
                            break;
                        case 2 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:465:14: p= '+'
                            {
                            p=(Token)match(input,94,FOLLOW_94_in_doubleNum3753); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_94.add(p);


                            }
                            break;

                    }

                    d=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3758); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIGITS.add(d);

                    char_literal228=(Token)match(input,95,FOLLOW_95_in_doubleNum3760); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_95.add(char_literal228);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3764); if (state.failed) return retval; 
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
                    // 465:44: -> ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:465:47: ^( NUMBER[p!=null?\"+\":\"-\" + $d.text + \".\" + $d2.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, p!=null?"+":"-" + (d!=null?d.getText():null) + "." + (d2!=null?d2.getText():null)), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:466:5: (n= '-' | p= '+' ) '.' d2= DIGITS
                    {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:466:5: (n= '-' | p= '+' )
                    int alt63=2;
                    int LA63_0 = input.LA(1);

                    if ( (LA63_0==93) ) {
                        alt63=1;
                    }
                    else if ( (LA63_0==94) ) {
                        alt63=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 63, 0, input);

                        throw nvae;
                    }
                    switch (alt63) {
                        case 1 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:466:6: n= '-'
                            {
                            n=(Token)match(input,93,FOLLOW_93_in_doubleNum3780); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(n);


                            }
                            break;
                        case 2 :
                            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:466:14: p= '+'
                            {
                            p=(Token)match(input,94,FOLLOW_94_in_doubleNum3786); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_94.add(p);


                            }
                            break;

                    }

                    char_literal229=(Token)match(input,95,FOLLOW_95_in_doubleNum3789); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_95.add(char_literal229);

                    d2=(Token)match(input,DIGITS,FOLLOW_DIGITS_in_doubleNum3793); if (state.failed) return retval; 
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
                    // 466:35: -> ^( NUMBER[p!=null?\"+\":\"-\" + \".\" + $d2.text] )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:466:38: ^( NUMBER[p!=null?\"+\":\"-\" + \".\" + $d2.text] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NUMBER, p!=null?"+":"-" + "." + (d2!=null?d2.getText():null)), root_1);

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
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:293:5: ( functionCall passOp )
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:293:6: functionCall passOp
        {
        pushFollow(FOLLOW_functionCall_in_synpred1_Stencil1853);
        functionCall();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_passOp_in_synpred1_Stencil1855);
        passOp();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_Stencil

    // $ANTLR start synpred2_Stencil
    public final void synpred2_Stencil_fragment() throws RecognitionException {   
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:4: ( callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList )
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:299:5: callName[MAIN_BLOCK_TAG] specializer[RuleOpts.All] valueList
        {
        pushFollow(FOLLOW_callName_in_synpred2_Stencil1922);
        callName(MAIN_BLOCK_TAG);

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_specializer_in_synpred2_Stencil1925);
        specializer(RuleOpts.All);

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_valueList_in_synpred2_Stencil1928);
        valueList();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_Stencil

    // $ANTLR start synpred3_Stencil
    public final void synpred3_Stencil_fragment() throws RecognitionException {   
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:5: ( PYTHON ARG )
        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Stencil.g:328:6: PYTHON ARG
        {
        match(input,PYTHON,FOLLOW_PYTHON_in_synpred3_Stencil2221); if (state.failed) return ;
        match(input,ARG,FOLLOW_ARG_in_synpred3_Stencil2223); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_Stencil

    // Delegated rules

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


    protected DFA28 dfa28 = new DFA28(this);
    protected DFA38 dfa38 = new DFA38(this);
    protected DFA40 dfa40 = new DFA40(this);
    protected DFA46 dfa46 = new DFA46(this);
    protected DFA51 dfa51 = new DFA51(this);
    static final String DFA28_eotS =
        "\u00b9\uffff";
    static final String DFA28_eofS =
        "\1\uffff\1\2\14\uffff\1\4\16\uffff\1\2\2\uffff\1\2\4\uffff\2\4\u0092"+
        "\uffff";
    static final String DFA28_minS =
        "\2\40\1\uffff\1\40\1\uffff\2\40\2\uffff\1\70\1\114\1\70\2\114\1"+
        "\40\3\66\1\114\3\66\1\65\1\114\1\66\2\114\2\66\1\40\1\70\1\114\1"+
        "\40\1\70\1\114\1\110\3\40\1\114\1\70\2\114\1\42\1\66\1\114\1\66"+
        "\1\114\1\70\1\114\3\70\1\114\1\70\2\114\1\66\1\70\1\114\1\66\1\70"+
        "\1\114\2\66\1\114\3\66\1\114\1\66\2\114\2\66\1\114\1\66\1\70\2\66"+
        "\1\70\1\114\1\66\1\70\1\114\1\70\1\114\1\70\1\42\1\66\1\110\1\114"+
        "\1\70\2\114\1\66\1\114\1\66\1\114\1\66\1\70\1\114\2\70\2\66\1\114"+
        "\3\66\1\114\1\66\2\114\1\66\2\70\1\114\1\70\2\114\1\70\1\114\1\66"+
        "\1\114\1\70\1\66\1\114\1\66\1\70\1\110\1\114\1\70\2\114\1\66\1\114"+
        "\1\66\1\114\2\66\1\70\1\114\1\66\1\70\1\114\2\70\1\114\1\66\2\70"+
        "\1\114\1\70\2\114\1\70\1\114\1\66\1\70\1\114\1\66\1\114\1\66\1\70"+
        "\1\114\2\70\2\66\1\70\1\114\1\66\1\70\1\114\1\70\1\114\1\70\1\66"+
        "\2\70\1\114\3\70";
    static final String DFA28_maxS =
        "\1\137\1\110\1\uffff\1\137\1\uffff\2\137\2\uffff\1\123\1\114\3\137"+
        "\1\133\3\132\1\137\3\132\1\65\1\114\3\137\2\132\1\110\1\72\1\114"+
        "\1\110\1\137\1\114\2\137\2\133\1\114\4\137\1\132\1\114\1\137\1\114"+
        "\1\72\1\114\1\72\2\70\1\114\3\137\1\71\1\70\1\114\1\132\1\137\1"+
        "\114\2\71\1\137\3\71\1\114\3\137\1\71\1\132\1\114\1\132\1\72\2\132"+
        "\1\70\1\114\1\132\1\137\1\114\1\70\1\114\1\70\1\137\1\132\1\137"+
        "\1\114\3\137\1\71\1\114\1\137\1\114\1\132\1\70\1\114\2\70\2\71\1"+
        "\137\3\71\1\114\3\137\1\71\2\70\1\114\3\137\1\70\1\114\1\71\1\114"+
        "\1\137\1\71\1\114\1\71\1\70\1\137\1\114\3\137\1\71\1\114\1\137\1"+
        "\114\2\71\1\70\1\114\1\71\1\137\1\114\2\70\1\114\1\71\2\70\1\114"+
        "\3\137\1\70\1\114\1\71\1\137\1\114\1\71\1\114\1\71\1\70\1\114\2"+
        "\70\2\71\1\70\1\114\1\71\1\137\1\114\1\70\1\114\1\70\1\71\2\70\1"+
        "\114\3\70";
    static final String DFA28_acceptS =
        "\2\uffff\1\1\1\uffff\1\4\2\uffff\1\2\1\3\u00b0\uffff";
    static final String DFA28_specialS =
        "\u00b9\uffff}>";
    static final String[] DFA28_transitionS = {
            "\1\2\1\uffff\2\2\6\uffff\1\2\7\uffff\1\2\2\uffff\1\3\1\uffff"+
            "\1\2\20\uffff\1\1\1\uffff\3\2\7\uffff\1\2\10\uffff\3\2",
            "\1\2\1\uffff\1\2\2\uffff\4\2\1\uffff\4\2\1\uffff\4\2\2\uffff"+
            "\1\6\1\uffff\1\5\3\uffff\1\4\14\uffff\1\2",
            "",
            "\1\10\1\uffff\2\10\6\uffff\1\10\7\uffff\1\10\3\uffff\1\7\1"+
            "\10\20\uffff\1\10\1\uffff\3\10\7\uffff\1\10\10\uffff\3\10",
            "",
            "\1\4\2\uffff\1\4\12\uffff\1\4\11\uffff\1\4\17\uffff\1\11\1"+
            "\uffff\2\4\1\13\7\uffff\1\2\10\uffff\1\14\1\15\1\12",
            "\1\20\1\uffff\1\23\1\34\6\uffff\1\24\7\uffff\1\25\3\uffff\1"+
            "\16\1\22\20\uffff\1\17\1\uffff\1\26\1\33\1\30\7\uffff\1\21\10"+
            "\uffff\1\31\1\32\1\27",
            "",
            "",
            "\1\35\2\4\12\uffff\1\4\15\uffff\1\4",
            "\1\36",
            "\1\40\2\4\44\uffff\1\37",
            "\1\41\22\uffff\1\42",
            "\1\41\22\uffff\1\42",
            "\1\4\1\uffff\1\4\2\uffff\4\4\1\uffff\4\4\1\uffff\4\4\2\uffff"+
            "\1\4\6\uffff\2\2\2\uffff\1\4\2\uffff\1\4\4\uffff\1\4\22\uffff"+
            "\1\4",
            "\1\45\1\43\1\uffff\1\44\27\uffff\1\2\1\uffff\1\2\1\uffff\6"+
            "\2",
            "\1\46\2\uffff\1\4\12\uffff\1\2\14\uffff\1\2\1\uffff\1\2\1\uffff"+
            "\6\2",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\50\20\uffff\1\51\1\52\1\47",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\53",
            "\1\54",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2\4\uffff"+
            "\1\55",
            "\1\56\22\uffff\1\57",
            "\1\56\22\uffff\1\57",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\2\1\uffff\1\2\2\uffff\4\2\1\uffff\4\2\1\uffff\4\2\2\uffff"+
            "\1\6\1\uffff\1\2\20\uffff\1\2",
            "\1\40\2\4",
            "\1\60",
            "\1\2\1\uffff\1\2\2\uffff\4\2\1\uffff\4\2\1\uffff\4\2\2\uffff"+
            "\1\6\1\uffff\1\2\20\uffff\1\2",
            "\1\40\2\4\44\uffff\1\61",
            "\1\62",
            "\1\63\3\uffff\1\66\7\uffff\1\64\10\uffff\1\67\1\70\1\65",
            "\1\4\1\uffff\2\4\6\uffff\1\4\7\uffff\1\4\4\uffff\1\4\20\uffff"+
            "\1\71\1\uffff\3\4\7\uffff\1\4\10\uffff\3\4",
            "\1\4\1\uffff\1\4\2\uffff\4\4\1\uffff\4\4\1\uffff\4\4\2\uffff"+
            "\1\4\6\uffff\2\2\2\uffff\1\4\2\uffff\1\4\4\uffff\1\4\22\uffff"+
            "\1\4",
            "\1\4\1\uffff\1\4\2\uffff\4\4\1\uffff\4\4\1\uffff\4\4\2\uffff"+
            "\1\4\12\uffff\1\4\2\uffff\1\4\1\2\3\uffff\1\4\22\uffff\1\4",
            "\1\72",
            "\1\74\46\uffff\1\73",
            "\1\75\22\uffff\1\76",
            "\1\75\22\uffff\1\76",
            "\1\102\7\uffff\1\103\7\uffff\1\104\4\uffff\1\101\20\uffff\1"+
            "\77\2\uffff\1\111\1\106\7\uffff\1\100\10\uffff\1\107\1\110\1"+
            "\105",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\112",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2\4\uffff"+
            "\1\113",
            "\1\114",
            "\1\40\2\4",
            "\1\115",
            "\1\40\2\4",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\122\46\uffff\1\121",
            "\1\123\22\uffff\1\124",
            "\1\123\22\uffff\1\124",
            "\1\45\1\4\1\uffff\1\44",
            "\1\74",
            "\1\125",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\74\46\uffff\1\126",
            "\1\127",
            "\1\131\1\132\1\uffff\1\130",
            "\1\131\1\132\1\uffff\1\130",
            "\1\134\20\uffff\1\135\1\136\1\133",
            "\1\131\1\132\1\uffff\1\130",
            "\1\131\1\132\1\uffff\1\130",
            "\1\131\1\132\1\uffff\1\130",
            "\1\137",
            "\1\131\2\uffff\1\130\45\uffff\1\140",
            "\1\141\22\uffff\1\142",
            "\1\141\22\uffff\1\142",
            "\1\131\2\uffff\1\130",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\143",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\40\2\4",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\122",
            "\1\144",
            "\1\4\1\43\1\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\122\46\uffff\1\145",
            "\1\146",
            "\1\74",
            "\1\147",
            "\1\74",
            "\1\153\7\uffff\1\154\7\uffff\1\155\4\uffff\1\152\20\uffff\1"+
            "\150\2\uffff\1\162\1\157\7\uffff\1\151\10\uffff\1\160\1\161"+
            "\1\156",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\163\3\uffff\1\166\7\uffff\1\164\10\uffff\1\167\1\170\1\165",
            "\1\171",
            "\1\173\46\uffff\1\172",
            "\1\175\22\uffff\1\174",
            "\1\175\22\uffff\1\174",
            "\1\131\2\uffff\1\130",
            "\1\176",
            "\1\131\2\uffff\1\130\45\uffff\1\177",
            "\1\u0080",
            "\1\4\2\uffff\1\4\27\uffff\1\2\1\uffff\1\2\1\uffff\6\2",
            "\1\122",
            "\1\u0081",
            "\1\122",
            "\1\74",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\u0084\20\uffff\1\u0085\1\u0086\1\u0083",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\u0087",
            "\1\131\2\uffff\1\130\45\uffff\1\u0088",
            "\1\u0089\22\uffff\1\u008a",
            "\1\u0089\22\uffff\1\u008a",
            "\1\131\2\uffff\1\130",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008f\46\uffff\1\u008e",
            "\1\u0090\22\uffff\1\u0091",
            "\1\u0090\22\uffff\1\u0091",
            "\1\173",
            "\1\u0092",
            "\1\131\1\132\1\uffff\1\130",
            "\1\u0093",
            "\1\173\46\uffff\1\u0094",
            "\1\131\2\uffff\1\130",
            "\1\u0095",
            "\1\131\2\uffff\1\130",
            "\1\122",
            "\1\u0096\3\uffff\1\u0099\7\uffff\1\u0097\10\uffff\1\u009a\1"+
            "\u009b\1\u0098",
            "\1\u009c",
            "\1\u009e\46\uffff\1\u009d",
            "\1\u009f\22\uffff\1\u00a0",
            "\1\u009f\22\uffff\1\u00a0",
            "\1\131\2\uffff\1\130",
            "\1\u00a1",
            "\1\131\2\uffff\1\130\45\uffff\1\u00a2",
            "\1\u00a3",
            "\1\131\1\132\1\uffff\1\130",
            "\1\131\1\132\1\uffff\1\130",
            "\1\u008f",
            "\1\u00a4",
            "\1\131\1\132\1\uffff\1\130",
            "\1\u008f\46\uffff\1\u00a5",
            "\1\u00a6",
            "\1\173",
            "\1\173",
            "\1\u00a7",
            "\1\131\2\uffff\1\130",
            "\1\u00a8",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ac\46\uffff\1\u00ab",
            "\1\u00ad\22\uffff\1\u00ae",
            "\1\u00ad\22\uffff\1\u00ae",
            "\1\u009e",
            "\1\u00af",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\u009e\46\uffff\1\u00b0",
            "\1\u00b1",
            "\1\131\2\uffff\1\130",
            "\1\u00b2",
            "\1\131\2\uffff\1\130",
            "\1\u008f",
            "\1\u00b3",
            "\1\u008f",
            "\1\173",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\u00ac",
            "\1\u00b4",
            "\1\131\1\u0082\1\uffff\1\130",
            "\1\u00ac\46\uffff\1\u00b5",
            "\1\u00b6",
            "\1\u009e",
            "\1\u00b7",
            "\1\u009e",
            "\1\131\2\uffff\1\130",
            "\1\u008f",
            "\1\u00ac",
            "\1\u00b8",
            "\1\u00ac",
            "\1\u009e",
            "\1\u00ac"
    };

    static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
    static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
    static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
    static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
    static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
    static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
    static final short[][] DFA28_transition;

    static {
        int numStates = DFA28_transitionS.length;
        DFA28_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
        }
    }

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = DFA28_eot;
            this.eof = DFA28_eof;
            this.min = DFA28_min;
            this.max = DFA28_max;
            this.accept = DFA28_accept;
            this.special = DFA28_special;
            this.transition = DFA28_transition;
        }
        public String getDescription() {
            return "285:1: callChainMember : ( value -> ^( PACK value ) | emptySet -> ^( PACK ) | valueList -> ^( PACK valueList ) | functionCallTarget );";
        }
    }
    static final String DFA38_eotS =
        "\47\uffff";
    static final String DFA38_eofS =
        "\1\2\46\uffff";
    static final String DFA38_minS =
        "\1\46\1\40\2\uffff\1\114\1\70\2\114\1\70\1\110\1\70\1\114\1\110"+
        "\1\114\1\70\1\110\2\uffff\3\70\1\114\1\70\2\114\1\70\1\114\1\70"+
        "\2\uffff\1\70\1\114\1\70\1\114\2\70\1\114\2\70";
    static final String DFA38_maxS =
        "\1\67\1\137\2\uffff\1\114\3\137\1\123\1\110\1\72\1\114\1\137\1\114"+
        "\1\137\1\110\2\uffff\1\105\1\72\1\105\1\114\3\137\1\72\1\114\1\105"+
        "\2\uffff\1\105\1\114\1\137\1\114\1\72\1\105\1\114\2\105";
    static final String DFA38_acceptS =
        "\2\uffff\1\6\1\5\14\uffff\1\2\1\4\12\uffff\1\1\1\3\11\uffff";
    static final String DFA38_specialS =
        "\47\uffff}>";
    static final String[] DFA38_transitionS = {
            "\1\2\1\uffff\1\2\2\uffff\3\2\1\uffff\1\2\1\uffff\1\2\3\uffff"+
            "\1\2\1\uffff\1\1",
            "\1\3\2\uffff\1\3\12\uffff\1\11\11\uffff\1\3\17\uffff\1\10\1"+
            "\uffff\2\3\1\5\20\uffff\1\6\1\7\1\4",
            "",
            "",
            "\1\12",
            "\2\3\1\14\44\uffff\1\13",
            "\1\16\22\uffff\1\15",
            "\1\16\22\uffff\1\15",
            "\2\20\1\17\12\uffff\1\21\15\uffff\1\3",
            "\1\22",
            "\2\3\1\14",
            "\1\23",
            "\1\24\3\uffff\1\26\20\uffff\1\27\1\30\1\25",
            "\1\31",
            "\2\3\1\14\44\uffff\1\32",
            "\1\33",
            "",
            "",
            "\2\20\13\uffff\1\21",
            "\2\3\1\14",
            "\2\34\13\uffff\1\35",
            "\1\36",
            "\2\34\13\uffff\1\35\31\uffff\1\37",
            "\1\40\22\uffff\1\41",
            "\1\40\22\uffff\1\41",
            "\2\3\1\14",
            "\1\42",
            "\2\34\13\uffff\1\35",
            "",
            "",
            "\2\34\13\uffff\1\35",
            "\1\43",
            "\2\34\13\uffff\1\35\31\uffff\1\44",
            "\1\45",
            "\2\3\1\14",
            "\2\34\13\uffff\1\35",
            "\1\46",
            "\2\34\13\uffff\1\35",
            "\2\34\13\uffff\1\35"
    };

    static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
    static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
    static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
    static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
    static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
    static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
    static final short[][] DFA38_transition;

    static {
        int numStates = DFA38_transitionS.length;
        DFA38_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
        }
    }

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = DFA38_eot;
            this.eof = DFA38_eof;
            this.min = DFA38_min;
            this.max = DFA38_max;
            this.accept = DFA38_accept;
            this.special = DFA38_special;
            this.transition = DFA38_transition;
        }
        public String getDescription() {
            return "347:1: specializer[RuleOpts opts] : ( ARG range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range ^( SPLIT BASIC PRE ID[(String) null] ) sepArgList ) | ARG split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) split sepArgList ) | ARG range SPLIT split[false] sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG split[true] SPLIT range sepArgList CLOSE_ARG {...}? -> ^( SPECIALIZER range split sepArgList ) | ARG argList CLOSE_ARG {...}? -> ^( SPECIALIZER ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) ^( SPLIT BASIC PRE ID[(String) null] ) argList ) | -> ^( SPECIALIZER DEFAULT ) );";
        }
    }
    static final String DFA40_eotS =
        "\u00f4\uffff";
    static final String DFA40_eofS =
        "\u00f4\uffff";
    static final String DFA40_minS =
        "\1\40\1\uffff\1\65\1\114\1\70\2\114\3\70\1\uffff\1\42\1\70\1\114"+
        "\1\40\1\uffff\1\70\1\114\2\66\1\114\3\66\1\114\1\66\2\114\1\66\1"+
        "\70\1\uffff\1\65\1\114\1\70\2\114\3\70\1\114\1\70\1\42\1\70\1\110"+
        "\1\114\1\70\2\114\1\66\1\114\1\66\1\114\1\42\1\70\2\114\2\70\2\66"+
        "\1\114\3\66\1\114\1\66\2\114\1\66\2\70\1\114\1\70\2\114\1\70\1\114"+
        "\1\66\1\70\1\114\1\66\1\114\3\66\1\114\3\66\1\114\1\66\2\114\1\66"+
        "\2\70\1\114\1\110\1\114\1\70\2\114\1\66\1\114\1\66\1\114\2\66\1"+
        "\70\1\114\1\66\1\70\1\114\1\70\1\114\1\70\1\66\1\42\1\70\1\110\1"+
        "\114\1\70\2\114\1\66\1\114\1\66\1\114\3\70\1\114\1\70\2\114\1\70"+
        "\1\114\1\66\1\114\1\70\1\66\1\114\1\66\1\70\1\114\2\70\2\66\1\114"+
        "\3\66\1\114\1\66\2\114\1\66\2\70\1\114\1\70\2\114\1\70\1\114\1\66"+
        "\1\114\1\70\1\66\1\114\3\66\1\70\1\114\1\66\1\70\1\114\2\70\1\114"+
        "\1\66\1\70\1\110\1\114\1\70\2\114\1\66\1\114\1\66\1\114\2\66\1\70"+
        "\1\114\1\66\1\70\1\114\2\70\1\114\1\66\1\70\1\114\4\70\1\114\1\70"+
        "\2\114\1\70\1\114\1\66\1\70\1\114\1\66\1\114\1\66\1\70\1\114\3\70"+
        "\2\66\1\70\1\114\1\66\1\70\1\114\1\70\1\114\1\70\1\66\2\70\1\114"+
        "\3\70";
    static final String DFA40_maxS =
        "\1\137\1\uffff\1\65\1\114\3\137\3\71\1\uffff\1\137\1\71\1\114\1"+
        "\137\1\uffff\1\137\1\114\2\71\1\137\3\71\1\114\3\137\2\71\1\uffff"+
        "\1\65\1\114\3\137\3\71\1\114\1\71\1\137\1\71\1\137\1\114\3\137\1"+
        "\71\1\114\1\137\1\114\1\137\1\71\2\114\1\137\3\71\1\137\3\71\1\114"+
        "\3\137\1\71\2\70\1\114\3\137\1\70\1\114\1\71\1\137\1\114\1\71\1"+
        "\114\3\71\1\137\3\71\1\114\3\137\3\71\1\114\1\137\1\114\3\137\1"+
        "\71\1\114\1\137\1\114\2\71\1\70\1\114\1\71\1\137\1\114\1\70\1\114"+
        "\1\70\1\71\1\137\1\71\1\137\1\114\3\137\1\71\1\114\1\137\1\114\1"+
        "\71\2\70\1\114\3\137\1\70\1\114\1\71\1\114\1\137\1\71\1\114\1\71"+
        "\1\70\1\114\2\70\2\71\1\137\3\71\1\114\3\137\1\71\2\70\1\114\3\137"+
        "\1\70\1\114\1\71\1\114\1\137\1\71\1\114\3\71\1\70\1\114\1\71\1\137"+
        "\1\114\2\70\1\114\1\71\1\70\1\137\1\114\3\137\1\71\1\114\1\137\1"+
        "\114\2\71\1\70\1\114\1\71\1\137\1\114\2\70\1\114\1\71\1\70\1\114"+
        "\4\70\1\114\3\137\1\70\1\114\1\71\1\137\1\114\1\71\1\114\1\71\1"+
        "\70\1\114\3\70\2\71\1\70\1\114\1\71\1\137\1\114\1\70\1\114\1\70"+
        "\1\71\2\70\1\114\3\70";
    static final String DFA40_acceptS =
        "\1\uffff\1\1\10\uffff\1\3\4\uffff\1\2\16\uffff\1\4\u00d5\uffff";
    static final String DFA40_specialS =
        "\u00f4\uffff}>";
    static final String[] DFA40_transitionS = {
            "\1\11\2\uffff\1\10\24\uffff\1\1\17\uffff\1\12\1\uffff\1\2\1"+
            "\7\1\4\20\uffff\1\5\1\6\1\3",
            "",
            "\1\13",
            "\1\14",
            "\1\17\1\16\45\uffff\1\15",
            "\1\20\22\uffff\1\21",
            "\1\20\22\uffff\1\21",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\17\1\16",
            "",
            "\1\25\7\uffff\1\26\7\uffff\1\27\4\uffff\1\24\20\uffff\1\22"+
            "\2\uffff\1\34\1\31\7\uffff\1\23\10\uffff\1\32\1\33\1\30",
            "\1\17\1\16",
            "\1\35",
            "\1\46\2\uffff\1\45\44\uffff\1\36\1\uffff\1\37\1\44\1\41\20"+
            "\uffff\1\42\1\43\1\40",
            "",
            "\1\17\1\16\45\uffff\1\47",
            "\1\50",
            "\1\52\1\53\1\uffff\1\51",
            "\1\52\1\53\1\uffff\1\51",
            "\1\55\20\uffff\1\56\1\57\1\54",
            "\1\52\1\53\1\uffff\1\51",
            "\1\52\1\53\1\uffff\1\51",
            "\1\52\1\53\1\uffff\1\51",
            "\1\60",
            "\1\52\2\uffff\1\51\45\uffff\1\61",
            "\1\62\22\uffff\1\63",
            "\1\62\22\uffff\1\63",
            "\1\52\2\uffff\1\51",
            "\1\17\1\16",
            "",
            "\1\64",
            "\1\65",
            "\1\17\1\16\45\uffff\1\66",
            "\1\70\22\uffff\1\67",
            "\1\70\22\uffff\1\67",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\71",
            "\1\17\1\16",
            "\1\75\7\uffff\1\76\7\uffff\1\77\4\uffff\1\74\20\uffff\1\72"+
            "\2\uffff\1\104\1\101\7\uffff\1\73\10\uffff\1\102\1\103\1\100",
            "\1\17\1\16",
            "\1\105\3\uffff\1\110\7\uffff\1\106\10\uffff\1\111\1\112\1\107",
            "\1\113",
            "\1\115\46\uffff\1\114",
            "\1\116\22\uffff\1\117",
            "\1\116\22\uffff\1\117",
            "\1\52\2\uffff\1\51",
            "\1\120",
            "\1\52\2\uffff\1\51\45\uffff\1\121",
            "\1\122",
            "\1\126\7\uffff\1\127\7\uffff\1\130\4\uffff\1\125\20\uffff\1"+
            "\123\2\uffff\1\135\1\132\7\uffff\1\124\10\uffff\1\133\1\134"+
            "\1\131",
            "\1\17\1\16",
            "\1\136",
            "\1\137",
            "\1\17\1\16\45\uffff\1\140",
            "\1\17\1\16",
            "\1\52\1\141\1\uffff\1\51",
            "\1\52\1\141\1\uffff\1\51",
            "\1\143\20\uffff\1\144\1\145\1\142",
            "\1\52\1\141\1\uffff\1\51",
            "\1\52\1\141\1\uffff\1\51",
            "\1\52\1\141\1\uffff\1\51",
            "\1\146",
            "\1\52\2\uffff\1\51\45\uffff\1\147",
            "\1\150\22\uffff\1\151",
            "\1\150\22\uffff\1\151",
            "\1\52\2\uffff\1\51",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\156\46\uffff\1\155",
            "\1\157\22\uffff\1\160",
            "\1\157\22\uffff\1\160",
            "\1\115",
            "\1\161",
            "\1\52\1\53\1\uffff\1\51",
            "\1\115\46\uffff\1\162",
            "\1\163",
            "\1\52\2\uffff\1\51",
            "\1\164",
            "\1\52\2\uffff\1\51",
            "\1\166\1\167\1\uffff\1\165",
            "\1\166\1\167\1\uffff\1\165",
            "\1\171\20\uffff\1\172\1\173\1\170",
            "\1\166\1\167\1\uffff\1\165",
            "\1\166\1\167\1\uffff\1\165",
            "\1\166\1\167\1\uffff\1\165",
            "\1\174",
            "\1\166\2\uffff\1\165\45\uffff\1\175",
            "\1\176\22\uffff\1\177",
            "\1\176\22\uffff\1\177",
            "\1\166\2\uffff\1\165",
            "\1\17\1\16",
            "\1\17\1\16",
            "\1\u0080",
            "\1\u0081\3\uffff\1\u0084\7\uffff\1\u0082\10\uffff\1\u0085\1"+
            "\u0086\1\u0083",
            "\1\u0087",
            "\1\u0089\46\uffff\1\u0088",
            "\1\u008b\22\uffff\1\u008a",
            "\1\u008b\22\uffff\1\u008a",
            "\1\52\2\uffff\1\51",
            "\1\u008c",
            "\1\52\2\uffff\1\51\45\uffff\1\u008d",
            "\1\u008e",
            "\1\52\1\53\1\uffff\1\51",
            "\1\52\1\53\1\uffff\1\51",
            "\1\156",
            "\1\u008f",
            "\1\52\1\53\1\uffff\1\51",
            "\1\156\46\uffff\1\u0090",
            "\1\u0091",
            "\1\115",
            "\1\u0092",
            "\1\115",
            "\1\52\2\uffff\1\51",
            "\1\u0096\7\uffff\1\u0097\7\uffff\1\u0098\4\uffff\1\u0095\20"+
            "\uffff\1\u0093\2\uffff\1\u009d\1\u009a\7\uffff\1\u0094\10\uffff"+
            "\1\u009b\1\u009c\1\u0099",
            "\1\17\1\16",
            "\1\u009e\3\uffff\1\u00a1\7\uffff\1\u009f\10\uffff\1\u00a2\1"+
            "\u00a3\1\u00a0",
            "\1\u00a4",
            "\1\u00a6\46\uffff\1\u00a5",
            "\1\u00a8\22\uffff\1\u00a7",
            "\1\u00a8\22\uffff\1\u00a7",
            "\1\166\2\uffff\1\165",
            "\1\u00a9",
            "\1\166\2\uffff\1\165\45\uffff\1\u00aa",
            "\1\u00ab",
            "\1\17\1\16",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00b0\46\uffff\1\u00af",
            "\1\u00b1\22\uffff\1\u00b2",
            "\1\u00b1\22\uffff\1\u00b2",
            "\1\u0089",
            "\1\u00b3",
            "\1\52\1\141\1\uffff\1\51",
            "\1\u00b4",
            "\1\u0089\46\uffff\1\u00b5",
            "\1\52\2\uffff\1\51",
            "\1\u00b6",
            "\1\52\2\uffff\1\51",
            "\1\156",
            "\1\u00b7",
            "\1\156",
            "\1\115",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\u00ba\20\uffff\1\u00bb\1\u00bc\1\u00b9",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\u00bd",
            "\1\166\2\uffff\1\165\45\uffff\1\u00be",
            "\1\u00bf\22\uffff\1\u00c0",
            "\1\u00bf\22\uffff\1\u00c0",
            "\1\166\2\uffff\1\165",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c5\46\uffff\1\u00c4",
            "\1\u00c6\22\uffff\1\u00c7",
            "\1\u00c6\22\uffff\1\u00c7",
            "\1\u00a6",
            "\1\u00c8",
            "\1\166\1\167\1\uffff\1\165",
            "\1\u00c9",
            "\1\u00a6\46\uffff\1\u00ca",
            "\1\166\2\uffff\1\165",
            "\1\u00cb",
            "\1\166\2\uffff\1\165",
            "\1\52\1\141\1\uffff\1\51",
            "\1\52\1\141\1\uffff\1\51",
            "\1\u00b0",
            "\1\u00cc",
            "\1\52\1\141\1\uffff\1\51",
            "\1\u00b0\46\uffff\1\u00cd",
            "\1\u00ce",
            "\1\u0089",
            "\1\u0089",
            "\1\u00cf",
            "\1\52\2\uffff\1\51",
            "\1\156",
            "\1\u00d0\3\uffff\1\u00d3\7\uffff\1\u00d1\10\uffff\1\u00d4\1"+
            "\u00d5\1\u00d2",
            "\1\u00d6",
            "\1\u00d8\46\uffff\1\u00d7",
            "\1\u00d9\22\uffff\1\u00da",
            "\1\u00d9\22\uffff\1\u00da",
            "\1\166\2\uffff\1\165",
            "\1\u00db",
            "\1\166\2\uffff\1\165\45\uffff\1\u00dc",
            "\1\u00dd",
            "\1\166\1\167\1\uffff\1\165",
            "\1\166\1\167\1\uffff\1\165",
            "\1\u00c5",
            "\1\u00de",
            "\1\166\1\167\1\uffff\1\165",
            "\1\u00c5\46\uffff\1\u00df",
            "\1\u00e0",
            "\1\u00a6",
            "\1\u00a6",
            "\1\u00e1",
            "\1\166\2\uffff\1\165",
            "\1\u00b0",
            "\1\u00e2",
            "\1\u00b0",
            "\1\u0089",
            "\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e7\46\uffff\1\u00e6",
            "\1\u00e8\22\uffff\1\u00e9",
            "\1\u00e8\22\uffff\1\u00e9",
            "\1\u00d8",
            "\1\u00ea",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\u00d8\46\uffff\1\u00eb",
            "\1\u00ec",
            "\1\166\2\uffff\1\165",
            "\1\u00ed",
            "\1\166\2\uffff\1\165",
            "\1\u00c5",
            "\1\u00ee",
            "\1\u00c5",
            "\1\u00a6",
            "\1\u00b0",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\u00e7",
            "\1\u00ef",
            "\1\166\1\u00b8\1\uffff\1\165",
            "\1\u00e7\46\uffff\1\u00f0",
            "\1\u00f1",
            "\1\u00d8",
            "\1\u00f2",
            "\1\u00d8",
            "\1\166\2\uffff\1\165",
            "\1\u00c5",
            "\1\u00e7",
            "\1\u00f3",
            "\1\u00e7",
            "\1\u00d8",
            "\1\u00e7"
    };

    static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
    static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
    static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
    static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
    static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
    static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
    static final short[][] DFA40_transition;

    static {
        int numStates = DFA40_transitionS.length;
        DFA40_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
        }
    }

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = DFA40_eot;
            this.eof = DFA40_eof;
            this.min = DFA40_min;
            this.max = DFA40_max;
            this.accept = DFA40_accept;
            this.special = DFA40_special;
            this.transition = DFA40_transition;
        }
        public String getDescription() {
            return "364:1: argList : ( -> ^( LIST[\"Values Arguments\"] ) ^( LIST[\"Map Arguments\"] ) | values -> values ^( LIST[\"Map Arguments\"] ) | mapList -> ^( LIST[\"Value Arguments\"] ) mapList | values SEPARATOR mapList );";
        }
    }
    static final String DFA46_eotS =
        "\21\uffff";
    static final String DFA46_eofS =
        "\21\uffff";
    static final String DFA46_minS =
        "\1\110\1\114\1\72\2\114\1\uffff\1\72\1\114\1\110\1\72\1\114\1\72"+
        "\2\uffff\1\114\2\72";
    static final String DFA46_maxS =
        "\1\137\1\114\3\137\1\uffff\1\72\1\114\2\137\1\114\1\72\2\uffff\1"+
        "\114\2\72";
    static final String DFA46_acceptS =
        "\5\uffff\1\3\6\uffff\1\2\1\1\3\uffff";
    static final String DFA46_specialS =
        "\21\uffff}>";
    static final String[] DFA46_transitionS = {
            "\1\5\3\uffff\1\2\20\uffff\1\3\1\4\1\1",
            "\1\6",
            "\1\10\44\uffff\1\7",
            "\1\11\22\uffff\1\12",
            "\1\11\22\uffff\1\12",
            "",
            "\1\10",
            "\1\13",
            "\1\14\3\uffff\1\15\20\uffff\3\15",
            "\1\10\44\uffff\1\16",
            "\1\17",
            "\1\10",
            "",
            "",
            "\1\20",
            "\1\10",
            "\1\10"
    };

    static final short[] DFA46_eot = DFA.unpackEncodedString(DFA46_eotS);
    static final short[] DFA46_eof = DFA.unpackEncodedString(DFA46_eofS);
    static final char[] DFA46_min = DFA.unpackEncodedStringToUnsignedChars(DFA46_minS);
    static final char[] DFA46_max = DFA.unpackEncodedStringToUnsignedChars(DFA46_maxS);
    static final short[] DFA46_accept = DFA.unpackEncodedString(DFA46_acceptS);
    static final short[] DFA46_special = DFA.unpackEncodedString(DFA46_specialS);
    static final short[][] DFA46_transition;

    static {
        int numStates = DFA46_transitionS.length;
        DFA46_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA46_transition[i] = DFA.unpackEncodedString(DFA46_transitionS[i]);
        }
    }

    class DFA46 extends DFA {

        public DFA46(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 46;
            this.eot = DFA46_eot;
            this.eof = DFA46_eof;
            this.min = DFA46_min;
            this.max = DFA46_max;
            this.accept = DFA46_accept;
            this.special = DFA46_special;
            this.transition = DFA46_transition;
        }
        public String getDescription() {
            return "392:1: range : ( number RANGE number -> ^( RANGE number number ) | number RANGE i= ID {...}? -> ^( RANGE number NUMBER[RANGE_END] ) | s= ID RANGE e= ID {...}? -> ^( RANGE NUMBER[RANGE_END] NUMBER[RANGE_END] ) );";
        }
    }
    static final String DFA51_eotS =
        "\26\uffff";
    static final String DFA51_eofS =
        "\1\uffff\2\7\1\uffff\3\7\10\uffff\1\7\6\uffff";
    static final String DFA51_minS =
        "\1\42\2\40\1\114\3\40\2\uffff\1\114\1\70\2\114\1\70\1\114\1\40\1"+
        "\70\1\114\1\70\1\114\2\70";
    static final String DFA51_maxS =
        "\1\124\2\132\1\137\3\132\2\uffff\1\114\3\137\1\70\1\114\1\132\1"+
        "\137\1\114\1\70\1\114\2\70";
    static final String DFA51_acceptS =
        "\7\uffff\1\1\1\2\15\uffff";
    static final String DFA51_specialS =
        "\26\uffff}>";
    static final String[] DFA51_transitionS = {
            "\1\4\7\uffff\1\5\7\uffff\1\6\4\uffff\1\3\20\uffff\1\1\13\uffff"+
            "\1\2",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "\1\12\20\uffff\1\13\1\14\1\11",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "",
            "",
            "\1\15",
            "\1\17\46\uffff\1\16",
            "\1\20\22\uffff\1\21",
            "\1\20\22\uffff\1\21",
            "\1\17",
            "\1\22",
            "\1\7\1\uffff\1\7\2\uffff\4\7\1\uffff\4\7\1\uffff\4\7\2\uffff"+
            "\2\7\1\10\1\uffff\1\7\16\uffff\1\7\10\uffff\1\7\1\uffff\1\7"+
            "\1\uffff\6\7",
            "\1\17\46\uffff\1\23",
            "\1\24",
            "\1\17",
            "\1\25",
            "\1\17",
            "\1\17"
    };

    static final short[] DFA51_eot = DFA.unpackEncodedString(DFA51_eotS);
    static final short[] DFA51_eof = DFA.unpackEncodedString(DFA51_eofS);
    static final char[] DFA51_min = DFA.unpackEncodedStringToUnsignedChars(DFA51_minS);
    static final char[] DFA51_max = DFA.unpackEncodedStringToUnsignedChars(DFA51_maxS);
    static final short[] DFA51_accept = DFA.unpackEncodedString(DFA51_acceptS);
    static final short[] DFA51_special = DFA.unpackEncodedString(DFA51_specialS);
    static final short[][] DFA51_transition;

    static {
        int numStates = DFA51_transitionS.length;
        DFA51_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA51_transition[i] = DFA.unpackEncodedString(DFA51_transitionS[i]);
        }
    }

    class DFA51 extends DFA {

        public DFA51(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 51;
            this.eot = DFA51_eot;
            this.eof = DFA51_eof;
            this.min = DFA51_min;
            this.max = DFA51_max;
            this.accept = DFA51_accept;
            this.special = DFA51_special;
            this.transition = DFA51_transition;
        }
        public String getDescription() {
            return "413:1: tupleRef : ( simpleRef | simpleRef ( qualifiedRef )+ -> ^( simpleRef ( qualifiedRef )+ ) );";
        }
    }
 

    public static final BitSet FOLLOW_imports_in_program637 = new BitSet(new long[]{0x0002FA1400000000L});
    public static final BitSet FOLLOW_externals_in_program640 = new BitSet(new long[]{0x0002F80400000000L});
    public static final BitSet FOLLOW_order_in_program642 = new BitSet(new long[]{0x0002B80400000000L});
    public static final BitSet FOLLOW_canvasLayer_in_program644 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_streamDef_in_program647 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_layerDef_in_program651 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_operatorDef_in_program655 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_pythonDef_in_program659 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_operatorTemplate_in_program663 = new BitSet(new long[]{0x0002B80000000002L});
    public static final BitSet FOLLOW_IMPORT_in_imports822 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_imports826 = new BitSet(new long[]{0x0088000000000002L});
    public static final BitSet FOLLOW_ARG_in_imports829 = new BitSet(new long[]{0x0100000900000000L,0x00000000E0001D00L});
    public static final BitSet FOLLOW_argList_in_imports833 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_imports835 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_AS_in_imports840 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_imports844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_order940 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_orderRef_in_order942 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_order945 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_orderRef_in_order947 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_ID_in_orderRef982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_orderRef997 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_orderRef999 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_orderRef1002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_orderRef1004 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_orderRef1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_externalStream_in_externals1026 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_externalStream1043 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_STREAM_in_externalStream1045 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_externalStream1049 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_externalStream1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CANVAS_in_canvasLayer1074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_canvasLayer1078 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_canvasProperties_in_canvasLayer1080 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_guideDef_in_canvasLayer1082 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_GUIDE_in_guideDef1134 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1138 = new BitSet(new long[]{0x0080004000000000L});
    public static final BitSet FOLLOW_specializer_in_guideDef1142 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_FROM_in_guideDef1145 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1149 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_guideDef1153 = new BitSet(new long[]{0x0025048400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_guideDef1155 = new BitSet(new long[]{0x0025048400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_specializer_in_canvasProperties1192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STREAM_in_streamDef1206 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_streamDef1210 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_streamDef1212 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_consumesBlock_in_streamDef1217 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_LAYER_in_layerDef1252 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_layerDef1256 = new BitSet(new long[]{0x0080004000000000L});
    public static final BitSet FOLLOW_implantationDef_in_layerDef1258 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_consumesBlock_in_layerDef1260 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_ARG_in_implantationDef1295 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_implantationDef1299 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_implantationDef1301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FROM_in_consumesBlock1330 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_consumesBlock1334 = new BitSet(new long[]{0x002504A400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_filterRule_in_consumesBlock1336 = new BitSet(new long[]{0x002504A400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_consumesBlock1339 = new BitSet(new long[]{0x0025048400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_FILTER_in_filterRule1379 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_rulePredicate_in_filterRule1381 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_DEFINE_in_filterRule1383 = new BitSet(new long[]{0x00A4040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_callChain_in_filterRule1385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_rulePredicate1409 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ALL_in_rulePredicate1411 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_rulePredicate1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_rulePredicate1436 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1438 = new BitSet(new long[]{0x0000000000000000L,0x0000000007EA0000L});
    public static final BitSet FOLLOW_booleanOp_in_rulePredicate1440 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1442 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_rulePredicate1445 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1447 = new BitSet(new long[]{0x0000000000000000L,0x0000000007EA0000L});
    public static final BitSet FOLLOW_booleanOp_in_rulePredicate1449 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_rulePredicate1451 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_rulePredicate1455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_operatorTemplate1489 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorTemplate1491 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorTemplate1495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorDef1514 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1519 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_operatorDef1521 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_YIELDS_in_operatorDef1524 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_operatorDef1526 = new BitSet(new long[]{0x0020000100000000L});
    public static final BitSet FOLLOW_operatorRule_in_operatorDef1529 = new BitSet(new long[]{0x0020000100000002L});
    public static final BitSet FOLLOW_OPERATOR_in_operatorDef1564 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1568 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_BASE_in_operatorDef1570 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_operatorDef1574 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_specializer_in_operatorDef1576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_operatorRule1620 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_GATE_in_operatorRule1622 = new BitSet(new long[]{0x0025048400000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_rule_in_operatorRule1624 = new BitSet(new long[]{0x0025048400000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_GROUP_in_predicate1656 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ALL_in_predicate1659 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_predicate1661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_predicate1685 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_predicate1687 = new BitSet(new long[]{0x0000000000000000L,0x0000000007EA0000L});
    public static final BitSet FOLLOW_booleanOp_in_predicate1689 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_predicate1691 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_predicate1694 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_predicate1696 = new BitSet(new long[]{0x0000000000000000L,0x0000000007EA0000L});
    public static final BitSet FOLLOW_booleanOp_in_predicate1698 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_predicate1700 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_predicate1704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_target_in_rule1742 = new BitSet(new long[]{0x3000000000000000L});
    public static final BitSet FOLLOW_DEFINE_in_rule1746 = new BitSet(new long[]{0x00A4040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_DYNAMIC_in_rule1750 = new BitSet(new long[]{0x00A4040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_callChain_in_rule1753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callChainMember_in_callChain1780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_callChainMember1797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptySet_in_callChainMember1811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueList_in_callChainMember1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCallTarget_in_callChainMember1837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_functionCallTarget1861 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000009L});
    public static final BitSet FOLLOW_passOp_in_functionCallTarget1863 = new BitSet(new long[]{0x00A4040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_callChainMember_in_functionCallTarget1867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_functionCallTarget1893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callName_in_functionCall1938 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_specializer_in_functionCall1941 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_valueList_in_functionCall1944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callName_in_functionCall1972 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_specializer_in_functionCall1975 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_emptySet_in_functionCall1978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_callName2012 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_NAMESPACE_in_callName2014 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_callName2018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_callName2066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GLYPH_in_target2117 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_target2127 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CANVAS_in_target2137 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_in_target2147 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VIEW_in_target2157 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_target2160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_target2167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PYTHON_in_pythonDef2228 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_ARG_in_pythonDef2230 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2234 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_pythonDef2236 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2240 = new BitSet(new long[]{0x0010000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_pythonBlock_in_pythonDef2242 = new BitSet(new long[]{0x0010000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_PYTHON_in_pythonDef2265 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonDef2269 = new BitSet(new long[]{0x0010000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_pythonBlock_in_pythonDef2271 = new BitSet(new long[]{0x0010000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_FACET_in_pythonBlock2300 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_pythonBlock2302 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_CODE_BLOCK_in_pythonBlock2304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_pythonBlock2341 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_FACET_in_pythonBlock2343 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_pythonBlock2347 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_pythonBlock2349 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_YIELDS_in_pythonBlock2352 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_tuple_in_pythonBlock2354 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_CODE_BLOCK_in_pythonBlock2357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations2390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAGGED_ID_in_annotation2433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2452 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001100L});
    public static final BitSet FOLLOW_range_in_specializer2454 = new BitSet(new long[]{0x0300000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2456 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2491 = new BitSet(new long[]{0x0000400000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2493 = new BitSet(new long[]{0x0300000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2496 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2530 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001100L});
    public static final BitSet FOLLOW_range_in_specializer2532 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_specializer2534 = new BitSet(new long[]{0x0000400000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2536 = new BitSet(new long[]{0x0300000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2539 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2566 = new BitSet(new long[]{0x0000400000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_split_in_specializer2568 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SPLIT_in_specializer2571 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001100L});
    public static final BitSet FOLLOW_range_in_specializer2573 = new BitSet(new long[]{0x0300000000000000L});
    public static final BitSet FOLLOW_sepArgList_in_specializer2575 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_specializer2601 = new BitSet(new long[]{0x0100000900000000L,0x00000000E0001D00L});
    public static final BitSet FOLLOW_argList_in_specializer2603 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_specializer2605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_sepArgList2662 = new BitSet(new long[]{0x0000000900000000L,0x00000000E0001D00L});
    public static final BitSet FOLLOW_argList_in_sepArgList2665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_values_in_argList2710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mapList_in_argList2725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_values_in_argList2740 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_argList2742 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_mapList_in_argList2745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_values2755 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_values2758 = new BitSet(new long[]{0x0000000900000000L,0x00000000E0001C00L});
    public static final BitSet FOLLOW_atom_in_values2760 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_mapEntry_in_mapList2782 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_SEPARATOR_in_mapList2785 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_mapEntry_in_mapList2787 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_ID_in_mapEntry2814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_mapEntry2816 = new BitSet(new long[]{0x0000000900000000L,0x00000000E0001C00L});
    public static final BitSet FOLLOW_atom_in_mapEntry2820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptySet_in_tuple2841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_tuple2859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_tuple2877 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_tuple2879 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_tuple2882 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_tuple2884 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_tuple2888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_emptySet2909 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_emptySet2912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_valueList2921 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_valueList2924 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_valueList2927 = new BitSet(new long[]{0x0084040D00000000L,0x00000000E0101D00L});
    public static final BitSet FOLLOW_value_in_valueList2930 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_valueList2934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_range2949 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range2951 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001000L});
    public static final BitSet FOLLOW_number_in_range2953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_range2973 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range2975 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_range2979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_range3008 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_RANGE_in_range3010 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_range3014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_split3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_split3098 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_split3100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleRef_in_value3158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_value3163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sigil_in_atom3171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_atom3175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_atom3179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_atom3183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_atom3187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleRef_in_tupleRef3199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleRef_in_tupleRef3205 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_qualifiedRef_in_tupleRef3207 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_ID_in_simpleRef3229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_simpleRef3244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_simpleRef3259 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001000L});
    public static final BitSet FOLLOW_number_in_simpleRef3261 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_simpleRef3263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CANVAS_in_simpleRef3279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_in_simpleRef3296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VIEW_in_simpleRef3313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_qualifiedRef3335 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_qualifiedRef3339 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_qualifiedRef3341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_qualifiedRef3356 = new BitSet(new long[]{0x0000000000000000L,0x00000000E0001000L});
    public static final BitSet FOLLOW_number_in_qualifiedRef3360 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_qualifiedRef3362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_qualifiedRef3377 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_qualifiedRef3379 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_CLOSE_ARG_in_qualifiedRef3381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAGGED_ID_in_sigil3399 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_sValueList_in_sigil3401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_sValueList3419 = new BitSet(new long[]{0x0084040400000000L,0x00000000E0101900L});
    public static final BitSet FOLLOW_sValue_in_sValueList3422 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_SEPARATOR_in_sValueList3425 = new BitSet(new long[]{0x0084040400000000L,0x00000000E0101900L});
    public static final BitSet FOLLOW_sValue_in_sValueList3428 = new BitSet(new long[]{0x0240000000000000L});
    public static final BitSet FOLLOW_CLOSE_GROUP_in_sValueList3432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleRef_in_sValue3442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_sValue3446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_sValue3450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_booleanOp3464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_booleanOp3479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_booleanOp3493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_booleanOp3508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_booleanOp3522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_booleanOp3537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_booleanOp3551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_booleanOp3565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_directYield_in_passOp3582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GUIDE_YIELD_in_passOp3588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_directYield3604 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_directYield3608 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_directYield3610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_YIELDS_in_directYield3623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doubleNum_in_number3642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_intNum_in_number3646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_intNum3661 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_94_in_intNum3667 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_intNum3672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIGITS_in_intNum3687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_doubleNum3706 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3725 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_95_in_doubleNum3727 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_doubleNum3747 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_94_in_doubleNum3753 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3758 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_95_in_doubleNum3760 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_doubleNum3780 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_94_in_doubleNum3786 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_95_in_doubleNum3789 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_DIGITS_in_doubleNum3793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionCall_in_synpred1_Stencil1853 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000009L});
    public static final BitSet FOLLOW_passOp_in_synpred1_Stencil1855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_callName_in_synpred2_Stencil1922 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_specializer_in_synpred2_Stencil1925 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_valueList_in_synpred2_Stencil1928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PYTHON_in_synpred3_Stencil2221 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_ARG_in_synpred3_Stencil2223 = new BitSet(new long[]{0x0000000000000002L});

}