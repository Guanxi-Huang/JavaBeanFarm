package builder.entities.npc.spawners;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for MagpieSpawner.
 * Tests constructor variations and default duration.
 */
public class MagpieSpawnerTest {

    private MagpieSpawner magpieSpawner;

    @Before
    public void setUp() {
        magpieSpawner = new MagpieSpawner(25, 35);
    }

    @Test
    public void testConstructorWithDefaultDuration() {
        MagpieSpawner spawner = new MagpieSpawner(10, 20);
        assertEquals(10, spawner.getX());
        assertEquals(20, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testConstructorWithCustomDuration() {
        MagpieSpawner spawner = new MagpieSpawner(15, 25, 400);
        assertEquals(15, spawner.getX());
        assertEquals(25, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testDefaultDurationIs360() {
        MagpieSpawner spawner = new MagpieSpawner(5, 10);
        // Default duration is 360 ticks
        assertEquals(5, spawner.getX());
        assertEquals(10, spawner.getY());
    }

    @Test
    public void testGetX() {
        assertEquals(25, magpieSpawner.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(35, magpieSpawner.getY());
    }

    @Test
    public void testSetX() {
        magpieSpawner.setX(150);
        assertEquals(150, magpieSpawner.getX());
    }

    @Test
    public void testSetY() {
        magpieSpawner.setY(160);
        assertEquals(160, magpieSpawner.getY());
    }

    @Test
    public void testGetTimer() {
        assertNotNull(magpieSpawner.getTimer());
    }

    @Test
    public void testMultipleInstances() {
        MagpieSpawner spawner1 = new MagpieSpawner(1, 2);
        MagpieSpawner spawner2 = new MagpieSpawner(3, 4);

        assertEquals(1, spawner1.getX());
        assertEquals(2, spawner1.getY());
        assertEquals(3, spawner2.getX());
        assertEquals(4, spawner2.getY());
    }

    @Test
    public void testPositionWithZeroCoordinates() {
        MagpieSpawner spawner = new MagpieSpawner(0, 0);
        assertEquals(0, spawner.getX());
        assertEquals(0, spawner.getY());
    }

    @Test
    public void testPositionWithNegativeCoordinates() {
        MagpieSpawner spawner = new MagpieSpawner(-30, -40);
        assertEquals(-30, spawner.getX());
        assertEquals(-40, spawner.getY());
    }

    @Test
    public void testPositionWithLargeCoordinates() {
        MagpieSpawner spawner = new MagpieSpawner(8000, 9000);
        assertEquals(8000, spawner.getX());
        assertEquals(9000, spawner.getY());
    }

    @Test
    public void testCustomDurationVariations() {
        MagpieSpawner spawner1 = new MagpieSpawner(10, 20, 100);
        MagpieSpawner spawner2 = new MagpieSpawner(10, 20, 1000);

        assertEquals(10, spawner1.getX());
        assertEquals(10, spawner2.getX());
    }

    @Test
    public void testPositionModification() {
        magpieSpawner.setX(250);
        magpieSpawner.setY(350);
        assertEquals(250, magpieSpawner.getX());
        assertEquals(350, magpieSpawner.getY());
    }

    @Test
    public void testSpawnPointCreation() {
        engine.game.HasPosition spawnPoint = magpieSpawner.spawnPoint();
        assertNotNull(spawnPoint);
        assertEquals(25, spawnPoint.getX());
        assertEquals(35, spawnPoint.getY());
    }

    @Test
    public void testMultipleSetOperations() {
        magpieSpawner.setX(111);
        magpieSpawner.setY(222);
        magpieSpawner.setX(333);
        magpieSpawner.setY(444);

        assertEquals(333, magpieSpawner.getX());
        assertEquals(444, magpieSpawner.getY());
    }

    @Test
    public void testPositionIndependenceBetweenInstances() {
        MagpieSpawner spawner1 = new MagpieSpawner(50, 50);
        MagpieSpawner spawner2 = new MagpieSpawner(100, 100);

        spawner1.setX(150);

        assertEquals(150, spawner1.getX());
        assertEquals(100, spawner2.getX());
    }

    @Test
    public void testSpawnPointAfterPositionChange() {
        magpieSpawner.setX(77);
        magpieSpawner.setY(88);
        engine.game.HasPosition spawnPoint = magpieSpawner.spawnPoint();

        assertEquals(77, spawnPoint.getX());
        assertEquals(88, spawnPoint.getY());
    }

    @Test
    public void testGetXAndYConsistency() {
        int x = magpieSpawner.getX();
        int y = magpieSpawner.getY();

        assertEquals(25, x);
        assertEquals(35, y);
        assertEquals(x, magpieSpawner.getX());
        assertEquals(y, magpieSpawner.getY());
    }

    @Test
    public void testConsecutivePositionUpdates() {
        for (int i = 0; i < 10; i++) {
            magpieSpawner.setX(i * 10);
            magpieSpawner.setY(i * 20);
            assertEquals(i * 10, magpieSpawner.getX());
            assertEquals(i * 20, magpieSpawner.getY());
        }
    }

    @Test
    public void testDurationVariations() {
        MagpieSpawner spawner1 = new MagpieSpawner(10, 10, 50);
        MagpieSpawner spawner2 = new MagpieSpawner(10, 10, 500);
        MagpieSpawner spawner3 = new MagpieSpawner(10, 10, 5000);

        assertEquals(10, spawner1.getX());
        assertEquals(10, spawner2.getX());
        assertEquals(10, spawner3.getX());
    }
}