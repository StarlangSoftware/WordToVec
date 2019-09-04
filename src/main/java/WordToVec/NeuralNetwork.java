package WordToVec;

import Dictionary.TurkishWordComparator;
import Dictionary.VectorizedDictionary;
import Dictionary.VectorizedWord;
import Math.*;
import Corpus.Corpus;
import Corpus.Sentence;

import java.util.Random;

public class NeuralNetwork {
    private Matrix wordVectors, wordVectorUpdate;
    private Vocabulary vocabulary;
    private WordToVecParameter parameter;
    private Corpus corpus;
    private double expTable[];
    private static int EXP_TABLE_SIZE = 1000;
    private static int MAX_EXP = 6;

    /**
     * Constructor for the {@link NeuralNetwork} class. Gets corpus and network parameters as input and sets the
     * corresponding parameters first. After that, initializes the network with random weights between -0.5 and 0.5.
     * Constructs vector update matrix and prepares the exp table.
     * @param corpus Corpus used to train word vectors using Word2Vec algorithm.
     * @param parameter Parameters of the Word2Vec algorithm.
     */
    public NeuralNetwork(Corpus corpus, WordToVecParameter parameter){
        this.vocabulary = new Vocabulary(corpus);
        this.parameter = parameter;
        this.corpus = corpus;
        wordVectors = new Matrix(vocabulary.size(), parameter.getLayerSize(), -0.5, 0.5);
        wordVectorUpdate = new Matrix(vocabulary.size(), parameter.getLayerSize());
        prepareExpTable();
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
            result.addWord(new VectorizedWord(vocabulary.getWord(i).getName(), wordVectors.getRow(i)));
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

    /**
     * Main method for training the CBow version of Word2Vec algorithm.
     */
    private void trainCbow() throws VectorSizeMismatch, MatrixColumnMismatch {
        int wordIndex, lastWordIndex;
        Iteration iteration = new Iteration(corpus, parameter);
        int target, label, l2, b, cw;
        double f, g;
        Sentence currentSentence = corpus.getSentence(iteration.getSentenceIndex());
        VocabularyWord currentWord;
        Random random = new Random();
        Vector outputs = new Vector(parameter.getLayerSize(), 0);
        Vector outputUpdate = new Vector(parameter.getLayerSize(), 0);
        corpus.shuffleSentences(1);
        while (iteration.getIterationCount() < parameter.getNumberOfIterations()) {
            iteration.alphaUpdate();
            wordIndex = vocabulary.getPosition(currentSentence.getWord(iteration.getSentencePosition()));
            currentWord = vocabulary.getWord(wordIndex);
            outputs.clear();
            outputUpdate.clear();
            b = random.nextInt(parameter.getWindow());
            cw = 0;
            for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++){
                int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                    lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                    outputs.add(wordVectors.getRow(lastWordIndex));
                    cw++;
                }
            }
            if (cw > 0) {
                outputs.divide(cw);
                if (parameter.isHierarchicalSoftMax()){
                    for (int d = 0; d < currentWord.getCodeLength(); d++) {
                        l2 = currentWord.getPoint(d);
                        f = outputs.dotProduct(wordVectorUpdate.getRow(l2));
                        if (f <= -MAX_EXP || f >= MAX_EXP){
                            continue;
                        } else{
                            f = expTable[(int)((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
                        }
                        g = (1 - currentWord.getCode(d) - f) * iteration.getAlpha();
                        outputUpdate.add(wordVectorUpdate.getRow(l2).product(g));
                        wordVectorUpdate.add(l2, outputs.product(g));
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
                        f = outputs.dotProduct(wordVectorUpdate.getRow(l2));
                        g = calculateG(f, iteration.getAlpha(), label);
                        outputUpdate.add(wordVectorUpdate.getRow(l2).product(g));
                        wordVectorUpdate.add(l2, outputs.product(g));
                    }
                }
                for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++){
                    int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                    if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                        lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                        wordVectors.add(lastWordIndex, outputUpdate);
                    }
                }
            }
            currentSentence = iteration.sentenceUpdate(currentSentence);
        }
    }

    /**
     * Main method for training the SkipGram version of Word2Vec algorithm.
     */
    private void trainSkipGram() throws VectorSizeMismatch, MatrixColumnMismatch {
        int wordIndex, lastWordIndex;
        Iteration iteration = new Iteration(corpus, parameter);
        int target, label, l1, l2, b;
        double f, g;
        Sentence currentSentence = corpus.getSentence(iteration.getSentenceIndex());
        VocabularyWord currentWord;
        Random random = new Random();
        Vector outputs = new Vector(parameter.getLayerSize(), 0);
        Vector outputUpdate = new Vector(parameter.getLayerSize(), 0);
        corpus.shuffleSentences(1);
        while (iteration.getIterationCount() < parameter.getNumberOfIterations()) {
            iteration.alphaUpdate();
            wordIndex = vocabulary.getPosition(currentSentence.getWord(iteration.getSentencePosition()));
            currentWord = vocabulary.getWord(wordIndex);
            outputs.clear();
            outputUpdate.clear();
            b = random.nextInt(parameter.getWindow());
            for (int a = b; a < parameter.getWindow() * 2 + 1 - b; a++) {
                int c = iteration.getSentencePosition() - parameter.getWindow() + a;
                if (a != parameter.getWindow() && currentSentence.safeIndex(c)) {
                    lastWordIndex = vocabulary.getPosition(currentSentence.getWord(c));
                    l1 = lastWordIndex;
                    outputUpdate.clear();
                    if (parameter.isHierarchicalSoftMax()) {
                        for (int d = 0; d < currentWord.getCodeLength(); d++) {
                            l2 = currentWord.getPoint(d);
                            f = wordVectors.getRow(l1).dotProduct(wordVectorUpdate.getRow(l2));
                            if (f <= -MAX_EXP || f >= MAX_EXP){
                                continue;
                            } else{
                                f = expTable[(int)((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
                            }
                            g = (1 - currentWord.getCode(d) - f) * iteration.getAlpha();
                            outputUpdate.add(wordVectorUpdate.getRow(l2).product(g));
                            wordVectorUpdate.add(l2, wordVectors.getRow(l1).product(g));
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
                            f = wordVectors.getRow(l1).dotProduct(wordVectorUpdate.getRow(l2));
                            g = calculateG(f, iteration.getAlpha(), label);
                            outputUpdate.add(wordVectorUpdate.getRow(l2).product(g));
                            wordVectorUpdate.add(l2, wordVectors.getRow(l1).product(g));
                        }
                    }
                    wordVectors.add(l1, outputUpdate);
                }
            }
            currentSentence = iteration.sentenceUpdate(currentSentence);
        }
    }

}
