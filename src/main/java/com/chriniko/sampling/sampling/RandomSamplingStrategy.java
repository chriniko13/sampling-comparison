package com.chriniko.sampling.sampling;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSamplingStrategy implements SamplingStrategy {

    public SamplingOutput perform(Double inputTrafficThreshold) {

        double randomNumber = ThreadLocalRandom.current().nextDouble(0.1D, 1.1D);

        BigDecimal scaledRandomNumber = BigDecimal
                .valueOf(randomNumber)
                .round(new MathContext(2, RoundingMode.FLOOR));

        BigDecimal trafficThresholdBD = BigDecimal.valueOf(inputTrafficThreshold);

        String inequalityAsString = String.format("[scaledRandomNumber <= trafficThresholdBD] ---> [%s <= %s]",
                scaledRandomNumber, trafficThresholdBD);

        boolean outcome = scaledRandomNumber.compareTo(trafficThresholdBD) <= 0;

        return new SamplingOutput(inputTrafficThreshold.toString(),
                scaledRandomNumber.toString(),
                inequalityAsString,
                outcome);

    }
}
