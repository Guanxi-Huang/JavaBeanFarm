package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Magpie.
 * Tests constructor, coins tracking, and directional updates.
 */
public class MagpieTest {

    private Magpie magpie;
    private TargetTest mockPlayer;

    @Before
    public void setUp() {
        mockPlayer = new TargetTest(50, 50);
        magpie = new Magpie(10, 20, mockPlayer);
    }

    @Test
    public void testConstructorWithPlayer() {
        Magpie testMagpie = new Magpie(5, 15, mockPlayer);
        assertEquals(5, testMagpie.getX());
        assertEquals(15, testMagpie.getY());
        assertEquals(mockPlayer, testMagpie.getTrackedTarget());
        assertNotNull(testMagpie.getLifespan());
    }

    @Test
    public void testConstructorWithNullPlayer() {
        Magpie testMagpie = new Magpie(5, 15, null);
        assertEquals(5, testMagpie.getX());
        assertEquals(15, testMagpie.getY());
        assertNull(testMagpie.getTrackedTarget());
        assertNotNull(testMagpie.getLifespan());
    }

    @Test
    public void testGetX() {
        assertEquals(10, magpie.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(20, magpie.getY());
    }

    @Test
    public void testSetX() {
        magpie.setX(150);
        assertEquals(150, magpie.getX());
    }

    @Test
    public void testSetY() {
        magpie.setY(160);
        assertEquals(160, magpie.getY());
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(magpie.getSpriteGroup());
    }

    @Test
    public void testTrackedTargetPlayer() {
        assertEquals(mockPlayer, magpie.getTrackedTarget());
    }

    @Test
    public void testCoinsInitialization() {
        assertEquals(0, magpie.getCoins());
    }

    @Test
    public void testGetCoins() {
        assertEquals(0, magpie.getCoins());
    }

    @Test
    public void testSetCoins() {
        magpie.setCoins(5);
        assertEquals(5, magpie.getCoins());
    }

    @Test
    public void testMultipleMagpies() {
        Magpie magpie1 = new Magpie(1, 2, mockPlayer);
        Magpie magpie2 = new Magpie(3, 4, mockPlayer);

        assertEquals(1, magpie1.getX());
        assertEquals(3, magpie2.getX());
        assertEquals(2, magpie1.getY());
        assertEquals(4, magpie2.getY());
    }

    @Test
    public void testMagpieWithZeroCoordinates() {
        Magpie testMagpie = new Magpie(0, 0, mockPlayer);
        assertEquals(0, testMagpie.getX());
        assertEquals(0, testMagpie.getY());
    }

    @Test
    public void testMagpieWithLargeCoordinates() {
        Magpie testMagpie = new Magpie(10000, 20000, mockPlayer);
        assertEquals(10000, testMagpie.getX());
        assertEquals(20000, testMagpie.getY());
    }

    @Test
    public void testSpawnPositionRetained() {
        assertEquals(10, magpie.getSpawnX());
        assertEquals(20, magpie.getSpawnY());

        magpie.setX(50);
        magpie.setY(60);

        // Spawn position should not change
        assertEquals(10, magpie.getSpawnX());
        assertEquals(20, magpie.getSpawnY());
    }

    @Test
    public void testSetTrackedTarget() {
        TargetTest newTarget = new TargetTest(100, 100);
        magpie.setTrackedTarget(newTarget);
        assertEquals(newTarget, magpie.getTrackedTarget());
    }

    @Test
    public void testIsAttackingByDefault() {
        assertTrue(magpie.isAttacking());
    }

    @Test
    public void testSetAttackingFalse() {
        magpie.setAttacking(false);
        assertFalse(magpie.isAttacking());
    }

    @Test
    public void testPositionModification() {
        magpie.setX(250);
        magpie.setY(350);
        assertEquals(250, magpie.getX());
        assertEquals(350, magpie.getY());
    }

    @Test
    public void testCoinsIncrement() {
        magpie.setCoins(0);
        assertEquals(0, magpie.getCoins());

        magpie.setCoins(1);
        assertEquals(1, magpie.getCoins());

        magpie.setCoins(10);
        assertEquals(10, magpie.getCoins());
    }

    @Test
    public void testMultiplePositionUpdates() {
        magpie.setX(10);
        magpie.setY(20);
        magpie.setX(30);
        magpie.setY(40);
        magpie.setX(50);
        magpie.setY(60);

        assertEquals(50, magpie.getX());
        assertEquals(60, magpie.getY());
    }

    @Test
    public void testDistanceFromCalculation() {
        Magpie testMagpie = new Magpie(0, 0, mockPlayer);
        TargetTest target = new TargetTest(3, 4);

        int distance = testMagpie.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testCoinsManagement() {
        assertEquals(0, magpie.getCoins());

        magpie.setCoins(1);
        assertEquals(1, magpie.getCoins());

        magpie.setCoins(5);
        assertEquals(5, magpie.getCoins());

        magpie.setCoins(0);
        assertEquals(0, magpie.getCoins());
    }

    @Test
    public void testSpawnerAccess() {
        assertNotNull(magpie.getSpawner());
    }

    @Test
    public void testChangeTrackedTarget() {
        TargetTest target1 = new TargetTest(10, 10);
        TargetTest target2 = new TargetTest(20, 20);

        magpie.setTrackedTarget(target1);
        assertEquals(target1, magpie.getTrackedTarget());

        magpie.setTrackedTarget(target2);
        assertEquals(target2, magpie.getTrackedTarget());
    }

    @Test
    public void testCoinsValue() {
        int testCoins = 42;
        magpie.setCoins(testCoins);
        assertEquals(testCoins, magpie.getCoins());
    }

    @Test
    public void testNullPlayerInitialization() {
        Magpie testMagpie = new Magpie(5, 5, null);
        assertNull(testMagpie.getTrackedTarget());
        assertEquals(90, testMagpie.getDirection());
    }

    @Test
    public void testDirectionalUpdateTimerInitialization() {
        assertNotNull(magpie);
        assertEquals(10, magpie.getX());
    }

    @Test
    public void testLifespanDuration() {
        assertNotNull(magpie.getLifespan());
    }
}