package WordToVec;

import java.util.ArrayList;
import java.util.Collections;

import Corpus.CorpusStream;
import Corpus.Sentence;
import DataStructure.CounterHashMap;
import Dictionary.TurkishWordComparator;
import Dictionary.Word;

public class Vocabulary {
    private ArrayList<VocabularyWord> vocabulary;
    private int[] table;
    private int totalNumberOfWords = 0;

    /**
     * Constructor for the {@link Vocabulary} class. For each distinct word in the corpus, a {@link VocabularyWord}
     * instance is created. After that, words are sorted according to their occurrences. Unigram table is constructed,
     * where after Huffman tree is created based on the number of occurrences of the words.
     * @param corpus Corpus used to train word vectors using Word2Vec algorithm.
     */
    public Vocabulary(CorpusStream corpus){
        CounterHashMap<String> counts = new CounterHashMap<>();
        corpus.open();
        Sentence sentence = corpus.getSentence();
        while (sentence != null){
            for (int i = 0; i < sentence.wordCount(); i++){
                counts.put(sentence.getWord(i).getName());
            }
            totalNumberOfWords += sentence.wordCount();
            sentence = corpus.getSentence();
        }
        corpus.close();
        vocabulary = new ArrayList<>();
        for (String word : counts.keySet()){
            vocabulary.add(new VocabularyWord(word, counts.get(word)));
        }
        Collections.sort(vocabulary);
        createUniGramTable();
        constructHuffmanTree();
        Collections.sort(vocabulary, new TurkishWordComparator());
    }

    /**
     * Returns number of words in the vocabulary.
     * @return Number of words in the vocabulary.
     */
    public int size(){
        return vocabulary.size();
    }

    /**
     * Searches a word and returns the position of that word in the vocabulary. Search is done using binary search.
     * @param word Word to be searched.
     * @return Position of the word searched.
     */
    public int getPosition(Word word){
        return Collections.binarySearch(vocabulary, word, new TurkishWordComparator());
    }

    public int getTotalNumberOfWords(){
        return totalNumberOfWords;
    }

    /**
     * Returns the word at a given index.
     * @param index Index of the word.
     * @return The word at a given index.
     */
    public VocabularyWord getWord(int index){
        return vocabulary.get(index);
    }

    /**
     * Constructs Huffman Tree based on the number of occurences of the words.
     */
    private void constructHuffmanTree(){
        int min1i, min2i, b, i;
        int[] count = new int[vocabulary.size() * 2 + 1];
        int[] code = new int[VocabularyWord.MAX_CODE_LENGTH];
        int[] point = new int[VocabularyWord.MAX_CODE_LENGTH];
        int[] binary = new int[vocabulary.size() * 2 + 1];
        int[] parentNode = new int[vocabulary.size() * 2 + 1];
        for (int a = 0; a < vocabulary.size(); a++)
            count[a] = vocabulary.get(a).getCount();
        for (int a = vocabulary.size(); a < vocabulary.size() * 2; a++)
            count[a] = 1000000000;
        int pos1 = vocabulary.size() - 1;
        int pos2 = vocabulary.size();
        for (int a = 0; a < vocabulary.size() - 1; a++) {
            if (pos1 >= 0) {
                if (count[pos1] < count[pos2]) {
                    min1i = pos1;
                    pos1--;
                } else {
                    min1i = pos2;
                    pos2++;
                }
            } else {
                min1i = pos2;
                pos2++;
            }
            if (pos1 >= 0) {
                if (count[pos1] < count[pos2]) {
                    min2i = pos1;
                    pos1--;
                } else {
                    min2i = pos2;
                    pos2++;
                }
            } else {
                min2i = pos2;
                pos2++;
            }
            count[vocabulary.size() + a] = count[min1i] + count[min2i];
            parentNode[min1i] = vocabulary.size() + a;
            parentNode[min2i] = vocabulary.size() + a;
            binary[min2i] = 1;
        }
        for (int a = 0; a < vocabulary.size(); a++) {
            b = a;
            i = 0;
            while (true) {
                code[i] = binary[b];
                point[i] = b;
                i++;
                b = parentNode[b];
                if (b == vocabulary.size() * 2 - 2)
                    break;
            }
            vocabulary.get(a).setCodeLength(i);
            vocabulary.get(a).setPoint(0, vocabulary.size() - 2);
            for (b = 0; b < i; b++) {
                vocabulary.get(a).setCode(i - b - 1, code[b]);
                vocabulary.get(a).setPoint(i - b, point[b] - vocabulary.size());
            }
        }
    }

    /**
     * Constructs the unigram table based on the number of occurences of the words.
     */
    private void createUniGramTable(){
        int i;
        double total = 0;
        double d1;
        table = new int[2 * vocabulary.size()];
        for (VocabularyWord vocabularyWord : vocabulary) {
            total += Math.pow(vocabularyWord.getCount(), 0.75);
        }
        i = 0;
        d1 = Math.pow(vocabulary.get(i).getCount(), 0.75) / total;
        for (int a = 0; a < 2 * vocabulary.size(); a++) {
            table[a] = i;
            if (a / (2 * vocabulary.size() + 0.0) > d1) {
                i++;
                d1 += Math.pow(vocabulary.get(i).getCount(), 0.75) / total;
            }
            if (i >= vocabulary.size())
                i = vocabulary.size() - 1;
        }
    }

    /**
     * Accessor for the unigram table.
     * @param index Index of the word.
     * @return Unigram table value at a given index.
     */
    public int getTableValue(int index){
        return table[index];
    }

    /**
     * Returns size of the unigram table.
     * @return Size of the unigram table.
     */
    public int getTableSize(){
        return table.length;
    }

}
