// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g 2009-10-23 23:25:06

	/** Performs the built of automatic guide mark generation.
	 *
	 * Precondition: To operate properly, this pass must be run after ensuring 
	 * guide operators exist and after annotating function calls with their
	 * associated call targets.
	 *  
	 * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality	  
	 **/
	 
	package stencil.parser.string;
	 
	import java.util.Set;
	import java.util.HashSet;
	import stencil.util.MultiPartName;
    import stencil.operator.module.*;
	import stencil.operator.StencilOperator;
	import stencil.parser.tree.*;
	
	import static stencil.parser.ParserConstants.BIND_OPERATOR;
	import static stencil.operator.module.OperatorData.OpType;
	import static stencil.util.Tuples.stripQuotes;
	import static stencil.parser.tree.Guide.SampleStrategy;
	 //TODO: Extend so we can handle more than the first field in a mapping definition


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class EnsureGuideOp extends TreeRewriter {
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


        public EnsureGuideOp(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public EnsureGuideOp(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return EnsureGuideOp.tokenNames; }
    public String getGrammarFileName() { return "/Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g"; }


    	protected ModuleCache modules;

      //Mapping from requested guides to descriptor construction strategy.  This is populated by the 'build' pass
      protected HashMap<String, SampleStrategy> requestedGuides = new HashMap<String, SampleStrategy>();

        
    	public EnsureGuideOp(TreeNodeStream input, ModuleCache modules) {
    		super(input, new RecognizerSharedState());
    		assert modules != null : "Module cache must not be null.";
    		this.modules = modules;
    	}

       	public Object transform(Object t) throws Exception {
    		build(t);
    		t = replace(t);
    		t = ensure(t);
    		return t;
    	}	

    	/**Build a list of things that need guides.**/
    	private void build(Object t) {
    		fptr down =	new fptr() {public Object rule() throws RecognitionException { return listRequirements(); }};
       	    fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
       	    downup(t, down, up);
        }
        
        /**Replace the auto-categorize operator.**/
        private Object replace(Object t) throws Exception {
    		fptr down =	new fptr() {public Object rule() throws RecognitionException { return replaceCompactForm(); }};
       	    fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
       	    Object r = downup(t, down, up);
    		return r;
        }
        
        /**Make sure that things which need guides have minimum necessary operators.
         *
         *@throws Exception Not all requested guides are found for ensuring
         */
        private Object ensure(Object t) throws Exception {
    		fptr down =	new fptr() {public Object rule() throws RecognitionException { return ensure(); }};
       	    fptr up = new fptr() {public Object rule() throws RecognitionException { return bottomup(); }};
       	    Object r = downup(t, down, up);
       	    return r;
        }


        private Object downup(Object t, final fptr down, final fptr up) {
            TreeVisitor v = new TreeVisitor(new CommonTreeAdaptor());
            TreeVisitorAction actions = new TreeVisitorAction() {
                public Object pre(Object t)  { return applyOnce(t, down); }
                public Object post(Object t) { return applyRepeatedly(t, up); }
            };
            t = v.visit(t, actions);
            return t;    
        }
        
        

        /**Given a tree, how should it be looked up in the guides map?*/
        private String key(Tree layer, Tree att) {return key(layer.getText(), att.getText());}
        private String key(String layer, Tree attribute) {return key(layer, attribute.getText());}
    	  private String key(String layer, String attribute) {
         	MultiPartName att = new MultiPartName(attribute);
        	String key= layer + BIND_OPERATOR + att.getName();	//Trim to just the attribute name
        	return key;
        } 
        
        

        /**What sample type is requested?
         * 
         * @param specializer The specializer of the guide
         */
        private SampleStrategy getSampleStrategy(Tree specializer) {
          Specializer s = (Specializer) specializer;
          SampleStrategy strat = new SampleStrategy(s);
          return strat;
        }

        /**Does the given call group need an echo operator?
        * 
        * For categorization, this checks that there is no other categorical operator
        * and only returns true if there is no other categorical operator.
        * 
        * For continuous guides, always returns if EchoContinuous is not found.
        * 
        **/ 
        private boolean requiresChanges(CallGroup group, SampleStrategy strat) {
           if (group.getChildCount() >1) {throw new RuntimeException("Cannot support auto-guide ensure for compound call groups.");}
           CallChain chain = group.getChains().get(0);
           CallTarget call = chain.getStart();


           if (strat.isCategorical()) {
             //Check if there is a categorize operator
             boolean hasCategorize = false;
             while (!(call instanceof Pack) && !hasCategorize) {
                Function f = (Function) call;
                MultiPartName name = new MultiPartName(f.getName());
                Module m = modules.findModuleForOperator(name.prefixedName()).module;
                  try {
                    OpType opType =  m.getOperatorData(name.getName(), f.getSpecializer()).getFacetData(name.getFacet()).getFacetType();
                    hasCategorize = (opType == OpType.CATEGORIZE);
                  } catch (SpecializationException e) {throw new Error("Specialization error after ensuring specialization supposedly performed.");}
               call = f.getCall();
             }      
             return !hasCategorize;
           } else {
              while(!(call instanceof Pack)) {
                 Function f  = (Function) call;
                 if (f.getName().equals("EchoContinuous.Map")) {return false;}
                 call = f.getCall();
              }
              return true;
           }

        }
        
        //Given a call group, what are the values retrieved from the tuple in the first round
        //of calls?
        private String findInitialArgs(CallGroup call) {
        	StringBuilder args= new StringBuilder();
        	for (CallChain chain: call.getChains()) {
        		CallTarget t = chain.getStart();
        		for (Value v: t.getArguments()) {
        			if (v.isTupleRef()) {
        				args.append("\"");
        				args.append(v.getChild(0).toString());//TODO: HACK...won't admit indexed tuple-refs
        				args.append("\"");
        				args.append(",");
        			}
        		}
        	}
        	if (args.length() ==0) {throw new RuntimeException("No tuple-dependent arguments found when creating guide operator.");}
        	args.deleteCharAt(args.length()-1); //Remove trailing comma
        	
        	return args.toString();
        }


        /**Construct a new call
         *
         * 
         * @param layer -- Layer to build operator for 
         * @param field -- Field being constructor for
         * @param c -- Call group being operated on
         */
    	 private Tree newCall(String layer, String field, CommonTree c) {
    	 	CallGroup call = (CallGroup) c; 
        	String key = key(layer, field);
        	if (!requestedGuides.containsKey(key)) {return call;}

            Guide.SampleStrategy strat = requestedGuides.get(key);
              
            if (!requiresChanges(call, strat)) {return call;}
        	
        	String intialArgs = findInitialArgs(call);
        	
        	String specSource = String.format("[1 .. n, %1$s]", intialArgs);
        	Specializer specializer;
        	StencilOperator op;
        
        	try {
    	    	specializer =ParseStencil.parseSpecializer(specSource); 
        	} catch (Exception e) {
        		throw new Error("Error creating auto-guide required categorize operator.",e);
        	}
        	
        	
        	stencil.parser.tree.List args = (stencil.parser.tree.List) adaptor.create(LIST, "args");
        	try {
        		String[] argNames = intialArgs.split(",");
        		for (String name: argNames) {
        			name= stripQuotes(name);
        			TupleRef ref = (TupleRef) adaptor.create(TUPLE_REF, "TUPLE_REF");
        			adaptor.addChild(ref, adaptor.create(ID, name));
        			adaptor.addChild(args,ref);
        		}
        	} catch (Exception e) {
        		throw new Error("Error creating auto-guide required argument list.",e);
        	}

    		  //Construct function node
    		  Function functionNode;
    		
          if (strat.isCategorical()) {functionNode = (Function) adaptor.create(FUNCTION, "EchoCategorize.Map");}
          else {functionNode = (Function) adaptor.create(FUNCTION, "EchoContinuous.Map");}

        	adaptor.addChild(functionNode, specializer);
        	adaptor.addChild(functionNode, args);
        	adaptor.addChild(functionNode, adaptor.create(YIELDS, "->"));
    		
    		//Construct chain node
    		CallChain chainNode = (CallChain) adaptor.create(CALL_CHAIN, "CALL_CHAIN");
    		adaptor.addChild(chainNode, functionNode);
    		
    		CallGroup groupNode;
    		if (call.getChains().size() == 1) {
    			adaptor.addChild(functionNode, call.getChains().get(0).getStart());
    			groupNode = (CallGroup) adaptor.create(CALL_GROUP, "CALL_GROUP");
    			adaptor.addChild(groupNode, chainNode);
    		} else {
    			throw new Error("Auto guide with joined call chains not supported.");
    		}
    		
    		
        	return groupNode;
        }
        
        
        /**Construct the specializer for the echo operatation.
         *
         * @param t Call target that will follow the new echo operator.
         */
    	public Specializer autoEchoSpecializer(CommonTree t) {
        	//Switch on the target type
        	//Get the names out of its arguments list
        	//Remember those names in the echo categorize
        
        	String specializerTemplate = "[1 .. n, %1$s]";
        	StringBuilder refs = new StringBuilder();
        	 
        	if (t instanceof Pack || t instanceof Function) {
        		CallTarget target = (CallTarget) t;
        		for (Value v:target.getArguments()) {
        			if (v.isAtom()) {continue;} //Skip all the atoms, we only want tuple-refs
        			refs.append("\"");
        			refs.append(v.getValue());
        			refs.append("\"");
        			refs.append(",");
        		}
        		refs.deleteCharAt(refs.length()-1); //Remove the last comma
        	} else {
        		throw new IllegalArgumentException("Attempt to use target of uknown type: " + t.getClass().getName());
        	}
        	
        	
    		  String specSource =String.format(specializerTemplate, refs);
        	try {
        		Specializer spec = ParseStencil.parseSpecializer(specSource);
        		return spec;
        	} catch (Exception e) {
        		throw new RuntimeException("Error creating default catgorical operator with specialzier " + specSource, e);
        	}
        }
        
        
        /**Construct the arguments section of an echo call block.
         *
         * @param t Call target that will follow the new echo operator.
         */
        public List<Value> autoEchoArgs(CommonTree t) {
        	CallTarget target = (CallTarget) t;
        	List<Value> args = (List<Value>) adaptor.create(LIST, "Arguments");
        	
     		  for (Value v: target.getArguments()) {
     			  if (v.isAtom()) {continue;}
     			  adaptor.addChild(args, adaptor.dupTree(v));
     		  }
     		  return args;
        } 
        
        public String selectOperator(Tree t) {
          Function f = (Function) t;
          
          Layer layer = (Layer) f.getAncestor(StencilParser.LAYER);
          Consumes consumes = (Consumes) f.getAncestor(StencilParser.CONSUMES);
          Rule r = (Rule) f.getAncestor(StencilParser.RULE);
          String field = r.getTarget().getPrototype().get(0);
          
          SampleStrategy strat = requestedGuides.get(key(layer.getName(), field));
          
          if (strat == null || strat.isCategorical()) {return "EchoCategorize.Map";}
          else {return "EchoContinuous.Map";}
        }
        


    public static class listRequirements_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "listRequirements"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:343:1: listRequirements : ^(att= GUIDE layer= . type= . spec= . actions= . ) ;
    public final EnsureGuideOp.listRequirements_return listRequirements() throws RecognitionException {
        EnsureGuideOp.listRequirements_return retval = new EnsureGuideOp.listRequirements_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree att=null;
        CommonTree layer=null;
        CommonTree type=null;
        CommonTree spec=null;
        CommonTree actions=null;

        CommonTree att_tree=null;
        CommonTree layer_tree=null;
        CommonTree type_tree=null;
        CommonTree spec_tree=null;
        CommonTree actions_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:343:17: ( ^(att= GUIDE layer= . type= . spec= . actions= . ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:343:19: ^(att= GUIDE layer= . type= . spec= . actions= . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            att=(CommonTree)match(input,GUIDE,FOLLOW_GUIDE_in_listRequirements65); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = att;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            layer=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = layer;
            _last = (CommonTree)input.LT(1);
            type=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = type;
            _last = (CommonTree)input.LT(1);
            spec=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = spec;
            _last = (CommonTree)input.LT(1);
            actions=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = actions;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {
              requestedGuides.put(key(layer, att), getSampleStrategy(spec));
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
    // $ANTLR end "listRequirements"

    public static class ensure_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ensure"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:347:1: ensure : ^(c= CONSUMES . ^( LIST ( rule[((Consumes) c).getLayer().getName()] )* ) ) ;
    public final EnsureGuideOp.ensure_return ensure() throws RecognitionException {
        EnsureGuideOp.ensure_return retval = new EnsureGuideOp.ensure_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree c=null;
        CommonTree wildcard1=null;
        CommonTree LIST2=null;
        EnsureGuideOp.rule_return rule3 = null;


        CommonTree c_tree=null;
        CommonTree wildcard1_tree=null;
        CommonTree LIST2_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:347:7: ( ^(c= CONSUMES . ^( LIST ( rule[((Consumes) c).getLayer().getName()] )* ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:347:9: ^(c= CONSUMES . ^( LIST ( rule[((Consumes) c).getLayer().getName()] )* ) )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            c=(CommonTree)match(input,CONSUMES,FOLLOW_CONSUMES_in_ensure97); if (state.failed) return retval;


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
            LIST2=(CommonTree)match(input,LIST,FOLLOW_LIST_in_ensure102); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = LIST2;
            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return retval;
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:347:31: ( rule[((Consumes) c).getLayer().getName()] )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==RULE) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:347:31: rule[((Consumes) c).getLayer().getName()]
                	    {
                	    _last = (CommonTree)input.LT(1);
                	    pushFollow(FOLLOW_rule_in_ensure104);
                	    rule3=rule(((Consumes) c).getLayer().getName());

                	    state._fsp--;
                	    if (state.failed) return retval;
                	    if ( state.backtracking==1 ) 
                	     
                	    if ( _first_2==null ) _first_2 = rule3.tree;

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
    // $ANTLR end "ensure"

    public static class rule_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rule"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:348:1: rule[String layer] : ^( RULE target= glyphField call= . bind= . ) -> ^( RULE $target $bind) ;
    public final EnsureGuideOp.rule_return rule(String layer) throws RecognitionException {
        EnsureGuideOp.rule_return retval = new EnsureGuideOp.rule_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree RULE4=null;
        CommonTree call=null;
        CommonTree bind=null;
        EnsureGuideOp.glyphField_return target = null;


        CommonTree RULE4_tree=null;
        CommonTree call_tree=null;
        CommonTree bind_tree=null;
        RewriteRuleNodeStream stream_RULE=new RewriteRuleNodeStream(adaptor,"token RULE");
        RewriteRuleSubtreeStream stream_glyphField=new RewriteRuleSubtreeStream(adaptor,"rule glyphField");
        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:348:19: ( ^( RULE target= glyphField call= . bind= . ) -> ^( RULE $target $bind) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:349:2: ^( RULE target= glyphField call= . bind= . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            RULE4=(CommonTree)match(input,RULE,FOLLOW_RULE_in_rule118); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_RULE.add(RULE4);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = RULE4;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            pushFollow(FOLLOW_glyphField_in_rule122);
            target=glyphField();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==1 ) stream_glyphField.add(target.getTree());
            _last = (CommonTree)input.LT(1);
            call=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = call;
            _last = (CommonTree)input.LT(1);
            bind=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = bind;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: RULE, bind, target
            // token labels: 
            // rule labels: retval, target
            // token list labels: 
            // rule list labels: 
            // wildcard labels: bind
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_bind=new RewriteRuleSubtreeStream(adaptor,"wildcard bind",bind);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_target=new RewriteRuleSubtreeStream(adaptor,"rule target",target!=null?target.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 350:3: -> ^( RULE $target $bind)
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:350:7: ^( RULE $target $bind)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_RULE.nextNode(), root_1);

                adaptor.addChild(root_1, stream_target.nextTree());
                adaptor.addChild(root_1, newCall(layer, (target!=null?target.field:null), call));
                adaptor.addChild(root_1, stream_bind.nextTree());

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
    // $ANTLR end "rule"

    public static class glyphField_return extends TreeRuleReturnScope {
        public String field;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "glyphField"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:353:1: glyphField returns [String field] : ^( GLYPH ^( TUPLE_PROTOTYPE f= ID ( . )* ) ) ;
    public final EnsureGuideOp.glyphField_return glyphField() throws RecognitionException {
        EnsureGuideOp.glyphField_return retval = new EnsureGuideOp.glyphField_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree f=null;
        CommonTree GLYPH5=null;
        CommonTree TUPLE_PROTOTYPE6=null;
        CommonTree wildcard7=null;

        CommonTree f_tree=null;
        CommonTree GLYPH5_tree=null;
        CommonTree TUPLE_PROTOTYPE6_tree=null;
        CommonTree wildcard7_tree=null;

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:353:34: ( ^( GLYPH ^( TUPLE_PROTOTYPE f= ID ( . )* ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:353:36: ^( GLYPH ^( TUPLE_PROTOTYPE f= ID ( . )* ) )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            GLYPH5=(CommonTree)match(input,GLYPH,FOLLOW_GLYPH_in_glyphField162); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = GLYPH5;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_2 = _last;
            CommonTree _first_2 = null;
            _last = (CommonTree)input.LT(1);
            TUPLE_PROTOTYPE6=(CommonTree)match(input,TUPLE_PROTOTYPE,FOLLOW_TUPLE_PROTOTYPE_in_glyphField165); if (state.failed) return retval;


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = TUPLE_PROTOTYPE6;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            f=(CommonTree)match(input,ID,FOLLOW_ID_in_glyphField169); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_2==null ) _first_2 = f;
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:353:67: ( . )*
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
            	    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:353:67: .
            	    {
            	    _last = (CommonTree)input.LT(1);
            	    wildcard7=(CommonTree)input.LT(1);
            	    matchAny(input); if (state.failed) return retval;
            	     
            	    if ( state.backtracking==1 )
            	    if ( _first_2==null ) _first_2 = wildcard7;

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


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {
              retval.field =(f!=null?f.getText():null);
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
    // $ANTLR end "glyphField"

    public static class replaceCompactForm_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "replaceCompactForm"
    // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:356:1: replaceCompactForm : ^(f= FUNCTION s= . a= . GUIDE_YIELD t= . ) -> ^( FUNCTION $s $a YIELDS ^( FUNCTION[selectOperator($f)] YIELDS ) ) ;
    public final EnsureGuideOp.replaceCompactForm_return replaceCompactForm() throws RecognitionException {
        EnsureGuideOp.replaceCompactForm_return retval = new EnsureGuideOp.replaceCompactForm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree f=null;
        CommonTree GUIDE_YIELD8=null;
        CommonTree s=null;
        CommonTree a=null;
        CommonTree t=null;

        CommonTree f_tree=null;
        CommonTree GUIDE_YIELD8_tree=null;
        CommonTree s_tree=null;
        CommonTree a_tree=null;
        CommonTree t_tree=null;
        RewriteRuleNodeStream stream_FUNCTION=new RewriteRuleNodeStream(adaptor,"token FUNCTION");
        RewriteRuleNodeStream stream_GUIDE_YIELD=new RewriteRuleNodeStream(adaptor,"token GUIDE_YIELD");

        try {
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:356:19: ( ^(f= FUNCTION s= . a= . GUIDE_YIELD t= . ) -> ^( FUNCTION $s $a YIELDS ^( FUNCTION[selectOperator($f)] YIELDS ) ) )
            // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:357:2: ^(f= FUNCTION s= . a= . GUIDE_YIELD t= . )
            {
            _last = (CommonTree)input.LT(1);
            {
            CommonTree _save_last_1 = _last;
            CommonTree _first_1 = null;
            _last = (CommonTree)input.LT(1);
            f=(CommonTree)match(input,FUNCTION,FOLLOW_FUNCTION_in_replaceCompactForm188); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_FUNCTION.add(f);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = f;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (CommonTree)input.LT(1);
            s=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = s;
            _last = (CommonTree)input.LT(1);
            a=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = a;
            _last = (CommonTree)input.LT(1);
            GUIDE_YIELD8=(CommonTree)match(input,GUIDE_YIELD,FOLLOW_GUIDE_YIELD_in_replaceCompactForm198); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_GUIDE_YIELD.add(GUIDE_YIELD8);

            _last = (CommonTree)input.LT(1);
            t=(CommonTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = t;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



            // AST REWRITE
            // elements: a, FUNCTION, FUNCTION, s
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: s, a
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"wildcard s",s);
            RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"wildcard a",a);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 357:40: -> ^( FUNCTION $s $a YIELDS ^( FUNCTION[selectOperator($f)] YIELDS ) )
            {
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:358:3: ^( FUNCTION $s $a YIELDS ^( FUNCTION[selectOperator($f)] YIELDS ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);

                adaptor.addChild(root_1, stream_s.nextTree());
                adaptor.addChild(root_1, stream_a.nextTree());
                adaptor.addChild(root_1, (CommonTree)adaptor.create(YIELDS, "YIELDS"));
                // /Users/jcottam/Documents/workspace/Stencil/Stencil/Core/stencil/parser/EnsureGuideOp.g:358:27: ^( FUNCTION[selectOperator($f)] YIELDS )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, selectOperator(f)), root_2);

                adaptor.addChild(root_2, autoEchoSpecializer(t));
                adaptor.addChild(root_2, autoEchoArgs(t));
                adaptor.addChild(root_2, (CommonTree)adaptor.create(YIELDS, "YIELDS"));
                adaptor.addChild(root_2, adaptor.dupTree(t));

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
    // $ANTLR end "replaceCompactForm"

    // Delegated rules


 

    public static final BitSet FOLLOW_GUIDE_in_listRequirements65 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CONSUMES_in_ensure97 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LIST_in_ensure102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rule_in_ensure104 = new BitSet(new long[]{0x0000000004000008L});
    public static final BitSet FOLLOW_RULE_in_rule118 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_glyphField_in_rule122 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000003FFFFFFFL});
    public static final BitSet FOLLOW_GLYPH_in_glyphField162 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TUPLE_PROTOTYPE_in_glyphField165 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_glyphField169 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF8L,0x000000003FFFFFFFL});
    public static final BitSet FOLLOW_FUNCTION_in_replaceCompactForm188 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GUIDE_YIELD_in_replaceCompactForm198 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000003FFFFFFFL});

}