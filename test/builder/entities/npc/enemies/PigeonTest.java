package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Pigeon class
 */
public class PigeonTest {

    private Pigeon pigeon;
    private TargetTest target;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 150;

    @Before
    public void setUp() {
        target = new TargetTest(200, 250);
        pigeon = new Pigeon(SPAWN_X, SPAWN_Y, target);
    }

    @Test
    public void testConstructorWithTarget() {
        Pigeon p = new Pigeon(SPAWN_X, SPAWN_Y, target);
        assertEquals(SPAWN_X, p.getX());
        assertEquals(SPAWN_Y, p.getY());
        assertEquals(SPAWN_X, p.getSpawnX());
        assertEquals(SPAWN_Y, p.getSpawnY());
        assertEquals(target, p.getTrackedTarget());
        assertNotNull(p.getLifespan());
        assertTrue(p.isAttacking());
        assertEquals(1, p.getSpeed(), 0.01);
    }

    @Test
    public void testConstructorWithoutTarget() {
        Pigeon p = new Pigeon(SPAWN_X, SPAWN_Y);
        assertEquals(SPAWN_X, p.getX());
        assertEquals(SPAWN_Y, p.getY());
        assertEquals(SPAWN_X, p.getSpawnX());
        assertEquals(SPAWN_Y, p.getSpawnY());
        assertNull(p.getTrackedTarget());
        assertNotNull(p.getLifespan());
        assertTrue(p.isAttacking());
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(pigeon.getSpriteGroup());
    }

    @Test
    public void testLifespanDuration() {
        FixedTimer lifespan = pigeon.getLifespan();
        assertNotNull(lifespan);
        assertFalse(lifespan.isFinished());
    }

    @Test
    public void testInitialSpeed() {
        assertEquals(1, pigeon.getSpeed(), 0.01);
    }

    @Test
    public void testInitialSprite() {
        assertNotNull(pigeon.getSprite());
        assertEquals("pigeon:down", pigeon.getSprite().getLabel());
    }

    @Test
    public void testLifespanForBothConstructors() {
        Pigeon p1 = new Pigeon(50, 50);
        Pigeon p2 = new Pigeon(50, 50, target);

        assertNotNull(p1.getLifespan());
        assertNotNull(p2.getLifespan());
        assertFalse(p1.getLifespan().isFinished());
        assertFalse(p2.getLifespan().isFinished());
    }
}