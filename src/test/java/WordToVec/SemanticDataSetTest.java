package WordToVec;

import org.junit.Assert;

public class SemanticDataSetTest {

    @org.junit.Test
    public void testSpearman() {
        SemanticDataSet semanticDataSet = new SemanticDataSet("AnlamverRel.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("MC.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("MEN.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("MTurk771.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("RareWords.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("RG.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.0);
        semanticDataSet = new SemanticDataSet("WS353.txt");
        Assert.assertEquals(1.0, semanticDataSet.spearmanCorrelation(semanticDataSet), 0.00001);
    }

}
