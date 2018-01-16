package entities.Action.Sort;

import base.Game;
import entities.Entity;
import entities.Ship;

/**
 * Created by Minato_Namikazee on 02/01/2017.
 */
public class LanceSoleil extends AttaquePokemon {

    boolean shot;

    public LanceSoleil(Game game, String ref, int x, int y, boolean shot) {
        super(game, ref, x, y);
        this.shot = shot;
        setVerticalMovement(200);
    }

    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it!
        if (other instanceof Ship && shot == true) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyDeath();
            used = true;
        }
    }
}
