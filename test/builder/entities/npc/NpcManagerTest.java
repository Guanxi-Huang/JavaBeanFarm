package builder.entities.npc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class NpcManagerTest {
    private NpcManager npcManager;

    @Before
    public void setUp() {
        npcManager = new NpcManager();

    }

    @Test
    public void testNpcManagerCreation() {
        assertNotNull(npcManager);
    }

    @Test
    public void testNpcManagerInitialization() {
        NpcManager manager = new NpcManager();
        assertNotNull(manager);
    }

    @Test
    public void testMultipleNpcManagers() {
        NpcManager manager1 = new NpcManager();
        NpcManager manager2 = new NpcManager();
        NpcManager manager3 = new NpcManager();

        assertNotNull(manager1);
        assertNotNull(manager2);
        assertNotNull(manager3);
    }

    @Test
    public void testNpcManagerIsNotNull() {
        NpcManager manager = new NpcManager();
        assertNotNull(manager);
    }
}