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
