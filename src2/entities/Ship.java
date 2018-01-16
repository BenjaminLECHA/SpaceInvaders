package entities;

import base.Game;
import entities.Mouvement.MouvementShip;
import entities.Pokemon.Mystherbe;
import entities.Pokemon.Pokemon;

import java.awt.*;


public class Ship extends Entity {

    private Game game;

    private int power;

    public Ship(Game game, String ref, int x, int y) {
        super(ref,x,y);
        this.game = game;
        power = 1;
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

    public int getPower() {
        return power;
    }

    public void setPower() {
        power++;
    }

    public void setPowerNull() {
        power = 1;
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
        him.setBounds((int) other.x,(int) other.y,other.sprite.getWidth(),other.sprite.getHeight());

        return me.intersects(him);
    }

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof Pokemon) {
            game.notifyDeath();
        }
    }

    @Override
    public double[] move(long delta, double dx, double dy) {
        if ((dx < 0) && (x < 10)) {
            return new double[]{0,0};
        }
        if ((dx > 0) && (x > 750)) {
            return new double[]{0,0};
        }
        return new MouvementShip().move(delta,dx,dy);
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        if ((dx < 0) && (x < 10)) {
            return new double[]{0,0};
        }
        if ((dx > 0) && (x > 750)) {
            return new double[]{0,0};
        }
        return new MouvementShip().moveAlea(delta, dx, dy);
    }
}