package com.chriniko.sampling.visualization;

import com.chriniko.sampling.sampling.SamplingOutput;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LogFrame extends JFrame {

    public LogFrame(
            Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForRandomSampling,
            Map<Boolean, List<SamplingOutput>> outputsPartitionedByResultForTimeRandomSampling) {

        // Note: create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        // Note: create the child node for random sampling
        DefaultMutableTreeNode randomSamplingNode = new DefaultMutableTreeNode("RandomSampling");

        List<SamplingOutput> samplingOutputsForRandom_True = Optional
                .ofNullable(outputsPartitionedByResultForRandomSampling.get(true))
                .orElse(Collections.emptyList());

        DefaultMutableTreeNode randomSamplingPassNode = new DefaultMutableTreeNode("RandomSampling-Pass (records: " + samplingOutputsForRandom_True.size() + ")");
        for (SamplingOutput samplingOutput : samplingOutputsForRandom_True) {

            DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode(samplingOutput.toString());

            outputNode.add(new DefaultMutableTreeNode(String.format("traffic threshold: %s", samplingOutput.getTrafficThresholdInput())));
            outputNode.add(new DefaultMutableTreeNode(String.format("random factor: %s", samplingOutput.getRandomFactor())));
            outputNode.add(new DefaultMutableTreeNode(String.format("inequality: %s", samplingOutput.getInequality())));
            outputNode.add(new DefaultMutableTreeNode(String.format("outcome: %s", samplingOutput.isSamplingOutcome())));

            randomSamplingPassNode.add(outputNode);
        }

        List<SamplingOutput> samplingOutputsForRandom_False = Optional
                .ofNullable(outputsPartitionedByResultForRandomSampling.get(false))
                .orElse(Collections.emptyList());

        DefaultMutableTreeNode randomSamplingNotPassNode = new DefaultMutableTreeNode("RandomSampling-NotPass (records: " + samplingOutputsForRandom_False.size() + ")");
        for (SamplingOutput samplingOutput : samplingOutputsForRandom_False) {

            DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode(samplingOutput.toString());

            outputNode.add(new DefaultMutableTreeNode(String.format("traffic threshold: %s", samplingOutput.getTrafficThresholdInput())));
            outputNode.add(new DefaultMutableTreeNode(String.format("random factor: %s", samplingOutput.getRandomFactor())));
            outputNode.add(new DefaultMutableTreeNode(String.format("inequality: %s", samplingOutput.getInequality())));
            outputNode.add(new DefaultMutableTreeNode(String.format("outcome: %s", samplingOutput.isSamplingOutcome())));

            randomSamplingNotPassNode.add(outputNode);
        }

        randomSamplingNode.add(randomSamplingPassNode);
        randomSamplingNode.add(randomSamplingNotPassNode);

        // Note: create the child node for time random sampling
        DefaultMutableTreeNode timeRandomSamplingNode = new DefaultMutableTreeNode("TimeRandomSampling");

        List<SamplingOutput> samplingOutputsForTimeRandom_True = Optional
                .ofNullable(outputsPartitionedByResultForTimeRandomSampling.get(true))
                .orElse(Collections.emptyList());

        DefaultMutableTreeNode timeRandomSamplingPassNode = new DefaultMutableTreeNode("TimeRandomSampling-Pass (records: " + samplingOutputsForTimeRandom_True.size() + ")");
        for (SamplingOutput samplingOutput : samplingOutputsForTimeRandom_True) {

            DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode(samplingOutput.toString());

            outputNode.add(new DefaultMutableTreeNode(String.format("traffic threshold: %s", samplingOutput.getTrafficThresholdInput())));
            outputNode.add(new DefaultMutableTreeNode(String.format("random factor: %s", samplingOutput.getRandomFactor())));
            outputNode.add(new DefaultMutableTreeNode(String.format("inequality: %s", samplingOutput.getInequality())));
            outputNode.add(new DefaultMutableTreeNode(String.format("outcome: %s", samplingOutput.isSamplingOutcome())));

            timeRandomSamplingPassNode.add(outputNode);
        }

        List<SamplingOutput> samplingOutputsForTimeRandom_False = Optional
                .ofNullable(outputsPartitionedByResultForTimeRandomSampling.get(false))
                .orElse(Collections.emptyList());

        DefaultMutableTreeNode timeRandomSamplingNotPassNode = new DefaultMutableTreeNode("TimeRandomSampling-NotPass (records: " + samplingOutputsForTimeRandom_False.size() + ")");
        for (SamplingOutput samplingOutput : samplingOutputsForTimeRandom_False) {

            DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode(samplingOutput.toString());

            outputNode.add(new DefaultMutableTreeNode(String.format("traffic threshold: %s", samplingOutput.getTrafficThresholdInput())));
            outputNode.add(new DefaultMutableTreeNode(String.format("random factor: %s", samplingOutput.getRandomFactor())));
            outputNode.add(new DefaultMutableTreeNode(String.format("inequality: %s", samplingOutput.getInequality())));
            outputNode.add(new DefaultMutableTreeNode(String.format("outcome: %s", samplingOutput.isSamplingOutcome())));

            timeRandomSamplingNotPassNode.add(outputNode);
        }

        timeRandomSamplingNode.add(timeRandomSamplingPassNode);
        timeRandomSamplingNode.add(timeRandomSamplingNotPassNode);


        // Note: continue with the necessary actions
        root.add(randomSamplingNode);
        root.add(timeRandomSamplingNode);

        JTree tree = new JTree(root);
        tree.setShowsRootHandles(true);

        JScrollPane jScrollPane = new JScrollPane(
                tree,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        add(jScrollPane);

    }
}
