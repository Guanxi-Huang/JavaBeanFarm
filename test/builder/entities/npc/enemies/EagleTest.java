package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Eagle class
 */
public class EagleTest {

    private Eagle eagle;
    private TargetTest target;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 150;

    @Before
    public void setUp() {
        target = new TargetTest(200, 250);
        eagle = new Eagle(SPAWN_X, SPAWN_Y, target);
    }

    @Test
    public void testConstructor() {
        assertEquals(SPAWN_X, eagle.getX());
        assertEquals(SPAWN_Y, eagle.getY());
        assertEquals(SPAWN_X, eagle.getSpawnX());
        assertEquals(SPAWN_Y, eagle.getSpawnY());
        assertEquals(target, eagle.getTrackedTarget());
        assertNotNull(eagle.getLifespan());
        assertEquals(2, eagle.getSpeed(), 0.01);
        assertTrue(eagle.isAttacking());
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(eagle.getSpriteGroup());
    }

    @Test
    public void testLifespanDuration() {
        FixedTimer lifespan = eagle.getLifespan();
        assertNotNull(lifespan);
        assertFalse(lifespan.isFinished());
    }

    @Test
    public void testInitialSpeed() {
        assertEquals(2, eagle.getSpeed(), 0.01);
    }

    @Test
    public void testDirection() {
        assertEquals(20, eagle.getDirection());
    }
}