package Alignments;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/** 
 * This subclass of the abstract class Alignment provides a tool for
 * storing genomes by matching them to their corresponding ids; 
 * it differs from the StdAlignment class in that 
 * a different type of alignment can be output as a String.
 */
public class SNiPAlignment extends Alignment{

    public SNiPAlignment(String fileName) {
        super(fileName);
    }

    public SNiPAlignment(LinkedHashMap<String, String> genomeLibrary) {
        super(genomeLibrary);
    }


    /** 
     * Creates a genome alignment according to the SNiP configuration.
     * Single genomes are compared to a reference; then, nucleotides are only explicitly output
     * if they differ from the corresponding position in the reference genome.
     * Otherwise, a dot is shown. 
     * @return A string representation of the obtained alignment.
     */
    @Override
    public String toString() {

        LinkedHashMap<String, String> genomeLibrary = getGenomeLibrary();
        if (genomeLibrary.isEmpty()) {
            return ("The corresponding genome library for this SNiP alignment is empty.");
        }

        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(getGenomeLibrary().keySet());

        // Create reference to first genome
        String firstId = ids.get(0);
        String firstGenome = getGenomeLibrary().get(firstId);
        // And print out
        String alignment = firstId;
        alignment += " ";
        alignment += firstGenome;
        alignment += "\n";
        
        // Loop over genomes in the library
        for (int i = 1; i < ids.size(); i++) {
            // Print out current id
            String currentId = ids.get(i);
            alignment += currentId;
            alignment += " ";

            // Loop over current genome and compare to reference
            String currentGenome = getGenomeLibrary().get(currentId);

            // Examine single nucleotides
            for (int j = 0; j < currentGenome.length(); j++) {
                char currentChar = currentGenome.charAt(j);
                if (firstGenome.charAt(j) == currentChar) {
                    alignment += ".";
                } else {
                    alignment += currentChar;
                }
            }
            alignment += "\n";
        }
        return alignment;
    }
}