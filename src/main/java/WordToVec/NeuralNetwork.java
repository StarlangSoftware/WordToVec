package WordToVec;

import Corpus.AbstractCorpus;
import Dictionary.TurkishWordComparator;
import Dictionary.VectorizedDictionary;
import Dictionary.VectorizedWord;
import Math.*;
import Corpus.Sentence;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
    private double[][] wordVectors, wordVectorUpdate;
    private Vocabulary vocabulary;
    private WordToVecParameter parameter;
    private AbstractCorpus corpus;
    private double[] expTable;
    private static final int EXP_TABLE_SIZE = 1000;
    private static final int MAX_EXP = 6;
    private final int vectorLength;

    /**
     * Constructor for the {@link NeuralNetwork} class. Gets corpus and network parameters as input and sets the
     * corresponding parameters first. After that, initializes the network with random weights between -0.5 and 0.5.
     * Constructs vector update matrix and prepares the exp table.
     * @param corpus Corpus used to train word vectors using Word2Vec algorithm.
     * @param parameter Parameters of the Word2Vec algorithm.
     */
    public NeuralNetwork(AbstractCorpus corpus, WordToVecParameter parameter){
        Random random = new Random(parameter.getSeed());
        int row;
        vectorLength = parameter.getLayerSize();
        this.vocabulary = new Vocabulary(corpus);
        row = vocabulary.size();
        this.parameter = parameter;
        this.corpus = corpus;
        wordVectors = new double[row][vectorLength];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < vectorLength; j++) {
                wordVectors[i][j] = -0.5 + random.nextDouble();
            }
        }
        wordVectorUpdate = new double[row][vectorLength];
        prepareExpTable();
    }

    public int vocabularySize(){
        return vocabulary.getTableSize();
    }

    /**
     * Constructs the fast exponentiation table. Instead of taking exponent at each time, the algorithm will lookup
     * the table.
     */
    private void prepareExpTable(){
        expTable = new double[EXP_TABLE_SIZE + 1];
        for (int i = 0; i < EXP_TABLE_SIZE; i++) {
            expTable[i] = Math.exp((i / (EXP_TABLE_SIZE + 0.0) * 2 - 1) * MAX_EXP);
            expTable[i] = expTable[i] / (expTable[i] + 1);
        }
    }

    /**
     * Main method for training the Word2Vec algorithm. Depending on the training parameter, CBox or SkipGram algorithm
     * is applied.
     * @return Dictionary of word vectors.
     */
    public VectorizedDictionary train() throws MatrixColumnMismatch, VectorSizeMismatch {
        VectorizedDictionary result = new VectorizedDictionary(new TurkishWordComparator());
        if (parameter.isCbow()){
            trainCbow();
        } else {
            trainSkipGram();
        }
        for (int i = 0; i < vocabulary.size(); i++){
            Vector vector = new Vector(0, 0);
            for (int j = 0; j < vectorLength; j++){
                vector.add(wordVectors[i][j]);
            }
            result.addWord(new VectorizedWord(vocabulary.getWord(i).getName(), vector));
        }
        return result;
    }

    /**
     * Calculates G value in the Word2Vec algorithm.
     * @param f F value.
     * @param alpha Learning rate alpha.
     * @param label Label of the instance.
     * @return Calculated G value.
     */
    private double calculateG(double f, double alpha, double label){
        if (f > MAX_EXP){
            return (label - 1) * alpha;
        } else {
            if (f < -MAX_EXP){
                return label * alpha;
            } else {
                return (label - expTable[(int) ((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))]) * alpha;
            }
        }
    }
    private void updateOutput(double[] outputUpdate, double[] outputs, int l2, double g){
        for (int j = 0; j < vectorLength; j++){
            outputUpdate[j] += wordVectorUpdate[l2][j] * g;
        }
        for (int j = 0; j < vectorLength; j++){
            wordVectorUpdate[l2][j] += outputs[j] * g;
        }
    }

    private double dotProduct(double[] vector1, double[] vector2){
        double sum = 0;
        for (int j = 0; j < vectorLength; j++){
            sum += vector1[j] * vector2[j];
        }
        return sum;
    }

    /**
     * Main method for training the CBow version of Word2Vec algorithm.
     */
    private void trainCbow() {
        int wordIndex, lastWordIndex;
        Iteration iteration = new Iteration(corpus, parameter);
        int target, label, l2, b, cw;
        double f, g;
        corpus.open();
        Sentence currentSentence = corpus.getSentence();
        VocabularyWord currentWord;
        Random random = new Random(parameter.getSeed());
        double[] outputs = new double[vectorLength];
        double[] outputUpdate = new double[vectorLength];
        while (iteration.getIterationCount() < parameter.getNumberOfIterations()) {
            iteration.alphaUpdate(vocabulary.getTotalNumberOfWords());
            wordIndex = vocabulary.getPosition(currentSentence.getWord(iteration.getSentencePosition()));
            currentWord = vocabulary.getWord(wordIndex);
            Arrays.fill(outputs, 0);
            Arrays.fill(outputUpdate, 0);
            b = random.nextInt(parameter.getWindow());
            cw = 0;
            for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++){
                int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                    lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                    for (int j = 0; j < vectorLength; j++){
                        outputs[j] += wordVectors[lastWordIndex][j];
                    }
                    cw++;
                }
            }
            if (cw > 0) {
                for (int j = 0; j < vectorLength; j++){
                    outputs[j] /= cw;
                }
                if (parameter.isHierarchicalSoftMax()){
                    for (int d = 0; d < currentWord.getCodeLength(); d++) {
                        l2 = currentWord.getPoint(d);
                        f = dotProduct(outputs, wordVectorUpdate[l2]);
                        if (f <= -MAX_EXP || f >= MAX_EXP){
                            continue;
                        } else{
                            f = expTable[(int)((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
                        }
                        g = (1 - currentWord.getCode(d) - f) * iteration.getAlpha();
                        updateOutput(outputUpdate, outputs, l2, g);
                    }
                } else {
                    for (int d = 0; d < parameter.getNegativeSamplingSize() + 1; d++) {
                        if (d == 0) {
                            target = wordIndex;
                            label = 1;
                        } else {
                            target = vocabulary.getTableValue(random.nextInt(vocabulary.getTableSize()));
                            if (target == 0)
                                target = random.nextInt(vocabulary.size() - 1) + 1;
                            if (target == wordIndex)
                                continue;
                            label = 0;
                        }
                        l2 = target;
                        f = dotProduct(outputs, wordVectorUpdate[l2]);
                        g = calculateG(f, iteration.getAlpha(), label);
                        updateOutput(outputUpdate, outputs, l2, g);
                    }
                }
                for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++){
                    int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                    if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                        lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                        for (int j = 0; j < vectorLength; j++){
                            wordVectors[lastWordIndex][j] += outputUpdate[j];
                        }
                    }
                }
            }
            currentSentence = iteration.sentenceUpdate(currentSentence);
        }
        corpus.close();
    }

    /**
     * Main method for training the SkipGram version of Word2Vec algorithm.
     */
    private void trainSkipGram() {
        int wordIndex, lastWordIndex;
        Iteration iteration = new Iteration(corpus, parameter);
        int target, label, l1, l2, b;
        double f, g;
        corpus.open();
        Sentence currentSentence = corpus.getSentence();
        VocabularyWord currentWord;
        Random random = new Random(parameter.getSeed());
        double[] outputUpdate = new double[parameter.getLayerSize()];
        while (iteration.getIterationCount() < parameter.getNumberOfIterations()) {
            iteration.alphaUpdate(vocabulary.getTotalNumberOfWords());
            wordIndex = vocabulary.getPosition(currentSentence.getWord(iteration.getSentencePosition()));
            currentWord = vocabulary.getWord(wordIndex);
            Arrays.fill(outputUpdate, 0);
            b = random.nextInt(parameter.getWindow());
            for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++) {
                int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                    lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                    l1 = lastWordIndex;
                    Arrays.fill(outputUpdate, 0);
                    if (parameter.isHierarchicalSoftMax()) {
                        for (int d = 0; d < currentWord.getCodeLength(); d++) {
                            l2 = currentWord.getPoint(d);
                            f = dotProduct(wordVectors[l1], wordVectorUpdate[l2]);
                            if (f <= -MAX_EXP || f >= MAX_EXP){
                                continue;
                            } else{
                                f = expTable[(int)((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
                            }
                            g = (1 - currentWord.getCode(d) - f) * iteration.getAlpha();
                            updateOutput(outputUpdate, wordVectors[l1], l2, g);
                        }
                    } else {
                        for (int d = 0; d < parameter.getNegativeSamplingSize() + 1; d++) {
                            if (d == 0) {
                                target = wordIndex;
                                label = 1;
                            } else {
                                target = vocabulary.getTableValue(random.nextInt(vocabulary.getTableSize()));
                                if (target == 0)
                                    target = random.nextInt(vocabulary.size() - 1) + 1;
                                if (target == wordIndex)
                                    continue;
                                label = 0;
                            }
                            l2 = target;
                            f = dotProduct(wordVectors[l1], wordVectorUpdate[l2]);
                            g = calculateG(f, iteration.getAlpha(), label);
                            updateOutput(outputUpdate, wordVectors[l1], l2, g);
                        }
                    }
                    for (int j = 0; j < vectorLength; j++){
                        wordVectors[l1][j] += outputUpdate[j];
                    }
                }
            }
            currentSentence = iteration.sentenceUpdate(currentSentence);
        }
        corpus.close();
    }

}
