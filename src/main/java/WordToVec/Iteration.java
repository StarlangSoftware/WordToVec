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

    public Iteration(Corpus corpus, WordToVecParameter wordToVecParameter){
        this.corpus = corpus;
        this.wordToVecParameter = wordToVecParameter;
        startingAlpha = wordToVecParameter.getAlpha();
        alpha = wordToVecParameter.getAlpha();
    }

    public double getAlpha() {
        return alpha;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    public int getSentencePosition() {
        return sentencePosition;
    }

    public void alphaUpdate(){
        if (wordCount - lastWordCount > 10000) {
            wordCountActual += wordCount - lastWordCount;
            lastWordCount = wordCount;
            alpha = startingAlpha * (1 - wordCountActual / (wordToVecParameter.getNumberOfIterations() * corpus.numberOfWords() + 1.0));
            if (alpha < startingAlpha * 0.0001)
                alpha = startingAlpha * 0.0001;
        }
    }

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
