package entities.Pokemon;

import base.Game;
import entities.Entity;
import entities.Mouvement.MouvementPokemon;

import java.awt.*;

public class Pokemon extends Entity {

    private double moveSpeed = 75;
    public Game game;

    public Pokemon(Game game, String ref, int x, int y) {
        super(ref,x,y);
        this.game = game;
        dx = -moveSpeed;
    }

    @Override
    public double[] move(long delta, double dx, double dy) {
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }
        return new MouvementPokemon().move(delta,dx,dy);
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }
        return new MouvementPokemon().moveAlea(delta, dx, dy);
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
    public void doLogic() {
        dx = -dx;
        y += 10;

        if (y > 570) {
            game.notifyDeath();
        }
    }

    @Override
    public void draw(Graphics g) {
        sprite.draw(g,(int) x,(int) y);
    }

    @Override
    public boolean collidesWith(Entity other) {
        me.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight());
        him.setBounds(other.getX(),other.getY(),other.getSprite().getWidth(),other.getSprite().getHeight());

        return me.intersects(him);
    }

    @Override
    public void collidedWith(Entity other) {

    }
}
