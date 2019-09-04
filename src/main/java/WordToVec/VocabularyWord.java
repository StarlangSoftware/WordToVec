package WordToVec;

import Dictionary.Word;

public class VocabularyWord extends Word implements Comparable{
    private int count;
    private int[] code;
    private int[] point;
    public static int MAX_CODE_LENGTH = 40;
    private int codeLength;

    /**
     * Constructor for a {@link VocabularyWord}. The constructor gets name and count values and sets the corresponding
     * attributes. It also initializes the code and point arrays for this word.
     * @param name Lemma of the word
     * @param count Number of occurences of this word in the corpus
     */
    public VocabularyWord(String name, int count){
        super(name);
        this.count = count;
        code = new int[MAX_CODE_LENGTH];
        point = new int[MAX_CODE_LENGTH];
        codeLength = 0;
    }

    /**
     * Comparator interface to other VocabularyWord's.
     * @param o Compared word
     * @return If the number of occurences of the current word is less than the number of occurences of o, returns 1.
     * If the number of occurences of the current word is larger than the number of occurences of o, returns -1.
     * Otherwise, returns 0.
     */
    public int compareTo(Object o) {
        VocabularyWord word = (VocabularyWord) o;
        if (count < word.count){
            return 1;
        } else {
            if (count > word.count){
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Accessor for the count attribute.
     * @return Number of occurences of this word.
     */
    public int getCount(){
        return count;
    }

    /**
     * Mutator for codeLength attribute.
     * @param codeLength New value for the codeLength.
     */
    public void setCodeLength(int codeLength){
        this.codeLength = codeLength;
    }

    /**
     * Mutator for code attribute.
     * @param index Index of the code
     * @param value New value for that indexed element of code.
     */
    public void setCode(int index, int value){
        code[index] = value;
    }

    /**
     * Mutator for point attribute.
     * @param index Index of the point
     * @param value New value for that indexed element of point.
     */
    public void setPoint(int index, int value){
        point[index] = value;
    }

    /**
     * Accessor for the codeLength attribute.
     * @return Length of the Huffman code for this word.
     */
    public int getCodeLength(){
        return codeLength;
    }

    /**
     * Accessor for point attribute.
     * @param index Index of the point.
     * @return Value for that indexed element of point.
     */
    public int getPoint(int index){
        return point[index];
    }

    /**
     * Accessor for code attribute.
     * @param index Index of the code.
     * @return Value for that indexed element of code.
     */
    public int getCode(int index){
        return code[index];
    }
}
