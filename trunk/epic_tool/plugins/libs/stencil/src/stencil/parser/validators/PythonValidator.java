// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/PythonValidator.g 2009-11-27 15:01:33

  /** Validates that ranges make sense.**/
   

  package stencil.parser.validators;
  
  import stencil.parser.tree.*;
  import static stencil.parser.ParserConstants.INIT_BLOCK_TAG;
    
  import org.python.core.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class PythonValidator extends TreeFilter {
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


        public PythonValidator(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public PythonValidator(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return PythonValidator.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/PythonValidator.g"; }


      private static final class PythonValidationException extends ValidationException {
      	public PythonValidationException(PythonFacet facet, String message) {
      		super("Error parsing %1$s.%2$s. %3$s.", ((Python) facet.getParent()).getEnvironment(), facet.getName(), message);
      	}
      	
        public PythonValidationException(PythonFacet facet, Exception e) {
          super(e, "Error parsing %1$s.%2$s.", ((Python) facet.getParent()).getEnvironment(), facet.getName());
        }
      }  

      private void stripIndent(PythonFacet facet) {
        String body = facet.getBody();
        
        String[] lines = body.split("\\n");
        StringBuilder newBody = new StringBuilder();
        int whiteCount =0;
        boolean removeIndent=false;
        boolean pastFirst=false;
        
        for (String line: lines) {
          if (line.trim().equals("")) {continue;}
          if (!pastFirst) {
          	pastFirst = true;
            while(Character.isWhitespace(line.charAt(whiteCount))) {whiteCount++;}
          }
          newBody.append(line.substring(whiteCount));
          newBody.append("\n");
        } 
        facet.setBody(newBody.toString().trim());
      }

      private void validate(PythonFacet facet) {
        stripIndent(facet);
        
        if (facet.getName().equals(INIT_BLOCK_TAG)) {
        	if (facet.getArguments().size() != 0) {throw new PythonValidationException(facet, INIT_BLOCK_TAG + " facet with arguments not permitted.");}
        }
        
        if (facet.getBody().equals("")) {return;}   
        try {
          Py.compile(new java.io.ByteArrayInputStream(facet.getBody().getBytes()), null, CompileMode.exec);      
        } catch (Exception e) {throw new PythonValidationException(facet, e);}
        

      }



    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/PythonValidator.g:101:1: topdown : r= PYTHON_FACET ;
    public final void topdown() throws RecognitionException {
        CommonTree r=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/PythonValidator.g:101:8: (r= PYTHON_FACET )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/PythonValidator.g:101:10: r= PYTHON_FACET
            {
            r=(CommonTree)match(input,PYTHON_FACET,FOLLOW_PYTHON_FACET_in_topdown65); if (state.failed) return ;
            if ( state.backtracking==1 ) {
              validate((PythonFacet) r);
            }

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


 

    public static final BitSet FOLLOW_PYTHON_FACET_in_topdown65 = new BitSet(new long[]{0x0000000000000002L});

}