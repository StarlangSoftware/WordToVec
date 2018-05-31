package WordToVec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import Corpus.Corpus;
import Dictionary.TurkishWordComparator;
import Dictionary.Word;

public class Vocabulary {
    private ArrayList<VocabularyWord> vocabulary;
    private int table[];

    public Vocabulary(Corpus corpus){
        Set<Word> wordList;
        wordList = corpus.getWordList();
        vocabulary = new ArrayList<>();
        for (Word word: wordList){
            vocabulary.add(new VocabularyWord(word.getName(), corpus.getCount(word)));
        }
        Collections.sort(vocabulary);
        createUniGramTable();
        constructHuffmanTree();
        Collections.sort(vocabulary, new TurkishWordComparator());
    }

    public int size(){
        return vocabulary.size();
    }

    public int getPosition(Word word){
        return Collections.binarySearch(vocabulary, word, new TurkishWordComparator());
    }

    public VocabularyWord getWord(int index){
        return vocabulary.get(index);
    }

    private void constructHuffmanTree(){
        int min1i, min2i, b, i;
        int count[] = new int[vocabulary.size() * 2 + 1];
        int code[] = new int[VocabularyWord.MAX_CODE_LENGTH];
        int point[] = new int[VocabularyWord.MAX_CODE_LENGTH];
        int binary[] = new int[vocabulary.size() * 2 + 1];
        int parentNode[] = new int[vocabulary.size() * 2 + 1];
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

    private void createUniGramTable(){
        int i;
        double total = 0;
        double d1;
        table = new int[2 * vocabulary.size()];
        for (int a = 0; a < vocabulary.size(); a++){
            total += Math.pow(vocabulary.get(a).getCount(), 0.75);
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

    public int getTableValue(int index){
        return table[index];
    }

    public int getTableSize(){
        return table.length;
    }

}
