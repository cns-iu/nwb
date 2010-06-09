// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g 2009-11-27 15:00:27

	package stencil.parser.string;

	import stencil.adapters.Adapter;
	import stencil.display.DisplayLayer;
  import stencil.operator.wrappers.EncapsulationGenerator;
  import stencil.operator.*;
  import stencil.operator.module.*;
  import stencil.operator.module.util.*;    
  import stencil.operator.wrappers.*;
  import stencil.parser.tree.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class AdHocOperators extends TreeFilter {
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


        public AdHocOperators(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public AdHocOperators(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return AdHocOperators.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g"; }


    	protected MutableModule adHoc;
    	protected Adapter adapter;
    	EncapsulationGenerator encGenerator = new EncapsulationGenerator();
    	
    	public AdHocOperators(TreeNodeStream input, ModuleCache modules, Adapter adapter) {
    		super(input, new RecognizerSharedState());
    		assert modules != null : "Module cache must not be null.";
    		assert adapter != null : "Adapter must not be null.";
    		
    		this.adHoc = modules.getAdHoc();
    		this.adapter = adapter;		
    	}

    	protected void makeOperator(Operator op) {
    		DynamicStencilOperator operator = new SyntheticOperator(adHoc.getModuleData().getName(), op);
    		
    		adHoc.addOperator(operator);
    	}
    	
    	protected void transferProxy(OperatorProxy proxy) {
    	  adHoc.addOperator(proxy.getName(), proxy.getOperator(), proxy.getOperatorData()); 
    	}	
    	
    	protected void makePython(Python p) {
    		encGenerator.generate(p, adHoc);
    	}
    	
    	protected void makeLayer(Layer l) {
    		DisplayLayer dl =adapter.makeLayer(l); 
    		l.setDisplayLayer(dl);
    		
    		DisplayOperator operator = new DisplayOperator(dl);
    		adHoc.addOperator(operator, operator.getOperatorData(adHoc.getName()));
    	}

    	



    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:92:1: topdown : ( ^(r= OPERATOR ( . )* ) | ^(r= OPERATOR_PROXY ( . )* ) | ^(r= PYTHON ( . )* ) | ^(r= LAYER ( . )* ) );
    public final void topdown() throws RecognitionException {
        StencilTree r=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:93:2: ( ^(r= OPERATOR ( . )* ) | ^(r= OPERATOR_PROXY ( . )* ) | ^(r= PYTHON ( . )* ) | ^(r= LAYER ( . )* ) )
            int alt5=4;
            switch ( input.LA(1) ) {
            case OPERATOR:
                {
                alt5=1;
                }
                break;
            case OPERATOR_PROXY:
                {
                alt5=2;
                }
                break;
            case PYTHON:
                {
                alt5=3;
                }
                break;
            case LAYER:
                {
                alt5=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:93:4: ^(r= OPERATOR ( . )* )
                    {
                    r=(StencilTree)match(input,OPERATOR,FOLLOW_OPERATOR_in_topdown65); if (state.failed) return ;

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); if (state.failed) return ;
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:93:17: ( . )*
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
                        	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:93:17: .
                        	    {
                        	    matchAny(input); if (state.failed) return ;

                        	    }
                        	    break;

                        	default :
                        	    break loop1;
                            }
                        } while (true);


                        match(input, Token.UP, null); if (state.failed) return ;
                    }
                    if ( state.backtracking==1 ) {
                      makeOperator((Operator) r);
                    }

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:94:4: ^(r= OPERATOR_PROXY ( . )* )
                    {
                    r=(StencilTree)match(input,OPERATOR_PROXY,FOLLOW_OPERATOR_PROXY_in_topdown79); if (state.failed) return ;

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); if (state.failed) return ;
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:94:23: ( . )*
                        loop2:
                        do {
                            int alt2=2;
                            int LA2_0 = input.LA(1);

                            if ( ((LA2_0>=ANNOTATION && LA2_0<=95)) ) {
                                alt2=1;
                            }
                            else if ( (LA2_0==UP) ) {
                                alt2=2;
                            }


                            switch (alt2) {
                        	case 1 :
                        	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:94:23: .
                        	    {
                        	    matchAny(input); if (state.failed) return ;

                        	    }
                        	    break;

                        	default :
                        	    break loop2;
                            }
                        } while (true);


                        match(input, Token.UP, null); if (state.failed) return ;
                    }
                    if ( state.backtracking==1 ) {
                      transferProxy((OperatorProxy) r);
                    }

                    }
                    break;
                case 3 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:95:4: ^(r= PYTHON ( . )* )
                    {
                    r=(StencilTree)match(input,PYTHON,FOLLOW_PYTHON_in_topdown93); if (state.failed) return ;

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); if (state.failed) return ;
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:95:15: ( . )*
                        loop3:
                        do {
                            int alt3=2;
                            int LA3_0 = input.LA(1);

                            if ( ((LA3_0>=ANNOTATION && LA3_0<=95)) ) {
                                alt3=1;
                            }
                            else if ( (LA3_0==UP) ) {
                                alt3=2;
                            }


                            switch (alt3) {
                        	case 1 :
                        	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:95:15: .
                        	    {
                        	    matchAny(input); if (state.failed) return ;

                        	    }
                        	    break;

                        	default :
                        	    break loop3;
                            }
                        } while (true);


                        match(input, Token.UP, null); if (state.failed) return ;
                    }
                    if ( state.backtracking==1 ) {
                      makePython((Python) r);
                    }

                    }
                    break;
                case 4 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:96:4: ^(r= LAYER ( . )* )
                    {
                    r=(StencilTree)match(input,LAYER,FOLLOW_LAYER_in_topdown107); if (state.failed) return ;

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); if (state.failed) return ;
                        // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:96:14: ( . )*
                        loop4:
                        do {
                            int alt4=2;
                            int LA4_0 = input.LA(1);

                            if ( ((LA4_0>=ANNOTATION && LA4_0<=95)) ) {
                                alt4=1;
                            }
                            else if ( (LA4_0==UP) ) {
                                alt4=2;
                            }


                            switch (alt4) {
                        	case 1 :
                        	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/AdHocOperators.g:96:14: .
                        	    {
                        	    matchAny(input); if (state.failed) return ;

                        	    }
                        	    break;

                        	default :
                        	    break loop4;
                            }
                        } while (true);


                        match(input, Token.UP, null); if (state.failed) return ;
                    }
                    if ( state.backtracking==1 ) {
                      makeLayer((Layer) r);
                    }

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
        return ;
    }
    // $ANTLR end "topdown"

    // Delegated rules


 

    public static final BitSet FOLLOW_OPERATOR_in_topdown65 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_OPERATOR_PROXY_in_topdown79 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PYTHON_in_topdown93 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LAYER_in_topdown107 = new BitSet(new long[]{0x0000000000000004L});

}