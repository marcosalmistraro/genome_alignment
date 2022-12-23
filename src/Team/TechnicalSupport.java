package Team;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import Alignments.SNiPAlignment;
import Alignments.StdAlignment;


/** 
 * This subclass of the abstract class Member implements TechnicalSupport-type operators
 * for a team, along with their associated methods.
 */
public class TechnicalSupport extends Member{

    private String lastBackUp;

    public TechnicalSupport(String name, String surname, String position, int yearsOfExperience) {
        super(name, surname, position, yearsOfExperience);
        this.lastBackUp = "No back-ups have been performed yet.";
    }
    

    /**
     * Outputs the last performed system back-up for the current user.
     * If no back-ups are found, a corresponding message is returned.
     */
    public void getLastBackUp() {
        if (lastBackUp.equals("No back-ups have been performed yet.")) {
            System.out.println(lastBackUp);
        } else {
            System.out.println("Last stored back-up - " + lastBackUp);
        }
    }
    
    
    /** 
     * Creates a copy of the team's SNiP alignment contained in the repository
     * as well as a copy of all members' personal alignments.
     * @param team The team whose data needs to be saved.
     * @param backUpFile The name of the file where the back-up
     *                   is stored.
     */
    public void backUp(Team team, String backUpFile) {
        try {
            if (team.getTechnicalSupport().contains(this)) {
                try (FileWriter fileWriter = new FileWriter(backUpFile)) {

                    // Save the team's optimal alignment
                    SNiPAlignment SNiPOptimal = getSNiPOptimal(team);
                    fileWriter.write(SNiPOptimal.toString());

                    // For each member, save their personal alignment
                    for (BioInfo bioInfo : team.getBioInfos()) {
                        String id = bioInfo.getId();
                        StdAlignment stdAlignment = bioInfo.getStdAlignment();

                        fileWriter.write("**********");
                        fileWriter.write("\n");
                        fileWriter.write(id);
                        fileWriter.write("\n");
                        fileWriter.write(stdAlignment.toString());
                    }
                    fileWriter.write("**********");
                    fileWriter.write("\n");
                    fileWriter.write("End of file");

                    // Update timestamp for the backup
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY, HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    lastBackUp = now.format(formatter);

                    // Return to user
                    System.out.println("Back-up performed - " + lastBackUp);

                } catch (IOException io) {
                    System.out.println("Something went wrong while executing back-up to " + backUpFile);
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The given TechnicalSupport object does not belong to the selected team.");
        }
    }

    
    /** 
     * Recovers the repository SNiP alignment for a team, as well as
     * all personal alignments for team members, given a back-up file.
     * @param backUpFile The back-up file from which data is restored.
     * @param team The team whose alignments are restored.
     */
    public void restore(String backUpFile, Team team) {
        try {
            if (team.getTechnicalSupport().contains(this)) {
                try(Scanner fileReader = new Scanner(Paths.get(backUpFile))) {

                    /* Restoring repository by converting SNiP-type
                    to standard alignment in place*/ 

                    LinkedHashMap<String, String> newGenomeLibrary = new LinkedHashMap<>();

                    String firstLine = fileReader.nextLine();
                    String[] firstInfo = firstLine.split(" ");
                    String firstId = firstInfo[0];
                    String firstGenome = firstInfo[1];

                    newGenomeLibrary.put(firstId, firstGenome);

                    while (fileReader.hasNextLine()) {

                        String line = fileReader.nextLine();

                        if (line.equals("**********")) {
                            break;
                        } else {
                            String[] info = line.split(" ");
                            String id = info[0];
                            String genome = info[1];

                            for (int i = 0; i < genome.length(); i++) {
                                if (Character.toString(genome.charAt(i)).equals(".")) {

                                    char[] genomeChars = genome.toCharArray();
                                    genomeChars[i] = firstGenome.charAt(i);
                                    genome = String.valueOf(genomeChars);
                                }
                            }

                            newGenomeLibrary.put(id, genome);
                        }
                    }
            
                    StdAlignment newStdAlignment = new StdAlignment(newGenomeLibrary);
                    team.getRepository().setStdOptimal(newStdAlignment);
                
                    //Restoring alignments for all bioinformaticians

                    /* Outer loop mapping bioinformaticians' ids 
                    obtained from the recovery file to personal libraries */
                    LinkedHashMap<String, LinkedHashMap<String,String>> personalGenomeLibraries = new LinkedHashMap<>();

                    while (fileReader.hasNextLine()) {

                        String outerLine = fileReader.nextLine();
                        
                        if (outerLine.equals("End of file")) {
                            break;
                        }

                        String id = outerLine;

                        // Inner loop to build single-user genome libraries 
                        LinkedHashMap<String, String> personalGenomeLibrary = new LinkedHashMap<>();

                        while(fileReader.hasNextLine()) {

                            String innerLine = fileReader.nextLine();

                            if (innerLine.equals("**********")) {
                                break;
                            }

                            String[] info = innerLine.split(" ");
                            String genomeId = info[0];
                            String genome = info[1];

                            personalGenomeLibrary.put(genomeId, genome);
                        }

                        // Assigning single-user genome library to a unique id
                        personalGenomeLibraries.put(id, personalGenomeLibrary);
                    
                        /* Scan over all bioinformaticians and restore their alignment
                        if their id matches any from the recovery file */
                        for (BioInfo bioInfo : team.getBioInfos()) {
                            for (String key : personalGenomeLibraries.keySet()) {
                                if(bioInfo.getId() == key) {
                                    bioInfo.setStdAlignment(personalGenomeLibraries.get(key));
                                }
                            }
                        }
                    }

                System.out.println("Team data correctly restored from " + backUpFile + ".");

                } catch (IOException io) {
                    System.out.println("Something went wrong while recovering data from " + backUpFile + ".");
                }
            } else {
                throw new Exception ();
            }
        } catch (Exception e) {
            System.out.println("The given TechnicalSupport object does not belong to the selected team.");
        }
    }

    
    /** 
     * Clears the repository for the team, as well as
     * all of the bioinformaticians' personal standard alignments.
     * @param team The team whose repository and alignments need
     *             to be cleared.
     */
    public void clear(Team team) {
        // Create new clear alignment
        LinkedHashMap<String, String> emptyGenomeLibrary = new LinkedHashMap<>();
        StdAlignment emptyAlignment = new StdAlignment(emptyGenomeLibrary);
        // Copy to optimal alignment for the team
        team.getRepository().setStdOptimal(emptyAlignment);
        // Reset all of users' alignments
        HashMap<String, StdAlignment> newUserAlignments = new HashMap<>();
        for (String id : team.getRepository().getUserAlignments().keySet()) {
            newUserAlignments.put(id, emptyAlignment);
        }
        team.getRepository().setUserAlignments(newUserAlignments);

        System.out.println("Team's repository cleared.");
    }


    /** 
     * Retrieves the optimal SNiPAlignment object for a
     * given team.
     * @param team The team whose optimal SNiP alignment needs
     *             to be recovered.
     */
    private SNiPAlignment getSNiPOptimal(Team team) {
        LinkedHashMap<String, String> genomeLibrary = team.getRepository().getOptimalGenomeLibrary();
        SNiPAlignment SNiPOptimal = new SNiPAlignment(genomeLibrary);
        return SNiPOptimal;
    }
}