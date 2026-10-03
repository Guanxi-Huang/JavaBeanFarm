package builder.entities.npc.enemies;

import builder.GameState;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * The type Magpie.
 */
public class Magpie extends Enemy {

    private static final SpriteGroup art = SpriteGallery.magpie;
    private int coins = 0;

    /**
     * Instantiates a new Magpie.
     *
     * @require x >= 0 && y >= 0;
     * @param x             the x
     * @param y             the y
     * @param trackedTarget the tracked target
     */
    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        setTrackedTarget(trackedTarget);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(10000));

        int initializeDirection = 0;
        if (trackedTarget != null) {
            setTargetDirection(trackedTarget.getX(), trackedTarget.getY());
        } else {
            initializeDirection = 90;
        }
        this.setDirection(initializeDirection);
    }

    /**
     * Gets coins.
     *
     * @return the coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Sets coins.
     *
     * @param coins the coins
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public void updateAttack(EngineState engine, GameState game) {
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