package com.bhf.averages;

import org.openjdk.jmh.annotations.*;

/**
 * Calculate the mean in batches
 */
@State(Scope.Thread)
public class CalculatingAveragesStrafed {

    //@Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    @Param({"32", "64"})
    public int noOfValues;

    //@Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    @Param({"4", "8", "16", "32"})
    public int strafe;

    public double[] vals;

    @Setup(Level.Iteration)
    public void populateArray(){
        if(strafe<noOfValues){
            vals=new double[noOfValues];
            for(int i = 0; i <vals.length ; i++) {
                vals[i]=i;
            }
        }
        else{
            System.exit(0);
        }
    }

    /**
     * Calculate the average of an array using for i
     * over sub regions of the array at a time
     *
     * @return
     */
    @Benchmark
    public double testSimpleArrayAverageForISubRegions() {

        if(strafe<vals.length){
            double sum=0;
            for(int i = 0; i < vals.length; i+=strafe) {
                for(int j=i; j<(i+strafe) && (j<vals.length); j++){
                    sum+=vals[j];
                }
            }
            return sum/(double)vals.length;
        }



        return 0;

    }

}
