package nosensegenerator.nosense;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "inputSentence", "templateSentence", "generatedSentence", "analysisResult", "toxicityResult" })
public class NoSenseController {
    private Generator generator;

    public NoSenseController() {
        this.generator = new Generator();
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
        if (sentence.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a sentence to analyze");
            return "index";
        }

        try {
            Sentence inputSentence = new Sentence(sentence);
            inputSentence.setAnalysisResultTokens(Analyzer.analyzeSyntax(sentence));
            String analysisResult = "Analysis completed successfully"; // Replace with actual analysis

            if (requestSyntacticTree) {
                // Provide the Syntactic Tree
            }

            model.addAttribute("inputSentence", inputSentence);
            model.addAttribute("analysisResult", analysisResult);

            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to analyze sentence: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/generate")
    public String generateSentence(@RequestParam(required = false, defaultValue = "present") String time,
            @ModelAttribute("inputSentence") Sentence inputSentence,
            Model model) {
        if (inputSentence == null) {
            model.addAttribute("error", "No input sentence has been analyzed yet");
            return "index";
        }

        try {
            String templateSentence = generateTemplateSentence();
            Sentence generatedSentence = generator.fillTemplateSentence(templateSentence, inputSentence, time);

            model.addAttribute("generatedSentence", generatedSentence);
            model.addAttribute("templateSentence", templateSentence);

            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error generating sentence: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/save")
    public String saveTerms(@ModelAttribute("inputSentence") Sentence inputSentence, Model model) {
        if (inputSentence == null) {
            model.addAttribute("error", "No sentence to save. Please analyze a sentence first.");
            return "index";
        }

        try {
            generator.saveFromSentence(inputSentence);

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

    @PostMapping("/toxicity")
    public String analyzeToxicity(@ModelAttribute("generatedSentence") Sentence generatedSentence, Model model) {
        if (generatedSentence == null) {
            model.addAttribute("error", "No sentence has been generated yet to analyze");
            return "index";
        }

        try {
            generatedSentence.setToxicityResultTokens(Analyzer.analyzeToxicity(generatedSentence.getText()));
            model.addAttribute("toxicityResult", "");
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error analyzing toxicity: " + e.getMessage());
            return "index";
        }
    }

    private String generateTemplateSentence() {
        // this.templateSentence=generator.generateTemplateSentence();
        return "Generated template sentence placeholder";
    }
}
