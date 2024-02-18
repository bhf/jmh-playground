package com.bhf.bitpack;

import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Getting a byte[] from unsafe buffer
 */
@State(Scope.Thread)
public class BufferHoist {

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

  @Benchmark
  public void testHoist(Blackhole bh) {
    byte[] selected = packedBytes;
    buffer.getBytes(0, selected, 0, selected.length);
    bh.consume(buffer);
  }

}
