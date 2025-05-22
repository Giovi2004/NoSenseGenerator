package nosensegenerator.nosense;

import java.util.ArrayList;

public class Adjectives {

    private ArrayList<String> adjectives = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePath = "src/main/resources/terms/adjectives.txt";

    public Adjectives() {
        adjectives = FileHandler.load(filePath);
    }

    public String getAdjective() {
        generatedNumber = (int) (Math.random() * (adjectives.size() - 1));
        String adjective = adjectives.get(generatedNumber);

        return adjective;
    }

    public void save(ArrayList<String> adjectivesForFile) {
        FileHandler.save(adjectives, adjectivesForFile, filePath);
    }
}
