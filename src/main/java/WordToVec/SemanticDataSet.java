package WordToVec;

import Dictionary.VectorizedDictionary;
import Dictionary.VectorizedWord;
import Util.FileUtils;
import Math.VectorSizeMismatch;

import java.util.ArrayList;
import java.util.Scanner;

public class SemanticDataSet {

    private final ArrayList<WordPair> pairs;

    /**
     * Empty constructor for the semantic dataset.
     */
    public SemanticDataSet(){
        pairs = new ArrayList<>();
    }

    /**
     * Constructor for the semantic dataset. Reads word pairs and their similarity scores from an input file.
     * @param fileName Input file that stores the word pair and similarity scores.
     */
    public SemanticDataSet(String fileName){
        pairs = new ArrayList<>();
        Scanner scanner = new Scanner(FileUtils.getInputStream(fileName));
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] items = line.split(" ");
            pairs.add(new WordPair(items[0], items[1], Double.parseDouble(items[2])));
        }
    }

    /**
     * Calculates the similarities between words in the dataset. The word vectors will be taken from the input
     * vectorized dictionary.
     * @param dictionary Vectorized dictionary that stores the word vectors.
     * @return Word pairs and their calculated similarities stored as a semantic dataset.
     */
    public SemanticDataSet calculateSimilarities(VectorizedDictionary dictionary) {
        SemanticDataSet result = new SemanticDataSet();
        double similarity;
        for (int i = 0; i < pairs.size(); i++){
            String word1 = pairs.get(i).getWord1();
            String word2 = pairs.get(i).getWord2();
            VectorizedWord vectorizedWord1 = (VectorizedWord) dictionary.getWord(word1);
            VectorizedWord vectorizedWord2 = (VectorizedWord) dictionary.getWord(word2);
            try{
                if (vectorizedWord1 != null && vectorizedWord2 != null){
                    similarity = vectorizedWord1.getVector().cosineSimilarity(vectorizedWord2.getVector());
                    result.pairs.add(new WordPair(word1, word2, similarity));
                } else {
                    pairs.remove(i);
                    i--;
                }
            } catch (VectorSizeMismatch ignored){
            }
        }
        return result;
    }

    /**
     * Returns the size of the semantic dataset.
     * @return The size of the semantic dataset.
     */
    public int size(){
        return pairs.size();
    }

    /**
     * Sorts the word pairs in the dataset according to the WordPairComparator.
     */
    private void sort(){
        pairs.sort(new WordPairComparator());
    }

    /**
     * Finds and returns the index of a word pair in the pairs array list. If there is no such word pair, it
     * returns -1.
     * @param wordPair Word pair to search in the semantic dataset.
     * @return Index of the given word pair in the pairs array list. If it does not exist, the method returns -1.
     */
    public int index(WordPair wordPair){
        for (int i = 0; i < pairs.size(); i++){
            if (wordPair.equals(pairs.get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates the Spearman correlation coefficient with this dataset to the given semantic dataset.
     * @param semanticDataSet Given semantic dataset with which Spearman correlation coefficient is calculated.
     * @return Spearman correlation coefficient with the given semantic dataset.
     */
    public double spearmanCorrelation(SemanticDataSet semanticDataSet){
        double sum = 0;
        int rank1, rank2;
        sort();
        semanticDataSet.sort();
        for (int i = 0; i < pairs.size(); i++){
            rank1 = i + 1;
            if (semanticDataSet.index(pairs.get(i)) != -1){
                rank2 = semanticDataSet.index(pairs.get(i)) + 1;
            } else {
                System.out.println("Error in ranks");
                return -1;
            }
            double di = rank1 - rank2;
            sum += 6 * di * di;
        }
        double n = pairs.size();
        double ratio = sum / (n * (n * n - 1));
        return 1 - ratio;
    }
}
