package entities.Action.Ball;

import base.Game;
import entities.Action.Sort.AttaquePokemon;
import entities.Action.Sort.BombBeurk;
import entities.Action.Sort.Tranchherbe;
import entities.Entity;
import entities.Pokemon.Mystherbe;
import entities.Pokemon.Ortide;
import entities.Pokemon.Raflesia;

/**
 * Created by Minato_Namikazee on 28/12/2016.
 */
public class Superball extends Pokeball {
    public Superball(Game game, String ref, int x, int y) {
        super(game, ref, x, y);
    }

    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit a Mystherbe, kill it!
        if (other instanceof Mystherbe) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }

        // if we've hit an Ortide
        if (other instanceof Ortide) {
            // remove the affected entities
            game.removeEntity(this);
            ((Ortide) other).healthPoint();

            used = true;
        }

        if (other instanceof Raflesia) {
            int loto = ((int) (Math.random() * 1000 ) - (nbEssai * 2));
            if(loto <= 0 && nbEssai > 24) {
                game.removeEntity(other);
                game.notifyAlienKilled();
            }
            game.removeEntity(this);
            used = true;
            nbEssai++;
        }

        if (other instanceof Tranchherbe) {
            game.removeEntity(other);
        }

        if (other instanceof BombBeurk) {
            game.removeEntity(this);
            game.removeEntity(other);
        }
    }
}
