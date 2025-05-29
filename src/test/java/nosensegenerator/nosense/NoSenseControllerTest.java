package nosensegenerator.nosense;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockedStatic;

@WebMvcTest(NoSenseController.class)
public class NoSenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private NoSenseController controller;
    private Model model;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        controller = new NoSenseController();
        model = new ExtendedModelMap();
        redirectAttributes = mock(RedirectAttributes.class);
    }

    private void assertSentenceAttribute(String attrName, String expectedText) {
        assertTrue(model.containsAttribute(attrName));
        Object obj = model.getAttribute(attrName);
        assertTrue(obj instanceof Sentence);
        assertEquals(expectedText, ((Sentence) obj).getText());
    }

    private void assertAnalyzedWords(String attrName, ArrayList<String> expectedWords) {
        assertTrue(model.containsAttribute(attrName));
        Object obj = model.getAttribute(attrName);
        assertTrue(obj instanceof ArrayList);
        ArrayList<?> words = (ArrayList<?>) obj;
        assertEquals(expectedWords.size(), words.size());
        for (int i = 0; i < expectedWords.size(); i++) {
            String word = (String) words.get(i);
            assertEquals(expectedWords.get(i), word);
        }
    }

    public void assertToxicityResultTokens(String attrName, ArrayList<ToxicityResultToken> expectedTokens) {
        assertTrue(model.containsAttribute(attrName));
        Object obj = model.getAttribute(attrName);
        assertTrue(obj instanceof ArrayList);
        ArrayList<?> tokens = (ArrayList<?>) obj;
        assertEquals(expectedTokens.size(), tokens.size());
        for (int i = 0; i < expectedTokens.size(); i++) {
            ToxicityResultToken token = (ToxicityResultToken) tokens.get(i);
            assertEquals(expectedTokens.get(i).getName(), token.getName());
            assertEquals(expectedTokens.get(i).getConfidence(), token.getConfidence());
        }
    }

    // Test for initializeSession
    @Test
    public void testInitializeSession() {
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getId()).thenReturn("dummySessionId");

        controller.initializeSession(mockSession, model);

        assertEquals("dummySessionId", model.getAttribute("sessionId"));

        assertSentenceAttribute("inputSentence", "");
        assertEquals("", model.getAttribute("templateSentence"));
        assertSentenceAttribute("generatedSentence", "");

        // Attributes expected to be null
        for (String attr : List.of(
                "nouns", "verbs", "adjectives")) {
            assertTrue(model.containsAttribute(attr));
            assertNull(model.getAttribute(attr));
        }

        assertTrue(model.containsAttribute("graphImageName"));
        assertEquals("graph", model.getAttribute("graphImageName"));

        assertTrue(model.containsAttribute("toxicityResultTokens"));
        assertEquals(new ArrayList<ToxicityResultToken>(), model.getAttribute("toxicityResultTokens"));
    }

    // Test for analyzeInputSentence
    @Test
    public void testAnalyzeInputSentence() {
        try (MockedStatic<Analyzer> analyzerMock = mockStatic(Analyzer.class);
                MockedStatic<GraphvizGenerator> graphvizGenMock = mockStatic(GraphvizGenerator.class);
                MockedStatic<GraphvizRenderer> graphvizRendererMock = mockStatic(GraphvizRenderer.class)) {

            ArrayList<AnalysisResultToken> mockAnalysisResultTokens = new ArrayList<>(
                    List.of(
                            new AnalysisResultToken(0, "dog", "NOUN", "nsubj", 1, "TENSE_UNKNOWN"),
                            new AnalysisResultToken(1, "eats", "VERB", "ROOT", 0, "PRESENT"),
                            new AnalysisResultToken(2, "happy", "ADJ", "amod", 1, "TENSE_UNKNOWN"),
                            new AnalysisResultToken(3, "ate", "VERB", "ROOT", 0, "PAST"),
                            new AnalysisResultToken(4, "will eat", "VERB", "ROOT", 0, "FUTURE")));
            analyzerMock.when(() -> Analyzer.analyzeSyntax("This is a test sentence."))
                    .thenReturn(mockAnalysisResultTokens);

            graphvizGenMock.when(() -> GraphvizGenerator.GenerateDependencyGraph(
                    org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString()))
                    .thenAnswer(invocation -> null);

            graphvizRendererMock.when(() -> GraphvizRenderer.RenderDependencyGraph("graphdummySessionId"))
                    .thenReturn("graphdummySessionId.png");

            String result = controller.analyzeInputSentence(
                    "dummySessionId",
                    "This is a test sentence.",
                    true,
                    model,
                    redirectAttributes);

            assertEquals("redirect:/", result);

            analyzerMock.verify(() -> Analyzer.analyzeSyntax("This is a test sentence."));
            graphvizGenMock.verify(
                    () -> GraphvizGenerator.GenerateDependencyGraph(mockAnalysisResultTokens, "graphdummySessionId"));
            graphvizRendererMock.verify(() -> GraphvizRenderer.RenderDependencyGraph("graphdummySessionId"));

            assertSentenceAttribute("inputSentence", "This is a test sentence.");

            assertEquals("graphdummySessionId.png", model.getAttribute("graphImageName"));
            assertEquals(true, model.getAttribute("requestSyntacticTree"));

            for (String attr : List.of(
                    "nouns", "verbs", "adjectives")) {
                ArrayList<String> words = new ArrayList<>();
                if (attr.equals("nouns")) {
                    words.add("dog");
                }
                if (attr.equals("verbs")) {
                    words.add("eats");
                    words.add("ate");
                    words.add("will eat");
                }
                if (attr.equals("adjectives")) {
                    words.add("happy");
                }

                assertAnalyzedWords(attr, words);
            }
        }
    }

    @Test
    public void testAnalyzeInputSentence_emptySentence() {
        String result = controller.analyzeInputSentence("sessionId", "   ", true, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "Please enter a sentence to analyze");
    }

    @Test
    public void testAnalyzeInputSentence_missingSentence() {
        String result = controller.analyzeInputSentence("sessionId", null, true, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "Please enter a sentence to analyze");
    }

    // Test for generateTemplateSentence
    @Test
    public void testGenerateTemplateSentence() {
        Generator generatorSpy = spy(new Generator());

        String mockTemplate = "Every [adjective] [noun] [verb] the [noun]\n";
        doReturn(mockTemplate).when(generatorSpy).generateTemplateSentence();

        Field field;
        try {
            field = NoSenseController.class.getDeclaredField("generator");
            field.setAccessible(true);
            field.set(controller, generatorSpy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String result = controller.generateTemplateSentence(model, redirectAttributes);
        assertEquals("redirect:/", result);

        assertEquals(mockTemplate, model.getAttribute("templateSentence"));

        verify(generatorSpy).generateTemplateSentence();
    }

    // Test for generateSentence
    @Test
    public void testGenerateSentence() {
        Generator generatorSpy = spy(new Generator());

        String mockTemplate = "Every [adjective] [noun] [verb] the [noun]\n";

        doReturn(new Sentence("Every kind dog eats the cat\n")).when(generatorSpy)
                .fillTemplateSentence(eq(mockTemplate), any(Sentence.class), eq("PRESENT"));

        Field field;
        try {
            field = NoSenseController.class.getDeclaredField("generator");
            field.setAccessible(true);
            field.set(controller, generatorSpy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String result = controller.generateSentence("PRESENT", mockTemplate, new Sentence("This is a test sentence."),
                model,
                redirectAttributes);
        assertEquals("redirect:/", result);

        assertSentenceAttribute("generatedSentence", "Every kind dog eats the cat\n");

        assertTrue(model.containsAttribute("selectedTime"));
        assertEquals("PRESENT", model.getAttribute("selectedTime"));

        verify(generatorSpy).fillTemplateSentence(eq(mockTemplate), any(Sentence.class), eq("PRESENT"));
    }

    @Test
    public void testGenerateSentence_noInputSentence() {
        String mockTemplate = "Every [adjective] [noun] [verb] the [noun]\n";

        String result = controller.generateSentence("PRESENT", mockTemplate, null, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "No input sentence has been analyzed yet");
    }

    @Test
    public void testGenerateSentence_noTemplate() {
        String result = controller.generateSentence("PRESENT", null, new Sentence("This is a test sentence."),
                model,
                redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "No template sentence has been generated yet");
    }

    @Test
    public void testGenerateSentence_wrongTimeParam() {
        String mockTemplate = "Every [adjective] [noun] [verb] the [noun]\n";

        String result = controller.generateSentence("wrongTime", mockTemplate, new Sentence("This is a test sentence."),
                model,
                redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error",
                "Invalid time value. Allowed values are PRESENT, PAST, or FUTURE.");
    }

    // Test for saveTerms
    @Test
    public void testSaveTerms() {
        Sentence mockSentence = new Sentence("This is a test sentence.");
        ArrayList<AnalysisResultToken> mockAnalysisResultTokens = new ArrayList<>(
                List.of(
                        new AnalysisResultToken(0, "dog", "NOUN", "nsubj", 1, "TENSE_UNKNOWN"),
                        new AnalysisResultToken(1, "eats", "VERB", "ROOT", 0, "PRESENT"),
                        new AnalysisResultToken(2, "happy", "ADJ", "amod", 1, "TENSE_UNKNOWN"),
                        new AnalysisResultToken(3, "ate", "VERB", "ROOT", 0, "PAST"),
                        new AnalysisResultToken(4, "will eat", "VERB", "ROOT", 0, "FUTURE")));

        mockSentence.setAnalysisResultTokens(mockAnalysisResultTokens);

        Generator generatorSpy = spy(new Generator());

        try {
            java.lang.reflect.Field field = NoSenseController.class.getDeclaredField("generator");
            field.setAccessible(true);
            field.set(controller, generatorSpy);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        doReturn(mockAnalysisResultTokens.size()).when(generatorSpy).saveFromSentence(any(Sentence.class));

        String result = controller.saveTerms(mockSentence, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("success", "Terms saved successfully!");
        verify(generatorSpy).saveFromSentence(mockSentence);
    }

    @Test
    public void testSaveTerms_noTermsToSave() {
        Sentence mockSentence = new Sentence("This is a test sentence.");
        mockSentence.setAnalysisResultTokens(new ArrayList<>());

        Generator generatorSpy = spy(new Generator());
        try {
            java.lang.reflect.Field field = NoSenseController.class.getDeclaredField("generator");
            field.setAccessible(true);
            field.set(controller, generatorSpy);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        doReturn(0).when(generatorSpy).saveFromSentence(any(Sentence.class));

        String result = controller.saveTerms(mockSentence, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("warning",
                "No terms found to save. Please analyze a more complete sentence.");
        verify(generatorSpy).saveFromSentence(mockSentence);
    }

    @Test
    public void testSaveTerms_missingSomeTerms() {
        Sentence mockSentence = new Sentence("This is a test sentence.");
        ArrayList<AnalysisResultToken> mockAnalysisResultTokens = new ArrayList<>(
                List.of(
                        new AnalysisResultToken(0, "dog", "NOUN", "nsubj", 1, "TENSE_UNKNOWN"),
                        new AnalysisResultToken(1, "eats", "VERB", "ROOT", 0, "PRESENT"),
                        new AnalysisResultToken(2, "happy", "ADJ", "amod", 1, "TENSE_UNKNOWN")));
        mockSentence.setAnalysisResultTokens(mockAnalysisResultTokens);

        Generator generatorSpy = spy(new Generator());
        try {
            java.lang.reflect.Field field = NoSenseController.class.getDeclaredField("generator");
            field.setAccessible(true);
            field.set(controller, generatorSpy);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        doReturn(mockAnalysisResultTokens.size()).when(generatorSpy).saveFromSentence(any(Sentence.class));

        String result = controller.saveTerms(mockSentence, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("success", "Terms saved successfully!");
        verify(generatorSpy).saveFromSentence(mockSentence);
    }

    @Test
    public void testSaveTerms_noInputSentence() {
        String result = controller.saveTerms(null, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "No sentence to save. Please analyze a sentence first.");
    }

    // Test for analyzeToxicity
    @Test
    public void testAnalyzeToxicity() {
        try (MockedStatic<Analyzer> analyzerMock = mockStatic(Analyzer.class)) {
            ArrayList<ToxicityResultToken> mockResult = new ArrayList<>();
            mockResult.add(new ToxicityResultToken("test", 0.5));

            analyzerMock.when(() -> Analyzer.analyzeToxicity("This is a generated sentence."))
                    .thenReturn(mockResult);

            String result = controller.analyzeToxicity(new Sentence("This is a generated sentence."), model,
                    redirectAttributes);
            assertEquals("redirect:/", result);

            analyzerMock.verify(() -> Analyzer.analyzeToxicity("This is a generated sentence."));

            assertToxicityResultTokens("toxicityResultTokens", mockResult);
        }
    }

    @Test
    public void testAnalyzeToxicity_noGeneratedSentence() {
        String result = controller.analyzeToxicity(null, model, redirectAttributes);
        assertEquals("redirect:/", result);

        verify(redirectAttributes).addFlashAttribute("error", "No sentence has been generated yet to analyze");
    }

    // Test for clearSession
    @Test
    public void testClearSession() {
        org.springframework.web.bind.support.SessionStatus sessionStatus = org.mockito.Mockito
                .mock(org.springframework.web.bind.support.SessionStatus.class);
        String result = controller.clearSession(sessionStatus);
        assertEquals("redirect:/", result);
        org.mockito.Mockito.verify(sessionStatus).setComplete();
    }

    // Test for getGraphImage
    @Test
    public void testGetGraphImage() {
        // Arrange: create a dummy file in the temp directory
        String sessionId = "dummySessionId";
        String expectedImageName = "graph" + sessionId + ".png";
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path imagePath = Paths.get(tmpDir, expectedImageName);

        // Write some dummy content to the file
        byte[] dummyContent = new byte[] { 1, 2, 3, 4, 5 };
        try {
            Files.write(imagePath, dummyContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseEntity<Resource> result = null;
        try {
            result = controller.serveDependencyGraphImage(expectedImageName);

            assertNotNull(result);
            assertEquals("image/png", result.getHeaders().getFirst("Content-Type"));

            assertNotNull(result.getBody());
            assertTrue(result.getBody().exists());
            assertTrue(result.getBody().isReadable());
            assertEquals(expectedImageName, result.getBody().getFilename());

            try (InputStream is = result.getBody().getInputStream()) {
                byte[] actualContent = is.readAllBytes();
                assertArrayEquals(dummyContent, actualContent);
            }

            assertEquals(HttpStatus.OK, result.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Clean up the dummy file
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetGraphImage_invalidFile() {
        String invalidFileName = "invalidFile.png";
        ResponseEntity<Resource> result;
        try {
            result = controller.serveDependencyGraphImage(invalidFileName);

            assertNotNull(result);
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}