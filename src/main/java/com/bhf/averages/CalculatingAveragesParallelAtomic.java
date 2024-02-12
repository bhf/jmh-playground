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
