// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DereferenceOperators.g 2009-11-27 15:00:55

  /**Takes operator references and creates operator instances out of them
   * 
   * Operators can be based off o either operator templates or operator instances from
   * an imported module.  If it is based on an operator template, the template is instantiated
   * and the operator reference is replaced with a new operator object that has
   * a body based on the template plus the specializer.  If the operator is
   * based on an instance, an instance of that operator is created and given the appropriate name.
   *
   * Uses ANTLR tree filter/rewrite: http://www.antlr.org/wiki/display/~admin/2008/11/29/Woohoo!+Tree+pattern+matching%2C+rewriting+a+reality    
   **/
  package stencil.parser.string;
  
  import stencil.parser.tree.*;  
  import stencil.operator.module.*;
  import stencil.operator.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("all")
public class DereferenceOperators extends TreeRewriteSequence {
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


        public DereferenceOperators(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public DereferenceOperators(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return DereferenceOperators.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DereferenceOperators.g"; }


      private Program program;
      private ModuleCache modules;
      
      public DereferenceOperators(TreeNodeStream input, ModuleCache modules) {
        super(input, new RecognizerSharedState());
        this.modules = modules;
        Object p = input.getTreeSource();
        
        assert p instanceof Program : "Input source must be a Program tree.";
        this.program = (Program) p;
      }


      public StencilTree instantiate(OperatorReference opRef) {
        OperatorBase base = opRef.getBase();
        Specializer spec = opRef.getSpecializer();
        String name = opRef.getName();
        String baseName = base.getName();  
        OperatorTemplate t = findTemplate(baseName);
        
        if (t==null) {
              Module module; 
              try {module = modules.findModuleForOperator(baseName).module;}
              catch (Exception e) {throw new RuntimeException(String.format("Could un-base operator %1$s, no template or instance named %2$s known.", name, baseName));}
                  
              StencilOperator l;
              OperatorData od;
              try {
                l = module.instance(baseName, spec);
                od = module.getOperatorData(baseName, spec); 
              } catch (Exception e) {throw new RuntimeException(String.format("Error instantiating %1$s as base for %2$s", baseName, name), e);}
               
              OperatorProxy p = (OperatorProxy) adaptor.create(OPERATOR_PROXY, name);
              p.setOperator(l, od);
              return p;
        } else {
          return t.instantiate(name, spec, adaptor);
        }
      }
      
      public OperatorTemplate findTemplate(String name) {
        for (OperatorTemplate t:program.getOperatorTemplates()) {
          if (name.equals(t.getName())) {return t;} 
        }
        return null;
      }


    public static class topdown_return extends TreeRuleReturnScope {
        StencilTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DereferenceOperators.g:107:1: topdown : ^(o= OPERATOR_REFERENCE base= . spec= . ) ->;
    public final DereferenceOperators.topdown_return topdown() throws RecognitionException {
        DereferenceOperators.topdown_return retval = new DereferenceOperators.topdown_return();
        retval.start = input.LT(1);

        StencilTree root_0 = null;

        StencilTree _first_0 = null;
        StencilTree _last = null;

        StencilTree o=null;
        StencilTree base=null;
        StencilTree spec=null;

        StencilTree o_tree=null;
        StencilTree base_tree=null;
        StencilTree spec_tree=null;
        RewriteRuleNodeStream stream_OPERATOR_REFERENCE=new RewriteRuleNodeStream(adaptor,"token OPERATOR_REFERENCE");

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DereferenceOperators.g:108:3: ( ^(o= OPERATOR_REFERENCE base= . spec= . ) ->)
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/DereferenceOperators.g:108:5: ^(o= OPERATOR_REFERENCE base= . spec= . )
            {
            _last = (StencilTree)input.LT(1);
            {
            StencilTree _save_last_1 = _last;
            StencilTree _first_1 = null;
            _last = (StencilTree)input.LT(1);
            o=(StencilTree)match(input,OPERATOR_REFERENCE,FOLLOW_OPERATOR_REFERENCE_in_topdown84); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_OPERATOR_REFERENCE.add(o);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = o;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (StencilTree)input.LT(1);
            base=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = base;
            _last = (StencilTree)input.LT(1);
            spec=(StencilTree)input.LT(1);
            matchAny(input); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = spec;

            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }



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

            root_0 = (StencilTree)adaptor.nil();
            // 109:7: ->
            {
                adaptor.addChild(root_0, instantiate((OperatorReference) o));

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


 

    public static final BitSet FOLLOW_OPERATOR_REFERENCE_in_topdown84 = new BitSet(new long[]{0x0000000000000004L});

}