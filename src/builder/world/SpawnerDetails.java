package builder.world;

import engine.game.HasPosition;

/**
 * The interface Spawner details.
 */
public interface SpawnerDetails extends HasPosition {
    int getX();

    int getY();

    void setX(int x);

    void setY(int y);

    /**
     * Gets duration.
     *
     * @return the duration
     */
    int getDuration();
}