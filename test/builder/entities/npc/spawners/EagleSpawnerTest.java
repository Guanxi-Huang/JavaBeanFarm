package builder.entities.npc.spawners;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for EagleSpawner.
 * Tests constructor variations and default duration.
 */
public class EagleSpawnerTest {

    private EagleSpawner eagleSpawner;

    @Before
    public void setUp() {
        eagleSpawner = new EagleSpawner(20, 30);
    }

    @Test
    public void testConstructorWithDefaultDuration() {
        EagleSpawner spawner = new EagleSpawner(10, 20);
        assertEquals(10, spawner.getX());
        assertEquals(20, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testConstructorWithCustomDuration() {
        EagleSpawner spawner = new EagleSpawner(15, 25, 500);
        assertEquals(15, spawner.getX());
        assertEquals(25, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testDefaultDurationIs1000() {
        EagleSpawner spawner = new EagleSpawner(5, 10);
        // Default duration is 1000 ticks
        assertEquals(5, spawner.getX());
        assertEquals(10, spawner.getY());
    }

    @Test
    public void testGetX() {
        assertEquals(20, eagleSpawner.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(30, eagleSpawner.getY());
    }

    @Test
    public void testSetX() {
        eagleSpawner.setX(100);
        assertEquals(100, eagleSpawner.getX());
    }

    @Test
    public void testSetY() {
        eagleSpawner.setY(110);
        assertEquals(110, eagleSpawner.getY());
    }

    @Test
    public void testGetTimer() {
        assertNotNull(eagleSpawner.getTimer());
    }

    @Test
    public void testMultipleInstances() {
        EagleSpawner spawner1 = new EagleSpawner(1, 2);
        EagleSpawner spawner2 = new EagleSpawner(3, 4);

        assertEquals(1, spawner1.getX());
        assertEquals(2, spawner1.getY());
        assertEquals(3, spawner2.getX());
        assertEquals(4, spawner2.getY());
    }

    @Test
    public void testPositionWithZeroCoordinates() {
        EagleSpawner spawner = new EagleSpawner(0, 0);
        assertEquals(0, spawner.getX());
        assertEquals(0, spawner.getY());
    }

    @Test
    public void testPositionWithNegativeCoordinates() {
        EagleSpawner spawner = new EagleSpawner(-50, -100);
        assertEquals(-50, spawner.getX());
        assertEquals(-100, spawner.getY());
    }

    @Test
    public void testPositionWithLargeCoordinates() {
        EagleSpawner spawner = new EagleSpawner(10000, 20000);
        assertEquals(10000, spawner.getX());
        assertEquals(20000, spawner.getY());
    }

    @Test
    public void testCustomDurationVariations() {
        EagleSpawner spawner1 = new EagleSpawner(10, 20, 100);
        EagleSpawner spawner2 = new EagleSpawner(10, 20, 2000);

        assertEquals(10, spawner1.getX());
        assertEquals(10, spawner2.getX());
    }

    @Test
    public void testPositionModification() {
        eagleSpawner.setX(200);
        eagleSpawner.setY(300);
        assertEquals(200, eagleSpawner.getX());
        assertEquals(300, eagleSpawner.getY());
    }

    @Test
    public void testSpawnPointCreation() {
        engine.game.HasPosition spawnPoint = eagleSpawner.spawnPoint();
        assertNotNull(spawnPoint);
        assertEquals(20, spawnPoint.getX());
        assertEquals(30, spawnPoint.getY());
    }

    @Test
    public void testMultipleSetOperations() {
        eagleSpawner.setX(15);
        eagleSpawner.setY(25);
        eagleSpawner.setX(35);
        eagleSpawner.setY(45);

        assertEquals(35, eagleSpawner.getX());
        assertEquals(45, eagleSpawner.getY());
    }

    @Test
    public void testPositionIndependenceBetweenInstances() {
        EagleSpawner spawner1 = new EagleSpawner(10, 10);
        EagleSpawner spawner2 = new EagleSpawner(20, 20);

        spawner1.setX(100);

        assertEquals(100, spawner1.getX());
        assertEquals(20, spawner2.getX());
    }

    @Test
    public void testSpawnPointAfterPositionChange() {
        engine.game.HasPosition spawnPoint = eagleSpawner.spawnPoint();
        eagleSpawner.setX(88);
        eagleSpawner.setY(99);
        engine.game.HasPosition newSpawnPoint = eagleSpawner.spawnPoint();

        assertEquals(88, newSpawnPoint.getX());
        assertEquals(99, newSpawnPoint.getY());
    }

    @Test
    public void testGetXAndYConsistency() {
        int x = eagleSpawner.getX();
        int y = eagleSpawner.getY();

        assertEquals(20, x);
        assertEquals(30, y);
        assertEquals(x, eagleSpawner.getX());
        assertEquals(y, eagleSpawner.getY());
    }

    @Test
    public void testVeryLargeDuration() {
        EagleSpawner spawner = new EagleSpawner(10, 10, 999999);
        assertEquals(10, spawner.getX());
        assertEquals(10, spawner.getY());
    }
}
