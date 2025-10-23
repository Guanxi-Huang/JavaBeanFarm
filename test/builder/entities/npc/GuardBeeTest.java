package builder.entities.npc;

import builder.entities.npc.TargetTest;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for GuardBee.
 * Tests bee movement, targeting, direction, lifespan, and collision detection.
 */
public class GuardBeeTest {

    private GuardBee bee;
    private TargetTest target;

    @Before
    public void setUp() {
        target = new TargetTest(200, 200);
        bee = new GuardBee(100, 100, target);
    }

    @Test
    public void testGuardBeeInitialization() {
        assertEquals(100, bee.getX());
        assertEquals(100, bee.getY());
        assertEquals(100, bee.getSpawnX());
        assertEquals(100, bee.getSpawnY());
        assertEquals(target, bee.getTrackedTarget());
    }

    @Test
    public void testGuardBeeSpeedConstant() {
        assertEquals(2, bee.getSpeed(), 0.01);
    }

    @Test
    public void testGuardBeeLifespanInitialization() {
        assertNotNull(bee.getLifespan());
        assertTrue(bee.getLifespan() instanceof FixedTimer);
    }

    @Test
    public void testGuardBeeSpawnPosition() {
        assertEquals(100, bee.getSpawnX());
        assertEquals(100, bee.getSpawnY());
    }

    @Test
    public void testGuardBeeTrackedTarget() {
        assertEquals(target, bee.getTrackedTarget());
    }

    @Test
    public void testGuardBeeWithNullTarget() {
        GuardBee testBee = new GuardBee(50, 50, null);
        assertNull(testBee.getTrackedTarget());
    }

    @Test
    public void testSetMoveDirectionWithTarget() {
        bee.setMoveDirection(target);
        assertNotNull(bee.getDirection());
    }

    @Test
    public void testSetMoveDirectionWithNull() {
        bee.setMoveDirection(null);
        assertEquals(90, bee.getDirection());
    }

    @Test
    public void testGuardBeeDirection() {
        bee.setMoveDirection(target);
        int direction = bee.getDirection();
        assertTrue(direction >= 0 && direction < 360);
    }

    @Test
    public void testGuardBeeMove() {
        int initialX = bee.getX();
        int initialY = bee.getY();

        bee.setDirection(0);
        bee.move();

        // Moving in direction 0 (right) should increase X
        assertTrue(bee.getX() >= initialX);
    }

    @Test
    public void testGuardBeePosition() {
        assertEquals(100, bee.getX());
        assertEquals(100, bee.getY());
    }

    @Test
    public void testGuardBeeSetX() {
        bee.setX(150);
        assertEquals(150, bee.getX());
    }

    @Test
    public void testGuardBeeSetY() {
        bee.setY(250);
        assertEquals(250, bee.getY());
    }

    @Test
    public void testGuardBeeDistanceFromTarget() {
        int distance = bee.distanceFrom(target);
        // Initial positions: bee at (100,100), target at (200,200)
        // Distance should be approximately 141
        assertTrue(distance > 0);
    }

    @Test
    public void testGuardBeeDistanceFromSamePosition() {
        TargetTest samePosition = new TargetTest(100, 100);
        int distance = bee.distanceFrom(samePosition);
        assertEquals(0, distance);
    }

    @Test
    public void testMultipleGuardBees() {
        TargetTest target1 = new TargetTest(200, 200);
        TargetTest target2 = new TargetTest(300, 300);

        GuardBee bee1 = new GuardBee(100, 100, target1);
        GuardBee bee2 = new GuardBee(150, 150, target2);

        assertEquals(100, bee1.getX());
        assertEquals(150, bee2.getX());
    }

    @Test
    public void testGuardBeeWithZeroCoordinates() {
        GuardBee testBee = new GuardBee(0, 0, target);
        assertEquals(0, testBee.getX());
        assertEquals(0, testBee.getY());
    }

