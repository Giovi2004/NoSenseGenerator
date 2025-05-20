package nosensegenerator.nosense;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

@Controller
@SessionAttributes(
    {
        "sessionId",
        "inputSentence",
        "templateSentence",
        "generatedSentence",
        "toxicityResultTokens",
        "graphImageName",
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
        if (!model.containsAttribute("toxicityResultTokens")) {
            model.addAttribute("toxicityResultTokens", null);
        }
        if (!model.containsAttribute("graphImageName")) {
            model.addAttribute("graphImageName", null);
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

            if (requestSyntacticTree) {
                GraphvizGenerator.GenerateDependencyGraph(
                    inputSentence.getAnalysisResultTokens(),
                    "graph" + sessionId
                );
                model.addAttribute(
                    "graphImageName",
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
        if (inputSentence == null || inputSentence.isTextBlank()) {
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
        if (inputSentence == null || inputSentence.isTextBlank()) {
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
        if (generatedSentence == null || generatedSentence.isTextBlank()) {
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

            model.addAttribute(
                "toxicityResultTokens",
                generatedSentence.getToxicityResultTokens()
            );

            return "index";
        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Error analyzing toxicity: " + e.getMessage()
            );
            return "index";
        }
    }

    @GetMapping("/graphs-images/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveDependencyGraphImage(
        @PathVariable String fileName
    ) throws IOException {
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
