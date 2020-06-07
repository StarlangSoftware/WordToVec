package WordToVec;

import Corpus.Corpus;
import Dictionary.VectorizedDictionary;
import Math.*;
import org.junit.Before;

public class NeuralNetworkTest {

    Corpus turkish, english;

    @Before
    public void setUp() {
        english = new Corpus("english-similarity-dataset.txt");
        turkish = new Corpus("turkish-similarity-dataset.txt");
    }

    private VectorizedDictionary train(Corpus corpus, boolean cBow){
        WordToVecParameter parameter = new WordToVecParameter();
        parameter.setCbow(cBow);
        NeuralNetwork neuralNetwork = new NeuralNetwork(corpus, parameter);
        try {
            return neuralNetwork.train();
        } catch (MatrixColumnMismatch | VectorSizeMismatch ignored) {
        }
        return null;
    }

    @org.junit.Test
    public void testTrainEnglishCBow() throws VectorSizeMismatch {
        VectorizedDictionary dictionary = train(english, true);
    }

    @org.junit.Test
    public void testTrainEnglishSkipGram() {
        VectorizedDictionary dictionary = train(english, false);
    }

    @org.junit.Test
    public void testTrainTurkishCBow() {
        VectorizedDictionary dictionary = train(turkish, true);
    }

    @org.junit.Test
    public void testTrainTurkishSkipGram() {
        VectorizedDictionary dictionary = train(turkish, false);
    }

}