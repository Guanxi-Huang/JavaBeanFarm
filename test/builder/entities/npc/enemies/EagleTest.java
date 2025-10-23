package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Eagle.
 * Tests constructor, speed management, and food tracking.
 */
public class EagleTest {

    private Eagle eagle;
    private TargetTest mockPlayer;

    @Before
    public void setUp() {
        mockPlayer = new TargetTest(50, 50);
        eagle = new Eagle(10, 20, mockPlayer);
    }

    @Test
    public void testConstructorWithPlayer() {
        Eagle testEagle = new Eagle(5, 15, mockPlayer);
        assertEquals(5, testEagle.getX());
        assertEquals(15, testEagle.getY());
        assertEquals(mockPlayer, testEagle.getTrackedTarget());
        assertEquals(2, testEagle.getSpeed(), 0.01);
        assertNotNull(testEagle.getLifespan());
    }

    @Test
    public void testConstructorWithNullPlayer() {
        Eagle testEagle = new Eagle(5, 15, null);
        assertEquals(5, testEagle.getX());
        assertEquals(15, testEagle.getY());
        assertNull(testEagle.getTrackedTarget());
        assertNotNull(testEagle.getLifespan());
    }

    @Test
    public void testGetX() {
        assertEquals(10, eagle.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(20, eagle.getY());
    }

    @Test
    public void testSetX() {
        eagle.setX(100);
        assertEquals(100, eagle.getX());
    }

    @Test
    public void testSetY() {
        eagle.setY(110);
        assertEquals(110, eagle.getY());
    }

    @Test
    public void testInitialSpeed() {
        assertEquals(2, eagle.getSpeed(), 0.01);
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(eagle.getSpriteGroup());
    }

    @Test
    public void testTrackedTargetPlayer() {
        assertEquals(mockPlayer, eagle.getTrackedTarget());
    }

    @Test
    public void testFoodInitialization() {
        assertEquals(0, eagle.getFood());
    }

    @Test
    public void testGetFood() {
        assertEquals(0, eagle.getFood());
    }

    @Test
    public void testSetFood() {
        eagle.setFood(3);
        assertEquals(3, eagle.getFood());
    }

    @Test
    public void testMultipleEagles() {
        Eagle eagle1 = new Eagle(1, 2, mockPlayer);
        Eagle eagle2 = new Eagle(3, 4, mockPlayer);

        assertEquals(1, eagle1.getX());
        assertEquals(3, eagle2.getX());
        assertEquals(2, eagle1.getY());
        assertEquals(4, eagle2.getY());
    }

    @Test
    public void testEagleWithZeroCoordinates() {
        Eagle testEagle = new Eagle(0, 0, mockPlayer);
        assertEquals(0, testEagle.getX());
        assertEquals(0, testEagle.getY());
    }

    @Test
    public void testEagleWithLargeCoordinates() {
        Eagle testEagle = new Eagle(10000, 20000, mockPlayer);
        assertEquals(10000, testEagle.getX());
        assertEquals(20000, testEagle.getY());
    }

    @Test
    public void testSpawnPositionRetained() {
        assertEquals(10, eagle.getSpawnX());
        assertEquals(20, eagle.getSpawnY());

        eagle.setX(50);
        eagle.setY(60);

        // Spawn position should not change
        assertEquals(10, eagle.getSpawnX());
        assertEquals(20, eagle.getSpawnY());
    }

    @Test
    public void testSetTrackedTarget() {
        TargetTest newTarget = new TargetTest(100, 100);
        eagle.setTrackedTarget(newTarget);
        assertEquals(newTarget, eagle.getTrackedTarget());
    }

    @Test
    public void testIsAttackingByDefault() {
        assertTrue(eagle.isAttacking());
    }

    @Test
    public void testSetAttackingFalse() {
        eagle.setAttacking(false);
        assertFalse(eagle.isAttacking());
    }

    @Test
    public void testPositionModification() {
        eagle.setX(200);
        eagle.setY(300);
        assertEquals(200, eagle.getX());
        assertEquals(300, eagle.getY());
    }

    @Test
    public void testFoodIncrement() {
        eagle.setFood(0);
        assertEquals(0, eagle.getFood());

        eagle.setFood(1);
        assertEquals(1, eagle.getFood());

        eagle.setFood(5);
        assertEquals(5, eagle.getFood());
    }

    @Test
    public void testMultiplePositionUpdates() {
        eagle.setX(10);
        eagle.setY(20);
        eagle.setX(30);
        eagle.setY(40);
        eagle.setX(50);
        eagle.setY(60);

        assertEquals(50, eagle.getX());
        assertEquals(60, eagle.getY());
    }

    @Test
    public void testDistanceFromCalculation() {
        Eagle testEagle = new Eagle(0, 0, mockPlayer);
        TargetTest target = new TargetTest(3, 4);

        int distance = testEagle.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testSpeedNotModifiedByConstructor() {
        TargetTest target = new TargetTest(50, 50);
        Eagle testEagle = new Eagle(10, 10, target);
        // Initial speed is 2
        assertEquals(2, testEagle.getSpeed(), 0.01);
    }

    @Test
    public void testFoodManagement() {
        assertEquals(0, eagle.getFood());

        eagle.setFood(1);
        assertEquals(1, eagle.getFood());

        eagle.setFood(0);
        assertEquals(0, eagle.getFood());
    }

    @Test
    public void testSpawnerAccess() {
        assertNotNull(eagle.getSpawner());
    }

    @Test
    public void testChangeTrackedTarget() {
        TargetTest target1 = new TargetTest(10, 10);
        TargetTest target2 = new TargetTest(20, 20);

        eagle.setTrackedTarget(target1);
        assertEquals(target1, eagle.getTrackedTarget());

        eagle.setTrackedTarget(target2);
        assertEquals(target2, eagle.getTrackedTarget());
    }

    @Test
    public void testNullPlayerInitialization() {
        Eagle testEagle = new Eagle(5, 5, null);
        assertNull(testEagle.getTrackedTarget());
        assertEquals(90, testEagle.getDirection());
    }
}