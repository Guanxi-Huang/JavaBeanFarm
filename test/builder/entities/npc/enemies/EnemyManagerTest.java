package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import builder.entities.npc.spawners.MagpieSpawner;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the EnemyManager class
 */
public class EnemyManagerTest {

    private EnemyManager enemyManager;
    private Player player;

    @Before
    public void setUp() {
        Dimensions dimensions = new Dimensions() {
            @Override
            public int tileSize() {
                return 25;
            }

            @Override
            public int windowSize() {
                return 800;
            }
        };
        enemyManager = new EnemyManager(dimensions);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enemyManager.getSpawners());
        assertNotNull(enemyManager.getBirds());
        assertTrue(enemyManager.getSpawners().isEmpty());
        assertTrue(enemyManager.getBirds().isEmpty());
    }

    @Test
    public void testAdd() {
        Spawner spawner = new MagpieSpawner(100, 100);
        enemyManager.add(spawner);

        assertEquals(1, enemyManager.getSpawners().size());
        assertTrue(enemyManager.getSpawners().contains(spawner));
    }

    @Test
    public void testAddMultipleSpawners() {
        Spawner spawner1 = new MagpieSpawner(100, 100);
        Spawner spawner2 = new MagpieSpawner(200, 200);

        enemyManager.add(spawner1);
        enemyManager.add(spawner2);

        assertEquals(2, enemyManager.getSpawners().size());
        assertTrue(enemyManager.getSpawners().contains(spawner1));
        assertTrue(enemyManager.getSpawners().contains(spawner2));
    }

    @Test
    public void testMkM() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);

        Magpie magpie = enemyManager.mkM(null);

        assertNotNull(magpie);
        assertTrue(enemyManager.getBirds().contains(magpie));
        assertEquals(1, enemyManager.getBirds().size());
    }

    @Test
    public void testMkP() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        Pigeon pigeon = enemyManager.mkP(target);

        assertNotNull(pigeon);
        assertTrue(enemyManager.getBirds().contains(pigeon));
        assertEquals(1, enemyManager.getBirds().size());
        assertEquals(target, pigeon.getTrackedTarget());
    }

    @Test
    public void testMkE() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);

        Eagle eagle = enemyManager.mkE(null);

        assertNotNull(eagle);
        assertEquals(100, eagle.getX());
        assertEquals(150, eagle.getY());
    }

    @Test
    public void testCleanup() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        Pigeon pigeon1 = enemyManager.mkP(target);
        Pigeon pigeon2 = enemyManager.mkP(target);
        Pigeon pigeon3 = enemyManager.mkP(target);

        assertEquals(3, enemyManager.getBirds().size());

        pigeon2.markForRemoval();

        enemyManager.cleanup();

        assertEquals(2, enemyManager.getBirds().size());
        assertTrue(enemyManager.getBirds().contains(pigeon1));
        assertFalse(enemyManager.getBirds().contains(pigeon2));
        assertTrue(enemyManager.getBirds().contains(pigeon3));
    }

    @Test
    public void testCleanupMultiple() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        Pigeon pigeon1 = enemyManager.mkP(target);
        Pigeon pigeon2 = enemyManager.mkP(target);
        Pigeon pigeon3 = enemyManager.mkP(target);

        pigeon1.markForRemoval();
        pigeon3.markForRemoval();

        enemyManager.cleanup();

        assertEquals(1, enemyManager.getBirds().size());
        assertTrue(enemyManager.getBirds().contains(pigeon2));
    }

    @Test
    public void testGetMagpies() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        Magpie magpie1 = enemyManager.mkM(null);
        Pigeon pigeon = enemyManager.mkP(target);
        Magpie magpie2 = enemyManager.mkM(null);

        ArrayList<Magpie> magpies = enemyManager.getMagpies();

        assertEquals(2, magpies.size());
        assertTrue(magpies.contains(magpie1));
        assertTrue(magpies.contains(magpie2));
        assertFalse(magpies.contains(pigeon));
    }

    @Test
    public void testGetMagpiesEmpty() {
        ArrayList<Magpie> magpies = enemyManager.getMagpies();
        assertTrue(magpies.isEmpty());
    }

    @Test
    public void testGetALl() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        enemyManager.mkM(null);
        enemyManager.mkP(target);
        enemyManager.mkM(null);

        ArrayList<Enemy> all = enemyManager.getAll();

        assertEquals(3, all.size());
        assertSame(enemyManager.getBirds(), all);
    }

    @Test
    public void testRender() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        TargetTest target = new TargetTest(200, 250);

        enemyManager.mkM(null);
        enemyManager.mkP(target);

        List<Renderable> renderables = enemyManager.render();

        assertEquals(2, renderables.size());
    }

    @Test
    public void testRenderEmpty() {
        List<Renderable> renderables = enemyManager.render();
        assertTrue(renderables.isEmpty());
    }

    @Test
    public void testInteract() {
        try {
            enemyManager.interact(null, null);
        } catch (Exception e) {
            fail("interact() should not throw any exception");
        }
    }

    @Test
    public void testSpawnXY() {
        enemyManager.setSpawnX(123);
        enemyManager.setSpawnY(456);

        assertEquals(123, enemyManager.getSpawnX());
        assertEquals(456, enemyManager.getSpawnY());
    }
}