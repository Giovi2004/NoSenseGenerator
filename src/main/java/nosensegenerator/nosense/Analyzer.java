package nosensegenerator.nosense;

import java.io.IOException;
import java.util.List;

import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Token;

public class Analyzer {
    private String text;

    public static List<Token> analyzeSyntax(String sentence) throws IOException {

        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(sentence)
                    .setType(Type.PLAIN_TEXT)
                    .build();

            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);

            // (Optional) print output
            for (Token token : response.getTokensList()) {
                System.out.printf("Text: %s, POS: %s, Lemma: %s\n",
                        token.getText().getContent(),
                        token.getPartOfSpeech().getTag(),
                        token.getLemma());
            }

            return response.getTokensList(); // Return full token data
        }
    }

}
