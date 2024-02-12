# jmh-playground

Some JMH benchmarks and analysis of array averaging. I mostly wrote this to refine my JMH process, not to try and find the average of an array in the fastest way. 


## How much value do we really get from all these features? ##
![](massScatter.png) 

## Array Size vs. Ops/sec ##
![](sizeVsScore.png) 

## Array Size vs. L1 Data Misses ##
![](l1dMiss.png) 

## Array Size vs. Total Stalls ##
![](sizeVsStalls.png) 

## Array Size vs. Total Stalls For All Sizes ##
![](cyclestalls.png) 

## Array Size vs. Total Stalls For Small Arrays ##
Example of dividing up the parameter space

![](cyclestalls-small-size.png) 
