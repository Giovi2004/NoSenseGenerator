package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles file operations for saving and loading terms.
 * It provides methods to save new terms to a file and load existing terms from a file.
 */
public class FileHandler {

    /**
     * Saves the provided terms to a file if they are not already present in the existing terms.
     *
     * @param terms          The list of existing terms.
     * @param termsForFile   The list of new terms to save.
     * @param filePath       The path to the file where terms will be saved.
     * @return The number of new terms saved, or -1000 if an error occurs.
     * @throws IllegalArgumentException if termsForFile is empty.
     */
    public static int save(ArrayList<String> terms, ArrayList<String> termsForFile, String filePath) {
        if (termsForFile.isEmpty()) {
            System.out.println("The list of terms is empty.");
            return 0;
        }
        int savedCount = 0;
        for (int i = 0; i < termsForFile.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < terms.size(); j++) {
                if (!termsForFile.get(i).equals(terms.get(j))) {
                    flag = true;
                }
            }
            if (flag) {
                terms.add(termsForFile.get(i));
                try (Writer writer = new FileWriter(filePath, true)) {
                    writer.write(termsForFile.get(i) + "\n");
                    writer.close();
                    savedCount++;
                } catch (java.io.IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                    return -1000;
                }
            }
        }
        return savedCount;
    }
    /**
     * Loads terms from a file into an ArrayList.
     *
     * @param filePath The path to the file from which terms will be loaded.
     * @return An ArrayList containing the terms loaded from the file.
     * * If the file does not exist, it returns an empty ArrayList and prints an error message.
     */
    public static ArrayList<String> load(String filePath) {
        ArrayList<String> terms = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                terms.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        return terms;
    }
}
