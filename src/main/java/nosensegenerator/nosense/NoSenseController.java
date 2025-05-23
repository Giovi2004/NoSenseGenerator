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
        "verbsPresent",
        "verbsPast",
        "verbsFuture",
        "adjectives",
        "requestSyntacticTree",
        "selectedTime"
})
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
        if (!model.containsAttribute("toxicityResultTokens")) {
            model.addAttribute("toxicityResultTokens", null);
        }
        if (!model.containsAttribute("graphImageName")) {
            model.addAttribute("graphImageName", null);
        }
        if (!model.containsAttribute("nouns")) {
            model.addAttribute("nouns", null);
        }
        if (!model.containsAttribute("verbsPresent")) {
            model.addAttribute("verbsPresent", null);
        }
        if (!model.containsAttribute("verbsPast")) {
            model.addAttribute("verbsPast", null);
        }
        if (!model.containsAttribute("verbsFuture")) {
            model.addAttribute("verbsFuture", null);
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
            model.addAttribute("inputSentence", new Sentence(""));
            model.addAttribute("templateSentence", "");
            model.addAttribute("generatedSentence", new Sentence(""));

            // Should not be needed, but just in case
            model.addAttribute("nouns", null);
            model.addAttribute("verbsPresent", null);
            model.addAttribute("verbsPast", null);
            model.addAttribute("verbsFuture", null);
            model.addAttribute("adjectives", null);

            model.addAttribute("toxicityResultTokens", null);
            model.addAttribute("graphImageName", null);

            model.addAttribute("selectedTime", "PRESENT");

            Sentence inputSentence = new Sentence(sentence);

            inputSentence.setAnalysisResultTokens(
                    Analyzer.analyzeSyntax(sentence));

            ArrayList<String> nouns = inputSentence.getNouns();
            ArrayList<String> verbsPresent = inputSentence.getVerbs("PRESENT");
            ArrayList<String> verbsPast = inputSentence.getVerbs("PAST");
            ArrayList<String> verbsFuture = inputSentence.getVerbs("FUTURE");
            ArrayList<String> adjectives = inputSentence.getAdjectives();

            if (requestSyntacticTree) {
                GraphvizGenerator.GenerateDependencyGraph(
                        inputSentence.getAnalysisResultTokens(),
                        "graph" + sessionId);
                model.addAttribute(
                        "graphImageName",
                        GraphvizRenderer.RenderDependencyGraph("graph" + sessionId));
            }

            model.addAttribute("inputSentence", inputSentence);
            model.addAttribute("nouns", nouns);
            model.addAttribute("verbsPresent", verbsPresent);
            model.addAttribute("verbsPast", verbsPast);
            model.addAttribute("verbsFuture", verbsFuture);
            model.addAttribute("adjectives", adjectives);

            model.addAttribute("requestSyntacticTree", requestSyntacticTree);

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to analyze sentence: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/generate")
    public String generateSentence(
            @RequestParam(required = false, defaultValue = "PRESENT") String time,
            @ModelAttribute("inputSentence") Sentence inputSentence,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (inputSentence == null || inputSentence.isTextBlank()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No input sentence has been analyzed yet");
            return "redirect:/";
        }

        String normalizedTime = time == null ? "PRESENT" : time.trim().toUpperCase();

        if (!normalizedTime.equalsIgnoreCase("PRESENT") &&
                !normalizedTime.equalsIgnoreCase("PAST") &&
                !normalizedTime.equalsIgnoreCase("FUTURE")) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Invalid time value. Allowed values are PRESENT, PAST, or FUTURE.");
            return "redirect:/";
        }

        try {
            model.addAttribute("toxicityResultTokens", null);

            String templateSentence = generator.generateTemplateSentence();
            Sentence generatedSentence = generator.fillTemplateSentence(
                    templateSentence,
                    inputSentence,
                    normalizedTime);

            model.addAttribute("templateSentence", templateSentence);
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

    @PostMapping("/save")
    public String saveTerms(
            @ModelAttribute("inputSentence") Sentence inputSentence,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (inputSentence == null || inputSentence.isTextBlank()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No sentence to save. Please analyze a sentence first.");
            return "redirect:/";
        }

        try {
            generator.saveFromSentence(inputSentence);

            List<String> empty = new ArrayList<>();
            if (inputSentence.getNouns().isEmpty())
                empty.add("nouns");
            if (inputSentence.getVerbs("PRESENT").isEmpty())
                empty.add("present verbs");
            if (inputSentence.getVerbs("PAST").isEmpty())
                empty.add("past verbs");
            if (inputSentence.getVerbs("FUTURE").isEmpty())
                empty.add("future verbs");
            if (inputSentence.getAdjectives().isEmpty())
                empty.add("adjectives");

            if (empty.size() == 5) {
                redirectAttributes.addFlashAttribute("warning",
                        "No terms found to save. Please analyze a more complete sentence.");
            } else if (!empty.isEmpty()) {
                redirectAttributes.addFlashAttribute("warning", "Some terms were missing: " + String.join(", ", empty));
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

    @PostMapping("/clear-session")
    public String clearSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

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
            generatedSentence.setToxicityResultTokens(
                    Analyzer.analyzeToxicity(generatedSentence.getText()));

            model.addAttribute(
                    "toxicityResultTokens",
                    generatedSentence.getToxicityResultTokens());

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Error analyzing toxicity: " + e.getMessage());
            return "redirect:/";
        }
    }

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
