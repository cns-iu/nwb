// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g 2009-11-27 15:00:17

	/**  Make sure that every mapping operator has a specializer.
	 *	 Make sure that every guide declaration has a specializer.
	 *
	 *
	 * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality	  
	 **/
	package stencil.parser.string;
	
	import stencil.parser.tree.*;
	import stencil.util.MultiPartName;
	import stencil.operator.module.*;
	import stencil.operator.module.util.*;
	
  import static stencil.parser.ParserConstants.SIMPLE_SPECIALIZER;
	


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** Make sure sensible specializers are present on every mapping operator. 
 */
@SuppressWarnings("all")
public class DefaultSpecializers extends TreeRewriteSequence {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANNOTATION", "BOOLEAN_OP", "BASIC", "CONSUMES", "CALL_CHAIN", "DIRECT_YIELD", "FUNCTION", "LIST", "CANVAS_DEF", "NUMBER", "OPERATOR_INSTANCE", "OPERATOR_PROXY", "OPERATOR_REFERENCE", "OPERATOR_TEMPLATE", "OPERATOR_RULE", "OPERATOR_BASE", "POST", "PRE", "PREDICATE", "PROGRAM", "PACK", "PYTHON_FACET", "RULE", "SIGIL", "SPECIALIZER", "TUPLE_PROTOTYPE", "TUPLE_REF", "MAP_ENTRY", "ALL", "BASE", "CANVAS", "DEFAULT", "EXTERNAL", "FILTER", "FROM", "GLYPH", "GUIDE", "IMPORT", "LOCAL", "LAYER", "OPERATOR", "TEMPLATE", "ORDER", "PYTHON", "RETURN", "STREAM", "VIEW", "AS", "FACET", "GROUP", "CLOSE_GROUP", "ARG", "CLOSE_ARG", "SEPARATOR", "RANGE", "NAMESPACE", "DEFINE", "DYNAMIC", "ANIMATED", "ANIMATED_DYNAMIC", "YIELDS", "FEED", "GUIDE_FEED", "GUIDE_YIELD", "GATE", "SPLIT", "JOIN", "TAG", "ID", "CODE_BLOCK", "TAGGED_ID", "STRING", "DIGITS", "NESTED_BLOCK", "ESCAPE_SEQUENCE", "WS", "COMMENT", "'>'", "'Init'", "'='", "'_'", "'>='", "'<'", "'<='", "'!='", "'=~'", "'!~'", "'-['", "']>'", "'-'", "'+'", "'.'"
    };
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


        public DefaultSpecializers(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public DefaultSpecializers(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return DefaultSpecializers.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g"; }


    	protected ModuleCache modules;
        
    	public DefaultSpecializers(TreeNodeStream input, ModuleCache modules) {
    		super(input, new RecognizerSharedState());
    		assert modules != null : "ModuleCache must not be null.";
    		this.modules = modules;
    	}
    	
    	  //Ensure specializers for operator references (must be done before references can be resolved in AdHoc creation)
        public Object function(Object t) {
          fptr down = new fptr() {public Object rule() throws RecognitionException { return function(); }};
          fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
          return downup(t, down, up);
        }
        

        //Ensure specializers for all references before operator instantiation    
        public Object misc(Object t) {
          fptr down = new fptr() {public Object rule() throws RecognitionException { return misc(); }};
          fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
          return downup(t, down, up);
        }

      public Specializer defaultCanvasSpecailizer() {
        try {
          return ParseStencil.parseSpecializer("[BACKGROUND_COLOR=@color(WHITE)]"); //TODO: Move this default some else...
        } catch (Exception e) {throw new Error("Parse or pre-defined constant failed.", e);}
      }

    	public Specializer getDefault(String fullName) {
    		MultiPartName name= new MultiPartName(fullName);
    		ModuleData md;
    		try{
        		Module m = modules.findModuleForOperator(name.prefixedName()).module;
        		md = m.getModuleData();
    		} catch (Exception e) {
    			throw new RuntimeException("Error getting module information for operator " + fullName, e);
    		}
    		    		
    		try {
        		return  (Specializer) adaptor.dupTree(md.getDefaultSpecializer(name.getName()));
        	} catch (Exception e) {
        		throw new RuntimeException("Error finding default specializer for " + fullName, e);
        	} 
    	}



    public static class function_return extends TreeRuleReturnScope {
        StencilTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:108:1: function : ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . ) -> ^( FUNCTION $args $op $target) ;
    public final DefaultSpecializers.function_return function() throws RecognitionException {
        DefaultSpecializers.function_return retval = new DefaultSpecializers.function_return();
        retval.start = input.LT(1);

        StencilTree root_0 = null;

        StencilTree _first_0 = null;
        StencilTree _last = null;

        StencilTree f=null;
        StencilTree SPECIALIZER1=null;
        StencilTree DEFAULT2=null;
        StencilTree args=null;
        StencilTree op=null;
        StencilTree target=null;

        StencilTree f_tree=null;
        StencilTree SPECIALIZER1_tree=null;
        StencilTree DEFAULT2_tree=null;
        StencilTree args_tree=null;
        StencilTree op_tree=null;
        StencilTree target_tree=null;
        RewriteRuleNodeStream stream_DEFAULT=new RewriteRuleNodeStream(adaptor,"token DEFAULT");
        RewriteRuleNodeStream stream_FUNCTION=new RewriteRuleNodeStream(adaptor,"token FUNCTION");
        RewriteRuleNodeStream stream_SPECIALIZER=new RewriteRuleNodeStream(adaptor,"token SPECIALIZER");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:109:3: ( ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . ) -> ^( FUNCTION $args $op $target) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:109:5: ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . )
            {
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_1 = _last;
            StencilTree _first_1 = null;
            _last = (StencilTree)input.LT(1);
            f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_function80); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_FUNCTION.add(f);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = f;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_2 = _last;
            StencilTree _first_2 = null;
            _last = (StencilTree)input.LT(1);
            SPECIALIZER1=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_function83); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER1);


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = SPECIALIZER1;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            DEFAULT2=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_function85); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_DEFAULT.add(DEFAULT2);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }

            _last = (StencilTree)input.LT(1);
            args=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = args;
            _last = (StencilTree)input.LT(1);
            op=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = op;
            _last = (StencilTree)input.LT(1);
            target=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = target;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: op, FUNCTION, target, args
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: op, args, target
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_op=new RewriteRuleSubtreeStream(adaptor,"wildcard op",op);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"wildcard args",args);
            RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"wildcard target",target);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (StencilTree)adaptor.nil();
            // 110:7: -> ^( FUNCTION $args $op $target)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:110:10: ^( FUNCTION $args $op $target)
                {
                StencilTree root_1 = (StencilTree)adaptor.nil();
                root_1 = (StencilTree)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);

                adaptor.addChild(root_1, getDefault(f.getText()));
                adaptor.addChild(root_1, stream_args.nextTree());
                adaptor.addChild(root_1, stream_op.nextTree());
                adaptor.addChild(root_1, stream_target.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = (StencilTree)adaptor.rulePostProcessing(root_0);
            input.replaceChildren(adaptor.getParent(retval.start),
                                  adaptor.getChildIndex(retval.start),
                                  adaptor.getChildIndex(_last),
                                  retval.tree);}
            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "function"

    public static class misc_return extends TreeRuleReturnScope {
        StencilTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "misc"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:112:1: misc : ( ^( GUIDE layer= . type= . ^( SPECIALIZER DEFAULT ) rules= . ) -> ^( GUIDE $layer $type $rules) | ^( OPERATOR_REFERENCE base= . ^( SPECIALIZER DEFAULT ) ) -> ^( OPERATOR_REFERENCE $base) | ^( CANVAS_DEF ^( SPECIALIZER DEFAULT ) (rest= . )* ) -> ^( CANVAS_DEF $rest) );
    public final DefaultSpecializers.misc_return misc() throws RecognitionException {
        DefaultSpecializers.misc_return retval = new DefaultSpecializers.misc_return();
        retval.start = input.LT(1);

        StencilTree root_0 = null;

        StencilTree _first_0 = null;
        StencilTree _last = null;

        StencilTree GUIDE3=null;
        StencilTree SPECIALIZER4=null;
        StencilTree DEFAULT5=null;
        StencilTree OPERATOR_REFERENCE6=null;
        StencilTree SPECIALIZER7=null;
        StencilTree DEFAULT8=null;
        StencilTree CANVAS_DEF9=null;
        StencilTree SPECIALIZER10=null;
        StencilTree DEFAULT11=null;
        StencilTree layer=null;
        StencilTree type=null;
        StencilTree rules=null;
        StencilTree base=null;
        StencilTree rest=null;

        StencilTree GUIDE3_tree=null;
        StencilTree SPECIALIZER4_tree=null;
        StencilTree DEFAULT5_tree=null;
        StencilTree OPERATOR_REFERENCE6_tree=null;
        StencilTree SPECIALIZER7_tree=null;
        StencilTree DEFAULT8_tree=null;
        StencilTree CANVAS_DEF9_tree=null;
        StencilTree SPECIALIZER10_tree=null;
        StencilTree DEFAULT11_tree=null;
        StencilTree layer_tree=null;
        StencilTree type_tree=null;
        StencilTree rules_tree=null;
        StencilTree base_tree=null;
        StencilTree rest_tree=null;
        RewriteRuleNodeStream stream_OPERATOR_REFERENCE=new RewriteRuleNodeStream(adaptor,"token OPERATOR_REFERENCE");
        RewriteRuleNodeStream stream_DEFAULT=new RewriteRuleNodeStream(adaptor,"token DEFAULT");
        RewriteRuleNodeStream stream_CANVAS_DEF=new RewriteRuleNodeStream(adaptor,"token CANVAS_DEF");
        RewriteRuleNodeStream stream_SPECIALIZER=new RewriteRuleNodeStream(adaptor,"token SPECIALIZER");
        RewriteRuleNodeStream stream_GUIDE=new RewriteRuleNodeStream(adaptor,"token GUIDE");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:113:2: ( ^( GUIDE layer= . type= . ^( SPECIALIZER DEFAULT ) rules= . ) -> ^( GUIDE $layer $type $rules) | ^( OPERATOR_REFERENCE base= . ^( SPECIALIZER DEFAULT ) ) -> ^( OPERATOR_REFERENCE $base) | ^( CANVAS_DEF ^( SPECIALIZER DEFAULT ) (rest= . )* ) -> ^( CANVAS_DEF $rest) )
            int alt2=3;
            switch ( input.LA(1) ) {
            case GUIDE:
                {
                alt2=1;
                }
                break;
            case OPERATOR_REFERENCE:
                {
                alt2=2;
                }
                break;
            case CANVAS_DEF:
                {
                alt2=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:113:4: ^( GUIDE layer= . type= . ^( SPECIALIZER DEFAULT ) rules= . )
                    {
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_1 = _last;
                    StencilTree _first_1 = null;
                    _last = (StencilTree)input.LT(1);
                    GUIDE3=(StencilTree)match(input,GUIDE,FOLLOW_GUIDE_in_misc133); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_GUIDE.add(GUIDE3);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = GUIDE3;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    layer=(StencilTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = layer;
                    _last = (StencilTree)input.LT(1);
                    type=(StencilTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = type;
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_2 = _last;
                    StencilTree _first_2 = null;
                    _last = (StencilTree)input.LT(1);
                    SPECIALIZER4=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_misc144); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER4);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = SPECIALIZER4;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    DEFAULT5=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_misc146); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DEFAULT.add(DEFAULT5);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    _last = (StencilTree)input.LT(1);
                    rules=(StencilTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = rules;

                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: GUIDE, rules, type, layer
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: type, rules, layer
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"wildcard type",type);
                    RewriteRuleSubtreeStream stream_rules=new RewriteRuleSubtreeStream(adaptor,"wildcard rules",rules);
                    RewriteRuleSubtreeStream stream_layer=new RewriteRuleSubtreeStream(adaptor,"wildcard layer",layer);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (StencilTree)adaptor.nil();
                    // 114:7: -> ^( GUIDE $layer $type $rules)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:114:10: ^( GUIDE $layer $type $rules)
                        {
                        StencilTree root_1 = (StencilTree)adaptor.nil();
                        root_1 = (StencilTree)adaptor.becomeRoot(stream_GUIDE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_layer.nextTree());
                        adaptor.addChild(root_1, stream_type.nextTree());
                        adaptor.addChild(root_1, adaptor.dupTree(SIMPLE_SPECIALIZER));
                        adaptor.addChild(root_1, stream_rules.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (StencilTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:115:7: ^( OPERATOR_REFERENCE base= . ^( SPECIALIZER DEFAULT ) )
                    {
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_1 = _last;
                    StencilTree _first_1 = null;
                    _last = (StencilTree)input.LT(1);
                    OPERATOR_REFERENCE6=(StencilTree)match(input,OPERATOR_REFERENCE,FOLLOW_OPERATOR_REFERENCE_in_misc185); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_OPERATOR_REFERENCE.add(OPERATOR_REFERENCE6);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = OPERATOR_REFERENCE6;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    base=(StencilTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = base;
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_2 = _last;
                    StencilTree _first_2 = null;
                    _last = (StencilTree)input.LT(1);
                    SPECIALIZER7=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_misc192); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER7);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = SPECIALIZER7;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    DEFAULT8=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_misc194); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DEFAULT.add(DEFAULT8);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: OPERATOR_REFERENCE, base
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: base
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_base=new RewriteRuleSubtreeStream(adaptor,"wildcard base",base);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (StencilTree)adaptor.nil();
                    // 116:7: -> ^( OPERATOR_REFERENCE $base)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:116:10: ^( OPERATOR_REFERENCE $base)
                        {
                        StencilTree root_1 = (StencilTree)adaptor.nil();
                        root_1 = (StencilTree)adaptor.becomeRoot(stream_OPERATOR_REFERENCE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_base.nextTree());
                        adaptor.addChild(root_1, getDefault(base.getText()));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (StencilTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:117:6: ^( CANVAS_DEF ^( SPECIALIZER DEFAULT ) (rest= . )* )
                    {
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_1 = _last;
                    StencilTree _first_1 = null;
                    _last = (StencilTree)input.LT(1);
                    CANVAS_DEF9=(StencilTree)match(input,CANVAS_DEF,FOLLOW_CANVAS_DEF_in_misc222); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_CANVAS_DEF.add(CANVAS_DEF9);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = CANVAS_DEF9;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    {
                    StencilTree _save_last_2 = _last;
                    StencilTree _first_2 = null;
                    _last = (StencilTree)input.LT(1);
                    SPECIALIZER10=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_misc225); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER10);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = SPECIALIZER10;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (StencilTree)input.LT(1);
                    DEFAULT11=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_misc227); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DEFAULT.add(DEFAULT11);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:117:46: (rest= . )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0>=ANNOTATION && LA1_0<=95)) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:117:46: rest= .
                    	    {
                    	    _last = (StencilTree)input.LT(1);
                    	    rest=(StencilTree)input.LT(1);
                    	    matchAny(input); if (state.failed) return retval;
                    	     
                    	    if ( state.backtracking==1 )
                    	    if ( _first_1==null ) _first_1 = rest;

                    	    if ( state.backtracking==1 ) {
                    	    retval.tree = (StencilTree)_first_0;
                    	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                    	        retval.tree = (StencilTree)adaptor.getParent(retval.tree);}
                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: rest, CANVAS_DEF
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: rest
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_rest=new RewriteRuleSubtreeStream(adaptor,"wildcard rest",rest);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (StencilTree)adaptor.nil();
                    // 118:7: -> ^( CANVAS_DEF $rest)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DefaultSpecializers.g:118:10: ^( CANVAS_DEF $rest)
                        {
                        StencilTree root_1 = (StencilTree)adaptor.nil();
                        root_1 = (StencilTree)adaptor.becomeRoot(stream_CANVAS_DEF.nextNode(), root_1);

                        adaptor.addChild(root_1, defaultCanvasSpecailizer());
                        adaptor.addChild(root_1, stream_rest.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (StencilTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "misc"

    // Delegated rules


 

    public static final BitSet FOLLOW_FUNCTION_in_function80 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SPECIALIZER_in_function83 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_function85 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GUIDE_in_misc133 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SPECIALIZER_in_misc144 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_misc146 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_OPERATOR_REFERENCE_in_misc185 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SPECIALIZER_in_misc192 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_misc194 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CANVAS_DEF_in_misc222 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SPECIALIZER_in_misc225 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_misc227 = new BitSet(new long[]{0x0000000000000008L});

}