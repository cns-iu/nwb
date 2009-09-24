// $ANTLR 3.1.2 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g 2009-09-24 12:13:00

package edu.iu.epic.spemshell.runner.preprocessing.parsing.generated;


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

@SuppressWarnings("all")

public class ModelFileLexer extends Lexer {
    public static final int COMMENT_MARKER=9;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int SPONTANEOUS_INTO=10;
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

    public ModelFileLexer() {;} 
    public ModelFileLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ModelFileLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g"; }

    // $ANTLR start "ADD"
    public final void mADD() throws RecognitionException {
        try {
            int _type = ADD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:7:5: ( '+' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:7:7: '+'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:8:5: ( '-' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:8:7: '-'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:9:6: ( '*' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:9:8: '*'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:10:5: ( '/' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:10:7: '/'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:11:15: ( '.' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:11:17: '.'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:12:16: ( '#' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:12:18: '#'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:13:18: ( '->' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:13:20: '->'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:14:18: ( '--' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:14:20: '--'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:15:16: ( '=' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:15:18: '='
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

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:16:7: ( 'susceptible' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:16:9: 'susceptible'
            {
            match("susceptible"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:17:7: ( 'infection' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:17:9: 'infection'
            {
            match("infection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:18:7: ( 'latent' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:18:9: 'latent'
            {
            match("latent"); 


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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:19:7: ( 'recovered' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:19:9: 'recovered'
            {
            match("recovered"); 


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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:20:7: ( 'secondary' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:20:9: 'secondary'
            {
            match("secondary"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:72:2: ( ( '\\t' | ' ' )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:72:4: ( '\\t' | ' ' )+
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:72:4: ( '\\t' | ' ' )+
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
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:84:2: ( '\\r' | '\\n' | '\\r\\n' )
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
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:84:4: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:85:4: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:86:4: '\\r\\n'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:90:2: ( COMMENT_MARKER ( . )* NEWLINE )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:90:4: COMMENT_MARKER ( . )* NEWLINE
            {
            mCOMMENT_MARKER(); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:90:19: ( . )*
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
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:90:19: .
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:161:2: ( '0' .. '9' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:161:4: '0' .. '9'
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:164:2: ( 'a' .. 'z' | 'A' .. 'Z' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:168:2: ( ALPHABETIC | '_' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:172:2: ( ALPHABETIC | NUMERIC )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:176:2: ( ALPHANUMERIC | '_' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:180:2: ( ALPHABETIC_ ( ALPHANUMERIC_ )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:180:4: ALPHABETIC_ ( ALPHANUMERIC_ )*
            {
            mALPHABETIC_(); 
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:180:16: ( ALPHANUMERIC_ )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:180:17: ALPHANUMERIC_
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

    // $ANTLR start "UNSIGNED_NUMBER"
    public final void mUNSIGNED_NUMBER() throws RecognitionException {
        try {
            int _type = UNSIGNED_NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:2: ( ( NUMERIC )+ ( DECIMAL_POINT ( NUMERIC )+ )? )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:4: ( NUMERIC )+ ( DECIMAL_POINT ( NUMERIC )+ )?
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:4: ( NUMERIC )+
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
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:5: NUMERIC
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

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:15: ( DECIMAL_POINT ( NUMERIC )+ )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='.') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:16: DECIMAL_POINT ( NUMERIC )+
                    {
                    mDECIMAL_POINT(); 
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:30: ( NUMERIC )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:188:31: NUMERIC
                    	    {
                    	    mNUMERIC(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);


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
    // $ANTLR end "UNSIGNED_NUMBER"

    public void mTokens() throws RecognitionException {
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:8: ( ADD | SUB | MULT | DIV | DECIMAL_POINT | COMMENT_MARKER | SPONTANEOUS_INTO | INTERACTED_ON_BY | INTERACTS_INTO | T__23 | T__24 | T__25 | T__26 | T__27 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_NUMBER )
        int alt8=19;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:10: ADD
                {
                mADD(); 

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:14: SUB
                {
                mSUB(); 

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:18: MULT
                {
                mMULT(); 

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:23: DIV
                {
                mDIV(); 

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:27: DECIMAL_POINT
                {
                mDECIMAL_POINT(); 

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:41: COMMENT_MARKER
                {
                mCOMMENT_MARKER(); 

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:56: SPONTANEOUS_INTO
                {
                mSPONTANEOUS_INTO(); 

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:73: INTERACTED_ON_BY
                {
                mINTERACTED_ON_BY(); 

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:90: INTERACTS_INTO
                {
                mINTERACTS_INTO(); 

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:105: T__23
                {
                mT__23(); 

                }
                break;
            case 11 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:111: T__24
                {
                mT__24(); 

                }
                break;
            case 12 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:117: T__25
                {
                mT__25(); 

                }
                break;
            case 13 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:123: T__26
                {
                mT__26(); 

                }
                break;
            case 14 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:129: T__27
                {
                mT__27(); 

                }
                break;
            case 15 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:135: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 16 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:146: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 17 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:154: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 18 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:162: ID
                {
                mID(); 

                }
                break;
            case 19 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.spemshell.runner\\src\\edu\\iu\\epic\\spemshell\\runner\\preprocessing\\parsing\\ModelFile.g:1:165: UNSIGNED_NUMBER
                {
                mUNSIGNED_NUMBER(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\2\uffff\1\22\3\uffff\1\23\1\uffff\4\16\11\uffff\27\16\1\61\4\16"+
        "\1\uffff\6\16\1\74\1\75\1\76\1\16\3\uffff\1\100\1\uffff";
    static final String DFA8_eofS =
        "\101\uffff";
    static final String DFA8_minS =
        "\1\11\1\uffff\1\55\3\uffff\1\0\1\uffff\1\145\1\156\1\141\1\145"+
        "\11\uffff\1\163\1\143\1\146\1\164\2\143\1\157\2\145\1\157\1\145"+
        "\1\156\1\143\1\156\1\166\1\160\1\144\2\164\1\145\1\164\1\141\1\151"+
        "\1\60\1\162\1\151\1\162\1\157\1\uffff\1\145\1\142\1\171\1\156\1"+
        "\144\1\154\3\60\1\145\3\uffff\1\60\1\uffff";
    static final String DFA8_maxS =
        "\1\172\1\uffff\1\76\3\uffff\1\uffff\1\uffff\1\165\1\156\1\141\1"+
        "\145\11\uffff\1\163\1\143\1\146\1\164\2\143\1\157\2\145\1\157\1"+
        "\145\1\156\1\143\1\156\1\166\1\160\1\144\2\164\1\145\1\164\1\141"+
        "\1\151\1\172\1\162\1\151\1\162\1\157\1\uffff\1\145\1\142\1\171\1"+
        "\156\1\144\1\154\3\172\1\145\3\uffff\1\172\1\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\4\1\5\1\uffff\1\11\4\uffff\1\17\1\20"+
        "\1\22\1\23\1\7\1\10\1\2\1\6\1\21\34\uffff\1\14\12\uffff\1\16\1\13"+
        "\1\15\1\uffff\1\12";
    static final String DFA8_specialS =
        "\6\uffff\1\0\72\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\14\1\15\2\uffff\1\15\22\uffff\1\14\2\uffff\1\6\6\uffff\1"+
            "\3\1\1\1\uffff\1\2\1\5\1\4\12\17\3\uffff\1\7\3\uffff\32\16\4"+
            "\uffff\1\16\1\uffff\10\16\1\11\2\16\1\12\5\16\1\13\1\10\7\16",
            "",
            "\1\21\20\uffff\1\20",
            "",
            "",
            "",
            "\0\24",
            "",
            "\1\26\17\uffff\1\25",
            "\1\27",
            "\1\30",
            "\1\31",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
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
            "\1\57",
            "\1\60",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            "\1\77",
            "",
            "",
            "",
            "\12\16\7\uffff\32\16\4\uffff\1\16\1\uffff\32\16",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ADD | SUB | MULT | DIV | DECIMAL_POINT | COMMENT_MARKER | SPONTANEOUS_INTO | INTERACTED_ON_BY | INTERACTS_INTO | T__23 | T__24 | T__25 | T__26 | T__27 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_NUMBER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_6 = input.LA(1);

                        s = -1;
                        if ( ((LA8_6>='\u0000' && LA8_6<='\uFFFF')) ) {s = 20;}

                        else s = 19;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}