package com.bhf.averages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.junit.jupiter.api.Test;

public class TestAverageCalculation {

    @Test
    public void testSimpleAverage(){
        CalculatingAveragesFori calc=new CalculatingAveragesFori();
        calc.noOfValues=32;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedForI=calc.testSimpleArrayAverageForI();
        assertEquals(meanValue, calculatedForI);
    }

    @Test
    public void testSimpleAverageForEach(){
        CalculatingAveragesForEach calc=new CalculatingAveragesForEach();
        calc.noOfValues=32;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedForEach=calc.testSimpleArrayAverageForEach();
        assertEquals(meanValue, calculatedForEach);
    }

    private static double getValidMean(double[] calc) {
        Mean mean = new Mean();
        double meanValue=mean.evaluate(calc);
        return meanValue;
    }

    private static double getValidMean(long[] vals) {
        double sum=0;
        for(int i = 0; i <vals.length ; i++) {
            sum+=vals[i];
        }
        return sum/(double)vals.length;
    }

    @Test
    public void testIncrementalAverageCalculation(){
        CalculatingAveragesStrafed calc=new CalculatingAveragesStrafed();
        calc.noOfValues=38;
        calc.strafe=3;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedSubRegions=calc.testSimpleArrayAverageForISubRegions();
        assertEquals(meanValue, calculatedSubRegions);
    }

    @Test
    public void testParallelPrefix(){
        CalculatingAveragesParallelPrefix calc=new CalculatingAveragesParallelPrefix();
        calc.noOfValues=38;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedSubRegions=calc.testAverageParallelPrefix();
        assertEquals(meanValue, calculatedSubRegions);
    }

    @Test
    public void testMultiArray(){
        CalculatingAveragesMultiArray calc=new CalculatingAveragesMultiArray();
        calc.noOfValues=32;
        calc.singleArraySize=8;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.flat);

        double calculatedSubRegions=calc.testMultiArrayAverage();
        assertEquals(meanValue, calculatedSubRegions);
    }

    @Test
    public void testWithFuturesCompletionService(){
        CalculatingAveragesFutures calc=new CalculatingAveragesFutures();
        calc.noOfValues=32;
        calc.populateArray();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedSubRegions=calc.testAverageWithFutures();
        assertEquals(meanValue, calculatedSubRegions);
    }

    @Test
    public void testWithParallelAtomicLong(){
        CalculatingAveragesParallelAtomic calc = new CalculatingAveragesParallelAtomic();
        calc.noOfValues=128;
        calc.populateArray();
        calc.setup();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedSubRegions=calc.testAverageWithParallelAtomic();
        assertEquals(meanValue, calculatedSubRegions);
    }

    @Test
    public void testWithParallelAtomicRef(){
        CalculatingAveragesParallelAtomicRef calc = new CalculatingAveragesParallelAtomicRef();
        calc.noOfValues=32;
        calc.populateArray();
        calc.setup();

        // use apache commons math to verify the mean value
        double meanValue = getValidMean(calc.vals);

        double calculatedSubRegions=calc.testAverageWithParallelAtomicRef();
        assertEquals(meanValue, calculatedSubRegions);
    }

}
