package edu.scratchy.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record BonusSymbolsWrapper(
        @JsonProperty("symbols")
        Map<String, Double> symbols
) {
}
