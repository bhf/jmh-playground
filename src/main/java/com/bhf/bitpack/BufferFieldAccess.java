package com.bhf.bitpack;

import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Comparing field access times
 */
@State(Scope.Thread)
public class BufferFieldAccess {

  @Param({"32", "64", "128"})
  public int bufferSize;
  DirectBuffer buffer;

  @Setup(Level.Iteration)
  public void setup() {
    buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(bufferSize, 32));
  }


  @Benchmark
  public void testReadLongs(Blackhole bh) {
    double sum=0;
    int items=bufferSize/8;
    for(int i=0;i<items;i++){
      sum+= buffer.getLong(i);
    }

    bh.consume(sum);
  }

  @Benchmark
  public void testReadInts(Blackhole bh) {
    double sum=0;
    int items=bufferSize/4;
    for(int i=0;i<items;i++){
      sum+= buffer.getInt(i);
    }

    bh.consume(sum);
  }

  @Benchmark
  public void testReadShorts(Blackhole bh) {
    double sum=0;
    int items=bufferSize/2;
    for(int i=0;i<items;i++){
      sum+= buffer.getShort(i);
    }

    bh.consume(sum);
  }

  @Benchmark
  public void testReadChar(Blackhole bh) {
    double sum=0;
    int items=bufferSize/2;
    for(int i=0;i<items;i++){
      sum+= buffer.getChar(i);
    }

    bh.consume(sum);
  }

  @Benchmark
  public void testReadDouble(Blackhole bh) {
    double sum=0;
    int items=bufferSize/8;
    for(int i=0;i<items;i++){
      sum+= buffer.getShort(i);
    }

    bh.consume(sum);
  }

  @Benchmark
  public void testReadFloat(Blackhole bh) {
    double sum=0;
    int items=bufferSize/4;
    for(int i=0;i<items;i++){
      sum+= buffer.getFloat(i);
    }

    bh.consume(sum);
  }

}

