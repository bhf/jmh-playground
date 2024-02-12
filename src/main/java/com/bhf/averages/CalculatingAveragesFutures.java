package com.bhf.averages;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.*;

/**
 * Calculating averages using a completion service
 */
@State(Scope.Thread)
public class CalculatingAveragesFutures {
    final int THREADS=2;

    @Param({"4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096"})
    public int noOfValues;

    public double[] vals;
    ExecutorService completionExecutor=Executors.newFixedThreadPool(THREADS);
    ExecutorCompletionService completionService=new ExecutorCompletionService(completionExecutor);

    @Setup(Level.Iteration)
    public void populateArray(){
        vals=new double[noOfValues];
        for(int i = 0; i <vals.length ; i++) {
            vals[i]=i;
        }
    }

    @TearDown
    public void tearDown(){
        completionExecutor.shutdownNow();
    }

    @Benchmark
    public double testAverageWithFutures() {

        int perThread=vals.length/THREADS;
        double sumAcrossThreads=0;

        for(int i=0;i<THREADS;i++){
            int finalI = i;
            completionService.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    int startIndex= finalI *perThread;
                    int endIndex=startIndex+perThread;
                    double sum=0;
                    for(int j = startIndex; j <endIndex ; j++) {
                        sum+=vals[j];
                    }
                    return sum;
                }
            });
        }

        for(int i=0;i<THREADS;i++){
            try {
                Future take=completionService.take();
                Object v=take.get();
                Double d= (Double) v;
                sumAcrossThreads+=d;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return sumAcrossThreads/(double)vals.length;
    }



}
