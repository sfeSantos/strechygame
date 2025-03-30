package edu.scratchy.game;

import edu.scratchy.models.GameConfig;
import edu.scratchy.models.GameResult;

public class ScratchGame {

    private final GameConfig config;
    private final SymbolGenerator symbolGenerator;
    private final WinningCombinationEvaluator winningEvaluator;
    private final RewardCalculator rewardCalculator;

    public ScratchGame(GameConfig config) {
        this.config = config;
        this.symbolGenerator = new SymbolGenerator(config);
        this.winningEvaluator = new WinningCombinationEvaluator(config);
        this.rewardCalculator = new RewardCalculator(config);
    }

    public GameResult play(double betAmount) {
        // Generate matrix
        var matrix = symbolGenerator.generateMatrix();

        // Evaluate winning combinations
        var appliedCombinations =
                winningEvaluator.evaluateWinningCombinations(matrix);

        // Find bonus symbol (first bonus symbol in matrix)
        var bonusSymbol = findBonusSymbol(matrix);

        // Calculate reward
        var reward = rewardCalculator.calculateReward(
                betAmount,
                appliedCombinations,
                bonusSymbol
        );

        // Create game result
        return new GameResult(matrix, reward, appliedCombinations, bonusSymbol);
    }

    private String findBonusSymbol(String[][] matrix) {
        return java.util.Arrays.stream(matrix)
                .flatMap(java.util.Arrays::stream)
                .filter(symbol ->
                        config.symbols().containsKey(symbol) &&
                                config.symbols().get(symbol).type().equals("bonus")
                )
                .findFirst()
                .orElse(null);
    }
}
