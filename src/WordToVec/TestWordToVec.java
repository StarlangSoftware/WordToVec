package WordToVec;

import Corpus.Corpus;
import Dictionary.VectorizedDictionary;
import Dictionary.VectorizedWord;
import Math.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TestWordToVec {

    private static void learnCBow(Corpus corpus, String fileName){
        WordToVecParameter parameter = new WordToVecParameter();
        parameter.setCbow(true);
        NeuralNetwork neuralNetwork = new NeuralNetwork(corpus, parameter);
        VectorizedDictionary dictionary = null;
        try {
            dictionary = neuralNetwork.train();
        } catch (MatrixColumnMismatch | VectorSizeMismatch matrixColumnMismatch) {
        }
        dictionary.save(fileName);
    }

    private static void learnSkipGram(Corpus corpus, String fileName){
        WordToVecParameter parameter = new WordToVecParameter();
        parameter.setCbow(false);
        NeuralNetwork neuralNetwork = new NeuralNetwork(corpus, parameter);
        VectorizedDictionary dictionary = null;
        try {
            dictionary = neuralNetwork.train();
        } catch (MatrixColumnMismatch | VectorSizeMismatch matrixColumnMismatch) {
        }
        dictionary.save(fileName);
    }

    private static void learnTurkishWordModels(String dataSet, boolean cbow){
        String[] models = {"root", "suffix", "surface"};
        WordToVecParameter parameter = new WordToVecParameter();
        parameter.setCbow(cbow);
        for (String model : models){
            Corpus corpus = new Corpus(dataSet + "-" + model + ".txt");
            NeuralNetwork neuralNetwork = new NeuralNetwork(corpus, parameter);
            VectorizedDictionary dictionary = null;
            try {
                dictionary = neuralNetwork.train();
            } catch (MatrixColumnMismatch | VectorSizeMismatch matrixColumnMismatch) {
            }
            if (cbow){
                dictionary.save(dataSet + "-" + model + "-cbow.bin");
            } else {
                dictionary.save(dataSet + "-" + model + "-skipgram.bin");
            }
        }
    }

    private static String mostSimilarWord(String fileName, String name){
        FileInputStream inFile;
        ObjectInputStream inObject;
        VectorizedDictionary dictionary;
        try {
            inFile = new FileInputStream(fileName);
            inObject = new ObjectInputStream(inFile);
            dictionary = (VectorizedDictionary) inObject.readObject();
            return dictionary.mostSimilarWord(name).getName();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String mostSimilarKWords(String fileName, String name, int k){
        FileInputStream inFile;
        ObjectInputStream inObject;
        VectorizedDictionary dictionary;
        try {
            inFile = new FileInputStream(fileName);
            inObject = new ObjectInputStream(inFile);
            dictionary = (VectorizedDictionary) inObject.readObject();
            ArrayList<VectorizedWord> result = dictionary.mostSimilarKWords(name, k);
            for (VectorizedWord word: result){
                System.out.println(word.getName());
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void kMeansClustering(String fileName, int k){
        FileInputStream inFile;
        ObjectInputStream inObject;
        VectorizedDictionary dictionary;
        try {
            inFile = new FileInputStream(fileName);
            inObject = new ObjectInputStream(inFile);
            dictionary = (VectorizedDictionary) inObject.readObject();
            ArrayList clusters[] = dictionary.kMeansClustering(10, k);
            for (int i = 0; i < k; i++){
                System.out.println("CLUSTER " + (i + 1));
                System.out.println("---------------------------------");
                for (int j = 0; j < clusters[i].size(); j++){
                    VectorizedWord word = (VectorizedWord) clusters[i].get(j);
                    System.out.println(word.getName());
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        learnCBow(new Corpus("./Data/Corpus/gazete-root.txt"), "root-cbow.bin");
    }

}
