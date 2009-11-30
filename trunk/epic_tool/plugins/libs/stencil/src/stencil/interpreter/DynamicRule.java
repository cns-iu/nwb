// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g 2009-11-27 15:01:24

	/** Converts a rule into a rule suitable for dynamic bindings.
	 *
	 * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality	  
	 **/

	package stencil.interpreter;
		
	import stencil.util.MultiPartName;
	import stencil.parser.tree.Function;
	import stencil.parser.tree.StencilTreeAdapter;
	import stencil.parser.tree.Rule;
	import static stencil.parser.ParserConstants.QUERY_BLOCK_TAG;

	import java.util.Map;
	import java.util.HashMap;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class DynamicRule extends TreeRewriter {
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


        public DynamicRule(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public DynamicRule(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return DynamicRule.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g"; }


    	/**Tree adaptor for translation and duplication.**/
    	private static StencilTreeAdapter treeAdaptor = new StencilTreeAdapter(); 
    	
    	/**Cache of rules that the dynamic representation has already been constructed for.**/
    	private static Map<Rule, Rule> mapCache  = new HashMap<Rule, Rule>();
    	
    	/**Convert a rule to its dynamic for; this is the preferred method
    	  *for creating a dynamic rule.
    	  *
    	  *This method is memoized.
    	 **/
    	public static Rule toDynamic(Rule original) {
    		if (mapCache.containsKey(original)) {return mapCache.get(original);}
    	
    		Rule rule = (Rule) treeAdaptor.dupTree(original);
    		
    		CommonTreeNodeStream treeTokens = new CommonTreeNodeStream(rule);
    		DynamicRule dr = new DynamicRule(treeTokens);
    		dr.setTreeAdaptor(treeAdaptor);
    		rule = (Rule) dr.downup(rule);
    		
    		mapCache.put(original, rule);
    		return rule;
    	}
        
        
        private String queryName(String name) {return new MultiPartName(name).modSuffix(QUERY_BLOCK_TAG).toString();}       


    public static class topdown_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g:85:1: topdown : ^(f= FUNCTION spec= . args= . yield= . target= . ) -> ^( FUNCTION[queryName($f.getText())] $spec $args $yield $target) ;
    public final DynamicRule.topdown_return topdown() throws RecognitionException {
        DynamicRule.topdown_return retval = new DynamicRule.topdown_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree f=null;
        CommonTree spec=null;
        CommonTree args=null;
        CommonTree yield=null;
        CommonTree target=null;

        CommonTree f_tree=null;
        CommonTree spec_tree=null;
        CommonTree args_tree=null;
        CommonTree yield_tree=null;
        CommonTree target_tree=null;
        RewriteRuleNodeStream stream_FUNCTION=new RewriteRuleNodeStream(adaptor,"token FUNCTION");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g:87:2: ( ^(f= FUNCTION spec= . args= . yield= . target= . ) -> ^( FUNCTION[queryName($f.getText())] $spec $args $yield $target) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g:87:3: ^(f= FUNCTION spec= . args= . yield= . target= . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_topdown71); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_FUNCTION.add(f);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = f;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            spec=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = spec;
            _last = (CommonTree)input.LT(1);
            args=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = args;
            _last = (CommonTree)input.LT(1);
            yield=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = yield;
            _last = (CommonTree)input.LT(1);
            target=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = target;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: FUNCTION, spec, args, target, yield
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: yield, target, args, spec
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_yield=new RewriteRuleSubtreeStream(adaptor,"wildcard yield",yield);
            RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"wildcard target",target);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"wildcard args",args);
            RewriteRuleSubtreeStream stream_spec=new RewriteRuleSubtreeStream(adaptor,"wildcard spec",spec);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 87:48: -> ^( FUNCTION[queryName($f.getText())] $spec $args $yield $target)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/DynamicRule.g:87:51: ^( FUNCTION[queryName($f.getText())] $spec $args $yield $target)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, queryName(f.getText())), root_1);

                adaptor.addChild(root_1, stream_spec.nextTree());
                adaptor.addChild(root_1, stream_args.nextTree());
                adaptor.addChild(root_1, stream_yield.nextTree());
                adaptor.addChild(root_1, stream_target.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            input.replaceChildren(adaptor.getParent(retval.start),
                                  adaptor.getChildIndex(retval.start),
                                  adaptor.getChildIndex(_last),
                                  retval.tree);}
            }

            if ( state.backtracking==1 ) {
              ((Function) retval.tree).setOperator(((Function) f).getOperator());
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


 

    public static final BitSet FOLLOW_FUNCTION_in_topdown71 = new BitSet(new long[]{0x0000000000000004L});

}