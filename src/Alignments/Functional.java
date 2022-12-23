package Alignments;


/** 
 * This interface provides methods for the asbtract class Assignment,
 * as well as for all of its subclasses.
 */
interface Functional {

    // Implemented at the level of StdAlignment and SNiPAlignment
    int getScore();

    // Implemented at the level of Alignment
    String toString();

}