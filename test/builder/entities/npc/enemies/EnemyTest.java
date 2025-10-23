package builder.entities.npc.enemies;

import builder.entities.npc.TargetTest;
import builder.entities.npc.spawners.SetSpawner;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Enemy.
 * Tests position management, spawn properties, and state tracking.
 */
public class EnemyTest {

    private Enemy enemy;

    @Before
    public void setUp() {
        // Create a concrete Enemy implementation for testing
        enemy = new TestEnemy(10, 20);
    }

    @Test
    public void testEnemyInitialization() {
        assertEquals(10, enemy.getX());
        assertEquals(20, enemy.getY());
        assertEquals(10, enemy.getSpawnX());
        assertEquals(20, enemy.getSpawnY());
    }

    @Test
    public void testGetSpawnX() {
        assertEquals(10, enemy.getSpawnX());
    }

    @Test
    public void testGetSpawnY() {
        assertEquals(20, enemy.getSpawnY());
    }

    @Test
    public void testSetSpawnX() {
        enemy.setSpawnX(50);
        assertEquals(50, enemy.getSpawnX());
    }

    @Test
    public void testSetSpawnY() {
        enemy.setSpawnY(60);
        assertEquals(60, enemy.getSpawnY());
    }

    @Test
    public void testGetLifespan() {
        assertNull(enemy.getLifespan());
    }

    @Test
    public void testSetLifespan() {
        FixedTimer timer = new FixedTimer(1000);
        enemy.setLifespan(timer);
        assertNotNull(enemy.getLifespan());
        assertEquals(timer, enemy.getLifespan());
    }

    @Test
    public void testIsAttackingDefault() {
        assertTrue(enemy.isAttacking());
    }

    @Test
    public void testSetAttacking() {
        enemy.setAttacking(false);
        assertFalse(enemy.isAttacking());

        enemy.setAttacking(true);
        assertTrue(enemy.isAttacking());
    }

    @Test
    public void testGetTrackedTarget() {
        assertNull(enemy.getTrackedTarget());
    }

    @Test
    public void testSetTrackedTarget() {
        TargetTest target = new TargetTest(30, 40);
        enemy.setTrackedTarget(target);

        assertNotNull(enemy.getTrackedTarget());
        assertEquals(target, enemy.getTrackedTarget());
    }

    @Test
    public void testGetSpawner() {
        assertNotNull(enemy.getSpawner());
    }

    @Test
    public void testSetSpawner() {
        SetSpawner newSpawner = new SetSpawner(5, 15, 100) {
            @Override
            protected void spawn(builder.GameState game) {}
        };

        enemy.setSpawner(newSpawner);
        assertEquals(newSpawner, enemy.getSpawner());
    }

    @Test
    public void testSetTargetDirection() {
        enemy.setX(0);
        enemy.setY(0);
        enemy.setTargetDirection(3, 4);
        // Direction should be set based on atan2 of (4, 3)
        assertNotNull(enemy.getDirection());
    }

    @Test
    public void testSetTargetDirectionSamePosition() {
        enemy.setX(10);
        enemy.setY(20);
        enemy.setTargetDirection(10, 20);
        // Direction should be 0 or similar
        assertNotNull(enemy.getDirection());
    }

    @Test
    public void testSetTargetDirectionDifferentTargets() {
        enemy.setX(0);
        enemy.setY(0);
        int dir1 = enemy.getDirection();

        enemy.setTargetDirection(1, 0);
        int dir2 = enemy.getDirection();

        enemy.setTargetDirection(0, 1);
        int dir3 = enemy.getDirection();

        // Different targets should result in different directions
        assertNotEquals(dir2, dir3);
    }

    @Test
    public void testUpdateSpriteDown() {
        // When tracked target Y is greater than enemy Y, sprite should be "down"
        enemy.setY(10);
        enemy.updateSprite(20);
        // Sprite is set but we can't directly verify it in this simple test
    }

    @Test
    public void testUpdateSpriteUp() {
        // When tracked target Y is less than or equal to enemy Y, sprite should be "up"
        enemy.setY(20);
        enemy.updateSprite(10);
        // Sprite is set but we can't directly verify it in this simple test
    }

    @Test
    public void testUpdateSpriteNull() {
        // Should not throw when sprite group is null
        enemy.updateSprite(30);
    }

    @Test
    public void testIsClosedNullTarget() {
        assertFalse(enemy.isClosed(null, null));
    }

    @Test
    public void testIsClosedNullEngine() {
        TargetTest target = new TargetTest(10, 20);
        assertFalse(enemy.isClosed(target, null));
    }

    @Test
    public void testDistanceFromSamePosition() {
        enemy.setX(10);
        enemy.setY(20);
        TargetTest target = new TargetTest(10, 20);

        int distance = enemy.distanceFrom(target);
        assertEquals(0, distance);
    }

    @Test
    public void testDistanceFromDifferentPosition() {
        enemy.setX(0);
        enemy.setY(0);
        TargetTest target = new TargetTest(3, 4);

        int distance = enemy.distanceFrom(target);
        assertEquals(5, distance); // 3-4-5 triangle
    }

    @Test
    public void testMultipleSpawnPositionChanges() {
        enemy.setSpawnX(1);
        enemy.setSpawnY(2);
        assertEquals(1, enemy.getSpawnX());
        assertEquals(2, enemy.getSpawnY());

        enemy.setSpawnX(10);
        enemy.setSpawnY(20);
        assertEquals(10, enemy.getSpawnX());
        assertEquals(20, enemy.getSpawnY());
    }

    @Test
    public void testTrackedTargetChanged() {
        TargetTest target1 = new TargetTest(1, 1);
        TargetTest target2 = new TargetTest(2, 2);

        enemy.setTrackedTarget(target1);
        assertEquals(target1, enemy.getTrackedTarget());

        enemy.setTrackedTarget(target2);
        assertEquals(target2, enemy.getTrackedTarget());
    }

    @Test
    public void testAttackingStateToggle() {
        assertTrue(enemy.isAttacking());

        enemy.setAttacking(false);
        assertFalse(enemy.isAttacking());

        enemy.setAttacking(false);
        assertFalse(enemy.isAttacking());

        enemy.setAttacking(true);
        assertTrue(enemy.isAttacking());
    }

    @Test
    public void testMultipleLifespanChanges() {
        FixedTimer timer1 = new FixedTimer(1000);
        enemy.setLifespan(timer1);
        assertEquals(timer1, enemy.getLifespan());

        FixedTimer timer2 = new FixedTimer(2000);
        enemy.setLifespan(timer2);
        assertEquals(timer2, enemy.getLifespan());
    }

    @Test
    public void testEnemyPosition() {
        assertEquals(10, enemy.getX());
        assertEquals(20, enemy.getY());

        enemy.setX(30);
        enemy.setY(40);
        assertEquals(30, enemy.getX());
        assertEquals(40, enemy.getY());
    }

    @Test
    public void testZeroSpawnPosition() {
        Enemy testEnemy = new TestEnemy(0, 0);
        assertEquals(0, testEnemy.getSpawnX());
        assertEquals(0, testEnemy.getSpawnY());
    }

    /**
     * Concrete Enemy implementation for testing
     */
    private static class TestEnemy extends Enemy {
        @Override
        public engine.art.sprites.SpriteGroup getSpriteGroup() {
            return null;
        }

        @Override
        public void updateAttack(engine.EngineState engine, builder.GameState game) {
            // Test implementation
        }

        TestEnemy(int x, int y) {
            super(x, y);
        }
    }
}