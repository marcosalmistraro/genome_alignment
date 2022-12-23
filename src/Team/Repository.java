package Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import Alignments.SNiPAlignment;
import Alignments.StdAlignment;

/** 
 * Class implementing the structure of a data repository for a given team.
 * It contains the optimal standard alignment for the team, which always corresponds
 * to the optimal SNiP one. Moreover, it stores all standard alignments for 
 * bioinformaticians belonging to the team.
 */
public class Repository {

    private StdAlignment StdOptimal;
    private SNiPAlignment SNiPOptimal;
    private HashMap<String, StdAlignment> userAlignments;

    public Repository(String fileName, ArrayList<BioInfo> bioInfos) {
        // Set StdOptimal and SNiPOptimal as new alignments
        this.StdOptimal = new StdAlignment(fileName);
        this.SNiPOptimal = new SNiPAlignment(fileName);

        // Set all bioinformaticians' alignments to coincide with StdOptimal
        HashMap<String, StdAlignment> userAlignments = new HashMap<>();
        for (BioInfo bioInfo : bioInfos) {
            userAlignments.put(bioInfo.getId(), StdOptimal);
        }
        this.userAlignments = userAlignments;
    }

    public HashMap<String, StdAlignment> getUserAlignments() {
        return userAlignments;
    }

    
    /**
     * Sets a new standard alignment as the optimal one
     * for the repository. At the same time, it updates the
     * optimal SNiP alignment by employing the corresponding
     * genome library.
     * @param StdOptimal The standard alignment to be set as
     *                   optimal for the repository.
     */
    protected void setStdOptimal(StdAlignment stdAlignment) {
        this.StdOptimal = stdAlignment;
        LinkedHashMap<String, String> genomeLibrary = stdAlignment.getGenomeLibrary();
        this.SNiPOptimal = new SNiPAlignment(genomeLibrary);
    }

    protected void setUserAlignments(HashMap<String, StdAlignment> userAlignments) {
        this.userAlignments = userAlignments;
    }

    public int getScore() {
        return SNiPOptimal.getScore();
    }

    protected LinkedHashMap<String, String> getOptimalGenomeLibrary() {
        return StdOptimal.getGenomeLibrary();
    }
}