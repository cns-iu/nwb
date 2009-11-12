// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g 2009-10-28 21:29:59

	/** Performs the built of automatic guide mark generation.
	 *
	 * Precondition: To operate properly, this pass must be run after ensuring 
	 * guide operators exist and after annotating function calls with their
	 * associated call targets.
	 *  
	 *
	 * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality	  
	 **/

	package stencil.parser.string;
	
	import java.util.Map;
	import java.util.HashMap;

  import org.antlr.runtime.tree.*;

	import stencil.parser.tree.*;
	import stencil.util.MultiPartName;
  import stencil.operator.module.*;
  import stencil.operator.module.util.*;
	import stencil.operator.module.OperatorData.OpType;

  import static stencil.parser.ParserConstants.GUIDE_BLOCK_TAG;

	


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class AutoGuide extends TreeRewriteSequence {
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


        public AutoGuide(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public AutoGuide(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return AutoGuide.tokenNames; }
    public String getGrammarFileName() { return "/Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g"; }


    	protected Map<String, CommonTree> attDefs = new HashMap<String, CommonTree>();
    	protected ModuleCache modules;
     
      public static class AutoGuideException extends RuntimeException {public AutoGuideException(String message) {super(message);}}
        
    	public AutoGuide(TreeNodeStream input, ModuleCache modules) {
    		super(input, new RecognizerSharedState());
    		this.modules = modules;
    	}
    		
    	public Object transform(Object t) {
    		t = build(t);
    		t = transfer(t);
    		t = trim(t);
    		t = rename(t);
    		return t;
    	}	
    	
    	 //Build a mapping from the layer/attribute names to mapping trees
    	 private Object build(Object t) {
          fptr down =	new fptr() {public Object rule() throws RecognitionException { return buildMappings(); }};
        	fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
        	return downup(t, down, up);
       }
        
        //Transfer appropriate mapping tree to the guide clause
        private Object transfer(Object t) {
    		  fptr down =	new fptr() {public Object rule() throws RecognitionException { return transferMappings(); }};
       	  fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
       	  return downup(t, down, up);
        }
        
        //Trim each mapping chain to its last categorical operator
        private Object trim(Object t) {
          fptr down = new fptr() {public Object rule() throws RecognitionException { return trimGuide(); }};
          fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
          return downup(t, down, up);
        }
        
        //Rename functions to use the guide channel
        private Object rename(Object t) {
    			fptr down =	new fptr() {public Object rule() throws RecognitionException { return renameMappingsDown(); }};
    	   	fptr up = new fptr() {public Object rule() throws RecognitionException { return  bottomup(); }};
    	   	return downup(t, down, up);		
        }
        
        
        private String key(Tree layer, Tree attribute) {return key(layer.getText(), attribute.getText());}
        private String key(String layer, Tree attribute) {return key(layer, attribute.getText());}
        private String key(String layer, String attribute) {
        	MultiPartName att = new MultiPartName(attribute);
    		String key = layer + ":" + att.getName();	//Trim to just the attribute name
    		return key;
        }
        
        private String guideName(String name) {return new MultiPartName(name).modSuffix(GUIDE_BLOCK_TAG).toString();}       


    	//EnsureGuideOp guarantees that one categorize exists...we move on from there!
    	private Tree trimCall(CallTarget tree) {
        	if (tree instanceof Pack) {return null;}

        	try {
        		Tree trimmed = trimCall(((Function) tree).getCall());
            	if (trimmed != null) {return trimmed;}
        	} catch (Exception e) {
        		throw new RuntimeException("Error trimming: " + tree.getText(),e);
        	}

        	if (isCategorize((Function) tree)) {return tree;}
        	else {return null;}
    	}
        	
    	
    	private boolean isCategorize(Function f) {
       		MultiPartName name = new MultiPartName(f.getName());
       		Module m = modules.findModuleForOperator(name.prefixedName()).module;
       		try {
       			OpType opType =  m.getOperatorData(name.getName(), f.getSpecializer()).getFacetData(name.getFacet()).getFacetType();;
       			return (opType == OpType.CATEGORIZE);
       		} catch (SpecializationException e) {throw new Error("Specialization error after ensuring specialization supposedly performed.");}

    	}



    public static class buildMappings_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "buildMappings"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:156:1: buildMappings : ^(c= CONSUMES . ^( LIST ( mapping[((Consumes)$c).getLayer().getName()] )* ) ) ;
    public final AutoGuide.buildMappings_return buildMappings() throws RecognitionException {
        AutoGuide.buildMappings_return retval = new AutoGuide.buildMappings_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree c=null;
        CommonTree wildcard1=null;
        CommonTree LIST2=null;
        AutoGuide.mapping_return mapping3 = null;


        CommonTree c_tree=null;
        CommonTree wildcard1_tree=null;
        CommonTree LIST2_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:156:14: ( ^(c= CONSUMES . ^( LIST ( mapping[((Consumes)$c).getLayer().getName()] )* ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:156:16: ^(c= CONSUMES . ^( LIST ( mapping[((Consumes)$c).getLayer().getName()] )* ) )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            c=(CommonTree)match(input,CONSUMES,FOLLOW_CONSUMES_in_buildMappings73); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = c;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            wildcard1=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard1;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_2 = _last;
            CommonTree _first_2 = null;
            _last = (CommonTree)input.LT(1);
            LIST2=(CommonTree)match(input,LIST,FOLLOW_LIST_in_buildMappings78); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = LIST2;
            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return retval;
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:156:38: ( mapping[((Consumes)$c).getLayer().getName()] )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==RULE) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:156:38: mapping[((Consumes)$c).getLayer().getName()]
                	    {
                	    _last = (CommonTree)input.LT(1);
                	    pushFollow(FOLLOW_mapping_in_buildMappings80);
                	    mapping3=mapping(((Consumes)c).getLayer().getName());

                	    state._fsp--;
                	    if (state.failed) return retval;
                	    if ( state.backtracking==1 ) 
                	     
                	    if ( _first_2==null ) _first_2 = mapping3.tree;

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
            }_last = _save_last_2;
            }


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
    // $ANTLR end "buildMappings"

    public static class mapping_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mapping"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:157:1: mapping[String layerName] : ^( RULE ^( GLYPH ^( TUPLE_PROTOTYPE field= . ) ) group= . . ) ;
    public final AutoGuide.mapping_return mapping(String layerName) throws RecognitionException {
        AutoGuide.mapping_return retval = new AutoGuide.mapping_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree RULE4=null;
        CommonTree GLYPH5=null;
        CommonTree TUPLE_PROTOTYPE6=null;
        CommonTree wildcard7=null;
        CommonTree field=null;
        CommonTree group=null;

        CommonTree RULE4_tree=null;
        CommonTree GLYPH5_tree=null;
        CommonTree TUPLE_PROTOTYPE6_tree=null;
        CommonTree wildcard7_tree=null;
        CommonTree field_tree=null;
        CommonTree group_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:158:3: ( ^( RULE ^( GLYPH ^( TUPLE_PROTOTYPE field= . ) ) group= . . ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:158:5: ^( RULE ^( GLYPH ^( TUPLE_PROTOTYPE field= . ) ) group= . . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            RULE4=(CommonTree)match(input,RULE,FOLLOW_RULE_in_mapping96); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = RULE4;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_2 = _last;
            CommonTree _first_2 = null;
            _last = (CommonTree)input.LT(1);
            GLYPH5=(CommonTree)match(input,GLYPH,FOLLOW_GLYPH_in_mapping99); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = GLYPH5;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_3 = _last;
            CommonTree _first_3 = null;
            _last = (CommonTree)input.LT(1);
            TUPLE_PROTOTYPE6=(CommonTree)match(input,TUPLE_PROTOTYPE,FOLLOW_TUPLE_PROTOTYPE_in_mapping102); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_2==null ) _first_2 = TUPLE_PROTOTYPE6;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            field=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_3==null ) _first_3 = field;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_3;
            }


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }

            _last = (CommonTree)input.LT(1);
            group=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = group;
            _last = (CommonTree)input.LT(1);
            wildcard7=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard7;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {
              attDefs.put(key(layerName, field), group);
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
    // $ANTLR end "mapping"

    public static class transferMappings_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "transferMappings"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:161:1: transferMappings : ^(field= GUIDE layerName= ID type= ID spec= . rules= . ) -> ^( $field $layerName $type $spec $rules) ;
    public final AutoGuide.transferMappings_return transferMappings() throws RecognitionException {
        AutoGuide.transferMappings_return retval = new AutoGuide.transferMappings_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree field=null;
        CommonTree layerName=null;
        CommonTree type=null;
        CommonTree spec=null;
        CommonTree rules=null;

        CommonTree field_tree=null;
        CommonTree layerName_tree=null;
        CommonTree type_tree=null;
        CommonTree spec_tree=null;
        CommonTree rules_tree=null;
        RewriteRuleNodeStream stream_GUIDE=new RewriteRuleNodeStream(adaptor,"token GUIDE");
        RewriteRuleNodeStream stream_ID=new RewriteRuleNodeStream(adaptor,"token ID");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:162:3: ( ^(field= GUIDE layerName= ID type= ID spec= . rules= . ) -> ^( $field $layerName $type $spec $rules) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:162:5: ^(field= GUIDE layerName= ID type= ID spec= . rules= . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            field=(CommonTree)match(input,GUIDE,FOLLOW_GUIDE_in_transferMappings132); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_GUIDE.add(field);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = field;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            layerName=(CommonTree)match(input,ID,FOLLOW_ID_in_transferMappings136); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_ID.add(layerName);

            _last = (CommonTree)input.LT(1);
            type=(CommonTree)match(input,ID,FOLLOW_ID_in_transferMappings140); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_ID.add(type);

            _last = (CommonTree)input.LT(1);
            spec=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = spec;
            _last = (CommonTree)input.LT(1);
            rules=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = rules;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {

              	 	 if (!attDefs.containsKey(key(layerName,field))) {throw new AutoGuideException("Guide requested for unavailable glyph attribute " + key(layerName, field));}
              	 	
            }


            // AST REWRITE
            // elements: type, layerName, spec, field, rules
            // token labels: field, layerName, type
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: spec, rules
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleNodeStream stream_field=new RewriteRuleNodeStream(adaptor,"token field",field);
            RewriteRuleNodeStream stream_layerName=new RewriteRuleNodeStream(adaptor,"token layerName",layerName);
            RewriteRuleNodeStream stream_type=new RewriteRuleNodeStream(adaptor,"token type",type);
            RewriteRuleSubtreeStream stream_spec=new RewriteRuleSubtreeStream(adaptor,"wildcard spec",spec);
            RewriteRuleSubtreeStream stream_rules=new RewriteRuleSubtreeStream(adaptor,"wildcard rules",rules);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 166:4: -> ^( $field $layerName $type $spec $rules)
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:166:7: ^( $field $layerName $type $spec $rules)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_field.nextNode(), root_1);

                adaptor.addChild(root_1, stream_layerName.nextNode());
                adaptor.addChild(root_1, stream_type.nextNode());
                adaptor.addChild(root_1, stream_spec.nextTree());
                adaptor.addChild(root_1, stream_rules.nextTree());
                adaptor.addChild(root_1, adaptor.dupTree(attDefs.get(key(layerName,field))));

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
    // $ANTLR end "transferMappings"

    public static class trimGuide_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "trimGuide"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:170:1: trimGuide : ^( GUIDE . . . . trimCallGroup ) ;
    public final AutoGuide.trimGuide_return trimGuide() throws RecognitionException {
        AutoGuide.trimGuide_return retval = new AutoGuide.trimGuide_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree GUIDE8=null;
        CommonTree wildcard9=null;
        CommonTree wildcard10=null;
        CommonTree wildcard11=null;
        CommonTree wildcard12=null;
        AutoGuide.trimCallGroup_return trimCallGroup13 = null;


        CommonTree GUIDE8_tree=null;
        CommonTree wildcard9_tree=null;
        CommonTree wildcard10_tree=null;
        CommonTree wildcard11_tree=null;
        CommonTree wildcard12_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:170:11: ( ^( GUIDE . . . . trimCallGroup ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:170:13: ^( GUIDE . . . . trimCallGroup )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            GUIDE8=(CommonTree)match(input,GUIDE,FOLLOW_GUIDE_in_trimGuide190); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = GUIDE8;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            wildcard9=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard9;
            _last = (CommonTree)input.LT(1);
            wildcard10=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard10;
            _last = (CommonTree)input.LT(1);
            wildcard11=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard11;
            _last = (CommonTree)input.LT(1);
            wildcard12=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard12;
            _last = (CommonTree)input.LT(1);
            pushFollow(FOLLOW_trimCallGroup_in_trimGuide200);
            trimCallGroup13=trimCallGroup();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==1 ) 
             
            if ( _first_1==null ) _first_1 = trimCallGroup13.tree;

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
    // $ANTLR end "trimGuide"

    public static class trimCallGroup_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "trimCallGroup"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:171:1: trimCallGroup : ^( CALL_GROUP ^( CALL_CHAIN call= . ) ) -> ^( CALL_GROUP ^( CALL_CHAIN ) ) ;
    public final AutoGuide.trimCallGroup_return trimCallGroup() throws RecognitionException {
        AutoGuide.trimCallGroup_return retval = new AutoGuide.trimCallGroup_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree CALL_GROUP14=null;
        CommonTree CALL_CHAIN15=null;
        CommonTree call=null;

        CommonTree CALL_GROUP14_tree=null;
        CommonTree CALL_CHAIN15_tree=null;
        CommonTree call_tree=null;
        RewriteRuleNodeStream stream_CALL_CHAIN=new RewriteRuleNodeStream(adaptor,"token CALL_CHAIN");
        RewriteRuleNodeStream stream_CALL_GROUP=new RewriteRuleNodeStream(adaptor,"token CALL_GROUP");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:171:14: ( ^( CALL_GROUP ^( CALL_CHAIN call= . ) ) -> ^( CALL_GROUP ^( CALL_CHAIN ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:171:16: ^( CALL_GROUP ^( CALL_CHAIN call= . ) )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            CALL_GROUP14=(CommonTree)match(input,CALL_GROUP,FOLLOW_CALL_GROUP_in_trimCallGroup208); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_CALL_GROUP.add(CALL_GROUP14);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = CALL_GROUP14;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_2 = _last;
            CommonTree _first_2 = null;
            _last = (CommonTree)input.LT(1);
            CALL_CHAIN15=(CommonTree)match(input,CALL_CHAIN,FOLLOW_CALL_CHAIN_in_trimCallGroup211); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_CALL_CHAIN.add(CALL_CHAIN15);


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = CALL_CHAIN15;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            call=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_2==null ) _first_2 = call;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: CALL_CHAIN, CALL_GROUP
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 171:51: -> ^( CALL_GROUP ^( CALL_CHAIN ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:171:54: ^( CALL_GROUP ^( CALL_CHAIN ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_CALL_GROUP.nextNode(), root_1);

                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:171:67: ^( CALL_CHAIN )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot(stream_CALL_CHAIN.nextNode(), root_2);

                adaptor.addChild(root_2, trimCall((CallTarget) call));

                adaptor.addChild(root_1, root_2);
                }

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
    // $ANTLR end "trimCallGroup"

    public static class renameMappingsDown_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "renameMappingsDown"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:176:1: renameMappingsDown : ^( GUIDE . . . . ^( CALL_GROUP ^( CALL_CHAIN renameGuideMapping ) ) ) ;
    public final AutoGuide.renameMappingsDown_return renameMappingsDown() throws RecognitionException {
        AutoGuide.renameMappingsDown_return retval = new AutoGuide.renameMappingsDown_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree GUIDE16=null;
        CommonTree wildcard17=null;
        CommonTree wildcard18=null;
        CommonTree wildcard19=null;
        CommonTree wildcard20=null;
        CommonTree CALL_GROUP21=null;
        CommonTree CALL_CHAIN22=null;
        AutoGuide.renameGuideMapping_return renameGuideMapping23 = null;


        CommonTree GUIDE16_tree=null;
        CommonTree wildcard17_tree=null;
        CommonTree wildcard18_tree=null;
        CommonTree wildcard19_tree=null;
        CommonTree wildcard20_tree=null;
        CommonTree CALL_GROUP21_tree=null;
        CommonTree CALL_CHAIN22_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:176:19: ( ^( GUIDE . . . . ^( CALL_GROUP ^( CALL_CHAIN renameGuideMapping ) ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:176:21: ^( GUIDE . . . . ^( CALL_GROUP ^( CALL_CHAIN renameGuideMapping ) ) )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            GUIDE16=(CommonTree)match(input,GUIDE,FOLLOW_GUIDE_in_renameMappingsDown240); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = GUIDE16;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            wildcard17=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard17;
            _last = (CommonTree)input.LT(1);
            wildcard18=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard18;
            _last = (CommonTree)input.LT(1);
            wildcard19=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard19;
            _last = (CommonTree)input.LT(1);
            wildcard20=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = wildcard20;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_2 = _last;
            CommonTree _first_2 = null;
            _last = (CommonTree)input.LT(1);
            CALL_GROUP21=(CommonTree)match(input,CALL_GROUP,FOLLOW_CALL_GROUP_in_renameMappingsDown251); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = CALL_GROUP21;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_3 = _last;
            CommonTree _first_3 = null;
            _last = (CommonTree)input.LT(1);
            CALL_CHAIN22=(CommonTree)match(input,CALL_CHAIN,FOLLOW_CALL_CHAIN_in_renameMappingsDown254); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_2==null ) _first_2 = CALL_CHAIN22;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            pushFollow(FOLLOW_renameGuideMapping_in_renameMappingsDown256);
            renameGuideMapping23=renameGuideMapping();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==1 ) 
             
            if ( _first_3==null ) _first_3 = renameGuideMapping23.tree;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_3;
            }


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }


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
    // $ANTLR end "renameMappingsDown"

    public static class renameGuideMapping_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "renameGuideMapping"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:177:1: renameGuideMapping : ( ^(f= FUNCTION spec= . args= . style= . call= renameGuideMapping ) -> ^( FUNCTION[guideName($f.text)] $spec $args $style $call) | ^( PACK ( . )* ) );
    public final AutoGuide.renameGuideMapping_return renameGuideMapping() throws RecognitionException {
        AutoGuide.renameGuideMapping_return retval = new AutoGuide.renameGuideMapping_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree f=null;
        CommonTree PACK24=null;
        CommonTree wildcard25=null;
        CommonTree spec=null;
        CommonTree args=null;
        CommonTree style=null;
        AutoGuide.renameGuideMapping_return call = null;


        CommonTree f_tree=null;
        CommonTree PACK24_tree=null;
        CommonTree wildcard25_tree=null;
        CommonTree spec_tree=null;
        CommonTree args_tree=null;
        CommonTree style_tree=null;
        RewriteRuleNodeStream stream_FUNCTION=new RewriteRuleNodeStream(adaptor,"token FUNCTION");
        RewriteRuleSubtreeStream stream_renameGuideMapping=new RewriteRuleSubtreeStream(adaptor,"rule renameGuideMapping");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:179:3: ( ^(f= FUNCTION spec= . args= . style= . call= renameGuideMapping ) -> ^( FUNCTION[guideName($f.text)] $spec $args $style $call) | ^( PACK ( . )* ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==FUNCTION) ) {
                alt3=1;
            }
            else if ( (LA3_0==PACK) ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:179:5: ^(f= FUNCTION spec= . args= . style= . call= renameGuideMapping )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_renameGuideMapping277); if (state.failed) return retval; 
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
                    style=(CommonTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;
                     
                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = style;
                    _last = (CommonTree)input.LT(1);
                    pushFollow(FOLLOW_renameGuideMapping_in_renameGuideMapping293);
                    call=renameGuideMapping();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==1 ) stream_renameGuideMapping.add(call.getTree());

                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: style, call, FUNCTION, args, spec
                    // token labels: 
                    // rule labels: call, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: spec, style, args
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_spec=new RewriteRuleSubtreeStream(adaptor,"wildcard spec",spec);
                    RewriteRuleSubtreeStream stream_style=new RewriteRuleSubtreeStream(adaptor,"wildcard style",style);
                    RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"wildcard args",args);
                    RewriteRuleSubtreeStream stream_call=new RewriteRuleSubtreeStream(adaptor,"rule call",call!=null?call.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 179:65: -> ^( FUNCTION[guideName($f.text)] $spec $args $style $call)
                    {
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:179:68: ^( FUNCTION[guideName($f.text)] $spec $args $style $call)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, guideName((f!=null?f.getText():null))), root_1);

                        adaptor.addChild(root_1, stream_spec.nextTree());
                        adaptor.addChild(root_1, stream_args.nextTree());
                        adaptor.addChild(root_1, stream_style.nextTree());
                        adaptor.addChild(root_1, stream_call.nextTree());

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
                    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:180:5: ^( PACK ( . )* )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    PACK24=(CommonTree)match(input,PACK,FOLLOW_PACK_in_renameGuideMapping320); if (state.failed) return retval;


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = PACK24;
                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); if (state.failed) return retval;
                        // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:180:12: ( . )*
                        loop2:
                        do {
                            int alt2=2;
                            int LA2_0 = input.LA(1);

                            if ( ((LA2_0>=ANNOTATION && LA2_0<=93)) ) {
                                alt2=1;
                            }
                            else if ( (LA2_0==UP) ) {
                                alt2=2;
                            }


                            switch (alt2) {
                        	case 1 :
                        	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/AutoGuide.g:180:12: .
                        	    {
                        	    _last = (CommonTree)input.LT(1);
                        	    wildcard25=(CommonTree)input.LT(1);
                        	    matchAny(input); if (state.failed) return retval;
                        	     
                        	    if ( state.backtracking==1 )
                        	    if ( _first_1==null ) _first_1 = wildcard25;

                        	    if ( state.backtracking==1 ) {
                        	    retval.tree = (CommonTree)_first_0;
                        	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        	        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
                        	    }
                        	    break;

                        	default :
                        	    break loop2;
                            }
                        } while (true);


                        match(input, Token.UP, null); if (state.failed) return retval;
                    }_last = _save_last_1;
                    }


                    if ( state.backtracking==1 ) {
                    retval.tree = (CommonTree)_first_0;
                    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        retval.tree = (CommonTree)adaptor.getParent(retval.tree);}
                    }
                    break;

            }
            if ( state.backtracking==1 ) {
              if (retval.tree instanceof Function) {((Function) retval.tree).setOperator(((Function) f).getOperator());}
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
    // $ANTLR end "renameGuideMapping"

    // Delegated rules


 

    public static final BitSet FOLLOW_CONSUMES_in_buildMappings73 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LIST_in_buildMappings78 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mapping_in_buildMappings80 = new BitSet(new long[]{0x0000000004000008L});
    public static final BitSet FOLLOW_RULE_in_mapping96 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GLYPH_in_mapping99 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TUPLE_PROTOTYPE_in_mapping102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GUIDE_in_transferMappings132 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_transferMappings136 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_transferMappings140 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000003FFFFFFFL});
    public static final BitSet FOLLOW_GUIDE_in_trimGuide190 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_trimCallGroup_in_trimGuide200 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CALL_GROUP_in_trimCallGroup208 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CALL_CHAIN_in_trimCallGroup211 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GUIDE_in_renameMappingsDown240 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CALL_GROUP_in_renameMappingsDown251 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CALL_CHAIN_in_renameMappingsDown254 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_renameGuideMapping_in_renameMappingsDown256 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_renameGuideMapping277 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_renameGuideMapping_in_renameGuideMapping293 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PACK_in_renameGuideMapping320 = new BitSet(new long[]{0x0000000000000004L});

}