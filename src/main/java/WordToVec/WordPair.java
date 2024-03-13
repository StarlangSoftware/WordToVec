package WordToVec;

public class WordPair{
    private final String word1;
    private final String word2;
    private double relatedBy;

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

    public double getRelatedBy() {
        return relatedBy;
    }

    public void setRelatedBy(double relatedBy){
        this.relatedBy = relatedBy;
    }

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

}
