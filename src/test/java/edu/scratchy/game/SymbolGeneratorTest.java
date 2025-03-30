package edu.scratchy.game;

import edu.scratchy.models.BonusSymbolsWrapper;
import edu.scratchy.models.GameConfig;
import edu.scratchy.models.ProbabilityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SymbolGeneratorTest {

    @Mock
    private GameConfig mockConfig;

    @Mock
    private SecureRandom mockRandom;

    private SymbolGenerator symbolGenerator;
    private final int ROWS = 3;
    private final int COLUMNS = 3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Standard symbols
        var standardSymbols = Map.of(
                "0:0", Map.of("A", 0.6, "B", 0.4),
                "1:1", Map.of("X", 0.7, "Y", 0.3));

        var bonusSymbols = new BonusSymbolsWrapper(Map.of("BONUS", 1.0));
        var probabilityConfig = new ProbabilityConfig(standardSymbols, bonusSymbols);

        // Mock game config
        when(mockConfig.rows()).thenReturn(ROWS);
        when(mockConfig.columns()).thenReturn(COLUMNS);
        when(mockConfig.probabilities()).thenReturn(probabilityConfig);

        symbolGenerator = spy(new SymbolGenerator(mockConfig));
    }

    @Test
    void testGenerateMatrix_CorrectSize() {
        var matrix = symbolGenerator.generateMatrix();
        assertEquals(ROWS, matrix.length);
        assertEquals(COLUMNS, matrix[0].length);
    }

    @Test
    void testGenerateSymbolForCell_FallbackWhenMissing() {
        var matrix = symbolGenerator.generateMatrix();

        // Any cell without explicit definition should fallback to random available symbols
        assertNotNull(matrix[2][2]); // No definition exists, should not be null
    }

    @Test
    void testGenerateMatrix_IncludesBonusSymbols() {
        doReturn(true).when(symbolGenerator).randomBonusSymbolChance();

        var matrix = symbolGenerator.generateMatrix();

        // At least one symbol should be "BONUS" since bonus chance is forced to true
        boolean containsBonus = Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .anyMatch(symbol -> symbol.equals("BONUS"));

        assertTrue(containsBonus);
    }

    @RepeatedTest(10)
    void testRandomBonusSymbolChance_TriggersAtExpectedRate() {
        int bonusCount = 0;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            if (symbolGenerator.randomBonusSymbolChance()) {
                bonusCount++;
            }
        }

        double probability = (double) bonusCount / iterations;
        assertTrue(probability >= 0.15 && probability <= 0.25);
    }

    @Test
    void testGenerateMatrix_NoStandardSymbolsDefined() {
        var matrix = symbolGenerator.generateMatrix();
        var probabilityConfig = new ProbabilityConfig(Collections.emptyMap(),
                new BonusSymbolsWrapper(Map.of("BONUS", 1.0)));

        when(mockConfig.probabilities()).thenReturn(probabilityConfig);

        // Ensure the matrix has valid symbols, avoiding nulls
        for (String[] row : matrix) {
            for (String cell : row) {
                assertNotNull(cell, "Symbol should not be null even if no standard symbols are defined");
            }
        }
    }
}
