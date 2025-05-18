package nosensegenerator.nosense;

import java.util.ArrayList;

public class Verb {

    private ArrayList<String> verbs = new ArrayList<>();
    private ArrayList<String> verbsPresent = new ArrayList<>();
    private ArrayList<String> verbsPast = new ArrayList<>();
    private ArrayList<String> verbsFuture = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePath = "src/main/resources/terms/verbs.txt";
    private String filePathPresent = "src/main/resources/terms/verbsPresent.txt";
    private String filePathPast = "src/main/resources/terms/verbsPast.txt";
    private String filePathFuture = "src/main/resources/terms/verbsFuture.txt";

    public Verb() {
        verbs = FileHandler.load(filePath);
        verbsPresent = FileHandler.load(filePathPresent);
        verbsPast = FileHandler.load(filePathPast);
        verbsFuture = FileHandler.load(filePathFuture);
    }

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
            default:
                generatedNumber = (int) (Math.random() * (verbs.size() - 1));
                return verbs.get(generatedNumber);
        }
    }

    public void save(String time, ArrayList<String> verbsForFile) {
        switch (time) {
            case "PRESENT":
                FileHandler.save(verbsPresent, verbsForFile, filePathPresent);
                break;
            case "PAST":
                FileHandler.save(verbsPast, verbsForFile, filePathPast);
                break;
            case "FUTURE":
                FileHandler.save(verbsFuture, verbsForFile, filePathFuture);
                break;
            default:
                FileHandler.save(verbs, verbsForFile, filePath);
                break;
        }
    }
}
