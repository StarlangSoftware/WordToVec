package WordToVec;

import Dictionary.Word;

public class VocabularyWord extends Word implements Comparable{
    private int count;
    private int[] code;
    private int[] point;
    public static int MAX_CODE_LENGTH = 40;
    private int codeLength;

    public VocabularyWord(String name, int count){
        super(name);
        this.count = count;
        code = new int[MAX_CODE_LENGTH];
        point = new int[MAX_CODE_LENGTH];
        codeLength = 0;
    }

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

    public int getCount(){
        return count;
    }

    public void setCodeLength(int codeLength){
        this.codeLength = codeLength;
    }

    public void setCode(int index, int value){
        code[index] = value;
    }

    public void setPoint(int index, int value){
        point[index] = value;
    }

    public int getCodeLength(){
        return codeLength;
    }

    public int getPoint(int index){
        return point[index];
    }

    public int getCode(int index){
        return code[index];
    }
}
