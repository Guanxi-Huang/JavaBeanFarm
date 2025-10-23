package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Magpie class
 */
public class MagpieTest {

    private Magpie magpie;
    private TargetTest target;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 150;

    @Before
    public void setUp() {
        target = new TargetTest(200, 250);
        magpie = new Magpie(SPAWN_X, SPAWN_Y, target);
    }

    @Test
    public void testConstructor() {
        assertEquals(SPAWN_X, magpie.getX());
        assertEquals(SPAWN_Y, magpie.getY());
        assertEquals(SPAWN_X, magpie.getSpawnX());
        assertEquals(SPAWN_Y, magpie.getSpawnY());
        assertEquals(target, magpie.getTrackedTarget());
        assertNotNull(magpie.getLifespan());
        assertTrue(magpie.isAttacking());
        assertEquals(0, magpie.getCoins());
    }

    @Test
    public void testGetSpriteGroup() {
        assertNotNull(magpie.getSpriteGroup());
    }

    @Test
    public void testLifespanDuration() {
        FixedTimer lifespan = magpie.getLifespan();
        assertNotNull(lifespan);
        assertFalse(lifespan.isFinished());
    }

    @Test
    public void testInitialCoins() {
        assertEquals(0, magpie.getCoins());
    }

    @Test
    public void testCoinsCanBeModified() {
        magpie.setCoins(5);
        assertEquals(5, magpie.getCoins());
    }

    @Test
    public void testInitialSprite() {
        assertNotNull(magpie.getSprite());
        assertEquals("magpie:down", magpie.getSprite().getLabel());
    }
}