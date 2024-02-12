package com.bhf.averages;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import org.agrona.concurrent.MemoryAccess;
import org.openjdk.jmh.annotations.*;

/**
 * Calculating averages using parallel threads summing on a double
 * wrapped in an Atomic ref
 */
@State(Scope.Thread)
public class CalculatingAveragesParallelAtomicRef {
    final int THREADS=2;

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public double[] vals;
    ExecutorService mexec =Executors.newFixedThreadPool(THREADS);

    @Setup(Level.Trial)
    public void populateArray(){
        vals=new double[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }

        MemoryAccess.acquireFence();
    }

    @Setup(Level.Trial)
    public void setup(){
        mexec =Executors.newFixedThreadPool(THREADS);
    }

    @TearDown(Level.Trial)
    public void down() {
        if(null!=mexec){
            mexec.shutdown();
        }
    }


    @Benchmark
    public double testAverageWithParallelAtomicRef() {

        AtomicReference<Double> atomicSum=new AtomicReference<Double>();
        CountDownLatch latch=new CountDownLatch(2);

        for(int i=0;i<2;i++){
            final int finalI1 = i;
            mexec.submit(new Runnable() {
                @Override
                public void run() {
                    double threadSum=0;
                    for(int j = finalI1; j <vals.length ; j+=2) {
                            threadSum+=vals[j];
                    }
                    //atomicSum.accumulateAndGet(threadSum, (aDouble, aDouble2) -> aDouble+aDouble2);
                    atomicSum.getAndAccumulate(threadSum, new BinaryOperator<Double>() {
                        @Override
                        public Double apply(Double aDouble, Double aDouble2) {
                            double unbA=null!=aDouble? aDouble.doubleValue() :0;
                            double unbB=null!=aDouble2? aDouble2.doubleValue() :0;
                            return unbA+unbB;
                        }
                    });
                    latch.countDown();

                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double s=atomicSum.get();
        double n=s;
        return n/(double)vals.length;
    }



}
