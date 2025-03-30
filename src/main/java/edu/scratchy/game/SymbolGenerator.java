package edu.scratchy.game;

import edu.scratchy.models.GameConfig;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class SymbolGenerator {

    private final SecureRandom random = new SecureRandom();
    private final GameConfig config;

    public SymbolGenerator(GameConfig config) {
        this.config = config;
    }

    public String[][] generateMatrix() {
        var rows = config.rows();
        var columns = config.columns();

        return IntStream.range(0, rows)
                .mapToObj(row ->
                        IntStream.range(0, columns)
                                .mapToObj(col -> generateSymbolForCell(row, col))
                                .toArray(String[]::new)
                )
                .toArray(String[][]::new);
    }

    private String generateSymbolForCell(int row, int col) {
        var key = row + ":" + col;
        var standardSymbolProbs = config.probabilities().standardSymbols().get(key);

        if (Objects.isNull(standardSymbolProbs )) {
            var availableValues = new ArrayList<>(config.probabilities().standardSymbols().values());

            if (!availableValues.isEmpty()) {
                standardSymbolProbs = availableValues.get(random.nextInt(availableValues.size()));
            }
        }

        var standardSymbol = selectSymbolByProbability(standardSymbolProbs);

        // Random bonus symbol generation
        if (randomBonusSymbolChance()) {
            return selectBonusSymbol();
        }

        return standardSymbol;
    }

    private String selectSymbolByProbability(Map<String, Double> symbolProbabilities) {
        if (symbolProbabilities == null || symbolProbabilities.isEmpty()) {
            throw new IllegalArgumentException("Symbol probabilities must not be null or empty.");
        }

        var totalProbability = symbolProbabilities.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        var randomValue = random.nextDouble() * totalProbability;
        var cumulativeProbability = 0.0;

        for (Map.Entry<String, Double> entry : symbolProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        // Fallback to first symbol if something goes wrong
        return new ArrayList<>(symbolProbabilities.keySet()).getFirst();
    }

    public boolean randomBonusSymbolChance() {
        // 20% chance of bonus symbol
        return random.nextDouble() < 0.2;
    }

    private String selectBonusSymbol() {
        var bonusSymbols = config.probabilities().bonusSymbols();
        return selectSymbolByProbability(bonusSymbols.symbols());
    }
}
