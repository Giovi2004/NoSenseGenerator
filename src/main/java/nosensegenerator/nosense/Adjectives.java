package nosensegenerator.nosense;

import java.util.ArrayList;
//*This class gives a random adjective read from a file and let's you write new ones in the file*/
public class Adjectives {

    private ArrayList<String> adjectives = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePath = "src/main/resources/terms/adjectives.txt";
    /**
     * Constructor for Adjectives.
     * Initializes the list of adjectives by loading them from a file.
     */
    public Adjectives() {
        adjectives = FileHandler.load(filePath);
    }
    /**
     * Returns a random adjective from the list.
     *
     * @return A randomly selected adjective.
     */
    public String getAdjective() {
        generatedNumber = (int) (Math.random() * (adjectives.size() - 1));
        String adjective = adjectives.get(generatedNumber);

        return adjective;
    }
    /**
     * Saves the provided adjectives to the file.
     *
     * @param adjectivesForFile The list of adjectives to save.
     * @return The result of the save operation (e.g., number of lines written).
     */
    public int save(ArrayList<String> adjectivesForFile) {
        return FileHandler.save(adjectives, adjectivesForFile, filePath);
    }
}
