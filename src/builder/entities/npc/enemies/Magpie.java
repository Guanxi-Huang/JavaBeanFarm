package builder.entities.npc.enemies;

import builder.GameState;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;
import engine.timing.RepeatingTimer;

/**
 * The type Magpie.
 */
public class Magpie extends Enemy {

    private static final SpriteGroup art = SpriteGallery.magpie;
    public int coins = 0;

    private final RepeatingTimer directionalUpdateTimer = new RepeatingTimer(30);

    /**
     * Instantiates a new Magpie.
     *
     * @require the tracked target must exist
     * @param x             the x
     * @param y             the y
     * @param trackedTarget the tracked target
     */
    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        setTrackedTarget(trackedTarget);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(10000));
        setTargetDirection(trackedTarget.getX(), trackedTarget.getY());
    }

    @Override
    public void updateAttack(EngineState engine, GameState game) {
        this.directionalUpdateTimer.tick();

        Player player = game.getPlayer();
        if (isClosed(player, engine) && game.getInventory().getCoins() > 0 && isAttacking()) {
            game.getInventory().addCoins(-1);
            this.coins += 1;
            setAttacking(false);
            this.setSpeed(2); // book it
        }
        if (this.isMarkedForRemoval() && isAttacking()) {
            game.getInventory().addCoins(this.coins);
        }
    }


    @Override
    public SpriteGroup getSpriteGroup() {
        return art;
    }
}
