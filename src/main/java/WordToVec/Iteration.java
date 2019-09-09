package WordToVec;

import Corpus.Corpus;
import Corpus.Sentence;

public class Iteration {
    private int wordCount = 0, lastWordCount = 0, wordCountActual = 0;
    private int iterationCount = 0;
    private int sentencePosition = 0, sentenceIndex = 0;
    private double startingAlpha;
    private double alpha;
    private Corpus corpus;
    private WordToVecParameter wordToVecParameter;

    /**
     * Constructor for the {@link Iteration} class. Get corpus and parameter as input, sets the corresponding
     * parameters.
     * @param corpus Corpus used to train word vectors using Word2Vec algorithm.
     * @param wordToVecParameter Parameters of the Word2Vec algorithm.
     */
    public Iteration(Corpus corpus, WordToVecParameter wordToVecParameter){
        this.corpus = corpus;
        this.wordToVecParameter = wordToVecParameter;
        startingAlpha = wordToVecParameter.getAlpha();
        alpha = wordToVecParameter.getAlpha();
    }

    /**
     * Accessor for the alpha attribute.
     * @return Alpha attribute.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Accessor for the iterationCount attribute.
     * @return IterationCount attribute.
     */
    public int getIterationCount() {
        return iterationCount;
    }

    /**
     * Accessor for the sentenceIndex attribute.
     * @return SentenceIndex attribute
     */
    public int getSentenceIndex() {
        return sentenceIndex;
    }

    /**
     * Accessor for the sentencePosition attribute.
     * @return SentencePosition attribute
     */
    public int getSentencePosition() {
        return sentencePosition;
    }

    /**
     * Updates the alpha parameter after 10000 words has been processed.
     */
    public void alphaUpdate(){
        if (wordCount - lastWordCount > 10000) {
            wordCountActual += wordCount - lastWordCount;
            lastWordCount = wordCount;
            alpha = startingAlpha * (1 - wordCountActual / (wordToVecParameter.getNumberOfIterations() * corpus.numberOfWords() + 1.0));
            if (alpha < startingAlpha * 0.0001)
                alpha = startingAlpha * 0.0001;
        }
    }

    /**
     * Updates sentencePosition, sentenceIndex (if needed) and returns the current sentence processed. If one sentence
     * is finished, the position shows the beginning of the next sentence and sentenceIndex is incremented. If the
     * current sentence is the last sentence, the system shuffles the sentences and returns the first sentence.
     * @param currentSentence Current sentence processed.
     * @return If current sentence is not changed, currentSentence; if changed the next sentence; if next sentence is
     * the last sentence; shuffles the corpus and returns the first sentence.
     */
    public Sentence sentenceUpdate(Sentence currentSentence){
        sentencePosition++;
        if (sentencePosition >= currentSentence.wordCount()) {
            wordCount += currentSentence.wordCount();
            sentenceIndex++;
            sentencePosition = 0;
            if (sentenceIndex == corpus.sentenceCount()){
                iterationCount++;
                wordCount = 0;
                lastWordCount = 0;
                sentenceIndex = 0;
                corpus.shuffleSentences(1);
            }
            return corpus.getSentence(sentenceIndex);
        }
        return currentSentence;
    }
}
