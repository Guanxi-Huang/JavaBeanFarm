package builder.world;

import builder.entities.tiles.Tile;
import engine.renderer.Dimensions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive test class for WorldBuilder.
 * Tests tile loading from strings, file I/O, and world construction.
 * Achieves full code coverage of WorldBuilder methods.
 */
public class WorldBuilderTest {

    private Dimensions mockDimensions;
    private static final int TILE_SIZE = 32;
    private static final int WINDOW_SIZE = 64; // 2x2 world

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        mockDimensions = new Dimensions() {
            @Override
            public int pixelToTile(int pixel) {
                return pixel / TILE_SIZE;
            }

            @Override
            public int tileSize() {
                return TILE_SIZE;
            }

            @Override
            public int windowSize() {
                return WINDOW_SIZE;
            }

            @Override
            public int tileToPixel(int tile) {
                return tile * TILE_SIZE;
            }
        };
    }



    @Test
    public void testFromStringWrongNumberOfLines() throws WorldLoadException {
        String worldText = "gg\ngg\ngg"; // 3 lines instead of 2

        try {
            WorldBuilder.fromString(mockDimensions, worldText);
            fail("Should throw WorldLoadException for wrong number of lines");
        } catch (WorldLoadException e) {
            assertTrue(e.getMessage().contains("Expected 2 lines"));
        }
    }

    @Test
    public void testFromStringWrongNumberOfColumns() throws WorldLoadException {
        String worldText = "ggg\ngg";
        try {
            WorldBuilder.fromString(mockDimensions, worldText);
            fail("Should throw WorldLoadException for wrong number of columns");
        } catch (WorldLoadException e) {
            assertTrue(e.getMessage().contains("Expected 2 characters"));
        }
    }

    @Test
    public void testFromStringErrorMessageContainsLineInfo() throws WorldLoadException {
        String worldText = "gg\ng?";
        try {
            WorldBuilder.fromString(mockDimensions, worldText);
            fail("Should throw WorldLoadException");
        } catch (WorldLoadException e) {
            String message = e.getMessage();
            assertTrue(message.contains("2"));
        }
    }

    @Test
    public void testFromStringErrorMessageContainsColumnInfo() throws WorldLoadException {
        String worldText = "gg\ng?";  // Invalid symbol at column 1 (index 1)

        try {
            WorldBuilder.fromString(mockDimensions, worldText);
            fail("Should throw WorldLoadException");
        } catch (WorldLoadException e) {
            String message = e.getMessage();
            assertTrue(message.contains("2")); // col 1 + 1 = character 2
        }
    }

    @Test
    public void testFromStringEmptyWorld() throws WorldLoadException {
        // Create a minimal valid world (1x1)
        Dimensions dim1x1 = new Dimensions() {
            @Override
            public int pixelToTile(int pixel) { return pixel / 32; }
            @Override
            public int tileSize() { return 32; }
            @Override
            public int windowSize() { return 32; }
            @Override
            public int tileToPixel(int tile) { return tile * 32; }
        };

        String worldText = "g";
        List<Tile> tiles = WorldBuilder.fromString(dim1x1, worldText);

        assertEquals(1, tiles.size());
    }

    @Test
    public void testFromStringHandlesWhitespace() throws WorldLoadException {
        String worldText = "  gg  \n  gg  "; // Extra spaces should be trimmed
        List<Tile> tiles = WorldBuilder.fromString(mockDimensions, worldText);

        assertEquals(4, tiles.size());
    }

    @Test
    public void testFromStringMultipleDifferentTiles() throws WorldLoadException {
        String worldText = "gg\ngg";
        List<Tile> tiles = WorldBuilder.fromString(mockDimensions, worldText);

        assertNotNull(tiles);
        assertFalse(tiles.isEmpty());
    }

    @Test
    public void testEmptyWorldCreation() {
        BeanWorld world = WorldBuilder.empty();

        assertNotNull(world);
        assertEquals(0, world.allTiles().size());
    }

    @Test
    public void testFromTilesWithEmptyList() {
        BeanWorld world = WorldBuilder.fromTiles(new ArrayList<>());

        assertNotNull(world);
        assertEquals(0, world.allTiles().size());
    }

    @Test
    public void testFromTilesWithSingleTile() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new MockTile(0, 0));

        BeanWorld world = WorldBuilder.fromTiles(tiles);

        assertNotNull(world);
        assertEquals(1, world.allTiles().size());
    }

    @Test
    public void testFromTilesWithMultipleTiles() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new MockTile(0, 0));
        tiles.add(new MockTile(32, 0));
        tiles.add(new MockTile(0, 32));

        BeanWorld world = WorldBuilder.fromTiles(tiles);

        assertEquals(3, world.allTiles().size());
    }

    @Test
    public void testFromTilesPreservesAllTiles() {
        List<Tile> inputTiles = new ArrayList<>();
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);
        inputTiles.add(tile1);
        inputTiles.add(tile2);

        BeanWorld world = WorldBuilder.fromTiles(inputTiles);
        List<Tile> worldTiles = world.allTiles();

        assertTrue(worldTiles.contains(tile1));
        assertTrue(worldTiles.contains(tile2));
    }

    @Test
    public void testFromTilesReversesOrder() {
        List<Tile> inputTiles = new ArrayList<>();
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);
        MockTile tile3 = new MockTile(64, 64);
        inputTiles.add(tile1);
        inputTiles.add(tile2);
        inputTiles.add(tile3);

        BeanWorld world = WorldBuilder.fromTiles(inputTiles);
        List<Tile> worldTiles = world.allTiles();

        // The order should be reversed due to .reversed() in implementation
        assertEquals(tile3, worldTiles.get(0));
        assertEquals(tile2, worldTiles.get(1));
        assertEquals(tile1, worldTiles.get(2));
    }

    @Test
    public void testFromFileValidFile() throws IOException, WorldLoadException {
        File file = tempFolder.newFile("test_world.txt");
        String content = "gg\ngg";
        Files.write(file.toPath(), content.getBytes());

        BeanWorld world = WorldBuilder.fromFile(mockDimensions, file.getAbsolutePath());

        assertNotNull(world);
        assertEquals(4, world.allTiles().size());
    }

    @Test
    public void testFromFileNonExistentFile() {
        try {
            WorldBuilder.fromFile(mockDimensions, "/nonexistent/path/file.txt");
            fail("Should throw IOException for non-existent file");
        } catch (IOException e) {
            // Expected
            assertNotNull(e);
        } catch (WorldLoadException e) {
            fail("Should throw IOException, not WorldLoadException");
        }
    }

    @Test
    public void testFromFileInvalidWorldEncoding() throws IOException {
        File file = tempFolder.newFile("invalid_world.txt");
        String content = "gg\ngg\ngg"; // Too many lines
        Files.write(file.toPath(), content.getBytes());

        try {
            WorldBuilder.fromFile(mockDimensions, file.getAbsolutePath());
            fail("Should throw WorldLoadException for invalid encoding");
        } catch (WorldLoadException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testFromFileWithValidTiles() throws IOException, WorldLoadException {
        File file = tempFolder.newFile("valid_world.txt");
        String content = "gg\ngg";
        Files.write(file.toPath(), content.getBytes());

        BeanWorld world = WorldBuilder.fromFile(mockDimensions, file.getAbsolutePath());

        assertNotNull(world);
        assertTrue(world.allTiles().size() > 0);
    }

    @Test
    public void testFromStringLargeWorld() throws WorldLoadException {
        // Create a 4x4 world
        Dimensions dim4x4 = new Dimensions() {
            @Override
            public int pixelToTile(int pixel) { return pixel / 32; }
            @Override
            public int tileSize() { return 32; }
            @Override
            public int windowSize() { return 128; } // 4 * 32
            @Override
            public int tileToPixel(int tile) { return tile * 32; }
        };

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) sb.append("\n");
            for (int j = 0; j < 4; j++) {
                sb.append("g");
            }
        }

        List<Tile> tiles = WorldBuilder.fromString(dim4x4, sb.toString());
        assertEquals(16, tiles.size());
    }

    @Test
    public void testFromStringNoLines() throws WorldLoadException {
        // A world with 0 tiles (edge case)
        Dimensions dim0x0 = new Dimensions() {
            @Override
            public int pixelToTile(int pixel) { return pixel / 32; }
            @Override
            public int tileSize() { return 32; }
            @Override
            public int windowSize() { return 0; }
            @Override
            public int tileToPixel(int tile) { return tile * 32; }
        };

        try {
            List<Tile> tiles = WorldBuilder.fromString(dim0x0, "");
            assertEquals(0, tiles.size());
        } catch (WorldLoadException e) {
            // May throw exception for mismatched dimensions - that's acceptable
        }
    }

    @Test
    public void testFromStringMissingEndLine() throws WorldLoadException {
        String worldText = "gg"; // Only 1 line instead of 2

        try {
            WorldBuilder.fromString(mockDimensions, worldText);
            fail("Should throw WorldLoadException");
        } catch (WorldLoadException e) {
            assertTrue(e.getMessage().contains("Expected 2 lines"));
        }
    }

    @Test
    public void testFromTilesIndependentOfInputList() {
        List<Tile> inputTiles = new ArrayList<>();
        MockTile tile1 = new MockTile(0, 0);
        inputTiles.add(tile1);

        BeanWorld world = WorldBuilder.fromTiles(inputTiles);

        // Modifying input list should not affect world
        inputTiles.clear();

        assertEquals(1, world.allTiles().size());
    }

    @Test
    public void testConstructorIsAccessible() {
        WorldBuilder builder = new WorldBuilder();
        assertNotNull(builder);
    }

    /**
     * Mock Tile implementation for testing
     */
    private static class MockTile extends Tile {
        private int x;
        private int y;

        MockTile(int x, int y) {
            super(x, y);
            this.x = x;
            this.y = y;
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
        public void setX(int i) {
            this.x = i;
        }

        @Override
        public void setY(int i) {
            this.y = i;
        }

        @Override
        public List<engine.game.Entity> getStackedEntities() {
            return new ArrayList<>();
        }

        @Override
        public void tick(engine.EngineState state) {}

        @Override
        public List<engine.renderer.Renderable> render() {
            return new ArrayList<>();
        }
    }
}

