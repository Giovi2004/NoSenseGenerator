package nosensegenerator.nosense;

import java.util.ArrayList;
//*This class gives a random verb read from a file based on the specified time and let's you write new ones in the file*/
public class Verbs {

    private ArrayList<String> verbsPresent = new ArrayList<>();
    private ArrayList<String> verbsPast = new ArrayList<>();
    private ArrayList<String> verbsFuture = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePathPresent = "src/main/resources/terms/verbsPresent.txt";
    private String filePathPast = "src/main/resources/terms/verbsPast.txt";
    private String filePathFuture = "src/main/resources/terms/verbsFuture.txt";

    /**
     * Constructor for Verbs.
     * Initializes the lists of verbs by loading them from files.
     */
    public Verbs() {
        verbsPresent = FileHandler.load(filePathPresent);
        verbsPast = FileHandler.load(filePathPast);
        verbsFuture = FileHandler.load(filePathFuture);
    }
    /**
     * Returns a random verb based on the specified time.
     *
     * @param time The tense of the verb (PRESENT, PAST, FUTURE).
     * @return A randomly selected verb from the corresponding list.
     */
    public String getVerb(String time) {
        switch (time) {
            case "PRESENT":
                generatedNumber = (int) (Math.random() *
                    (verbsPresent.size() - 1));
                return verbsPresent.get(generatedNumber);
            case "PAST":
                generatedNumber = (int) (Math.random() *
                    (verbsPast.size() - 1));
                return verbsPast.get(generatedNumber);
            case "FUTURE":
                generatedNumber = (int) (Math.random() *
                    (verbsFuture.size() - 1));
                return verbsFuture.get(generatedNumber);
            // Default case if the time is not recognized gives a random verb from present
            default:
                generatedNumber = (int) (Math.random() *
                    (verbsPresent.size() - 1));
                return verbsPresent.get(generatedNumber);
        }
    }
    /**
     * Saves the provided verbs to the corresponding file based on the specified time.
     *
     * @param time The tense of the verbs (PRESENT, PAST, FUTURE).
     * @param verbsForFile The list of verbs to save.
     * @return The result of the save operation (e.g., number of lines written).
     */
    public int save(String time, ArrayList<String> verbsForFile) {
        switch (time) {
            case "PRESENT":
                return FileHandler.save(verbsPresent, verbsForFile, filePathPresent);
            case "PAST":
                return FileHandler.save(verbsPast, verbsForFile, filePathPast);
            case "FUTURE":
                return FileHandler.save(verbsFuture, verbsForFile, filePathFuture);
            // Default case if the time is not recognized saves to present
            default:
                return FileHandler.save(verbsPresent, verbsForFile, filePathPresent);
        }
    }
}
