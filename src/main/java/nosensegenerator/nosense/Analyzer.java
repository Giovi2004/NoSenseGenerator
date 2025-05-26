package nosensegenerator.nosense;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
/**
 * This class is responsible for analysing sentences using the Google Cloud Natural language API.
 */
@Component
public class Analyzer {
    private static String apiKey;
    
    // This method is used to set the API key from the environment variables.
    // Called automatically by Spring when the class is instantiated.
    @Autowired
    public void setEnvironment(Environment environment) {
        apiKey = environment.getProperty("GOOGLE_API_KEY");
    }
    /**
     * Analyzes the syntax of a given sentence using the analyzeSyntax Google Cloud API.
     * 
     * @param sentence The sentence to analyze.
     * @return A list of AnalysisResultToken objects containing the analysis results.
     */
    public static ArrayList<AnalysisResultToken> analyzeSyntax(String sentence) {
        // Check if the API key is set
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: API Key is not set");
            return null;
        }
        
        try {
            // Create HTTP client
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
                
            // Create request JSON
            JSONObject document = new JSONObject();
            document.put("content", sentence);
            document.put("type", "PLAIN_TEXT");
            
            JSONObject requestBody = new JSONObject();
            requestBody.put("document", document);
            requestBody.put("encodingType", "UTF16");
            
            // Create HTTP request
            String apiUrl = "https://language.googleapis.com/v1/documents:analyzeSyntax?key=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
            
            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                System.err.println("Error: API returned status code " + response.statusCode());
                System.err.println("Response: " + response.body());
                return null;
            }
            
            // Parse response
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray tokens = jsonResponse.getJSONArray("tokens");
            ArrayList<AnalysisResultToken> analysisResultTokens = new ArrayList<>();
            // For every  token extract the necessary information and save them in an AnalysisResultToken object to be returned
            for (int i = 0; i < tokens.length(); i++) {
                JSONObject token = tokens.getJSONObject(i);
                String text = token.getJSONObject("text").getString("content").toLowerCase();
                String tag = token.getJSONObject("partOfSpeech").getString("tag");
                String tense = token.getJSONObject("partOfSpeech").getString("tense");
                int headTokenIndex= token.getJSONObject("dependencyEdge").getInt("headTokenIndex");
                String dependencyLabel = token.getJSONObject("dependencyEdge").getString("label");
                AnalysisResultToken analysisResultToken = new AnalysisResultToken(i, text, tag, dependencyLabel, headTokenIndex, tense);
                analysisResultTokens.add(analysisResultToken);
            }
            return analysisResultTokens;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Analyzes the toxicity of a given sentence using the modareateText Google Cloud API.
     * @param sentence The sentence to analyze.
     * @return A list of ToxicityResultToken objects containing the analysis results.
     * */
    public static ArrayList<ToxicityResultToken> analyzeToxicity(String sentence) {
        // Check if the API key is set
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: API Key is not set");
            return null;
        }
        
        try {
            // Create HTTP client
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
                
            // Create request JSON
            JSONObject document = new JSONObject();
            document.put("content", sentence);
            document.put("type", "PLAIN_TEXT");
            
            JSONObject requestBody = new JSONObject();
            requestBody.put("document", document);
            
            // Create HTTP request
            String apiUrl = "https://language.googleapis.com/v1/documents:moderateText?key=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
            
            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                System.err.println("Error: API returned status code " + response.statusCode());
                System.err.println("Response: " + response.body());
                return null;
            }
            
            // Parse response
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray tokens = jsonResponse.getJSONArray("moderationCategories");
            ArrayList<ToxicityResultToken> toxicityResultTokens = new ArrayList<>();
            // For every token extract the necessary information and save them in a ToxicityResultToken object to be returned
            for (int i = 0; i < tokens.length(); i++) {
                JSONObject token = tokens.getJSONObject(i);
                String name = token.getString("name");
                Double confidence= token.getDouble("confidence");
                ToxicityResultToken toxicityResultToken = new ToxicityResultToken(name, confidence);
                toxicityResultTokens.add(toxicityResultToken);
            }
            return toxicityResultTokens;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}