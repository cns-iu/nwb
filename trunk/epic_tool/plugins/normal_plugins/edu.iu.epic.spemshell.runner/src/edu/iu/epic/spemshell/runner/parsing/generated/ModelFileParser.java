// $ANTLR 3.1.2 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g 2009-09-22 16:49:34

package edu.iu.epic.spemshell.runner.parsing.generated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

public class ModelFileParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ADD", "SUB", "MULT", "DIV", "DECIMAL_POINT", "COMMENT_MARKER", "SPONTANEOUS_INTO", "INTERACTED_ON_BY", "INTERACTS_INTO", "WHITESPACE", "COMMENT", "NEWLINE", "ID", "NUMERIC", "ALPHABETIC", "ALPHABETIC_", "ALPHANUMERIC", "ALPHANUMERIC_", "UNSIGNED_NUMBER", "UNSIGNED_INTEGER", "UNSIGNED_REAL", "'susceptible'", "'infection'", "'latent'", "'recovered'"
    };
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int COMMENT_MARKER=9;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int SPONTANEOUS_INTO=10;
    public static final int UNSIGNED_REAL=24;
    public static final int WHITESPACE=13;
    public static final int SUB=5;
    public static final int INTERACTED_ON_BY=11;
    public static final int ALPHABETIC=18;
    public static final int NUMERIC=17;
    public static final int MULT=6;
    public static final int ID=16;
    public static final int EOF=-1;
    public static final int ALPHANUMERIC_=21;
    public static final int UNSIGNED_INTEGER=23;
    public static final int DECIMAL_POINT=8;
    public static final int NEWLINE=15;
    public static final int ALPHANUMERIC=20;
    public static final int UNSIGNED_NUMBER=22;
    public static final int ALPHABETIC_=19;
    public static final int DIV=7;
    public static final int COMMENT=14;
    public static final int INTERACTS_INTO=12;
    public static final int ADD=4;

    // delegates
    // delegators


        public ModelFileParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ModelFileParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("ModelFileParserTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return ModelFileParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g"; }


    private Map<String, String> parameterBindings = new LinkedHashMap<String, String>();
    public Map<String, String> getParameterBindings() {
    	return parameterBindings;
    }

    private Set<String> referencedParameters = new HashSet<String>();
    public Set<String> getReferencedParameters() {
    	return referencedParameters;
    }

    private String susceptibleCompartmentID;
    public String getSusceptibleCompartmentID() {
    	return susceptibleCompartmentID;
    }

    private Set<String> infectionCompartments = new HashSet<String>();
    public Set<String> getInfectionCompartments() {
    	return infectionCompartments;
    }

    /*
    private Set<String> latentCompartments = new HashSet<String>();
    public Set<String> getLatentCompartments() {
    	return latentCompartments;
    }

    private Set<String> recoveredCompartments = new HashSet<String>();
    public Set<String> getRecoveredCompartments() {
    	return recoveredCompartments;
    }
    */


    public static class modelFile_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "modelFile"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:74:1: modelFile : ( line )+ ;
    public final ModelFileParser.modelFile_return modelFile() throws RecognitionException {
        ModelFileParser.modelFile_return retval = new ModelFileParser.modelFile_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:75:2: ( ( line )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:75:4: ( line )+
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:75:4: ( line )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=COMMENT && LA1_0<=ID)||(LA1_0>=25 && LA1_0<=28)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:75:5: line
            	    {
            	    pushFollow(FOLLOW_line_in_modelFile157);
            	    line();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "modelFile"

    public static class line_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "line"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:77:1: line : ( COMMENT | parameterAssignment | compartmentDeclaration | transitionRule )? NEWLINE ;
    public final ModelFileParser.line_return line() throws RecognitionException {
        ModelFileParser.line_return retval = new ModelFileParser.line_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:2: ( ( COMMENT | parameterAssignment | compartmentDeclaration | transitionRule )? NEWLINE )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:4: ( COMMENT | parameterAssignment | compartmentDeclaration | transitionRule )? NEWLINE
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:4: ( COMMENT | parameterAssignment | compartmentDeclaration | transitionRule )?
            int alt2=5;
            switch ( input.LA(1) ) {
                case COMMENT:
                    {
                    alt2=1;
                    }
                    break;
                case ID:
                    {
                    int LA2_2 = input.LA(2);

                    if ( (LA2_2==INTERACTS_INTO) ) {
                        alt2=2;
                    }
                    else if ( ((LA2_2>=SPONTANEOUS_INTO && LA2_2<=INTERACTED_ON_BY)) ) {
                        alt2=4;
                    }
                    }
                    break;
                case 25:
                case 26:
                case 27:
                case 28:
                    {
                    alt2=3;
                    }
                    break;
            }

            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:5: COMMENT
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_line170); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:15: parameterAssignment
                    {
                    pushFollow(FOLLOW_parameterAssignment_in_line174);
                    parameterAssignment();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:37: compartmentDeclaration
                    {
                    pushFollow(FOLLOW_compartmentDeclaration_in_line178);
                    compartmentDeclaration();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:78:62: transitionRule
                    {
                    pushFollow(FOLLOW_transitionRule_in_line182);
                    transitionRule();

                    state._fsp--;


                    }
                    break;

            }

            match(input,NEWLINE,FOLLOW_NEWLINE_in_line186); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "line"

    public static class parameterAssignment_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterAssignment"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:92:1: parameterAssignment : parameterID '=' parameterValue ;
    public final ModelFileParser.parameterAssignment_return parameterAssignment() throws RecognitionException {
        ModelFileParser.parameterAssignment_return retval = new ModelFileParser.parameterAssignment_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID1 = null;

        ModelFileParser.parameterValue_return parameterValue2 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:93:2: ( parameterID '=' parameterValue )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:93:4: parameterID '=' parameterValue
            {
            pushFollow(FOLLOW_parameterID_in_parameterAssignment238);
            parameterID1=parameterID();

            state._fsp--;

            match(input,INTERACTS_INTO,FOLLOW_INTERACTS_INTO_in_parameterAssignment240); 
            pushFollow(FOLLOW_parameterValue_in_parameterAssignment242);
            parameterValue2=parameterValue();

            state._fsp--;

            parameterBindings.put((parameterID1!=null?input.toString(parameterID1.start,parameterID1.stop):null), (parameterValue2!=null?input.toString(parameterValue2.start,parameterValue2.stop):null));

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterAssignment"

    public static class parameterID_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterID"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:95:1: parameterID : ID ;
    public final ModelFileParser.parameterID_return parameterID() throws RecognitionException {
        ModelFileParser.parameterID_return retval = new ModelFileParser.parameterID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:96:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:96:4: ID
            {
            match(input,ID,FOLLOW_ID_in_parameterID254); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterID"

    public static class parameterValue_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterValue"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:100:1: parameterValue : arithmeticParameterExpression ;
    public final ModelFileParser.parameterValue_return parameterValue() throws RecognitionException {
        ModelFileParser.parameterValue_return retval = new ModelFileParser.parameterValue_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:101:2: ( arithmeticParameterExpression )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:101:4: arithmeticParameterExpression
            {
            pushFollow(FOLLOW_arithmeticParameterExpression_in_parameterValue266);
            arithmeticParameterExpression();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterValue"

    public static class arithmeticParameterExpression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arithmeticParameterExpression"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:103:1: arithmeticParameterExpression : arithmeticParameterTerm ( ( ADD | SUB ) arithmeticParameterTerm )* ;
    public final ModelFileParser.arithmeticParameterExpression_return arithmeticParameterExpression() throws RecognitionException {
        ModelFileParser.arithmeticParameterExpression_return retval = new ModelFileParser.arithmeticParameterExpression_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:104:2: ( arithmeticParameterTerm ( ( ADD | SUB ) arithmeticParameterTerm )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:104:4: arithmeticParameterTerm ( ( ADD | SUB ) arithmeticParameterTerm )*
            {
            pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression277);
            arithmeticParameterTerm();

            state._fsp--;

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:104:28: ( ( ADD | SUB ) arithmeticParameterTerm )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=ADD && LA3_0<=SUB)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:104:29: ( ADD | SUB ) arithmeticParameterTerm
            	    {
            	    if ( (input.LA(1)>=ADD && input.LA(1)<=SUB) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression288);
            	    arithmeticParameterTerm();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmeticParameterExpression"

    public static class arithmeticParameterTerm_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arithmeticParameterTerm"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:106:1: arithmeticParameterTerm : arithmeticParameterFactor ( ( MULT | DIV ) arithmeticParameterFactor )* ;
    public final ModelFileParser.arithmeticParameterTerm_return arithmeticParameterTerm() throws RecognitionException {
        ModelFileParser.arithmeticParameterTerm_return retval = new ModelFileParser.arithmeticParameterTerm_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:107:2: ( arithmeticParameterFactor ( ( MULT | DIV ) arithmeticParameterFactor )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:107:4: arithmeticParameterFactor ( ( MULT | DIV ) arithmeticParameterFactor )*
            {
            pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm300);
            arithmeticParameterFactor();

            state._fsp--;

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:107:30: ( ( MULT | DIV ) arithmeticParameterFactor )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=MULT && LA4_0<=DIV)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:107:31: ( MULT | DIV ) arithmeticParameterFactor
            	    {
            	    if ( (input.LA(1)>=MULT && input.LA(1)<=DIV) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm311);
            	    arithmeticParameterFactor();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmeticParameterTerm"

    public static class arithmeticParameterFactor_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arithmeticParameterFactor"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:109:1: arithmeticParameterFactor : ( parameterID | number );
    public final ModelFileParser.arithmeticParameterFactor_return arithmeticParameterFactor() throws RecognitionException {
        ModelFileParser.arithmeticParameterFactor_return retval = new ModelFileParser.arithmeticParameterFactor_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID3 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:110:2: ( parameterID | number )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ID) ) {
                alt5=1;
            }
            else if ( (LA5_0==SUB||LA5_0==UNSIGNED_NUMBER) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:110:4: parameterID
                    {
                    pushFollow(FOLLOW_parameterID_in_arithmeticParameterFactor323);
                    parameterID3=parameterID();

                    state._fsp--;

                    referencedParameters.add((parameterID3!=null?input.toString(parameterID3.start,parameterID3.stop):null));

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:111:4: number
                    {
                    pushFollow(FOLLOW_number_in_arithmeticParameterFactor330);
                    number();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arithmeticParameterFactor"

    public static class compartmentDeclaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compartmentDeclaration"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:116:1: compartmentDeclaration : ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration );
    public final ModelFileParser.compartmentDeclaration_return compartmentDeclaration() throws RecognitionException {
        ModelFileParser.compartmentDeclaration_return retval = new ModelFileParser.compartmentDeclaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:117:2: ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration )
            int alt6=4;
            switch ( input.LA(1) ) {
            case 25:
                {
                alt6=1;
                }
                break;
            case 26:
                {
                alt6=2;
                }
                break;
            case 27:
                {
                alt6=3;
                }
                break;
            case 28:
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:117:4: susceptibleCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration344);
                    susceptibleCompartmentDeclaration();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:118:4: infectedCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration349);
                    infectedCompartmentDeclaration();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:119:4: latentCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration354);
                    latentCompartmentDeclaration();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:120:4: recoveredCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration359);
                    recoveredCompartmentDeclaration();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "compartmentDeclaration"

    public static class susceptibleCompartmentDeclaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "susceptibleCompartmentDeclaration"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:122:1: susceptibleCompartmentDeclaration : 'susceptible' compartmentID ;
    public final ModelFileParser.susceptibleCompartmentDeclaration_return susceptibleCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.susceptibleCompartmentDeclaration_return retval = new ModelFileParser.susceptibleCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID4 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:123:2: ( 'susceptible' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:123:4: 'susceptible' compartmentID
            {
            match(input,25,FOLLOW_25_in_susceptibleCompartmentDeclaration369); 
            pushFollow(FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration371);
            compartmentID4=compartmentID();

            state._fsp--;

            susceptibleCompartmentID = (compartmentID4!=null?input.toString(compartmentID4.start,compartmentID4.stop):null);

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "susceptibleCompartmentDeclaration"

    public static class infectedCompartmentDeclaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "infectedCompartmentDeclaration"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:125:1: infectedCompartmentDeclaration : 'infection' compartmentID ;
    public final ModelFileParser.infectedCompartmentDeclaration_return infectedCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.infectedCompartmentDeclaration_return retval = new ModelFileParser.infectedCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID5 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:126:2: ( 'infection' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:126:4: 'infection' compartmentID
            {
            match(input,26,FOLLOW_26_in_infectedCompartmentDeclaration383); 
            pushFollow(FOLLOW_compartmentID_in_infectedCompartmentDeclaration385);
            compartmentID5=compartmentID();

            state._fsp--;

            infectionCompartments.add((compartmentID5!=null?input.toString(compartmentID5.start,compartmentID5.stop):null));

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "infectedCompartmentDeclaration"

    public static class latentCompartmentDeclaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "latentCompartmentDeclaration"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:128:1: latentCompartmentDeclaration : 'latent' ( compartmentID )+ ;
    public final ModelFileParser.latentCompartmentDeclaration_return latentCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.latentCompartmentDeclaration_return retval = new ModelFileParser.latentCompartmentDeclaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:129:2: ( 'latent' ( compartmentID )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:129:4: 'latent' ( compartmentID )+
            {
            match(input,27,FOLLOW_27_in_latentCompartmentDeclaration397); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:129:13: ( compartmentID )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==ID) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:129:13: compartmentID
            	    {
            	    pushFollow(FOLLOW_compartmentID_in_latentCompartmentDeclaration399);
            	    compartmentID();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "latentCompartmentDeclaration"

    public static class recoveredCompartmentDeclaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "recoveredCompartmentDeclaration"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:131:1: recoveredCompartmentDeclaration : 'recovered' ( compartmentID )+ ;
    public final ModelFileParser.recoveredCompartmentDeclaration_return recoveredCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.recoveredCompartmentDeclaration_return retval = new ModelFileParser.recoveredCompartmentDeclaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:132:2: ( 'recovered' ( compartmentID )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:132:4: 'recovered' ( compartmentID )+
            {
            match(input,28,FOLLOW_28_in_recoveredCompartmentDeclaration411); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:132:16: ( compartmentID )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==ID) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:132:16: compartmentID
            	    {
            	    pushFollow(FOLLOW_compartmentID_in_recoveredCompartmentDeclaration413);
            	    compartmentID();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "recoveredCompartmentDeclaration"

    public static class transitionRule_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRule"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:138:1: transitionRule : transitionRelation transitionRate ;
    public final ModelFileParser.transitionRule_return transitionRule() throws RecognitionException {
        ModelFileParser.transitionRule_return retval = new ModelFileParser.transitionRule_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:139:2: ( transitionRelation transitionRate )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:139:4: transitionRelation transitionRate
            {
            pushFollow(FOLLOW_transitionRelation_in_transitionRule429);
            transitionRelation();

            state._fsp--;

            pushFollow(FOLLOW_transitionRate_in_transitionRule431);
            transitionRate();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "transitionRule"

    public static class transitionRate_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRate"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:142:1: transitionRate : parameterValue ;
    public final ModelFileParser.transitionRate_return transitionRate() throws RecognitionException {
        ModelFileParser.transitionRate_return retval = new ModelFileParser.transitionRate_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:143:2: ( parameterValue )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:143:4: parameterValue
            {
            pushFollow(FOLLOW_parameterValue_in_transitionRate442);
            parameterValue();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "transitionRate"

    public static class transitionRelation_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRelation"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:145:1: transitionRelation : ( spontaneousTransitionRelation | interactionTransitionRelation );
    public final ModelFileParser.transitionRelation_return transitionRelation() throws RecognitionException {
        ModelFileParser.transitionRelation_return retval = new ModelFileParser.transitionRelation_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:146:2: ( spontaneousTransitionRelation | interactionTransitionRelation )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==INTERACTED_ON_BY) ) {
                    alt9=2;
                }
                else if ( (LA9_1==SPONTANEOUS_INTO) ) {
                    alt9=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:146:4: spontaneousTransitionRelation
                    {
                    pushFollow(FOLLOW_spontaneousTransitionRelation_in_transitionRelation452);
                    spontaneousTransitionRelation();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:147:4: interactionTransitionRelation
                    {
                    pushFollow(FOLLOW_interactionTransitionRelation_in_transitionRelation457);
                    interactionTransitionRelation();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "transitionRelation"

    public static class spontaneousTransitionRelation_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "spontaneousTransitionRelation"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:149:1: spontaneousTransitionRelation : compartmentID SPONTANEOUS_INTO compartmentID ;
    public final ModelFileParser.spontaneousTransitionRelation_return spontaneousTransitionRelation() throws RecognitionException {
        ModelFileParser.spontaneousTransitionRelation_return retval = new ModelFileParser.spontaneousTransitionRelation_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:150:2: ( compartmentID SPONTANEOUS_INTO compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:150:4: compartmentID SPONTANEOUS_INTO compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation467);
            compartmentID();

            state._fsp--;

            match(input,SPONTANEOUS_INTO,FOLLOW_SPONTANEOUS_INTO_in_spontaneousTransitionRelation469); 
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation471);
            compartmentID();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "spontaneousTransitionRelation"

    public static class compartmentID_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compartmentID"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:152:1: compartmentID : ID ;
    public final ModelFileParser.compartmentID_return compartmentID() throws RecognitionException {
        ModelFileParser.compartmentID_return retval = new ModelFileParser.compartmentID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:153:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:153:4: ID
            {
            match(input,ID,FOLLOW_ID_in_compartmentID481); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "compartmentID"

    public static class interactionTransitionRelation_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "interactionTransitionRelation"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:155:1: interactionTransitionRelation : compartmentID INTERACTED_ON_BY compartmentID INTERACTS_INTO compartmentID ;
    public final ModelFileParser.interactionTransitionRelation_return interactionTransitionRelation() throws RecognitionException {
        ModelFileParser.interactionTransitionRelation_return retval = new ModelFileParser.interactionTransitionRelation_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:156:2: ( compartmentID INTERACTED_ON_BY compartmentID INTERACTS_INTO compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:156:4: compartmentID INTERACTED_ON_BY compartmentID INTERACTS_INTO compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation491);
            compartmentID();

            state._fsp--;

            match(input,INTERACTED_ON_BY,FOLLOW_INTERACTED_ON_BY_in_interactionTransitionRelation493); 
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation495);
            compartmentID();

            state._fsp--;

            match(input,INTERACTS_INTO,FOLLOW_INTERACTS_INTO_in_interactionTransitionRelation497); 
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation499);
            compartmentID();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "interactionTransitionRelation"

    public static class number_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "number"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:185:1: number : ( '-' )? UNSIGNED_NUMBER ;
    public final ModelFileParser.number_return number() throws RecognitionException {
        ModelFileParser.number_return retval = new ModelFileParser.number_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:186:2: ( ( '-' )? UNSIGNED_NUMBER )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:186:4: ( '-' )? UNSIGNED_NUMBER
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:186:4: ( '-' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==SUB) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:186:5: '-'
                    {
                    match(input,SUB,FOLLOW_SUB_in_number617); 

                    }
                    break;

            }

            match(input,UNSIGNED_NUMBER,FOLLOW_UNSIGNED_NUMBER_in_number621); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "number"

    // Delegated rules


 

    public static final BitSet FOLLOW_line_in_modelFile157 = new BitSet(new long[]{0x000000001E01C002L});
    public static final BitSet FOLLOW_COMMENT_in_line170 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_parameterAssignment_in_line174 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_compartmentDeclaration_in_line178 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_transitionRule_in_line182 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_NEWLINE_in_line186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterID_in_parameterAssignment238 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_INTERACTS_INTO_in_parameterAssignment240 = new BitSet(new long[]{0x0000000000410020L});
    public static final BitSet FOLLOW_parameterValue_in_parameterAssignment242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_parameterID254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterExpression_in_parameterValue266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression277 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterExpression280 = new BitSet(new long[]{0x0000000000410020L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression288 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm300 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterTerm303 = new BitSet(new long[]{0x0000000000410020L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm311 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_parameterID_in_arithmeticParameterFactor323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_arithmeticParameterFactor330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_susceptibleCompartmentDeclaration369 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_infectedCompartmentDeclaration383 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_infectedCompartmentDeclaration385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_latentCompartmentDeclaration397 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_latentCompartmentDeclaration399 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_28_in_recoveredCompartmentDeclaration411 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_recoveredCompartmentDeclaration413 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_transitionRelation_in_transitionRule429 = new BitSet(new long[]{0x0000000000410020L});
    public static final BitSet FOLLOW_transitionRate_in_transitionRule431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_transitionRate442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_spontaneousTransitionRelation_in_transitionRelation452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interactionTransitionRelation_in_transitionRelation457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation467 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_SPONTANEOUS_INTO_in_spontaneousTransitionRelation469 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_compartmentID481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation491 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_INTERACTED_ON_BY_in_interactionTransitionRelation493 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation495 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_INTERACTS_INTO_in_interactionTransitionRelation497 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_number617 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_UNSIGNED_NUMBER_in_number621 = new BitSet(new long[]{0x0000000000000002L});

}