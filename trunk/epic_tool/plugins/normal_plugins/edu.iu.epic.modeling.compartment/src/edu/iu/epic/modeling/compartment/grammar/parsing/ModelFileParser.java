// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g 2009-11-10 11:31:27

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
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
public class ModelFileParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "WHITESPACE", "COMMENT", "NEWLINE", "ID", "NUMERIC", "UNSIGNED_NUMBER", "'='", "'+'", "'-'", "'*'", "'/'", "'susceptible'", "'infection'", "'latent'", "'recovered'", "'secondary'", "'->'", "'--'"
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
    public static final int NEWLINE=6;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int UNSIGNED_NUMBER=9;
    public static final int COMMENT=5;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:84:1: modelFile : ( line )+ EOF ;
    public final ModelFileParser.modelFile_return modelFile() throws RecognitionException {
        ModelFileParser.modelFile_return retval = new ModelFileParser.modelFile_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:85:2: ( ( line )+ EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:85:4: ( line )+ EOF
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:85:4: ( line )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=COMMENT && LA1_0<=ID)||(LA1_0>=15 && LA1_0<=18)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:85:5: line
            	    {
            	    pushFollow(FOLLOW_line_in_modelFile81);
            	    line();

            	    state._fsp--;
            	    if (state.failed) return retval;

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

            match(input,EOF,FOLLOW_EOF_in_modelFile85); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:89:1: line : ( COMMENT NEWLINE | ( ID '=' ( . )* )=> parameterAssignment NEWLINE | compartmentDeclaration NEWLINE | ( ID ( '--' | '->' ) ( . )* )=> transitionRule | NEWLINE );
    public final ModelFileParser.line_return line() throws RecognitionException {
        ModelFileParser.line_return retval = new ModelFileParser.line_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:90:2: ( COMMENT NEWLINE | ( ID '=' ( . )* )=> parameterAssignment NEWLINE | compartmentDeclaration NEWLINE | ( ID ( '--' | '->' ) ( . )* )=> transitionRule | NEWLINE )
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

                if ( (LA2_2==20) && (synpred2_ModelFile())) {
                    alt2=4;
                }
                else if ( (LA2_2==10) && (synpred1_ModelFile())) {
                    alt2=2;
                }
                else if ( (LA2_2==21) && (synpred2_ModelFile())) {
                    alt2=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;
                }
                }
                break;
            case 15:
            case 16:
            case 17:
            case 18:
                {
                alt2=3;
                }
                break;
            case NEWLINE:
                {
                alt2=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:90:4: COMMENT NEWLINE
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_line97); if (state.failed) return retval;
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_line99); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:91:4: ( ID '=' ( . )* )=> parameterAssignment NEWLINE
                    {
                    pushFollow(FOLLOW_parameterAssignment_in_line114);
                    parameterAssignment();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_line116); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:92:4: compartmentDeclaration NEWLINE
                    {
                    pushFollow(FOLLOW_compartmentDeclaration_in_line121);
                    compartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_line123); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:4: ( ID ( '--' | '->' ) ( . )* )=> transitionRule
                    {
                    pushFollow(FOLLOW_transitionRule_in_line144);
                    transitionRule();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:94:4: NEWLINE
                    {
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_line149); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:106:1: parameterAssignment : parameterID '=' parameterValue[new HashSet()] ;
    public final ModelFileParser.parameterAssignment_return parameterAssignment() throws RecognitionException {
        ModelFileParser.parameterAssignment_return retval = new ModelFileParser.parameterAssignment_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID1 = null;

        ModelFileParser.parameterValue_return parameterValue2 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:107:2: ( parameterID '=' parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:107:4: parameterID '=' parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterID_in_parameterAssignment205);
            parameterID1=parameterID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_parameterAssignment207); if (state.failed) return retval;
            pushFollow(FOLLOW_parameterValue_in_parameterAssignment209);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:110:1: parameterID : ID ;
    public final ModelFileParser.parameterID_return parameterID() throws RecognitionException {
        ModelFileParser.parameterID_return retval = new ModelFileParser.parameterID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:111:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:111:4: ID
            {
            match(input,ID,FOLLOW_ID_in_parameterID228); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:114:1: parameterValueValidator[Set referencedParameters] : parameterValue[referencedParameters] EOF ;
    public final ModelFileParser.parameterValueValidator_return parameterValueValidator(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValueValidator_return retval = new ModelFileParser.parameterValueValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:115:2: ( parameterValue[referencedParameters] EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:115:4: parameterValue[referencedParameters] EOF
            {
            pushFollow(FOLLOW_parameterValue_in_parameterValueValidator240);
            parameterValue(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_parameterValueValidator243); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:117:1: parameterValue[Set referencedParameters] : arithmeticParameterExpression[referencedParameters] ;
    public final ModelFileParser.parameterValue_return parameterValue(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValue_return retval = new ModelFileParser.parameterValue_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:118:2: ( arithmeticParameterExpression[referencedParameters] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:118:4: arithmeticParameterExpression[referencedParameters]
            {
            pushFollow(FOLLOW_arithmeticParameterExpression_in_parameterValue254);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:120:1: arithmeticParameterExpression[Set referencedParameters] : arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterExpression_return arithmeticParameterExpression(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterExpression_return retval = new ModelFileParser.arithmeticParameterExpression_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:121:2: ( arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:121:4: arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression267);
            arithmeticParameterTerm(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:121:50: ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=11 && LA3_0<=12)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:121:51: ( '+' | '-' ) arithmeticParameterTerm[referencedParameters]
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

            	    pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression279);
            	    arithmeticParameterTerm(referencedParameters);

            	    state._fsp--;
            	    if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:123:1: arithmeticParameterTerm[Set referencedParameters] : arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterTerm_return arithmeticParameterTerm(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterTerm_return retval = new ModelFileParser.arithmeticParameterTerm_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:124:2: ( arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:124:4: arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm293);
            arithmeticParameterFactor(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:124:52: ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=13 && LA4_0<=14)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:124:53: ( '*' | '/' ) arithmeticParameterFactor[referencedParameters]
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

            	    pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm305);
            	    arithmeticParameterFactor(referencedParameters);

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
    // $ANTLR end "arithmeticParameterTerm"

    public static class arithmeticParameterFactor_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arithmeticParameterFactor"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:126:1: arithmeticParameterFactor[Set referencedParameters] : ( parameterID | number );
    public final ModelFileParser.arithmeticParameterFactor_return arithmeticParameterFactor(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterFactor_return retval = new ModelFileParser.arithmeticParameterFactor_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID3 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:127:2: ( parameterID | number )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ID) ) {
                alt5=1;
            }
            else if ( (LA5_0==UNSIGNED_NUMBER||LA5_0==12) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:127:4: parameterID
                    {
                    pushFollow(FOLLOW_parameterID_in_arithmeticParameterFactor319);
                    parameterID3=parameterID();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                       referencedParameters.add((parameterID3!=null?input.toString(parameterID3.start,parameterID3.stop):null)); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:128:4: number
                    {
                    pushFollow(FOLLOW_number_in_arithmeticParameterFactor326);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:133:1: compartmentDeclaration : ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration );
    public final ModelFileParser.compartmentDeclaration_return compartmentDeclaration() throws RecognitionException {
        ModelFileParser.compartmentDeclaration_return retval = new ModelFileParser.compartmentDeclaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:134:2: ( susceptibleCompartmentDeclaration | infectedCompartmentDeclaration | latentCompartmentDeclaration | recoveredCompartmentDeclaration )
            int alt6=4;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt6=1;
                }
                break;
            case 16:
                {
                alt6=2;
                }
                break;
            case 17:
                {
                alt6=3;
                }
                break;
            case 18:
                {
                alt6=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:134:4: susceptibleCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration339);
                    susceptibleCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:135:4: infectedCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration344);
                    infectedCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:136:4: latentCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration349);
                    latentCompartmentDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:137:4: recoveredCompartmentDeclaration
                    {
                    pushFollow(FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration354);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:139:1: susceptibleCompartmentDeclaration : 'susceptible' compartmentID ;
    public final ModelFileParser.susceptibleCompartmentDeclaration_return susceptibleCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.susceptibleCompartmentDeclaration_return retval = new ModelFileParser.susceptibleCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID4 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:140:2: ( 'susceptible' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:140:4: 'susceptible' compartmentID
            {
            match(input,15,FOLLOW_15_in_susceptibleCompartmentDeclaration364); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration366);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:143:1: infectedCompartmentDeclaration : 'infection' compartmentID ;
    public final ModelFileParser.infectedCompartmentDeclaration_return infectedCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.infectedCompartmentDeclaration_return retval = new ModelFileParser.infectedCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID5 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:144:2: ( 'infection' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:144:4: 'infection' compartmentID
            {
            match(input,16,FOLLOW_16_in_infectedCompartmentDeclaration384); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_infectedCompartmentDeclaration386);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:147:1: latentCompartmentDeclaration : 'latent' compartmentID ;
    public final ModelFileParser.latentCompartmentDeclaration_return latentCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.latentCompartmentDeclaration_return retval = new ModelFileParser.latentCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID6 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:148:2: ( 'latent' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:148:4: 'latent' compartmentID
            {
            match(input,17,FOLLOW_17_in_latentCompartmentDeclaration404); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_latentCompartmentDeclaration406);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:151:1: recoveredCompartmentDeclaration : 'recovered' compartmentID ;
    public final ModelFileParser.recoveredCompartmentDeclaration_return recoveredCompartmentDeclaration() throws RecognitionException {
        ModelFileParser.recoveredCompartmentDeclaration_return retval = new ModelFileParser.recoveredCompartmentDeclaration_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return compartmentID7 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:152:2: ( 'recovered' compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:152:4: 'recovered' compartmentID
            {
            match(input,18,FOLLOW_18_in_recoveredCompartmentDeclaration424); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_recoveredCompartmentDeclaration426);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:158:1: transitionRate : parameterValue[new HashSet()] ;
    public final ModelFileParser.transitionRate_return transitionRate() throws RecognitionException {
        ModelFileParser.transitionRate_return retval = new ModelFileParser.transitionRate_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:159:2: ( parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:159:4: parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterValue_in_transitionRate447);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:161:1: transitionRule : ( spontaneousTransitionRelation transitionRate ( 'secondary' )? | interactionTransitionRelation transitionRate ( 'secondary' )? );
    public final ModelFileParser.transitionRule_return transitionRule() throws RecognitionException {
        ModelFileParser.transitionRule_return retval = new ModelFileParser.transitionRule_return();
        retval.start = input.LT(1);

        ModelFileParser.spontaneousTransitionRelation_return spontaneousTransitionRelation8 = null;

        ModelFileParser.transitionRate_return transitionRate9 = null;

        ModelFileParser.interactionTransitionRelation_return interactionTransitionRelation10 = null;

        ModelFileParser.transitionRate_return transitionRate11 = null;


         boolean isSecondary = false; 
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:163:2: ( spontaneousTransitionRelation transitionRate ( 'secondary' )? | interactionTransitionRelation transitionRate ( 'secondary' )? )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==21) ) {
                    alt9=2;
                }
                else if ( (LA9_1==20) ) {
                    alt9=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:163:4: spontaneousTransitionRelation transitionRate ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_spontaneousTransitionRelation_in_transitionRule464);
                    spontaneousTransitionRelation8=spontaneousTransitionRelation();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRate_in_transitionRule466);
                    transitionRate9=transitionRate();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:163:49: ( 'secondary' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==19) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:163:50: 'secondary'
                            {
                            match(input,19,FOLLOW_19_in_transitionRule469); if (state.failed) return retval;
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
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:171:4: interactionTransitionRelation transitionRate ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_interactionTransitionRelation_in_transitionRule482);
                    interactionTransitionRelation10=interactionTransitionRelation();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRate_in_transitionRule484);
                    transitionRate11=transitionRate();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:171:49: ( 'secondary' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==19) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:171:50: 'secondary'
                            {
                            match(input,19,FOLLOW_19_in_transitionRule487); if (state.failed) return retval;
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:182:1: spontaneousTransitionRelation returns [String source, String target] : s= compartmentID '->' t= compartmentID ;
    public final ModelFileParser.spontaneousTransitionRelation_return spontaneousTransitionRelation() throws RecognitionException {
        ModelFileParser.spontaneousTransitionRelation_return retval = new ModelFileParser.spontaneousTransitionRelation_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:183:2: (s= compartmentID '->' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:183:4: s= compartmentID '->' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation517);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,20,FOLLOW_20_in_spontaneousTransitionRelation519); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_spontaneousTransitionRelation523);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:189:1: compartmentID : ID ;
    public final ModelFileParser.compartmentID_return compartmentID() throws RecognitionException {
        ModelFileParser.compartmentID_return retval = new ModelFileParser.compartmentID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:190:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:190:4: ID
            {
            match(input,ID,FOLLOW_ID_in_compartmentID537); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:192:1: compartmentIDValidator : compartmentID EOF ;
    public final ModelFileParser.compartmentIDValidator_return compartmentIDValidator() throws RecognitionException {
        ModelFileParser.compartmentIDValidator_return retval = new ModelFileParser.compartmentIDValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:193:2: ( compartmentID EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:193:4: compartmentID EOF
            {
            pushFollow(FOLLOW_compartmentID_in_compartmentIDValidator547);
            compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_compartmentIDValidator549); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:195:1: interactionTransitionRelation returns [String source, String interactor, String target] : s= compartmentID '--' i= compartmentID '=' t= compartmentID ;
    public final ModelFileParser.interactionTransitionRelation_return interactionTransitionRelation() throws RecognitionException {
        ModelFileParser.interactionTransitionRelation_return retval = new ModelFileParser.interactionTransitionRelation_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return i = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:196:2: (s= compartmentID '--' i= compartmentID '=' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:196:4: s= compartmentID '--' i= compartmentID '=' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation565);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,21,FOLLOW_21_in_interactionTransitionRelation567); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation571);
            i=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_interactionTransitionRelation573); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_interactionTransitionRelation577);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:236:1: number : ( '-' )? UNSIGNED_NUMBER ;
    public final ModelFileParser.number_return number() throws RecognitionException {
        ModelFileParser.number_return retval = new ModelFileParser.number_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:237:2: ( ( '-' )? UNSIGNED_NUMBER )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:237:4: ( '-' )? UNSIGNED_NUMBER
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:237:4: ( '-' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==12) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:237:5: '-'
                    {
                    match(input,12,FOLLOW_12_in_number654); if (state.failed) return retval;

                    }
                    break;

            }

            match(input,UNSIGNED_NUMBER,FOLLOW_UNSIGNED_NUMBER_in_number658); if (state.failed) return retval;

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
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:91:4: ( ID '=' ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:91:5: ID '=' ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred1_ModelFile105); if (state.failed) return ;
        match(input,10,FOLLOW_10_in_synpred1_ModelFile107); if (state.failed) return ;
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:91:12: ( . )*
        loop11:
        do {
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=WHITESPACE && LA11_0<=21)) ) {
                alt11=1;
            }
            else if ( (LA11_0==EOF) ) {
                alt11=2;
            }


            switch (alt11) {
        	case 1 :
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:91:12: .
        	    {
        	    matchAny(input); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop11;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred1_ModelFile

    // $ANTLR start synpred2_ModelFile
    public final void synpred2_ModelFile_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:4: ( ID ( '--' | '->' ) ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:5: ID ( '--' | '->' ) ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred2_ModelFile129); if (state.failed) return ;
        if ( (input.LA(1)>=20 && input.LA(1)<=21) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:22: ( . )*
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
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:93:22: .
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


 

    public static final BitSet FOLLOW_line_in_modelFile81 = new BitSet(new long[]{0x00000000000780E0L});
    public static final BitSet FOLLOW_EOF_in_modelFile85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMENT_in_line97 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NEWLINE_in_line99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterAssignment_in_line114 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NEWLINE_in_line116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentDeclaration_in_line121 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NEWLINE_in_line123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transitionRule_in_line144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEWLINE_in_line149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterID_in_parameterAssignment205 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_parameterAssignment207 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_parameterValue_in_parameterAssignment209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_parameterID228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_parameterValueValidator240 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parameterValueValidator243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterExpression_in_parameterValue254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression267 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterExpression271 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression279 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm293 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterTerm297 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm305 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_parameterID_in_arithmeticParameterFactor319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_arithmeticParameterFactor326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_susceptibleCompartmentDeclaration_in_compartmentDeclaration339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infectedCompartmentDeclaration_in_compartmentDeclaration344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_latentCompartmentDeclaration_in_compartmentDeclaration349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_recoveredCompartmentDeclaration_in_compartmentDeclaration354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_susceptibleCompartmentDeclaration364 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_susceptibleCompartmentDeclaration366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_infectedCompartmentDeclaration384 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_infectedCompartmentDeclaration386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_latentCompartmentDeclaration404 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_latentCompartmentDeclaration406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_recoveredCompartmentDeclaration424 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_recoveredCompartmentDeclaration426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_transitionRate447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_spontaneousTransitionRelation_in_transitionRule464 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRate_in_transitionRule466 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_transitionRule469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interactionTransitionRelation_in_transitionRule482 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRate_in_transitionRule484 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_transitionRule487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation517 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_spontaneousTransitionRelation519 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_spontaneousTransitionRelation523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_compartmentID537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_compartmentIDValidator547 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_compartmentIDValidator549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation565 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_interactionTransitionRelation567 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation571 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_interactionTransitionRelation573 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_interactionTransitionRelation577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_number654 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_UNSIGNED_NUMBER_in_number658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred1_ModelFile105 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_synpred1_ModelFile107 = new BitSet(new long[]{0x00000000003FFFF2L});
    public static final BitSet FOLLOW_ID_in_synpred2_ModelFile129 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_set_in_synpred2_ModelFile131 = new BitSet(new long[]{0x00000000003FFFF2L});

}