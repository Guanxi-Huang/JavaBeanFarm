package builder.entities.npc.enemies;

import builder.GameState;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * The type Eagle.
 */
public class Eagle extends Enemy {

    private static final SpriteGroup art = SpriteGallery.eagle;
    private int food = 0;

    /**
     * Instantiates a new Eagle.
     *
     * @param x             the x
     * @param y             the y
     * @param trackedTarget the tracked target
     */
    public Eagle(int x, int y, HasPosition trackedTarget) {
        super(x, y);

        // derive direction based on where the eagle is and the initial target is
        this.setSpeed(2);
        setTrackedTarget(trackedTarget);
        setLifespan(new FixedTimer(5000));
        int initializeDirection = 20;
        if (trackedTarget != null) {
            setTargetDirection(trackedTarget.getX(), trackedTarget.getY());
        } else {
            initializeDirection = 90;
        }
        this.setDirection(initializeDirection);
    }

    @Override
    public SpriteGroup getSpriteGroup() {
        return art;
    }

    @Override
    public void updateAttack(EngineState engine, GameState game) {
        Player player = game.getPlayer();
        if (player != null && isClosed(player, engine) && isAttacking()) {
            setAttacking(false);
            if (game.getInventory().getFood() > 0) {
                if (this.food == 0) {
                    game.getInventory().addFood(-3);
                    this.food = 3;
                }
                this.setSpeed(4); // the eagle BOOKS it once it has the food
            }
        }

        if (this.isMarkedForRemoval() && getTrackedTarget() != null
                && isClosed(getTrackedTarget(), engine)) {
            game.getInventory().addFood(this.food);
        }
    }
}
