package WordToVec;

import java.util.Comparator;

public class WordPairComparator implements Comparator<WordPair> {

    @Override
    public int compare(WordPair o1, WordPair o2) {
        return -Double.compare(o1.getRelatedBy(), o2.getRelatedBy());
    }
}
