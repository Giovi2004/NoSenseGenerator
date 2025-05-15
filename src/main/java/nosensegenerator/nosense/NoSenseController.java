package nosensegenerator.nosense;

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
    public void AnalyzeInputSentence(String input){
        this.inputSentence=new Sentence(input);
        //inserire il risultato nella inputSentence
        //string result = Analyzer.analyzeStructure(inputSentence);
        //return result;
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
    public String AnalyzeGeneratedSentenceToxicity(){
        //Analyzer.analyzeToxicity(this.generatedSentence);
        return "";
    }
    public void SaveTerms(){
        //nouns.save(inputSentence.getNouns());
        //verbs.save(inputSentence.getVerbs());
        //adjectives.save(inputSentence.getAdjectives());
    }
}
