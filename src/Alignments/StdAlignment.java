package Alignments;

import java.util.LinkedHashMap;


/** 
 * This subclass of the abstract class Alignment provides a tool for
 * storing genomes by matching them to their corresponding ids.
 * Visualization of the alignment is possible according to a prescribed standard paradigm.
 * See the overridden toString() method for implementation details.
 */
public class StdAlignment extends Alignment{

    public StdAlignment(String fileName) {
        super(fileName);
    }

    public StdAlignment(LinkedHashMap<String, String> genomeLibrary) {
        super(genomeLibrary);
    }

    /** 
     * Creates a genome alignment according to the standard configuration.
     * @return A String representation of the obtained alignment.
     */
    @Override
    public String toString() {

        LinkedHashMap<String, String> genomeLibrary = getGenomeLibrary();
        if (genomeLibrary.isEmpty()) {
            return ("The corresponding genome library for this standard alignment is empty.");
        }

        // Initialize alignment as empty string
        String alignment = "";

        // Iterate thorugh the genome library and append
        for (String id : getGenomeLibrary().keySet()) {
            alignment += id;
            alignment += " ";
            alignment += getGenomeLibrary().get(id);
            alignment += "\n";
        }
        return alignment;
    }
}