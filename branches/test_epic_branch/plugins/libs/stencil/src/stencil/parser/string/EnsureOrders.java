// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g 2009-11-27 15:00:22

package stencil.parser.string;

import stencil.parser.tree.*;



import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class EnsureOrders extends TreeRewriteSequence {
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


        public EnsureOrders(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public EnsureOrders(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return EnsureOrders.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g"; }


    	private Order newOrder;
    	private void addName(String name) {
    		adaptor.addChild(newOrder.getChild(0), adaptor.create(ID, name));
    	}
    	
    	public Program ensureOrder(Program p) {
    		//Prep new-order node
    		newOrder = (Order) adaptor.create(ORDER, "Order");
    		adaptor.addChild(newOrder, adaptor.create(LIST, "Streams"));
    		
    		//Gather Streams
    		fptr down =	new fptr() {public Object rule() throws RecognitionException { return gatherStreams(); }};
       	    fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
       	    downup(p, down, up);

    		//Ensure Orders
    		down =	new fptr() {public Object rule() throws RecognitionException { return fixOrder(); }};
       	    up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
    		return (Program) downup(p, down, up);
    	}
    		



    public static class gatherStreams_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "gatherStreams"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:73:1: gatherStreams : ^(e= EXTERNAL ( . )* ) ;
    public final EnsureOrders.gatherStreams_return gatherStreams() throws RecognitionException {
        EnsureOrders.gatherStreams_return retval = new EnsureOrders.gatherStreams_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree e=null;
        CommonTree wildcard1=null;

        CommonTree e_tree=null;
        CommonTree wildcard1_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:74:2: ( ^(e= EXTERNAL ( . )* ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:74:4: ^(e= EXTERNAL ( . )* )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            e=(CommonTree)match(input,EXTERNAL,FOLLOW_EXTERNAL_in_gatherStreams79); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = e;
            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return retval;
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:74:17: ( . )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( ((LA1_0>=ANNOTATION && LA1_0<=95)) ) {
                        alt1=1;
                    }
                    else if ( (LA1_0==UP) ) {
                        alt1=2;
                    }


                    switch (alt1) {
                	case 1 :
                	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:74:17: .
                	    {
                	    _last = (CommonTree)input.LT(1);
                	    wildcard1=(CommonTree)input.LT(1);
                	    matchAny(input); if (state.failed) return retval;
                	     
                	    if ( state.backtracking==1 )
                	    if ( _first_1==null ) _first_1 = wildcard1;

                	    if ( state.backtracking==1 ) {
                	    retval.tree = (CommonTree)_first_0;
                	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                	        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
                	    }
                	    break;

                	default :
                	    break loop1;
                    }
                } while (true);


                match(input, Token.UP, null); if (state.failed) return retval;
            }_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {
              addName((e!=null?e.getText():null));
            }

            if ( state.backtracking==1 ) {
            retval.tree = (CommonTree)_first_0;
            if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
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
    // $ANTLR end "gatherStreams"

    public static class fixOrder_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fixOrder"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:76:1: fixOrder : ( ORDER -> | ^( ORDER ( orderRef )+ ) -> ^( ORDER ( orderRef )+ ) );
    public final EnsureOrders.fixOrder_return fixOrder() throws RecognitionException {
        EnsureOrders.fixOrder_return retval = new EnsureOrders.fixOrder_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree ORDER2=null;
        CommonTree ORDER3=null;
        EnsureOrders.orderRef_return orderRef4 = null;


        CommonTree ORDER2_tree=null;
        CommonTree ORDER3_tree=null;
        RewriteRuleNodeStream stream_ORDER=new RewriteRuleNodeStream(adaptor,"token ORDER");
        RewriteRuleSubtreeStream stream_orderRef=new RewriteRuleSubtreeStream(adaptor,"rule orderRef");
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:77:2: ( ORDER -> | ^( ORDER ( orderRef )+ ) -> ^( ORDER ( orderRef )+ ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==ORDER) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==DOWN) ) {
                    alt3=2;
                }
                else if ( (LA3_1==EOF) ) {
                    alt3=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:77:4: ORDER
                    {
                    _last = (CommonTree)input.LT(1);
                    ORDER2=(CommonTree)match(input,ORDER,FOLLOW_ORDER_in_fixOrder95); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_ORDER.add(ORDER2);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 77:10: ->
                    {
                        adaptor.addChild(root_0, newOrder);

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:78:4: ^( ORDER ( orderRef )+ )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    ORDER3=(CommonTree)match(input,ORDER,FOLLOW_ORDER_in_fixOrder105); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_ORDER.add(ORDER3);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = ORDER3;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:78:12: ( orderRef )+
                    int cnt2=0;
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==LIST) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:78:12: orderRef
                    	    {
                    	    _last = (CommonTree)input.LT(1);
                    	    pushFollow(FOLLOW_orderRef_in_fixOrder107);
                    	    orderRef4=orderRef();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==1 ) stream_orderRef.add(orderRef4.getTree());

                    	    if ( state.backtracking==1 ) {
                    	    retval.tree = (CommonTree)_first_0;
                    	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                    	        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
                    	    }
                    	    break;

                    	default :
                    	    if ( cnt2 >= 1 ) break loop2;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(2, input);
                                throw eee;
                        }
                        cnt2++;
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: orderRef, ORDER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 78:23: -> ^( ORDER ( orderRef )+ )
                    {
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:78:26: ^( ORDER ( orderRef )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_ORDER.nextNode(), root_1);

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
    // $ANTLR end "fixOrder"

    public static class orderRef_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orderRef"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:80:1: orderRef : ^( LIST ( ID )+ ) ;
    public final EnsureOrders.orderRef_return orderRef() throws RecognitionException {
        EnsureOrders.orderRef_return retval = new EnsureOrders.orderRef_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree LIST5=null;
        CommonTree ID6=null;

        CommonTree LIST5_tree=null;
        CommonTree ID6_tree=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:80:9: ( ^( LIST ( ID )+ ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:80:12: ^( LIST ( ID )+ )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            LIST5=(CommonTree)match(input,LIST,FOLLOW_LIST_in_orderRef128); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = LIST5;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:80:19: ( ID )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==ID) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/EnsureOrders.g:80:19: ID
            	    {
            	    _last = (CommonTree)input.LT(1);
            	    ID6=(CommonTree)match(input,ID,FOLLOW_ID_in_orderRef130); if (state.failed) return retval;
            	     
            	    if ( state.backtracking==1 )
            	    if ( _first_1==null ) _first_1 = ID6;

            	    if ( state.backtracking==1 ) {
            	    retval.tree = (CommonTree)_first_0;
            	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
            	        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }


            if ( state.backtracking==1 ) {
            retval.tree = (CommonTree)_first_0;
            if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
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
    // $ANTLR end "orderRef"

    // Delegated rules


 

    public static final BitSet FOLLOW_EXTERNAL_in_gatherStreams79 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ORDER_in_fixOrder95 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_fixOrder105 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_orderRef_in_fixOrder107 = new BitSet(new long[]{0x0000000000000808L});
    public static final BitSet FOLLOW_LIST_in_orderRef128 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_orderRef130 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000100L});

}