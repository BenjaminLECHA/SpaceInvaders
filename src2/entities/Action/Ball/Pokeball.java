package entities.Action.Ball;

import base.Game;
import entities.Action.Sort.AttaquePokemon;
import entities.Action.Sort.BombBeurk;
import entities.Action.Sort.Tranchherbe;
import entities.Entity;
import entities.Mouvement.MouvementShot;
import entities.Pokemon.Mystherbe;
import entities.Pokemon.Ortide;
import entities.Pokemon.Raflesia;

import java.awt.*;

public class Pokeball extends Entity {

    /**
     * The vertical speed at which the players shot moves
     */
    private double moveSpeed = -300;
    /**
     * The game in which this entity exists
     */
    protected Game game;
    /**
     * True if this shot has been "used", i.e. its hit something
     */
    protected boolean used = false;

    protected static int nbEssai = 0;

    public Pokeball(Game game, String ref, int x, int y) {
        super(ref, x, y);

        this.game = game;

        dy = moveSpeed;
    }

    public static void setNbEssai() {
        Pokeball.nbEssai = 0;
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
        him.setBounds(other.getX(),other.getY(),other.getSprite().getWidth(),other.getSprite().getHeight());

        return me.intersects(him);
    }

    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it!
        if (other instanceof Mystherbe) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }

        // if we've hit an alien2
        if (other instanceof Ortide) {
            // remove the affected entities
            game.removeEntity(this);
            ((Ortide) other).healthPoint();

            used = true;
        }

        if (other instanceof Raflesia) {
            int loto = ((int) (Math.random() * 1000 ) - nbEssai);
            if(loto <= 0 && nbEssai > 24) {
                game.removeEntity(other);
                game.notifyAlienKilled();
            }
            game.removeEntity(this);
            used = true;
            nbEssai++;
        }

        if (other instanceof Tranchherbe) {
            game.removeEntity(this);
            game.removeEntity(other);
            used = true;
        }

        if (other instanceof BombBeurk) {
            game.removeEntity(this);
            used = true;
        }
    }

    @Override
    public double[] move(long delta, double dx, double dy) {
        if (y < -100) {
            game.removeEntity(this);
        }
        return new MouvementShot().move(delta,dx,dy);
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        if (y < -100) {
            game.removeEntity(this);
        }
        return new MouvementShot().moveAlea(delta, dx, dy);
    }

}