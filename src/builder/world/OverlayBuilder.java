package builder.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts a list of information that changes the default beginning game state regarding starting
 * resources, placement of the player, cabbages, spawner locations and times etc
 */
public class OverlayBuilder {

    /**
     * load file.
     *
     * @param filepath - location of the text file we wish to load
     * @return String representation of the contents of file found at the filepath.
     */
    private static String load(String filepath) throws IOException {
        if (!filepath.endsWith(".details")) {
            throw new IllegalArgumentException("incorrect file name must have .details at end!");
        }
        return Files.readString(Path.of(filepath));
    }

    /**
     * Search the given string for a line equivalent to the given label surrounded by a pair of ':'
     * then collect all lines of text between that label and the next line that reads as 'end;'
     *
     * @param label label we are searching for
     * @param contents file contents we are searching through
     * @throws IOException if the section is not found
     */
    public static List<String> getSection(String label, String contents) throws IOException {
        final String[] lines = contents.split("\n");
        boolean collectingLines = false;
        final List<String> section = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            if (collectingLines && lines[i].toLowerCase().trim().equals("end;")) {
                return section;
            }
            if (collectingLines) {
                section.add(lines[i].toLowerCase().trim());
            }
            if (lines[i].toLowerCase().trim().equals(":" + label.toLowerCase().trim() + ":")) {
                collectingLines = true;
            }
        }
        throw new IOException("Section not Found!");
    }

    /**
     * Search the given string for a line equivalent to the given label surrounded by a pair of ':'
     * then collect all lines of text between that label and the next line that reads as 'end;'
     *
     * @param line line to process.
     * @return a new {@link SpawnerDetails} holding the information extracted from the line.
     */
    public static SpawnerDetails extractSpawnDetailsFromLine(String line) {
        String[] chunks = line.split(" ");
        assert chunks.length == 3; // should always be 3 chunks in a correctly shaped line.
        String[] chunkX = chunks[0].split(":");
        String[] chunkY = chunks[1].split(":");
        String[] durationChunk = chunks[2].split(":");
        final int x = Integer.parseInt(chunkX[1]);
        final int y = Integer.parseInt(chunkY[1]);
        final int duration = Integer.parseInt(durationChunk[1]);
        return new SpawnerDetails() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }

            @Override
            public void setX(int x) {}

            @Override
            public void setY(int y) {}

            @Override
            public int getDuration() {
                return duration;
            }

            @Override
            public String toString() {
                return "OverlayBuilder["
                        + "x:"
                        + x
                        + ","
                        + "y:"
                        + y
                        + ",duration:"
                        + duration
                        + "]";
            }
        };
    }

    /**
     * Gets enemy spawn details from string.
     *
     * @param label          the label
     * @param detailsContent the details content
     * @return the enemy spawn details from string
     * @throws IOException the io exception
     */
    public static List<SpawnerDetails> getEnemySpawnDetailsFromString(
            String label, String detailsContent) throws IOException {
        List<String> section = OverlayBuilder.getSection(label, detailsContent);
        final List<SpawnerDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractSpawnDetailsFromLine(entry));
        }
        return list;
    }

    /**
     * Extract player details from line player details.
     *
     * @param line the line
     * @return the player details
     */
    public static PlayerDetails extractPlayerDetailsFromLine(String line) {
        String[] chunks = line.split(" ");
        assert chunks.length == 4; // should always be 3 chunks in a correctly shaped line.
        String[] chunkX = chunks[0].split(":");
        String[] chunkY = chunks[1].split(":");
        String[] coinChunk = chunks[2].split(":");
        String[] foodChunk = chunks[3].split(":");
        final int x = Integer.parseInt(chunkX[1]);
        final int y = Integer.parseInt(chunkY[1]);
        final int coins = Integer.parseInt(coinChunk[1]);
        final int food = Integer.parseInt(foodChunk[1]);
        return new PlayerDetails() {
            @Override
            public int getStartingFood() {
                return food;
            }

            @Override
            public int getStartingCoins() {
                return coins;
            }

            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }

            @Override
            public String toString() {
                return "OverlayBuilder["
                        + "x:"
                        + x
                        + ","
                        + "y:"
                        + y
                        + ",coins:"
                        + coins
                        + ",food:"
                        + food
                        + "]";
            }
        };
    }

    /**
     * Gets player details from file.
     *
     * @param detailsContent the details content
     * @return the player details from file
     * @throws IOException the io exception
     */
    public static PlayerDetails getPlayerDetailsFromFile(String detailsContent) throws IOException {
        List<String> section = OverlayBuilder.getSection("chickenFarmer", detailsContent);
        assert section.size()
                == 1; // right now we only expect there to ever be one chicken farmer entry
        String entry = section.getFirst();
        return OverlayBuilder.extractPlayerDetailsFromLine(entry);
    }

    /**
     * Gets cabbage spawn details from string.
     *
     * @param detailsContent the details content
     * @return the cabbage spawn details from string
     * @throws IOException the io exception
     */
    public static List<CabbageDetails> getCabbageSpawnDetailsFromString(String detailsContent)
            throws IOException {
        final List<String> section = OverlayBuilder.getSection("cabbages", detailsContent);
        final List<CabbageDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractCabbageDetailsFromLine(entry));
        }
        return list;
    }

    private static CabbageDetails extractCabbageDetailsFromLine(String line) {
        final String[] chunks = line.split(" ");
        String[] chunkX = chunks[0].split(":");
        String[] chunkY = chunks[1].split(":");
        final int x = Integer.parseInt(chunkX[1]);
        final int y = Integer.parseInt(chunkY[1]);
        return new CabbageDetails() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }

            @Override
            public String toString() {
                return "OverlayBuilder[" + "x:" + x + "," + "y:" + y + "]";
            }
        };
    }
}
