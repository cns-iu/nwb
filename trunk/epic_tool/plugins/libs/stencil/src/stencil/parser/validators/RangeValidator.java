// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g 2009-11-27 15:01:29

  /** Validates that ranges make sense.**/
   

   package stencil.parser.validators;
  
    import static stencil.parser.ParserConstants.RANGE_END_INT;
    import stencil.parser.tree.Specializer;
    import stencil.parser.tree.Range;
    import stencil.parser.tree.StencilTree;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class RangeValidator extends TreeFilter {
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


        public RangeValidator(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public RangeValidator(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return RangeValidator.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g"; }


      private static final class RangeValidationException extends ValidationException {
        public RangeValidationException(Range range) {
             super("Invalid range:" + range.rangeString());
        }    
      }

       private void validate(Range range) {
          int start = range.getStart();
          boolean relativeStart = range.relativeStart();
          int end = range.getEnd();
          boolean relativeEnd = range.relativeEnd();
       
          if ((end == RANGE_END_INT)  //If the end is range-end, any start value can be used
              || (relativeStart == relativeEnd && relativeStart && start > end)
              || (relativeStart == relativeEnd && !relativeStart && start < end)
              || (relativeEnd && !relativeStart)) {return;}
              
          throw new RangeValidationException(range);
       }



    // $ANTLR start "topdown"
    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g:72:1: topdown : ^(r= RANGE ( . )* ) ;
    public final void topdown() throws RecognitionException {
        CommonTree r=null;

        try {
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g:72:8: ( ^(r= RANGE ( . )* ) )
            // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g:72:10: ^(r= RANGE ( . )* )
            {
            r=(CommonTree)match(input,RANGE,FOLLOW_RANGE_in_topdown66); if (state.failed) return ;

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return ;
                // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g:72:20: ( . )*
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
                	    // /Volumes/scratch/jcottam/workspace/Stencil/Stencil/Core/stencil/parser/validators/RangeValidator.g:72:20: .
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
              validate((Range) r);
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


 

    public static final BitSet FOLLOW_RANGE_in_topdown66 = new BitSet(new long[]{0x0000000000000004L});

}