package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Pigeon.
 * Tests constructor variations, speed, and basic properties.
 */
public class PigeonTest {

    private Pigeon pigeon;

    @Before
    public void setUp() {
        pigeon = new Pigeon(10, 20);
    }

    @Test
    public void testConstructorBasic() {
        Pigeon testPigeon = new Pigeon(5, 15);
        assertEquals(5, testPigeon.getX());
        assertEquals(15, testPigeon.getY());
        assertEquals(5, testPigeon.getSpawnX());
        assertEquals(15, testPigeon.getSpawnY());
        assertNotNull(testPigeon.getLifespan());
    }

    @Test
    public void testConstructorWithTrackedTarget() {
        TargetTest target = new TargetTest(30, 40);
        Pigeon testPigeon = new Pigeon(10, 20, target);

        assertEquals(10, testPigeon.getX());
        assertEquals(20, testPigeon.getY());
        assertEquals(target, testPigeon.getTrackedTarget());
        assertEquals(1, testPigeon.getSpeed(), 0.01);
        assertNotNull(testPigeon.getLifespan());
    }

    @Test
    public void testGetX() {
        assertEquals(10, pigeon.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(20, pigeon.getY());
    }

    @Test
    public void testSetX() {
        pigeon.setX(50);
        assertEquals(50, pigeon.getX());
    }

    @Test
    public void testSetY() {
        pigeon.setY(60);
        assertEquals(60, pigeon.getY());
    }

    @Test
    public void testLifespan() {
        assertNotNull(pigeon.getLifespan());
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(pigeon.getSpriteGroup());
    }

    @Test
    public void testMultiplePigeons() {
        Pigeon pigeon1 = new Pigeon(1, 2);
        Pigeon pigeon2 = new Pigeon(3, 4);

        assertEquals(1, pigeon1.getX());
        assertEquals(3, pigeon2.getX());
        assertEquals(2, pigeon1.getY());
        assertEquals(4, pigeon2.getY());
    }

    @Test
    public void testPigeonWithZeroCoordinates() {
        Pigeon testPigeon = new Pigeon(0, 0);
        assertEquals(0, testPigeon.getX());
        assertEquals(0, testPigeon.getY());
    }


    @Test
    public void testPigeonWithLargeCoordinates() {
        Pigeon testPigeon = new Pigeon(5000, 6000);
        assertEquals(5000, testPigeon.getX());
        assertEquals(6000, testPigeon.getY());
    }

    @Test
    public void testSpawnPositionRetained() {
        assertEquals(10, pigeon.getSpawnX());
        assertEquals(20, pigeon.getSpawnY());

        pigeon.setX(50);
        pigeon.setY(60);

        // Spawn position should not change
        assertEquals(10, pigeon.getSpawnX());
        assertEquals(20, pigeon.getSpawnY());
    }

    @Test
    public void testSpeedIsSetWithTarget() {
        TargetTest target = new TargetTest(30, 40);
        Pigeon testPigeon = new Pigeon(10, 20, target);
        assertEquals(1, testPigeon.getSpeed(), 0.01);
    }

    @Test
    public void testTrackedTargetWithConstructor() {
        TargetTest target = new TargetTest(25, 35);
        Pigeon testPigeon = new Pigeon(10, 20, target);
        assertEquals(target, testPigeon.getTrackedTarget());
    }

    @Test
    public void testTrackedTargetNull() {
        assertNull(pigeon.getTrackedTarget());
    }

    @Test
    public void testPigeonIsAttackingByDefault() {
        assertTrue(pigeon.isAttacking());
    }

    @Test
    public void testSetAttackingFalse() {
        pigeon.setAttacking(false);
        assertFalse(pigeon.isAttacking());
    }

    @Test
    public void testPositionModification() {
        pigeon.setX(100);
        pigeon.setY(200);
        assertEquals(100, pigeon.getX());
        assertEquals(200, pigeon.getY());
    }

    @Test
    public void testMultiplePositionUpdates() {
        pigeon.setX(10);
        pigeon.setY(20);
        pigeon.setX(30);
        pigeon.setY(40);
        pigeon.setX(50);
        pigeon.setY(60);

        assertEquals(50, pigeon.getX());
        assertEquals(60, pigeon.getY());
    }

    @Test
    public void testPigeonWithTargetPositionChange() {
        TargetTest target = new TargetTest(30, 40);
        Pigeon testPigeon = new Pigeon(10, 20, target);

        target.setPosition(100, 150);
        assertEquals(100, testPigeon.getTrackedTarget().getX());
        assertEquals(150, testPigeon.getTrackedTarget().getY());
    }

    @Test
    public void testDistanceFromCalculation() {
        Pigeon testPigeon = new Pigeon(0, 0);
        TargetTest target = new TargetTest(3, 4);

        int distance = testPigeon.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testConstructorVariationsPositionConsistency() {
        Pigeon pigeon1 = new Pigeon(5, 5);
        TargetTest target = new TargetTest(10, 10);
        Pigeon pigeon2 = new Pigeon(5, 5, target);

        assertEquals(pigeon1.getX(), pigeon2.getX());
        assertEquals(pigeon1.getY(), pigeon2.getY());
    }

    @Test
    public void testSpawnPointAccess() {
        engine.game.HasPosition spawnPoint = pigeon.getSpawner().spawnPoint();
        assertNotNull(spawnPoint);
    }

    @Test
    public void testLifespanDuration() {
        // Pigeon's lifespan is set to 3000 ticks
        assertNotNull(pigeon.getLifespan());
    }

    @Test
    public void testChangeTrackedTarget() {
        TargetTest target1 = new TargetTest(10, 10);
        TargetTest target2 = new TargetTest(20, 20);

        pigeon.setTrackedTarget(target1);
        assertEquals(target1, pigeon.getTrackedTarget());

        pigeon.setTrackedTarget(target2);
        assertEquals(target2, pigeon.getTrackedTarget());
    }
}