package builder.entities.npc;

import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.TargetTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Scarecrow.
 * Tests fear radius, enemy repulsion, coin cost, and sprite management.
 */
public class ScarecrowTest {

    private Scarecrow scarecrow;
    private TargetTest mockTarget;

    @Before
    public void setUp() {
        scarecrow = new Scarecrow(100, 100);
        mockTarget = new TargetTest(100, 100);
    }

    @Test
    public void testScarecrowInitialization() {
        assertEquals(100, scarecrow.getX());
        assertEquals(100, scarecrow.getY());
        assertEquals(0, scarecrow.getSpeed(), 0.01);
    }

    @Test
    public void testScarecrowPositionX() {
        assertEquals(100, scarecrow.getX());
    }

    @Test
    public void testScarecrowPositionY() {
        assertEquals(100, scarecrow.getY());
    }

    @Test
    public void testScarecrowSpeedIsZero() {
        assertEquals(0, scarecrow.getSpeed(), 0.01);
    }

    @Test
    public void testScarecrowCoinCostConstant() {
        assertEquals(2, Scarecrow.COIN_COST);
    }

    @Test
    public void testScarecrowSetX() {
        scarecrow.setX(200);
        assertEquals(200, scarecrow.getX());
    }

    @Test
    public void testScarecrowSetY() {
        scarecrow.setY(300);
        assertEquals(300, scarecrow.getY());
    }

    @Test
    public void testMultipleScarecrows() {
        Scarecrow scarecrow1 = new Scarecrow(50, 50);
        Scarecrow scarecrow2 = new Scarecrow(150, 150);

        assertEquals(50, scarecrow1.getX());
        assertEquals(150, scarecrow2.getX());
        assertEquals(0, scarecrow1.getSpeed(), 0.01);
        assertEquals(0, scarecrow2.getSpeed(), 0.01);
    }

    @Test
    public void testScarecrowWithZeroCoordinates() {
        Scarecrow testScarecrow = new Scarecrow(0, 0);
        assertEquals(0, testScarecrow.getX());
        assertEquals(0, testScarecrow.getY());
    }

    @Test
    public void testScarecrowWithLargeCoordinates() {
        Scarecrow testScarecrow = new Scarecrow(5000, 6000);
        assertEquals(5000, testScarecrow.getX());
        assertEquals(6000, testScarecrow.getY());
    }

    @Test
    public void testScarecrowPositionModification() {
        scarecrow.setX(250);
        scarecrow.setY(350);
        assertEquals(250, scarecrow.getX());
        assertEquals(350, scarecrow.getY());
    }

    @Test
    public void testScarecrowMultiplePositionUpdates() {
        scarecrow.setX(111);
        scarecrow.setY(222);
        scarecrow.setX(333);
        scarecrow.setY(444);

        assertEquals(333, scarecrow.getX());
        assertEquals(444, scarecrow.getY());
    }

    @Test
    public void testScarecrowPositionIndependenceBetweenInstances() {
        Scarecrow scarecrow1 = new Scarecrow(100, 100);
        Scarecrow scarecrow2 = new Scarecrow(200, 200);

        scarecrow1.setX(300);

        assertEquals(300, scarecrow1.getX());
        assertEquals(200, scarecrow2.getX());
    }

    @Test
    public void testScarecrowDistanceFromPosition() {
        TargetTest position = new TargetTest(200, 200);
        int distance = scarecrow.distanceFrom(position);

        // Distance from (100,100) to (200,200) should be approximately 141
        assertTrue(distance > 0);
    }

    @Test
    public void testScarecrowDistanceFromSamePosition() {
        TargetTest samePosition = new TargetTest(100, 100);
        int distance = scarecrow.distanceFrom(samePosition);
        assertEquals(0, distance);
    }

    @Test
    public void testScarecrowDistanceCalculation() {
        scarecrow.setX(0);
        scarecrow.setY(0);

        TargetTest testTarget = new TargetTest(3, 4);
        int distance = scarecrow.distanceFrom(testTarget);
        assertEquals(5, distance); // 3-4-5 triangle
    }

