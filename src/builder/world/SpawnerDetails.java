package builder.world;

import engine.game.HasPosition;

/**
 * The interface Spawner details.
 */
public interface SpawnerDetails extends HasPosition {

    /**
     * Gets spawner position x.
     *
     * @return position x.
     */
    int getX();

    /**
     * Gets spawner position y.
     *
     * @return position y.
     */
    int getY();

    /**
     * Sets spawner position x.
     */
    void setX(int x);

    /**
     * Sets spawner position y.
     */
    void setY(int y);

    /**
     * Gets duration.
     *
     * @return the duration
     */
    int getDuration();
}