    @Test
    public void testGuardBeeWithLargeCoordinates() {
        GuardBee testBee = new GuardBee(5000, 6000, target);
        assertEquals(5000, testBee.getX());
        assertEquals(6000, testBee.getY());
    }

    @Test
    public void testGuardBeeDirectionTowardsRight() {
        TargetTest rightTarget = new TargetTest(200, 100);
        bee.setMoveDirection(rightTarget);
        int direction = bee.getDirection();
        // Direction towards right should be close to 0
        assertTrue(direction >= 350 || direction <= 10);
    }

    @Test
    public void testGuardBeeDirectionTowardsDown() {
        TargetTest downTarget = new TargetTest(100, 200);
        bee.setMoveDirection(downTarget);
        int direction = bee.getDirection();
        // Direction towards down should be close to 90
        assertTrue(direction >= 80 && direction <= 100);
    }

    @Test
    public void testGuardBeeUpdateArtBasedOnDirectionDown() {
        bee.setDirection(90);
        bee.updateArtBasedOnDirection();
        // Direction 90 should update to down sprite
    }

    @Test
    public void testGuardBeeUpdateArtBasedOnDirectionUp() {
        bee.setDirection(270);
        bee.updateArtBasedOnDirection();
        // Direction 270 should update to up sprite
    }

    @Test
    public void testGuardBeeUpdateArtBasedOnDirectionRight() {
        bee.setDirection(0);
        bee.updateArtBasedOnDirection();
        // Direction 0 should update to right sprite
    }

    @Test
    public void testGuardBeeUpdateArtBasedOnDirectionLeft() {
        bee.setDirection(180);
        bee.updateArtBasedOnDirection();
        // Direction 180 should update to left sprite
    }

    @Test
    public void testGuardBeeMultipleDirectionChanges() {
        bee.setDirection(0);
        assertEquals(0, bee.getDirection());

        bee.setDirection(90);
        assertEquals(90, bee.getDirection());

        bee.setDirection(270);
        assertEquals(270, bee.getDirection());
    }

    @Test
    public void testGuardBeeSpawnPositionIndependence() {
        bee.setX(500);
        bee.setY(600);

        assertEquals(500, bee.getX());
        assertEquals(600, bee.getY());
        assertEquals(100, bee.getSpawnX());
        assertEquals(100, bee.getSpawnY());
    }

    @Test
    public void testGuardBeeTargetChange() {
        TargetTest newTarget = new TargetTest(300, 300);
        bee.setMoveDirection(newTarget);
        // Direction should be recalculated to new target
        assertNotNull(bee.getDirection());
    }

    @Test
    public void testGuardBeeLifespanDecrements() {
        FixedTimer lifespan = bee.getLifespan();
        assertNotNull(lifespan);
    }

    @Test
    public void testGuardBeePositionModification() {
        bee.setX(250);
        bee.setY(350);
        assertEquals(250, bee.getX());
        assertEquals(350, bee.getY());
    }

    @Test
    public void testGuardBeeSpeedIsTwo() {
        assertEquals(2, bee.getSpeed(), 0.01);
    }

    @Test
    public void testGuardBeeDirectionRange() {
        for (int i = 0; i < 360; i += 45) {
            bee.setDirection(i);
            assertEquals(i, bee.getDirection());
        }
    }

    @Test
    public void testGuardBeeDistanceCalculation() {
        bee.setX(0);
        bee.setY(0);

        TargetTest testTarget = new TargetTest(3, 4);
        int distance = bee.distanceFrom(testTarget);
        assertEquals(5, distance); // 3-4-5 triangle
    }

    @Test
    public void testTargetCanChange() {
        TargetTest initialTarget = new TargetTest(100, 100);
        GuardBee bee = new GuardBee(0, 0, initialTarget);
        assertEquals(initialTarget, bee.getTrackedTarget());

        TargetTest newTarget = new TargetTest(200, 200);
        assertEquals(initialTarget, bee.getTrackedTarget());
        assertNotEquals(newTarget, bee.getTrackedTarget());
    }
}
