package com.bhf.averages;


import java.util.Arrays;
import org.openjdk.jmh.annotations.*;

/**
 * Use parallel prefix with and without array copy
 */
@State(Scope.Thread)
public class CalculatingAveragesParallelPrefix {

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public double[] vals;

    @Setup(Level.Iteration)
    public void populateArray(){
        vals=new double[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }
    }

    /**
     * Calculate the average of an array using
     * parallel prefix
     *
     * @return
     */
    @Benchmark
    public double testAverageParallelPrefix() {
        Arrays.parallelPrefix(vals, 0, vals.length, (x, y) -> x + y);
        return vals[vals.length-1]/(double)vals.length;
    }

    /**
     * Calculate the average of a copy of an array using
     * parallel prefix
     *
     * @return
     */
    @Benchmark
    public double testAverageParallelPrefixWithArrayCopy() {
        double[] copy = Arrays.copyOf(vals, vals.length);
        Arrays.parallelPrefix(copy, 0, copy.length, (x, y) -> x + y);
        return copy[copy.length-1]/(double)copy.length;
    }

}
