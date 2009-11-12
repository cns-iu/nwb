// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g 2009-10-23 23:25:18

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
    public static final int STREAM=50;
    public static final int COMMENT=80;
    public static final int GATE=68;
    public static final int OPERATOR_PROXY=15;
    public static final int GLYPH=40;
    public static final int MAP_ENTRY=31;
    public static final int T__81=81;
    public static final int VIEW=51;
    public static final int T__82=82;
    public static final int RULE=26;
    public static final int CLOSE_GROUP=55;
    public static final int T__83=83;
    public static final int NUMBER=13;
    public static final int OPERATOR_INSTANCE=14;
    public static final int NAMESPLIT=61;
    public static final int LOCAL=43;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int LIST=11;
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


        public UpdateGuides(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public UpdateGuides(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return UpdateGuides.tokenNames; }
    public String getGrammarFileName() { return "/Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g"; }


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
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:1: topdown : ^(att= GUIDE layer= ID type= . spec= . rules= . callGroup[$layer.text, $att.text] ) ;
    public final void topdown() throws RecognitionException {
        StencilTree att=null;
        StencilTree layer=null;
        StencilTree type=null;
        StencilTree spec=null;
        StencilTree rules=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:8: ( ^(att= GUIDE layer= ID type= . spec= . rules= . callGroup[$layer.text, $att.text] ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:124:10: ^(att= GUIDE layer= ID type= . spec= . rules= . callGroup[$layer.text, $att.text] )
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
            pushFollow(FOLLOW_callGroup_in_topdown78);
            callGroup((layer!=null?layer.getText():null), (att!=null?att.getText():null));

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


    // $ANTLR start "callGroup"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:1: callGroup[String layerName, String att] : ^( CALL_GROUP callChain[layerName, att] ) ;
    public final void callGroup(String layerName, String att) throws RecognitionException {
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:40: ( ^( CALL_GROUP callChain[layerName, att] ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:126:42: ^( CALL_GROUP callChain[layerName, att] )
            {
            match(input,CALL_GROUP,FOLLOW_CALL_GROUP_in_callGroup91); if (state.failed) return ;

            match(input, Token.DOWN, null); if (state.failed) return ;
            pushFollow(FOLLOW_callChain_in_callGroup93);
            callChain(layerName, att);

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
    // $ANTLR end "callGroup"


    // $ANTLR start "callChain"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:127:1: callChain[String layerName, String att] : ^( CALL_CHAIN invoke[layerName, att] ) ;
    public final void callChain(String layerName, String att) throws RecognitionException {
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:127:40: ( ^( CALL_CHAIN invoke[layerName, att] ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:127:42: ^( CALL_CHAIN invoke[layerName, att] )
            {
            match(input,CALL_CHAIN,FOLLOW_CALL_CHAIN_in_callChain103); if (state.failed) return ;

            match(input, Token.DOWN, null); if (state.failed) return ;
            pushFollow(FOLLOW_invoke_in_callChain105);
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
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:128:1: invoke[String layerName, String att] : ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] ) ;
    public final void invoke(String layerName, String att) throws RecognitionException {
        StencilTree f=null;

        List<Object[]> inputs = null;
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:130:2: ( ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:130:4: ^(f= FUNCTION . . . target[layerName, att, inputs, inputs, getPrototype((Function) f)] )
            {
            f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_invoke124); if (state.failed) return ;

            if ( state.backtracking==1 ) {
              inputs = invokeGuide((Function) f, null, null);
            }

            match(input, Token.DOWN, null); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            matchAny(input); if (state.failed) return ;
            pushFollow(FOLLOW_target_in_invoke134);
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
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:132:1: target[String layerName, String att, List<Object[]> inputs, List<Object[]> vals, List<String> prototype] : ( ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] ) | ^(p= PACK . ) );
    public final void target(String layerName, String att, List<Object[]> inputs, List<Object[]> vals, List<String> prototype) throws RecognitionException {
        StencilTree f=null;
        StencilTree p=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:133:2: ( ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] ) | ^(p= PACK . ) )
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
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:133:4: ^(f= FUNCTION . . . target[layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f)] )
                    {
                    f=(StencilTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_target152); if (state.failed) return ;

                    match(input, Token.DOWN, null); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    matchAny(input); if (state.failed) return ;
                    pushFollow(FOLLOW_target_in_target160);
                    target(layerName, att, inputs, invokeGuide((Function) f, vals, prototype), getPrototype((Function) f));

                    state._fsp--;
                    if (state.failed) return ;

                    match(input, Token.UP, null); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/interpreter/UpdateGuides.g:134:4: ^(p= PACK . )
                    {
                    p=(StencilTree)match(input,PACK,FOLLOW_PACK_in_target170); if (state.failed) return ;

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
    public static final BitSet FOLLOW_ID_in_topdown64 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000003FFFFFFFL});
    public static final BitSet FOLLOW_callGroup_in_topdown78 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CALL_GROUP_in_callGroup91 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_callChain_in_callGroup93 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CALL_CHAIN_in_callChain103 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_invoke_in_callChain105 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_invoke124 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_target_in_invoke134 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_target152 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_target_in_target160 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PACK_in_target170 = new BitSet(new long[]{0x0000000000000004L});

}