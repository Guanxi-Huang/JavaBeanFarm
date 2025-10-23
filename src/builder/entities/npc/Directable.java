package builder.entities.npc;

/**
 * The interface Directable.
 */
public interface Directable {

    /**
     * Gets direction.
     *
     * @return the direction
     */
    int getDirection();

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    void setDirection(int direction);

    /**
     * Move.
     */
    void move();
}
