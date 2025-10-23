package builder.world;

import engine.game.ImmutablePosition;

/**
 * The interface Player details.
 */
public interface PlayerDetails extends ImmutablePosition {
    /**
     * Gets starting food.
     *
     * @return the starting food
     */
    int getStartingFood();

    /**
     * Gets starting coins.
     *
     * @return the starting coins
     */
    int getStartingCoins();
    
}
