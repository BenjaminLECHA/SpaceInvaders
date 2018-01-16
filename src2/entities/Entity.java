package entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import base.Sprite;
import base.SpriteStore;
import entities.Mouvement.Mouvement;

public abstract class Entity implements Mouvement {

    /** The current x location of this entity */
    protected double x;
    /** The current y location of this entity */
    protected double y;
    /** The sprite that represents this entity */
    protected Sprite sprite;
    /** The current speed of this entity horizontally (pixels/sec) */
    protected double dx;
    /** The current speed of this entity vertically (pixels/sec) */
    protected double dy;
    /** The rectangle used for this entity during collisions  resolution */
    protected Rectangle me = new Rectangle();
    /** The rectangle used for other entities during collision resolution */
    protected Rectangle him = new Rectangle();


    public Entity(String ref,int x,int y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
    }

    public void move(long delta) {
        double [] deplace;
        deplace = this.move(delta,dx,dy);
        x += deplace[0];
        y += deplace[1];
    }

    public void moveAlea(long delta) {
        double [] deplace;
        deplace = this.moveAlea(delta,dx,dy);
        x += deplace[0];
        y += deplace[1];
    }

    public abstract void setHorizontalMovement(double dx);

    public abstract void setVerticalMovement(double dy);

    public abstract double getHorizontalMovement();

    public abstract double getVerticalMovement();

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract void draw(Graphics g);

    public abstract void doLogic();

    public abstract boolean collidesWith(Entity other);

    public abstract void collidedWith(Entity other);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}