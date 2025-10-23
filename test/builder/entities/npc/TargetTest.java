package builder.entities.npc;

import engine.game.HasPosition;

/**
 * Simple test class that implements HasPosition for testing purposes
 */
public class TargetTest implements HasPosition {
    private int x;
    private int y;

    public TargetTest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int i) {

    }

    @Override
    public void setY(int i) {

    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
