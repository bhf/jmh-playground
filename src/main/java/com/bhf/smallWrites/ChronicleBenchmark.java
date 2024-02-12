package com.bhf.smallWrites;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

public class ChronicleBenchmark implements SmallWritesBenchmark {

    @Setup(Level.Iteration)
    public void setup(){
        initDataToWrite();
    }

    @Override
    public void doWrite(Blackhole bh) {

    }

    @Override
    public void getDataWritten() {
        // return @State data for unit testing
    }

    @Override
    public void initDataToWrite() {
        // write to @State data
    }
}
