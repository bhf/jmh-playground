package com.bhf.averages;

import org.openjdk.jmh.annotations.*;

/**
 * Break up the vals array of size noOfValues into multiple
 * smaller arrays, each of length singleArraySize
 */
@State(Scope.Thread)
public class CalculatingAveragesMultiArray {

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int singleArraySize;

    public double[][] vals;
    public double[] flat;

    @Setup(Level.Iteration)
    public void populateArray(){
        int buckets=noOfValues/singleArraySize;
        flat=new double[noOfValues];
        int vc=0;

        if(buckets>1){
            int perBucket=singleArraySize;
            vals=new double[buckets][perBucket];

            for(int i = 0; i <vals.length ; i++) {
                double[] sub = vals[i];
                for(int j = 0; j < sub.length; j++) {
                    vals[i][j]=i*j;
                    flat[vc++]=i*j;
                }
            }
        }
        else{
            System.exit(0);
        }
    }

    /**
     * Calculate the average of the array of arrays
     *
     * @return
     */
    @Benchmark
    public double testMultiArrayAverage() {

       double sum=0;
       double count=0;
       //vals.length typically much smaller than the sub.length
       for(int i = 0; i <vals.length ; i++) {
           double[] sub = vals[i];
           for(int j = 0; j < sub.length; j++) {
               sum+=vals[i][j];
               count++;
           }
       }

        return sum/count;

    }

}
