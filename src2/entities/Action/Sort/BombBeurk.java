package entities.Action.Sort;

import base.Game;

/**
 * Created by Minato_Namikazee on 29/12/2016.
 */
public class BombBeurk extends AttaquePokemon {
    public BombBeurk(Game game, String ref, int x, int y) {
        super(game, ref, x, y);
        setVerticalMovement(250);
    }
}
