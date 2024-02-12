
package com.bhf.averages;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.openjdk.jmh.annotations.*;

/**
 * Use the Apache commons maths lib to calculate the mean
 */
@State(Scope.Thread)
public class CalculatingAveragesApache {

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public double[] vals;
    Mean m = new Mean();

    @Setup(Level.Iteration)
    public void populateArray(){
        vals=new double[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }
        m.clear();
    }

    /**
     * Calculate the average of an array using
     * the first moment
     *
     * @return
     */
    @Benchmark
    public double testApacheFirstMoment() {
        return m.evaluate(vals);
    }

}
