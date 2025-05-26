package nosensegenerator.nosense;

import java.util.ArrayList;
//*This class gives a random nouns read from a file and let's you write new ones in the file*/
public class Nouns {

    private ArrayList<String> nouns = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePath = "src/main/resources/terms/nouns.txt";

    /**
     * Constructor for Nouns.
     * Initializes the list of nouns by loading them from a file.
     */
    public Nouns() {
        nouns = FileHandler.load(filePath);
    }

    /**
     * Returns a random noun from the list.
     *
     * @return A randomly selected noun.
     */
    public String getNoun() {
        generatedNumber = (int) (Math.random() * (nouns.size() - 1));
        String noun = nouns.get(generatedNumber);

        return noun;
    }

    /**
     * Saves the provided nouns to the file.
     *
     * @param nounsForFile The list of nouns to save.
     * @return The result of the save operation (e.g., number of lines written).
     */
    public int save(ArrayList<String> nounsForFile) {
        return FileHandler.save(nouns, nounsForFile, filePath);
    }
}
