package WordToVec;

public class WordPair{
    private final String word1;
    private final String word2;
    private double relatedBy;

    /**
     * Constructor of the WordPair object. WordPair stores the information about two words and their similarity scores.
     * @param word1 First word
     * @param word2 Second word
     * @param relatedBy Similarity score between first and second word.
     */
    public WordPair(String word1, String word2, double relatedBy) {
        this.word1 = word1;
        this.word2 = word2;
        this.relatedBy = relatedBy;
    }

    @Override
    public boolean equals(Object second) {
        if (this == second)
            return true;
        if (!(second instanceof WordPair))
            return false;
        WordPair secondWordPair = (WordPair) second;
        return word1.equals(secondWordPair.word1) && word2.equals(secondWordPair.word2);
    }

    /**
     * Accessor for the similarity score.
     * @return Similarity score.
     */
    public double getRelatedBy() {
        return relatedBy;
    }

    /**
     * Mutator for the similarity score.
     * @param relatedBy New similarity score
     */
    public void setRelatedBy(double relatedBy){
        this.relatedBy = relatedBy;
    }

    /**
     * Accessor for the first word.
     * @return First word.
     */
    public String getWord1() {
        return word1;
    }

    /**
     * Accessor for the second word.
     * @return Second word.
     */
    public String getWord2() {
        return word2;
    }

}
