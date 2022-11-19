package WordToVec;

import Corpus.Corpus;
import Corpus.AbstractCorpus;
import Dictionary.TurkishWordComparator;
import Dictionary.VectorizedDictionary;
import Math.*;
import org.junit.Before;

public class NeuralNetworkTest {

    AbstractCorpus turkish, english;
    SemanticDataSet mc, rg, ws, av, men, mturk, rare;

    @Before
    public void setUp() {
        english = new Corpus("english-xs.txt");
        turkish = new Corpus("turkish-xs.txt");
        mc = new SemanticDataSet("MC.txt");
        rg = new SemanticDataSet("RG.txt");
        ws = new SemanticDataSet("WS353.txt");
        men = new SemanticDataSet("MEN.txt");
        mturk = new SemanticDataSet("MTurk771.txt");
        rare = new SemanticDataSet("RareWords.txt");
        av = new SemanticDataSet("AnlamverRel.txt");
    }

    private VectorizedDictionary train(AbstractCorpus corpus, boolean cBow){
        WordToVecParameter parameter = new WordToVecParameter();
        parameter.setCbow(cBow);
        NeuralNetwork neuralNetwork = new NeuralNetwork(corpus, parameter);
        System.out.println(neuralNetwork.vocabularySize());
        try {
            return neuralNetwork.train();
        } catch (MatrixColumnMismatch | VectorSizeMismatch ignored) {
        }
        return null;
    }

    @org.junit.Test
    public void testTrainEnglishCBow() {
        VectorizedDictionary dictionary = train(english, true);
        SemanticDataSet mc2 = mc.calculateSimilarities(dictionary);
        System.out.println("(" + mc.size() + ") " + mc.spearmanCorrelation(mc2));
        SemanticDataSet rg2 = rg.calculateSimilarities(dictionary);
        System.out.println("(" + rg.size() + ") " + rg.spearmanCorrelation(rg2));
        SemanticDataSet ws2 = ws.calculateSimilarities(dictionary);
        System.out.println("(" + ws.size() + ") " + ws.spearmanCorrelation(ws2));
        SemanticDataSet men2 = men.calculateSimilarities(dictionary);
        System.out.println("(" + men.size() + ") " + men.spearmanCorrelation(men2));
        SemanticDataSet mturk2 = mturk.calculateSimilarities(dictionary);
        System.out.println("(" + mturk.size() + ") " + mturk.spearmanCorrelation(mturk2));
        SemanticDataSet rare2 = rare.calculateSimilarities(dictionary);
        System.out.println("(" + rare.size() + ") " + rare.spearmanCorrelation(rare2));
    }
    @org.junit.Test
    public void testWithWordVectors() {
        VectorizedDictionary dictionary = new VectorizedDictionary("vectors-english-xs.txt", new TurkishWordComparator());
        System.out.println(dictionary.size());
        SemanticDataSet mc2 = mc.calculateSimilarities(dictionary);
        System.out.println("(" + mc.size() + ") " + mc.spearmanCorrelation(mc2));
        SemanticDataSet rg2 = rg.calculateSimilarities(dictionary);
        System.out.println("(" + rg.size() + ") " + rg.spearmanCorrelation(rg2));
        SemanticDataSet ws2 = ws.calculateSimilarities(dictionary);
        System.out.println("(" + ws.size() + ") " + ws.spearmanCorrelation(ws2));
        SemanticDataSet men2 = men.calculateSimilarities(dictionary);
        System.out.println("(" + men.size() + ") " + men.spearmanCorrelation(men2));
        SemanticDataSet mturk2 = mturk.calculateSimilarities(dictionary);
        System.out.println("(" + mturk.size() + ") " + mturk.spearmanCorrelation(mturk2));
        SemanticDataSet rare2 = rare.calculateSimilarities(dictionary);
        System.out.println("(" + rare.size() + ") " + rare.spearmanCorrelation(rare2));
    }

    @org.junit.Test
    public void testTrainEnglishSkipGram() {
        VectorizedDictionary dictionary = train(english, false);
        SemanticDataSet mc2 = mc.calculateSimilarities(dictionary);
        System.out.println("(" + mc.size() + ") " + mc.spearmanCorrelation(mc2));
        SemanticDataSet rg2 = rg.calculateSimilarities(dictionary);
        System.out.println("(" + rg.size() + ") " + rg.spearmanCorrelation(rg2));
        SemanticDataSet ws2 = ws.calculateSimilarities(dictionary);
        System.out.println("(" + ws.size() + ") " + ws.spearmanCorrelation(ws2));
        SemanticDataSet men2 = men.calculateSimilarities(dictionary);
        System.out.println("(" + men.size() + ") " + men.spearmanCorrelation(men2));
        SemanticDataSet mturk2 = mturk.calculateSimilarities(dictionary);
        System.out.println("(" + mturk.size() + ") " + mturk.spearmanCorrelation(mturk2));
        SemanticDataSet rare2 = rare.calculateSimilarities(dictionary);
        System.out.println("(" + rare.size() + ") " + rare.spearmanCorrelation(rare2));
    }

    @org.junit.Test
    public void testTrainTurkishCBow() {
        VectorizedDictionary dictionary = train(turkish, true);
        SemanticDataSet av2 = av.calculateSimilarities(dictionary);
        System.out.println("(" + av.size() + ") " + av.spearmanCorrelation(av2));
    }

    @org.junit.Test
    public void testTrainTurkishSkipGram() {
        VectorizedDictionary dictionary = train(turkish, false);
        SemanticDataSet av2 = av.calculateSimilarities(dictionary);
        System.out.println("(" + av.size() + ") " + av.spearmanCorrelation(av2));
    }

}