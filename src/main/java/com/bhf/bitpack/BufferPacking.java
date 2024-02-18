package com.bhf.bitpack;

import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.openjdk.jmh.annotations.*;

/**
 * Unpacking types of different bit lengths
 */
@State(Scope.Thread)
public class BufferPacking {

  @Param({"16","32","64"})
  public int noOfValues;

  DirectBuffer buffer;
  public byte[] vals;
  byte[] packedBytes;

  @Setup(Level.Iteration)
  public void setup() {
    packedBytes = new byte[noOfValues];
    vals = new byte[noOfValues];
    for (int i = 0; i < vals.length; i++) {
      vals[i] = (byte)i;
    }
    buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(noOfValues, 32));
  }

  @Param({"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"})
 // @Param({"4","8","16"})
  public int bitsPerType;
  /**
   *
   * @return
   */
  @Benchmark
  public double testUnpackBits() {

    buffer.getBytes(0, packedBytes, 0, noOfValues);
    double sum = 0;

    int last=(8*noOfValues)/bitsPerType;
    for (int i = 0; i < last; i++) {
      int res = BitPacker.get(packedBytes, i, bitsPerType);
      sum += res;
    }

    return sum;
  }


}
