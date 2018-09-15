package com.chriniko.sampling.sampling;

public interface SamplingStrategy {

    SamplingOutput perform(Double inputTrafficThreshold);
}
