/*
 * Util.java
 *
 * collects a list of methods useful to manage data
 */

package it.polimi.elet.contextaddict.microjena.util;

/**
 *
 * @author ilBuccia
 */
public class Util {
    
    /** Creates a new instance of Util */
    public Util() {
    }
    
    public static int splitNamespace(String s) {
//        int i=0;
//	boolean found = false;
//        while((!found)&&(i<s.length())) {
//            found = (s.charAt(i)=='#')||(s.charAt(i)==':');
//	    i++;
//	}
//        return i;
	//RICERCA DA DESTRA
	int i=s.length();
	boolean found = false;
        while((!found)&&(i>0)) {
	    i--;
            found = (s.charAt(i)=='#')||(s.charAt(i)==':');
	}
        return i+1;
    }    
}
