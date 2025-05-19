package nosensegenerator.nosense;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes(
    {
        "sessionId",
        "inputSentence",
        "templateSentence",
        "generatedSentence",
        "toxicityResult",
        "graphImagePath",
    }
)
public class NoSenseController {

    private Generator generator;

    public NoSenseController() {
        this.generator = new Generator();
    }

    @ModelAttribute
    public void initializeSession(HttpSession session, Model model) {
        if (!model.containsAttribute("sessionId")) {
            System.out.println(session.getId());
            model.addAttribute("sessionId", session.getId());
        }
        if (!model.containsAttribute("inputSentence")) {
            model.addAttribute("inputSentence", new Sentence(""));
        }
        if (!model.containsAttribute("templateSentence")) {
            model.addAttribute("templateSentence", "");
        }
        if (!model.containsAttribute("generatedSentence")) {
            model.addAttribute("generatedSentence", new Sentence(""));
        }
        if (!model.containsAttribute("toxicityResult")) {
            model.addAttribute("toxicityResult", null);
        }
        if (!model.containsAttribute("graphImagePath")) {
            model.addAttribute("graphImagePath", null);
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/analyze")
    public String analyzeInputSentence(
        @ModelAttribute("sessionId") String sessionId,
        @RequestParam String sentence,
        @RequestParam(defaultValue = "false") boolean requestSyntacticTree,
        Model model
    ) {
        if (sentence.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a sentence to analyze");
            return "index";
        }

        try {
            Sentence inputSentence = new Sentence(sentence);
            inputSentence.setAnalysisResultTokens(
                Analyzer.analyzeSyntax(sentence)
            );

            // Example tokens (replace with actual tokens)

            ArrayList<AnalysisResultToken> tokens = new ArrayList<>(
                List.of(
                    new AnalysisResultToken(0, "Ask", "VERB", "ROOT", 0, ""),
                    new AnalysisResultToken(1, "not", "ADV", "neg", 0, ""),
                    new AnalysisResultToken(2, "what", "PRON", "dobj", 6, ""),
                    new AnalysisResultToken(3, "your", "PRON", "poss", 4, ""),
                    new AnalysisResultToken(
                        4,
                        "country",
                        "NOUN",
                        "nsubj",
                        6,
                        ""
                    ),
                    new AnalysisResultToken(5, "can", "VERB", "aux", 6, ""),
                    new AnalysisResultToken(6, "do", "VERB", "ROOT", 0, ""),
                    new AnalysisResultToken(7, "for", "ADP", "prep", 6, ""),
                    new AnalysisResultToken(8, "you", "PRON", "pobj", 7, "")
                )
            );

            if (requestSyntacticTree) {
                // Provide the Syntactic Tree
                GraphvizGenerator.GenerateDependencyGraph(
                    //inputSentence.getAnalysisResultTokens(),
                    tokens,
                    "graph" + sessionId
                );
                model.addAttribute(
                    "graphImagePath",
                    GraphvizRenderer.RenderDependencyGraph("graph" + sessionId)
                );
            }

            model.addAttribute("inputSentence", inputSentence);

            return "index";
        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Failed to analyze sentence: " + e.getMessage()
            );
            return "index";
        }
    }

    @PostMapping("/generate")
    public String generateSentence(
        @RequestParam(required = false, defaultValue = "present") String time,
        @ModelAttribute("inputSentence") Sentence inputSentence,
        Model model
    ) {
        if (inputSentence == null || inputSentence.getText().isEmpty()) {
            model.addAttribute(
                "error",
                "No input sentence has been analyzed yet"
            );
            return "index";
        }

        try {
            String templateSentence = generator.generateTemplateSentence();
            Sentence generatedSentence = generator.fillTemplateSentence(
                templateSentence,
                inputSentence,
                time
            );

            model.addAttribute("templateSentence", templateSentence);
            model.addAttribute("generatedSentence", generatedSentence);

            return "index";
        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Error generating sentence: " + e.getMessage()
            );
            return "index";
        }
    }

    @PostMapping("/save")
    public String saveTerms(
        @ModelAttribute("inputSentence") Sentence inputSentence,
        Model model
    ) {
        if (inputSentence == null || inputSentence.getText().isEmpty()) {
            model.addAttribute(
                "error",
                "No sentence to save. Please analyze a sentence first."
            );
            return "index";
        }

        try {
            generator.saveFromSentence(inputSentence);

            model.addAttribute("success", "Terms saved successfully!");

            return "index";
        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Failed to save terms: " + e.getMessage()
            );
            return "index";
        }
    }

    @PostMapping("/clear-session")
    public String clearSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @PostMapping("/toxicity")
    public String analyzeToxicity(
        @ModelAttribute("generatedSentence") Sentence generatedSentence,
        Model model
    ) {
        if (
            generatedSentence == null || generatedSentence.getText().isEmpty()
        ) {
            model.addAttribute(
                "error",
                "No sentence has been generated yet to analyze"
            );
            return "index";
        }

        try {
            generatedSentence.setToxicityResultTokens(
                Analyzer.analyzeToxicity(generatedSentence.getText())
            );

            List<ToxicityResultToken> toxicityResults =
                generatedSentence.getToxicityResultTokens();
            ToxicityResultToken lastResult = toxicityResults.get(
                toxicityResults.size() - 1
            );

            model.addAttribute("toxicityResult", lastResult);

            return "index";
        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Error analyzing toxicity: " + e.getMessage()
            );
            return "index";
        }
    }
}
