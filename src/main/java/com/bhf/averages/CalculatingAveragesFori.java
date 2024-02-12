

package com.bhf.averages;

import org.openjdk.jmh.annotations.*;

/**
 * Calculating averages using types of for loops
 */
@State(Scope.Thread)
public class CalculatingAveragesFori {

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
     * Calculate the average of an array using for i
     *
     * @return
     */
    @Benchmark
    public double testSimpleArrayAverageForI() {
        double sum=0;
        for(int i = 0; i < vals.length; i++) {
            sum+=vals[i];
        }

        return sum/(double)vals.length;
    }

}
