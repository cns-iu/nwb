// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g 2009-10-20 11:27:58

	/**  Make sure that every mapping operator has a specializer.
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

@SuppressWarnings("all")
public class Specializers extends TreeRewriter {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANNOTATION", "BOOLEAN_OP", "BASIC", "CONSUMES", "CALL_GROUP", "CALL_CHAIN", "FUNCTION", "LIST", "CANVAS_DEF", "NUMBER", "OPERATOR_INSTANCE", "OPERATOR_PROXY", "OPERATOR_REFERENCE", "OPERATOR_TEMPLATE", "OPERATOR_RULE", "OPERATOR_BASE", "POST", "PRE", "PREDICATE", "PROGRAM", "PACK", "PYTHON_FACET", "RULE", "SIGIL", "SPECIALIZER", "TUPLE_PROTOTYPE", "TUPLE_REF", "MAP_ENTRY", "ALL", "BASE", "CANVAS", "COLOR", "DEFAULT", "EXTERNAL", "FILTER", "FROM", "GLYPH", "GUIDE", "IMPORT", "LOCAL", "LAYER", "OPERATOR", "TEMPLATE", "ORDER", "PYTHON", "RETURN", "STREAM", "VIEW", "AS", "FACET", "GROUP", "CLOSE_GROUP", "ARG", "CLOSE_ARG", "SEPARATOR", "RANGE", "NAMESPACE", "NAMESPLIT", "DEFINE", "DYNAMIC", "YIELDS", "FEED", "GUIDE_FEED", "GUIDE_YIELD", "GATE", "SPLIT", "JOIN", "TAG", "ID", "CODE_BLOCK", "TAGGED_ID", "STRING", "DIGITS", "NESTED_BLOCK", "ESCAPE_SEQUENCE", "WS", "COMMENT", "'>'", "'Init'", "'='", "'n'", "'_'", "'>='", "'<'", "'<='", "'!='", "'=~'", "'!~'", "'-'", "'+'"
    };
    public static final int PRE=21;
    public static final int CLOSE_GROUP=55;
    public static final int AS=52;
    public static final int CALL_GROUP=8;
    public static final int NUMBER=13;
    public static final int TEMPLATE=46;
    public static final int FACET=53;
    public static final int NAMESPACE=60;
    public static final int VIEW=51;
    public static final int IMPORT=42;
    public static final int TUPLE_REF=30;
    public static final int PREDICATE=22;
    public static final int POST=20;
    public static final int ORDER=47;
    public static final int BASIC=6;
    public static final int OPERATOR_BASE=19;
    public static final int LOCAL=43;
    public static final int FUNCTION=10;
    public static final int YIELDS=64;
    public static final int TAG=71;
    public static final int CANVAS=34;
    public static final int RANGE=59;
    public static final int SIGIL=27;
    public static final int SPLIT=69;
    public static final int FILTER=38;
    public static final int T__89=89;
    public static final int WS=79;
    public static final int STRING=75;
    public static final int NAMESPLIT=61;
    public static final int T__92=92;
    public static final int T__88=88;
    public static final int T__90=90;
    public static final int OPERATOR_REFERENCE=16;
    public static final int CANVAS_DEF=12;
    public static final int T__91=91;
    public static final int OPERATOR_TEMPLATE=17;
    public static final int CONSUMES=7;
    public static final int DYNAMIC=63;
    public static final int PYTHON=48;
    public static final int T__85=85;
    public static final int ANNOTATION=4;
    public static final int FEED=65;
    public static final int BOOLEAN_OP=5;
    public static final int ALL=32;
    public static final int T__93=93;
    public static final int T__86=86;
    public static final int MAP_ENTRY=31;
    public static final int EXTERNAL=37;
    public static final int LIST=11;
    public static final int GLYPH=40;
    public static final int OPERATOR_PROXY=15;
    public static final int GATE=68;
    public static final int RULE=26;
    public static final int PACK=24;
    public static final int TUPLE_PROTOTYPE=29;
    public static final int PROGRAM=23;
    public static final int T__87=87;
    public static final int GUIDE_FEED=66;
    public static final int OPERATOR_INSTANCE=14;
    public static final int GUIDE=41;
    public static final int LAYER=44;
    public static final int GROUP=54;
    public static final int NESTED_BLOCK=77;
    public static final int OPERATOR=45;
    public static final int JOIN=70;
    public static final int PYTHON_FACET=25;
    public static final int BASE=33;
    public static final int FROM=39;
    public static final int ID=72;
    public static final int STREAM=50;
    public static final int DIGITS=76;
    public static final int OPERATOR_RULE=18;
    public static final int TAGGED_ID=74;
    public static final int CODE_BLOCK=73;
    public static final int CALL_CHAIN=9;
    public static final int COMMENT=80;
    public static final int SPECIALIZER=28;
    public static final int CLOSE_ARG=57;
    public static final int GUIDE_YIELD=67;
    public static final int T__84=84;
    public static final int SEPARATOR=58;
    public static final int DEFINE=62;
    public static final int RETURN=49;
    public static final int ESCAPE_SEQUENCE=78;
    public static final int ARG=56;
    public static final int EOF=-1;
    public static final int COLOR=35;
    public static final int T__82=82;
    public static final int DEFAULT=36;
    public static final int T__81=81;
    public static final int T__83=83;

    // delegates
    // delegators


        public Specializers(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public Specializers(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return Specializers.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g"; }


    	protected ModuleCache modules;
        
    	public Specializers(TreeNodeStream input, ModuleCache modules) {
    		super(input, new RecognizerSharedState());
    		assert modules != null : "ModuleCache must not be null.";
    		this.modules = modules;
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



    public static class topdown_return extends TreeRuleReturnScope {
        StencilTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g:86:1: topdown : ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . ) -> ^( FUNCTION $args $op $target) ;
    public final Specializers.topdown_return topdown() throws RecognitionException {
        Specializers.topdown_return retval = new Specializers.topdown_return();
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
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g:86:8: ( ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . ) -> ^( FUNCTION $args $op $target) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g:86:10: ^(f= FUNCTION ^( SPECIALIZER DEFAULT ) args= . op= . target= . )
            {
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_1 = _last;
            StencilTree _first_1 = null;
            _last = (StencilTree)input.LT(1);
            f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_topdown68); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_FUNCTION.add(f);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = f;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_2 = _last;
            StencilTree _first_2 = null;
            _last = (StencilTree)input.LT(1);
            SPECIALIZER1=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_topdown71); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER1);


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = SPECIALIZER1;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            DEFAULT2=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_topdown73); if (state.failed) return retval; 
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
            // elements: FUNCTION, op, args, target
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: op, target, args
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_op=new RewriteRuleSubtreeStream(adaptor,"wildcard op",op);
            RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"wildcard target",target);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"wildcard args",args);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (StencilTree)adaptor.nil();
            // 86:68: -> ^( FUNCTION $args $op $target)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/Specializers.g:86:71: ^( FUNCTION $args $op $target)
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
    // $ANTLR end "topdown"

    // Delegated rules


 

    public static final BitSet FOLLOW_FUNCTION_in_topdown68 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SPECIALIZER_in_topdown71 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_topdown73 = new BitSet(new long[]{0x0000000000000008L});

}