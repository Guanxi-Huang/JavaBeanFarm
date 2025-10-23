package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;


/**
 * The type Scarecrow.
 */
public class Scarecrow extends Npc {

    public static final int COIN_COST = 2;
    private static final SpriteGroup art = SpriteGallery.scarecrow;

    /**
     * Instantiates a new Scarecrow.
     *
     * @param x the initialized x-coordinate of Scarecrow
     * @param y the initialized y-coordinate of Scarecrow
     * Initialize the sprite and speed of Scarecrow
     */
    public Scarecrow(int x, int y) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.setSpeed(0);
    }

    @Override
    public void tick(EngineState state) {
        super.tick(state);
    }

    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);
        EnemyManager enemies = game.getEnemies();
        final int scareRadius = state.getDimensions().tileSize() * 4;

        for (Enemy enemy : enemies.getBirds()) {
            if (enemy instanceof Magpie || enemy instanceof Pigeon) {
                if (this.distanceFrom(enemy) < scareRadius) {
                    enemy.setAttacking(false);
                }
            }
        }
    }
}
