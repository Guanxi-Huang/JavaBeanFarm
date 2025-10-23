package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.enemies.EnemyManager;
import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SetSpawner.
 * Tests position management, timer functionality, and abstract methods.
 */
public class SetSpawnerTest {

    private SetSpawner spawner;
    private GameState mockGameState;
    private EngineState mockEngineState;

    @Before
    public void setUp() {
        // Create a concrete implementation of SetSpawner for testing
        spawner = new SetSpawner(10, 20, 100) {
            @Override
            protected void spawn(GameState game) {
                // Test implementation
            }
        };

        mockGameState = null;
        mockEngineState = null;
    }

    @Test
    public void testConstructorWithDuration() {
        SetSpawner testSpawner = new SetSpawner(5, 15, 200) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(5, testSpawner.getX());
        assertEquals(15, testSpawner.getY());
        assertNotNull(testSpawner.getTimer());
    }

    @Test
    public void testConstructorWithoutDuration() {
        SetSpawner testSpawner = new SetSpawner(30, 40) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(30, testSpawner.getX());
        assertEquals(40, testSpawner.getY());
    }

    @Test
    public void testGetX() {
        assertEquals(10, spawner.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(20, spawner.getY());
    }

    @Test
    public void testSetX() {
        spawner.setX(50);
        assertEquals(50, spawner.getX());
    }

    @Test
    public void testSetY() {
        spawner.setY(60);
        assertEquals(60, spawner.getY());
    }

    @Test
    public void testSetXAndY() {
        spawner.setX(100);
        spawner.setY(200);
        assertEquals(100, spawner.getX());
        assertEquals(200, spawner.getY());
    }

    @Test
    public void testGetTimer() {
        TickTimer timer = spawner.getTimer();
        assertNotNull(timer);
        assertTrue(timer instanceof RepeatingTimer);
    }

    @Test
    public void testSpawnPointPosition() {
        HasPosition spawnPoint = (HasPosition) spawner.spawnPoint();
        assertEquals(10, spawnPoint.getX());
        assertEquals(20, spawnPoint.getY());
    }

    @Test
    public void testSpawnPointAfterPositionChange() {
        spawner.setX(77);
        spawner.setY(88);
        HasPosition spawnPoint = (HasPosition) spawner.spawnPoint();
        assertEquals(77, spawnPoint.getX());
        assertEquals(88, spawnPoint.getY());
    }

    @Test
    public void testMultipleSetXCalls() {
        spawner.setX(11);
        assertEquals(11, spawner.getX());
        spawner.setX(22);
        assertEquals(22, spawner.getX());
        spawner.setX(33);
        assertEquals(33, spawner.getX());
    }

    @Test
    public void testMultipleSetYCalls() {
        spawner.setY(12);
        assertEquals(12, spawner.getY());
        spawner.setY(23);
        assertEquals(23, spawner.getY());
        spawner.setY(34);
        assertEquals(34, spawner.getY());
    }

    @Test
    public void testZeroPosition() {
        SetSpawner testSpawner = new SetSpawner(0, 0, 50) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(0, testSpawner.getX());
        assertEquals(0, testSpawner.getY());
    }

    @Test
    public void testNegativePosition() {
        SetSpawner testSpawner = new SetSpawner(-50, -100, 75) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(-50, testSpawner.getX());
        assertEquals(-100, testSpawner.getY());
    }

    @Test
    public void testLargePosition() {
        SetSpawner testSpawner = new SetSpawner(9999, 8888, 100) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(9999, testSpawner.getX());
        assertEquals(8888, testSpawner.getY());
    }

    @Test
    public void testTimerNotNull() {
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testPositionIndependence() {
        SetSpawner spawner1 = new SetSpawner(1, 2, 100) {
            @Override
            protected void spawn(GameState game) {}
        };

        SetSpawner spawner2 = new SetSpawner(3, 4, 100) {
            @Override
            protected void spawn(GameState game) {}
        };

        assertEquals(1, spawner1.getX());
        assertEquals(2, spawner1.getY());
        assertEquals(3, spawner2.getX());
        assertEquals(4, spawner2.getY());

        spawner1.setX(10);
        assertEquals(10, spawner1.getX());
        assertEquals(3, spawner2.getX());
    }

    @Test
    public void testSetPositionMultipleTimes() {
        spawner.setX(1);
        spawner.setY(2);
        assertEquals(1, spawner.getX());
        assertEquals(2, spawner.getY());

        spawner.setX(5);
        spawner.setY(6);
        assertEquals(5, spawner.getX());
        assertEquals(6, spawner.getY());

        spawner.setX(9);
        spawner.setY(10);
        assertEquals(9, spawner.getX());
        assertEquals(10, spawner.getY());
    }
}
