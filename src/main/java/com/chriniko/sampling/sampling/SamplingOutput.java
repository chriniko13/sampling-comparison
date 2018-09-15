package com.chriniko.sampling.sampling;

public class SamplingOutput {

    private final String trafficThresholdInput;
    private final String randomFactor;
    private final String inequality;
    private final boolean samplingOutcome;

    SamplingOutput(String trafficThresholdInput, String randomFactor, String inequality, boolean samplingOutcome) {
        this.trafficThresholdInput = trafficThresholdInput;
        this.randomFactor = randomFactor;
        this.inequality = inequality;
        this.samplingOutcome = samplingOutcome;
    }

    public String getTrafficThresholdInput() {
        return trafficThresholdInput;
    }

    public String getRandomFactor() {
        return randomFactor;
    }

    public String getInequality() {
        return inequality;
    }

    public boolean isSamplingOutcome() {
        return samplingOutcome;
    }

    @Override
    public String toString() {
        return "SamplingOutput{" +
                "trafficThresholdInput='" + trafficThresholdInput + '\'' +
                ", randomFactor='" + randomFactor + '\'' +
                ", inequality='" + inequality + '\'' +
                ", samplingOutcome=" + samplingOutcome +
                '}';
    }
}
