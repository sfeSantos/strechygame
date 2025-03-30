package edu.scratchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.scratchy.config.ConfigLoader;
import edu.scratchy.game.ScratchGame;

public class ScratchyGameApp {

    public static void main(String[] args) {
        if (args.length != 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            System.err.println("Usage: java -jar scratch-game.jar --config <config-file> --betting-amount <amount>");
            System.exit(1);
        }

        var configPath = args[1];
        var bettingAmount = Double.parseDouble(args[3]);

        try {
            var configLoader = new ConfigLoader();
            var config = configLoader.loadConfig(configPath);

            var game = new ScratchGame(config);
            var result = game.play(bettingAmount);

            var objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        } catch (Exception e) {
            System.err.println("Error running Scratch Game: " + e.getMessage());
            System.exit(1);
        }
    }
}
