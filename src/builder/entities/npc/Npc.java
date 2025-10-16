package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

public class Npc extends Entity implements Interactable, Tickable, Directable {

    private int direction = 0;
    private double speed = 1;

    public Npc(int x, int y) {
        super(x, y);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    /** Adjust the X and Y of {@link Npc} */
    public void move() {
        final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
        final int deltaY = (int) Math.round(Math.sin(Math.toRadians(this.direction)) * this.speed);
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    @Override
    public void tick(EngineState state) {
        this.move();
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.move();
    }

    @Override
    public void interact(EngineState state, GameState game) {}

    /**
     * Return how far away this npc is from the given position
     *
     * @param position the position we are measuring to from this npcs position!
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(HasPosition position) {
        return distanceFrom(position.getX(), position.getY());
    }

    /**
     * Return how far away this npc is from the given position
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(int x, int y) {
        int deltaX = x - this.getX();
        int deltaY = y - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
