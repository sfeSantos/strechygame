package edu.scratchy.game;

import edu.scratchy.models.GameConfig;

import java.util.*;
import java.util.stream.Collectors;

public class WinningCombinationEvaluator {

    private final GameConfig config;

    public WinningCombinationEvaluator(GameConfig config) {
        this.config = config;
    }

    public Map<String, List<String>> evaluateWinningCombinations(String[][] matrix) {
        Map<String, List<String>> appliedCombinations = new HashMap<>();

        // Evaluate same symbols combinations
        evaluateSameSymbolCombinations(matrix, appliedCombinations);
        evaluateLinearCombinations(matrix, appliedCombinations); // linear evaluation

        return appliedCombinations;
    }

    private void evaluateSameSymbolCombinations(
            String[][] matrix,
            Map<String, List<String>> appliedCombinations
    ) {
        // Flatten matrix and count symbol occurrences
        var symbolCounts = Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(symbol -> config.symbols().get(symbol).type().equals("standard"))
                .collect(Collectors.groupingBy(
                        s -> s,
                        Collectors.counting()
                ));

        // Check against winning combinations for repeated symbols
        symbolCounts.forEach((symbol, count) -> {
            config.winCombinations().entrySet().stream()
                    .filter(entry ->
                            entry.getValue().when().equals("same_symbols") &&
                                    count >= entry.getValue().count()
                    )
                    .map(Map.Entry::getKey)
                    .forEach(combination ->
                            appliedCombinations
                                    .computeIfAbsent(symbol, k -> new ArrayList<>())
                                    .add(combination)
                    );
        });
    }

    private void evaluateLinearCombinations(String[][] matrix, Map<String, List<String>> appliedCombinations) {
        config.winCombinations().entrySet().stream()
                .filter(entry -> "linear_symbols".equals(entry.getValue().when()))
                .forEach(entry -> {
                    String combinationName = entry.getKey();
                    List<List<String>> coveredAreas = entry.getValue().coveredAreas();

                    if (coveredAreas != null) {
                        for (List<String> areaList : coveredAreas) {
                            for (String area : areaList) {
                                checkLinearCombination(matrix, area, combinationName, appliedCombinations);
                            }
                        }
                    }
                });
    }

    private void checkLinearCombination(
            String[][] matrix,
            String area,
            String combinationName,
            Map<String, List<String>> appliedCombinations
    ) {
        var coordinates = area.split(",");
        var symbols = Arrays.stream(coordinates)
                .map(coord -> {
                    String[] rowCol = coord.trim().split(":");
                    int row = Integer.parseInt(rowCol[0]);
                    int col = Integer.parseInt(rowCol[1]);
                    return matrix[row][col];
                })
                .toList();

        // Check if all symbols are the same and standard
        var isValidCombination = symbols.stream()
                .allMatch(symbol ->
                        symbol.equals(symbols.getFirst()) &&
                                config.symbols().get(symbol).type().equals("standard")
                );

        if (isValidCombination) {
            symbols.forEach(symbol ->
                    appliedCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add(combinationName)
            );
        }
    }
}
