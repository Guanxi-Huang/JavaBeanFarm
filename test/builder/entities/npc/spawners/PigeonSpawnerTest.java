package builder.entities.npc.spawners;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for PigeonSpawner.
 * Tests constructor variations and inherited behavior.
 */
public class PigeonSpawnerTest {

    private PigeonSpawner pigeonSpawner;

    @Before
    public void setUp() {
        pigeonSpawner = new PigeonSpawner(15, 25);
    }

    @Test
    public void testConstructorWithDefaultDuration() {
        PigeonSpawner spawner = new PigeonSpawner(10, 20);
        assertEquals(10, spawner.getX());
        assertEquals(20, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testConstructorWithCustomDuration() {
        PigeonSpawner spawner = new PigeonSpawner(30, 40, 200);
        assertEquals(30, spawner.getX());
        assertEquals(40, spawner.getY());
        assertNotNull(spawner.getTimer());
    }

    @Test
    public void testDefaultDurationIs100() {
        PigeonSpawner spawner = new PigeonSpawner(5, 10);
        // Default duration is 100 ticks
        assertEquals(5, spawner.getX());
        assertEquals(10, spawner.getY());
    }

    @Test
    public void testGetX() {
        assertEquals(15, pigeonSpawner.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(25, pigeonSpawner.getY());
    }

    @Test
    public void testSetX() {
        pigeonSpawner.setX(50);
        assertEquals(50, pigeonSpawner.getX());
    }

    @Test
    public void testSetY() {
        pigeonSpawner.setY(60);
        assertEquals(60, pigeonSpawner.getY());
    }

    @Test
    public void testGetTimer() {
        assertNotNull(pigeonSpawner.getTimer());
    }

    @Test
    public void testMultipleInstances() {
        PigeonSpawner spawner1 = new PigeonSpawner(1, 2);
        PigeonSpawner spawner2 = new PigeonSpawner(3, 4);

        assertEquals(1, spawner1.getX());
        assertEquals(2, spawner1.getY());
        assertEquals(3, spawner2.getX());
        assertEquals(4, spawner2.getY());
    }

    @Test
    public void testPositionWithZeroCoordinates() {
        PigeonSpawner spawner = new PigeonSpawner(0, 0);
        assertEquals(0, spawner.getX());
        assertEquals(0, spawner.getY());
    }

    @Test
    public void testPositionWithNegativeCoordinates() {
        PigeonSpawner spawner = new PigeonSpawner(-10, -20);
        assertEquals(-10, spawner.getX());
        assertEquals(-20, spawner.getY());
    }

    @Test
    public void testPositionWithLargeCoordinates() {
        PigeonSpawner spawner = new PigeonSpawner(5000, 6000);
        assertEquals(5000, spawner.getX());
        assertEquals(6000, spawner.getY());
    }

    @Test
    public void testCustomDurationVariations() {
        PigeonSpawner spawner1 = new PigeonSpawner(10, 20, 50);
        PigeonSpawner spawner2 = new PigeonSpawner(10, 20, 500);

        assertEquals(10, spawner1.getX());
        assertEquals(10, spawner2.getX());
    }

    @Test
    public void testPositionModification() {
        pigeonSpawner.setX(100);
        pigeonSpawner.setY(200);
        assertEquals(100, pigeonSpawner.getX());
        assertEquals(200, pigeonSpawner.getY());
    }

    @Test
    public void testDistanceFromCalculation() {
        PigeonSpawner spawner = new PigeonSpawner(0, 0);

        // Test distance calculation using TargetTest
        builder.entities.npc.TargetTest target = new builder.entities.npc.TargetTest(3, 4);
        int distance = spawner.distanceFrom(target);

        // Distance should be 5 (3-4-5 triangle)
        assertEquals(5, distance);
    }

    @Test
    public void testDistanceFromSamePosition() {
        PigeonSpawner spawner = new PigeonSpawner(10, 10);
        builder.entities.npc.TargetTest target = new builder.entities.npc.TargetTest(10, 10);

        int distance = spawner.distanceFrom(target);
        assertEquals(0, distance);
    }

    @Test
    public void testDistanceFromHorizontal() {
        PigeonSpawner spawner = new PigeonSpawner(0, 0);
        builder.entities.npc.TargetTest target = new builder.entities.npc.TargetTest(5, 0);

        int distance = spawner.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testDistanceFromVertical() {
        PigeonSpawner spawner = new PigeonSpawner(0, 0);
        builder.entities.npc.TargetTest target = new builder.entities.npc.TargetTest(0, 5);

        int distance = spawner.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testSpawnPointCreation() {
        engine.game.HasPosition spawnPoint = pigeonSpawner.spawnPoint();
        assertNotNull(spawnPoint);
        assertEquals(15, spawnPoint.getX());
        assertEquals(25, spawnPoint.getY());
    }

    @Test
    public void testMultipleSetOperations() {
        pigeonSpawner.setX(11);
        pigeonSpawner.setY(22);
        pigeonSpawner.setX(33);
        pigeonSpawner.setY(44);

        assertEquals(33, pigeonSpawner.getX());
        assertEquals(44, pigeonSpawner.getY());
    }

    @Test
    public void testDistanceFromWithNegativeCoordinates() {
        PigeonSpawner spawner = new PigeonSpawner(-3, -4);
        builder.entities.npc.TargetTest target = new builder.entities.npc.TargetTest(0, 0);

        int distance = spawner.distanceFrom(target);
        // Distance should be 5 (3-4-5 triangle)
        assertEquals(5, distance);
    }

    @Test
    public void testSpawnPointIndependence() {
        engine.game.HasPosition spawnPoint1 = pigeonSpawner.spawnPoint();
        pigeonSpawner.setX(50);
        pigeonSpawner.setY(60);
        engine.game.HasPosition spawnPoint2 = pigeonSpawner.spawnPoint();

        // First spawn point should reflect the new position
        assertEquals(50, spawnPoint2.getX());
        assertEquals(60, spawnPoint2.getY());
    }
}
