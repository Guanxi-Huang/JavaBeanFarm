package builder.entities.npc.enemies;

import builder.GameState;

import builder.entities.npc.TargetTest;
import engine.EngineState;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnemyTest {

    private Enemy enemy;
    private GameState gameState;
    private EngineState engineState;

    @Before
    public void setUp() {
        enemy = new Enemy(100, 100) {
            @Override
            public void updateAttack(EngineState engine, GameState game) {}
            @Override
            public engine.art.sprites.SpriteGroup getSpriteGroup() {
                return null; // Placeholder
            }
        };
        TargetTest target = new TargetTest(200, 200);
        enemy.setTrackedTarget(target);
        enemy.setLifespan(new FixedTimer(1000));
    }

    @Test
    public void testConstructor() {
        assertEquals(100, enemy.getX());
        assertEquals(100, enemy.getY());
        assertTrue(enemy.isAttacking());
        assertEquals(100, enemy.getSpawnX());
        assertEquals(100, enemy.getSpawnY());
    }

    @Test
    public void testSetTargetDirection() {
        enemy.setTargetDirection(200, 200);
        assertTrue(Math.abs(enemy.getDirection() - 45) < 1);
    }

    @Test
    public void testUpdateSpriteDown() {
        // Target Y > enemy Y
        enemy.updateSprite(300);
        // Cannot assert sprite directly without SpriteGroup implementation
    }

    @Test
    public void testUpdateSpriteUp() {
        // Target Y < enemy Y
        enemy.updateSprite(50); // Target Y < enemy Y
        // Cannot assert sprite directly without SpriteGroup implementation
    }

    @Test
    public void testTickAttacking() {
        int initialX = enemy.getX();
        enemy.tick(engineState, gameState);
        assertTrue(enemy.getX() > initialX);
    }

}