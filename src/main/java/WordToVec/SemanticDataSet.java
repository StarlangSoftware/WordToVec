package WordToVec;

import Dictionary.VectorizedDictionary;
import Dictionary.VectorizedWord;
import Util.FileUtils;
import Math.VectorSizeMismatch;

import java.util.ArrayList;
import java.util.Scanner;

public class SemanticDataSet {

    private ArrayList<WordPair> pairs;

    public SemanticDataSet(){
        pairs = new ArrayList<>();
    }
    public SemanticDataSet(String fileName){
        pairs = new ArrayList<>();
        Scanner scanner = new Scanner(FileUtils.getInputStream(fileName));
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] items = line.split(" ");
            pairs.add(new WordPair(items[0], items[1], Double.parseDouble(items[2])));
        }
    }

    public SemanticDataSet calculateSimilarities(VectorizedDictionary dictionary) {
        SemanticDataSet result = new SemanticDataSet();
        double similarity;
        for (int i = 0; i < pairs.size(); i++){
            String word1 = pairs.get(i).getWord1();
            String word2 = pairs.get(i).getWord2();
            VectorizedWord vectorizedWord1 = (VectorizedWord) dictionary.getWord(word1);
            VectorizedWord vectorizedWord2 = (VectorizedWord) dictionary.getWord(word2);
            similarity = 0;
            try{
                if (vectorizedWord1 != null && vectorizedWord2 != null){
                    similarity = vectorizedWord1.getVector().cosineSimilarity(vectorizedWord2.getVector());
                }
            } catch (VectorSizeMismatch ignored){
            }
            if (similarity > 0){
                result.pairs.add(new WordPair(word1, word2, similarity));
            } else {
                pairs.remove(i);
                i--;
            }
        }
        return result;
    }

    public int size(){
        return pairs.size();
    }

    private void sort(){
        pairs.sort(new WordPairComparator());
    }

    public int index(WordPair wordPair){
        for (int i = 0; i < pairs.size(); i++){
            if (wordPair.equals(pairs.get(i))){
                return i;
            }
        }
        return -1;
    }

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
