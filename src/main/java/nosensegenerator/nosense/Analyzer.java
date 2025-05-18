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

@Component
public class Analyzer {
    private static String apiKey;
    
    @Autowired
    public void setEnvironment(Environment environment) {
        apiKey = environment.getProperty("GOOGLE_API_KEY");
    }

    public static ArrayList<AnalysisResultToken> analyzeSyntax(String sentence) {
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
    public static ArrayList<ToxicityResultToken> analyzeToxicity(String sentence) {
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