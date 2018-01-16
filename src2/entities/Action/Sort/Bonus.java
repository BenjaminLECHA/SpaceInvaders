package entities.Action.Sort;

import base.Game;
import entities.Entity;
import entities.Ship;

/**
 * Created by Minato_Namikazee on 28/12/2016.
 */
public class Bonus extends AttaquePokemon {

    public Bonus(Game game, String ref, int x, int y) {
        super(game, ref, x, y);
        setVerticalMovement(200);
    }

    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it!
        if (other instanceof Ship) {

            game.removeEntity(this);
            ((Ship) other).setPower();

            used = true;
        }
    }
}
