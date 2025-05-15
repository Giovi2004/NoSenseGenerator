package nosensegenerator.nosense;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"inputSentence", "generatedSentence", "analysisResult", "toxicityResult"})
public class NoSenseController {
    private Generator generator;
    private Noun nouns;
    private Verb verbs;
    private Adjective adjectives;
    
    public NoSenseController() {
        this.generator = new Generator();
        this.nouns = new Noun();
        this.verbs = new Verb();
        this.adjectives = new Adjective();
    }

    @ModelAttribute
    public void initializeSession(Model model) {
        if (!model.containsAttribute("inputSentence")) {
            model.addAttribute("inputSentence", "");
        }
        if (!model.containsAttribute("generatedSentence")) {
            model.addAttribute("generatedSentence", "");
        }
        if (!model.containsAttribute("analysisResult")) {
            model.addAttribute("analysisResult", "");
        }
        if (!model.containsAttribute("toxicityResult")) {
            model.addAttribute("toxicityResult", "");
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/analyze")
    public String analyzeInputSentence(@RequestParam String sentence, 
                                     @RequestParam(defaultValue = "false") boolean requestSyntacticTree, 
                                     Model model) {
        try {
            Sentence inputSentence = new Sentence(sentence);
            String analysisResult = "Analysis completed successfully"; // Replace with actual analysis
            
            if (requestSyntacticTree) {
                //Provide the Syntactic Tree
            }
            
            model.addAttribute("inputSentence", sentence);
            model.addAttribute("analysisResult", analysisResult);
            
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to analyze sentence: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/generate")
    public String generateSentence(@RequestParam(required = false, defaultValue = "present") String time,
                                 @ModelAttribute("inputSentence") String inputSentence,
                                 Model model) {
        if (inputSentence == null || inputSentence.isEmpty()) {
            model.addAttribute("error", "No input sentence has been analyzed yet");
            return "index";
        }

        try {
            String generatedText = generateTemplateSentence();
            String toxicityResult = analyzeToxicity();
            
            model.addAttribute("generatedSentence", generatedText);
            model.addAttribute("toxicityResult", toxicityResult);
            
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error generating sentence: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/save")
    public String saveTerms(@ModelAttribute("inputSentence") String inputSentence, Model model) {
        if (inputSentence == null || inputSentence.isEmpty()) {
            model.addAttribute("error", "No sentence to save. Please analyze a sentence first.");
            return "index";
        }

        try {
            //nouns.save(inputSentence.getNouns());
            //verbs.save(inputSentence.getVerbs());
            //adjectives.save(inputSentence.getAdjectives());
            
            model.addAttribute("success", "Terms saved successfully!");
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save terms: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/clear-session")
    public String clearSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    private String generateTemplateSentence() {
        //this.templateSentence=generator.generateTemplateSentence();
        return "Generated template sentence placeholder";
    }

    private String analyzeToxicity() {
        //return Analyzer.analyzeToxicity(this.generatedSentence);
        return "Toxicity analysis placeholder";
    }
}
