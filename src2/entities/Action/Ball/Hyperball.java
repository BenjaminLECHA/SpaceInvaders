package entities.Action.Ball;

import base.Game;
import entities.Action.Sort.AttaquePokemon;
import entities.Action.Sort.BombBeurk;
import entities.Action.Sort.Tranchherbe;
import entities.Entity;
import entities.Pokemon.Pokemon;
import entities.Pokemon.Raflesia;

/**
 * Created by Minato_Namikazee on 28/12/2016.
 */
public class Hyperball extends Pokeball {
    public Hyperball(Game game, String ref, int x, int y) {
        super(game, ref, x, y);
    }

    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit a Mystherbe, kill it!
        if (other instanceof Pokemon) {
            if (other instanceof Raflesia) {
                int loto = ((int) (Math.random() * 1000 ) - (nbEssai * 3));
                if(loto <= 0 && nbEssai > 24) {
                    game.removeEntity(other);
                    game.notifyAlienKilled();
                }
                game.removeEntity(this);
                used = true;
                nbEssai++;
            }
            else {
                // remove the affected entities
                game.removeEntity(this);
                game.removeEntity(other);

                // notify the game that the alien has been killed
                game.notifyAlienKilled();
                used = true;
            }
        }

        if (other instanceof Tranchherbe) {
            game.removeEntity(other);
        }

        if (other instanceof BombBeurk) {
            game.removeEntity(other);
        }
    }
}
