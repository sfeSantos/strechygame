package edu.scratchy.game;

import edu.scratchy.models.GameConfig;

import java.util.List;
import java.util.Map;

public class RewardCalculator {

    private final GameConfig config;

    public RewardCalculator(GameConfig config) {
        this.config = config;
    }

    public double calculateReward(
            double betAmount,
            Map<String, List<String>> appliedCombinations,
            String bonusSymbol
    ) {
        var finalReward = 0.0;

        // Calculate reward for each winning symbol
        for (Map.Entry<String, List<String>> entry : appliedCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            var symbolReward = calculateSymbolReward(betAmount, symbol, combinations);
            finalReward += symbolReward;
        }

        // Apply bonus symbol modifications
        if (bonusSymbol != null && !bonusSymbol.equals("MISS")) {
            finalReward = applyBonusSymbol(finalReward, bonusSymbol);
        }

        return finalReward;
    }

    private double calculateSymbolReward(
            double betAmount,
            String symbol,
            List<String> combinations
    ) {
        var symbolReward = betAmount;

        // Get base symbol multiplier
        var symbolConfig = config.symbols().get(symbol);
        symbolReward *= symbolConfig.rewardMultiplier();

        // Apply winning combination multipliers
        for (String combination : combinations) {
            var winCombination = config.winCombinations().get(combination);
            symbolReward *= winCombination.rewardMultiplier();
        }

        return symbolReward;
    }

    private double applyBonusSymbol(double currentReward, String bonusSymbol) {
        var bonusSymbolConfig = config.symbols().get(bonusSymbol);

        return switch (bonusSymbolConfig.impact()) {
            case "multiply_reward" -> currentReward * bonusSymbolConfig.rewardMultiplier();
            case "extra_bonus" -> currentReward + bonusSymbolConfig.extra();
            default -> currentReward;
        };
    }
}