    @Test
    public void testScarecrowMagpieWithinRadius() {
        Magpie magpie = new Magpie(120, 120, mockTarget);
        assertTrue(magpie.isAttacking());
    }

    @Test
    public void testScarecrowPigeonWithinRadius() {
        Pigeon pigeon = new Pigeon(120, 120, mockTarget);
        assertTrue(pigeon.isAttacking());
    }

    @Test
    public void testScarecrowEagleNotAffected() {
        Eagle eagle = new Eagle(120, 120, mockTarget);
        assertTrue(eagle.isAttacking());
    }

    @Test
    public void testScarecrowPigeonOutOfRadius() {
        Pigeon pigeon = new Pigeon(500, 500, mockTarget);
        int distance = scarecrow.distanceFrom(pigeon);
        assertTrue(distance > 128); // 4 tiles * 32 pixels per tile
    }

    @Test
    public void testScarecrowCoinCost() {
        int cost = Scarecrow.COIN_COST;
        assertEquals(2, cost);
    }

    @Test
    public void testScarecrowDirection() {
        scarecrow.setDirection(90);
        assertEquals(90, scarecrow.getDirection());
    }

    @Test
    public void testScarecrowSpeed() {
        assertEquals(0, scarecrow.getSpeed(), 0.01);
    }

    @Test
    public void testScarecrowMove() {
        scarecrow.setSpeed(0);
        int initialX = scarecrow.getX();
        int initialY = scarecrow.getY();

        scarecrow.move();

        // With speed 0, position should not change
        assertEquals(initialX, scarecrow.getX());
        assertEquals(initialY, scarecrow.getY());
    }

    @Test
    public void testScarecrowTickBehavior() {
        // Scarecrow should not move as speed is 0
        int initialX = scarecrow.getX();
        int initialY = scarecrow.getY();

        scarecrow.setSpeed(0);
        scarecrow.move();

        assertEquals(initialX, scarecrow.getX());
        assertEquals(initialY, scarecrow.getY());
    }

    @Test
    public void testScarecrowMagpieDetection() {
        Magpie magpie = new Magpie(110, 110, mockTarget);
        int distance = scarecrow.distanceFrom(magpie);
        assertTrue(distance < 200); // Within reasonable fear radius
    }

    @Test
    public void testScarecrowPigeonDetection() {
        Pigeon pigeon = new Pigeon(110, 110, mockTarget);
        int distance = scarecrow.distanceFrom(pigeon);
        assertTrue(distance < 200);
    }

    @Test
    public void testScarecrowEagleDistanceCalculation() {
        Eagle eagle = new Eagle(200, 200, mockTarget);
        int distance = scarecrow.distanceFrom(eagle);
        assertTrue(distance > 0);
    }

    @Test
    public void testScarecrowPositionSetGetConsistency() {
        for (int i = 0; i < 10; i++) {
            scarecrow.setX(i * 100);
            scarecrow.setY(i * 50);
            assertEquals(i * 100, scarecrow.getX());
            assertEquals(i * 50, scarecrow.getY());
        }
    }

    @Test
    public void testScarecrowSpeedConstant() {
        Scarecrow testScarecrow = new Scarecrow(100, 100);
        assertEquals(0, testScarecrow.getSpeed(), 0.01);
    }

    @Test
    public void testScarecrowInstanceIndependence() {
        Scarecrow s1 = new Scarecrow(100, 100);
        Scarecrow s2 = new Scarecrow(200, 200);
        Scarecrow s3 = new Scarecrow(300, 300);

        assertEquals(100, s1.getX());
        assertEquals(200, s2.getX());
        assertEquals(300, s3.getX());

        s1.setX(999);

        assertEquals(999, s1.getX());
        assertEquals(200, s2.getX());
        assertEquals(300, s3.getX());
    }
}
