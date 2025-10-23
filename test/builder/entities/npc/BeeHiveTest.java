package builder.entities.npc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeeHiveTest {
    private BeeHive beeHive;

    @Before
    public void setUp() {
        beeHive = new BeeHive(50, 60);
    }

    @Test
    public void testConstructor() {
        assertEquals(50, beeHive.getX());
        assertEquals(60, beeHive.getY());
    }

    @Test
    public void testDifferentPositions() {
        BeeHive beeHive1 = new BeeHive(0, 0);
        BeeHive beeHive2 = new BeeHive(100, 150);

        assertEquals(0, beeHive1.getX());
        assertEquals(0, beeHive1.getY());
        assertEquals(100, beeHive2.getX());
        assertEquals(150, beeHive2.getY());
    }

    @Test
    public void testBeeHiveInheritance() {
        // BeeHive should inherit NPC properties
        assertEquals(0, beeHive.getDirection());
        assertEquals(0.0, beeHive.getSpeed(), 0.01);
    }

    @Test
    public void testMultipleBeeHives() {
        BeeHive beeHive1 = new BeeHive(10, 20);
        BeeHive beeHive2 = new BeeHive(30, 40);
        BeeHive beeHive3 = new BeeHive(50, 60);

        assertNotEquals(beeHive1.getX(), beeHive2.getX());
        assertNotEquals(beeHive2.getY(), beeHive3.getY());
    }

    @Test
    public void testZeroCoordinates() {
        BeeHive zeroHive = new BeeHive(0, 0);
        assertEquals(0, zeroHive.getX());
        assertEquals(0, zeroHive.getY());
    }
}