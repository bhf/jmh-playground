/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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
