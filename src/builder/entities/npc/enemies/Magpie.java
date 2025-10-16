package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;
import engine.timing.RepeatingTimer;

public class Magpie extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.magpie;
    public int coins = 0;

    private RepeatingTimer directionalUpdateTimer = new RepeatingTimer(30);

    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        setTrackedTarget(trackedTarget);
        this.setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(10000));

        double deltaX = trackedTarget.getX() - this.getX();
        double deltaY = trackedTarget.getY() - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            this.markForRemoval();
        }
        if (isAttacking()) {
            double deltaX = getTrackedTarget().getX() - this.getX();
            double deltaY = getTrackedTarget().getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            /** target is below */
            if (getTrackedTarget().getY() > this.getY()) {
                this.setSprite(art.getSprite("down"));
            } else {
                this.setSprite(art.getSprite("up"));
            }
        } else {
            double deltaX = getSpawnX() - this.getX();
            double deltaY = getSpawnY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (getSpawnX() < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }
        this.move();
        this.directionalUpdateTimer.tick();

        Player player = game.getPlayer();

        final boolean hasHitPlayer =
                this.distanceFrom(player.getX(), player.getY()) < engine.getDimensions().tileSize();
        if (hasHitPlayer && game.getInventory().getCoins() > 0 && isAttacking()) {
            game.getInventory().addCoins(-1);
            this.coins += 1;
            setAttacking(false);
            this.setSpeed(2); // book it
        }

        if (!isAttacking()) {
            if (this.distanceFrom(getSpawnX(), getSpawnX()) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
        }

        if (this.isMarkedForRemoval() && isAttacking()) {
            game.getInventory().addCoins(this.coins);
        }
    }

    @Override
    public void interact(EngineState engine, GameState game) {}
}
