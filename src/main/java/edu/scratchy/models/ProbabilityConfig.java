package edu.scratchy.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.scratchy.util.StandardSymbolsDeserializer;

import java.util.Map;

public record ProbabilityConfig(
        @JsonProperty("standard_symbols")
        @JsonDeserialize(using = StandardSymbolsDeserializer.class)
        Map<String, Map<String, Double>> standardSymbols,
        @JsonProperty("bonus_symbols")
        BonusSymbolsWrapper bonusSymbols
) {}