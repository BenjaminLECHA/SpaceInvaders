package entities.Pokemon;

import base.Game;
import entities.Mouvement.MouvementPokemon;

/**
 * Created by Minato_Namikazee on 28/12/2016.
 */
public class Raflesia extends Pokemon {
    public Raflesia(Game game, String ref, int x, int y) {
        super(game, ref, x, y);
        setHorizontalMovement(25);
    }

    @Override
    public double[] move(long delta, double dx, double dy) {
        if ((dx < 0) && (x < 200)) {
            game.updateLogic();
        }
        if ((dx > 0) && (x > 600)) {
            game.updateLogic();
        }
        return new MouvementPokemon().move(delta,dx,dy);
    }

    public double[] moveAlea(long delta, double dx, double dy) {
        return new double[]{0,0};
    }

}
