package nosensegenerator.nosense;
/* 
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NosenseApplication {

	public static void main(String[] args) {
		
		
		/*URL resource = NosenseApplication.class.getClassLoader().getResource("keys/google-nlp-key.json");
		if (resource == null) {
			throw new RuntimeException("Could not find the Google NLP key in classpath!");
		}
		File keyFile = Paths.get(resource.toURI()).toFile();
		System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", keyFile.getAbsolutePath());
		String sentence = "The dog is barking at the mailman";
		Analyzer analyzer = new Analyzer();
		try {
			analyzer.analyzeSyntax(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}*/


		SpringApplication.run(NosenseApplication.class, args);
	}

}
