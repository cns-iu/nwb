// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g 2009-11-27 15:00:59

	package stencil.parser.string;
	
	import stencil.parser.tree.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class TupleRefChain extends TreeRewriter {
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


        public TupleRefChain(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public TupleRefChain(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return TupleRefChain.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g"; }


    public static class topdown_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:47:1: topdown : ^( TUPLE_REF r= . chain ) -> ^( TUPLE_REF $r chain ) ;
    public final TupleRefChain.topdown_return topdown() throws RecognitionException {
        TupleRefChain.topdown_return retval = new TupleRefChain.topdown_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree TUPLE_REF1=null;
        CommonTree r=null;
        TupleRefChain.chain_return chain2 = null;


        CommonTree TUPLE_REF1_tree=null;
        CommonTree r_tree=null;
        RewriteRuleNodeStream stream_TUPLE_REF=new RewriteRuleNodeStream(adaptor,"token TUPLE_REF");
        RewriteRuleSubtreeStream stream_chain=new RewriteRuleSubtreeStream(adaptor,"rule chain");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:47:9: ( ^( TUPLE_REF r= . chain ) -> ^( TUPLE_REF $r chain ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:47:11: ^( TUPLE_REF r= . chain )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            TUPLE_REF1=(CommonTree)match(input,TUPLE_REF,FOLLOW_TUPLE_REF_in_topdown63); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_TUPLE_REF.add(TUPLE_REF1);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = TUPLE_REF1;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            r=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = r;
            _last = (CommonTree)input.LT(1);
            pushFollow(FOLLOW_chain_in_topdown69);
            chain2=chain();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==1 ) stream_chain.add(chain2.getTree());

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: TUPLE_REF, r, chain
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: r
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"wildcard r",r);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 47:35: -> ^( TUPLE_REF $r chain )
            {
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:47:38: ^( TUPLE_REF $r chain )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_TUPLE_REF.nextNode(), root_1);

                adaptor.addChild(root_1, stream_r.nextTree());
                adaptor.addChild(root_1, stream_chain.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
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

    public static class chain_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "chain"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:1: chain : ( ( ^( TUPLE_REF r= . ) chain ) -> ^( TUPLE_REF $r chain ) | ^( TUPLE_REF r= . ) -> ^( TUPLE_REF $r) );
    public final TupleRefChain.chain_return chain() throws RecognitionException {
        TupleRefChain.chain_return retval = new TupleRefChain.chain_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree TUPLE_REF3=null;
        CommonTree TUPLE_REF5=null;
        CommonTree r=null;
        TupleRefChain.chain_return chain4 = null;


        CommonTree TUPLE_REF3_tree=null;
        CommonTree TUPLE_REF5_tree=null;
        CommonTree r_tree=null;
        RewriteRuleNodeStream stream_TUPLE_REF=new RewriteRuleNodeStream(adaptor,"token TUPLE_REF");
        RewriteRuleSubtreeStream stream_chain=new RewriteRuleSubtreeStream(adaptor,"rule chain");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:6: ( ( ^( TUPLE_REF r= . ) chain ) -> ^( TUPLE_REF $r chain ) | ^( TUPLE_REF r= . ) -> ^( TUPLE_REF $r) )
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:8: ( ^( TUPLE_REF r= . ) chain )
                    {
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:8: ( ^( TUPLE_REF r= . ) chain )
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:9: ^( TUPLE_REF r= . ) chain
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    TUPLE_REF3=(CommonTree)match(input,TUPLE_REF,FOLLOW_TUPLE_REF_in_chain90); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_TUPLE_REF.add(TUPLE_REF3);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = TUPLE_REF3;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    r=(CommonTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = r;

                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }

                    _last = (CommonTree)input.LT(1);
                    pushFollow(FOLLOW_chain_in_chain97);
                    chain4=chain();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==1 ) stream_chain.add(chain4.getTree());

                    if ( state.backtracking==1 ) {
                    retval.tree = (CommonTree)_first_0;
                    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
                    }



                    // AST REWRITE
                    // elements: chain, TUPLE_REF, r
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: r
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"wildcard r",r);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 48:33: -> ^( TUPLE_REF $r chain )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:48:36: ^( TUPLE_REF $r chain )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_TUPLE_REF.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_r.nextTree());
                        adaptor.addChild(root_1, stream_chain.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:49:5: ^( TUPLE_REF r= . )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    TUPLE_REF5=(CommonTree)match(input,TUPLE_REF,FOLLOW_TUPLE_REF_in_chain116); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_TUPLE_REF.add(TUPLE_REF5);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = TUPLE_REF5;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    r=(CommonTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = r;

                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: r, TUPLE_REF
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: r
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"wildcard r",r);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 49:22: -> ^( TUPLE_REF $r)
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/TupleRefChain.g:49:25: ^( TUPLE_REF $r)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_TUPLE_REF.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_r.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
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
    // $ANTLR end "chain"

    // Delegated rules


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\12\uffff";
    static final String DFA1_eofS =
        "\12\uffff";
    static final String DFA1_minS =
        "\1\36\1\2\1\4\1\2\1\4\2\3\2\uffff\1\3";
    static final String DFA1_maxS =
        "\1\36\1\2\1\137\1\3\1\137\1\36\1\137\2\uffff\1\3";
    static final String DFA1_acceptS =
        "\7\uffff\1\2\1\1\1\uffff";
    static final String DFA1_specialS =
        "\12\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1",
            "\1\2",
            "\134\3",
            "\1\4\1\5",
            "\134\6",
            "\1\7\32\uffff\1\10",
            "\1\11\134\6",
            "",
            "",
            "\1\5"
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "48:1: chain : ( ( ^( TUPLE_REF r= . ) chain ) -> ^( TUPLE_REF $r chain ) | ^( TUPLE_REF r= . ) -> ^( TUPLE_REF $r) );";
        }
    }
 

    public static final BitSet FOLLOW_TUPLE_REF_in_topdown63 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_chain_in_topdown69 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TUPLE_REF_in_chain90 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_chain_in_chain97 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TUPLE_REF_in_chain116 = new BitSet(new long[]{0x0000000000000004L});

}