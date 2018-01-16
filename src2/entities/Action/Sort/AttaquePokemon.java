package entities.Action.Sort;

import base.Game;
import entities.Entity;
import entities.Mouvement.MouvementShot;
import entities.Ship;

import java.awt.*;

public class AttaquePokemon extends Entity {
    /**
     * The vertical speed at which the players shot moves
     */
    private double moveSpeed = 300;
    /**
     * The game in which this entity exists
     */
    protected Game game;
    /**
     * True if this shot has been "used", i.e. its hit something
     */
    protected boolean used = false;

    public AttaquePokemon(Game game, String ref, int x, int y) {
        super(ref, x, y);

        this.game = game;

        dy = moveSpeed;
    }

    @Override
    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }

    @Override
    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }

    @Override
    public double getHorizontalMovement() {
        return dx;
    }

    @Override
    public double getVerticalMovement() {
        return dy;
    }

    @Override
    public void draw(Graphics g) {
        sprite.draw(g,(int) x,(int) y);
    }

    @Override
    public void doLogic() {
        //
    }

    @Override
    public boolean collidesWith(Entity other) {
        me.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight());
        him.setBounds((int) other.getX(),(int) other.getY(),other.getSprite().getWidth(),other.getSprite().getHeight());

        return me.intersects(him);
    }


    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it!
        if (other instanceof Ship) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyDeath();
            used = true;
        }
    }

    @Override
    public double[] move(long delta, double dx, double dy) {
        if (y > 570) {
            game.removeEntity(this);
        }
        return new MouvementShot().move(delta,dx,dy);
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        if (y > 570) {
            game.removeEntity(this);
        }
        return new MouvementShot().moveAlea(delta, dx, dy);
    }
}
