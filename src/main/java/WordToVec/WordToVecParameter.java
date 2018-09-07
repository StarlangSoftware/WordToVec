package WordToVec;

public class WordToVecParameter {
    private int layerSize = 100;
    private boolean cbow = true;
    private double alpha = 0.025;
    private int window = 5;
    private boolean hierarchicalSoftMax = false;
    private int negativeSamplingSize = 5;
    private int numberOfIterations = 3;

    public WordToVecParameter(){
    }

    public int getLayerSize() {
        return layerSize;
    }

    public boolean isCbow() {
        return cbow;
    }

    public double getAlpha() {
        return alpha;
    }

    public int getWindow() {
        return window;
    }

    public boolean isHierarchicalSoftMax() {
        return hierarchicalSoftMax;
    }

    public int getNegativeSamplingSize() {
        return negativeSamplingSize;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setLayerSize(int layerSize) {
        this.layerSize = layerSize;
    }

    public void setCbow(boolean cbow) {
        this.cbow = cbow;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public void setHierarchicalSoftMax(boolean hierarchicalSoftMax) {
        this.hierarchicalSoftMax = hierarchicalSoftMax;
    }

    public void setNegativeSamplingSize(int negativeSamplingSize) {
        this.negativeSamplingSize = negativeSamplingSize;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

}
