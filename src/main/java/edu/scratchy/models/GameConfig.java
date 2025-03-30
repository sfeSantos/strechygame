package edu.scratchy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GameConfig(
        int columns,
        int rows,
        Map<String, SymbolConfig> symbols,
        ProbabilityConfig probabilities,
        Map<String, WinningCombinationConfig> winCombinations
) {}
