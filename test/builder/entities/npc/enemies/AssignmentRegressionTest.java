package builder.entities.npc.enemies;

import builder.GameState;
import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.spawners.EagleSpawner;
import builder.entities.tiles.Dirt;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import builder.world.OverlayBuilder;
import builder.world.WorldBuilder;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class AssignmentRegressionTest {
    @Test(expected = IOException.class)
    public void missingDetailsSectionReportsIOException() throws IOException {
        OverlayBuilder.getSection("missing", ":cabbages:\nend;");
    }

    @Test(expected = IOException.class)
    public void unterminatedDetailsSectionReportsIOException() throws IOException {
        OverlayBuilder.getSection("cabbages", ":cabbages:\nx:100 y:100");
    }

    @Test
    public void eagleSpawnerAddsOneBirdAtItsOwnPosition() {
        Dimensions dimensions = new TileGrid(10, 800);
        EnemyManager enemies = new EnemyManager(dimensions);
        GameState game = new JavaBeanGameState(WorldBuilder.empty(),
                new ChickenFarmer(400, 400), new TinyInventory(5, 10, 10),
                new NpcManager(), enemies);
        new EagleSpawner(500, 100, 1).tick(new MockEngineState(dimensions), game);
        assertEquals(1, enemies.getBirds().size());
        assertEquals(500, enemies.getBirds().getFirst().getX());
        assertEquals(100, enemies.getBirds().getFirst().getY());
    }

    @Test
    public void pigeonTargetsNearestCabbageRegardlessOfTileOrder() {
        Dimensions dimensions = new TileGrid(25, 800);
        BeanWorld world = WorldBuilder.empty();
        TinyInventory inventory = new TinyInventory(5, 100, 10);
        Dirt far = new Dirt(300, 0);
        Dirt nearest = new Dirt(100, 0);
        Dirt middle = new Dirt(200, 0);
        for (Dirt tile : new Dirt[] {far, nearest, middle}) {
            tile.till();
            tile.plant(inventory);
            world.place(tile);
        }
        GameState game = new JavaBeanGameState(world, new ChickenFarmer(400, 400),
                inventory, new NpcManager(), new EnemyManager(dimensions));
        Pigeon pigeon = new Pigeon(0, 0, far);
        pigeon.updateAttack(new MockEngineState(dimensions), game);
        assertSame(nearest, pigeon.getTrackedTarget());
    }
}
