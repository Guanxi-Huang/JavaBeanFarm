package builder.entities.npc.enemies;

import builder.GameState;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;
import engine.timing.RepeatingTimer;

public class Magpie extends Enemy {

    private static final SpriteGroup art = SpriteGallery.magpie;
    public int coins = 0;

    private final RepeatingTimer directionalUpdateTimer = new RepeatingTimer(30);

    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        setTrackedTarget(trackedTarget);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(10000));
        setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
    }

    @Override
    public void updateAttack (EngineState engine, GameState game){
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


    @Override
    public void interact(EngineState engine, GameState game) {}
}
