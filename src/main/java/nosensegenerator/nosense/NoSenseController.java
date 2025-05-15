package nosensegenerator.nosense;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class NoSenseController {
    //da sistemate i metodi delle analisi in base a quello che serve
    private Sentence inputSentence;
    private Sentence templateSentence;
    private Sentence generatedSentence;
    private Generator generator;
    private Noun nouns;
    private Verb verbs;
    private Adjective adjectives;
    
    public NoSenseController(){
        this.inputSentence=null;
        this.templateSentence=null;
        this.generatedSentence=null;
        this.generator=new Generator();
        this.nouns=new Noun();
        this.verbs=new Verb();
        this.adjectives=new Adjective();
    }

    @PostMapping("/api/analyze")
    public ResponseEntity<ApiResponse<String>> AnalyzeInputSentence(@RequestBody String input) {
        try {
            this.inputSentence = new Sentence(input);
            //inserire il risultato nella inputSentence
            //string result = Analyzer.analyzeStructure(inputSentence);
            return ResponseEntity.ok(ApiResponse.success("result"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to analyze sentence: " + e.getMessage()));
        }
    }

    @GetMapping("/api/generate")
    public ResponseEntity<ApiResponse<String>> GetGeneratedSentence(@RequestParam(required = false, defaultValue = "present") String time) {
        if(inputSentence == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No input sentence has been analyzed yet"));
        }

        try {
            // First generate the template
            GenerateTemplateSentence();
            
            // Then fill it with content
            String generatedText = FillTemplateSentence();
            
            return ResponseEntity.ok(ApiResponse.success(generatedText));
        } catch (Exception e) {
            // Log the error and return a user-friendly message
            System.err.println("Error generating sentence: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Error generating sentence. Please try again."));
        }
    }

    public String GenerateTemplateSentence(){
        //this.templateSentence=generator.generateTemplateSentence();
        return this.templateSentence.getText();
    }

    //da vedere se gestire il tempo della frase
    /*public String FillTemplateSentence(String time){
        //this.generatedSentence=generator.fillTemplateSentence(this.templateSentence,this.inputSentence,this.nouns,this.verbs,this.adjectives,time);
        return this.generatedSentence.getText();
    }*/

    public String FillTemplateSentence(){
        //this.generatedSentence=generator.fillTemplateSentence(this.templateSentence,this.inputSentence,this.nouns,this.verbs,this.adjectives);
        return this.generatedSentence.getText();
    }

    @GetMapping("/api/toxicity")
    public ResponseEntity<ApiResponse<String>> AnalyzeGeneratedSentenceToxicity() {
        if (generatedSentence == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No sentence has been generated yet"));
        }

        try {
            //String toxicityResult = Analyzer.analyzeToxicity(this.generatedSentence);
            // Temporary placeholder until the actual analyzer is implemented
            String toxicityResult = "Toxicity analysis placeholder for: " + generatedSentence.getText();
            return ResponseEntity.ok(ApiResponse.success(toxicityResult));
        } catch (Exception e) {
            System.err.println("Error analyzing toxicity: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Error analyzing toxicity. Please try again."));
        }
    }

    @PostMapping("/api/saveterms")
    public ResponseEntity<ApiResponse<Void>> SaveTerms() {
        try {
            //nouns.save(inputSentence.getNouns());
            //verbs.save(inputSentence.getVerbs());
            //adjectives.save(inputSentence.getAdjectives());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to save terms: " + e.getMessage()));
        }
    }
}
