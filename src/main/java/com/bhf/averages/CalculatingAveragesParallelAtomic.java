package com.bhf.averages;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import org.openjdk.jmh.annotations.*;

/**
 * Calculating averages using parallel threads summing on an atomic
 */
@State(Scope.Thread)
public class CalculatingAveragesParallelAtomic {
    final int THREADS=2;

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public long[] vals;
    ExecutorService mexec =Executors.newFixedThreadPool(THREADS);

    @Setup(Level.Trial)
    public void populateArray(){
        vals=new long[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }
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
    public double testAverageWithParallelAtomic() {

        AtomicLong atomicSum=new AtomicLong();
        CountDownLatch latch=new CountDownLatch(2);

        for(int i=0;i<2;i++){
            final int finalI1 = i;
            mexec.submit(new Runnable() {
                @Override
                public void run() {
                    long threadSum=0;
                    for(int j = finalI1; j <vals.length ; j+=2) {
                            threadSum+=vals[j];
                    }
                    atomicSum.getAndAdd(threadSum);
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long s=atomicSum.get();
        double n=s;
        return n/(double)vals.length;
    }



}
