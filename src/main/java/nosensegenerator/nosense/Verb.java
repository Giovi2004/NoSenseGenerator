package nosensegenerator.nosense;

import java.util.ArrayList;

public class Verb {

    private ArrayList<String> verbs = new ArrayList<>();
    private ArrayList<String> verbsPresent = new ArrayList<>();
    private ArrayList<String> verbsPast = new ArrayList<>();
    private ArrayList<String> verbsFuture = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/verbs.txt";
    private String FilePathPresent =
        "src/main/resources/terms/verbsPresent.txt";
    private String FilePathPast = "src/main/resources/terms/verbsPast.txt";
    private String FilePathFuture = "src/main/resources/terms/verbsFuture.txt";

    public Verb() {
        verbs = FileHandler.load(FilePath);
        verbsPresent = FileHandler.load(FilePathPresent);
        verbsPast = FileHandler.load(FilePathPast);
        verbsFuture = FileHandler.load(FilePathFuture);
    }

    public String getverb(String time) {
        switch (time) {
            case "present":
                generatedNumber = (int) (Math.random() *
                    (verbsPresent.size() - 1));
                return verbsPresent.get(generatedNumber);
            case "past":
                generatedNumber = (int) (Math.random() *
                    (verbsPast.size() - 1));
                return verbsPast.get(generatedNumber);
            case "future":
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
            case "present":
                FileHandler.save(verbsPresent, verbsForFile, FilePathPresent);
                break;
            case "past":
                FileHandler.save(verbsPast, verbsForFile, FilePathPast);
                break;
            case "future":
                FileHandler.save(verbsFuture, verbsForFile, FilePathFuture);
                break;
            default:
                FileHandler.save(verbs, verbsForFile, FilePath);
                break;
        }
    }
}
