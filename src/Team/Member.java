package Team;

import Alignments.StdAlignment;


/** 
 * This abstract class provides a template for implementing different
 * members of a given team, the structure of which is granted by the Team class,
 * hosted within the same package. 
 */
public abstract class Member {
    
    private String name;
    private String surname;
    private String position;
    private int yearsOfExperience;
    protected StdAlignment stdAlignment;
    private final String id;

    public Member(String name, String surname, String position, int yearsOfExperience) {
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.yearsOfExperience = yearsOfExperience;
        this.id = name.substring(0, 2) + 
                  surname.substring(0, 2) +
                  String.valueOf(yearsOfExperience);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPosition() {
        return position;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        String output =  "Team member - " + name +
                         " " + surname + ", working as " +
                         position + ", with " + yearsOfExperience +
                         " years of experience.";
        return output;
    }    
}