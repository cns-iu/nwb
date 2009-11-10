// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g 2009-11-10 14:03:03

package edu.iu.epic.modeling.compartment.grammar.parsing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exceptions.ModelModificationException;


import org.antlr.runtime.*;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
public class ModelFileParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "WHITESPACE", "NEWLINE", "COMMENT", "ID", "NUMERIC", "UNSIGNED_NUMBER", "'='", "'+'", "'-'", "'*'", "'/'", "'susceptible'", "'infection'", "'latent'", "'recovered'", "'secondary'", "'->'", "'--'"
    };
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int WHITESPACE=4;
    public static final int NUMERIC=8;
    public static final int ID=7;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int NEWLINE=5;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int UNSIGNED_NUMBER=9;
    public static final int COMMENT=6;

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
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g"; }


    // The model that this parser will produce.
    private Model model = new Model();
    public Model getModel() {
    	return model;
    }

    public static class UncheckedParsingException extends RuntimeException {
    	private static final long serialVersionUID = 1L;

    	public UncheckedParsingException(Throwable cause) {
    		super(cause);
    	}

    	public UncheckedParsingException(String message) {
    		super(message);
    	}
    }


    public static ModelFileParser createParserOn(ANTLRStringStream stream) {
    	ModelFileLexer lex = new ModelFileLexer(stream);
    	CommonTokenStream tokens = new CommonTokenStream(lex);
       	return new ModelFileParser(tokens);
    }

    public static ModelFileParser createParserOn(String input) {
       	return createParserOn(new ANTLRStringStream(input));
    }

    public static ModelFileParser createParserOn(File file) throws IOException {
    	return createParserOn(new ANTLRFileStream(file.getCanonicalPath()));
    }


    public static class modelFile_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "modelFile"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:68:1: modelFile : ( line EOF | ( NEWLINE | ( line NEWLINE ) )+ EOF );
    public final ModelFileParser.modelFile_return modelFile() throws RecognitionException {
        ModelFileParser.modelFile_return retval = new ModelFileParser.modelFile_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:69:2: ( line EOF | ( NEWLINE | ( line NEWLINE ) )+ EOF )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:69:4: line EOF
                    {
                    pushFollow(FOLLOW_line_in_modelFile76);
                    line();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,EOF,FOLLOW_EOF_in_modelFile78); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:4: ( NEWLINE | ( line NEWLINE ) )+ EOF
                    {
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:4: ( NEWLINE | ( line NEWLINE ) )+
                    int cnt1=0;
                    loop1:
                    do {
                        int alt1=3;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==NEWLINE) ) {
                            alt1=1;
                        }
                        else if ( ((LA1_0>=COMMENT && LA1_0<=ID)||(LA1_0>=15 && LA1_0<=18)) ) {
                            alt1=2;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:5: NEWLINE
                    	    {
                    	    match(input,NEWLINE,FOLLOW_NEWLINE_in_modelFile84); if (state.failed) return retval;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:15: ( line NEWLINE )
                    	    {
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:15: ( line NEWLINE )
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:70:16: line NEWLINE
                    	    {
                    	    pushFollow(FOLLOW_line_in_modelFile89);
                    	    line();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    match(input,NEWLINE,FOLLOW_NEWLINE_in_modelFile91); if (state.failed) return retval;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt1 >= 1 ) break loop1;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(1, input);
                                throw eee;
                        }
                        cnt1++;
                    } while (true);

                    match(input,EOF,FOLLOW_EOF_in_modelFile96); if (state.failed) return retval;

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
    // $ANTLR end "modelFile"

    public static class line_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "line"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:73:1: line : ( COMMENT | ( ID '=' ( . )* )=> parameterAssignment | compartmentDeclaration | ( ID ( '--' | '->' ) ( . )* )=> transitionRule );
    public final ModelFileParser.line_return line() throws RecognitionException {
        ModelFileParser.line_return retval = new ModelFileParser.line_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:74:2: ( COMMENT | ( ID '=' ( . )* )=> parameterAssignment | compartmentDeclaration | ( ID ( '--' | '->' ) ( . )* )=> transitionRule )
            int alt3=4;
            switch ( input.LA(1) ) {
            case COMMENT:
                {
                alt3=1;
                }
                break;
            case ID:
                {
                int LA3_2 = input.LA(2);

                if ( (LA3_2==10) && (synpred1_ModelFile())) {
                    alt3=2;
                }
                else if ( (LA3_2==20) && (synpred2_ModelFile())) {
                    alt3=4;
                }
                else if ( (LA3_2==21) && (synpred2_ModelFile())) {
                    alt3=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 2, input);

                    throw nvae;
                }
                }
                break;
            case 15:
            case 16:
            case 17:
            case 18:
                {
                alt3=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:74:4: COMMENT
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_line107); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:75:4: ( ID '=' ( . )* )=> parameterAssignment
                    {
                    pushFollow(FOLLOW_parameterAssignment_in_line122);
                    parameterAssignment();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:76:4: compartmentDeclaration
                    {
                    pushFollow(FOLLOW_compartmentDeclaration_in_line127);
                    compartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:77:4: ( ID ( '--' | '->' ) ( . )* )=> transitionRule
                    {
                    pushFollow(FOLLOW_transitionRule_in_line148);
                    transitionRule();

                    state._fsp--;
                    if (state.failed) return retval;

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
    // $ANTLR end "line"

    public static class parameterAssignment_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterAssignment"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:89:1: parameterAssignment : parameterID '=' parameterValue[new HashSet()] ;
    public final ModelFileParser.parameterAssignment_return parameterAssignment() throws RecognitionException {
        ModelFileParser.parameterAssignment_return retval = new ModelFileParser.parameterAssignment_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID1 = null;

        ModelFileParser.parameterValue_return parameterValue2 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:90:2: ( parameterID '=' parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:90:4: parameterID '=' parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterID_in_parameterAssignment209);
            parameterID1=parameterID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_parameterAssignment211); if (state.failed) return retval;
            pushFollow(FOLLOW_parameterValue_in_parameterAssignment213);
            parameterValue2=parameterValue(new HashSet());

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
               model.setParameterDefinition((parameterID1!=null?input.toString(parameterID1.start,parameterID1.stop):null), (parameterValue2!=null?input.toString(parameterValue2.start,parameterValue2.stop):null));
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:1: parameterID : ID ;
    public final ModelFileParser.parameterID_return parameterID() throws RecognitionException {
        ModelFileParser.parameterID_return retval = new ModelFileParser.parameterID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:94:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:94:4: ID
            {
            match(input,ID,FOLLOW_ID_in_parameterID232); if (state.failed) return retval;

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

    public static class parameterValueValidator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterValueValidator"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:96:1: parameterValueValidator[Set referencedParameters] : parameterValue[referencedParameters] EOF ;
    public final ModelFileParser.parameterValueValidator_return parameterValueValidator(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValueValidator_return retval = new ModelFileParser.parameterValueValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:97:2: ( parameterValue[referencedParameters] EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:97:4: parameterValue[referencedParameters] EOF
            {
            pushFollow(FOLLOW_parameterValue_in_parameterValueValidator243);
            parameterValue(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_parameterValueValidator246); if (state.failed) return retval;

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
    // $ANTLR end "parameterValueValidator"

    public static class parameterValue_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterValue"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:99:1: parameterValue[Set referencedParameters] : arithmeticParameterExpression[referencedParameters] ;
    public final ModelFileParser.parameterValue_return parameterValue(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValue_return retval = new ModelFileParser.parameterValue_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:100:2: ( arithmeticParameterExpression[referencedParameters] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:100:4: arithmeticParameterExpression[referencedParameters]
            {
            pushFollow(FOLLOW_arithmeticParameterExpression_in_parameterValue257);
            arithmeticParameterExpression(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:102:1: arithmeticParameterExpression[Set referencedParameters] : arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterExpression_return arithmeticParameterExpression(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterExpression_return retval = new ModelFileParser.arithmeticParameterExpression_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:103:2: ( arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:103:4: arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression270);
            arithmeticParameterTerm(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:103:50: ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=11 && LA4_0<=12)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:103:51: ( '+' | '-' ) arithmeticParameterTerm[referencedParameters]
            	    {
            	    if ( (input.LA(1)>=11 && input.LA(1)<=12) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression282);
            	    arithmeticParameterTerm(referencedParameters);

            	    state._fsp--;
            	    if (state.failed) return retval;

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
    // $ANTLR end "arithmeticParameterExpression"

    public static class arithmeticParameterTerm_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arithmeticParameterTerm"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:105:1: arithmeticParameterTerm[Set referencedParameters] : arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterTerm_return arithmeticParameterTerm(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterTerm_return retval = new ModelFileParser.arithmeticParameterTerm_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:106:2: ( arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:106:4: arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm296);
            arithmeticParameterFactor(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:106:52: ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=13 && LA5_0<=14)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:106:53: ( '*' | '/' ) arithmeticParameterFactor[referencedParameters]
            	    {
            	    if ( (input.LA(1)>=13 && input.LA(1)<=14) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm308);
            	    arithmeticParameterFactor(referencedParameters);

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop5;
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:108:1: arithmeticParameterFactor[Set referencedParameters] : ( parameterID | number );
    public final ModelFileParser.arithmeticParameterFactor_return arithmeticParameterFactor(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterFactor_return retval = new ModelFileParser.arithmeticParameterFactor_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID3 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:109:2: ( parameterID | number )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==ID) ) {
                alt6=1;
            }
            else if ( (LA6_0==UNSIGNED_NUMBER||LA6_0==12) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:109:4: parameterID
                    {
                    pushFollow(FOLLOW_parameterID_in_arithmeticParameterFactor322);
                    parameterID3=parameterID();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                       referencedParameters.add((parameterID3!=null?input.toString(parameterID3.start,parameterID3.stop):null));
                    }

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:110:4: number
                    {
                    pushFollow(FOLLOW_number_in_arithmeticParameterFactor329);
                    number();

                    state._fsp--;
                    if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:115:1: compartmentDeclaration : ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration );
    public final ModelFileParser.compartmentDeclaration_return compartmentDeclaration() throws RecognitionException {
        ModelFileParser.compartmentDeclaration_return retval = new ModelFileParser.compartmentDeclaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:116:2: ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration )
            int alt7=4;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt7=1;
                }
                break;
            case 16:
                {
                alt7=2;
                }
                break;
            case 17:
                {
                alt7=3;
                }
                break;
            case 18:
                {
                alt7=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:116:4: susceptibleCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration342);
                    susceptibleCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:117:4: infectedCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration347);
                    infectedCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:118:4: latentCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration352);
                    latentCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:119:4: recoveredCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration357);
                    recoveredCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:121:1: susceptibleCompartmentDeclaration : 'susceptible' compartmentID ;
    public final ModelFileParser.susceptibleCompartmentDeclaration_return susceptibleCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.susceptibleCompartmentDeclaration_return retval = new ModelFileParser.susceptibleCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID4 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:122:2: ( 'susceptible' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:122:4: 'susceptible' compartmentID
            {
            match(input,15,FOLLOW_15_in_susceptibleCompartmentDeclaration367); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration369);
            compartmentID4=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
               model.addCompartment((compartmentID4!=null?input.toString(compartmentID4.start,compartmentID4.stop):null), Compartment.Type.SUSCEPTIBLE);
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:125:1: infectedCompartmentDeclaration : 'infection' compartmentID ;
    public final ModelFileParser.infectedCompartmentDeclaration_return infectedCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.infectedCompartmentDeclaration_return retval = new ModelFileParser.infectedCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID5 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:126:2: ( 'infection' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:126:4: 'infection' compartmentID
            {
            match(input,16,FOLLOW_16_in_infectedCompartmentDeclaration387); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_infectedCompartmentDeclaration389);
            compartmentID5=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
               model.addCompartment((compartmentID5!=null?input.toString(compartmentID5.start,compartmentID5.stop):null), Compartment.Type.INFECTED);
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:129:1: latentCompartmentDeclaration : 'latent' compartmentID ;
    public final ModelFileParser.latentCompartmentDeclaration_return latentCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.latentCompartmentDeclaration_return retval = new ModelFileParser.latentCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID6 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:130:2: ( 'latent' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:130:4: 'latent' compartmentID
            {
            match(input,17,FOLLOW_17_in_latentCompartmentDeclaration407); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_latentCompartmentDeclaration409);
            compartmentID6=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
               model.addCompartment((compartmentID6!=null?input.toString(compartmentID6.start,compartmentID6.stop):null), Compartment.Type.LATENT);
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:133:1: recoveredCompartmentDeclaration : 'recovered' compartmentID ;
    public final ModelFileParser.recoveredCompartmentDeclaration_return recoveredCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.recoveredCompartmentDeclaration_return retval = new ModelFileParser.recoveredCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID7 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:134:2: ( 'recovered' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:134:4: 'recovered' compartmentID
            {
            match(input,18,FOLLOW_18_in_recoveredCompartmentDeclaration427); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_recoveredCompartmentDeclaration429);
            compartmentID7=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
               model.addCompartment((compartmentID7!=null?input.toString(compartmentID7.start,compartmentID7.stop):null), Compartment.Type.RECOVERED);
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "recoveredCompartmentDeclaration"

    public static class transitionRate_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRate"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:140:1: transitionRate : parameterValue[new HashSet()] ;
    public final ModelFileParser.transitionRate_return transitionRate() throws RecognitionException {
        ModelFileParser.transitionRate_return retval = new ModelFileParser.transitionRate_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:141:2: ( parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:141:4: parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterValue_in_transitionRate450);
            parameterValue(new HashSet());

            state._fsp--;
            if (state.failed) return retval;

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

    public static class transitionRule_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRule"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:143:1: transitionRule : ( spontaneousTransitionRelation transitionRate ( 'secondary' )? | interactionTransitionRelation transitionRate ( 'secondary' )? );
    public final ModelFileParser.transitionRule_return transitionRule() throws RecognitionException {
        ModelFileParser.transitionRule_return retval = new ModelFileParser.transitionRule_return();
        retval.start = input.LT(1);

        ModelFileParser.spontaneousTransitionRelation_return spontaneousTransitionRelation8 = null;

        ModelFileParser.transitionRate_return transitionRate9 = null;

        ModelFileParser.interactionTransitionRelation_return interactionTransitionRelation10 = null;

        ModelFileParser.transitionRate_return transitionRate11 = null;


         boolean isSecondary = false;
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:145:2: ( spontaneousTransitionRelation transitionRate ( 'secondary' )? | interactionTransitionRelation transitionRate ( 'secondary' )? )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ID) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==21) ) {
                    alt10=2;
                }
                else if ( (LA10_1==20) ) {
                    alt10=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:145:4: spontaneousTransitionRelation transitionRate ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_spontaneousTransitionRelation_in_transitionRule467);
                    spontaneousTransitionRelation8=spontaneousTransitionRelation();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRate_in_transitionRule469);
                    transitionRate9=transitionRate();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:145:49: ( 'secondary' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==19) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:145:50: 'secondary'
                            {
                            match(input,19,FOLLOW_19_in_transitionRule472); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                               isSecondary = true;
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                      		model.addSpontaneousTransition(
                      			model.getCompartment((spontaneousTransitionRelation8!=null?spontaneousTransitionRelation8.source:null)),
                      			model.getCompartment((spontaneousTransitionRelation8!=null?spontaneousTransitionRelation8.target:null)),
                      			(transitionRate9!=null?input.toString(transitionRate9.start,transitionRate9.stop):null),
                      			isSecondary);

                    }

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:153:4: interactionTransitionRelation transitionRate ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_interactionTransitionRelation_in_transitionRule485);
                    interactionTransitionRelation10=interactionTransitionRelation();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRate_in_transitionRule487);
                    transitionRate11=transitionRate();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:153:49: ( 'secondary' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==19) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:153:50: 'secondary'
                            {
                            match(input,19,FOLLOW_19_in_transitionRule490); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                               isSecondary = true;
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                      		model.addInteractionTransition(
                      			model.getCompartment((interactionTransitionRelation10!=null?interactionTransitionRelation10.source:null)),
                      			model.getCompartment((interactionTransitionRelation10!=null?interactionTransitionRelation10.interactor:null)),
                      			model.getCompartment((interactionTransitionRelation10!=null?interactionTransitionRelation10.target:null)),
                      			(transitionRate11!=null?input.toString(transitionRate11.start,transitionRate11.stop):null),
                      			isSecondary);

                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (ModelModificationException e) {
             throw new UncheckedParsingException(e);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "transitionRule"

    public static class spontaneousTransitionRelation_return extends ParserRuleReturnScope {
        public String source;
        public String target;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "spontaneousTransitionRelation"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:164:1: spontaneousTransitionRelation returns [String source, String target] : s= compartmentID '->' t= compartmentID ;
    public final ModelFileParser.spontaneousTransitionRelation_return spontaneousTransitionRelation() throws RecognitionException {
        ModelFileParser.spontaneousTransitionRelation_return retval = new ModelFileParser.spontaneousTransitionRelation_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:165:2: (s= compartmentID '->' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:165:4: s= compartmentID '->' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation520);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,20,FOLLOW_20_in_spontaneousTransitionRelation522); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation526);
            t=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {

              		retval.source = (s!=null?input.toString(s.start,s.stop):null);
              		retval.target = (t!=null?input.toString(t.start,t.stop):null);

            }

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:171:1: compartmentID : ID ;
    public final ModelFileParser.compartmentID_return compartmentID() throws RecognitionException {
        ModelFileParser.compartmentID_return retval = new ModelFileParser.compartmentID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:172:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:172:4: ID
            {
            match(input,ID,FOLLOW_ID_in_compartmentID540); if (state.failed) return retval;

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

    public static class compartmentIDValidator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compartmentIDValidator"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:174:1: compartmentIDValidator : compartmentID EOF ;
    public final ModelFileParser.compartmentIDValidator_return compartmentIDValidator() throws RecognitionException {
        ModelFileParser.compartmentIDValidator_return retval = new ModelFileParser.compartmentIDValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:175:2: ( compartmentID EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:175:4: compartmentID EOF
            {
            pushFollow(FOLLOW_compartmentID_in_compartmentIDValidator550);
            compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_compartmentIDValidator552); if (state.failed) return retval;

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
    // $ANTLR end "compartmentIDValidator"

    public static class interactionTransitionRelation_return extends ParserRuleReturnScope {
        public String source;
        public String interactor;
        public String target;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "interactionTransitionRelation"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:177:1: interactionTransitionRelation returns [String source, String interactor, String target] : s= compartmentID '--' i= compartmentID '=' t= compartmentID ;
    public final ModelFileParser.interactionTransitionRelation_return interactionTransitionRelation() throws RecognitionException {
        ModelFileParser.interactionTransitionRelation_return retval = new ModelFileParser.interactionTransitionRelation_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return i = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:178:2: (s= compartmentID '--' i= compartmentID '=' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:178:4: s= compartmentID '--' i= compartmentID '=' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation568);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,21,FOLLOW_21_in_interactionTransitionRelation570); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation574);
            i=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_interactionTransitionRelation576); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation580);
            t=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {

              		retval.source = (s!=null?input.toString(s.start,s.stop):null);
              		retval.interactor = (i!=null?input.toString(i.start,i.stop):null);
              		retval.target = (t!=null?input.toString(t.start,t.stop):null);

            }

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:218:1: number : ( '-' )? UNSIGNED_NUMBER ;
    public final ModelFileParser.number_return number() throws RecognitionException {
        ModelFileParser.number_return retval = new ModelFileParser.number_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:219:2: ( ( '-' )? UNSIGNED_NUMBER )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:219:4: ( '-' )? UNSIGNED_NUMBER
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:219:4: ( '-' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==12) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:219:5: '-'
                    {
                    match(input,12,FOLLOW_12_in_number657); if (state.failed) return retval;

                    }
                    break;

            }

            match(input,UNSIGNED_NUMBER,FOLLOW_UNSIGNED_NUMBER_in_number661); if (state.failed) return retval;

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

    // $ANTLR start synpred1_ModelFile
    public final void synpred1_ModelFile_fragment() throws RecognitionException {
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:75:4: ( ID '=' ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:75:5: ID '=' ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred1_ModelFile113); if (state.failed) return ;
        match(input,10,FOLLOW_10_in_synpred1_ModelFile115); if (state.failed) return ;
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:75:12: ( . )*
        loop12:
        do {
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( ((LA12_0>=WHITESPACE && LA12_0<=21)) ) {
                alt12=1;
            }
            else if ( (LA12_0==EOF) ) {
                alt12=2;
            }


            switch (alt12) {
        	case 1 :
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:75:12: .
        	    {
        	    matchAny(input); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop12;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred1_ModelFile

    // $ANTLR start synpred2_ModelFile
    public final void synpred2_ModelFile_fragment() throws RecognitionException {
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:77:4: ( ID ( '--' | '->' ) ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:77:5: ID ( '--' | '->' ) ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred2_ModelFile133); if (state.failed) return ;
        if ( (input.LA(1)>=20 && input.LA(1)<=21) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:77:22: ( . )*
        loop13:
        do {
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=WHITESPACE && LA13_0<=21)) ) {
                alt13=1;
            }
            else if ( (LA13_0==EOF) ) {
                alt13=2;
            }


            switch (alt13) {
        	case 1 :
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:77:22: .
        	    {
        	    matchAny(input); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop13;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred2_ModelFile

    // Delegated rules

    public final boolean synpred1_ModelFile() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_ModelFile_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_ModelFile() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_ModelFile_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\103\uffff";
    static final String DFA2_eofS =
        "\1\uffff\1\10\12\uffff\4\10\2\uffff\1\10\1\uffff\1\10\1\uffff\1"+
        "\10\1\uffff\1\10\5\uffff\2\10\1\uffff\2\10\1\uffff\2\10\1\uffff"+
        "\2\10\1\uffff\2\10\1\uffff\1\10\3\uffff\1\10\1\uffff\1\10\1\uffff"+
        "\2\10\1\uffff\2\10\1\uffff\2\10\1\uffff\1\10\1\uffff\1\10\1\uffff"+
        "\1\10";
    static final String DFA2_minS =
        "\2\5\1\12\4\7\2\uffff\3\7\4\5\1\12\1\7\1\5\1\11\1\5\1\7\1\5\1\11"+
        "\1\5\5\7\2\5\1\11\2\5\1\11\2\5\1\11\2\5\1\11\2\5\1\11\1\5\3\7\1"+
        "\5\1\7\1\5\1\11\2\5\1\11\2\5\1\11\2\5\1\11\1\5\1\7\1\5\1\11\1\5";
    static final String DFA2_maxS =
        "\1\22\1\5\1\25\4\7\2\uffff\2\7\1\14\4\5\1\12\1\14\1\16\1\11\1\16"+
        "\1\7\1\23\1\11\1\23\5\14\1\5\1\16\1\11\2\16\1\11\1\16\1\23\1\11"+
        "\2\23\1\11\2\23\1\11\1\23\3\14\1\5\1\14\1\16\1\11\1\16\1\23\1\11"+
        "\2\23\1\11\2\23\1\11\1\23\1\14\1\23\1\11\1\23";
    static final String DFA2_acceptS =
        "\7\uffff\1\2\1\1\72\uffff";
    static final String DFA2_specialS =
        "\103\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\7\1\1\1\2\7\uffff\1\3\1\4\1\5\1\6",
            "\1\7",
            "\1\13\11\uffff\1\12\1\11",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\17",
            "",
            "",
            "\1\20",
            "\1\21",
            "\1\22\1\uffff\1\24\2\uffff\1\23",
            "\1\7",
            "\1\7",
            "\1\7",
            "\1\7",
            "\1\25",
            "\1\26\1\uffff\1\30\2\uffff\1\27",
            "\1\7\5\uffff\2\32\2\31",
            "\1\24",
            "\1\7\5\uffff\2\32\2\31",
            "\1\33",
            "\1\7\5\uffff\2\35\2\34\4\uffff\1\36",
            "\1\30",
            "\1\7\5\uffff\2\35\2\34\4\uffff\1\36",
            "\1\37\1\uffff\1\41\2\uffff\1\40",
            "\1\42\1\uffff\1\44\2\uffff\1\43",
            "\1\45\1\uffff\1\47\2\uffff\1\46",
            "\1\50\1\uffff\1\52\2\uffff\1\51",
            "\1\53\1\uffff\1\55\2\uffff\1\54",
            "\1\7",
            "\1\7\5\uffff\2\32\2\31",
            "\1\41",
            "\1\7\5\uffff\2\32\2\31",
            "\1\7\5\uffff\2\32\2\56",
            "\1\44",
            "\1\7\5\uffff\2\32\2\56",
            "\1\7\5\uffff\2\60\2\57\4\uffff\1\61",
            "\1\47",
            "\1\7\5\uffff\2\60\2\57\4\uffff\1\61",
            "\1\7\5\uffff\2\35\2\34\4\uffff\1\36",
            "\1\52",
            "\1\7\5\uffff\2\35\2\34\4\uffff\1\36",
            "\1\7\5\uffff\2\35\2\62\4\uffff\1\36",
            "\1\55",
            "\1\7\5\uffff\2\35\2\62\4\uffff\1\36",
            "\1\63\1\uffff\1\65\2\uffff\1\64",
            "\1\66\1\uffff\1\70\2\uffff\1\67",
            "\1\71\1\uffff\1\73\2\uffff\1\72",
            "\1\7",
            "\1\74\1\uffff\1\76\2\uffff\1\75",
            "\1\7\5\uffff\2\32\2\56",
            "\1\65",
            "\1\7\5\uffff\2\32\2\56",
            "\1\7\5\uffff\2\60\2\57\4\uffff\1\61",
            "\1\70",
            "\1\7\5\uffff\2\60\2\57\4\uffff\1\61",
            "\1\7\5\uffff\2\60\2\77\4\uffff\1\61",
            "\1\73",
            "\1\7\5\uffff\2\60\2\77\4\uffff\1\61",
            "\1\7\5\uffff\2\35\2\62\4\uffff\1\36",
            "\1\76",
            "\1\7\5\uffff\2\35\2\62\4\uffff\1\36",
            "\1\100\1\uffff\1\102\2\uffff\1\101",
            "\1\7\5\uffff\2\60\2\77\4\uffff\1\61",
            "\1\102",
            "\1\7\5\uffff\2\60\2\77\4\uffff\1\61"
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "68:1: modelFile : ( line EOF | ( NEWLINE | ( line NEWLINE ) )+ EOF );";
        }
    }


    public static final BitSet FOLLOW_line_in_modelFile76 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_modelFile78 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEWLINE_in_modelFile84 = new BitSet(new long[]{0x00000000000780E0L});
    public static final BitSet FOLLOW_line_in_modelFile89 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_modelFile91 = new BitSet(new long[]{0x00000000000780E0L});
    public static final BitSet FOLLOW_EOF_in_modelFile96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMENT_in_line107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterAssignment_in_line122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentDeclaration_in_line127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transitionRule_in_line148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterID_in_parameterAssignment209 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_parameterAssignment211 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_parameterValue_in_parameterAssignment213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_parameterID232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_parameterValueValidator243 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parameterValueValidator246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterExpression_in_parameterValue257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression270 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterExpression274 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression282 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm296 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterTerm300 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm308 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_parameterID_in_arithmeticParameterFactor322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_arithmeticParameterFactor329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_susceptibleCompartmentDeclaration367 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_infectedCompartmentDeclaration387 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_infectedCompartmentDeclaration389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_latentCompartmentDeclaration407 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_latentCompartmentDeclaration409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_recoveredCompartmentDeclaration427 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_recoveredCompartmentDeclaration429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_transitionRate450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_spontaneousTransitionRelation_in_transitionRule467 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRate_in_transitionRule469 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_transitionRule472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interactionTransitionRelation_in_transitionRule485 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRate_in_transitionRule487 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_transitionRule490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation520 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_spontaneousTransitionRelation522 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_compartmentID540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_compartmentIDValidator550 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_compartmentIDValidator552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation568 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_interactionTransitionRelation570 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation574 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_interactionTransitionRelation576 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_number657 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_UNSIGNED_NUMBER_in_number661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred1_ModelFile113 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_synpred1_ModelFile115 = new BitSet(new long[]{0x00000000003FFFF2L});
    public static final BitSet FOLLOW_ID_in_synpred2_ModelFile133 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_set_in_synpred2_ModelFile135 = new BitSet(new long[]{0x00000000003FFFF2L});

}