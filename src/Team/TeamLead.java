package Team;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import Alignments.StdAlignment;


/** 
 * Provides a template for implementing Team Leaders
 * for a given team. It is a subclass of the abstract Member class.
 */
public class TeamLead extends Member {

    public Team team;

    public TeamLead(String name, String surname, String position, int yearsOfExperience) {
        super(name, surname, position, yearsOfExperience);
    }
    

    /**
     * Allows for retrieving the StdAlignment object associated with a given bioinformatician.
     * @param team The team to which both the Team Leader and the bioinformatician need to belong to.
     * @param id Id String for the selected bioinformatician. Employed to query the ArrayList of
     *           BioInfo objects for the given team.
     * @return The StdAlignment attribute for the selected BioInfo object.
     */
    public StdAlignment getUserAlignment(Team team, String id) {
        for (BioInfo bioInfo : team.getBioInfos()) {
            if (id == bioInfo.getId()) {
                return bioInfo.getStdAlignment();
            }
        }
        return null; // This could also be set to throw an exception
    }
    

    /**
     * Returns the Repository object for the selected team.
     * @param team The team whose repository needs to be found.
     * @return The Repository object for the selected team.
     */
    public Repository getRepository(Team team) {
        return team.getRepository();
    }


    /** 
     * Allows the TeamLead to promote the standard alignment
     * for any bioinformatician as the optimal alignment for the team.
     * @param team The team whose repository will be updated.
     * @param bioInfo The bioinformatician whose standard alignment will be
     *                used for updating the repository.
     */
    public void promoteAlignment(Team team, BioInfo bioInfo) {
        try {
            if (team.getTeamLead() == this && team.getMembers().contains(bioInfo)) {
                System.out.println("Promoting alignment by user " + 
                bioInfo.getName() + " " + bioInfo.getSurname() + " as optimal alignment.");

                Repository repo = team.getRepository();
                StdAlignment stdAlignment = bioInfo.getStdAlignment();
                repo.setStdOptimal(stdAlignment);
            } else {
                throw new Exception("Both the TeamLead and the BioInfo objects need to belong to the selected team.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
    /** 
     * Allows a TeamLead to overwrite any bioinformatician's alignment
     * with the optimal one stored in the repository.
     * @param team The team from which the optimal alignment is derived.
     * @param bioInfo The bioinformatician whose standard alignment is overwritten.
     */
    public void overwriteAlignment(Team team, BioInfo bioInfo) {
        try {
            if (team.getTeamLead() == this) {
                System.out.println("Overwriting alignment for user " + 
                bioInfo.getName() + " " + bioInfo.getSurname() + " and replacing it with the team's optimal alignment.");

                Repository repo = team.getRepository();
                LinkedHashMap<String, String> optimalGenomeLibrary = repo.getOptimalGenomeLibrary();
                bioInfo.setStdAlignment(optimalGenomeLibrary);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The given TeamLead object does not belong to the selected team.");
        }
    }

    
    /** 
     * Considers all the bioinformaticians in the Team Leader's team, 
     * takes their alignments and exports them to a file. The file name and 
     * extensions are automatically set by the method.
     * @param team The team to which the interested bioinformaticians belong to.
     */
    public void writeDataToFile(Team team) {
        try {
            if (team.getTeamLead()==this)   {
                String fileName = getName() + getSurname() + ".alignment.txt";

                try (FileWriter fileWriter = new FileWriter(fileName, false)) {

                    for (BioInfo bioInfo : team.getBioInfos()) {
                        fileWriter.write(bioInfo.getName() + " ");
                        fileWriter.write(bioInfo.getSurname() + "\n");
                        fileWriter.write(bioInfo.getStdAlignment().toString());
                    }
                
                System.out.println(getName() + " " + getSurname() + " correctly exported report to " + fileName + ".");
        
                } catch (IOException io) {
                    System.out.println("Something went wrong while exporting alignment data to file.");
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The given TeamLead object does not belong to the selected team.");
        }
    }
    
    
    /** 
     * Considers all the bioinformaticians in the Team Leader's team, 
     * takes their alignment scores and exports them to a file. The file name and 
     * extensions are automatically set by the method.
     * @param team The team to which the interested bioinformaticians belong to.
     */
    public void writeReportToFile(Team team) {
        try {
            if (team.getTeamLead()==this) {
                String fileName = getName() + getSurname() + ".score.txt";

                try (FileWriter fileWriter = new FileWriter(fileName, false)) {
                    int i = 0;

                    for (BioInfo bioInfo : team.getBioInfos()) {
                        if (i==team.getBioInfos().size() - 1) {
                            fileWriter.write(bioInfo.getName() + " ");
                            fileWriter.write(bioInfo.getSurname() + " ");
                            fileWriter.write(String.valueOf(bioInfo.getStdAlignment().getScore()));
                            break;
                        }
                        fileWriter.write(bioInfo.getName() + " ");
                        fileWriter.write(bioInfo.getSurname() + " ");
                        fileWriter.write(String.valueOf(bioInfo.getStdAlignment().getScore()));
                        fileWriter.write("\n");
                        i++;
                    }

                System.out.println(getName() + " " + getSurname() + " correctly exported scores to " + fileName + ".");

                } catch (IOException io) {
                    System.out.println("Something went wrong while exporting the reports' scores to file.");
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The given TeamLead object does not belong to the selected team.");
        }
    }
}