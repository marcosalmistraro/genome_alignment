package Team;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import Alignments.StdAlignment;


/** 
 * Subclass for the abastract Member class. It provides an implementation
 * for bioinformaticians, including the related constructor and methods for
 * available actions.
 */
public class BioInfo extends Member {

    private StdAlignment stdAlignment;

    public BioInfo(String name, String surname, String position, int yearsOfExperience, String fileName) {
        super(name, surname, position, yearsOfExperience);
        this.stdAlignment = new StdAlignment(fileName);
    }


    /** 
     * Provides a method for accessing the StdAlignment object
     * associated with a bioinformatician.
     * @return The StdAlignment object associated to the bioinformatician's profile.
     */
    public StdAlignment getStdAlignment() {
        return stdAlignment;
    }


    /**
     * Allows for replacing the stored standard alignment by invoking an overloaded
     * constructor for the StdAlignment class.
     * @param newGenomeLibrary The LinkedHashMap object to be used to construct the
     *                         new StdAlignment object.
     */
    public void setStdAlignment(LinkedHashMap<String, String> newGenomeLibrary) {
        stdAlignment = new StdAlignment(newGenomeLibrary);
    }


    /** 
     * Outputs an ArrayList of String objects representing the ids of
     * single genomes in the LinkedHashMap library.
     * @return An ArrayList object containing all of the ids, in order of insertion.
     */
    private ArrayList<String> getKeyList() {
        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(stdAlignment.getGenomeLibrary().keySet());
        return ids;
    }

    
    /** 
     * Outputs an ArrayList of all the genomes containing a certain sequence of nucleotides.
     * @param sequence String sequence of successive nucleotides to be searched for.         
     * @return An ArrayList of all the ids for genomes containing the queried subsequence.
     */
    public ArrayList<String> searchSequenceInLibrary(String sequence) {
        ArrayList<String> correspondingIds = new ArrayList<>();

        for (int i = 0; i < getKeyList().size(); i++) {
            String currentId = getKeyList().get(i);
            String currentGenome = stdAlignment.getGenomeLibrary().get(currentId);
            if (currentGenome.contains(sequence)) {
                correspondingIds.add(currentId);
            }
        }
        return correspondingIds;
    }

    
    /** 
     * Identifies an existing genome according to its id and replaces it with a new
     * couple of String objects consisting of an id and a genome. The new information 
     * is inserted at the corresponding position in the LinkedHashMap structure. 
     * Therefore, new genomes are not simply appended to the library but rather inserted 
     * in an ordered fashion.
     * @param oldId Id of the genome to be replaced.
     * @param newId New id of the genome to be inserted. It will be inserted at the same position
     *              of the id to be discarded.
     * @param newGenome New genome to be added to the library. Can be identified according to its id.
     */
    public void replaceGenomeInAlignment(String oldId, String newId, String newGenome) {
        ArrayList<String> ids = getKeyList();
        
        // Identify index of the ordered KeySet at which the id and genome should be replaced
        int stopIndex = -1;

        for (int i = 0; i < ids.size(); i++) {
            String currentId = ids.get(i);
            if (currentId.equals(oldId)) {
                stopIndex = i;
                break;
            }
        }

        // Recreate LinkedHashMap to maintain order of genomes
        LinkedHashMap<String, String> newGenomeLibrary = new LinkedHashMap<>();
            
        int i = 0;
        while(i < ids.size()) {
            if (i == stopIndex) {
                newGenomeLibrary.put(newId, newGenome);
            } else {
                String currentId = ids.get(i);
                String currentGenome = stdAlignment.getGenomeLibrary().get(currentId);
                newGenomeLibrary.put(currentId, currentGenome);
            }
            i++;
        }

        // Impose recreated genome library onto the StdAlignment object
        stdAlignment = new StdAlignment(newGenomeLibrary);
    }

    
    /** 
     * Selects a given genome in the library and replaces 
     * a given sequence of nucleotides with a new one.
     * @param id Id of the genome containing the sequence to replaced.
     * @param oldSequence Sequence to be replaced.
     * @param newSequence New sequence of nucleotides to be inserted into the given genome
     *                    as a replacement for the old one.
     */
    public void replaceAllSequencesInGenome(String id, String oldSequence, String newSequence) {
        String currentGenome = stdAlignment.getGenomeLibrary().get(id);
        String updatedGenome = currentGenome.replaceAll(oldSequence, newSequence);
        stdAlignment.getGenomeLibrary().put(id, updatedGenome);
    }

    
    /** 
     * Replaces all occurrencies of a given sequence of nucleotides with
     * a new sequence. The method does so for all genomes in the library 
     * for which the input sequence appears.
     * @param oldSequence Sequence to be replaced.
     * @param newSequence New sequence of nucleotides to be inserted into all genomes
     * containing the input sequence.
     */
    public void replaceAllSequencesInLibrary(String oldSequence, String newSequence) {

        for (int i = 0; i < getKeyList().size(); i++) {
            String currentId = getKeyList().get(i);
            String currentGenome =stdAlignment. getGenomeLibrary().get(currentId);
            if (currentGenome.contains(oldSequence)) {
                String updatedGenome = currentGenome.replaceAll(oldSequence, newSequence);
                stdAlignment.getGenomeLibrary().put(currentId, updatedGenome);
            }
        }
    }

    
    /** 
     * Appends a new genome to the library, by identifying it as a couple of
     * two String-type objects.
     * @param id The id of the newly inserted genome.
     * @param genome The corresponding nucleotide sequence to be appended
     * to the library.
     */
    public void addGenomeToLibrary(String id, String genome) {
        stdAlignment.getGenomeLibrary().put(id, genome);
    }

    
    /** 
     * Removes a genome from the library according to its id String.
     * It preserves the library's order insertion.
     * @param id Identification String for the genome to be removed.
     */
    public void removeGenomeFromLibrary(String id) {
        stdAlignment.getGenomeLibrary().remove(id);
    }


    /** 
     * Exports the standard alignment to an external file.
     * The file name and extensions are automatically set
     * by the method.
     */
    public void writeDataToFile() {
        String fileName = getName() + getSurname() + ".alignment.txt";

        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            fileWriter.write(stdAlignment.toString());
            System.out.println(getName() + " " + getSurname() + " correctly exported report to " + fileName);
        } catch (IOException io) {
            System.out.println("Something went wrong while exporting alignment data to file.");
        }
    }


    /** 
     * Exports the alignment'score to an external file.
     * The file name and extensions are automatically set
     * by the method.
     */
    public void writeReportToFile() {
        String fileName = getName() + getSurname() + ".score.txt";

        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            fileWriter.write(String.valueOf(stdAlignment.getScore()));
            System.out.println(getName() + " " + getSurname() + " correctly exported score to " + fileName);
        } catch (IOException io) {
            System.out.println("Something went wrong while exporting the report's score to file.");
        }
    }
}