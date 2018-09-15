package com.chriniko.sampling.core;

import com.chriniko.sampling.sampling.SamplingOutput;
import com.chriniko.sampling.sampling.SamplingStrategy;
import com.chriniko.sampling.visualization.LogFrame;
import com.chriniko.sampling.visualization.PieChart;
import com.chriniko.sampling.visualization.XYLineChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
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
        animationPhase(samplingStrategiesOutputs, trafficThreshold, NO_OF_SAMPLES);

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


    private static void animationPhase(Map<String, List<SamplingOutput>> samplingStrategiesOutputs,
                                       double trafficThreshold,
                                       int NO_OF_SAMPLES) {

        final ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Note: collect necessary shared data.
        final String titleAppend = String.format(" (trafficThreshold: %s, noOfSamples: %d)", String.valueOf(trafficThreshold), NO_OF_SAMPLES);

        final List<SamplingOutput> outputsForRandomSampling = samplingStrategiesOutputs.get("random");
        final Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForRandomSampling = outputsForRandomSampling
                .stream()
                .collect(Collectors.groupingBy(SamplingOutput::isSamplingOutcome));

        final List<SamplingOutput> outputsForTimeRandomSampling = samplingStrategiesOutputs.get("time-random");
        final Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForTimeRandomSampling = outputsForTimeRandomSampling
                .stream()
                .collect(Collectors.groupingBy(SamplingOutput::isSamplingOutcome));

        // Note: pie chart for random sampling.
        Runnable workToDo1 = () -> {

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
                    "Results of random sampling" + titleAppend,
                    defaultPieDatasetForRandomSampling
            );
            randomSamplingPieChart.pack();
            RefineryUtilities.positionFrameRandomly(randomSamplingPieChart);
            randomSamplingPieChart.setVisible(true);
            System.out.println("PieChart for Random Sampling Created!");
        };
        ForkJoinTask<?> result1 = forkJoinPool.submit(workToDo1);

        // Note: pie chart for time random sampling.
        Runnable workToDo2 = () -> {

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
                    "Results of time-random sampling" + titleAppend,
                    defaultPieDatasetForTimeRandomSampling
            );
            timeRandomSamplingPieChart.pack();
            RefineryUtilities.positionFrameRandomly(timeRandomSamplingPieChart);
            timeRandomSamplingPieChart.setVisible(true);
            System.out.println("PieChart for Time Random Sampling Created!");
        };
        ForkJoinTask<?> result2 = forkJoinPool.submit(workToDo2);


        // Note: create line chart for random sampling
        Runnable workToDo3 = () -> {
            final XYSeries randomSampling = new XYSeries("Random Sampling");
            for (int i = 0; i < outputsForRandomSampling.size(); i++) {
                randomSampling.add(i + 1, Double.parseDouble(outputsForRandomSampling.get(i).getRandomFactor()));
            }

            final XYSeriesCollection datasetForRandom = new XYSeriesCollection();
            datasetForRandom.addSeries(randomSampling);

            XYLineChart xyLineChartForRandom = new XYLineChart("Comparison of samplings - RandomSampling",
                    "Results of random sampling" + titleAppend,
                    datasetForRandom);
            xyLineChartForRandom.pack();
            RefineryUtilities.positionFrameRandomly(xyLineChartForRandom);
            xyLineChartForRandom.setVisible(true);
            System.out.println("XYSeries for Random Sampling Created!");
        };
        ForkJoinTask<?> result3 = forkJoinPool.submit(workToDo3);

        // Note: create line chart for time random sampling
        Runnable workToDo4 = () -> {

            final XYSeries timeRandomSampling = new XYSeries("Time Random Sampling");
            for (int i = 0; i < outputsForTimeRandomSampling.size(); i++) {
                timeRandomSampling.add(i + 1, Double.parseDouble(outputsForTimeRandomSampling.get(i).getRandomFactor()));
            }

            final XYSeriesCollection datasetForTimeRandom = new XYSeriesCollection();
            datasetForTimeRandom.addSeries(timeRandomSampling);

            XYLineChart xyLineChartForTimeRandom = new XYLineChart("Comparison of samplings - TimeRandomSampling",
                    "Results of time random sampling" + titleAppend,
                    datasetForTimeRandom);
            xyLineChartForTimeRandom.pack();
            RefineryUtilities.positionFrameRandomly(xyLineChartForTimeRandom);
            xyLineChartForTimeRandom.setVisible(true);
            System.out.println("XYSeries for Time Random Sampling Created!");
        };
        ForkJoinTask<?> result4 = forkJoinPool.submit(workToDo4);

        // Note: create results log frame.
        Runnable workToDo5 = () -> {
            LogFrame logFrame = new LogFrame(outputsPartitionedByResultForRandomSampling, outputsPartitionedByResultForTimeRandomSampling);
            logFrame.setTitle("Logs Result");
            logFrame.pack();
            RefineryUtilities.positionFrameRandomly(logFrame);
            logFrame.setVisible(true);
            System.out.println("Results Log Frame Created!");
        };
        ForkJoinTask<?> result5 = forkJoinPool.submit(workToDo5);

        result1.join();
        result2.join();
        result3.join();
        result4.join();
        result5.join();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutting down fork join pool...");
            forkJoinPool.shutdownNow();
        }));
    }

}
