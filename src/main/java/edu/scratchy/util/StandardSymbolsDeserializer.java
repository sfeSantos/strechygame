package edu.scratchy.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StandardSymbolsDeserializer extends JsonDeserializer<Map<String, Map<String, Double>>> {


    @Override
    public Map<String, Map<String, Double>> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Handle both array and object cases
        Map<String, Map<String, Double>> standardSymbols = new HashMap<>();

        if (node.isArray()) {
            for (JsonNode symbolConfig : node) {
                int column = symbolConfig.get("column").asInt();
                int row = symbolConfig.get("row").asInt();

                JsonNode symbolsNode = symbolConfig.get("symbols");
                Map<String, Double> symbolProbabilities = new HashMap<>();

                Iterator<Map.Entry<String, JsonNode>> fields = symbolsNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    symbolProbabilities.put(entry.getKey(), entry.getValue().asDouble());
                }

                standardSymbols.put(column + ":" + row, symbolProbabilities);
            }
        } else if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();

                Map<String, Double> symbolProbabilities = new HashMap<>();
                Iterator<Map.Entry<String, JsonNode>> symbolFields = entry.getValue().fields();
                while (symbolFields.hasNext()) {
                    Map.Entry<String, JsonNode> symbolEntry = symbolFields.next();
                    symbolProbabilities.put(symbolEntry.getKey(), symbolEntry.getValue().asDouble());
                }

                standardSymbols.put(entry.getKey(), symbolProbabilities);
            }
        }

        return !standardSymbols.isEmpty() ? standardSymbols : Map.of("0:0", Map.of());
    }
}
