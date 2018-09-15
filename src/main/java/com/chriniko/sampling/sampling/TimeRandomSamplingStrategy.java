package com.chriniko.sampling.sampling;

public class TimeRandomSamplingStrategy implements SamplingStrategy {

    public SamplingOutput perform(Double inputTrafficThreshold) {

        long randomFactor = System.currentTimeMillis();
        boolean outcome = randomFactor % 1000 < inputTrafficThreshold * 1000;

        String inequalityAsString = String.format("[randomFactor mod 1000 < inputTrafficThreshold * 1000] " +
                        "---> [%s mod 1000 < %s * 1000] " +
                        "---> [%s < %s]",
                randomFactor,
                inputTrafficThreshold,
                randomFactor % 1000,
                inputTrafficThreshold * 1000);

        return new SamplingOutput(inputTrafficThreshold.toString(),
                String.valueOf(randomFactor % 1000),
                inequalityAsString,
                outcome);
    }
}
