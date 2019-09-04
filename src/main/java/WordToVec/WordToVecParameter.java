package WordToVec;

public class WordToVecParameter {
    private int layerSize = 100;
    private boolean cbow = true;
    private double alpha = 0.025;
    private int window = 5;
    private boolean hierarchicalSoftMax = false;
    private int negativeSamplingSize = 5;
    private int numberOfIterations = 3;

    /**
     * Empty constructor for Word2Vec parameter
     */
    public WordToVecParameter(){
    }

    /**
     * Accessor for layerSize attribute.
     * @return Size of the word vectors.
     */
    public int getLayerSize() {
        return layerSize;
    }

    /**
     * Accessor for CBow attribute.
     * @return True is CBow will be applied, false otherwise.
     */
    public boolean isCbow() {
        return cbow;
    }

    /**
     * Accessor for the alpha attribute.
     * @return Current learning rate alpha.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Accessor for the window size attribute.
     * @return Current window size.
     */
    public int getWindow() {
        return window;
    }

    /**
     * Accessor for the hierarchicalSoftMax attribute.
     * @return If hierarchical softmax will be applied, returns true; false otherwise.
     */
    public boolean isHierarchicalSoftMax() {
        return hierarchicalSoftMax;
    }

    /**
     * Accessor for the negativeSamplingSize attribute.
     * @return Number of negative samples that will be withdrawn.
     */
    public int getNegativeSamplingSize() {
        return negativeSamplingSize;
    }

    /**
     * Accessor for the numberOfIterations attribute.
     * @return Number of epochs to train the network.
     */
    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    /**
     * Mutator for the layerSize attribute.
     * @param layerSize New size of the word vectors.
     */
    public void setLayerSize(int layerSize) {
        this.layerSize = layerSize;
    }

    /**
     * Mutator for cBow attribute
     * @param cbow True if CBow applied; false if SkipGram applied.
     */
    public void setCbow(boolean cbow) {
        this.cbow = cbow;
    }

    /**
     * Mutator for alpha attribute
     * @param alpha New learning rate.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Mutator for the window size attribute.
     * @param window New window size.
     */
    public void setWindow(int window) {
        this.window = window;
    }

    /**
     * Mutator for the hierarchicalSoftMax attribute.
     * @param hierarchicalSoftMax True is hierarchical softMax applied; false otherwise.
     */
    public void setHierarchicalSoftMax(boolean hierarchicalSoftMax) {
        this.hierarchicalSoftMax = hierarchicalSoftMax;
    }

    /**
     * Mutator for the negativeSamplingSize attribute.
     * @param negativeSamplingSize New number of negative instances that will be withdrawn.
     */
    public void setNegativeSamplingSize(int negativeSamplingSize) {
        this.negativeSamplingSize = negativeSamplingSize;
    }

    /**
     * Mutator for the numberOfIterations attribute.
     * @param numberOfIterations New number of iterations.
     */
    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

}
