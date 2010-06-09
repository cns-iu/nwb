// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g 2009-11-27 15:01:15

	package stencil.interpreter;
	
	import java.util.Arrays;
	
	import stencil.util.AutoguidePair;
	import stencil.parser.tree.*;	
	import stencil.util.MultiPartName;
	import stencil.display.*;
	import stencil.operator.module.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**Operators over a properly formed-AST and ensures that
 * all guides are up-to date.
 */
@SuppressWarnings("all")
public class UpdateGuides extends TreeFilter {
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


        public UpdateGuides(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public UpdateGuides(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return UpdateGuides.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g"; }


    	private StencilPanel panel; //Panel to take elements from
    	
    	private ModuleCache cache;
    		
    	public void updateGuides(StencilPanel panel) {
    		this.panel = panel;
    				
    		downup(panel.getProgram().getCanvasDef().getGuides());
    	}
    	
    	//TODO: Remove when all tuple references are positional
    	public void setModuleCache(ModuleCache c) {this.cache = c;}

    	/**Update an actual guide on the current layer using the passed panel.*/
        private void update(String layerName, String attribute, List<Object[]> categories, List<Object[]> results) {
        	try {
           		List<AutoguidePair> pairs = zip(categories, results);
           		DisplayLayer l = panel.getLayer(layerName);
           		DisplayGuide g = l.getGuide(attribute);
           		g.setElements(pairs);
        	} catch (Exception e) {
        		throw new RuntimeException(String.format("Error creating guide for attribute %1$s", attribute), e);
        	}
       	}
    	
    	/**Turn a pair of lists into a list of AutoguidePairs.*/
        private List<AutoguidePair> zip(List<? extends Object[]> categories, List<? extends Object[]> results) {
    		assert categories.size() == results.size() : "Category and result lists must be of the same length";
    		AutoguidePair[] pairs = new AutoguidePair[categories.size()]; 
    		
    		for (int i=0; i < categories.size(); i++) {
       			pairs[i] = new AutoguidePair<Object, Object>(categories.get(i), results.get(i));
    		}
    		return Arrays.asList(pairs);	
    	}	

    	private final List<String> getPrototype(Function f) {
    		MultiPartName name = new MultiPartName(f.getName());
    		Specializer spec = f.getSpecializer();
    		try {
       			Module m = cache.findModuleForOperator(name.prefixedName()).module;
       			OperatorData ld = m.getOperatorData(name.getName(), spec);
       			FacetData fd = ld.getFacetData("Query");//TODO: This is not always query...we need to add guide facet data
       			assert fd.tupleFields() != null : "Unexpected null prototype tuple.";
       			return fd.tupleFields();
       		} catch (Exception e) {throw new RuntimeException("Error Specailizing", e);}
    	}
    	
    	private final List<Object[]> invokeGuide(Function f, List<Object[]> vals, List<String> prototype) {
    		return f.getOperator().guide(f.getArguments(), vals, prototype);
    	}
    	
      	private final List<Object[]> packGuide(Pack p, List<Object[]> vals, List<String> prototype) {
    		Object[][] results = new Object[vals.size()][];
    		
    		int i=0;
    		for (Object[] val: vals) {
    			results[i] = new Object[p.getArguments().size()];
    			int j=0;
    			Value arg = p.getArguments().get(j); //TODO: Really need to handle the case where chain is setting more than one value
    			
       	  if (arg instanceof TupleRef) {
       	    int idx = ((TupleRef) arg).toNumericRef(prototype);
            results[i][j] = val[idx]; 
       	  }	else {results[i][j] = arg.getValue();}
       			i++;
    		}
    		return Arrays.asList(results);
    	}



    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:1: topdown : ^(att= GUIDE layer= ID type= . spec= . rules= . callChain[$layer.text, $att.text] ) ;
    public final void topdown() throws RecognitionException {
        StencilTree att=null;
        StencilTree layer=null;
        StencilTree type=null;
        StencilTree spec=null;
        StencilTree rules=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:8: ( ^(att= GUIDE layer= ID type= . spec= . rules= . callChain[$layer.text, $att.text] ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:10: ^(att= GUIDE layer= ID type= . spec= . rules= . callChain[$layer.text, $att.text] )
            {
            att=(StencilTree)match(input,GUIDE,FOLLOW_GUIDE_in_topdown60); if (state.failed) return ;

            match(input, Token.DOWN, null); if (state.failed) return ;
            layer=(StencilTree)match(input,ID,FOLLOW_ID_in_topdown64); if (state.failed) return ;
            type=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return ;
            spec=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return ;
            rules=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return ;
            pushFollow(FOLLOW_callChain_in_topdown78);
            callChain((layer!=null?layer.getText():null), (att!=null?att.getText():null));

            state._fsp--;
            if (state.failed) return ;

            match(input, Token.UP, null); if (state.failed) return ;

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


    // $ANTLR start "callChain"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:1: callChain[String layerName, String att] : ^( CALL_CHAIN invoke[layerName, att] ) ;
    public final void callChain(String layerName, String att) throws RecognitionException {
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:40: ( ^( CALL_CHAIN invoke[layerName, att] ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:42: ^( CALL_CHAIN invoke[layerName, att] )
            {
            match(input,CALL_CHAIN,FOLLOW_CALL_CHAIN_in_callChain91); if (state.failed) return ;

            match(input, Token.DOWN, null); if (state.failed) return ;
            pushFollow(FOLLOW_invoke_in_callChain93);
            invoke(layerName, att);

            state._fsp--;
            if (state.failed) return ;

            match(input, Token.UP, null); if (state.failed) return ;

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
    // $ANTLR end "callChain"


    // $ANTLR start "invoke"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:127:1: invoke[String layerName, String att] : ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] ) ;
    public final void invoke(String layerName, String att) throws RecognitionException {
        StencilTree f=null;

        List<Object[]> inputs = null;
        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:129:2: ( ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:129:4: ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] )
            {
            f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_invoke112); if (state.failed) return ;

            if ( state.backtracking==1 ) {
              inputs = invokeGuide((Function) f, null, null);
            }

            match(input, Token.DOWN, null); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            pushFollow(FOLLOW_target_in_invoke122);
            target(layerName, att, inputs, inputs, getPrototype((Function) f));

            state._fsp--;
            if (state.failed) return ;

            match(input, Token.UP, null); if (state.failed) return ;

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
    // $ANTLR end "invoke"


    // $ANTLR start "target"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:131:1: target[String layerName, String att, List<Object[]> inputs, List<Object[]> vals, List<String> prototype] : ( ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] ) | ^(p= PACK . ) );
    public final void target(String layerName, String att, List<Object[]> inputs, List<Object[]> vals, List<String> prototype) throws RecognitionException {
        StencilTree f=null;
        StencilTree p=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:132:2: ( ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] ) | ^(p= PACK . ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==FUNCTION) ) {
                alt1=1;
            }
            else if ( (LA1_0==PACK) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:132:4: ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] )
                    {
                    f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_target140); if (state.failed) return ;

                    match(input, Token.DOWN, null); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    pushFollow(FOLLOW_target_in_target148);
                    target(layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f));

                    state._fsp--;
                    if (state.failed) return ;

                    match(input, Token.UP, null); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:133:4: ^(p= PACK . )
                    {
                    p=(StencilTree)match(input,PACK,FOLLOW_PACK_in_target158); if (state.failed) return ;

                    match(input, Token.DOWN, null); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;

                    match(input, Token.UP, null); if (state.failed) return ;
                    if ( state.backtracking==1 ) {
                      update(layerName, att, inputs, packGuide((Pack) p, vals, prototype));
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
    // $ANTLR end "target"

    // Delegated rules


 

    public static final BitSet FOLLOW_GUIDE_in_topdown60 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_topdown64 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x00000000FFFFFFFFL});
    public static final BitSet FOLLOW_callChain_in_topdown78 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CALL_CHAIN_in_callChain91 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_invoke_in_callChain93 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_invoke112 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_target_in_invoke122 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_target140 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_target_in_target148 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PACK_in_target158 = new BitSet(new long[]{0x0000000000000004L});

}