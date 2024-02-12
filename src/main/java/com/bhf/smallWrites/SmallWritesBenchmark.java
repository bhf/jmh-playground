package com.bhf.smallWrites;

import org.openjdk.jmh.infra.Blackhole;

/**
 * All benchmarks for small writes should also be tested in
 * /test by using getDataWritten
 *
 * Some implementations may be asynch whilst others synchronous,
 * so you must be wary of what the no. of ops/sec means, but
 * this structure will still allow comparing the cache behaviour
 */
public interface SmallWritesBenchmark {

    public void doWrite(Blackhole bh);

    public void getDataWritten();

    public void initDataToWrite();

}
