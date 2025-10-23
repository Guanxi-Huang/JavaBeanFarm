package builder.entities.npc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NpcTest {
    private Npc npc;

    @Before
    public void setUp() {
        npc = new Npc(10, 20);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, npc.getX());
        assertEquals(20, npc.getY());
        assertEquals(0, npc.getDirection());
        assertEquals(1.0, npc.getSpeed(), 0.01);
    }

    @Test
    public void testSetAndGetSpeed() {
        npc.setSpeed(5);
        assertEquals(5.0, npc.getSpeed(), 0.01);
    }

    @Test
    public void testSetAndGetDirection() {
        npc.setDirection(45);
        assertEquals(45, npc.getDirection());
    }

    @Test
    public void testDirectionZero() {
        npc.setDirection(0);
        npc.move();
        // Direction 0 means moving right
        assertEquals(11, npc.getX()); // 10 + cos(0)*1 = 11
        assertEquals(20, npc.getY()); // 20 + sin(0)*1 = 20
    }

    @Test
    public void testDirection90() {
        npc.setDirection(90);
        npc.move();
        // Direction 90 means moving down
        assertEquals(10, npc.getX()); // 10 + cos(90)*1 ≈ 10
        assertEquals(21, npc.getY()); // 20 + sin(90)*1 = 21
    }

    @Test
    public void testDirection180() {
        npc.setDirection(180);
        npc.move();
        // Direction 180 means moving left
        assertEquals(9, npc.getX()); // 10 + cos(180)*1 = 9
        assertEquals(20, npc.getY()); // 20 + sin(180)*1 ≈ 20
    }

    @Test
    public void testDirection270() {
        npc.setDirection(270);
        npc.move();
        // Direction 270 means moving up
        assertEquals(10, npc.getX()); // 10 + cos(270)*1 ≈ 10
        assertEquals(19, npc.getY()); // 20 + sin(270)*1 = 19
    }

    @Test
    public void testMoveWithDifferentSpeeds() {
        npc.setSpeed(2);
        npc.setDirection(0); // Moving right
        npc.move();
        assertEquals(12, npc.getX()); // 10 + 2
        assertEquals(20, npc.getY());
    }

    @Test
    public void testDistanceFromCoordinates() {
        int distance = npc.distanceFrom(13, 24);
        // Distance = sqrt((13-10)^2 + (24-20)^2) = sqrt(9 + 16) = sqrt(25) = 5
        assertEquals(5, distance);
    }

    @Test
    public void testDistanceFromSamePosition() {
        npc.setX(10);
        npc.setY(20);
        int distance = npc.distanceFrom(10, 20);
        assertEquals(0, distance);
    }

    @Test
    public void testDistanceFromWithObject() {
        Npc otherNpc = new Npc(13, 24);
        int distance = npc.distanceFrom(otherNpc);
        assertEquals(5, distance);
    }

    @Test
    public void testMultipleMoves() {
        npc.setDirection(0); // Moving right
        npc.setSpeed(1);
        int initialX = npc.getX();
        npc.move();
        npc.move();
        npc.move();
        assertEquals(initialX + 3, npc.getX());
    }

    @Test
    public void testNegativeCoordinates() {
        Npc npcNegative = new Npc(-10, -20);
        assertEquals(-10, npcNegative.getX());
        assertEquals(-20, npcNegative.getY());
    }

    @Test
    public void testZeroCoordinates() {
        Npc npcZero = new Npc(0, 0);
        assertEquals(0, npcZero.getX());
        assertEquals(0, npcZero.getY());
    }

    @Test
    public void testDirectionNegative() {
        npc.setDirection(-45);
        npc.move();
        // Direction -45 is equivalent to 315 (moving down-right)
        assertTrue(npc.getX() > 10);
        assertTrue(npc.getY() > 20);
    }

    @Test
    public void testDirectionGreaterThan360() {
        npc.setDirection(405); // Same as 45 degrees
        int x1 = npc.getX();
        int y1 = npc.getY();
        npc.move();
        int movedX = npc.getX();
        int movedY = npc.getY();

        npc.setX(x1);
        npc.setY(y1);
        npc.setDirection(45);
        npc.move();

        assertEquals(movedX, npc.getX());
        assertEquals(movedY, npc.getY());
    }

    @Test
    public void testSpeedZero() {
        npc.setSpeed(0);
        int initialX = npc.getX();
        int initialY = npc.getY();
        npc.move();
        assertEquals(initialX, npc.getX());
        assertEquals(initialY, npc.getY());
    }

    @Test
    public void testHighSpeed() {
        npc.setSpeed(100);
        npc.setDirection(0);
        npc.move();
        assertEquals(110, npc.getX()); // 10 + 100
        assertEquals(20, npc.getY());
    }

    @Test
    public void testDistanceCalculationPythagorean() {
        // Test with 3-4-5 triangle
        Npc npc1 = new Npc(0, 0);
        Npc npc2 = new Npc(3, 4);
        assertEquals(5, npc1.distanceFrom(npc2));
    }

    @Test
    public void testDistanceCalculation5_12_13() {
        // Test with 5-12-13 triangle
        npc.setX(0);
        npc.setY(0);
        Npc otherNpc = new Npc(5, 12);
        assertEquals(13, npc.distanceFrom(otherNpc));
    }
}
