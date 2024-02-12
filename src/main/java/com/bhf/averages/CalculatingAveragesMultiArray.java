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
