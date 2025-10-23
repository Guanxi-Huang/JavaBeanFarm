package builder.world;

import builder.entities.tiles.Tile;
import engine.renderer.Dimensions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for BeanWorld.
 * Tests tile placement, positioning, and selection operations.
 */
public class BeanWorldTest {

    private BeanWorld world;
    private Dimensions mockDimensions;

    @Before
    public void setUp() {
        world = new BeanWorld();
        mockDimensions = new Dimensions() {
            @Override
            public int pixelToTile(int pixel) {
                return pixel / 32; // Assuming 32 pixels per tile
            }

            @Override
            public int tileSize() {
                return 32;
            }

            @Override
            public int windowSize() {
                return 4800;
            }
        };
    }

    @Test
    public void testEmptyWorldInitialization() {
        List<Tile> allTiles = world.allTiles();
        assertNotNull(allTiles);
        assertEquals(0, allTiles.size());
    }

    @Test
    public void testPlaceSingleTile() {
        MockTile tile = new MockTile(0, 0);
        world.place(tile);

        List<Tile> allTiles = world.allTiles();
        assertEquals(1, allTiles.size());
        assertTrue(allTiles.contains(tile));
    }

    @Test
    public void testPlaceMultipleTiles() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);
        MockTile tile3 = new MockTile(64, 64);

        world.place(tile1);
        world.place(tile2);
        world.place(tile3);

        List<Tile> allTiles = world.allTiles();
        assertEquals(3, allTiles.size());
    }

    @Test
    public void testTilesAtPositionExact() {
        MockTile tile = new MockTile(0, 0);
        world.place(tile);

        List<Tile> tiles = world.tilesAtPosition(0, 0, mockDimensions);
        assertEquals(1, tiles.size());
        assertTrue(tiles.contains(tile));
    }

    @Test
    public void testTilesAtPositionNoMatch() {
        MockTile tile = new MockTile(0, 0);
        world.place(tile);

        List<Tile> tiles = world.tilesAtPosition(100, 100, mockDimensions);
        assertEquals(0, tiles.size());
    }

    @Test
    public void testTilesAtPositionWithinSameTile() {
        MockTile tile = new MockTile(0, 0);
        world.place(tile);

        // Any pixel within the same tile should match
        List<Tile> tiles1 = world.tilesAtPosition(10, 10, mockDimensions);
        List<Tile> tiles2 = world.tilesAtPosition(31, 31, mockDimensions);

        assertEquals(1, tiles1.size());
        assertEquals(1, tiles2.size());
    }

    @Test
    public void testTileSelector() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);

        world.place(tile1);
        world.place(tile2);

        List<Tile> selected = world.tileSelector(tile -> tile.getX() == 0);
        assertEquals(1, selected.size());
        assertTrue(selected.contains(tile1));
    }

    @Test
    public void testTileSelectorMultipleMatches() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 0);
        MockTile tile3 = new MockTile(64, 0);

        world.place(tile1);
        world.place(tile2);
        world.place(tile3);

        List<Tile> selected = world.tileSelector(tile -> tile.getY() == 0);
        assertEquals(3, selected.size());
    }

    @Test
    public void testAllTilesReturnsIndependentCopy() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);

        world.place(tile1);
        world.place(tile2);

        List<Tile> tiles1 = world.allTiles();
        List<Tile> tiles2 = world.allTiles();

        // Both should have the same content
        assertEquals(tiles1.size(), tiles2.size());

        // But modifying one should not affect the other
        tiles1.clear();
        assertEquals(2, world.allTiles().size());
    }

    @Test
    public void testTilesAtPositionReturnsIndependentList() {
        MockTile tile = new MockTile(0, 0);
        world.place(tile);

        List<Tile> tiles1 = world.tilesAtPosition(0, 0, mockDimensions);
        List<Tile> tiles2 = world.tilesAtPosition(0, 0, mockDimensions);

        // Modifying one list should not affect the other
        tiles1.clear();
        assertEquals(1, tiles2.size());
    }

    @Test
    public void testTileSelectorComplexCondition() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(32, 32);
        MockTile tile3 = new MockTile(64, 0);

        world.place(tile1);
        world.place(tile2);
        world.place(tile3);

        List<Tile> selected = world.tileSelector(
                tile -> tile.getX() >= 32 && tile.getY() <= 32
        );

        assertEquals(2, selected.size());
    }

    @Test
    public void testPlacePreservesPreviousTiles() {
        MockTile tile1 = new MockTile(0, 0);
        world.place(tile1);

        MockTile tile2 = new MockTile(32, 32);
        world.place(tile2);

        List<Tile> allTiles = world.allTiles();
        assertEquals(2, allTiles.size());
        assertTrue(allTiles.contains(tile1));
        assertTrue(allTiles.contains(tile2));
    }

    @Test
    public void testMultipleTilesAtSamePosition() {
        MockTile tile1 = new MockTile(0, 0);
        MockTile tile2 = new MockTile(0, 0);

        world.place(tile1);
        world.place(tile2);

        List<Tile> tiles = world.tilesAtPosition(0, 0, mockDimensions);
        assertEquals(2, tiles.size());
    }

    @Test
    public void testTileSelectorOnEmptyWorld() {
        List<Tile> selected = world.tileSelector(tile -> true);
        assertEquals(0, selected.size());
    }

    @Test
    public void testTilesAtPositionOnEmptyWorld() {
        List<Tile> tiles = world.tilesAtPosition(0, 0, mockDimensions);
        assertEquals(0, tiles.size());
    }

    @Test
    public void testLargNumberOfTiles() {
        for (int i = 0; i < 100; i++) {
            world.place(new MockTile(i * 32, i * 32));
        }

        List<Tile> allTiles = world.allTiles();
        assertEquals(100, allTiles.size());
    }

    /**
     * Mock Tile implementation for testing
     */
    private static class MockTile extends Tile {
        private int x;
        private int y;

        MockTile(int x, int y) {
            super(x,y);
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