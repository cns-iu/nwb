// $ANTLR 3.1.2 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g 2009-09-22 16:49:34
package edu.iu.epic.spemshell.runner.parsing.generated;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class ModelFileLexer extends Lexer {
    public static final int T__28=28;
    public static final int COMMENT_MARKER=9;
    public static final int T__27=27;
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
    public static final int DECIMAL_POINT=8;
    public static final int UNSIGNED_INTEGER=23;
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

    public ModelFileLexer() {} 
    public ModelFileLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ModelFileLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g"; }

    // $ANTLR start "ADD"
    public final void mADD() throws RecognitionException {
        try {
            int _type = ADD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:5:5: ( '+' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:5:7: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ADD"

    // $ANTLR start "SUB"
    public final void mSUB() throws RecognitionException {
        try {
            int _type = SUB;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:6:5: ( '-' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:6:7: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUB"

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:7:6: ( '*' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:7:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULT"

    // $ANTLR start "DIV"
    public final void mDIV() throws RecognitionException {
        try {
            int _type = DIV;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:8:5: ( '/' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:8:7: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIV"

    // $ANTLR start "DECIMAL_POINT"
    public final void mDECIMAL_POINT() throws RecognitionException {
        try {
            int _type = DECIMAL_POINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:9:15: ( '.' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:9:17: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DECIMAL_POINT"

    // $ANTLR start "COMMENT_MARKER"
    public final void mCOMMENT_MARKER() throws RecognitionException {
        try {
            int _type = COMMENT_MARKER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:10:16: ( '#' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:10:18: '#'
            {
            match('#'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT_MARKER"

    // $ANTLR start "SPONTANEOUS_INTO"
    public final void mSPONTANEOUS_INTO() throws RecognitionException {
        try {
            int _type = SPONTANEOUS_INTO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:11:18: ( '->' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:11:20: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPONTANEOUS_INTO"

    // $ANTLR start "INTERACTED_ON_BY"
    public final void mINTERACTED_ON_BY() throws RecognitionException {
        try {
            int _type = INTERACTED_ON_BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:12:18: ( '--' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:12:20: '--'
            {
            match("--"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTERACTED_ON_BY"

    // $ANTLR start "INTERACTS_INTO"
    public final void mINTERACTS_INTO() throws RecognitionException {
        try {
            int _type = INTERACTS_INTO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:13:16: ( '=' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:13:18: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTERACTS_INTO"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:14:7: ( 'susceptible' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:14:9: 'susceptible'
            {
            match("susceptible"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:15:7: ( 'infection' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:15:9: 'infection'
            {
            match("infection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:16:7: ( 'latent' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:16:9: 'latent'
            {
            match("latent"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:17:7: ( 'recovered' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:17:9: 'recovered'
            {
            match("recovered"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:69:2: ( ( '\\t' | ' ' )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:69:4: ( '\\t' | ' ' )+
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:69:4: ( '\\t' | ' ' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:81:2: ( '\\r' | '\\n' | '\\r\\n' )
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\r') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='\n') ) {
                    alt2=3;
                }
                else {
                    alt2=1;}
            }
            else if ( (LA2_0=='\n') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:81:4: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:82:4: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:83:4: '\\r\\n'
                    {
                    match("\r\n"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:87:2: ( COMMENT_MARKER ( . )* NEWLINE )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:87:4: COMMENT_MARKER ( . )* NEWLINE
            {
            mCOMMENT_MARKER(); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:87:19: ( . )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\r') ) {
                    alt3=2;
                }
                else if ( (LA3_0=='\n') ) {
                    alt3=2;
                }
                else if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:87:19: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            mNEWLINE(); 
            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "NUMERIC"
    public final void mNUMERIC() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:162:2: ( '0' .. '9' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:162:4: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NUMERIC"

    // $ANTLR start "ALPHABETIC"
    public final void mALPHABETIC() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:165:2: ( 'a' .. 'z' | 'A' .. 'Z' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHABETIC"

    // $ANTLR start "ALPHABETIC_"
    public final void mALPHABETIC_() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:169:2: ( ALPHABETIC | '_' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHABETIC_"

    // $ANTLR start "ALPHANUMERIC"
    public final void mALPHANUMERIC() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:173:2: ( ALPHABETIC | NUMERIC )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHANUMERIC"

    // $ANTLR start "ALPHANUMERIC_"
    public final void mALPHANUMERIC_() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:177:2: ( ALPHANUMERIC | '_' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHANUMERIC_"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:181:2: ( ALPHABETIC_ ( ALPHANUMERIC_ )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:181:4: ALPHABETIC_ ( ALPHANUMERIC_ )*
            {
            mALPHABETIC_(); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:181:16: ( ALPHANUMERIC_ )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:181:17: ALPHANUMERIC_
            	    {
            	    mALPHANUMERIC_(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "UNSIGNED_INTEGER"
    public final void mUNSIGNED_INTEGER() throws RecognitionException {
        try {
            int _type = UNSIGNED_INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:2: ( ( NUMERIC )+ ( DECIMAL_POINT )? )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:4: ( NUMERIC )+ ( DECIMAL_POINT )?
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:4: ( NUMERIC )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:5: NUMERIC
            	    {
            	    mNUMERIC(); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:15: ( DECIMAL_POINT )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='.') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:190:16: DECIMAL_POINT
                    {
                    mDECIMAL_POINT(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNSIGNED_INTEGER"

    // $ANTLR start "UNSIGNED_REAL"
    public final void mUNSIGNED_REAL() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:2: ( ( ( NUMERIC )+ )? DECIMAL_POINT ( NUMERIC )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:4: ( ( NUMERIC )+ )? DECIMAL_POINT ( NUMERIC )+
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:4: ( ( NUMERIC )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:5: ( NUMERIC )+
                    {
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:5: ( NUMERIC )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:6: NUMERIC
                    	    {
                    	    mNUMERIC(); 

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
                    break;

            }

            mDECIMAL_POINT(); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:32: ( NUMERIC )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:193:33: NUMERIC
            	    {
            	    mNUMERIC(); 

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "UNSIGNED_REAL"

    // $ANTLR start "UNSIGNED_NUMBER"
    public final void mUNSIGNED_NUMBER() throws RecognitionException {
        try {
            int _type = UNSIGNED_NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:196:2: ( UNSIGNED_INTEGER | UNSIGNED_REAL )
            int alt10=2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:196:4: UNSIGNED_INTEGER
                    {
                    mUNSIGNED_INTEGER(); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:197:4: UNSIGNED_REAL
                    {
                    mUNSIGNED_REAL(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNSIGNED_NUMBER"

    public void mTokens() throws RecognitionException {
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:8: ( ADD | SUB | MULT | DIV | DECIMAL_POINT | COMMENT_MARKER | SPONTANEOUS_INTO | INTERACTED_ON_BY | INTERACTS_INTO | T__25 | T__26 | T__27 | T__28 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_INTEGER | UNSIGNED_NUMBER )
        int alt11=19;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:10: ADD
                {
                mADD(); 

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:14: SUB
                {
                mSUB(); 

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:18: MULT
                {
                mMULT(); 

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:23: DIV
                {
                mDIV(); 

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:27: DECIMAL_POINT
                {
                mDECIMAL_POINT(); 

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:41: COMMENT_MARKER
                {
                mCOMMENT_MARKER(); 

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:56: SPONTANEOUS_INTO
                {
                mSPONTANEOUS_INTO(); 

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:73: INTERACTED_ON_BY
                {
                mINTERACTED_ON_BY(); 

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:90: INTERACTS_INTO
                {
                mINTERACTS_INTO(); 

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:105: T__25
                {
                mT__25(); 

                }
                break;
            case 11 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:111: T__26
                {
                mT__26(); 

                }
                break;
            case 12 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:117: T__27
                {
                mT__27(); 

                }
                break;
            case 13 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:123: T__28
                {
                mT__28(); 

                }
                break;
            case 14 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:129: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 15 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:140: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 16 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:148: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 17 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:156: ID
                {
                mID(); 

                }
                break;
            case 18 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:159: UNSIGNED_INTEGER
                {
                mUNSIGNED_INTEGER(); 

                }
                break;
            case 19 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\parsing\\ModelFile.g:1:176: UNSIGNED_NUMBER
                {
                mUNSIGNED_NUMBER(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA10_eotS =
        "\1\uffff\1\3\2\uffff\1\3";
    static final String DFA10_eofS =
        "\5\uffff";
    static final String DFA10_minS =
        "\2\56\2\uffff\1\60";
    static final String DFA10_maxS =
        "\2\71\2\uffff\1\71";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\1\1\1\uffff";
    static final String DFA10_specialS =
        "\5\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\4\1\uffff\12\1",
            "",
            "",
            "\12\2"
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "195:1: UNSIGNED_NUMBER : ( UNSIGNED_INTEGER | UNSIGNED_REAL );";
        }
    }
    static final String DFA11_eotS =
        "\2\uffff\1\22\2\uffff\1\23\1\25\1\uffff\4\16\3\uffff\1\33\7\uffff"+
        "\4\16\1\uffff\1\33\16\16\1\57\3\16\1\uffff\5\16\1\70\1\71\1\16\2"+
        "\uffff\1\73\1\uffff";
    static final String DFA11_eofS =
        "\74\uffff";
    static final String DFA11_minS =
        "\1\11\1\uffff\1\55\2\uffff\1\60\1\0\1\uffff\1\165\1\156\1\141\1"+
        "\145\3\uffff\1\56\7\uffff\1\163\1\146\1\164\1\143\1\uffff\1\60\1"+
        "\143\2\145\1\157\1\145\1\143\1\156\1\166\1\160\2\164\1\145\1\164"+
        "\1\151\1\60\1\162\1\151\1\157\1\uffff\1\145\1\142\1\156\1\144\1"+
        "\154\2\60\1\145\2\uffff\1\60\1\uffff";
    static final String DFA11_maxS =
        "\1\172\1\uffff\1\76\2\uffff\1\71\1\uffff\1\uffff\1\165\1\156\1"+
        "\141\1\145\3\uffff\1\71\7\uffff\1\163\1\146\1\164\1\143\1\uffff"+
        "\1\71\1\143\2\145\1\157\1\145\1\143\1\156\1\166\1\160\2\164\1\145"+
        "\1\164\1\151\1\172\1\162\1\151\1\157\1\uffff\1\145\1\142\1\156\1"+
        "\144\1\154\2\172\1\145\2\uffff\1\172\1\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\4\2\uffff\1\11\4\uffff\1\16\1\17\1\21"+
        "\1\uffff\1\7\1\10\1\2\1\5\1\23\1\6\1\20\4\uffff\1\22\23\uffff\1"+
        "\14\10\uffff\1\13\1\15\1\uffff\1\12";
    static final String DFA11_specialS =
        "\6\uffff\1\0\65\uffff}>";
    static final String[] DFA11_transitionS = {
            "\1\14\1\15\2\uffff\1\15\22\uffff\1\14\2\uffff\1\6\6\uffff\1"+
            "\3\1\1\1\uffff\1\2\1\5\1\4\12\17\3\uffff\1\7\3\uffff\32\16\4"+
            "\uffff\1\16\1\uffff\10\16\1\11\2\16\1\12\5\16\1\13\1\10\7\16",
            "",
            "\1\21\20\uffff\1\20",
            "",
            "",
            "\12\24",
            "\0\26",
            "",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "",
            "",
            "",
            "\1\34\1\uffff\12\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "",
            "\12\24",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\1\72",
            "",
            "",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ADD | SUB | MULT | DIV | DECIMAL_POINT | COMMENT_MARKER | SPONTANEOUS_INTO | INTERACTED_ON_BY | INTERACTS_INTO | T__25 | T__26 | T__27 | T__28 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_INTEGER | UNSIGNED_NUMBER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_6 = input.LA(1);

                        s = -1;
                        if ( ((LA11_6>='\u0000' && LA11_6<='\uFFFF')) ) {s = 22;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}