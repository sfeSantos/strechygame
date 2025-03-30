package edu.scratchy.models;

import java.util.List;

public record WinningCombinationConfig(
        double rewardMultiplier,
        String when,
        int count,
        String group,
        List<List<String>> coveredAreas
) {}
