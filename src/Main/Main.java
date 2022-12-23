package Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import Alignments.StdAlignment;
import Team.BioInfo;
import Team.Repository;
import Team.Team;
import Team.TeamLead;
import Team.TechnicalSupport;

public class Main {

    public static void main(String[] args) {

        // Loading the config.properties file
        String[] fileProps = loadProperties("config.properties");
        // Creating the corresponding team and assigning it to the given geneFile
        Team team = new Team(fileProps[0], fileProps[1]);

        // Listing out the team's members
        team.printMembersDescriptions();
        
        // Creating TeamLead-type instance for the team's leader
        TeamLead teamLead = team.getTeamLead();
        System.out.println("Team Lead member for the selected team:");
        System.out.println(teamLead);

        // Team Leader outputting the repository's optimal alignment score
        Repository repo = teamLead.getRepository(team);
        System.out.println("Current repository score: " + repo.getScore() + ".");
                
        /* Outputting BioInfo-type members belonging to the team
        Generated strings are used as unique identifiers for the BioInfo objects */
        ArrayList<BioInfo> bioInfos = team.getBioInfos();
        for (BioInfo bioInfo : bioInfos) {
            System.out.println(bioInfo);
            System.out.println("Unique id for " + bioInfo.getName() + 
            " " + bioInfo.getSurname() + ": " + 
            bioInfo.getId());
        }
       
        // Identifying a single bioinformatician - Marc Janssens
        BioInfo MJ = bioInfos.get(0);
        System.out.println("Bioinformatician member for the selected team:");
        System.out.println(MJ);

        // Bioinformatician inspecting personal alignment'score
        System.out.println("Alignment score for user " + MJ.getName() + " " + MJ.getSurname() + ": " +
        MJ.getStdAlignment().getScore() + ".");

        /* Team Leader visualizing the standard alignment for a bioinformatician.
        For all the following operations a reference must be made 
        to the StdAlignment-type attribute of the corresponding BioInfo class 
        for modifications to be stored in the resulting BioInfo object */
        System.out.println(teamLead.getUserAlignment(team, MJ.getId()));
        // Visualizing the score for the bioinformatician's alignment
        System.out.println("Alignment score for " + MJ.getName() + " " + MJ.getSurname() + ": " +
        teamLead.getUserAlignment(team, MJ.getId()).getScore() + ".");

        // Bioinformatician searching for a given sequence within his alignment
        String querySequence = "TTCAAAAGCC";
        ArrayList<String> outputIds = MJ.searchSequenceInLibrary(querySequence);
        System.out.println("Genomes containging the sequence " + querySequence + " for " +
        MJ.getName() + " " + MJ.getSurname() + "'s alignment: " + outputIds);

        // Bioinformatician replacing a sequence in a genome on his alignment
        String oldId = "1992.A1.UG.92.92UG037";
        String oldSequence = "TTCAAAAGCC";
        String newSequence = "ATTAGCAATG";
        MJ.replaceAllSequencesInGenome(oldId, oldSequence, newSequence);
        System.out.println("New score for user " + MJ.getName() + " " + MJ.getSurname() + 
                           " after replacing sequence " + oldSequence + " with " + newSequence + 
                           " on genome " + oldId + ": " + MJ.getStdAlignment().getScore() + ".");

        // Bioinformatician replacing all occurring sequences in the library
        MJ.replaceAllSequencesInLibrary(querySequence, newSequence);
        System.out.println("New score for user " + MJ.getName() + " " + MJ.getSurname() + 
                           " after replacing sequence " + querySequence + " with " + newSequence +
                           " on the whole alignment: " + MJ.getStdAlignment().getScore() + ".");

        // Bioinformatician adding a genome to his alignment
        String newId = "2022.MA.SA.89.AA000";
        // Extracting one genome to be duplicated onto the library
        String newGenome = MJ.getStdAlignment().getGenomeLibrary().get(oldId);
        MJ.addGenomeToLibrary(newId, newGenome);
        System.out.println("New score for user " + MJ.getName() + " " + MJ.getSurname() + 
                           " after duplicating genome onto ID " + newId + 
                           " and adding it to the library: " + MJ.getStdAlignment().getScore() + ".");
        
        // Bioinformatician removing a genome from his alignment
        MJ.removeGenomeFromLibrary(newId);
        System.out.println("New score for user " + MJ.getName() + " " + MJ.getSurname() + 
                           " after removing genome with ID " + newId + " from the library: " +
                           MJ.getStdAlignment().getScore() + ".");

        // Replacing a genome in the library with a new one (employing an existing genome for simplicity)
        String replaceId = "1997.A2.CD.97.97CDKTB48";
        newGenome = MJ.getStdAlignment().getGenomeLibrary().get(replaceId);
        MJ.replaceGenomeInAlignment(oldId, newId, newGenome);
        System.out.println("New score for user " + MJ.getName() + " " + MJ.getSurname() + 
                           " after replacing genome with ID " + oldId + 
                           " and overwriting it with a new one, with ID " + newId + ": " +
                           MJ.getStdAlignment().getScore() + ".");

        // Extracting a new bioinformatician from the team
        BioInfo WL = bioInfos.get(1);
        System.out.println("Bioinformatician member for the selected team:");
        System.out.println(WL);
        // The initial score is the same for all bioinformaticians upon team instantiation
        System.out.println("Initial alignment score for user " + WL.getName() + " " + WL.getSurname() + ": " +
                          WL.getStdAlignment().getScore() + ".");

        // Bioinformatician modifying his personal alignment, thereby yielding a new score
        WL.removeGenomeFromLibrary(oldId);
        System.out.println("New score for user " + WL.getName() + " " + WL.getSurname() + 
                           " after removing genome with ID " + oldId + " from the library: " +
                           WL.getStdAlignment().getScore() + ".");

        /* Team Leader copying the repository's optimal alignment to the bioinformatician's profile.
        The user's score is now back to its original value */
        teamLead.overwriteAlignment(team, WL);
        System.out.println("New score for user " + WL.getName() + " " + WL.getSurname() + 
                           " after Team Leader's choice to overwrite the personal alignment with the optimal one: " +
                           WL.getStdAlignment().getScore() + ".");

        /* Bioinformatician making new modification to his personal alignment,
        thereby yielding a new score */
        WL.replaceAllSequencesInLibrary(oldSequence, newSequence);;
        System.out.println("New score for user " + WL.getName() + " " + WL.getSurname() + 
                           " after replacing " + oldSequence + " with " + newSequence +
                           " on the whole alignment: " + WL.getStdAlignment().getScore() + ".");

        /* Team Leader promoting a single bioinformatician's alignement as optimal
        for the team */
        teamLead.promoteAlignment(team, WL);
        // Current repository score now equals the selected bioinformatician's score
        System.out.println("New repository score after promotion of " + WL.getName() + " " + WL.getSurname() + 
                           "'s alignment: " +
                           teamLead.getRepository(team).getScore() + ".");

        // Team Leader not belonging to the team trying to overwrite a bioinformatician's profile
        TeamLead notATL = new TeamLead("Jack", "Frost", "TeamLead", 1);
        System.out.println(notATL);
        notATL.overwriteAlignment(team, MJ); // This does not work; it raises an exception
        
        // Team Leader not belonging to the team trying to promote a bioinformatician's profile
        notATL.promoteAlignment(team, MJ); // This does not work; it raises an exception

        // Deriving technical support personnel from the Team object
        ArrayList<TechnicalSupport> technicalSupport = team.getTechnicalSupport();
        TechnicalSupport JS = technicalSupport.get(0);
        System.out.println("Technical support member for the selected team:");
        System.out.println(JS);

        // Printing out the last performed system back-up. No back-ups available yet
        JS.getLastBackUp();

        // Performing system back-up for the team
        String backUpFile = "backup.txt";
        JS.backUp(team, backUpFile);

        // Printing out the last performed system back-up. The system now displays the last performed action
        JS.getLastBackUp();

        // Clearing all the alignments within the repository
        JS.clear(team);

        // The optimal alignment is now empty
        System.out.println("Optimal alignment score after clearing the whole repository: " + 
                           teamLead.getRepository(team).getScore() + ".");


        // All of the bioinformaticians' alignments are also empty
        HashMap<String, StdAlignment> userAlignments = teamLead.getRepository(team).getUserAlignments();
        for (String id : userAlignments.keySet()) {
            for (BioInfo bioInfo : team.getBioInfos()) {
                if (bioInfo.getId() == id) {
                    System.out.println("New score for user " + bioInfo.getName() + " " + bioInfo.getSurname() + 
                                       " after clearing the whole repository: " +
                                       userAlignments.get(id).getScore() + ".");
                }
            }
        }

        // Restoring the data for the team by calling the back-up file
        JS.restore(backUpFile, team);

        // Visualizing repository's optimal score
        System.out.println("Optimal alignment score " +
                           "after restoring team's repository from " + backUpFile +  ": " +
                           teamLead.getRepository(team).getScore() + ".");
        
        // Outputting restored scores for all bioinformaticians
        for (BioInfo bioInfo : team.getBioInfos()) {
            System.out.println("Alignment score for " + 
                               bioInfo.getName() + " " + bioInfo.getSurname() +
                               " after restoring team's repository from " + backUpFile +  ": " +
                               bioInfo.getStdAlignment().getScore());
        }

        // Creating a new TechnicalSupport object that does not belong to the team
        TechnicalSupport notATS = new TechnicalSupport("Bob", "Ross", "TechnicalSupport", 2);
        notATS.backUp(team, backUpFile);// This does not work; it raises an exception

        // Team Leader writing report to file
        teamLead.writeReportToFile(team);

        // Team Leader writing scores to file
        teamLead.writeDataToFile(team);
        
        // Bioinformatician writing report to file
        MJ.writeReportToFile();

        // Bioinformatician writing scores to file
        MJ.writeDataToFile();

        // Team Leader not belonging to the team. Exporting a report to file raises an exception
        notATL.writeReportToFile(team);

        // Team Leader not belonging to the team. Exporting data to file raises an exception
        notATL.writeDataToFile(team);   
        
    }

    
    /** 
     * A static method for loading properties for the program, which
     * needs a teamFile, detailing the information regarding team members,
     * and a geneFile, reporting genome samples together with their ids.
     * It allows for specifying a given .config file containing the locations
     * of the working files to be referred to.
     * @param configFile The file from which properties should be read.
     * @return A String-type array of length 2 containing information
     *         regarding the locations of the teamFile and geneFile, respectively.
     */
    public static String[] loadProperties(String configFile) {

        // Instantiate Properties class to extraxt info from the config file
        Properties prop = new Properties();
        String[] fileProps = new String[2];

        // Wrap program execution inside of a try/catch block to handle exceptions
        try(InputStream input = new FileInputStream(configFile)) {
            
            prop.load(input);

            String teamFile = prop.getProperty("teamFile");
            String geneFile = prop.getProperty("geneFile");

            fileProps[0] = teamFile;
            fileProps[1] = geneFile;

        } catch (IOException io) {
            System.out.println("Something went wrong while uploading information from " + configFile + ".");
        }
        System.out.println("Information correctly uploaded from " + configFile + ".");
        return fileProps;
    }
}