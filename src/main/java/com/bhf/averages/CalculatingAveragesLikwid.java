package com.bhf.averages;

import org.jl.perftools.likwid.ILikwidMarker;
import org.jl.perftools.likwid.LikwidMarkerAPIProvider;
import org.openjdk.jmh.annotations.*;

/**
 * Using this to compare a likwid marked vs unmarked vs likwid wrapped
 * to assess overhead+variance introduced by marking and profiling techniques
 */
@State(Scope.Thread)
public class CalculatingAveragesLikwid {

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public double[] vals;

    ILikwidMarker marker;

    public CalculatingAveragesLikwid() {
        marker = LikwidMarkerAPIProvider.getInstance();
    }

    @Setup(Level.Iteration)
    public void populateArray(){
        vals=new double[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }

        marker = LikwidMarkerAPIProvider.getInstance();
    }

    @TearDown
    public void tearDown(){
        marker.close();
    }

    @Benchmark
    public double testSimpleArrayAverageForIMarked() {
        marker.start("fori");
        double sum=0;
        for(int i = 0; i < vals.length; i++) {
            sum+=vals[i];
        }
        marker.stop("fori");
        return sum/(double)vals.length;
    }

    @Benchmark
    public double testSimpleArrayAverageForEachMarked() {
        marker.start("foreach");
        double sum=0;
        for(double d : vals) {
            sum+=d;
        }
        marker.stop("foreach");
        return sum/(double)vals.length;
    }

}
