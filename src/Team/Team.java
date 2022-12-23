package Team;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


/** 
 * This class provides a structure for a team of different Member-type objects.
 * As Member is an abstract class, members are instantiated and added 
 * to a team by referring to their specific class type, thereby exploiting
 * Java's inherent object polymorphism.
 */
public class Team {

    private ArrayList<Member> members;
    private TeamLead teamLead;
    private Repository repository;

    public Team(String teamFile, String geneFile) {
        
        members = new ArrayList<>();

        try(Scanner fileReader = new Scanner(Paths.get(teamFile))) {
            
            while(fileReader.hasNextLine()) {

                String[] info = fileReader.nextLine().split(" ");
                String position = info[0];
    
                if (position.equals("TeamLead")) {
                    teamLead = new TeamLead(info[1], info[2], position, Integer.parseInt(info[3]));
                    members.add(teamLead);
                } else if (position.equals("Bioinformatician")) {
                    BioInfo bioInfo = new BioInfo(info[1], info[2], position, Integer.parseInt(info[3]), geneFile);
                    members.add(bioInfo);
                } else if (position.equals("TechnicalSupport")) {
                    TechnicalSupport technicalSupport = new TechnicalSupport(info[1], info[2], position, Integer.parseInt(info[3]));
                    members.add(technicalSupport);
                }
            }
        } catch (IOException io) {
            System.out.println("Something went wrong while uploading information from " + teamFile);
        }

        // Initialze repository to be modified by authorized users
        repository = new Repository(geneFile, getBioInfos());
    }

    protected ArrayList<Member> getMembers() {
        return members;
    }

    public TeamLead getTeamLead() {
        return teamLead;
    }

    protected Repository getRepository() {
        return repository;
    }

    /** 
     * Provides an ArrayList of all the bioinformaticians belonging
     * to the team.
     * @return The ArrayList containing all the
     *         bioinformaticians whithin the team.
     */
    public ArrayList<BioInfo> getBioInfos() {
        ArrayList<BioInfo> bioInfos = new ArrayList<>();
        for (Member member : members) {
            if (member.getPosition().equals("Bioinformatician")) {
                bioInfos.add((BioInfo) member);
            }
        }
        return bioInfos;
    }


    /** 
     * Provides an ArrayList of all the team members working
     * as Technical Support.
     * @return The ArrayList containing all
     *         the people in team who work as Technical Support.
     */
    public ArrayList<TechnicalSupport> getTechnicalSupport() {
        ArrayList<TechnicalSupport> technicalSupport = new ArrayList<>();
        for (Member member : members) {
            if (member.getPosition().equals("TechnicalSupport")) {
                technicalSupport.add((TechnicalSupport) member);
            }
        }
        return technicalSupport;
    }


    /**
     * Prints out a description of the team members
     * by employing the overriden toString() method inherited from
     * the abstract Member class and by exploiting object polymorphism
     * across the different member typologies.
     */
    public void printMembersDescriptions() {
        System.out.println("Team members:");
        for (Member member : members) {
            System.out.println(member);
        }
    }
}