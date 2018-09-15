package com.chriniko.sampling.core;

import com.chriniko.sampling.sampling.RandomSamplingStrategy;
import com.chriniko.sampling.sampling.SamplingOutput;
import com.chriniko.sampling.sampling.SamplingStrategy;
import com.chriniko.sampling.sampling.TimeRandomSamplingStrategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class PreparePhaseOutput {

    private double trafficThreshold;
    private SamplingStrategy randomStrategy;
    private SamplingStrategy timeRandomStrategy;
    private int noOfSamples;
    private Map<String, List<SamplingOutput>> samplingStrategiesOutputs;

    double getTrafficThreshold() {
        return trafficThreshold;
    }

    SamplingStrategy getRandomStrategy() {
        return randomStrategy;
    }

    SamplingStrategy getTimeRandomStrategy() {
        return timeRandomStrategy;
    }

    int getNoOfSamples() {
        return noOfSamples;
    }

    Map<String, List<SamplingOutput>> getSamplingStrategiesOutputs() {
        return samplingStrategiesOutputs;
    }

    PreparePhaseOutput invoke() {
        trafficThreshold = 0.7D;

        randomStrategy = new RandomSamplingStrategy();
        timeRandomStrategy = new TimeRandomSamplingStrategy();

        noOfSamples = 100;

        samplingStrategiesOutputs = new HashMap<>();
        samplingStrategiesOutputs.put("random", new LinkedList<>());
        samplingStrategiesOutputs.put("time-random", new LinkedList<>());
        return this;
    }
}