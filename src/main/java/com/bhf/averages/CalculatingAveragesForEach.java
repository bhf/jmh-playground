
package com.bhf.averages;

import org.openjdk.jmh.annotations.*;

/**
 * Calculating averages using types of for loops
 */
@State(Scope.Thread)
public class CalculatingAveragesForEach {

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
     * Calculate the average of an array using foreach
     *
     * @return
     */
    @Benchmark
    public double testSimpleArrayAverageForEach() {
        double sum=0;
        for(double d : vals) {
            sum+=d;
        }

        return sum/(double)vals.length;
    }



}
