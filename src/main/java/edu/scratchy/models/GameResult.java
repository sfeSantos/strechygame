package edu.scratchy.models;

import java.util.List;
import java.util.Map;

public record GameResult(
        String[][] matrix,
        double reward,
        Map<String, List<String>> appliedWinningCombinations,
        String appliedBonusSymbol
) {

    public GameResult {
        // TODO add validations
    }
}
