package Alignments;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;


/** 
 * This abstract class provides a template for implementing two different
 * kinds of genome alignments: the Standard one and its SNiP counterpart.
 * As with all abstract classes in Java, no direct instantiation is possible;
 * instead, constructors are inherited and employed by non-abstract subclasses.
 * This class also implements the Functional interface, whose methods need 
 * to be implemented either at the level of the abstract class itself or at the level
 * of its subclasses.
 */
public abstract class Alignment implements Functional{

    private LinkedHashMap<String, String> genomeLibrary;

    public Alignment(String fileName) {
        genomeLibrary = new LinkedHashMap<>();

        // Extract gene expressions from file and store into genomeLibrary
        try(Scanner fileReader = new Scanner(Paths.get(fileName))) {
            while(fileReader.hasNextLine()) {
                String id = fileReader.nextLine().substring(1);
                String genome = fileReader.nextLine();
                genomeLibrary.put(id, genome);
            }
        } catch (IOException io) {
            System.out.println("Something went wrong while reading data from " 
            + fileName + ".");
        }
    }

    public Alignment(LinkedHashMap<String, String> newGenomeLibrary) {
        genomeLibrary = newGenomeLibrary;
    }
    

    /** 
     * Outputs the LinkedHashMap matching ids to genomes.
     * The order of insertion is preserved by intrinsic virtue of the chosen data structure. 
     * @return A LinkedHashMap object mapping ids to genomes.
     */
    public LinkedHashMap<String, String> getGenomeLibrary() {
        return genomeLibrary;
    }


    /** 
     * Outputs an alignment score for the current alignment,
     * intuitively corresponding to a similarity measure across all the library.
     * For each genome and each position, it computes the amount of nucleotides 
     * that differ compared to a reference sequence. 
     * By defaut this is set as the first one in the library.
     * @return The computed alignment score.
     */
    public int getScore() {
        LinkedHashMap<String, String> genomeLibrary = getGenomeLibrary();
        
        if(genomeLibrary.isEmpty()) {
            return 0;
        }

        int score = 0;
  
        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(getGenomeLibrary().keySet());

        // Create reference to first genome
        String firstId = ids.get(0);
        String firstGenome = getGenomeLibrary().get(firstId);
        
        // Loop over genomes in the library
        for (int i = 1; i < ids.size(); i++) {
            // Save current id into memory
            String currentId = ids.get(i);

            // Loop over current genome and compare to reference
            String currentGenome = getGenomeLibrary().get(currentId);

            // Examine single nucleotides
            for (int j = 0; j < currentGenome.length(); j++) {
                if (firstGenome.charAt(j) != currentGenome.charAt(j)) {
                    score++;
                } 
            }
        }
        return score;
    }
}