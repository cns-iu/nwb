// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g 2009-11-11 16:04:11

package edu.iu.epic.modeling.compartment.grammar.parsing;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class ModelFileLexer extends Lexer {
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

    public ModelFileLexer() {;} 
    public ModelFileLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ModelFileLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g"; }

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:7:7: ( '=' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:7:9: '='
            {
            match('='); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:8:7: ( '+' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:8:9: '+'
            {
            match('+'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:9:7: ( '-' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:9:9: '-'
            {
            match('-'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:10:7: ( '*' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:10:9: '*'
            {
            match('*'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:11:7: ( '/' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:11:9: '/'
            {
            match('/'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:12:7: ( 'susceptible' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:12:9: 'susceptible'
            {
            match("susceptible"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:13:7: ( 'infection' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:13:9: 'infection'
            {
            match("infection"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:14:7: ( 'latent' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:14:9: 'latent'
            {
            match("latent"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:15:7: ( 'recovered' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:15:9: 'recovered'
            {
            match("recovered"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:16:7: ( 'secondary' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:16:9: 'secondary'
            {
            match("secondary"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:17:7: ( '->' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:17:9: '->'
            {
            match("->"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:18:7: ( '--' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:18:9: '--'
            {
            match("--"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:63:2: ( ( '\\t' | ' ' )+ )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:63:4: ( '\\t' | ' ' )+
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:63:4: ( '\\t' | ' ' )+
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
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            if ( state.backtracking==0 ) {
              _channel = HIDDEN;
            }

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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:2: ( ( ( '\\r' )? '\\n' )=> ( '\\r' )? '\\n' | '\\r' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1=='\n') && (synpred1_ModelFile())) {
                    alt3=1;
                }
                else {
                    alt3=2;}
            }
            else if ( (LA3_0=='\n') && (synpred1_ModelFile())) {
                alt3=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:4: ( ( '\\r' )? '\\n' )=> ( '\\r' )? '\\n'
                    {
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:19: ( '\\r' )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0=='\r') ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:19: '\\r'
                            {
                            match('\r'); if (state.failed) return ;

                            }
                            break;

                    }

                    match('\n'); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:81:4: '\\r'
                    {
                    match('\r'); if (state.failed) return ;

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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:84:2: ( '#' (~ ( '\\n' | '\\r' ) )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:84:4: '#' (~ ( '\\n' | '\\r' ) )*
            {
            match('#'); if (state.failed) return ;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:84:8: (~ ( '\\n' | '\\r' ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:84:9: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            if ( state.backtracking==0 ) {
              _channel = HIDDEN;
            }

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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:192:2: ( '0' .. '9' )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:192:4: '0' .. '9'
            {
            matchRange('0','9'); if (state.failed) return ;

            }

        }
        finally {
        }
    }
    // $ANTLR end "NUMERIC"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:217:2: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:217:4: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();
            state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:217:28: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();
            	    state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
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
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:2: ( ( NUMERIC )+ ( '.' ( NUMERIC )+ )? )
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:4: ( NUMERIC )+ ( '.' ( NUMERIC )+ )?
            {
            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:4: ( NUMERIC )+
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
            	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:5: NUMERIC
            	    {
            	    mNUMERIC(); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:15: ( '.' ( NUMERIC )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='.') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:16: '.' ( NUMERIC )+
                    {
                    match('.'); if (state.failed) return ;
                    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:20: ( NUMERIC )+
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
                    	    // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:225:21: NUMERIC
                    	    {
                    	    mNUMERIC(); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
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
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:8: ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_NUMBER )
        int alt9=17;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:10: T__10
                {
                mT__10(); if (state.failed) return ;

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:16: T__11
                {
                mT__11(); if (state.failed) return ;

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:22: T__12
                {
                mT__12(); if (state.failed) return ;

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:28: T__13
                {
                mT__13(); if (state.failed) return ;

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:34: T__14
                {
                mT__14(); if (state.failed) return ;

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:40: T__15
                {
                mT__15(); if (state.failed) return ;

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:46: T__16
                {
                mT__16(); if (state.failed) return ;

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:52: T__17
                {
                mT__17(); if (state.failed) return ;

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:58: T__18
                {
                mT__18(); if (state.failed) return ;

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:64: T__19
                {
                mT__19(); if (state.failed) return ;

                }
                break;
            case 11 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:70: T__20
                {
                mT__20(); if (state.failed) return ;

                }
                break;
            case 12 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:76: T__21
                {
                mT__21(); if (state.failed) return ;

                }
                break;
            case 13 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:82: WHITESPACE
                {
                mWHITESPACE(); if (state.failed) return ;

                }
                break;
            case 14 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:93: NEWLINE
                {
                mNEWLINE(); if (state.failed) return ;

                }
                break;
            case 15 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:101: COMMENT
                {
                mCOMMENT(); if (state.failed) return ;

                }
                break;
            case 16 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:109: ID
                {
                mID(); if (state.failed) return ;

                }
                break;
            case 17 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:1:112: UNSIGNED_NUMBER
                {
                mUNSIGNED_NUMBER(); if (state.failed) return ;

                }
                break;

        }

    }

    // $ANTLR start synpred1_ModelFile
    public final void synpred1_ModelFile_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:4: ( ( '\\r' )? '\\n' )
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:5: ( '\\r' )? '\\n'
        {
        // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:5: ( '\\r' )?
        int alt10=2;
        int LA10_0 = input.LA(1);

        if ( (LA10_0=='\r') ) {
            alt10=1;
        }
        switch (alt10) {
            case 1 :
                // C:\\Documents and Settings\\jrbibers\\workspace\\edu.iu.epic.modeling.compartment\\src\\edu\\iu\\epic\\modeling\\compartment\\grammar\\ModelFile.g:80:5: '\\r'
                {
                match('\r'); if (state.failed) return ;

                }
                break;

        }

        match('\n'); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_ModelFile

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


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\3\uffff\1\21\2\uffff\4\15\10\uffff\27\15\1\56\4\15\1\uffff\6\15"+
        "\1\71\1\72\1\73\1\15\3\uffff\1\75\1\uffff";
    static final String DFA9_eofS =
        "\76\uffff";
    static final String DFA9_minS =
        "\1\11\2\uffff\1\55\2\uffff\1\145\1\156\1\141\1\145\10\uffff\1\163"+
        "\1\143\1\146\1\164\2\143\1\157\2\145\1\157\1\145\1\156\1\143\1\156"+
        "\1\166\1\160\1\144\2\164\1\145\1\164\1\141\1\151\1\60\1\162\1\151"+
        "\1\162\1\157\1\uffff\1\145\1\142\1\171\1\156\1\144\1\154\3\60\1"+
        "\145\3\uffff\1\60\1\uffff";
    static final String DFA9_maxS =
        "\1\172\2\uffff\1\76\2\uffff\1\165\1\156\1\141\1\145\10\uffff\1"+
        "\163\1\143\1\146\1\164\2\143\1\157\2\145\1\157\1\145\1\156\1\143"+
        "\1\156\1\166\1\160\1\144\2\164\1\145\1\164\1\141\1\151\1\172\1\162"+
        "\1\151\1\162\1\157\1\uffff\1\145\1\142\1\171\1\156\1\144\1\154\3"+
        "\172\1\145\3\uffff\1\172\1\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\5\4\uffff\1\15\1\16\1\17\1\20\1"+
        "\21\1\13\1\14\1\3\34\uffff\1\10\12\uffff\1\12\1\7\1\11\1\uffff\1"+
        "\6";
    static final String DFA9_specialS =
        "\76\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\12\1\13\2\uffff\1\13\22\uffff\1\12\2\uffff\1\14\6\uffff"+
            "\1\4\1\2\1\uffff\1\3\1\uffff\1\5\12\16\3\uffff\1\1\3\uffff\32"+
            "\15\4\uffff\1\15\1\uffff\10\15\1\7\2\15\1\10\5\15\1\11\1\6\7"+
            "\15",
            "",
            "",
            "\1\20\20\uffff\1\17",
            "",
            "",
            "\1\23\17\uffff\1\22",
            "\1\24",
            "\1\25",
            "\1\26",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\27",
            "\1\30",
            "\1\31",
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
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\74",
            "",
            "",
            "",
            "\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | WHITESPACE | NEWLINE | COMMENT | ID | UNSIGNED_NUMBER );";
        }
    }
 

}