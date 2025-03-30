package edu.scratchy.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SymbolConfig(
        String type,
        @JsonProperty("reward_multiplier")
        double rewardMultiplier,
        String impact,
        Double extra
) {}
