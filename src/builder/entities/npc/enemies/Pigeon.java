package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

public class Pigeon extends Enemy implements Expirable {

    private static final SpriteGroup art = SpriteGallery.pigeon;

    public Pigeon(int x, int y) {
        super(x, y);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(3000));
    }

    public Pigeon(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.setSpeed(1);
        setTrackedTarget(trackedTarget);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(3000));
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        if (!isAttacking()) {
            double deltaX = (getSpawnX() - this.getX());
            double deltaY = (getSpawnY() - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));

            if (this.distanceFrom(getSpawnX(), getSpawnY())
                    < engine.getDimensions().tileSize()) { // get close to spawn
                this.markForRemoval();
            }
            if (getSpawnY() < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }
        this.move();
        if (getTrackedTarget() == null
                && isAttacking()) { // if the pigeon has no target, it should go to the center of
                                      // the screen if its hunting
            double deltaX = ((double) engine.getDimensions().windowSize() / 2 - this.getX());
            double deltaY = ((double) engine.getDimensions().windowSize() / 2 - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            if (getTrackedTarget().getY() > this.getY()) {
                this.setSprite(art.getSprite("down"));
            } else {
                this.setSprite(art.getSprite("up"));
            }
        } else {
            // do nothing
        }
        if (getTrackedTarget() != null && isAttacking()) {
            double deltaX = (getTrackedTarget().getX() - this.getX());
            double deltaY = (getTrackedTarget().getY() - this.getY());
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
        } else {
            // do nothing
        }
        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            this.markForRemoval();
        } else {
            // do nothing
        }
        if (!isAttacking()) {
            if (this.distanceFrom(getSpawnX(), getSpawnY()) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
            if (getSpawnY() < this.getY()) {
                this.setSprite(art.getSprite("up"));
            } else {
                this.setSprite(art.getSprite("down"));
            }
        }

        List<Tile> tiles =
                game.getWorld()
                        .tileSelector(
                                tile -> {
                                    for (Entity entity : tile.getStackedEntities()) {
                                        if (entity instanceof Cabbage) {
                                            return true;
                                        } else {
                                            // do nothing
                                        }
                                    }
                                    return false;
                                });
        if (tiles.size() > 0) {
            int distance = this.distanceFrom(tiles.getFirst());
            Tile closest = tiles.getFirst();
            for (Tile tile : tiles) {
                if (this.distanceFrom(tile) < distance) {
                    closest = tile;
                } else {
                    // do nothing
                }
            }
            setTrackedTarget(closest);

            if (isAttacking()
                    && this.distanceFrom(getTrackedTarget()) < engine.getDimensions().tileSize()) {
                for (Entity entity : closest.getStackedEntities()) {
                    if (entity instanceof Cabbage cabbage) {
                        cabbage.markForRemoval();
                        setAttacking(false);
                    } else {
                        // do nothing
                    }
                }
            }
        } else { // no cabbages to get
            setAttacking(false);
        }
    }
}
