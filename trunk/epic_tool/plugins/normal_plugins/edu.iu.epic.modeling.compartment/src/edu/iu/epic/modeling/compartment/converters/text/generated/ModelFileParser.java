// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g 2010-10-04 17:43:44

package edu.iu.epic.modeling.compartment.converters.text.generated;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;


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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "WHITESPACE", "NEWLINE", "COMMENT", "ID", "NUMERIC", "UNSIGNED_NUMBER", "'='", "'+'", "'-'", "'*'", "'/'", "'secondary'", "'->'", "'--'"
    };
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int NEWLINE=5;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int WHITESPACE=4;
    public static final int UNSIGNED_NUMBER=9;
    public static final int NUMERIC=8;
    public static final int ID=7;
    public static final int COMMENT=6;
    public static final int EOF=-1;

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
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g"; }


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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:68:1: modelFile : ( line EOF | ( NEWLINE | ( line NEWLINE ) )+ EOF );
    public final ModelFileParser.modelFile_return modelFile() throws RecognitionException {
        ModelFileParser.modelFile_return retval = new ModelFileParser.modelFile_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:69:2: ( line EOF | ( NEWLINE | ( line NEWLINE ) )+ EOF )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:69:4: line EOF
                    {
                    pushFollow(FOLLOW_line_in_modelFile76);
                    line();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,EOF,FOLLOW_EOF_in_modelFile78); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:4: ( NEWLINE | ( line NEWLINE ) )+ EOF
                    {
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:4: ( NEWLINE | ( line NEWLINE ) )+
                    int cnt1=0;
                    loop1:
                    do {
                        int alt1=3;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==NEWLINE) ) {
                            alt1=1;
                        }
                        else if ( ((LA1_0>=COMMENT && LA1_0<=ID)) ) {
                            alt1=2;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:5: NEWLINE
                    	    {
                    	    match(input,NEWLINE,FOLLOW_NEWLINE_in_modelFile84); if (state.failed) return retval;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:15: ( line NEWLINE )
                    	    {
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:15: ( line NEWLINE )
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:70:16: line NEWLINE
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:72:1: line : ( COMMENT | ( ID '=' ( . )* )=> parameterAssignment | ( ID ( '--' | '->' ) ( . )* )=> transition );
    public final ModelFileParser.line_return line() throws RecognitionException {
        ModelFileParser.line_return retval = new ModelFileParser.line_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:73:2: ( COMMENT | ( ID '=' ( . )* )=> parameterAssignment | ( ID ( '--' | '->' ) ( . )* )=> transition )
            int alt3=3;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==COMMENT) ) {
                alt3=1;
            }
            else if ( (LA3_0==ID) ) {
                int LA3_2 = input.LA(2);

                if ( (LA3_2==17) && (synpred2_ModelFile())) {
                    alt3=3;
                }
                else if ( (LA3_2==10) && (synpred1_ModelFile())) {
                    alt3=2;
                }
                else if ( (LA3_2==16) && (synpred2_ModelFile())) {
                    alt3=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:73:4: COMMENT
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_line106); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:74:4: ( ID '=' ( . )* )=> parameterAssignment
                    {
                    pushFollow(FOLLOW_parameterAssignment_in_line121);
                    parameterAssignment();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:75:4: ( ID ( '--' | '->' ) ( . )* )=> transition
                    {
                    pushFollow(FOLLOW_transition_in_line142);
                    transition();

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:87:1: parameterAssignment : parameterID '=' parameterValue[new HashSet()] ;
    public final ModelFileParser.parameterAssignment_return parameterAssignment() throws RecognitionException {
        ModelFileParser.parameterAssignment_return retval = new ModelFileParser.parameterAssignment_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID1 = null;

        ModelFileParser.parameterValue_return parameterValue2 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:88:2: ( parameterID '=' parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:88:4: parameterID '=' parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterID_in_parameterAssignment203);
            parameterID1=parameterID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_parameterAssignment205); if (state.failed) return retval;
            pushFollow(FOLLOW_parameterValue_in_parameterAssignment207);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:91:1: parameterID : ID ;
    public final ModelFileParser.parameterID_return parameterID() throws RecognitionException {
        ModelFileParser.parameterID_return retval = new ModelFileParser.parameterID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:92:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:92:4: ID
            {
            match(input,ID,FOLLOW_ID_in_parameterID226); if (state.failed) return retval;

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

    public static class parameterIDValidator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterIDValidator"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:94:1: parameterIDValidator : ID EOF ;
    public final ModelFileParser.parameterIDValidator_return parameterIDValidator() throws RecognitionException {
        ModelFileParser.parameterIDValidator_return retval = new ModelFileParser.parameterIDValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:95:2: ( ID EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:95:4: ID EOF
            {
            match(input,ID,FOLLOW_ID_in_parameterIDValidator236); if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_parameterIDValidator238); if (state.failed) return retval;

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
    // $ANTLR end "parameterIDValidator"

    public static class parameterValueValidator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameterValueValidator"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:97:1: parameterValueValidator[Set referencedParameters] : parameterValue[referencedParameters] EOF ;
    public final ModelFileParser.parameterValueValidator_return parameterValueValidator(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValueValidator_return retval = new ModelFileParser.parameterValueValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:98:2: ( parameterValue[referencedParameters] EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:98:4: parameterValue[referencedParameters] EOF
            {
            pushFollow(FOLLOW_parameterValue_in_parameterValueValidator249);
            parameterValue(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_parameterValueValidator252); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:100:1: parameterValue[Set referencedParameters] : arithmeticParameterExpression[referencedParameters] ;
    public final ModelFileParser.parameterValue_return parameterValue(Set referencedParameters) throws RecognitionException {
        ModelFileParser.parameterValue_return retval = new ModelFileParser.parameterValue_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:101:2: ( arithmeticParameterExpression[referencedParameters] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:101:4: arithmeticParameterExpression[referencedParameters]
            {
            pushFollow(FOLLOW_arithmeticParameterExpression_in_parameterValue263);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:103:1: arithmeticParameterExpression[Set referencedParameters] : arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterExpression_return arithmeticParameterExpression(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterExpression_return retval = new ModelFileParser.arithmeticParameterExpression_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:104:2: ( arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:104:4: arithmeticParameterTerm[referencedParameters] ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression276);
            arithmeticParameterTerm(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:104:50: ( ( '+' | '-' ) arithmeticParameterTerm[referencedParameters] )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=11 && LA4_0<=12)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:104:51: ( '+' | '-' ) arithmeticParameterTerm[referencedParameters]
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

            	    pushFollow(FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression288);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:106:1: arithmeticParameterTerm[Set referencedParameters] : arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* ;
    public final ModelFileParser.arithmeticParameterTerm_return arithmeticParameterTerm(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterTerm_return retval = new ModelFileParser.arithmeticParameterTerm_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:107:2: ( arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:107:4: arithmeticParameterFactor[referencedParameters] ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            {
            pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm302);
            arithmeticParameterFactor(referencedParameters);

            state._fsp--;
            if (state.failed) return retval;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:107:52: ( ( '*' | '/' ) arithmeticParameterFactor[referencedParameters] )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=13 && LA5_0<=14)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:107:53: ( '*' | '/' ) arithmeticParameterFactor[referencedParameters]
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

            	    pushFollow(FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm314);
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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:109:1: arithmeticParameterFactor[Set referencedParameters] : ( parameterID | number );
    public final ModelFileParser.arithmeticParameterFactor_return arithmeticParameterFactor(Set referencedParameters) throws RecognitionException {
        ModelFileParser.arithmeticParameterFactor_return retval = new ModelFileParser.arithmeticParameterFactor_return();
        retval.start = input.LT(1);

        ModelFileParser.parameterID_return parameterID3 = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:110:2: ( parameterID | number )
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
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:110:4: parameterID
                    {
                    pushFollow(FOLLOW_parameterID_in_arithmeticParameterFactor328);
                    parameterID3=parameterID();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                       referencedParameters.add((parameterID3!=null?input.toString(parameterID3.start,parameterID3.stop):null)); 
                    }

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:111:4: number
                    {
                    pushFollow(FOLLOW_number_in_arithmeticParameterFactor335);
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

    public static class transitionRatio_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transitionRatio"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:116:1: transitionRatio : parameterValue[new HashSet()] ;
    public final ModelFileParser.transitionRatio_return transitionRatio() throws RecognitionException {
        ModelFileParser.transitionRatio_return retval = new ModelFileParser.transitionRatio_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:117:2: ( parameterValue[new HashSet()] )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:117:4: parameterValue[new HashSet()]
            {
            pushFollow(FOLLOW_parameterValue_in_transitionRatio348);
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
    // $ANTLR end "transitionRatio"

    public static class transition_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "transition"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:119:1: transition : ( ratioTransition transitionRatio ( 'secondary' )? | infectionTransition transitionRatio ( 'secondary' )? );
    public final ModelFileParser.transition_return transition() throws RecognitionException {
        ModelFileParser.transition_return retval = new ModelFileParser.transition_return();
        retval.start = input.LT(1);

        ModelFileParser.ratioTransition_return ratioTransition4 = null;

        ModelFileParser.transitionRatio_return transitionRatio5 = null;

        ModelFileParser.infectionTransition_return infectionTransition6 = null;

        ModelFileParser.transitionRatio_return transitionRatio7 = null;


         boolean isSecondary = false; 
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:121:2: ( ratioTransition transitionRatio ( 'secondary' )? | infectionTransition transitionRatio ( 'secondary' )? )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==ID) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==17) ) {
                    alt9=2;
                }
                else if ( (LA9_1==16) ) {
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
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:121:4: ratioTransition transitionRatio ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_ratioTransition_in_transition365);
                    ratioTransition4=ratioTransition();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRatio_in_transition367);
                    transitionRatio5=transitionRatio();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:121:36: ( 'secondary' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==15) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:121:37: 'secondary'
                            {
                            match(input,15,FOLLOW_15_in_transition370); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                               isSecondary = true; 
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                      		Compartment target = model.getOrAddCompartment((ratioTransition4!=null?ratioTransition4.target:null));
                      		target.setSecondary(isSecondary);
                      		
                      		model.addRatioTransition(
                      			model.getOrAddCompartment((ratioTransition4!=null?ratioTransition4.source:null)),
                      			target,
                      			(transitionRatio5!=null?input.toString(transitionRatio5.start,transitionRatio5.stop):null));
                      		
                    }

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:131:4: infectionTransition transitionRatio ( 'secondary' )?
                    {
                    pushFollow(FOLLOW_infectionTransition_in_transition383);
                    infectionTransition6=infectionTransition();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_transitionRatio_in_transition385);
                    transitionRatio7=transitionRatio();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:131:40: ( 'secondary' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==15) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:131:41: 'secondary'
                            {
                            match(input,15,FOLLOW_15_in_transition388); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                               isSecondary = true; 
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                      		Compartment target = model.getOrAddCompartment((infectionTransition6!=null?infectionTransition6.target:null));
                      		target.setSecondary(isSecondary);
                      		
                      		model.addInfectionTransition(
                      			model.getOrAddCompartment((infectionTransition6!=null?infectionTransition6.source:null)),
                      			model.getOrAddCompartment((infectionTransition6!=null?infectionTransition6.infector:null)),
                      			target,
                      			(transitionRatio7!=null?input.toString(transitionRatio7.start,transitionRatio7.stop):null));
                      		
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
    // $ANTLR end "transition"

    public static class ratioTransition_return extends ParserRuleReturnScope {
        public String source;
        public String target;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "ratioTransition"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:144:1: ratioTransition returns [String source, String target] : s= compartmentID '->' t= compartmentID ;
    public final ModelFileParser.ratioTransition_return ratioTransition() throws RecognitionException {
        ModelFileParser.ratioTransition_return retval = new ModelFileParser.ratioTransition_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:145:2: (s= compartmentID '->' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:145:4: s= compartmentID '->' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_ratioTransition418);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,16,FOLLOW_16_in_ratioTransition420); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_ratioTransition424);
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
    // $ANTLR end "ratioTransition"

    public static class infectionTransition_return extends ParserRuleReturnScope {
        public String source;
        public String infector;
        public String target;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "infectionTransition"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:151:1: infectionTransition returns [String source, String infector, String target] : s= compartmentID '--' i= compartmentID '=' t= compartmentID ;
    public final ModelFileParser.infectionTransition_return infectionTransition() throws RecognitionException {
        ModelFileParser.infectionTransition_return retval = new ModelFileParser.infectionTransition_return();
        retval.start = input.LT(1);

        ModelFileParser.compartmentID_return s = null;

        ModelFileParser.compartmentID_return i = null;

        ModelFileParser.compartmentID_return t = null;


        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:152:2: (s= compartmentID '--' i= compartmentID '=' t= compartmentID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:152:4: s= compartmentID '--' i= compartmentID '=' t= compartmentID
            {
            pushFollow(FOLLOW_compartmentID_in_infectionTransition444);
            s=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,17,FOLLOW_17_in_infectionTransition446); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_infectionTransition450);
            i=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,10,FOLLOW_10_in_infectionTransition452); if (state.failed) return retval;
            pushFollow(FOLLOW_compartmentID_in_infectionTransition456);
            t=compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {

              		retval.source = (s!=null?input.toString(s.start,s.stop):null);
              		retval.infector = (i!=null?input.toString(i.start,i.stop):null);
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
    // $ANTLR end "infectionTransition"

    public static class compartmentID_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compartmentID"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:159:1: compartmentID : ID ;
    public final ModelFileParser.compartmentID_return compartmentID() throws RecognitionException {
        ModelFileParser.compartmentID_return retval = new ModelFileParser.compartmentID_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:160:2: ( ID )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:160:4: ID
            {
            match(input,ID,FOLLOW_ID_in_compartmentID470); if (state.failed) return retval;

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
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:162:1: compartmentIDValidator : compartmentID EOF ;
    public final ModelFileParser.compartmentIDValidator_return compartmentIDValidator() throws RecognitionException {
        ModelFileParser.compartmentIDValidator_return retval = new ModelFileParser.compartmentIDValidator_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:163:2: ( compartmentID EOF )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:163:4: compartmentID EOF
            {
            pushFollow(FOLLOW_compartmentID_in_compartmentIDValidator480);
            compartmentID();

            state._fsp--;
            if (state.failed) return retval;
            match(input,EOF,FOLLOW_EOF_in_compartmentIDValidator482); if (state.failed) return retval;

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

    public static class number_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "number"
    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:198:1: number : ( '-' )? UNSIGNED_NUMBER ;
    public final ModelFileParser.number_return number() throws RecognitionException {
        ModelFileParser.number_return retval = new ModelFileParser.number_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:199:2: ( ( '-' )? UNSIGNED_NUMBER )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:199:4: ( '-' )? UNSIGNED_NUMBER
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:199:4: ( '-' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==12) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:199:5: '-'
                    {
                    match(input,12,FOLLOW_12_in_number555); if (state.failed) return retval;

                    }
                    break;

            }

            match(input,UNSIGNED_NUMBER,FOLLOW_UNSIGNED_NUMBER_in_number559); if (state.failed) return retval;

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
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:74:4: ( ID '=' ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:74:5: ID '=' ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred1_ModelFile112); if (state.failed) return ;
        match(input,10,FOLLOW_10_in_synpred1_ModelFile114); if (state.failed) return ;
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:74:12: ( . )*
        loop11:
        do {
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=WHITESPACE && LA11_0<=17)) ) {
                alt11=1;
            }
            else if ( (LA11_0==EOF) ) {
                alt11=2;
            }


            switch (alt11) {
        	case 1 :
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:74:12: .
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
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:75:4: ( ID ( '--' | '->' ) ( . )* )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:75:5: ID ( '--' | '->' ) ( . )*
        {
        match(input,ID,FOLLOW_ID_in_synpred2_ModelFile127); if (state.failed) return ;
        if ( (input.LA(1)>=16 && input.LA(1)<=17) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:75:22: ( . )*
        loop12:
        do {
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( ((LA12_0>=WHITESPACE && LA12_0<=17)) ) {
                alt12=1;
            }
            else if ( (LA12_0==EOF) ) {
                alt12=2;
            }


            switch (alt12) {
        	case 1 :
        	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\converters\\text\\ModelFile.g:75:22: .
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


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\73\uffff";
    static final String DFA2_eofS =
        "\1\uffff\1\4\6\uffff\1\4\1\uffff\1\4\5\uffff\1\4\1\uffff\2\4\1"+
        "\uffff\2\4\1\uffff\1\4\3\uffff\1\4\1\uffff\1\4\1\uffff\2\4\1\uffff"+
        "\2\4\1\uffff\2\4\1\uffff\1\4\2\uffff\1\4\1\uffff\1\4\1\uffff\2\4"+
        "\1\uffff\2\4\1\uffff\1\4\1\uffff\1\4\1\uffff\1\4";
    static final String DFA2_minS =
        "\2\5\1\12\2\uffff\3\7\1\5\1\11\1\5\1\12\4\7\1\5\1\11\2\5\1\11\2"+
        "\5\1\11\1\5\3\7\1\5\1\7\1\5\1\11\2\5\1\11\2\5\1\11\2\5\1\11\1\5"+
        "\2\7\1\5\1\7\1\5\1\11\2\5\1\11\2\5\1\11\1\5\1\7\1\5\1\11\1\5";
    static final String DFA2_maxS =
        "\1\7\1\5\1\21\2\uffff\1\14\2\7\1\16\1\11\1\16\1\12\3\14\1\7\1\17"+
        "\1\11\1\17\1\16\1\11\2\16\1\11\1\16\3\14\1\5\1\14\1\17\1\11\2\17"+
        "\1\11\2\17\1\11\1\17\1\16\1\11\1\16\2\14\1\5\1\14\1\17\1\11\2\17"+
        "\1\11\2\17\1\11\1\17\1\14\1\17\1\11\1\17";
    static final String DFA2_acceptS =
        "\3\uffff\1\2\1\1\66\uffff";
    static final String DFA2_specialS =
        "\73\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\3\1\1\1\2",
            "\1\3",
            "\1\5\5\uffff\1\7\1\6",
            "",
            "",
            "\1\10\1\uffff\1\12\2\uffff\1\11",
            "\1\13",
            "\1\14",
            "\1\3\5\uffff\2\16\2\15",
            "\1\12",
            "\1\3\5\uffff\2\16\2\15",
            "\1\17",
            "\1\20\1\uffff\1\22\2\uffff\1\21",
            "\1\23\1\uffff\1\25\2\uffff\1\24",
            "\1\26\1\uffff\1\30\2\uffff\1\27",
            "\1\31",
            "\1\3\5\uffff\2\33\2\32\1\34",
            "\1\22",
            "\1\3\5\uffff\2\33\2\32\1\34",
            "\1\3\5\uffff\2\16\2\15",
            "\1\25",
            "\1\3\5\uffff\2\16\2\15",
            "\1\3\5\uffff\2\16\2\35",
            "\1\30",
            "\1\3\5\uffff\2\16\2\35",
            "\1\36\1\uffff\1\40\2\uffff\1\37",
            "\1\41\1\uffff\1\43\2\uffff\1\42",
            "\1\44\1\uffff\1\46\2\uffff\1\45",
            "\1\3",
            "\1\47\1\uffff\1\51\2\uffff\1\50",
            "\1\3\5\uffff\2\53\2\52\1\54",
            "\1\40",
            "\1\3\5\uffff\2\53\2\52\1\54",
            "\1\3\5\uffff\2\33\2\32\1\34",
            "\1\43",
            "\1\3\5\uffff\2\33\2\32\1\34",
            "\1\3\5\uffff\2\33\2\55\1\34",
            "\1\46",
            "\1\3\5\uffff\2\33\2\55\1\34",
            "\1\3\5\uffff\2\16\2\35",
            "\1\51",
            "\1\3\5\uffff\2\16\2\35",
            "\1\56\1\uffff\1\60\2\uffff\1\57",
            "\1\61\1\uffff\1\63\2\uffff\1\62",
            "\1\3",
            "\1\64\1\uffff\1\66\2\uffff\1\65",
            "\1\3\5\uffff\2\53\2\52\1\54",
            "\1\60",
            "\1\3\5\uffff\2\53\2\52\1\54",
            "\1\3\5\uffff\2\53\2\67\1\54",
            "\1\63",
            "\1\3\5\uffff\2\53\2\67\1\54",
            "\1\3\5\uffff\2\33\2\55\1\34",
            "\1\66",
            "\1\3\5\uffff\2\33\2\55\1\34",
            "\1\70\1\uffff\1\72\2\uffff\1\71",
            "\1\3\5\uffff\2\53\2\67\1\54",
            "\1\72",
            "\1\3\5\uffff\2\53\2\67\1\54"
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
    public static final BitSet FOLLOW_NEWLINE_in_modelFile84 = new BitSet(new long[]{0x00000000000000E0L});
    public static final BitSet FOLLOW_line_in_modelFile89 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_modelFile91 = new BitSet(new long[]{0x00000000000000E0L});
    public static final BitSet FOLLOW_EOF_in_modelFile96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMENT_in_line106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterAssignment_in_line121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transition_in_line142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterID_in_parameterAssignment203 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_parameterAssignment205 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_parameterValue_in_parameterAssignment207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_parameterID226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_parameterIDValidator236 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parameterIDValidator238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_parameterValueValidator249 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parameterValueValidator252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterExpression_in_parameterValue263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression276 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterExpression280 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterTerm_in_arithmeticParameterExpression288 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm302 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_set_in_arithmeticParameterTerm306 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_arithmeticParameterFactor_in_arithmeticParameterTerm314 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_parameterID_in_arithmeticParameterFactor328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_arithmeticParameterFactor335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameterValue_in_transitionRatio348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ratioTransition_in_transition365 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRatio_in_transition367 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_transition370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_infectionTransition_in_transition383 = new BitSet(new long[]{0x0000000000001280L});
    public static final BitSet FOLLOW_transitionRatio_in_transition385 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_transition388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_ratioTransition418 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ratioTransition420 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_ratioTransition424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_infectionTransition444 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_infectionTransition446 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_infectionTransition450 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_infectionTransition452 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_compartmentID_in_infectionTransition456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_compartmentID470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compartmentID_in_compartmentIDValidator480 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_compartmentIDValidator482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_number555 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_UNSIGNED_NUMBER_in_number559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred1_ModelFile112 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_synpred1_ModelFile114 = new BitSet(new long[]{0x000000000003FFF2L});
    public static final BitSet FOLLOW_ID_in_synpred2_ModelFile127 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_set_in_synpred2_ModelFile129 = new BitSet(new long[]{0x000000000003FFF2L});

}