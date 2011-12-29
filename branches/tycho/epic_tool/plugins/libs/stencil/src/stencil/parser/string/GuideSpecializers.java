// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g 2009-11-27 15:00:45

	/**  Make sure that every guide a specializer.
	 *
	 *
	 * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality	  
	 **/
	package stencil.parser.string;
	
	import stencil.parser.tree.*;
	import stencil.adapters.Adapter;
	import java.lang.reflect.Field;
	import java.util.Arrays;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class GuideSpecializers extends TreeRewriter {
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


        public GuideSpecializers(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public GuideSpecializers(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return GuideSpecializers.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g"; }


    	
    	public static final String DEFAULT_FIELD_NAME = "DEFAULT_ARGUMENTS";
    	protected Adapter adapter;
        
    	public GuideSpecializers(TreeNodeStream input, Adapter adapter) {
    		super(input, new RecognizerSharedState());
    		assert adapter != null : "ModuleCache must not be null.";
    		this.adapter = adapter;
    	}

    	public Specializer getDefault(String guideType) {
    		Class clss = adapter.getGuideClass(guideType);
    				    		
    		try {
    			Specializer defaultSpec;
    			
    			if (Arrays.asList(clss.getFields()).contains(DEFAULT_FIELD_NAME)) {
    				Field f = clss.getField(DEFAULT_FIELD_NAME);
        			defaultSpec = (Specializer) f.get(null);
    			} else {
    				defaultSpec = ParseStencil.parseSpecializer("[]");
    			}
    			
    			
        		return  (Specializer) adaptor.dupTree(defaultSpec);
        	} catch (Exception e) {
        		throw new RuntimeException("Error finding default specializer for guide " + guideType, e);
        	} 
    	}



    public static class topdown_return extends TreeRuleReturnScope {
        StencilTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g:88:1: topdown : ^( GUIDE type= ID ^( SPECIALIZER DEFAULT ) target= ID ) -> ^( GUIDE $type $target) ;
    public final GuideSpecializers.topdown_return topdown() throws RecognitionException {
        GuideSpecializers.topdown_return retval = new GuideSpecializers.topdown_return();
        retval.start = input.LT(1);

        StencilTree root_0 = null;

        StencilTree _first_0 = null;
        StencilTree _last = null;

        StencilTree type=null;
        StencilTree target=null;
        StencilTree GUIDE1=null;
        StencilTree SPECIALIZER2=null;
        StencilTree DEFAULT3=null;

        StencilTree type_tree=null;
        StencilTree target_tree=null;
        StencilTree GUIDE1_tree=null;
        StencilTree SPECIALIZER2_tree=null;
        StencilTree DEFAULT3_tree=null;
        RewriteRuleNodeStream stream_DEFAULT=new RewriteRuleNodeStream(adaptor,"token DEFAULT");
        RewriteRuleNodeStream stream_SPECIALIZER=new RewriteRuleNodeStream(adaptor,"token SPECIALIZER");
        RewriteRuleNodeStream stream_GUIDE=new RewriteRuleNodeStream(adaptor,"token GUIDE");
        RewriteRuleNodeStream stream_ID=new RewriteRuleNodeStream(adaptor,"token ID");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g:88:8: ( ^( GUIDE type= ID ^( SPECIALIZER DEFAULT ) target= ID ) -> ^( GUIDE $type $target) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g:88:11: ^( GUIDE type= ID ^( SPECIALIZER DEFAULT ) target= ID )
            {
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_1 = _last;
            StencilTree _first_1 = null;
            _last = (StencilTree)input.LT(1);
            GUIDE1=(StencilTree)match(input,GUIDE,FOLLOW_GUIDE_in_topdown67); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_GUIDE.add(GUIDE1);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = GUIDE1;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            type=(StencilTree)match(input,ID,FOLLOW_ID_in_topdown71); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_ID.add(type);

            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_2 = _last;
            StencilTree _first_2 = null;
            _last = (StencilTree)input.LT(1);
            SPECIALIZER2=(StencilTree)match(input,SPECIALIZER,FOLLOW_SPECIALIZER_in_topdown74); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_SPECIALIZER.add(SPECIALIZER2);


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = SPECIALIZER2;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            DEFAULT3=(StencilTree)match(input,DEFAULT,FOLLOW_DEFAULT_in_topdown76); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_DEFAULT.add(DEFAULT3);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }

            _last = (StencilTree)input.LT(1);
            target=(StencilTree)match(input,ID,FOLLOW_ID_in_topdown81); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_ID.add(target);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: type, GUIDE, target
            // token labels: type, target
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleNodeStream stream_type=new RewriteRuleNodeStream(adaptor,"token type",type);
            RewriteRuleNodeStream stream_target=new RewriteRuleNodeStream(adaptor,"token target",target);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (StencilTree)adaptor.nil();
            // 88:61: -> ^( GUIDE $type $target)
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/GuideSpecializers.g:88:64: ^( GUIDE $type $target)
                {
                StencilTree root_1 = (StencilTree)adaptor.nil();
                root_1 = (StencilTree)adaptor.becomeRoot(stream_GUIDE.nextNode(), root_1);

                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, getDefault(type.getText()));
                adaptor.addChild(root_1, stream_target.nextNode());

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


 

    public static final BitSet FOLLOW_GUIDE_in_topdown67 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_topdown71 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_SPECIALIZER_in_topdown74 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DEFAULT_in_topdown76 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ID_in_topdown81 = new BitSet(new long[]{0x0000000000000008L});

}