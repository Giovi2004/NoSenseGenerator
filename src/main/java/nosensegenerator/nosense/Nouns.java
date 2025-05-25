package nosensegenerator.nosense;

import java.util.ArrayList;

public class Nouns {

    private ArrayList<String> nouns = new ArrayList<>();
    private int generatedNumber = 0;
    private String filePath = "src/main/resources/terms/nouns.txt";

    public Nouns() {
        nouns = FileHandler.load(filePath);
    }

    public String getNoun() {
        generatedNumber = (int) (Math.random() * (nouns.size() - 1));
        String noun = nouns.get(generatedNumber);

        return noun;
    }

    public int save(ArrayList<String> nounsForFile) {
        return FileHandler.save(nouns, nounsForFile, filePath);
    }
}
