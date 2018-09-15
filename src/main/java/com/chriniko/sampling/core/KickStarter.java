package com.chriniko.sampling.core;

import com.chriniko.sampling.visualization.PieChart;
import com.chriniko.sampling.visualization.XYLineChart;
import com.chriniko.sampling.sampling.SamplingOutput;
import com.chriniko.sampling.sampling.SamplingStrategy;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class KickStarter {

    public static void main(String[] args) {

        // --- prepare phase ---
        final PreparePhaseOutput preparePhaseOutput = new PreparePhaseOutput().invoke();

        double trafficThreshold = preparePhaseOutput.getTrafficThreshold();
        SamplingStrategy randomStrategy = preparePhaseOutput.getRandomStrategy();
        SamplingStrategy timeRandomStrategy = preparePhaseOutput.getTimeRandomStrategy();
        int NO_OF_SAMPLES = preparePhaseOutput.getNoOfSamples();
        Map<String, List<SamplingOutput>> samplingStrategiesOutputs = preparePhaseOutput.getSamplingStrategiesOutputs();


        // --- execution and collection phase ---
        executionAndCollectionPhase(
                trafficThreshold,
                randomStrategy,
                timeRandomStrategy,
                NO_OF_SAMPLES,
                samplingStrategiesOutputs
        );


        // --- animation phase ---
        animationPhase(samplingStrategiesOutputs);

    }

    private static void executionAndCollectionPhase(double trafficThreshold,
                                                    SamplingStrategy randomStrategy,
                                                    SamplingStrategy timeRandomStrategy,
                                                    int NO_OF_SAMPLES,
                                                    Map<String, List<SamplingOutput>> samplingStrategiesOutputs) {
        for (int i = 0; i < NO_OF_SAMPLES; i++) {

            SamplingOutput samplingOutputOfRandomStrategy = randomStrategy.perform(trafficThreshold);
            samplingStrategiesOutputs.get("random").add(samplingOutputOfRandomStrategy);

            SamplingOutput samplingOutputOfTimeRandomStrategy = timeRandomStrategy.perform(trafficThreshold);
            samplingStrategiesOutputs.get("time-random").add(samplingOutputOfTimeRandomStrategy);

        }
    }


    private static void animationPhase(Map<String, List<SamplingOutput>> samplingStrategiesOutputs) {

        // Note: pie chart for random sampling.
        List<SamplingOutput> outputsForRandomSampling = samplingStrategiesOutputs.get("random");
        Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForRandomSampling = outputsForRandomSampling
                .stream()
                .collect(Collectors.groupingBy(SamplingOutput::isSamplingOutcome));

        DefaultPieDataset defaultPieDatasetForRandomSampling = new DefaultPieDataset();
        defaultPieDatasetForRandomSampling.setValue(
                "Pass",
                Optional.ofNullable(outputsPartitionedByResultForRandomSampling.get(true)).map(List::size).orElse(0)
        );
        defaultPieDatasetForRandomSampling.setValue(
                "Not Pass",
                Optional.ofNullable(outputsPartitionedByResultForRandomSampling.get(false)).map(List::size).orElse(0)
        );

        PieChart randomSamplingPieChart = new PieChart(
                "Comparison of samplings - RandomSampling",
                "Results of random sampling",
                defaultPieDatasetForRandomSampling
        );
        randomSamplingPieChart.pack();
        RefineryUtilities.positionFrameRandomly(randomSamplingPieChart);
        randomSamplingPieChart.setVisible(true);


        // Note: pie chart for time random sampling.
        List<SamplingOutput> outputsForTimeRandomSampling = samplingStrategiesOutputs.get("time-random");
        Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForTimeRandomSampling = outputsForTimeRandomSampling
                .stream()
                .collect(Collectors.groupingBy(SamplingOutput::isSamplingOutcome));

        DefaultPieDataset defaultPieDatasetForTimeRandomSampling = new DefaultPieDataset();
        defaultPieDatasetForTimeRandomSampling.setValue(
                "Pass",
                Optional.ofNullable(outputsPartitionedByResultForTimeRandomSampling.get(true)).map(List::size).orElse(0)
        );
        defaultPieDatasetForTimeRandomSampling.setValue(
                "Not Pass",
                Optional.ofNullable(outputsPartitionedByResultForTimeRandomSampling.get(false)).map(List::size).orElse(0)
        );

        PieChart timeRandomSamplingPieChart = new PieChart(
                "Comparison of samplings - TimeRandomSampling",
                "Results of time-random sampling",
                defaultPieDatasetForTimeRandomSampling
        );
        timeRandomSamplingPieChart.pack();
        RefineryUtilities.positionFrameRandomly(timeRandomSamplingPieChart);
        timeRandomSamplingPieChart.setVisible(true);


        // Note: create line chart for random sampling
        final XYSeries randomSampling = new XYSeries("Random Sampling");
        for (int i = 0; i < outputsForRandomSampling.size(); i++) {
            randomSampling.add(i + 1, Double.parseDouble(outputsForRandomSampling.get(i).getRandomFactor()));
        }

        final XYSeriesCollection datasetForRandom = new XYSeriesCollection();
        datasetForRandom.addSeries(randomSampling);

        XYLineChart xyLineChartForRandom = new XYLineChart("Comparison of samplings - RandomSampling",
                "Results of random sampling",
                datasetForRandom);
        xyLineChartForRandom.pack();
        RefineryUtilities.positionFrameRandomly(xyLineChartForRandom);
        xyLineChartForRandom.setVisible(true);


        // Note: create line chart for time random sampling
        final XYSeries timeRandomSampling = new XYSeries("Time Random Sampling");
        for (int i = 0; i < outputsForTimeRandomSampling.size(); i++) {
            timeRandomSampling.add(i + 1, Double.parseDouble(outputsForTimeRandomSampling.get(i).getRandomFactor()));
        }

        final XYSeriesCollection datasetForTimeRandom = new XYSeriesCollection();
        datasetForTimeRandom.addSeries(timeRandomSampling);

        XYLineChart xyLineChartForTimeRandom = new XYLineChart("Comparison of samplings - TimeRandomSampling",
                "Results of time random sampling",
                datasetForTimeRandom);
        xyLineChartForTimeRandom.pack();
        RefineryUtilities.positionFrameRandomly(xyLineChartForTimeRandom);
        xyLineChartForTimeRandom.setVisible(true);

    }

}
