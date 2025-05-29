package nosensegenerator.nosense;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({
        "sessionId",
        "inputSentence",
        "templateSentence",
        "generatedSentence",
        "toxicityResultTokens",
        "graphImageName",
        "nouns",
        "verbs",
        "adjectives",
        "requestSyntacticTree",
        "selectedTime"
})
public class NoSenseController {

    private Generator generator;

    public NoSenseController() {
        this.generator = new Generator();
    }

    /**
     * Initializes the session attributes if they are not already set.
     * This method is called before any request handling methods.
     *
     * @param session the current HTTP session
     * @param model   the model to hold attributes for the view
     */
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
        if (!model.containsAttribute("toxicityResultTokens")) {
            model.addAttribute("toxicityResultTokens", new ArrayList<ToxicityResultToken>());
        }
        if (!model.containsAttribute("graphImageName")) {
            model.addAttribute("graphImageName", "graph");
        }
        if (!model.containsAttribute("nouns")) {
            model.addAttribute("nouns", null);
        }
        if (!model.containsAttribute("verbs")) {
            model.addAttribute("verbs", null);
        }
        if (!model.containsAttribute("adjectives")) {
            model.addAttribute("adjectives", null);
        }
        if (!model.containsAttribute("requestSyntacticTree")) {
            model.addAttribute("requestSyntacticTree", false);
        }
        if (!model.containsAttribute("selectedTime")) {
            model.addAttribute("selectedTime", "PRESENT");
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * Handles the analysis of the input sentence.
     * This method is called when the user submits a sentence for analysis.
     *
     * @param sessionId            the session ID
     * @param sentence             the input sentence to analyze
     * @param requestSyntacticTree whether to show or not the syntactic tree
     * @param model                the model to hold attributes for the view
     * @param redirectAttributes   used to pass flash attributes for redirects
     * @return a redirect to the index page with appropriate attributes set
     */
    @PostMapping("/analyze")
    public String analyzeInputSentence(
            @ModelAttribute("sessionId") String sessionId,
            @RequestParam String sentence,
            @RequestParam(defaultValue = "false") boolean requestSyntacticTree,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (sentence == null || sentence.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Please enter a sentence to analyze");
            return "redirect:/";
        }

        try {
            // Reset the model attributes for a new analysis
            model.addAttribute("inputSentence", new Sentence(""));
            model.addAttribute("templateSentence", "");
            model.addAttribute("generatedSentence", new Sentence(""));

            // Should not be needed, but just in case
            model.addAttribute("nouns", null);
            model.addAttribute("verbs", null);
            model.addAttribute("adjectives", null);

            model.addAttribute("toxicityResultTokens", new ArrayList<>());
            model.addAttribute("graphImageName", "graph");

            model.addAttribute("selectedTime", "PRESENT");

            Sentence inputSentence = new Sentence(sentence);
            ArrayList<AnalysisResultToken> analysisResultTokens = Analyzer.analyzeSyntax(sentence);
            inputSentence.setAnalysisResultTokens(analysisResultTokens);

            if (requestSyntacticTree) {
                GraphvizGenerator.GenerateDependencyGraph(
                        analysisResultTokens,
                        "graph" + sessionId);
                model.addAttribute(
                        "graphImageName",
                        GraphvizRenderer.RenderDependencyGraph("graph" + sessionId));
            }

            if (analysisResultTokens == null) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Failed to analyze sentence. API key not set, please try again later.");
                return "redirect:/";
            }

            if (analysisResultTokens.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "warning",
                        "No tokens found in the sentence.");
            }

            for (String attr : List.of("nouns", "verbs", "adjectives")) {
                try {
                    if (attr.equals("nouns")) {
                        model.addAttribute(attr, inputSentence.getNouns());
                    } else if (attr.equals("verbs")) {
                        model.addAttribute(attr, inputSentence.getVerbs(""));
                    } else if (attr.equals("adjectives")) {
                        model.addAttribute(attr, inputSentence.getAdjectives());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            model.addAttribute("inputSentence", inputSentence);
            model.addAttribute("requestSyntacticTree", requestSyntacticTree);

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to analyze sentence: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Handles the generation of a template sentence.
     * This method is called when the user requests to generate a template sentence.
     *
     * @param model              the model to hold attributes for the view
     * @param redirectAttributes used to pass flash attributes for redirects
     * @return a redirect to the index page with appropriate attributes set
     */
    @PostMapping("/generate-template")
    public String generateTemplateSentence(
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("generatedSentence", new Sentence(""));
            model.addAttribute("toxicityResultTokens", new ArrayList<>());

            String templateSentence = generator.generateTemplateSentence();

            model.addAttribute("templateSentence", templateSentence);

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Error generating template sentence: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Handles the filling of the template sentence with the result tokens from the
     * analysis of the input sentence.
     * This method is called when the user submits a request to fill the template.
     *
     * @param time               the time to use for filling (PRESENT, PAST, or
     *                           FUTURE)
     * @param templateSentence   the template sentence to fill
     * @param inputSentence      the input sentence to use for filling
     * @param model              the model to hold attributes for the view
     * @param redirectAttributes used to pass flash attributes for redirects
     * @return a redirect to the index page with appropriate attributes set
     */
    @PostMapping("/fill-template")
    public String generateSentence(
            @RequestParam(required = false, defaultValue = "PRESENT") String tense,
            @ModelAttribute("templateSentence") String templateSentence,
            @ModelAttribute("inputSentence") Sentence inputSentence,
            Model model,
            RedirectAttributes redirectAttributes) {
        String normalizedTime = tense == null ? "PRESENT" : tense.trim().toUpperCase();

        if (inputSentence == null || inputSentence.isTextBlank() ||
                inputSentence.getAnalysisResultTokens() == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No input sentence has been analyzed yet");
            return "redirect:/";
        }

        if (templateSentence == null || templateSentence.isBlank()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No template sentence has been generated yet");
            return "redirect:/";
        }

        if (!normalizedTime.equalsIgnoreCase("PRESENT") &&
                !normalizedTime.equalsIgnoreCase("PAST") &&
                !normalizedTime.equalsIgnoreCase("FUTURE")) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Invalid time value. Allowed values are PRESENT, PAST, or FUTURE.");
            return "redirect:/";
        }

        try {
            model.addAttribute("toxicityResultTokens", new ArrayList<>());

            Sentence generatedSentence = generator.fillTemplateSentence(
                    templateSentence,
                    inputSentence,
                    normalizedTime);

            model.addAttribute("generatedSentence", generatedSentence);
            model.addAttribute("selectedTime", normalizedTime);

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Error generating sentence: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Handles the saving of terms from the input sentence.
     * This method is called when the user submits a request to save terms.
     *
     * @param inputSentence      the input sentence to save terms from
     * @param model              the model to hold attributes for the view
     * @param redirectAttributes used to pass flash attributes for redirects
     * @return a redirect to the index page with appropriate attributes set
     */
    @PostMapping("/save")
    public String saveTerms(
            @ModelAttribute("inputSentence") Sentence inputSentence,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (inputSentence == null || inputSentence.isTextBlank() || inputSentence.getAnalysisResultTokens() == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No sentence to save. Please analyze a sentence first.");
            return "redirect:/";
        }

        try {
            int savedTermsCount = generator.saveFromSentence(inputSentence);

            if (savedTermsCount < 0) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Failed to save terms. Please try again later.");
                return "redirect:/";
            }
            if (savedTermsCount == 0) {
                redirectAttributes.addFlashAttribute("warning",
                        "No terms found to save. Please analyze a more complete sentence.");
            } else {
                redirectAttributes.addFlashAttribute("success", "Terms saved successfully!");
            }

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to save terms: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Clears the session attributes and resets the session.
     * This method is called when the user submits a request to clear the session.
     *
     * @param sessionStatus signal that the session processing is complete, ready
     *                      for cleanup
     * @return a redirect to the index page
     */
    @PostMapping("/clear-session")
    public String clearSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    /**
     * Analyzes the toxicity of the generated sentence.
     * This method is called when the user submits a request to analyze toxicity.
     *
     * @param generatedSentence  the generated sentence to analyze
     * @param model              the model to hold attributes for the view
     * @param redirectAttributes used to pass flash attributes for redirects
     * @return a redirect to the index page with appropriate attributes set
     */
    @PostMapping("/toxicity")
    public String analyzeToxicity(
            @ModelAttribute("generatedSentence") Sentence generatedSentence,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (generatedSentence == null || generatedSentence.isTextBlank()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No sentence has been generated yet to analyze");
            return "redirect:/";
        }

        try {
            ArrayList<ToxicityResultToken> toxicityResultTokens = Analyzer.analyzeToxicity(generatedSentence.getText());
            generatedSentence.setToxicityResultTokens(toxicityResultTokens);

            if (toxicityResultTokens == null || toxicityResultTokens.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Failed to analyze toxicity. API key not set, please try again later.");
                return "redirect:/";
            }


            model.addAttribute(
                    "toxicityResultTokens",
                    toxicityResultTokens);

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Error analyzing toxicity: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Serves the generated dependency graph image.
     * This method is called when the user requests to view the dependency graph
     * image.
     *
     * @param fileName the name of the image file to serve
     * @return a ResponseEntity containing the image resource or a 404 if not found
     * @throws IOException if an I/O error occurs while accessing the file
     */
    @GetMapping("/graphs-images/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveDependencyGraphImage(
            @PathVariable String fileName) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path imagePath = Paths.get(tmpDir, fileName);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource fileResource = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(fileResource);
    }
}
