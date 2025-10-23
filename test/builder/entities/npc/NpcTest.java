package builder.entities.npc;

import builder.entities.npc.TargetTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Npc abstract class.
 * Tests movement, direction, speed, and distance calculations.
 */
public class NpcTest {

    private Npc npc;

    @Before
    public void setUp() {
        // Create concrete implementation of Npc for testing
        npc = new TestNpc(10, 20);
    }

    @Test
    public void testNpcInitialization() {
        assertEquals(10, npc.getX());
        assertEquals(20, npc.getY());
    }

    @Test
    public void testNpcGetX() {
        assertEquals(10, npc.getX());
    }

    @Test
    public void testNpcGetY() {
        assertEquals(20, npc.getY());
    }

    @Test
    public void testNpcSetX() {
        npc.setX(50);
        assertEquals(50, npc.getX());
    }

    @Test
    public void testNpcSetY() {
        npc.setY(60);
        assertEquals(60, npc.getY());
    }

    @Test
    public void testNpcGetDirection() {
        assertEquals(0, npc.getDirection());
    }

    @Test
    public void testNpcSetDirection() {
        npc.setDirection(90);
        assertEquals(90, npc.getDirection());
    }

    @Test
    public void testNpcGetSpeed() {
        assertEquals(1, npc.getSpeed(), 0.01);
    }

    @Test
    public void testNpcSetSpeed() {
        npc.setSpeed(5);
        assertEquals(5, npc.getSpeed(), 0.01);
    }

    @Test
    public void testNpcMove() {
        int initialX = npc.getX();
        int initialY = npc.getY();

        npc.setDirection(0);
        npc.setSpeed(1);
        npc.move();

        // Direction 0 means moving right, so X should increase
        assertTrue(npc.getX() > initialX);
    }

    @Test
    public void testNpcMoveDirection90() {
        npc.setX(0);
        npc.setY(0);
        npc.setDirection(90);
        npc.setSpeed(10);

        int initialY = npc.getY();
        npc.move();

        // Direction 90 means moving down, so Y should increase
        assertTrue(npc.getY() > initialY);
    }

    @Test
    public void testNpcMoveDirection180() {
        npc.setX(100);
        npc.setY(0);
        npc.setDirection(180);
        npc.setSpeed(10);

        int initialX = npc.getX();
        npc.move();

        // Direction 180 means moving left, so X should decrease
        assertTrue(npc.getX() < initialX);
    }

    @Test
    public void testNpcMoveDirection270() {
        npc.setX(0);
        npc.setY(100);
        npc.setDirection(270);
        npc.setSpeed(10);

        int initialY = npc.getY();
        npc.move();

        // Direction 270 means moving up, so Y should decrease
        assertTrue(npc.getY() < initialY);
    }

    @Test
    public void testNpcDistanceFromPosition() {
        npc.setX(0);
        npc.setY(0);

        int distance = npc.distanceFrom(3, 4);
        assertEquals(5, distance); // 3-4-5 triangle
    }

    @Test
    public void testNpcDistanceFromHasPosition() {
        npc.setX(0);
        npc.setY(0);

        TargetTest target = new TargetTest(3, 4);
        int distance = npc.distanceFrom(target);
        assertEquals(5, distance);
    }

    @Test
    public void testNpcDistanceFromSamePosition() {
        npc.setX(10);
        npc.setY(20);

        int distance = npc.distanceFrom(10, 20);
        assertEquals(0, distance);
    }

    @Test
    public void testNpcMultipleDirectionChanges() {
        npc.setDirection(0);
        assertEquals(0, npc.getDirection());

        npc.setDirection(45);
        assertEquals(45, npc.getDirection());

        npc.setDirection(90);
        assertEquals(90, npc.getDirection());
    }

    @Test
    public void testNpcMultiplePositionUpdates() {
        npc.setX(10);
        npc.setY(20);
        assertEquals(10, npc.getX());
        assertEquals(20, npc.getY());

        npc.setX(30);
        npc.setY(40);
        assertEquals(30, npc.getX());
        assertEquals(40, npc.getY());
    }

    @Test
    public void testNpcMultipleSpeedChanges() {
        npc.setSpeed(1);
        assertEquals(1, npc.getSpeed(), 0.01);

        npc.setSpeed(5);
        assertEquals(5, npc.getSpeed(), 0.01);

        npc.setSpeed(10);
        assertEquals(10, npc.getSpeed(), 0.01);
    }

    @Test
    public void testNpcZeroCoordinates() {
        Npc testNpc = new TestNpc(0, 0);
        assertEquals(0, testNpc.getX());
        assertEquals(0, testNpc.getY());
    }

    @Test
    public void testNpcLargeCoordinates() {
        Npc testNpc = new TestNpc(5000, 6000);
        assertEquals(5000, testNpc.getX());
        assertEquals(6000, testNpc.getY());
    }

    @Test
    public void testNpcSpeedZero() {
        Npc testNpc = new TestNpc(0, 0);
        testNpc.setSpeed(0);

        int initialX = testNpc.getX();
        int initialY = testNpc.getY();

        testNpc.move();

        // With speed 0, position should not change significantly
        assertEquals(initialX, testNpc.getX());
        assertEquals(initialY, testNpc.getY());
    }

    @Test
    public void testNpcDistanceNegativeCoordinates() {
        npc.setX(-50);
        npc.setY(-100);

        int distance = npc.distanceFrom(-50, -100);
        assertEquals(0, distance);
    }

    @Test
    public void testNpcDirectionFullRange() {
        for (int i = 0; i < 360; i += 45) {
            npc.setDirection(i);
            assertEquals(i, npc.getDirection());
        }
    }

    @Test
    public void testNpcMoveMultipleTimes() {
        npc.setDirection(0);
        npc.setSpeed(1);

        int initialX = npc.getX();

        npc.move();
        npc.move();
        npc.move();

        // After 3 moves with direction 0, X should increase
        assertTrue(npc.getX() > initialX);
    }

    @Test
    public void testNpcInstanceIndependence() {
        Npc npc1 = new TestNpc(10, 20);
        Npc npc2 = new TestNpc(30, 40);

        npc1.setX(100);

        assertEquals(100, npc1.getX());
        assertEquals(30, npc2.getX());
    }

    @Test
    public void testNpcDistanceDifferentPositions() {
        npc.setX(0);
        npc.setY(0);

        int distance1 = npc.distanceFrom(5, 0);
        int distance2 = npc.distanceFrom(0, 5);
        int distance3 = npc.distanceFrom(3, 4);

        assertEquals(5, distance1);
        assertEquals(5, distance2);
        assertEquals(5, distance3);
    }

    @Test
    public void testNpcDirectionWithMove() {
        npc.setX(100);
        npc.setY(100);
        npc.setDirection(45);
        npc.setSpeed(10);

        npc.move();

        // Moving at 45 degrees should increase both X and Y
        assertTrue(npc.getX() > 100);
        assertTrue(npc.getY() > 100);
    }

    @Test
    public void testNpcDefaultSpeed() {
        Npc testNpc = new TestNpc(0, 0);
        assertEquals(1, testNpc.getSpeed(), 0.01);
    }

    @Test
    public void testNpcDefaultDirection() {
        Npc testNpc = new TestNpc(0, 0);
        assertEquals(0, testNpc.getDirection());
    }

    /**
     * Concrete implementation of Npc for testing abstract class
     */
    private static class TestNpc extends Npc {
        TestNpc(int x, int y) {
            super(x, y);
        }
    }
}