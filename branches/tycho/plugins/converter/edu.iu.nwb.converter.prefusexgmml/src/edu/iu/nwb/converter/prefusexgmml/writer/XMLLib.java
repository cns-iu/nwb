package edu.iu.nwb.converter.prefusexgmml.writer;

/*
 * Retrieved 2009.05.22 without alteration from:
 * http://prefuse.cvs.sourceforge.net/viewvc/prefuse/prefuse/src/edu/berkeley/guir/prefuse/util/XMLLib.java?revision=1.1
 * 
 * Pulled this into the codebase because directly calling
 * 		edu.berkeley.guir.prefuse.util.XMLLib.EscapeString(str)
 * is, for some reason, hanging.
 */
/**
 * Utility methods for working with XML.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class XMLLib {
	
    // unicode ranges and valid/invalid characters
    private static final char   LOWER_RANGE = 0x20;
    private static final char   UPPER_RANGE = 0x7f;
    private static final char[] VALID_CHARS = { 0x9, 0xA, 0xD };
    
    private static final char[] INVALID = { '<', '>', '"', '\'', '&' };
    private static final String[] VALID = 
        { "&lt;", "&gt;", "&quot;", "&apos;", "&amp;" };
    
    public static String EscapeString(String str) {
	    StringBuffer sbuf = new StringBuffer();
		EscapeString(str, sbuf);
		return sbuf.toString();
	} //
	
	public static void EscapeString(String str, StringBuffer sbuf) {
	    if ( sbuf == null ) {
		    throw new IllegalArgumentException(
		    		"Input StringBuffer must be non-null.");
	    }
        if ( str == null ) {
            sbuf.append("null");
            return;
        }
        
        int len = str.length();
        sbuf.ensureCapacity(sbuf.length() + (2*len));
        
        char buf[] = str.toCharArray();
        for (int i = 0; i < len; ++i) {
            char c = buf[i];
            
            if ( (c < LOWER_RANGE     && c != VALID_CHARS[0] && 
                  c != VALID_CHARS[1] && c != VALID_CHARS[2]) 
                 || (c > UPPER_RANGE) )
            {
                // character out of range, escape with character value
                sbuf.append("&#");
                sbuf.append(Integer.toString(c));
                sbuf.append(';');
            } else {
                boolean valid = true;
                // check for invalid characters (e.g., "<", "&", etc)
                for (int j=INVALID.length-1; j >= 0; --j )
                {
                    if ( INVALID[j] == c) {
                        valid = false;
                        sbuf.append(VALID[j]);
                        break;
                    }
                }
                // if character is valid, don't escape
                if (valid) {
                    sbuf.append(c);
                }
            }
        }
	} //
	
} // end of class XMLLib